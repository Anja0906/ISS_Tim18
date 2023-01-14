package org.tim_18.UberApp.dto.rideDTOs;

import lombok.Data;
import org.tim_18.UberApp.dto.locationDTOs.LocationSetDTO;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerEmailDTO;
import org.tim_18.UberApp.model.*;

import java.util.*;

@Data
public class FavoriteRideDTO {

    private Integer id;
    private String favoriteName;
    private Set<LocationSetDTO> locations;
    private Set<PassengerEmailDTO> passengers;
    private VehicleType vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

    public FavoriteRideDTO(){}

    public FavoriteRideDTO(Integer id, String favoriteName, Set<Location> _locations, Set<Passenger> passengers, VehicleType vehicleType, boolean babyTransport, boolean petTransport) {
        this.id                     = id;
        this.favoriteName           = favoriteName;
        this.vehicleType            = vehicleType;
        this.babyTransport          = babyTransport;
        this.petTransport           = petTransport;
        List<Location> locList = new ArrayList<>();
        for (Location loc : _locations){
            locList.add(loc);
        }
        Set<LocationSetDTO> locationSetDTOSet = new HashSet<>();
        for (int i = 1; i < locList.size(); i++) {
            Location loc1 = locList.get(i-1);
            Location loc2 = locList.get(i);
            locationSetDTOSet.add(new LocationSetDTO(loc1, loc2));
        }
        this.locations              = locationSetDTOSet;
        Set<PassengerEmailDTO> passengerSet = new HashSet<>();
        for (Passenger p:passengers) {
            passengerSet.add(new PassengerEmailDTO(p));
        }
        this.passengers             = passengerSet;
    }

    public FavoriteRideDTO(FavoriteRide ride, Passenger passenger){
        Set<Passenger> passengerSet = new HashSet<>();
        passengerSet.add(passenger);
        new FavoriteRideDTO(ride.getId(), ride.getFavoriteName(), ride.getLocations(),
                passengerSet, ride.getVehicleType(),
                ride.isBabyTransport(), ride.isPetTransport());
    }

    public FavoriteRideDTO(FavoriteRide ride){
        this(ride.getId(), ride.getFavoriteName(), ride.getLocations(),
                ride.getPassengers(), ride.getVehicleType(),
                ride.isBabyTransport(), ride.isPetTransport());
    }

    public static List<FavoriteRideDTO> getFavoriteRidesDTO(List<FavoriteRide> favoriteRides) {
        List<FavoriteRideDTO> favoriteRidesDTO = new ArrayList<>() ;
        for (FavoriteRide fr : favoriteRides) {
            favoriteRidesDTO.add(new FavoriteRideDTO(fr));
        }
        return favoriteRidesDTO;
    }
}
