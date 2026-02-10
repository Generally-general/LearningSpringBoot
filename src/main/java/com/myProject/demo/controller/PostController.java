package com.myProject.demo.controller;

import com.myProject.demo.dto.ApiResponse;
import com.myProject.demo.dto.PostResponse;
import com.myProject.demo.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@Tag(name="Posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    @Operation(summary="Get All Posts")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Posts Fetched")
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getPosts(
            @PageableDefault(size = 5, sort = "id") Pageable pageable
    ) {
        Page<PostResponse> page = postService.getPosts(pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Posts fetched", page));
    }

    @GetMapping("/{postId}")
    @Operation(summary="Get Post By Id")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Post Fetched By ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found with ID")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable Integer postId) {
        PostResponse post = postService.getPostByIdOrThrow(postId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Post By ID fetched", post));
    }
}
