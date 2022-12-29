package org.tim_18.UberApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;






@Entity
@Table(name = "rides")
public class Ride implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private Date startTime;
    private Date endTime;
    private long totalCost;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "rides")
    private Set<Passenger> passengers;
    private int estimatedTimeInMinutes;
    private VehicleType vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rejection_id", referencedColumnName = "id")
    private Rejection rejection;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "panic_id", referencedColumnName = "id")
    private Panic panic;

    @OneToMany(targetEntity = Location.class,cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "ride")
    private Set<Location> locations = new HashSet<Location>();
    private Status status;

    @JsonIgnore
    @OneToMany(targetEntity = Review.class,cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "ride")
    private Set<Review> reviews = new HashSet<Review>();

    public Ride(Date startTime, Date endTime, long totalCost, Driver driver, HashSet<Passenger> passengers, int estimatedTimeInMinutes, VehicleType vehicleType, boolean babyTransport, boolean petTransport, Rejection rejection, HashSet<Location> locations, Status status, HashSet<Review> reviews, Panic panic) {
        this.startTime              = startTime;
        this.endTime                = endTime;
        this.totalCost              = totalCost;
        this.driver                 = driver;
        this.passengers             = passengers;
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
        this.vehicleType            = vehicleType;
        this.babyTransport          = babyTransport;
        this.petTransport           = petTransport;
        this.rejection              = rejection;
        this.locations              = locations;
        this.status                 = status;
        this.reviews                = reviews;
        this.panic                  = panic;
    }

    public Ride() {}


    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public long getTotalCost() {
        return totalCost;
    }
    public void setTotalCost(long totalCost) {
        this.totalCost = totalCost;
    }

    public Driver getDriver() {
        return driver;
    }
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Set<Passenger> getPassengers() {
        return passengers;
    }
    public void setPassengers(HashSet<Passenger> passengers) {
        this.passengers = passengers;
    }

    public int getEstimatedTimeInMinutes() {
        return estimatedTimeInMinutes;
    }
    public void setEstimatedTimeInMinutes(int estimatedTimeInMinutes) {this.estimatedTimeInMinutes = estimatedTimeInMinutes;}

    public VehicleType getVehicleType() {
        return vehicleType;
    }
    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isBabyTransport() {
        return babyTransport;
    }
    public void setBabyTransport(boolean babyTransport) {
        this.babyTransport = babyTransport;
    }

    public boolean isPetTransport() {
        return petTransport;
    }
    public void setPetTransport(boolean petTransport) {
        this.petTransport = petTransport;
    }

    public Rejection getRejection() {
        return rejection;
    }
    public void setRejection(Rejection rejection) {
        this.rejection = rejection;
    }

    public Set<Location> getLocations() {
        return locations;
    }
    public void setLocations(HashSet<Location> locations) {
        this.locations = locations;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Review> getReviews() {
        return reviews;
    }
    public void setReviews(HashSet<Review> reviews) {
        this.reviews = reviews;
    }

    public Panic getPanic() {
        return panic;
    }
    public void setPanic(Panic panic) {
        this.panic = panic;
    }

    @Override
    public String toString() {
        return "Ride{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", totalCost=" + totalCost +
                ", driver=" + driver +
                ", passengers=" + passengers +
                ", estimatedTimeInMinutes=" + estimatedTimeInMinutes +
                ", vehicleType=" + vehicleType +
                ", babyTransport=" + babyTransport +
                ", petTransport=" + petTransport +
                ", rejection=" + rejection +
                ", locations=" + locations +
                ", status=" + status +


                '}';
    }
}