package org.tim_18.UberApp.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim_18.UberApp.dto.UserActivationDTO;
import org.tim_18.UberApp.dto.UserDTO;
import org.tim_18.UberApp.model.User;
import org.tim_18.UberApp.model.UserActivation;

public class UserActivationDTOMapper {

    private static ModelMapper modelMapper;

    @Autowired
    public UserActivationDTOMapper(ModelMapper modelMapper) {this.modelMapper = modelMapper;
    }

    public static UserActivation fromDTOtoUser(UserActivationDTOMapper dto) {
        return modelMapper.map(dto, UserActivation.class);
    }

    public static UserActivationDTO fromUsertoDTO(User dto) {
        return modelMapper.map(dto, UserActivationDTO.class);
    }
}

