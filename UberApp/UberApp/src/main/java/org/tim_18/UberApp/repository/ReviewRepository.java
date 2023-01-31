package org.tim_18.UberApp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tim_18.UberApp.model.Review;

import java.util.ArrayList;
import java.util.HashSet;


public interface ReviewRepository extends JpaRepository<Review, Integer> {
    //    public Page<Review> findAll(Pageable pageable);
    @Query(value = "SELECT rev.id as id, comment, is_driver, rating, passenger_id, ride_id, r.id as rideid, d.id as driverid FROM reviews rev INNER JOIN rides r ON rev.ride_id=r.id INNER JOIN drivers d ON r.driver_id=d.id where vehicle_id=?1 and rev.is_driver = false", nativeQuery = true)
    Page<Review> findByVehicleId(int id, Pageable pageable);

    //    rev NATURAL JOIN rides rid NATURAL JOIN passenger p where rev.passenger_id = p.id and rid.id = ?1
    @Query(value = "SELECT * FROM reviews rev where rev.ride_id = ?1 ", nativeQuery = true)
    HashSet<Review> findByRideIdHash(Integer id);

    @Query(value = "SELECT * FROM reviews rev NATURAL JOIN rides rid where rid.id = ?1", nativeQuery = true)
    Page<Review> findByRideId(int id, Pageable pageable);
    @Query(value = "SELECT * FROM reviews rev NATURAL JOIN rides rid  where rid.driver_id=?1 and rev.is_driver = true", nativeQuery = true)
    Page<Review> findByDriverId(int id, Pageable pageable);

    @Query(value = "SELECT rev.id as id, comment, is_driver, rating, passenger_id, ride_id, r.id as rideid, d.id as driverid FROM reviews rev INNER JOIN rides r ON rev.ride_id=r.id INNER JOIN drivers d ON r.driver_id=d.id where vehicle_id=?1 and passenger_id=?2 and rev.is_driver = false", nativeQuery = true)
    Page<Review> findByVehicleAndPassengerId(int vehicleId, int passengerId, Pageable pageable);

    @Query(value = "SELECT rev.id as id, comment, is_driver, rating, passenger_id, ride_id, r.id as rideid, d.id as driverid FROM reviews rev INNER JOIN rides r ON rev.ride_id=r.id INNER JOIN drivers d ON r.driver_id=d.id where r.driver_id=?1 and passenger_id=?2 and rev.is_driver = true", nativeQuery = true)
    Page<Review> findByDriverAndPassengerId(int driverId, int passengerId, Pageable pageable);

    @Query(value = "SELECT * FROM reviews rev where rev.ride_id=?1 and rev.passenger_id=?2 and rev.is_driver=false", nativeQuery = true)
    Review findByRideAndPassengerIdForVehicle(int rideId, int passengerId);

    @Query(value = "SELECT * FROM reviews rev where rev.ride_id=?1 and rev.passenger_id=?2 and rev.is_driver=true", nativeQuery = true)
    Review findByRideAndPassengerIdForDriver(int rideId, int passengerId);

}