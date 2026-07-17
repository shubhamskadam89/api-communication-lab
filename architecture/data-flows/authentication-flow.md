# Authentication Flow

> Documents the authentication lifecycle from login to authenticated service requests.

---

# Login Flow

```mermaid
sequenceDiagram

participant Client
participant API

Client->>API: Login Request

API->>API: Validate Credentials

API-->>Client: JWT
```

---

# Authenticated Request

```mermaid
sequenceDiagram

participant Client
participant API
participant Service

Client->>API: Authorization: Bearer JWT

API->>API: Validate JWT

API->>Service: Internal Identity Context

Service-->>API: Response

API-->>Client: Response
```

---

# Key Decisions

- JWT validated only at the gateway.
- Services trust the gateway.
- Identity propagated independently of transport.

---

# Related ADRs

- [ADR-001 — API Gateway as the Single Entry Point](../adr/ADR-001-api-gateway.md)
- [ADR-004 — JWT Validation at the Gateway](../adr/ADR-004-jwt-at-gateway.md)
- [ADR-009 — Transport-Independent Identity Context](../adr/ADR-009-identity-context.md)
