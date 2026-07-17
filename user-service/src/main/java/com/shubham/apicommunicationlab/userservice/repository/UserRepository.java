package com.shubham.apicommunicationlab.userservice.repository;

import com.shubham.apicommunicationlab.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndIsDeletedFalse(Long id);
    Optional<User> findByUuidAndIsDeletedFalse(UUID uuid);
    Optional<User> findByUsernameAndIsDeletedFalse(String username);
}
