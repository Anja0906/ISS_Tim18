package org.tim_18.UberApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tim_18.UberApp.model.LocationsForFavoriteRide;

import java.util.Optional;
import java.util.Set;

public interface LocationsForFavoriteRideRepository extends JpaRepository<LocationsForFavoriteRide, Integer> {

    @Query(value = "SELECT * FROM locations_for_favorite_rides where ride_id = ?1", nativeQuery = true)
    Set<LocationsForFavoriteRide> getLocationsForRideByRideId(Integer id);
}
