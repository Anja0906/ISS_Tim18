package org.tim_18.UberApp.dto.Distance;

import lombok.Data;

@Data
public class DurationDistance {
    private double duration;
    private double distance;

    public DurationDistance(double duration, double distance) {
        this.duration = duration;
        this.distance = distance;
    }
}
