package org.tim_18.UberApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tim_18.UberApp.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	List<Role> findByName(String name);
	Optional<Role> findById(Integer id);

	@Query(value = "SELECT * FROM role inner join user_role on id=role_id WHERE user_id = ?1", nativeQuery = true)
	List<Role> findByUserId(Integer id);
}
