package org.tim_18.UberApp.dto;

import lombok.Data;

@Data
public class DriverReviewDTO {
    private Integer id;
    private Integer rating;
    private String comment;
    private PassengerEmailDTO passenger;

    public DriverReviewDTO(Integer id, Integer rating, String comment, PassengerEmailDTO passenger) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.passenger = passenger;
    }
}