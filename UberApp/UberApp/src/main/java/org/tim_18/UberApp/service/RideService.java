package org.tim_18.UberApp.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.exception.RideNotFoundException;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.repository.RideRepository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

@Service
public class RideService {

    @Autowired
    private final RideRepository rideRepository;


    public RideService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    public List<Ride> findRidesByPassengersId(Integer id, String from, String to) {
        return rideRepository.findRidesByPassengersId(id, from, to);
    }

    public Page<Ride> findRidesByPassengersId(Integer id, String from, String to, Pageable pageable) {
        return rideRepository.findRidesByPassengersId(id, from, to, pageable);
    }

    public Page<Ride> findPendingRidesByStatus(String status,Pageable pageable) {
        return rideRepository.findPendingRidesByStatus(status,pageable);
    }

    public Ride createRide(Ride ride){
        return rideRepository.save(ride);
    }

    public Ride getDriverActiveRide(Integer driverId) {
        return rideRepository.findDriverActiveRide(driverId)
                .orElseThrow(() -> new RideNotFoundException("Ride was not found"));

    }

    public Ride getPassengerActiveRide(Integer passengerId) {
        return rideRepository.findPassengerActiveRide(passengerId)
                .orElseThrow(() -> new RideNotFoundException("Ride was not found"));

    }

    public Ride findRideById(Integer id) {
        return rideRepository.findRideById(id)
                .orElseThrow(() -> new RideNotFoundException("Ride was not found"));
    }

    public Page<Ride> findRidesForUserPage(Integer id, Pageable pageable) {return rideRepository.findRidesForUserPage(id,pageable);}

    //    Called by: acceptRide, cancelRide, endRide, activatePanic, withdrawRide
    public Ride updateRide(Ride ride) {
        return rideRepository.save(ride);
    }

    public Page<Ride> findRidesForDriver(Integer id, String start, String end, Pageable pageable){return rideRepository.findRidesForDriver(id,start,end,pageable);}

    public boolean checkRide(Integer passengerId) {
        List<Ride> pendingRides = rideRepository.findPassengersRidesByStatus(passengerId, "PENDING");
        return pendingRides.isEmpty();
        // returns true if there's no pending rides => passenger can make a new ride
    }

    public Page<Ride> findAll(Pageable pageable) {
        return rideRepository.findAll(pageable);
    }

    public List<Ride> findAll() {
        return rideRepository.findAll();
    }

    public List<Ride> findRidesByUser(Integer id) {
        return rideRepository.findRidesForUser(id);
    }

    public List<Ride> findRidesByUser(Integer id, String from, String to) {
        return rideRepository.findRidesForUser(id, from, to);
    }
}
