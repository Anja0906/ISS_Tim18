package org.tim_18.UberApp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.tim_18.UberApp.model.Driver;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Integer> {
    Optional<Driver> findDriverById(Integer id);

    @Modifying
    @Query(value = "DELETE FROM Driver driver WHERE driver.id = :id")
    void deleteDriverById(Integer id);

    @Query(value = "SELECT * from drivers d join users u where d.id=u.id AND d.is_online=true AND u.active=true",nativeQuery = true)
    ArrayList<Driver> findDriversByStatus();
}
