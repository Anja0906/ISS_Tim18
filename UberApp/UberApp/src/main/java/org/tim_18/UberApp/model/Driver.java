package org.tim_18.UberApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.tim_18.UberApp.dto.driverDTOs.DriverDTOWithoutId;
import java.util.HashSet;
import java.util.List;
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

    private Boolean isOnline;

    public Driver() {
        this.documents  = new HashSet<Document>();
        this.rides      = new HashSet<Ride>();
        this.vehicle    = null;
        this.isOnline   = false;
    }
    public Driver(String name, String surname, String profilePicture, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active, Integer id, Set<Document> documents, Set<Ride> rides, Vehicle vehicle, Boolean isOnline) {
        super(name, surname, profilePicture, telephoneNumber, email, address, password, blocked, active);
        this.id         = id;
        this.documents  = documents;
        this.rides      = rides;
        this.vehicle    = vehicle;
        this.isOnline   = isOnline;
    }

    public Driver(String name, String surname, String profilePicture, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active, Integer id, Vehicle vehicle, Boolean isOnline,List<Role>roles) {
        super(name, surname, profilePicture, telephoneNumber, email, address, password, blocked, active,roles);
        this.id         = id;
        this.documents  = new HashSet<Document>();
        this.rides      = new HashSet<Ride>();
        this.vehicle    = vehicle;
        this.isOnline   = isOnline;
    }
    public Driver(String name, String surname, String profilePicture, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active, Boolean isOnline,List<Role>roles) {
        super(name, surname, profilePicture, telephoneNumber, email, address, password, blocked, active,roles);
        this.documents  = new HashSet<Document>();
        this.rides      = new HashSet<Ride>();
        this.vehicle    = null;
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

}