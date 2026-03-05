package com.myProject.demo.controller;

import com.myProject.demo.dto.*;
import com.myProject.demo.entity.User;
import com.myProject.demo.exception.AuthenticationException;
import com.myProject.demo.service.AuthService;
import com.myProject.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name="Auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    @Operation(description = "Log in")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login Successful")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        LoginResponse data = authService.login(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Login successful", data)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> me(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Fetched Me", userService.toResponse(user)));
    }

    @PutMapping("/me")
    @Operation(summary = "Update my own profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateMe(
            @AuthenticationPrincipal User authenticatedUser,
            @Valid @RequestBody UserRequest request
    ) {
        UserResponse updated = userService.updateUserOrThrow(authenticatedUser.getId(), request);

        return ResponseEntity.ok(new ApiResponse<>(true, "Profile updated", updated));
    }

    @PatchMapping("/me")
    @Operation(summary = "Patch my own profile")
    public ResponseEntity<ApiResponse<UserResponse>> patchMe(
            @AuthenticationPrincipal User authenticatedUser,
            @Valid @RequestBody UserRequest request
    ) {
        UserResponse updated = userService.patchUserOrThrow(authenticatedUser.getId(), request);

        return ResponseEntity.ok(new ApiResponse<>(true, "Profile patched", updated));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshAccessToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        LoginResponse data = authService.refreshAccessToken(request.getRefreshToken());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Token refreshed successfully", data)
        );
    }
}
