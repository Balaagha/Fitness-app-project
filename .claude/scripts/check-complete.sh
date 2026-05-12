#!/bin/bash
# ------------------------------------------------------------------
# check-complete.sh — report phase completion state for a feature
# Usage: check-complete.sh [feature-name]
#
# Without an arg, resolves the feature via the active pointer
# (.claude/features/.active). Always exits 0 — incomplete is a
# normal state, not an error.
#
# Recognises both live and archived features. Reports the feature
# type and, for type=feature, the Feature Spec fill-in status
# (matches orchestrator 4-phase: Spec → Generate → Review → Finalize).
# ------------------------------------------------------------------

set -o pipefail

PROJECT_DIR="${CLAUDE_PROJECT_DIR:-.}"
SCRIPT_DIR="$PROJECT_DIR/.claude/scripts"
FEATURE_NAME="$1"

if [ -z "$FEATURE_NAME" ] && [ -x "$SCRIPT_DIR/resolve-feature.sh" ]; then
  FEATURE_NAME=$("$SCRIPT_DIR/resolve-feature.sh")
fi

if [ -z "$FEATURE_NAME" ]; then
  echo "No active feature (pointer empty)."
  echo "Use /feature-start, /feature-resume, or /feature-list."
  exit 0
fi

FEATURE_FILE="$PROJECT_DIR/.claude/features/$FEATURE_NAME/feature.md"
LOCATION="active"

if [ ! -f "$FEATURE_FILE" ]; then
  ARCHIVED="$PROJECT_DIR/.claude/features/_archive/$FEATURE_NAME/feature.md"
  if [ -f "$ARCHIVED" ]; then
    FEATURE_FILE="$ARCHIVED"
    LOCATION="archived"
  else
    echo "No feature.md found for '$FEATURE_NAME' (checked active + archive)."
    exit 0
  fi
fi

count_matches() {
  grep -F "$1" "$2" 2>/dev/null | wc -l | tr -d ' '
}

count_re() {
  grep -E "$1" "$2" 2>/dev/null | wc -l | tr -d ' '
}

extract_meta() {
  grep -E "^- \*\*$1\*\*:" "$FEATURE_FILE" 2>/dev/null \
    | head -1 \
    | sed -E "s/^- \*\*$1\*\*:[[:space:]]*//; s/^\`//; s/\`$//"
}

TYPE=$(extract_meta 'Type')
TYPE="${TYPE:-feature}"
BRANCH=$(extract_meta 'Branch')

TOTAL=$(count_re '^### Phase' "$FEATURE_FILE")
COMPLETE=$(count_matches '**Status:** complete' "$FEATURE_FILE")
IN_PROGRESS=$(count_matches '**Status:** in_progress' "$FEATURE_FILE")
PENDING=$(count_matches '**Status:** pending' "$FEATURE_FILE")
BLOCKED=$(count_matches '**Status:** blocked' "$FEATURE_FILE")

echo "Feature: $FEATURE_NAME  (location: $LOCATION)"
echo "Type: $TYPE${BRANCH:+ | Branch: $BRANCH}"
echo "Phases: $COMPLETE/$TOTAL complete | $IN_PROGRESS in progress | $PENDING pending | $BLOCKED blocked"

# Orchestrator 4-phase awareness (type=feature only)
if [ "$TYPE" = "feature" ]; then
  SPEC_FILLED="no"
  # Any content under "### API Contract" / "### State Contract" etc.
  if grep -q '^### API Contract' "$FEATURE_FILE" 2>/dev/null; then
    # Crude check: any non-empty non-comment line within 8 lines of the heading
    if awk '/^### API Contract/{f=1;next} f && /^###/{exit} f && !/^<!--/ && !/^$/{print;exit}' \
        "$FEATURE_FILE" 2>/dev/null | grep -q '.'; then
      SPEC_FILLED="yes"
    fi
  fi
  echo "Feature Spec filled: $SPEC_FILLED"
fi

if [ "$COMPLETE" -ge "$TOTAL" ] && [ "$TOTAL" -gt 0 ]; then
  echo "Status: ALL PHASES COMPLETE"
elif [ "$BLOCKED" -gt 0 ]; then
  echo "Status: BLOCKED"
else
  echo "Status: in progress"
fi

exit 0
