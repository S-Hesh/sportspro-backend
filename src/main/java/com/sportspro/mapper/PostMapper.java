// src/main/java/com/sportspro/mapper/PostMapper.java
package com.sportspro.mapper;

import com.sportspro.dto.PostDTO;
import com.sportspro.model.Post;
import org.mapstruct.*;

@Mapper(componentModel="spring")
public interface PostMapper {
    @Mapping(target="authorId",    source="user.userId")
    @Mapping(target="authorName",  source="user.name")
    @Mapping(target="authorAvatar",source="user.avatar")
    @Mapping(target="createdAt",   source="createdAt")
    PostDTO postToPostDTO(Post post);

    @Mapping(target="postId", ignore=true)
    @Mapping(target="user", ignore=true)
    Post postDTOToPost(PostDTO dto);
}
