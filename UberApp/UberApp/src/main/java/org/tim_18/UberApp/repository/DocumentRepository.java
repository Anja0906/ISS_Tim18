package org.tim_18.UberApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tim_18.UberApp.model.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Integer> {
    Optional<Document> findDocumentById(Integer id);
    @Query(value = "SELECT * FROM documents WHERE driver_id = ?1", nativeQuery = true)
    HashSet<Document> findByDriverId(Integer id);

    void deleteDocumentById(Integer id);
}
