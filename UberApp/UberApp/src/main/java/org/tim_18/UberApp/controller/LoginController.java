package org.tim_18.UberApp.controller;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.LoginDTO;
import org.tim_18.UberApp.dto.LoginResponseDTO;
import org.tim_18.UberApp.model.User;
//import org.tim_18.UberApp.security.jwt.JwtTokenUtil;
import org.tim_18.UberApp.service.UserService;

@RestController
@RequestMapping("/api/user/login")
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {
    @Autowired
    private final UserService userService;

//    @Autowired
//    private AuthenticationManager authenticationManager;
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody User user) {
        LoginDTO loginDTO = new LoginDTO(user);
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO("absdaghdajhfjas", "sdfhsjdahfshadjf");
        return new ResponseEntity<>(loginResponseDTO, HttpStatus.OK);
    }

//    @PostMapping()
//    public User login(@RequestBody User user) {
//        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(user.getEmail(),
//                user.getPassword());
//        Authentication auth = authenticationManager.authenticate(authReq);
//
//        SecurityContext sc = SecurityContextHolder.getContext();
//        sc.setAuthentication(auth);
//
//        String token = jwtTokenUtil.generateToken(user.getEmail());
//        user.setJwt(token);
//
//        return user;
//    }
}
