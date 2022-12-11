package org.tim_18.UberApp.controller;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.locationDTOs.LocationDTO;
import org.tim_18.UberApp.dto.locationDTOs.LocationSetDTO;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerEmailDTO;
import org.tim_18.UberApp.dto.rideDTOs.RideDTOWithPanic;
import org.tim_18.UberApp.dto.rideDTOs.RideRecDTO;
import org.tim_18.UberApp.dto.rideDTOs.RideRetDTO;
import org.tim_18.UberApp.mapper.LocationDTOMapper;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.service.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/ride")
public class RideController {


    private final RideService rideService;
    private final DriverService driverService;
    private final RejectionService rejectionService;
    private final ReviewService reviewService;
    private final PanicService panicService;
    private final PassengerService passengerService;
    private LocationDTOMapper locationDTOMapper = new LocationDTOMapper(new ModelMapper());
//
//    private RideRecDTOMapper dtoRecMapper;

    public RideController(RideService rideService, DriverService driverService, RejectionService rejectionService, ReviewService reviewService, PanicService panicService, PassengerService passengerService) {
        this.rideService = rideService;
        this.driverService = driverService;
        this.rejectionService = rejectionService;
        this.reviewService = reviewService;
        this.panicService = panicService;
        this.passengerService = passengerService;
    }

    @PostMapping
    public ResponseEntity<RideRetDTO> createARide(@RequestBody RideRecDTO oldDTO){
        Ride ride = fromDTOtoRide(oldDTO);
        ride = rideService.createRide(ride);
        RideRetDTO newDto = new RideRetDTO(ride);
        return new ResponseEntity<>(newDto, HttpStatus.CREATED);
    }

    @GetMapping("/driver/{driverId}/active")
    public RideRetDTO getDriverActiveRide(@PathVariable("driverId") Integer driverId) {
        Ride ride = rideService.getDriverActiveRide(driverId);
        return new RideRetDTO(ride);
    }

    @GetMapping("/passenger/{passengerId}/active")
    public RideRetDTO getPassengerActiveRide(@PathVariable("passengerId") Integer passengerId) {
        Ride ride = rideService.getPassengerActiveRide(passengerId);
        return new RideRetDTO(ride);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideRetDTO> findRideById(@PathVariable("id") Integer id) {
        Ride ride = rideService.findRideById(id);
        return new ResponseEntity<>(new RideRetDTO(ride), HttpStatus.OK);

    }

    @PutMapping("/{id}/withdraw")
    public RideRetDTO cancelRide(@PathVariable("id") Integer id) {
        Ride ride = rideService.findRideById(id);
        ride.setStatus(Status.CANCELLED);
        ride = rideService.updateRide(ride);
        return new RideRetDTO(ride);
    }

    @PutMapping("/{id}/panic")
    public RideDTOWithPanic activatePanic(@PathVariable("id") Integer id, @RequestBody String reason){
        return null;
    }

    @PutMapping("/{id}/accept")
    public RideRetDTO acceptRide(@PathVariable("id")Integer id) {
        Ride ride = rideService.findRideById(id);
        ride.setStatus(Status.ACCEPTED);
        ride = rideService.updateRide(ride);
        return new RideRetDTO(ride);
    }

    @PutMapping("/{id}/end")
    public RideRetDTO endRide(@PathVariable("id")Integer id) {
        Ride ride = rideService.findRideById(id);
        ride.setStatus(Status.FINISHED);
        ride = rideService.updateRide(ride);
        return new RideRetDTO(ride);
    }

    @PutMapping("/{id}/cancel")
    public RideRetDTO cancelRide(@PathVariable("id") Integer id,  @RequestBody String reason){
        return null;
    }

    private Ride fromDTOtoRide(RideRecDTO dto) {
        Date startTime = new Date();
        Date endTime = new Date();
        long totalCost = 5000;
        Driver driver = driverService.findDriverById(4);
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
//        Rejection newRejection = new Rejection();
//        newRejection.setTime(new Date());
//        newRejection.setReason("blablabla");
//        newRejection = rejectionService.addRejection(newRejection);
//        RejectionDTO rejectionDTO = new RejectionDTO(newRejection);
        return new Ride(startTime, endTime, totalCost, driver, passengers, estimatedTimeInMinutes, dto.getVehicleType(),
                dto.isBabyTransport(), dto.isPetTransport(), null, locations, status, reviews, panic);
    }
}
