package org.tim_18.UberApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.AssumptionDTO;
import org.tim_18.UberApp.model.Document;
import org.tim_18.UberApp.model.Driver;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.service.AssumptionService;

import java.util.List;

@RestController
@RequestMapping("/api/unregisteredUser/")
public class AssumptionController {
    @Autowired
    private final AssumptionService assumptionService;

    public AssumptionController(AssumptionService assumptionService) {
        this.assumptionService = assumptionService;
    }

    @PostMapping("")
    public ResponseEntity<AssumptionDTO> rideAssumption (@RequestBody Ride ride) {
        Ride calculatedRide = assumptionService.calculatePrice(ride);
        AssumptionDTO assumptionDTO = new AssumptionDTO(10, 450);
        return new ResponseEntity<>(assumptionDTO, HttpStatus.OK);
    }
}