package com.example.apilab.activity.controller;

import com.example.apilab.activity.dto.request.CreateActivityRequest;
import com.example.apilab.activity.dto.request.UpdateActivityRequest;
import com.example.apilab.activity.dto.response.ApiResponse;
import com.example.apilab.activity.dto.response.ActivityResponse;
import com.example.apilab.activity.dto.response.ActivitySummaryResponse;
import com.example.apilab.activity.service.ActivityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping("/api/v1/users/{authorUuid}/activities")
    public ResponseEntity<ApiResponse<ActivityResponse>> create(
            @PathVariable UUID authorUuid,
            @RequestBody @Valid CreateActivityRequest request,
            HttpServletRequest servletRequest) {
        log.info("REST Request - Create activity for author: {}", authorUuid);
        ActivityResponse response = activityService.create(authorUuid, request);
        ApiResponse<ActivityResponse> apiResponse = ApiResponse.success(
                HttpStatus.CREATED.value(),
                "Activity created successfully",
                response,
                servletRequest.getRequestURI()
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/activities/{uuid}")
    public ResponseEntity<ApiResponse<ActivityResponse>> get(
            @PathVariable UUID uuid,
            HttpServletRequest servletRequest) {
        log.info("REST Request - Get activity by uuid: {}", uuid);
        ActivityResponse response = activityService.get(uuid);
        ApiResponse<ActivityResponse> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(),
                "Activity retrieved successfully",
                response,
                servletRequest.getRequestURI()
        );
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/api/v1/activities")
    public ResponseEntity<ApiResponse<Page<ActivitySummaryResponse>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort,
            HttpServletRequest servletRequest) {
        log.info("REST Request - Get paginated activities feed, page: {}, size: {}, sort: {}", page, size, sort);

        int boundedSize = Math.min(size, 100);
        Pageable pageable = parsePageable(page, boundedSize, sort);
        Page<ActivitySummaryResponse> response = activityService.list(pageable);

        ApiResponse<Page<ActivitySummaryResponse>> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(),
                "Activities feed retrieved successfully",
                response,
                servletRequest.getRequestURI()
        );
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/api/v1/users/{authorUuid}/activities")
    public ResponseEntity<ApiResponse<Page<ActivitySummaryResponse>>> listByUser(
            @PathVariable UUID authorUuid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort,
            HttpServletRequest servletRequest) {
        log.info("REST Request - Get paginated activities for user: {}, page: {}, size: {}, sort: {}", authorUuid, page, size, sort);

        int boundedSize = Math.min(size, 100);
        Pageable pageable = parsePageable(page, boundedSize, sort);
        Page<ActivitySummaryResponse> response = activityService.listByUser(authorUuid, pageable);

        ApiResponse<Page<ActivitySummaryResponse>> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(),
                "User activities retrieved successfully",
                response,
                servletRequest.getRequestURI()
        );
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/api/v1/activities/{uuid}")
    public ResponseEntity<ApiResponse<ActivityResponse>> update(
            @PathVariable UUID uuid,
            @RequestBody @Valid UpdateActivityRequest request,
            HttpServletRequest servletRequest) {
        log.info("REST Request - Update activity with uuid: {}", uuid);
        ActivityResponse response = activityService.update(uuid, request);
        ApiResponse<ActivityResponse> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(),
                "Activity updated successfully",
                response,
                servletRequest.getRequestURI()
        );
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/api/v1/activities/{uuid}")
    public ResponseEntity<ApiResponse<ActivityResponse>> delete(
            @PathVariable UUID uuid,
            HttpServletRequest servletRequest) {
        log.info("REST Request - Delete activity with uuid: {}", uuid);
        ActivityResponse response = activityService.delete(uuid);
        ApiResponse<ActivityResponse> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(),
                "Activity deleted successfully",
                response,
                servletRequest.getRequestURI()
        );
        return ResponseEntity.ok(apiResponse);
    }

    private Pageable parsePageable(int page, int size, String sort) {
        String[] sortParams = sort.split(",");
        String sortProperty = sortParams[0];
        Sort.Direction sortDirection = Sort.Direction.DESC;
        if (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")) {
            sortDirection = Sort.Direction.ASC;
        }
        return PageRequest.of(page, size, Sort.by(sortDirection, sortProperty));
    }
}
