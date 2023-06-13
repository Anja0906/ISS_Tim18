package org.tim_18.UberApp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.vehicleDTOs.VehicleDTO;
import org.tim_18.UberApp.dto.vehicleDTOs.VehiclesForMapDTO;
import org.tim_18.UberApp.dto.locationDTOs.LocationDTO;
import org.tim_18.UberApp.exception.RideNotFoundException;
import org.tim_18.UberApp.exception.VehicleNotFoundException;
import org.tim_18.UberApp.model.Driver;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.model.Vehicle;
import org.tim_18.UberApp.service.DriverService;
import org.tim_18.UberApp.service.RideService;
import org.tim_18.UberApp.service.VehicleService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {
    @Autowired
    private final VehicleService vehicleService;
    @Autowired
    private final DriverService driverService;

    @Autowired
    private final RideService rideService;
    public VehicleController(VehicleService vehicleService, DriverService driverService, RideService rideService) {
        this.vehicleService = vehicleService;
        this.driverService = driverService;
        this.rideService = rideService;
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<?> updateVehicleById(
            @PathVariable("id") int id,
            @RequestBody LocationDTO locationDTO) {
        try {
            Vehicle vehicle = vehicleService.findVehicleById(id);
            vehicle.updateLocation(locationDTO);
            Vehicle updateVehicle = vehicleService.updateVehicle(vehicle);
            return new ResponseEntity<>("Coordinates successfully updated", HttpStatus.NO_CONTENT);
        } catch (VehicleNotFoundException e) {
            return new ResponseEntity<>("Vehicle does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public ResponseEntity<VehiclesForMapDTO> getAll() {
        Set<VehicleDTO> inUse = new HashSet<>();
        Set<VehicleDTO> outOfUse = new HashSet<>();
        List<Driver> drivers = driverService.findAllDrivers();
        for (Driver driver:drivers) {
            if(driver.getIsOnline()){
                try{
                    Ride currentRide = rideService.getDriverActiveRide(driver.getId());
                    inUse.add(new VehicleDTO(driver.getVehicle()));
                }catch (RideNotFoundException e){
                    outOfUse.add(new VehicleDTO(driver.getVehicle()));
                }
            }
            else{outOfUse.add(new VehicleDTO(driver.getVehicle()));}
        }
        return new ResponseEntity<>(new VehiclesForMapDTO(inUse, outOfUse), HttpStatus.OK);
    }

}
