#!/bin/bash
# ------------------------------------------------------------------
# get-active.sh — print the active feature name (stdout)
# Prints empty string if no active feature. Always exits 0.
# ------------------------------------------------------------------

PROJECT_DIR="${CLAUDE_PROJECT_DIR:-.}"
ACTIVE_FILE="$PROJECT_DIR/.claude/features/.active"

if [ -f "$ACTIVE_FILE" ]; then
  tr -d '[:space:]' < "$ACTIVE_FILE"
fi
exit 0
