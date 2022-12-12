package org.tim_18.UberApp.controller;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.PanicDTO;
import org.tim_18.UberApp.dto.RejectionDTO;
import org.tim_18.UberApp.dto.locationDTOs.LocationDTO;
import org.tim_18.UberApp.dto.locationDTOs.LocationSetDTO;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerEmailDTO;
import org.tim_18.UberApp.dto.rideDTOs.RideRecDTO;
import org.tim_18.UberApp.dto.rideDTOs.RideRetDTO;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.mapper.LocationDTOMapper;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.service.*;

import java.util.*;

@RestController
@RequestMapping("/api/ride")
public class RideController {

    private final RideService rideService;
    private final DriverService driverService;
    private final RejectionService rejectionService;
    private final ReviewService reviewService;
    private final PanicService panicService;
    private final PassengerService passengerService;
    private final UserService userService;


    private LocationDTOMapper locationDTOMapper = new LocationDTOMapper(new ModelMapper());


    public RideController(RideService rideService, DriverService driverService, RejectionService rejectionService, ReviewService reviewService, PanicService panicService, PassengerService passengerService, UserService userService) {
        this.rideService        = rideService;
        this.driverService      = driverService;
        this.rejectionService   = rejectionService;
        this.reviewService      = reviewService;
        this.panicService       = panicService;
        this.passengerService   = passengerService;
        this.userService        = userService;
    }

    @PostMapping
    public ResponseEntity<RideRetDTO> createARide(@RequestBody RideRecDTO oldDTO){
        Ride ride = fromDTOtoRide(oldDTO);
        ride = rideService.createRide(ride);
        RideRetDTO newDto = new RideRetDTO(ride);
        return new ResponseEntity<>(newDto, HttpStatus.CREATED);
    }

    @GetMapping("/driver/{driverId}/active")
    public ResponseEntity<RideRetDTO> getDriverActiveRide(@PathVariable("driverId") Integer driverId) {
        try {
            Ride ride = rideService.getDriverActiveRide(driverId);
            return new ResponseEntity<>(new RideRetDTO(ride), HttpStatus.OK);
        } catch(UserNotFoundException e){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/passenger/{passengerId}/active")
    public ResponseEntity<RideRetDTO> getPassengerActiveRide(@PathVariable("passengerId") Integer passengerId) {
        try {
            Ride ride = rideService.getPassengerActiveRide(passengerId);
            return new ResponseEntity<>(new RideRetDTO(ride), HttpStatus.OK);
        } catch(UserNotFoundException e){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideRetDTO> findRideById(@PathVariable("id") Integer id) {
        try {
            Ride ride = rideService.findRideById(id);
            return new ResponseEntity<>(new RideRetDTO(ride), HttpStatus.OK);
        } catch(UserNotFoundException e){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/{id}/withdraw")
    public ResponseEntity<RideRetDTO> cancelRide(@PathVariable("id") Integer id) {
        try {
            Ride ride = rideService.findRideById(id);
            ride.setStatus(Status.CANCELLED);
            ride = rideService.updateRide(ride);
            return new ResponseEntity<>(new RideRetDTO(ride), HttpStatus.OK);
        } catch(UserNotFoundException e){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/panic")
    public ResponseEntity<PanicDTO> activatePanic(@PathVariable("id") Integer id, @RequestBody String reason){
        try {
            Ride ride = rideService.findRideById(id);
            Panic panic = new Panic();
            panic.setRide(ride);
            List<User> users = userService.findAllUsers();
            if (users.size() == 0) throw new UserNotFoundException("No users");
            User user = users.get(0);
            panic.setUser(user);
            panic.setTime(new Date());
            panic.setReason(reason);
            panic = panicService.addPanic(panic);
            ride.setPanic(panic);
            return new ResponseEntity<>(new PanicDTO(panic), HttpStatus.OK);
        } catch(UserNotFoundException e){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<RideRetDTO> acceptRide(@PathVariable("id")Integer id) {
        try {
            Ride ride = rideService.findRideById(id);
            ride.setStatus(Status.ACCEPTED);
            ride = rideService.updateRide(ride);
            return new ResponseEntity<>(new RideRetDTO(ride), HttpStatus.OK);
        } catch(UserNotFoundException e){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/end")
    public ResponseEntity<RideRetDTO> endRide(@PathVariable("id")Integer id) {
        Ride ride = null;
        try {
            ride = rideService.findRideById(id);
        } catch(UserNotFoundException e){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
        ride.setStatus(Status.FINISHED);
        ride = rideService.updateRide(ride);
        return new ResponseEntity<>(new RideRetDTO(ride), HttpStatus.OK);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<RideRetDTO> cancelRide(@PathVariable("id") Integer id,  @RequestBody String reason){
        try {
            Ride ride = rideService.findRideById(id);
            Rejection rejection = new Rejection();
            rejection.setTime(new Date());
            rejection.setReason(reason);
//            rejection = rejectionService.addRejection(rejection);
            ride.setRejection(rejection);
//            ride = rideService.updateRide(ride);
            return new ResponseEntity<>(new RideRetDTO(ride), HttpStatus.OK);
        } catch(UserNotFoundException e){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    private Ride fromDTOtoRide(RideRecDTO dto) throws UserNotFoundException {
        Date startTime = new Date();
        Date endTime = new Date();
        long totalCost = 5000;
        List<Driver> drivers = driverService.findAllDrivers();
        if (drivers.size() == 0) throw new UserNotFoundException("No available driver");
        Driver driver = drivers.get(0);
        Set<PassengerEmailDTO> passengersDTOs = dto.getPassengers();
        HashSet<Passenger> passengers = new HashSet<>();
        for (PassengerEmailDTO passDTO: passengersDTOs) {
            passengers.add(passengerService.findById(passDTO.getId()));
        }
        int estimatedTimeInMinutes = 4;
        Set<LocationSetDTO> locationsDTO = dto.getLocations();
        ArrayList<LocationSetDTO> locationSetDTOArrayList = new ArrayList<>();
        for (LocationSetDTO locDTO:locationsDTO) {
            locationSetDTOArrayList.add(locDTO);
        }
        HashSet<Location> locations = new HashSet<>();
        for (int i = 0; i < locationSetDTOArrayList.size(); i++) {
            LocationSetDTO lsd = locationSetDTOArrayList.get(i);
            LocationDTO ld = lsd.getDeparture();
            Location loc = LocationDTOMapper.fromDTOtoLocation(ld);
            locations.add(loc);
            if (i == locationSetDTOArrayList.size() - 1 ) {
                ld = lsd.getDestination();
                loc = LocationDTOMapper.fromDTOtoLocation(ld);
                locations.add(loc);
            }
        }
        Status status = Status.PENDING;
        HashSet<Review> reviews = new HashSet<>();
        Review review = new Review();
        review = reviewService.addReview(review);
        reviews.add(review);
        Panic panic = new Panic();
        panic = panicService.addPanic(panic);
        List<Rejection> rejections = rejectionService.findAll();
        Rejection newRejection;
        if (rejections.size() == 0) {
            newRejection = null;
        }else {
            newRejection = rejections.get(0);
        }
        RejectionDTO rejectionDTO = new RejectionDTO(newRejection);
        return new Ride(startTime, endTime, totalCost, driver, passengers, estimatedTimeInMinutes, dto.getVehicleType(),
                dto.isBabyTransport(), dto.isPetTransport(), newRejection, locations, status, reviews, panic);
    }
}
