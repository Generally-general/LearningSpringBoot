package com.myProject.demo.repository;

import com.myProject.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    Page<User> findByFirstNameContainingIgnoreCase(
            String firstName,
            Pageable pageable
    );

    Page<User> findByEmailContainingIgnoreCase(
            String email,
            Pageable pageable
    );

    Page<User> findByFirstNameContainingIgnoreCaseAndEmailContainingIgnoreCase(
            String firstName,
            String email,
            Pageable pageable
    );

    Optional<User> findByEmail(String email);
}
