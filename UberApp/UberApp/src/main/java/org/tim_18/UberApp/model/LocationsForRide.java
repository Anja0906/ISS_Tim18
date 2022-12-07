package org.tim_18.UberApp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "locations_for_rides")
public class LocationsForRide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @ManyToOne(cascade = {CascadeType.ALL})
    private Location departure;

    @ManyToOne(cascade = {CascadeType.ALL})
    private Location destination;
    private Double kilometers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocationsForRide() {
    }

    public LocationsForRide(Integer id, Location departure, Location destination, Double kilometers) {
        this.id          = id;
        this.departure   = departure;
        this.destination = destination;
        this.kilometers  = kilometers;
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
}