package org.tim_18.UberApp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tim_18.UberApp.model.Message;

import java.util.ArrayList;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message,Integer> {
    Optional<Message> findMessageById(Integer id);

    void deleteMessageById(Integer id);

    @Query(value = "SELECT * FROM messages m where m.sender_id = ?1 OR m.receiver_id = ?1", nativeQuery = true)
    Page<Message> findMessagesByUserId(Integer id, Pageable pageable);
}
