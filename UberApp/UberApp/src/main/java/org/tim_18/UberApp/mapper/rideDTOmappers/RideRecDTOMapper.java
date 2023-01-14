package org.tim_18.UberApp.mapper.rideDTOmappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tim_18.UberApp.dto.rideDTOs.RideRecDTO;
import org.tim_18.UberApp.model.Ride;

@Component
public class RideRecDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public RideRecDTOMapper(ModelMapper modelMapper) {this.modelMapper = modelMapper;}

    public Ride fromDTOtoRide(RideRecDTO dto) {return modelMapper.map(dto, Ride.class);}

    public RideRecDTO fromRideToDTO(Ride dto) {return modelMapper.map(dto, RideRecDTO.class);}
}
