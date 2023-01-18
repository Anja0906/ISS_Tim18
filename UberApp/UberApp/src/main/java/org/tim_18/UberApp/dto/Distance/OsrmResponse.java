package org.tim_18.UberApp.dto.Distance;

import java.util.List;

public class OsrmResponse {
    private String code;
    private List<OsrmRoute> routes;
    private List<Waypoint> waypoints;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public List<OsrmRoute> getRoutes() {
        return routes;
    }
    public void setRoutes(List<OsrmRoute> routes) {
        this.routes = routes;
    }
    public List<Waypoint> getWaypoints() {
        return waypoints;
    }
    public void setWaypoints(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }


}
