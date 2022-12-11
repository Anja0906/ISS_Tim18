package org.tim_18.UberApp.dto;

import org.springframework.data.domain.Page;
import org.tim_18.UberApp.model.WorkTime;

import java.time.LocalDateTime;
import java.util.HashSet;

public class WorkTimeDTOWithoutDriver {
    private Integer id;
    private LocalDateTime start;
    private LocalDateTime end;


    public WorkTimeDTOWithoutDriver() {}

    public WorkTimeDTOWithoutDriver(Integer id, LocalDateTime start, LocalDateTime end) {
        this.id         = id;
        this.start      = start;
        this.end        = end;
    }
    public WorkTimeDTOWithoutDriver(WorkTime workTime) {
        this(workTime.getId(),workTime.getStart(),workTime.getEnd());
    }

    public HashSet<WorkTimeDTOWithoutDriver> makeWorkTimeDTOWithoutDriver(Page<WorkTime> workTimes){
        HashSet<WorkTimeDTOWithoutDriver>workTimeDTOS = new HashSet<>();
        for(WorkTime workTime: workTimes){
            workTimeDTOS.add(new WorkTimeDTOWithoutDriver(workTime));
        }
        return workTimeDTOS;
    }


    public LocalDateTime getStart() {return start;}
    public void setStart(LocalDateTime start) {this.start = start;}

    public LocalDateTime getEnd() {return end;}


    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "WorkTimeDTOWithoutDriver{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
