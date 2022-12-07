package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.model.Document;
import org.tim_18.UberApp.model.Driver;
import org.tim_18.UberApp.repository.DocumentRepository;
import org.tim_18.UberApp.repository.DriverRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service("driverService")

public class DriverService {
    @Autowired
    private final DriverRepository driverRepository;

    @Autowired
    private final DocumentRepository documentRepository;

    public DriverService(DriverRepository driverRepository, DocumentRepository documentRepository) {
        this.driverRepository = driverRepository;
        this.documentRepository = documentRepository;
    }

    public Driver addDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    public List<Driver> findAllDrivers() {
        return driverRepository.findAll();
    }

    public Driver updateDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    public Driver findDriverById(Integer id) {
        return driverRepository.findDriverById(id)
                .orElseThrow(() -> new UserNotFoundException("Driver by id " + id + " was not found"));
    }

    public HashSet<Document> findByDriverId(Integer id){
        return documentRepository.findByDriverId(id);
    }
    public void deleteDriver(Integer id) {
        driverRepository.deleteDriverById(id);
    }
    public void deleteDriversDocuments(Integer id) {
        //proveriti da li je ovo ispravno
        Optional<Driver> driver = driverRepository.findDriverById(id);
        Set<Document> documents = driver.get().getDocuments();
        for (Document d:documents) {
            documentRepository.deleteDocumentById(d.getId());
        }
        driver.get().setDocuments(new HashSet<>());
    }
}
