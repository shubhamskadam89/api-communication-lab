package com.example.apilab.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "repositories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Repository extends BaseEntity {

    @Column(nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "owner_uuid", nullable = false)
    private UUID ownerUuid;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 120)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private RepositoryVisibility visibility;

    @Column(name = "default_branch", nullable = false, length = 50)
    @Builder.Default
    private String defaultBranch = "main";

    @Column(name = "primary_language", length = 50)
    private String primaryLanguage;

    @Column(name = "is_archived", nullable = false)
    @Builder.Default
    private boolean isArchived = false;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @PrePersist
    @Override
    protected void onCreate() {
        super.onCreate();
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
        if (defaultBranch == null) {
            defaultBranch = "main";
        }
    }
}
