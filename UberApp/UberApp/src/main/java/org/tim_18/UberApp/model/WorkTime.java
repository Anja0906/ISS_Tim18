package org.tim_18.UberApp.model;

import jakarta.persistence.*;
import org.tim_18.UberApp.dto.WorkTimeDTOWithoutDriver;

import java.time.LocalDateTime;

@Entity
@Table(name = "work_time")
public class WorkTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private LocalDateTime start;
    private LocalDateTime end;
    @OneToOne (cascade = {CascadeType.ALL})
    private Driver driver;


    public WorkTime() {}

    public WorkTime(Integer id, LocalDateTime start, LocalDateTime end, Driver driver) {
        this.id         = id;
        this.start      = start;
        this.end        = end;
        this.driver     = driver;
    }

    public WorkTime(LocalDateTime start, LocalDateTime end, Driver driver) {
        this.start      = start;
        this.end        = end;
        this.driver     = driver;
    }
    public void updateWorkTime(WorkTimeDTOWithoutDriver workTimeDTOWithoutDriver){
        setStart(workTimeDTOWithoutDriver.getStart());
        setEnd(workTimeDTOWithoutDriver.getEnd());
    }

    public LocalDateTime getStart() {return start;}
    public void setStart(LocalDateTime start) {this.start = start;}

    public LocalDateTime getEnd() {return end;}
    public void setEnd(LocalDateTime end) {this.end = end;}

    public Driver getDriver() {return driver;}
    public void setDriver(Driver driver) {this.driver = driver;}

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "WorkTime{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", driver=" + driver +
                '}';
    }
}