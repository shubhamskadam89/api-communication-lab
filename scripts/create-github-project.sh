#!/usr/bin/env bash
set -euo pipefail

# Ensure jq is installed
if ! command -v jq &> /dev/null; then
  echo "Error: jq is required but not installed." >&2
  exit 1
fi

OWNER="shubhamskadam89"
TITLE="API Communication Lab Timeline"

echo "=========================================="
echo "Creating GitHub Project: $TITLE"
echo "=========================================="

# Create project and extract number & URL
PROJECT_DATA=$(gh project create --owner "$OWNER" --title "$TITLE" --format json)
PROJECT_NUM=$(echo "$PROJECT_DATA" | jq '.number')
PROJECT_URL=$(echo "$PROJECT_DATA" | jq -r '.url')

echo "✓ Project created successfully!"
echo "  URL: $PROJECT_URL"
echo "  Number: $PROJECT_NUM"
echo

echo "Fetching issues from repository..."
issues=$(gh issue list --limit 100 --json number,url | jq -r '.[] | "\(.number) \(.url)"' | sort -n)

echo "Adding issues to project board..."
while read -r num url; do
  if [[ -n "$num" && -n "$url" ]]; then
    echo "→ Adding issue #$num..."
    gh project item-add "$PROJECT_NUM" --owner "$OWNER" --url "$url" > /dev/null
  fi
done <<< "$issues"

echo
echo "=========================================="
echo "✓ All issues successfully added to GitHub Project board!"
echo "View your board here: $PROJECT_URL"
echo "=========================================="
