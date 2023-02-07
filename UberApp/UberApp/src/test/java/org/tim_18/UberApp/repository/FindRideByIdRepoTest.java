package org.tim_18.UberApp.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.tim_18.UberApp.model.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class FindRideByIdRepoTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RideRepository rideRepository;



    @Test
    public void whenFindById_thenReturnRide() {
        Ride newRide = new Ride();
        entityManager.persist(newRide);
        entityManager.flush();

        Optional<Ride> found = rideRepository.findRideById(newRide.getId());
        assertTrue(found.isPresent());
    }

}
