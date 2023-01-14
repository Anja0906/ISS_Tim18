package org.tim_18.UberApp.model;

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
    private String reason;
    private Date timeOfRejection;

    @OneToOne(mappedBy = "rejection")
    private Ride ride;

    public Rejection() {}
    public Rejection(Integer id, String reason, Date timeOfRejection, Ride ride) {
        this.reason     = reason;
        this.timeOfRejection = timeOfRejection;
        this.id         = id;
        this.ride       = ride;
    }

    public Rejection(String reason, Date timeOfRejection, Ride ride) {
        this.reason     = reason;
        this.timeOfRejection = timeOfRejection;
        this.ride       = ride;
    }

    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getTimeOfRejection() {
        return timeOfRejection;
    }
    public void setTimeOfRejection(Date time) {
        this.timeOfRejection = time;
    }

    @Id
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }




}
