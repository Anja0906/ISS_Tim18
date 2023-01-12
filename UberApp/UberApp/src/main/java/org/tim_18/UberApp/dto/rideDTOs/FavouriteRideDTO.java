package org.tim_18.UberApp.dto.rideDTOs;

import lombok.Data;
import org.tim_18.UberApp.dto.locationDTOs.LocationSetDTO;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerEmailDTO;
import org.tim_18.UberApp.model.Location;
import org.tim_18.UberApp.model.Passenger;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.model.VehicleType;

import java.util.*;

@Data
public class FavouriteRideDTO {
    private Integer id;
    private String favouriteName;
    private Set<LocationSetDTO> locations;
    private PassengerEmailDTO passenger;
    private VehicleType vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

    public FavouriteRideDTO(){}

    public FavouriteRideDTO(Integer id, Set<Location> _locations, Passenger passenger, VehicleType vehicleType, boolean babyTransport, boolean petTransport) {
        this.id                     = id;
        this.vehicleType            = vehicleType;
        this.babyTransport          = babyTransport;
        this.petTransport           = petTransport;
        List<Location> locList = new ArrayList<>();
        for (Location loc : _locations){
            locList.add(loc);
        }
        Set<LocationSetDTO> locationSetDTOSet = new HashSet<>();
        for (int i = 1; i < locList.size(); i++) {
            locationSetDTOSet.add(new LocationSetDTO(locList.get(i-1), locList.get(i)));
        }
        this.locations              = locationSetDTOSet;
        this.passenger             = new PassengerEmailDTO(passenger);
    }

    public FavouriteRideDTO(Ride ride, Passenger passenger){
        this(ride.getId(), ride.getLocations(),
                passenger, ride.getVehicleType(),
                ride.isBabyTransport(), ride.isPetTransport());
    }
}
