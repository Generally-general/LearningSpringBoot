package com.myProject.demo.controller;

import com.myProject.demo.dto.*;
import com.myProject.demo.entity.User;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.AccessDeniedException;

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
    @PreAuthorize("hasRole('ADMIN')")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Users Fetched")
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
    @PreAuthorize("hasRole('ADMIN')")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User Fetched")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Unauthorised")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @AuthenticationPrincipal User authenticatedUser,
            @PathVariable Integer id
    ) {
        UserResponse response = userService.getUserResponseByIdOrThrow(authenticatedUser, id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User fetched", response));
    }

    @PostMapping
    @Operation(summary="Create new user")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User created")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation error")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Email conflict")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody UserRequest request
    ) {
        UserResponse savedUser = userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "User Created", savedUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary="Update User")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User updated")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Email conflict")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UserRequest request
    ) {
        UserResponse updated = userService.updateUserOrThrow(id, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "User Updated", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Delete User")
    @PreAuthorize("hasRole('ADMIN')")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User deleted")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "User not found")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Integer id) {
        userService.deleteUserOrThrow(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User deleted", null));
    }

    @PatchMapping("/{id}")
    @Operation(summary="Patch User")
    @PreAuthorize("hasRole('ADMIN')")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User created")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "User not found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Email conflict")
    public ResponseEntity<ApiResponse<UserResponse>> patchUser(
            @PathVariable Integer id,
            @RequestBody UserRequest request
    ) {
        UserResponse updated = userService.patchUserOrThrow(id, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "User patched", updated));
    }

    @GetMapping("/posts")
    @Operation(summary="Get All Posts By User")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Posts Fetched by User")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "User not found")
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getPostsByUser(
            @AuthenticationPrincipal User authenticatedUser,
            @PageableDefault(size = 5, sort = "id") Pageable pageable
    ) {
        Page<PostResponse> response = postService.getPostsByUserOrThrow(authenticatedUser, pageable);

        return ResponseEntity.ok(new ApiResponse<>(true, "Posts fetched", response));
    }
}
