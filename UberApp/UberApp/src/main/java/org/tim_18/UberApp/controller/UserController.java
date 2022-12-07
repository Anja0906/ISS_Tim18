package org.tim_18.UberApp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.DocumentDTO;
import org.tim_18.UberApp.model.Document;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.model.User;
import org.tim_18.UberApp.service.UserService;

import javax.naming.ldap.HasControls;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping("/{id}/ride")
//    public ResponseEntity<HashSet<Ride>> getRidesForUser(@PathVariable("id") Integer id) {
//        HashSet<Ride> rides = documentService.findByDriverId(id);
//        if(documents.isEmpty()) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//
//        }else{
//            for(Document document:documents){
//                DocumentDTO documentDTO = new DocumentDTO(document);
//                documentDTOS.add(documentDTO);
//            }
//            return new ResponseEntity<>(documentDTOS, HttpStatus.OK);
//        }
//    }

    @GetMapping("")
    public ResponseEntity<List<User>> getUserDetails(){
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<User> getUserById (@PathVariable("id") Integer id) {
        User user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User newUser = userService.addUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updateUser = userService.updateUser(user);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

