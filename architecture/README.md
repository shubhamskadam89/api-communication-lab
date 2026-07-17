# Architecture Documentation

## Purpose

This directory contains the architecture documentation for the **API Communication Lab** project.

Unlike implementation documentation, these documents focus on **system design decisions**, **service boundaries**, **communication patterns**, and **engineering trade-offs** before implementation.

The goal is to define the architecture first so that implementation becomes a direct execution of an agreed design rather than ad-hoc development.

---

# Project Overview

API Communication Lab is an engineering project that explores different communication mechanisms used in distributed systems.

The project begins with a REST-based microservice architecture and progressively evolves through multiple communication technologies while benchmarking each stage under identical workloads.

The objective is not only to implement these technologies, but to understand the engineering trade-offs between them in terms of performance, scalability, complexity, and maintainability.

---

# Architecture Principles

The project follows several guiding principles throughout every stage of development.

## 1. Design Before Implementation

Every major architectural decision is documented before code is written.

This encourages intentional engineering decisions and reduces unnecessary redesign during implementation.

---

## 2. Clear Service Ownership

Each microservice owns its own business capability and persistence layer.

Services are responsible only for their own domain and should not directly access another service's database.

---

## 3. API Gateway as the Entry Point

All external client requests enter the system through the API Service.

The gateway is responsible for:

- Authentication
- Authorization
- Request routing
- Response aggregation
- Cross-cutting concerns

Backend services are not exposed directly to clients.

---

## 4. Loose Coupling

Services communicate only through well-defined APIs.

Business logic remains isolated within the owning service to minimize coupling and improve maintainability.

---

## 5. Incremental Evolution

Rather than introducing multiple technologies simultaneously, the system evolves in controlled stages.

Each stage introduces a single architectural change, allowing its impact to be measured independently.

---

# Architecture Roadmap

The project is implemented in multiple phases.

| Phase | Goal |
|--------|------|
| Phase 1 | Independent REST microservices |
| Phase 2 | REST communication through API Gateway |
| Phase 3 | REST performance benchmarking |
| Phase 4 | Replace internal REST with gRPC |
| Phase 5 | Benchmark gRPC communication |
| Phase 6 | Introduce GraphQL Gateway |
| Phase 7 | Benchmark GraphQL queries |
| Phase 8 | Compare architectural approaches |

---

# Documentation Structure

| Document | Purpose |
|----------|---------|
| 01-system-overview.md | High-level architecture and system goals |
| 02-service-responsibilities.md | Responsibilities and ownership of each service |
| 03-rest-architecture.md | REST communication design |
| 04-api-contracts.md | Public API definitions |
| 05-security.md | Authentication and authorization model |
| 06-error-handling.md | Standard error handling strategy |
| 07-data-flow.md | Request and response flows |
| 08-benchmark-plan.md | Performance evaluation methodology |
| 09-architecture-decisions.md | Architecture Decision Records (ADRs) |

---

# Intended Audience

These documents are intended for:

- Software Engineers
- Backend Developers
- System Architects
- Technical Reviewers
- Contributors to the project

The documentation assumes familiarity with REST APIs, Spring Boot, and distributed system fundamentals.

---

# Repository Structure

```text
api-communication-lab/
│
├── architecture/
├── api-service/
├── user-service/
├── repository-service/
├── activity-service/
└── shared-proto/
```

---

# Reading Order

The recommended order is:

1. README.md
2. 01-system-overview.md
3. 02-service-responsibilities.md
4. 03-rest-architecture.md
5. 04-api-contracts.md
6. 05-security.md
7. 06-error-handling.md
8. 07-data-flow.md
9. 08-benchmark-plan.md
10. 09-architecture-decisions.md

Following this order moves from high-level architecture to implementation details.

---

# Future Evolution

The architecture is intentionally designed to evolve.

Future stages will introduce:

- gRPC
- GraphQL
- Redis caching
- Event-driven communication
- Observability
- Distributed tracing

Each enhancement will build upon the baseline established during the REST implementation.

---

# License

This documentation is maintained alongside the source code and should evolve as the architecture evolves.

Architectural changes should always be reflected in the corresponding design documents before implementation whenever practical.