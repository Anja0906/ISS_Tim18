package org.tim_18.UberApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tim_18.UberApp.model.Review;

import java.util.HashSet;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
//    @Query(value = "SELECT * FROM reviews rev INNER JOIN rides rid ON rev.ride_id = rid.id INNER JOIN drivers driv on rid.driver_id = driv.id where driv.vehicle_id = ?1", nativeQuery = true)
    @Query(value = "SELECT * FROM reviews rev NATURAL JOIN rides rid NATURAL JOIN drivers driv where driv.vehicle_id = ?1", nativeQuery = true)
    HashSet<Review> findByVehicleId(Integer id);

    @Query(value = "SELECT * FROM reviews rev NATURAL JOIN rides rid where rid.id = ?1", nativeQuery = true)
    HashSet<Review> findByRideId(int id);

    @Query(value = "SELECT * FROM reviews rev NATURAL JOIN rides rid NATURAL JOIN drivers driv where driv.id = ?1", nativeQuery = true)
    HashSet<Review> findByDriverId(int id);
}
