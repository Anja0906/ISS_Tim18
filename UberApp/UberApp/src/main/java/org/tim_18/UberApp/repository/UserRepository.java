package org.tim_18.UberApp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.tim_18.UberApp.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserById(Integer id);

    User findUserByEmail(String email);

    void deleteById(Integer id);


    Page<User> findAll(Pageable pageable);
    List<User> findAll();

}