package org.tim_18.UberApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.tim_18.UberApp.dto.DocumentDTO;

import java.io.Serializable;

@Entity
@Table(name = "documents")
public class Document implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String name;
    private String documentImage;
    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "driver_id", nullable=false)
    private Driver driver;

    public Document() {}

    public Document(Integer id, String name, String documentImage, Driver driver) {
        this.id             = id;
        this.name           = name;
        this.documentImage  = documentImage;
        this.driver         = driver;
    }
    public Document makeDocumentFromDTO(DocumentDTO documentDTO,Driver driver){
        setName(documentDTO.getName());
        setDocumentImage(documentDTO.getDocumentImage());
        setDriver(driver);
        return this;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentImage() {
        return documentImage;
    }
    public void setDocumentImage(String documentImage) {
        this.documentImage = documentImage;
    }

    public Driver getDriver() {
        return driver;
    }
    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}