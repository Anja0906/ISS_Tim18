package org.tim_18.UberApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tim_18.UberApp.model.User;
import org.tim_18.UberApp.model.UserActivation;

import java.util.Optional;

public interface UserActivationRepository extends JpaRepository<UserActivation, Integer> {
    Optional<UserActivation> findUserActivationById(Integer id);

    void deleteUserActivationById(Integer id);
}
