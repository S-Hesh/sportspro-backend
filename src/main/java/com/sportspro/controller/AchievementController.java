package com.sportspro.controller;

import com.sportspro.dto.AchievementDTO;
import com.sportspro.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achievements")
public class AchievementController {

    @Autowired
    private AchievementService achievementService;

    // Add a new achievement
    @PostMapping("/add")
    public ResponseEntity<AchievementDTO> addAchievement(@RequestBody AchievementDTO achievementDTO) {
        AchievementDTO createdAchievement = achievementService.addAchievement(achievementDTO);
        return ResponseEntity.ok(createdAchievement);
    }

    // Get achievements by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AchievementDTO>> getAchievementsByUserId(@PathVariable Long userId) {
        List<AchievementDTO> achievements = achievementService.getAchievementsByUserId(userId);
        return ResponseEntity.ok(achievements);
    }
}

