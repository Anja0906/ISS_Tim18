package org.tim_18.UberApp.dto.rideDTOs;

import lombok.Data;
import org.tim_18.UberApp.dto.rejectionDTO.RejectionDTO;
import org.tim_18.UberApp.dto.driverDTOs.DriverEmailDTO;
import org.tim_18.UberApp.dto.locationDTOs.LocationDTO;
import org.tim_18.UberApp.dto.locationDTOs.LocationSetDTO;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerIdEmailDTO;
import org.tim_18.UberApp.model.*;

import java.util.*;

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

    public RideRetDTO(Integer id, String startTime, String endTime, long totalCost, Driver driver, Set<Passenger> passengers, int estimatedTimeInMinutes, VehicleType vehicleType, boolean babyTransport, boolean petTransport, Rejection rejection, Set<LocationsForRide> _locations, Status status, Date scheduledTime) {
        this.id                             = id;
        this.startTime                      = startTime;
        this.endTime                        = endTime;
        this.totalCost                      = totalCost;
        if (driver == null) {
            this.driver =null;
        } else {
            this.driver = new DriverEmailDTO(driver);
        }
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
        Set<LocationSetDTO> locationSetDTOSet = new HashSet<>();
        for (LocationsForRide loc : _locations){
            LocationSetDTO locationSetDTO = new LocationSetDTO();
            locationSetDTO.setDeparture(new LocationDTO(loc.getDeparture()));
            locationSetDTO.setDestination(new LocationDTO(loc.getDestination()));
            locationSetDTOSet.add(locationSetDTO);
        }

        this.locations                      = locationSetDTOSet;
        this.status                         = status;
        if (scheduledTime==null) {
            this.scheduledTime              = "";
        }
        else {
            this.scheduledTime              = scheduledTime.toString();
        }
    }


    public RideRetDTO(Ride ride, Set<LocationsForRide> locations){
        this(ride.getId(), ride.getStartTime().toString(), ride.getEndTime().toString(),
                ride.getTotalCost(), ride.getDriver(),
                ride.getPassengers(), ride.getEstimatedTimeInMinutes(),
                ride.getVehicleType(), ride.isBabyTransport(),
                ride.isPetTransport(), ride.getRejection(),
                locations, ride.getStatus(), ride.getScheduledTime());

    }

//    public static List<RideRetDTO> getRidesDTO(Page<Ride> rides) {
//        List<RideRetDTO> ridesDTO = new ArrayList<>();
//        for (Ride r : rides) {
//            ridesDTO.add(new RideRetDTO(r));
//        }
//        return ridesDTO;
//    }
//
//    public HashSet<RideRetDTO> makeRideRideDTOS(Page<Ride> rides) {
//        HashSet<RideRetDTO> ridesDTO = new HashSet<>();
//        for (Ride r : rides) {
//            ridesDTO.add(new RideRetDTO(r));
//        }
//        return ridesDTO;
//    }
}
