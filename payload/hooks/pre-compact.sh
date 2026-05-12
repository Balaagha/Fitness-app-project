#!/bin/bash
# ------------------------------------------------------------------
# PreCompact hook: transcript backup + in-session state reminder
#
# Fires right before Claude Code compacts the context window:
#   1. Copy the raw transcript to .claude/backups/transcripts/
#   2. Remind Claude to flush unsaved decisions to feature.md
# ------------------------------------------------------------------

set -o pipefail

INPUT=$(cat 2>/dev/null || echo '{}')

PROJECT_DIR="${CLAUDE_PROJECT_DIR:-.}"
SCRIPT_DIR="$PROJECT_DIR/.claude/scripts"
BACKUP_DIR="$PROJECT_DIR/.claude/backups/transcripts"
mkdir -p "$BACKUP_DIR" 2>/dev/null

# Extract transcript path (jq if available, grep fallback)
TRANSCRIPT=""
if [ -x "$SCRIPT_DIR/json-extract.sh" ]; then
  TRANSCRIPT=$(printf '%s' "$INPUT" | "$SCRIPT_DIR/json-extract.sh" transcript_path)
fi

# Best-effort backup — don't fail the hook if the file is unreadable
if [ -n "$TRANSCRIPT" ] && [ -f "$TRANSCRIPT" ]; then
  TS=$(date +%Y%m%d-%H%M%S)
  cp "$TRANSCRIPT" "$BACKUP_DIR/$TS.jsonl" 2>/dev/null || true

  # Rotate: keep only last 20 backups.
  # Portable: macOS xargs has no -r, so filter empty pipes explicitly.
  OLD_BACKUPS=$(ls -t "$BACKUP_DIR"/*.jsonl 2>/dev/null | tail -n +21)
  if [ -n "$OLD_BACKUPS" ]; then
    printf '%s\n' "$OLD_BACKUPS" | xargs rm -f 2>/dev/null || true
  fi
fi

# Resolve active feature for the reminder (informational)
FEATURE=""
if [ -x "$SCRIPT_DIR/resolve-feature.sh" ]; then
  FEATURE=$("$SCRIPT_DIR/resolve-feature.sh")
fi

cat <<EOF
[feature-memory] Context compaction is about to run.

${FEATURE:+Active feature: $FEATURE}
If you have unsaved decisions, findings, or a known error state from this
session, APPEND them to feature.md right now — compaction will summarize
away conversation details that may matter later. The SessionStart hook
will re-inject feature.md after compaction, but only what's on disk survives.

Transcript has been backed up to: .claude/backups/transcripts/
EOF

# Dump the full notes.md so working-memory survives compaction. Highlight
# [invariant] and [criteria] separately — these are the sticky signals the
# model must not lose mid-feature.
if [ -n "$FEATURE" ]; then
  NOTES_FILE="$PROJECT_DIR/.claude/features/$FEATURE/notes.md"
  if [ -f "$NOTES_FILE" ]; then
    echo ""
    echo "--- notes.md (full, pre-compact dump) ---"
    cat "$NOTES_FILE"
    echo ""

    STICKY=$(grep -E '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2} \[(invariant|criteria)\]' "$NOTES_FILE" 2>/dev/null)
    if [ -n "$STICKY" ]; then
      echo "--- STICKY lines (must survive compaction) ---"
      echo "$STICKY"
      echo ""
    fi
  fi
fi

exit 0
