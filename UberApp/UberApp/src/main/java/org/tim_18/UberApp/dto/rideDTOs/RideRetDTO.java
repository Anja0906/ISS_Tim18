package org.tim_18.UberApp.dto.rideDTOs;

import lombok.Data;
import org.tim_18.UberApp.dto.LocationsForRideDTO;
import org.tim_18.UberApp.dto.RejectionDTO;
import org.tim_18.UberApp.dto.driverDTOs.DriverEmailDTO;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerEmailDTO;
import org.tim_18.UberApp.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class RideRetDTO {
    private Integer id;
    private String startTime;
    private String endTime;
    private long totalCost;
    private DriverEmailDTO driver;
    private Set<PassengerEmailDTO> passengers;
    private int estimatedTimeInMinutes;
    private VehicleType vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
    private RejectionDTO rejection;
    private Set<LocationsForRideDTO> locations;
    private Status status;

    public RideRetDTO(){}

    public RideRetDTO(Integer id, String startTime, String endTime, long totalCost, Driver driver, Set<Passenger> passengers, int estimatedTimeInMinutes, VehicleType vehicleType, boolean babyTransport, boolean petTransport, Rejection rejection, Set<Location> locations, Status status) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalCost = totalCost;
        this.driver = new DriverEmailDTO(driver);
        Set<PassengerEmailDTO> passengerSet = new HashSet<>();
        for (Passenger p:passengers) {
            passengerSet.add(new PassengerEmailDTO(p));
        }
        this.passengers = passengerSet;
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
        this.rejection = new RejectionDTO(rejection);
        List<Location> locList = new ArrayList<>();
        for (Location loc : locations){
            locList.add(loc);
        }
        Set<LocationsForRideDTO> locationsForRideDTOSet = new HashSet<>();
        locationsForRideDTOSet.add(new LocationsForRideDTO(locList.get(0), locList.get(1)));
        this.status = status;
    }
    public RideRetDTO(Ride ride){
        this(ride.getId(), ride.getStartTime().toString(), ride.getEndTime().toString(),
                ride.getTotalCost(), ride.getDriver(), ride.getPassengers(),
                ride.getEstimatedTimeInMinutes(), ride.getVehicleType(), ride.isBabyTransport(),
                ride.isPetTransport(), ride.getRejection(), ride.getLocations(), ride.getStatus());

    }
}
