package org.tim_18.UberApp.model;

import jakarta.persistence.*;
import org.tim_18.UberApp.dto.vehicleDTOs.VehicleDTOWithoutIds;
import org.tim_18.UberApp.dto.locationDTOs.LocationDTO;

import java.io.Serializable;


@Entity
@Table(name = "vehicle")
public class Vehicle implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(cascade = {CascadeType.ALL})
    private Driver driver;
    private VehicleType vehicleType;
    private String model;
    private String licenseNumber;

    @ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name = "current_location_id")
    private Location currentLocation;

    private Integer passengerSeats;
    private Boolean babyTransport;
    private Boolean petTransport;

    public Vehicle() {}

    public Vehicle(Driver driver, VehicleType vehicleType, String model, String licenseNumber, Location currentLocation, Integer passengerSeats, Boolean babyTransport, Boolean petTransport) {
        this.driver             = driver;
        this.vehicleType        = vehicleType;
        this.model              = model;
        this.licenseNumber      = licenseNumber;
        this.currentLocation    = currentLocation;
        this.passengerSeats     = passengerSeats;
        this.babyTransport      = babyTransport;
        this.petTransport       = petTransport;
    }

    public void updateVehicle(VehicleDTOWithoutIds vehicleDTOWithoutIds,Location location){
        setVehicleType(vehicleDTOWithoutIds.getVehicleType());
        setModel(vehicleDTOWithoutIds.getModel());
        setLicenseNumber(vehicleDTOWithoutIds.getLicenseNumber());
        setCurrentLocation(location);
        setPassengerSeats(vehicleDTOWithoutIds.getPassengerSeats());
        setBabyTransport(vehicleDTOWithoutIds.getBabyTransport());
        setPetTransport(vehicleDTOWithoutIds.getPetTransport());
    }
    public void updateLocation(LocationDTO locationDTO){
        getCurrentLocation().setAddress(locationDTO.getAddress());
        getCurrentLocation().setLatitude(locationDTO.getLongitude());
        getCurrentLocation().setLongitude(locationDTO.getLongitude());
    }


    public Integer getPassengerSeats() {
        return passengerSeats;
    }

    public void setPassengerSeats(Integer passengerSeats) {
        this.passengerSeats = passengerSeats;
    }

    public Boolean getBabyTransport() {
        return babyTransport;
    }

    public void setBabyTransport(Boolean babyTransport) {
        this.babyTransport = babyTransport;
    }

    public Boolean getPetTransport() {
        return petTransport;
    }

    public void setPetTransport(Boolean petTransport) {
        this.petTransport = petTransport;
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

    public String getModel() {
        return model;
    }
    public void setModel(String model) {
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

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", driver=" + driver +
                ", model='" + model + '\'' +
                ", vehicleType=" + vehicleType +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", currentLocation=" + currentLocation +
                ", passengerSeats=" + passengerSeats +
                ", babyTransport=" + babyTransport +
                ", petTransport=" + petTransport +
                '}';
    }
}