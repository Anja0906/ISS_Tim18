package org.tim_18.UberApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.tim_18.UberApp.dto.driverDTOs.DriverDTOWithoutId;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private Boolean isOnline;

    public Driver() {
        this.documents  = new HashSet<Document>();
        this.rides      = new HashSet<Ride>();
        this.vehicle    = null;
        this.isOnline   = false;
    }
    public Driver(String name, String surname, String profilePicture, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active, Set<Document> documents, Set<Ride> rides, Vehicle vehicle, Boolean isOnline) {
        super(name, surname, profilePicture, telephoneNumber, email, address, password, blocked, active);
        this.documents  = documents;
        this.rides      = rides;
        this.vehicle    = vehicle;
        this.isOnline   = isOnline;
    }

    public Driver(String name, String surname, String profilePicture, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active, Vehicle vehicle, Boolean isOnline,List<Role>roles) {
        super(name, surname, profilePicture, telephoneNumber, email, address, password, blocked, active,roles);
        this.documents  = new HashSet<Document>();
        this.rides      = new HashSet<Ride>();
        this.vehicle    = vehicle;
        this.isOnline   = isOnline;
    }

    public Driver(User user) {
        super(user.getName(), user.getSurname(), user.getProfilePicture(), user.getTelephoneNumber(),
                user.getEmail(), user.getAddress(), user.getPassword(), user.isBlocked(), user.isActive());
    }


    public void driverUpdate(DriverDTOWithoutId driverDTOWithoutId){
        setName(driverDTOWithoutId.getName());
        setSurname(driverDTOWithoutId.getSurname());
        setProfilePicture(driverDTOWithoutId.getProfilePicture());
        setTelephoneNumber(driverDTOWithoutId.getTelephoneNumber());
        setAddress(driverDTOWithoutId.getAddress());
        setEmail(driverDTOWithoutId.getEmail());
    }

    @Override
    public Integer getId() {return id;}

    public Set<Document> getDocuments() {return documents;}
    public void setDocuments(Set<Document> documents) {this.documents = documents;}

    public Set<Ride> getRides() {return rides;}
    public void setRides(Set<Ride> rides) {this.rides = rides;}

    public Vehicle getVehicle() {return vehicle;}
    public void setVehicle(Vehicle vehicle) {this.vehicle = vehicle;}

    public Boolean getIsOnline() {return isOnline;}
    public void setIsOnline(Boolean online) {isOnline = online;}

    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", documents=" + documents +
                ", rides=" + rides +
                ", vehicle=" + vehicle +
                ", isOnline=" + isOnline +
                '}';
    }
}