package org.tim_18.UberApp.mapper.passengerDTOmappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerEmailDTO;
import org.tim_18.UberApp.model.Passenger;

@Component
public class PassengerEmailDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
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
