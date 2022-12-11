package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.model.Review;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.model.Vehicle;
import org.tim_18.UberApp.repository.ReviewRepository;
import org.tim_18.UberApp.repository.RideRepository;
import org.tim_18.UberApp.repository.VehicleRepository;

import java.util.HashSet;

@Service("reviewService")
public class ReviewService {
    @Autowired
    private final ReviewRepository reviewRepository;
    @Autowired
    private final RideRepository rideRepository;

    @Autowired
    private final VehicleRepository vehicleRepository;

    public ReviewService(ReviewRepository reviewRepository, RideRepository rideRepository, VehicleRepository vehicleRepository) {
        this.reviewRepository = reviewRepository;
        this.rideRepository = rideRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public Ride getRideById(Integer id){
        return rideRepository.findById(id).get();
    }

    public void save(Review review) {
        reviewRepository.save(review);
    }

    public Vehicle getVehicleById(Integer id){
        return vehicleRepository.findById(id).get();
    }

    public HashSet<Review> findByVehicleId(int id) {
        return reviewRepository.findByVehicleId(id);
    }

    public HashSet<Review> findByRideId(int id) {
        return reviewRepository.findByRideId(id);
    }

    public HashSet<Review> findByDriverId(int id) {
        return reviewRepository.findByDriverId(id);
    }
    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }

}
