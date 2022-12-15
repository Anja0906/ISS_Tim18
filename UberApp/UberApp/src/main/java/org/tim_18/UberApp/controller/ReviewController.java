package org.tim_18.UberApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.*;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.service.ReviewService;
import org.tim_18.UberApp.service.RideService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@RestController
@RequestMapping("api/review")
public class ReviewController {
    @Autowired
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{rideId}/vehicle/{id}")
    public ResponseEntity<ReviewDTO> addReviewToVehicle(
            @PathVariable("rideId") int rideId,
            @PathVariable("id") int id,
            @RequestBody ReviewPostDTO reviewPostDTO) {
        Review review = new Review(reviewPostDTO.getRating(),reviewPostDTO.getComment());
        review.setRide(reviewService.getRideById(rideId));
        reviewService.save(review);

        PassengerEmailDTO passengerEmailDTO = new PassengerEmailDTO(1,"bane");
        ReviewDTO reviewDTO = new ReviewDTO(review,passengerEmailDTO);

        return new ResponseEntity<>(reviewDTO, HttpStatus.OK);
    }

    @GetMapping("/vehicle/{id}")
    public ResponseEntity<Map<String, Object>> getReviewsForVehicle (
            @PathVariable("id") int id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviews = reviewService.findByVehicleId(id, pageable);

        Map<String, Object> map = new HashMap<>();
        HashSet<ReviewDTO> reviewDTOS = new ReviewDTO().makeReviewDTOS(reviews);

        map.put("totalCount",reviewDTOS.size());
        map.put("results",reviewDTOS);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/{rideId}/driver/{id}")
    public ResponseEntity<ReviewDTO> addReviewToDriver(
            @PathVariable("rideId") int rideId,
            @PathVariable("id") int id,
            @RequestBody ReviewPostDTO reviewPostDTO) {
        Ride ride = reviewService.getRideById(rideId);
        Review review = new Review(reviewPostDTO.getRating(),reviewPostDTO.getComment(),ride);
        reviewService.save(review);

        PassengerEmailDTO passengerEmailDTO = new PassengerEmailDTO(1,"bane");
        ReviewDTO reviewDTO = new ReviewDTO(review,passengerEmailDTO);

        return new ResponseEntity<>(reviewDTO, HttpStatus.OK);
    }

    @GetMapping("/driver/{id}")
    public ResponseEntity<Map<String, Object>> getReviewsForDriver (
            @PathVariable("id") int id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviews = reviewService.findByDriverId(id, pageable);

        Map<String, Object> map = new HashMap<>();
        HashSet<ReviewDTO> reviewDTOS = new ReviewDTO().makeReviewDTOS(reviews);

        map.put("totalCount",reviewDTOS.size());
        map.put("results",reviewDTOS);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/{rideId}")
    public ResponseEntity<ReviewDTOResponse> getReviewsForRide (@PathVariable("rideId") int id) {
        PassengerEmailDTO passengerDTO = new PassengerEmailDTO(123,"user@example.com");
        VehicleReviewDTO vehicleReviewDTO = new VehicleReviewDTO(123,3,"The vehicle was bad and dirty",passengerDTO);
        DriverReviewDTO driverReviewDTO = new DriverReviewDTO(123,3,"The driver was driving too fast",passengerDTO);

        ReviewDTOResponse reviewDTOResponse = new ReviewDTOResponse(vehicleReviewDTO,driverReviewDTO);
        return new ResponseEntity<>(reviewDTOResponse, HttpStatus.OK);

    }
}
