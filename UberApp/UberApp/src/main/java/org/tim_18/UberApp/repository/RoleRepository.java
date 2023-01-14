package org.tim_18.UberApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tim_18.UberApp.model.Role;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
	List<Role> findByName(String name);
}
