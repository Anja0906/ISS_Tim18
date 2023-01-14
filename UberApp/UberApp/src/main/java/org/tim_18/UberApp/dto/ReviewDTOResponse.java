package org.tim_18.UberApp.dto;

import lombok.Data;
import org.tim_18.UberApp.model.Passenger;
import org.tim_18.UberApp.model.Review;

import java.util.HashSet;

@Data
public class ReviewDTOResponse {
    private VehicleReviewDTO vehicleReview;
    private DriverReviewDTO driverReview;


    public ReviewDTOResponse(VehicleReviewDTO vehicleReview, DriverReviewDTO driverReview) {
        this.vehicleReview = vehicleReview;
        this.driverReview  = driverReview;
    }


}