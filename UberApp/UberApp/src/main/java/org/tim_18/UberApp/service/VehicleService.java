package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.exception.VehicleNotFoundException;
import org.tim_18.UberApp.model.Vehicle;
import org.tim_18.UberApp.repository.VehicleRepository;

import java.util.List;

@Service("vehicleService")
public class VehicleService {
    @Autowired
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {this.vehicleRepository = vehicleRepository;}
    public Vehicle addVehicle(Vehicle vehicle) {return vehicleRepository.save(vehicle);}
    public List<Vehicle> findAllVehicles() {return vehicleRepository.findAll();}
    public Vehicle updateVehicle(Vehicle vehicle) {return vehicleRepository.save(vehicle);}
    public Vehicle findVehicleByDriverId(Integer id){return vehicleRepository.findVehicleByDriverId(id);}
    public Vehicle findVehicleById(Integer id) {
        return vehicleRepository.findVehicleById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle by id " + id + " was not found"));
    }
    public void deleteVehicle(Integer id) {vehicleRepository.deleteById(id);}
}
