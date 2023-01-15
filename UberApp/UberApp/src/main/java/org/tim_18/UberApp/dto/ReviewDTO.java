package org.tim_18.UberApp.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.tim_18.UberApp.model.Passenger;
import org.tim_18.UberApp.model.Review;

import java.util.HashSet;

@Data
public class ReviewDTO {
    private Integer id;
    private Integer rating;
    private String comment;
    private PassengerIdEmailDTO passenger;

    public ReviewDTO(Integer id, Integer rating, String comment, PassengerIdEmailDTO passenger) {
        this.id         = id;
        this.rating     = rating;
        this.comment    = comment;
        this.passenger  = passenger;
    }

    public ReviewDTO(){}

    public ReviewDTO(Review review,PassengerIdEmailDTO passengerEmailDTO) {
        this(review.getId(),review.getRating(),review.getComment(),passengerEmailDTO);
    }

    public ReviewDTO(Review review) {
        this.id         = review.getId();
        this.rating     = review.getRating();
        this.comment    = review.getComment();
        this.passenger  = new PassengerIdEmailDTO(review.getPassenger());
    }

    public HashSet<ReviewDTO> makeReviewDTOS(Page<Review> reviews){
        HashSet<ReviewDTO> reviewDTOS = new HashSet<>();
        for (Review review:reviews) {
            reviewDTOS.add(new ReviewDTO(review));
        }
        return reviewDTOS;
    }
    @Override
    public String toString() {
        return "ReviewDTO{" +
                "id=" + id +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", passenger=" + passenger +
                '}';
    }
}
