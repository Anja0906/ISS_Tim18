package org.tim_18.UberApp.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.tim_18.UberApp.model.Driver;

public class DocumentDTO {
    private Integer id;
    private String image;
    private Driver driver;

    public DocumentDTO() {}

    public DocumentDTO(Integer id, String image, Driver driver) {
        this.id     = id;
        this.image  = image;
        this.driver = driver;
    }

    public String getImage() {return image;}
    public void setImage(String image) {this.image = image;}

    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
}
