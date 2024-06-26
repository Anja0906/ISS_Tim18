package org.tim_18.UberApp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.tim_18.UberApp.model.Panic;

import java.util.Optional;

public interface PanicRepository extends JpaRepository<Panic, Integer> {

    Optional<Panic> findPanicById(Integer id);
    Page<Panic> findAll(Pageable pageable);
}
