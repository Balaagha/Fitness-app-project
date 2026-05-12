#!/bin/bash
# ------------------------------------------------------------------
# resolve-feature.sh — resolve the "effective" feature name
#
# Resolution order:
#   1. Active pointer (.claude/features/.active) — primary source
#   2. (no fallback) — branch-independent by design
#
# Prints the resolved feature name to stdout (empty if none).
# Exits 0 always.
# ------------------------------------------------------------------

PROJECT_DIR="${CLAUDE_PROJECT_DIR:-.}"
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

ACTIVE=$("$SCRIPT_DIR/get-active.sh")
if [ -n "$ACTIVE" ]; then
  echo "$ACTIVE"
fi
exit 0
