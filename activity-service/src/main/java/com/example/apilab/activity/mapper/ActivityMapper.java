package com.example.apilab.activity.mapper;

import com.example.apilab.activity.dto.request.CreateActivityRequest;
import com.example.apilab.activity.dto.request.UpdateActivityRequest;
import com.example.apilab.activity.dto.response.ActivityResponse;
import com.example.apilab.activity.dto.response.ActivitySummaryResponse;
import com.example.apilab.activity.entity.Activity;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class ActivityMapper {

    public Activity toEntity(CreateActivityRequest request) {
        if (request == null) {
            return null;
        }
        Activity activity = new Activity();
        activity.setContent(request.getContent());
        activity.setVisibility(request.getVisibility());
        activity.setMediaUrls(request.getMediaUrls());
        return activity;
    }

    public ActivityResponse toResponse(Activity activity) {
        if (activity == null) {
            return null;
        }
        return ActivityResponse.builder()
                .uuid(activity.getUuid())
                .authorUuid(activity.getAuthorUuid())
                .content(activity.getContent())
                .visibility(activity.getVisibility())
                .likeCount(activity.getLikeCount())
                .commentCount(activity.getCommentCount())
                .mediaUrls(activity.getMediaUrls() != null ? activity.getMediaUrls() : Collections.emptyList())
                .createdAt(activity.getCreatedAt())
                .updatedAt(activity.getUpdatedAt())
                .build();
    }

    public ActivitySummaryResponse toSummaryResponse(Activity activity) {
        if (activity == null) {
            return null;
        }
        return ActivitySummaryResponse.builder()
                .uuid(activity.getUuid())
                .authorUuid(activity.getAuthorUuid())
                .content(activity.getContent())
                .createdAt(activity.getCreatedAt())
                .build();
    }

    public void updateEntity(UpdateActivityRequest request, Activity activity) {
        if (request == null || activity == null) {
            return;
        }
        if (request.getContent() != null) {
            activity.setContent(request.getContent());
        }
        if (request.getVisibility() != null) {
            activity.setVisibility(request.getVisibility());
        }
        if (request.getMediaUrls() != null) {
            activity.setMediaUrls(request.getMediaUrls());
        }
    }
}
