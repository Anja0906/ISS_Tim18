package org.tim_18.UberApp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.tim_18.UberApp.model.Passenger;

import java.util.List;
import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Integer> {
    Optional<Passenger> findPassengerById(Integer id);

    Optional<Passenger> findPassengerByEmail(String email);

    List<Passenger> findAll();
    public Page<Passenger> findAll(Pageable pageable);}
