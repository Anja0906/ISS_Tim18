package org.tim_18.UberApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tim_18.UberApp.dto.LoginDTO;
import org.tim_18.UberApp.dto.LoginResponseDTO;
import org.tim_18.UberApp.model.User;
import org.tim_18.UberApp.service.UserService;

@RestController
@RequestMapping("/api/user/login")
public class LoginController {
    @Autowired
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody User user) {
        LoginDTO loginDTO = new LoginDTO(user);
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO("absdaghdajhfjas", "sdfhsjdahfshadjf");
        return new ResponseEntity<>(loginResponseDTO, HttpStatus.OK);
    }
}
