# Repository Service

## Responsibility
Owns git repository metadata, access bounds, and handles repository lifecycle and search indexing endpoints.

## Why UUID + Long?
We use a dual-identifier layout:
- **Internal `Long` ID**: Used as the database auto-incrementing primary key. Highly optimized for B-Tree indexes, database joins, and logged internally for trace monitoring.
- **External `UUID` uuid**: Exposed to clients in DTO responses, exceptions, and endpoints. Prevents resource enumeration attacks and insulates client integrations from our database storage structure.

## Why Soft Delete?
Instead of physical SQL `DELETE` calls, the service toggles `is_deleted = true` and logs `deleted_at`. This protects historical audit paths, avoids breaking references in linked services (e.g. Activity feed), and enables clean record recovery in production. All public query methods automatically filter out soft-deleted entities.

## Archiving vs Deleting
- **Archiving**: Toggles `is_archived = true`. Archived repositories remain publicly read-only (endpoints return their data) but block write actions like pushing commits (modeled for future stages).
- **Deleting**: Soft-deletes the repository, immediately hiding it from all normal lookups (yields `404 Not Found`).

## Ownership vs Creation
The service makes a deliberate distinction between:
- `owner_uuid`: Identifies the current owner of the repository.
- `created_by`: Records the user who originally created the repository (immutable).

In Phase 1, both values are populated identically. They will diverge in future evolution scenarios such as repository ownership transfers, organization group ownerships, or system template creations. Designing this distinction now prevents disruptive future database migrations.

## Name Renaming & Slug Stability
When a repository is created, we generate an initial URL-safe lowercase `slug` from the `name` (using the `SlugGenerator` utility).
If a repository is renamed later, **only the `name` display name updates, while the `slug` remains stable**. This ensures bookmarks, shared links, and API integrations do not break when name revisions occur.

---

## API Endpoints

### 1. Create Repository
- **Method & URL**: `POST /api/users/{ownerUuid}/repositories`
- **Body**:
  ```json
  {
    "name": "Spring Boot Core",
    "description": "Core spring boot project",
    "visibility": "PUBLIC",
    "primaryLanguage": "Java"
  }
  ```
- **Response**: `201 Created`

### 2. Get Repository
- **Method & URL**: `GET /api/repositories/{uuid}`
- **Response**: `200 OK`

### 3. Get Repositories by Owner
- **Method & URL**: `GET /api/users/{ownerUuid}/repositories`
- **Response**: `200 OK`

### 4. Update Repository
- **Method & URL**: `PUT /api/repositories/{uuid}`
- **Body**:
  ```json
  {
    "name": "Awesome Core Project",
    "description": "Updated project description",
    "visibility": "PRIVATE",
    "primaryLanguage": "Kotlin"
  }
  ```
- **Response**: `200 OK`

### 5. Archive Repository
- **Method & URL**: `PUT /api/repositories/{uuid}/archive`
- **Response**: `200 OK`

### 6. Delete Repository
- **Method & URL**: `DELETE /api/repositories/{uuid}`
- **Response**: `200 OK` (Returns soft-deleted record payload)

---

## Database Schema

```sql
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
```

---

## Future Evolution
- **REST today**: Exposed CRUD nested REST resource hierarchy.
- **gRPC consumers later**: Enabling high-frequency low-latency internal queries from gateway and aggregators.
- **Kafka publisher later**: Emitting events on repository creations, archives, and deletes.
