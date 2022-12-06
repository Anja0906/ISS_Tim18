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

    public Driver(String firstName, String lastName, String imageLink, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active, Integer id, HashSet<Document> documents, HashSet<Ride> rides) {
        super(firstName, lastName, imageLink, telephoneNumber, email, address, password, blocked, active);
        this.id         = id;
        this.documents  = documents;
        this.rides      = rides;
    }

    public Driver() {}

    @OneToMany
    public HashSet<Ride> getRides() {return rides;}
    public void setRides(HashSet<Ride> rides) {this.rides = rides;}

    public HashSet<Document> getDocuments() {return documents;}
    public void setDocuments(HashSet<Document> documents) {this.documents = documents;}

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
}