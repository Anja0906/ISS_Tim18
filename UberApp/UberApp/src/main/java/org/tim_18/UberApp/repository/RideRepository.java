package org.tim_18.UberApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tim_18.UberApp.model.Ride;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public interface RideRepository extends JpaRepository<Ride, Integer> {
    public Optional<Ride> findById(Integer id);

    Ride findRideById(Integer id);
    @Query(value = "SELECT * FROM rides r INNER JOIN passenger_rides pr on r.id=pr.ride_id WHERE pr.passenger_id = ?1", nativeQuery = true)
    List<Ride> findRidesByPassengersId(Integer passenger_id);

    @Query(value = "SELECT * FROM rides r WHERE driver_id=?1 and now() between r.start_time and r.end_time;", nativeQuery = true)
    Ride findDriverActiveRide(Integer driver_id);
    @Query(value = "SELECT * FROM rides r INNER JOIN passenger_rides pr on r.id=pr.ride_id WHERE pr.passenger_id = ?1 and now() between r.start_time and r.end_time;", nativeQuery = true)
    Ride findPassengerActiveRide(Integer passenger_id);
}
