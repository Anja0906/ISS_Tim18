package org.tim_18.UberApp.dto.locationDTOs;

import lombok.Data;
import org.tim_18.UberApp.model.Location;

@Data
public class LocationSetDTO {
    private LocationDTO departure;
    private LocationDTO destination;

    public LocationSetDTO() {
    }

    public LocationSetDTO(Location departure, Location destination) {
        this.departure = new LocationDTO(departure);
        this.destination = new LocationDTO(destination);
    }


}
