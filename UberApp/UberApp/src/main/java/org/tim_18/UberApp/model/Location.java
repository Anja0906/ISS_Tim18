package org.tim_18.UberApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "locations")
public class Location implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private Double longitude;
    private Double latitude;
    private String address;
    @JsonIgnore
    @ManyToOne
    private FavoriteRide favRide;
    @JsonIgnore
    @ManyToOne
    private Ride ride;

    public Location() {}

    public Location(Integer id, Double longitude, Double latitude, String address) {
        this.id         = id;
        this.longitude  = longitude;
        this.latitude   = latitude;
        this.address    = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}