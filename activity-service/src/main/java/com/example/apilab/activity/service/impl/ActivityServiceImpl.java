package com.example.apilab.activity.service.impl;

import com.example.apilab.activity.dto.request.CreateActivityRequest;
import com.example.apilab.activity.dto.request.UpdateActivityRequest;
import com.example.apilab.activity.dto.response.ActivityResponse;
import com.example.apilab.activity.dto.response.ActivitySummaryResponse;
import com.example.apilab.activity.entity.Activity;
import com.example.apilab.activity.exception.ActivityNotFoundException;
import com.example.apilab.activity.mapper.ActivityMapper;
import com.example.apilab.activity.repository.ActivityRepository;
import com.example.apilab.activity.service.ActivityService;
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
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;

    @Override
    @Transactional
    public ActivityResponse create(UUID authorUuid, CreateActivityRequest request) {
        log.info("Creating activity for author: {}", authorUuid);
        Activity activity = activityMapper.toEntity(request);
        activity.setAuthorUuid(authorUuid);

        activity = activityRepository.save(activity);
        log.info("Activity created successfully with internal ID: {}", activity.getId());
        return activityMapper.toResponse(activity);
    }

    @Override
    @Transactional(readOnly = true)
    public ActivityResponse get(UUID uuid) {
        Activity activity = activityRepository.findByUuidAndIsDeletedFalse(uuid)
                .orElseThrow(() -> new ActivityNotFoundException("Activity not found with uuid: " + uuid));
        log.info("Fetched activity for internal ID: {}", activity.getId());
        return activityMapper.toResponse(activity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivitySummaryResponse> list(Pageable pageable) {
        log.info("Fetching public activities feed with parameters: {}", pageable);
        return activityRepository.findAllByIsDeletedFalse(pageable)
                .map(activityMapper::toSummaryResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivitySummaryResponse> listByUser(UUID authorUuid, Pageable pageable) {
        log.info("Fetching activities for author: {} with parameters: {}", authorUuid, pageable);
        return activityRepository.findAllByAuthorUuidAndIsDeletedFalse(authorUuid, pageable)
                .map(activityMapper::toSummaryResponse);
    }

    @Override
    @Transactional
    public ActivityResponse update(UUID uuid, UpdateActivityRequest request) {
        Activity activity = activityRepository.findByUuidAndIsDeletedFalse(uuid)
                .orElseThrow(() -> new ActivityNotFoundException("Activity not found with uuid: " + uuid));

        performInternalUpdate(activity.getId(), request);

        Activity refreshed = activityRepository.findByIdAndIsDeletedFalse(activity.getId())
                .orElseThrow(() -> new ActivityNotFoundException("Activity not found with id: " + activity.getId()));
        return activityMapper.toResponse(refreshed);
    }

    private void performInternalUpdate(Long id, UpdateActivityRequest request) {
        log.info("Performing internal update for activity ID: {}", id);
        Activity activity = activityRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ActivityNotFoundException("Activity not found with id: " + id));
        activityMapper.updateEntity(request, activity);
        activityRepository.save(activity);
        log.info("Internal update completed for activity ID: {}", id);
    }

    @Override
    @Transactional
    public ActivityResponse delete(UUID uuid) {
        Activity activity = activityRepository.findByUuidAndIsDeletedFalse(uuid)
                .orElseThrow(() -> new ActivityNotFoundException("Activity not found with uuid: " + uuid));

        log.info("Soft deleting activity with internal ID: {}", activity.getId());
        activity.setDeleted(true);
        activity.setDeletedAt(Instant.now());
        activity = activityRepository.save(activity);
        log.info("Soft delete completed for activity ID: {}", activity.getId());

        return activityMapper.toResponse(activity);
    }
}
