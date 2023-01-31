package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.model.VehiclePrice;
import org.tim_18.UberApp.model.VehicleType;
import org.tim_18.UberApp.repository.VehiclePriceRepository;

@Service("vehiclePriceService")
public class VehiclePriceService {

    @Autowired
    private final VehiclePriceRepository repo;

    public VehiclePriceService(VehiclePriceRepository repo) {this.repo = repo;}
    public VehiclePrice findVehiclePriceByVehicleType(VehicleType vehicleType) {
        return repo.findVehiclePriceByVehicleType(vehicleType.toString().toUpperCase().trim());
    }
}
