package org.tim_18.UberApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.*;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.service.ReviewService;

import java.util.HashSet;
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
    public ResponseEntity<HashSet<ReviewDTO>> getReviewsForVehicle (
            @PathVariable("id") int id) {
        HashSet<ReviewDTO> reviewDTOs = new HashSet<>();
        HashSet<Review> reviews = reviewService.findByVehicleId(id);
        if(reviews.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        }else{
            for(Review review : reviews){
                ReviewDTO reviewDTO = new ReviewDTO(review);
                reviewDTOs.add(reviewDTO);
            }
            return new ResponseEntity<>(reviewDTOs, HttpStatus.OK);
        }
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
    public ResponseEntity<HashSet<ReviewDTO>> getReviewsForDriver (
            @PathVariable("id") int id) {
        HashSet<ReviewDTO> reviewDTOs = new HashSet<>();
        HashSet<Review> reviews = reviewService.findByDriverId(id);
        if(reviews.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        }else{
            for(Review review : reviews){
                ReviewDTO reviewDTO = new ReviewDTO(review);
                reviewDTOs.add(reviewDTO);
            }
            return new ResponseEntity<>(reviewDTOs, HttpStatus.OK);
        }
    }

    @GetMapping("/{rideId}")
    public ResponseEntity<HashSet<ReviewDTO>> getReviewsForRide (
            @PathVariable("rideId") int id) {
        HashSet<ReviewDTO> reviewDTOs = new HashSet<>();
        HashSet<Review> reviews = reviewService.findByRideId(id);
        if(reviews.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        }else{
            for(Review review : reviews){
                ReviewDTO reviewDTO = new ReviewDTO(review);
                reviewDTOs.add(reviewDTO);
            }
            return new ResponseEntity<>(reviewDTOs, HttpStatus.OK);
        }
    }
}
