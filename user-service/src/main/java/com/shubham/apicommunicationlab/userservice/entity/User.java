package com.shubham.apicommunicationlab.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private UUID uuid;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "display_name")
    private String displayName;

    private String bio;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    @Version
    @Column(nullable = false)
    private Long version;

    @PrePersist
    @Override
    protected void onCreate() {
        super.onCreate();
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return uuid != null && uuid.equals(user.getUuid());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}