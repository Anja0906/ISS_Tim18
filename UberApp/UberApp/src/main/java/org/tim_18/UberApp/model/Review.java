package org.tim_18.UberApp.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;

import java.io.Serializable;

@Entity
@Table(name = "reviews")
public class Review implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private Integer rating;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    @ManyToOne
    @JoinColumn(name = "ride_id")
    private Ride ride;

    private Boolean isDriver;

    public Review() {}

    public Review(Integer id, Integer rating, String comment, Passenger passenger, Boolean isDriver) {
        this.rating     = rating;
        this.comment    = comment;
        this.passenger  = passenger;
        this.id         = id;
        this.isDriver   = isDriver;
    }


    public Review(Integer rating, String comment, Ride ride, Boolean isDriver) {
        this.rating     = rating;
        this.comment    = comment;
        this.ride       = ride;
        this.isDriver   = isDriver;
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

    public Passenger getPassenger() {
        return passenger;
    }
    public void setPassenger(Passenger passenger) {this.passenger = passenger;}

    public Ride getRide() {
        return ride;
    }
    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public Boolean getDriver() {return isDriver;}
    public void setDriver(Boolean driver) {isDriver = driver;}



    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", ride=" + ride +
                ", isDriver=" + isDriver +
                '}';
    }
}