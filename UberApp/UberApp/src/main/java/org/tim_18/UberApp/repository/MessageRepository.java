package org.tim_18.UberApp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.tim_18.UberApp.model.Message;

import java.util.ArrayList;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message,Integer> {
    Optional<Message> findMessageById(Integer id);

    @Modifying
    @Query(value = "DELETE FROM Message msg WHERE msg.id = :id")
    void deleteMessageById(Integer id);

    @Query(value = "SELECT * FROM messages where sender_id = ?1 OR receiver_id = ?1", nativeQuery = true)
    Page<Message> findMessagesByUserId(Integer id, Pageable pageable);

    @Query(value = "SELECT * FROM messages where ((sender_id = ?1 OR receiver_id = ?1) and (sender_id = ?2 OR receiver_id = ?2)) and ride_id=?3", nativeQuery = true)
    Page<Message> findMessagesByUserAndRideId(Integer id, Integer otherId, Integer rideId, Pageable pageable);
}
