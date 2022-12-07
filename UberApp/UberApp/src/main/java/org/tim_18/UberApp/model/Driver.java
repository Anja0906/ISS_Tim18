package org.tim_18.UberApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "drivers")
public class Driver extends User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @OneToMany(targetEntity = Document.class,cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "driver")
    private Set<Document> documents = new HashSet<Document>();
    @OneToMany(targetEntity = Ride.class,cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "driver")
    private Set<Ride> rides = new HashSet<Ride>();

    @JsonIgnore
    @OneToOne
    private Vehicle vehicle;

    public Driver() {
    }

    public Driver(String name, String surname, String profilePicture, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active, Integer id, Set<Document> documents, Set<Ride> rides, Vehicle vehicle) {
        super(name, surname, profilePicture, telephoneNumber, email, address, password, blocked, active);
        this.id         = id;
        this.documents  = documents;
        this.rides      = rides;
        this.vehicle    = vehicle;
    }

    public Driver(String name, String surname, String profilePicture, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active, Integer id, Vehicle vehicle) {
        super(name, surname, profilePicture, telephoneNumber, email, address, password, blocked, active);
        this.id = id;
//        this.documents = new HashSet<Document>();
//        this.rides = new HashSet<Ride>();
        this.vehicle = vehicle;
    }

}