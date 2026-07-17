package com.shubham.apicommunicationlab.userservice.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    @Size(max = 100, message = "Display name cannot exceed 100 characters")
    private String displayName;

    @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
    private String bio;

    @Size(max = 2000, message = "Avatar URL cannot exceed 2000 characters")
    private String avatarUrl;
}
