package org.tim_18.UberApp.mapper.rideDTOmappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tim_18.UberApp.model.Ride;

@Component
public class FavouriteRideDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public FavouriteRideDTOMapper(ModelMapper modelMapper) {this.modelMapper = modelMapper;}

    public Ride fromDTOtoRide(FavouriteRideDTOMapper dto) {return modelMapper.map(dto, Ride.class);}

    public FavouriteRideDTOMapper fromLocationToDTO(Ride dto) {return modelMapper.map(dto, FavouriteRideDTOMapper.class);}
}