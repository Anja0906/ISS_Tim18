package org.tim_18.UberApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.Validation.ErrorMessage;
import org.tim_18.UberApp.dto.*;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerEmailDTO;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerIdEmailDTO;
import org.tim_18.UberApp.exception.DriverNotFoundException;
import org.tim_18.UberApp.exception.PassengerNotFoundException;
import org.tim_18.UberApp.exception.RideNotFoundException;
import org.tim_18.UberApp.exception.VehicleNotFoundException;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.service.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("api/review")
public class ReviewController {
    @Autowired
    private final ReviewService reviewService;

    private final RideService rideService;
    private final VehicleService vehicleService;
    private final PassengerService passengerService;
    private final DriverService driverService;

    private final UserService userService;


    public ReviewController(ReviewService reviewService, RideService rideService, VehicleService vehicleService, PassengerService passengerService, DriverService driverService, UserService userService) {

        this.reviewService      = reviewService;
        this.rideService        = rideService;
        this.vehicleService     = vehicleService;
        this.passengerService   = passengerService;
        this.driverService      = driverService;
        this.userService        = userService;
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @PostMapping("/{rideId}/vehicle")
    public ResponseEntity<?> addReviewToVehicle(
            Principal principal,
            @PathVariable("rideId") int rideId,
            @RequestBody ReviewPostDTO reviewPostDTO) {
        try {
            Ride ride = rideService.findRideById(rideId);
            checkPassengersAuthorities(principal, ride);
            Passenger passenger = passengerService.findById(userService.findUserByEmail(principal.getName()).getId());
            Review review = reviewService.findByRideAndPassengerIdForVehicle(ride.getId(), passenger.getId());
            if (!(review==null)) {
                return new ResponseEntity<>(new ErrorMessage("Review for vehicle already left!"), HttpStatus.BAD_REQUEST);
            }

            review = new Review(reviewPostDTO.getRating(), reviewPostDTO.getComment(), passenger, ride,false);
            reviewService.save(review);

            Set<Review> reviews = ride.getReviews();
            reviews.add(review);
            ride.setReviews(new HashSet<>(reviews));
            rideService.updateRide(ride);
            return new ResponseEntity<>(new ReviewDTO(review), HttpStatus.OK);
        }catch(RideNotFoundException e){
            return new ResponseEntity<>("Ride does not exist!", HttpStatus.NOT_FOUND);
        }catch (VehicleNotFoundException e){
            return new ResponseEntity<>("Vehicle does not exist!", HttpStatus.NOT_FOUND);
        }

    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PASSENGER')")
    @GetMapping("/vehicle/{id}")
    public ResponseEntity<?> getReviewsForVehicle (
            Principal principal,
            @PathVariable("id") int id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size) {
        try{
            Vehicle vehicle = vehicleService.findVehicleById(id);
            Pageable pageable = PageRequest.of(page, size);
            Page<Review> reviews;
            if (checkRole(principal) == 1){
                Integer passengerId = userService.findUserByEmail(principal.getName()).getId();
                reviews = reviewService.findByVehicleAndPassengerId(id, passengerId, pageable);
            } else {
                reviews =reviewService.findByVehicleId(id, pageable);
            }

            Map<String, Object> map = new HashMap<>();
            HashSet<ReviewDTO> reviewDTOS = new ReviewDTO().makeReviewDTOS(reviews);

            map.put("totalCount",reviewDTOS.size());
            map.put("results",reviewDTOS);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }catch (VehicleNotFoundException e){
            return new ResponseEntity<>("Vehicle does not exist!", HttpStatus.NOT_FOUND);
        }

    }


    @PreAuthorize("hasRole('PASSENGER')")
    @PostMapping("/{rideId}/driver")
    public ResponseEntity<?> addReviewToDriver(
            Principal principal,
            @PathVariable("rideId") int rideId,
            @RequestBody ReviewPostDTO reviewPostDTO) {
        try {
            Ride ride = rideService.findRideById(rideId);

            checkPassengersAuthorities(principal, ride);
            Passenger passenger = passengerService.findById(userService.findUserByEmail(principal.getName()).getId());

            Review review = reviewService.findByRideAndPassengerIdForDriver(ride.getId(), passenger.getId());
            if (!(review==null)) {
                return new ResponseEntity<>(new ErrorMessage("Review for driver already left!"), HttpStatus.BAD_REQUEST);
            }

            review = new Review(reviewPostDTO.getRating(), reviewPostDTO.getComment(), passenger, ride,true);
            reviewService.save(review);
            Set<Review> reviews = ride.getReviews();
            reviews.add(review);
            ride.setReviews(new HashSet<>(reviews));
            rideService.updateRide(ride);

            ReviewDTO reviewDTO = new ReviewDTO(review);
            return new ResponseEntity<>(reviewDTO, HttpStatus.OK);
        }catch (RideNotFoundException rideNotFoundException){
            return new ResponseEntity<>("Ride does not exist!", HttpStatus.NOT_FOUND);
        }
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'PASSENGER')")
    @GetMapping("/driver/{id}")
    public ResponseEntity<?> getReviewsForDriver (
            Principal principal,
            @PathVariable("id") int id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size) {
        try{
            Driver driver = driverService.findDriverById(id);
            Pageable pageable = PageRequest.of(page, size);
            Page<Review> reviews;
            if (checkRole(principal) == 1){
                Integer passengerId = userService.findUserByEmail(principal.getName()).getId();
                reviews = reviewService.findByDriverAndPassengerId(id, passengerId, pageable);
            } else {
                reviews = reviewService.findByDriverId(id, pageable);
            }

            Map<String, Object> map = new HashMap<>();
            HashSet<ReviewDTO> reviewDTOS = new ReviewDTO().makeReviewDTOS(reviews);

            map.put("totalCount",reviewDTOS.size());
            map.put("results",reviewDTOS);
            return new ResponseEntity<>(map, HttpStatus.OK);

        }catch (DriverNotFoundException e){
            return new ResponseEntity<>("Driver does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{rideId}")
    public ResponseEntity<?> getReviewsForRide (
            @PathVariable("rideId") int id) {
        try {
            Ride ride = rideService.findRideById(id);
            Set<Passenger> passengers = ride.getPassengers();
            HashSet<ReviewDTOResponse> reviewsDTO = new HashSet<>();
            Review reviewDriver,reviewVehicle;
            for (Passenger passenger:passengers) {
                reviewDriver = reviewService.findByRideAndPassengerIdForDriver(id,passenger.getId());
                reviewVehicle = reviewService.findByRideAndPassengerIdForVehicle(id,passenger.getId());
                reviewsDTO.add(new ReviewDTOResponse(
                        new VehicleReviewDTO(reviewVehicle,new PassengerIdEmailDTO(passenger)),
                        new DriverReviewDTO(reviewDriver,new PassengerIdEmailDTO(passenger))));
            }
            return new ResponseEntity<>(reviewsDTO, HttpStatus.OK);
        } catch (RideNotFoundException e) {
            return new ResponseEntity<>("Ride does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    private void checkPassengersAuthorities(Principal principal, Ride ride) throws RideNotFoundException{
        User user = userService.findUserByEmail(principal.getName());
        Integer userId = user.getId();
        Set<Passenger> passengers = ride.getPassengers();
        for (Passenger p : passengers) {
            if (p.getId().equals(userId)) {
                return;
            }
        }
        throw new RideNotFoundException("Ride does not exist!");
    }

    private int checkRole(Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        Integer userId = user.getId();
        boolean isPassenger = false;
        while (true) {
            try {
                if (!isPassenger) {
                    Passenger passenger = passengerService.findById(userId);
                    return 1;
                }
            }
            catch (PassengerNotFoundException e) {
                return 2;
            }
        }
    }

    private void checkAuthorities(Principal principal, Ride ride) throws RideNotFoundException {
        if (checkRole(principal) == 1)
            checkPassengersAuthorities(principal, ride);
    }
}