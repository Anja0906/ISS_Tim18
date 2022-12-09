package org.tim_18.UberApp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.dto.RejectionDTO;
import org.tim_18.UberApp.dto.driverDTOs.DriverEmailDTO;
import org.tim_18.UberApp.dto.rideDTOs.RideDTOWithPanic;
import org.tim_18.UberApp.dto.rideDTOs.RideRecDTO;
import org.tim_18.UberApp.dto.rideDTOs.RideRetDTO;
import org.tim_18.UberApp.mapper.rideDTOmappers.RideRetDTOMapper;
import org.tim_18.UberApp.model.Driver;
import org.tim_18.UberApp.model.Rejection;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.model.Status;
import org.tim_18.UberApp.repository.RideRepository;

import java.util.Date;

@Service
public class RideService {

    @Autowired
    private final RideRepository rideRepository;
    @Autowired
    private final DriverService driverService;
    @Autowired
    private final RejectionService rejectionService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    RideRetDTOMapper dtoMapper;

    public RideService(RideRepository rideRepository, DriverService driverService, RejectionService rejectionService) {
        this.rideRepository = rideRepository;
        this.driverService = driverService;
        this.rejectionService = rejectionService;
    }

    public RideRetDTO createARide(RideRecDTO oldDTO){
        return null;
    }

    public RideRetDTO getDriverActiveRide(Integer driverId) {
        return null;
    }

    public RideRetDTO getPassengerActiveRide(Integer passengerId) {
        return null;
    }

    public Ride findRideById(Integer id) {
        return rideRepository.findRideById(id);
    }

    public RideRetDTO cancelRide(Integer id) {
        return null;
    }

    public RideDTOWithPanic activatePanic(Integer id, String reason){
        return null;
    }

    public RideRetDTO acceptRide(Integer id) {
        return null;
    }

    public RideRetDTO endRide(Integer id) {
        return null;
    }

    public RideRetDTO cancelRide(Integer id,  String reason){
        return null;
    }

    public RideRetDTO save(RideRecDTO oldDTO){
        RideRetDTO newDTO = new RideRetDTO();
//        newDTO.setLocations(oldDTO.getLocations());
        newDTO.setPassengers(oldDTO.getPassengers());
        newDTO.setVehicleType(oldDTO.getVehicleType());
        newDTO.setBabyTransport(oldDTO.isBabyTransport());
        newDTO.setPetTransport(oldDTO.isPetTransport());
        newDTO.setStartTime("2017-07-21T17:32:28Z");
        newDTO.setEndTime("2017-07-21T17:32:28Z");
        Driver driver = new Driver();
        driver.setEmail("newDriver@gmailc.com");
        driver.setPassword("123");
        driver = driverService.addDriver(driver);
        DriverEmailDTO driverDTO = new DriverEmailDTO(driver);
        newDTO.setDriver(driverDTO);
        newDTO.setEstimatedTimeInMinutes(5);
        Rejection newRejection = new Rejection();
        newRejection.setTime(new Date());
        newRejection.setReason("blablabla");
        newRejection = rejectionService.addRejection(newRejection);
        RejectionDTO rejectionDTO = new RejectionDTO(newRejection);
        newDTO.setRejection(rejectionDTO);
        newDTO.setStatus(Status.PENDING);
        Ride ride = dtoMapper.fromDTOtoRide(newDTO);
        ride = rideRepository.save(ride);
        return new RideRetDTO(ride);
    }
}
