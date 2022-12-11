package org.tim_18.UberApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
            Page<Panic> pagedResult = panicService.findAll(paging);
            List<Panic> panics = panicService.findAllPanics();
            List<PanicDTO> panicDTOs = new ArrayList<>();
            for (Panic panic : panics) {
                panicDTOs.add(new PanicDTO(panic));
            }
            Map<String, Object> response = new HashMap<>();
            response.put("totalCount", pagedResult.getTotalElements());
            response.put("results", panicDTOs);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(UserNotFoundException e){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }
}
