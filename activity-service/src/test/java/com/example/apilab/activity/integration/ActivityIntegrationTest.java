package com.example.apilab.activity.integration;

import com.example.apilab.activity.dto.request.CreateActivityRequest;
import com.example.apilab.activity.dto.request.UpdateActivityRequest;
import com.example.apilab.activity.dto.response.ApiResponse;
import com.example.apilab.activity.dto.response.ActivityResponse;
import com.example.apilab.activity.dto.response.ActivitySummaryResponse;
import com.example.apilab.activity.entity.ActivityVisibility;
import com.example.apilab.activity.exception.ErrorResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class ActivityIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final UUID authorUuid = UUID.randomUUID();
    private static UUID createdActivityUuid;

    // Helper classes to avoid generic type mapping problems with TestRestTemplate
    public static class ActivityApiResponse extends ApiResponse<ActivityResponse> {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RestPage<T> {
        private List<T> content;
        private int number;
        private int size;
        private long totalElements;
        private int totalPages;

        public List<T> getContent() { return content; }
        public void setContent(List<T> content) { this.content = content; }
        public int getNumber() { return number; }
        public void setNumber(int number) { this.number = number; }
        public int getSize() { return size; }
        public void setSize(int size) { this.size = size; }
        public long getTotalElements() { return totalElements; }
        public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
        public int getTotalPages() { return totalPages; }
        public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    }

    public static class ActivityPageApiResponse extends ApiResponse<RestPage<ActivitySummaryResponse>> {}

    @Test
    @Order(1)
    public void testCreateActivity() {
        CreateActivityRequest request = CreateActivityRequest.builder()
                .content("Initial activity post content")
                .visibility(ActivityVisibility.PUBLIC)
                .mediaUrls(List.of("https://example.com/pic1.jpg", "https://example.com/pic2.jpg"))
                .build();

        ResponseEntity<ActivityApiResponse> response = restTemplate.postForEntity(
                "/api/v1/users/" + authorUuid + "/activities",
                request,
                ActivityApiResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getBody().getMessage()).contains("created successfully");
        assertThat(response.getBody().getData()).isNotNull();

        ActivityResponse act = response.getBody().getData();
        assertThat(act.getUuid()).isNotNull();
        assertThat(act.getAuthorUuid()).isEqualTo(authorUuid);
        assertThat(act.getContent()).isEqualTo("Initial activity post content");
        assertThat(act.getVisibility()).isEqualTo(ActivityVisibility.PUBLIC);
        assertThat(act.getMediaUrls()).containsExactly("https://example.com/pic1.jpg", "https://example.com/pic2.jpg");

        createdActivityUuid = act.getUuid();
    }

    @Test
    @Order(2)
    public void testCreateActivity_ValidationFail() {
        CreateActivityRequest request = CreateActivityRequest.builder()
                .content("") // Blank content validation
                .visibility(ActivityVisibility.PUBLIC)
                .build();

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/api/v1/users/" + authorUuid + "/activities",
                request,
                ErrorResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getBody().getMessage()).contains("Validation failed");
    }

    @Test
    @Order(3)
    public void testGetActivity() {
        assertThat(createdActivityUuid).isNotNull();

        ResponseEntity<ActivityApiResponse> response = restTemplate.getForEntity(
                "/api/v1/activities/" + createdActivityUuid,
                ActivityApiResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNotNull();

        ActivityResponse act = response.getBody().getData();
        assertThat(act.getUuid()).isEqualTo(createdActivityUuid);
        assertThat(act.getContent()).isEqualTo("Initial activity post content");
    }

    @Test
    @Order(4)
    public void testListPublicFeed() {
        assertThat(createdActivityUuid).isNotNull();

        ResponseEntity<ActivityPageApiResponse> response = restTemplate.getForEntity(
                "/api/v1/activities?page=0&size=10",
                ActivityPageApiResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNotNull();
        assertThat(response.getBody().getData().getContent()).isNotEmpty();
        assertThat(response.getBody().getData().getContent().get(0).getUuid()).isEqualTo(createdActivityUuid);
    }

    @Test
    @Order(5)
    public void testListUserActivities() {
        assertThat(createdActivityUuid).isNotNull();

        ResponseEntity<ActivityPageApiResponse> response = restTemplate.getForEntity(
                "/api/v1/users/" + authorUuid + "/activities?page=0&size=10",
                ActivityPageApiResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNotNull();
        assertThat(response.getBody().getData().getContent()).isNotEmpty();
        assertThat(response.getBody().getData().getContent().get(0).getUuid()).isEqualTo(createdActivityUuid);
    }

    @Test
    @Order(6)
    public void testUpdateActivity() {
        assertThat(createdActivityUuid).isNotNull();

        UpdateActivityRequest request = UpdateActivityRequest.builder()
                .content("Updated activity content here")
                .visibility(ActivityVisibility.PRIVATE)
                .mediaUrls(List.of("https://example.com/updated.jpg"))
                .build();

        HttpEntity<UpdateActivityRequest> entity = new HttpEntity<>(request);

        ResponseEntity<ActivityApiResponse> response = restTemplate.exchange(
                "/api/v1/activities/" + createdActivityUuid,
                HttpMethod.PUT,
                entity,
                ActivityApiResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNotNull();

        ActivityResponse act = response.getBody().getData();
        assertThat(act.getContent()).isEqualTo("Updated activity content here");
        assertThat(act.getVisibility()).isEqualTo(ActivityVisibility.PRIVATE);
        assertThat(act.getMediaUrls()).containsExactly("https://example.com/updated.jpg");
    }

    @Test
    @Order(7)
    public void testDeleteActivity() {
        assertThat(createdActivityUuid).isNotNull();

        ResponseEntity<ActivityApiResponse> response = restTemplate.exchange(
                "/api/v1/activities/" + createdActivityUuid,
                HttpMethod.DELETE,
                null,
                ActivityApiResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNotNull();
        assertThat(response.getBody().getData().getUuid()).isEqualTo(createdActivityUuid);

        // Verify soft-deleted activity acts as non-existent (yields 404)
        ResponseEntity<ErrorResponse> getResponse = restTemplate.getForEntity(
                "/api/v1/activities/" + createdActivityUuid,
                ErrorResponse.class
        );

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
