package com.sahilkadian.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordDto {

    @NotNull(message = "Password cannot be empty")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}$", message = "Password must have atleast 1 Uppercase, 1 lowercase, 1 Special Character and 1 Number")
    String password;

    @NotNull(message = "Confirm Password cannot be empty")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}$", message = "Password must have atleast 1 Uppercase, 1 lowercase, 1 Special Character and 1 Number")
    String confirmPassword;
}
