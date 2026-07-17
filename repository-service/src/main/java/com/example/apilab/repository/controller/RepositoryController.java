package com.example.apilab.repository.controller;

import com.example.apilab.repository.dto.request.CreateRepositoryRequest;
import com.example.apilab.repository.dto.request.UpdateRepositoryRequest;
import com.example.apilab.repository.dto.response.ApiResponse;
import com.example.apilab.repository.dto.response.RepositoryResponse;
import com.example.apilab.repository.service.RepositoryService;
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
public class RepositoryController {

    private final RepositoryService repositoryService;

    @PostMapping("/api/users/{ownerUuid}/repositories")
    public ResponseEntity<ApiResponse<RepositoryResponse>> createRepository(
            @PathVariable UUID ownerUuid,
            @RequestBody @Valid CreateRepositoryRequest request,
            HttpServletRequest servletRequest) {
        log.info("REST Request - Create repository for owner: {}", ownerUuid);
        RepositoryResponse response = repositoryService.createRepository(ownerUuid, request);
        ApiResponse<RepositoryResponse> apiResponse = ApiResponse.success(
                HttpStatus.CREATED.value(),
                "Repository created successfully",
                response,
                servletRequest.getRequestURI()
            );
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/api/users/{ownerUuid}/repositories")
    public ResponseEntity<ApiResponse<Page<RepositoryResponse>>> getRepositoriesByOwner(
            @PathVariable UUID ownerUuid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort,
            HttpServletRequest servletRequest) {
        log.info("REST Request - Get paginated repositories for owner: {}, page: {}, size: {}, sort: {}", ownerUuid, page, size, sort);

        // Parse sort parameters
        String[] sortParams = sort.split(",");
        String sortProperty = sortParams[0];
        Sort.Direction sortDirection = Sort.Direction.DESC;
        if (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")) {
            sortDirection = Sort.Direction.ASC;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortProperty));
        Page<RepositoryResponse> response = repositoryService.getRepositoriesByOwner(ownerUuid, pageable);

        ApiResponse<Page<RepositoryResponse>> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(),
                "Repositories retrieved successfully",
                response,
                servletRequest.getRequestURI()
        );
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/api/repositories/{repositoryUuid}")
    public ResponseEntity<ApiResponse<RepositoryResponse>> getRepositoryByUuid(
            @PathVariable UUID repositoryUuid,
            HttpServletRequest servletRequest) {
        log.info("REST Request - Get repository by uuid: {}", repositoryUuid);
        RepositoryResponse response = repositoryService.getRepositoryByUuid(repositoryUuid);
        ApiResponse<RepositoryResponse> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(),
                "Repository retrieved successfully",
                response,
                servletRequest.getRequestURI()
        );
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/api/repositories/{repositoryUuid}")
    public ResponseEntity<ApiResponse<RepositoryResponse>> updateRepository(
            @PathVariable UUID repositoryUuid,
            @RequestBody @Valid UpdateRepositoryRequest request,
            HttpServletRequest servletRequest) {
        log.info("REST Request - Update repository with uuid: {}", repositoryUuid);
        RepositoryResponse response = repositoryService.updateRepository(repositoryUuid, request);
        ApiResponse<RepositoryResponse> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(),
                "Repository updated successfully",
                response,
                servletRequest.getRequestURI()
        );
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/api/repositories/{repositoryUuid}/archive")
    public ResponseEntity<ApiResponse<RepositoryResponse>> archiveRepository(
            @PathVariable UUID repositoryUuid,
            HttpServletRequest servletRequest) {
        log.info("REST Request - Archive repository with uuid: {}", repositoryUuid);
        RepositoryResponse response = repositoryService.archiveRepository(repositoryUuid);
        ApiResponse<RepositoryResponse> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(),
                "Repository archived successfully",
                response,
                servletRequest.getRequestURI()
        );
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/api/repositories/{repositoryUuid}")
    public ResponseEntity<ApiResponse<RepositoryResponse>> deleteRepository(
            @PathVariable UUID repositoryUuid,
            HttpServletRequest servletRequest) {
        log.info("REST Request - Delete repository with uuid: {}", repositoryUuid);
        RepositoryResponse response = repositoryService.deleteRepository(repositoryUuid);
        ApiResponse<RepositoryResponse> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(),
                "Repository deleted successfully",
                response,
                servletRequest.getRequestURI()
        );
        return ResponseEntity.ok(apiResponse);
    }
}
