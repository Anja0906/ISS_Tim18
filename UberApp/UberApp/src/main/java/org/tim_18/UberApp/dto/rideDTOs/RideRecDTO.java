package org.tim_18.UberApp.dto.rideDTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.tim_18.UberApp.dto.RejectionDTO;
import org.tim_18.UberApp.dto.driverDTOs.DriverEmailDTO;
import org.tim_18.UberApp.dto.locationDTOs.LocationDTO;
import org.tim_18.UberApp.dto.locationDTOs.LocationSetDTO;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerIdEmailDTO;
import org.tim_18.UberApp.model.*;

import java.util.*;

@Data
public class RideRecDTO {
    private Integer id;
    @NotNull
    private Set<LocationSetDTO> locations;

    @NotNull
    private Set<PassengerIdEmailDTO> passengers;

    @NotNull
    private VehicleType vehicleType;

    @NotNull
    private boolean babyTransport;

    @NotNull
    private boolean petTransport;

    @NotNull
    private String scheduledTime;

    public RideRecDTO(){}

    public RideRecDTO(Integer id, Set<Location> _locations, Set<Passenger> passengers, VehicleType vehicleType, boolean babyTransport, boolean petTransport, String scheduledTime) {
        this.id                     = id;
        this.vehicleType            = vehicleType;
        this.babyTransport          = babyTransport;
        this.petTransport           = petTransport;
        this.scheduledTime          = scheduledTime;
        List<Location> locList = new ArrayList<>();
        for (Location loc : _locations){
            locList.add(loc);
        }
        Set<LocationSetDTO> locationSetDTOSet = new HashSet<>();
        for (int i = 1; i < locList.size(); i++) {
            locationSetDTOSet.add(new LocationSetDTO(locList.get(i-1), locList.get(i)));
        }
        this.locations              = locationSetDTOSet;
        Set<PassengerIdEmailDTO> passengerSet = new HashSet<>();
        for (Passenger p:passengers) {
            passengerSet.add(new PassengerIdEmailDTO(p));
        }
        this.passengers             = passengerSet;
    }

//    public RideRecDTO(Ride ride){
//        this(ride.getId(), ride.getLocations(),
//             ride.getPassengers(), ride.getVehicleType(),
//             ride.isBabyTransport(), ride.isPetTransport(), ride.getScheduledTime().toString());
//    }

    public RideRecDTO(Integer id, Set<Passenger> passengers, VehicleType vehicleType, boolean babyTransport, boolean petTransport, Set<LocationsForRide> _locations, Date scheduledTime) {
        this.id                             = id;
        Set<PassengerIdEmailDTO> passengerSet = new HashSet<>();
        for (Passenger p:passengers) {
            passengerSet.add(new PassengerIdEmailDTO(p));
        }
        this.passengers                     = passengerSet;
        this.vehicleType                    = vehicleType;
        this.babyTransport                  = babyTransport;
        this.petTransport                   = petTransport;
        Set<LocationSetDTO> locationSetDTOSet = new HashSet<>();
        for (LocationsForRide loc : _locations){
            LocationSetDTO locationSetDTO = new LocationSetDTO();
            locationSetDTO.setDeparture(new LocationDTO(loc.getDeparture()));
            locationSetDTO.setDestination(new LocationDTO(loc.getDestination()));
            locationSetDTOSet.add(locationSetDTO);
        }
        this.locations                      = locationSetDTOSet;
        if (scheduledTime==null) {
            this.scheduledTime              = "";
        }
        else {
            this.scheduledTime              = scheduledTime.toString();
        }
    }
    public RideRecDTO(Ride ride, Set<LocationsForRide> locations){
        this(ride.getId(),
                ride.getPassengers(),
                ride.getVehicleType(), ride.isBabyTransport(),
                ride.isPetTransport(),
                locations, ride.getScheduledTime());

    }
}
