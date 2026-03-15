package com.gyg;

import com.gyg.entity.Activity;
import com.gyg.repository.ActivityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static com.gyg.helpers.ActivityHelper.createActivity;
import static com.gyg.helpers.SupplierHelper.createSupplier;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
public class ActivityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ActivityRepository activityRepository;

    private Activity berlinActivity;
    private Activity parisActivity;

    @BeforeEach
    void setup() {
        activityRepository.deleteAll();
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
    }

    @Test
    void testGetActivitiesPagedData() throws Exception {

        mockMvc.perform(get("/activities").param("page", "0").param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    void testGetActivities_defaultData() throws Exception {

        mockMvc.perform(get("/activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void testGetActivities_sizecapOf5() throws Exception {

        mockMvc.perform(get("/activities").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(5));
    }

    // ── GET /activities/{id} ──────────────────────────────────────────────────
    @Test
    void testGetActivityById_found_returns200() throws Exception {
        mockMvc.perform(get("/activities/" + berlinActivity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Berlin City Tour"))
                .andExpect(jsonPath("$.price").value("50"))
                .andExpect(jsonPath("$.supplierName").value("Berlin Supplier"));
    }

    @Test
    void testGetActivityById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/activities/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetActivityById_negative_returns400() throws Exception {
        mockMvc.perform(get("/activities/-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetActivityById_zero_returns400() throws Exception {
        mockMvc.perform(get("/activities/0"))
                .andExpect(status().isBadRequest());
    }

    // ── GET /activities/search/{search} ───────────────────────────────────────
    @Test
    void testSearchActivities_exactMatch_returns200() throws Exception {
        mockMvc.perform(get("/activities/search/Berlin City Tour"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Berlin City Tour"));
    }

    @Test
    void testSearchActivities_returnsMultiple() throws Exception {
        mockMvc.perform(get("/activities/search/City"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testSearchActivities_caseInSensitive() throws Exception {
        mockMvc.perform(get("/activities/search/berlin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Berlin City Tour"));
    }

    @Test
    void testSearchActivities_noMatch() throws Exception {
        mockMvc.perform(get("/activities/search/xyz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value("0"));
    }
}
