package com.myProject.demo.service;

import com.myProject.demo.dto.UserRequest;
import com.myProject.demo.dto.UserResponse;
import com.myProject.demo.entity.User;
import com.myProject.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserResponse createUser(UserRequest request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setMiddleName(request.getMiddleName());
        user.setLastName(request.getLastName());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public Optional<UserResponse> updateUser(Integer id, UserRequest request) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setFirstName(request.getFirstName());
                    existingUser.setMiddleName(request.getMiddleName());
                    existingUser.setLastName(request.getLastName());
                    existingUser.setDateOfBirth(request.getDateOfBirth());
                    existingUser.setEmail(request.getEmail());
                    existingUser.setPhone(request.getPhone());

                    User saved = userRepository.save(existingUser);
                    return toResponse(saved);
                });
    }

    public boolean deleteUser(Integer id) {
        if(!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
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
