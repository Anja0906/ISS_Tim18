package org.tim_18.UberApp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "favourite_rides")
@Data
public class FavouriteRide implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String favouriteName;
    @ManyToOne(cascade = CascadeType.ALL)
    private Passenger passenger;
    private VehicleType vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
    @OneToMany(targetEntity = Location.class,cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "favRide")
    private Set<Location> locations = new HashSet<Location>();
}
