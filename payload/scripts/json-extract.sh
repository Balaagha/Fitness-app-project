#!/bin/bash
# ------------------------------------------------------------------
# json-extract.sh — extract a top-level string field from JSON on stdin
# Usage: echo "$INPUT" | json-extract.sh <field_name>
#
# Uses jq when available (safe), falls back to grep/sed otherwise.
# ------------------------------------------------------------------

FIELD="$1"
[ -z "$FIELD" ] && exit 1

INPUT=$(cat)

if command -v jq >/dev/null 2>&1; then
  echo "$INPUT" | jq -r --arg k "$FIELD" '.[$k] // empty' 2>/dev/null
else
  # Fallback: handles simple string values only. Escaped quotes inside
  # values may fail; jq is preferred whenever possible.
  echo "$INPUT" \
    | grep -o "\"$FIELD\"[[:space:]]*:[[:space:]]*\"[^\"]*\"" \
    | head -1 \
    | sed -E "s/.*\"([^\"]+)\"$/\1/"
fi
exit 0
