package org.tim_18.UberApp.model;

import jakarta.persistence.*;
import lombok.Data;
import org.tim_18.UberApp.exception.UserNotFoundException;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "panics")
public class Panic implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @ManyToOne
    private User user;
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
}
