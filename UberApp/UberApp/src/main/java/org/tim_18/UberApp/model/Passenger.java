package org.tim_18.UberApp.model;

import jakarta.persistence.*;

import javax.persistence.Table;
import java.util.HashSet;

@Entity
@DiscriminatorValue("Passengers")
@Table(name = "passengers")
public class Passenger extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToMany(targetEntity = Location.class,cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private HashSet<Location> favouriteLocations;
    @ManyToMany
    private HashSet<Ride> rides;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private Review review;

    public Passenger(){}

    public Passenger(String firstName, String lastName, String imageLink, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active, Integer id, HashSet<Location> favouriteLocations, HashSet<Ride> rides) {
        super(firstName, lastName, imageLink, telephoneNumber, email, address, password, blocked, active);
        this.id = id;
        this.favouriteLocations = favouriteLocations;
        this.rides = rides;
    }

    public Passenger(String firstName, String lastName, String imageLink, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active) {
        super(firstName, lastName, imageLink, telephoneNumber, email, address, password, blocked, active);
    }


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public HashSet<Location> getFavouriteLocations() {
        return favouriteLocations;
    }

    public void setFavouriteLocations(HashSet<Location> favouriteLocations) {
        this.favouriteLocations = favouriteLocations;
    }

    public HashSet<Ride> getRides() {
        return rides;
    }

    public void setRides(HashSet<Ride> rides) {
        this.rides = rides;
    }
}
