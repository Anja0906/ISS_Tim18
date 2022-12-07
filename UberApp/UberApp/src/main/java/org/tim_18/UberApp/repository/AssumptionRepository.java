package org.tim_18.UberApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tim_18.UberApp.model.Ride;

public interface AssumptionRepository extends JpaRepository<Ride, Integer> {

}