package org.tim_18.UberApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tim_18.UberApp.model.Administrator;
import org.tim_18.UberApp.model.Location;

import java.util.Optional;

public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {
    Optional<Administrator> findAdministratorById(Integer id);
    void deleteAdministratorById(Integer id);
}
