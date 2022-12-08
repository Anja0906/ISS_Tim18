package org.tim_18.UberApp.dto;

import jakarta.persistence.*;
import org.tim_18.UberApp.model.User;

import java.time.LocalDate;

public class UserActivationDTO {


    private Integer id;
    private User user;
    private LocalDate creationDate;
    private int duration;

    public UserActivationDTO(Integer id, User user, LocalDate creationDate, int duration) {
        this.id           = id;
        this.user         = user;
        this.creationDate = creationDate;
        this.duration     = duration;
    }

    public UserActivationDTO() {}

    public LocalDate getCreationDate() {return creationDate;}
    public void setCreationDate(LocalDate creationDate) {this.creationDate = creationDate;}

    public int getDuration() {return duration;}
    public void setDuration(int duration) {this.duration = duration;}

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
}
