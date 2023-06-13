package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.tim_18.UberApp.dto.Distance.DurationDistance;
import org.tim_18.UberApp.dto.Distance.OsrmResponse;
import org.tim_18.UberApp.dto.locationDTOs.LocationDTO;
import org.tim_18.UberApp.dto.locationDTOs.LocationSetDTO;
import org.tim_18.UberApp.dto.rideDTOs.RideRecDTO;
import org.tim_18.UberApp.exception.LocationNotFoundException;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.repository.LocationRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("locationService")
public class LocationService {

    @Autowired
    private final LocationRepository locationRepository;

    @Autowired
    private final LocationsForRideService locationsForRideService;

    @Autowired
    private final LocationsForFavoriteRideService locationsForFavoriteRideService;
    public LocationService(LocationRepository locationRepository, LocationsForRideService locationsForRideService,LocationsForFavoriteRideService locationsForFavoriteRideService) {
        this.locationRepository      = locationRepository;
        this.locationsForRideService = locationsForRideService;
        this.locationsForFavoriteRideService = locationsForFavoriteRideService;
    }
    public Location addLocation(Location location) {return locationRepository.save(location);}

    public List<Location> findAllLocations() {return locationRepository.findAll();}
    public Location updateLocation(Location location) {
        return locationRepository.save(location);
    }
    public Location findLocationById(Integer id) {
        return locationRepository.findLocationById(id)
                .orElseThrow(() -> new LocationNotFoundException("Location by id " + id + " was not found"));
    }
    public Location findLocationByAddressLongitudeLatitude(Double longitude,Double latitude,String address){
        Location location = locationRepository.findLocationByAdressLongitudeLatitude(longitude,latitude,address);
        if (location == null)
            location = addLocation(new Location(longitude, latitude, address));
        return location;
    }

    public Set<LocationsForFavoriteRide> addLocations(FavoriteRide ride, Set<LocationSetDTO> locs) {
        Set<LocationsForFavoriteRide> ridesLocations = new HashSet<>();
        for (LocationSetDTO locationSetDTO : locs) {
            LocationDTO departureDTO = locationSetDTO.getDeparture();
            Location departure = findLocationByAddressLongitudeLatitude(departureDTO.getLongitude(), departureDTO.getLatitude(), departureDTO.getAddress());
            updateLocation(departure);

            LocationDTO destinationDTO = locationSetDTO.getDestination();
            Location destination = findLocationByAddressLongitudeLatitude(destinationDTO.getLongitude(), destinationDTO.getLatitude(), destinationDTO.getAddress());


            updateLocation(destination);
            LocationsForFavoriteRide locationsForFavoriteRideo = new LocationsForFavoriteRide(departure, destination, ride);
            locationsForFavoriteRideService.addFavRide(locationsForFavoriteRideo);
            ridesLocations.add(locationsForFavoriteRideo);
        }
        return ridesLocations;
    }

    public Set<LocationsForRide> addLocations(RideRecDTO oldDTO, Ride ride) {
        Set<LocationsForRide> locationsForRides = new HashSet<>();
        for (LocationSetDTO locationSetDTO : oldDTO.getLocations()) {
            LocationDTO departureDTO = locationSetDTO.getDeparture();
            Location departure = findLocationByAddressLongitudeLatitude(locationSetDTO.getDeparture().getLongitude(),
                    locationSetDTO.getDeparture().getLatitude(), locationSetDTO.getDeparture().getAddress());
            updateLocation(departure);

            Location destination = findLocationByAddressLongitudeLatitude(locationSetDTO.getDestination().getLongitude(),
                    locationSetDTO.getDestination().getLatitude(), locationSetDTO.getDestination().getAddress());
            updateLocation(destination);

            DurationDistance durationDistance = getDurationDistance(departureDTO.getLatitude(), departure.getLongitude(), destination.getLatitude(), destination.getLongitude());
            LocationsForRide locationsForRide = new LocationsForRide(departure, destination, durationDistance.getDistance(), durationDistance.getDuration(), ride);
            locationsForRide = locationsForRideService.addLocationsForRide(locationsForRide);
            locationsForRides.add(locationsForRide);
        }
        return locationsForRides;
    }

    public DurationDistance getDurationDistance(double startLat, double startLng, double endLat, double endLng) {

        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("http://router.project-osrm.org/route/v1/driving/%s,%s;%s,%s", startLng, startLat, endLng, endLat);
        OsrmResponse response = restTemplate.getForObject(url, OsrmResponse.class);

        double duration = response.getRoutes().get(0).getDuration();
        double distance = response.getRoutes().get(0).getDistance();

        return new DurationDistance(duration, distance);
    }
    public void deleteLocation(Integer id) {
        locationRepository.deleteLocationById(id);
    }
}
