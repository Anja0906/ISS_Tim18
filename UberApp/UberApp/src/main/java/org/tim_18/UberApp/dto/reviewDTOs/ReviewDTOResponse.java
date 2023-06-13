package org.tim_18.UberApp.dto.reviewDTOs;

import lombok.Data;
import org.tim_18.UberApp.dto.vehicleDTOs.VehicleReviewDTO;

@Data
public class ReviewDTOResponse {
    private VehicleReviewDTO vehicleReview;
    private DriverReviewDTO driverReview;


    public ReviewDTOResponse(VehicleReviewDTO vehicleReview, DriverReviewDTO driverReview) {
        this.vehicleReview = vehicleReview;
        this.driverReview  = driverReview;
    }


}