package org.tim_18.UberApp.controller;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.Validation.ErrorMessage;
import org.tim_18.UberApp.dto.PanicDTO;
import org.tim_18.UberApp.dto.RejectionDTO;
import org.tim_18.UberApp.dto.locationDTOs.LocationDTO;
import org.tim_18.UberApp.dto.locationDTOs.LocationSetDTO;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerEmailDTO;
import org.tim_18.UberApp.dto.rideDTOs.RideRecDTO;
import org.tim_18.UberApp.dto.rideDTOs.RideRetDTO;
import org.tim_18.UberApp.exception.DriverNotFoundException;
import org.tim_18.UberApp.exception.PassengerNotFoundException;
import org.tim_18.UberApp.exception.RideNotFoundException;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.mapper.LocationDTOMapper;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.service.*;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/ride")
@CrossOrigin(origins = "http://localhost:4200")
public class RideController {

    private final RideService rideService;
    private final DriverService driverService;
    private final RejectionService rejectionService;
    private final ReviewService reviewService;
    private final PanicService panicService;
    private final PassengerService passengerService;
    private final UserService userService;


    private LocationDTOMapper locationDTOMapper = new LocationDTOMapper(new ModelMapper());


    public RideController(RideService rideService, DriverService driverService, RejectionService rejectionService, ReviewService reviewService, PanicService panicService, PassengerService passengerService, UserService userService) {
        this.rideService        = rideService;
        this.driverService      = driverService;
        this.rejectionService   = rejectionService;
        this.reviewService      = reviewService;
        this.panicService       = panicService;
        this.passengerService   = passengerService;
        this.userService        = userService;
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @PostMapping
    public ResponseEntity<?> createARide(Principal principal, @RequestBody RideRecDTO oldDTO){
        User user = userService.findUserByEmail(principal.getName());
        boolean found = false;
        for (PassengerEmailDTO p:oldDTO.getPassengers()) {
            if (p.getId().equals(user.getId())){
                found = true;
                break;
            }
        }
        if (!found) {
            return new ResponseEntity<>("Cannot make ride for other people!",HttpStatus.NOT_FOUND);
        }
        boolean canMakeRide = rideService.checkRide(user.getId());
        if (canMakeRide) {
            Passenger p = passengerService.findById(user.getId());
            Set<Ride> pr = p.getRides();
            Ride ride = rideService.createRide(fromDTOtoRide(oldDTO));
            pr.add(ride);
            p.setRides(new HashSet<>(pr));
            passengerService.update(p);
            return new ResponseEntity<>(new RideRetDTO(ride), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMessage("Cannot create a ride while you have one already pending!"),HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('DRIVER')")
    @GetMapping("/driver/{driverId}/active")
    public ResponseEntity<?> getDriverActiveRide(Principal principal, @PathVariable("driverId") Integer driverId) {
        try {
            User user = userService.findUserByEmail(principal.getName());
            if (!user.getId().equals(driverId)){
                throw new RideNotFoundException("Active ride does not exist!");
            }
            Ride ride = rideService.getDriverActiveRide(driverId);
            return new ResponseEntity<>(new RideRetDTO(ride), HttpStatus.OK);
        } catch(RideNotFoundException e){
            return new ResponseEntity<>("Active ride does not exist!",HttpStatus.NOT_FOUND);
        }
    }
    @PreAuthorize("hasRole('PASSENGER')")
    @GetMapping("/passenger/{passengerId}/active")
    public ResponseEntity<?> getPassengerActiveRide(Principal principal, @PathVariable("passengerId") Integer passengerId) {
        try {
            User user = userService.findUserByEmail(principal.getName());
            if (!user.getId().equals(passengerId)){
                throw new RideNotFoundException("Active ride does not exist!");
            }
            Ride ride = rideService.getPassengerActiveRide(passengerId);
            return new ResponseEntity<>(new RideRetDTO(ride), HttpStatus.OK);
        } catch(RideNotFoundException e){
            return new ResponseEntity<>("Active ride does not exist!",HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> findRideById(Principal principal, @PathVariable("id") Integer id) {
        try {
            Ride ride = rideService.findRideById(id);
            int role = checkRole(principal);
            if (role == 1){
                checkDriversAuthorities(principal, ride);
            } else {
                checkPassengersAuthorities(principal, ride);
            }
            return new ResponseEntity<>(new RideRetDTO(ride), HttpStatus.OK);
        } catch (RideNotFoundException e) {
            return new ResponseEntity<>("Ride does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<Map<String, Object>> getPendingRides(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Ride> rides = rideService.findPendingRidesByStatus("PENDING",pageable);
        Map<String, Object> map = new HashMap<>();
        HashSet<RideRetDTO> rideRetDTOS = new RideRetDTO().makeRideRideDTOS(rides);

        map.put("totalCount",rideRetDTOS.size());
        map.put("results",rideRetDTOS);
        return new ResponseEntity<>(map, HttpStatus.OK);
        //TREBA OVO IZMENITI DA DOBIJA PENDING RIDES ZA POSEBNOG DRIVERA SA ID JER JOS UVEK NEMAMO LOGIN

    }

    @PreAuthorize("hasRole('PASSENGER')")
    @PutMapping("/{id}/withdraw")
    public ResponseEntity<?> cancelRide(Principal principal, @PathVariable("id") Integer id) {
        try {
            Ride ride = rideService.findRideById(id);
            checkPassengersAuthorities(principal, ride);
            Status status = ride.getStatus();
            if (status == Status.PENDING || status == Status.STARTED) {
                ride.setStatus(Status.CANCELLED);
                ride = rideService.updateRide(ride);
                return new ResponseEntity<>(new RideRetDTO(ride), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorMessage("Cannot cancel a ride that is not in status PENDING or STARTED!"), HttpStatus.BAD_REQUEST);
            }
        } catch(RideNotFoundException e){
            return new ResponseEntity<>("Ride does not exist!",HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    @PutMapping("/{id}/panic")
    public ResponseEntity<?> activatePanic(Principal principal, @PathVariable("id") Integer id, @RequestBody String reason){
        try {
            Ride ride = rideService.findRideById(id);
            int role = checkRole(principal);
            if (role == 1){
                checkDriversAuthorities(principal, ride);
            } else {
                checkPassengersAuthorities(principal, ride);
            }
            User user = userService.findUserByEmail(principal.getName());
            Panic panic = new Panic(ride, user, new Date(), reason);
            panic = panicService.addPanic(panic);
            ride.setPanic(panic);
            return new ResponseEntity<>(new PanicDTO(panic), HttpStatus.OK);
        } catch(RideNotFoundException e){
            return new ResponseEntity<>("Ride does not exist!",HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('DRIVER')")
    @PutMapping("/{id}/accept")
    public ResponseEntity<?> acceptRide(Principal principal, @PathVariable("id")Integer id) {
        try {
            Ride ride = rideService.findRideById(id);
            checkDriversAuthorities(principal, ride);
            Status status = ride.getStatus();
            if (status == Status.PENDING) {
                ride.setStatus(Status.ACCEPTED);
                ride = rideService.updateRide(ride);
                return new ResponseEntity<>(new RideRetDTO(ride), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorMessage("Cannot accept a ride that is not in status PENDING!"), HttpStatus.BAD_REQUEST);
            }
        } catch(RideNotFoundException e){
            return new ResponseEntity<>("Ride does not exist!",HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @PutMapping("/{id}/start")
    public ResponseEntity<?> startRide(Principal principal, @PathVariable("id")Integer id) {
        try {
            Ride ride = rideService.findRideById(id);
            checkPassengersAuthorities(principal, ride);
            Status status = ride.getStatus();
            if (status == Status.ACCEPTED) {
                ride.setStatus(Status.STARTED);
                ride = rideService.updateRide(ride);
                return new ResponseEntity<>(new RideRetDTO(ride), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorMessage("Cannot accept a ride that is not in status ACCEPTED!"), HttpStatus.BAD_REQUEST);
            }
        } catch(RideNotFoundException e){
            return new ResponseEntity<>("Ride does not exist!",HttpStatus.NOT_FOUND);
        }
    }
    @PreAuthorize("hasRole('DRIVER')")
    @PutMapping("/{id}/end")
    public ResponseEntity<?> endRide(Principal principal, @PathVariable("id")Integer id) {
        try {
            Ride ride = rideService.findRideById(id);
            checkDriversAuthorities(principal, ride);
            Status status = ride.getStatus();
            if (status == Status.STARTED) {
                ride.setStatus(Status.FINISHED);
                ride = rideService.updateRide(ride);
                return new ResponseEntity<>(new RideRetDTO(ride), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorMessage("Cannot accept a ride that is not in status STARTED!"), HttpStatus.BAD_REQUEST);
            }
        } catch(RideNotFoundException e){
            return new ResponseEntity<>("Ride does not exist!",HttpStatus.NOT_FOUND);
        }
    }


    @PreAuthorize("hasRole('DRIVER')")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelRide(Principal principal, @PathVariable("id") Integer id,  @RequestBody String reason){
        try {
            Ride ride = rideService.findRideById(id);
            checkDriversAuthorities(principal, ride);
            Status status = ride.getStatus();
            if (status == Status.PENDING || status == Status.ACCEPTED) {
                ride.setStatus(Status.REJECTED);
                Rejection rejection = new Rejection(reason, new Date(), ride);
                ride.setRejection(rejection);
               // ride = rideService.updateRide(ride);
                return new ResponseEntity<>(new RideRetDTO(ride), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorMessage("Cannot accept a ride that is not in status PENDING or ACCEPTED!"), HttpStatus.BAD_REQUEST);
            }
        } catch(RideNotFoundException e){
            return new ResponseEntity<>("Ride does not exist!",HttpStatus.NOT_FOUND);
        }
    }

    private void checkPassengersAuthorities(Principal principal, Ride ride) throws RideNotFoundException{
        User user = userService.findUserByEmail(principal.getName());
        Integer userId = user.getId();
        Set<Passenger> passengers = ride.getPassengers();
        for (Passenger p : passengers) {
            if (p.getId().equals(userId)) {
                // passenger ima pristup ride-u i ne mora da se baci exception
                // ali nam nije bitan pa funkcija ne mora nista bitno da radi :P
                return;
            }
        }
        throw new RideNotFoundException("Active ride does not exist!");
    }

    private void checkDriversAuthorities(Principal principal, Ride ride) throws RideNotFoundException{
        User user = userService.findUserByEmail(principal.getName());
        Integer userId = user.getId();
        Driver driver = ride.getDriver();
        if (!driver.getId().equals(userId)) {
            throw new RideNotFoundException("Active ride does not exist!");
        }
    }

    // 1 - driver
    // 2 - passenger
    // iz nekog razloga role nece da prepozna sam sebe
    private int checkRole(Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        Integer userId = user.getId();
        boolean isPassenger = false;
        boolean isDriver = false;
        while (true) {
            try {
                if (!isDriver) {
                    Passenger passenger = passengerService.findById(userId);
//                    isPassenger = true;
                    return 2;
                }
                if (!isPassenger) {
                    Driver driver = driverService.findDriverById(userId);
//                    isDriver = true;
                    return 1;
                }
            }
            catch (PassengerNotFoundException e) {
                isPassenger = false;
                isDriver = true;
            }
            catch (DriverNotFoundException e) {
                isDriver = false;
                isPassenger = true;
            }
        }
    }

    private Ride fromDTOtoRide(RideRecDTO dto) throws UserNotFoundException {
        Date startTime = new Date();
        Date endTime = new Date();
        long totalCost = 5000;
        List<Driver> drivers = driverService.findAllDrivers();
        if (drivers.size() == 0) throw new UserNotFoundException("No available driver");
        Driver driver = drivers.get(0);
        Set<PassengerEmailDTO> passengersDTOs = dto.getPassengers();
        HashSet<Passenger> passengers = new HashSet<>();
        for (PassengerEmailDTO passDTO: passengersDTOs) {
            passengers.add(passengerService.findById(passDTO.getId()));
        }
        int estimatedTimeInMinutes = 4;
        Set<LocationSetDTO> locationsDTO = dto.getLocations();
        ArrayList<LocationSetDTO> locationSetDTOArrayList = new ArrayList<>();
        for (LocationSetDTO locDTO:locationsDTO) {
            locationSetDTOArrayList.add(locDTO);
        }
        HashSet<Location> locations = new HashSet<>();
        for (int i = 0; i < locationSetDTOArrayList.size(); i++) {
            LocationSetDTO lsd = locationSetDTOArrayList.get(i);
            LocationDTO ld = lsd.getDeparture();
            Location loc = LocationDTOMapper.fromDTOtoLocation(ld);
            locations.add(loc);
            if (i == locationSetDTOArrayList.size() - 1 ) {
                ld = lsd.getDestination();
                loc = LocationDTOMapper.fromDTOtoLocation(ld);
                locations.add(loc);
            }
        }
        Status status = Status.PENDING;
        HashSet<Review> reviews = new HashSet<>();
        Review review = new Review();
        review = reviewService.addReview(review);
        reviews.add(review);
        Panic panic = new Panic();
        panic = panicService.addPanic(panic);
        List<Rejection> rejections = rejectionService.findAll();
        Rejection newRejection;
        if (rejections.size() == 0) {
            newRejection = null;
        }else {
            newRejection = rejections.get(0);
        }
        RejectionDTO rejectionDTO = new RejectionDTO(newRejection);
        Instant instant = Instant.parse(dto.getScheduledTime());
        Date date = Date.from(instant);
        return new Ride(startTime, endTime, totalCost, driver, passengers, estimatedTimeInMinutes, dto.getVehicleType(),
                dto.isBabyTransport(), dto.isPetTransport(), newRejection, locations, status, reviews, panic, date);
    }
}
