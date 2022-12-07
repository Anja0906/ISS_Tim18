package org.tim_18.UberApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tim_18.UberApp.model.WorkTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface WorkTimeRepository extends JpaRepository<WorkTime,Integer> {
    Optional<WorkTime> findWorkTimeById(Integer id);
    @Query(value = "SELECT * FROM work_time WHERE driver_id = ?1", nativeQuery = true)
    ArrayList<WorkTime> findByDriverId(Integer id);

    void deleteWorkTimeById(Integer id);
}
