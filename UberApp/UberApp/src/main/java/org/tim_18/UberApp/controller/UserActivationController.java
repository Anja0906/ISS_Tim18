package org.tim_18.UberApp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.model.User;
import org.tim_18.UberApp.model.UserActivation;
import org.tim_18.UberApp.service.UserActivationService;
import org.tim_18.UberApp.service.UserService;

import java.util.List;
@RestController
@RequestMapping("/useractivation")
public class UserActivationController {

    private final UserActivationService userActivationService;

    public UserActivationController(UserActivationService userActivationService) {
        this.userActivationService = userActivationService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserActivation>> getAllUserActivations () {
        List<UserActivation> userActivations = userActivationService.findAllUserActivations();
        return new ResponseEntity<>(userActivations, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<UserActivation> getUserActivationById (@PathVariable("id") Integer id) {
        UserActivation userActivation = userActivationService.findUserActivationById(id);
        return new ResponseEntity<>(userActivation, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<UserActivation> addUserActivation(@RequestBody UserActivation userActivation) {
        UserActivation newUserActivation = userActivationService.addUserActivation(userActivation);
        return new ResponseEntity<>(newUserActivation, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<UserActivation> updateUser(@RequestBody UserActivation userActivation) {
        UserActivation updateUserActivation = userActivationService.updateUserActivation(userActivation);
        return new ResponseEntity<>(updateUserActivation, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUserActivation(@PathVariable("id") Integer id) {
        userActivationService.deleteUserActivation(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
