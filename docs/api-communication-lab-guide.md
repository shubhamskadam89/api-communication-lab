# API Communication Lab: Step-by-Step Build Guide

## Project Goal

Build the same backend capability through different API communication styles and measure the engineering trade-offs with evidence.

The project should answer one question:

> When should I choose REST, GraphQL, or gRPC for a backend problem?

The key rule is that business logic, data shape, database engine, and workload stay consistent. Only the communication style changes.

## Architecture Decision

Use a Gradle multi-module Spring Boot monorepo.

The system has one public gateway and three internal services:

- `api-service`: external entry point; exposes REST, GraphQL, and benchmark-facing gRPC aggregation endpoints; calls internal services with gRPC.
- `user-service`: owns users and profiles; exposes gRPC only.
- `repository-service`: owns repositories, issues, and pull requests; exposes gRPC only. Repository stars stay a scalar field on the repository record.
- `activity-service`: owns activity feed, notifications, and events; exposes gRPC only.
- `shared-proto`: owns protobuf contracts and generated gRPC Java stubs.

Use one local Postgres container with three separate databases:

- `user_db`
- `repository_db`
- `activity_db`

The `api-service` must stay database-less. It should aggregate data only through gRPC calls.

## Why This Shape

This project is not a CRUD demo. CRUD endpoints make REST, GraphQL, and gRPC look almost identical.

The domain needs:

- Deep relationships.
- Large and variable payloads.
- Different client shapes.
- Internal fan-out between services.
- High-frequency calls.
- Measurable over-fetching, under-fetching, latency, and payload differences.

The chosen domain is a GitHub-like developer platform because it naturally creates cross-service aggregation:

- Dashboard needs user profile, repositories, activity, and notifications.
- Repository details need owner data, repo metadata, issues, pull requests, and commits.
- Activity feeds need event streams.
- Search needs different projections for web, mobile, and admin-like clients.

## Milestones

### Milestone 1: Foundation and Gradle Monorepo

Create the repo skeleton, Gradle Kotlin DSL setup, shared conventions, and empty Spring Boot services.

Target outcome:

- `./gradlew build` works.
- All modules exist.
- Each service starts with a basic health check.
- Docker Compose starts Postgres.

### Milestone 2: Proto Contracts and gRPC Skeleton

Define protobuf contracts and make one real gRPC call from `api-service` to an internal service.

Target outcome:

- `shared-proto` generates Java stubs.
- Internal services implement gRPC servers.
- `api-service` has gRPC clients.
- One dummy end-to-end call works.

### Milestone 3: Persistence and Seed Data

Add isolated database schemas, migrations, JPA entities, repositories, and deterministic seed data.

Target outcome:

- Each service owns only its own tables.
- Services cannot query another service database.
- Seed data supports realistic dashboard and repository-detail scenarios.

### Milestone 4: Scenario 1 - Developer Dashboard

Build the first complete comparison scenario.

Target outcome:

- REST dashboard endpoint works.
- GraphQL dashboard query works.
- gRPC aggregation path works internally.
- Same logical response can be produced through REST and GraphQL.

### Milestone 5: Scenario 2 - Repository Details

Build a deeper relationship scenario with variable payload needs.

Target outcome:

- REST exposes multiple resource endpoints and an aggregate endpoint.
- GraphQL lets clients request exact fields.
- Internal gRPC calls fetch repository, owner, issues, pull requests, commits, and stars.

### Milestone 6: Scenario 3 - Activity Feed and Streaming

Build high-frequency activity feed behavior and at least one streaming comparison.

Target outcome:

- REST exposes paginated polling.
- GraphQL exposes feed query/subscription if feasible.
- gRPC supports server streaming internally.
- Latency and update delivery behavior can be compared.

### Milestone 7: gRPC Aggregation Baseline

Expose scenario-level gRPC endpoints from `api-service` so REST, GraphQL, and gRPC can be compared against the same logical response shapes.

Target outcome:

- `api-service` exposes `ApiAggregationService` over gRPC.
- Dashboard and repository details are available through gRPC aggregation.
- REST, GraphQL, and gRPC reuse the same underlying aggregation logic.
- The final comparison matrix can include a fair gRPC column.

### Milestone 8: Observability and Benchmarking

Add repeatable performance measurements and request tracing.

Target outcome:

- k6 scripts exist for each scenario.
- Metrics capture latency, throughput, payload size, and error rate.
- Logs show internal fan-out timing.
- Results are stored in docs.

### Milestone 9: Final Comparison and Documentation

Turn implementation results into engineering conclusions.

Target outcome:

- README explains architecture and how to run everything.
- ADRs document decisions.
- Comparison matrix is backed by measured results.
- Final write-up explains when REST, GraphQL, and gRPC fit best.

## Step-by-Step Execution Plan

### Phase 1: Build the Foundation

1. Initialize Gradle wrapper and Kotlin DSL files.
2. Create modules: `api-service`, `user-service`, `repository-service`, `activity-service`, `shared-proto`.
3. Add shared version catalog.
4. Add Spring Boot application classes for all services.
5. Add service-specific ports.
6. Add basic `/actuator/health` endpoints.
7. Add Docker Compose with Postgres and database init script.
8. Confirm all services can start locally.

### Phase 2: Make gRPC Real Early

1. Configure protobuf Gradle plugin in `shared-proto`.
2. Define first proto contract, starting with `UserService`.
3. Generate Java stubs.
4. Implement a simple `GetUserSummary` RPC.
5. Add gRPC server config to `user-service`.
6. Add gRPC client config to `api-service`.
7. Create a REST endpoint in `api-service` that calls `user-service` through gRPC.
8. Verify one external REST call crosses a real service boundary.

### Phase 3: Add Persistence

1. Add Flyway migrations per service.
2. Add JPA entities per service.
3. Add repository interfaces.
4. Add seed data scripts.
5. Make gRPC implementations read from databases.
6. Prove each service only reads its own database.

### Phase 4: Build the Dashboard Scenario

1. Define the dashboard response model.
2. Add gRPC methods needed from all internal services.
3. Implement fan-out aggregation in `api-service`.
4. Expose REST endpoint: `GET /api/dashboard/{userId}`.
5. Expose GraphQL query: `dashboard(userId: ID!)`.
6. Keep REST and GraphQL backed by the same application service.
7. Add tests proving response consistency.

### Phase 5: Build Repository Details

1. Define repository detail projections: mobile, web, and full.
2. Add repository, issue, pull request, and activity/commit data.
3. Expose REST resource endpoints.
4. Expose REST aggregate endpoint.
5. Expose GraphQL repository detail query.
6. Measure over-fetching and under-fetching cases.

### Phase 6: Build Activity Feed and Streaming

1. Add activity event schema.
2. Implement paginated REST feed.
3. Implement GraphQL feed query.
4. Implement internal gRPC server streaming.
5. Add a demo endpoint that consumes a stream and returns summarized output.
6. Optional: add GraphQL subscription after the core comparison is stable.

### Phase 7: Add gRPC Aggregation Baseline

1. Define `api_aggregation_service.proto` in `shared-proto`.
2. Add `GetDashboard` and `GetRepositoryDetails` RPCs first.
3. Add activity streaming RPC if you keep the streaming scenario.
4. Implement the gRPC service in `api-service`.
5. Reuse the same aggregation application services used by REST and GraphQL.
6. Add tests proving REST, GraphQL, and gRPC aggregation return equivalent business data.

### Phase 8: Benchmark

1. Write k6 scripts for dashboard REST and GraphQL.
2. Write k6 scripts for repository details REST and GraphQL.
3. Add gRPC benchmarking with `ghz` against `ApiAggregationService`.
4. Benchmark the same scenarios for REST, GraphQL, and gRPC aggregation.
5. Record request count, payload size, p95 latency, p99 latency, throughput, and error rate.
6. Save results in `docs/results`.

### Phase 9: Explain the Trade-Offs

1. Write ADRs for monorepo, database choice, API gateway shape, and gRPC internal calls.
2. Create comparison tables.
3. Explain what REST did well.
4. Explain what GraphQL did well.
5. Explain what gRPC did well.
6. Explain operational complexity honestly.
7. Add final recommendations.

## Issue Backlog

Use these issues in order. Each issue has a milestone, objective, implementation notes, and acceptance criteria.

### Issue 1: Initialize Gradle Kotlin DSL Monorepo

Milestone: Foundation and Gradle Monorepo

Objective:
Create the root Gradle project using Kotlin DSL.

Implementation notes:
- Add `settings.gradle.kts`.
- Add root `build.gradle.kts`.
- Add `gradle/libs.versions.toml`.
- Configure Java toolchain.
- Use consistent group and version.

Acceptance criteria:
- `./gradlew projects` lists all planned modules.
- `./gradlew build` runs even if modules are still mostly empty.

### Issue 2: Add Core Service Modules

Milestone: Foundation and Gradle Monorepo

Objective:
Create empty Gradle modules for all services.

Implementation notes:
- Add `api-service`.
- Add `user-service`.
- Add `repository-service`.
- Add `activity-service`.
- Add `shared-proto`.

Acceptance criteria:
- Each module has a valid `build.gradle.kts`.
- Root Gradle build can discover every module.

### Issue 3: Add Spring Boot Application Skeletons

Milestone: Foundation and Gradle Monorepo

Objective:
Make each service a runnable Spring Boot application.

Implementation notes:
- Add main application class per service.
- Configure unique local ports.
- Add basic package structure.
- Keep `shared-proto` as a library module, not a Spring Boot app.

Acceptance criteria:
- Each service starts independently.
- No service depends on another service at startup yet.

### Issue 4: Add Actuator Health Checks

Milestone: Foundation and Gradle Monorepo

Objective:
Expose health endpoints for all runtime services.

Implementation notes:
- Add Spring Boot Actuator dependency.
- Configure health endpoint exposure.
- Use predictable ports in `application.yml`.

Acceptance criteria:
- `GET /actuator/health` returns `UP` for `api-service`.
- Same health check works for user, repository, and activity services.

### Issue 5: Add Docker Compose for Local Infrastructure

Milestone: Foundation and Gradle Monorepo

Objective:
Create local infrastructure with one Postgres container.

Implementation notes:
- Add `docker-compose.yml`.
- Use one Postgres service.
- Mount an init script.
- Expose Postgres on one local port.

Acceptance criteria:
- `docker compose up -d` starts Postgres.
- Postgres is reachable from the host machine.

### Issue 6: Create Three Logical Databases

Milestone: Foundation and Gradle Monorepo

Objective:
Create separate databases inside the single Postgres instance.

Implementation notes:
- Add init SQL script.
- Create `user_db`.
- Create `repository_db`.
- Create `activity_db`.
- Avoid cross-service schemas in the same database.

Acceptance criteria:
- All three databases exist after Docker Compose startup.
- Services will use separate JDBC URLs.

### Issue 7: Add Shared Build Conventions

Milestone: Foundation and Gradle Monorepo

Objective:
Reduce duplicate Gradle config across services.

Implementation notes:
- Add common Java settings.
- Add common test settings.
- Add dependency versions in version catalog.
- Keep service-specific dependencies local to service modules.

Acceptance criteria:
- Common config is not repeated in every module.
- Build files stay readable for a new Gradle user.

### Issue 8: Add Local Run Documentation

Milestone: Foundation and Gradle Monorepo

Objective:
Document how to run the empty system locally.

Implementation notes:
- Add initial README.
- Include required Java version.
- Include Docker startup command.
- Include service startup commands.

Acceptance criteria:
- A fresh clone can start infrastructure and at least one service by following the README.

### Issue 9: Configure Protobuf Gradle Plugin

Milestone: Proto Contracts and gRPC Skeleton

Objective:
Enable proto compilation in `shared-proto`.

Implementation notes:
- Add protobuf plugin.
- Add gRPC Java plugin.
- Configure generated source directories.
- Make IntelliJ recognize generated stubs.

Acceptance criteria:
- `./gradlew :shared-proto:generateProto` succeeds.
- Generated Java and gRPC classes appear under Gradle generated sources.

### Issue 10: Define User Service Proto Contract

Milestone: Proto Contracts and gRPC Skeleton

Objective:
Create the first real internal service contract.

Implementation notes:
- Add `user_service.proto`.
- Include `GetUserSummaryRequest`.
- Include `UserSummaryResponse`.
- Include stable field numbers.
- Use clear package names.

Acceptance criteria:
- Proto compiles.
- Generated Java stubs include `UserServiceGrpc`.

### Issue 11: Implement User gRPC Server Skeleton

Milestone: Proto Contracts and gRPC Skeleton

Objective:
Make `user-service` serve a simple gRPC method.

Implementation notes:
- Add gRPC server dependency.
- Implement `GetUserSummary`.
- Return hard-coded data initially.
- Configure gRPC port separately from HTTP port.

Acceptance criteria:
- gRPC server starts with `user-service`.
- A local gRPC client can call `GetUserSummary`.

### Issue 12: Add gRPC Client in API Service

Milestone: Proto Contracts and gRPC Skeleton

Objective:
Make `api-service` call `user-service` over gRPC.

Implementation notes:
- Add dependency on `shared-proto`.
- Configure channel to `user-service`.
- Create a small client wrapper.
- Keep gRPC details out of controllers.

Acceptance criteria:
- `api-service` can call `user-service`.
- Connection settings are configurable.

### Issue 13: Add First REST-to-gRPC Endpoint

Milestone: Proto Contracts and gRPC Skeleton

Objective:
Prove an external REST call can cross an internal gRPC boundary.

Implementation notes:
- Add `GET /api/users/{userId}/summary`.
- Controller calls application service.
- Application service calls gRPC client.
- Map proto response to REST DTO.

Acceptance criteria:
- Calling REST endpoint returns data from `user-service`.
- No database is required yet.

### Issue 14: Define Repository and Activity Proto Skeletons

Milestone: Proto Contracts and gRPC Skeleton

Objective:
Create initial proto files for the other internal services.

Implementation notes:
- Add `repository_service.proto`.
- Add `activity_service.proto`.
- Start with one simple RPC each.
- Keep contracts intentionally small until persistence exists.

Acceptance criteria:
- Both proto files compile.
- Generated stubs are available to service modules.

### Issue 15: Add Flyway to User Service

Milestone: Persistence and Seed Data

Objective:
Create the user database schema.

Implementation notes:
- Add Flyway.
- Add migrations for users and profiles.
- Use `user_db` only.
- Add indexes needed for lookup by ID and username.

Acceptance criteria:
- `user-service` applies migrations on startup.
- Tables exist only in `user_db`.

### Issue 16: Add Flyway to Repository Service

Milestone: Persistence and Seed Data

Objective:
Create repository-owned schema.

Implementation notes:
- Add repositories, issues, and pull requests.
- Keep `stars` as an integer field on `repositories`, not a separate table.
- Store owner ID as external reference, not a foreign key to user DB.
- Add indexes for repository lookup and owner lookup.

Acceptance criteria:
- `repository-service` applies migrations on startup.
- No tables are created in `user_db` or `activity_db`.

### Issue 17: Add Flyway to Activity Service

Milestone: Persistence and Seed Data

Objective:
Create activity-owned schema.

Implementation notes:
- Add activity events.
- Add notifications.
- Store actor IDs and repository IDs as external references.
- Add indexes for feed queries.

Acceptance criteria:
- `activity-service` applies migrations on startup.
- Feed queries can filter by user ID.

### Issue 18: Add JPA Entities and Repositories

Milestone: Persistence and Seed Data

Objective:
Implement persistence access in all internal services.

Implementation notes:
- Add JPA entities per service.
- Add Spring Data repositories.
- Keep entity relationships within service boundaries only.
- Do not model cross-service relationships as JPA joins.

Acceptance criteria:
- Each service can read its own data.
- No service has a datasource for another service database.

### Issue 19: Add Deterministic Seed Data

Milestone: Persistence and Seed Data

Objective:
Create enough data to support realistic comparison scenarios.

Implementation notes:
- Add seed data for several users.
- Add repositories with issues, pull requests, and scalar star counts.
- Add activity events and notifications.
- Keep IDs stable so benchmark scripts can target known records.

Acceptance criteria:
- Fresh database startup creates repeatable test data.
- Dashboard and repository-detail scenarios have meaningful data.

### Issue 20: Replace Hard-Coded gRPC Responses with Database Reads

Milestone: Persistence and Seed Data

Objective:
Make internal gRPC methods return persisted data.

Implementation notes:
- Update user gRPC implementation.
- Update repository gRPC implementation.
- Update activity gRPC implementation.
- Return useful `NOT_FOUND` errors when data is missing.

Acceptance criteria:
- gRPC methods return seed data from Postgres.
- Missing IDs produce predictable errors.

### Issue 21: Define Dashboard API Contract

Milestone: Scenario 1 - Developer Dashboard

Objective:
Specify the first comparison scenario.

Implementation notes:
- Define dashboard fields.
- Include profile summary.
- Include top repositories.
- Include recent activity.
- Include unread notification count.

Acceptance criteria:
- REST DTO and GraphQL type model the same logical dashboard.
- Required internal gRPC calls are listed.

### Issue 22: Add Dashboard gRPC Methods

Milestone: Scenario 1 - Developer Dashboard

Objective:
Support dashboard aggregation from internal services.

Implementation notes:
- Add user summary RPC.
- Add top repositories RPC.
- Add recent activity RPC.
- Add notification count RPC.

Acceptance criteria:
- All required proto contracts compile.
- Internal services implement the methods using database reads.

### Issue 23: Implement Dashboard Aggregator

Milestone: Scenario 1 - Developer Dashboard

Objective:
Build the core aggregation logic inside `api-service`.

Implementation notes:
- Create an application service.
- Call user, repository, and activity services.
- Use parallel calls where appropriate.
- Add timeout handling.

Acceptance criteria:
- Aggregator returns one dashboard object.
- Partial failure behavior is documented and tested.

### Issue 24: Expose REST Dashboard Endpoint

Milestone: Scenario 1 - Developer Dashboard

Objective:
Expose dashboard through REST.

Implementation notes:
- Add `GET /api/dashboard/{userId}`.
- Return JSON DTO.
- Keep controller thin.
- Add request/response examples.

Acceptance criteria:
- Endpoint returns expected dashboard for seeded user.
- Endpoint returns proper error for unknown user.

### Issue 25: Expose GraphQL Dashboard Query

Milestone: Scenario 1 - Developer Dashboard

Objective:
Expose the same dashboard through GraphQL.

Implementation notes:
- Add Spring GraphQL dependency.
- Define schema.
- Add resolver.
- Reuse same dashboard aggregator.

Acceptance criteria:
- GraphQL query returns dashboard data.
- Client can request a subset of fields.

### Issue 26: Add Dashboard Consistency Tests

Milestone: Scenario 1 - Developer Dashboard

Objective:
Prove REST and GraphQL represent the same business capability.

Implementation notes:
- Add integration tests.
- Compare core dashboard values.
- Avoid snapshot tests that are too brittle.

Acceptance criteria:
- Tests prove REST and GraphQL are consistent for seeded data.
- Tests fail if one protocol path omits required business fields.

### Issue 27: Define Repository Detail Projections

Milestone: Scenario 2 - Repository Details

Objective:
Design mobile, web, and full repository-detail shapes.

Implementation notes:
- Mobile needs minimal fields.
- Web needs richer fields.
- Full view includes issues, pull requests, commits, scalar star count, and owner summary.
- Use this to demonstrate over-fetching and under-fetching.

Acceptance criteria:
- Projection definitions are documented.
- Required fields are mapped to service owners.

### Issue 28: Add Repository Detail gRPC Methods

Milestone: Scenario 2 - Repository Details

Objective:
Support repository detail aggregation.

Implementation notes:
- Add repository summary RPC.
- Add issue list RPC.
- Add pull request list RPC.
- Add recent commits RPC.
- Include star count in repository summary instead of creating a separate star RPC.

Acceptance criteria:
- Proto contracts compile.
- Repository service returns real seeded data.

### Issue 29: Add REST Repository Resource Endpoints

Milestone: Scenario 2 - Repository Details

Objective:
Expose resource-oriented REST endpoints.

Implementation notes:
- Add `GET /api/repositories/{id}`.
- Add `GET /api/repositories/{id}/issues`.
- Add `GET /api/repositories/{id}/pull-requests`.
- Add `GET /api/repositories/{id}/commits`.
- Keep stars on the repository representation as a scalar value.

Acceptance criteria:
- Each endpoint returns only its resource.
- The multi-call REST client shape is clear.

### Issue 30: Add REST Repository Aggregate Endpoint

Milestone: Scenario 2 - Repository Details

Objective:
Expose a convenience REST endpoint for full repository details.

Implementation notes:
- Add `GET /api/repositories/{id}/details`.
- Reuse aggregation logic.
- Include owner data from user service.
- Include repository-owned collections.

Acceptance criteria:
- Endpoint returns full repository detail.
- Docs explain the trade-off versus multiple resource calls.

### Issue 31: Add GraphQL Repository Detail Query

Milestone: Scenario 2 - Repository Details

Objective:
Expose repository details with client-selected fields.

Implementation notes:
- Add GraphQL `repository(id: ID!)` query.
- Support nested owner, issues, pull requests, commits, and scalar star count.
- Avoid fetching expensive nested fields unless requested if feasible.

Acceptance criteria:
- GraphQL can return mobile projection with fewer fields.
- GraphQL can return full projection in one request.

### Issue 32: Add Repository Detail Tests

Milestone: Scenario 2 - Repository Details

Objective:
Protect repository detail behavior.

Implementation notes:
- Test REST resource endpoints.
- Test REST aggregate endpoint.
- Test GraphQL nested query.
- Test missing repository behavior.

Acceptance criteria:
- Main repository-detail paths are covered.
- Unknown repository ID returns consistent errors.

### Issue 33: Add Activity Feed Pagination

Milestone: Scenario 3 - Activity Feed and Streaming

Objective:
Build a paginated activity feed.

Implementation notes:
- Add cursor or page-based pagination.
- Sort by event time.
- Include actor and target references.
- Keep enrichment in `api-service`.

Acceptance criteria:
- REST feed endpoint returns stable pages.
- Activity service owns raw event data only.

### Issue 34: Add GraphQL Activity Feed Query

Milestone: Scenario 3 - Activity Feed and Streaming

Objective:
Expose activity feed through GraphQL.

Implementation notes:
- Add feed type.
- Add pagination input.
- Let clients choose fields.
- Reuse feed aggregation logic.

Acceptance criteria:
- GraphQL returns feed entries.
- Query can request minimal or detailed event fields.

### Issue 35: Add Internal gRPC Server Streaming

Milestone: Scenario 3 - Activity Feed and Streaming

Objective:
Demonstrate gRPC streaming for activity events.

Implementation notes:
- Add streaming RPC to activity proto.
- Implement server streaming from activity service.
- Add cancellation handling.
- Add simple demo consumer in `api-service`.

Acceptance criteria:
- `api-service` can consume streamed activity events.
- Stream stops cleanly when client cancels or timeout occurs.

### Issue 36: Define API Aggregation gRPC Contract

Milestone: gRPC Aggregation Baseline

Objective:
Create a fair gRPC entry point for scenario-level benchmarking.

Implementation notes:
- Add `api_aggregation_service.proto`.
- Define `GetDashboard`.
- Define `GetRepositoryDetails`.
- Define activity streaming RPC only if the streaming scenario remains in scope.
- Response shapes should match the REST and GraphQL business capabilities, not individual internal service RPCs.

Acceptance criteria:
- Proto compiles.
- Generated stubs include `ApiAggregationServiceGrpc`.
- Contract is documented as the benchmark-facing gRPC API.

### Issue 37: Implement API Aggregation gRPC Service

Milestone: gRPC Aggregation Baseline

Objective:
Expose dashboard and repository details through gRPC from `api-service`.

Implementation notes:
- Implement `ApiAggregationService` in `api-service`.
- Reuse the same dashboard aggregator used by REST and GraphQL.
- Reuse the same repository detail aggregator used by REST and GraphQL.
- Do not duplicate business logic in the gRPC adapter.

Acceptance criteria:
- gRPC client can call dashboard aggregation and receive the full dashboard shape.
- gRPC client can call repository details and receive the requested repository detail shape.
- REST, GraphQL, and gRPC paths share the same aggregation application services.

### Issue 38: Add Cross-Protocol Consistency Tests

Milestone: gRPC Aggregation Baseline

Objective:
Prove all three protocol entry points represent the same business scenarios.

Implementation notes:
- Test dashboard through REST, GraphQL, and gRPC aggregation.
- Test repository details through REST, GraphQL, and gRPC aggregation.
- Compare business fields, not raw serialization formats.
- Keep projection-specific comparisons explicit.

Acceptance criteria:
- Tests fail if one protocol path omits required business data.
- The final comparison matrix can honestly compare all three protocols for the same scenario.

### Issue 39: Add Benchmarking and Payload Measurement

Milestone: Observability and Benchmarking

Objective:
Measure REST, GraphQL, and gRPC aggregation for the same scenarios.

Implementation notes:
- Add k6 scripts for REST and GraphQL scenarios.
- Add `ghz` scripts or commands for `ApiAggregationService`.
- Log total request duration.
- Log individual internal gRPC call duration.
- Add correlation/request ID.
- Capture REST JSON response sizes.
- Capture GraphQL JSON response sizes.
- Capture gRPC aggregation protobuf message sizes where practical.
- Compare mobile versus full projections.
- Save results in `docs/results`.

Acceptance criteria:
- Benchmark scripts run locally.
- Results include request count, p50, p95, p99, throughput, error rate, and payload size.
- Findings explain over-fetching and under-fetching with numbers.

### Issue 40: Write Final Comparison Report

Milestone: Final Comparison and Documentation

Objective:
Convert implementation and benchmark results into a clear engineering conclusion.

Implementation notes:
- Add final report under `docs`.
- Include architecture diagram.
- Include benchmark tables.
- Include trade-off matrix.
- Include recommendations for REST, GraphQL, and gRPC.

Acceptance criteria:
- Report explains when each protocol is strongest.
- Claims are backed by project evidence.
- README links to the final report.

## Recommended Working Order

Work the issues in numeric order until Issue 14. After that, keep going in order unless you discover a missing foundation piece.

Do not start benchmarking before the first two scenarios are stable. Benchmarks against half-built flows create noisy conclusions.

Do not add REST or GraphQL directly to internal services. Keep REST and GraphQL at `api-service`; keep internal services gRPC-only.

Do not let `api-service` connect to any database. If it needs data, it must call the owning service.

## Repository Labels to Use

Suggested labels:

- `type: setup`
- `type: feature`
- `type: test`
- `type: docs`
- `type: benchmark`
- `area: gradle`
- `area: grpc`
- `area: rest`
- `area: graphql`
- `area: database`
- `area: observability`

## Definition of Done

For every issue:

- Code builds.
- Relevant tests pass.
- Local run instructions still work.
- Service boundaries are preserved.
- Any new endpoint or RPC has a small example in docs or tests.
