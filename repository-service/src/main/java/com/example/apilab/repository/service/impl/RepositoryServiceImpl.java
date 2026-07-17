package com.example.apilab.repository.service.impl;

import com.example.apilab.repository.dto.request.CreateRepositoryRequest;
import com.example.apilab.repository.dto.request.UpdateRepositoryRequest;
import com.example.apilab.repository.dto.response.RepositoryResponse;
import com.example.apilab.repository.entity.Repository;
import com.example.apilab.repository.exception.DuplicateRepositoryException;
import com.example.apilab.repository.exception.RepositoryNotFoundException;
import com.example.apilab.repository.mapper.RepositoryMapper;
import com.example.apilab.repository.repository.RepositoryRepository;
import com.example.apilab.repository.service.RepositoryService;
import com.example.apilab.repository.util.SlugGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RepositoryServiceImpl implements RepositoryService {

    private final RepositoryRepository repositoryRepository;
    private final RepositoryMapper repositoryMapper;

    @Override
    @Transactional
    public RepositoryResponse createRepository(UUID ownerUuid, CreateRepositoryRequest request) {
        log.info("Creating repository with name: {} for owner: {}", request.getName(), ownerUuid);

        String slug = SlugGenerator.generate(request.getName());
        if (repositoryRepository.existsByOwnerUuidAndSlugAndIsDeletedFalse(ownerUuid, slug)) {
            throw new DuplicateRepositoryException("Repository with slug '" + slug + "' already exists for owner: " + ownerUuid);
        }

        Repository repository = repositoryMapper.toEntity(request);
        repository.setOwnerUuid(ownerUuid);
        repository.setCreatedBy(ownerUuid); // In Phase 1, created_by equals owner_uuid

        repository = repositoryRepository.save(repository);
        log.info("Repository created successfully with internal ID: {}", repository.getId());

        return repositoryMapper.toResponse(repository);
    }

    @Override
    @Transactional(readOnly = true)
    public RepositoryResponse getRepositoryByUuid(UUID uuid) {
        Repository repository = repositoryRepository.findByUuidAndIsDeletedFalse(uuid)
                .orElseThrow(() -> new RepositoryNotFoundException("Repository not found with uuid: " + uuid));
        log.info("Fetched repository for internal ID: {}", repository.getId());
        return repositoryMapper.toResponse(repository);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RepositoryResponse> getRepositoriesByOwner(UUID ownerUuid, Pageable pageable) {
        log.info("Fetching paginated repositories for owner: {} with parameters: {}", ownerUuid, pageable);
        return repositoryRepository.findAllByOwnerUuidAndIsDeletedFalse(ownerUuid, pageable)
                .map(repositoryMapper::toResponse);
    }

    @Override
    @Transactional
    public RepositoryResponse updateRepository(UUID uuid, UpdateRepositoryRequest request) {
        Repository repository = repositoryRepository.findByUuidAndIsDeletedFalse(uuid)
                .orElseThrow(() -> new RepositoryNotFoundException("Repository not found with uuid: " + uuid));

        performInternalUpdate(repository.getId(), request);

        Repository refreshed = repositoryRepository.findByIdAndIsDeletedFalse(repository.getId())
                .orElseThrow(() -> new RepositoryNotFoundException("Repository not found with id: " + repository.getId()));
        return repositoryMapper.toResponse(refreshed);
    }

    private void performInternalUpdate(Long id, UpdateRepositoryRequest request) {
        log.info("Performing internal update for repository ID: {}", id);
        Repository repository = repositoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RepositoryNotFoundException("Repository not found with id: " + id));
        repositoryMapper.updateEntity(request, repository);
        repositoryRepository.save(repository);
        log.info("Internal update completed for repository ID: {}", id);
    }

    @Override
    @Transactional
    public RepositoryResponse archiveRepository(UUID uuid) {
        Repository repository = repositoryRepository.findByUuidAndIsDeletedFalse(uuid)
                .orElseThrow(() -> new RepositoryNotFoundException("Repository not found with uuid: " + uuid));

        performInternalArchive(repository.getId());

        Repository refreshed = repositoryRepository.findByIdAndIsDeletedFalse(repository.getId())
                .orElseThrow(() -> new RepositoryNotFoundException("Repository not found with id: " + repository.getId()));
        return repositoryMapper.toResponse(refreshed);
    }

    private void performInternalArchive(Long id) {
        log.info("Performing internal archive for repository ID: {}", id);
        Repository repository = repositoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RepositoryNotFoundException("Repository not found with id: " + id));
        repository.setArchived(true);
        repositoryRepository.save(repository);
        log.info("Internal archive completed for repository ID: {}", id);
    }

    @Override
    @Transactional
    public RepositoryResponse deleteRepository(UUID uuid) {
        Repository repository = repositoryRepository.findByUuidAndIsDeletedFalse(uuid)
                .orElseThrow(() -> new RepositoryNotFoundException("Repository not found with uuid: " + uuid));

        log.info("Soft deleting repository with internal ID: {}", repository.getId());
        repository.setDeleted(true);
        repository.setDeletedAt(Instant.now());
        repository = repositoryRepository.save(repository);
        log.info("Soft delete completed for repository ID: {}", repository.getId());

        return repositoryMapper.toResponse(repository);
    }
}
