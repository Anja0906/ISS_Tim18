package org.tim_18.UberApp.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "panics")
public class Panic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @ManyToOne
    private User user;
    @OneToOne(mappedBy = "panic")
    private Ride ride;
    private Date date;
    private String reason;
}
