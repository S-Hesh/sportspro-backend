// src/main/java/com/sportspro/dto/PostDTO.java
package com.sportspro.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
public class PostDTO {
    private Long postId;
    private String content;
    private String imageUrl;
    private Integer likes;
    private Integer commentsCount;
    private String tags;
    // new:
    private Long authorId;
    private String authorName;
    private String authorAvatar;
    private LocalDateTime createdAt;
}
