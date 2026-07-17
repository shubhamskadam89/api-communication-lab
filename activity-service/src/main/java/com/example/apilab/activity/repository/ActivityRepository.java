package com.example.apilab.activity.repository;

import com.example.apilab.activity.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Repository("activityRepository")
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Optional<Activity> findByIdAndIsDeletedFalse(Long id);
    Optional<Activity> findByUuidAndIsDeletedFalse(UUID uuid);
    Page<Activity> findAllByIsDeletedFalse(Pageable pageable);
    Page<Activity> findAllByAuthorUuidAndIsDeletedFalse(UUID authorUuid, Pageable pageable);
}
