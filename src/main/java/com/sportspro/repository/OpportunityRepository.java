package com.sportspro.repository;

import com.sportspro.model.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {

    // Existing
    List<Opportunity> findBySport(String sport);
    List<Opportunity> findByLocation(String location);
    List<Opportunity> findBySportAndLocation(String sport, String location);

    // Java 11–safe JPQL
    @Query("SELECT o FROM Opportunity o " +
            "WHERE LOWER(o.title)        LIKE LOWER(CONCAT('%', :q, '%')) " +
            "   OR LOWER(o.company)      LIKE LOWER(CONCAT('%', :q, '%')) " +
            "   OR LOWER(o.location)     LIKE LOWER(CONCAT('%', :q, '%')) " +
            "   OR LOWER(o.type)         LIKE LOWER(CONCAT('%', :q, '%')) " +
            "   OR LOWER(o.sport)        LIKE LOWER(CONCAT('%', :q, '%')) " +
            "   OR LOWER(o.description)  LIKE LOWER(CONCAT('%', :q, '%')) " +
            "   OR LOWER(o.requirements) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Opportunity> searchBasic(@Param("q") String q);
}
