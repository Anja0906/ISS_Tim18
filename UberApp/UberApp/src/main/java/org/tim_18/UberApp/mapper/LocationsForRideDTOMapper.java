package org.tim_18.UberApp.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim_18.UberApp.dto.LocationsForRideDTO;
import org.tim_18.UberApp.model.LocationsForRide;

public class LocationsForRideDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public LocationsForRideDTOMapper(ModelMapper modelMapper) {this.modelMapper = modelMapper;}

    public static LocationsForRide fromDTOtoLocationsForRide(LocationsForRideDTOMapper dto) {return modelMapper.map(dto, LocationsForRide.class);}

    public static LocationsForRideDTO fromLocationsForRideToDTO(LocationsForRide dto) {return modelMapper.map(dto, LocationsForRideDTO.class);}
}
