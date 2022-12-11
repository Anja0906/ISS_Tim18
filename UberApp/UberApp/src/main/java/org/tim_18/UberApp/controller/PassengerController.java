package org.tim_18.UberApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.FindAllDTO;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerDTOnoPassword;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerDTOwithPassword;
import org.tim_18.UberApp.dto.rideDTOs.RideRetDTO;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.mapper.passengerDTOmappers.PassengerDTOwithPasswordMapper;
import org.tim_18.UberApp.model.Passenger;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.model.UserActivation;
import org.tim_18.UberApp.service.PassengerService;
import org.tim_18.UberApp.service.RideService;
import org.tim_18.UberApp.service.UserActivationService;

import java.util.*;

@RestController
@RequestMapping("/api/passenger")
public class PassengerController {
    private final PassengerService passengerService;
    private final RideService rideService;
    private final UserActivationService userActivationService;
    @Autowired
    private PassengerDTOwithPasswordMapper dtoWithPasswordMapper;

    public PassengerController(PassengerService passengerService, RideService rideService, UserActivationService userActivationService) {
        this.passengerService = passengerService;
        this.rideService = rideService;
        this.userActivationService = userActivationService;
    }

    @PostMapping()
    public ResponseEntity<PassengerDTOnoPassword> addPassenger(@RequestBody PassengerDTOwithPassword dto) {
        Passenger passenger = dtoWithPasswordMapper.fromDTOtoPassenger(dto);
        passenger = passengerService.addPassenger(passenger);
        PassengerDTOnoPassword retDto = new PassengerDTOnoPassword(passenger);
        return new ResponseEntity<>(retDto, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Map<String, Object>> findAll(@RequestParam(defaultValue = "0") Integer page,
                                                       @RequestParam(defaultValue = "4") Integer size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Passenger> pagedResult = passengerService.findAll(paging);
        List<Passenger> passengers = passengerService.findAll();
        List<PassengerDTOnoPassword> passengersDTO = new ArrayList<>() ;
        for (Passenger p : passengers) {
            passengersDTO.add(new PassengerDTOnoPassword(p));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("totalCount", pagedResult.getTotalElements());
        response.put("results", passengersDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("activate/{activationId}")
    public ResponseEntity activateUser(@PathVariable("activationId") Integer id){
        try {
            UserActivation ua = userActivationService.findUserActivationById(id);
            ua.getUser().setActive(true);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(UserNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerDTOnoPassword> findById(@PathVariable("id") Integer id) {
        try {
            Passenger passenger = passengerService.findById(id);
            return new ResponseEntity<>(new PassengerDTOnoPassword(passenger), HttpStatus.OK);
        } catch(UserNotFoundException e){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<PassengerDTOnoPassword> updatePassenger(@RequestBody PassengerDTOwithPassword dto, @PathVariable("id") Integer id) {
        try {
            Passenger old = passengerService.findById(id);
            dto.setId(id);
            Passenger updatedPassenger = dtoWithPasswordMapper.fromDTOtoPassenger(dto);
            updatedPassenger = passengerService.update(updatedPassenger);
            PassengerDTOnoPassword updatedPassengerDTO = new PassengerDTOnoPassword(updatedPassenger);
            return new ResponseEntity<>(updatedPassengerDTO, HttpStatus.OK);
        } catch(UserNotFoundException e){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/ride")
    public ResponseEntity<Map<String, Object>> findPassengersRides(@PathVariable("id") Integer id,
                                                                   @RequestParam(defaultValue = "0") Integer page,
                                                                   @RequestParam(defaultValue = "4") Integer size,
                                                                   @RequestParam(defaultValue = "id") String sort,
                                                                   @RequestParam (defaultValue = "2021-10-10T10:00")String from,
                                                                   @RequestParam (defaultValue = "2023-10-10T10:00")String to) {
        try {
            Passenger passenger = passengerService.findById(id);
            Pageable paging = PageRequest.of(page, size, Sort.by(sort));
            Page<Ride> pagedResult = rideService.findRidesByPassengersId(id, from, to, paging);
            List<Ride> rides = rideService.findRidesByPassengersId(id, from, to);
            List<RideRetDTO> ridesDTO = new ArrayList<>();
            for (Ride r : rides) {
                ridesDTO.add(new RideRetDTO(r));
            }
            Map<String, Object> response = new HashMap<>();
            response.put("totalcounts", pagedResult.getTotalElements());
            response.put("results", ridesDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(UserNotFoundException e){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }
}
