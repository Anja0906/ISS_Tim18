package org.tim_18.UberApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tim_18.UberApp.dto.FindAllDTO;
import org.tim_18.UberApp.model.Panic;
import org.tim_18.UberApp.service.PanicService;

import java.util.List;

@RestController
@RequestMapping("api/panic")
public class PanicController {

    @Autowired
    private final PanicService service;

    public PanicController(PanicService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<FindAllDTO<Panic>> findAllPanics() {
        List<Panic> panics = service.findAllPanics();
        FindAllDTO<Panic> panicsDTO = new FindAllDTO<>(panics);
        return new ResponseEntity<>(panicsDTO, HttpStatus.OK);
    }
}
