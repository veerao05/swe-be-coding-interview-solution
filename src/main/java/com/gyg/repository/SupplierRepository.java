package com.gyg.repository;

import com.gyg.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    @Query("""
                 select s from Supplier s where
                 lower(s.name) like lower(concat('%',:search,'%')) OR
                 lower(s.address) like lower(concat('%',:search,'%')) OR
                 lower(s.city) like lower(concat('%',:search,'%')) OR
                 lower(s.country) like lower(concat('%',:search,'%'))
            """)
    List<Supplier> searchSupplier(@Param("search") String search);
}
