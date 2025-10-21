package com.sportspro.mapper;

import com.sportspro.dto.AchievementDTO;
import com.sportspro.model.Achievement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AchievementMapper {

    AchievementDTO achievementToAchievementDTO(Achievement achievement);

    Achievement achievementDTOToAchievement(AchievementDTO achievementDTO);
}
