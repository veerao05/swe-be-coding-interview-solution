package com.gyg.repository;

import com.gyg.dto.SupplierStats;
import com.gyg.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<Supplier, Long> {
    /*String SUPPLIER_STATS_QUERY = """
        SELECT s.* FROM getyourguide.supplier s
        """;

    @Query(value = SUPPLIER_STATS_QUERY, nativeQuery = true)*/
    @Query("""
                 select s.id , s.name , count(a.id), sum(a.price) , avg(a.rating) 
                 from Supplier s left join s.activities a
                 group by s.id,s.name
                 order by sum(a.price) desc
            
            """)
    List<SupplierStats> getSupplierStats();


}
