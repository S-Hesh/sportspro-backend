// src/main/java/com/sportspro/dto/CreatePostRequest.java
package com.sportspro.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;

@Data @NoArgsConstructor @AllArgsConstructor
public class CreatePostRequest {
    @NotBlank private String content;
    private String imageUrl;
    private String tags;      // comma-sep
}
