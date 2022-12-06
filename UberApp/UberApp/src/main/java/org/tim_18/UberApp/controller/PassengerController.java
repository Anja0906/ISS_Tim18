package org.tim_18.UberApp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.PassengerDTO;
import org.tim_18.UberApp.mapper.PassengerDTOMapper;
import org.tim_18.UberApp.model.Location;
import org.tim_18.UberApp.model.Passenger;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.service.PassengerService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/passenger")
public class PassengerController {
    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @GetMapping()
    public ResponseEntity<List<Passenger>> findAll() {
        List<Passenger> passengers = passengerService.findAll();
        List<PassengerDTO> passengersDTO = new ArrayList<>() ;
        for (Passenger p : passengers) {
            System.out.println(p);
            passengersDTO.add(new PassengerDTO(p));
        }
        return new ResponseEntity<>(passengers, HttpStatus.OK);
    }

    @GetMapping("/find{id}")
    public ResponseEntity<Passenger> findById(@PathVariable("id") Integer id) {
        Passenger passenger = passengerService.findById(id);
        return new ResponseEntity<>(passenger, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<PassengerDTO> addPassenger(@RequestBody PassengerDTO userActivationDTO) {
        Passenger passenger = new Passenger();
        passenger.setFirstName(userActivationDTO.getFirstName());
        passenger.setLastName(userActivationDTO.getLastName());
        passenger.setAddress(userActivationDTO.getAddress());
        passenger.setEmail(userActivationDTO.getEmail());
        passenger.setPassword(userActivationDTO.getPassword());
        passenger.setImageLink(userActivationDTO.getImageLink());
        passenger.setTelephoneNumber(userActivationDTO.getTelephoneNumber());
        PassengerDTO retDTO = new PassengerDTO(passenger);
        HashSet<Location> locs = new HashSet<>();
        HashSet<Ride> rides = new HashSet<>();
        passenger.setFavouriteLocations(locs);
//        passenger.setRides(rides);
        return new ResponseEntity<>(retDTO, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Passenger> updatePassenger(@RequestBody Passenger passenger) {
        Passenger updatedPassenger = passengerService.updatePassenger(passenger);
        return new ResponseEntity<>(updatedPassenger, HttpStatus.OK);
    }
}
