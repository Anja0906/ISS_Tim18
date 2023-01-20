package org.tim_18.UberApp.dto;

import lombok.Data;
import org.tim_18.UberApp.dto.locationDTOs.LocationDTO;
import org.tim_18.UberApp.model.Vehicle;
import org.tim_18.UberApp.model.VehicleType;
@Data
public class VehicleDTOWithoutIds {
    private VehicleType vehicleType;
    private String model;
    private String licenseNumber;

    private LocationDTO currentLocation;
    private Integer passengerSeats;
    private Boolean babyTransport;
    private Boolean petTransport;



    public VehicleDTOWithoutIds() {}

    public VehicleDTOWithoutIds(VehicleType vehicleType, String model, String licenseNumber, LocationDTO currentLocation, Integer passengerSeats, Boolean babyTransport, Boolean petTransport) {
        this.vehicleType        = vehicleType;
        this.model              = model;
        this.licenseNumber      = licenseNumber;
        this.currentLocation    = currentLocation;
        this.passengerSeats     = passengerSeats;
        this.babyTransport      = babyTransport;
        this.petTransport       = petTransport;
    }

    public VehicleDTOWithoutIds(Vehicle vehicle, LocationDTO locationDTO){
        this(vehicle.getVehicleType(),
                vehicle.getModel(),vehicle.getLicenseNumber(),
                locationDTO,
                vehicle.getPassengerSeats(),vehicle.getBabyTransport(),
                vehicle.getPetTransport());
    }
}
