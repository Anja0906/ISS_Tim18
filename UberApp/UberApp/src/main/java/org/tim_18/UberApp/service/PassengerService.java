package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.mapper.PassengerDTOMapper;
import org.tim_18.UberApp.model.Location;
import org.tim_18.UberApp.model.Passenger;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.repository.PassengerRepository;

import java.util.HashSet;
import java.util.List;

@Service("passengerService")
public class PassengerService {
    @Autowired
    private final PassengerRepository passengerRepository;

    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    public Passenger addPassenger(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    public Page<Passenger> findAll(Pageable page){
        return passengerRepository.findAll(page);
    }

    public List<Passenger> findAll(){
        return passengerRepository.findAll();
    }

    public Passenger updatePassenger(Passenger passenger) {
        return passengerRepository.save(passenger);
    };

    public Passenger findById(Integer id) {
        return passengerRepository.findPassengerById(id).orElseThrow(() -> new UserNotFoundException("Passenger by id " + id + " was not found") );
    }

    public Passenger save(Passenger passenger){
        return passengerRepository.save(passenger);
    }
}
