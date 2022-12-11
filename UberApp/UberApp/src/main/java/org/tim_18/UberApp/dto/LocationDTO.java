package org.tim_18.UberApp.dto;

import org.tim_18.UberApp.model.Location;

public class LocationDTO {

    private String address;
    private Double latitude;
    private Double longitude;


    public LocationDTO() {
    }

    public LocationDTO(String address, Double longitude, Double latitude) {
        this.address    = address;
        this.latitude   = latitude;
        this.longitude  = longitude;

    }
    public LocationDTO(Location location){
        this(location.getAddress(),location.getLatitude(),location.getLongitude());
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
