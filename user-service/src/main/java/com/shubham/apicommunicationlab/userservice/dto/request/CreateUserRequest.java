package com.shubham.apicommunicationlab.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be a valid email address")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Size(max = 100, message = "Display name cannot exceed 100 characters")
    private String displayName;

    @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
    private String bio;

    @Size(max = 2000, message = "Avatar URL cannot exceed 2000 characters")
    private String avatarUrl;
}
