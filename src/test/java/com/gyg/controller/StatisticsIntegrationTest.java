package com.gyg.controller;

import com.gyg.entity.Supplier;
import com.gyg.repository.ActivityRepository;
import com.gyg.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.gyg.helpers.ActivityHelper.createActivity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
public class StatisticsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @BeforeEach
    void setup() {
        activityRepository.deleteAll();
        supplierRepository.deleteAll();

        var berlinSupplier = new Supplier();
        berlinSupplier.setName("Berlin Supplier");
        berlinSupplier.setCity("Berlin");
        berlinSupplier.setCountry("Germany");
        supplierRepository.save(berlinSupplier);

        activityRepository.save(createActivity("Berlin City Tour", 50, 5.0, false, berlinSupplier));
        activityRepository.save(createActivity("Berlin Night Tour", 80, 4.5, false, berlinSupplier));

        var parisSupplier = new Supplier();
        parisSupplier.setName("Paris Supplier");
        parisSupplier.setCity("Paris");
        parisSupplier.setCountry("France");
        supplierRepository.save(parisSupplier);

        activityRepository.save(createActivity("Paris City Tour", 150, 8.0, false, parisSupplier));
    }

    // ── GET /stats/suppliers ──────────────────────────────────────────────────

    @Test
    void testGetSupplierStats_returns200() throws Exception {
        mockMvc.perform(get("/stats/suppliers"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetSupplierStats_returnsTwoSuppliers() throws Exception {
        mockMvc.perform(get("/stats/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetSupplierStats_emptyDb_returnsEmptyList() throws Exception {
        activityRepository.deleteAll();
        supplierRepository.deleteAll();

        mockMvc.perform(get("/stats/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}