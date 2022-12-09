package org.tim_18.UberApp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.DocumentDTO;
import org.tim_18.UberApp.dto.MessageDTO;
import org.tim_18.UberApp.dto.ReviewDTO;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.service.MessageService;
import org.tim_18.UberApp.service.UserService;

import javax.naming.ldap.HasControls;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final MessageService messageService;
   // private final RideService rideService;
    //Napises funkciju koja radi query za ride za drivere
    //Napises funkciju koja radi query za ride za passengera
    //oba rade po idu
    //Kada dobavis HashSetove (ovo vracaju ove dve funkcije)
    //Onda radis if(hashset.isEmpty)
    //Foreach prebacujes u DTORIDES
    //HASHSET<DTORIDES> new HashSet
    //u foreach petljama dodajes dto elemente u tu hashset listu
    //na kraju vratis hashset listu i to je kraj

    public UserController(UserService userService,MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }
    @GetMapping("")
    public ResponseEntity<List<User>> getUserDetails(){
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
//    @GetMapping("{id}/ride")
//    public ResponseEntity<ArrayList<Ride>> getRidesForUsers(@PathVariable("id") Integer id){
//        ArrayList<Ride> rides = userService.findRidesForUser(id);
//        if(rides.isEmpty()) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }else {
//            return new ResponseEntity<>(rides, HttpStatus.OK);
//        }
//    }
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


}

