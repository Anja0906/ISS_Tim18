package org.tim_18.UberApp.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.data.domain.Page;
import org.tim_18.UberApp.dto.driverDTOs.DriverDTO;
import org.tim_18.UberApp.model.Document;
import org.tim_18.UberApp.model.Driver;

import java.util.HashSet;

public class DocumentDTO {
    private Integer id;

    private String name;
    private String documentImage;
    private Integer driverId;

    public DocumentDTO() {}

    public DocumentDTO(Document document){
        this(document.getId(),document.getName(), document.getDocumentImage(), document.getDriver().getId());
    }
    public DocumentDTO(Integer id, String name, String documentImage, Integer driverId) {
        this.id         = id;
        this.name       = name;
        this.documentImage      = documentImage;
        this.driverId   = driverId;
    }

    public HashSet<DocumentDTO> makeDocumentsDTO(HashSet<Document> documents){
        HashSet<DocumentDTO>documentDTOS = new HashSet<>();
        for(Document document: documents){
            documentDTOS.add(new DocumentDTO(document));
        }
        return documentDTOS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentImage() {return documentImage;}
    public void setDocumentImage(String documentImage) {this.documentImage = documentImage;}

    public Integer getDriverId() { return driverId; }
    public void setDriverId(Integer driverId) { this.driverId = driverId; }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
}
