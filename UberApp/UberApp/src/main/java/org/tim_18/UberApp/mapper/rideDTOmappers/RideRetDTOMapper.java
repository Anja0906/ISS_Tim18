package org.tim_18.UberApp.mapper.rideDTOmappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tim_18.UberApp.dto.rideDTOs.RideRetDTO;
import org.tim_18.UberApp.model.Ride;

@Component
public class RideRetDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public RideRetDTOMapper(ModelMapper modelMapper) {this.modelMapper = modelMapper;}

    public Ride fromDTOtoRide(RideRetDTO dto) {return modelMapper.map(dto, Ride.class);}

    public RideRetDTO fromRideToDTO(Ride dto) {return modelMapper.map(dto, RideRetDTO.class);}

}
