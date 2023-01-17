package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.exception.LocationNotFoundException;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.model.Location;
import org.tim_18.UberApp.repository.LocationRepository;

import java.util.List;

@Service("locationService")
public class LocationService {

    @Autowired
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location addLocation(Location location) {
        return locationRepository.save(location);
    }

    public List<Location> findAllLocations() {
        return locationRepository.findAll();
    }

    public Location updateLocation(Location location) {
        return locationRepository.save(location);
    }

    public Location findLocationById(Integer id) {
        return locationRepository.findLocationById(id)
                .orElseThrow(() -> new LocationNotFoundException("Location by id " + id + " was not found"));
    }
    public Location findLocationByAddressLongitudeLatitude(Double longitude,Double latitude,String address){
        return locationRepository.findLocationByAdressLongitudeLatitude(longitude,latitude,address);
    }

    public void deleteLocation(Integer id) {
        locationRepository.deleteLocationById(id);
    }
}
