package org.tim_18.UberApp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.dto.RejectionDTO;
import org.tim_18.UberApp.dto.driverDTOs.DriverEmailDTO;
import org.tim_18.UberApp.dto.rideDTOs.RideDTOWithPanic;
import org.tim_18.UberApp.dto.rideDTOs.RideRecDTO;
import org.tim_18.UberApp.dto.rideDTOs.RideRetDTO;
import org.tim_18.UberApp.mapper.rideDTOmappers.RideRetDTOMapper;
import org.tim_18.UberApp.model.Driver;
import org.tim_18.UberApp.model.Rejection;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.model.Status;
import org.tim_18.UberApp.repository.RideRepository;

import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.repository.RideRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
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

    public Ride createRide(Ride ride){
        return rideRepository.save(ride);
    }

    public Ride getDriverActiveRide(Integer driverId) {
        return rideRepository.findDriverActiveRide(driverId);
    }

    public Ride getPassengerActiveRide(Integer passengerId) {
        return rideRepository.findPassengerActiveRide(passengerId);
    }

    public Ride findRideById(Integer id) {
        return rideRepository.findRideById(id);
    }

    //    Called by: acceptRide, cancelRide, endRide, activatePanic, withdrawRide
    public Ride updateRide(Ride ride) {
        return rideRepository.save(ride);
    }

    public Page<Ride> findRidesForDriver(Integer id, LocalDateTime start, LocalDateTime end, Pageable pageable){return rideRepository.findRidesForDriver(id,start,end,pageable);}

}
