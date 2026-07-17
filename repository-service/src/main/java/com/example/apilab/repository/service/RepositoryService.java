package com.example.apilab.repository.service;

import com.example.apilab.repository.dto.request.CreateRepositoryRequest;
import com.example.apilab.repository.dto.request.UpdateRepositoryRequest;
import com.example.apilab.repository.dto.response.RepositoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface RepositoryService {
    RepositoryResponse createRepository(UUID ownerUuid, CreateRepositoryRequest request);
    RepositoryResponse getRepositoryByUuid(UUID uuid);
    Page<RepositoryResponse> getRepositoriesByOwner(UUID ownerUuid, Pageable pageable);
    RepositoryResponse updateRepository(UUID uuid, UpdateRepositoryRequest request);
    RepositoryResponse archiveRepository(UUID uuid);
    RepositoryResponse deleteRepository(UUID uuid);
}
