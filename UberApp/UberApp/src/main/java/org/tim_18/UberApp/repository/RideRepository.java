package org.tim_18.UberApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tim_18.UberApp.model.Ride;

import java.util.Optional;

public interface RideRepository extends JpaRepository<Ride, Integer> {
    public Optional<Ride> findById(Integer id);
}
