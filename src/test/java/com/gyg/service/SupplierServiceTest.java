package com.gyg.service;

import com.gyg.entity.Supplier;
import com.gyg.repository.SupplierRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gyg.helpers.ActivityHelper.createActivity;
import static com.gyg.helpers.SupplierHelper.createSupplier;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SupplierServiceTest {

    private SupplierRepository supplierRepository;
    private ActivityService activityService;
    private SupplierService supplierService;

    @BeforeEach
    void setup() {
        supplierRepository = mock(SupplierRepository.class);
        activityService = mock(ActivityService.class);
        supplierService = new SupplierService(supplierRepository, activityService);
    }

    private Supplier fullSupplier(String name) {
        var supplier = createSupplier(name);
        supplier.setAddress("Main St 1");
        supplier.setZip("10001");
        supplier.setCity("Berlin");
        supplier.setCountry("Germany");
        return supplier;
    }

    // ── AllSuppliers ──────────────────────────────────────────────────────────

    @Test
    void testAllSuppliers_returnsList() {
        when(supplierRepository.findAll()).thenReturn(List.of(fullSupplier("Berlin Supplier")));

        var result = supplierService.AllSuppliers();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Berlin Supplier", result.get(0).getName());
        Assertions.assertEquals("Berlin", result.get(0).getCity());
        Assertions.assertEquals("Germany", result.get(0).getCountry());
    }

    @Test
    void testAllSuppliers_emptyList() {
        when(supplierRepository.findAll()).thenReturn(List.of());

        var result = supplierService.AllSuppliers();

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testAllSuppliers_mapsActivitiesToDtos() {
        var supplier = fullSupplier("Berlin Supplier");
        var activity = createActivity("Berlin City Tour", 50, 5.0, false, supplier);
        supplier.setActivities(List.of(activity));

        when(supplierRepository.findAll()).thenReturn(List.of(supplier));

        var result = supplierService.AllSuppliers();

        Assertions.assertEquals(1, result.get(0).getActivities().size());
        Assertions.assertEquals("Berlin City Tour", result.get(0).getActivities().get(0).getTitle());
        Assertions.assertEquals(50, result.get(0).getActivities().get(0).getPrice());
    }

    @Test
    void testAllSuppliers_nullActivities_returnsEmptyList() {
        var supplier = fullSupplier("Berlin Supplier");
        supplier.setActivities(null);

        when(supplierRepository.findAll()).thenReturn(List.of(supplier));

        var result = supplierService.AllSuppliers();

        Assertions.assertNotNull(result.get(0).getActivities());
        Assertions.assertTrue(result.get(0).getActivities().isEmpty());
    }

    // ── searchSuppliers ───────────────────────────────────────────────────────

    @Test
    void testSearchSuppliers_found() {
        when(supplierRepository.searchSupplier("Berlin"))
                .thenReturn(List.of(fullSupplier("Berlin Supplier")));

        var result = supplierService.searchSuppliers("Berlin");

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Berlin Supplier", result.get(0).getName());
    }

    @Test
    void testSearchSuppliers_noMatch_returnsEmptyList() {
        when(supplierRepository.searchSupplier("Tokyo")).thenReturn(List.of());

        var result = supplierService.searchSuppliers("Tokyo");

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testSearchSuppliers_multipleResults() {
        when(supplierRepository.searchSupplier("Supplier"))
                .thenReturn(List.of(fullSupplier("Berlin Supplier"), fullSupplier("Paris Supplier")));

        var result = supplierService.searchSuppliers("Supplier");

        Assertions.assertEquals(2, result.size());
    }
}