package org.tim_18.UberApp.dto;

import jakarta.persistence.*;
import org.tim_18.UberApp.model.Document;
import org.tim_18.UberApp.model.Ride;

import java.util.HashSet;

public class DriverDTO extends UserDTO{

    private HashSet<Document> documents;
    private HashSet<Ride> rides;

    public DriverDTO() {}

    public DriverDTO(Integer id,String firstName, String lastName, String imageLink, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active, HashSet<Document> documents, HashSet<Ride> rides) {
        super(id, firstName, lastName, imageLink, telephoneNumber, email, address, password, blocked, active);
        this.documents  = documents;
        this.rides      = rides;
    }

    public HashSet<Ride> getRides() {return rides;}
    public void setRides(HashSet<Ride> rides) {this.rides = rides;}

    public HashSet<Document> getDocuments() {return documents;}
    public void setDocuments(HashSet<Document> documents) {this.documents = documents;}
}
