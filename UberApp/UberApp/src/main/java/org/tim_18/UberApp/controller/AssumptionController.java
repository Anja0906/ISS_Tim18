package org.tim_18.UberApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.tim_18.UberApp.dto.AssumptionDTO;
import org.tim_18.UberApp.dto.Distance.DurationDistance;
import org.tim_18.UberApp.dto.Distance.OsrmResponse;
import org.tim_18.UberApp.dto.locationDTOs.LocationDTO;
import org.tim_18.UberApp.dto.locationDTOs.LocationSetDTO;
import org.tim_18.UberApp.dto.rideDTOs.RideRecDTO;
import org.tim_18.UberApp.model.VehiclePrice;
import org.tim_18.UberApp.service.AssumptionService;
import org.tim_18.UberApp.service.VehiclePriceService;

import java.util.Set;

@RestController
@RequestMapping("/api/unregisteredUser/")
@CrossOrigin(origins = "http://localhost:4200")
public class AssumptionController {
    @Autowired
    private final AssumptionService assumptionService;
    private final VehiclePriceService vehiclePriceService;

    public AssumptionController(AssumptionService assumptionService, VehiclePriceService vehiclePriceService) {
        this.assumptionService = assumptionService;
        this.vehiclePriceService = vehiclePriceService;
    }

    @PostMapping("")
    public ResponseEntity<AssumptionDTO> rideAssumption (@RequestBody RideRecDTO ride) {
        Set<LocationSetDTO> setDTOS = ride.getLocations();
        LocationDTO departure = null, destination = null;
        for (LocationSetDTO loc : setDTOS) {
            departure = loc.getDeparture();
            destination = loc.getDestination();
        }
        DurationDistance depdis = getDurationDistance(departure.getLatitude(), departure.getLongitude(), destination.getLatitude(), destination.getLongitude());
        VehiclePrice vehicle = vehiclePriceService.findVehiclePriceByVehicleType(ride.getVehicleType());
        double vehiclePrice = vehicle.getPrice();
        AssumptionDTO assumptionDTO = new AssumptionDTO((int)depdis.getDuration()/60, (int) (vehiclePrice + 120*depdis.getDistance()/1000));
        return new ResponseEntity<>(assumptionDTO, HttpStatus.OK);
    }

    private DurationDistance getDurationDistance(double startLat, double startLng, double endLat, double endLng) {

        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("http://router.project-osrm.org/route/v1/driving/%s,%s;%s,%s", startLng, startLat, endLng, endLat);
        OsrmResponse response = restTemplate.getForObject(url, OsrmResponse.class);

        double duration = response.getRoutes().get(0).getDuration();
        double distance = response.getRoutes().get(0).getDistance();

        return new DurationDistance(duration, distance);
    }
}