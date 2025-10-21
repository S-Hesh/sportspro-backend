package com.sportspro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionRequestDTO {
    private Long connectionId;
    private Long requesterId;
    private String name;
    private String role;
    private String location;
    private String avatar;
    private List<String> skills;
    private int mutualConnections;
    private String requestDate;
}
