package com.example.apilab.activity.service;

import com.example.apilab.activity.dto.request.CreateActivityRequest;
import com.example.apilab.activity.dto.request.UpdateActivityRequest;
import com.example.apilab.activity.dto.response.ActivityResponse;
import com.example.apilab.activity.dto.response.ActivitySummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ActivityService {
    ActivityResponse create(UUID authorUuid, CreateActivityRequest request);
    ActivityResponse get(UUID uuid);
    Page<ActivitySummaryResponse> list(Pageable pageable);
    Page<ActivitySummaryResponse> listByUser(UUID authorUuid, Pageable pageable);
    ActivityResponse update(UUID uuid, UpdateActivityRequest request);
    ActivityResponse delete(UUID uuid);
}
