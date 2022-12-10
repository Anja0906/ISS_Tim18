package org.tim_18.UberApp.dto.rideDTOs;

import lombok.Data;
import org.tim_18.UberApp.dto.LocationsForRideDTO;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerEmailDTO;
import org.tim_18.UberApp.model.Location;
import org.tim_18.UberApp.model.Passenger;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.model.VehicleType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class RideRecDTO {
    private Integer id;
    private Set<LocationsForRideDTO> locations;
    private Set<PassengerEmailDTO> passengers;
    private VehicleType vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

    public RideRecDTO(){}

    public RideRecDTO(Integer id, Set<Location> _locations, Set<Passenger> passengers, VehicleType vehicleType, boolean babyTransport, boolean petTransport) {
        this.id = id;
        List<Location> locList = new ArrayList<>();
        for (Location loc : _locations){
            locList.add(loc);
        }
        Set<LocationsForRideDTO> locationsForRideDTOSet = new HashSet<>();
        locationsForRideDTOSet.add(new LocationsForRideDTO(locList.get(0), locList.get(1)));
        this.locations = locationsForRideDTOSet;
        Set<PassengerEmailDTO> passengerSet = new HashSet<>();
        for (Passenger p:passengers) {
            passengerSet.add(new PassengerEmailDTO(p));
        }
        this.passengers = passengerSet;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
    }

    public RideRecDTO(Ride ride){
        this(ride.getId(), ride.getLocations(), ride.getPassengers(), ride.getVehicleType(), ride.isBabyTransport(), ride.isPetTransport());
    }
}
