package org.tim_18.UberApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tim_18.UberApp.model.Document;


import java.util.HashSet;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Integer> {
    Optional<Document> findDocumentById(Integer id);
    @Query(value = "SELECT * FROM documents WHERE driver_id = ?1", nativeQuery = true)
    HashSet<Document> findByDriverId(Integer id);

    @Modifying
    @Query(value = "DELETE FROM Document doc WHERE doc.id = :id")
    void deleteById(@Param("id") int id);


}
