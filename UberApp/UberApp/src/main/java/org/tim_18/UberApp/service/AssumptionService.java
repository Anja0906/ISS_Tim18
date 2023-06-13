package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.repository.AssumptionRepository;
@Service("assumptionService")
public class AssumptionService {
    @Autowired
    private final AssumptionRepository assumptionRepository;

    public AssumptionService(AssumptionRepository assumptionRepository) {this.assumptionRepository = assumptionRepository;}
}