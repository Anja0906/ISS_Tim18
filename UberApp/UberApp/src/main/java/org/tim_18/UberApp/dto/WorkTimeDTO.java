package org.tim_18.UberApp.dto;

import jakarta.persistence.*;
import org.hibernate.jdbc.Work;
import org.springframework.data.domain.Page;
import org.tim_18.UberApp.dto.driverDTOs.DriverDTO;
import org.tim_18.UberApp.model.Driver;
import org.tim_18.UberApp.model.WorkTime;

import java.time.LocalDateTime;
import java.util.HashSet;

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
    public WorkTimeDTO(WorkTime workTime) {
        this(workTime.getId(),workTime.getStart(),workTime.getEnd());
    }

    public WorkTimeDTO(Integer id, LocalDateTime startTime, LocalDateTime endTime) {
        this.id         = id;
        this.startTime  = startTime;
        this.endTime    = endTime;
    }

    public HashSet<WorkTimeDTO> makeWorkTimeDTO(Page<WorkTime> workTimes){
        HashSet<WorkTimeDTO>workTimeDTOS = new HashSet<>();
        for(WorkTime workTime: workTimes){
            workTimeDTOS.add(new WorkTimeDTO(workTime));
        }
        return workTimeDTOS;
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
