package org.tim_18.UberApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.*;
<<<<<<< Updated upstream
import org.tim_18.UberApp.dto.driverDTOs.DriverDTO;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.service.ReviewService;
=======
import org.tim_18.UberApp.exception.DriverNotFoundException;
import org.tim_18.UberApp.exception.RideNotFoundException;
import org.tim_18.UberApp.exception.VehicleNotFoundException;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.service.*;
>>>>>>> Stashed changes

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/review")
public class ReviewController {
    @Autowired
    private final ReviewService reviewService;

<<<<<<< Updated upstream
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{rideId}/vehicle/{id}")
    public ResponseEntity<ReviewDTO> addReviewToVehicle(@PathVariable("rideId") int rideId, @PathVariable("id") int id,  @RequestBody ReviewPostDTO reviewPostDTO) {
        Review review = new Review();
        review.setRating(reviewPostDTO.getRating());
        review.setComment(reviewPostDTO.getComment());
        review.setRide(reviewService.getRideById(rideId));
        reviewService.save(review);
        System.out.println(review.toString());
        PassengerEmailDTO passengerEmailDTO = new PassengerEmailDTO(1,"bane");
        ReviewDTO reviewDTO = new ReviewDTO(review,passengerEmailDTO);
        System.out.println(reviewDTO.toString());
        return new ResponseEntity<>(reviewDTO, HttpStatus.CREATED);
    }

    @GetMapping("/vehicle/{id}")
    public ResponseEntity<Map<String, Object>> getReviewsForVehicle (@PathVariable("id") int id, @RequestParam(defaultValue = "0") Integer page,
                                                           @RequestParam(defaultValue = "4") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviews = reviewService.findByVehicleId(id, pageable);
        Map<String, Object> map = new HashMap<>();
        HashSet<ReviewDTO> reviewDTOS = new HashSet<>();
        for (Review review:reviews) {
            reviewDTOS.add(new ReviewDTO(review));
        }

        map.put("totalCount",reviewDTOS.size());
        map.put("results",reviewDTOS);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/{rideId}/driver/{id}")
    public ResponseEntity<ReviewDTO> addReviewToDriver(@PathVariable("rideId") int rideId, @PathVariable("id") int id,  @RequestBody ReviewPostDTO reviewPostDTO) {
        Review review = new Review();
        review.setRating(reviewPostDTO.getRating());
        review.setComment(reviewPostDTO.getComment());
        review.setRide(reviewService.getRideById(rideId));
        reviewService.save(review);
        System.out.println(review.toString());
        PassengerEmailDTO passengerEmailDTO = new PassengerEmailDTO(1,"bane");
        ReviewDTO reviewDTO = new ReviewDTO(review,passengerEmailDTO);
        System.out.println(reviewDTO.toString());
        return new ResponseEntity<>(reviewDTO, HttpStatus.CREATED);
    }

    @GetMapping("/driver/{id}")
    public ResponseEntity<Map<String, Object>> getReviewsForDriver (@PathVariable("id") int id, @RequestParam(defaultValue = "0") Integer page,
                                                                     @RequestParam(defaultValue = "4") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviews = reviewService.findByDriverId(id, pageable);
        Map<String, Object> map = new HashMap<>();
        HashSet<ReviewDTO> reviewDTOS = new HashSet<>();
        for (Review review:reviews) {
            reviewDTOS.add(new ReviewDTO(review));
        }
=======
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

    @PostMapping("/{rideId}/vehicle/{id}")
    public ResponseEntity<ReviewDTO> addReviewToVehicle(
            @PathVariable("rideId") int rideId,
            @PathVariable("id") int id,
            @RequestBody ReviewPostDTO reviewPostDTO) {
        try {
            Ride ride = rideService.findRideById(rideId);
            Vehicle vehicle = vehicleService.findVehicleById(id);

            Review review = new Review(reviewPostDTO.getRating(), reviewPostDTO.getComment(),ride,false);
            review.setRide(ride);
            reviewService.save(review);

            PassengerEmailDTO passengerEmailDTO = new PassengerEmailDTO(1, "bane");
            ReviewDTO reviewDTO = new ReviewDTO(review, passengerEmailDTO);

            return new ResponseEntity<>(reviewDTO, HttpStatus.OK);
        }catch(RideNotFoundException e){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }catch (VehicleNotFoundException e){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/vehicle/{id}")
    public ResponseEntity<Map<String, Object>> getReviewsForVehicle (
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
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/{rideId}/driver/{id}")
    public ResponseEntity<ReviewDTO> addReviewToDriver(
            @PathVariable("rideId") int rideId,
            @PathVariable("id") int id,
            @RequestBody ReviewPostDTO reviewPostDTO) {
        try {
            Ride ride = rideService.findRideById(rideId);
            Review review = new Review(reviewPostDTO.getRating(), reviewPostDTO.getComment(), ride, true);
            reviewService.save(review);

            PassengerEmailDTO passengerEmailDTO = new PassengerEmailDTO(1, "bane");
            ReviewDTO reviewDTO = new ReviewDTO(review, passengerEmailDTO);

            return new ResponseEntity<>(reviewDTO, HttpStatus.OK);
        }catch (RideNotFoundException rideNotFoundException){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/driver/{id}")
    public ResponseEntity<Map<String, Object>> getReviewsForDriver (
            @PathVariable("id") int id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size) {
        try{
            Driver driver = driverService.findDriverById(id);
            Pageable pageable = PageRequest.of(page, size);
            Page<Review> reviews = reviewService.findByDriverId(id, pageable);

            Map<String, Object> map = new HashMap<>();
            HashSet<ReviewDTO> reviewDTOS = new ReviewDTO().makeReviewDTOS(reviews);
>>>>>>> Stashed changes

            map.put("totalCount",reviewDTOS.size());
            map.put("results",reviewDTOS);
            return new ResponseEntity<>(map, HttpStatus.OK);

        }catch (DriverNotFoundException e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/{rideId}")
<<<<<<< Updated upstream
    public ResponseEntity<ReviewDTOResponse> getReviewsForRide (@PathVariable("rideId") int id) {
        PassengerEmailDTO passengerDTO = new PassengerEmailDTO(123,"user@example.com");
        VehicleReviewDTO vehicleReviewDTO = new VehicleReviewDTO(123,3,"The vehicle was bad and dirty",passengerDTO);
        DriverReviewDTO driverReviewDTO = new DriverReviewDTO(123,3,"The driver was driving too fast",passengerDTO);
        ReviewDTOResponse reviewDTOResponse = new ReviewDTOResponse(vehicleReviewDTO,driverReviewDTO);
        return new ResponseEntity<>(reviewDTOResponse, HttpStatus.OK);

=======
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
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
>>>>>>> Stashed changes
    }
}
