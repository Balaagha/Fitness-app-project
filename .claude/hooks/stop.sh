#!/bin/bash
# ------------------------------------------------------------------
# Stop hook: propose-confirm decision logging
#
# Blocks the stop ONCE per session when an active feature is set,
# asking Claude to propose decisions/findings/errors. User confirms
# (y / numbers / n); only approved entries are written to feature.md.
#
# Behavior:
#   - Fires only when .active pointer resolves to a feature.md
#   - Fires only once per session_id (marker file)
#   - First stop: exit 2 with prompt in stderr → Claude drafts
#   - Subsequent stops in same session: exit 0 → normal termination
#   - If the user replies "n" / "none", nothing is written and the
#     hook won't fire again for this session. Next session will
#     re-trigger (new session_id = new marker).
# ------------------------------------------------------------------

set -o pipefail

INPUT=$(cat 2>/dev/null || echo '{}')

PROJECT_DIR="${CLAUDE_PROJECT_DIR:-.}"
SCRIPT_DIR="$PROJECT_DIR/.claude/scripts"

# Ralph Loop bypass: when the ralph-loop plugin is actively running an
# autonomous iteration in this project, skip propose-confirm so the loop
# is not interrupted. The plugin creates/removes this state file itself.
if [ -f "$PROJECT_DIR/.claude/ralph-loop.local.md" ]; then
  exit 0
fi

# Extract session_id (jq if available, grep fallback)
SESSION_ID=""
if [ -x "$SCRIPT_DIR/json-extract.sh" ]; then
  SESSION_ID=$(printf '%s' "$INPUT" | "$SCRIPT_DIR/json-extract.sh" session_id)
fi
SESSION_ID="${SESSION_ID:-default-$$}"

MARKER_DIR="$PROJECT_DIR/.claude/backups/stops"
MARKER="$MARKER_DIR/$SESSION_ID"

# Resolve active feature
FEATURE=""
if [ -x "$SCRIPT_DIR/resolve-feature.sh" ]; then
  FEATURE=$("$SCRIPT_DIR/resolve-feature.sh")
fi
FEATURE_FILE="$PROJECT_DIR/.claude/features/$FEATURE/feature.md"

# No active feature → allow stop
[ -z "$FEATURE" ] && exit 0
[ ! -f "$FEATURE_FILE" ] && exit 0

# Optional plug-in extension point: if the project drops an executable
# script at .claude/hooks/post-stop.local.sh, we run it fire-and-forget
# before the propose-confirm prompt. Useful for project-specific scans
# (e.g. pattern detectors that append to notes.md). Output discarded so
# it can never interfere with the stop marker / exit 2 flow below.
if [ -x "$PROJECT_DIR/.claude/hooks/post-stop.local.sh" ]; then
  bash "$PROJECT_DIR/.claude/hooks/post-stop.local.sh" >/dev/null 2>&1 || true
fi

# Already prompted this session → allow stop
if [ -f "$MARKER" ]; then
  exit 0
fi

# First stop this session → block once and prompt for decisions
mkdir -p "$MARKER_DIR" 2>/dev/null
touch "$MARKER"

cat >&2 <<EOF
[feature-memory] Session wrap-up for feature: $FEATURE

Before stopping, review this session and propose entries to log in:
  $FEATURE_FILE

Draft in this exact format so the user can confirm quickly:

  DECISIONS (architecture choices, rejected alternatives, contracts agreed):
  1. <decision> — <one-line rationale>
  2. ...

  FINDINGS (research results, gotchas, API quirks, reusable insights):
  1. <finding>
  2. ...

  ERRORS (3-strike protocol failures with their resolution):
  1. <error> — <resolution>
  2. ...

Rules:
  • Include only entries that have lasting value beyond this chat
  • Exclude: trivial edits, intermediate reasoning, superseded ideas
  • If nothing substantive happened, write exactly: "No entries to log"

After drafting, wait for user confirmation:
  - "y" or "all"      → write every proposed entry
  - "n" or "none"     → skip (write nothing; hook will NOT fire again this session)
  - "1,3" or "d1 f2"  → selective (d=decisions, f=findings, e=errors)

Only after user confirms, edit feature.md: append approved entries to the
correct section under today's date. Do not overwrite existing rows.
EOF

exit 2
