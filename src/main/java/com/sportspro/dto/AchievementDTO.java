package com.sportspro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchievementDTO {

    private Long achievementId;
    private String title;
    private String description;
    private String sport;
    private Integer year;
}
