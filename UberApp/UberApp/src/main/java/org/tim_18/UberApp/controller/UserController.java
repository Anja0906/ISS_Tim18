package org.tim_18.UberApp.controller;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.Validation.ErrorMessage;
import org.tim_18.UberApp.dto.*;
import org.tim_18.UberApp.dto.noteDTOs.NotePostDTO;
import org.tim_18.UberApp.dto.noteDTOs.NoteResponseDTO;
import org.tim_18.UberApp.dto.rideDTOs.RideRetDTO;
import org.tim_18.UberApp.exception.PassengerNotFoundException;
import org.tim_18.UberApp.exception.RideNotFoundException;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.security.TokenUtils;
import org.tim_18.UberApp.service.*;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    private final UserService userService;
    private final MessageService messageService;
    private final RideService rideService;
    private final NoteService noteService;
    private final ReviewService reviewService;

    private final RoleService roleService;

    public UserController(UserService userService, MessageService messageService, RideService rideService, NoteService noteService, ReviewService reviewService, RoleService roleService) {
        this.userService    = userService;
        this.messageService = messageService;
        this.rideService    = rideService;
        this.noteService    = noteService;
        this.reviewService  = reviewService;
        this.roleService = roleService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getUserDetails (
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.findAll(pageable);

        Map<String, Object> map = new HashMap<>();
        HashSet<UserDTO> usersDTO = new UserDTO().makeUserDTOS(users);

        map.put("totalCount",usersDTO.size());
        map.put("results",usersDTO);
        return new ResponseEntity<>(map, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser (
            @PathVariable("id") int id) {
        User user = userService.findUserById(id);
        System.out.println(user);
        UserDTO userDTO = new UserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateUser(@PathVariable("id") int id, @RequestBody UserDTO userDTO) {
        try{
            userService.updateUserFromDto(id, userDTO);
        }catch (UserNotFoundException e){
            System.out.println("User not found");
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/ride")
    public ResponseEntity<?> getRides (Principal principal,
                                       @PathVariable("id") int id,
                                       @RequestParam(defaultValue = "0") Integer page,
                                       @RequestParam(defaultValue = "4") Integer size,
                                       @RequestParam(defaultValue = "start_time") String sort,
                                       @RequestParam(defaultValue = "2021-12-07T07:00:50") String from,
                                       @RequestParam(defaultValue = "2025-12-08T10:40:00") String to) {
        try {
            userService.findUserById(id); //throws 404
            checkAuthorities(principal, id);
            Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
//            Page<Ride> rides;
            List<Ride> rides = rideService.findRidesByUser(id, from, to);
            Map<String, Object> map = new HashMap<>();
            HashSet<RideRetDTO> ridesDTO = new RideRetDTO().makeRideRideDTOS(rides);
            map.put("totalCount",ridesDTO.size());
            map.put("results",ridesDTO);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("{id}/message")
    public ResponseEntity<?> getMessagesForUser(Principal principal,
                                                @PathVariable("id") int id,
                                                @RequestParam(defaultValue = "0") Integer page,
                                                @RequestParam(defaultValue = "4") Integer size){
        try {
            checkAuthorities(principal, id);
            Pageable pageable = PageRequest.of(page, size);
            Page<Message> messages = messageService.findMessagesByUserId(id, pageable);

            Map<String, Object> map = new HashMap<>();
            HashSet<MessageResponseDTO> messageDTOS = new MessageResponseDTO().makeMessageResponseDTOS(messages);

            map.put("totalCount", messageDTOS.size());
            map.put("results", messageDTOS);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User does not exist!", HttpStatus.NOT_FOUND);
        }
    }


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/message")
    public ResponseEntity<?> sendMessage(Principal principal,
                                         @PathVariable("id") int id,
                                         @RequestBody MessageDTO messageDTO) {
        try {
            userService.findUserById(id); //throws 404
            rideService.findRideById(messageDTO.getRideId()); //throws 404
            User sender = userService.findUserByEmail(principal.getName());
            if (sender.getId().equals(id)) {
                return new ResponseEntity<>("Cannot send message to yourself!", HttpStatus.BAD_REQUEST);
            }
            messageDTO.setReceiverId(id);
            Message message = messageFromMessageDTO(principal, messageDTO);
            messageService.saveMessage(message);
            return new ResponseEntity<>(new MessageResponseDTO(message), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("Receiver does not exist!", HttpStatus.NOT_FOUND);
        } catch (RideNotFoundException e) {
            return new ResponseEntity<>("Ride does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    private Message messageFromMessageDTO(Principal principal, MessageDTO messageDTO){
        User user = userService.findUserByEmail(principal.getName());
        return new Message(user,
                userService.findUserById(messageDTO.getReceiverId()),
                messageDTO.getMessage(),
                new Date(),
                messageDTO.getType(),
                rideService.findRideById(messageDTO.getRideId()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/block")
    public ResponseEntity<?> blockUser(@PathVariable("id") int id) {
        try{
            User user = userService.findUserById(id);
            if (user.isBlocked()) {
                return new ResponseEntity<>(new ErrorMessage("User already blocked!"), HttpStatus.BAD_REQUEST);
            }
            user.setBlocked(true);
            userService.updateUser(user);
            return new ResponseEntity<>(new ErrorMessage("User is successfully blocked"), HttpStatus.NO_CONTENT);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/unblock")
    public ResponseEntity<?> unblockUser(@PathVariable("id") int id) {
        try{
            User user = userService.findUserById(id);
            if (!user.isBlocked()) {
                return new ResponseEntity<>(new ErrorMessage("User is not blocked!"), HttpStatus.BAD_REQUEST);
            }
            user.setBlocked(false);
            userService.updateUser(user);
            return new ResponseEntity<>(new ErrorMessage("User is successfully unblocked"), HttpStatus.NO_CONTENT);
        }catch (UserNotFoundException e) {
            return new ResponseEntity<>("User does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/note")
    public ResponseEntity<?> addNoteForUser(
            @PathVariable("id") int id,
            @RequestBody NotePostDTO notePostDTO) {
        try{
            User user =userService.findUserById(id);
            Note note = new Note(user,notePostDTO.getMessage());
            noteService.saveNote(note);
            NoteResponseDTO noteResponseDTO = new NoteResponseDTO(note);
            return new ResponseEntity<>(noteResponseDTO, HttpStatus.OK);
        }catch (UserNotFoundException e) {
            return new ResponseEntity<>("User does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{id}/note")
    public ResponseEntity<?> getNotesForUser(
            @PathVariable("id") int id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size){
        try {
            userService.findUserById(id);
            Pageable pageable = PageRequest.of(page, size);
            Page<Note> notes = noteService.findNotesByUserId(id, pageable);

            Map<String, Object> map = new HashMap<>();
            HashSet<NoteResponseDTO> noteResponseDTOS = new NoteResponseDTO().makeNoteResponseDTOS(notes);

            map.put("totalCount", noteResponseDTOS.size());
            map.put("results", noteResponseDTOS);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User does not exist!", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/whoami")
    @PreAuthorize("hasRole('ROLE_USER')")
    public User user(Principal user) {
        return this.userService.findUserByEmail(user.getName());
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {
        try {
            // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
            // AuthenticationException
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(), authenticationRequest.getPassword()));

            // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
            // kontekst
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Kreiraj token za tog korisnika
            User user = (User) authentication.getPrincipal();
            String jwt = tokenUtils.generateToken(user.getEmail());
            int expiresIn = tokenUtils.getExpiredIn();
            List<String> rolesStr = new ArrayList<>();
            for (Role r : user.getRoles()) {
                rolesStr.add(r.getName());
            }
            // Vrati token kao odgovor na uspesnu autentifikaciju
            return ResponseEntity.ok(new JwtResponse(jwt, expiresIn, user.getId(), user.getEmail(), rolesStr, user.getName() + " " + user.getSurname()));
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new ErrorMessage("Wrong username or password!"), HttpStatus.BAD_REQUEST);
        }
    }

    private void checkAuthorities(Principal principal, Integer id) throws UserNotFoundException {
        User user = userService.findUserByEmail(principal.getName());
        if (!user.getRoles().contains(roleService.findById(4)) && !user.getId().equals(id)) {
            throw new UserNotFoundException("");
        }
    }
}

