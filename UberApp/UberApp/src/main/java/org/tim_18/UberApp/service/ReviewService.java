package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.model.Review;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.model.Vehicle;
import org.tim_18.UberApp.repository.ReviewRepository;
import org.tim_18.UberApp.repository.RideRepository;
import org.tim_18.UberApp.repository.VehicleRepository;

import java.util.ArrayList;
import java.util.HashSet;

@Service("reviewService")
public class ReviewService {
    @Autowired
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository, RideRepository rideRepository, VehicleRepository vehicleRepository) {
        this.reviewRepository = reviewRepository;
    }


    public void save(Review review) {
        reviewRepository.save(review);
    }
    public Page<Review> findAll(Pageable page){
        return reviewRepository.findAll(page);
    }

    public Page<Review> findByRideId(int id, Pageable pageable) {
        return reviewRepository.findByRideId(id, pageable);
    }

    public Page<Review> findByDriverId(int id, Pageable pageable) {
        return reviewRepository.findByDriverId(id, pageable);
    }

    public Page<Review> findByVehicleId(int id, Pageable pageable) {
        return reviewRepository.findByVehicleId(id, pageable);
    }

    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }

    public HashSet<Review> findByRideIdHash(Integer id) {
        return reviewRepository.findByRideIdHash(id);
    }

    public Page<Review> findByVehicleAndPassengerId(int vehicleId, int passengerId, Pageable pageable) {
        return reviewRepository.findByVehicleAndPassengerId(vehicleId, passengerId, pageable);
    }

    public Page<Review> findByDriverAndPassengerId(int driverId, int passengerId, Pageable pageable) {
        return reviewRepository.findByDriverAndPassengerId(driverId, passengerId, pageable);
    }

    public Review findByRideAndPassengerIdForVehicle(int rideId, int passengerId) {
        return reviewRepository.findByRideAndPassengerIdForVehicle(rideId, passengerId);
    }

    public Review findByRideAndPassengerIdForDriver(int rideId, int passengerId) {
        return reviewRepository.findByRideAndPassengerIdForDriver(rideId, passengerId);
    }
}