package com.gyg.controller;

import com.gyg.dto.SupplierStats;
import com.gyg.service.StatisticsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StatisticsController.class)
public class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StatisticsService statisticsService;

    // ── GET /stats/suppliers ──────────────────────────────────────────────────

    @Test
    void testGetSupplierStats_returns200() throws Exception {
        var stat = mock(SupplierStats.class);
        given(statisticsService.getSupplierStats()).willReturn(List.of(stat));

        mockMvc.perform(get("/stats/suppliers"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetSupplierStats_emptyList_returns200() throws Exception {
        given(statisticsService.getSupplierStats()).willReturn(List.of());

        mockMvc.perform(get("/stats/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}