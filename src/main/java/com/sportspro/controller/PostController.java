
package com.sportspro.controller;

import com.sportspro.dto.*;
import com.sportspro.model.User;
import com.sportspro.repository.PostRepository;
import com.sportspro.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired private PostService postService;
    @Autowired private UserService userService;
    @Autowired private FileStorageService fileStorageService;

    /** GET /api/posts?page=0&size=10 */
    @GetMapping
    public ResponseEntity<Page<PostDTO>> feed(@RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size) {
        return ResponseEntity.ok(postService.getAllPosts(page,size));
    }

    /** POST /api/posts */
    @PostMapping
    public ResponseEntity<PostDTO> create(@Valid @RequestBody CreatePostRequest req, @AuthenticationPrincipal UserDetails ud) {
        User author = userService.findEntityByEmail(ud.getUsername());
        return ResponseEntity.ok(postService.createPost(req, author));
    }

    // Image upload endpoint
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = fileStorageService.storeImage(file);
            return ResponseEntity.ok(imageUrl); // Return the image URL for use in creating a post
        } catch (Exception e) {
            return ResponseEntity.status(500).body("File upload failed");
        }
    }
}
