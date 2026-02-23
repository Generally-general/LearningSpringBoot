package com.myProject.demo.controller;

import com.myProject.demo.dto.ApiResponse;
import com.myProject.demo.dto.LoginRequest;
import com.myProject.demo.dto.LoginResponse;
import com.myProject.demo.dto.UserResponse;
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
    public ResponseEntity<ApiResponse<UserResponse>> me(Authentication authentication) {

        if (authentication == null ||
                authentication.getPrincipal() == null ||
                !(authentication.getPrincipal() instanceof User user)) {
            throw new AuthenticationException("Unauthorized");
        }

        UserResponse data = userService.toResponse(user);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Fetched Me", data)
        );
    }
}
