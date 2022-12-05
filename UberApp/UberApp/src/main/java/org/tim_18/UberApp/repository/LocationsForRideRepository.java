package org.tim_18.UberApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tim_18.UberApp.model.Location;
import org.tim_18.UberApp.model.LocationsForRide;

import java.util.Optional;

public interface LocationsForRideRepository extends JpaRepository<LocationsForRide, Integer> {
    Optional<LocationsForRide> findLocationsForRideById(Integer id);
    void deleteLocationsForRideById(Integer id);
}
