package org.tim_18.UberApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "panics")
public class Panic implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @JsonIgnore
    @ManyToOne
    private User user;
    @JsonIgnore
    @OneToOne(mappedBy = "panic")
    private Ride ride;
    private Date time;
    private String reason;


    public Panic(Ride ride, User user, Date time, String reason) {
        this.ride   = ride;
        this.user   = user;
        this.time   = time;
        this.reason = reason;
    }

    public Panic() {}

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
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "Panic{" +
                "id=" + id +
                ", user=" + user +
                ", ride=" + ride +
                ", time=" + time +
                ", reason='" + reason + '\'' +
                '}';
    }
}
