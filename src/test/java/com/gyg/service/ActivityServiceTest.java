package com.gyg.service;

import com.gyg.entity.Activity;
import com.gyg.error.ActivityNotFoundException;
import com.gyg.repository.ActivityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.gyg.helpers.ActivityHelper.createActivity;
import static com.gyg.helpers.SupplierHelper.createSupplier;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ActivityServiceTest {
    private ActivityRepository activityRepository;
    //private SupplierController supplierController;
    private ActivityService activityService;

    @BeforeEach
    void setup() {
        this.activityRepository = mock(ActivityRepository.class);
        //this.supplierController = mock(SupplierController.class);
        this.activityService = new ActivityService(activityRepository);
    }

    @Test
    void testGetActivities() {
        var testActivity = createActivity("Test Activity", 100, 5.0, false, createSupplier("Test Supplier"));
        when(activityRepository.findAll()).thenReturn(List.of(testActivity));

        var result = activityService.getActivities();

        Assertions.assertNotNull(result);
        //New Additions
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Test Activity", result.get(0).getTitle());
        Assertions.assertEquals(100, result.get(0).getPrice());
        Assertions.assertEquals(5.0, result.get(0).getRating());
        Assertions.assertFalse(result.get(0).isSpecialOffer());
        Assertions.assertEquals("Test Supplier", result.get(0).getSupplierName());


    }

    @Test
    void testGetActivitiesWithNullSupplier() throws ActivityNotFoundException {
        var activity = createActivity("Sample Activity", 200, 10.0, false, null);

        when(activityRepository.findById(2L)).thenReturn(Optional.of(activity));

        var result = activityService.getActivity(2L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("", result.getSupplierName());

    }

    @Test
    void testGetSearchActivities() {

        //before
        var testActivity = createActivity("Test Activity", 100, 5.0, false, null);

        //when
        //when(activityRepository.findAll()).thenReturn(List.of(testActivity));
        when(activityRepository.findTitle("Test")).thenReturn(List.of(testActivity));
        var result = activityService.getSearchActivities("Test");

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Test Activity", result.get(0).getTitle());
        Assertions.assertEquals(100, result.get(0).getPrice());
        Assertions.assertEquals(5.0, result.get(0).getRating());
        Assertions.assertFalse(result.get(0).isSpecialOffer());
        Assertions.assertEquals("", result.get(0).getSupplierName());
    }

    @Test
    void testGetSearchActivitiesNoMatch() {

        when(activityRepository.findTitle("xyz")).thenReturn(List.of());
        var result = activityService.getSearchActivities("xyz");

        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetActivityById() throws ActivityNotFoundException {
        var activity = createActivity("Activity test", 150, 6.0, false, createSupplier("supplier A"));

        //when
        when(activityRepository.findById(3L)).thenReturn(Optional.of(activity));

        var result = activityService.getActivity(3L);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(activity.getId(), result.getId());

    }

    @Test
    void testGetActivityByIdNotFound() {

        //when
        when(activityRepository.findById(30L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ActivityNotFoundException.class, () -> activityService.getActivity(30L));

    }

    @Test
    void testActivityPagination() {

        var activity = createActivity("Activity test", 150, 6.0, false, createSupplier("supplier A"));

        Page<Activity> page = new PageImpl<>(List.of(activity));

        //when - very important - pageable
        when(activityRepository.findAll(any(Pageable.class))).thenReturn(page);

        var results = activityService.getActivities(1, 1);

        Assertions.assertNotNull(results);
        Assertions.assertEquals(1, results.getTotalElements());
        Assertions.assertEquals(activity.getTitle(), results.getContent().get(0).getTitle());

    }

    @Test
    void testActivityNegative() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> activityService.getActivities(-1, 1));
    }

    @Test
    void testActivityZero() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> activityService.getActivities(0, 0));
    }
}
