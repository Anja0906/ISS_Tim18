package org.tim_18.UberApp.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.tim_18.UberApp.model.Document;
import org.tim_18.UberApp.model.Driver;

public class DocumentDTO {
    private Integer id;

    private String name;
    private String image;
    private Integer driverId;

    public DocumentDTO() {}

    public DocumentDTO(Document document){
        this(document.getId(),document.getName(), document.getImage(), document.getDriver().getId());
    }
    public DocumentDTO(Integer id, String name, String image, Integer driverId) {
        this.id         = id;
        this.name       = name;
        this.image      = image;
        this.driverId   = driverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {return image;}
    public void setImage(String image) {this.image = image;}

    public Integer getDriverId() { return driverId; }
    public void setDriverId(Integer driverId) { this.driverId = driverId; }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
}
