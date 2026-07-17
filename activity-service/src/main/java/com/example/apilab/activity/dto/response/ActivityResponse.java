package com.example.apilab.activity.dto.response;

import com.example.apilab.activity.entity.ActivityVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityResponse {
    private UUID uuid;
    private UUID authorUuid;
    private String content;
    private ActivityVisibility visibility;
    private long likeCount;
    private long commentCount;
    private List<String> mediaUrls;
    private Instant createdAt;
    private Instant updatedAt;
}
