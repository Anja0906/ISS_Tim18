package org.tim_18.UberApp.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.exception.WorkTimeNotFoundException;
import org.tim_18.UberApp.model.WorkTime;
import org.tim_18.UberApp.repository.WorkTimeRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service("workTimeService")

public class WorkTimeService {
    @Autowired
    private final WorkTimeRepository workTimeRepository;

    public WorkTimeService(WorkTimeRepository workTimeRepository) {
        this.workTimeRepository = workTimeRepository;
    }

    public WorkTime addWorkTime(WorkTime workTime) {
        return workTimeRepository.save(workTime);
    }

    public List<WorkTime> findAllWorkTime() {
        return workTimeRepository.findAll();
    }
    public ArrayList<WorkTime> findByDriversId(Integer id) {
        return workTimeRepository.findByDriverId(id);
    }

    public WorkTime updateWorkTime(WorkTime workTime) {
        return workTimeRepository.save(workTime);
    }

    public WorkTime findWorkTimeById(Integer id) {
        return workTimeRepository.findWorkTimeById(id)
                .orElseThrow(() -> new WorkTimeNotFoundException("Work time by id " + id + " was not found"));
    }
    public Page<WorkTime> findWorkTimesFromToDate(Integer id, String start, String end, Pageable pageable) { return workTimeRepository.findWorkTimesByDate(id,start,end,pageable);}

    public void deleteWorkTime(Integer id) {
        workTimeRepository.deleteWorkTimeById(id);
    }
}
