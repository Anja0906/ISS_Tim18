package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.model.Role;
import org.tim_18.UberApp.repository.RoleRepository;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role findOneById(Integer id) {
        return this.roleRepository.getOne(id);
    }
    public Role findById(Integer id) {
        return this.roleRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Role by id " + id + " was not found"));
    }
    public List<Role> findByName(String name) {return this.roleRepository.findByName(name);}
}
