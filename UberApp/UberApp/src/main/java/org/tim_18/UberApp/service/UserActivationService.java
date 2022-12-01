package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.model.User;
import org.tim_18.UberApp.model.UserActivation;
import org.tim_18.UberApp.repository.UserActivationRepository;
import org.tim_18.UberApp.repository.UserRepository;

import java.util.List;

@Service("userActivationService")

public class UserActivationService {
    @Autowired
    private final UserActivationRepository userActivationRepository;

    public UserActivationService(UserActivationRepository userActivationRepository) {
        this.userActivationRepository = userActivationRepository;
    }

    public UserActivation addUserActivation(UserActivation userActivation) {
        return userActivationRepository.save(userActivation);
    }

    public List<UserActivation> findAllUserActivations() {
        return userActivationRepository.findAll();
    }

    public UserActivation updateUserActivation(UserActivation userActivation) {
        return userActivationRepository.save(userActivation);
    }

    public UserActivation findUserActivationById(Integer id) {
        return userActivationRepository.findUserActivationById(id)
                .orElseThrow(() -> new UserNotFoundException("User activation by id " + id + " was not found"));
    }

    public void deleteUserActivation(Integer id) {
        userActivationRepository.deleteUserActivationById(id);
    }
}
