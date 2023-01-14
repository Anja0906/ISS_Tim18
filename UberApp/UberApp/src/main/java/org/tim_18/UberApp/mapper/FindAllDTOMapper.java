package org.tim_18.UberApp.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;
import org.tim_18.UberApp.dto.FindAllDTO;

import java.lang.reflect.Type;

@Component
public class FindAllDTOMapper<T> {
    private static ModelMapper modelMapper;

    public FindAllDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public T fromDTOtoObject(FindAllDTO<T> dto) {
        Type type = new TypeToken<T>() {}.getType();
        return modelMapper.map(dto, type);
    }

    public FindAllDTO fromObjectToDTO(T object) {
        return modelMapper.map(object, FindAllDTO.class);
    }
}

