package org.tim_18.UberApp.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.tim_18.UberApp.model.Request;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    @Query(value = "SELECT r FROM Request r WHERE r.id=?1")
    Page<Request> findByUserId(int id, Pageable pageable);

    @Modifying
    @Query(value = "delete from Request r WHERE r.id=?1")
    void deleteById(Integer integer);
}
