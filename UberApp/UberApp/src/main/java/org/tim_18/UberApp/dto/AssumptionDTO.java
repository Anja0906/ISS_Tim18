package org.tim_18.UberApp.dto;

import org.tim_18.UberApp.model.Ride;

public class AssumptionDTO {
    private int estimatedTimeInMinutes;
    private int estimatedCost;

    public AssumptionDTO(int estimatedTimeInMinutes, int estimatedCost) {
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
        this.estimatedCost = estimatedCost;
    }

    public AssumptionDTO(Ride ride){
        this.estimatedTimeInMinutes = ride.getEstimatedTimeInMinutes();
        this.estimatedCost = (int)ride.getTotalCost();
    }

    public int getEstimatedTimeInMinutes() {
        return estimatedTimeInMinutes;
    }

    public void setEstimatedTimeInMinutes(int estimatedTimeInMinutes) {
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
    }

    public int getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(int estimatedCost) {
        this.estimatedCost = estimatedCost;
    }
}
