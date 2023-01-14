package org.tim_18.UberApp.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim_18.UberApp.dto.AdministratorDTO;
import org.tim_18.UberApp.model.Administrator;

public class AdministratorDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public AdministratorDTOMapper(ModelMapper modelMapper) {this.modelMapper = modelMapper;}

    public static Administrator fromDTOtoAdministrator(AdministratorDTOMapper dto) {return modelMapper.map(dto, Administrator.class);}

    public static AdministratorDTO fromAdministratorToDTO(Administrator dto) {return modelMapper.map(dto, AdministratorDTO.class);}
}
