package org.tim_18.UberApp.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tim_18.UberApp.UberAppApplication;
import org.tim_18.UberApp.exception.DriverNotFoundException;
import org.tim_18.UberApp.model.Ride;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = UberAppApplication.class)
@ExtendWith(SpringExtension.class)
@PropertySource("classpath:application-test.properties")
@Sql(scripts = {"/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RideRepositoryTest {


    @Autowired
    private RideRepository rideRepository;




    @Test
    @DisplayName(value = "Find ride by id valid")
    public void testFindRideById(){
        Optional<Ride> ride = rideRepository.findRideById(1);
        assertNotEquals(ride,Optional.empty());
        assertNotNull(ride.get());
    }

    @Test
    @DisplayName(value = "Find ride by non-existent id")
    public void testFindRideByNonExistentId() {
        Optional<Ride> ride = rideRepository.findRideById(100);
        assertEquals(Optional.empty(), ride);
    }


    @Test
    @DisplayName(value = "Find ride by valid id and compare ride details")
    public void testFindRideByIdAndCompareDetails() {
        Optional<Ride> ride = rideRepository.findRideById(1);
        assertTrue(ride.isPresent());
        assertEquals(null, ride.get().getDriver());
        assertEquals(500, ride.get().getTotalCost());
        assertEquals(0, ride.get().getPassengers().size());


    }

    @Test
    @DisplayName(value = "Find rides by passenger id")
    public void testFindRidesByPassengerId() {
        Page<Ride> rides = rideRepository.findRidesByPassengersId(1, "2021-10-10", "2024-10-10", PageRequest.of(0, 10));
        assertNotNull(rides);
        for(Ride ride:rides){
            System.out.println(ride.toString());
        }
        assertEquals(0, rides.getNumberOfElements()); // assumes you have at least 5 rides for this passenger in the given date range
    }

//    @Test
//    @DisplayName(value = "Find active ride by driver id")
//    public void testFindDriverActiveRide() {
//        Optional<Ride> ride = rideRepository.findDriverActiveRide(1);
//        assertTrue(ride.isPresent());
//        assertEquals(1, ride.get().getDriver().getId());
//    }

//    @Test
//    @DisplayName(value = "Find accepted rides by driver id")
//    public void testFindDriverAcceptedRides() {
//        List<Ride> rides = rideRepository.findDriverAcceptedRides(1, "accepted", "another status");
//        assertNotNull(rides);
//        assertFalse(rides.isEmpty());
//        assertEquals("accepted", rides.get(0).getStatus());
//        // You might also want to check that all rides have the expected driver id and status, or that they're in the expected order
//    }


//    @Test
//    @DisplayName(value = "Find active ride by passenger id")
//    public void testFindPassengerActiveRide() {
//        Optional<Ride> ride = rideRepository.findPassengerActiveRide(1, "active");
//        assertTrue(ride.isPresent());
//        assertEquals(1, ride.get().getPassengers()); // you need to replace getPassengerId() with your actual method
//    }

    @Test
    @DisplayName(value = "Find rides for user by id")
    public void testFindRidesForUser() {
        List<Ride> rides = rideRepository.findRidesForUser(1, "2022-01-01", "2022-12-31", "start_time");
        assertNotNull(rides);
        assertFalse(rides.isEmpty());
        // More assertions to check that the rides are for the expected user, in the correct date range and sorted by start_time
    }

//    @Test
//    @DisplayName(value = "Find rides for driver by id")
//    public void testFindRidesForDriver() {
//        Page<Ride> rides = rideRepository.findRidesForDriver(1, "2022-01-01", "2022-12-31", PageRequest.of(0, 5));
//        assertNotNull(rides);
//        assertEquals(5, rides.getNumberOfElements()); // assumes you have at least 5 rides for this driver in the given date range
//    }

//    @Test
//    @DisplayName(value = "Find rides for passenger by id")
//    public void testFindRidesForPassenger() {
//        List<Ride> rides = rideRepository.findRidesForPassenger(1, "2022-01-01", "2022-12-31", "start_time");
//        assertNotNull(rides);
//        assertFalse(rides.isEmpty());
//        // More assertions to check that the rides are for the expected passenger, in the correct date range and sorted by start_time
//    }


    @Test
    @DisplayName(value = "Find rides in date range")
    public void testFindRidesInDateRange() {
        List<Ride> rides = rideRepository.findRidesInDateRange("2022-01-01", "2022-12-31", "start_time");
        assertNotNull(rides);
        assertFalse(rides.isEmpty());
        // More assertions to check that the rides are in the correct date range and sorted by start_time
    }

//    @Test
//    @DisplayName(value = "Find pending rides by status")
//    public void testFindPendingRidesByStatus() {
//        Page<Ride> rides = rideRepository.findPendingRidesByStatus("pending", PageRequest.of(0, 5));
//        assertNotNull(rides);
//        assertEquals(5, rides.getNumberOfElements()); // assumes you have at least 5 pending rides
//        // More assertions to check that the rides have the expected status
//    }


//    @Test
//    @DisplayName(value = "Find passenger's rides by status")
//    public void testFindPassengersRidesByStatus() {
//        ArrayList<Ride> rides = rideRepository.findPassengersRidesByStatus(1, "status");
//        assertNotNull(rides);
//        assertFalse(rides.isEmpty());
//        // More assertions to check that the rides are for the expected passenger and have the expected status
//    }

//    @Test
//    @DisplayName(value = "Find scheduled rides with specific parameters")
//    public void testFindScheduledRidesWithParameters() {
//        List<Ride> rides = rideRepository.findScheduledRides(60.0, "vehicleType", true, true);
//        assertNotNull(rides);
//        assertFalse(rides.isEmpty());
//        // More assertions to check that the rides have the expected parameters
//    }

//    @Test
//    @DisplayName(value = "Find scheduled rides within given time frame")
//    public void testFindScheduledRidesWithinTime() {
//        List<Ride> rides = rideRepository.findScheduledRides(60.0);
//        assertNotNull(rides);
//        assertFalse(rides.isEmpty());
//        // More assertions to check that the rides are within the expected time frame
//    }

    @Test
    @DisplayName(value = "Find rides for driver by id with sorting")
    public void testFindRidesForDriverWithSorting() {
        List<Ride> rides = rideRepository.findRidesForDriver(1, "2022-01-01", "2022-12-31", "start_time");
        assertNotNull(rides);
        assertFalse(rides.isEmpty());
    }

//    @Test
//    @DisplayName(value = "Find rides for passenger by id with sorting")
//    public void testFindRidesForPassengerWithSorting() {
//        List<Ride> rides = rideRepository.findRidesForPassenger(1, "2022-01-01", "2022-12-31", "start_time");
//        assertNotNull(rides);
//        assertFalse(rides.isEmpty());
//    }

    @Test
    @DisplayName(value = "Find rides in date range with sorting")
    public void testFindRidesInDateRangeWithSorting() {
        List<Ride> rides = rideRepository.findRidesInDateRange("2022-01-01", "2022-12-31", "start_time");
        assertNotNull(rides);
        assertFalse(rides.isEmpty());
        // More assertions to check that the rides are in the correct date range and sorted by start_time
    }

//    @Test
//    @DisplayName(value = "Find accepted rides for driver by id")
//    public void testFindDriverAcceptedRides1() {
//        List<Ride> rides = rideRepository.findDriverAcceptedRides(1, "accepted", "completed");
//        assertNotNull(rides);
//        assertFalse(rides.isEmpty());
//        // More assertions to check that the rides are for the expected driver and have the expected status
//    }

//    @Test
//    @DisplayName(value = "Find active ride for driver by id")
//    public void testFindDriverActiveRide() {
//        Optional<Ride> ride = rideRepository.findDriverActiveRide(1);
//        assertTrue(ride.isPresent());
//        assertEquals(1, ride.get().getDriver().getId()); // you need to replace getDriverId() with your actual method
//    }

    @Test
    @DisplayName(value = "Find rides by passenger id within date range")
    public void testFindRidesByPassengersId() {
        Page<Ride> rides = rideRepository.findRidesByPassengersId(1, "2022-01-01", "2022-12-31", PageRequest.of(0, 5));
        assertNotNull(rides);
        assertEquals(0, rides.getNumberOfElements()); // assumes you have at least 5 rides for this passenger in the given date range
    }

//    @Test
//    @DisplayName(value = "Ensure null is not returned")
//    public void testNotNull() {
//        List<Ride> rides = rideRepository.findSomeRidesMethod();
//        assertNotNull(rides);
//        // add more assertions
//    }

//    @Test
//    @DisplayName(value = "Ensure correct number of rides returned")
//    public void testNumberOfRides() {
//        List<Ride> rides = rideRepository.findSomeRidesMethod();
//        assertEquals(5, rides.size());
//        // add more assertions
//    }
//
//    @Test
//    @DisplayName(value = "Ensure method returns empty result when no matching rides")
//    public void testNoMatchingRides() {
//        List<Ride> rides = rideRepository.findSomeRidesMethod();
//        assertTrue(rides.isEmpty());
//    }























}
