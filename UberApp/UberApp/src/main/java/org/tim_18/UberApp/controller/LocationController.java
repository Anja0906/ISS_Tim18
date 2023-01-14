package org.tim_18.UberApp.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.model.Location;
import org.tim_18.UberApp.service.LocationService;

import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {
    @Autowired
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<Location>> getAllLocations () {
        List<Location> locations = locationService.findAllLocations();
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Location> getLocationById (@PathVariable("id") Integer id) {
        Location location = locationService.findLocationById(id);
        return new ResponseEntity<>(location, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Location> addLocation(@RequestBody Location location) {
        Location newLocation = locationService.addLocation(location);
        return new ResponseEntity<>(newLocation, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Location> updateLocation(@RequestBody Location location) {
        Location updateLocation = locationService.updateLocation(location);
        return new ResponseEntity<>(updateLocation, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable("id") Integer id) {
        locationService.deleteLocation(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
