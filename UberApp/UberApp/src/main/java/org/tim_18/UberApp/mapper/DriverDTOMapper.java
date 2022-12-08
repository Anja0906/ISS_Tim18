package org.tim_18.UberApp.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim_18.UberApp.dto.DriverDTO;
import org.tim_18.UberApp.dto.UserActivationDTO;
import org.tim_18.UberApp.model.Driver;
import org.tim_18.UberApp.model.User;

public class DriverDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public DriverDTOMapper(ModelMapper modelMapper) {this.modelMapper = modelMapper;}

    public static Driver fromDTOtoDriver(DriverDTOMapper dto) {return modelMapper.map(dto, Driver.class);}

    public static DriverDTO fromDrivertoDTO(Driver dto) {return modelMapper.map(dto, DriverDTO.class);}
}
