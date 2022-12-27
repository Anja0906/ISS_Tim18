package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.model.Role;
import org.tim_18.UberApp.repository.RoleRepository;

import java.util.List;

@Service
public class RoleService {
	@Autowired
	private RoleRepository roleRepository;

	public Role findById(Long id) {
		Role auth = this.roleRepository.getOne(id);
		return auth;
	}

	public List<Role> findByName(String name) {
		List<Role> roles = this.roleRepository.findByName(name);
		return roles;
	}
}
