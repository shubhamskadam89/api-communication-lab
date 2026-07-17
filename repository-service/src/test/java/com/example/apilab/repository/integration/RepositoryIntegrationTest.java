package com.example.apilab.repository.integration;

import com.example.apilab.repository.dto.request.CreateRepositoryRequest;
import com.example.apilab.repository.dto.request.UpdateRepositoryRequest;
import com.example.apilab.repository.dto.response.ApiResponse;
import com.example.apilab.repository.dto.response.RepositoryResponse;
import com.example.apilab.repository.entity.RepositoryVisibility;
import com.example.apilab.repository.exception.ErrorResponse;
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

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RepositoryIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final UUID ownerUuid = UUID.randomUUID();
    private static UUID createdRepositoryUuid;
    private static String repositorySlug;

    // Helper classes to avoid generic type mapping problems with TestRestTemplate
    public static class RepositoryApiResponse extends ApiResponse<RepositoryResponse> {}
    public static class RepositoryListApiResponse extends ApiResponse<List<RepositoryResponse>> {}

    @Test
    @Order(1)
    public void testCreateRepository() {
        CreateRepositoryRequest request = CreateRepositoryRequest.builder()
                .name("Spring Boot Core")
                .description("Core spring boot project")
                .visibility(RepositoryVisibility.PUBLIC)
                .primaryLanguage("Java")
                .build();

        ResponseEntity<RepositoryApiResponse> response = restTemplate.postForEntity(
                "/api/users/" + ownerUuid + "/repositories",
                request,
                RepositoryApiResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getBody().getMessage()).contains("created successfully");
        assertThat(response.getBody().getData()).isNotNull();

        RepositoryResponse repo = response.getBody().getData();
        assertThat(repo.getUuid()).isNotNull();
        assertThat(repo.getOwnerUuid()).isEqualTo(ownerUuid);
        assertThat(repo.getCreatedByUuid()).isEqualTo(ownerUuid);
        assertThat(repo.getName()).isEqualTo("Spring Boot Core");
        assertThat(repo.getSlug()).isEqualTo("spring-boot-core");
        assertThat(repo.getVisibility()).isEqualTo(RepositoryVisibility.PUBLIC);
        assertThat(repo.getDefaultBranch()).isEqualTo("main");

        createdRepositoryUuid = repo.getUuid();
        repositorySlug = repo.getSlug();
    }

    @Test
    @Order(2)
    public void testCreateRepository_DuplicateSlug() {
        CreateRepositoryRequest request = CreateRepositoryRequest.builder()
                .name("spring boot core") // Will generate slug "spring-boot-core"
                .visibility(RepositoryVisibility.PUBLIC)
                .build();

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/api/users/" + ownerUuid + "/repositories",
                request,
                ErrorResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(response.getBody().getError()).isEqualTo("CONFLICT");
        assertThat(response.getBody().getMessage()).contains("already exists");
    }

    @Test
    @Order(3)
    public void testGetRepository() {
        assertThat(createdRepositoryUuid).isNotNull();

        ResponseEntity<RepositoryApiResponse> response = restTemplate.getForEntity(
                "/api/repositories/" + createdRepositoryUuid,
                RepositoryApiResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNotNull();

        RepositoryResponse repo = response.getBody().getData();
        assertThat(repo.getUuid()).isEqualTo(createdRepositoryUuid);
        assertThat(repo.getSlug()).isEqualTo(repositorySlug);
    }

    @Test
    @Order(4)
    public void testGetRepositoriesByOwner() {
        assertThat(createdRepositoryUuid).isNotNull();

        ResponseEntity<RepositoryListApiResponse> response = restTemplate.getForEntity(
                "/api/users/" + ownerUuid + "/repositories",
                RepositoryListApiResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNotEmpty();
        assertThat(response.getBody().getData().get(0).getUuid()).isEqualTo(createdRepositoryUuid);
    }

    @Test
    @Order(5)
    public void testUpdateRepository() {
        assertThat(createdRepositoryUuid).isNotNull();

        UpdateRepositoryRequest request = UpdateRepositoryRequest.builder()
                .name("Awesome Core Repository") // display name changes
                .description("New updated desc")
                .visibility(RepositoryVisibility.PRIVATE)
                .build();

        HttpEntity<UpdateRepositoryRequest> entity = new HttpEntity<>(request);

        ResponseEntity<RepositoryApiResponse> response = restTemplate.exchange(
                "/api/repositories/" + createdRepositoryUuid,
                HttpMethod.PUT,
                entity,
                RepositoryApiResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNotNull();

        RepositoryResponse repo = response.getBody().getData();
        assertThat(repo.getName()).isEqualTo("Awesome Core Repository");
        assertThat(repo.getSlug()).isEqualTo(repositorySlug); // Slug remains stable!
        assertThat(repo.getVisibility()).isEqualTo(RepositoryVisibility.PRIVATE);
        assertThat(repo.getDescription()).isEqualTo("New updated desc");
    }

    @Test
    @Order(6)
    public void testArchiveRepository() {
        assertThat(createdRepositoryUuid).isNotNull();

        ResponseEntity<RepositoryApiResponse> response = restTemplate.exchange(
                "/api/repositories/" + createdRepositoryUuid + "/archive",
                HttpMethod.PUT,
                null,
                RepositoryApiResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNotNull();
        assertThat(response.getBody().getData().isArchived()).isTrue();
    }

    @Test
    @Order(7)
    public void testDeleteRepository() {
        assertThat(createdRepositoryUuid).isNotNull();

        ResponseEntity<RepositoryApiResponse> response = restTemplate.exchange(
                "/api/repositories/" + createdRepositoryUuid,
                HttpMethod.DELETE,
                null,
                RepositoryApiResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNotNull();
        assertThat(response.getBody().getData().getUuid()).isEqualTo(createdRepositoryUuid);

        // Verify soft-deleted repository acts as non-existent (yields 404)
        ResponseEntity<ErrorResponse> getResponse = restTemplate.getForEntity(
                "/api/repositories/" + createdRepositoryUuid,
                ErrorResponse.class
        );

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
