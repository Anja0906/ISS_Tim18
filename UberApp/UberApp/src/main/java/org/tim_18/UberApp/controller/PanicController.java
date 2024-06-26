package org.tim_18.UberApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.panicDTOs.PanicDTO;
import org.tim_18.UberApp.exception.PanicNotFoundException;
import org.tim_18.UberApp.model.LocationsForRide;
import org.tim_18.UberApp.model.Panic;
import org.tim_18.UberApp.service.LocationsForRideService;
import org.tim_18.UberApp.service.PanicService;

import java.util.*;

@RestController
@RequestMapping("api/panic")
@CrossOrigin( value = "*")
public class PanicController {

    @Autowired
    private final PanicService panicService;

    private final LocationsForRideService locationsForRideService;

    public PanicController(PanicService service, LocationsForRideService locationsForRideService) {
        this.panicService = service;
        this.locationsForRideService = locationsForRideService;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Map<String, Object>> findAllPanics(@RequestParam(defaultValue = "0") Integer page,
                                                             @RequestParam(defaultValue = "4") Integer size) {
        try {
            Pageable paging = PageRequest.of(page, size);
            Page<Panic> panics = panicService.findAll(paging);
            List<PanicDTO> panicDTOs = getPanicsDTO(panics);

            Map<String, Object> response = new HashMap<>();
            response.put("totalCount", panics.getTotalElements());
            response.put("results", panicDTOs);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(PanicNotFoundException panicNotFoundException){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    private List<PanicDTO> getPanicsDTO(Page<Panic> panics) {
        List<PanicDTO> panicDTOs = new ArrayList<>();
        for (Panic panic : panics) {
            panicDTOs.add(new PanicDTO(panic, getLocationsByRideId(panic.getRide().getId())));
        }
        return panicDTOs;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PanicDTO> getUser (
            @PathVariable("id") int id) {
        Panic panic = panicService.findById(id);
        System.out.println(panic);
        PanicDTO panicDTO = new PanicDTO(panic, getLocationsByRideId(panic.getRide().getId()));
        return new ResponseEntity<>(panicDTO, HttpStatus.OK);
    }

    private Set<LocationsForRide> getLocationsByRideId(Integer id) {
        return locationsForRideService.getByRideId(id);
    }
}
