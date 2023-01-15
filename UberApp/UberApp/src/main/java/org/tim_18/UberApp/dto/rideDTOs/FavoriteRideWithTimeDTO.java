package org.tim_18.UberApp.dto.rideDTOs;

import org.tim_18.UberApp.model.FavoriteRide;
import org.tim_18.UberApp.model.Location;
import org.tim_18.UberApp.model.Passenger;
import org.tim_18.UberApp.model.VehicleType;

import java.util.Date;
import java.util.Set;

public class FavoriteRideWithTimeDTO extends FavoriteRideDTO {
    private Date scheduledTime;

    public FavoriteRideWithTimeDTO(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public FavoriteRideWithTimeDTO(Integer id, String favoriteName, Set<Location> locations, Set<Passenger> passengers, VehicleType vehicleType, boolean babyTransport, boolean petTransport, Date scheduledTime) {
        super(id, favoriteName, locations, passengers, vehicleType, babyTransport, petTransport);
        this.scheduledTime = scheduledTime;
    }

    public FavoriteRideWithTimeDTO(FavoriteRide ride, Passenger passenger, Date scheduledTime) {
        super(ride, passenger);
        this.scheduledTime = scheduledTime;
    }

    public FavoriteRideWithTimeDTO(FavoriteRide ride, Date scheduledTime) {
        super(ride);
        this.scheduledTime = scheduledTime;
    }
}
