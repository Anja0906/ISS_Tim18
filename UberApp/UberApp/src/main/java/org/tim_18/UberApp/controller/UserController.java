package org.tim_18.UberApp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.*;
import org.tim_18.UberApp.dto.driverDTOs.DriverDTO;
import org.tim_18.UberApp.dto.noteDTOs.NotePostDTO;
import org.tim_18.UberApp.dto.noteDTOs.NoteResponseDTO;
import org.tim_18.UberApp.dto.rideDTOs.RideRetDTO;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.service.MessageService;
import org.tim_18.UberApp.service.RideService;
import org.tim_18.UberApp.service.UserService;

import javax.naming.ldap.HasControls;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final MessageService messageService;
    private final RideService rideService;

    public UserController(UserService userService,MessageService messageService,RideService rideService) {
        this.userService    = userService;
        this.messageService = messageService;
        this.rideService    = rideService;
    }
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getUserDetails (
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.findAll(pageable);
        Map<String, Object> map = new HashMap<>();
        HashSet<UserDTO> usersDTO = new HashSet<>();
        for (User user:users) {
            usersDTO.add(new UserDTO(user));
        }

        map.put("totalCount",usersDTO.size());
        map.put("results",usersDTO);
        return new ResponseEntity<>(map, HttpStatus.OK);

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
        HashSet<RideRetDTO> ridesDTO = new HashSet<>();
        for (Ride ride: rides){
            ridesDTO.add(new RideRetDTO(ride));
        }

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
        HashSet<MessageResponseDTO> messageDTOS = new HashSet<>();
        for (Message message:messages) {
            messageDTOS.add(new MessageResponseDTO(message));
        }

        map.put("totalCount",messageDTOS.size());
        map.put("results",messageDTOS);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
    @PostMapping("/{id}/message")
    public ResponseEntity<MessageResponseDTO> addReviewToVehicle(
            @PathVariable("id") int id,
            @RequestBody MessageDTO messageDTO) {

        Message message = messageFromMessageDTO(id, messageDTO);
        userService.saveMessage(message);
        MessageResponseDTO messageResponseDTO = new MessageResponseDTO(message);
        return new ResponseEntity<>(messageResponseDTO, HttpStatus.OK);
    }

    private Message messageFromMessageDTO(Integer id, MessageDTO messageDTO){

        Message message = new Message();
        message.setReceiver(userService.findUserById(messageDTO.getReceiverId()));
        message.setMessage(messageDTO.getMessage());
        message.setMessageType(messageDTO.getType());
        message.setRide(userService.findRideById(messageDTO.getRideId()));
        message.setSender(userService.findUserById(id));
        message.setTime(LocalDateTime.now());
        return message;
    }

    @PutMapping("/{id}/block")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void blockUser(@PathVariable("id") int id) {
        User user = userService.findUserById(id);
        if(user!=null){
            user.setBlocked(true);
            userService.updateUser(user);
        }
    }

    @PutMapping("/{id}/unblock")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void unblockUser(@PathVariable("id") int id) {
        User user = userService.findUserById(id);
        if(user!=null){
            user.setBlocked(false);
            userService.updateUser(user);
        }
    }
    @PostMapping("/{id}/note")
    public ResponseEntity<NoteResponseDTO> addReviewToVehicle(
            @PathVariable("id") int id,
            @RequestBody NotePostDTO notePostDTO) {

        Note note = new Note();
        note.setUser(userService.findUserById(id));
        note.setMessage(notePostDTO.getMessage());
        userService.saveNote(note);
        NoteResponseDTO noteResponseDTO = new NoteResponseDTO(note);
        return new ResponseEntity<>(noteResponseDTO, HttpStatus.OK);
    }

    @GetMapping("{id}/note")
    public ResponseEntity<Map<String, Object>> getNotesForUser(
            @PathVariable("id") int id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size){

        Pageable pageable = PageRequest.of(page, size);
        Page<Note> notes = userService.findNotesByUserId(id, pageable);
        Map<String, Object> map = new HashMap<>();
        HashSet<NoteResponseDTO> noteResponseDTOS = new HashSet<>();
        for (Note note:notes) {
            noteResponseDTOS.add(new NoteResponseDTO(note));
        }

        map.put("totalCount",noteResponseDTOS.size());
        map.put("results",noteResponseDTOS);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


}

