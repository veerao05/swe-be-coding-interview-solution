package com.gyg.controller;

import com.gyg.dto.SupplierDto;
import com.gyg.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/* 🚨 Issue 1: @Controller instead of @RestController

@Controller is for MVC views (returns HTML). For a REST API that returns JSON,
@RestController is required — it adds @ResponseBody automatically.
Without it, Spring tries to resolve a view named "suppliers" and returns 404/500.

*/

//@Controller

@Slf4j
@RestController
@RequiredArgsConstructor
public class SupplierController {

    /*🚨 Issue 2: EntityManager injected directly into the controller
    Controllers must not contain data access logic. This violates layered architecture:
    Controller → Service → Repository.
    The EntityManager belongs in the repository layer.

   🚨 Issue 3: Field injection with @PersistenceContext
   Even if EntityManager were appropriate here, field injection makes the class
   harder to test. Constructor injection (via @AllArgsConstructor / @RequiredArgsConstructor)
   is the recommended approach.
   @PersistenceContext
   private EntityManager entityManager;
    */
    private final SupplierService supplierService;

    @GetMapping("/suppliers")
    public ResponseEntity<List<SupplierDto>> suppliers() {

        /*🚨 Issue 4: Native SQL query with hardcoded schema name
         "SELECT * FROM GETYOURGUIDE.SUPPLIER" bypasses JPA/Hibernate portability.
         The schema name is hardcoded — breaks if the schema changes.
         Should use a SupplierRepository (JpaRepository) and delegate to a SupplierService.
        var list = (List<Supplier>) entityManager.createNativeQuery("SELECT * FROM GETYOURGUIDE.SUPPLIER", Supplier.class).getResultList();
        return ResponseEntity.ok(list);
         */

        List<SupplierDto> suppliers = supplierService.AllSuppliers();
        return ResponseEntity.ok(suppliers);
    }


    @GetMapping("/suppliers/search/{search}")
    public ResponseEntity<List<SupplierDto>> suppliersSearch(@PathVariable String search) {

            /*🚨 Issue 5: Fetches ALL suppliers from DB, then filters in Java
            This is a full table scan on every search request. Filtering should happen
            in the database via a WHERE clause (JPQL or derived query), not in Java.
            var list = (List<Supplier>) entityManager.createNativeQuery("SELECT * FROM GETYOURGUIDE.SUPPLIER", Supplier.class).getResultList();

            for (Supplier s : list) {
                if (new StringBuilder().append(s.getName()).append(s.getAddress()).append(s.getZip()).append(s.getCity()).append(s.getCountry()).toString().contains(search)) {
                    🚨 Issue 6: Returns only the FIRST match and exits immediately
                    If multiple suppliers match the search term, only one is returned.
                    A search API should return ALL matching results, not just the first.
                    return ResponseEntity.ok(List.of(s));
                }
            }

            🚨 Issue 7: Returns the FULL list when no match is found
            When no supplier matches the search, the fallback returns ALL suppliers.
            It should return an empty list to indicate no results were found.
            return ResponseEntity.ok(list);
             */
        log.info("********** supplier search ***********");
        List<SupplierDto> suppliers = supplierService.searchSuppliers(search);
        log.info("size:: {}", suppliers.size());
        return ResponseEntity.ok(suppliers);

    }
}
