package org.tim_18.UberApp.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reviews")
public class Review implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private Integer rating;
    private String comment;

    @OneToMany
    private Set<Passenger> passengers = new HashSet<Passenger>();

    @ManyToOne
    @JoinColumn(name = "ride_id")
    private Ride ride;

    public Review() {}

    public Review(Integer id, Integer rating, String comment, HashSet<Passenger> passengers) {
        this.rating     = rating;
        this.comment    = comment;
        this.passengers = passengers;
        this.id         = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(HashSet<Passenger> passengers) {
        this.passengers = passengers;
    }

    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", passengers=" + passengers +
                ", ride=" + ride +
                '}';
    }
}
