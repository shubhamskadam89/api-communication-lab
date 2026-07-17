# User Service

## Responsibility
Owns user profiles, metadata, and handles profile management endpoints.

## Why UUID + Long?
We use a dual-identifier strategy:
- **Internal `Long` ID**: Used as the database auto-incrementing primary key. It is highly optimized for index sequencing (B-Tree traversal), internal table joins, and is logged internally for system operations.
- **External `UUID` uuid**: Exposed to the public in DTO responses, endpoints, and exceptions. This prevents sequence enumeration attacks (resource guessing) and isolates consumers from our internal storage layout, making sharding or database migrations transparent to clients.

## Why Soft Delete?
Instead of physical SQL `DELETE` calls, we toggle `is_deleted = true`. This preserves data audit integrity, avoids breaking referential integrity boundaries with other services (like repository or activity feeds), and allows record recovery in production environments.

## API Endpoints

### 1. Create User
- **Method & URL**: `POST /api/users`
- **Body**:
  ```json
  {
    "username": "john_doe",
    "email": "john.doe@example.com",
    "displayName": "John Doe",
    "bio": "Software developer",
    "avatarUrl": "https://example.com/avatar.png"
  }
  ```
- **Response**: `201 Created`
  ```json
  {
    "uuid": "439281a8-9d41-4770-9854-e7417ececacb",
    "username": "john_doe",
    "email": "john.doe@example.com",
    "displayName": "John Doe",
    "bio": "Software developer",
    "avatarUrl": "https://example.com/avatar.png",
    "createdAt": "2026-07-17T13:45:22Z",
    "updatedAt": "2026-07-17T13:45:22Z"
  }
  ```

### 2. Get User
- **Method & URL**: `GET /api/users/{uuid}`
- **Response**: `200 OK`
  ```json
  {
    "uuid": "439281a8-9d41-4770-9854-e7417ececacb",
    "username": "john_doe",
    "email": "john.doe@example.com",
    "displayName": "John Doe",
    "bio": "Software developer",
    "avatarUrl": "https://example.com/avatar.png",
    "createdAt": "2026-07-17T13:45:22Z",
    "updatedAt": "2026-07-17T13:45:22Z"
  }
  ```

### 3. Get User Summary
- **Method & URL**: `GET /api/users/{uuid}/summary`
- **Response**: `200 OK`
  ```json
  {
    "uuid": "439281a8-9d41-4770-9854-e7417ececacb",
    "username": "john_doe",
    "displayName": "John Doe",
    "avatarUrl": "https://example.com/avatar.png"
  }
  ```

### 4. Update User
- **Method & URL**: `PUT /api/users/{uuid}`
- **Body**:
  ```json
  {
    "displayName": "Johnathan Doe",
    "bio": "Full-stack engineer",
    "avatarUrl": "https://example.com/avatar_new.png"
  }
  ```
- **Response**: `200 OK`

### 5. Delete User (Soft Delete)
- **Method & URL**: `DELETE /api/users/{uuid}`
- **Response**: `204 No Content`

---

## Database Schema
The database migrations are managed using Flyway. The schema consists of a single `users` table:

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    display_name VARCHAR(255),
    bio TEXT,
    avatar_url VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_uuid ON users(uuid);
CREATE INDEX idx_users_display_name ON users(display_name);
```

---

## Future Evolution
- **REST today**: Initial client-facing CRUD services.
- **gRPC consumers later**: Enabling high-frequency low-latency internal queries from gateway and repository aggregators.
- **Kafka publisher later**: Emitting events on user profile updates or creations.
