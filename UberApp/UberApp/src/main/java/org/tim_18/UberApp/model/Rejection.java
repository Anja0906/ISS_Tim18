package org.tim_18.UberApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "rejections")
public class Rejection implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @JsonIgnore
    @ManyToOne
    private User user;
    @JsonIgnore
    @OneToOne(mappedBy = "rejection")
    private Ride ride;
    private Date timeOfRejection;
    private String reason;


    public Rejection(Ride ride, User user, Date time, String reason) {
        this.ride   = ride;
        this.user   = user;
        this.timeOfRejection   = time;
        this.reason = reason;
    }

    public Rejection() {}


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public Date getTime() {
        return timeOfRejection;
    }

    public void setTime(Date time) {
        this.timeOfRejection = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    @Override
    public String toString() {
        return "Rejection{" +
                "id=" + id +
                ", user=" + user +
                ", ride=" + ride +
                ", timeOfRejection=" + timeOfRejection +
                ", reason='" + reason + '\'' +
                '}';
    }
}
