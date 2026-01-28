package com.myProject.demo.service;

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

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> updateUser(Integer id, User updatedData) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setFirstName(updatedData.getFirstName());
                    existingUser.setMiddleName(updatedData.getMiddleName());
                    existingUser.setLastName(updatedData.getLastName());
                    existingUser.setDateOfBirth(updatedData.getDateOfBirth());
                    existingUser.setEmail(updatedData.getEmail());
                    existingUser.setPhone(updatedData.getPhone());

                    return userRepository.save(existingUser);
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
