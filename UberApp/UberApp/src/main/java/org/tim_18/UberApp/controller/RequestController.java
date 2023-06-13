package org.tim_18.UberApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.RequestDTO;
import org.tim_18.UberApp.dto.userDTOs.UserDTO;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.model.Request;
import org.tim_18.UberApp.service.RequestService;
import org.tim_18.UberApp.service.UserService;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("api/request")
@CrossOrigin(value = "*")
public class RequestController {

    @Autowired
    RequestService requestService;

    @Autowired
    UserService userService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @PostMapping("/{id}")
    public void updateDriver(@PathVariable("id") int id, @RequestBody UserDTO userDTO) {
        try{
            userService.findUserById(id);
            userDTO.setId(id);
            requestService.makeRequest(userDTO);
        }catch (UserNotFoundException e){
            System.out.println("User not found");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<?> getRequests () {
        try {
            List<Request> requests = requestService.findAll();
            ArrayList<RequestDTO> requestDTOS = new RequestDTO().makeRequestDTO(requests);
            return new ResponseEntity<>(requestDTOS, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User does not exist!", HttpStatus.NOT_FOUND);
        }
    }


    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteDriverRequest(@PathVariable("id") int id) {
        try{
            requestService.deleteByUserId(id);
        }catch (UserNotFoundException e){
            System.out.println("User not found");
        }
    }
}
