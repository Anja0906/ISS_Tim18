package org.tim_18.UberApp.dto.Distance;

public class Step {
    private String geometry;
    private String mode;
    private double distance;
    private double duration;
    private double weight;
    private String name;
    private String ref;
    private String destinations;
    private String rotary_name;
    private String rotary_pronunciation;
    private Maneuver maneuver;

    private String driving_side;

    public Step() {
    }

    public Step(String geometry, String mode, double distance, double duration, double weight, String name, String ref, String destinations, String rotary_name, String rotary_pronunciation, Maneuver maneuver, String driving_side) {
        this.geometry = geometry;
        this.mode = mode;
        this.distance = distance;
        this.duration = duration;
        this.weight = weight;
        this.name = name;
        this.ref = ref;
        this.destinations = destinations;
        this.rotary_name = rotary_name;
        this.rotary_pronunciation = rotary_pronunciation;
        this.maneuver = maneuver;
        this.driving_side = driving_side;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getDestinations() {
        return destinations;
    }

    public void setDestinations(String destinations) {
        this.destinations = destinations;
    }

    public String getRotary_name() {
        return rotary_name;
    }

    public void setRotary_name(String rotary_name) {
        this.rotary_name = rotary_name;
    }

    public String getRotary_pronunciation() {
        return rotary_pronunciation;
    }

    public void setRotary_pronunciation(String rotary_pronunciation) {
        this.rotary_pronunciation = rotary_pronunciation;
    }

    public Maneuver getManeuver() {
        return maneuver;
    }

    public void setManeuver(Maneuver maneuver) {
        this.maneuver = maneuver;
    }

    public String getDriving_side() {
        return driving_side;
    }

    public void setDriving_side(String driving_side) {
        this.driving_side = driving_side;
    }
}

