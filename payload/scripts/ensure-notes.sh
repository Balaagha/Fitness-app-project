#!/bin/bash
# ------------------------------------------------------------------
# ensure-notes.sh — make sure notes.md exists for a feature
# Usage: ensure-notes.sh <feature-name>
#
# Idempotent: if notes.md already exists, does nothing and exits 0.
# Otherwise renders .claude/templates/notes.md with the feature name
# and current timestamp.
#
# Safe to call from commands that may run on legacy features (created
# before notes.md was part of the system).
# ------------------------------------------------------------------

set -e

FEATURE_NAME="$1"
if [ -z "$FEATURE_NAME" ]; then
  echo "Usage: ensure-notes.sh <feature-name>" >&2
  exit 1
fi

PROJECT_DIR="${CLAUDE_PROJECT_DIR:-.}"
FEATURE_DIR="$PROJECT_DIR/.claude/features/$FEATURE_NAME"
NOTES_FILE="$FEATURE_DIR/notes.md"
TEMPLATE="$PROJECT_DIR/.claude/templates/notes.md"

if [ ! -d "$FEATURE_DIR" ]; then
  echo "Feature directory not found: $FEATURE_DIR" >&2
  exit 1
fi

if [ -f "$NOTES_FILE" ]; then
  exit 0
fi

if [ ! -f "$TEMPLATE" ]; then
  echo "Notes template not found: $TEMPLATE" >&2
  exit 1
fi

DATE=$(date +%Y-%m-%d)
TIME=$(date '+%Y-%m-%d %H:%M')

awk -v name="$FEATURE_NAME" \
    -v date="$DATE" \
    -v time="$TIME" \
'
{
  gsub(/\{\{FEATURE_NAME\}\}/, name)
  gsub(/\{\{DATE\}\}/, date)
  gsub(/\{\{TIME\}\}/, time)
  print
}
' "$TEMPLATE" > "$NOTES_FILE"

echo "Created $NOTES_FILE"
