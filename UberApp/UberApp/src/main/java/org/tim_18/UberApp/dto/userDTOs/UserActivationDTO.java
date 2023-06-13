package org.tim_18.UberApp.dto.userDTOs;

import jakarta.persistence.*;
import lombok.Data;
import org.tim_18.UberApp.model.User;

import java.time.LocalDate;
@Data
public class UserActivationDTO {


    private Integer id;
    private User user;
    private LocalDate creationDate;
    private int duration;

    public UserActivationDTO() {}
    public UserActivationDTO(Integer id, User user, LocalDate creationDate, int duration) {
        this.id           = id;
        this.user         = user;
        this.creationDate = creationDate;
        this.duration     = duration;
    }

}
