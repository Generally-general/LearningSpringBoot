package com.myProject.demo.controller;

import com.myProject.demo.dto.ApiResponse;
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
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getPosts(
            @PageableDefault(size = 5, sort = "id") Pageable pageable
    ) {
        Page<PostResponse> page = postService.getPosts(pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Posts fetched", page));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable Integer postId) {
        PostResponse post = postService.getPostByIdOrThrow(postId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Post By ID fetched", post));
    }
}
