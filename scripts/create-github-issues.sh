#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
GUIDE_FILE="$ROOT_DIR/docs/api-communication-lab-guide.md"

echo "This script creates milestones, labels, and the 40 detailed issues from:"
echo "$GUIDE_FILE"
echo
echo "Usage:"
echo "  ./scripts/create-github-issues.sh"
echo "  ./scripts/create-github-issues.sh OWNER/REPO"
echo

if [[ "${1:-}" != "" ]]; then
  export GH_REPO="$1"
fi

gh repo view >/dev/null

create_milestone() {
  local title="$1"
  local description="$2"

  gh api repos/{owner}/{repo}/milestones \
    --method POST \
    -f "title=$title" \
    -f "description=$description" \
    --silent || true
}

create_label() {
  local name="$1"
  local color="$2"
  local description="$3"

  gh label create "$name" \
    --color "$color" \
    --description "$description" \
    --force >/dev/null
}

infer_labels() {
  local title_and_body="$1"
  local labels="type: feature"

  if [[ "$title_and_body" == *"setup"* || "$title_and_body" == *"Skeleton"* || "$title_and_body" == *"Configure"* || "$title_and_body" == *"Initialize"* ]]; then
    labels="type: setup"
  fi
  if [[ "$title_and_body" == *"Test"* || "$title_and_body" == *"Tests"* ]]; then
    labels="type: test"
  fi
  if [[ "$title_and_body" == *"Documentation"* || "$title_and_body" == *"README"* || "$title_and_body" == *"Report"* ]]; then
    labels="type: docs"
  fi
  if [[ "$title_and_body" == *"Benchmark"* || "$title_and_body" == *"Payload Size"* || "$title_and_body" == *"k6"* ]]; then
    labels="type: benchmark"
  fi

  [[ "$title_and_body" == *"Gradle"* ]] && labels="$labels,area: gradle"
  [[ "$title_and_body" == *"gRPC"* || "$title_and_body" == *"Proto"* || "$title_and_body" == *"protobuf"* ]] && labels="$labels,area: grpc"
  [[ "$title_and_body" == *"REST"* ]] && labels="$labels,area: rest"
  [[ "$title_and_body" == *"GraphQL"* ]] && labels="$labels,area: graphql"
  [[ "$title_and_body" == *"Database"* || "$title_and_body" == *"Postgres"* || "$title_and_body" == *"Flyway"* || "$title_and_body" == *"JPA"* || "$title_and_body" == *"Seed"* ]] && labels="$labels,area: database"
  [[ "$title_and_body" == *"Timing"* || "$title_and_body" == *"Observability"* || "$title_and_body" == *"Logs"* ]] && labels="$labels,area: observability"

  echo "$labels"
}

create_milestone "Foundation and Gradle Monorepo" "Gradle monorepo, service skeletons, health checks, and local Postgres infrastructure."
create_milestone "Proto Contracts and gRPC Skeleton" "Shared protobuf contracts, generated stubs, and first working REST-to-gRPC flow."
create_milestone "Persistence and Seed Data" "Service-owned schemas, migrations, JPA repositories, and deterministic seed data."
create_milestone "Scenario 1 - Developer Dashboard" "First complete REST vs GraphQL comparison backed by internal gRPC aggregation."
create_milestone "Scenario 2 - Repository Details" "Deep relationship scenario with REST resources, REST aggregate, and GraphQL field selection."
create_milestone "Scenario 3 - Activity Feed and Streaming" "Paginated feed behavior and internal gRPC streaming."
create_milestone "gRPC Aggregation Baseline" "Scenario-level gRPC entry points from api-service for fair REST vs GraphQL vs gRPC comparison."
create_milestone "Observability and Benchmarking" "Repeatable load tests, timing logs, payload-size measurement, and benchmark results."
create_milestone "Final Comparison and Documentation" "Architecture docs, ADRs, benchmark interpretation, and final protocol recommendations."

create_label "type: setup" "bfd4f2" "Project setup and scaffolding"
create_label "type: feature" "a2eeef" "User-facing or service capability"
create_label "type: test" "d4c5f9" "Test coverage"
create_label "type: docs" "0075ca" "Documentation"
create_label "type: benchmark" "fbca04" "Benchmarking and measurement"
create_label "area: gradle" "5319e7" "Gradle build system"
create_label "area: grpc" "0e8a16" "gRPC and protobuf"
create_label "area: rest" "1d76db" "REST API"
create_label "area: graphql" "c2e0c6" "GraphQL API"
create_label "area: database" "f9d0c4" "Persistence and database"
create_label "area: observability" "ededed" "Logging, tracing, and metrics"

tmp_dir="$(mktemp -d)"
trap 'rm -rf "$tmp_dir"' EXIT

awk -v dir="$tmp_dir" '
  /^### Issue [0-9]+:/ {
    if (body) {
      close(body)
      close(title_file)
    }
    count += 1
    title = $0
    sub(/^### Issue [0-9]+: /, "", title)
    body = sprintf("%s/%02d.md", dir, count)
    title_file = sprintf("%s/%02d.title", dir, count)
    print title > title_file
    next
  }
  /^## Recommended Working Order/ {
    if (body) {
      close(body)
      close(title_file)
    }
    body = ""
    next
  }
  body {
    print > body
  }
' "$GUIDE_FILE"

issue_count=0
for body_file in "$tmp_dir"/*.md; do
  [[ -e "$body_file" ]] || continue

  issue_count=$((issue_count + 1))
  title_file="${body_file%.md}.title"
  title="$(cat "$title_file")"
  milestone="$(awk -F': ' '/^Milestone: / { print $2; exit }' "$body_file")"
  labels="$(infer_labels "$title $(cat "$body_file")")"

  echo "Creating issue $issue_count: $title"
  gh issue create \
    --title "$title" \
    --milestone "$milestone" \
    --label "$labels" \
    --body-file "$body_file" >/dev/null
done

echo
echo "Done. Created $issue_count issues."
