package org.tim_18.UberApp.dto.rideDTOs;

import lombok.Data;
import org.tim_18.UberApp.dto.locationDTOs.LocationDTO;
import org.tim_18.UberApp.dto.locationDTOs.LocationSetDTO;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerIdEmailDTO;
import org.tim_18.UberApp.model.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Data
public class FavoriteRideWithTimeDTO {

    private Integer id;
    private String favoriteName;
    private Set<LocationSetDTO> locations;
    private Set<PassengerIdEmailDTO> passengers;
    private VehicleType vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
    private Date scheduledTime;

    public FavoriteRideWithTimeDTO(Integer id, String favoriteName, Set<LocationsForFavoriteRide> locations, Set<Passenger> passengers, VehicleType vehicleType, boolean babyTransport, boolean petTransport, Date scheduledTime) {
        this.id = id;
        this.favoriteName = favoriteName;
        Set<PassengerIdEmailDTO> passengerSet = new HashSet<>();
        for (Passenger p:passengers) {
            passengerSet.add(new PassengerIdEmailDTO(p));
        }
        this.passengers                     = passengerSet;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
        this.scheduledTime = scheduledTime;
        Set<LocationSetDTO> locationSetDTOSet = new HashSet<>();
        for (LocationsForFavoriteRide loc : locations){
            LocationSetDTO locationSetDTO = new LocationSetDTO();
            locationSetDTO.setDeparture(new LocationDTO(loc.getDeparture()));
            locationSetDTO.setDestination(new LocationDTO(loc.getDestination()));
            locationSetDTOSet.add(locationSetDTO);
        }
        this.locations = locationSetDTOSet;
    }

    //    public FavoriteRideWithTimeDTO(FavoriteRide ride, Passenger passenger, Date scheduledTime) {
//        super(ride, passenger);
//        this.scheduledTime = scheduledTime;
//    }

    public FavoriteRideWithTimeDTO(FavoriteRide ride, Date date, Set<LocationsForFavoriteRide> locations) {
        this(ride.getId(), ride.getFavoriteName(), locations, ride.getPassengers(), ride.getVehicleType(), ride.isBabyTransport(), ride.isPetTransport(), date);
    }


    public FavoriteRideWithTimeDTO(FavoriteRide ride, Set<LocationsForFavoriteRide> locations) {
        this(ride.getId(), ride.getFavoriteName(), locations, ride.getPassengers(), ride.getVehicleType(), ride.isBabyTransport(), ride.isPetTransport(), new Date());
    }
}
