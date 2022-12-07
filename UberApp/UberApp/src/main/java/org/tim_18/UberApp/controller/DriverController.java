package org.tim_18.UberApp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.DocumentDTO;
import org.tim_18.UberApp.dto.DriverDTO;
import org.tim_18.UberApp.dto.FindAllDTO;
import org.tim_18.UberApp.model.Document;
import org.tim_18.UberApp.model.Driver;
import org.tim_18.UberApp.service.DocumentService;
import org.tim_18.UberApp.service.DriverService;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@RestController
@RequestMapping("api/driver")
public class DriverController {
    private final DriverService driverService;
    private final DocumentService documentService;

    public DriverController(DriverService driverService,DocumentService documentService) {
        this.driverService = driverService;
        this.documentService = documentService;
    }

    @GetMapping
    public ResponseEntity<FindAllDTO<DriverDTO>> getDrivers (Pageable pageable) {
        Page<Driver> drivers = driverService.findAll(pageable);
        List<DriverDTO> driversDTO = new ArrayList<>() ;
        for(Driver driver:drivers) {
            driversDTO.add(new DriverDTO(driver));
        }
        FindAllDTO<DriverDTO> driverDTOWithCount= new FindAllDTO<>(driversDTO);
        return new ResponseEntity<>(driverDTOWithCount, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getDriverById (
            @PathVariable("id") int id) {
        Driver driver = driverService.findDriverById(id);
        if(driver != null) {
            DriverDTO driverDTO = new DriverDTO(driver);
            return new ResponseEntity<>(driverDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}/documents")
    public ResponseEntity<HashSet<DocumentDTO>> getDocumentById (
            @PathVariable("id") int id) {
        HashSet<DocumentDTO> documentDTOS = new HashSet<>();
        HashSet<Document> documents = documentService.findByDriverId(id);
        if(documents.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        }else{
            for(Document document:documents){
                DocumentDTO documentDTO = new DocumentDTO(document);
                documentDTOS.add(documentDTO);
            }
            return new ResponseEntity<>(documentDTOS, HttpStatus.OK);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<DriverDTO> updateDriver(
            @PathVariable("id") int id) {
        Driver driver = driverService.findDriverById(id);
        if(driver!=null){
            Driver updateDriver = driverService.updateDriver(driver);
            DriverDTO driverDTO = new DriverDTO(updateDriver);
            return new ResponseEntity<>(driverDTO, HttpStatus.OK);
        }else return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/{id}/documents")
    public ResponseEntity deleteDocumentByDriverId (
            @PathVariable("id") int id) {
        HashSet<Document> documents = documentService.findByDriverId(id);
        if(!documents.isEmpty()) {
            for(Document document:documents){
                documentService.remove(document.getId());
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }
    @PostMapping()
    public ResponseEntity<DriverDTO> addDriver(@RequestBody Driver driver) {
        Driver newDriver = driverService.addDriver(driver);
        Document document = documentService.addDocument(new Document());
        DriverDTO newDriverDTO = new DriverDTO(newDriver);
        return new ResponseEntity<>(newDriverDTO, HttpStatus.CREATED);
    }
    @PostMapping("/{id}/documents")
    public ResponseEntity<DocumentDTO> addDocument(
            @PathVariable("id") int id,
            @RequestBody DocumentDTO documentDTO) {

        Driver driver = driverService.findDriverById(documentDTO.getDriverId());
        if(driver!=null){
            Document document = new Document();
            document.setId(documentDTO.getId());
            document.setName(documentDTO.getName());
            document.setImage(documentDTO.getImage());
            document.setDriver(driver);
            document = documentService.addDocument(document);
            return new ResponseEntity<>(new DocumentDTO(document), HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDriver(@PathVariable("id") Integer id) {
        driverService.deleteDriver(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
