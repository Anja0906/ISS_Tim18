package org.tim_18.UberApp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tim_18.UberApp.model.Note;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    public List<Note> findAll();
    @Query(value = "select * from notes n where n.user_id = ?1", nativeQuery = true)
    public Page<Note> findByUserId(Integer id, Pageable pageable);

    Optional<Note> findNoteById(Integer id);

    void deleteNoteById(Integer id);
}
