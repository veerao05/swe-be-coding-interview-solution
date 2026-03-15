package com.gyg.repository;

import com.gyg.entity.Supplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static com.gyg.helpers.ActivityHelper.createActivity;

@DataJpaTest(properties = {
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StatisticsRepositoryTest {

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Supplier berlinSupplier;
    private Supplier parisSupplier;

    @BeforeEach
    void setup() {
        berlinSupplier = new Supplier();
        berlinSupplier.setName("Berlin Supplier");
        berlinSupplier.setCity("Berlin");
        berlinSupplier.setCountry("Germany");
        entityManager.persist(berlinSupplier);

        parisSupplier = new Supplier();
        parisSupplier.setName("Paris Supplier");
        parisSupplier.setCity("Paris");
        parisSupplier.setCountry("France");
        entityManager.persist(parisSupplier);

        entityManager.persist(createActivity("Berlin City Tour", 50, 5.0, false, berlinSupplier));
        entityManager.persist(createActivity("Berlin Night Tour", 80, 4.5, false, berlinSupplier));
        entityManager.persist(createActivity("Paris City Tour", 150, 8.0, false, parisSupplier));

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testGetSupplierStats_returnsResults() {
        var result = statisticsRepository.getSupplierStats();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void testGetSupplierStats_orderedByTotalRevenue() {
        // Paris (150) > Berlin (50+80=130), so Paris should be first
        var result = statisticsRepository.getSupplierStats();
        Assertions.assertEquals("Paris Supplier", result.get(0).getSupplierName());
        Assertions.assertEquals("Berlin Supplier", result.get(1).getSupplierName());
    }

    @Test
    void testGetSupplierStats_supplierWithNoActivities_stillAppearsViaLeftJoin() {
        var tokyoSupplier = new Supplier();
        tokyoSupplier.setName("Tokyo Supplier");
        tokyoSupplier.setCity("Tokyo");
        tokyoSupplier.setCountry("Japan");
        entityManager.persist(tokyoSupplier);
        entityManager.flush();
        entityManager.clear();

        var result = statisticsRepository.getSupplierStats();
        Assertions.assertEquals(3, result.size());
    }

    @Test
    void testGetSupplierStats_emptyDb_returnsEmptyList() {
        activityRepository.deleteAll();
        statisticsRepository.deleteAll();
        entityManager.flush();

        var result = statisticsRepository.getSupplierStats();
        Assertions.assertTrue(result.isEmpty());
    }
}