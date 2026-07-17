package com.shubham.apicommunicationlab.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID uuid;
    private String username;
    private String email;
    private String displayName;
    private String bio;
    private String avatarUrl;
    private Instant createdAt;
    private Instant updatedAt;
}
