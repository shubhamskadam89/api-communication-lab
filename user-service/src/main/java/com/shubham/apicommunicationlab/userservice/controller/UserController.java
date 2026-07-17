package com.shubham.apicommunicationlab.userservice.controller;

import com.shubham.apicommunicationlab.userservice.dto.request.CreateUserRequest;
import com.shubham.apicommunicationlab.userservice.dto.request.UpdateUserRequest;
import com.shubham.apicommunicationlab.userservice.dto.response.UserResponse;
import com.shubham.apicommunicationlab.userservice.dto.response.UserSummaryResponse;
import com.shubham.apicommunicationlab.userservice.service.UserService;
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
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        log.info("REST Request - Create user with username: {}", request.getUsername());
        UserResponse response = userService.createUser(request);
        log.info("REST Response - User created with uuid: {}", response.getUuid());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID uuid, @RequestBody @Valid UpdateUserRequest request) {
        log.info("REST Request - Update user with uuid: {}", uuid);
        UserResponse response = userService.updateUser(uuid, request);
        log.info("REST Response - User updated with uuid: {}", response.getUuid());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<UserResponse> getUserByUuid(@PathVariable UUID uuid) {
        log.info("REST Request - Get user by uuid: {}", uuid);
        UserResponse response = userService.getUserByUuid(uuid);
        log.info("REST Response - User retrieved with uuid: {}", response.getUuid());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{uuid}/summary")
    public ResponseEntity<UserSummaryResponse> getUserSummaryByUuid(@PathVariable UUID uuid) {
        log.info("REST Request - Get user summary by uuid: {}", uuid);
        UserSummaryResponse response = userService.getUserSummaryByUuid(uuid);
        log.info("REST Response - User summary retrieved with uuid: {}", response.getUuid());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID uuid) {
        log.info("REST Request - Delete user by uuid: {}", uuid);
        userService.deleteUser(uuid);
        log.info("REST Response - User deleted successfully for uuid: {}", uuid);
        return ResponseEntity.noContent().build();
    }
}
