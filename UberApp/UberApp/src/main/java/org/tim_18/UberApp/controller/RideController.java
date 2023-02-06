package org.tim_18.UberApp.controller;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.tim_18.UberApp.Validation.ErrorMessage;
import org.tim_18.UberApp.dto.Distance.*;
import org.tim_18.UberApp.dto.PanicDTO;
import org.tim_18.UberApp.dto.PanicSocketDTO;
import org.tim_18.UberApp.dto.ReasonDTO;
import org.tim_18.UberApp.dto.locationDTOs.LocationDTO;
import org.tim_18.UberApp.dto.locationDTOs.LocationSetDTO;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerIdEmailDTO;
import org.tim_18.UberApp.dto.rideDTOs.*;
import org.tim_18.UberApp.exception.*;
import org.tim_18.UberApp.mapper.LocationDTOMapper;
import org.tim_18.UberApp.mapper.rideDTOmappers.FavoriteRideDTOMapper;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.service.*;

import java.security.Principal;
import java.time.Instant;
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
    private final VehicleService vehicleService;
    private final LocationService locationService;
    private final LocationsForRideService locationsForRideService;
    private final LocationsForFavoriteRideService locationsForFavoriteRideService;
    private final VehiclePriceService vehiclePriceService;
    private final RoleService roleService;
    private final FavoriteRideDTOMapper favoriteRideDTOMapper = new FavoriteRideDTOMapper(new ModelMapper());
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public RideController(SimpMessagingTemplate simpMessagingTemplate, RideService rideService, DriverService driverService, RejectionService rejectionService, ReviewService reviewService, PanicService panicService, PassengerService passengerService, UserService userService, FavoriteRideService favoriteRideService, VehicleService vehicleService, LocationService locationService, LocationsForRideService locationsForRideService, LocationsForFavoriteRideService locationsForFavoriteRideService, VehiclePriceService vehiclePriceService, RoleService roleService) {
        this.rideService                        = rideService;
        this.driverService                      = driverService;
        this.rejectionService                   = rejectionService;
        this.reviewService                      = reviewService;
        this.panicService                       = panicService;
        this.passengerService                   = passengerService;
        this.userService                        = userService;
        this.favoriteRideService                = favoriteRideService;
        this.vehicleService                     = vehicleService;
        this.locationService                    = locationService;
        this.locationsForRideService            = locationsForRideService;
        this.locationsForFavoriteRideService    = locationsForFavoriteRideService;
        this.vehiclePriceService                = vehiclePriceService;
        this.roleService                        = roleService;
        this.simpMessagingTemplate              = simpMessagingTemplate;
    }
    @PreAuthorize("hasRole('PASSENGER')")
    @PostMapping
    public ResponseEntity<?> createRide(Principal principal, @RequestBody RideRecDTO oldDTO) {
        try {
            checkPassengersForRide(principal, oldDTO);
            checkScheduledTime(oldDTO);
            Ride ride = fromDTOtoRide(oldDTO);
            ride = rideService.createRide(ride);
            Set<LocationsForRide> locationsForRides = addLocations(oldDTO, ride);
            addPassengers(oldDTO, ride);
            if (ride.getDriver()!=null)
                this.simpMessagingTemplate.convertAndSend("/socket-topic/driver-new-ride/" + ride.getDriver().getId(), new RideRetDTO(ride, locationsForRides));
            for (Passenger passenger : ride.getPassengers())
                this.simpMessagingTemplate.convertAndSend("/socket-topic/passenger-new-ride/" + passenger.getId(), new RideRetDTO(ride, locationsForRides));
            return new ResponseEntity<>(new RideRetDTO(ride, locationsForRides), HttpStatus.OK);
        } catch (PassengerNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (DriverNotFoundException e) {
            return new ResponseEntity<>("No driver found", HttpStatus.NOT_FOUND);
        }
    }



    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @GetMapping("/driver/{driverId}/active")
    public ResponseEntity<?> getDriverActiveRide(Principal principal, @PathVariable("driverId") Integer driverId) {
        try {
            User user = userService.findUserByEmail(principal.getName());
            if(user.getRoles().get(1).getName() =="ROLE_DRIVER")
                if (!user.getId().equals(driverId))
                    return new ResponseEntity<>("Active ride does not exist!",HttpStatus.NOT_FOUND);
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
            if (!user.getId().equals(passengerId))
                return new ResponseEntity<>("Active ride does not exist!",HttpStatus.NOT_FOUND);
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
                rejection.withdrawRide();
                rejectionService.updateRejection(rejection);
                if (ride.getDriver()!=null)
                    this.simpMessagingTemplate.convertAndSend("/socket-topic/withdraw/" + ride.getDriver().getId(), new RideRetDTO(ride, getLocationsByRideId(ride.getId())));
                return new ResponseEntity<>(new RideRetDTO(ride, getLocationsByRideId(ride.getId())), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorMessage("Cannot cancel a ride that is not in status PENDING or ACCEPTED!"), HttpStatus.BAD_REQUEST);
            }
        } catch(RideNotFoundException e){
            return new ResponseEntity<>("Ride does not exist!",HttpStatus.NOT_FOUND);
        }
    }
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    @PutMapping("/{id}/panic")
    public ResponseEntity<?> activatePanic(Principal principal, @PathVariable("id") Integer id, @RequestBody ReasonDTO reason){
        try {
            Ride ride = rideService.findRideById(id);
            checkRole(principal, ride);
            if (ride.getStatus() == Status.STARTED) {
                User user = userService.findUserByEmail(principal.getName());
                Panic panic = panicService.findById(ride.getPanic().getId());
                panic.updatePanic(user,reason,ride);
                panic = panicService.addPanic(panic);
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
            if (ride.getStatus() == Status.PENDING) {
                ride.setStatus(Status.ACCEPTED);
                ride = rideService.updateRide(ride);
                for (Passenger passenger : ride.getPassengers())
                    this.simpMessagingTemplate.convertAndSend("/socket-topic/accepted/" + passenger.getId(), new RideRetDTO(ride, getLocationsByRideId(ride.getId())));
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
            if (ride.getStatus() == Status.ACCEPTED) {
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
    @PreAuthorize("hasRole('PASSENGER')")
    @PutMapping("/{id}/route")
    public ResponseEntity<?> routeRide(Principal principal, @PathVariable("id")Integer id) {
        try {
            Ride ride = rideService.findRideById(id);
            checkPassengersAuthorities(principal, ride);
            if (ride.getStatus() == Status.STARTED) {
                Set<LocationsForRide> locations = this.getLocationsByRideId(ride.getId());
                Object[] locationSetDTOList = locations.toArray();
                LocationsForRide locationsForRide = (LocationsForRide) locationSetDTOList[locationSetDTOList.length - 1];
                OsrmResponse route = rideService.getSteps(locationsForRide);
                vehicleService.routeVehicle(route, ride);
                return new ResponseEntity<>(route, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorMessage("Cannot simulate a ride that is not in status STARTED!"), HttpStatus.BAD_REQUEST);
            }
        } catch(RideNotFoundException e){
            return new ResponseEntity<>("Ride does not exist!",HttpStatus.NOT_FOUND);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasRole('DRIVER')")
    @PutMapping("/{id}/end")
    public ResponseEntity<?> endRide(Principal principal, @PathVariable("id")Integer id) {
        try {
            Ride ride = rideService.findRideById(id);
            checkDriversAuthorities(principal, ride);
            if (ride.getStatus() == Status.STARTED) {
                ride.setStatus(Status.FINISHED);
                ride = rideService.updateRide(ride);
                for (Passenger passenger : ride.getPassengers())
                    this.simpMessagingTemplate.convertAndSend("/socket-topic/ended/" + passenger.getId(), new RideRetDTO(ride, getLocationsByRideId(ride.getId())));
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
                for (Passenger passenger : ride.getPassengers())
                    this.simpMessagingTemplate.convertAndSend("/socket-topic/cancelled/" + passenger.getId(), new RideRetDTO(ride, getLocationsByRideId(ride.getId())));
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
            Passenger passenger = passengerService.findById(user.getId());
            Set<PassengerIdEmailDTO> passengersDTOs = oldDTO.getPassengers();
            ride = handlePassengers(ride, user.getId(), passengersDTOs, passenger);
            ride = favoriteRideService.createFavRide(ride);
            updatePassenger(ride, passenger);
            Set<LocationsForFavoriteRide> ridesLocations = addLocations(ride, oldDTO.getLocations());
            Set<Passenger> passengerSet = new HashSet<>();
            passengerSet.add(passenger);
            ride.setPassengers(passengerSet);
            return new ResponseEntity<>(new FavoriteRideWithTimeDTO(ride, new Date(), ridesLocations), HttpStatus.OK);
        } catch (FavoriteRideNotFoundException e) {
            return new ResponseEntity<>("Favorite Ride does not exist",HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    private FavoriteRide handlePassengers(FavoriteRide ride, Integer userId, Set<PassengerIdEmailDTO> passengersDTOs, Passenger passenger) {
        Set<Passenger> passengerSet = ride.getPassengers();
        if (passengerSet.size() != 1)
            throw new BadRequestException("Cannot make favorite ride for other people!");
        for (Passenger pass : passengerSet)
            if (!pass.getId().equals(userId))
                throw new BadRequestException("Cannot make favorite ride for other people!");
        HashSet<Passenger> passengers = new HashSet<>();
        for (PassengerIdEmailDTO passDTO: passengersDTOs)
            passengers.add(passengerService.findById(passDTO.getId()));
        ride.setPassengers(passengers);
        if (passenger.getFavoriteRides().size() > 10)
            throw new BadRequestException("Number of favorite rides cannot exceed 10!");
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
            if (departure==null)
                departure = locationService.addLocation(new Location(departureDTO.getLongitude(), departureDTO.getLatitude(), departureDTO.getAddress()));
            locationService.updateLocation(departure);
            LocationDTO destinationDTO = locationSetDTO.getDestination();
            Location destination = locationService.findLocationByAddressLongitudeLatitude(destinationDTO.getLongitude(), destinationDTO.getLatitude(), destinationDTO.getAddress());
            if (destination==null)
                destination = locationService.addLocation(new Location(destinationDTO.getLongitude(), destinationDTO.getLatitude(), destinationDTO.getAddress()));
            locationService.updateLocation(destination);
            LocationsForFavoriteRide locationsForFavoriteRide = new LocationsForFavoriteRide(departure, destination, ride);
            locationsForFavoriteRideService.addFavRide(locationsForFavoriteRide);
            ridesLocations.add(locationsForFavoriteRide);
        }
        return ridesLocations;
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @GetMapping("/favorites")
    public ResponseEntity<?> findAllFavouriteRides(Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        Passenger passenger = passengerService.findById(user.getId());
        Set<Passenger> passengers = new HashSet<>();
        passengers.add(passenger);
        List<FavoriteRide> favoriteRides = favoriteRideService.findAllByPassenger(passenger.getId());
        for (FavoriteRide r : favoriteRides)
            r.setPassengers(passengers);
        HashSet<FavoriteRideWithTimeDTO> favoriteRidesDTO = getFavoriteRidesDTO(favoriteRides);
        return new ResponseEntity<>(favoriteRidesDTO, HttpStatus.OK);
    }

    private HashSet<FavoriteRideWithTimeDTO> getFavoriteRidesDTO(List<FavoriteRide> rides) {
        HashSet<FavoriteRideWithTimeDTO> ridesDTO = new HashSet<>();
        for (FavoriteRide ride : rides){
            Set<LocationsForFavoriteRide> locationsForRides = locationsForFavoriteRideService.getByRideId(ride.getId());
            ridesDTO.add(new FavoriteRideWithTimeDTO(ride, locationsForRides));
        }
        return ridesDTO;
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @DeleteMapping("/favorites/{id}")
    public ResponseEntity deleteFavRideById (Principal principal, @PathVariable("id") int id) {
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
        Set<Passenger> passengers = ride.getPassengers();
        for (Passenger p : passengers)
            if (p.getId().equals(user.getId()))
                return;
        throw new RideNotFoundException("Active ride does not exist!");
    }

    private void checkPassengersAuthorities(Principal principal, FavoriteRide ride) throws RideNotFoundException{
        User user = userService.findUserByEmail(principal.getName());
        Set<Passenger> passengers = ride.getPassengers();
        for (Passenger p : passengers)
            if (p.getId().equals(user.getId()))
                return;
        throw new FavoriteRideNotFoundException("Favorite Ride does not exist");
    }
    private void checkDriversAuthorities(Principal principal, Ride ride) throws RideNotFoundException{
        User user = userService.findUserByEmail(principal.getName());
        if (!ride.getDriver().getId().equals(user.getId()))
            throw new RideNotFoundException("Active ride does not exist!");
    }
    private void checkRole(Principal principal, Ride ride) {
        User user = userService.findUserByEmail(principal.getName());
        if (user.getRoles().contains(roleService.findById(2))) {
            checkPassengersAuthorities(principal, ride);
        } else {
            checkDriversAuthorities(principal, ride);
        }
    }

    private Ride fromDTOtoRide(RideRecDTO dto) throws DriverNotFoundException {
        Date date = null; // scheduled
        DriverTime driverTime;
        Driver driver = null;
        int time = -1;
        if (dto.getScheduledTime() != null) {
            date = Date.from(Instant.parse(dto.getScheduledTime()));
        }else{
            driverTime = generateDriver(dto);
            driver = driverTime.getDriver();
            time = driverTime.getTime();
        }
        Date startTime = new Date();
        Calendar date1 = Calendar.getInstance();
        date1.setTime(startTime);
        long timeInSecs = date1.getTimeInMillis();
        if (date != null) {
            startTime = date;
        } else {
            startTime = new Date(timeInSecs + ((long) time * 60 * 1000));
            date = startTime;
        }
        date1.setTime(startTime);
        timeInSecs = date1.getTimeInMillis();
        Set<LocationSetDTO> locationSetDTOSet = dto.getLocations();
        DurationDistance durationAndDistance;
        double totalDistance = 0;
        double totalDuration = 0;
        for (LocationSetDTO loc : locationSetDTOSet) {
            durationAndDistance= getDurationDistance(loc.getDeparture().getLatitude(), loc.getDeparture().getLongitude(),
                    loc.getDestination().getLatitude(), loc.getDestination().getLongitude());
            totalDistance += durationAndDistance.getDistance();
            totalDuration += durationAndDistance.getDuration()/60;
        }
        Date endDate = new Date((long) (timeInSecs + (totalDuration * 60 * 1000)));
        VehiclePrice vehicle = vehiclePriceService.findVehiclePriceByVehicleType(dto.getVehicleType());
        long totalCost = (long)(vehicle.getPrice() + 120*totalDistance/1000);
        HashSet<Passenger> passengers = new HashSet<>();
        for (PassengerIdEmailDTO passDTO: dto.getPassengers())
            passengers.add(passengerService.findByEmail(passDTO.getEmail()));
        HashSet<Review> reviews = new HashSet<>();
        Review review = reviewService.addReview(new Review());
        reviews.add(review);
        Panic panic = panicService.addPanic(new Panic());
        Rejection newRejection = new Rejection();
        newRejection.withdrawRide();
        rejectionService.addRejection(newRejection);
        return new Ride(startTime, endDate, totalCost, driver, passengers, time, dto.getVehicleType(),
                dto.isBabyTransport(), dto.isPetTransport(), newRejection, Status.PENDING, reviews, panic, date);
    }
    private DriverTime generateDriver(RideRecDTO ride) throws DriverNotFoundException {
        List<Driver> allDrivers = driverService.findAllDrivers();
        if (allDrivers.isEmpty())
            throw new DriverNotFoundException("");
        List<Driver> activeDrivers = new ArrayList<>();
        for (Driver driver : allDrivers)
            if (driver.isActive() && driver.getIsOnline())
                activeDrivers.add(driver);
        List<Driver> potentialDriversCarEdition = new ArrayList<>();
        for (Driver driver : activeDrivers) {
            Vehicle vehicle = driver.getVehicle();
            if (!(vehicle.getVehicleType().equals(ride.getVehicleType())))
                continue;
            if (vehicle.getPassengerSeats() < ride.getPassengers().size())
                continue;
            if (ride.isBabyTransport() && !vehicle.getBabyTransport())
                continue;
            if (ride.isPetTransport() && !vehicle.getPetTransport())
                continue;
            potentialDriversCarEdition.add(driver);
        }
        if (potentialDriversCarEdition.isEmpty())
            throw new DriverNotFoundException("");
        LocationSetDTO setDTO = getRoute(ride);
        LocationDTO departure = setDTO.getDeparture();
        LocationDTO destination = setDTO.getDestination();
        DurationDistance depdis = getDurationDistance(departure.getLatitude(), departure.getLongitude(), destination.getLatitude(), destination.getLongitude());
        HashMap<Driver, Integer> durations = new HashMap<>();
        boolean isScheduled = !(ride.getScheduledTime()==null);
        List<Ride> scheduledRides;
        boolean scheduledRideExist;
        if (!isScheduled) {
            scheduledRides = rideService.findScheduledRides(depdis.getDuration() / 60 + 15, ride.getVehicleType(), ride.isBabyTransport(), ride.isPetTransport());
            scheduledRideExist = !scheduledRides.isEmpty();
            if (scheduledRideExist && potentialDriversCarEdition.size() == 1)
                throw new DriverNotFoundException("");
        }
        for (Driver driver : potentialDriversCarEdition) {
            List<Ride> acceptedRides = rideService.getDriverAcceptedRides(driver.getId());
            Optional<Ride> currentRideOpt = rideService.getActiveRideDriver(driver.getId());
            Vehicle vehicle = driver.getVehicle();
            Location driversCurrentLocation = vehicle.getCurrentLocation();
            boolean acceptedRidesEmpty = acceptedRides.isEmpty();
            int totalMinutes = 0;
            if (driversCurrentLocation == null || departure == null)
                throw new DriverNotFoundException("");
            if (!isScheduled) {
                if (currentRideOpt.isEmpty() && acceptedRidesEmpty) {
                    depdis = getDurationDistance(driversCurrentLocation.getLatitude(), driversCurrentLocation.getLongitude(), departure.getLatitude(), departure.getLongitude());
                    totalMinutes += (int) depdis.getDuration() / 60;
                }
                else if (currentRideOpt.isPresent() && acceptedRidesEmpty) {
                    totalMinutes = getTotalMinutesForCurrentRide(departure, currentRideOpt, driversCurrentLocation, totalMinutes);
                }
                else if (currentRideOpt.isEmpty() && !acceptedRidesEmpty) {
                    totalMinutes = getTotalMinutesForAcceptedRides(departure, acceptedRides, totalMinutes, driversCurrentLocation);
                }
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
        if (durations.isEmpty())
            throw new DriverNotFoundException("");
        Driver bestDriver = null;
        int minutes = 2147483645;
        for (Map.Entry<Driver, Integer> map : durations.entrySet())
            if (map.getValue() < minutes) {
                bestDriver = map.getKey();
                minutes = map.getValue();
            }
        if (bestDriver==null)
            throw new DriverNotFoundException("");
        return new DriverTime(bestDriver, minutes);
    }

    private void checkPassengersForRide(Principal principal, RideRecDTO oldDTO){
        User user = userService.findUserByEmail(principal.getName());
        boolean found = false;
        Passenger passenger;
        boolean canMakeRide;
        for (PassengerIdEmailDTO p : oldDTO.getPassengers()) {
            try {
                passenger = passengerService.findByEmail(p.getEmail());
                canMakeRide = rideService.checkRide(passenger.getId());
                if (!canMakeRide)
                    throw new BadRequestException("Cannot create a ride while you have one already pending!");
            } catch (PassengerNotFoundException e) {
                throw new PassengerNotFoundException("Passenger not found!");
            }if (passenger.isBlocked())
                throw new PassengerNotFoundException("Passenger with id " + passenger.getId() + " is blocked and cannot order ride!");
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
        if (oldDTO.getScheduledTime()==null)
            return;
        Date scheduledTimeDate = Date.from(Instant.parse(oldDTO.getScheduledTime()));
        long diff = scheduledTimeDate.getTime() - new Date().getTime();
        if (diff <= 0)
            throw new BadRequestException("Scheduled time should be in the future!");
        long diffHours = diff / (60 * 60 * 1000) % 24;
        if (diffHours > 5)
            throw new BadRequestException("You can schedule only in next 5 hours!");
    }

    private Set<LocationsForRide> addLocations(RideRecDTO oldDTO, Ride ride) {
        Set<LocationsForRide> locationsForRides = new HashSet<>();
        for (LocationSetDTO locationSetDTO : oldDTO.getLocations()) {
            LocationDTO location = locationSetDTO.getDeparture();
            Location departure = locationService.findLocationByAddressLongitudeLatitude(location.getLongitude(), location.getLatitude(), location.getAddress());
            if (departure == null)
                departure = locationService.addLocation(new Location(location.getLongitude(), location.getLatitude(), location.getAddress()));
            locationService.updateLocation(departure);
            location = locationSetDTO.getDestination();
            Location destination = locationService.findLocationByAddressLongitudeLatitude(location.getLongitude(), location.getLatitude(), location.getAddress());
            if (destination == null)
                destination = locationService.addLocation(new Location(location.getLongitude(), location.getLatitude(), location.getAddress()));
            locationService.updateLocation(destination);
            DurationDistance durationDistance = getDurationDistance(departure.getLatitude(), departure.getLongitude(), destination.getLatitude(), destination.getLongitude());
            LocationsForRide locationsForRide = new LocationsForRide(departure, destination, durationDistance.getDistance(), durationDistance.getDuration(), ride);
            locationsForRide = locationsForRideService.addLocationsForRide(locationsForRide);
            locationsForRides.add(locationsForRide);
        }
        return locationsForRides;
    }

    private void addPassengers(RideRecDTO oldDTO, Ride ride) {
        for (PassengerIdEmailDTO passenger : oldDTO.getPassengers()) {
            Passenger p = passengerService.findByEmail(passenger.getEmail());
            Set<Ride> pr = p.getRides();
            pr.add(ride);
            p.setRides(new HashSet<>(pr));
            passengerService.update(p);
        }
    }

    private int getTotalMinutesForCurrentRide(LocationDTO departure, Optional<Ride> currentRideOpt, Location driversCurrentLocation, int totalMinutes) {
        Set<LocationsForRide> locations = getLocationsByRideId(currentRideOpt.get().getId());
        Object[] locationSetDTOList = locations.toArray();
        LocationsForRide locationsForRide = (LocationsForRide)locationSetDTOList[locationSetDTOList.length - 1];
        Location currentDestination = locationsForRide.getDestination();
        DurationDistance depdis = getDurationDistance(driversCurrentLocation.getLatitude(), driversCurrentLocation.getLongitude(), currentDestination.getLatitude(), currentDestination.getLongitude());
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
        return new DurationDistance(response.getRoutes().get(0).getDuration(), response.getRoutes().get(0).getDistance());
    }
    private Set<LocationsForRide> getLocationsByRideId(Integer id) {
        return locationsForRideService.getByRideId(id);
    }
    @Scheduled(fixedDelay = 60000)
    public void execute() {
        try {
            List<Ride> scheduledRidesLackingDriver = rideService.findScheduledRides(15);
            for (Ride ride : scheduledRidesLackingDriver) {
                DriverTime driverTime = generateDriver(new RideRecDTO(ride, getLocationsByRideId(ride.getId())));
                ride.updateRide(driverTime.getDriver(),driverTime.getTime());
                ride = rideService.updateRide(ride);
                this.simpMessagingTemplate.convertAndSend("/socket-topic/driver-new-ride/" + ride.getDriver().getId(), new RideRetDTO(ride, getLocationsByRideId(ride.getId())));
            }
        } catch (DriverNotFoundException e) {
            System.out.println("Unable to find driver!");
        }
    }
}