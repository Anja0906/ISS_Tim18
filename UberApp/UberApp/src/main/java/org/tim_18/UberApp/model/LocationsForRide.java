package org.tim_18.UberApp.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "locations_for_rides")
public class LocationsForRide implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @ManyToOne(cascade = {CascadeType.ALL})
    private Location departure;

    @ManyToOne(cascade = {CascadeType.ALL})
    private Location destination;
    private Double kilometers;

    private Double duration;

    @ManyToOne(cascade = {CascadeType.ALL})
    private Ride ride;

    public LocationsForRide() {}

    public LocationsForRide(Location departure, Location destination, Double kilometers, Double duration, Ride ride) {
        this.departure   = departure;
        this.destination = destination;
        this.kilometers  = kilometers;
        this.duration    = duration;
        this.ride        = ride;
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

    public Double getKilometers() {
        return kilometers;
    }

    public void setKilometers(Double kilometers) {
        this.kilometers = kilometers;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }
}