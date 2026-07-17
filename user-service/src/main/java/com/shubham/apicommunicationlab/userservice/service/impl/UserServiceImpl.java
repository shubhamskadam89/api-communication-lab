package com.shubham.apicommunicationlab.userservice.service.impl;

import com.shubham.apicommunicationlab.userservice.dto.request.CreateUserRequest;
import com.shubham.apicommunicationlab.userservice.dto.request.UpdateUserRequest;
import com.shubham.apicommunicationlab.userservice.dto.response.UserResponse;
import com.shubham.apicommunicationlab.userservice.dto.response.UserSummaryResponse;
import com.shubham.apicommunicationlab.userservice.entity.User;
import com.shubham.apicommunicationlab.userservice.exception.UserNotFoundException;
import com.shubham.apicommunicationlab.userservice.mapper.UserMapper;
import com.shubham.apicommunicationlab.userservice.repository.UserRepository;
import com.shubham.apicommunicationlab.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        log.info("Creating user with username: {}", request.getUsername());
        User user = userMapper.toEntity(request);
        user = userRepository.save(user);
        log.info("User created successfully with internal ID: {}", user.getId());
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID uuid, UpdateUserRequest request) {
        User user = userRepository.findByUuidAndIsDeletedFalse(uuid)
                .orElseThrow(() -> new UserNotFoundException("User not found with uuid: " + uuid));

        // Call internal function-to-function using internal Long ID
        performInternalUpdate(user.getId(), request);

        User refreshedUser = userRepository.findByIdAndIsDeletedFalse(user.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + user.getId()));
        return userMapper.toResponse(refreshedUser);
    }

    // Internal function-to-function helper method using internal Long ID
    private void performInternalUpdate(Long id, UpdateUserRequest request) {
        log.info("Performing internal update for user ID: {}", id);
        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        userMapper.updateEntity(request, user);
        userRepository.save(user);
        log.info("Internal update completed for user ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByUuid(UUID uuid) {
        User user = userRepository.findByUuidAndIsDeletedFalse(uuid)
                .orElseThrow(() -> new UserNotFoundException("User not found with uuid: " + uuid));
        log.info("Fetched user profile for internal ID: {}", user.getId());
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserSummaryResponse getUserSummaryByUuid(UUID uuid) {
        User user = userRepository.findByUuidAndIsDeletedFalse(uuid)
                .orElseThrow(() -> new UserNotFoundException("User not found with uuid: " + uuid));
        log.info("Fetched user summary for internal ID: {}", user.getId());
        return userMapper.toSummaryResponse(user);
    }

    @Override
    @Transactional
    public void deleteUser(UUID uuid) {
        User user = userRepository.findByUuidAndIsDeletedFalse(uuid)
                .orElseThrow(() -> new UserNotFoundException("User not found with uuid: " + uuid));
        log.info("Soft deleting user with internal ID: {}", user.getId());
        user.setDeleted(true);
        userRepository.save(user);
        log.info("Soft delete completed for internal ID: {}", user.getId());
    }
}
