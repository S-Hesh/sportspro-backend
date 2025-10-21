package com.sportspro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpportunityDTO {

    private Long opportunityId;
    private String title;
    private String company;
    private String location;
    private String type;
    private String sport;
    private String salary;
    private String description;
    private String requirements;

    // Additional fields populated by OpportunityMapper (from Opportunity.user)
    private String postedByName;
    private String postedByAvatar;
    private LocalDateTime postedAt;
}
