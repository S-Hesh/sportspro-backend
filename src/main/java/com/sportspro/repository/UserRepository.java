package com.sportspro.repository;

import com.sportspro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Existing
    User findByEmail(String email);
    List<User> findByUserIdNot(Long userId);

    // Java 11–safe JPQL (single-line string)
    @Query("SELECT u FROM User u " +
            "WHERE LOWER(u.name)     LIKE LOWER(CONCAT('%', :q, '%')) " +
            "   OR LOWER(u.bio)      LIKE LOWER(CONCAT('%', :q, '%')) " +
            "   OR LOWER(u.location) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "   OR LOWER(u.sport)    LIKE LOWER(CONCAT('%', :q, '%'))")
    List<User> searchBasic(@Param("q") String q);
}
