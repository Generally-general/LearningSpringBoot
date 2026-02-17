package com.myProject.demo.controller;

import com.myProject.demo.dto.ApiResponse;
import com.myProject.demo.dto.LoginRequest;
import com.myProject.demo.dto.UserResponse;
import com.myProject.demo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name="Auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(description = "Log in")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login Successful")
    public ResponseEntity<ApiResponse<UserResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        UserResponse data = authService.login(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Login successful", data)
        );
    }
}
