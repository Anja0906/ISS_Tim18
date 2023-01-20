package org.tim_18.UberApp.dto;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.jdbc.Work;
import org.springframework.data.domain.Page;
import org.tim_18.UberApp.dto.driverDTOs.DriverDTO;
import org.tim_18.UberApp.model.Driver;
import org.tim_18.UberApp.model.WorkTime;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
@Data
public class WorkTimeDTO {

    private Integer id;
    private String startTime;
    private String endTime;
    private Driver driver;


    public WorkTimeDTO() {}

    public WorkTimeDTO(Integer id, Date startTime, Date endTime, Driver driver) {
        this.id         = id;
        this.startTime  = startTime.toString();
        this.endTime    = endTime.toString();
        this.driver     = driver;
    }
    public WorkTimeDTO(WorkTime workTime) {
        this(workTime.getId(),workTime.getStart(),workTime.getEnd());
    }

    public WorkTimeDTO(Integer id, Date startTime, Date endTime) {
        this.id         = id;
        this.startTime  = startTime.toString();
        this.endTime    = endTime.toString();
    }

    public HashSet<WorkTimeDTO> makeWorkTimeDTO(Page<WorkTime> workTimes){
        HashSet<WorkTimeDTO>workTimeDTOS = new HashSet<>();
        for(WorkTime workTime: workTimes){
            workTimeDTOS.add(new WorkTimeDTO(workTime));
        }
        return workTimeDTOS;
    }
}
