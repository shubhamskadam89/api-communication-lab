package com.example.apilab.repository.mapper;

import com.example.apilab.repository.dto.request.CreateRepositoryRequest;
import com.example.apilab.repository.dto.request.UpdateRepositoryRequest;
import com.example.apilab.repository.dto.response.RepositoryResponse;
import com.example.apilab.repository.entity.Repository;
import com.example.apilab.repository.util.SlugGenerator;
import org.springframework.stereotype.Component;

@Component
public class RepositoryMapper {

    public Repository toEntity(CreateRepositoryRequest request) {
        if (request == null) {
            return null;
        }
        Repository repository = new Repository();
        repository.setName(request.getName());
        repository.setSlug(SlugGenerator.generate(request.getName()));
        repository.setDescription(request.getDescription());
        repository.setVisibility(request.getVisibility());
        repository.setPrimaryLanguage(request.getPrimaryLanguage());
        return repository;
    }

    public RepositoryResponse toResponse(Repository repository) {
        if (repository == null) {
            return null;
        }
        return RepositoryResponse.builder()
                .uuid(repository.getUuid())
                .ownerUuid(repository.getOwnerUuid())
                .createdByUuid(repository.getCreatedBy())
                .name(repository.getName())
                .slug(repository.getSlug())
                .description(repository.getDescription())
                .visibility(repository.getVisibility())
                .defaultBranch(repository.getDefaultBranch())
                .primaryLanguage(repository.getPrimaryLanguage())
                .isArchived(repository.isArchived())
                .createdAt(repository.getCreatedAt())
                .updatedAt(repository.getUpdatedAt())
                .build();
    }

    public void updateEntity(UpdateRepositoryRequest request, Repository repository) {
        if (request == null || repository == null) {
            return;
        }
        if (request.getName() != null) {
            repository.setName(request.getName());
            // Slug is a stable identifier and is not changed when display name changes.
        }
        if (request.getDescription() != null) {
            repository.setDescription(request.getDescription());
        }
        if (request.getVisibility() != null) {
            repository.setVisibility(request.getVisibility());
        }
        if (request.getPrimaryLanguage() != null) {
            repository.setPrimaryLanguage(request.getPrimaryLanguage());
        }
        if (request.getIsArchived() != null) {
            repository.setArchived(request.getIsArchived());
        }
    }
}
