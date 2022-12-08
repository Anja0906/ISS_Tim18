package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.model.Administrator;
import org.tim_18.UberApp.repository.AdministratorRepository;

import java.util.List;
@Service("administratorService")
public class AdministratorService {
    @Autowired
    private final AdministratorRepository administratorRepository;

    public AdministratorService(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }

    public Administrator addAdministrator(Administrator administrator) {
        return administratorRepository.save(administrator);
    }

    public List<Administrator> findAllAdministrators() {
        return administratorRepository.findAll();
    }

    public Administrator updateAdministrator(Administrator administrator) {
        return administratorRepository.save(administrator);
    }

    public Administrator findAdministratorById(Integer id) {
        return administratorRepository.findAdministratorById(id)
                .orElseThrow(() -> new UserNotFoundException("Administrator by id " + id + " was not found"));
    }

    public void deleteAdministrator(Integer id) {
        administratorRepository.deleteAdministratorById(id);
    }
}
