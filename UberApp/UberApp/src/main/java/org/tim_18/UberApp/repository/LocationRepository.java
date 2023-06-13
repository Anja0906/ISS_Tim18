package org.tim_18.UberApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.tim_18.UberApp.model.Location;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    Optional<Location> findLocationById(Integer id);

    @Modifying
    @Query(value = "DELETE FROM Location loc WHERE loc.id = :id")
    void deleteLocationById(Integer id);

    @Query(value = "SELECT * FROM locations WHERE longitude= ?1 and latitude = ?2 and address = ?3", nativeQuery = true)
    Location findLocationByAdressLongitudeLatitude(Double longitude, Double latitude, String address);

}
