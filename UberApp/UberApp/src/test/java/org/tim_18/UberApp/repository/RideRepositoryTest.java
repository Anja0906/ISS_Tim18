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
import org.tim_18.UberApp.model.Driver;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.model.Status;
import org.tim_18.UberApp.model.VehicleType;
import org.tim_18.UberApp.service.RideService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = UberAppApplication.class)
@ExtendWith(SpringExtension.class)
@PropertySource("classpath:application-test.properties")
@Sql(scripts = {"/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RideRepositoryTest {
    @Autowired
    private RideRepository rideRepository;


    @Test
    @DisplayName(value = "Find ride by existing ID")
    public void testFindRideById(){
        Optional<Ride> ride = rideRepository.findRideById(7);
        assertNotEquals(ride,Optional.empty());
        assertNotNull(ride.get());
    }

    @Test
    @DisplayName(value = "Attempt to find ride by non-existent ID")
    public void testFindRideByNonExistentId() {
        Optional<Ride> ride = rideRepository.findRideById(100);
        assertEquals(Optional.empty(), ride);
    }

    @Test
    @DisplayName(value = "Find ride by by valid id and compare details")
    public void testFindRideByIdAndCompareDetails() {
        Optional<Ride> ride = rideRepository.findRideById(1);
        assertTrue(ride.isPresent());
        assertEquals(2, ride.get().getDriver().getId());
        assertEquals(500, ride.get().getTotalCost());
        assertEquals(Status.STARTED, ride.get().getStatus());
    }

    @Test
    @DisplayName(value = "Find rides by valid passenger ID within date range")
    public void testFindRidesByValidPassengerAndDateRange() {
        Page<Ride> rides = rideRepository.findRidesByPassengersId(5, "2021-10-10", "2024-10-10", PageRequest.of(0, 10));
        assertNotNull(rides);
        assertEquals(3, rides.getNumberOfElements());
    }

    @Test
    @DisplayName(value = "Attempt to find rides by invalid passenger ID")
    public void testFindRidesByInvalidPassengerId() {
        Page<Ride> rides = rideRepository.findRidesByPassengersId(100, "2021-10-10", "2024-10-10", PageRequest.of(0, 10));
        assertEquals(0, rides.getNumberOfElements());
    }

    @Test
    @DisplayName(value = "Find rides by passenger ID within a date range with no rides")
    public void testFindRidesByDateRangeWithoutRides() {
        Page<Ride> rides = rideRepository.findRidesByPassengersId(5, "2023-10-10", "2024-10-10", PageRequest.of(0, 10));
        assertEquals(0, rides.getNumberOfElements());
    }

    @Test
    @DisplayName(value = "Find active ride by driver id")
    public void testFindDriverActiveRide() {
        Optional<Ride> ride = rideRepository.findDriverActiveRide(2);
        assertTrue(ride.isPresent());
        assertEquals(2, ride.get().getDriver().getId());
    }

    @Test
    @DisplayName(value = "Find active ride by driver ID")
    public void testFindActiveRideByDriverId() {
        Optional<Ride> ride = rideRepository.findDriverActiveRide(2);
        assertNotNull(ride);
        assertFalse(ride.isEmpty());
        assertEquals(Status.STARTED, ride.get().getStatus());
    }


    @Test
    @DisplayName(value = "Attempt to find active ride by driver ID with no active rides")
    public void testFindActiveRideByDriverIdWithoutActiveRides() {
        Optional<Ride> ride = rideRepository.findDriverActiveRide(1);
        assertTrue(ride.isEmpty());
    }

    @Test
    @DisplayName(value = "Find accepted rides by driver ID")
    public void testFindAcceptedRidesByDriverId() {
        List<Ride> rides = rideRepository.findDriverAcceptedRides(3, "1", "0");
        assertNotNull(rides);
        assertFalse(rides.isEmpty());
    }

    @Test
    @DisplayName(value = "Attempt to find accepted rides by driver ID with no accepted rides")
    public void testFindAcceptedRidesByDriverIdWithoutAcceptedRides() {
        List<Ride> rides = rideRepository.findDriverAcceptedRides(1, "1", "0");
        assertNotNull(rides);
        assertEquals(0, rides.size());
    }

    @Test
    @DisplayName(value = "Find active ride by passenger ID")
    public void testFindActiveRideByPassengerId() {
        Optional<Ride> rides = rideRepository.findPassengerActiveRide(4,"2");
        assertNotNull(rides);
        assertFalse(rides.isEmpty());
    }

    @Test
    @DisplayName(value = "Attempt to find active ride by passenger ID with no active rides")
    public void testFindActiveRideByPassengerIdWithoutActiveRides() {
        Optional<Ride> rides = rideRepository.findPassengerActiveRide(1,"2");
        assertNotNull(rides);
        assertTrue(rides.isEmpty());
    }


    @Test
    @DisplayName(value = "Find rides for user within date range")
    public void testFindRidesForUserWithinDateRange() {
        List<Ride> rides = rideRepository.findRidesForUser(4, "2022-10-10", "2024-10-10","start_time");
        assertNotNull(rides);
        assertFalse(rides.isEmpty());
    }

    @Test
    @DisplayName(value = "Find rides for user outside date range")
    public void testFindRidesForUserOutsideDateRange() {
        List<Ride> rides = rideRepository.findRidesForUser(4, "2023-10-10", "2024-10-10","start_time");
        assertNotNull(rides);
        assertTrue(rides.isEmpty());
    }

    @Test
    @DisplayName(value = "Find rides for driver within date range")
    public void testFindRidesForDriverWithinDateRange() {
        Page<Ride> rides = rideRepository.findRidesForDriver(1,"2021-01-01", "2023-10-10",PageRequest.of(0, 10));
        assertNotNull(rides);
        assertFalse(rides.getContent().isEmpty());
    }

    @Test
    @DisplayName(value = "Find rides for driver outside date range")
    public void testFindRidesForDriverOutsideDateRange() {
        Page<Ride> rides = rideRepository.findRidesForDriver(1,"2023-01-01", "2023-10-10",PageRequest.of(0, 10));
        assertNotNull(rides);
        assertTrue(rides.getContent().isEmpty());
    }

    @Test
    @DisplayName(value = "Find sorted rides for driver within date range")
    public void testFindSortedRidesForDriverWithinDateRange() {
        List<Ride> rides = rideRepository.findRidesForDriver(1,"2021-01-01", "2023-10-10","start_time");
        assertNotNull(rides);
        assertFalse(rides.isEmpty());
    }

    @Test
    @DisplayName(value = "Find sorted rides for driver outside date range")
    public void testFindSortedRidesForDriverOutsideDateRange() {
        List<Ride> rides = rideRepository.findRidesForDriver(1,"2023-01-01", "2023-10-10","start_time");
        assertNotNull(rides);
        assertTrue(rides.isEmpty());
    }

    @Test
    @DisplayName(value = "Find sorted rides for passenger within date range")
    public void testFindSortedRidesForPassengerWithinDateRange() {
        List<Ride> rides = rideRepository.findRidesForPassenger(4,"2021-01-01", "2023-10-10","start_time");
        assertNotNull(rides);
        assertFalse(rides.isEmpty());
    }

    @Test
    @DisplayName(value = "Find sorted rides for passenger outside date range")
    public void testFindSortedRidesForPassengerOutsideDateRange() {
        List<Ride> rides = rideRepository.findRidesForPassenger(4,"2023-01-01", "2023-10-10","start_time");
        assertNotNull(rides);
        assertTrue(rides.isEmpty());
    }

    @Test
    @DisplayName(value = "Find sorted rides in date range")
    public void testFindSortedRidesInDateRange() {
        List<Ride> rides = rideRepository.findRidesInDateRange("2021-01-01", "2023-10-10","start_time");
        assertNotNull(rides);
        assertFalse(rides.isEmpty());
    }

    @Test
    @DisplayName(value = "Find sorted rides outside date range")
    public void testFindSortedRidesOutsideDateRange() {
        List<Ride> rides = rideRepository.findRidesInDateRange("2024-01-01", "2024-10-10","start_time");
        assertNotNull(rides);
        assertTrue(rides.isEmpty());
    }


    @Test
    @DisplayName(value = "Find pending rides by status")
    public void testFindPendingRidesByStatus() {
        Page<Ride> rides = rideRepository.findPendingRidesByStatus("0", PageRequest.of(0, 10));
        assertNotNull(rides);
        assertFalse(rides.getContent().isEmpty());
    }

    @Test
    @DisplayName(value = "Attempt to find pending rides by non-existent status")
    public void testFindPendingRidesByNonExistentStatus() {
        Page<Ride> rides = rideRepository.findPendingRidesByStatus("100", PageRequest.of(0, 10));
        assertNotNull(rides);
        assertTrue(rides.getContent().isEmpty());
    }


    @Test
    @DisplayName(value = "Find passenger's rides by status")
    public void testFindPassengerRidesByStatus() {
        ArrayList<Ride> rides = rideRepository.findPassengersRidesByStatus(4,"3");
        assertNotNull(rides);
        assertFalse(rides.isEmpty());
    }

    @Test
    @DisplayName(value = "Attempt to find passenger's rides by non-existent status")
    public void testFindPassengerRidesByNonExistentStatus() {
        ArrayList<Ride> rides = rideRepository.findPassengersRidesByStatus(4,"100");
        assertNotNull(rides);
        assertTrue(rides.isEmpty());
    }

    @Test
    @DisplayName(value = "Find scheduled rides with matching parameters")
    public void testFindScheduledRidesWithParameters() {
        List<Ride> rides = rideRepository.findScheduledRides(1000000000, "0",true,false);
        assertNotNull(rides);
        assertFalse(rides.isEmpty());
    }

    @Test
    @DisplayName(value = "Attempt to find scheduled rides with no matching parameters")
    public void testFindScheduledRidesWithParametersNoMatches() {
        List<Ride> rides = rideRepository.findScheduledRides(10, "2",false,false);
        assertNotNull(rides);
        assertTrue(rides.isEmpty());
    }

    @Test
    @DisplayName(value = "Find scheduled rides within time range")
    public void testFindScheduledRidesWithinTime() {
        List<Ride> rides = rideRepository.findScheduledRides(1000000000);
        assertNotNull(rides);
        assertFalse(rides.isEmpty());
    }

    @Test
    @DisplayName(value = "Attempt to find scheduled rides outside time range")
    public void testFindScheduledRidesOutsideTime() {
        List<Ride> rides = rideRepository.findScheduledRides(10);
        assertNotNull(rides);
        assertTrue(rides.isEmpty());
    }
}
