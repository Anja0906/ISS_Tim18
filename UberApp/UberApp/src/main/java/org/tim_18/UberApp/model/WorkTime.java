package org.tim_18.UberApp.model;

import jakarta.persistence.*;
import org.tim_18.UberApp.dto.WorkTimeDTOWithoutDriver;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "work_time")
public class WorkTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private Date start;
    private Date end;
    @OneToOne (cascade = {CascadeType.ALL})
    private Driver driver;

    public WorkTime() {}
    public WorkTime(Integer id, Date start, Date end, Driver driver) {
        this.id         = id;
        this.start      = start;
        this.end        = end;
        this.driver     = driver;
    }
    public WorkTime(Date start, Date end, Driver driver) {
        this.start      = start;
        this.end        = end;
        this.driver     = driver;
    }
    public void updateWorkTime(WorkTimeDTOWithoutDriver workTimeDTOWithoutDriver){
        Instant instant = Instant.parse(workTimeDTOWithoutDriver.getStart());
        setStart(Date.from(instant));
        instant = Instant.parse(workTimeDTOWithoutDriver.getEnd());
        setEnd(Date.from(instant));
    }

    public Date getStart() {return start;}
    public void setStart(Date start) {this.start = start;}

    public Date getEnd() {return end;}
    public void setEnd(Date end) {this.end = end;}

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