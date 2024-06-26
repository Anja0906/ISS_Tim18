package org.tim_18.UberApp.controller;


import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.tim_18.UberApp.Validation.ErrorMessage;
import org.tim_18.UberApp.dto.JwtAuthenticationRequest;
import org.tim_18.UberApp.dto.userDTOs.UserDTOwithPassword;
import org.tim_18.UberApp.exception.ResourceConflictException;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.mapper.userDTOmappers.UserDTOwithPasswordMapper;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.security.TokenUtils;
import org.tim_18.UberApp.service.DriverService;
import org.tim_18.UberApp.service.PassengerService;
import org.tim_18.UberApp.service.RoleService;
import org.tim_18.UberApp.service.UserService;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


//Kontroler zaduzen za autentifikaciju korisnika
@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private PassengerService passengerService;

	@Autowired
	private DriverService driverService;

	@Autowired
	private UserDTOwithPasswordMapper mapper;

	@Autowired
	PasswordEncoder passwordEncoder;

//	@GetMapping("/whoami")
//	@PreAuthorize("hasRole('USER')")
//	public User user(Principal user) {
//		return this.userService.findByEmail(user.getName());
//	}


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
			if (!user.isActive()) {
				return new ResponseEntity<>(new ErrorMessage("Account not activated!"), HttpStatus.BAD_REQUEST);
			}
			if (user.isBlocked()) {
				return new ResponseEntity<>(new ErrorMessage("Account is blocked!"), HttpStatus.BAD_REQUEST);
			}
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

	// Endpoint za registraciju novog korisnika
	//@TODO
	@PostMapping("/signupPassenger")
	public ResponseEntity<?> addPassenger(@RequestBody UserDTOwithPassword userRequest, UriComponentsBuilder ucBuilder) throws MessagingException, UnsupportedEncodingException {
		try{
			User existUser = this.userService.findUserByEmail(userRequest.getEmail());
			if (existUser != null) {
				return new ResponseEntity<>(new ResourceConflictException(userRequest.getId(), "Username already exists").getMessage(), HttpStatus.BAD_REQUEST);
			}
		}catch(UserNotFoundException e){
			User user = mapper.fromDTOtoUser(userRequest);
			user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
			String randomCode = RandomString.make(64);
			System.out.println(randomCode);
			user.setVerificationCode(randomCode);
			user.setActive(false);
			Passenger passenger = new Passenger(user);
			passenger.setRoles(getRoles(1));

			this.passengerService.addPassenger(passenger);
			try{

				this.userService.sendVerificationEmail(user, "http://localhost:8080/api/user");
				return new ResponseEntity<>(passenger, HttpStatus.CREATED);
			}catch (MessagingException | UnsupportedEncodingException exception){
				return new ResponseEntity<>("Email is not valid", HttpStatus.BAD_REQUEST);
			}
		}
		return  new ResponseEntity<>(HttpStatus.OK);
	}

	private List<Role> getRoles(int role) {
		List<Role> rP = roleService.findByName("ROLE_PASSENGER");
		List<Role> rD = roleService.findByName("ROLE_DRIVER");
		List<Role> rU = roleService.findByName("ROLE_USER");
		List<Role> roles = new ArrayList<>();
		roles.add(rU.get(0));
		if (role==1) {
			roles.add(rP.get(0));
		} else {
			roles.add(rD.get(0));
		}
		return roles;
	}

	@PostMapping("/signupDriver")
	public ResponseEntity<Driver> addDriver(@RequestBody UserDTOwithPassword userRequest, UriComponentsBuilder ucBuilder) {
		User existUser = this.userService.findUserByEmail(userRequest.getEmail());

		if (existUser != null) {
			throw new ResourceConflictException(userRequest.getId(), "Username already exists");
		}
		User user = mapper.fromDTOtoUser(userRequest);
		user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

		user = this.userService.save(user);

		Driver driver = new Driver(user);
		driver.setRoles(getRoles(2));
		this.driverService.addDriver(driver);

		return new ResponseEntity<>(driver, HttpStatus.CREATED);
	}
}