# Data Flows

> This directory contains detailed request lifecycle documentation for API Communication Lab.

Each document focuses on a single business capability to keep diagrams small, maintainable, and easy to evolve.

---

# Flow Index

| Document | Description |
|----------|-------------|
| [authentication-flow.md](authentication-flow.md) | Login, JWT validation, identity propagation |
| [repository-flow.md](repository-flow.md) | Repository CRUD request lifecycle |
| [user-flow.md](user-flow.md) | User profile lifecycle |
| [activity-flow.md](activity-flow.md) | Activity recording lifecycle |
| [error-flow.md](error-flow.md) | Error ownership and propagation |
| [communication-matrix.md](communication-matrix.md) | Allowed service interactions |
| [future-evolution.md](future-evolution.md) | Communication evolution roadmap |

---

# Design Principles

- One business capability per document.
- Diagram-first documentation.
- Transport-independent workflows.
- Focus on request lifecycle rather than implementation details.

---

# Future Expansion

As the architecture evolves, additional flow documents may be added, such as:

- cache-flow.md
- grpc-flow.md
- graphql-flow.md
- event-flow.md
- notification-flow.md
- retry-flow.md
- circuit-breaker-flow.md
