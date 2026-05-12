#!/bin/bash
# ------------------------------------------------------------------
# resume-feature.sh — resume a paused or archived feature
# Usage: resume-feature.sh <feature-name>
#
# Behaviour:
#   - If feature is already active (features/<n>/feature.md exists)
#     → just update the pointer to it
#   - If feature is archived (features/_archive/<n>/feature.md exists)
#     → move it back to features/<n>/ and update the pointer
#   - If neither exists → error
#
# Appends a "Resumed on <date>" line to the Progress Log.
# ------------------------------------------------------------------

set -e

FEATURE_NAME="$1"
if [ -z "$FEATURE_NAME" ]; then
  echo "Usage: resume-feature.sh <feature-name>" >&2
  exit 1
fi

# Normalize
FEATURE_NAME=$(echo "$FEATURE_NAME" \
  | tr '[:upper:]' '[:lower:]' \
  | sed -E 's/[^a-z0-9-]+/-/g' \
  | sed -E 's/^-+|-+$//g')

PROJECT_DIR="${CLAUDE_PROJECT_DIR:-.}"
LIVE_DIR="$PROJECT_DIR/.claude/features/$FEATURE_NAME"
ARCHIVE_DIR="$PROJECT_DIR/.claude/features/_archive/$FEATURE_NAME"

MOVED_FROM_ARCHIVE="no"

if [ -f "$LIVE_DIR/feature.md" ]; then
  :  # Already live, nothing to move
elif [ -f "$ARCHIVE_DIR/feature.md" ]; then
  mv "$ARCHIVE_DIR" "$LIVE_DIR"
  MOVED_FROM_ARCHIVE="yes"
else
  echo "No feature '$FEATURE_NAME' found (active or archived)." >&2
  exit 1
fi

TIME=$(date '+%Y-%m-%d %H:%M')
LOG_ENTRY="- $TIME — Resumed"
[ "$MOVED_FROM_ARCHIVE" = "yes" ] && LOG_ENTRY="$LOG_ENTRY from archive"

# Append to Progress Log — find the heading and insert after the existing bullets.
# Portable approach: awk-based append to the end of the Progress Log block.
FEATURE_FILE="$LIVE_DIR/feature.md"
TMP=$(mktemp)
awk -v entry="$LOG_ENTRY" '
BEGIN { in_log = 0; inserted = 0; blank_streak = 0 }
{
  if (in_log && !inserted) {
    if ($0 == "") { blank_streak++; next }
    if (/^## /) {
      # Entry → one blank line → next heading
      print entry
      print ""
      print
      inserted = 1; in_log = 0; blank_streak = 0
      next
    }
    # Regular content inside log block: flush buffered blanks, continue
    while (blank_streak > 0) { print ""; blank_streak-- }
    print
    next
  }
  print
  if ($0 ~ /^## Progress Log/) { in_log = 1 }
}
END {
  if (in_log && !inserted) {
    while (blank_streak > 0) { print ""; blank_streak-- }
    print entry
  }
}
' "$FEATURE_FILE" > "$TMP" && mv "$TMP" "$FEATURE_FILE"

# Set active pointer
"$PROJECT_DIR/.claude/scripts/set-active.sh" "$FEATURE_NAME" >/dev/null

if [ "$MOVED_FROM_ARCHIVE" = "yes" ]; then
  echo "Restored '$FEATURE_NAME' from archive and set as active."
else
  echo "Resumed '$FEATURE_NAME' (was already live). Set as active."
fi
