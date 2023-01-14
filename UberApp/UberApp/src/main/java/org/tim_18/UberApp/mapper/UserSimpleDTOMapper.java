package org.tim_18.UberApp.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim_18.UberApp.dto.UserSimpleDTO;
import org.tim_18.UberApp.model.User;

public class UserSimpleDTOMapper {

    private static ModelMapper modelMapper;

    @Autowired
    public UserSimpleDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    public static User fromDTOtoUser(UserSimpleDTO dto) {
        return modelMapper.map(dto, User.class);
    }

    public static UserSimpleDTO fromUserToDTO(User dto) {
        return modelMapper.map(dto, UserSimpleDTO.class);
    }
}
