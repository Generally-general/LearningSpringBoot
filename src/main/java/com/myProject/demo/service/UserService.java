package com.myProject.demo.service;

import com.myProject.demo.dto.UserRequest;
import com.myProject.demo.dto.UserResponse;
import com.myProject.demo.entity.User;
import com.myProject.demo.exception.ConflictException;
import com.myProject.demo.exception.ResourceNotFoundException;
import com.myProject.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

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

    public Page<UserResponse> getUsers(
            String firstName,
            String email,
            Pageable pageable
    ) {
        Page<User> page;

        if(firstName != null && email != null) {
            page = userRepository
                    .findByFirstNameContainingIgnoreCaseAndEmailContainingIgnoreCase(
                            firstName, email, pageable);
        } else if(firstName != null) {
            page = userRepository
                    .findByFirstNameContainingIgnoreCase(
                            firstName, pageable);
        } else if(email != null) {
            page = userRepository
                    .findByEmailContainingIgnoreCase(
                            email, pageable);
        } else {
            page = userRepository.findAll(pageable);
        }

        return page.map(this::toResponse);
    }

    public UserResponse createUser(UserRequest request) {
        User user = new User();
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already exists");
        }
        log.info("User created with email {}", request.getEmail());
        return mapAndSaveUser(request, user);
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
        log.info("User updated from {} to {}", existingUser.getEmail(), request.getEmail());
        return mapAndSaveUser(request, existingUser);
    }

    public void deleteUserOrThrow(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id " + id
                ));
        log.info("User deleted with id {}", id);
        userRepository.delete(user);
    }

    public UserResponse patchUserOrThrow(Integer id, UserRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("User not found");
                    return new ResourceNotFoundException(
                        "User not found with id " + id);
                });

        if(request.getFirstName() != null) {
            existingUser.setFirstName(request.getFirstName());
        }
        if(request.getMiddleName() != null) {
            existingUser.setMiddleName(request.getMiddleName());
        }
        if(request.getLastName() != null) {
            existingUser.setLastName(request.getLastName());
        }
        if(request.getDateOfBirth() != null) {
            existingUser.setDateOfBirth(request.getDateOfBirth());
        }
        if(request.getEmail() != null) {
            if(!existingUser.getEmail().equals(request.getEmail())
                    && userRepository.existsByEmail(request.getEmail())) {
                log.warn("Email conflict");
                throw new ConflictException("Email already exists");
            }
            existingUser.setEmail(request.getEmail());
        }
        if(request.getPhone() != null) {
            existingUser.setPhone(request.getPhone());
        }

        User saved = userRepository.save(existingUser);
        return toResponse(saved);
    }

    private UserResponse mapAndSaveUser(UserRequest request, User existingUser) {
        existingUser.setFirstName(request.getFirstName());
        existingUser.setMiddleName(request.getMiddleName());
        existingUser.setLastName(request.getLastName());
        existingUser.setDateOfBirth(request.getDateOfBirth());
        existingUser.setEmail(request.getEmail());
        existingUser.setPhone(request.getPhone());
        if(request.getPassword() != null) {
            existingUser.setPassword(request.getPassword());
        }
        if(existingUser.getId() == null) {
            existingUser.setRole("USER");
        }
        User saved = userRepository.save(existingUser);
        return toResponse(saved);
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
