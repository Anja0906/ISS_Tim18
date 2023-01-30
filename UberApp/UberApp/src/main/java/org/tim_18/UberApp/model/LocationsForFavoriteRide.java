package org.tim_18.UberApp.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "locations_for_favorite_rides")
public class LocationsForFavoriteRide implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @ManyToOne(cascade = {CascadeType.ALL})
    private Location departure;

    @ManyToOne(cascade = {CascadeType.ALL})
    private Location destination;

    @ManyToOne(cascade = {CascadeType.ALL})
    private FavoriteRide ride;

    public LocationsForFavoriteRide() {
    }


    public LocationsForFavoriteRide(Integer id, Location departure, Location destination, FavoriteRide ride) {
        this.id = id;
        this.departure = departure;
        this.destination = destination;
        this.ride = ride;
    }

    public LocationsForFavoriteRide(Location departure, Location destination, FavoriteRide ride) {
        this.departure = departure;
        this.destination = destination;
        this.ride = ride;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Location getDeparture() {
        return departure;
    }

    public void setDeparture(Location departure) {
        this.departure = departure;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public FavoriteRide getRide() {
        return ride;
    }

    public void setRide(FavoriteRide ride) {
        this.ride = ride;
    }
}