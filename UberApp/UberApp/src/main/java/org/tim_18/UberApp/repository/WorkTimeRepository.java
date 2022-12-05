package org.tim_18.UberApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tim_18.UberApp.model.WorkTime;

import java.util.Optional;

public interface WorkTimeRepository extends JpaRepository<WorkTime,Integer> {
    Optional<WorkTime> findWorkTimeById(Integer id);

    void deleteWorkTimeById(Integer id);
}
