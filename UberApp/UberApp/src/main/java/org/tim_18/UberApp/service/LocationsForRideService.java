package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.exception.LocationForRideNotFoundException;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.model.LocationsForRide;
import org.tim_18.UberApp.repository.LocationsForRideRepository;

import java.util.List;

@Service("locationsForRidesService")
public class LocationsForRideService {
    @Autowired
    private final LocationsForRideRepository locationsForRideRepository;

    public LocationsForRideService(LocationsForRideRepository locationsForRideRepository) {
        this.locationsForRideRepository = locationsForRideRepository;
    }


    public LocationsForRide addLocationsForRide(LocationsForRide locationsForRide) {
        return locationsForRideRepository.save(locationsForRide);
    }

    public List<LocationsForRide> findAllLocationsForRide() {
        return locationsForRideRepository.findAll();
    }

    public LocationsForRide updateLocationsForRide(LocationsForRide locationsForRide) {
        return locationsForRideRepository.save(locationsForRide);
    }

    public LocationsForRide findLocationsForRideById(Integer id) {
        return locationsForRideRepository.findLocationsForRideById(id)
                .orElseThrow(() -> new LocationForRideNotFoundException("LocationsForRide by id " + id + " was not found"));
    }

    public void deleteLocationsForRide(Integer id) {
        locationsForRideRepository.deleteLocationsForRideById(id);
    }
}
