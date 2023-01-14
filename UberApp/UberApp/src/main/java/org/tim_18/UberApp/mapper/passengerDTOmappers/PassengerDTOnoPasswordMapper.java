package org.tim_18.UberApp.mapper.passengerDTOmappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerDTOnoPassword;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerDTOwithPassword;
import org.tim_18.UberApp.model.Passenger;

@Component
public class PassengerDTOnoPasswordMapper {

    private static ModelMapper modelMapper;

    public PassengerDTOnoPasswordMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Passenger fromDTOtoPassenger(PassengerDTOnoPassword dto) {
        return modelMapper.map(dto, Passenger.class);
    }

    public PassengerDTOnoPassword fromPassengerToDTO(Passenger passenger) {
        return modelMapper.map(passenger, PassengerDTOnoPassword.class);
    }
}
