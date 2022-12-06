package org.tim_18.UberApp.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim_18.UberApp.dto.PassengerDTO;
import org.tim_18.UberApp.dto.UserDTO;
import org.tim_18.UberApp.model.Passenger;
import org.tim_18.UberApp.model.User;

public class PassengerDTOMapper {


    private static ModelMapper modelMapper;

    @Autowired
    public PassengerDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public static Passenger fromDTOtoPassenger(PassengerDTO dto) {
        return modelMapper.map(dto, Passenger.class);
    }

    public static PassengerDTO fromPassengerToDTO(Passenger dto) {
        return modelMapper.map(dto, PassengerDTO.class);
    }
}
