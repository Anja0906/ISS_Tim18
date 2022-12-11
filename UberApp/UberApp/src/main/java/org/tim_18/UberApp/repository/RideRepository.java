package org.tim_18.UberApp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tim_18.UberApp.model.Ride;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public interface RideRepository extends JpaRepository<Ride, Integer> {
    public Optional<Ride> findById(Integer id);
    @Query(value = "SELECT * FROM rides rid natural join passenger p where rid.driver_id = ?1 OR p.id = ?1", nativeQuery = true)
    ArrayList<Ride> findRidesForUser(int id);

    Ride findRideById(Integer id);
    @Query(value = "SELECT * FROM rides r INNER JOIN passenger_rides pr on r.id=pr.ride_id WHERE pr.passenger_id = ?1 and date(r.start_time) > ?2 and date(r.start_time) < ?3 and date(r.end_time) >?2 and date(r.end_time) < ?3", nativeQuery = true)
    List<Ride> findRidesByPassengersId(Integer passenger_id, String from, String to);
    @Query(value = "SELECT * FROM rides r INNER JOIN passenger_rides pr on r.id=pr.ride_id WHERE pr.passenger_id = ?1 and date(r.start_time) > ?2 and date(r.start_time) < ?3 and date(r.end_time) >?2 and date(r.end_time) < ?3", nativeQuery = true)
    Page<Ride> findRidesByPassengersId(Integer passenger_id, String from, String to, Pageable pageable);

    @Query(value = "SELECT * FROM rides r WHERE driver_id=?1 and now() between r.start_time and r.end_time", nativeQuery = true)
    Ride findDriverActiveRide(Integer driver_id);
    @Query(value = "SELECT * FROM rides r INNER JOIN passenger_rides pr on r.id=pr.ride_id WHERE pr.passenger_id = ?1 and now() between r.start_time and r.end_time", nativeQuery = true)
    Ride findPassengerActiveRide(Integer passenger_id);

    @Query(value = "SELECT * FROM rides r NATURAL JOIN passenger p WHERE p.id = ?1 or r.driver_id = ?1", nativeQuery = true)
    List<Ride> findRidesForUser(Integer id);
    @Query(value = "SELECT * FROM rides WHERE rides.driver_id = ?1 and DATE(rides.start_time)>?2 and DATE(rides.end_time)<?3", nativeQuery = true)
    Page<Ride> findRidesForDriver(Integer id, String start, String end, Pageable pageable);

}
