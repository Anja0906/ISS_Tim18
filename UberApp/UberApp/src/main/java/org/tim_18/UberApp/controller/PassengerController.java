//package org.tim_18.UberApp.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.tim_18.UberApp.dto.PassengerDTO;
//import org.tim_18.UberApp.dto.passengerDTOs.PassengerDTOnoPassword;
//import org.tim_18.UberApp.dto.passengerDTOs.PassengerDTOwithPassword;
//import org.tim_18.UberApp.dto.UserActivationDTO;
//import org.tim_18.UberApp.mapper.passengerDTOmappers.PassengerDTOnoPasswordMapper;
//import org.tim_18.UberApp.mapper.passengerDTOmappers.PassengerDTOwithPasswordMapper;
//import org.tim_18.UberApp.model.Location;
//import org.tim_18.UberApp.model.Passenger;
//import org.tim_18.UberApp.model.Ride;
//import org.tim_18.UberApp.service.PassengerService;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/passenger")
//public class PassengerController {
//    private final PassengerService passengerService;
//    @Autowired
//    private PassengerDTOwithPasswordMapper dtoWithPasswordMapper;
//    @Autowired
//    private PassengerDTOnoPasswordMapper dtoNoPasswordMapper;
//
//    public PassengerController(PassengerService passengerService) {
//        this.passengerService = passengerService;
//    }
//
//    @PostMapping()
//    public ResponseEntity<PassengerDTOnoPassword> addPassenger(@RequestBody PassengerDTOwithPassword userActivationDTO) {
//        PassengerDTOwithPassword dto = passengerService.save(userActivationDTO);
//        Passenger passenger = dtoWithPasswordMapper.fromDTOtoPassenger(dto);
//        PassengerDTOnoPassword retDto = dtoNoPasswordMapper.fromPassengerToDTO(passenger);
//        return new ResponseEntity<>(retDto, HttpStatus.CREATED);
//    }
//
////    @GetMapping()
////    public ResponseEntity<FindAllDTO<PassengerDTOnoPassword>> findAll() {
////        List<Passenger> passengers = passengerService.findAll();
////        List<PassengerDTOnoPassword> passengersDTO = new ArrayList<>() ;
////        for (Passenger p : passengers) {
////            passengersDTO.add(new PassengerDTOnoPassword(p));
////        }
////        FindAllDTO<PassengerDTOnoPassword> retDTO = new FindAllDTO<PassengerDTOnoPassword>(passengersDTO);
////        return new ResponseEntity<>(retDTO, HttpStatus.OK);
////    }
//
//    @GetMapping("activate/{activationId}")
//    public ResponseEntity<UserActivationDTO> activateUser(){
//        return new ResponseEntity<>(new UserActivationDTO(), HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Passenger> findById(@PathVariable("id") Integer id) {
//        Passenger passenger = passengerService.findById(id);
//        return new ResponseEntity<>(passenger, HttpStatus.OK);
//    }
//
//
//    @PostMapping()
//    public ResponseEntity<PassengerDTO> addPassenger(@RequestBody PassengerDTO userActivationDTO) {
//        Passenger passenger = new Passenger();
//        passenger.setName(userActivationDTO.getName());
//        passenger.setSurname(userActivationDTO.getSurname());
//        passenger.setAddress(userActivationDTO.getAddress());
//        passenger.setEmail(userActivationDTO.getEmail());
//        passenger.setPassword(userActivationDTO.getPassword());
//        passenger.setProfilePicture(userActivationDTO.getProfilePicture());
//        passenger.setTelephoneNumber(userActivationDTO.getTelephoneNumber());
//        PassengerDTO retDTO = new PassengerDTO(passenger);
//        HashSet<Location> locs = new HashSet<>();
//        HashSet<Ride> rides = new HashSet<>();
//        passenger.setFavouriteLocations(locs);
////        passenger.setRides(rides);
//        return new ResponseEntity<>(retDTO, HttpStatus.CREATED);
//    }
//
//
//    @PutMapping("/{id}/ride")
//    public ResponseEntity<Passenger> updatePassenger(@RequestBody Passenger passenger) {
//        Passenger updatedPassenger = passengerService.updatePassenger(passenger);
//        return new ResponseEntity<>(updatedPassenger, HttpStatus.OK);
//    }
//}
