package org.tim_18.UberApp.dto;

import lombok.Data;
import org.tim_18.UberApp.model.Passenger;
import org.tim_18.UberApp.model.Review;
@Data
public class ReviewDTO {
    private Integer id;
    private Integer rating;
    private String comment;
    private PassengerEmailDTO passenger;

    public ReviewDTO(Integer id, Integer rating, String comment, PassengerEmailDTO passenger) {
        this.id         = id;
        this.rating     = rating;
        this.comment    = comment;
        this.passenger  = passenger;
    }

    public ReviewDTO(Review review,PassengerEmailDTO passengerEmailDTO) {
        this(review.getId(),review.getRating(),review.getComment(),passengerEmailDTO);
    }

    public ReviewDTO(Review review) {
        this.id         = review.getId();
        this.rating     = review.getRating();
        this.comment    = review.getComment();
        this.passenger  = new PassengerEmailDTO(1, "Anja@gmail.com");
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
