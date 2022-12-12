package org.tim_18.UberApp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.tim_18.UberApp.model.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@Data
public class RideDTO {

    private Integer id;
    private Date startTime;
    private Date endTime;
    private long totalCost;

    private Driver driver;
    private Set<Passenger> passengers;
    private int estimatedTimeInMinutes;
    private VehicleType vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
    private Rejection rejection;
    private Panic panic;

    private Set<Location> locations = new HashSet<Location>();

    public RideDTO(Integer id,Date startTime, Date endTime, long totalCost, Driver driver, Set<Passenger> passengers, int estimatedTimeInMinutes, VehicleType vehicleType, boolean babyTransport, boolean petTransport, Rejection rejection, Set<Location> locations) {
        this.id                     = id;
        this.startTime              = startTime;
        this.endTime                = endTime;
        this.totalCost              = totalCost;
        this.driver                 = driver;
        this.passengers             = passengers;
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
        this.vehicleType            = vehicleType;
        this.babyTransport          = babyTransport;
        this.petTransport           = petTransport;
        this.rejection              = rejection;
        this.locations              = locations;
    }

    public RideDTO() {}
    public RideDTO(Ride ride) {
        this(ride.getId(), ride.getStartTime(),
                ride.getEndTime(), ride.getTotalCost(),
                ride.getDriver(), ride.getPassengers(),
                ride.getEstimatedTimeInMinutes(), ride.getVehicleType(),
                ride.isBabyTransport(), ride.isPetTransport(),
                ride.getRejection(), ride.getLocations());
    }
    public HashSet<RideDTO> makeRides(Page<Ride> rides){
        HashSet<RideDTO> rideDTOS = new HashSet<>();
        for(Ride ride:rides)
            rideDTOS.add(new RideDTO(ride));
        return rideDTOS;
    }
}