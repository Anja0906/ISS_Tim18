package org.tim_18.UberApp.service;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.model.Ride;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Service
public class ReportsService {

    public HashMap<String, Integer>  getRidesPerDay(List<Ride> rides){
        HashMap<String, Integer> map = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        for (Ride ride : rides){map.put(String.valueOf(dateFormat.format(ride.getScheduledTime())), 0);}
        for (Ride ride : rides){
            map.compute(String.valueOf(dateFormat.format(ride.getScheduledTime())), (k, v) -> v + 1);
        }
        return map;
    }

    public HashMap<String, Long>  getMoneyCountPerDay(List<Ride> rides){
        HashMap<String, Long> map = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        for (Ride ride : rides){map.put(String.valueOf(dateFormat.format(ride.getScheduledTime())), 0L);}
        for (Ride ride : rides){
            map.compute(String.valueOf(dateFormat.format(ride.getScheduledTime())), (k, v) -> v + ride.getTotalCost());
        }
        return map;
    }
    public HashMap<String, Double>  getKilometersPerDay(List<Ride> rides){
        HashMap<String, Double> map = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Random r = new Random();
        for (Ride ride : rides){map.put(String.valueOf(dateFormat.format(ride.getScheduledTime())), 0.0);}
        for (Ride ride : rides){
            //mokovano
            map.compute(String.valueOf(dateFormat.format(ride.getScheduledTime())), (k, v) -> v+ 1 + r.nextDouble() * 9);
        }
        return map;
    }

    public double getMoneySum(List<Ride> rides){
        double sum = 0.0;
        for (Ride ride : rides){
            sum += ride.getTotalCost();
        }
        return sum;
    }

    public double getTotalKilometers(HashMap<String, Double> kilometersPerDay){
        double sum = 0;
        for (double value: kilometersPerDay.values()) {
            sum += value;
        }
        return sum;
    }

    public double getAverage(List<Ride> rides){
        HashMap<String, Long> moneySum = this.getMoneyCountPerDay(rides);
        int dayCount = 0;
        for (String day : moneySum.keySet()){
            dayCount += 1;
        }
        double getMoneySum = this.getMoneySum(rides);
        return getMoneySum/dayCount;
    }

//    private double getDistance(double startLat, double startLng, double endLat, double endLng) {
//        RestTemplate restTemplate = new RestTemplate();
//        String url = String.format("http://router.project-osrm.org/route/v1/driving/%s,%s;%s,%s", startLng, startLat, endLng, endLat);
//        OsrmResponse response = restTemplate.getForObject(url, OsrmResponse.class);
//        double distance = response.getRoutes().get(0).getDistance();
//        return distance;
//    }
}
