package org.tim_18.UberApp.mapper.passengerDTOmappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerDTOwithPassword;
import org.tim_18.UberApp.model.Passenger;

@Component
public class PassengerDTOwithPasswordMapper {


    private static ModelMapper modelMapper;

    public PassengerDTOwithPasswordMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Passenger fromDTOtoPassenger(PassengerDTOwithPassword dto) {
        return modelMapper.map(dto, Passenger.class);
    }

    public PassengerDTOwithPassword fromPassengerToDTO(Passenger passenger) {
        return modelMapper.map(passenger, PassengerDTOwithPassword.class);
    }

    public Passenger fromDTOtoPassenger(PassengerDTOwithPassword dto, Integer id) {
        dto.setId(id);
        return modelMapper.map(dto, Passenger.class);
    }
}
