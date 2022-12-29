package org.tim_18.UberApp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerDTOwithPassword;
import org.tim_18.UberApp.exception.PassengerNotFoundException;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.mapper.passengerDTOmappers.PassengerDTOwithPasswordMapper;
import org.tim_18.UberApp.model.Passenger;
import org.tim_18.UberApp.repository.PassengerRepository;

import java.util.List;

@Service("passengerService")
public class PassengerService {
    @Autowired
    private final PassengerRepository passengerRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    PassengerDTOwithPasswordMapper dtoMapper;

    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    public Passenger addPassenger(Passenger passenger) {
        return passengerRepository.save(passenger);
    }
    public Passenger update(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    public List<Passenger> findAll() {
        return passengerRepository.findAll();
    }
    public Page<Passenger> findAll(Pageable page){return passengerRepository.findAll(page);}


    public Passenger findById(Integer id) {
        return passengerRepository.findPassengerById(id)
                .orElseThrow(() -> new PassengerNotFoundException("Passenger by id " + id + " was not found"));
    }

    public PassengerDTOwithPassword save(PassengerDTOwithPassword dto) {
        Passenger passenger = dtoMapper.fromDTOtoPassenger(dto);
        passenger = passengerRepository.save(passenger);
        return dtoMapper.fromPassengerToDTO(passenger);
    }
}
