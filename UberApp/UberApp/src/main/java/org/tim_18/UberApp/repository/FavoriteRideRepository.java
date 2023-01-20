package org.tim_18.UberApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.tim_18.UberApp.model.FavoriteRide;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

public interface FavoriteRideRepository extends JpaRepository<FavoriteRide, Integer> {
    Optional<FavoriteRide> findById(Integer id);

    @Query(value = "SELECT * FROM favorite_rides r INNER JOIN passenger_favorite_rides pr on r.id=pr.favorite_rides_id WHERE pr.passengers_id = ?1", nativeQuery = true)
    List<FavoriteRide> findAllByPassenger(Integer id);


    @Modifying
    @Query(value="DELETE FROM passenger_favorite_rides pr WHERE pr.favorite_rides_id=?1", nativeQuery = true)
    void deleteFavoriteRideById(Integer id);

    Optional<FavoriteRide> findFavoriteRideById(Integer id);
}
