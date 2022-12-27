package org.tim_18.UberApp.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.tim_18.UberApp.model.WorkTime;

import java.time.LocalDateTime;
import java.util.HashSet;
@Data
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


    @Override
    public String toString() {
        return "WorkTimeDTOWithoutDriver{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
