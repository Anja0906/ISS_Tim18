package org.tim_18.UberApp.service;

import net.bytebuddy.pool.TypePool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import org.tim_18.UberApp.UberAppApplication;
import org.tim_18.UberApp.dto.Distance.OsrmResponse;
import org.tim_18.UberApp.dto.rideDTOs.RideRecDTO;
import org.tim_18.UberApp.exception.BadRequestException;
import org.tim_18.UberApp.exception.RideNotFoundException;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.repository.RideRepository;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = UberAppApplication.class)
@ExtendWith(SpringExtension.class)
public class RideServiceTest {
    Ride ride;
    @Autowired
    private RideService rideService;

    @MockBean
    private RideRepository rideRepository;

    private OsrmResponse osrmResponse;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        osrmResponse = new OsrmResponse();
        User driver1 = new User("name", "surname", "profilePicture",
                "telephoneNumber", "email", "address",
                "password", false, true, new ArrayList<>());
        User passenger = new User("name", "surname", "profilePicture",
                "telephoneNumber", "email", "address",
                "password", false, true, new ArrayList<>());
        Driver driver = new Driver();
        driver.setName("John");
        driver.setSurname("Doe");
        driver.setProfilePicture("profile.jpg");
        driver.setTelephoneNumber("123456789");
        driver.setEmail("john.doe@example.com");
        driver.setAddress("123 Main St, City");
        driver.setPassword("password123");
        driver.setBlocked(false);
        driver.setActive(true);
        driver.setIsOnline(true);

        Vehicle vehicle = new Vehicle();
        vehicle.setDriver(driver);
        vehicle.setVehicleType(VehicleType.LUKSUZNO);
        vehicle.setModel("Toyota Camry");
        vehicle.setLicenseNumber("ABC123");
        vehicle.setCurrentLocation(new Location(1, 45.45, 23.23, "Street"));
        vehicle.setPassengerSeats(4);
        vehicle.setBabyTransport(false);
        vehicle.setPetTransport(true);

        driver.setVehicle(vehicle);

        Document document = new Document();
        document.setName("Driver's License");
        document.setDocumentImage("license.jpg");
        document.setDriver(driver);

        Set<Document> documents = new HashSet<>();
        documents.add(document);
        driver.setDocuments(documents);

        this.ride = new Ride();
        ride.setDriver(driver);

        Set<Ride> rides = new HashSet<>();
        rides.add(ride);
        driver.setRides(rides);

        ride.setStatus(Status.PENDING);
        ride.setRejection(new Rejection("blabla", Date.from(Instant.now())));
        ride.setPanic(new Panic(ride, new User(), Date.from(Instant.now()), "blablabla"));
        ride.setId(15);
        ride.setDriver(driver);
        HashSet<Passenger> passengers = new HashSet<Passenger>();
        passengers.add(new Passenger(passenger));
        ride.setPassengers(passengers);
        ride.setVehicleType(vehicle.getVehicleType());
        ride.setEstimatedTimeInMinutes(10);
        ride.setBabyTransport(vehicle.getBabyTransport());
        ride.setPetTransport(vehicle.getPetTransport());
        ride.setTotalCost(100L);
        ride.setStartTime(Date.from(Instant.now()));
        ride.setEndTime(Date.from(Instant.now()));

        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindRidesByPassengersIdWithNoRides() {
        when(rideRepository.findRidesByPassengersId(any(Integer.class), anyString(), anyString(), any(Pageable.class))).thenReturn(new PageImpl<Ride>(Collections.emptyList()));
        Page<Ride> rides = rideService.findRidesByPassengersId(1, "Start", "End", pageable);
        assertEquals(0, rides.getTotalElements());
    }

    @Test
    void testFindRidesByPassengersIdWithRides() {
        Ride ride = new Ride(); // fill this with appropriate data
        List<Ride> rideList = Arrays.asList(ride);
        when(rideRepository.findRidesByPassengersId(any(Integer.class), anyString(), anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(rideList));
        Page<Ride> rides = rideService.findRidesByPassengersId(1, "2023-12-01 23:59:59", "2023-12-11 23:59:59", pageable);
        assertEquals(1, rides.getTotalElements());
        assertEquals(ride, rides.getContent().get(0));
    }

    @Test
    void testFindRidesByPassengersIdWithInvalidId() {
        when(rideRepository.findRidesByPassengersId(any(Integer.class), anyString(), anyString(), any(Pageable.class))).thenReturn(new PageImpl<Ride>(Collections.emptyList()));
        Page<Ride> rides = rideService.findRidesByPassengersId(-1, "2023-12-11 23:59:59", "2023-12-11 23:59:59", pageable);
        assertEquals(0, rides.getTotalElements());
    }

    @Test
    public void testFindRidesByPassengersIdWithInvalidPageNumber() {
        assertThrows(IllegalArgumentException.class, () -> {
            rideService.findRidesByPassengersId(1,
                    "2023-12-11 23:59:59", "2023-12-11 23:59:59",
                    PageRequest.of(-1, 20));});
    }

    @Test
    void testFindPendingRidesByStatusWithNoRides() {
        when(rideRepository.findPendingRidesByStatus(any(String.class), any(Pageable.class))).thenReturn(Page.empty());
        Page<Ride> rides = rideService.findPendingRidesByStatus("PENDING", pageable);
        assertEquals(0, rides.getTotalElements());
    }

    @Test
    void testFindPendingRidesByStatusWithRides() {
        ArrayList<Ride> rides = new ArrayList<Ride>();
        rides.add(this.ride);
        Page<Ride> page = new PageImpl<Ride>(rides);
        when(rideRepository.findPendingRidesByStatus(any(String.class), any(Pageable.class))).thenReturn(page);
        Page<Ride> actualRides = rideService.findPendingRidesByStatus("PENDING", pageable);
        assertEquals(page.getTotalElements(), actualRides.getTotalElements());
    }

    @Test
    void testFindPendingRidesByStatusWithInvalidStatus() {
        when(rideRepository.findPendingRidesByStatus(any(String.class), any(Pageable.class))).thenReturn(Page.empty());
        Page<Ride> rides = rideService.findPendingRidesByStatus("INVALID_STATUS", pageable);
        assertEquals(0, rides.getTotalElements());
    }

    @Test
    public void testFindPendingRidesByStatusWithInvalidPageNumber() {
        String status = "PENDING";
        assertThrows(IllegalArgumentException.class, () -> {
            rideService.findPendingRidesByStatus(status, PageRequest.of(-1, 20));
        });
    }

    @Test
    public void testCreateRideWithValidData() {
        when(rideRepository.save(any(Ride.class))).thenReturn(this.ride);
        Ride savedRide = rideService.createRide(this.ride);
        assertNotNull(savedRide);
        assertEquals(ride.getId(), savedRide.getId());
        assertEquals(ride.getTotalCost(), savedRide.getTotalCost());
        verify(rideRepository, times(1)).save(ride);
    }

    @Test
    public void testCreateRideWithInvalidParameters() {
        when(rideRepository.save(null)).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> {
            rideService.createRide(null);
        });
        verify(rideRepository, times(1)).save(null);
    }

    @Test
    public void testGetDriverActiveRideWithValidParameters() {
        Integer driverId = 1;
        when(rideRepository.findDriverActiveRide(anyInt())).thenReturn(Optional.of(this.ride));
        Ride savedRide = rideService.getDriverActiveRide(driverId);
        assertNotNull(savedRide);
        assertEquals(ride.getId(), savedRide.getId());
        assertEquals(ride.getTotalCost(), savedRide.getTotalCost());
        verify(rideRepository, times(1)).findDriverActiveRide(driverId);
    }

    @Test
    public void testGetDriverActiveRideWithNullParameters() {
        Integer driverId = 1;
        when(rideRepository.findDriverActiveRide(anyInt())).thenReturn(Optional.empty());
        assertThrows(RideNotFoundException.class, () -> {
            rideService.getDriverActiveRide(driverId);});
        verify(rideRepository, times(1)).findDriverActiveRide(driverId);
    }

    @Test
    public void testGetDriverActiveRideWithInvalidParameters() {
        Integer driverId = -1;
        when(rideRepository.findDriverActiveRide(driverId)).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> {
            rideRepository.findDriverActiveRide(driverId);
        });
    }

    @Test
    public void testGetActiveDriverRideWithValidParameters() {
        Integer driverId = 1;
        when(rideRepository.findDriverActiveRide(anyInt())).thenReturn(Optional.of(this.ride));
        Optional<Ride> savedRide = rideService.getActiveRideDriver(driverId);
        assertNotNull(savedRide);
        assertEquals(ride.getId(), savedRide.get().getId());
        assertEquals(ride.getTotalCost(), savedRide.get().getTotalCost());
        verify(rideRepository, times(1)).findDriverActiveRide(driverId);
    }

    @Test
    public void testGetActiveDriverRideWithInvalidParameters() {
        Integer driverId = -1;
        when(rideRepository.findDriverActiveRide(driverId)).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> {
            rideRepository.findDriverActiveRide(driverId);
        });
    }

    @Test
    public void testGetDriverAcceptedRidesWithValidParameters() {
        Integer driverId = 1;
        Ride ride2 = this.ride;
        ride2.setStatus(Status.ACCEPTED);
        List<Ride> rides = Arrays.asList(this.ride, ride2);
        when(rideRepository.findDriverAcceptedRides(eq(driverId), eq("ACCEPTED"), eq("PENDING"))).thenReturn(rides);
        List<Ride> returnedRides = rideService.getDriverAcceptedRides(driverId);
        assertNotNull(returnedRides);
        assertEquals(2, returnedRides.size());
        assertTrue(returnedRides.containsAll(rides));
        verify(rideRepository, times(1)).findDriverAcceptedRides(driverId, "ACCEPTED", "PENDING");
    }

    @Test
    public void testGetDriverAcceptedRidesWithInvalidParameters() {
        Integer driverId = 1;
        when(rideRepository.findDriverAcceptedRides(eq(driverId), eq("ACCEPTED"), eq("PENDING"))).thenReturn(Arrays.asList());
        List<Ride> returnedRides = rideService.getDriverAcceptedRides(driverId);
        assertNotNull(returnedRides);
        assertTrue(returnedRides.isEmpty());
        verify(rideRepository, times(1)).findDriverAcceptedRides(driverId, "ACCEPTED", "PENDING");
    }

    @Test
    public void testGetDriverAcceptedRidesWithInvalidDriverId() {
        Integer driverId = -1;
        when(rideRepository.findDriverAcceptedRides(eq(driverId), eq("ACCEPTED"),
                eq("PENDING"))).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> {
            rideService.getDriverAcceptedRides(driverId);
        });
        verify(rideRepository, times(1)).findDriverAcceptedRides(driverId, "ACCEPTED", "PENDING");
    }

    @Test
    public void testGetDriverAcceptedRidesWithExceptionFromRepository() {
        Integer driverId = 1;
        when(rideRepository.findDriverAcceptedRides(eq(driverId), eq("ACCEPTED"),
                eq("PENDING"))).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> {
            rideService.getDriverAcceptedRides(driverId);
        });
        verify(rideRepository, times(1)).findDriverAcceptedRides(driverId, "ACCEPTED", "PENDING");
    }

    @Test
    public void testGetDriverAcceptedRidesWithInvalidStatuses() {
        Integer driverId = 1;
        when(rideRepository.findDriverAcceptedRides(eq(driverId), eq("COMPLETED"), eq("REJECTED"))).thenReturn(new ArrayList<>());
        List<Ride> returnedRides = rideService.getDriverAcceptedRides(driverId);
        assertNotNull(returnedRides);
        assertTrue(returnedRides.isEmpty());
        verify(rideRepository, times(1)).findDriverAcceptedRides(driverId, "ACCEPTED", "PENDING");
    }

    @Test
    public void testGetDriverAcceptedRidesWithNullDriverId() {
        Integer driverId = null;
        List<Ride> allRides = Arrays.asList(new Ride(), new Ride());
        when(rideRepository.findDriverAcceptedRides(eq(driverId), eq("ACCEPTED"), eq("PENDING"))).thenReturn(allRides);
        List<Ride> returnedRides = rideService.getDriverAcceptedRides(driverId);
        assertNotNull(returnedRides);
        assertEquals(allRides.size(), returnedRides.size());
        verify(rideRepository, times(1)).findDriverAcceptedRides(driverId, "ACCEPTED", "PENDING");
    }
    @Test
    public void testGetPassengerActiveRideWithValidParameters() {
        Integer passengerId = 1;
        when(rideRepository.findPassengerActiveRide(eq(passengerId), eq("ACCEPTED"))).thenReturn(Optional.of(ride));
        Ride returnedRide = rideService.getPassengerActiveRide(passengerId);
        assertNotNull(returnedRide);
        assertEquals(ride, returnedRide);
        verify(rideRepository, times(1)).findPassengerActiveRide(passengerId, "ACCEPTED");
    }

    @Test
    public void testGetPassengerActiveRideWithNoRide() {
        Integer passengerId = 1;
        when(rideRepository.findPassengerActiveRide(eq(passengerId), eq("ACCEPTED"))).thenReturn(Optional.empty());
        assertThrows(RideNotFoundException.class, () -> {
            rideService.getPassengerActiveRide(passengerId);
        });
        verify(rideRepository, times(1)).findPassengerActiveRide(passengerId, "ACCEPTED");
    }

    @Test
    public void testGetPassengerActiveRideWithNullPassengerId() {
        Integer passengerId = null;
        when(rideRepository.findPassengerActiveRide(eq(passengerId), eq("ACCEPTED"))).thenReturn(Optional.empty());
        assertThrows(RideNotFoundException.class, () -> {
            rideService.getPassengerActiveRide(passengerId);
        });
        verify(rideRepository, times(1)).findPassengerActiveRide(passengerId, "ACCEPTED");
    }

    @Test
    public void testFindRideByIdWithValidParameters() {
        Integer id = 1;
        when(rideRepository.findRideById(eq(id))).thenReturn(Optional.of(ride));
        Ride returnedRide = rideService.findRideById(id);
        assertNotNull(returnedRide);
        assertEquals(ride, returnedRide);
        verify(rideRepository, times(1)).findRideById(id);
    }

    @Test
    public void testFindRideByIdWithNoRide() {
        Integer id = 1;
        when(rideRepository.findRideById(eq(id))).thenReturn(Optional.empty());
        assertThrows(RideNotFoundException.class, () -> {
            rideService.findRideById(id);
        });
        verify(rideRepository, times(1)).findRideById(id);
    }

    @Test
    public void testFindRideByIdWithNullId() {
        Integer id = null;
        when(rideRepository.findRideById(eq(id))).thenReturn(Optional.empty());
        assertThrows(RideNotFoundException.class, () -> {
            rideService.findRideById(id);
        });
        verify(rideRepository, times(1)).findRideById(id);
    }

    @Test
    public void testUpdateRideWithValidParameters() {
        when(rideRepository.save(eq(ride))).thenReturn(ride);
        Ride returnedRide = rideService.updateRide(ride);
        assertNotNull(returnedRide);
        assertEquals(ride, returnedRide);
        verify(rideRepository, times(1)).save(ride);
    }

    @Test
    public void testUpdateRideWithInvalidParameters() {
        Ride ride = null;
        when(rideRepository.save(null)).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> {
            rideService.updateRide(ride);
        });
        verify(rideRepository, times(1)).save(null);
    }

    @Test
    public void testFindRidesForDriverWithValidParameters() {
        Integer id = 1;
        String start = "2023-01-01";
        String end = "2023-12-31";
        Pageable pageable = PageRequest.of(0, 5);
        ArrayList<Ride> rides1 = new ArrayList<Ride>();
        rides1.add(ride);
        Page<Ride> rides = new PageImpl<>(rides1);
        when(rideRepository.findRidesForDriver(eq(id), eq(start), eq(end), eq(pageable))).thenReturn(rides);
        Page<Ride> returnedRides = rideService.findRidesForDriver(id, start, end, pageable);
        assertNotNull(returnedRides);
        assertEquals(rides, returnedRides);
        verify(rideRepository, times(1)).findRidesForDriver(id, start, end, pageable);
    }

    @Test
    public void testFindRidesForDriverWithNullId() {
        Integer id = null;
        String start = "2023-01-01";
        String end = "2023-12-31";
        Pageable pageable = PageRequest.of(0, 5);
        when(rideRepository.findRidesForDriver(null,start,end,pageable)).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> {
            rideService.findRidesForDriver(id, start, end, pageable);
        });
    }

    @Test
    public void testFindRidesForDriverWithWrongPageable() {
        Integer id = 1;
        String start = "2023-01-01";
        String end = "2023-12-31";
        assertThrows(IllegalArgumentException.class, () -> {
            rideService.findRidesForDriver(id, start, end, PageRequest.of(-1, 20));
        });
    }

    @Test
    public void testFindRidesForDriverWithWrongDates() {
        Integer id = 1;
        Pageable pageable = PageRequest.of(0, 5);
        when(rideRepository.findRidesForDriver(id,"","",pageable)).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> {
            rideService.findRidesForDriver(id, "", "", pageable);
        });
    }

    @Test
    public void testFindRidesForDriverWithValidData() {
        Integer id = 1;
        String start = "2023-01-01";
        String end = "2023-12-31";
        String sort = "start_time";
        List<Ride> rides = Arrays.asList(ride);
        when(rideRepository.findRidesForDriver(eq(id), eq(start), eq(end), eq(sort))).thenReturn(rides);
        List<Ride> returnedRides = rideService.findRidesForDriver(id, start, end, sort);
        assertNotNull(returnedRides);
        assertEquals(rides, returnedRides);
        verify(rideRepository, times(1)).findRidesForDriver(id, start, end, sort);
    }


    @Test
    void testFindRidesForPassengerWithValidInputs() {
        Integer id = 1;
        String start = "LocationA";
        String end = "LocationB";
        String sort = "ASC";
        Ride ride2 = ride;
        ride2.setStatus(Status.FINISHED);
        when(rideRepository.findRidesForPassenger(id, start, end, sort)).thenReturn(Arrays.asList(ride, ride2));
        List<Ride> result = rideService.findRidesForPassenger(id, start, end, sort);
        assertEquals(2, result.size());
        assertEquals(ride, result.get(0));
        assertEquals(ride2, result.get(1));
    }

    @Test
    void testFindRidesForPassengerWithInvalidInputs(){
        Integer id = null;
        String start = "";
        String end = "";
        String sort = "";
        when(rideRepository.findRidesForPassenger(id, start, end, sort)).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> {
            rideService.findRidesForPassenger(id, start, end, sort);
        });
    }

    @Test
    void testFindRidesForPassengerWithNonExistingPassenger(){
        Integer id = 999;
        String start = "LocationA";
        String end = "LocationB";
        String sort = "ASC";
        when(rideRepository.findRidesForPassenger(id, start, end, sort)).thenReturn(Collections.emptyList());
        List<Ride> result = rideService.findRidesForPassenger(id, start, end, sort);
        assertEquals(0, result.size());
    }

    @Test
    void testFindRidesForPassengerWithInvalidSort(){
        Integer id = 1;
        String start = "LocationA";
        String end = "LocationB";
        String sort = "INVALID";
        when(rideRepository.findRidesForPassenger(id, start, end, sort)).thenReturn(Collections.emptyList());
        List<Ride> result = rideService.findRidesForPassenger(id, start, end, sort);
        assertEquals(0, result.size());
    }

    @Test
    void testCheckRideWithValidParameters(){
        int passengerId = 1;
        ArrayList<Ride> rides = new ArrayList<>();
        rides.add(ride);
        when(rideRepository.findPassengersRidesByStatus(passengerId,
                "PENDING")).thenReturn(rides);
        ArrayList<Ride> result = rideRepository.findPassengersRidesByStatus(passengerId, "PENDING");
        assertFalse(result.isEmpty());
    }

    @Test
    void testCheckRideWithValidNoPendingRidesExist(){
        int passengerId = 1;
        when(rideRepository.findPassengersRidesByStatus(passengerId,
                "PENDING")).thenReturn(new ArrayList<>());
        ArrayList<Ride> result = rideRepository.findPassengersRidesByStatus(passengerId, "PENDING");
        assertTrue(result.isEmpty());
    }

    @Test
    void testCheckRideWithNonExistingPassengerId(){
        int passengerId = 1000;
        when(rideRepository.findPassengersRidesByStatus(passengerId,
                "PENDING")).thenReturn(new ArrayList<>());
        ArrayList<Ride> result = rideRepository.findPassengersRidesByStatus(passengerId, "PENDING");
        assertTrue(result.isEmpty());
    }

    @Test
    void testCheckRideWithInvalidPassengerId(){
        int passengerId = -1;
        when(rideRepository.findPassengersRidesByStatus(passengerId,
                "PENDING")).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> {
            rideService.checkRide(passengerId);
        });
    }

    @Test
    void testFindRidesInDateRangeWithValidParameters(){
        String start = "2023-06-01", end = "2023-06-30", sort = "asc";
        ArrayList<Ride> rides = new ArrayList<>();
        rides.add(ride);
        when(rideRepository.findRidesInDateRange(start, end, sort)).thenReturn(rides);
        List<Ride> result = rideService.findRidesInDateRange(start, end, sort);
        assertEquals(1, result.size());
    }

    @Test
    void testFindRidesInDateRangeWithEmptyParameters(){
        String start = "", end = "", sort = "";
        when(rideRepository.findRidesInDateRange(start, end, sort)).thenReturn(Collections.emptyList());
        List<Ride> result = rideService.findRidesInDateRange(start, end, sort);
        assertEquals(0, result.size());
    }

    @Test
    void testFindRidesInDateRangeWithNonExistingRides(){
        String start = "2023-06-01", end = "2023-06-30", sort = "asc";
        when(rideRepository.findRidesInDateRange(start, end, sort)).thenReturn(Collections.emptyList());
        List<Ride> result = rideService.findRidesInDateRange(start, end, sort);
        assertEquals(0, result.size());
    }

    @Test
    void testFindRidesInDateRangeWithInvalidDateFormat(){
        String start = "2023/06/01", end = "2023-06-30", sort = "asc";
        when(rideRepository.findRidesInDateRange(start, end, sort)).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> {
            rideService.findRidesInDateRange(start, end, sort);
        });
    }

    @Test
    void testFindRidesInDateRangeWithInvalidSort(){
        String start = "2023-06-01", end = "2023-06-30", sort = "invalid";
        when(rideRepository.findRidesInDateRange(start, end, sort)).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> {
            rideService.findRidesInDateRange(start, end, sort);
        });
    }

    @Test
    void testFindRidesByUserWithValidParameters(){
        int userId = 1;
        String start = "2023-06-01", end = "2023-06-30", sort = "asc";
        List<Ride> rides = new ArrayList<>();
        rides.add(ride);
        when(rideRepository.findRidesForUser(userId, start, end, sort)).thenReturn(rides);
        List<Ride> result = rideService.findRidesByUser(userId, start, end, sort);
        assertEquals(1, result.size());
    }

    @Test
    void testFindRidesByUserWithInvalidParameters(){
        int userId = -1;
        String start = "", end = "", sort = "";
        when(rideRepository.findRidesForUser(userId, start, end, sort)).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> {
            rideService.findRidesByUser(userId, start, end, sort);
        });
    }

    @Test
    void testFindRidesByUserWithNonExistingUser(){
        int userId = 1000;
        String start = "2023-06-01", end = "2023-06-30", sort = "asc";
        when(rideRepository.findRidesForUser(userId, start, end, sort)).thenReturn(Collections.emptyList());
        List<Ride> result = rideService.findRidesByUser(userId, start, end, sort);
        assertEquals(0, result.size());
    }

    @Test
    void testFindRidesByUserWithNonExistingRides(){
        int userId = 7;
        String start = "2023-06-01", end = "2023-06-30", sort = "asc";
        when(rideRepository.findRidesForUser(userId, start, end, sort)).thenReturn(Collections.emptyList());
        List<Ride> result = rideService.findRidesByUser(userId, start, end, sort);
        assertEquals(0, result.size());
    }

    @Test
    void testFindRidesByUserWithInvalidDates(){
        int userId = 2;
        String start = "2023/06/01", end = "2023-06-30", sort = "asc";
        when(rideRepository.findRidesForUser(userId, start, end, sort)).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> {
            rideService.findRidesByUser(userId, start, end, sort);
        });
    }

    @Test
    void testFindScheduledRidesWithValidInput(){
        double time = 55.5;
        VehicleType vehicleType = VehicleType.KOMBI;
        boolean babyTransport = true;
        boolean petTransport = true;
        List<Ride> rides = new ArrayList<>();
        rides.add(ride);
        when(rideRepository.findScheduledRides(time, vehicleType.toString(), babyTransport, petTransport)).thenReturn(rides);
        List<Ride> results = rideService.findScheduledRides(time, vehicleType, babyTransport, petTransport);
        assertEquals(1, results.size());
    }

    @Test
    void testFindScheduledRidesWithInvalidInput(){
        double time = -55.5;
        VehicleType vehicleType = VehicleType.KOMBI;
        boolean babyTransport = true;
        boolean petTransport = true;
        when(rideRepository.findScheduledRides(time, vehicleType.toString(), babyTransport, petTransport)).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> {
            rideService.findScheduledRides(time, vehicleType, babyTransport, petTransport);
        });
    }

    @Test
    void testFindScheduledRidesWithNullParameters(){
        double time = 0.0;
        VehicleType vehicleType = VehicleType.LUKSUZNO;
        boolean babyTransport = false;
        boolean petTransport = false;
        when(rideRepository.findScheduledRides(time, vehicleType.toString(), babyTransport, petTransport)).thenReturn(Collections.emptyList());
        List<Ride> result = rideService.findScheduledRides(time, vehicleType, babyTransport, petTransport);
        assertEquals(0, result.size());
    }

    @Test
    void testFindScheduledRidesByTimeWithValidInput(){
        double time = 55.5;
        List<Ride> rides = new ArrayList<>();
        rides.add(ride);
        when(rideRepository.findScheduledRides(time)).thenReturn(rides);
        List<Ride> results = rideService.findScheduledRides(time);
        assertEquals(1, results.size());
    }

    @Test
    void testFindScheduledRidesByTimeWithInvalidInput(){
        double time = -55.5;
        when(rideRepository.findScheduledRides(time)).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> {
            rideService.findScheduledRides(time);
        });
    }

    @Test
    void testFindScheduledRidesByTimeWithNullParameters(){
        double time = 0.0;
        when(rideRepository.findScheduledRides(time)).thenReturn(Collections.emptyList());
        List<Ride> result = rideService.findScheduledRides(time);
        assertEquals(0, result.size());
    }

    @Test
    void testGetStepsWithValidLocations() {
        Location departure = new Location(1, 52.5200, 13.4050, "");
        Location destination = new Location(2, 48.8566, 2.3522, "");
        LocationsForRide locations = new LocationsForRide();
        locations.setDeparture(departure);
        locations.setDestination(destination);
        OsrmResponse expectedResponse = new OsrmResponse();
        String expectedUrl = "http://router.project-osrm.org/route/v1/driving/52.5200,13.4050;48.8566,2.3522?steps=true";
        when(restTemplate.getForObject(expectedUrl, OsrmResponse.class)).thenReturn(expectedResponse);
        OsrmResponse result = restTemplate.getForObject(expectedUrl, OsrmResponse.class);
        assertEquals(expectedResponse, result);
        verify(restTemplate).getForObject(expectedUrl, OsrmResponse.class);
    }

    @Test
    void testGetStepsWithNullLocations() {
        LocationsForRide locations = null;
        assertThrows(NullPointerException.class, () -> restTemplate.getForObject(String.format("http://router.project-osrm.org/route/v1/driving/%s,%s;%s,%s?steps=true", locations.getDeparture().getLatitude(), locations.getDeparture().getLongitude(), locations.getDestination().getLatitude(), locations.getDestination().getLongitude()), OsrmResponse.class));
    }

    @Test
    void testGetStepsWithLocationsWithNullCoordinates() {
        Location departure = new Location(0, null, null, "");
        Location destination = new Location(0, null, null, "");
        LocationsForRide locations = new LocationsForRide();
        locations.setDeparture(departure);
        locations.setDestination(destination);
        String expectedUrl = String.format("http://router.project-osrm.org/route/v1/driving/%s,%s;%s,%s?steps=true", locations.getDeparture().getLatitude(), locations.getDeparture().getLongitude(), locations.getDestination().getLatitude(), locations.getDestination().getLongitude());
        when(restTemplate.getForObject(expectedUrl, OsrmResponse.class)).thenThrow(new NullPointerException());
        assertThrows(NullPointerException.class, () -> restTemplate.getForObject(expectedUrl, OsrmResponse.class));
    }


    @Test
    void testGetStepsWhenExternalServiceFails() {
        Location departure = new Location(1, 52.5200, 13.4050, ""); // Berlin
        Location destination = new Location(2, 48.8566, 2.3522, ""); // Paris
        LocationsForRide locations = new LocationsForRide();
        locations.setDestination(destination);
        locations.setDeparture(departure);
        String expectedUrl = String.format("http://router.project-osrm.org/route/v1/driving/%s,%s;%s,%s?steps=true", locations.getDeparture().getLatitude(), locations.getDeparture().getLongitude(), locations.getDestination().getLatitude(), locations.getDestination().getLongitude());
        when(restTemplate.getForObject(expectedUrl, OsrmResponse.class)).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> restTemplate.getForObject(expectedUrl, OsrmResponse.class));
    }

    @Test
    void testCheckScheduledTimeWithTimeInPast() {
        RideRecDTO oldDTO = new RideRecDTO();
        Instant pastTime = Instant.now().minusSeconds(60);
        oldDTO.setScheduledTime(pastTime.toString());
        assertThrows(BadRequestException.class, () -> rideService.checkScheduledTime(oldDTO));
    }

    @Test
    void testCheckScheduledTimeWithTimeInMoreThan5Hours() {
        RideRecDTO oldDTO = new RideRecDTO();
        Instant futureTime = Instant.now().plusSeconds(60 * 60 * 7);
        oldDTO.setScheduledTime(futureTime.toString());
        assertThrows(BadRequestException.class, () -> rideService.checkScheduledTime(oldDTO));
    }

    @Test
    void testCheckScheduledTimeWithTimeWithin5Hours() {
        RideRecDTO oldDTO = new RideRecDTO();
        Instant futureTime = Instant.now().plusSeconds(60 * 60 * 4);
        oldDTO.setScheduledTime(futureTime.toString());
        assertDoesNotThrow(() -> rideService.checkScheduledTime(oldDTO));
    }

    @Test
    void testCheckScheduledTimeWIthScheduledTimeNull() {
        RideRecDTO oldDTO = new RideRecDTO();
        oldDTO.setScheduledTime(null);
        assertDoesNotThrow(() -> rideService.checkScheduledTime(oldDTO));
    }
}
