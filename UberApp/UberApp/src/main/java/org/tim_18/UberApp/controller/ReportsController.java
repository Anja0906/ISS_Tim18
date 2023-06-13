package org.tim_18.UberApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.dto.reportDTOS.ReportDTO;
import org.tim_18.UberApp.exception.DriverNotFoundException;
import org.tim_18.UberApp.exception.PassengerNotFoundException;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.service.ReportsService;
import org.tim_18.UberApp.service.RideService;

import java.util.List;

@RestController
@RequestMapping("api/reports")
@CrossOrigin(value = "*")
public class ReportsController {

    @Autowired RideService rideService;

    @Autowired ReportsService reportsService;

    public ReportsController(RideService rideService, ReportsService reportsService) {
        this.rideService = rideService;
        this.reportsService = reportsService;
    }

    @GetMapping("/{id}/driver")
    public ResponseEntity<?> getRidesForDriver (
            @PathVariable("id") int id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size,
            @RequestParam(defaultValue = "start_time") String sort,
            @RequestParam(defaultValue = "2021-12-07T07:00:50") String from,
            @RequestParam(defaultValue = "2023-12-08T10:40:00") String to) {

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
            List<Ride> rides = rideService.findRidesForDriver(id, from, to, sort);
            return new ResponseEntity<>(this.getReportsDTO(rides), HttpStatus.OK);
        } catch (DriverNotFoundException driverNotFoundException) {
            return new ResponseEntity<>("Driver does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/passenger")
    public ResponseEntity<?> getRidesForPassenger (
            @PathVariable("id") int id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size,
            @RequestParam(defaultValue = "start_time") String sort,
            @RequestParam(defaultValue = "2021-12-07T07:00:50") String from,
            @RequestParam(defaultValue = "2023-12-08T10:40:00") String to) {

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
            List<Ride> rides = rideService.findRidesForPassenger(id, from, to, sort);
            return new ResponseEntity<>(this.getReportsDTO(rides), HttpStatus.OK);
        } catch (PassengerNotFoundException passengerNotFoundException) {
            return new ResponseEntity<>("Passenger does not exist!", HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{id}/user")
    public ResponseEntity<?> getRidesForUser (
            @PathVariable("id") int id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size,
            @RequestParam(defaultValue = "start_time") String sort,
            @RequestParam(defaultValue = "2021-12-07T07:00:50") String from,
            @RequestParam(defaultValue = "2023-12-08T10:40:00") String to) {

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
            List<Ride> rides = rideService.findRidesByUser(id, from, to, sort);
            return new ResponseEntity<>(this.getReportsDTO(rides), HttpStatus.OK);
        } catch (UserNotFoundException userNotFoundException) {
            return new ResponseEntity<>("User does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll (
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size,
            @RequestParam(defaultValue = "start_time") String sort,
            @RequestParam(defaultValue = "2021-12-07T07:00:50") String from,
            @RequestParam(defaultValue = "2023-12-08T10:40:00") String to) {

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
            List<Ride> rides = rideService.findRidesInDateRange(from, to, sort);
            return new ResponseEntity<>(this.getReportsDTO(rides), HttpStatus.OK);
        } catch (DriverNotFoundException driverNotFoundException) {
            return new ResponseEntity<>("Driver does not exist!", HttpStatus.NOT_FOUND);
        }
    }


    private ReportDTO getReportsDTO(List<Ride> rides) {
        ReportDTO reportDTO = new ReportDTO(this.reportsService.getRidesPerDay(rides),
                this.reportsService.getKilometersPerDay(rides),
                this.reportsService.getMoneyCountPerDay(rides),
                this.reportsService.getMoneySum(rides),
                this.reportsService.getAverage(rides),
                this.reportsService.getTotalKilometers(this.reportsService.getKilometersPerDay(rides)));

        return reportDTO;
    }

}
