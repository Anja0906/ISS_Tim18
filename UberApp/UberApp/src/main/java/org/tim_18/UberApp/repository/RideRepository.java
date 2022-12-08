package org.tim_18.UberApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tim_18.UberApp.model.Ride;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

public interface RideRepository extends JpaRepository<Ride, Integer> {
    public Optional<Ride> findById(Integer id);
    @Query(value = "SELECT * FROM rides rid natural join passenger p where rid.driver_id = ?1 OR p.id = ?1", nativeQuery = true)
    ArrayList<Ride> findRidesForUser(int id);
}
