package org.tim_18.UberApp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tim_18.UberApp.model.WorkTime;

import java.time.LocalDateTime;
import java.util.*;

public interface WorkTimeRepository extends JpaRepository<WorkTime,Integer> {
    Optional<WorkTime> findWorkTimeById(Integer id);
    @Query(value = "SELECT * FROM work_time WHERE driver_id = ?1 ", nativeQuery = true)
    ArrayList<WorkTime> findByDriverId(Integer id);
    void deleteWorkTimeById(Integer id);
    @Query(value = "SELECT * FROM work_time WHERE driver_id = ?1 and DATE(work_time.start)>?2 and DATE(work_time.end)<?3", nativeQuery = true)
    Page<WorkTime> findWorkTimesByDate(Integer id, String start, String end, Pageable pageable);

    @Query(value = "SELECT * FROM work_time w WHERE w.driver_id = :driver_id AND w.start > :start AND w.end < :end", nativeQuery = true)
    ArrayList<WorkTime> findWorkTimesByDateHash(@Param("driver_id")Integer id,@Param("start") String start,@Param("end") String end);

}
