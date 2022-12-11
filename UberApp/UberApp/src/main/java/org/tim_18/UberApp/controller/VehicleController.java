package org.tim_18.UberApp.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.VehicleDTOWithoutIds;
import org.tim_18.UberApp.dto.driverDTOs.DriverDTO;
import org.tim_18.UberApp.dto.driverDTOs.DriverDTOWithoutId;
import org.tim_18.UberApp.dto.locationDTOs.LocationDTO;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.model.Driver;
import org.tim_18.UberApp.model.Vehicle;
import org.tim_18.UberApp.service.VehicleService;



@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<DriverDTO> updateVehicleById(
            @PathVariable("id") int id,
            @RequestBody LocationDTO locationDTO) {
        try {
            Vehicle vehicle = vehicleService.findVehicleById(id);
            vehicle.getCurrentLocation().setAddress(locationDTO.getAddress());
            vehicle.getCurrentLocation().setLatitude(locationDTO.getLongitude());
            vehicle.getCurrentLocation().setLongitude(locationDTO.getLongitude());
            Vehicle updateVehicle = vehicleService.updateVehicle(vehicle);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserNotFoundException userNotFoundException) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

}
