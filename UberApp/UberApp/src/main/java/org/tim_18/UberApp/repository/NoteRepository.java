package org.tim_18.UberApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tim_18.UberApp.model.Note;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    public List<Note> findAll();
    @Query(value = "select * from notes n where n.user_id = ?1", nativeQuery = true)
    public List<Note> findByUserId(Integer id);
}
