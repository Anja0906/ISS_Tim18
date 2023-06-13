package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.tim_18.UberApp.dto.Distance.OsrmResponse;
import org.tim_18.UberApp.dto.rideDTOs.RideRecDTO;
import org.tim_18.UberApp.exception.BadRequestException;
import org.tim_18.UberApp.exception.RideNotFoundException;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.repository.RideRepository;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RideService {

    @Autowired
    private final RideRepository rideRepository;



    public RideService(RideRepository rideRepository) {this.rideRepository = rideRepository;}

    public Page<Ride> findRidesByPassengersId(Integer id, String from, String to, Pageable pageable) {
        return rideRepository.findRidesByPassengersId(id, from, to, pageable);
    }

    public Page<Ride> findPendingRidesByStatus(String status,Pageable pageable) {
        return rideRepository.findPendingRidesByStatus(status,pageable);
    }

    public Ride createRide(Ride ride){
        return rideRepository.save(ride);
    }

    public Ride getDriverActiveRide(Integer driverId) {
        return rideRepository.findDriverActiveRide(driverId)
                .orElseThrow(() -> new RideNotFoundException("Ride was not found"));
    }
    public Optional<Ride> getActiveRideDriver(Integer driverId) {
        return rideRepository.findDriverActiveRide(driverId);
    }

    public List<Ride> getDriverAcceptedRides(Integer driverId) {
        return rideRepository.findDriverAcceptedRides(driverId, "ACCEPTED", "PENDING");

    }

    public Ride getPassengerActiveRide(Integer passengerId) {
        return rideRepository.findPassengerActiveRide(passengerId, "ACCEPTED")
                .orElseThrow(() -> new RideNotFoundException("Ride was not found"));
    }

    public Ride findRideById(Integer id) {
        return rideRepository.findRideById(id)
                .orElseThrow(() -> new RideNotFoundException("Ride was not found"));
    }

    public Ride updateRide(Ride ride) {
        return rideRepository.save(ride);
    }

    public Page<Ride> findRidesForDriver(Integer id, String start, String end, Pageable pageable){
        return rideRepository.findRidesForDriver(id,start,end,pageable);
    }

    public List<Ride> findRidesForDriver(Integer id, String start, String end, String sort){
        return rideRepository.findRidesForDriver(id,start,end, sort);
    }
    public List<Ride> findRidesForPassenger(Integer id, String start, String end, String sort){
        return rideRepository.findRidesForPassenger(id,start,end, sort);
    }
    public boolean checkRide(Integer passengerId) {
        List<Ride> pendingRides = rideRepository.findPassengersRidesByStatus(passengerId, "PENDING");
        return pendingRides.isEmpty();
    }
    public List<Ride> findRidesInDateRange(String start, String end, String sort) {
        return rideRepository.findRidesInDateRange(start,end, sort);
    }

    public List<Ride> findRidesByUser(Integer id, String from, String to, String sort) {
        return rideRepository.findRidesForUser(id, from, to, sort);
    }

    public List<Ride> findScheduledRides(double time, VehicleType vehicleType, boolean babyTransport, boolean petTransport) {
        return rideRepository.findScheduledRides(time, vehicleType.toString(), babyTransport, petTransport);
    }

    public List<Ride> findScheduledRides(double time) {
        return rideRepository.findScheduledRides(time);
    }

    public OsrmResponse getSteps (LocationsForRide locations) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("http://router.project-osrm.org/route/v1/driving/%s,%s;%s,%s?steps=true", locations.getDeparture().getLatitude(), locations.getDeparture().getLongitude(), locations.getDestination().getLatitude(), locations.getDestination().getLongitude());
        OsrmResponse response = restTemplate.getForObject(url, OsrmResponse.class);
        return response;
    }

    public void checkScheduledTime(RideRecDTO oldDTO) throws BadRequestException {
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
}