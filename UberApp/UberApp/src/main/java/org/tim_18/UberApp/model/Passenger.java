package org.tim_18.UberApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerDTOnoPassword;

import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "passengers")
public class Passenger extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passenger_id", nullable = false)
    private Integer id;

    @JsonIgnore
    @ManyToMany(targetEntity = Location.class,cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private Set<Location> favouriteLocations = new HashSet<Location>();
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.ALL
    })
    @JoinTable(name = "passenger_rides",
            joinColumns = @JoinColumn(name = "passenger_id"),
            inverseJoinColumns = @JoinColumn(name = "ride_id"))
    private Set<Ride> rides = new HashSet<Ride>();

    @JsonIgnore
    @ManyToMany(targetEntity = FavoriteRide.class,cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.ALL}, fetch = FetchType.LAZY)
    private Set<FavoriteRide> favoriteRides = new HashSet<FavoriteRide>();
    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "passenger_id")
    private Set<Review> review;


    public Passenger(){}

    public Passenger(String name, String surname, String profilePicture, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active, Integer id, HashSet<Location> favouriteLocations, HashSet<Ride> rides, HashSet<FavoriteRide> favoriteRides) {
        super(name, surname, profilePicture, telephoneNumber, email, address, password, blocked, active);
        this.id                 = id;
        this.favouriteLocations = favouriteLocations;
        this.rides              = rides;
        this.favoriteRides      = favoriteRides;
    }

    public Passenger(String name, String surname, String profilePicture, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active) {
        super(name, surname, profilePicture, telephoneNumber, email, address, password, blocked, active);
    }

    public Passenger(User user) {
        super(user.getName(), user.getSurname(), user.getProfilePicture(), user.getTelephoneNumber(),
                user.getEmail(), user.getAddress(), user.getPassword(), user.isBlocked(), user.isActive());
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

    public Set<FavoriteRide> getFavoriteRides() {
        return favoriteRides;
    }

    public void setFavoriteRides(Set<FavoriteRide> favoriteRides) {
        this.favoriteRides = favoriteRides;
    }
    
    @Override
    public String toString() {
        return "Passenger{" +
                "id=" + id +
                '}';
    }

    public void passengerUpdate(PassengerDTOnoPassword dto) {
        setName(dto.getName());
        setSurname(dto.getSurname());
        setProfilePicture(dto.getProfilePicture());
        setTelephoneNumber(dto.getTelephoneNumber());
        setAddress(dto.getAddress());
        setEmail(dto.getEmail());
    }
}
