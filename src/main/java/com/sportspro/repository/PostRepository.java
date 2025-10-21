package com.sportspro.repository;

import com.sportspro.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Existing
    List<Post> findByUserUserId(Long userId);
    List<Post> findAllByOrderByCreatedAtDesc();

    // Java 11–safe JPQL
    @Query("SELECT p FROM Post p " +
            "WHERE LOWER(p.content)   LIKE LOWER(CONCAT('%', :q, '%')) " +
            "   OR LOWER(p.tags)      LIKE LOWER(CONCAT('%', :q, '%')) " +
            "   OR LOWER(p.user.name) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Post> searchBasic(@Param("q") String q);
}
