package org.tim_18.UberApp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.model.Driver;
import org.tim_18.UberApp.service.DriverService;

import java.util.List;

@RestController
@RequestMapping("/driver")
public class DriverController {
    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }
    @GetMapping("/all")
    public ResponseEntity<List<Driver>> getAllDrivers () {
        List<Driver> drivers = driverService.findAllDrivers();
        return new ResponseEntity<>(drivers, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Driver> getDriverById (@PathVariable("id") Integer id) {
        Driver driver = driverService.findDriverById(id);
        return new ResponseEntity<>(driver, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Driver> addDriver(@RequestBody Driver userActivation) {
        Driver newUserActivation = driverService.addDriver(userActivation);
        return new ResponseEntity<>(newUserActivation, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Driver> updateDriver(@RequestBody Driver driver) {
        Driver updateDriver = driverService.updateDriver(driver);
        return new ResponseEntity<>(updateDriver, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDriver(@PathVariable("id") Integer id) {
        driverService.deleteDriver(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
