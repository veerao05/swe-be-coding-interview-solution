package com.gyg.repository;

import com.gyg.entity.Supplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

@DataJpaTest(properties = {
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SupplierRepositoryTest {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Supplier berlinSupplier;
    private Supplier parisSupplier;

    @BeforeEach
    void setup() {
        berlinSupplier = new Supplier();
        berlinSupplier.setName("Berlin Supplier");
        berlinSupplier.setAddress("Unter den Linden 1");
        berlinSupplier.setZip("10117");
        berlinSupplier.setCity("Berlin");
        berlinSupplier.setCountry("Germany");
        supplierRepository.save(berlinSupplier);

        parisSupplier = new Supplier();
        parisSupplier.setName("Paris Supplier");
        parisSupplier.setAddress("Champs-Élysées 1");
        parisSupplier.setZip("75008");
        parisSupplier.setCity("Paris");
        parisSupplier.setCountry("France");
        supplierRepository.save(parisSupplier);

        entityManager.flush();
        entityManager.clear();
    }

    // ── findAll ───────────────────────────────────────────────────────────────

    @Test
    void testFindAll_returnsAllSuppliers() {
        var result = supplierRepository.findAll();
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void testFindAll_emptyAfterDeleteAll() {
        supplierRepository.deleteAll();
        entityManager.flush();
        Assertions.assertTrue(supplierRepository.findAll().isEmpty());
    }

    // ── findById ──────────────────────────────────────────────────────────────

    @Test
    void testFindById_found() {
        var result = supplierRepository.findById(berlinSupplier.getId());
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Berlin Supplier", result.get().getName());
    }

    @Test
    void testFindById_notFound() {
        var result = supplierRepository.findById(9999L);
        Assertions.assertFalse(result.isPresent());
    }

    // ── save & update ─────────────────────────────────────────────────────────

    @Test
    void testSaveSupplier() {
        var newSupplier = new Supplier();
        newSupplier.setName("Tokyo Supplier");
        newSupplier.setCity("Tokyo");
        newSupplier.setCountry("Japan");
        var saved = supplierRepository.save(newSupplier);
        entityManager.flush();
        entityManager.clear();

        var found = supplierRepository.findById(saved.getId());
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("Tokyo Supplier", found.get().getName());
    }

    @Test
    void testUpdateSupplier() {
        berlinSupplier.setCity("Munich");
        supplierRepository.save(berlinSupplier);
        entityManager.flush();
        entityManager.clear();

        var updated = supplierRepository.findById(berlinSupplier.getId());
        Assertions.assertEquals("Munich", updated.get().getCity());
    }

    // ── searchSupplier ────────────────────────────────────────────────────────

    @Test
    void testSearchSupplier_byName() {
        var result = supplierRepository.searchSupplier("Berlin Supplier");
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Berlin Supplier", result.get(0).getName());
    }

    @Test
    void testSearchSupplier_byCity() {
        var result = supplierRepository.searchSupplier("Paris");
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Paris Supplier", result.get(0).getName());
    }

    @Test
    void testSearchSupplier_byCountry() {
        var result = supplierRepository.searchSupplier("Germany");
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Berlin Supplier", result.get(0).getName());
    }

    @Test
    void testSearchSupplier_byAddress() {
        var result = supplierRepository.searchSupplier("Linden");
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Berlin Supplier", result.get(0).getName());
    }

    @Test
    void testSearchSupplier_caseInsensitive() {
        var result = supplierRepository.searchSupplier("berlin");
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void testSearchSupplier_partialMatchReturnsMultiple() {
        var result = supplierRepository.searchSupplier("Supplier");
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void testSearchSupplier_noMatch_returnsEmpty() {
        var result = supplierRepository.searchSupplier("Tokyo");
        Assertions.assertTrue(result.isEmpty());
    }
}