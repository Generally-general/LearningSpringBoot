package com.myProject.demo.controller;

import com.myProject.demo.dto.PostRequest;
import com.myProject.demo.dto.PostResponse;
import com.myProject.demo.dto.UserRequest;
import com.myProject.demo.dto.UserResponse;
import com.myProject.demo.service.PostService;
import com.myProject.demo.service.UserService;
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
public class UserController {

    private final UserService userService;
    private final PostService postService;

    public UserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String email,
            @PageableDefault(size = 10, sort = "id")
            Pageable pageable
    ) {
        Page<UserResponse> page = userService.getUsers(firstName, email, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id) {
        UserResponse response = userService.getUserResponseByIdOrThrow(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserRequest request
    ) {
        UserResponse savedUser = userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UserRequest request
    ) {
        UserResponse updated = userService.updateUserOrThrow(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUserOrThrow(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> patchUser(
            @PathVariable Integer id,
            @RequestBody UserRequest request
    ) {
        UserResponse updated = userService.patchUserOrThrow(id, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{userId}/posts")
    public ResponseEntity<Page<PostResponse>> getPostsByUser(
            @PathVariable Integer userId,
            @PageableDefault(size = 5, sort = "id") Pageable pageable
    ) {
        Page<PostResponse> response = postService.getPostsByUserOrThrow(userId, pageable);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{userId}/posts")
    public ResponseEntity<PostResponse> createPost(
            @PathVariable Integer userId,
            @Valid @RequestBody PostRequest request
    ) {
        PostResponse savedPost = postService.createPost(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedPost);
    }

    @PutMapping("/{userId}/posts/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Integer userId,
            @PathVariable Integer postId,
            @Valid @RequestBody PostRequest request
    ) {
        PostResponse updated = postService.updatePostOrThrow(userId, postId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{userId}/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer userId ,@PathVariable Integer postId) {
        postService.deletePostOrThrow(userId, postId);
        return ResponseEntity.noContent().build();
    }
}
