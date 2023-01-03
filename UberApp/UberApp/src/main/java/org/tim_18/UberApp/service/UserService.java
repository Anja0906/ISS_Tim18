package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
<<<<<<< Updated upstream
=======
import org.tim_18.UberApp.dto.UserDTO;
import org.tim_18.UberApp.dto.UserDTOwithPassword;
>>>>>>> Stashed changes
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.model.Message;
import org.tim_18.UberApp.model.Note;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.model.User;
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

    public User updateUserFromDto(Integer id, UserDTO userDTO){
        User user = this.findUserById(id);
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setEmail(userDTO.getEmail());
        user.setAddress(userDTO.getAddress());
        user.setProfilePicture(userDTO.getProfilePicture());
        user.setTelephoneNumber(userDTO.getTelephoneNumber());
        user.setBlocked(userDTO.isBlocked());
        return this.updateUser(user);
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

    public List<Ride> findRidesForUser(Integer id) {
        return rideRepository.findRidesForUser(id);
    }

    public Ride findRideById(Integer id){
        return rideRepository.findRideById(id);
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


    public List<Note> findNotesByUserId(Integer id) {
        return noteRepository.findByUserId(id);
    }
}
