package org.tim_18.UberApp.dto;

import org.tim_18.UberApp.model.Document;

public class DocumentDTOWithoutId {

    private String name;
    private String image;
    private Integer driverId;

    public DocumentDTOWithoutId() {}

    public DocumentDTOWithoutId(Document document){
        this(document.getName(), document.getImage(), document.getDriver().getId());
    }
    public DocumentDTOWithoutId(String name, String image, Integer driverId) {
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

}
