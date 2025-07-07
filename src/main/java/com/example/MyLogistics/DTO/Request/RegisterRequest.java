package com.example.MyLogistics.DTO.Request;

import com.example.MyLogistics.Model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "please enter your name")
    private String name;
    @Email(message = "invalid email")
    private String email;
    @Size(min = 6, max = 20, message = "password must be at least 6 characters")
    private String password;
    @NotBlank(message = "enter a valid phone number")
    @Pattern(
            regexp = "^\\+?[0-9]{10,15}$",
            message = "Invalid phone number format"
    )
    private String phoneNumber;
    private Role role;

}
