package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.dto.UserDTOwithPassword;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.model.Message;
import org.tim_18.UberApp.model.Note;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.model.User;
import org.tim_18.UberApp.repository.MessageRepository;
import org.tim_18.UberApp.repository.NoteRepository;
import org.tim_18.UberApp.repository.RideRepository;
import org.tim_18.UberApp.repository.UserRepository;

import java.util.List;

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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

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

    public User findByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }

    public User save(UserDTOwithPassword userRequest) {
        User u = new User();
        u.setEmail(userRequest.getEmail());

        // pre nego sto postavimo lozinku u atribut hesiramo je kako bi se u bazi nalazila hesirana lozinka
        // treba voditi racuna da se koristi isi password encoder bean koji je postavljen u AUthenticationManager-u kako bi koristili isti algoritam
        u.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        u.setName(userRequest.getName());
        u.setSurname(userRequest.getSurname());
        u.setBlocked(false);
        u.setEmail(userRequest.getEmail());

        // u primeru se registruju samo obicni korisnici i u skladu sa tim im se i dodeljuje samo rola USER
//        List<Role> roles = roleService.findByName("ROLE_USER");
//        u.setRoles(roles);

        return this.userRepository.save(u);
    }

}
