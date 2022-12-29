package org.tim_18.UberApp.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim_18.UberApp.dto.RejectionDTO;
import org.tim_18.UberApp.model.Rejection;

public class RejectionDTOMapper {

    private static ModelMapper modelMapper;

    @Autowired
    public RejectionDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Rejection fromDTOtoRejection(RejectionDTO dto) {
        return modelMapper.map(dto, Rejection.class);
    }

    public RejectionDTO fromRejectionToDTO(Rejection rejection) {
        return modelMapper.map(rejection, RejectionDTO.class);
    }


}
