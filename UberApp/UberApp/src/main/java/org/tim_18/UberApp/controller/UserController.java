package org.tim_18.UberApp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.*;
import org.tim_18.UberApp.dto.driverDTOs.DriverDTO;
import org.tim_18.UberApp.dto.noteDTOs.NotePostDTO;
import org.tim_18.UberApp.dto.noteDTOs.NoteResponseDTO;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.service.MessageService;
import org.tim_18.UberApp.service.UserService;

import javax.naming.ldap.HasControls;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final MessageService messageService;

    public UserController(UserService userService,MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }
    @GetMapping("")
    public ResponseEntity<List<User>> getUserDetails(){
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @GetMapping("{id}/ride")
    public ResponseEntity<List<Ride>> getRidesForUsers(@PathVariable("id") Integer id){
        List<Ride> rides = userService.findRidesForUser(id);
        if(rides.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>(rides, HttpStatus.OK);
        }
    }
    @GetMapping("{id}/message")
    public ResponseEntity<HashSet<MessageDTO>> getMessagesForUser(
            @PathVariable("id") Integer id){
        ArrayList<Message> messages = messageService.findMessagesByUserId(id);
        HashSet<MessageDTO> messageDTOS = new HashSet<>();
        if(messages.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        }else{
            for (Message message: messages) {
                messageDTOS.add(new MessageDTO(message));
            }
            return new ResponseEntity<>(messageDTOS, HttpStatus.OK);
        }
    }
    @PostMapping("/{id}/message")
    public ResponseEntity<MessageResponseDTO> addReviewToVehicle(@PathVariable("id") int id,  @RequestBody MessageDTO messageDTO) {
        Message message = messageFromMessageDTO(id, messageDTO);
        userService.saveMessage(message);
        MessageResponseDTO messageResponseDTO = new MessageResponseDTO(message);
        return new ResponseEntity<>(messageResponseDTO, HttpStatus.CREATED);
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
    public void blockUser(
            @PathVariable("id") int id) {
        User user = userService.findUserById(id);
        if(user!=null){
            user.setBlocked(true);
            userService.updateUser(user);
        }
    }

    @PutMapping("/{id}/unblock")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void unblockUser(
            @PathVariable("id") int id) {
        User user = userService.findUserById(id);
        if(user!=null){
            user.setBlocked(false);
            userService.updateUser(user);
        }
    }
    @PostMapping("/{id}/note")
    public ResponseEntity<NoteResponseDTO> addReviewToVehicle(@PathVariable("id") int id, @RequestBody NotePostDTO notePostDTO) {
        Note note = new Note();
        note.setUser(userService.findUserById(id));
        note.setMessage(notePostDTO.getMessage());
        userService.saveNote(note);
        NoteResponseDTO noteResponseDTO = new NoteResponseDTO(note);
        return new ResponseEntity<>(noteResponseDTO, HttpStatus.OK);
    }

    @GetMapping("{id}/note")
    public ResponseEntity<HashSet<NoteResponseDTO>> getNotesForUser(
            @PathVariable("id") Integer id){
        List<Note> notes = userService.findNotesByUserId(id);
        HashSet<NoteResponseDTO> noteResponseDTOS = new HashSet<>();
        if(notes.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        }else{
            for (Note note: notes) {
                noteResponseDTOS.add(new NoteResponseDTO(note));
            }
            return new ResponseEntity<>(noteResponseDTOS, HttpStatus.OK);
        }
    }



}

