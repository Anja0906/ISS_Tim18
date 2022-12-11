package org.tim_18.UberApp.dto;

import org.tim_18.UberApp.dto.locationDTOs.LocationDTO;
import org.tim_18.UberApp.model.Vehicle;
import org.tim_18.UberApp.model.VehicleType;

public class VehicleDTO {

    private Integer id;
    private Integer driverId;

    private VehicleType vehicleType;
    private String model;
    private String licenseNumber;

    private LocationDTO currentLocation;
    private Integer passengerSeats;
    private Boolean babyTransport;
    private Boolean petTransport;



    public VehicleDTO() {}

    public VehicleDTO(Integer id, Integer driverId, VehicleType vehicleType, String model, String licenseNumber, LocationDTO currentLocation, Integer passengerSeats, Boolean babyTransport, Boolean petTransport) {
        this.id                 = id;
        this.driverId           = driverId;
        this.vehicleType        = vehicleType;
        this.model              = model;
        this.licenseNumber      = licenseNumber;
        this.currentLocation    = currentLocation;
        this.passengerSeats     = passengerSeats;
        this.babyTransport      = babyTransport;
        this.petTransport       = petTransport;
    }

    public VehicleDTO(Vehicle vehicle,LocationDTO locationDTO){
        this(vehicle.getId(), vehicle.getDriver().getId(), vehicle.getVehicleType(),
                vehicle.getModel(),vehicle.getLicenseNumber(),
                locationDTO,
                vehicle.getPassengerSeats(),vehicle.getBabyTransport(),
                vehicle.getPetTransport());
    }

    public LocationDTO getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LocationDTO currentLocation) {
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

    @Override
    public String toString() {
        return "VehicleDTO{" +
                "id=" + id +
                ", driverId=" + driverId +
                ", vehicleType=" + vehicleType +
                ", model='" + model + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", currentLocation=" + currentLocation +
                ", passengerSeats=" + passengerSeats +
                ", babyTransport=" + babyTransport +
                ", petTransport=" + petTransport +
                '}';
    }
}
