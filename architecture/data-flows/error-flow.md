# Error Flow

> Documents how failures propagate through the system.

---

# Resource Not Found

```mermaid
sequenceDiagram

participant Client
participant API
participant Repository

Client->>API: GET /api/v1/users/{ownerUuid}/repositories/{uuid}

API->>Repository: Retrieve Repository

Repository-->>API: 404 Not Found

API-->>Client: 404 Not Found
```

---

# Validation Failure

```mermaid
sequenceDiagram

participant Client
participant API
participant Service

Client->>API: POST /api/v1/users (invalid body)

API->>Service: Create User

Service-->>API: 400 Bad Request

API-->>Client: 400 Validation Failed
```

---

# Authentication Failure

```mermaid
sequenceDiagram

participant Client
participant API

Client->>API: Invalid JWT

API->>API: Validate Token

API-->>Client: 401 Unauthorized
```

Repository Service is never contacted.

---

# Authorization Failure

```mermaid
sequenceDiagram

participant Client
participant API

Client->>API: Request Without Permission

API->>API: Evaluate Authorization

API-->>Client: 403 Forbidden
```

---

# Unexpected Error

```mermaid
sequenceDiagram

participant Client
participant API
participant Service

Client->>API: Valid Request

API->>Service: Forward Request

Service-->>API: 500 Internal Server Error

API-->>Client: 500 Internal Server Error
```

---

# Standard Error Response

Every error follows this structure:

```json
{
  "timestamp": "2026-07-17T14:30:00Z",
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Repository not found with uuid: ...",
  "path": "/api/v1/users/.../repositories/..."
}
```

---

# Principles

- Errors originate at the owning service.
- API Service preserves error semantics.
- Stack traces are never exposed.
- Standard `ErrorResponse` is always returned.
- Correlation ID is included in logs.

---

# Related ADRs

- [ADR-004 — JWT Validation at the Gateway](../adr/ADR-004-jwt-at-gateway.md)
- [ADR-008 — Standard Response Envelope](../adr/ADR-008-response-envelope.md)
