package org.tim_18.UberApp.controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.*;
import org.tim_18.UberApp.dto.noteDTOs.NotePostDTO;
import org.tim_18.UberApp.dto.noteDTOs.NoteResponseDTO;
import org.tim_18.UberApp.dto.rideDTOs.RideRetDTO;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.service.*;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserService userService;
    private final MessageService messageService;
    private final RideService rideService;
    private final NoteService noteService;
    private final ReviewService reviewService;

    public UserController(UserService userService,MessageService messageService,RideService rideService,NoteService noteService,ReviewService reviewService) {
        this.userService    = userService;
        this.messageService = messageService;
        this.rideService    = rideService;
        this.noteService    = noteService;
        this.reviewService  = reviewService;
    }
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

    @GetMapping("/{id}/ride")
    public ResponseEntity<Map<String, Object>> getRides (
            @PathVariable("id") int id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size,
            @RequestParam(defaultValue = "start_time") String sort,
            @RequestParam(defaultValue = "2022-12-07T07:00:50") String from,
            @RequestParam(defaultValue = "2022-12-08T10:40:00") String to) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Ride> rides = rideService.findRidesForUserPage(id,pageable);
        Map<String, Object> map = new HashMap<>();
        HashSet<RideRetDTO> ridesDTO = new RideRetDTO().makeRideRideDTOS(rides);
        map.put("totalCount",ridesDTO.size());
        map.put("results",ridesDTO);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
    @GetMapping("{id}/message")
    public ResponseEntity<Map<String, Object>> getMessagesForUser(
            @PathVariable("id") int id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Message> messages = messageService.findMessagesByUserId(id, pageable);

        Map<String, Object> map = new HashMap<>();
        HashSet<MessageResponseDTO> messageDTOS = new MessageResponseDTO().makeMessageResponseDTOS(messages);

        map.put("totalCount",messageDTOS.size());
        map.put("results",messageDTOS);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    @PostMapping("/{id}/message")
    public ResponseEntity<MessageResponseDTO> addReviewToVehicle(
            @PathVariable("id") int id,
            @RequestBody MessageDTO messageDTO) {
        Message message = messageFromMessageDTO(id, messageDTO);
        messageService.saveMessage(message);

        MessageResponseDTO messageResponseDTO = new MessageResponseDTO(message);
        return new ResponseEntity<>(messageResponseDTO, HttpStatus.OK);
    }

    private Message messageFromMessageDTO(Integer id, MessageDTO messageDTO){
        return new Message(id,userService.findUserById(id),
                                      userService.findUserById(messageDTO.getReceiverId()),
                                      messageDTO.getMessage(),LocalDateTime.now(),
                                      messageDTO.getType(),rideService.findRideById(messageDTO.getRideId()));
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<?> blockUser(@PathVariable("id") int id) {
        try{
            User user = userService.findUserById(id);
            user.setBlocked(true);
            userService.updateUser(user);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (UserNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/unblock")
    public ResponseEntity<?> unblockUser(@PathVariable("id") int id) {
        try{
            User user = userService.findUserById(id);
            user.setBlocked(false);
            userService.updateUser(user);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (UserNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/{id}/note")
    public ResponseEntity<NoteResponseDTO> addNoteForUser(
            @PathVariable("id") int id,
            @RequestBody NotePostDTO notePostDTO) {
        try{
            User user =userService.findUserById(id);
            Note note = new Note(user,notePostDTO.getMessage());
            noteService.saveNote(note);
            NoteResponseDTO noteResponseDTO = new NoteResponseDTO(note);
            return new ResponseEntity<>(noteResponseDTO, HttpStatus.OK);
        }catch (UserNotFoundException e){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{id}/note")
    public ResponseEntity<Map<String, Object>> getNotesForUser(
            @PathVariable("id") int id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size){

        Pageable pageable = PageRequest.of(page, size);
        Page<Note> notes = noteService.findNotesByUserId(id, pageable);

        Map<String, Object> map = new HashMap<>();
        HashSet<NoteResponseDTO> noteResponseDTOS = new NoteResponseDTO().makeNoteResponseDTOS(notes);

        map.put("totalCount",noteResponseDTOS.size());
        map.put("results",noteResponseDTOS);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
    @GetMapping("/whoami")
    @PreAuthorize("hasRole('ROLE_USER')")
    public User user(Principal user) {
        return this.userService.findUserByEmail(user.getName());
    }

}

