package com.example.apilab.repository.repository;

import com.example.apilab.repository.entity.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Repository("repositoryRepository")
public interface RepositoryRepository extends JpaRepository<Repository, Long> {
    Optional<Repository> findByIdAndIsDeletedFalse(Long id);
    Optional<Repository> findByUuidAndIsDeletedFalse(UUID uuid);
    Optional<Repository> findByOwnerUuidAndSlugAndIsDeletedFalse(UUID ownerUuid, String slug);
    boolean existsByOwnerUuidAndSlugAndIsDeletedFalse(UUID ownerUuid, String slug);
    List<Repository> findAllByOwnerUuidAndIsDeletedFalse(UUID ownerUuid);
}
