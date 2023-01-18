package org.tim_18.UberApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tim_18.UberApp.model.VehiclePrice;

public interface VehiclePriceRepository extends JpaRepository<VehiclePrice, Integer> {
    VehiclePrice findVehiclePriceByVehicleType(String vehicleType);
}
