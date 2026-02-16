package com.myProject.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Schema(description = "Response object representing a user")
public class UserResponse {
    @Schema(description = "Unique user ID", example = "1")
    private Integer id;
    @Schema(description = "First name of the user", example = "Pranjal")
    private String firstName;
    @Schema(description = "Last name of the user", example = "Kumar")
    private String lastName;
    @Schema(description = "User email address", example = "test@gmail.com")
    private String email;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
