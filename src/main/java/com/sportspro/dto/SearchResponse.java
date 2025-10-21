package com.sportspro.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class SearchResponse {
    private String query;
    private int page;
    private int pageSize;
    private long total;
    private List<SearchResultDTO> results;
}
