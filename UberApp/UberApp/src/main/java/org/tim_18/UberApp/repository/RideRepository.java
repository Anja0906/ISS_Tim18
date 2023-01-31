package org.tim_18.UberApp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.model.VehicleType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface RideRepository extends JpaRepository<Ride, Integer> {
    @Query(value = "SELECT * FROM rides r INNER JOIN locations_rides lr ON  r.id=lr.rid_id WHERE r.id=?1", nativeQuery = true)
    public Optional<Ride> findById(Integer id);
//    @Query(value = "SELECT * FROM rides rid natural join passenger p where rid.driver_id = ?1 OR p.id = ?1", nativeQuery = true)
//    ArrayList<Ride> findRidesForUser(int id);

    Optional<Ride> findRideById(Integer id);
    @Query(value = "SELECT * FROM rides r INNER JOIN passenger_rides pr on r.id=pr.ride_id WHERE pr.passenger_id = ?1 and date(r.start_time) > ?2 and date(r.start_time) < ?3 and date(r.end_time) >?2 and date(r.end_time) < ?3", nativeQuery = true)
    List<Ride> findRidesByPassengersId(Integer passenger_id, String from, String to);

    @Query(value = "SELECT * FROM rides INNER JOIN passenger_rides on rides.id=passenger_rides.ride_id WHERE passenger_id = ?1 and date(start_time) > ?2 and date(start_time) < ?3 and date(end_time) > ?2 and date(end_time) < ?3", nativeQuery = true)
    Page<Ride> findRidesByPassengersId(Integer passenger_id, String from, String to, Pageable pageable);

    @Query(value = "SELECT * FROM rides r WHERE driver_id=?1 and now() between r.start_time and r.end_time", nativeQuery = true)
    Optional<Ride> findDriverActiveRide(Integer driver_id);

    @Query(value = "SELECT * FROM rides r WHERE driver_id=?1 and (r.status = ?2 or r.status = ?3)", nativeQuery = true)
    List<Ride> findDriverAcceptedRides(Integer driverId, String status, String otherStatus);
    @Query(value = "SELECT * FROM rides r INNER JOIN passenger_rides pr on r.id=pr.ride_id WHERE pr.passenger_id = ?1 and (now() between date_sub(start_time, INTERVAL estimated_time_in_minutes minute) and start_time or now() between start_time and end_time) and (r.status=1 or r.status=2)", nativeQuery = true)
    Optional<Ride> findPassengerActiveRide(Integer passenger_id, String status);

    @Query(value = "SELECT * FROM rides r INNER JOIN passenger_rides pr on r.id=pr.ride_id WHERE pr.passenger_id = ?1 or r.driver_id = ?1", nativeQuery = true)
    Page<Ride> findRidesForUserPage(Integer id, Pageable pageable);

    @Query(value = "SELECT * FROM rides r INNER JOIN passenger_rides pr on r.id=pr.ride_id WHERE pr.passenger_id = ?1 or r.driver_id = ?1", nativeQuery = true)
    List<Ride> findRidesForUser(Integer id);

    @Query(value = "SELECT * FROM rides r INNER JOIN passenger_rides pr on r.id=pr.ride_id WHERE (pr.passenger_id = ?1 or r.driver_id = ?1) and (date(r.start_time) > ?2 and date(r.start_time) < ?3 and date(r.end_time) >?2 and date(r.end_time) < ?3)", nativeQuery = true)
    List<Ride> findRidesForUser(Integer id, String from, String to);

    @Query(value = "SELECT * FROM rides WHERE rides.driver_id = ?1 and DATE(rides.start_time)>?2 and DATE(rides.end_time)<?3", nativeQuery = true)
    Page<Ride> findRidesForDriver(Integer id, String start, String end, Pageable pageable);

    @Query(value = "SELECT * FROM rides WHERE rides.status = ?1 ", nativeQuery = true)
    Page<Ride> findPendingRidesByStatus(String status,Pageable pageable);


    @Query(value = "SELECT * FROM rides r INNER JOIN passenger_rides pr on r.id=pr.ride_id WHERE pr.passenger_id = ?1 and r.status = ?2", nativeQuery = true)
    ArrayList<Ride> findPassengersRidesByStatus(Integer id, String status);

    @Query(value = "SELECT * FROM rides WHERE rides.driver_id = ?1 and DATE(rides.start_time)>?3 and rides.status=?2", nativeQuery = true)
    List<Ride> findRidesForDriverByStatus(Integer id, String status, String now);

    @Query(value = "select * from rides where scheduled_time IS NOT NULL and driver_id IS NULL and estimated_time_in_minutes=-1 and pet_transport = ?4 and baby_transport = ?3 and vehicle_type = ?2 and scheduled_time BETWEEN now() and date_add(now(), INTERVAL ?1 minute)", nativeQuery = true)
    List<Ride> findScheduledRides(double time, String vehicleType, boolean babyTransport, boolean petTransport);

    @Query(value = "select * from rides where scheduled_time IS NOT NULL and driver_id IS NULL and estimated_time_in_minutes=-1 and scheduled_time BETWEEN now() and date_add(now(), INTERVAL ?1 minute)", nativeQuery = true)
    List<Ride> findScheduledRides(double time);
}
