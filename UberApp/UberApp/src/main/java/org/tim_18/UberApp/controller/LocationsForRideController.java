package org.tim_18.UberApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.model.Location;
import org.tim_18.UberApp.model.LocationsForRide;
import org.tim_18.UberApp.service.LocationService;
import org.tim_18.UberApp.service.LocationsForRideService;

import java.util.List;

@RestController
@RequestMapping("/locationsForRide")
public class LocationsForRideController {
    @Autowired
    private final LocationsForRideService locationsForRideService;

    public LocationsForRideController(LocationsForRideService locationsForRideService) {
        this.locationsForRideService = locationsForRideService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<LocationsForRide>> getAllLocationsForRide () {
        List<LocationsForRide> locationsForRide = locationsForRideService.findAllLocationsForRide();
        return new ResponseEntity<>(locationsForRide, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<LocationsForRide> getLocationsForRideById (@PathVariable("id") Integer id) {
        LocationsForRide locationsForRide = locationsForRideService.findLocationsForRideById(id);
        return new ResponseEntity<>(locationsForRide, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<LocationsForRide> addLocationsForRide(@RequestBody LocationsForRide locationsForRide) {
        LocationsForRide newLocationsForRide = locationsForRideService.addLocationsForRide(locationsForRide);
        return new ResponseEntity<>(newLocationsForRide, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<LocationsForRide> updateLocationsForRide(@RequestBody LocationsForRide locationsForRide) {
        LocationsForRide updateLocationsForRide = locationsForRideService.updateLocationsForRide(locationsForRide);
        return new ResponseEntity<>(updateLocationsForRide, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLocationsForRide(@PathVariable("id") Integer id) {
        locationsForRideService.deleteLocationsForRide(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
