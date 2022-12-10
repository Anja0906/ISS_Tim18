package org.tim_18.UberApp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.*;
import org.tim_18.UberApp.dto.driverDTOs.DriverDTO;
import org.tim_18.UberApp.dto.driverDTOs.DriverDTOWithoutId;
import org.tim_18.UberApp.model.Document;
import org.tim_18.UberApp.model.Driver;
import org.tim_18.UberApp.model.Vehicle;
import org.tim_18.UberApp.service.DocumentService;
import org.tim_18.UberApp.service.DriverService;
import org.tim_18.UberApp.service.VehicleService;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@RestController
@RequestMapping("api/driver")
public class DriverController {
    private final DriverService driverService;
    private final DocumentService documentService;
    private final VehicleService vehicleService;


    public DriverController(DriverService driverService,DocumentService documentService,VehicleService vehicleService) {
        this.driverService   = driverService;
        this.documentService = documentService;
        this.vehicleService  = vehicleService;
    }

    @GetMapping
    public ResponseEntity<FindAllDTO<DriverDTO>> getDrivers (@RequestParam(defaultValue = "0") Integer page,
                                                             @RequestParam(defaultValue = "4") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
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
    @GetMapping("/{id}/vehicle")
    public ResponseEntity<VehicleDTO> getVehicleById (
            @PathVariable("id") int id) {
        Driver driver = driverService.findDriverById(id);
        Vehicle vehicle = vehicleService.findVehicleByDriverId(driver.getId());
        if(vehicle == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(new VehicleDTO(vehicle), HttpStatus.OK);
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
    public ResponseEntity<DriverDTO> addDriver(@RequestBody DriverDTOWithoutId driverDTOWithoutId) {
        Driver driver = new Driver();
        driver.setName(driverDTOWithoutId.getName());
        driver.setSurname(driverDTOWithoutId.getSurname());
        driver.setProfilePicture(driverDTOWithoutId.getProfilePicture());
        driver.setTelephoneNumber(driverDTOWithoutId.getTelephoneNumber());
        driver.setEmail(driverDTOWithoutId.getEmail());
        driver.setAddress(driverDTOWithoutId.getAddress());
        driver.setPassword(driverDTOWithoutId.getPassword());
        driver.setBlocked(false);
        driver.setActive(false);
        driverService.addDriver(driver);
        return new ResponseEntity<>(new DriverDTO(driver), HttpStatus.CREATED);
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
