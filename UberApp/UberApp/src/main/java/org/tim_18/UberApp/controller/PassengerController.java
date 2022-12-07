package org.tim_18.UberApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.FindAllDTO;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerDTOnoPassword;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerDTOwithPassword;
import org.tim_18.UberApp.mapper.passengerDTOmappers.PassengerDTOnoPasswordMapper;
import org.tim_18.UberApp.mapper.passengerDTOmappers.PassengerDTOwithPasswordMapper;
import org.tim_18.UberApp.model.Passenger;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.service.PassengerService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/passenger")
public class PassengerController {
    private final PassengerService passengerService;
    @Autowired
    private PassengerDTOwithPasswordMapper dtoWithPasswordMapper;
    @Autowired
    private PassengerDTOnoPasswordMapper dtoNoPasswordMapper;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }

    @PostMapping()
    public ResponseEntity<PassengerDTOnoPassword> addPassenger(@RequestBody PassengerDTOwithPassword userActivationDTO) {
        PassengerDTOwithPassword dto = passengerService.save(userActivationDTO);
        Passenger passenger = dtoWithPasswordMapper.fromDTOtoPassenger(dto);
        PassengerDTOnoPassword retDto = dtoNoPasswordMapper.fromPassengerToDTO(passenger);
        return new ResponseEntity<>(retDto, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<FindAllDTO<PassengerDTOnoPassword>> findAll() {
        List<Passenger> passengers = passengerService.findAll();
        List<PassengerDTOnoPassword> passengersDTO = new ArrayList<>() ;
        for (Passenger p : passengers) {
            passengersDTO.add(new PassengerDTOnoPassword(p));
        }
        FindAllDTO<PassengerDTOnoPassword> retDTO = new FindAllDTO<PassengerDTOnoPassword>(passengersDTO);
        return new ResponseEntity<>(retDTO, HttpStatus.OK);
    }
    

    @GetMapping("activate/{activationId}")
    public HttpStatus activateUser(@PathVariable("activationId") Integer id){
        //return new ResponseEntity<>(new UserActivationDTO(), HttpStatus.OK);
        return HttpStatus.OK;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerDTOnoPassword> findById(@PathVariable("id") Integer id) {
        Passenger passenger = passengerService.findById(id);
        return new ResponseEntity<>(new PassengerDTOnoPassword(passenger), HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<PassengerDTOnoPassword> updatePassenger(@RequestBody PassengerDTOwithPassword dto) {
        PassengerDTOwithPassword updatedPassengerDTO = passengerService.save(dto);
        Passenger updatedPassenger = dtoWithPasswordMapper.fromDTOtoPassenger(updatedPassengerDTO);
        PassengerDTOnoPassword updatedPassengerDTO2 = new PassengerDTOnoPassword(updatedPassenger);
        return new ResponseEntity<>(updatedPassengerDTO2, HttpStatus.OK);
    }

    @GetMapping("/{id}/ride")
    public ResponseEntity<FindAllDTO<Ride>> findPassengersRides(@PathVariable("id") Integer id) {
        Passenger passenger = passengerService.findById(id);
        Set<Ride> rideSet = passenger.getRides();
//        ArrayList<Ride> rideArray
//                = (ArrayList<Ride>) rideSet.stream().toList();
        FindAllDTO<Ride> ridesDTO = new FindAllDTO<>(rideSet);
        return new ResponseEntity<>(ridesDTO, HttpStatus.OK);
    }
}
