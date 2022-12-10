package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.model.Panic;
import org.tim_18.UberApp.repository.PanicRepository;

import java.util.List;

@Service
public class PanicService {
    @Autowired
    private final PanicRepository repo;

    public PanicService(PanicRepository repo) {
        this.repo = repo;
    }

    public List<Panic> findAllPanics() {
        return repo.findAll();
    }

    public Page<Panic> findAll(Pageable page){return repo.findAll(page);}

}
