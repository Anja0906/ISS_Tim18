package org.tim_18.UberApp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.model.Document;
import org.tim_18.UberApp.model.Driver;
import org.tim_18.UberApp.service.DriverService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/driver")
public class DriverController {
    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }
    @GetMapping("")
    public ResponseEntity<List<Driver>> getAllDrivers () {
        List<Driver> drivers = driverService.findAllDrivers();
        return new ResponseEntity<>(drivers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Driver> getDriverById (@PathVariable("id") Integer id) {
        Driver driver = driverService.findDriverById(id);
        driver.setDocuments(driverService.findByDriverId(id));
        return new ResponseEntity<>(driver, HttpStatus.OK);
    }

    @GetMapping("/{id}/documents")
    public ResponseEntity<Set<Document>> getDriversDocuments (@PathVariable("id") Integer id) {
        Driver driver = driverService.findDriverById(id);
        Set<Document> documents = driver.getDocuments();
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/documents")
    public ResponseEntity<?> deleteDriver(@PathVariable("id") Integer id) {
        driverService.deleteDriversDocuments(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @PostMapping("/{id}/documents")
//    public ResponseEntity<Document> addDocuments(@PathVariable("id") Integer id, @RequestBody Document document) {
//        Driver driver = driverService.findDriverById(id);
//
//        return new ResponseEntity<>(newUserActivation, HttpStatus.CREATED);
//    }

    @PostMapping("")
    public ResponseEntity<Driver> addDriver(@RequestBody Driver userActivation) {
        Driver newUserActivation = driverService.addDriver(userActivation);
        return new ResponseEntity<>(newUserActivation, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Driver> updateDriver(@RequestBody Driver driver) {
        Driver updateDriver = driverService.updateDriver(driver);
        return new ResponseEntity<>(updateDriver, HttpStatus.OK);
    }





}
