package org.tim_18.UberApp.dto.Distance;

import java.util.List;

public class Maneuver {
    private String type;
    private List<Double> location;

    public Maneuver() {
    }

    public Maneuver(String type, List<Double> location) {
        this.type = type;
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }
}
