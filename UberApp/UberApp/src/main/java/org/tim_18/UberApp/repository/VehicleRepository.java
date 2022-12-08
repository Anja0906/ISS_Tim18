package org.tim_18.UberApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tim_18.UberApp.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

}
