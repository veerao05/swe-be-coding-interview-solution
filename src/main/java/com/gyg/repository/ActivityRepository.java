package com.gyg.repository;

import com.gyg.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    @Query("""
                 select a from Activity a
                 left join fetch a.supplier
            """)
    List<Activity> findAllWithSuppliers();

    @Query("""
                 select a from Activity a
                 where lower(a.title) like
                 lower(concat('%',:search,'%'))
            """)
    List<Activity> findTitle(@Param("search") String search);
}
