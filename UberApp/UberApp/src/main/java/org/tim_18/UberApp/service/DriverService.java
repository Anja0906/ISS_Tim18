package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.model.Driver;
import org.tim_18.UberApp.model.UserActivation;
import org.tim_18.UberApp.repository.DriverRepository;
import org.tim_18.UberApp.repository.UserActivationRepository;

import java.util.List;
@Service("driverService")

public class DriverService {
    @Autowired
    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public Driver addDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    public List<Driver> findAllDrivers() {
        return driverRepository.findAll();
    }
    public Page<Driver> findAll(Pageable page){
        return driverRepository.findAll(page);
    }

    public Driver updateDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    public Driver findDriverById(Integer id) {
        return driverRepository.findDriverById(id)
                .orElseThrow(() -> new UserNotFoundException("Driver by id " + id + " was not found"));
    }

    public void deleteDriver(Integer id) {
        driverRepository.deleteDriverById(id);
    }
}
