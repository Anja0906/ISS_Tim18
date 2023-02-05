package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.dto.Distance.OsrmResponse;
import org.tim_18.UberApp.dto.Distance.Waypoint;
import org.tim_18.UberApp.exception.VehicleNotFoundException;
import org.tim_18.UberApp.model.Location;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.model.Vehicle;
import org.tim_18.UberApp.repository.VehicleRepository;

import java.util.List;

@Service("vehicleService")
public class VehicleService {
    @Autowired
    private final VehicleRepository vehicleRepository;
    private SimpMessagingTemplate simpMessagingTemplate;

    public VehicleService(VehicleRepository vehicleRepository, SimpMessagingTemplate simpMessagingTemplate) {this.vehicleRepository = vehicleRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }
    public Vehicle addVehicle(Vehicle vehicle) {return vehicleRepository.save(vehicle);}
    public List<Vehicle> findAllVehicles() {return vehicleRepository.findAll();}
    public Vehicle updateVehicle(Vehicle vehicle) {return vehicleRepository.save(vehicle);}
    public Vehicle findVehicleByDriverId(Integer id){return vehicleRepository.findVehicleByDriverId(id);}
    public Vehicle findVehicleById(Integer id) {
        return vehicleRepository.findVehicleById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle by id " + id + " was not found"));
    }
    public void deleteVehicle(Integer id) {vehicleRepository.deleteById(id);}

    public void routeVehicle(OsrmResponse route, Ride ride) throws InterruptedException {
        for (Waypoint waypoint: route.getWaypoints()) {
            Thread.sleep(6000);
            Vehicle vehicle = ride.getDriver().getVehicle();
            vehicle.setCurrentLocation(new Location(waypoint.getLocation().get(1), waypoint.getLocation().get(0), ""));
            vehicle = this.updateVehicle(vehicle);
            System.out.println(vehicle.getCurrentLocation().getLatitude());
            this.simpMessagingTemplate.convertAndSend("/socket-topic/route", vehicle);
        }
    }
}
