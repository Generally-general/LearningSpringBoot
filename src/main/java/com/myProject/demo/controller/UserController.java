package com.myProject.demo.controller;

import com.myProject.demo.dto.*;
import com.myProject.demo.service.PostService;
import com.myProject.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name="Users")
public class UserController {

    private final UserService userService;
    private final PostService postService;

    public UserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping
    @Operation(summary="Get All Users")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String email,
            @PageableDefault(size = 10, sort = "id")
            Pageable pageable
    ) {
        Page<UserResponse> page = userService.getUsers(firstName, email, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Users fetched", page));
    }

    @GetMapping("/{id}")
    @Operation(summary="Get User By Id")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Integer id) {
        UserResponse response = userService.getUserResponseByIdOrThrow(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User fetched", response));
    }

    @PostMapping
    @Operation(summary="Create User")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody UserRequest request
    ) {
        UserResponse savedUser = userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "User Created", savedUser));
    }

    @PutMapping("/{id}")
    @Operation(summary="Update User")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UserRequest request
    ) {
        UserResponse updated = userService.updateUserOrThrow(id, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "User Updated", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Delete User")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Integer id) {
        userService.deleteUserOrThrow(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User deleted", null));
    }

    @PatchMapping("/{id}")
    @Operation(summary="Patch User")
    public ResponseEntity<ApiResponse<UserResponse>> patchUser(
            @PathVariable Integer id,
            @RequestBody UserRequest request
    ) {
        UserResponse updated = userService.patchUserOrThrow(id, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "User patched", updated));
    }

    @GetMapping("/{userId}/posts")
    @Operation(summary="Get All Posts By User")
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getPostsByUser(
            @PathVariable Integer userId,
            @PageableDefault(size = 5, sort = "id") Pageable pageable
    ) {
        Page<PostResponse> response = postService.getPostsByUserOrThrow(userId, pageable);

        return ResponseEntity.ok(new ApiResponse<>(true, "Posts fetched", response));
    }

    @PostMapping("/{userId}/posts")
    @Operation(summary="Create Post")
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @PathVariable Integer userId,
            @Valid @RequestBody PostRequest request
    ) {
        PostResponse savedPost = postService.createPost(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Post created", savedPost));
    }

    @PutMapping("/{userId}/posts/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable Integer userId,
            @PathVariable Integer postId,
            @Valid @RequestBody PostRequest request
    ) {
        PostResponse updated = postService.updatePostOrThrow(userId, postId, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Post updated", updated));
    }

    @DeleteMapping("/{userId}/posts/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Integer userId ,@PathVariable Integer postId) {
        postService.deletePostOrThrow(userId, postId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Post", null));
    }
}
