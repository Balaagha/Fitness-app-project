#!/bin/bash
# ------------------------------------------------------------------
# SessionStart hook: pointer-based feature context loader
# Matchers: startup | resume | clear | compact
#
# Reads .claude/features/.active (pointer) and injects that
# feature's feature.md into Claude's context. Branch-independent:
# pause a feature, switch to another, resume archived work —
# all controlled by the pointer, not the git branch.
#
# Exits 0 always (a hook failure should never block session start).
# ------------------------------------------------------------------

set -o pipefail

INPUT=$(cat 2>/dev/null || echo '{}')

PROJECT_DIR="${CLAUDE_PROJECT_DIR:-.}"
SCRIPT_DIR="$PROJECT_DIR/.claude/scripts"

# Clean up stale stop-markers (older than 1 day)
find "$PROJECT_DIR/.claude/backups/stops" -type f -mtime +1 -delete 2>/dev/null

# Best-effort current branch (informational only)
BRANCH=""
if git -C "$PROJECT_DIR" rev-parse --git-dir >/dev/null 2>&1; then
  BRANCH=$(git -C "$PROJECT_DIR" branch --show-current 2>/dev/null)
fi

# Resolve active feature via pointer
FEATURE=""
if [ -x "$SCRIPT_DIR/resolve-feature.sh" ]; then
  FEATURE=$("$SCRIPT_DIR/resolve-feature.sh")
fi

FEATURE_FILE="$PROJECT_DIR/.claude/features/$FEATURE/feature.md"
NOTES_FILE="$PROJECT_DIR/.claude/features/$FEATURE/notes.md"
DECISIONS_LOG="$PROJECT_DIR/.claude/features/$FEATURE/decisions.jsonl"

# No active feature → hint at commands (not an error)
if [ -z "$FEATURE" ] || [ ! -f "$FEATURE_FILE" ]; then
  cat <<EOF
[feature-memory] No active feature.
  • Start a new one:    /feature-start <name>
  • Resume paused/archived: /feature-resume <name>
  • List all features:  /feature-list
${BRANCH:+Current branch (informational): $BRANCH}
EOF
  exit 0
fi

# Inject feature context
echo "[feature-memory] Active feature: $FEATURE${BRANCH:+ | branch: $BRANCH}"
echo ""
echo "--- feature.md ---"
cat "$FEATURE_FILE"
echo ""

# If working-memory notes exist, inject only the tail so context stays small.
# Always surface [invariant] and [criteria] lines in full (sticky signal),
# regardless of where in the file they appear.
if [ -f "$NOTES_FILE" ]; then
  NOTES_TAIL=$(tail -50 "$NOTES_FILE" 2>/dev/null)
  if [ -n "$NOTES_TAIL" ]; then
    echo "--- notes.md (tail 50 lines) ---"
    echo "$NOTES_TAIL"
    echo ""
  fi
  # Pull sticky entries from the whole file — invariants/criteria matter
  # even if the entry is older than the 50-line window. Anchor on the
  # timestamp prefix so the template's comment block doesn't leak in.
  STICKY=$(grep -E '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2} \[(invariant|criteria)\]' "$NOTES_FILE" 2>/dev/null | tail -30)
  if [ -n "$STICKY" ]; then
    echo "--- notes.md sticky (invariants + criteria) ---"
    echo "$STICKY"
    echo ""
  fi
fi

# If append-only decisions log exists, show recent entries
if [ -f "$DECISIONS_LOG" ]; then
  RECENT=$(tail -10 "$DECISIONS_LOG" 2>/dev/null)
  if [ -n "$RECENT" ]; then
    echo "--- recent parallel-session decisions (last 10) ---"
    echo "$RECENT"
    echo ""
  fi
fi

echo "--- End of feature context ---"
echo ""
echo "Reminders:"
echo "  • Re-read feature.md before architectural decisions"
echo "  • Update Progress Log after completing a phase"
echo "  • Stop hook at session end will ask for decisions to log"
echo "  • Drop quick one-liners with /feature-note [tag] <text> or append"
echo "    a finding to feature.md with /feature-finding <text>"
echo "  • Pause with /feature-pause, switch with /feature-resume <name>"

exit 0
