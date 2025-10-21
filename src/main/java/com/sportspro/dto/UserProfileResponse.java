package com.sportspro.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserProfileResponse {
    // Getters and Setters
    private String name;
    private String avatar;
    private String title;
    private String location;
    private String joined;
    private String bio;
    private List<String> skills;

}
