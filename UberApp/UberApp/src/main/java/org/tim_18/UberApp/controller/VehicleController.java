package org.tim_18.UberApp.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.locationDTOs.LocationDTO;
import org.tim_18.UberApp.exception.VehicleNotFoundException;
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

}
