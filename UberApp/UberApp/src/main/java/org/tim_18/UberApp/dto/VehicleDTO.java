package org.tim_18.UberApp.dto;

import jakarta.persistence.*;
import org.tim_18.UberApp.model.Driver;
import org.tim_18.UberApp.model.Location;
import org.tim_18.UberApp.model.Vehicle;
import org.tim_18.UberApp.model.VehicleType;

public class VehicleDTO {

    private Integer id;
    private Integer driverId;
    private String model;
    private VehicleType vehicleType;
    private String licenseNumber;

    private Location currentLocation;
    private Integer passengerSeats;
    private Boolean babyTransport;
    private Boolean petTransport;



    public VehicleDTO() {}

    public VehicleDTO(Integer id, Integer driverId, String model, VehicleType vehicleType, String licenseNumber, Location currentLocation, Integer passengerSeats, Boolean babyTransport, Boolean petTransport) {
        this.id                 = id;
        this.driverId           = driverId;
        this.model              = model;
        this.vehicleType        = vehicleType;
        this.licenseNumber      = licenseNumber;
        this.currentLocation    = currentLocation;
        this.passengerSeats     = passengerSeats;
        this.babyTransport      = babyTransport;
        this.petTransport       = petTransport;
    }

    public VehicleDTO(Vehicle vehicle){
        this(vehicle.getId(), vehicle.getDriver().getId(), vehicle.getModel(),
                vehicle.getVehicleType(),vehicle.getLicenseNumber(),
                vehicle.getCurrentLocation(),
                vehicle.getPassengerSeats(),vehicle.getBabyTransport(),
                vehicle.getPetTransport());
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Integer getDriverId() {
        return driverId;
    }
    public void setDriverId(Integer integer) {
        this.driverId = driverId;
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
        return "VehicleDTO{" +
                "id=" + id +
                ", driverId=" + driverId +
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
