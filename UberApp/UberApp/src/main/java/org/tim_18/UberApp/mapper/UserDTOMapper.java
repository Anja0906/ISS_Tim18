package org.tim_18.UberApp.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.tim_18.UberApp.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.tim_18.UberApp.model.User;

public class UserDTOMapper {

    private static ModelMapper modelMapper;

    @Autowired
    public UserDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public static User fromDTOtoUser(UserDTO dto) {
        return modelMapper.map(dto, User.class);
    }

    public static UserDTO fromUserToDTO(User dto) {
        return modelMapper.map(dto, UserDTO.class);
    }
}
