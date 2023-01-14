package org.tim_18.UberApp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.tim_18.UberApp.model.Driver;

import java.awt.print.Pageable;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Integer> {
    Optional<Driver> findDriverById(Integer id);

    void deleteDriverById(Integer id);
}
