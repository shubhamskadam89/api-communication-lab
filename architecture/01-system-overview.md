# 01. System Overview

> This document defines the high-level architecture, system boundaries, and long-term evolution of API Communication Lab.

---

# Purpose

API Communication Lab is an engineering project that evaluates different communication mechanisms used in distributed systems.

The business domain intentionally remains stable while the communication layer evolves.

---

# High-Level Architecture

```mermaid
flowchart TD

    Client[Client / Frontend]

    Client --> API[API Service<br/>Gateway / BFF]

    API --> User[User Service]
    API --> Repo[Repository Service]
    API --> Activity[Activity Service]

    User --> UserDB[(User DB)]
    Repo --> RepoDB[(Repository DB)]
    Activity --> ActivityDB[(Activity DB)]
```

---

# System Goals

- Build production-oriented microservices
- Compare REST, gRPC and GraphQL
- Benchmark communication mechanisms
- Keep business logic constant across implementations
- Demonstrate architecture evolution using measurable evidence

---

# Domain Model

The project models a simplified collaborative development platform.

```mermaid
flowchart LR

User --> Repository
User --> Activity
Repository --> Activity
```

| Domain | Responsibility |
|----------|----------------|
| User | Profile Management |
| Repository | Repository Metadata |
| Activity | User & Repository Activity Timeline |

---

# Service Boundaries

```mermaid
flowchart LR

API --> User
API --> Repository
API --> Activity

User -. NO DIRECT CALL .-> Repository
Repository -. NO DIRECT CALL .-> Activity
Activity -. NO DIRECT CALL .-> User
```

## Rules

✅ API Service is the only public entry point

✅ Every service owns its own database

✅ Communication happens only through APIs

❌ No shared database

❌ No direct service-to-service communication

---

# Request Lifecycle

```mermaid
sequenceDiagram

participant Client
participant API
participant Repository

Client->>API: HTTP Request

API->>API: Authenticate & Authorize

API->>Repository: Internal REST

Repository-->>API: JSON Response

API-->>Client: Response
```

---

# Architecture Principles

| Principle | Description |
|------------|-------------|
| Single Responsibility | Each service owns one business capability |
| Database per Service | No shared persistence |
| Loose Coupling | Communication through APIs only |
| API Gateway | Single external entry point |
| Evolutionary Architecture | Communication mechanism changes without changing domains |

---

# Technology Stack

| Layer | Technology |
|--------|------------|
| Language | Java 21 |
| Framework | Spring Boot |
| Build | Gradle Kotlin DSL |
| Database | PostgreSQL |
| ORM | Spring Data JPA |
| API | REST (Stage 1) |
| Documentation | OpenAPI |
| Containers | Docker |

---

# Evolution Roadmap

```mermaid
flowchart LR

A[Independent Services]
-->B[REST Communication]
-->C[REST Benchmark]
-->D[gRPC Migration]
-->E[gRPC Benchmark]
-->F[GraphQL Gateway]
-->G[GraphQL Benchmark]
-->H[Engineering Comparison]
```

---

# Out of Scope (Current Phase)

The following technologies are intentionally deferred until the REST baseline is complete.

```mermaid
mindmap
  root((Deferred))
    gRPC
    GraphQL
    Kafka
    Redis
    Circuit Breakers
    Service Discovery
    Distributed Tracing
```

---

# Success Criteria

The REST stage is considered complete when:

- API Gateway routes all external requests
- Services expose versioned REST APIs
- End-to-end communication is operational
- JWT authentication is handled by the gateway
- Benchmarks can be executed consistently

---

# Related Documents

| Document | Purpose |
|-----------|---------|
| README.md | Documentation Index |
| 02-service-responsibilities.md | Service Ownership |
| 03-rest-architecture.md | REST Communication Design |
| 04-api-contracts.md | Endpoint Specifications |
| 05-security.md | Authentication & Authorization |
| 06-error-handling.md | Error Strategy |
| 07-data-flow.md | Request Flows |
| 08-benchmark-plan.md | Performance Evaluation |
| 09-architecture-decisions.md | Architecture Decision Records |