package com.shubham.apicommunicationlab.userservice.controller;

import com.shubham.apicommunicationlab.userservice.dto.request.CreateUserRequest;
import com.shubham.apicommunicationlab.userservice.dto.request.UpdateUserRequest;
import com.shubham.apicommunicationlab.userservice.dto.response.ApiResponse;
import com.shubham.apicommunicationlab.userservice.dto.response.UserResponse;
import com.shubham.apicommunicationlab.userservice.dto.response.UserSummaryResponse;
import com.shubham.apicommunicationlab.userservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @RequestBody @Valid CreateUserRequest request,
            HttpServletRequest servletRequest) {
        log.info("REST Request - Create user with username: {}", request.getUsername());
        UserResponse response = userService.createUser(request);
        ApiResponse<UserResponse> apiResponse = ApiResponse.success(
                HttpStatus.CREATED.value(),
                "User created successfully",
                response,
                servletRequest.getRequestURI()
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable UUID uuid,
            @RequestBody @Valid UpdateUserRequest request,
            HttpServletRequest servletRequest) {
        log.info("REST Request - Update user with uuid: {}", uuid);
        UserResponse response = userService.updateUser(uuid, request);
        ApiResponse<UserResponse> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(),
                "User updated successfully",
                response,
                servletRequest.getRequestURI()
        );
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByUuid(
            @PathVariable UUID uuid,
            HttpServletRequest servletRequest) {
        log.info("REST Request - Get user by uuid: {}", uuid);
        UserResponse response = userService.getUserByUuid(uuid);
        ApiResponse<UserResponse> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(),
                "User retrieved successfully",
                response,
                servletRequest.getRequestURI()
        );
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{uuid}/summary")
    public ResponseEntity<ApiResponse<UserSummaryResponse>> getUserSummaryByUuid(
            @PathVariable UUID uuid,
            HttpServletRequest servletRequest) {
        log.info("REST Request - Get user summary by uuid: {}", uuid);
        UserSummaryResponse response = userService.getUserSummaryByUuid(uuid);
        ApiResponse<UserSummaryResponse> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(),
                "User summary retrieved successfully",
                response,
                servletRequest.getRequestURI()
        );
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResponse<UserResponse>> deleteUser(
            @PathVariable UUID uuid,
            HttpServletRequest servletRequest) {
        log.info("REST Request - Delete user by uuid: {}", uuid);
        UserResponse response = userService.deleteUser(uuid);
        ApiResponse<UserResponse> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(),
                "User deleted successfully",
                response,
                servletRequest.getRequestURI()
        );
        return ResponseEntity.ok(apiResponse);
    }
}
