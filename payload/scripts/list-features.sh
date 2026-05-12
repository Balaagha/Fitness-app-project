#!/bin/bash
# ------------------------------------------------------------------
# list-features.sh — list all features with their state
#
# Output design (2026-04-22 redesign):
#   - Fixed-width columns (name=30, type=10, phase=11, when=11)
#   - Glyphs: ● active, ○ paused, 🗄 archived
#   - Per-state counts + grand total
#   - Recency ordering (feature.md mtime desc) within each group
#   - Long names truncated with "…" to preserve alignment
#   - ANSI colors when stdout is a TTY and TERM != dumb
#
# Always exits 0.
# ------------------------------------------------------------------

set -o pipefail

PROJECT_DIR="${CLAUDE_PROJECT_DIR:-.}"
SCRIPT_DIR="$PROJECT_DIR/.claude/scripts"
FEATURES_DIR="$PROJECT_DIR/.claude/features"
ARCHIVE_DIR="$FEATURES_DIR/_archive"

# Column widths
COL_NAME=30
COL_TYPE=10
COL_PHASE=11
COL_WHEN=11

# ------------------------------------------------------------------
# Color helpers — only when TTY and TERM != dumb
# ------------------------------------------------------------------
if [ -t 1 ] && [ "${TERM:-dumb}" != "dumb" ] && [ -z "${NO_COLOR:-}" ]; then
  C_BOLD=$'\033[1m'
  C_DIM=$'\033[2m'
  C_GREEN=$'\033[32m'
  C_YELLOW=$'\033[33m'
  C_GRAY=$'\033[90m'
  C_CYAN=$'\033[36m'
  C_RESET=$'\033[0m'
else
  C_BOLD=""
  C_DIM=""
  C_GREEN=""
  C_YELLOW=""
  C_GRAY=""
  C_CYAN=""
  C_RESET=""
fi

# ------------------------------------------------------------------
# Parse metadata from feature.md
# ------------------------------------------------------------------
get_meta() {
  local file="$1" key="$2"
  grep -E "^- \*\*$key\*\*:" "$file" 2>/dev/null \
    | head -1 \
    | sed -E "s/^- \*\*$key\*\*:[[:space:]]*//; s/^\`//; s/\`$//"
}

# Extract "N / M" prefix from Current phase value, tolerate noisy trailing text
get_phase() {
  local file="$1"
  local raw
  raw=$(get_meta "$file" 'Current phase')
  [ -z "$raw" ] && { echo "?/?"; return; }
  # Try to capture leading "N / M" or "N/M"
  local n m
  n=$(printf '%s' "$raw" | sed -nE 's/^[[:space:]]*([0-9]+)[[:space:]]*\/[[:space:]]*([0-9]+).*/\1/p')
  m=$(printf '%s' "$raw" | sed -nE 's/^[[:space:]]*([0-9]+)[[:space:]]*\/[[:space:]]*([0-9]+).*/\2/p')
  if [ -n "$n" ] && [ -n "$m" ]; then
    echo "$n/$m"
  else
    echo "?/?"
  fi
}

# ------------------------------------------------------------------
# Relative time for a file mtime (macOS BSD stat)
# Output forms: "today", "Nd ago", "Nw ago", "Nmo ago", "Ny ago"
# ------------------------------------------------------------------
relative_time() {
  local file="$1"
  [ -e "$file" ] || { echo "—"; return; }
  local mtime now diff
  mtime=$(stat -f %m "$file" 2>/dev/null)
  [ -z "$mtime" ] && mtime=$(stat -c %Y "$file" 2>/dev/null) # fallback GNU
  [ -z "$mtime" ] && { echo "—"; return; }
  now=$(date +%s)
  diff=$(( now - mtime ))
  [ $diff -lt 0 ] && diff=0
  if   [ $diff -lt 86400 ];    then echo "today"
  elif [ $diff -lt 604800 ];   then echo "$((diff/86400))d ago"
  elif [ $diff -lt 2592000 ];  then echo "$((diff/604800))w ago"
  elif [ $diff -lt 31536000 ]; then echo "$((diff/2592000))mo ago"
  else                              echo "$((diff/31536000))y ago"
  fi
}

# mtime for sorting (0 on failure)
mtime_of() {
  local file="$1"
  local m
  m=$(stat -f %m "$file" 2>/dev/null)
  [ -z "$m" ] && m=$(stat -c %Y "$file" 2>/dev/null)
  echo "${m:-0}"
}

# ------------------------------------------------------------------
# Pad / truncate a display string to exactly WIDTH printable chars.
# Truncation appends "…" so total still fits WIDTH.
# ------------------------------------------------------------------
pad_or_trunc() {
  local s="$1" width="$2"
  local len=${#s}
  if [ $len -gt $width ]; then
    # Truncate with ellipsis
    local keep=$(( width - 1 ))
    [ $keep -lt 1 ] && keep=1
    s="${s:0:$keep}…"
  fi
  printf "%-${width}s" "$s"
}

# ------------------------------------------------------------------
# Render one feature row. Does NOT emit trailing newline; caller does.
#   $1 glyph  $2 name  $3 type  $4 phase  $5 when  $6 name-color
# ------------------------------------------------------------------
render_row() {
  local glyph="$1" name="$2" type="$3" phase="$4" when="$5" name_color="$6"
  local padded_name padded_type padded_phase padded_when
  padded_name=$(pad_or_trunc "$name"  $COL_NAME)
  padded_type=$(pad_or_trunc "$type"  $COL_TYPE)
  padded_phase=$(pad_or_trunc "$phase" $COL_PHASE)
  padded_when=$(pad_or_trunc "$when"  $COL_WHEN)
  printf "  %s %s%s%s  %s%s%s  %s%s%s  %s%s%s\n" \
    "$glyph" \
    "$name_color" "$padded_name" "$C_RESET" \
    "$C_DIM"     "$padded_type"  "$C_RESET" \
    "$C_DIM"     "$padded_phase" "$C_RESET" \
    "$C_GRAY"    "$padded_when"  "$C_RESET"
}

# ------------------------------------------------------------------
# Collect features into arrays keyed by state.
# Each entry: "<mtime>\t<name>\t<type>\t<phase>\t<when>"
# Using literal TAB (\t) as separator (feature names cannot contain TAB).
# ------------------------------------------------------------------
ACTIVE=""
[ -x "$SCRIPT_DIR/get-active.sh" ] && ACTIVE=$("$SCRIPT_DIR/get-active.sh")

ACTIVE_ROWS=""
PAUSED_ROWS=""
ARCHIVED_ROWS=""

ACTIVE_COUNT=0
PAUSED_COUNT=0
ARCHIVED_COUNT=0

TAB=$(printf '\t')

collect_row() {
  # $1 dir  $2 name  returns echo of formatted row or empty
  local dir="$1" name="$2"
  local f="$dir/feature.md"
  [ -f "$f" ] || return 1
  local type phase when mtime
  type=$(get_meta "$f" 'Type')
  [ -z "$type" ] && type="feature"
  phase=$(get_phase "$f")
  when=$(relative_time "$f")
  mtime=$(mtime_of "$f")
  printf '%s\t%s\t%s\t%s\t%s\n' "$mtime" "$name" "$type" "$phase" "$when"
}

# --- Active ---
if [ -n "$ACTIVE" ] && [ -f "$FEATURES_DIR/$ACTIVE/feature.md" ]; then
  row=$(collect_row "$FEATURES_DIR/$ACTIVE" "$ACTIVE")
  if [ -n "$row" ]; then
    ACTIVE_ROWS="$row"
    ACTIVE_COUNT=1
  fi
fi

# --- Paused ---
if [ -d "$FEATURES_DIR" ]; then
  for dir in "$FEATURES_DIR"/*/; do
    [ -d "$dir" ] || continue
    name=$(basename "$dir")
    [ "$name" = "_archive" ] && continue
    [ "$name" = "$ACTIVE" ] && continue
    [ -f "$dir/feature.md" ] || continue
    row=$(collect_row "$dir" "$name")
    if [ -n "$row" ]; then
      if [ -z "$PAUSED_ROWS" ]; then
        PAUSED_ROWS="$row"
      else
        PAUSED_ROWS="$PAUSED_ROWS"$'\n'"$row"
      fi
      PAUSED_COUNT=$((PAUSED_COUNT + 1))
    fi
  done
fi

# --- Archived ---
if [ -d "$ARCHIVE_DIR" ]; then
  for dir in "$ARCHIVE_DIR"/*/; do
    [ -d "$dir" ] || continue
    name=$(basename "$dir")
    [ -f "$dir/feature.md" ] || continue
    row=$(collect_row "$dir" "$name")
    if [ -n "$row" ]; then
      if [ -z "$ARCHIVED_ROWS" ]; then
        ARCHIVED_ROWS="$row"
      else
        ARCHIVED_ROWS="$ARCHIVED_ROWS"$'\n'"$row"
      fi
      ARCHIVED_COUNT=$((ARCHIVED_COUNT + 1))
    fi
  done
fi

# Recency sort (mtime desc). Paused/archived groups get sorted; active is single.
sort_by_mtime_desc() {
  # numeric desc on first TAB-separated column
  sort -t"$TAB" -k1,1nr
}

if [ -n "$PAUSED_ROWS" ]; then
  PAUSED_ROWS=$(printf '%s\n' "$PAUSED_ROWS" | sort_by_mtime_desc)
fi
if [ -n "$ARCHIVED_ROWS" ]; then
  # Archived: keep last 10 after sort
  ARCHIVED_ROWS=$(printf '%s\n' "$ARCHIVED_ROWS" | sort_by_mtime_desc | head -10)
fi

TOTAL=$((ACTIVE_COUNT + PAUSED_COUNT + ARCHIVED_COUNT))

# ------------------------------------------------------------------
# Header
# ------------------------------------------------------------------
HEADER_LABEL="FEATURE SYSTEM STATUS"
RIGHT="$TOTAL features total"
# Pad header to 60 chars then append RIGHT
printf "\n%s%s%s%s  %s%s%s\n\n" \
  "$C_BOLD" "$HEADER_LABEL" "$C_RESET" \
  "$(printf '%*s' $((60 - ${#HEADER_LABEL})) '')" \
  "$C_DIM" "$RIGHT" "$C_RESET"

# ------------------------------------------------------------------
# Active section
# ------------------------------------------------------------------
printf "%s● ACTIVE%s   (%d)\n" "$C_GREEN$C_BOLD" "$C_RESET" "$ACTIVE_COUNT"
if [ -n "$ACTIVE_ROWS" ]; then
  while IFS="$TAB" read -r mtime name type phase when; do
    [ -z "$name" ] && continue
    render_row "${C_GREEN}●${C_RESET}" "$name" "$type" "Phase $phase" "$when" "$C_BOLD"
  done <<<"$ACTIVE_ROWS"
else
  printf "  %s(none — /feature-start <name> or /feature-resume <name>)%s\n" "$C_DIM" "$C_RESET"
fi

# ------------------------------------------------------------------
# Paused section
# ------------------------------------------------------------------
printf "\n%s○ PAUSED%s   (%d)\n" "$C_YELLOW$C_BOLD" "$C_RESET" "$PAUSED_COUNT"
if [ -n "$PAUSED_ROWS" ]; then
  while IFS="$TAB" read -r mtime name type phase when; do
    [ -z "$name" ] && continue
    render_row "${C_YELLOW}○${C_RESET}" "$name" "$type" "Phase $phase" "$when" ""
  done <<<"$PAUSED_ROWS"
else
  printf "  %s—%s\n" "$C_DIM" "$C_RESET"
fi

# ------------------------------------------------------------------
# Archived section
# ------------------------------------------------------------------
printf "\n%s🗄 ARCHIVED%s (%d)" "$C_GRAY$C_BOLD" "$C_RESET" "$ARCHIVED_COUNT"
if [ -n "$ARCHIVED_ROWS" ]; then
  printf "\n"
  while IFS="$TAB" read -r mtime name type phase when; do
    [ -z "$name" ] && continue
    render_row "${C_GRAY}🗄${C_RESET}" "$name" "$type" "Phase $phase" "$when" "$C_GRAY"
  done <<<"$ARCHIVED_ROWS"
else
  printf "   %s—%s\n" "$C_DIM" "$C_RESET"
fi

# ------------------------------------------------------------------
# Footer
# ------------------------------------------------------------------
HINT_NAME="${ACTIVE:-<name>}"
printf "\n%sActions:%s\n" "$C_BOLD" "$C_RESET"
printf "  %s/feature-resume <name>%s    → reactivate paused/archived\n" "$C_CYAN" "$C_RESET"
printf "  %s/feature-start <name>%s     → create new feature\n"         "$C_CYAN" "$C_RESET"
printf "  %s/feature-status%s           → show active feature detail\n" "$C_CYAN" "$C_RESET"
echo ""

exit 0
