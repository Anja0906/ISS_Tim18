package org.tim_18.UberApp.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.Validation.ErrorMessage;
import org.tim_18.UberApp.dto.*;
import org.tim_18.UberApp.dto.driverDTOs.DriverDTO;
import org.tim_18.UberApp.dto.driverDTOs.DriverDTOWithoutId;
import org.tim_18.UberApp.dto.locationDTOs.LocationDTO;
import org.tim_18.UberApp.dto.rideDTOs.RideRetDTO;
import org.tim_18.UberApp.exception.*;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.service.*;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;


@RestController
@RequestMapping("api/driver")
@CrossOrigin(origins = "http://localhost:4200")
public class DriverController {
    @Autowired
    private final DriverService driverService;
    @Autowired
    private final DocumentService documentService;
    @Autowired
    private final VehicleService vehicleService;
    @Autowired
    private final LocationService locationService;
    @Autowired
    private final WorkTimeService workTimeService;
    @Autowired
    private final RideService rideService;
    @Autowired
    private final UserService userService;
    @Autowired
    private final RoleService roleService;

    private final LocationsForRideService locationsForRideService;

    @Autowired
    PasswordEncoder passwordEncoder;


    public DriverController(DriverService driverService, DocumentService documentService, VehicleService vehicleService, LocationService locationService, WorkTimeService workTimeService, RideService rideService, UserService userService, RoleService roleService, LocationsForRideService locationsForRideService) {
        this.driverService   = driverService;
        this.documentService = documentService;
        this.vehicleService  = vehicleService;
        this.locationService = locationService;
        this.workTimeService = workTimeService;
        this.rideService     = rideService;
        this.userService     = userService;
        this.roleService     = roleService;
        this.locationsForRideService = locationsForRideService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDrivers (
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "100") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Driver> drivers = driverService.findAll(pageable);
        Map<String, Object> map = new HashMap<>();
        HashSet<DriverDTO> driversDTO = new DriverDTO().makeDriversDTO(drivers);

        map.put("totalCount",driversDTO.size());
        map.put("results",driversDTO);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN', 'PASSENGER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getDriverById (
            Principal principal,
            @PathVariable("id") int id) {
        try{
            checkAuthorities(principal, id);
            Driver driver = driverService.findDriverById(id);
            DriverDTO driverDTO = new DriverDTO(driver);
            return new ResponseEntity<>(driverDTO, HttpStatus.OK);
        }catch(DriverNotFoundException driverNotFoundException) {
            return new ResponseEntity<>("Driver does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<?> addDriver(@RequestBody DriverDTOWithoutId driverDTOWithoutId) {
        try{
            User user = userService.findUserByEmail(driverDTOWithoutId.getEmail());
            return new ResponseEntity<>(new ErrorMessage("User with that email already exists!"),HttpStatus.BAD_REQUEST);
        }catch (UserNotFoundException e){
            Driver driver = new Driver(driverDTOWithoutId.getName() , driverDTOWithoutId.getSurname(),
                    driverDTOWithoutId.getProfilePicture()          , driverDTOWithoutId.getTelephoneNumber(),
                    driverDTOWithoutId.getEmail()                   , driverDTOWithoutId.getAddress(),
                    passwordEncoder.encode(driverDTOWithoutId.getPassword()),
                    false                                    ,false,
                    false                                    ,this.getRoles()
            );
            driverService.save(driver);
            return new ResponseEntity<>(new DriverDTO(driver), HttpStatus.OK);
        }
    }

    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDriver(
            Principal principal,
            @PathVariable("id") int id,
            @Valid @RequestBody DriverDTOWithoutId driverDTOWithoutId) {
        try {
            checkAuthorities(principal, id);
            Driver driver = driverService.findDriverById(id);
            driver.driverUpdate(driverDTOWithoutId);
            Driver updateDriver = driverService.updateDriver(driver);
            return new ResponseEntity<>(new DriverDTO(updateDriver), HttpStatus.OK);
        }catch (ConstraintViolationException constraintViolationException){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (DriverNotFoundException driverNotFoundException) {
            return new ResponseEntity<>("Driver does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @GetMapping("/{id}/documents")
    public ResponseEntity<?> getDocumentById (
            Principal principal,
            @PathVariable("id") int id) {
        try {
            checkAuthorities(principal, id);
            HashSet<Document> documents = documentService.findByDriverId(id);
            return new ResponseEntity<>(new DocumentDTO().makeDocumentsDTO(documents), HttpStatus.OK);
        } catch (DriverNotFoundException e) {
            return new ResponseEntity<>("Driver does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @PostMapping("/{id}/documents")
    public ResponseEntity<?> addDocument(
            Principal principal,
            @PathVariable("id") int id,
            @RequestBody DocumentDTO documentDTO) {
        try{
            checkAuthorities(principal, id);
            Driver driver = driverService.findDriverById(id);
            Document document = new Document().makeDocumentFromDTO(documentDTO,driver);
            document = documentService.addDocument(document);
            return new ResponseEntity<>(new DocumentDTO(document), HttpStatus.CREATED);
        }catch (DriverNotFoundException driverNotFoundException){
            return new ResponseEntity<>("Driver does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @DeleteMapping("/document/{id}")
    public ResponseEntity deleteDocumentById (
            Principal principal,
            @PathVariable("id") int id) {
        try {
            Document document = documentService.findDocumentById(id);
            checkAuthorities(principal, document.getDriver().getId());
            Driver driver = driverService.findDriverById(document.getDriver().getId());
            documentService.deleteById(id);
            return new ResponseEntity<>("Driver document deleted successfully" ,HttpStatus.NO_CONTENT);
        } catch (DocumentNotFoundException documentNotFoundException) {
            return new ResponseEntity<>("Document does not exist!", HttpStatus.NOT_FOUND);
        } catch (DriverNotFoundException driverNotFoundException) {
            return new ResponseEntity<>("Cannot delete someone else's documents!", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @GetMapping("/{id}/vehicle")
    public ResponseEntity<?> getVehicleById (
            Principal principal,
            @PathVariable("id") int id) {
        try {
            checkAuthorities(principal, id);
            Driver driver = driverService.findDriverById(id);
            Vehicle vehicle = vehicleService.findVehicleByDriverId(driver.getId());
            if (vehicle == null)
                throw new VehicleNotFoundException("Vehicle is not assigned!");
            LocationDTO locationDTO = new LocationDTO(vehicle.getCurrentLocation());
            return new ResponseEntity<>(new VehicleDTO(vehicle,locationDTO), HttpStatus.OK);
        }catch (DriverNotFoundException driverNotFoundException){
            return new ResponseEntity<>("Driver does not exist!", HttpStatus.NOT_FOUND);
        }catch (VehicleNotFoundException vehicleNotFoundException){
            return new ResponseEntity<>("Vehicle is not assigned!", HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @PostMapping("/{id}/vehicle")
    public ResponseEntity<?> addVehicle(
            Principal principal,
            @PathVariable("id") int id,
            @RequestBody VehicleDTOWithoutIds vehicleDTOWithoutIds) {
        try{
            checkAuthorities(principal, id);
            Driver driver = driverService.findDriverById(id);
            if (!(driver.getVehicle() == null))
                return new ResponseEntity<>(new ErrorMessage("Vehicle already assigned"), HttpStatus.NOT_FOUND);

            Location location = locationService.findLocationByAddressLongitudeLatitude(
                    vehicleDTOWithoutIds.getCurrentLocation().getLongitude(),
                    vehicleDTOWithoutIds.getCurrentLocation().getLatitude(),
                    vehicleDTOWithoutIds.getCurrentLocation().getAddress()
            );
            if(location == null)
                location = locationService.addLocation(new Location(vehicleDTOWithoutIds.getCurrentLocation().getLongitude(),
                        vehicleDTOWithoutIds.getCurrentLocation().getLatitude(),
                        vehicleDTOWithoutIds.getCurrentLocation().getAddress()));

            Vehicle vehicle = new Vehicle(
                    driver,vehicleDTOWithoutIds.getVehicleType(), vehicleDTOWithoutIds.getModel(),vehicleDTOWithoutIds.getLicenseNumber(),
                    location                                    ,vehicleDTOWithoutIds.getPassengerSeats(),
                    vehicleDTOWithoutIds.getBabyTransport()     ,vehicleDTOWithoutIds.getPetTransport()
            );
            vehicle = vehicleService.addVehicle(vehicle);
            driver.setVehicle(vehicle);
            driverService.updateDriver(driver);
            return new ResponseEntity<>(new VehicleDTO(vehicle,vehicleDTOWithoutIds.getCurrentLocation()), HttpStatus.OK);
        }catch (DriverNotFoundException driverNotFoundException){
            return new ResponseEntity<>("Driver does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @PutMapping("/{id}/vehicle")
    public ResponseEntity<?> changeDriversVehicle(
            Principal principal,
            @PathVariable("id") int id,
            @RequestBody VehicleDTOWithoutIds vehicleDTOWithoutIds) {
        try{
            Driver driver = driverService.findDriverById(id);
            System.out.println(principal.getName() + driver.getEmail());
            checkAuthorities(principal, id);
            Location location = locationService.findLocationByAddressLongitudeLatitude(
                    vehicleDTOWithoutIds.getCurrentLocation().getLongitude(),
                    vehicleDTOWithoutIds.getCurrentLocation().getLatitude(),
                    vehicleDTOWithoutIds.getCurrentLocation().getAddress()
            );
            Vehicle vehicle = vehicleService.findVehicleByDriverId(id);
            vehicle.updateVehicle(vehicleDTOWithoutIds,location);
            vehicle = vehicleService.updateVehicle(vehicle);
            return new ResponseEntity<>(new VehicleDTO(vehicle,vehicleDTOWithoutIds.getCurrentLocation()), HttpStatus.CREATED);
        }catch (DriverNotFoundException driverNotFoundException){
            return new ResponseEntity<>("Driver does not exist!", HttpStatus.NOT_FOUND);
        }catch (VehicleNotFoundException vehicleNotFoundException){
            return new ResponseEntity<>("Vehicle does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @GetMapping("/{id}/working-hour")
    public ResponseEntity<?> getWorkingHours (
            Principal principal,
            @PathVariable("id") int id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size,
            @RequestParam(defaultValue = "start") String sort,
            @RequestParam(defaultValue = "2021-12-07T07:00:50") String from,
            @RequestParam(defaultValue = "2024-12-08T10:40:00") String to) {
        try {
            checkAuthorities(principal, id);
            Map<String, Object> map = new HashMap<>();
            Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
            Page<WorkTime> workTimes = workTimeService.findWorkTimesFromToDate(id, from, to, pageable);
            HashSet<WorkTimeDTOWithoutDriver> workTimeDTOS = new WorkTimeDTOWithoutDriver().makeWorkTimeDTOWithoutDriver(workTimes);
            map.put("totalCount", workTimeDTOS.size());
            map.put("results", workTimeDTOS);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }catch (DriverNotFoundException driverNotFoundException) {
            return new ResponseEntity<>("Driver does not exist!", HttpStatus.NOT_FOUND);
        }
    }
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @PostMapping("/{id}/working-hour")
    public ResponseEntity<?> addWorkingHourForDriver(
            Principal principal,
            @PathVariable("id") int id) {
        try{
            checkAuthorities(principal, id);
            Driver driver = driverService.findDriverById(id);
            Date date = new Date();
            WorkTime workTime = new WorkTime(date, date, driver,date,0);
            workTime = workTimeService.addWorkTime(workTime);
            return new ResponseEntity<>(new WorkTimeDTOWithoutDriver(workTime), HttpStatus.OK);
        }catch (DriverNotFoundException driverNotFoundException){
            return new ResponseEntity<>("Driver does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('DRIVER')")
    @GetMapping("/working-hour/{working-hour-id}")
    public ResponseEntity<?> getWorkingHourById (
            Principal principal,
            @PathVariable("working-hour-id") int id) {
        try {
            User user = userService.findUserByEmail(principal.getName());
            WorkTime workTime = workTimeService.findWorkTimeById(id);
            if (user.getId().equals(workTime.getDriver().getId())) {
                return new ResponseEntity<>(new WorkTimeDTOWithoutDriver(workTime),HttpStatus.OK);
            }
            return new ResponseEntity<>("Working hour does not exist!",HttpStatus.NOT_FOUND);
        }catch(WorkTimeNotFoundException workTimeNotFoundException){
            return new ResponseEntity<>("Working hour does not exist!",HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('DRIVER')")
    @PutMapping("/working-hour/{working-hour-id}")
    public ResponseEntity<?> updateWorkingHourById (
            Principal principal,
            @PathVariable("working-hour-id") int id,
            int flag) {
        try {
            User user = userService.findUserByEmail(principal.getName());
            WorkTime workTime = workTimeService.findWorkTimeById(id);
            Date date = new Date();
            if (user.getId().equals(workTime.getDriver().getId())) {
                if(flag == 1){
                    workTime.updateWorkingHour(date);
                    workTime = workTimeService.updateWorkTime(workTime);
                    return new ResponseEntity<>(new WorkTimeDTOWithoutDriver(workTime),HttpStatus.OK);
                }else{
                    workTime.updateWorkingHourLogin(date);
                    workTime = workTimeService.updateWorkTime(workTime);
                    return new ResponseEntity<>(new WorkTimeDTOWithoutDriver(workTime),HttpStatus.OK);
                }
            }
            return new ResponseEntity<>("Working hour does not exist!",HttpStatus.NOT_FOUND);
        }catch(WorkTimeNotFoundException workTimeNotFoundException){
            return new ResponseEntity<>("Working hour does not exist!",HttpStatus.NOT_FOUND);
        }
    }


    @PreAuthorize("hasRole('DRIVER')")
    @GetMapping("/working-hour/{driverId}/logged")
    public ResponseEntity<?> checkDriver (
            Principal principal,
            @PathVariable("driverId") int id) {
        try {
            User user = userService.findUserByEmail(principal.getName());
            Date startTime = new Date();
            Calendar start = Calendar.getInstance();
            start.setTime(startTime);
            start.add(Calendar.HOUR_OF_DAY, -24);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.");
            ArrayList<WorkTime> workTime = workTimeService.findWorkTimesFromToDateHash(id,dateFormat.format(start.getTime()),dateFormat.format(startTime));
            System.out.println(workTime.size());
            if(workTime.size() == 1)
                if (user.getId().equals(workTime.get(0).getDriver().getId()))
                    if(workTime.get(0).getWorkedTimeInMinutes()+(int)(startTime.getTime()/60000-workTime.get(0).getFlagStart().getTime()/60000)>=480)
                        return new ResponseEntity<>(-1,HttpStatus.OK);
            return new ResponseEntity<>(0,HttpStatus.OK);
        }catch(WorkTimeNotFoundException workTimeNotFoundException){
            return new ResponseEntity<>("Working hour does not exist!",HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('DRIVER')")
    @GetMapping("/working-hour/{driverId}/login")
    public ResponseEntity<?> workingHourValidation (
            Principal principal,
            @PathVariable("driverId") int id) {
        try {
            User user = userService.findUserByEmail(principal.getName());
            Date startTime = new Date();
            Calendar start = Calendar.getInstance();
            start.setTime(startTime);
            start.add(Calendar.HOUR_OF_DAY, -24);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ArrayList<WorkTime> workTime = workTimeService.findWorkTimesFromToDateHash(id,dateFormat.format(start.getTime()),dateFormat.format(startTime));
            System.out.println(workTime.size());
            if(workTime.size() == 1)
                if (user.getId().equals(workTime.get(0).getDriver().getId()))
                    if(workTime.get(0).getWorkedTimeInMinutes()>=480){
                        return new ResponseEntity<>(-1,HttpStatus.OK);
                    }else{
                        workTime.get(0).updateWorkingHourLogin(startTime);
                        workTimeService.updateWorkTime(workTime.get(0));
                        return new ResponseEntity<>(new WorkTimeDTOWithoutDriver(workTime.get(0)),HttpStatus.OK);
                    }
            return new ResponseEntity<>(0,HttpStatus.OK);
        }catch(WorkTimeNotFoundException workTimeNotFoundException){
            return new ResponseEntity<>("Working hour does not exist!",HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('DRIVER')")
    @GetMapping("/working-hour/{driverId}/logout")
    public ResponseEntity<?> workingHourValidationLogout (
            Principal principal,
            @PathVariable("driverId") int id) {
        try {
            User user = userService.findUserByEmail(principal.getName());
            Date startTime = new Date();
            Calendar start = Calendar.getInstance();
            start.setTime(startTime);
            start.add(Calendar.HOUR_OF_DAY, -24);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ArrayList<WorkTime> workTime = workTimeService.findWorkTimesFromToDateHash(id,dateFormat.format(start.getTime()),dateFormat.format(startTime));
            if(workTime.size() == 1)
                if (user.getId().equals(workTime.get(0).getDriver().getId())){
                    workTime.get(0).updateWorkingHour(startTime);
                    workTimeService.updateWorkTime(workTime.get(0));
                }
            return new ResponseEntity<>(0,HttpStatus.OK);
        }catch(WorkTimeNotFoundException workTimeNotFoundException){
            return new ResponseEntity<>("Working hour does not exist!",HttpStatus.NOT_FOUND);
        }
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/online/{id}")
    public ResponseEntity<?> onlineDriver (
            Principal principal,
            @PathVariable("id") int id) {
        try {
            User user = userService.findUserByEmail(principal.getName());
            Driver driver = driverService.findDriverById(id);
            if (user.getId().equals(driver.getId()))
                if(!driver.getIsOnline()) {
                    driver.setIsOnline(true);
                    driverService.updateDriver(driver);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
        }catch(DriverNotFoundException driverNotFoundException){
            return new ResponseEntity<>(new DriverNotFoundException("Driver not found").getMessage(),HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //ovo samo menja drivera u offline iz online
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/offline/{id}")
    public ResponseEntity<?> offlineDriver (
            Principal principal,
            @PathVariable("id") int id) {
        try {
            User user = userService.findUserByEmail(principal.getName());
            Driver driver = driverService.findDriverById(id);
            if (user.getId().equals(driver.getId()))
                if (driver.getIsOnline()) {
                    driver.setIsOnline(false);
                    driverService.updateDriver(driver);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
        }catch(DriverNotFoundException driverNotFoundException){
            return new ResponseEntity<>(new DriverNotFoundException("Driver not found").getMessage(),HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('DRIVER')")
    @GetMapping("/{id}/ride")
    public ResponseEntity<?> getRides (
            Principal principal,
            @PathVariable("id") int id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size,
            @RequestParam(defaultValue = "start_time") String sort,
            @RequestParam(defaultValue = "2021-12-07T07:00:50") String from,
            @RequestParam(defaultValue = "2023-12-08T10:40:00") String to) {

        try {
            checkDriversAuthorities(principal, id);
            System.out.println(id);
            Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
            Page<Ride> rides = rideService.findRidesForDriver(id, from, to, pageable);

            Map<String, Object> map = new HashMap<>();
            HashSet<RideRetDTO> ridesDTO = makeRideDTOS(rides);
            for(RideRetDTO rideRetDTO:ridesDTO){
                System.out.println(rideRetDTO);
            }
            map.put("totalCount", ridesDTO.size());
            map.put("results", ridesDTO);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (DriverNotFoundException driverNotFoundException) {
            return new ResponseEntity<>("Driver does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    private HashSet<RideRetDTO> makeRideDTOS(Page<Ride> rides) {
        HashSet<RideRetDTO> rideRetDTOHashSet = new HashSet<>();
        for (Ride ride : rides) {
            rideRetDTOHashSet.add(new RideRetDTO(ride, getLocationsByRideId(ride.getId())));
        }
        return rideRetDTOHashSet;
    }

    private Set<LocationsForRide> getLocationsByRideId(Integer id) {
        return locationsForRideService.getByRideId(id);
    }


    private List<Role> getRoles() {
        List<Role> roleDriver = roleService.findByName("ROLE_DRIVER");
        List<Role> roleUser = roleService.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        roles.add(roleUser.get(0));
        roles.add(roleDriver.get(0));
        return roles;
    }

    private void checkDriversAuthorities(Principal principal, Integer id) throws DriverNotFoundException{
        User user = userService.findUserByEmail(principal.getName());
        if (!id.equals(user.getId())) {
            throw new DriverNotFoundException("Driver does not exist!");
        }
    }

    private int checkRole(Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        boolean isDriver = false;
        while (true) {
            try {
                if (!isDriver) {
                    Driver driver = driverService.findDriverById(user.getId());
                    return 1;
                }
            }
            catch (DriverNotFoundException driverNotFoundException) {
                return 2;
            }
        }
    }
    private void checkAuthorities(Principal principal, Integer id) throws DriverNotFoundException {
        if (checkRole(principal) == 1){
            checkDriversAuthorities(principal, id);
        }
    }
}
