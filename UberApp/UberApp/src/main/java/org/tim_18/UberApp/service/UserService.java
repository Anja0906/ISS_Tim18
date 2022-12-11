package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.repository.MessageRepository;
import org.tim_18.UberApp.repository.NoteRepository;
import org.tim_18.UberApp.repository.RideRepository;
import org.tim_18.UberApp.repository.UserRepository;

import java.util.*;

@Service("userService")
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RideRepository rideRepository;
    @Autowired
    private final MessageRepository messageRepository;
    @Autowired
    private final NoteRepository noteRepository;

    public UserService(UserRepository userRepository, RideRepository rideRepository, MessageRepository messageRepository,
                       NoteRepository noteRepository) {
        this.userRepository = userRepository;
        this.rideRepository = rideRepository;
        this.messageRepository = messageRepository;
        this.noteRepository = noteRepository;
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }


    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }


    public User findUserById(Integer id) {
        return userRepository.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id " + id + " was not found"));
    }

    public Page<Ride> findRidesForUser(Integer id, Pageable pageable) {
        return rideRepository.findRidesForUserPage(id, pageable);
    }

    public Ride findRideById(Integer id){
        return rideRepository.findRideById(id)
                .orElseThrow(() -> new UserNotFoundException("Ride was not found"));
    }
    public void saveMessage(Message message){
        messageRepository.save(message);
    }

    public void saveNote(Note note){
        noteRepository.save(note);
    }
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }


    public Page<Note> findNotesByUserId(Integer id, Pageable pageable) {
        return noteRepository.findByUserId(id, pageable);
    }

}
