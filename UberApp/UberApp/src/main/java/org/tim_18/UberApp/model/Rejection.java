package org.tim_18.UberApp.model;

import jakarta.persistence.*;
import jakarta.websocket.OnError;

import java.util.Date;

@Entity
@Table(name = "rejections")
public class Rejection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String reason;
    private Date time;

    public Rejection() {}
    public String getReason() {
        return reason;
    }
    @OneToOne(mappedBy = "rejection")
    private Ride ride;

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Rejection(Integer id, String reason, Date time, Ride ride) {
        this.reason     = reason;
        this.time       = time;
        this.id         = id;
//        this.ride = ride;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Id
    public Integer getId() {
        return id;
    }



}
