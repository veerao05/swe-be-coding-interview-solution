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
public class SupplierIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ActivityRepository activityRepository;

    private Supplier berlinSupplier;

    @BeforeEach
    void setup() {
        activityRepository.deleteAll();
        supplierRepository.deleteAll();

        berlinSupplier = new Supplier();
        berlinSupplier.setName("Berlin Supplier");
        berlinSupplier.setAddress("Unter den Linden 1");
        berlinSupplier.setZip("10117");
        berlinSupplier.setCity("Berlin");
        berlinSupplier.setCountry("Germany");
        supplierRepository.save(berlinSupplier);

        var parisSupplier = new Supplier();
        parisSupplier.setName("Paris Supplier");
        parisSupplier.setAddress("Champs-Élysées 1");
        parisSupplier.setZip("75008");
        parisSupplier.setCity("Paris");
        parisSupplier.setCountry("France");
        supplierRepository.save(parisSupplier);
    }

    // ── GET /suppliers ────────────────────────────────────────────────────────

    @Test
    void testGetAllSuppliers_returns200WithAllSuppliers() throws Exception {
        mockMvc.perform(get("/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Berlin Supplier"));
    }

    @Test
    void testGetAllSuppliers_includesActivities() throws Exception {
        activityRepository.save(createActivity("Berlin City Tour", 50, 5.0, false, berlinSupplier));

        mockMvc.perform(get("/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].activities[0].title").value("Berlin City Tour"));
    }

    // ── GET /suppliers/search/{search} ────────────────────────────────────────

    @Test
    void testSearchSuppliers_byName_returns200() throws Exception {
        mockMvc.perform(get("/suppliers/search/Berlin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Berlin Supplier"));
    }

    @Test
    void testSearchSuppliers_byCity_returns200() throws Exception {
        mockMvc.perform(get("/suppliers/search/Paris"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city").value("Paris"));
    }

    @Test
    void testSearchSuppliers_caseInsensitive() throws Exception {
        mockMvc.perform(get("/suppliers/search/berlin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testSearchSuppliers_noMatch_returnsEmptyList() throws Exception {
        mockMvc.perform(get("/suppliers/search/Tokyo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}