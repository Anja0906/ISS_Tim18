package org.tim_18.UberApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "favorite_rides")
@Data
public class FavoriteRide implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String favoriteName;
    private VehicleType vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
//    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "favoriteRides")
//    private Set<Location> locations = new HashSet<Location>();
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "favoriteRides")
    private Set<Passenger> passengers = new HashSet<Passenger>();
}
