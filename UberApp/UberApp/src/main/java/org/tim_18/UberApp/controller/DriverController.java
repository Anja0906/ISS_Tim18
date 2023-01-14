package org.tim_18.UberApp.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.Validation.ErrorMessage;
import org.tim_18.UberApp.dto.*;

import org.tim_18.UberApp.dto.driverDTOs.DriverDTO;
import org.tim_18.UberApp.dto.driverDTOs.DriverDTOWithoutId;
import org.tim_18.UberApp.dto.locationDTOs.LocationDTO;
import org.tim_18.UberApp.dto.rideDTOs.RideRetDTO;
import org.tim_18.UberApp.exception.*;
import org.tim_18.UberApp.model.Document;
import org.tim_18.UberApp.model.Driver;
import org.tim_18.UberApp.model.Vehicle;
import org.tim_18.UberApp.service.DocumentService;
import org.tim_18.UberApp.service.DriverService;
import org.tim_18.UberApp.service.VehicleService;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.service.*;

import java.time.LocalDateTime;
import java.util.*;


@RestController
@RequestMapping("api/driver")
@CrossOrigin(origins = "http://localhost:4200")
public class DriverController {
    private final DriverService driverService;
    private final DocumentService documentService;
    private final VehicleService vehicleService;
    private final LocationService locationService;
    private final WorkTimeService workTimeService;
    private final RideService rideService;
    private final UserService userService;

    public DriverController(DriverService driverService,DocumentService documentService,VehicleService vehicleService,LocationService locationService, WorkTimeService workTimeService,RideService rideService,UserService userService) {
        this.driverService   = driverService;
        this.documentService = documentService;
        this.vehicleService  = vehicleService;
        this.locationService = locationService;
        this.workTimeService = workTimeService;
        this.rideService     = rideService;
        this.userService     = userService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDrivers (
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Driver> drivers = driverService.findAll(pageable);
        Map<String, Object> map = new HashMap<>();
        HashSet<DriverDTO> driversDTO = new DriverDTO().makeDriversDTO(drivers);

        map.put("totalCount",driversDTO.size());
        map.put("results",driversDTO);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getDriverById (@PathVariable("id") int id) {
        try{
            Driver driver = driverService.findDriverById(id);
            DriverDTO driverDTO = new DriverDTO(driver);
            return new ResponseEntity<>(driverDTO, HttpStatus.OK);
        }catch(DriverNotFoundException driverNotFoundException) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    public ResponseEntity<?> addDriver(@RequestBody DriverDTOWithoutId driverDTOWithoutId) {
            User user = userService.findUserByEmail(driverDTOWithoutId.getEmail());
            if(user.equals(null)){
                Driver driver = new Driver(driverDTOWithoutId.getName(), driverDTOWithoutId.getSurname(),
                        driverDTOWithoutId.getProfilePicture(), driverDTOWithoutId.getTelephoneNumber(),
                        driverDTOWithoutId.getEmail(), driverDTOWithoutId.getAddress(),
                        driverDTOWithoutId.getPassword(),false,false) ;
                driverService.addDriver(driver);
                DriverDTO driverDTO = new DriverDTO(driver);
                return new ResponseEntity<>(driverDTO, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new ErrorMessage("User with that email already exists!"),HttpStatus.BAD_REQUEST);
            }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDriver(
            @PathVariable("id") int id,
            @Valid @RequestBody DriverDTOWithoutId driverDTOWithoutId) {
        try {
            Driver driver = driverService.findDriverById(id);
            driver.driverUpdate(driverDTOWithoutId);
            Driver updateDriver = driverService.updateDriver(driver);
            DriverDTO driverDTO = new DriverDTO(updateDriver);
            return new ResponseEntity<>(driverDTO, HttpStatus.OK);
        }catch (ConstraintViolationException constraintViolationException){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (DriverNotFoundException driverNotFoundException) {
            return new ResponseEntity<>(new ErrorMessage("Driver does not exist!"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/documents")
    public ResponseEntity<?> getDocumentById (
            @PathVariable("id") int id) {
        HashSet<Document> documents = documentService.findByDriverId(id);
        if(documents.isEmpty()) {
            return new ResponseEntity<>(new ErrorMessage("Driver does not exist!"), HttpStatus.NOT_FOUND);
        }else{
            HashSet<DocumentDTO> documentDTOS = new DocumentDTO().makeDocumentsDTO(documents);
            return new ResponseEntity<>(documentDTOS, HttpStatus.OK);
        }
    }
    @PostMapping("/{id}/documents")
    public ResponseEntity<?> addDocument(
            @PathVariable("id") int id,
            @RequestBody DocumentDTO documentDTO) {
        try{
        Driver driver = driverService.findDriverById(id);
        Document document = new Document().makeDocumentFromDTO(documentDTO,driver);
        document = documentService.addDocument(document);
            return new ResponseEntity<>(new DocumentDTO(document), HttpStatus.CREATED);
        }catch (DriverNotFoundException driverNotFoundException){
            return new ResponseEntity<>(new ErrorMessage("Driver does not exist!"), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/document/{id}")
    public ResponseEntity deleteDocumentById (
            @PathVariable("id") int id) {
        try {
            Document document = documentService.findDocumentById(id);
            documentService.deleteDocument(document);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (DocumentNotFoundException documentNotFoundException) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/vehicle")
    public ResponseEntity<?> getVehicleById (
            @PathVariable("id") int id) {
        try {
            Driver driver = driverService.findDriverById(id);
            Vehicle vehicle = vehicleService.findVehicleByDriverId(driver.getId());
            LocationDTO locationDTO = new LocationDTO(vehicle.getCurrentLocation());
            return new ResponseEntity<>(new VehicleDTO(vehicle,locationDTO), HttpStatus.OK);
        }catch (DriverNotFoundException driverNotFoundException){
            return new ResponseEntity<>(new ErrorMessage("Driver does not exist!"), HttpStatus.NOT_FOUND);
        }catch (VehicleNotFoundException vehicleNotFoundException){
            return new ResponseEntity<>(new ErrorMessage("Vehicle does not exist!"), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/vehicle")
    public ResponseEntity<VehicleDTO> addVehicle(
            @PathVariable("id") int id,
            @RequestBody VehicleDTOWithoutIds vehicleDTOWithoutIds) {
        try{
            Driver driver = driverService.findDriverById(id);
            Location location = locationService.findLocationByAdressLongitudeLatitude(vehicleDTOWithoutIds.getCurrentLocation().getLongitude(),
                                                                                      vehicleDTOWithoutIds.getCurrentLocation().getLatitude(),
                                                                                      vehicleDTOWithoutIds.getCurrentLocation().getAddress());
            Vehicle vehicle = new Vehicle(driver,vehicleDTOWithoutIds.getVehicleType(),
                                          vehicleDTOWithoutIds.getModel(),vehicleDTOWithoutIds.getLicenseNumber(),
                                          location,vehicleDTOWithoutIds.getPassengerSeats(),
                                          vehicleDTOWithoutIds.getBabyTransport(),vehicleDTOWithoutIds.getPetTransport());
            vehicle = vehicleService.addVehicle(vehicle);
            return new ResponseEntity<>(new VehicleDTO(vehicle,vehicleDTOWithoutIds.getCurrentLocation()), HttpStatus.CREATED);
        }catch (DriverNotFoundException driverNotFoundException){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/{id}/vehicle")
    public ResponseEntity<VehicleDTO> changeDriversVehicle(
            @PathVariable("id") int id,
            @RequestBody VehicleDTOWithoutIds vehicleDTOWithoutIds) {
        try{
            Driver driver = driverService.findDriverById(id);
            Location location = locationService.findLocationByAdressLongitudeLatitude(vehicleDTOWithoutIds.getCurrentLocation().getLongitude(),
                    vehicleDTOWithoutIds.getCurrentLocation().getLatitude(),
                    vehicleDTOWithoutIds.getCurrentLocation().getAddress());
            Vehicle vehicle = vehicleService.findVehicleByDriverId(id);
            vehicle.updateVehicle(vehicleDTOWithoutIds,location);
            vehicle = vehicleService.addVehicle(vehicle);
            vehicle = vehicleService.updateVehicle(vehicle);
            return new ResponseEntity<>(new VehicleDTO(vehicle,vehicleDTOWithoutIds.getCurrentLocation()), HttpStatus.CREATED);
        }catch (DriverNotFoundException driverNotFoundException){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }catch (VehicleNotFoundException vehicleNotFoundException){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/working-hour")
    public ResponseEntity<Map<String, Object>> getWorkingHours (
            @PathVariable("id") int id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size,
            @RequestParam(defaultValue = "start") String sort,
            @RequestParam(defaultValue = "2022-12-07T07:00:50") String from,
            @RequestParam(defaultValue = "2022-12-08T10:40:00") String to) {
        Map<String, Object> map = new HashMap<>();
        Pageable pageable = PageRequest.of(page, size,Sort.by(sort));

        Page<WorkTime> workTimes = workTimeService.findWorkTimesFromToDate(id,from,to,pageable);
        HashSet<WorkTimeDTOWithoutDriver> workTimeDTOS = new WorkTimeDTOWithoutDriver().makeWorkTimeDTOWithoutDriver(workTimes);

        map.put("totalCount",workTimeDTOS.size());
        map.put("results",workTimeDTOS);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/{id}/working-hour")
    public ResponseEntity<?> addWorkingHourForDriver(
            @PathVariable("id") int id,
            @RequestBody WorkTimeDTOWithoutDriver workTimeDTOWithoutDriver) {
        try{
            Driver driver = driverService.findDriverById(id);
            WorkTime workTime = new WorkTime(workTimeDTOWithoutDriver.getStart(),workTimeDTOWithoutDriver.getEnd(), driver);
            workTime = workTimeService.addWorkTime(workTime);
            return new ResponseEntity<>(new WorkTimeDTOWithoutDriver(workTime), HttpStatus.CREATED);
        }catch (DriverNotFoundException driverNotFoundException){
            return new ResponseEntity<>(new ErrorMessage("Driver does not exist!"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/working-hour/{working-hour-id}")
    public ResponseEntity<?> getWorkingHourById (
            @PathVariable("working-hour-id") int id) {
        try {
            WorkTime workTime = workTimeService.findWorkTimeById(id);
            return new ResponseEntity<>(new WorkTimeDTOWithoutDriver(workTime),HttpStatus.OK);
        }catch(WorkTimeNotFoundException workTimeNotFoundException){
            return new ResponseEntity<>(new ErrorMessage("Working hour does not exist!"),HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/working-hour/{working-hour-id}")
    public ResponseEntity<?> updateWorkingHourById (
            @PathVariable("working-hour-id") int id,
            @RequestBody WorkTimeDTOWithoutDriver workTimeDTOWithoutDriver) {
        try {
            WorkTime workTime = workTimeService.findWorkTimeById(id);
            workTime.updateWorkTime(workTimeDTOWithoutDriver);
            workTime = workTimeService.updateWorkTime(workTime);
            return new ResponseEntity<>(new WorkTimeDTOWithoutDriver(workTime),HttpStatus.OK);
        }catch(WorkTimeNotFoundException workTimeNotFoundException){
            return new ResponseEntity<>(new ErrorMessage("Working hour does not exist!"),HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/ride")
    public ResponseEntity<Map<String, Object>> getRides (
            @PathVariable("id") int id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size,
            @RequestParam(defaultValue = "start_time") String sort,
            @RequestParam(defaultValue = "2021-12-07T07:00:50") String from,
            @RequestParam(defaultValue = "2023-12-08T10:40:00") String to) {

        Pageable pageable = PageRequest.of(page, size,Sort.by(sort));
        Page<Ride> rides = rideService.findRidesForDriver(id,from,to,pageable);

        Map<String, Object> map = new HashMap<>();
        HashSet<RideRetDTO> ridesDTO = new RideRetDTO().makeRideRideDTOS(rides);

        map.put("totalCount",ridesDTO.size());
        map.put("results",ridesDTO);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
