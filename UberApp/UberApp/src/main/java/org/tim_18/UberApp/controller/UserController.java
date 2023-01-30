package org.tim_18.UberApp.controller;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
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
import org.tim_18.UberApp.exception.RideNotFoundException;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.security.TokenUtils;
import org.tim_18.UberApp.service.*;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
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

    private final LocationsForRideService locationsForRideService;

    public UserController(UserService userService, MessageService messageService, RideService rideService, NoteService noteService, ReviewService reviewService, RoleService roleService, LocationsForRideService locationsForRideService) {
        this.userService    = userService;
        this.messageService = messageService;
        this.rideService    = rideService;
        this.noteService    = noteService;
        this.reviewService  = reviewService;
        this.roleService = roleService;
        this.locationsForRideService = locationsForRideService;
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

    @PostMapping("/register")
    public ResponseEntity<?> processRegister(@RequestBody User user)
            throws UnsupportedEncodingException, MessagingException {
        userService.register(user, "http://localhost:8080/api/user");
        return new ResponseEntity<>("Successful account activation!", HttpStatus.OK);
    }

    //odraditi ovo u frontu
    //<div class="container text-center">
    //    <h3>You have signed up successfully!</h3>
    //    <p>Please check your email to verify your account.</p>
    //    <h4><a th:href="/@{/login}">Click here to Login</a></h4>
    //</div>

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (userService.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }

    //uraditi ovo u frontu za uspesan verify
    //<div class="container text-center">
    //    <h3>Congratulations, your account has been verified.</h3>
    //    <h4><a th:href="/@{/login}">Click here to Login</a></h4>
    //</div>

    //uraditi ovo u frontu za neuspesan verify
    //<div class="container text-center">
    //    <h3>Sorry, we could not verify account. It maybe already verified,
    //        or verification code is incorrect.</h3>
    //</div>


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
            HashSet<RideRetDTO> ridesDTO = makeRideDTOS(rides);
            map.put("totalCount",ridesDTO.size());
            map.put("results",ridesDTO);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    private HashSet<RideRetDTO> makeRideDTOS(List<Ride> rides) {
        HashSet<RideRetDTO> rideRetDTOHashSet = new HashSet<>();
        for (Ride ride : rides) {
            rideRetDTOHashSet.add(new RideRetDTO(ride, getLocationsByRideId(ride.getId())));
        }
        return rideRetDTOHashSet;
    }

    private Set<LocationsForRide> getLocationsByRideId(Integer id) {
        return locationsForRideService.getByRideId(id);
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
            if (notePostDTO.getMessage().length() > 200) {
                return new ResponseEntity<>("Message too long!", HttpStatus.BAD_REQUEST);
            }
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


    @PreAuthorize("hasRole('PASSENGER')")
    @PutMapping("/{id}/changePassword")
    public ResponseEntity<?> changePassword(Principal principal, @PathVariable("id") int id, @RequestBody changePasswordDTO password) {
        try{
            User userRequest = userService.findUserById(id);
            User user = userService.findUserByEmail(principal.getName());
            if (!user.getId().equals(userRequest.getId())){
                return new ResponseEntity<>("User does not exist!", HttpStatus.NOT_FOUND);
            }
            String oldPassword = password.getOldPassword();
            if (!passwordEncoder.matches(oldPassword, user.getPassword())){
                return new ResponseEntity<>("Current password is not matching!", HttpStatus.BAD_REQUEST);
            }
            userRequest.setPassword(passwordEncoder.encode(password.getNewPassword()));
            userService.updateUser(userRequest);
            return new ResponseEntity<>(new ErrorMessage("Password successfully changed!"), HttpStatus.NO_CONTENT);
        }catch (UserNotFoundException e) {
            return new ResponseEntity<>("User does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{id}/resetPassword")
    public ResponseEntity<?> changePasswordRequest(@PathVariable("id") int id) {
        try{
            User user = userService.findUserById(id);
            String email = user.getEmail();
            String token = String.valueOf(userService.generateRandomInt());
            userService.updateResetPasswordToken(token, email);
            userService.sendEmail(email, token);
            return new ResponseEntity<>("Email with reset code has been sent!", HttpStatus.NO_CONTENT);
        }catch (UserNotFoundException e){
            return new ResponseEntity<>("User does not exist!", HttpStatus.NOT_FOUND);
        } catch (UnsupportedEncodingException | MessagingException e) {
            return new ResponseEntity<>("Error while sending email", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("{id}/resetPassword")
    public ResponseEntity<?> changePassword(@PathVariable("id") int id, @RequestBody ResetPasswordDTO resetPasswordDTO) {
        try{
            User user = userService.findUserById(id);
            String token = user.getResetPasswordToken();
            Date expiresIn = user.getTimeOfResetPasswordToken();
            boolean isExpired = userService.compareIfCodeIsExpired(expiresIn);
            if(token.equals(resetPasswordDTO.getCode()) && !isExpired){
                userService.updatePassword(user, resetPasswordDTO.getNewPassword());
                return new ResponseEntity<>("Password successfully changed!", HttpStatus.NO_CONTENT);
            }
            else {
                return new ResponseEntity<>(new ErrorMessage("Code is expired or not correct!"), HttpStatus.BAD_REQUEST);
            }
        }catch (UserNotFoundException e){
            return new ResponseEntity<>("User does not exist!", HttpStatus.NOT_FOUND);
        }catch (NullPointerException e){
            return new ResponseEntity<>("Code is not sent to the email!", HttpStatus.NOT_FOUND);
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

