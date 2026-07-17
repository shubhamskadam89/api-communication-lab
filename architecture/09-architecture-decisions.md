# 09. Architecture Decision Records

> Architecture Decision Records (ADRs) for API Communication Lab are maintained in the [`adr/`](adr/) directory.
>
> Each ADR captures a single significant architectural decision: its context, rationale, alternatives considered, and consequences.

---

## ADR Index

| ADR | Title | Status |
|-----|-------|--------|
| [ADR-001](adr/ADR-001-api-gateway.md) | API Gateway as the Single Entry Point | Accepted |
| [ADR-002](adr/ADR-002-database-per-service.md) | Database per Service | Accepted |
| [ADR-003](adr/ADR-003-gateway-orchestration.md) | Gateway-Orchestrated Communication | Accepted |
| [ADR-004](adr/ADR-004-jwt-at-gateway.md) | JWT Validation at the Gateway | Accepted |
| [ADR-005](adr/ADR-005-rest-baseline.md) | REST Before gRPC | Accepted |
| [ADR-006](adr/ADR-006-public-uuid.md) | UUID as Public Identifier | Accepted |
| [ADR-007](adr/ADR-007-api-versioning.md) | Versioned REST APIs | Accepted |
| [ADR-008](adr/ADR-008-response-envelope.md) | Standard Response Envelope | Accepted |
| [ADR-009](adr/ADR-009-identity-context.md) | Transport-Independent Identity Context | Accepted |
| [ADR-010](adr/ADR-010-benchmark-first.md) | Benchmark Before Optimization | Accepted |

---

## Revision Policy

Architecture decisions are immutable once accepted.

If a decision changes in the future:

- Do **not** modify the original ADR.
- Create a new ADR referencing the previous one.
- Mark the previous ADR as **Superseded** if applicable.

This preserves the architectural history and the rationale behind the system's evolution.
