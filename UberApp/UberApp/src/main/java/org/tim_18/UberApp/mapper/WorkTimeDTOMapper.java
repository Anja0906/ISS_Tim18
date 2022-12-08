package org.tim_18.UberApp.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim_18.UberApp.dto.UserDTO;
import org.tim_18.UberApp.dto.WorkTimeDTO;
import org.tim_18.UberApp.model.User;
import org.tim_18.UberApp.model.WorkTime;

public class WorkTimeDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public WorkTimeDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public static WorkTime fromDTOtoWorkTIme(WorkTimeDTO dto) {
        return modelMapper.map(dto, WorkTime.class);
    }

    public static WorkTimeDTO fromWorkTimetoDTO(WorkTime dto) {
        return modelMapper.map(dto, WorkTimeDTO.class);
    }
}
