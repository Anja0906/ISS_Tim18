package org.tim_18.UberApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.FindAllDTO;
import org.tim_18.UberApp.dto.PanicDTO;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.model.Panic;
import org.tim_18.UberApp.service.PanicService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/panic")
@CrossOrigin(origins = "http://localhost:4200")
public class PanicController {

    @Autowired
    private final PanicService panicService;

    public PanicController(PanicService service) {
        this.panicService = service;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> findAllPanics(@RequestParam(defaultValue = "0") Integer page,
                                                             @RequestParam(defaultValue = "4") Integer size) {
        try {
            Pageable paging = PageRequest.of(page, size);
            Page<Panic> panics = panicService.findAll(paging);
            List<PanicDTO> panicDTOs = PanicDTO.getPanicsDTO(panics);

            Map<String, Object> response = new HashMap<>();
            response.put("totalCount", panics.getTotalElements());
            response.put("results", panicDTOs);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(UserNotFoundException e){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }
}
