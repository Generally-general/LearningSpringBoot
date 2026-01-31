package com.myProject.demo.controller;

import com.myProject.demo.dto.UserRequest;
import com.myProject.demo.dto.UserResponse;
import com.myProject.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(userService.getAllUserResponses());
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
}
