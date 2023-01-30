package org.tim_18.UberApp.service;

import org.springframework.stereotype.Service;
import org.tim_18.UberApp.model.FavoriteRide;
import org.tim_18.UberApp.model.LocationsForFavoriteRide;
import org.tim_18.UberApp.repository.LocationsForFavoriteRideRepository;

import java.util.Set;

@Service("locationsForFavoriteRidesService")
public class LocationsForFavoriteRideService {

    private final LocationsForFavoriteRideRepository repo;

    public LocationsForFavoriteRideService(LocationsForFavoriteRideRepository repo) {
        this.repo = repo;
    }

    public Set<LocationsForFavoriteRide> getByRideId(Integer id) {
        return repo.getLocationsForRideByRideId(id);
    }

    public LocationsForFavoriteRide addFavRide(LocationsForFavoriteRide ride) {
        return repo.save(ride);
    }
}
