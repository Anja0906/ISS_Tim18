package org.tim_18.UberApp.controller;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.tim_18.UberApp.Validation.ErrorMessage;
import org.tim_18.UberApp.dto.Distance.DriverTime;
import org.tim_18.UberApp.dto.Distance.DurationDistance;
import org.tim_18.UberApp.dto.Distance.OsrmResponse;
import org.tim_18.UberApp.dto.PanicDTO;
import org.tim_18.UberApp.dto.PanicSocketDTO;
import org.tim_18.UberApp.dto.ReasonDTO;
import org.tim_18.UberApp.dto.locationDTOs.LocationDTO;
import org.tim_18.UberApp.dto.locationDTOs.LocationSetDTO;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerEmailDTO;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerIdEmailDTO;
import org.tim_18.UberApp.dto.rideDTOs.*;
import org.tim_18.UberApp.exception.*;
import org.tim_18.UberApp.mapper.LocationDTOMapper;
import org.tim_18.UberApp.mapper.rideDTOmappers.FavoriteRideDTOMapper;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.service.*;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping("/api/ride")
@CrossOrigin(value = "*")
public class RideController {

    private final RideService rideService;
    private final DriverService driverService;
    private final RejectionService rejectionService;
    private final ReviewService reviewService;
    private final PanicService panicService;
    private final PassengerService passengerService;
    private final UserService userService;
    private final FavoriteRideService favoriteRideService;

    private final LocationService locationService;

    private final LocationsForRideService locationsForRideService;

    private final LocationsForFavoriteRideService locationsForFavoriteRideService;

    private final VehiclePriceService vehiclePriceService;

    private final RoleService roleService;


    private LocationDTOMapper locationDTOMapper = new LocationDTOMapper(new ModelMapper());
    private FavoriteRideDTOMapper favoriteRideDTOMapper = new FavoriteRideDTOMapper(new ModelMapper());

    private final WorkTimeService workTimeService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    public RideController(SimpMessagingTemplate simpMessagingTemplate, RideService rideService, DriverService driverService, RejectionService rejectionService, ReviewService reviewService, PanicService panicService, PassengerService passengerService, UserService userService, FavoriteRideService favoriteRideService, LocationService locationService, LocationsForRideService locationsForRideService, LocationsForFavoriteRideService locationsForFavoriteRideService, VehiclePriceService vehiclePriceService, RoleService roleService, WorkTimeService workTimeService) {
        this.rideService        = rideService;
        this.driverService      = driverService;
        this.rejectionService   = rejectionService;
        this.reviewService      = reviewService;
        this.panicService       = panicService;
        this.passengerService   = passengerService;
        this.userService        = userService;
        this.favoriteRideService = favoriteRideService;
        this.locationService = locationService;
        this.locationsForRideService = locationsForRideService;
        this.locationsForFavoriteRideService = locationsForFavoriteRideService;
        this.vehiclePriceService = vehiclePriceService;
        this.roleService = roleService;
        this.workTimeService = workTimeService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }
    @PreAuthorize("hasRole('PASSENGER')")
    @PostMapping
    public ResponseEntity<?> createRide(Principal principal, @RequestBody RideRecDTO oldDTO) {
        try {
            checkPassengersForRide(principal, oldDTO);
            checkScheduledTime(oldDTO);

            Ride ride = fromDTOtoRide(oldDTO);
            ride = rideService.createRide(ride);
            Set<LocationsForRide> lfr = addLocations(oldDTO, ride);
            addPassengers(oldDTO, ride);
            if (ride.getDriver()!=null) {
                Integer id = ride.getDriver().getId();
                this.simpMessagingTemplate.convertAndSend("/socket-topic/driver-new-ride/" + id, new RideRetDTO(ride, lfr));
            }
            for (Passenger passenger : ride.getPassengers()) {
                this.simpMessagingTemplate.convertAndSend("/socket-topic/passenger-new-ride/" + passenger.getId(), new RideRetDTO(ride, lfr));
            }
            return new ResponseEntity<>(new RideRetDTO(ride, lfr), HttpStatus.OK);
        } catch (PassengerNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (DriverNotFoundException e) {
            return new ResponseEntity<>("No driver found", HttpStatus.NOT_FOUND);
        }
    }

    private void checkPassengersForRide(Principal principal, RideRecDTO oldDTO) throws PassengerNotFoundException, BadRequestException {
        User user = userService.findUserByEmail(principal.getName());
        boolean found = false;
        Passenger passenger;
        boolean canMakeRide;
        for (PassengerIdEmailDTO p : oldDTO.getPassengers()) {
            try {
                String email = p.getEmail();
                passenger = passengerService.findByEmail(email);
                canMakeRide = rideService.checkRide(passenger.getId());
                if (!canMakeRide) {
                    throw new BadRequestException("Cannot create a ride while you have one already pending!");
                }
            } catch (PassengerNotFoundException e) {
                throw new PassengerNotFoundException("Passenger not found!");
            }
            if (passenger.isBlocked()) {
                throw new PassengerNotFoundException("Passenger with id " + passenger.getId() + " is blocked and cannot order ride!");
            }
            if (p.getEmail().equals(user.getEmail())) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new PassengerNotFoundException("Cannot make ride for other people!");
        }
    }

    private void checkScheduledTime(RideRecDTO oldDTO) throws BadRequestException {
        if (oldDTO.getScheduledTime()==null) {
            return;
        }
        String scheduledTime = oldDTO.getScheduledTime();
        Instant instant = Instant.parse(scheduledTime);
        Date scheduledTimeDate = Date.from(instant);
        long diff = scheduledTimeDate.getTime() - new Date().getTime();
        if (diff <= 0) {
            throw new BadRequestException("Scheduled time should be in the future!");
        }
        long diffHours = diff / (60 * 60 * 1000) % 24;
        if (diffHours > 5) {
            throw new BadRequestException("You can schedule only in next 5 hours!");
        }
    }

    private Set<LocationsForRide> addLocations(RideRecDTO oldDTO, Ride ride) {
//        Set<Ride> rides = new HashSet<>();
//        rides.add(ride);
        Set<LocationSetDTO> locs = oldDTO.getLocations();
        Set<LocationsForRide> locationsForRides = new HashSet<>();
//        Set<Location> ridesLocations = new HashSet<>();
        for (LocationSetDTO locationSetDTO : locs) {
            LocationDTO departureDTO = locationSetDTO.getDeparture();
            Location departure = locationService.findLocationByAddressLongitudeLatitude(departureDTO.getLongitude(), departureDTO.getLatitude(), departureDTO.getAddress());
            if (departure == null) {
                departure = locationService.addLocation(new Location(departureDTO.getLongitude(), departureDTO.getLatitude(), departureDTO.getAddress()));
            }
//            ridesLocations.add(departure);
//            departure.setRides(rides);
            locationService.updateLocation(departure);

            LocationDTO destinationDTO = locationSetDTO.getDestination();
            Location destination = locationService.findLocationByAddressLongitudeLatitude(destinationDTO.getLongitude(), destinationDTO.getLatitude(), destinationDTO.getAddress());
            if (destination == null) {
                destination = locationService.addLocation(new Location(destinationDTO.getLongitude(), destinationDTO.getLatitude(), destinationDTO.getAddress()));
            }
//            ridesLocations.add(destination);
//            destination.setRides(rides);
            locationService.updateLocation(destination);

            DurationDistance dd = getDurationDistance(departureDTO.getLatitude(), departure.getLongitude(), destination.getLatitude(), destination.getLongitude());
            LocationsForRide lfr = new LocationsForRide(departure, destination, dd.getDistance(), dd.getDuration(), ride);
            lfr = locationsForRideService.addLocationsForRide(lfr);
            locationsForRides.add(lfr);
        }
//        ride.setLocations(new HashSet<>(ridesLocations));
        return locationsForRides;
    }

    private void addPassengers(RideRecDTO oldDTO, Ride ride) {
        for (PassengerIdEmailDTO ped : oldDTO.getPassengers()) {
            Passenger p = passengerService.findByEmail(ped.getEmail());
            Set<Ride> pr = p.getRides();
            pr.add(ride);
            p.setRides(new HashSet<>(pr));
            passengerService.update(p);
        }
    }

    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @GetMapping("/driver/{driverId}/active")
    public ResponseEntity<?> getDriverActiveRide(Principal principal, @PathVariable("driverId") Integer driverId) {
        try {
            User user = userService.findUserByEmail(principal.getName());
            if (!user.getId().equals(driverId)){
                return new ResponseEntity<>("Active ride does not exist!",HttpStatus.NOT_FOUND);
            }
            Ride ride = rideService.getDriverActiveRide(driverId);
            return new ResponseEntity<>(new RideRetDTO(ride, getLocationsByRideId(ride.getId())), HttpStatus.OK);
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
                return new ResponseEntity<>("Active ride does not exist!",HttpStatus.NOT_FOUND);
            }
            Ride ride = rideService.getPassengerActiveRide(passengerId);
            return new ResponseEntity<>(new RideRetDTO(ride, getLocationsByRideId(ride.getId())), HttpStatus.OK);
        } catch(RideNotFoundException e){
            return new ResponseEntity<>("Active ride does not exist!",HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> findRideById(Principal principal, @PathVariable("id") Integer id) {
        try {
            Ride ride = rideService.findRideById(id);
            checkRole(principal, ride);

            return new ResponseEntity<>(new RideRetDTO(ride, getLocationsByRideId(id)), HttpStatus.OK);
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
        HashSet<RideRetDTO> rideRetDTOS = makeRideRideDTOS(rides);

        map.put("totalCount",rideRetDTOS.size());
        map.put("results",rideRetDTOS);
        return new ResponseEntity<>(map, HttpStatus.OK);
        //TREBA OVO IZMENITI DA DOBIJA PENDING RIDES ZA POSEBNOG DRIVERA SA ID JER JOS UVEK NEMAMO LOGIN
    }

    private HashSet<RideRetDTO> makeRideRideDTOS(Page<Ride> rides) {
        HashSet<RideRetDTO> ridesDTO = new HashSet<>();
        for (Ride r : rides) {
            ridesDTO.add(new RideRetDTO(r, getLocationsByRideId(r.getId())));
        }
        return ridesDTO;
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @PutMapping("/{id}/withdraw")
    public ResponseEntity<?> withdrawRide(Principal principal, @PathVariable("id") Integer id) {
        try {
            Ride ride = rideService.findRideById(id);
            checkPassengersAuthorities(principal, ride);
            Status status = ride.getStatus();
            if (status == Status.PENDING || status == Status.ACCEPTED) {
                ride.setStatus(Status.CANCELLED);
                Rejection rejection = ride.getRejection();
                rejection.setTime(new Date());
                rejection.setReason("");
                rejectionService.updateRejection(rejection);
                ride.setRejection(rejection);
                ride = rideService.updateRide(ride);
                if (ride.getDriver()!=null) {
                    this.simpMessagingTemplate.convertAndSend("/socket-topic/withdraw/" + ride.getDriver().getId(), new RideRetDTO(ride, getLocationsByRideId(ride.getId())));
                }
                return new ResponseEntity<>(new RideRetDTO(ride, getLocationsByRideId(ride.getId())), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorMessage("Cannot cancel a ride that is not in status PENDING or ACCEPTED!"), HttpStatus.BAD_REQUEST);
            }
        } catch(RideNotFoundException e){
            return new ResponseEntity<>("Ride does not exist!",HttpStatus.NOT_FOUND);
        }
    }
//
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    @PutMapping("/{id}/panic")
    public ResponseEntity<?> activatePanic(Principal principal, @PathVariable("id") Integer id, @RequestBody ReasonDTO reason){
        try {
            Ride ride = rideService.findRideById(id);
            checkRole(principal, ride);
            Status status = ride.getStatus();
            if (status == Status.STARTED) {
                User user = userService.findUserByEmail(principal.getName());
                Panic panic = panicService.findById(ride.getPanic().getId());
                panic.updatePanic(user,reason,ride);
                panic = panicService.addPanic(panic);
                ride.setPanic(panic);
                rideService.updateRide(ride);
                this.simpMessagingTemplate.convertAndSend("/socket-topic/newPanic", new PanicSocketDTO(panic));
                return new ResponseEntity<>(new PanicDTO(panic, getLocationsByRideId(ride.getId())), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorMessage("Cannot panic in ride that is not in status STARTED!"), HttpStatus.BAD_REQUEST);
            }
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
                for (Passenger passenger : ride.getPassengers()) {
                    this.simpMessagingTemplate.convertAndSend("/socket-topic/accepted/" + passenger.getId(), new RideRetDTO(ride, getLocationsByRideId(ride.getId())));
                }
                return new ResponseEntity<>(new RideRetDTO(ride, getLocationsByRideId(ride.getId())), HttpStatus.OK);
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
                this.simpMessagingTemplate.convertAndSend("/socket-topic/started/" + ride.getDriver().getId(), new RideRetDTO(ride, getLocationsByRideId(ride.getId())));
                return new ResponseEntity<>(new RideRetDTO(ride, getLocationsByRideId(ride.getId())), HttpStatus.OK);
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
                for (Passenger passenger : ride.getPassengers()) {
                    this.simpMessagingTemplate.convertAndSend("/socket-topic/ended/" + passenger.getId(), new RideRetDTO(ride, getLocationsByRideId(ride.getId())));
                }
                return new ResponseEntity<>(new RideRetDTO(ride, getLocationsByRideId(ride.getId())), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorMessage("Cannot accept a ride that is not in status STARTED!"), HttpStatus.BAD_REQUEST);
            }
        } catch(RideNotFoundException e){
            return new ResponseEntity<>("Ride does not exist!",HttpStatus.NOT_FOUND);
        }
    }


    @PreAuthorize("hasRole('DRIVER')")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelRide(Principal principal, @PathVariable("id") Integer id,  @RequestBody ReasonDTO reason){
        try {
            Ride ride = rideService.findRideById(id);
            checkDriversAuthorities(principal, ride);
            Status status = ride.getStatus();
            User user = userService.findUserById(ride.getDriver().getId());
            if (status == Status.PENDING || status == Status.ACCEPTED) {
                ride.setStatus(Status.REJECTED);
                Rejection rejection = new Rejection(ride, user, new Date(), reason.getReason());
                rejectionService.addRejection(rejection);
                ride.setRejection(rejection);
                ride = rideService.updateRide(ride);
                for (Passenger passenger : ride.getPassengers()) {
                    this.simpMessagingTemplate.convertAndSend("/socket-topic/cancelled/" + passenger.getId(), new RideRetDTO(ride, getLocationsByRideId(ride.getId())));
                }
                return new ResponseEntity<>(new RideRetDTO(ride, getLocationsByRideId(ride.getId())), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorMessage("Cannot accept a ride that is not in status PENDING or ACCEPTED!"), HttpStatus.BAD_REQUEST);
            }
        } catch(RideNotFoundException e){
            return new ResponseEntity<>("Ride does not exist!",HttpStatus.NOT_FOUND);
        }
    }


    @PreAuthorize("hasRole('PASSENGER')")
    @PostMapping("/favorites")
    public ResponseEntity<?> createFavRide(Principal principal, @RequestBody FavoriteRideDTO oldDTO){
        try {
            FavoriteRide ride = favoriteRideDTOMapper.fromDTOtoRide(oldDTO);
            User user = userService.findUserByEmail(principal.getName());
            Integer userId = user.getId();
            Passenger p = passengerService.findById(user.getId());

            Set<PassengerIdEmailDTO> passengersDTOs = oldDTO.getPassengers();

            ride = handlePassengers(ride, userId, passengersDTOs, p);

            ride = favoriteRideService.createFavRide(ride);

            updatePassenger(ride, p);

            Set<LocationSetDTO> locs = oldDTO.getLocations();
            Set<LocationsForFavoriteRide> ridesLocations = addLocations(ride, locs);

            // osiguravamo da u dto objektu vrati samo passengera koji je poslao zahtev ali ne sacuva tako u bazi!
            // paranoicno
            Set<Passenger> passengerSet = new HashSet<>();
            passengerSet.add(p);
            ride.setPassengers(passengerSet);
            return new ResponseEntity<>(new FavoriteRideWithTimeDTO(ride, new Date(), ridesLocations), HttpStatus.OK);
        } catch (FavoriteRideNotFoundException e) {
            return new ResponseEntity<>("Favorite Ride does not exist",HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    private FavoriteRide handlePassengers(FavoriteRide ride, Integer userId, Set<PassengerIdEmailDTO> passengersDTOs, Passenger p) {
        Set<Passenger> passengerSet = ride.getPassengers();
        if (passengerSet.size() != 1) {
            throw new BadRequestException("Cannot make favorite ride for other people!");
        }
        for (Passenger pass : passengerSet) {
            if (!pass.getId().equals(userId)) {
                throw new BadRequestException("Cannot make favorite ride for other people!");
            }
        }
        HashSet<Passenger> passengers = new HashSet<>();
        for (PassengerIdEmailDTO passDTO: passengersDTOs) {
            passengers.add(passengerService.findById(passDTO.getId()));
        }
        ride.setPassengers(passengers);

        Set<FavoriteRide> pFavRides = p.getFavoriteRides();
        if (pFavRides.size() > 10) {
            throw new BadRequestException("Number of favorite rides cannot exceed 10!");
        }
        return ride;
    }

    private void updatePassenger(FavoriteRide ride, Passenger p) {
        Set<FavoriteRide> pr = p.getFavoriteRides();
        pr.add(ride);
        p.setFavoriteRides(new HashSet<>(pr));
        passengerService.update(p);
    }

    private Set<LocationsForFavoriteRide> addLocations(FavoriteRide ride, Set<LocationSetDTO> locs) {
        Set<LocationsForFavoriteRide> ridesLocations = new HashSet<>();
        for (LocationSetDTO locationSetDTO : locs) {
            LocationDTO departureDTO = locationSetDTO.getDeparture();
            Location departure = locationService.findLocationByAddressLongitudeLatitude(departureDTO.getLongitude(), departureDTO.getLatitude(), departureDTO.getAddress());
            if (departure==null) {
                departure = locationService.addLocation(new Location(departureDTO.getLongitude(), departureDTO.getLatitude(), departureDTO.getAddress()));
            }
//            ridesLocations.add(departure);
//            departure.setFavoriteRides(rides);
            locationService.updateLocation(departure);

            LocationDTO destinationDTO = locationSetDTO.getDestination();
            Location destination = locationService.findLocationByAddressLongitudeLatitude(destinationDTO.getLongitude(), destinationDTO.getLatitude(), destinationDTO.getAddress());
            if (destination==null) {
                destination = locationService.addLocation(new Location(destinationDTO.getLongitude(), destinationDTO.getLatitude(), destinationDTO.getAddress()));
            }
//            ridesLocations.add(destination);
//            destination.setFavoriteRides(rides);
            locationService.updateLocation(destination);
            LocationsForFavoriteRide lffr = new LocationsForFavoriteRide(departure, destination, ride);
            locationsForFavoriteRideService.addFavRide(lffr);
            ridesLocations.add(lffr);
        }
        return ridesLocations;
    }


    @PreAuthorize("hasRole('PASSENGER')")
    @GetMapping("/favorites")
    public ResponseEntity<?> findAllFavs(Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        Passenger passenger = passengerService.findById(user.getId());
        Set<Passenger> pS = new HashSet<>();
        pS.add(passenger);
        List<FavoriteRide> favoriteRides = favoriteRideService.findAllByPassenger(passenger.getId());
        for (FavoriteRide r : favoriteRides) {
            r.setPassengers(pS);
        }
        HashSet<FavoriteRideWithTimeDTO> favoriteRidesDTO = getFavoriteRidesDTO(favoriteRides);
        return new ResponseEntity<>(favoriteRidesDTO, HttpStatus.OK);
    }

    private HashSet<FavoriteRideWithTimeDTO> getFavoriteRidesDTO(List<FavoriteRide> rides) {
        HashSet<FavoriteRideWithTimeDTO> ridesDTO = new HashSet<>();
        for (FavoriteRide r : rides) {
            ridesDTO.add(new FavoriteRideWithTimeDTO(r, getLocationsByFavoriteRideId(r.getId())));
        }
        return ridesDTO;
    }

    private Set<LocationsForFavoriteRide> getLocationsByFavoriteRideId(Integer id) {
        return locationsForFavoriteRideService.getByRideId(id);
    }



    @PreAuthorize("hasRole('PASSENGER')")
    @DeleteMapping("/favorites/{id}")
    public ResponseEntity deleteFavRideById (Principal principal,
                                             @PathVariable("id") int id) {
        try {
            FavoriteRide ride = favoriteRideService.findById(id);
            checkPassengersAuthorities(principal, ride);
            Passenger passenger = passengerService.findById(userService.findUserByEmail(principal.getName()).getId());
            Set<Passenger> ps = ride.getPassengers();
            ps.remove(passenger);
            ride.setPassengers(ps);
            favoriteRideService.update(ride);
            Set<FavoriteRide> fr = passenger.getFavoriteRides();
            fr.remove(ride);
            passenger.setFavoriteRides(fr);
            passengerService.update(passenger);
            return new ResponseEntity<>("Successful deletion of favorite location!", HttpStatus.NO_CONTENT);
        } catch (FavoriteRideNotFoundException e) {
            return new ResponseEntity<>("Favorite location does not exist!" ,HttpStatus.NOT_FOUND);
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

    private void checkPassengersAuthorities(Principal principal, FavoriteRide ride) throws RideNotFoundException{
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
        throw new FavoriteRideNotFoundException("Favorite Ride does not exist");
    }

    private void checkDriversAuthorities(Principal principal, Ride ride) throws RideNotFoundException{
        User user = userService.findUserByEmail(principal.getName());
        Integer userId = user.getId();
        Driver driver = ride.getDriver();
        if (!driver.getId().equals(userId)) {
            throw new RideNotFoundException("Active ride does not exist!");
        }
    }


    private void checkRole(Principal principal, Ride ride) {
        User user = userService.findUserByEmail(principal.getName());
        if (user.getRoles().contains(roleService.findById(2))) {
            // passenger
            checkPassengersAuthorities(principal, ride);
        } else {
            // driver
            checkDriversAuthorities(principal, ride);
        }
    }

    private Ride fromDTOtoRide(RideRecDTO dto) throws DriverNotFoundException {
        Date date = null; // scheduled
        if (dto.getScheduledTime() != null) {
            Instant instant = Instant.parse(dto.getScheduledTime());
            date = Date.from(instant);
        }


        DriverTime driverTime = null;
        Driver driver = null;
        int time = -1;
        if (date == null) {
            driverTime = generateDriver(dto);
            driver = driverTime.getDriver(); // 404
            time = driverTime.getTime();
        }

        Date startTime = new Date();
        if (date==null) {
            date=startTime;
        }
        Calendar date1 = Calendar.getInstance();
        date1.setTime(startTime);
        long timeInSecs = date1.getTimeInMillis();

        if (date != null) {
            startTime = date;
        } else {
            startTime = new Date(timeInSecs + ((long) time * 60 * 1000));
        }

        date1.setTime(startTime);
        timeInSecs = date1.getTimeInMillis();


        Set<LocationSetDTO> locationSetDTOSet = dto.getLocations();
        DurationDistance depdis;
        double totalDistance = 0;
        double totalDuration = 0;
        for (LocationSetDTO loc : locationSetDTOSet) {
            LocationDTO departure = loc.getDeparture();
            LocationDTO destination = loc.getDestination();
            depdis = getDurationDistance(departure.getLatitude(), departure.getLongitude(), destination.getLatitude(), destination.getLongitude());
            totalDistance += depdis.getDistance();
            totalDuration += depdis.getDuration()/60;
        }

        Date endDate = new Date((long) (timeInSecs + (totalDuration * 60 * 1000)));

        VehiclePrice vehicle = vehiclePriceService.findVehiclePriceByVehicleType(dto.getVehicleType());
        double vehiclePrice = vehicle.getPrice();
        long totalCost = (long)(vehiclePrice + 120*totalDistance/1000);

        Set<PassengerIdEmailDTO> passengersDTOs = dto.getPassengers();
        HashSet<Passenger> passengers = new HashSet<>();
        for (PassengerIdEmailDTO passDTO: passengersDTOs) {
            passengers.add(passengerService.findByEmail(passDTO.getEmail()));
        }
        Status status = Status.PENDING;

        // manje bitno.................
        HashSet<Review> reviews = new HashSet<>();
        Review review = new Review();
        review = reviewService.addReview(review);
        reviews.add(review);
        Panic panic = new Panic();
        panic = panicService.addPanic(panic);
        Rejection newRejection = new Rejection();
        newRejection.setTime(new Date());
        newRejection.setReason("lalala");
        rejectionService.addRejection(newRejection);


        return new Ride(startTime, endDate, totalCost, driver, passengers, time, dto.getVehicleType(),
                dto.isBabyTransport(), dto.isPetTransport(), newRejection, status, reviews, panic, date);
    }

    private DriverTime generateDriver(RideRecDTO ride) throws DriverNotFoundException {
        // gledamo imali li drivera u sistemu :,(
        List<Driver> allDrivers = driverService.findAllDrivers();
        if (allDrivers.isEmpty()) {
            throw new DriverNotFoundException("");
        }

        List<Driver> activeDrivers = new ArrayList<>();
        for (Driver driver : allDrivers) {
            if (driver.isActive() && driver.getIsOnline()) {
                activeDrivers.add(driver);
            }
        }

        // ako ima, moramo proci kroz svakog drivera i njegovo vozilo
        // i videti da li ima odgovarajuce vozilo za ovu voznju
        List<Driver> potentialDriversCarEdition = new ArrayList<>();
        for (Driver driver : activeDrivers) {
            Vehicle vehicle = driver.getVehicle();
            if (!(vehicle.getVehicleType().equals(ride.getVehicleType()))) {
                continue;
            }
            if (vehicle.getPassengerSeats() < ride.getPassengers().size()) {
                continue;
            }
            if (ride.isBabyTransport() && !vehicle.getBabyTransport()) {
                continue;
            }
            if (ride.isPetTransport() && !vehicle.getPetTransport()) {
                continue;
            }

            // driver ima dobro vozilo
            potentialDriversCarEdition.add(driver);
        }

        if (potentialDriversCarEdition.isEmpty()) {
            throw new DriverNotFoundException("");
        }

        // vadimo jedan set lokacija koji je dosao sa fronta na malo tezi nacin jer je na backu set setova
        LocationSetDTO setDTO = getRoute(ride);
        LocationDTO departure = setDTO.getDeparture();
        LocationDTO destination = setDTO.getDestination();

        // racunamo estimated time da se obavi voznja - potrebno zbog scheduled ride
        DurationDistance depdis = getDurationDistance(departure.getLatitude(), departure.getLongitude(), destination.getLatitude(), destination.getLongitude());

        HashMap<Driver, Integer> durations = new HashMap<>();

        // u ovoj sekciji drugacije tretiramo scheduled ride od obicnog, zakazani ride ima prednost!
        // odnosno
        // ako gledamo ride koji nam treba sada, a imamo ride koji je zakazan, i mozda mu zatreba vozilo kakvom treba i trenutnom vozilo
        // a mi smo by far nasli jednog drivera,
        // koji nece uspeti da obavi voznju (estimatedTime) do 15 minuta pre zakazane (kada krece trazenje za zakazane)
        // takvu voznju ne mozemo da prihvatimo jer nam je bitnije da driver ostane slobodan za zakazanu voznju
        // ukoliko smo nasli vise takvih drivera, ovaj jedan jedini ce se vratiti, voznja je drugacije specifikacije itd
        // mozemo zakazati voznju
        boolean isScheduled = !(ride.getScheduledTime()==null);
        List<Ride> scheduledRides = new ArrayList<>();
        boolean scheduledRideExist = false;
        if (!isScheduled) {
            scheduledRides = rideService.findScheduledRides(depdis.getDuration() / 60 + 15, ride.getVehicleType(), ride.isBabyTransport(), ride.isPetTransport());
            System.out.println(depdis.getDuration() + 15);
//            System.out.println(ride.getVehicleType().toString());

            scheduledRideExist = !scheduledRides.isEmpty();
            if (scheduledRideExist && potentialDriversCarEdition.size() == 1) {
                throw new DriverNotFoundException("");
            }
        }
        // za svakog drivera gledamo trenutne i accepted voznje - voznje koje se desavaju ili ce se sigurno desiti
        for (Driver driver : potentialDriversCarEdition) {
            List<Ride> acceptedRides = rideService.getDriverAcceptedRides(driver.getId());
            Optional<Ride> currentRideOpt = rideService.getActiveRideDriver(driver.getId());
            Vehicle vehicle = driver.getVehicle();
            Location driversCurrentLocation = vehicle.getCurrentLocation();
            boolean acceptedRidesEmpty = acceptedRides.isEmpty();
            int totalMinutes = 0;
            //nece se desiti
            if (driversCurrentLocation == null || departure == null) {
                throw new DriverNotFoundException("");
            }

            // ako je ride zakazan, prethodnim postupkom smo obezbedili da sigurno postoji vozilo slobodno!
            // u suprotnom, za sada mozda ne postoji vozilo pa moramo da istrazujemo dalje....

            if (!isScheduled) {

                // slucaj 1 - driver nema nijedno, gledamo samo koliko mu vremena treba da ode do pocetka voznje
                if (currentRideOpt.isEmpty() && acceptedRidesEmpty) {
                    depdis = getDurationDistance(driversCurrentLocation.getLatitude(), driversCurrentLocation.getLongitude(), departure.getLatitude(), departure.getLongitude());
                    totalMinutes += (int) depdis.getDuration() / 60;
                }

                // slucaj 2 - imamo trenutnu voznju, nemamo accepted
                // gledamo koliko vremena je potrebno da se ta voznja zavrsi, pa odatle idemo do potencijalne naredne
                else if (currentRideOpt.isPresent() && acceptedRidesEmpty) {
                    totalMinutes = getTotalMinutesForCurrentRide(departure, currentRideOpt, driversCurrentLocation, totalMinutes);
                }


                // slucaj 3 - imamo accepted rides, nemamo current ride
                // gledamo koliko treba vremena dok krene prva, pa njeno trajanje, pa na to nadovezujemo put do naredne, pa njeno trajanje itd
                else if (currentRideOpt.isEmpty() && !acceptedRidesEmpty) {
                    totalMinutes = getTotalMinutesForAcceptedRides(departure, acceptedRides, totalMinutes, driversCurrentLocation);
                }
                // slucaj 4 - imamo i current ride i accepted rides, pa kombinujemo prethodna dva...
                // gledamo za koliko ce driver stici do sadasnje destinacije
                // pa za koliko ce zavrsiti accepted ride
                else {
                    Ride currentRide = currentRideOpt.get();
                    Set<LocationsForRide> locations = getLocationsByRideId(currentRide.getId());
                    Object[] locationSetDTOList = locations.toArray();
                    LocationsForRide locationsForRide = (LocationsForRide) locationSetDTOList[locationSetDTOList.length - 1];
                    Location currentDestination = locationsForRide.getDestination();

                    depdis = getDurationDistance(driversCurrentLocation.getLatitude(), driversCurrentLocation.getLongitude(), currentDestination.getLatitude(), currentDestination.getLongitude());
                    totalMinutes += (int) depdis.getDuration() / 60;
                    totalMinutes += getTotalMinutesForAcceptedRides(departure, acceptedRides, totalMinutes, currentDestination);
                }
            } else {
                depdis = getDurationDistance(driversCurrentLocation.getLatitude(), driversCurrentLocation.getLongitude(), departure.getLatitude(), departure.getLongitude());
                totalMinutes += (int) depdis.getDuration() / 60;
            }

            durations.put(driver, totalMinutes);
        }

        // nece se desiti
        if (durations.isEmpty()) {
            throw new DriverNotFoundException("");
        }

        Driver bestDriver = null;
        int minutes = 2147483645;
        for (Map.Entry<Driver, Integer> map : durations.entrySet()) {
            if (map.getValue() < minutes) {
                bestDriver = map.getKey();
                minutes = map.getValue();
            }
        }

        if (bestDriver==null) {
            throw new DriverNotFoundException("");
        }
        return new DriverTime(bestDriver, minutes);
    }

    private int getTotalMinutesForCurrentRide(LocationDTO departure, Optional<Ride> currentRideOpt, Location driversCurrentLocation, int totalMinutes) {
        DurationDistance depdis;
        Ride currentRide = currentRideOpt.get();
        Set<LocationsForRide> locations = getLocationsByRideId(currentRide.getId());
        Object[] locationSetDTOList = locations.toArray();
        LocationsForRide locationsForRide = (LocationsForRide)locationSetDTOList[locationSetDTOList.length - 1];
        Location currentDestination = locationsForRide.getDestination();
        depdis = getDurationDistance(driversCurrentLocation.getLatitude(), driversCurrentLocation.getLongitude(), currentDestination.getLatitude(), currentDestination.getLongitude());
        totalMinutes += (int)depdis.getDuration()/60;
        depdis = getDurationDistance(currentDestination.getLatitude(), currentDestination.getLongitude(), departure.getLatitude(), departure.getLongitude());
        totalMinutes += (int)depdis.getDuration()/60;
        return totalMinutes;
    }

    private int getTotalMinutesForAcceptedRides(LocationDTO departure, List<Ride> acceptedRides, int totalMinutes, Location driversCurrentLocation) {
        DurationDistance depdis;
        for (int i = 0; i < acceptedRides.size(); i++) {
            Ride acceptedRide = acceptedRides.get(i);
            Set<LocationsForRide> locations = getLocationsByRideId(acceptedRide.getId());
            Object[] locationSetDTOList = locations.toArray();
            LocationsForRide locationsForRide = (LocationsForRide)locationSetDTOList[locationSetDTOList.length - 1];
            Location acceptedDeparture = locationsForRide.getDeparture();
            Location acceptedDestination = locationsForRide.getDestination();
            if (i==0) {
                depdis = getDurationDistance(driversCurrentLocation.getLatitude(), driversCurrentLocation.getLongitude(), acceptedDeparture.getLatitude(), acceptedDeparture.getLongitude());
                totalMinutes += (int)depdis.getDuration()/60;
                depdis = getDurationDistance(acceptedDeparture.getLatitude(), acceptedDeparture.getLongitude(), acceptedDestination.getLatitude(), acceptedDestination.getLongitude());
                totalMinutes += (int)depdis.getDuration()/60;
            } else {
                Ride previousAcceptedRide = acceptedRides.get(i);
                Set<LocationsForRide> locationsPrevious = getLocationsByRideId(previousAcceptedRide.getId());
                Object[] locationSetDTOListPrevious = locationsPrevious.toArray();
                LocationsForRide locationsForRidePrevious = (LocationsForRide)locationSetDTOListPrevious[locationSetDTOListPrevious.length - 1];
                Location previousAcceptedDestination = locationsForRidePrevious.getDestination();
                depdis = getDurationDistance(previousAcceptedDestination.getLatitude(), previousAcceptedDestination.getLongitude(), acceptedDeparture.getLatitude(), acceptedDeparture.getLongitude());
                totalMinutes += (int)depdis.getDuration()/60;
                depdis = getDurationDistance(acceptedDeparture.getLatitude(), acceptedDeparture.getLongitude(), acceptedDestination.getLatitude(), acceptedDestination.getLongitude());
                totalMinutes += (int)depdis.getDuration()/60;
            }
            if (i <= acceptedRides.size() -1) {
                depdis = getDurationDistance(acceptedDestination.getLatitude(), acceptedDestination.getLongitude(), departure.getLatitude(), departure.getLongitude());
                totalMinutes += (int) depdis.getDuration() / 60;
            }
        }
        return totalMinutes;
    }

    private static LocationSetDTO getRoute(RideRecDTO ride) {
        Set<LocationSetDTO> setDTOS = ride.getLocations();
        Object[] locationSetDTOList = setDTOS.toArray();
        return (LocationSetDTO)locationSetDTOList[0];
    }


    private DurationDistance getDurationDistance(double startLat, double startLng, double endLat, double endLng) {

        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("http://router.project-osrm.org/route/v1/driving/%s,%s;%s,%s", startLng, startLat, endLng, endLat);
        OsrmResponse response = restTemplate.getForObject(url, OsrmResponse.class);

        double duration = response.getRoutes().get(0).getDuration();
        double distance = response.getRoutes().get(0).getDistance();

        return new DurationDistance(duration, distance);
    }

    private static Date atStartOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    private static Date atEndOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }

    private static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private Set<LocationsForRide> getLocationsByRideId(Integer id) {
        return locationsForRideService.getByRideId(id);
    }

    @Scheduled(fixedDelay = 60000)
    public void execute() {
        try {
            System.out.println("Finding driver for scheduled ride...");
            List<Ride> scheduledRidesLackingDriver = rideService.findScheduledRides(15);
            for (Ride ride : scheduledRidesLackingDriver) {
                System.out.println(ride.toString2());
                DriverTime driverTime = generateDriver(new RideRecDTO(ride, getLocationsByRideId(ride.getId())));
                ride.setDriver(driverTime.getDriver());
                ride.setEstimatedTimeInMinutes(driverTime.getTime());
                ride = rideService.updateRide(ride);
                System.out.println(ride.toString2());
                Integer id = ride.getDriver().getId();
                this.simpMessagingTemplate.convertAndSend("/socket-topic/driver/" + id, new RideRetDTO(ride, getLocationsByRideId(ride.getId())));
            }
            if (scheduledRidesLackingDriver.isEmpty()) {
                System.out.println("All scheduled ride have drivers");
            }
        } catch (DriverNotFoundException e) {
            System.out.println("Unable to find driver!");
        }
    }



    // --------------------------- ANDROID -------------------------------//
//    @PreAuthorize("hasRole('PASSENGER')")
//    @PostMapping("android")
//    public ResponseEntity<?> createARideAndroid(Principal principal, @RequestBody RideAndroidDTO oldDTO){
//        User user = userService.findUserByEmail(principal.getName());
//        boolean found = false;
//        for (PassengerEmailDTO p:oldDTO.getPassengers()) {
//            if (p.getEmail().equals(user.getEmail())){
//                found = true;
//                break;
//            }
//        }
//        if (!found) {
//            return new ResponseEntity<>("Cannot make ride for other people!",HttpStatus.NOT_FOUND);
//        }
//
//        Ride ride = fromDTOtoRide(oldDTO);
//
//        boolean canMakeRide;
//        for (PassengerEmailDTO ped: oldDTO.getPassengers()) {
//            Passenger pa = passengerService.findByEmail(ped.getEmail());
//            canMakeRide = rideService.checkRide(pa.getId());
//            if (!canMakeRide) {
//                return new ResponseEntity<>(new ErrorMessage("Cannot create a ride while you have one already pending!"), HttpStatus.BAD_REQUEST);
//            }
//        }
//        ride = rideService.createRide(ride);
//        for (PassengerEmailDTO ped: oldDTO.getPassengers()) {
//            Passenger p = passengerService.findByEmail(ped.getEmail());
//            Set<Ride> pr = p.getRides();
//            pr.add(ride);
//            p.setRides(new HashSet<>(pr));
//            passengerService.update(p);
//        }
//        return new ResponseEntity<>(new RideRetDTO(ride), HttpStatus.OK);
//    }

    private Ride fromDTOtoRide(RideAndroidDTO dto) throws UserNotFoundException {
        Date startTime = new Date();
        Date endTime = new Date();
        endTime.setYear(2025);
        long totalCost = 5000;
        List<Driver> drivers = driverService.findAllDrivers();
        if (drivers.size() == 0) throw new UserNotFoundException("No available driver");
        Driver driver = drivers.get(0);
        Set<PassengerEmailDTO> passengersDTOs = dto.getPassengers();
        HashSet<Passenger> passengers = new HashSet<>();
        for (PassengerEmailDTO passDTO: passengersDTOs) {
            passengers.add(passengerService.findByEmail(passDTO.getEmail()));
        }
        int estimatedTimeInMinutes = 4;
        Set<LocationSetDTO> locationsDTO = dto.getLocations();
        ArrayList<LocationSetDTO> locationSetDTOArrayList = new ArrayList<>();
        for (LocationSetDTO locDTO:locationsDTO) {
            locationSetDTOArrayList.add(locDTO);
        }
        HashSet<Location> locations = new HashSet<>();
        Status status = Status.PENDING;
        HashSet<Review> reviews = new HashSet<>();
        Review review = new Review();
        review = reviewService.addReview(review);
        reviews.add(review);
        Panic panic = new Panic();
        panic = panicService.addPanic(panic);
        Rejection newRejection = new Rejection();
        newRejection.setTime(new Date());
        newRejection.setReason("lalala");
        rejectionService.addRejection(newRejection);
        Instant instant = Instant.parse(dto.getScheduledTime());
        Date date = Date.from(instant);
        return new Ride(startTime, endTime, totalCost, driver, passengers, estimatedTimeInMinutes, dto.getVehicleType(),
                dto.isBabyTransport(), dto.isPetTransport(), newRejection, /*locations,*/ status, reviews, panic, date);
    }
}
