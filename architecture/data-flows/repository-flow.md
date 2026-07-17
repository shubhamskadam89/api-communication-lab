# Repository Flow

> Documents the request lifecycle for repository management.

---

# Create Repository

```mermaid
sequenceDiagram

participant Client
participant API
participant Repository
participant DB

Client->>API: POST /api/v1/users/{ownerUuid}/repositories

API->>Repository: Create Repository

Repository->>DB: INSERT

DB-->>Repository: Success

Repository-->>API: Repository DTO

API-->>Client: 201 Created
```

---

# Retrieve Repository

```mermaid
sequenceDiagram

participant Client
participant API
participant Repository
participant DB

Client->>API: GET /api/v1/users/{ownerUuid}/repositories/{uuid}

API->>Repository: Retrieve Repository

Repository->>DB: SELECT

DB-->>Repository: Repository

Repository-->>API: DTO

API-->>Client: Response
```

---

# List Repositories by Owner

```mermaid
sequenceDiagram

participant Client
participant API
participant Repository
participant DB

Client->>API: GET /api/v1/users/{ownerUuid}/repositories?page=0&size=20

API->>Repository: Find Repositories by Owner

Repository->>DB: SELECT with Pagination

DB-->>Repository: Page of Repositories

Repository-->>API: Paginated DTO

API-->>Client: Response
```

---

# Update Repository

```mermaid
sequenceDiagram

participant Client
participant API
participant Repository
participant DB

Client->>API: PUT /api/v1/users/{ownerUuid}/repositories/{uuid}

API->>Repository: Update Repository

Repository->>DB: UPDATE

DB-->>Repository: Success

Repository-->>API: Updated DTO

API-->>Client: Response
```

---

# Delete Repository

```mermaid
sequenceDiagram

participant Client
participant API
participant Repository
participant DB

Client->>API: DELETE /api/v1/users/{ownerUuid}/repositories/{uuid}

API->>Repository: Soft Delete Repository

Repository->>DB: UPDATE is_deleted = true

DB-->>Repository: Success

Repository-->>API: Deleted DTO

API-->>Client: Response
```

---

# Related ADRs

- [ADR-003 — Gateway-Orchestrated Communication](../adr/ADR-003-gateway-orchestration.md)
- [ADR-006 — UUID as Public Identifier](../adr/ADR-006-public-uuid.md)
- [ADR-008 — Standard Response Envelope](../adr/ADR-008-response-envelope.md)
