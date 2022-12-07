package org.tim_18.UberApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@DiscriminatorValue("Passengers")
@Table(name = "passengers")
public class Passenger extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToMany(targetEntity = Location.class,cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private Set<Location> favouriteLocations = new HashSet<Location>();
    @ManyToMany
    private Set<Ride> rides = new HashSet<Ride>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private Review review;


    public Passenger(){}

    public Passenger(String name, String surname, String imageLink, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active, Integer id, HashSet<Location> favouriteLocations, HashSet<Ride> rides) {
        super(name, surname, imageLink, telephoneNumber, email, address, password, blocked, active);
        this.id = id;
        this.favouriteLocations = favouriteLocations;
        this.rides = rides;
    }

    public Passenger(String name, String surname, String imageLink, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active) {
        super(name, surname, imageLink, telephoneNumber, email, address, password, blocked, active);
    }


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Set<Location> getFavouriteLocations() {
        return favouriteLocations;
    }

    public void setFavouriteLocations(HashSet<Location> favouriteLocations) {
        this.favouriteLocations = favouriteLocations;
    }

    public Set<Ride> getRides() {
        return rides;
    }

    public void setRides(HashSet<Ride> rides) {
        this.rides = rides;
    }

    @Override
    public boolean equals(Object o) {
        Passenger p = (Passenger) o;
        return this.id.equals(p.getId());
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getFavouriteLocations() != null ? getFavouriteLocations().hashCode() : 0);
        result = 31 * result + (getRides() != null ? getRides().hashCode() : 0);
        result = 31 * result + (review != null ? review.hashCode() : 0);
        return result;
    }
}
