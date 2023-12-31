package com.sahilkadian.ecommerce.dto;

import com.sahilkadian.ecommerce.entities.Address;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SellerRegisterDto {

    @NotBlank(message = "Firstname cannot be blank")
    private String firstName;
    private String middleName;
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}$", message = "Password must have atleast 1 Uppercase, 1 lowercase, 1 Special Character and 1 Number")
    private String password;

    @NotBlank(message = "Confirm Password cannot be blank")
    private String confirmPassword;

    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "Please provide a valid phone number")
    @NotBlank(message = "Company Contact cannot be blank")
    private String companyContact;

    @NotBlank(message = "Company name cannot be blank")
    private String companyName;

    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Za-z]{1}Z[0-9A-Za-z]{1}$", message = "Please provide a valid GST number")
    @NotBlank(message = "GST cannot be blank")
    private String gst;

    private Address address;
}
