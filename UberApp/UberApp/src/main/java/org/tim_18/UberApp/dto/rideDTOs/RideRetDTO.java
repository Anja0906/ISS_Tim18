package org.tim_18.UberApp.dto.rideDTOs;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.tim_18.UberApp.dto.RejectionDTO;
import org.tim_18.UberApp.dto.driverDTOs.DriverEmailDTO;
import org.tim_18.UberApp.dto.locationDTOs.LocationSetDTO;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerIdEmailDTO;
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
    private Set<PassengerIdEmailDTO> passengers;
    private int estimatedTimeInMinutes;
    private VehicleType vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
    private RejectionDTO rejection;
    private Set<LocationSetDTO> locations;
    private Status status;
    private String scheduledTime;

    public RideRetDTO(){}

    public RideRetDTO(Integer id, String startTime, String endTime, long totalCost, Driver driver, Set<Passenger> passengers, int estimatedTimeInMinutes, VehicleType vehicleType, boolean babyTransport, boolean petTransport, Rejection rejection, Set<Location> _locations, Status status, String scheduledTime) {
        this.id                             = id;
        this.startTime                      = startTime;
        this.endTime                        = endTime;
        this.totalCost                      = totalCost;
        this.driver                         = new DriverEmailDTO(driver);
        Set<PassengerIdEmailDTO> passengerSet = new HashSet<>();
        for (Passenger p:passengers) {
            passengerSet.add(new PassengerIdEmailDTO(p));
        }
        this.passengers                     = passengerSet;
        this.estimatedTimeInMinutes         = estimatedTimeInMinutes;
        this.vehicleType                    = vehicleType;
        this.babyTransport                  = babyTransport;
        this.petTransport                   = petTransport;
        if (rejection == null) {
            this.rejection                  = null;
        }else {
            this.rejection                  = new RejectionDTO(rejection);
        }
        List<Location> locList = new ArrayList<>();
        for (Location loc : _locations){
            locList.add(loc);
        }
        Set<LocationSetDTO> locationSetDTOSet = new HashSet<>();
        for (int i = 1; i < locList.size(); i++) {
            locationSetDTOSet.add(new LocationSetDTO(locList.get(i-1), locList.get(i)));
        }
        this.locations                      = locationSetDTOSet;
        this.status                         = status;
        this.scheduledTime                  = scheduledTime;
    }
    public RideRetDTO(Ride ride){
        this(ride.getId(), ride.getStartTime().toString(), ride.getEndTime().toString(),
                ride.getTotalCost(), ride.getDriver(),
                ride.getPassengers(), ride.getEstimatedTimeInMinutes(),
                ride.getVehicleType(), ride.isBabyTransport(),
                ride.isPetTransport(), ride.getRejection(),
                ride.getLocations(), ride.getStatus(), ride.getScheduledTime().toString());

    }

    public static List<RideRetDTO> getRidesDTO(Page<Ride> rides) {
        List<RideRetDTO> ridesDTO = new ArrayList<>();
        for (Ride r : rides) {
            ridesDTO.add(new RideRetDTO(r));
        }
        return ridesDTO;
    }

    public HashSet<RideRetDTO> makeRideRideDTOS(Page<Ride> rides) {
        HashSet<RideRetDTO> ridesDTO = new HashSet<>();
        for (Ride r : rides) {
            ridesDTO.add(new RideRetDTO(r));
        }
        return ridesDTO;
    }

    public HashSet<RideRetDTO> makeRideRideDTOS(List<Ride> rides) {
        HashSet<RideRetDTO> ridesDTO = new HashSet<>();
        for (Ride r : rides) {
            ridesDTO.add(new RideRetDTO(r));
        }
        return ridesDTO;
    }
}
