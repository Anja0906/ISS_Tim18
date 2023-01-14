package org.tim_18.UberApp.dto.locationDTOs;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.tim_18.UberApp.model.Location;
@Data
public class LocationsForRideDTO {
    private Location departure;
    private Location destination;

    public LocationsForRideDTO() {}

    public LocationsForRideDTO(Location departure, Location destination) {
        this.departure      = departure;
        this.destination    = destination;
    }


}
