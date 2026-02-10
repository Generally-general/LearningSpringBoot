package com.myProject.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Schema(description = "Request object representing a user")
public class UserRequest {

    @NotBlank
    @Schema(description = "First name of the user", example = "Arpita")
    private String firstName;
    @Schema(description = "Middle name of the user", example = "Mallikarjun")
    private String middleName;

    @NotBlank
    @Schema(description = "Last name of the user", example = "Patil")
    private String lastName;
    @Schema(description = "Date of Birth of the user")
    private LocalDate dateOfBirth;

    @NotBlank
    @Email
    @Schema(description="User email address")
    private String email;

    @Schema(description = "User phone number")
    private String phone;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
