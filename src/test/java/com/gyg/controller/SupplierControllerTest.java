package com.gyg.controller;

import com.gyg.dto.ActivityDto;
import com.gyg.dto.SupplierDto;
import com.gyg.service.SupplierService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SupplierController.class)
public class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SupplierService supplierService;

    // ── helpers ───────────────────────────────────────────────────────────────

    private SupplierDto berlinSupplierDto() {
        return SupplierDto.builder()
                .id(1L)
                .name("Berlin Supplier")
                .address("Unter den Linden 1")
                .zip("10117")
                .city("Berlin")
                .country("Germany")
                .activities(List.of(ActivityDto.builder()
                        .id(1L).title("Berlin City Tour").price(50)
                        .currency("$").rating(5.0).specialOffer(false)
                        .supplierName("Berlin Supplier").build()))
                .build();
    }

    private SupplierDto parisSupplierDto() {
        return SupplierDto.builder()
                .id(2L)
                .name("Paris Supplier")
                .address("Champs-Élysées 1")
                .zip("75008")
                .city("Paris")
                .country("France")
                .activities(List.of())
                .build();
    }

    // ── GET /suppliers ────────────────────────────────────────────────────────

    @Test
    void testGetAllSuppliers_returns200WithList() throws Exception {
        given(supplierService.AllSuppliers()).willReturn(List.of(berlinSupplierDto(), parisSupplierDto()));

        mockMvc.perform(get("/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Berlin Supplier"))
                .andExpect(jsonPath("$[0].city").value("Berlin"))
                .andExpect(jsonPath("$[0].activities[0].title").value("Berlin City Tour"));
    }

    @Test
    void testGetAllSuppliers_emptyList_returns200() throws Exception {
        given(supplierService.AllSuppliers()).willReturn(List.of());

        mockMvc.perform(get("/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ── GET /suppliers/search/{search} ────────────────────────────────────────

    @Test
    void testSearchSuppliers_found_returns200() throws Exception {
        given(supplierService.searchSuppliers("Berlin")).willReturn(List.of(berlinSupplierDto()));

        mockMvc.perform(get("/suppliers/search/Berlin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Berlin Supplier"));
    }

    @Test
    void testSearchSuppliers_noResults_returnsEmptyList() throws Exception {
        given(supplierService.searchSuppliers("Tokyo")).willReturn(List.of());

        mockMvc.perform(get("/suppliers/search/Tokyo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testSearchSuppliers_byCity_returns200() throws Exception {
        given(supplierService.searchSuppliers("Paris")).willReturn(List.of(parisSupplierDto()));

        mockMvc.perform(get("/suppliers/search/Paris"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city").value("Paris"));
    }

    @Test
    void testSearchSuppliers_multipleResults_returns200() throws Exception {
        given(supplierService.searchSuppliers("Supplier"))
                .willReturn(List.of(berlinSupplierDto(), parisSupplierDto()));

        mockMvc.perform(get("/suppliers/search/Supplier"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}