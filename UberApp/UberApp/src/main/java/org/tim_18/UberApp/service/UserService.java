package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.model.User;
import org.tim_18.UberApp.repository.RideRepository;
import org.tim_18.UberApp.repository.UserRepository;

import java.util.*;

@Service("userService")
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    private final RideRepository rideRepository;

    public UserService(UserRepository userRepository, RideRepository rideRepository) {
        this.userRepository = userRepository;
        this.rideRepository = rideRepository;
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }


    public User findUserById(Integer id) {
        return userRepository.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id " + id + " was not found"));
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }


    public ArrayList<Ride> findRidesForUser(Integer id) {
        return rideRepository.findRidesForUser(id);
    }


}
