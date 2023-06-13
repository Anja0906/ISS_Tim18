package org.tim_18.UberApp.dto.vehicleDTOs;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.tim_18.UberApp.dto.locationDTOs.LocationDTO;
import org.tim_18.UberApp.model.Vehicle;
import org.tim_18.UberApp.model.VehicleType;
@Data
public class VehicleDTOWithoutIds {
    private VehicleType vehicleType;
    @Length(max = 100)
    private String model;
    @Length(max = 20)
    private String licenseNumber;

    private LocationDTO currentLocation;
    @Min(value=1)
    @Max(value=20)
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
