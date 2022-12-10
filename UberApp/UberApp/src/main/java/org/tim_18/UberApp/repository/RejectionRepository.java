package org.tim_18.UberApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tim_18.UberApp.model.Rejection;

import java.util.Optional;

public interface RejectionRepository extends JpaRepository<Rejection, Integer> {
    Optional<Rejection> findRejectionById(Integer id);
}
