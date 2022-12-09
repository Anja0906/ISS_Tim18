package org.tim_18.UberApp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.rideDTOs.RideDTOWithPanic;
import org.tim_18.UberApp.dto.rideDTOs.RideRecDTO;
import org.tim_18.UberApp.dto.rideDTOs.RideRetDTO;
import org.tim_18.UberApp.mapper.rideDTOmappers.RideRecDTOMapper;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.service.RideService;

@RestController
@RequestMapping("/api/ride")
public class RideController {


    private final RideService rideService;

    private RideRecDTOMapper dtoRecMapper;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping
    public ResponseEntity<RideRetDTO> createARide(@RequestBody RideRecDTO oldDTO){
        RideRetDTO dto = rideService.save(oldDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/driver/{driverId}/active")
    public RideRetDTO getDriverActiveRide(@PathVariable("driverId") Integer driverId) {
        return null;
    }

    @GetMapping("/passenger/{passengerId}/active")
    public RideRetDTO getPassengerActiveRide(@PathVariable("passengerId") Integer passengerId) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideRetDTO> findRideById(@PathVariable("id") Integer id) {
        Ride ride = rideService.findRideById(id);
        return new ResponseEntity<>(new RideRetDTO(ride), HttpStatus.OK);

    }

    @PutMapping("/{id}/withdraw")
    public RideRetDTO cancelRide(@PathVariable("id") Integer id) {
        return null;
    }

    @PutMapping("/{id}/panic")
    public RideDTOWithPanic activatePanic(@PathVariable("id") Integer id, @RequestBody String reason){
        return null;
    }

    @PutMapping("/{id}/accept")
    public RideRetDTO acceptRide(@PathVariable("id")Integer id) {
        return null;
    }

    @PutMapping("/{id}/end")
    public RideRetDTO endRide(@PathVariable("id")Integer id) {
        return null;
    }

    @PutMapping("/{id}/cancel")
    public RideRetDTO cancelRide(@PathVariable("id") Integer id,  @RequestBody String reason){
        return null;
    }
}
