package org.tim_18.UberApp.dto.driverDTOs;

import lombok.Data;
import org.tim_18.UberApp.model.Driver;


@Data
public class DriverEmailDTO {
    private Integer id;
    private String email;

    public DriverEmailDTO(){}

    public DriverEmailDTO(Driver driver) {
        this(driver.getId(), driver.getEmail());
    }

    public DriverEmailDTO(Integer id, String email) {
        this.id     = id;
        this.email  = email;
    }
}
