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
import org.tim_18.UberApp.Validation.ErrorMessage;
import org.tim_18.UberApp.dto.Distance.*;
import org.tim_18.UberApp.dto.locationDTOs.LocationDTO;
import org.tim_18.UberApp.dto.locationDTOs.LocationSetDTO;
import org.tim_18.UberApp.dto.panicDTOs.PanicDTO;
import org.tim_18.UberApp.dto.panicDTOs.PanicSocketDTO;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerIdEmailDTO;
import org.tim_18.UberApp.dto.rejectionDTO.ReasonDTO;
import org.tim_18.UberApp.dto.rideDTOs.*;
import org.tim_18.UberApp.exception.*;
import org.tim_18.UberApp.mapper.rideDTOmappers.FavoriteRideDTOMapper;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.service.*;
import java.security.Principal;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/ride")
@CrossOrigin(value = "http://localhost:4200")
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
    private FavoriteRideDTOMapper favoriteRideDTOMapper = new FavoriteRideDTOMapper(new ModelMapper());
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public RideController(SimpMessagingTemplate simpMessagingTemplate, RideService rideService, DriverService driverService, RejectionService rejectionService, ReviewService reviewService, PanicService panicService, PassengerService passengerService, UserService userService, FavoriteRideService favoriteRideService, VehicleService vehicleService, LocationService locationService, LocationsForRideService locationsForRideService, LocationsForFavoriteRideService locationsForFavoriteRideService, VehiclePriceService vehiclePriceService) {
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
        this.simpMessagingTemplate              = simpMessagingTemplate;
    }

    private void checkPassengersForRide(Principal principal, RideRecDTO oldDTO) throws PassengerNotFoundException, BadRequestException {
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
            }
            if (passenger.isBlocked())
                throw new PassengerNotFoundException("Passenger with id " + passenger.getId() + " is blocked and cannot order ride!");
            if (p.getEmail().equals(user.getEmail())) {
                found = true;
                break;
            }
        }
        if (!found)
            throw new PassengerNotFoundException("Cannot make ride for other people!");
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @PostMapping
    public ResponseEntity<?> createRide(Principal principal, @RequestBody RideRecDTO oldDTO) {
        try {
            checkPassengersForRide(principal, oldDTO);
            rideService.checkScheduledTime(oldDTO);
            Ride ride = fromDTOtoRide(oldDTO);
            ride = rideService.createRide(ride);
            Set<LocationsForRide> lfr = locationService.addLocations(oldDTO, ride);
            passengerService.addPassengers(oldDTO, ride);
            if (ride.getDriver()!=null)
                this.simpMessagingTemplate.convertAndSend("/socket-topic/driver-new-ride/" + ride.getDriver().getId(), new RideRetDTO(ride, lfr));

            for (Passenger passenger : ride.getPassengers())
                this.simpMessagingTemplate.convertAndSend("/socket-topic/passenger-new-ride/" + passenger.getId(), new RideRetDTO(ride, lfr));
            return new ResponseEntity<>(new RideRetDTO(ride, lfr), HttpStatus.OK);
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
            if(user.getRoles().get(1).getName().equals("ROLE_DRIVER"))
                if (!user.getId().equals(driverId))
                    return new ResponseEntity<>("Active ride does not exist!",HttpStatus.NOT_FOUND);
            Ride ride = rideService.getDriverActiveRide(driverId);
            return new ResponseEntity<>(new RideRetDTO(ride, locationsForRideService.getByRideId(ride.getId())), HttpStatus.OK);
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
            return new ResponseEntity<>(new RideRetDTO(ride, locationsForRideService.getByRideId(ride.getId())), HttpStatus.OK);
        } catch(RideNotFoundException e){
            return new ResponseEntity<>("Active ride does not exist!",HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> findRideById(Principal principal, @PathVariable("id") Integer id) {
        try {
            Ride ride = rideService.findRideById(id);
            userService.checkRole(principal, ride);
            return new ResponseEntity<>(new RideRetDTO(ride, locationsForRideService.getByRideId(id)), HttpStatus.OK);
        }catch (RideNotFoundException e) {
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
        HashSet<RideRetDTO> rideRetDTOS = new HashSet<>();
        for (Ride r : rides)
            rideRetDTOS.add(new RideRetDTO(r, locationsForRideService.getByRideId(r.getId())));
        map.put("totalCount",rideRetDTOS.size());
        map.put("results",rideRetDTOS);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @PutMapping("/{id}/withdraw")
    public ResponseEntity<?> withdrawRide(Principal principal, @PathVariable("id") Integer id) {
        try {
            Ride ride = rideService.findRideById(id);
            userService.checkPassengersAuthorities(principal, ride);

            if (ride.getStatus() == Status.PENDING || ride.getStatus() == Status.ACCEPTED) {
                ride.setStatus(Status.CANCELLED);
                Rejection rejection = ride.getRejection();
                rejection.withdrawRide();
                rejectionService.updateRejection(rejection);
                ride.setRejection(rejection);
                ride = rideService.updateRide(ride);
                RideRetDTO rideRetDTO = new RideRetDTO(ride, locationsForRideService.getByRideId(ride.getId()));
                if (ride.getDriver() != null)
                    this.simpMessagingTemplate.convertAndSend("/socket-topic/withdraw/" + ride.getDriver().getId(), rideRetDTO);
                return new ResponseEntity<>(rideRetDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorMessage("Cannot cancel a ride that is not in status PENDING or ACCEPTED!"), HttpStatus.BAD_REQUEST);
            }
        } catch (RideNotFoundException e) {
            return new ResponseEntity<>("Ride does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    @PutMapping("/{id}/panic")
    public ResponseEntity<?> activatePanic(Principal principal, @PathVariable("id") Integer id, @RequestBody ReasonDTO reason){
        try {
            Ride ride = rideService.findRideById(id);
            userService.checkRole(principal, ride);
            if (ride.getStatus() == Status.STARTED) {
                User user = userService.findUserByEmail(principal.getName());
                Panic panic = panicService.findById(ride.getPanic().getId());
                panic.updatePanic(user,reason,ride);
                panic = panicService.addPanic(panic);
                ride.setPanic(panic);
                rideService.updateRide(ride);
                this.simpMessagingTemplate.convertAndSend("/socket-topic/newPanic", new PanicSocketDTO(panic));
                return new ResponseEntity<>(new PanicDTO(panic, locationsForRideService.getByRideId(ride.getId())), HttpStatus.OK);
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
            userService.checkDriversAuthorities(principal, ride);
            if (ride.getStatus() == Status.PENDING) {
                ride.setStatus(Status.ACCEPTED);
                ride = rideService.updateRide(ride);
                for (Passenger passenger : ride.getPassengers()) 
                    this.simpMessagingTemplate.convertAndSend("/socket-topic/accepted/" + passenger.getId(), new RideRetDTO(ride, locationsForRideService.getByRideId(ride.getId())));
                return new ResponseEntity<>(new RideRetDTO(ride, locationsForRideService.getByRideId(ride.getId())), HttpStatus.OK);
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
            userService.checkPassengersAuthorities(principal, ride);
            if (ride.getStatus() == Status.ACCEPTED) {
                ride.setStatus(Status.STARTED);
                ride = rideService.updateRide(ride);
                this.simpMessagingTemplate.convertAndSend("/socket-topic/started/" + ride.getDriver().getId(), new RideRetDTO(ride, locationsForRideService.getByRideId(ride.getId())));
                return new ResponseEntity<>(new RideRetDTO(ride, locationsForRideService.getByRideId(ride.getId())), HttpStatus.OK);
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
            userService.checkPassengersAuthorities(principal, ride);
            if (ride.getStatus() == Status.STARTED) {
                Set<LocationsForRide> locations = locationsForRideService.getByRideId(ride.getId());
                LocationsForRide locationsForRide = (LocationsForRide) locations.toArray()[locations.toArray().length - 1];
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
            userService.checkDriversAuthorities(principal, ride);
            if (ride.getStatus() == Status.STARTED) {
                ride.setStatus(Status.FINISHED);
                ride = rideService.updateRide(ride);
                for (Passenger passenger : ride.getPassengers())
                    this.simpMessagingTemplate.convertAndSend("/socket-topic/ended/" + passenger.getId(), new RideRetDTO(ride, locationsForRideService.getByRideId(ride.getId())));
                return new ResponseEntity<>(new RideRetDTO(ride, locationsForRideService.getByRideId(ride.getId())), HttpStatus.OK);
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
            userService.checkDriversAuthorities(principal, ride);
            User user = userService.findUserById(ride.getDriver().getId());
            if (ride.getStatus() == Status.PENDING || ride.getStatus() == Status.ACCEPTED) {
                ride.setStatus(Status.REJECTED);
                Rejection rejection = new Rejection(ride, user, new Date(), reason.getReason());
                rejectionService.addRejection(rejection);
                ride.setRejection(rejection);
                ride = rideService.updateRide(ride);
                for (Passenger passenger : ride.getPassengers())
                    this.simpMessagingTemplate.convertAndSend("/socket-topic/cancelled/" + passenger.getId(), new RideRetDTO(ride, locationsForRideService.getByRideId(ride.getId())));
                return new ResponseEntity<>(new RideRetDTO(ride, locationsForRideService.getByRideId(ride.getId())), HttpStatus.OK);
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
            Passenger p = passengerService.findById(user.getId());
            Set<PassengerIdEmailDTO> passengersDTOs = oldDTO.getPassengers();
            ride = passengerService.handlePassengers(ride, user.getId(), passengersDTOs, p);
            ride = favoriteRideService.createFavRide(ride);
            passengerService.updatePassenger(ride, p);
            Set<LocationSetDTO> locs = oldDTO.getLocations();
            Set<LocationsForFavoriteRide> ridesLocations = locationService.addLocations(ride, locs);
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

    @PreAuthorize("hasRole('PASSENGER')")
    @GetMapping("/favorites")
    public ResponseEntity<?> findAllFavs(Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        Passenger passenger = passengerService.findById(user.getId());
        Set<Passenger> passengers = new HashSet<>();
        HashSet<FavoriteRideWithTimeDTO> favoriteRidesDTO = new HashSet<>();
        passengers.add(passenger);
        List<FavoriteRide> favoriteRides = favoriteRideService.findAllByPassenger(passenger.getId());
        for (FavoriteRide r : favoriteRides) {
            r.setPassengers(passengers);
            favoriteRidesDTO.add(new FavoriteRideWithTimeDTO(r, locationsForFavoriteRideService.getByRideId(r.getId())));
        }
        return new ResponseEntity<>(favoriteRidesDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @DeleteMapping("/favorites/{id}")
    public ResponseEntity deleteFavRideById (Principal principal,
                                             @PathVariable("id") int id) {
        try {
            FavoriteRide ride = favoriteRideService.findById(id);
            userService.checkPassengersAuthorities(principal, ride);
            Passenger passenger = passengerService.findById(userService.findUserByEmail(principal.getName()).getId());
            Set<Passenger> ps = ride.getPassengers();
            ps.remove(passenger);
            ride.setPassengers(ps);
            favoriteRideService.updateRide(ride);
            Set<FavoriteRide> fr = passenger.getFavoriteRides();
            fr.remove(ride);
            passenger.setFavoriteRides(fr);
            passengerService.update(passenger);
            return new ResponseEntity<>("Successful deletion of favorite location!", HttpStatus.NO_CONTENT);
        } catch (FavoriteRideNotFoundException e) {
            return new ResponseEntity<>("Favorite location does not exist!" ,HttpStatus.NOT_FOUND);
        }
    }
    private Ride fromDTOtoRide(RideRecDTO dto) throws DriverNotFoundException {
        Date startTime = getStartTime(dto);
        Date endTime = getEndTime(dto, startTime);
        long totalCost = getTotalCost(dto);
        HashSet<Passenger> passengers = new HashSet<>();
        HashSet<Review> reviews = new HashSet<>();

        for (PassengerIdEmailDTO passDTO: dto.getPassengers())
            passengers.add(passengerService.findByEmail(passDTO.getEmail()));
        Review review = reviewService.addReview(new Review());
        reviews.add(review);
        Panic panic = panicService.addPanic(new Panic());
        Rejection newRejection = new Rejection("",new Date());
        rejectionService.addRejection(newRejection);

        return new Ride(startTime, endTime, totalCost, getDriver(dto), passengers, getDriverTime(dto),
                dto.getVehicleType(), dto.isBabyTransport(), dto.isPetTransport(), newRejection,
                Status.PENDING, reviews, panic, startTime);
    }

    private Driver getDriver(RideRecDTO dto) {
        Date date = dto.getScheduledTime() != null ? Date.from(Instant.parse(dto.getScheduledTime())) : null;
        if (date == null) {
            DriverTime driverTime = generateDriver(dto);
            return driverTime.getDriver();
        }
        return null;
    }

    private int getDriverTime(RideRecDTO dto) {
        Date date = dto.getScheduledTime() != null ? Date.from(Instant.parse(dto.getScheduledTime())) : null;
        if (date == null) {
            DriverTime driverTime = generateDriver(dto);
            return driverTime.getTime();
        }
        return -1;
    }
    private Date getEndTime(RideRecDTO dto, Date startTime) {
        double totalDuration = 0;
        for (LocationSetDTO loc : dto.getLocations()) {
            DurationDistance depdis = locationService.getDurationDistance(loc.getDeparture().getLatitude(), loc.getDeparture().getLongitude(), loc.getDestination().getLatitude(), loc.getDestination().getLongitude());
            totalDuration += depdis.getDuration()/60;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        long timeInSecs = calendar.getTimeInMillis();
        return new Date((long) (timeInSecs + (totalDuration * 60 * 1000)));
    }

    private long getTotalCost(RideRecDTO dto) {
        double totalDistance = 0;
        for (LocationSetDTO loc : dto.getLocations()) {
            DurationDistance depdis = locationService.getDurationDistance(loc.getDeparture().getLatitude(), loc.getDeparture().getLongitude(), loc.getDestination().getLatitude(), loc.getDestination().getLongitude());
            totalDistance += depdis.getDistance();
        }
        VehiclePrice vehicle = vehiclePriceService.findVehiclePriceByVehicleType(dto.getVehicleType());
        return (long)( vehicle.getPrice() + 120*totalDistance/1000);
    }

    private Date getStartTime(RideRecDTO dto) {
        Date date = dto.getScheduledTime() != null ? Date.from(Instant.parse(dto.getScheduledTime())) : null;
        if (date != null)
            return date;
        DriverTime driverTime = generateDriver(dto);
        long timeInSecs = Calendar.getInstance().getTimeInMillis();
        return new Date(timeInSecs + ((long) driverTime.getTime() * 60 * 1000));
    }

    private DriverTime generateDriver(RideRecDTO ride) throws DriverNotFoundException {
        Driver bestDriver = null;
        int minutes = Integer.MAX_VALUE;
        ArrayList<Driver> activeDrivers = driverService.findDriverByStatus();


        ArrayList<Driver> potentialDriversCarEdition = driverService.filterDriversByVehicleSpec(activeDrivers,ride);
        for(Driver d:potentialDriversCarEdition){
            System.out.println(d.getId());
        }
        if (potentialDriversCarEdition.isEmpty())
            throw new DriverNotFoundException("Driver not found");

        LocationSetDTO setDTO = (LocationSetDTO)ride.getLocations().toArray()[0];
        DurationDistance durationDistance = locationService.getDurationDistance(setDTO.getDeparture().getLatitude(), setDTO.getDeparture().getLongitude(), setDTO.getDestination().getLatitude(), setDTO.getDestination().getLongitude());
        boolean isScheduled = !(ride.getScheduledTime()==null);

        if (!isScheduled) {
            List<Ride> scheduledRides = rideService.findScheduledRides(durationDistance.getDuration() / 60 + 15, ride.getVehicleType(), ride.isBabyTransport(), ride.isPetTransport());
            if (!scheduledRides.isEmpty() && potentialDriversCarEdition.size() == 1)
                throw new DriverNotFoundException("Driver not found");
        }
        HashMap<Driver, Integer> durations = findDrivers(potentialDriversCarEdition,setDTO,isScheduled,durationDistance);
        for (Map.Entry<Driver, Integer> map : durations.entrySet())
            if (map.getValue() < minutes) {
                bestDriver = map.getKey();
                minutes = map.getValue();
            }
        if (bestDriver==null)
            throw new DriverNotFoundException("Driver not found");
        return new DriverTime(bestDriver, minutes);
    }


    private HashMap<Driver,Integer> findDrivers(ArrayList<Driver> potentialDriversCarEdition,LocationSetDTO setDTO, boolean isScheduled, DurationDistance durationDistance){
        HashMap<Driver, Integer> durations = new HashMap<>();
        for (Driver driver : potentialDriversCarEdition) {
            List<Ride> acceptedRides = rideService.getDriverAcceptedRides(driver.getId());
            Optional<Ride> currentRideOpt = rideService.getActiveRideDriver(driver.getId());
            Location driversCurrentLocation = driver.getVehicle().getCurrentLocation();
            int totalMinutes = 0;

            if (driversCurrentLocation == null || setDTO.getDeparture() == null)
                throw new DriverNotFoundException("");

            if (!isScheduled) {
                if (currentRideOpt.isEmpty() && acceptedRides.isEmpty()) {
                    durationDistance = locationService.getDurationDistance(driversCurrentLocation.getLatitude(),
                            driversCurrentLocation.getLongitude(), setDTO.getDeparture().getLatitude(), setDTO.getDeparture().getLongitude());
                    totalMinutes += (int) durationDistance.getDuration() / 60;
                }
                else if (currentRideOpt.isPresent() && acceptedRides.isEmpty())
                    totalMinutes = getTotalMinutesForCurrentRide(setDTO.getDeparture(), currentRideOpt, driversCurrentLocation, totalMinutes);

                else if (currentRideOpt.isEmpty())
                    totalMinutes = getTotalMinutesForAcceptedRides(setDTO.getDeparture(), acceptedRides, totalMinutes, driversCurrentLocation);

                else {
                    Set<LocationsForRide> locations = locationsForRideService.getByRideId(currentRideOpt.get().getId());
                    LocationsForRide locationsForRide = (LocationsForRide) locations.toArray()[locations.toArray().length - 1];
                    durationDistance = locationService.getDurationDistance(driversCurrentLocation.getLatitude(), driversCurrentLocation.getLongitude(), locationsForRide.getDestination().getLatitude(), locationsForRide.getDestination().getLongitude());
                    totalMinutes += (int) durationDistance.getDuration() / 60;
                    totalMinutes += getTotalMinutesForAcceptedRides(setDTO.getDeparture(), acceptedRides, totalMinutes, locationsForRide.getDestination());
                }
            } else {
                durationDistance = locationService.getDurationDistance(driversCurrentLocation.getLatitude(), driversCurrentLocation.getLongitude(), setDTO.getDeparture().getLatitude(), setDTO.getDeparture().getLongitude());
                totalMinutes += (int) durationDistance.getDuration() / 60;
            }
            durations.put(driver, totalMinutes);
        }
        return durations;
    }

    private int getTotalMinutesForCurrentRide(LocationDTO departure, Optional<Ride> currentRideOpt, Location driversCurrentLocation, int totalMinutes) {
        DurationDistance depdis;
        Set<LocationsForRide> locations = locationsForRideService.getByRideId(currentRideOpt.get().getId());
        LocationsForRide locationsForRide = (LocationsForRide)locations.toArray()[locations.toArray().length - 1];
        Location currentDestination = locationsForRide.getDestination();
        depdis = locationService.getDurationDistance(driversCurrentLocation.getLatitude(), driversCurrentLocation.getLongitude(), currentDestination.getLatitude(), currentDestination.getLongitude());
        totalMinutes += (int)depdis.getDuration()/60;
        depdis = locationService.getDurationDistance(currentDestination.getLatitude(), currentDestination.getLongitude(), departure.getLatitude(), departure.getLongitude());
        totalMinutes += (int)depdis.getDuration()/60;
        return totalMinutes;
    }

    private int getTotalMinutesForAcceptedRides(LocationDTO departure, List<Ride> acceptedRides, int totalMinutes, Location driversCurrentLocation) {
        DurationDistance depdis;
        for (int i = 0; i < acceptedRides.size(); i++) {
            Set<LocationsForRide> locations = locationsForRideService.getByRideId(acceptedRides.get(i).getId());
            LocationsForRide locationsForRide = (LocationsForRide)locations.toArray()[locations.toArray().length - 1];
            Location acceptedDeparture = locationsForRide.getDeparture();
            Location acceptedDestination = locationsForRide.getDestination();
            if (i==0) {
                depdis = locationService.getDurationDistance(driversCurrentLocation.getLatitude(), driversCurrentLocation.getLongitude(), acceptedDeparture.getLatitude(), acceptedDeparture.getLongitude());
                totalMinutes += (int)depdis.getDuration()/60;
                depdis = locationService.getDurationDistance(acceptedDeparture.getLatitude(), acceptedDeparture.getLongitude(), acceptedDestination.getLatitude(), acceptedDestination.getLongitude());
                totalMinutes += (int)depdis.getDuration()/60;
            } else {
                Set<LocationsForRide> locationsPrevious = locationsForRideService.getByRideId(acceptedRides.get(i).getId());
                LocationsForRide locationsForRidePrevious = (LocationsForRide)locationsPrevious.toArray()[locationsPrevious.toArray().length - 1];
                depdis = locationService.getDurationDistance(locationsForRidePrevious.getDestination().getLatitude(), locationsForRidePrevious.getDestination().getLongitude(), acceptedDeparture.getLatitude(), acceptedDeparture.getLongitude());
                totalMinutes += (int)depdis.getDuration()/60;
                depdis = locationService.getDurationDistance(acceptedDeparture.getLatitude(), acceptedDeparture.getLongitude(), acceptedDestination.getLatitude(), acceptedDestination.getLongitude());
                totalMinutes += (int)depdis.getDuration()/60;
            }
            if (i <= acceptedRides.size() -1) {
                depdis = locationService.getDurationDistance(acceptedDestination.getLatitude(), acceptedDestination.getLongitude(), departure.getLatitude(), departure.getLongitude());
                totalMinutes += (int) depdis.getDuration() / 60;
            }
        }
        return totalMinutes;
    }
    @Scheduled(fixedDelay = 60000)
    public void execute() {
        try {
            System.out.println("Finding driver for scheduled ride...");
            List<Ride> scheduledRidesLackingDriver = rideService.findScheduledRides(15);
            for (Ride ride : scheduledRidesLackingDriver) {
                DriverTime driverTime = generateDriver(new RideRecDTO(ride, locationsForRideService.getByRideId(ride.getId())));
                ride.updateRide(driverTime.getDriver(),driverTime.getTime());
                ride = rideService.updateRide(ride);
                this.simpMessagingTemplate.convertAndSend("/socket-topic/driver-new-ride/" + ride.getDriver().getId(), new RideRetDTO(ride, locationsForRideService.getByRideId(ride.getId())));
            }
            if (scheduledRidesLackingDriver.isEmpty())
                System.out.println("All scheduled ride have drivers");
        } catch (DriverNotFoundException e) {
            System.out.println("Unable to find driver!");
        }
    }
}
