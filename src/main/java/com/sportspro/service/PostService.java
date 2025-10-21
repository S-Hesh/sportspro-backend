package com.sportspro.service;

import com.sportspro.dto.*;
import com.sportspro.mapper.PostMapper;
import com.sportspro.model.*;
import com.sportspro.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    @Autowired private PostRepository postRepository;
    @Autowired private PostMapper    postMapper;

    /** Fetch paged feed, newest first */
    public Page<PostDTO> getAllPosts(int page, int size) {
        Pageable p = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findAll(p)
                .map(postMapper::postToPostDTO);
    }

    /** Create new post under `author` */
    public PostDTO createPost(CreatePostRequest req, User author) {
        Post post = new Post();
        post.setContent(req.getContent());
        post.setTags(req.getTags());

        // If there's an image URL, set it on the post
        if (req.getImageUrl() != null && !req.getImageUrl().isEmpty()) {
            post.setImageUrl(req.getImageUrl());
        }

        post.setUser(author);
        post = postRepository.save(post);
        return postMapper.postToPostDTO(post);
    }
}
