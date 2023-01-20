package org.tim_18.UberApp.dto.Distance;

import java.util.List;

public class OsrmRoute {
    private String geometry;
    private List<Leg> legs;
    private String weight_name;
    private double weight;
    private double duration;
    private double distance;
    public String getGeometry() {
        return geometry;
    }
    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }
    public List<Leg> getLegs() {
        return legs;
    }
    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }
    public String getWeight_name() {
        return weight_name;
    }
    public void setWeight_name(String weight_name) {
        this.weight_name = weight_name;
    }
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
    public double getDuration() {
        return duration;
    }
    public void setDuration(double duration) {
        this.duration = duration;
    }
    public double getDistance() {
        return distance;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }


}
