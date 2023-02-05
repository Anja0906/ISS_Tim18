package org.tim_18.UberApp.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.tim_18.UberApp.dto.Distance.OsrmResponse;
import org.tim_18.UberApp.dto.locationDTOs.LocationSetDTO;
import org.tim_18.UberApp.exception.RideNotFoundException;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.model.VehicleType;
import org.tim_18.UberApp.repository.RideRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RideService {

    @Autowired
    private final RideRepository rideRepository;


    public RideService(RideRepository rideRepository) {this.rideRepository = rideRepository;}
    public List<Ride> findRidesByPassengersId(Integer id, String from, String to) {return rideRepository.findRidesByPassengersId(id, from, to);}
    public Page<Ride> findRidesByPassengersId(Integer id, String from, String to, Pageable pageable) {
        return rideRepository.findRidesByPassengersId(id, from, to, pageable);
    }
    public Page<Ride> findPendingRidesByStatus(String status,Pageable pageable) {
        return rideRepository.findPendingRidesByStatus(status,pageable);
    }
    public Ride createRide(Ride ride){return rideRepository.save(ride);}

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
    public Page<Ride> findRidesForUserPage(Integer id, Pageable pageable) {return rideRepository.findRidesForUserPage(id,pageable);}
    public Ride updateRide(Ride ride) {return rideRepository.save(ride);}

    public Page<Ride> findRidesForDriver(Integer id, String start, String end, Pageable pageable){return rideRepository.findRidesForDriver(id,start,end,pageable);}

    public List<Ride> findRidesForDriver(Integer id, String start, String end, String sort){return rideRepository.findRidesForDriver(id,start,end, sort);}
    public Page<Ride> findRidesForPassenger(Integer id, String start, String end, Pageable pageable){return rideRepository.findRidesForPassenger(id,start,end,pageable);}
    public List<Ride> findRidesForPassenger(Integer id, String start, String end, String sort){return rideRepository.findRidesForPassenger(id,start,end, sort);}
    public Page<Ride> findRidesForUser(Integer id, String start, String end, Pageable pageable){return rideRepository.findRidesForUser(id,start,end,pageable);}
    public boolean checkRide(Integer passengerId) {
        List<Ride> pendingRides = rideRepository.findPassengersRidesByStatus(passengerId, "PENDING");
        return pendingRides.isEmpty();
        // returns true if there's no pending rides => passenger can make a new ride
    }
    public Page<Ride> findAll(Pageable pageable) {
        return rideRepository.findAll(pageable);
    }
    public List<Ride> findAll() {return rideRepository.findAll();}
    public Page<Ride> findRidesInDateRange(String start, String end, Pageable pageable) {return rideRepository.findRidesInDateRange(start,end,pageable);}

    public List<Ride> findRidesInDateRange(String start, String end, String sort) {return rideRepository.findRidesInDateRange(start,end, sort);}

    public List<Ride> findRidesByUser(Integer id, String from, String to, String sort) {return rideRepository.findRidesForUser(id, from, to, sort);}
    public List<Ride> findRidesForDriverByStatus(Integer id, String status, String now) {
        return rideRepository.findRidesForDriverByStatus(id, status, now);
    }

    public List<Ride> findScheduledRides(double time, VehicleType vehicleType, boolean babyTransport, boolean petTransport) {
        return rideRepository.findScheduledRides(time, vehicleType.toString(), babyTransport, petTransport);
    }

    public List<Ride> findScheduledRides(double time) {
        return rideRepository.findScheduledRides(time);
    }

    public OsrmResponse getSteps (LocationSetDTO locationSetDTO) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("http://router.project-osrm.org/route/v1/driving/%s,%s;%s,%s?steps=true", locationSetDTO.getDeparture().getLatitude(), locationSetDTO.getDeparture().getLongitude(), locationSetDTO.getDestination().getLatitude(), locationSetDTO.getDestination().getLongitude());
        OsrmResponse response = restTemplate.getForObject(url, OsrmResponse.class);
        return response;
    }
}
