package org.tim_18.UberApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.*;
import org.tim_18.UberApp.exception.DriverNotFoundException;
import org.tim_18.UberApp.exception.RideNotFoundException;
import org.tim_18.UberApp.exception.VehicleNotFoundException;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.service.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@RestController
@RequestMapping("api/review")
public class ReviewController {
    @Autowired
    private final ReviewService reviewService;

    private final RideService rideService;
    private final VehicleService vehicleService;
    private final PassengerService passengerService;
    private final DriverService driverService;


    public ReviewController(ReviewService reviewService, RideService rideService, VehicleService vehicleService,PassengerService passengerService,DriverService driverService) {

        this.reviewService      = reviewService;
        this.rideService        = rideService;
        this.vehicleService     = vehicleService;
        this.passengerService   = passengerService;
        this.driverService      = driverService;
    }

    @PostMapping("/{rideId}/vehicle")
    public ResponseEntity<?> addReviewToVehicle(
            @PathVariable("rideId") int rideId,
            @RequestBody ReviewPostDTO reviewPostDTO) {
        try {
            Ride ride = rideService.findRideById(rideId);

            Review review = new Review(reviewPostDTO.getRating(), reviewPostDTO.getComment(),ride,false);
            review.setRide(ride);
            reviewService.save(review);

            PassengerEmailDTO passengerEmailDTO = new PassengerEmailDTO(1, "bane");
            ReviewDTO reviewDTO = new ReviewDTO(review, passengerEmailDTO);

            return new ResponseEntity<>(reviewDTO, HttpStatus.OK);
        }catch(RideNotFoundException e){
            return new ResponseEntity<>(new RideNotFoundException("Ride does not exist!").getMessage(),HttpStatus.NOT_FOUND);
        }catch (VehicleNotFoundException e){
            return new ResponseEntity<>(new VehicleNotFoundException("Vehicle does not exist!").getMessage(),HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/vehicle/{id}")
    public ResponseEntity<?> getReviewsForVehicle (
            @PathVariable("id") int id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size) {
        try{
            Vehicle vehicle = vehicleService.findVehicleById(id);
            Pageable pageable = PageRequest.of(page, size);
            Page<Review> reviews = reviewService.findByVehicleId(id, pageable);

            Map<String, Object> map = new HashMap<>();
            HashSet<ReviewDTO> reviewDTOS = new ReviewDTO().makeReviewDTOS(reviews);

            map.put("totalCount",reviewDTOS.size());
            map.put("results",reviewDTOS);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }catch (VehicleNotFoundException e){
            return new ResponseEntity<>(new VehicleNotFoundException("Vehicle does not exist!").getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/{rideId}/driver")
    public ResponseEntity<?> addReviewToDriver(
            @PathVariable("rideId") int rideId,
            @RequestBody ReviewPostDTO reviewPostDTO) {
        try {
            Ride ride = rideService.findRideById(rideId);
            Review review = new Review(reviewPostDTO.getRating(), reviewPostDTO.getComment(), ride, true);
            reviewService.save(review);

            PassengerEmailDTO passengerEmailDTO = new PassengerEmailDTO(1, "bane");
            ReviewDTO reviewDTO = new ReviewDTO(review, passengerEmailDTO);

            return new ResponseEntity<>(reviewDTO, HttpStatus.OK);
        }catch (RideNotFoundException rideNotFoundException){
            return new ResponseEntity<>(new RideNotFoundException("Ride does not exist!").getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/driver/{id}")
    public ResponseEntity<?> getReviewsForDriver (
            @PathVariable("id") int id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size) {
        try{
            Driver driver = driverService.findDriverById(id);
            Pageable pageable = PageRequest.of(page, size);
            Page<Review> reviews = reviewService.findByDriverId(id, pageable);

            Map<String, Object> map = new HashMap<>();
            HashSet<ReviewDTO> reviewDTOS = new ReviewDTO().makeReviewDTOS(reviews);

            map.put("totalCount",reviewDTOS.size());
            map.put("results",reviewDTOS);
            return new ResponseEntity<>(map, HttpStatus.OK);

        }catch (DriverNotFoundException e){
            return new ResponseEntity<>(new DriverNotFoundException("Driver does not exist!").getMessage(), HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/{rideId}")
    public ResponseEntity<?> getReviewsForRide (
            @PathVariable("rideId") int id) {
        try {
            Ride ride = rideService.findRideById(id);
            HashSet<Review> reviews = reviewService.findByRideIdHash(id);
            HashSet<ReviewDTOResponse> reviewsDTO = new HashSet<>();
            Passenger passenger;
            PassengerEmailDTO passengerEmailDTO;
            int ind = 0;
            for (Review review : reviews) {
                passenger = passengerService.findById(review.getPassenger().getId());
                passengerEmailDTO = new PassengerEmailDTO(passenger);
                if (review.getDriver() == true) {
                    for (Review reviewVehicle : reviews)
                        if (reviewVehicle.getPassenger().getId() == review.getPassenger().getId() && reviewVehicle.getDriver() == false) {
                            reviewsDTO.add(new ReviewDTOResponse(new VehicleReviewDTO(review, passengerEmailDTO), new DriverReviewDTO(reviewVehicle, passengerEmailDTO)));
                            ind = 1;
                        }
                    if (ind == 0)
                        reviewsDTO.add(new ReviewDTOResponse(new VehicleReviewDTO(), new DriverReviewDTO(review, passengerEmailDTO)));

                } else {
                    for (Review reviewDriver : reviews)
                        if (reviewDriver.getPassenger().getId() == review.getPassenger().getId() && reviewDriver.getDriver() == true) {
                            reviewsDTO.add(new ReviewDTOResponse(new VehicleReviewDTO(reviewDriver, passengerEmailDTO), new DriverReviewDTO(review, passengerEmailDTO)));
                            ind = 1;
                        }
                    if (ind == 0)
                        reviewsDTO.add(new ReviewDTOResponse(new VehicleReviewDTO(review, passengerEmailDTO), new DriverReviewDTO()));
                }
                ind = 0;
            }
            return new ResponseEntity<>(reviewsDTO, HttpStatus.OK);
        } catch (RideNotFoundException e) {
            return new ResponseEntity<>(new RideNotFoundException("Ride does not exist!").getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
