package org.tim_18.UberApp.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tim_18.UberApp.dto.PassengerEmailDTO;
import org.tim_18.UberApp.model.Passenger;

@Component
public class PassengerEmailDTOMapper {
    @Autowired
    private static ModelMapper modelMapper;

    public PassengerEmailDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Passenger fromDTOtoPassenger(PassengerEmailDTO dto) {
        return modelMapper.map(dto, Passenger.class);
    }

    public PassengerEmailDTO fromPassengerToDTO(Passenger passenger) {
        return modelMapper.map(passenger, PassengerEmailDTO.class);
    }
}
