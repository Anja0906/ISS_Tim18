package org.tim_18.UberApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.FindAllDTO;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerDTOnoPassword;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerDTOwithPassword;
import org.tim_18.UberApp.mapper.passengerDTOmappers.PassengerDTOwithPasswordMapper;
import org.tim_18.UberApp.model.Passenger;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.service.PassengerService;
import org.tim_18.UberApp.service.RideService;

import java.util.*;

@RestController
@RequestMapping("/api/passenger")
public class PassengerController {
    private final PassengerService passengerService;
    private final RideService rideService;
    @Autowired
    private PassengerDTOwithPasswordMapper dtoWithPasswordMapper;

    public PassengerController(PassengerService passengerService, RideService rideService) {
        this.passengerService = passengerService;
        this.rideService = rideService;
    }

    @PostMapping()
    public ResponseEntity<PassengerDTOnoPassword> addPassenger(@RequestBody PassengerDTOwithPassword dto) {
        Passenger passenger = dtoWithPasswordMapper.fromDTOtoPassenger(dto);
        passenger = passengerService.addPassenger(passenger);
        PassengerDTOnoPassword retDto = new PassengerDTOnoPassword(passenger);
        return new ResponseEntity<>(retDto, HttpStatus.CREATED);
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
        response.put("totalcounts", pagedResult.getTotalElements());
        response.put("results", passengersDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("activate/{activationId}")
    public HttpStatus activateUser(@PathVariable("activationId") Integer id){
        return HttpStatus.OK;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerDTOnoPassword> findById(@PathVariable("id") Integer id) {
        Passenger passenger = passengerService.findById(id);
        return new ResponseEntity<>(new PassengerDTOnoPassword(passenger), HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<PassengerDTOnoPassword> updatePassenger(@RequestBody PassengerDTOwithPassword dto, @PathVariable("id") Integer id) {
        dto.setId(id);
        Passenger updatedPassenger = dtoWithPasswordMapper.fromDTOtoPassenger(dto);
        updatedPassenger = passengerService.update(updatedPassenger);
        PassengerDTOnoPassword updatedPassengerDTO = new PassengerDTOnoPassword(updatedPassenger);
        return new ResponseEntity<>(updatedPassengerDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}/ride")
    public ResponseEntity<FindAllDTO<Ride>> findPassengersRides(@PathVariable("id") Integer id, @RequestParam(defaultValue = "0") Integer page,
                                                                   @RequestParam(defaultValue = "4") Integer size) {
        List<Ride> rides = rideService.findRidesByPassengersId(id);
        FindAllDTO<Ride> ridesDTO = new FindAllDTO<>(rides);
        return new ResponseEntity<>(ridesDTO, HttpStatus.OK);

    }
}
