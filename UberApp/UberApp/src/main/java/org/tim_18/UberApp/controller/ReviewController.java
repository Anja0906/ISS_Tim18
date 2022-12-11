package org.tim_18.UberApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.*;
import org.tim_18.UberApp.dto.driverDTOs.DriverDTO;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.service.ReviewService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/review")
public class ReviewController {
    @Autowired
    private final ReviewService reviewService;

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

        map.put("totalCount",reviewDTOS.size());
        map.put("results",reviewDTOS);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/{rideId}")
    public ResponseEntity<Map<String, Object>> getReviewsForRide (@PathVariable("rideId") int id, @RequestParam(defaultValue = "0") Integer page,
                                                                 @RequestParam(defaultValue = "4") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviews = reviewService.findByRideId(id, pageable);
        Map<String, Object> map = new HashMap<>();
        HashSet<ReviewDTO> reviewDTOS = new HashSet<>();
        for (Review review:reviews) {
            reviewDTOS.add(new ReviewDTO(review));
        }

        map.put("totalCount",reviewDTOS.size());
        map.put("results",reviewDTOS);
        return new ResponseEntity<>(map, HttpStatus.OK);

    }
}
