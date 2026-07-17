# ADR-006 — UUID as Public Identifier

## Status

Accepted

## Date

2026-07-17

## Context

Database primary keys should remain internal implementation details.

## Decision

Expose UUIDs externally while retaining numeric IDs internally.

```text
Internal

id = 42

↓

External

uuid = 550e8400-e29b-41d4-a716-446655440000
```

## Alternatives Considered

| Alternative | Reason Rejected |
|-------------|-----------------|
| Expose numeric IDs | Predictable and harder to evolve |
| UUID as primary key | Increased index/storage overhead for this project |

## Consequences

### Positive

- Stable public API
- Better security
- Easier future migrations

### Negative

- Additional UUID index
