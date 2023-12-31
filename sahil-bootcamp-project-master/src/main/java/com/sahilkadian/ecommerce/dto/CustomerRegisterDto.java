package com.sahilkadian.ecommerce.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CustomerRegisterDto {

    @NotBlank(message = "Firstname cannot be blank")
    private String firstName;
    private String middleName;
    private String lastName;

    @NotNull(message = "Email cannot be empty")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotNull(message = "Password cannot be empty")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}$", message = "Password must have atleast 1 Uppercase, 1 lowercase, 1 Special Character and 1 Number")
    private String password;

    @NotNull(message = "Confirm Password cannot be empty")
    private String confirmPassword;

    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "Please provide a valid phone number")
    @NotNull(message = "Contact cannot be empty")
    private String contact;
}
