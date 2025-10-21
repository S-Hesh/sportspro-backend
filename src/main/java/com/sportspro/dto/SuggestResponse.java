package com.sportspro.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class SuggestResponse {
    private String query;
    private List<String> users;
    private List<String> posts;
    private List<String> opportunities;
}
