package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.exception.RejectionNotFoundException;
import org.tim_18.UberApp.model.Rejection;
import org.tim_18.UberApp.repository.RejectionRepository;

import java.util.List;

@Service
public class RejectionService {

    @Autowired
    private final RejectionRepository rejectionRepository;

    public RejectionService(RejectionRepository rejectionRepository) {this.rejectionRepository = rejectionRepository;}
    public Rejection findRejectionById(Integer id) {
        return rejectionRepository.findRejectionById(id)
                .orElseThrow(() -> new RejectionNotFoundException("Rejection by id " + id + " was not found"));
    }
    public Rejection addRejection(Rejection rejection) {return rejectionRepository.save(rejection);}
    public Rejection save(Rejection rejection) {return rejectionRepository.save(rejection);}
    public List<Rejection> findAll() {return rejectionRepository.findAll();}
    public Rejection updateRejection(Rejection rejection) {return rejectionRepository.save(rejection);}

}
