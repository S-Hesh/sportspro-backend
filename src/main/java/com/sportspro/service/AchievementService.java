package com.sportspro.service;

import com.sportspro.dto.AchievementDTO;
import com.sportspro.mapper.AchievementMapper;
import com.sportspro.model.Achievement;
import com.sportspro.repository.AchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private AchievementMapper achievementMapper;

    // Add a new achievement
    public AchievementDTO addAchievement(AchievementDTO achievementDTO) {
        Achievement achievement = achievementMapper.achievementDTOToAchievement(achievementDTO); // Convert DTO to entity
        achievement = achievementRepository.save(achievement);  // Save the achievement
        return achievementMapper.achievementToAchievementDTO(achievement); // Return the saved achievement as DTO
    }

    // Get achievements by userId
    public List<AchievementDTO> getAchievementsByUserId(Long userId) {
        List<Achievement> achievements = achievementRepository.findByUserUserId(userId);
        return achievements.stream()
                .map(achievementMapper::achievementToAchievementDTO)
                .collect(Collectors.toList());
    }
}

