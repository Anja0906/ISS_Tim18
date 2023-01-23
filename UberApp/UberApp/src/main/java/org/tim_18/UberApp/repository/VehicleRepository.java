package org.tim_18.UberApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tim_18.UberApp.model.Vehicle;

import java.util.Optional;


public interface VehicleRepository extends JpaRepository<Vehicle,Integer> {
     Optional<Vehicle> findVehicleById(Integer id) ;
     @Query(value = "SELECT * FROM vehicle WHERE driver_id = ?1", nativeQuery = true)
     Vehicle findVehicleByDriverId(Integer id);
}
