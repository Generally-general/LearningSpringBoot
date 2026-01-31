package com.myProject.demo.service;

import com.myProject.demo.dto.UserRequest;
import com.myProject.demo.dto.UserResponse;
import com.myProject.demo.entity.User;
import com.myProject.demo.exception.ConflictException;
import com.myProject.demo.exception.ResourceNotFoundException;
import com.myProject.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getUserResponseByIdOrThrow(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id " + id
                ));

        return toResponse(user);
    }

    public List<UserResponse> getAllUserResponses() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse createUser(UserRequest request) {
        User user = new User();
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already exists");
        }
        return getUserResponse(request, user);
    }

    public UserResponse updateUserOrThrow(Integer id, UserRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id " + id
                ));
        if(!existingUser.getEmail().equals(request.getEmail())
            && userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already exists");
        }
        return getUserResponse(request, existingUser);
    }

    private UserResponse getUserResponse(UserRequest request, User existingUser) {
        existingUser.setFirstName(request.getFirstName());
        existingUser.setMiddleName(request.getMiddleName());
        existingUser.setLastName(request.getLastName());
        existingUser.setDateOfBirth(request.getDateOfBirth());
        existingUser.setEmail(request.getEmail());
        existingUser.setPhone(request.getPhone());

        User saved = userRepository.save(existingUser);
        return toResponse(saved);
    }

    public void deleteUserOrThrow(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id " + id
                ));
        userRepository.delete(user);
    }

    public UserResponse toResponse(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
