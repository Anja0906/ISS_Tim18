package org.tim_18.UberApp.dto;

import lombok.Data;

@Data
public class ReviewPostDTO {

    private Integer rating;
    private String comment;

    public ReviewPostDTO(Integer rating, String comment) {
        this.rating     = rating;
        this.comment    = comment;
    }


}
