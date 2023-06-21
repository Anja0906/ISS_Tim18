package org.tim_18.UberApp.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestClientException;
import org.tim_18.UberApp.Validation.ErrorMessage;
import org.tim_18.UberApp.dto.Distance.OsrmResponse;
import org.tim_18.UberApp.dto.JwtAuthenticationRequest;
import org.tim_18.UberApp.dto.locationDTOs.LocationDTO;
import org.tim_18.UberApp.dto.locationDTOs.LocationSetDTO;
import org.tim_18.UberApp.dto.loginDTOs.LoginDTO;
import org.tim_18.UberApp.dto.panicDTOs.PanicDTO;
import org.tim_18.UberApp.dto.passengerDTOs.PassengerIdEmailDTO;
import org.tim_18.UberApp.dto.rejectionDTO.ReasonDTO;
import org.tim_18.UberApp.dto.rideDTOs.*;
import org.tim_18.UberApp.exception.DriverNotFoundException;
import org.tim_18.UberApp.model.*;
import org.tim_18.UberApp.service.RideService;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(scripts = {"/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class RideControllerTest {

    @Autowired
    private RideService rideService;
    @Autowired
    private RideController rideController;
    private final String BASE_PATH = "http://localhost:8080/api/ride";

    private HttpEntity<Void> passengerEntity;
    private HttpEntity<Void> driverEntity;
    private HttpEntity<Void> adminEntity;
    private JwtResponse passengerJWT,driverJWT,adminJWT;

    @Autowired
    private TestRestTemplate restTemplate;

    private void loginPassenger(){
        ResponseEntity<JwtResponse> responsePassenger = restTemplate.postForEntity("http://localhost:8080/api/user/login",new JwtAuthenticationRequest("mika@gmail.com", "123"), JwtResponse.class);
        HttpHeaders headersPassenger = new HttpHeaders();
        passengerJWT = responsePassenger.getBody();
        System.out.println(passengerJWT.getId());
        headersPassenger.setContentType(MediaType.APPLICATION_JSON);
        headersPassenger.set("Authorization",  "Bearer "+responsePassenger.getBody().getAccessToken());
        passengerEntity = new HttpEntity<>(headersPassenger);
    }

    private void loginPassengerWithoutAnyRidesHistory(){
        ResponseEntity<JwtResponse> responsePassenger = restTemplate.postForEntity("http://localhost:8080/api/user/login",new JwtAuthenticationRequest("zika@gmail.com", "123"), JwtResponse.class);
        HttpHeaders headersPassenger = new HttpHeaders();
        passengerJWT = responsePassenger.getBody();
        System.out.println(passengerJWT.getId());
        headersPassenger.setContentType(MediaType.APPLICATION_JSON);
        headersPassenger.set("Authorization",  "Bearer "+responsePassenger.getBody().getAccessToken());
        passengerEntity = new HttpEntity<>(headersPassenger);
    }

    private void loginPassengerWithRide(){
        ResponseEntity<JwtResponse> responsePassenger = restTemplate.postForEntity("http://localhost:8080/api/user/login",new JwtAuthenticationRequest("andrijinkristina@gmail.com", "123"), JwtResponse.class);
        HttpHeaders headersPassenger = new HttpHeaders();
        passengerJWT = responsePassenger.getBody();
        headersPassenger.setContentType(MediaType.APPLICATION_JSON);
        headersPassenger.set("Authorization",  "Bearer "+responsePassenger.getBody().getAccessToken());
        passengerEntity = new HttpEntity<>(headersPassenger);
    }

    private void loginDriverWithoutRide(){
        ResponseEntity<JwtResponse> responseDriver = restTemplate.postForEntity("http://localhost:8080/api/user/login",new JwtAuthenticationRequest("anjapetkovic92@gmail.com", "123"), JwtResponse.class);
        HttpHeaders headersDriver = new HttpHeaders();
        driverJWT = responseDriver.getBody();
        headersDriver.setContentType(MediaType.APPLICATION_JSON);
        headersDriver.set("Authorization",  "Bearer "+responseDriver.getBody().getAccessToken());
        driverEntity = new HttpEntity<>(headersDriver);
    }

    private void loginDriverWithRide(){
        ResponseEntity<JwtResponse> responseDriver = restTemplate.postForEntity("http://localhost:8080/api/user/login",new JwtAuthenticationRequest("bangiekg1@gmail.com", "123"), JwtResponse.class);
        HttpHeaders headersDriver = new HttpHeaders();
        driverJWT = responseDriver.getBody();
        headersDriver.setContentType(MediaType.APPLICATION_JSON);
        headersDriver.set("Authorization",  "Bearer "+responseDriver.getBody().getAccessToken());
        driverEntity = new HttpEntity<>(headersDriver);
    }


    private void loginAdmin(){
        ResponseEntity<JwtResponse> responseAdmin = restTemplate.postForEntity("http://localhost:8080/api/user/login",new JwtAuthenticationRequest("anita@gmail.com", "123"), JwtResponse.class);
        HttpHeaders headersAdmin = new HttpHeaders();
        adminJWT = responseAdmin.getBody();
        headersAdmin.setContentType(MediaType.APPLICATION_JSON);
        headersAdmin.set("Authorization",  "Bearer "+responseAdmin.getBody().getAccessToken());
        adminEntity = new HttpEntity<>(headersAdmin);
    }

    private RideRecDTO makeRideRecDTO(String email){
        Set<LocationsForRide> locations = new HashSet<>();
        LocationsForRide locationsForRide = new LocationsForRide();
        locationsForRide.setDestination(new Location(1, 44.7866, 20.4489, "Beograd, Srbija"));
        locationsForRide.setDeparture(new Location(2, 44.7836, 20.4449, "Pariz, Francuska"));
        locations.add(locationsForRide);
        Set<Passenger> passengers = new HashSet<>();
        Passenger passenger1 = new Passenger("Marko", "Petrović", "profile1.jpg", "+381123456789", email, "Beograd, Srbija", "pass123", false, true, "abc123");
        passengers.add(passenger1);
        Integer id = 1;
        VehicleType vehicleType = VehicleType.STANDARD;
        boolean babyTransport = true;
        boolean petTransport = true;
        Date scheduledTime = null;
        RideRecDTO rideRecDTO = new RideRecDTO(id,  passengers, vehicleType, babyTransport, petTransport,locations, scheduledTime);
        return rideRecDTO;
    }

    private RideRecDTO makeRideRecDTO1(String email){
        Set<LocationsForRide> locations = new HashSet<>();
        LocationsForRide locationsForRide = new LocationsForRide();
        locationsForRide.setDestination(new Location(1, 44.7866, 20.4489, "Beograd, Srbija"));
        locationsForRide.setDeparture(new Location(2, 44.7836, 20.4449, "Pariz, Francuska"));
        locations.add(locationsForRide);
        Set<Passenger> passengers = new HashSet<>();
        Passenger passenger1 = new Passenger("Marko", "Petrović", "profile1.jpg", "+381123456789", email, "Beograd, Srbija", "pass123", false, true, "abc123");
        passengers.add(passenger1);
        Integer id = 1;
        VehicleType vehicleType = VehicleType.KOMBI;
        boolean babyTransport = true;
        boolean petTransport = true;
        Date scheduledTime = null;
        RideRecDTO rideRecDTO = new RideRecDTO(id,  passengers, vehicleType, babyTransport, petTransport,locations, scheduledTime);
        return rideRecDTO;
    }

    private FavoriteRideDTO makeFavoriteRideDTO(Integer id ,String email){
        FavoriteRideDTO favoriteRideDTO = new FavoriteRideDTO();

        favoriteRideDTO.setFavoriteName("My Favorite Ride");

        Set<LocationSetDTO> locations = new HashSet<>();
        LocationSetDTO locationsForRide = new LocationSetDTO();
        locationsForRide.setDestination(new LocationDTO("Beograd, Srbija", 44.7866, 20.4489));
        locationsForRide.setDeparture(new LocationDTO("Pariz, Francuska", 44.7836, 20.4449));
        locations.add(locationsForRide);
        Set<PassengerIdEmailDTO> passengers = new HashSet<>();
        PassengerIdEmailDTO passenger1 = new PassengerIdEmailDTO(id ,email);
        passengers.add(passenger1);
        favoriteRideDTO.setLocations(locations);
        favoriteRideDTO.setPassengers(passengers);
        favoriteRideDTO.setVehicleType(VehicleType.LUKSUZNO);

        favoriteRideDTO.setBabyTransport(true);

        favoriteRideDTO.setPetTransport(true);
        return favoriteRideDTO;
    }



    @Test
    @DisplayName("Test createRide with valid principal and oldDTO")
    public void testCreateRide_ValidPrincipalAndDTO() {
        loginPassengerWithoutAnyRidesHistory();
        RideRecDTO rideRecDTO = makeRideRecDTO(passengerJWT.getEmail());
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH,
                HttpMethod.POST,
                new HttpEntity<>(rideRecDTO, passengerEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    @DisplayName("Test createRide where principal is not a valid user")
    public void testCreateRide_InvalidPrincipal() {
        loginDriverWithRide();
        RideRecDTO rideRecDTO = makeRideRecDTO(driverJWT.getEmail());
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH,
                HttpMethod.POST,
                new HttpEntity<>(rideRecDTO, driverEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test createRide where oldDTO is null or empty")
    public void testCreateRide_NullOrEmptyDTO() {
        loginPassenger();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH,
                HttpMethod.POST,
                new HttpEntity<>(null, passengerEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test createRide where passengers cannot ride")
    public void testCreateRide_UnavailablePassengers() {
        loginPassengerWithRide();
        RideRecDTO rideRecDTO = makeRideRecDTO(passengerJWT.getEmail());

        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH,
                HttpMethod.POST,
                new HttpEntity<>(rideRecDTO, passengerEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test createRide where driver doesn't exist")
    public void testCreateRide_NonexistentDriver() {
        loginPassengerWithoutAnyRidesHistory();
        RideRecDTO rideRecDTO = makeRideRecDTO1(passengerJWT.getEmail());

        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH,
                HttpMethod.POST,
                new HttpEntity<>(rideRecDTO, passengerEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test createRide where scheduled time is not valid")
    public void testCreateRide_InvalidScheduledTime() {
        loginPassenger();
        RideRecDTO rideRecDTO = makeRideRecDTO(passengerJWT.getEmail());
        rideRecDTO.setScheduledTime("2021-12-10T05:00:00");

        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH,
                HttpMethod.POST,
                new HttpEntity<>(rideRecDTO, passengerEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test getDriverActiveRide without login")
    public void testGetDriverActiveRide_withoutLogin() {
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH +"/driver/"+1+"/active",
                HttpMethod.GET,
                new HttpEntity<>(null),
                String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

    }

    @Test
    @DisplayName("Test getDriverActiveRide where user is not a driver")
    public void testGetDriverActiveRide_NonDriverUser() {
        loginPassenger();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH +"/driver/"+passengerJWT.getId()+"/active",
                HttpMethod.GET,
                new HttpEntity<>(passengerEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test getDriverActiveRide where driver doesn't exist")
    public void testGetDriverActiveRide_NonexistentDriver() {
        loginDriverWithoutRide();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/driver/"+9000+"/active",
                HttpMethod.GET,
                new HttpEntity<>(driverEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }


    @Test
    @DisplayName("Test getDriverActiveRide where driverId does not match logged user")
    public void testGetDriverActiveRide_WrongDriverId() {
        loginDriverWithoutRide();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/driver/"+2+"/active",
                HttpMethod.GET,
                new HttpEntity<>(driverEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }


    @Test
    @DisplayName("Test getDriverActiveRide where driver doesn't have an active ride")
    public void testGetDriverActiveRide_NoActiveRide() {
        loginDriverWithoutRide();
        System.out.println(driverJWT.getEmail());
        System.out.println(driverJWT.getRoles());
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/driver/"+driverJWT.getId()+"/active",
                HttpMethod.GET,
                new HttpEntity<>(driverEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test getDriverActiveRide where driver has an active ride")
    public void testGetDriverActiveRide_WithActiveRide() {
        loginDriverWithRide();
        ResponseEntity<RideRetDTO> responseEntity = restTemplate.exchange(BASE_PATH+"/driver/"+driverJWT.getId()+"/active",
                HttpMethod.GET,
                new HttpEntity<>(driverEntity.getHeaders()),
                RideRetDTO.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test getPassengerActiveRide without login")
    public void testGetPassengerActiveRide_withoutLogin() {
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/passenger/"+3+"/active",
                HttpMethod.GET,
                new HttpEntity<>(null),
                String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

    }


    @Test
    @DisplayName("Test getPassengerActiveRide where user is not a passenger")
    public void testGetPassengerActiveRide_NonPassengerUser() {
        loginDriverWithoutRide();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/passenger/"+driverJWT.getId()+"/active",
                HttpMethod.GET,
                new HttpEntity<>(driverEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test getPassengerActiveRide where passenger doesn't exist")
    public void testGetPassengerActiveRide_NonexistentPassenger() {
        loginPassenger();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/passenger/"+7000+"/active",
                HttpMethod.GET,
                new HttpEntity<>(passengerEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test getPassengerActiveRide where passengerId does not match logged user")
    public void testGetPassengerActiveRide_WrongPassengerId() {
        loginPassenger();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/passenger/"+4+"/active",
                HttpMethod.GET,
                new HttpEntity<>(passengerEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test getPassengerActiveRide where passenger doesn't have an active ride")
    public void testGetPassengerActiveRide_NoActiveRide() {
        loginPassenger();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/passenger/"+passengerJWT.getId()+"/active",
                HttpMethod.GET,
                new HttpEntity<>(passengerEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test getPassengerActiveRide where passenger has an active ride")
    public void testGetPassengerActiveRide_WithActiveRide() {
        loginPassengerWithRide();
        ResponseEntity<RideRetDTO> responseEntity = restTemplate.exchange(BASE_PATH+"/passenger/"+passengerJWT.getId()+"/active",
                HttpMethod.GET,
                new HttpEntity<>(passengerEntity.getHeaders()),
                RideRetDTO.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test findRideById without login")
    public void testFindRideById_withoutLogin() {
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH +"/" + 2,
                HttpMethod.GET,
                new HttpEntity<>(null),
                String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

    }


    @Test
    @DisplayName("Test findRideById where user role is neither DRIVER nor PASSENGER")
    public void testFindRideById_NonDriverNonPassengerUser() {
        loginAdmin();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH +"/" + 2,
                HttpMethod.GET,
                new HttpEntity<>(adminEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }


    @Test
    @DisplayName("Test findRideById where ride doesn't exist")
    public void testFindRideById_NonexistentRide() {
        loginPassenger();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH +"/" + 10000000,
                HttpMethod.GET,
                new HttpEntity<>(passengerEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test findRideById where user doesn't have permission to view the ride")
    public void testFindRideById_NoPermissionToView() {
        loginAdmin();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH +"/" + 1,
                HttpMethod.GET,
                new HttpEntity<>(adminEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test findRideById where ride exists and user has permission to view it")
    public void testFindRideById_RideExistsAndHasPermission() {
        loginDriverWithRide();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH +"/" + 1,
                HttpMethod.GET,
                new HttpEntity<>(driverEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


    @Test
    @DisplayName("Test withdrawRide where user role is not a PASSENGER")
    public void testWithdrawRide_NonPassengerUser() {
        loginAdmin();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/"+1+"/withdraw",
                HttpMethod.PUT,
                new HttpEntity<>(adminEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test withdrawRide where ride doesn't exist")
    public void testWithdrawRide_NonexistentRide() {
        loginPassenger();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/"+8000+"/withdraw",
                HttpMethod.PUT,
                new HttpEntity<>(passengerEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test withdrawRide where user doesn't have authority to withdraw the ride")
    public void testWithdrawRide_NoAuthorityToWithdraw() {
        loginDriverWithoutRide();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/"+1+"/withdraw",
                HttpMethod.PUT,
                new HttpEntity<>(driverEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test withdrawRide where ride is not in PENDING or ACCEPTED status")
    public void testWithdrawRide_NotPendingOrAcceptedRide() {
        loginPassengerWithRide();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/"+1+"/withdraw",
                HttpMethod.PUT,
                new HttpEntity<>(passengerEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test withdrawRide where ride is in PENDING or ACCEPTED status")
    public void testWithdrawRide_PendingOrAcceptedRide() {
        loginPassenger();
        ResponseEntity<RideRetDTO> responseEntity = restTemplate.exchange(BASE_PATH+"/"+6+"/withdraw",
                HttpMethod.PUT,
                new HttpEntity<>(passengerEntity.getHeaders()),
                RideRetDTO.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


    @Test
    @DisplayName("Test activatePanic where user role is neither DRIVER nor PASSENGER")
    public void testActivatePanic_NonDriverNonPassengerUser() {
        loginAdmin();
        ReasonDTO reasonDTO = new ReasonDTO();
        reasonDTO.setReason("reason");
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/"+1+"/panic",
                HttpMethod.PUT,
                new HttpEntity<>(reasonDTO, adminEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test activatePanic where ride doesn't exist")
    public void testActivatePanic_NonexistentRide() {
        loginPassenger();
        ReasonDTO reasonDTO = new ReasonDTO();
        reasonDTO.setReason("reason");
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/"+100000+"/panic",
                HttpMethod.PUT,
                new HttpEntity<>(reasonDTO, passengerEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test activatePanic where user doesn't have permission to activate panic")
    public void testActivatePanic_NoPermissionToActivate() {
        loginAdmin();
        ReasonDTO reasonDTO = new ReasonDTO();
        reasonDTO.setReason("reason");
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/"+1+"/panic",
                HttpMethod.PUT,
                new HttpEntity<>(reasonDTO, adminEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test activatePanic where ride is not in STARTED status")
    public void testActivatePanic_NotStartedRide() {
        loginDriverWithoutRide();
        ReasonDTO reasonDTO = new ReasonDTO();
        reasonDTO.setReason("reason");
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/"+4+"/panic",
                HttpMethod.PUT,
                new HttpEntity<>(reasonDTO, driverEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test activatePanic where ride is in STARTED status")
    public void testActivatePanic_StartedRide() {
        loginDriverWithRide();
        ReasonDTO reasonDTO = new ReasonDTO();
        reasonDTO.setReason("reason");
        ResponseEntity<PanicDTO> responseEntity = restTemplate.exchange(BASE_PATH+"/"+1+"/panic",
                HttpMethod.PUT,
                new HttpEntity<>(reasonDTO, driverEntity.getHeaders()),
                PanicDTO.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test acceptRide where user role is not a DRIVER")
    public void testAcceptRide_NonDriverUser() {
        loginPassenger();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/"+3+"/accept",
                HttpMethod.PUT,
                new HttpEntity<>(passengerEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test acceptRide where ride doesn't exist")
    public void testAcceptRide_NonexistentRide() {
        loginDriverWithoutRide();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/"+310000+"/accept",
                HttpMethod.PUT,
                new HttpEntity<>(driverEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test acceptRide where driver doesn't have permission to accept the ride")
    public void testAcceptRide_NoPermissionToAccept() {
        loginDriverWithRide();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/"+4+"/accept",
                HttpMethod.PUT,
                new HttpEntity<>(driverEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test acceptRide where ride is not in PENDING status")
    public void testAcceptRide_NotPendingRide() {
        loginDriverWithRide();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/"+1+"/accept",
                HttpMethod.PUT,
                new HttpEntity<>(driverEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test acceptRide where ride is in PENDING status")
    public void testAcceptRide_PendingRide() {
        loginDriverWithoutRide();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/"+5+"/accept",
                HttpMethod.PUT,
                new HttpEntity<>(driverEntity.getHeaders()),
                String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test startRide where user role is not a PASSENGER")
    public void testStartRide_NonPassengerUser() {
        loginAdmin();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/"+1+"/start",
                HttpMethod.PUT,
                new HttpEntity<>(adminEntity.getHeaders()),
                String.class);
        System.out.println(responseEntity.getBody());
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test startRide where ride doesn't exist")
    public void testStartRide_NonexistentRide() {
        loginPassenger();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/"+1+"/start",
                HttpMethod.PUT,
                new HttpEntity<>(passengerEntity.getHeaders()),
                String.class);
        System.out.println(responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test startRide where passenger doesn't have permission to start the ride")
    public void testStartRide_NoPermissionToStart() {
        loginPassengerWithRide();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/"+4+"/start",
                HttpMethod.PUT,
                new HttpEntity<>(passengerEntity.getHeaders()),
                String.class);
        System.out.println(responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test startRide where ride is not in ACCEPTED status")
    public void testStartRide_NotAcceptedRide() {
        loginPassenger();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/"+4+"/start",
                HttpMethod.PUT,
                new HttpEntity<>(passengerEntity.getHeaders()),
                String.class);
        System.out.println(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test startRide where ride is in ACCEPTED status")
    public void testStartRide_AcceptedRide() {
        loginPassenger();
        ResponseEntity<RideRetDTO> responseEntity = restTemplate.exchange(BASE_PATH+"/"+6+"/start",
                HttpMethod.PUT,
                new HttpEntity<>(passengerEntity.getHeaders()),
                RideRetDTO.class);
        System.out.println(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test routeRide where user role is not a PASSENGER")
    public void testRouteRide_NonPassengerUser() {
        loginAdmin();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/"+6+"/route",
                HttpMethod.PUT,
                new HttpEntity<>(adminEntity.getHeaders()),
                String.class);
        System.out.println(responseEntity.getBody());
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test routeRide where ride doesn't exist")
    public void testRouteRide_NonexistentRide() {
        loginPassenger();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/"+100+"/route",
                HttpMethod.PUT,
                new HttpEntity<>(passengerEntity.getHeaders()),
                String.class);
        System.out.println(responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test routeRide where passenger doesn't have permission to route the ride")
    public void testRouteRide_NoPermissionToRoute() {
        loginPassenger();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/"+1+"/route",
                HttpMethod.PUT,
                new HttpEntity<>(passengerEntity.getHeaders()),
                String.class);
        System.out.println(responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test routeRide where ride is not in STARTED status")
    public void testRouteRide_NotStartedRide() {
        loginPassenger();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/"+6+"/route",
                HttpMethod.PUT,
                new HttpEntity<>(passengerEntity.getHeaders()),
                String.class);
        System.out.println(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }


    @Test
    @DisplayName("Test endRide where user role is not a DRIVER")
    public void testEndRide_NonDriverUser() {
        loginAdmin();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/"+6+"/end",
                HttpMethod.PUT,
                new HttpEntity<>(adminEntity.getHeaders()),
                String.class);
        System.out.println(responseEntity.getBody());
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test endRide where ride doesn't exist")
    public void testEndRide_NonexistentRide() {
        loginDriverWithRide();
        ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_PATH+"/"+9000+"/end",
                HttpMethod.PUT,
                new HttpEntity<>(driverEntity.getHeaders()),
                String.class);
        System.out.println(responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }


}