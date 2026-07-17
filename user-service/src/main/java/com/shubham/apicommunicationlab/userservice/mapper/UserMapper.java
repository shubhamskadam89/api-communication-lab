package com.shubham.apicommunicationlab.userservice.mapper;

import com.shubham.apicommunicationlab.userservice.dto.request.CreateUserRequest;
import com.shubham.apicommunicationlab.userservice.dto.request.UpdateUserRequest;
import com.shubham.apicommunicationlab.userservice.dto.response.UserResponse;
import com.shubham.apicommunicationlab.userservice.dto.response.UserSummaryResponse;
import com.shubham.apicommunicationlab.userservice.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(CreateUserRequest request) {
        if (request == null) {
            return null;
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setDisplayName(request.getDisplayName());
        user.setBio(request.getBio());
        user.setAvatarUrl(request.getAvatarUrl());
        return user;
    }

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .uuid(user.getUuid())
                .username(user.getUsername())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .bio(user.getBio())
                .avatarUrl(user.getAvatarUrl())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public UserSummaryResponse toSummaryResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserSummaryResponse.builder()
                .uuid(user.getUuid())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

    public void updateEntity(UpdateUserRequest request, User user) {
        if (request == null || user == null) {
            return;
        }
        if (request.getDisplayName() != null) {
            user.setDisplayName(request.getDisplayName());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
    }
}
