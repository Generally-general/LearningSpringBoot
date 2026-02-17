package com.myProject.demo.service;

import com.myProject.demo.dto.UserResponse;
import com.myProject.demo.entity.User;
import com.myProject.demo.exception.AuthenticationException;
import com.myProject.demo.exception.ResourceNotFoundException;
import com.myProject.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    public UserResponse login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Invalid Email or Password"
                ));

        if(!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new AuthenticationException("Invalid Email or Password");
        }

        return userService.toResponse(user);
    }
}
