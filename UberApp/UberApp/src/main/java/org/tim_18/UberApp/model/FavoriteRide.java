package org.tim_18.UberApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "favorite_rides")
public class FavoriteRide implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String favoriteName;
    private VehicleType vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "favoriteRides")
    private Set<Passenger> passengers = new HashSet<Passenger>();

    public FavoriteRide(Set<Passenger> passengers) {
        this.passengers = passengers;
    }

    public FavoriteRide(Integer id, String favoriteName, VehicleType vehicleType, boolean babyTransport, boolean petTransport, Set<Passenger> passengers) {
        this.id = id;
        this.favoriteName = favoriteName;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
        this.passengers = passengers;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFavoriteName() {
        return favoriteName;
    }

    public void setFavoriteName(String favoriteName) {
        this.favoriteName = favoriteName;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isBabyTransport() {
        return babyTransport;
    }

    public void setBabyTransport(boolean babyTransport) {
        this.babyTransport = babyTransport;
    }

    public boolean isPetTransport() {
        return petTransport;
    }

    public void setPetTransport(boolean petTransport) {
        this.petTransport = petTransport;
    }

    public Set<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
    }

    @Override
    public String toString() {
        return "FavoriteRide{" +
                "id=" + id +
                ", favoriteName='" + favoriteName + '\'' +
                ", vehicleType=" + vehicleType +
                ", babyTransport=" + babyTransport +
                ", petTransport=" + petTransport +
                ", passengers=" + passengers +
                '}';
    }
}
