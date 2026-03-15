package com.gyg.controller;

import com.gyg.dto.ActivityDto;
import com.gyg.error.ActivityNotFoundException;
import com.gyg.service.ActivityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
//@AllArgsConstructor

@WebMvcTest(ActivityController.class)
class ActivitiesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    //@Autowired
    private ActivityService activityService;

    private ActivityDto berlinDto() {
        return ActivityDto.builder()
                .id(1L)
                .title("Berlin City Tour")
                .price(50)
                .currency("$")
                .rating(5.0)
                .specialOffer(false)
                .supplierName("Berlin Supplier")
                .build();
    }

    private ActivityDto parisDto() {
        return ActivityDto.builder()
                .id(2L)
                .title("Paris City Tour")
                .price(150)
                .currency("$")
                .rating(8.0)
                .specialOffer(false)
                .supplierName("Paris Supplier")
                .build();
    }

    @Test
    void testGetActivitiesDefaultParams() throws Exception {
        var page = new PageImpl<>(List.of(berlinDto()), PageRequest.of(0, 1), 2);
        when(activityService.getActivities(0, 1)).thenReturn(page);

        mockMvc.perform(get("/activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Berlin City Tour"))
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void testGetActivitiesForNegativePage() throws Exception {
        var page = new PageImpl<>(List.of(berlinDto()), PageRequest.of(0, 1), 2);
        when(activityService.getActivities(0, 1)).thenReturn(page);

        mockMvc.perform(get("/activities?page=-1&size=1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetActivities_sizeCapAt5() throws Exception {
        var page = new PageImpl<>(List.of(berlinDto()), PageRequest.of(0, 5), 6);
        when(activityService.getActivities(0, 5)).thenReturn(page);

        mockMvc.perform(get("/activities").param("page", "0").param("size", "10"))
                .andExpect(status().isOk());
        verify(activityService).getActivities(0, 5);

    }

    @Test
    void testGetActivities_empty() throws Exception {
        var emptyList = new PageImpl<ActivityDto>(List.of());
        when(activityService.getActivities(0, 1)).thenReturn(emptyList);

        mockMvc.perform(get("/activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    // ── GET /activities/{id} ──────────────────────────────────────────────────
    @Test
    void testGetActivitiesById_success() throws Exception {
        var berlin = berlinDto();
        when(activityService.getActivity(1L)).thenReturn(berlin);

        mockMvc.perform(get("/activities/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(berlin.getId()))
                .andExpect(jsonPath("$.title").value(berlin.getTitle()))
                .andExpect(jsonPath("$.price").value(berlin.getPrice()))
                .andExpect(jsonPath("$.supplierName").value(berlin.getSupplierName()));
    }

    @Test
    void testGetActivitiesById_notFound() throws Exception {
        when(activityService.getActivity(999L))
                .thenThrow(new ActivityNotFoundException("Activity Not Found"));

        mockMvc.perform(get("/activities/1")).andExpect(status().isNotFound());

    }

    @Test
    void testGetActivitiesById_badRequest() throws Exception {
        mockMvc.perform(get("/activities/-1")).andExpect(status().isBadRequest());

    }

    @Test
    void testGetActivitiesById_zeroActivity() throws Exception {
        when(activityService.getActivity(0L))
                .thenThrow(new ActivityNotFoundException("Activity Not Found"));

        mockMvc.perform(get("/activities/0")).andExpect(status().isNotFound());
    }

    // ── GET /activities/search/{search} ───────────────────────────────────────
    @Test
    void testSearchActivities_success() throws Exception {
        when(activityService.getSearchActivities("BERLIN")).thenReturn(List.of(berlinDto()));

        mockMvc.perform(get("/activities/search/Berlin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Berlin City Tour"))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testSearchActivities_notFound() throws Exception {
        when(activityService.getSearchActivities("XXXX")).thenReturn(List.of());

        mockMvc.perform(get("/activities/search/Berlin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testSearchActivities_multiple() throws Exception {
        when(activityService.getSearchActivities("City")).thenReturn(List.of(berlinDto(), parisDto()));

        mockMvc.perform(get("/activities/search/City"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length").value(2));
    }
}
