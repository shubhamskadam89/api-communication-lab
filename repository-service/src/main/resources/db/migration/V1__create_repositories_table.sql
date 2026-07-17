CREATE TABLE repositories (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    owner_uuid UUID NOT NULL,
    created_by UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(120) NOT NULL,
    description TEXT,
    visibility VARCHAR(20) NOT NULL,
    default_branch VARCHAR(50) NOT NULL DEFAULT 'main',
    primary_language VARCHAR(50),
    is_archived BOOLEAN NOT NULL DEFAULT FALSE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    deleted_at TIMESTAMP WITH TIME ZONE NULL,
    CONSTRAINT unique_owner_slug UNIQUE (owner_uuid, slug)
);

CREATE INDEX idx_repositories_owner_uuid ON repositories(owner_uuid);
CREATE INDEX idx_repositories_created_by ON repositories(created_by);
CREATE INDEX idx_repositories_created_at ON repositories(created_at);
CREATE INDEX idx_repositories_visibility ON repositories(visibility);
