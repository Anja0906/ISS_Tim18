package org.tim_18.UberApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.model.Administrator;
import org.tim_18.UberApp.service.AdministratorService;

import java.util.List;

@RestController
@RequestMapping("/administrator")
public class AdministratorController {
    @Autowired
    private final AdministratorService administratorService;

    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<Administrator>> getAllAdministrators () {
        List<Administrator> administrators = administratorService.findAllAdministrators();
        return new ResponseEntity<>(administrators, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Administrator> getAdministratorById (@PathVariable("id") Integer id) {
        Administrator administrator = administratorService.findAdministratorById(id);
        return new ResponseEntity<>(administrator, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Administrator> addAdministrator(@RequestBody Administrator administrator) {
        Administrator newAdministrator = administratorService.addAdministrator(administrator);
        return new ResponseEntity<>(newAdministrator, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Administrator> updateAdministrator(@RequestBody Administrator administrator) {
        Administrator updateAdministrator = administratorService.updateAdministrator(administrator);
        return new ResponseEntity<>(updateAdministrator, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAdministrator(@PathVariable("id") Integer id) {
        administratorService.deleteAdministrator(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
