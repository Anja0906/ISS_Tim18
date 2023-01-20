package org.tim_18.UberApp.dto;

import lombok.Data;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerIdEmailDTO;
import org.tim_18.UberApp.model.Review;

@Data
public class VehicleReviewDTO {
    private Integer id;
    private Integer rating;
    private String comment;
    private PassengerIdEmailDTO passenger;

    public VehicleReviewDTO(){}
    public VehicleReviewDTO(Integer id, Integer rating, String comment, PassengerIdEmailDTO passenger) {
        this.id         = id;
        this.rating     = rating;
        this.comment    = comment;
        this.passenger  = passenger;
    }
    public VehicleReviewDTO(Review review, PassengerIdEmailDTO passenger){
        this(review.getId(),review.getRating(),review.getComment(), passenger);
    }
}