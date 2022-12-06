package org.tim_18.UberApp.model;

import jakarta.persistence.*;

import java.util.HashSet;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private Integer rating;
    private String comment;

    @OneToMany
    private HashSet<Passenger> passengers;

    @ManyToOne
    @JoinColumn(name = "ride_id")
    private Ride ride;

    public Review() {}

    public Review(Integer id, Integer rating, String comment, HashSet<Passenger> passengers) {
        this.rating = rating;
        this.comment = comment;
        this.passengers = passengers;
        this.id = id;
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

    public HashSet<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(HashSet<Passenger> passengers) {
        this.passengers = passengers;
    }
}
