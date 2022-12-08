package org.tim_18.UberApp.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim_18.UberApp.dto.UserDTO;
import org.tim_18.UberApp.dto.VehicleDTO;
import org.tim_18.UberApp.model.User;
import org.tim_18.UberApp.model.Vehicle;

public class VehicleDTOMap {
    private static ModelMapper modelMapper;

    @Autowired
    public VehicleDTOMap(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public static Vehicle fromDTOtoVehicle(VehicleDTO dto) {
        return modelMapper.map(dto, Vehicle.class);
    }

    public static VehicleDTO fromVehicleToDTO(Vehicle dto) {
        return modelMapper.map(dto, VehicleDTO.class);
    }
}
