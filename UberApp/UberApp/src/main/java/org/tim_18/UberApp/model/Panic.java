package org.tim_18.UberApp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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
}
