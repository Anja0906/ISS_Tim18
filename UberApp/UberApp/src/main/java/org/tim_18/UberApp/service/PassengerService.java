package org.tim_18.UberApp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerDTOwithPassword;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerIdEmailDTO;
import org.tim_18.UberApp.dto.rideDTOs.RideRecDTO;
import org.tim_18.UberApp.exception.BadRequestException;
import org.tim_18.UberApp.exception.PassengerNotFoundException;
import org.tim_18.UberApp.mapper.passengerDTOmappers.PassengerDTOwithPasswordMapper;
import org.tim_18.UberApp.model.FavoriteRide;
import org.tim_18.UberApp.model.Passenger;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.repository.PassengerRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("passengerService")
public class PassengerService {
    @Autowired
    private final PassengerRepository passengerRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    PassengerDTOwithPasswordMapper dtoMapper;

    public PassengerService(PassengerRepository passengerRepository) {this.passengerRepository = passengerRepository;}
    public Passenger addPassenger(Passenger passenger) {return passengerRepository.save(passenger);}
    public Passenger update(Passenger passenger) {return passengerRepository.save(passenger);}
    public List<Passenger> findAll() {return passengerRepository.findAll();}
    public Page<Passenger> findAllPassangersPage(Pageable page){return passengerRepository.findAll(page);}
    public Passenger findById(Integer id) {
        return passengerRepository.findPassengerById(id)
                .orElseThrow(() -> new PassengerNotFoundException("Passenger by id " + id + " was not found"));
    }
    public Passenger findByEmail(String email) {
        return passengerRepository.findPassengerByEmail(email)
                .orElseThrow(() -> new PassengerNotFoundException("Passenger by email " + email + " was not found"));
    }
    public void addPassengers(RideRecDTO oldDTO, Ride ride) {
        for (PassengerIdEmailDTO ped : oldDTO.getPassengers()) {
            Passenger p = findByEmail(ped.getEmail());
            Set<Ride> pr = p.getRides();
            pr.add(ride);
            p.setRides(new HashSet<>(pr));
            update(p);
        }
    }
    public void updatePassenger(FavoriteRide ride, Passenger passenger) {
        Set<FavoriteRide> passengerRides = passenger.getFavoriteRides();
        passengerRides.add(ride);
        passenger.setFavoriteRides(new HashSet<>(passengerRides));
        update(passenger);
    }
    public FavoriteRide handlePassengers(FavoriteRide ride, Integer userId, Set<PassengerIdEmailDTO> passengersDTOs, Passenger p) {
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
            passengers.add(findById(passDTO.getId()));
        }
        ride.setPassengers(passengers);

        Set<FavoriteRide> pFavRides = p.getFavoriteRides();
        if (pFavRides.size() > 10) {
            throw new BadRequestException("Number of favorite rides cannot exceed 10!");
        }
        return ride;
    }
}
