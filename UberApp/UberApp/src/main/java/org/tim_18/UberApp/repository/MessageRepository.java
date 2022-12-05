package org.tim_18.UberApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tim_18.UberApp.model.Message;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message,Integer> {
    Optional<Message> findMessageById(Integer id);

    void deleteMessageById(Integer id);
}
