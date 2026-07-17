# ADR-008 — Standard Response Envelope

## Status

Accepted

## Date

2026-07-17

## Context

Clients should receive consistent responses regardless of which service handled the request.

## Decision

Every service returns the shared response models already implemented.

### Success

```json
{
  "timestamp": "...",
  "status": 200,
  "message": "...",
  "data": {},
  "path": "/api/v1/..."
}
```

### Error

```json
{
  "timestamp": "...",
  "status": 404,
  "error": "Not Found",
  "message": "...",
  "path": "/api/v1/..."
}
```

## Alternatives Considered

| Alternative | Reason Rejected |
|-------------|-----------------|
| Different response per service | Inconsistent client experience |
| Deep metadata envelope | Unnecessary complexity for current scope |

## Consequences

### Positive

- Consistent API design
- Simpler client implementation
- Easier debugging

### Negative

- Slightly larger payloads
