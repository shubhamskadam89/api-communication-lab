# Activity Flow

> Documents the request lifecycle for activity management.

---

# Record Activity

```mermaid
sequenceDiagram

participant Client
participant API
participant Activity
participant DB

Client->>API: POST /api/v1/users/{authorUuid}/activities

API->>Activity: Create Activity

Activity->>DB: INSERT

DB-->>Activity: Success

Activity-->>API: Activity DTO

API-->>Client: 201 Created
```

---

# Retrieve Activity

```mermaid
sequenceDiagram

participant Client
participant API
participant Activity
participant DB

Client->>API: GET /api/v1/activities/{uuid}

API->>Activity: Retrieve Activity

Activity->>DB: SELECT

DB-->>Activity: Activity

Activity-->>API: Activity DTO

API-->>Client: Response
```

---

# List Public Feed

```mermaid
sequenceDiagram

participant Client
participant API
participant Activity
participant DB

Client->>API: GET /api/v1/activities?page=0&size=20

API->>Activity: Fetch Public Activities

Activity->>DB: SELECT WHERE visibility = PUBLIC

DB-->>Activity: Paginated Activities

Activity-->>API: Paginated DTO

API-->>Client: Response
```

---

# List User Activity Feed

```mermaid
sequenceDiagram

participant Client
participant API
participant Activity
participant DB

Client->>API: GET /api/v1/users/{authorUuid}/activities?page=0&size=20

API->>Activity: Fetch Activities by Author

Activity->>DB: SELECT WHERE author_uuid = ?

DB-->>Activity: Paginated Activities

Activity-->>API: Paginated DTO

API-->>Client: Response
```

---

# Update Activity

```mermaid
sequenceDiagram

participant Client
participant API
participant Activity
participant DB

Client->>API: PUT /api/v1/activities/{uuid}

API->>Activity: Update Activity

Activity->>DB: UPDATE

DB-->>Activity: Success

Activity-->>API: Updated DTO

API-->>Client: Response
```

---

# Delete Activity (Soft Delete)

```mermaid
sequenceDiagram

participant Client
participant API
participant Activity
participant DB

Client->>API: DELETE /api/v1/activities/{uuid}

API->>Activity: Soft Delete Activity

Activity->>DB: UPDATE is_deleted = true

DB-->>Activity: Success

Activity-->>API: Deleted DTO

API-->>Client: Response
```

---

# Related ADRs

- [ADR-003 — Gateway-Orchestrated Communication](../adr/ADR-003-gateway-orchestration.md)
- [ADR-006 — UUID as Public Identifier](../adr/ADR-006-public-uuid.md)
- [ADR-008 — Standard Response Envelope](../adr/ADR-008-response-envelope.md)
