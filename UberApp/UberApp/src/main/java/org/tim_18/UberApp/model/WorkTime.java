package org.tim_18.UberApp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "work_time")
public class WorkTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @OneToOne (cascade = {CascadeType.ALL})
    private Driver driver;


    public WorkTime() {}

    public WorkTime(Integer id, LocalDateTime startTime, LocalDateTime endTime, Driver driver) {
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