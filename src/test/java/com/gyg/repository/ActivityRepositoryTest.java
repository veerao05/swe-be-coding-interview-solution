package com.gyg.repository;

import com.gyg.entity.Activity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static com.gyg.helpers.ActivityHelper.createActivity;
import static com.gyg.helpers.SupplierHelper.createSupplier;

@DataJpaTest(properties = {
        "spring.sql.init.mode=never",           // stop migration SQL files from running
        "spring.jpa.hibernate.ddl-auto=create"  // let Hibernate create the tables (no drop needed for in-memory H2)
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class ActivityRepositoryTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Activity berlinActivity;
    private Activity parisActivity;

    @BeforeEach
    void setup() {
        berlinActivity = createActivity(
                "Berlin City Tour",
                50,
                5.0,
                false,
                createSupplier("Berlin Supplier"));
        activityRepository.save(berlinActivity);

        parisActivity = createActivity(
                "Paris City Tour",
                150,
                8.0,
                false,
                createSupplier("Paris Supplier"));

        activityRepository.save(parisActivity);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testFindAllActivities() {
        var result = activityRepository.findAll();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void testNoActivities() {
        activityRepository.deleteAll();
        entityManager.flush();
        var result = activityRepository.findAll();
        Assertions.assertTrue(result.isEmpty());
    }

    //findById
    @Test
    void testFindByIdFound() {
        var result = activityRepository.findById(berlinActivity.getId());
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Berlin City Tour", result.get().getTitle());
    }

    //findById
    @Test
    void testFindByIdNotFound() {
        var result = activityRepository.findById(9999L);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void testFindTitle() {
        var result = activityRepository.findTitle("Berlin City Tour");
        Assertions.assertEquals("Berlin City Tour", result.get(0).getTitle());

    }

    @Test
    void testFindTitleNotFound() {
        var result = activityRepository.findTitle("berlin City Tour");
        Assertions.assertEquals("Berlin City Tour", result.get(0).getTitle());

    }

    @Test
    void testFindTitlePartialFound() {
        var result = activityRepository.findTitle(" City Tour");
        Assertions.assertEquals(2, result.size());

    }

    @Test
    void testFindTitleNoMatchFound() {
        var result = activityRepository.findTitle("Rome Tour");
        Assertions.assertTrue(result.isEmpty());

    }


}
