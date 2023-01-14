package org.tim_18.UberApp.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim_18.UberApp.dto.locationDTOs.LocationDTO;
import org.tim_18.UberApp.model.Location;

public class LocationDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public LocationDTOMapper(ModelMapper modelMapper) {this.modelMapper = modelMapper;}

    public static Location fromDTOtoLocation(LocationDTO dto) {return modelMapper.map(dto, Location.class);}

    public static LocationDTO fromLocationToDTO(Location dto) {return modelMapper.map(dto, LocationDTO.class);}
}
