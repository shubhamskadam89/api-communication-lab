# ADR-007 — Versioned REST APIs

## Status

Accepted

## Date

2026-07-17

## Context

Public APIs evolve over time.

Breaking changes should not affect existing consumers.

## Decision

All endpoints are versioned.

```text
/api/v1/...
```

## Alternatives Considered

| Alternative | Reason Rejected |
|-------------|-----------------|
| No versioning | Difficult future evolution |
| Header versioning | Less discoverable for this project |

## Consequences

### Positive

- Clear evolution strategy
- Predictable API lifecycle

### Negative

- Slightly longer URLs
