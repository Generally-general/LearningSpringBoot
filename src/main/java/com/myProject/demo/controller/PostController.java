package com.myProject.demo.controller;

import com.myProject.demo.dto.ApiResponse;
import com.myProject.demo.dto.PostRequest;
import com.myProject.demo.dto.PostResponse;
import com.myProject.demo.entity.User;
import com.myProject.demo.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<PostResponse> page = postService.getPosts(pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Posts fetched", page));
    }

    @GetMapping("/me")
    @Operation(summary = "Get My Posts")
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getMyPosts(
            @AuthenticationPrincipal User authenticatedUser,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<PostResponse> response = postService.getPostsByUserOrThrow(authenticatedUser, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Your posts fetched", response));
    }

    @PostMapping
    @Operation(summary="Create Post")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Post created")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "User not found")
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @AuthenticationPrincipal User authenticatedUser,
            @Valid @RequestBody PostRequest request
    ) {
        PostResponse savedPost = postService.createPost(authenticatedUser, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Post created", savedPost));
    }

    @GetMapping("/{postId}")
    @Operation(summary="Get Post By Id")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Post Fetched By ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found with ID")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable Integer postId) {
        PostResponse post = postService.getPostByIdOrThrow(postId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Post By ID fetched", post));
    }

    @PutMapping("/{postId}")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Post updated")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @AuthenticationPrincipal User authenticatedUser,
            @PathVariable Integer postId,
            @Valid @RequestBody PostRequest request
    ) {
        PostResponse data = postService.updatePostOrThrow(authenticatedUser, postId, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Post updated", data));
    }

    @DeleteMapping("/{postId}")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Post deleted")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @AuthenticationPrincipal User authenticatedUser,
            @PathVariable Integer postId
    )  {
        postService.deletePostOrThrow(authenticatedUser, postId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Post deleted", null));
    }
}
