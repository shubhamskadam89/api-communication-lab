package com.example.apilab.repository.dto.response;

import com.example.apilab.repository.entity.RepositoryVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryResponse {
    private UUID uuid;
    private UUID ownerUuid;
    private UUID createdBy;
    private String name;
    private String slug;
    private String description;
    private RepositoryVisibility visibility;
    private String defaultBranch;
    private String primaryLanguage;
    private boolean isArchived;
    private Instant createdAt;
    private Instant updatedAt;
}
