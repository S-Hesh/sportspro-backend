package com.sportspro.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class SearchResultDTO {
    private String type;          // "user" | "post" | "opportunity"
    private Long id;
    private String title;         // username | "Post by {author}" | job title
    private String subtitle;      // sport • location | author name | company • location
    private String excerpt;       // bio | post snippet | job snippet
    private String avatarUrl;     // user avatar or null
    private String url;           // optional; we keep null to avoid breaking routes
    private LocalDateTime createdAt;  // createdAt / postedAt
    private double score;         // ranking
}
