package org.tim_18.UberApp.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim_18.UberApp.dto.PanicDTO;
import org.tim_18.UberApp.model.Panic;

public class PanicDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public PanicDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Panic fromDTOtoRejection(PanicDTO dto) {
        return modelMapper.map(dto, Panic.class);
    }

    public PanicDTO fromRejectionToDTO(Panic panic) {
        return modelMapper.map(panic, PanicDTO.class);
    }
}
