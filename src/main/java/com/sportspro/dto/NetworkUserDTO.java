package com.sportspro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NetworkUserDTO {
    private Long id;
    private String name;
    private String role;
    private String location;
    private String avatar;
    private List<String> skills;
    private int connections;

    // IMPORTANT: expose this as "isConnected" in JSON so the TS expects it correctly
    @JsonProperty("isConnected")
    private boolean connected;

    private boolean hasIncomingRequest;
    private boolean hasOutgoingRequest;
}
