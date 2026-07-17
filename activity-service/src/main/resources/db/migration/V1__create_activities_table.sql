CREATE TABLE activities (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    author_uuid UUID NOT NULL,
    content TEXT NOT NULL,
    visibility VARCHAR(20) NOT NULL,
    like_count BIGINT NOT NULL DEFAULT 0,
    comment_count BIGINT NOT NULL DEFAULT 0,
    media_urls JSONB,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    deleted_at TIMESTAMP WITH TIME ZONE NULL
);

CREATE INDEX idx_activities_uuid ON activities(uuid);
CREATE INDEX idx_activities_author_uuid ON activities(author_uuid);
CREATE INDEX idx_activities_created_at ON activities(created_at);
CREATE INDEX idx_activities_author_uuid_created_at ON activities(author_uuid, created_at);
