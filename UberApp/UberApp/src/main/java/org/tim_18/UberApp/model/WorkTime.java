package org.tim_18.UberApp.model;

import jakarta.persistence.*;
import org.tim_18.UberApp.dto.worktimeDTOs.WorkTimeDTOWithoutDriver;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "work_time")
public class WorkTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private Date start;
    private Date flagStart;
    private Date end;
    private Integer workedTimeInMinutes;
    @OneToOne (cascade = {CascadeType.ALL})
    private Driver driver;

    public WorkTime() {}
    public WorkTime(Date start, Date end, Driver driver,Date flagStart,Integer workedTimeInMinutes) {
        this.start               = start;
        this.flagStart           = flagStart;
        this.end                 = end;
        this.driver              = driver;
        this.workedTimeInMinutes = workedTimeInMinutes;
    }

    public void updateWorkingHour(Date date){
        if(getWorkedTimeInMinutes()+(int)( date.getTime()/60000 -  getFlagStart().getTime()/60000)>480)
            setWorkedTimeInMinutes(480);
        else
            setWorkedTimeInMinutes(getWorkedTimeInMinutes()+(int)( date.getTime()/60000 -  getFlagStart().getTime()/60000));

        setEnd(date);
        setFlagStart(date);
    }

    public void updateWorkingHourLogin(Date date){
        setEnd(date);
        setFlagStart(date);
    }


    public Date getFlagStart() {
        return flagStart;
    }

    public void setFlagStart(Date flagStart) {
        this.flagStart = flagStart;
    }

    public Integer getWorkedTimeInMinutes() {
        return workedTimeInMinutes;
    }


    public void setWorkedTimeInMinutes(Integer workedTimeInMinutes) {
        this.workedTimeInMinutes = workedTimeInMinutes;
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
                ", flagStart=" + flagStart +
                ", end=" + end +
                ", workedTimeInMinutes=" + workedTimeInMinutes +
                ", driver=" + driver +
                '}';
    }
}