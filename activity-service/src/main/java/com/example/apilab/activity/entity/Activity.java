package com.example.apilab.activity.entity;

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
@Table(name = "activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "author_uuid", nullable = false)
    private UUID authorUuid;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ActivityVisibility visibility;

    @Column(name = "like_count", nullable = false)
    @Builder.Default
    private long likeCount = 0;

    @Column(name = "comment_count", nullable = false)
    @Builder.Default
    private long commentCount = 0;

    @Column(name = "media_urls", columnDefinition = "TEXT")
    private String mediaUrls;

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
    }
}
