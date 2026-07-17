# User Flow

> Documents the request lifecycle for user profile management.

---

# Create User

```mermaid
sequenceDiagram

participant Client
participant API
participant User
participant DB

Client->>API: POST /api/v1/users

API->>User: Create User

User->>DB: INSERT

DB-->>User: Success

User-->>API: User DTO

API-->>Client: 201 Created
```

---

# Retrieve User

```mermaid
sequenceDiagram

participant Client
participant API
participant User
participant DB

Client->>API: GET /api/v1/users/{uuid}

API->>User: Retrieve User

User->>DB: SELECT

DB-->>User: User

User-->>API: User DTO

API-->>Client: Response
```

---

# Retrieve User Summary

```mermaid
sequenceDiagram

participant Client
participant API
participant User
participant DB

Client->>API: GET /api/v1/users/{uuid}/summary

API->>User: Retrieve User Summary

User->>DB: SELECT

DB-->>User: User

User-->>API: Summary DTO

API-->>Client: Response
```

---

# Update User

```mermaid
sequenceDiagram

participant Client
participant API
participant User
participant DB

Client->>API: PUT /api/v1/users/{uuid}

API->>User: Update User

User->>DB: UPDATE

DB-->>User: Success

User-->>API: Updated User DTO

API-->>Client: Response
```

---

# Delete User (Soft Delete)

```mermaid
sequenceDiagram

participant Client
participant API
participant User
participant DB

Client->>API: DELETE /api/v1/users/{uuid}

API->>User: Soft Delete User

User->>DB: UPDATE is_deleted = true

DB-->>User: Success

User-->>API: Deleted User DTO

API-->>Client: Response
```

---

# Related ADRs

- [ADR-003 — Gateway-Orchestrated Communication](../adr/ADR-003-gateway-orchestration.md)
- [ADR-006 — UUID as Public Identifier](../adr/ADR-006-public-uuid.md)
- [ADR-008 — Standard Response Envelope](../adr/ADR-008-response-envelope.md)
