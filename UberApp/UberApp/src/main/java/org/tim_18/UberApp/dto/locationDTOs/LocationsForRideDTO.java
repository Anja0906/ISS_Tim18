package org.tim_18.UberApp.dto.locationDTOs;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import org.tim_18.UberApp.model.Location;

public class LocationsForRideDTO {
    private Location departure;
    private Location destination;

    public LocationsForRideDTO() {
    }

    public LocationsForRideDTO(Location departure, Location destination) {
        this.departure = departure;
        this.destination = destination;
    }

    public Location getDeparture() {
        return departure;
    }

    public void setDeparture(Location departure) {
        this.departure = departure;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }
}
