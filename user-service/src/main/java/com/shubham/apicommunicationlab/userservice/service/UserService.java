package com.shubham.apicommunicationlab.userservice.service;

import com.shubham.apicommunicationlab.userservice.dto.request.CreateUserRequest;
import com.shubham.apicommunicationlab.userservice.dto.request.UpdateUserRequest;
import com.shubham.apicommunicationlab.userservice.dto.response.UserResponse;
import com.shubham.apicommunicationlab.userservice.dto.response.UserSummaryResponse;

import java.util.UUID;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);
    UserResponse updateUser(UUID uuid, UpdateUserRequest request);
    UserResponse getUserByUuid(UUID uuid);
    UserSummaryResponse getUserSummaryByUuid(UUID uuid);
    UserResponse deleteUser(UUID uuid);
}
