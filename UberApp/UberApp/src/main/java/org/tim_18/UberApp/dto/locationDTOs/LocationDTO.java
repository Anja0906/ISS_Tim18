package org.tim_18.UberApp.dto.locationDTOs;

import lombok.Data;
import org.tim_18.UberApp.model.Location;
@Data
public class LocationDTO {

    private String address;
    private Double latitude;
    private Double longitude;


    public LocationDTO() {}

    public LocationDTO(String address, Double longitude, Double latitude) {
        this.address    = address;
        this.latitude   = latitude;
        this.longitude  = longitude;

    }
    public LocationDTO(Location location){
        this(location.getAddress(),location.getLatitude(),
             location.getLongitude());
    }

}
