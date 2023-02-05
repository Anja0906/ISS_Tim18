package org.tim_18.UberApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tim_18.UberApp.model.Location;
import org.tim_18.UberApp.model.LocationsForRide;

import java.util.Optional;
import java.util.Set;

public interface LocationsForRideRepository extends JpaRepository<LocationsForRide, Integer> {
    Optional<LocationsForRide> findLocationsForRideById(Integer id);
    void deleteLocationsForRideById(Integer id);

    @Query(value = "SELECT * FROM locations_for_rides where ride_id = ?1", nativeQuery = true)
    Set<LocationsForRide> getLocationsForRideByRideId(Integer id);
}
