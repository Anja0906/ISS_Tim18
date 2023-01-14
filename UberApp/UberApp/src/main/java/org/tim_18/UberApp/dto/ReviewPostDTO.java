package org.tim_18.UberApp.dto;

<<<<<<< Updated upstream
=======

import lombok.Data;


@Data
>>>>>>> Stashed changes
public class ReviewPostDTO {
    //{
    //  "rating": 3,
    //  "comment": "The vehicle was bad and dirty"
    //}
    private Integer rating;
    private String comment;

    public ReviewPostDTO(){
    }
    public ReviewPostDTO(Integer rating, String comment) {
        this.rating = rating;
        this.comment = comment;
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
}
