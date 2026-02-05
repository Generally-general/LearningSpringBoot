package com.myProject.demo.controller;

import com.myProject.demo.dto.PostResponse;
import com.myProject.demo.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> getPosts(
            @PageableDefault(size = 5, sort = "id") Pageable pageable
    ) {
        Page<PostResponse> page = postService.getPosts(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPostByPostId(@PathVariable Integer postId) {
        PostResponse post = postService.getPostByIdOrThrow(postId);
        return ResponseEntity.ok(post);
    }
}
