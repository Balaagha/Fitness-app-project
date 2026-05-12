#!/bin/bash
# ------------------------------------------------------------------
# init-feature.sh — bootstrap a new feature directory + feature.md
# Usage:
#   init-feature.sh <name> [type] [branch] [related]
#
# Parameters:
#   name    - feature name (normalized: lowercase, dashes)
#   type    - feature | bug-fix | research | refactor | investigation
#             (default: feature)
#   branch  - branch name to record, or empty   (default: "n/a")
#   related - comma-list of related feature names, or empty (default: "none")
#
# Idempotent: does NOT overwrite an existing feature.md; restore from
# _archive/ is handled by /feature-resume, not here.
#
# Note: a hard-coded skill list was intentionally omitted — project skills
# auto-trigger on their own keywords, so listing them in feature.md would
# be redundant noise.
# ------------------------------------------------------------------

set -e

FEATURE_NAME="$1"
TYPE="${2:-feature}"
BRANCH="${3:-n/a}"
RELATED="${4:-none}"

[ -z "$BRANCH" ] && BRANCH="n/a"
[ -z "$RELATED" ] && RELATED="none"

if [ -z "$FEATURE_NAME" ]; then
  echo "Usage: init-feature.sh <name> [type] [branch] [related]" >&2
  exit 1
fi

# Normalize name
FEATURE_NAME=$(echo "$FEATURE_NAME" \
  | tr '[:upper:]' '[:lower:]' \
  | sed -E 's/[^a-z0-9-]+/-/g' \
  | sed -E 's/^-+|-+$//g')

if [ -z "$FEATURE_NAME" ]; then
  echo "Invalid feature name" >&2
  exit 1
fi

# Validate type
case "$TYPE" in
  feature|bug-fix|research|refactor|investigation) ;;
  *)
    echo "Invalid type: $TYPE" >&2
    echo "Expected: feature | bug-fix | research | refactor | investigation" >&2
    exit 1
    ;;
esac

PROJECT_DIR="${CLAUDE_PROJECT_DIR:-.}"
FEATURE_DIR="$PROJECT_DIR/.claude/features/$FEATURE_NAME"
TEMPLATE="$PROJECT_DIR/.claude/templates/feature-$TYPE.md"

# Never overwrite existing active feature
if [ -f "$FEATURE_DIR/feature.md" ]; then
  echo "Feature '$FEATURE_NAME' already exists at $FEATURE_DIR/feature.md"
  exit 0
fi

# Guard: if archived with same name, tell the user to resume instead
ARCHIVE_FILE="$PROJECT_DIR/.claude/features/_archive/$FEATURE_NAME/feature.md"
if [ -f "$ARCHIVE_FILE" ]; then
  echo "An archived feature '$FEATURE_NAME' already exists."
  echo "Use: /feature-resume $FEATURE_NAME    (to restore it)"
  echo "Or choose a different name."
  exit 2
fi

mkdir -p "$FEATURE_DIR"

DATE=$(date +%Y-%m-%d)
TIME=$(date '+%Y-%m-%d %H:%M')

if [ ! -f "$TEMPLATE" ]; then
  echo "Template not found: $TEMPLATE" >&2
  exit 1
fi

render_template() {
  awk -v name="$FEATURE_NAME" \
      -v date="$DATE" \
      -v time="$TIME" \
      -v type="$TYPE" \
      -v branch="$BRANCH" \
      -v related="$RELATED" \
  '
  {
    gsub(/\{\{FEATURE_NAME\}\}/, name)
    gsub(/\{\{DATE\}\}/, date)
    gsub(/\{\{TIME\}\}/, time)
    gsub(/\{\{TYPE\}\}/, type)
    gsub(/\{\{BRANCH\}\}/, branch)
    gsub(/\{\{RELATED\}\}/, related)
    print
  }
  ' "$1" > "$2"
}

render_template "$TEMPLATE" "$FEATURE_DIR/feature.md"

# Create the working-memory file (notes.md) alongside feature.md.
# Template is shared across all feature types.
NOTES_TEMPLATE="$PROJECT_DIR/.claude/templates/notes.md"
if [ -f "$NOTES_TEMPLATE" ]; then
  render_template "$NOTES_TEMPLATE" "$FEATURE_DIR/notes.md"
fi

# Set this as the active feature
"$PROJECT_DIR/.claude/scripts/set-active.sh" "$FEATURE_NAME" >/dev/null

echo "Created $FEATURE_DIR/feature.md"
[ -f "$FEATURE_DIR/notes.md" ] && echo "Created $FEATURE_DIR/notes.md (working memory)"
echo "Feature: $FEATURE_NAME (type: $TYPE)"
echo "Active pointer set → this feature will auto-load on next session."
