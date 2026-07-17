package com.shubham.apicommunicationlab.userservice.integration;

import com.shubham.apicommunicationlab.userservice.dto.request.CreateUserRequest;
import com.shubham.apicommunicationlab.userservice.dto.request.UpdateUserRequest;
import com.shubham.apicommunicationlab.userservice.dto.response.UserResponse;
import com.shubham.apicommunicationlab.userservice.dto.response.UserSummaryResponse;
import com.shubham.apicommunicationlab.userservice.exception.ErrorResponse;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static UUID createdUserUuid;
    private static final String username = "test_user_" + UUID.randomUUID().toString().substring(0, 8);
    private static final String email = username + "@example.com";

    @Test
    @Order(1)
    public void testCreateUser() {
        CreateUserRequest request = CreateUserRequest.builder()
                .username(username)
                .email(email)
                .displayName("Test User")
                .bio("Initial Bio")
                .avatarUrl("https://example.com/avatar.png")
                .build();

        ResponseEntity<UserResponse> response = restTemplate.postForEntity(
                "/api/users",
                request,
                UserResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUuid()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo(username);
        assertThat(response.getBody().getEmail()).isEqualTo(email);

        createdUserUuid = response.getBody().getUuid();
    }

    @Test
    @Order(2)
    public void testGetUser() {
        assertThat(createdUserUuid).isNotNull();

        ResponseEntity<UserResponse> response = restTemplate.getForEntity(
                "/api/users/" + createdUserUuid,
                UserResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUuid()).isEqualTo(createdUserUuid);
        assertThat(response.getBody().getUsername()).isEqualTo(username);
    }

    @Test
    @Order(3)
    public void testGetUserSummary() {
        assertThat(createdUserUuid).isNotNull();

        ResponseEntity<UserSummaryResponse> response = restTemplate.getForEntity(
                "/api/users/" + createdUserUuid + "/summary",
                UserSummaryResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUuid()).isEqualTo(createdUserUuid);
        assertThat(response.getBody().getUsername()).isEqualTo(username);
        assertThat(response.getBody().getDisplayName()).isEqualTo("Test User");
    }

    @Test
    @Order(4)
    public void testUpdateUser() {
        assertThat(createdUserUuid).isNotNull();

        UpdateUserRequest request = UpdateUserRequest.builder()
                .displayName("Updated Test User")
                .bio("Updated Bio")
                .avatarUrl("https://example.com/avatar_new.png")
                .build();

        HttpEntity<UpdateUserRequest> entity = new HttpEntity<>(request);

        ResponseEntity<UserResponse> response = restTemplate.exchange(
                "/api/users/" + createdUserUuid,
                HttpMethod.PUT,
                entity,
                UserResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUuid()).isEqualTo(createdUserUuid);
        assertThat(response.getBody().getDisplayName()).isEqualTo("Updated Test User");
        assertThat(response.getBody().getBio()).isEqualTo("Updated Bio");
    }

    @Test
    @Order(5)
    public void testDeleteUser() {
        assertThat(createdUserUuid).isNotNull();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/users/" + createdUserUuid,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Verify soft deleted user acts as non-existent (returns 404)
        ResponseEntity<ErrorResponse> getResponse = restTemplate.getForEntity(
                "/api/users/" + createdUserUuid,
                ErrorResponse.class
        );

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getStatus()).isEqualTo(404);
        assertThat(getResponse.getBody().getError()).isEqualTo("NOT_FOUND");
        assertThat(getResponse.getBody().getMessage()).contains("User not found");
        assertThat(getResponse.getBody().getPath()).contains("/api/users/");
    }

    @Test
    @Order(6)
    public void testCreateUser_ValidationFailed() {
        CreateUserRequest request = CreateUserRequest.builder()
                .username("") // Blank (Invalid)
                .email("not-an-email") // Invalid email format
                .build();

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/api/users",
                request,
                ErrorResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getError()).isEqualTo("BAD_REQUEST");
        assertThat(response.getBody().getMessage()).contains("Validation failed");
    }
}
