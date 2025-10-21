package com.sportspro.repository;

import com.sportspro.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    // Custom query to find skills by userId
    List<Skill> findByUserUserId(Long userId);
}
