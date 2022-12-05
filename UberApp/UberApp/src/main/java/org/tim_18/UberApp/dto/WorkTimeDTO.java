package org.tim_18.UberApp.dto;

import jakarta.persistence.*;
import org.tim_18.UberApp.model.Driver;

import java.time.LocalDateTime;

public class WorkTimeDTO {

    private Integer id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Driver driver;


    public WorkTimeDTO() {}

    public WorkTimeDTO(Integer id, LocalDateTime startTime, LocalDateTime endTime, Driver driver) {
        this.id         = id;
        this.startTime  = startTime;
        this.endTime    = endTime;
        this.driver     = driver;
    }

    public LocalDateTime getStartTime() {return startTime;}
    public void setStartTime(LocalDateTime startTime) {this.startTime = startTime;}

    public LocalDateTime getEndTime() {return endTime;}
    public void setEndTime(LocalDateTime endTime) {this.endTime = endTime;}

    public Driver getDriver() {return driver;}
    public void setDriver(Driver driver) {this.driver = driver;}

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
}
