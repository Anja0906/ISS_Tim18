package org.tim_18.UberApp.mapper.userDTOmappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tim_18.UberApp.dto.userDTOs.UserDTOwithPassword;
import org.tim_18.UberApp.model.User;

@Component
public class UserDTOwithPasswordMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public UserDTOwithPasswordMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User fromDTOtoUser(UserDTOwithPassword dto) {
        return modelMapper.map(dto, User.class);
    }

    public UserDTOwithPassword fromUserToDTO(User dto) {
        return modelMapper.map(dto, UserDTOwithPassword.class);
    }
}
