# Activity Service

## Responsibility
Owns user activity records (social posts, feed items), manages visibility states, and handles CRUD operations for the activity feed.

## Why UUID + Long?
We use a dual-identifier strategy:
- **Internal `Long` ID**: Used as the database auto-incrementing primary key. Highly optimized for sequential B-Tree indexing, database joins, and logged internally for trace analysis.
- **External `UUID` uuid**: Exposed to clients in DTO responses, endpoints, and exception paths. Isolates internal primary keys from public client integrations to prevent enumeration attacks.

## Why Soft Delete?
Instead of physical SQL `DELETE` calls, we toggle `is_deleted = true` and log `deleted_at`. This maintains historical audit integrity, prevents broken references in related gateways, and allows clean record recovery. All public query methods filter out soft-deleted activities.

## Why authorUuid instead of FK?
The user profile owns author metadata and resides in `user-service`. In a microservices architecture, crossing service boundaries with SQL Foreign Keys violates database isolation and microservice autonomy. Instead, we store the `authorUuid` as a logical reference and resolve user metadata asynchronously or via gateway-level REST/gRPC compositions.

---

## API Endpoints

### 1. Create Activity
- **Method & URL**: `POST /api/users/{authorUuid}/activities`
- **Body**:
  ```json
  {
    "content": "Just launched a new Spring Boot microservice!",
    "visibility": "PUBLIC",
    "mediaUrls": ["https://example.com/screenshot.png"]
  }
  ```
- **Response**: `201 Created`

### 2. Get Activity
- **Method & URL**: `GET /api/activities/{uuid}`
- **Response**: `200 OK`

### 3. List Public Feed
- **Method & URL**: `GET /api/activities?page=0&size=10`
- **Response**: `200 OK`

### 4. List User Activities
- **Method & URL**: `GET /api/users/{authorUuid}/activities?page=0&size=10`
- **Response**: `200 OK`

### 5. Update Activity
- **Method & URL**: `PUT /api/activities/{uuid}`
- **Body**:
  ```json
  {
    "content": "Just launched a new Spring Boot microservice! Check out the README.",
    "visibility": "PUBLIC",
    "mediaUrls": ["https://example.com/screenshot.png"]
  }
  ```
- **Response**: `200 OK`

### 6. Delete Activity
- **Method & URL**: `DELETE /api/activities/{uuid}`
- **Response**: `200 OK` (Returns soft-deleted representation)

---

## Future Evolution
- **REST today**: Initial CRUD feed REST resource hierarchy.
- **gRPC consumers later**: Enabling high-frequency low-latency internal queries from gateway and feed aggregators.
- **GraphQL gateway later**: Composing user profile, repositories, and activities into a single client query graphs.
