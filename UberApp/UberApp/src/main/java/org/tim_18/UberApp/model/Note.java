package org.tim_18.UberApp.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "notes")
public class Note implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String message;
    @ManyToOne
    private User user;
}
