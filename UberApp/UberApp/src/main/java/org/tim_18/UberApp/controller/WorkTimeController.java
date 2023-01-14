package org.tim_18.UberApp.controller;

import org.hibernate.jdbc.Work;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.model.User;
import org.tim_18.UberApp.model.WorkTime;
import org.tim_18.UberApp.service.UserService;
import org.tim_18.UberApp.service.WorkTimeService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/worktime")
public class WorkTimeController {
    private final WorkTimeService workTimeService;

    public WorkTimeController(WorkTimeService workTimeService) {
        this.workTimeService = workTimeService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<WorkTime>> getAllWorkTimes () {
        List<WorkTime> workTimes = workTimeService.findAllWorkTime();
        return new ResponseEntity<>(workTimes, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<WorkTime> getWorkTimeById (@PathVariable("id") Integer id) {
        WorkTime workTime = workTimeService.findWorkTimeById(id);
        return new ResponseEntity<>(workTime, HttpStatus.OK);
    }

    @GetMapping("/{id}/working-hours")
    public ResponseEntity<ArrayList<WorkTime>> getWorkTimesByDriverId (@PathVariable("id") Integer id) {
        ArrayList<WorkTime> driversWorkTimes = workTimeService.findByDriversId(id);
        return new ResponseEntity<>(driversWorkTimes, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<WorkTime> addWorkTIme(@RequestBody WorkTime workTime) {
        WorkTime newWorkTime = workTimeService.addWorkTime(workTime);
        return new ResponseEntity<>(newWorkTime, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<WorkTime> updateWorkTIme(@RequestBody WorkTime workTime) {
        WorkTime updateWorkTime = workTimeService.updateWorkTime(workTime);
        return new ResponseEntity<>(updateWorkTime, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id) {
        workTimeService.deleteWorkTime(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
