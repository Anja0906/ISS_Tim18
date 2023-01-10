package org.tim_18.UberApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerDTOnoPassword;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerDTOwithPassword;
import org.tim_18.UberApp.dto.rideDTOs.RideRetDTO;
import org.tim_18.UberApp.exception.PassengerNotFoundException;
import org.tim_18.UberApp.exception.UserActivationNotFoundException;
import org.tim_18.UberApp.mapper.passengerDTOmappers.PassengerDTOwithPasswordMapper;
import org.tim_18.UberApp.model.Passenger;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.model.Role;
import org.tim_18.UberApp.model.UserActivation;
import org.tim_18.UberApp.service.PassengerService;
import org.tim_18.UberApp.service.RideService;
import org.tim_18.UberApp.service.RoleService;
import org.tim_18.UberApp.service.UserActivationService;

import java.util.*;

@RestController
@RequestMapping("/api/passenger")
@CrossOrigin(origins = "http://localhost:4200")
//@PreAuthorize("hasRole('PASSENGER')")
public class PassengerController {
    private final PassengerService passengerService;
    private final RideService rideService;
    private final UserActivationService userActivationService;
    private final RoleService roleService;
    @Autowired
    private PassengerDTOwithPasswordMapper dtoWithPasswordMapper;

    public PassengerController(PassengerService passengerService, RideService rideService, UserActivationService userActivationService, RoleService roleService) {
        this.passengerService       = passengerService;
        this.rideService            = rideService;
        this.userActivationService  = userActivationService;
        this.roleService = roleService;
    }

    @PostMapping()
    public ResponseEntity<PassengerDTOnoPassword> addPassenger(@RequestBody PassengerDTOwithPassword dto) {
        Passenger passenger = dtoWithPasswordMapper.fromDTOtoPassenger(dto);
        passenger.setRoles(this.getRoles());
        passenger = passengerService.addPassenger(passenger);
        return new ResponseEntity<>(new PassengerDTOnoPassword(passenger), HttpStatus.OK);
    }

    private List<Role> getRoles() {
        List<Role> rP = roleService.findByName("ROLE_PASSENGER");
        List<Role> rU = roleService.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        roles.add(rU.get(0));
        roles.add(rP.get(0));
        return roles;
    }

    @GetMapping()
    public ResponseEntity<Map<String, Object>> findAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Passenger> passengers = passengerService.findAll(paging);
        List<PassengerDTOnoPassword> passengersDTO = PassengerDTOnoPassword.getPassengersDTO(passengers);

        Map<String, Object> response = new HashMap<>();
        response.put("totalCount", passengers.getTotalElements());
        response.put("results", passengersDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("activate/{activationId}")
    public ResponseEntity activateUser(@PathVariable("activationId") Integer id){
        try {
            UserActivation ua = userActivationService.findUserActivationById(id);
            ua.getUser().setActive(true);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(UserActivationNotFoundException userActivationNotFoundException){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<PassengerDTOnoPassword> findById(@PathVariable("id") Integer id) {
        try {
            Passenger passenger = passengerService.findById(id);
            return new ResponseEntity<>(new PassengerDTOnoPassword(passenger), HttpStatus.OK);
        } catch(PassengerNotFoundException passengerNotFoundException){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<PassengerDTOnoPassword> updatePassenger(
            @RequestBody PassengerDTOwithPassword dto,
            @PathVariable("id") Integer id) {
        try {
            Passenger oldPassenger = passengerService.findById(id); //throws 404
            oldPassenger = dtoWithPasswordMapper.fromDTOtoPassenger(dto, id);
            Passenger updatedPassenger = passengerService.update(oldPassenger);
            PassengerDTOnoPassword updatedPassengerDTO = new PassengerDTOnoPassword(updatedPassenger);
            return new ResponseEntity<>(updatedPassengerDTO, HttpStatus.OK);
        } catch(PassengerNotFoundException passengerNotFoundException){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/ride")
    public ResponseEntity<Map<String, Object>> findPassengersRides(
            @PathVariable("id") Integer id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam (defaultValue = "2021-10-10T10:00")String from,
            @RequestParam (defaultValue = "2023-10-10T10:00")String to) {
        try {
            Passenger passenger = passengerService.findById(id); //throws 404

            Pageable paging = PageRequest.of(page, size, Sort.by(sort));
            Page<Ride> rides = rideService.findRidesByPassengersId(id, from, to, paging);
            List<RideRetDTO> ridesDTO = RideRetDTO.getRidesDTO(rides);

            Map<String, Object> response = new HashMap<>();
            response.put("totalCount", rides.getTotalElements());
            response.put("results", ridesDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(PassengerNotFoundException passengerNotFoundException){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }
}
