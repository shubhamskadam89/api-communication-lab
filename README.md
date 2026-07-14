# API Communication Lab: REST vs GraphQL vs gRPC

This repository contains the architecture, implementation guide, and milestones for the **API Communication Lab**. 

The goal of this project is to build the same backend capabilities using different API communication styles (**REST**, **GraphQL**, and **gRPC**) under a consistent domain, and measure the engineering trade-offs using objective benchmarks.

---

## 🏗️ System Architecture

The project is structured as a **Gradle multi-module Spring Boot monorepo** containing:
* `api-service`: External entry point gateway exposing REST, GraphQL, and aggregator gRPC endpoints. It aggregates data by querying internal services via gRPC.
* `user-service`: Owns users and profiles; exposes gRPC only.
* `repository-service`: Owns repositories, issues, and pull requests; exposes gRPC only.
* `activity-service`: Owns activity feed, notifications, and events; exposes gRPC only.
* `shared-proto`: Shared protobuf contracts and generated gRPC stubs.
* **Database infrastructure**: A single Postgres container initializing three logical databases: `user_db`, `repository_db`, and `activity_db`.

---

## 📅 15-Day Milestone Schedule

Here is the daily breakdown mapping the milestones to the project's **40 Issues** defined in the [Build Guide](docs/api-communication-lab-guide.md):

| Day | Phase / Milestone | Key Focus & Issues |
| :---: | :--- | :--- |
| **Day 1** | **Project Setup** | Initialize Gradle Monorepo, docker-compose, and Spring Boot skeletons ([Issues 1-8](docs/project-timeline.md#day-1-project-setup-milestone-1)). |
| **Days 2–3** | **API Basics** | Configure Protobuf compiler and build first REST-to-gRPC boundary channel ([Issues 9-14](docs/project-timeline.md#day-2-api-basics---proto-setup-milestone-2---part-1)). |
| **Days 4–6** | **API Client & Core** | Add isolated flyway schemas, seed data, and build the Dashboard aggregator ([Issues 15-26](docs/project-timeline.md#day-4-persistence-setup-milestone-3---part-1)). |
| **Days 7–8** | **Error Handling** | Model repository projections and handle nested errors & service timeouts ([Issues 27-32](docs/project-timeline.md#day-7-error-mappings--resource-paths-milestone-5---part-1)). |
| **Days 9–11** | **Integration & Baseline** | Implement paginated streams, gRPC aggregate services, and check data consistency ([Issues 33-38](docs/project-timeline.md#day-9-feed-pagination--streaming-integration-milestone-6---part-1)). |
| **Days 12–13**| **Testing & Benchmarks** | Execute load tests using `k6` and `ghz` profiles, measuring latencies and payload sizes ([Issue 39](docs/project-timeline.md#days-1213-observability--load-testing-milestone-8)). |
| **Day 14** | **Documentation** | Document architecture decision logs (ADRs) and output comparison report ([Issue 40](docs/project-timeline.md#day-14-documentation-milestone-9)). |
| **Day 15** | **Final Review** | Polish project, verify tests run successfully, and prepare submission. |

> [!TIP]
> For a detailed, issue-by-issue view of each day's execution items, check out the **[Detailed 15-Day Timeline Document](docs/project-timeline.md)**.

---

## 🛠️ Getting Started

For a step-by-step walkthrough on how to set up the build system, write the protobuf contracts, integrate databases, and configure endpoints, refer to the **[Step-by-Step Build Guide](docs/api-communication-lab-guide.md)**.

To initialize milestones, labels, and issue tickets inside your GitHub repository automatically, you can run the provided provisioning script:
```bash
./scripts/create-github-issues.sh OWNER/REPO
```
