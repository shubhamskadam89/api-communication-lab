# ADR-009 — Transport-Independent Identity Context

## Status

Accepted

## Date

2026-07-17

## Context

The communication mechanism will evolve from REST to gRPC and GraphQL.

Identity propagation should remain independent of the transport protocol.

## Decision

Identity is modeled as an **Identity Context**.

Transport mapping:

| Transport | Representation |
|------------|----------------|
| REST | HTTP Headers |
| gRPC | Metadata |
| GraphQL | Request Context |

## Alternatives Considered

| Alternative | Reason Rejected |
|-------------|-----------------|
| Transport-specific identity models | Harder migration and duplicated concepts |

## Consequences

### Positive

- Protocol independence
- Cleaner migrations
- Reusable security model

### Negative

- Requires mapping layer per transport
