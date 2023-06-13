package org.tim_18.UberApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.exception.FavoriteRideNotFoundException;
import org.tim_18.UberApp.model.FavoriteRide;
import org.tim_18.UberApp.repository.FavoriteRideRepository;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
public class FavoriteRideService {

    @Autowired
    private final FavoriteRideRepository favoriteRideRepository;

    public FavoriteRideService(FavoriteRideRepository favoriteRideRepository) {this.favoriteRideRepository = favoriteRideRepository;}
    public List<FavoriteRide> findAll() {return this.favoriteRideRepository.findAll();}
    public FavoriteRide createFavRide(FavoriteRide ride){return this.favoriteRideRepository.save(ride);}
    public List<FavoriteRide> findAllByPassenger(Integer passengerId) {return this.favoriteRideRepository.findAllByPassenger(passengerId);}
    public void deleteFavRide(Integer id) {this.favoriteRideRepository.deleteFavoriteRideById(id);}
    public FavoriteRide findById(Integer id) {
        return this.favoriteRideRepository.findFavoriteRideById(id)
                .orElseThrow(() -> new FavoriteRideNotFoundException("Favorite Ride by id " + id + " was not found"));
    }
    public FavoriteRide updateRide(FavoriteRide favRide) {return this.favoriteRideRepository.save(favRide);}
}
