package com.sportspro.repository;

import com.sportspro.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    // Custom query to find achievements by userId
    List<Achievement> findByUserUserId(Long userId);
}
