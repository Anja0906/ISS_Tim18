package org.tim_18.UberApp.model;

import jakarta.persistence.*;
import java.util.HashSet;

@Entity
@Table(name = "drivers")
public class Driver extends User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @OneToMany(targetEntity = Document.class,cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "driver")
    private HashSet<Document> documents;
    @OneToMany(targetEntity = Document.class,cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private HashSet<Ride> rides;

    @OneToOne
    private Vehicle vehicle;

    public Driver() {
    }

    public Driver(String firstName, String lastName, String imageLink, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active, Integer id, HashSet<Document> documents, HashSet<Ride> rides, Vehicle vehicle) {
        super(firstName, lastName, imageLink, telephoneNumber, email, address, password, blocked, active);
        this.id = id;
        this.documents = documents;
        this.rides = rides;
        this.vehicle = vehicle;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public HashSet<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(HashSet<Document> documents) {
        this.documents = documents;
    }

    public HashSet<Ride> getRides() {
        return rides;
    }

    public void setRides(HashSet<Ride> rides) {
        this.rides = rides;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}