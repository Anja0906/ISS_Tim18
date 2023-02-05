package org.tim_18.UberApp.dto.locationDTOs;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.Value;
import org.tim_18.UberApp.model.Location;
@Data
public class LocationDTO {

    private String address;
    @Min(value=-90)
    @Max(value=90)
    private Double latitude;
    @Min(value=-180)
    @Max(value=180)
    private Double longitude;


    public LocationDTO() {}

    public LocationDTO(String address, Double longitude, Double latitude) {
        this.address    = address;
        this.latitude   = latitude;
        this.longitude  = longitude;

    }
    public LocationDTO(Location location){
        this(location.getAddress(),location.getLongitude(),
             location.getLatitude());
    }

}
