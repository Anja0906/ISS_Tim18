package org.tim_18.UberApp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.data.domain.Page;
import org.tim_18.UberApp.model.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class RideDTO {

    private Integer id;
    private Date startTime;
    private Date endTime;
    private long totalCost;

    private Driver driver;
    private Set<Passenger> passengers;
    private int estimatedTimeInMinutes;
    private VehicleType vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
    private Rejection rejection;
    private Panic panic;

    private Set<Location> locations = new HashSet<Location>();

    public RideDTO(Integer id,Date startTime, Date endTime, long totalCost, Driver driver, Set<Passenger> passengers, int estimatedTimeInMinutes, VehicleType vehicleType, boolean babyTransport, boolean petTransport, Rejection rejection, Set<Location> locations) {
        this.id                     = id;
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
    }

    public RideDTO() {}
    public RideDTO(Ride ride) {
        this(ride.getId(), ride.getStartTime(),
                ride.getEndTime(), ride.getTotalCost(),
                ride.getDriver(), ride.getPassengers(),
                ride.getEstimatedTimeInMinutes(), ride.getVehicleType(),
                ride.isBabyTransport(), ride.isPetTransport(),
                ride.getRejection(), ride.getLocations());
    }

    public HashSet<RideDTO> makeRides(Page<Ride> rides){
        HashSet<RideDTO> rideDTOS = new HashSet<>();
        for(Ride ride:rides)
            rideDTOS.add(new RideDTO(ride));
        return rideDTOS;
    }
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


    public Panic getPanic() {
        return panic;
    }
    public void setPanic(Panic panic) {
        this.panic = panic;
    }

}
