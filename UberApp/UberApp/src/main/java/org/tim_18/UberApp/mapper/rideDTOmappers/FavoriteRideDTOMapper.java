package org.tim_18.UberApp.mapper.rideDTOmappers;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tim_18.UberApp.dto.rideDTOs.FavoriteRideDTO;
import org.tim_18.UberApp.model.FavoriteRide;

@Component
public class FavoriteRideDTOMapper {
    private static ModelMapper modelMapper;



    @Autowired
    public FavoriteRideDTOMapper(ModelMapper modelMapper) {this.modelMapper = modelMapper;
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STANDARD);}

    public FavoriteRide fromDTOtoRide(FavoriteRideDTO dto) {return modelMapper.map(dto, FavoriteRide.class);}

    public FavoriteRideDTO fromRideToDTO(FavoriteRide dto) {return modelMapper.map(dto, FavoriteRideDTO.class);}
}