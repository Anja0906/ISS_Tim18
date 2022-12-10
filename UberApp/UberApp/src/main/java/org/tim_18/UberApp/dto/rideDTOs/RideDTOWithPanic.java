package org.tim_18.UberApp.dto.rideDTOs;

import lombok.Data;
import org.tim_18.UberApp.dto.RejectionDTO;
import org.tim_18.UberApp.dto.driverDTOs.DriverEmailDTO;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerEmailDTO;
import org.tim_18.UberApp.model.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class RideDTOWithPanic {

    private Integer id;
    private Date startTime;
    private Date endTime;
    private long totalCost;
    private DriverEmailDTO driver;
    private Set<PassengerEmailDTO> passengers;
    private int estimatedTimeInMinutes;
    private VehicleType vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
    private RejectionDTO rejection;
    private Set<Location> locations = new HashSet<Location>();
    private Status status;

    public RideDTOWithPanic(){}

    public RideDTOWithPanic(Integer id, Date startTime, Date endTime, long totalCost, Driver driver, Set<Passenger> passengers, int estimatedTimeInMinutes, VehicleType vehicleType, boolean babyTransport, boolean petTransport, Rejection rejection, Set<Location> locations, Status status) {
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
        this.locations = locations;
        this.status = status;
    }
    public RideDTOWithPanic(Ride ride){
        this(ride.getId(), ride.getStartTime(), ride.getEndTime(),
                ride.getTotalCost(), ride.getDriver(), ride.getPassengers(),
                ride.getEstimatedTimeInMinutes(), ride.getVehicleType(), ride.isBabyTransport(),
                ride.isPetTransport(), ride.getRejection(), ride.getLocations(), ride.getStatus());

    }

}
