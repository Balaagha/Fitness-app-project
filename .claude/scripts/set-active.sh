#!/bin/bash
# ------------------------------------------------------------------
# set-active.sh — write the active feature pointer
# Usage: set-active.sh <feature-name>   # activate
#        set-active.sh --clear           # pause (clear pointer)
#
# The pointer at .claude/features/.active is the single source of
# truth for "which feature context loads this session". It is
# branch-independent so users can:
#   - run multiple features on the same branch
#   - pause and resume across sessions
#   - resume an archived feature for a follow-up bug
# ------------------------------------------------------------------

set -e

PROJECT_DIR="${CLAUDE_PROJECT_DIR:-.}"
ACTIVE_FILE="$PROJECT_DIR/.claude/features/.active"

mkdir -p "$PROJECT_DIR/.claude/features" 2>/dev/null

if [ "$1" = "--clear" ] || [ -z "$1" ]; then
  : > "$ACTIVE_FILE"
  echo "Active feature cleared."
  exit 0
fi

FEATURE_NAME="$1"

# Normalize: lowercase, alphanumeric + dash
FEATURE_NAME=$(echo "$FEATURE_NAME" \
  | tr '[:upper:]' '[:lower:]' \
  | sed -E 's/[^a-z0-9-]+/-/g' \
  | sed -E 's/^-+|-+$//g')

if [ -z "$FEATURE_NAME" ]; then
  echo "Invalid feature name" >&2
  exit 1
fi

FEATURE_FILE="$PROJECT_DIR/.claude/features/$FEATURE_NAME/feature.md"
ARCHIVE_FILE="$PROJECT_DIR/.claude/features/_archive/$FEATURE_NAME/feature.md"

if [ ! -f "$FEATURE_FILE" ] && [ ! -f "$ARCHIVE_FILE" ]; then
  echo "No feature '$FEATURE_NAME' found (active or archived)." >&2
  exit 1
fi

printf '%s' "$FEATURE_NAME" > "$ACTIVE_FILE"
echo "Active feature set: $FEATURE_NAME"
