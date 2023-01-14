package org.tim_18.UberApp.dto;

import lombok.Data;
import org.tim_18.UberApp.dto.locationDTOs.LocationDTO;
import org.tim_18.UberApp.model.Vehicle;
import org.tim_18.UberApp.model.VehicleType;
@Data
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
        this(vehicle.getId(), vehicle.getDriver().getId(),
             vehicle.getVehicleType(), vehicle.getModel(),
             vehicle.getLicenseNumber(), locationDTO,
             vehicle.getPassengerSeats(),vehicle.getBabyTransport(),
             vehicle.getPetTransport());
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
