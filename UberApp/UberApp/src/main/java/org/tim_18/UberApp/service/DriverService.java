package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.dto.rideDTOs.RideRecDTO;
import org.tim_18.UberApp.exception.DriverNotFoundException;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.model.Driver;
import org.tim_18.UberApp.model.UserActivation;
import org.tim_18.UberApp.model.Vehicle;
import org.tim_18.UberApp.repository.DriverRepository;
import org.tim_18.UberApp.repository.UserActivationRepository;

import java.util.ArrayList;
import java.util.List;
@Service("driverService")

public class DriverService {
    @Autowired
    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {this.driverRepository = driverRepository;}
    public Driver addDriver(Driver driver) {return driverRepository.save(driver);}
    public List<Driver> findAllDrivers() {return driverRepository.findAll();}
    public Page<Driver> findAllDriversPage(Pageable page){return driverRepository.findAll(page);}
    public Driver updateDriver(Driver driver) {return driverRepository.save(driver);}
    public Driver findDriverById(Integer id) {
        return driverRepository.findDriverById(id)
                .orElseThrow(() -> new DriverNotFoundException("Driver by id " + id + " was not found"));
    }

    public ArrayList<Driver> findDriverByStatus(){
        return driverRepository.findDriversByStatus();
    }
    public void deleteDriver(Integer id) {driverRepository.deleteDriverById(id);}

    public ArrayList<Driver> filterDriversByVehicleSpec(ArrayList<Driver> activeDrivers, RideRecDTO ride){
        ArrayList<Driver> potentialDriversCarEdition = new ArrayList<>();
        for (Driver driver : activeDrivers) {
            Vehicle vehicle = driver.getVehicle();
            System.out.println(vehicle.getVehicleType() + "     "+ride.getVehicleType());
            System.out.println(vehicle.getPassengerSeats() + "  =>   "+ride.getPassengers().size());
            System.out.println(vehicle.getBabyTransport() + "     "+ride.isBabyTransport());
            System.out.println(vehicle.getPetTransport() + "     "+ride.isPetTransport());
            if ((vehicle.getVehicleType().equals(ride.getVehicleType())) && (vehicle.getPassengerSeats() >= ride.getPassengers().size())
                    && (ride.isBabyTransport() && vehicle.getBabyTransport()) && (ride.isPetTransport() && vehicle.getPetTransport()))
                potentialDriversCarEdition.add(driver);
        }
        return potentialDriversCarEdition;
    }
}
