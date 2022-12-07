package org.tim_18.UberApp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne
    private Driver driver;

    private String model;
    private VehicleType vehicleType;
    private String licenseNumber;

    @ManyToOne
    @JoinColumn(name = "current_location_id")
    private Location currentLocation;

    private Integer passengerSeats;
    private Boolean babyTransport;
    private Boolean petTransport;



    public Vehicle() {}

    public Vehicle(Integer id, Driver driver, String model, VehicleType vehicleType, String licenseNumber, Location currentLocation, Integer passengerSeats, Boolean babyTransport, Boolean petTransport) {
        this.id                 = id;
        this.driver             = driver;
        this.model              = model;
        this.vehicleType        = vehicleType;
        this.licenseNumber      = licenseNumber;
        this.currentLocation    = currentLocation;
        this.passengerSeats     = passengerSeats;
        this.babyTransport      = babyTransport;
        this.petTransport       = petTransport;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Driver getDriver() {
        return driver;
    }
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getmodel() {
        return model;
    }
    public void setmodel(String model) {
        this.model = model;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }
    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

}