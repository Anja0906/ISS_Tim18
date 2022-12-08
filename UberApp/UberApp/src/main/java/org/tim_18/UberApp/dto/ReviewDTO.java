package org.tim_18.UberApp.dto;

import org.tim_18.UberApp.model.Passenger;
import org.tim_18.UberApp.model.Review;

public class ReviewDTO {
    //{
    //  "id": 123,
    //  "rating": 3,
    //  "comment": "The driver was driving really fast",
    //  "passenger": {
    //    "id": 123,
    //    "email": "user@example.com"
    //  }
    //}
    private Integer id;
    private Integer rating;
    private String comment;
    private PassengerEmailDTO passenger;

    public ReviewDTO(Integer id, Integer rating, String comment, PassengerEmailDTO passenger) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.passenger = passenger;
    }

    public ReviewDTO(Review review,PassengerEmailDTO passengerEmailDTO) {
        this(review.getId(),review.getRating(),review.getComment(),passengerEmailDTO);
    }

    public ReviewDTO(Review review) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.passenger = new PassengerEmailDTO(1, "Anja@gmail.com");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public PassengerEmailDTO getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerEmailDTO passenger) {
        this.passenger = passenger;
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
