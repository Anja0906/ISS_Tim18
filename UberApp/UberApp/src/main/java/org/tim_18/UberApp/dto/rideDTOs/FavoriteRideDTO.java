package org.tim_18.UberApp.dto.rideDTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.tim_18.UberApp.dto.locationDTOs.LocationSetDTO;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerIdEmailDTO;
import org.tim_18.UberApp.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoriteRideDTO {

    private Integer id;

    @NotNull
    private String favoriteName;

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
        Set<PassengerIdEmailDTO> passengerSet = new HashSet<>();
        for (Passenger p:passengers) {
            passengerSet.add(new PassengerIdEmailDTO(p));
        }
        this.passengers             = passengerSet;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFavoriteName() {
        return favoriteName;
    }

    public void setFavoriteName(String favoriteName) {
        this.favoriteName = favoriteName;
    }

    public Set<LocationSetDTO> getLocations() {
        return locations;
    }

    public void setLocations(Set<LocationSetDTO> locations) {
        this.locations = locations;
    }

    public Set<PassengerIdEmailDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<PassengerIdEmailDTO> passengers) {
        this.passengers = passengers;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isBabyTransport() {
        return babyTransport;
    }

    public void setBabyTransport(boolean babyTransport) {
        this.babyTransport = babyTransport;
    }

    public boolean isPetTransport() {
        return petTransport;
    }

    public void setPetTransport(boolean petTransport) {
        this.petTransport = petTransport;
    }
}
