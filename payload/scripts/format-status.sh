#!/bin/bash
# ------------------------------------------------------------------
# format-status.sh — visual /feature-status renderer
#
# Output design (Task 8.D, 2026-04-22):
#   - Header block + metadata (Type, Branch, Started, Last-touch)
#   - Phase progress: 12-char Unicode bars (█ / ░) with status text
#   - Last 5 notes from notes.md (tail, non-empty, non-header)
#   - Last 3 rows from feature.md Decisions table
#   - Last 2 Findings (### ... headers or numbered items)
#   - notes.md cap status (lines / KB vs 300 / 25KB)
#   - feature.md size summary (counts of Phases, Decisions, Findings, Errors)
#
# Read-only. Never writes to feature.md or notes.md.
# Unicode-aware (LC_ALL=en_US.UTF-8).
# Optional: --short for one-line compact mode (for future).
#
# Always exits 0.
# ------------------------------------------------------------------

set -o pipefail
export LC_ALL="${LC_ALL:-en_US.UTF-8}"
export LANG="${LANG:-en_US.UTF-8}"

PROJECT_DIR="${CLAUDE_PROJECT_DIR:-.}"
SCRIPT_DIR="$PROJECT_DIR/.claude/scripts"
FEATURES_DIR="$PROJECT_DIR/.claude/features"

# ---------- arg parsing ----------
SHORT=0
for arg in "$@"; do
  case "$arg" in
    --short|-s) SHORT=1 ;;
    --help|-h)
      cat <<EOF
Usage: format-status.sh [--short]

Renders a visual status report for the currently active feature.

Options:
  --short, -s   One-line compact mode (name / type / phase / last-touch)
  --help,  -h   Show this help

Reads: .claude/features/.active pointer, feature.md, notes.md
Writes: nothing
EOF
      exit 0
      ;;
  esac
done

# ---------- color helpers ----------
if [ -t 1 ] && [ "${TERM:-dumb}" != "dumb" ] && [ -z "${NO_COLOR:-}" ]; then
  C_BOLD=$'\033[1m'
  C_DIM=$'\033[2m'
  C_GREEN=$'\033[32m'
  C_YELLOW=$'\033[33m'
  C_RED=$'\033[31m'
  C_GRAY=$'\033[90m'
  C_CYAN=$'\033[36m'
  C_MAGENTA=$'\033[35m'
  C_RESET=$'\033[0m'
else
  C_BOLD=""; C_DIM=""; C_GREEN=""; C_YELLOW=""; C_RED=""
  C_GRAY=""; C_CYAN=""; C_MAGENTA=""; C_RESET=""
fi

# ---------- resolve active feature ----------
ACTIVE=""
if [ -x "$SCRIPT_DIR/resolve-feature.sh" ]; then
  ACTIVE=$("$SCRIPT_DIR/resolve-feature.sh")
fi

if [ -z "$ACTIVE" ] || [ ! -f "$FEATURES_DIR/$ACTIVE/feature.md" ]; then
  echo "No active feature. /feature-start <name> to begin."
  exit 0
fi

FEATURE_DIR="$FEATURES_DIR/$ACTIVE"
FEATURE_MD="$FEATURE_DIR/feature.md"
NOTES_MD="$FEATURE_DIR/notes.md"

# ---------- helpers ----------
get_meta() {
  local key="$1"
  grep -E "^- \*\*$key\*\*:" "$FEATURE_MD" 2>/dev/null \
    | head -1 \
    | sed -E "s/^- \*\*$key\*\*:[[:space:]]*//; s/^\`//; s/\`$//"
}

# Relative time for file mtime. Output: "today", "Nm ago", "Nh ago", "Nd ago", etc.
relative_time() {
  local file="$1"
  [ -e "$file" ] || { echo "—"; return; }
  # Prefer git mtime if repo knows the file (nice for "Nm ago" granularity)
  if command -v git >/dev/null 2>&1; then
    local gtime
    gtime=$(git -C "$PROJECT_DIR" log -1 --format=%cr -- "$file" 2>/dev/null)
    if [ -n "$gtime" ]; then
      echo "$gtime"
      return
    fi
  fi
  local mtime now diff
  mtime=$(stat -f %m "$file" 2>/dev/null)
  [ -z "$mtime" ] && mtime=$(stat -c %Y "$file" 2>/dev/null)
  [ -z "$mtime" ] && { echo "—"; return; }
  now=$(date +%s)
  diff=$(( now - mtime ))
  [ $diff -lt 0 ] && diff=0
  if   [ $diff -lt 60 ];       then echo "just now"
  elif [ $diff -lt 3600 ];     then echo "$((diff/60))m ago"
  elif [ $diff -lt 86400 ];    then echo "$((diff/3600))h ago"
  elif [ $diff -lt 604800 ];   then echo "$((diff/86400))d ago"
  elif [ $diff -lt 2592000 ];  then echo "$((diff/604800))w ago"
  elif [ $diff -lt 31536000 ]; then echo "$((diff/2592000))mo ago"
  else                              echo "$((diff/31536000))y ago"
  fi
}

# Started (date-only, relative hint)
started_relative() {
  local started="$1"
  [ -z "$started" ] && { echo "—"; return; }
  local today diff_days
  today=$(date +%Y-%m-%d)
  if [ "$started" = "$today" ]; then
    echo "$started (today)"
    return
  fi
  # Cross-platform date diff (best effort)
  local started_epoch today_epoch
  started_epoch=$(date -j -f "%Y-%m-%d" "$started" "+%s" 2>/dev/null)
  [ -z "$started_epoch" ] && started_epoch=$(date -d "$started" "+%s" 2>/dev/null)
  today_epoch=$(date +%s)
  if [ -n "$started_epoch" ] && [ -n "$today_epoch" ]; then
    diff_days=$(( (today_epoch - started_epoch) / 86400 ))
    if   [ $diff_days -le 0 ]; then echo "$started (today)"
    elif [ $diff_days -eq 1 ]; then echo "$started (1 day ago)"
    elif [ $diff_days -lt 30 ];then echo "$started ($diff_days days ago)"
    elif [ $diff_days -lt 365 ];then echo "$started ($((diff_days/30))mo ago)"
    else                             echo "$started ($((diff_days/365))y ago)"
    fi
  else
    echo "$started"
  fi
}

# Pad or truncate to WIDTH printable chars; ellipsis on overflow.
pad_or_trunc() {
  local s="$1" width="$2"
  local len=${#s}
  if [ $len -gt $width ]; then
    local keep=$(( width - 1 ))
    [ $keep -lt 1 ] && keep=1
    s="${s:0:$keep}…"
  fi
  printf "%-${width}s" "$s"
}

# Bar renderer: $1 filled (0..12) of 12 total
render_bar() {
  local filled="$1"
  [ -z "$filled" ] && filled=0
  [ "$filled" -lt 0 ] && filled=0
  [ "$filled" -gt 12 ] && filled=12
  local empty=$(( 12 - filled ))
  local bar="" i
  for (( i=0; i<filled; i++ )); do bar="${bar}█"; done
  for (( i=0; i<empty;  i++ )); do bar="${bar}░"; done
  printf "%s" "$bar"
}

# Color a bar based on filled amount (for TTY output)
bar_color_for() {
  local filled="$1" text="$2"
  if   [ "$filled" -ge 12 ]; then printf "%s%s%s" "$C_GREEN"  "$text" "$C_RESET"
  elif [ "$filled" -ge 7 ];  then printf "%s%s%s" "$C_CYAN"   "$text" "$C_RESET"
  elif [ "$filled" -ge 1 ];  then printf "%s%s%s" "$C_YELLOW" "$text" "$C_RESET"
  else                             printf "%s%s%s" "$C_GRAY"   "$text" "$C_RESET"
  fi
}

# ---------- Metadata ----------
TYPE=$(get_meta 'Type')
[ -z "$TYPE" ] && TYPE="feature"
BRANCH=$(get_meta 'Branch')
[ -z "$BRANCH" ] && BRANCH="n/a"
STARTED=$(get_meta 'Started')
CURRENT_PHASE_RAW=$(get_meta 'Current phase')
RELATED=$(get_meta 'Related features')
LAST_TOUCH=$(relative_time "$FEATURE_MD")

# Extract "N / M" prefix from Current phase
PHASE_N=""
PHASE_M=""
if [ -n "$CURRENT_PHASE_RAW" ]; then
  PHASE_N=$(printf '%s' "$CURRENT_PHASE_RAW" | sed -nE 's/^[[:space:]]*([0-9]+)[[:space:]]*\/[[:space:]]*([0-9]+).*/\1/p')
  PHASE_M=$(printf '%s' "$CURRENT_PHASE_RAW" | sed -nE 's/^[[:space:]]*([0-9]+)[[:space:]]*\/[[:space:]]*([0-9]+).*/\2/p')
fi

# Extract all actual ### Phase N: <name> headers
# We need: phase_number | phase_name | status_hint
PHASES_RAW=$(awk '
  /^### Phase [0-9]+:/ {
    header=$0
    sub(/^### Phase /, "", header)
    # capture: "<N>: <rest>"
    n = header
    sub(/:.*/, "", n)
    name = header
    sub(/^[0-9]+:[[:space:]]*/, "", name)
    # read following lines until next ### or ##
    current_n = n
    current_name = name
    status = ""
    # Instead of lookahead, emit a record now and let a second pass fill status
    printf "%s\t%s\n", current_n, current_name
  }
' "$FEATURE_MD")

# Second pass: per phase, scan between this "### Phase X:" and the next "###" or "##" and grab a status/bullets summary
# We also collect open vs done sub-task counts where possible.
#
# Heuristics used for filled bar:
#   - explicit "**Status:** done" / "DONE" / "tamamlandı" / "HƏLL OLUNDU" → 12
#   - "CONDITIONAL PASS" → 8
#   - "IN PROGRESS" / "in progress" / "AÇIQ, HIGH priority" with [x]/[ ] mix → partial
#   - title contains "AÇIQ" (open) with no [x] → 0
#   - fallback: count [x] vs [ ] checkboxes → filled = round(12 * done/total)
#   - absolute fallback: 0
#
# This awk block produces tab-separated: num \t name \t filled \t status_text

PHASES_TABLE=$(awk -v OFS='\t' '
  function trim(s){ sub(/^[[:space:]]+/,"",s); sub(/[[:space:]]+$/,"",s); return s }
  function flush(   filled, status_text, done_ct, total_ct){
    if (phase_num == "") return
    done_ct = done_cnt
    total_ct = done_cnt + open_cnt

    # Fallback: task-marker counting when no checkboxes were found
    task_total = task_done + task_open
    if (total_ct == 0 && task_total > 0) {
      done_ct = task_done
      total_ct = task_total
    }

    filled = 0
    status_text = ""

    # Title-level CONDITIONAL PASS takes precedence over a plain explicit "done"
    if (status_conditional == 1) { filled = 8;  status_text = "conditional-pass" }
    else if (status_done == 1) { filled = 12; status_text = "completed" }
    else if (status_in_progress == 1) {
      if (total_ct > 0) {
        filled = int(12 * done_ct / total_ct)
        if (filled < 1 && done_ct > 0) filled = 1
        if (filled > 11) filled = 11
        status_text = done_ct " done / " (total_ct - done_ct) " open"
      } else {
        filled = 4
        status_text = "in progress"
      }
    }
    else if (status_proposed == 1) { filled = 0; status_text = "proposals documented" }
    else if (status_open == 1) {
      if (total_ct > 0) {
        if (done_ct == 0) { filled = 0 }
        else { filled = int(12 * done_ct / total_ct); if (filled < 1) filled = 1 }
        status_text = done_ct " done / " (total_ct - done_ct) " open"
      } else {
        filled = 0
        status_text = "open"
      }
    }
    else {
      # Fallback: checkbox / task-marker counting
      if (total_ct > 0) {
        filled = int(12 * done_ct / total_ct)
        if (filled < 1 && done_ct > 0) filled = 1
        if (filled > 12) filled = 12
        status_text = done_ct " done / " (total_ct - done_ct) " open"
      } else {
        filled = 0
        status_text = "—"
      }
    }

    # If explicit "Status: ..." was captured AND the title-level status is not more specific,
    # prefer the explicit phrasing. But keep conditional-pass/in-progress title wins.
    if (explicit_status != "" && status_conditional == 0 && status_in_progress == 0) {
      es = explicit_status
      sub(/[[:space:]]*—.*$/, "", es)
      sub(/[[:space:]]*\(.*$/, "", es)
      status_text = es
      if (status_text == "") status_text = explicit_status
    }

    # Truncate status_text to 40 chars
    if (length(status_text) > 40) status_text = substr(status_text, 1, 39) "…"

    print phase_num, phase_name, filled, status_text
  }

  # Phase header boundary
  /^### Phase [0-9]+:/ {
    flush()
    phase_num = $0
    sub(/^### Phase /, "", phase_num)
    phase_name = phase_num
    sub(/:.*$/, "", phase_num)
    sub(/^[0-9]+:[[:space:]]*/, "", phase_name)

    # detect title-level hints (use upper for ASCII and raw for unicode)
    title_upper = toupper(phase_name)
    status_done = 0
    status_conditional = 0
    status_in_progress = 0
    status_proposed = 0
    status_open = 0
    explicit_status = ""

    if (title_upper ~ /CONDITIONAL PASS/) status_conditional = 1
    if (title_upper ~ /IN PROGRESS/) status_in_progress = 1
    # Open markers (Turkish/Azerbaijani "AÇIQ" / "AÇIK" / English "OPEN")
    if (phase_name ~ /AÇIQ|AÇIK|OPEN/) status_open = 1
    if (title_upper ~ /DOCUMENTED PROPOSALS/) status_proposed = 1

    # Strip trailing "(...)" / "— ..." qualifiers from display name
    sub(/[[:space:]]*—.*$/, "", phase_name)
    sub(/[[:space:]]*\(.*$/, "", phase_name)

    done_cnt = 0
    open_cnt = 0
    task_done = 0
    task_open = 0
    next
  }

  # Hard stop boundary: a new top-level section
  /^## [^#]/ {
    flush()
    phase_num = ""
    next
  }

  # Within a phase block
  phase_num != "" {
    # Checkbox rows
    if ($0 ~ /^[[:space:]]*-[[:space:]]*\[x\]/) done_cnt++
    else if ($0 ~ /^[[:space:]]*-[[:space:]]*\[[[:space:]]?\]/) open_cnt++

    # Phase 5/8-style task markers: "#### Task X.Y — ..." with (HƏLL OLUNDU)/(AÇIQ)/(DONE)/(IN PROGRESS)
    if ($0 ~ /^####[[:space:]]/) {
      task_u = toupper($0)
      if ($0 ~ /HƏLL OLUNDU|DONE|RESOLVED|COMPLETE/ || task_u ~ /DONE|RESOLVED|COMPLETE/) task_done++
      else if ($0 ~ /AÇIQ|AÇIK|OPEN|IN PROGRESS/ || task_u ~ /OPEN|IN PROGRESS/) task_open++
    }

    # Explicit **Status:** line
    if ($0 ~ /^[[:space:]]*-[[:space:]]*\*\*Status:\*\*/) {
      s = $0
      sub(/.*\*\*Status:\*\*[[:space:]]*/, "", s)
      s = trim(s)
      explicit_status = s
      if (tolower(s) ~ /done|completed|tamamlandı/) status_done = 1
      if (tolower(s) ~ /conditional/) status_conditional = 1
      if (tolower(s) ~ /in progress/) status_in_progress = 1
      if (tolower(s) ~ /open/) status_open = 1
    }
  }

  END { flush() }
' "$FEATURE_MD")

# Phase count (for the header line)
PHASE_TOTAL=$(printf '%s\n' "$PHASES_TABLE" | awk -F'\t' 'NF>=4 {c++} END{print c+0}')

# If Current phase metadata gave us N/M and it's sensible, prefer its M for header display
[ -z "$PHASE_M" ] && PHASE_M="$PHASE_TOTAL"
[ -z "$PHASE_N" ] && PHASE_N="?"

# ---------- Short mode ----------
if [ "$SHORT" = "1" ]; then
  printf "%s● %s%s  [%s]  Phase %s/%s  %s\n" \
    "$C_GREEN$C_BOLD" "$ACTIVE" "$C_RESET" \
    "$TYPE" "$PHASE_N" "$PHASE_M" "$LAST_TOUCH"
  exit 0
fi

# ---------- Header ----------
printf "\n%sFeature: %s%s%s\n" "$C_BOLD" "$C_GREEN" "$ACTIVE" "$C_RESET"
# A simple underline the same width as "Feature: <name>"
TITLE_LEN=$(( 9 + ${#ACTIVE} ))
[ $TITLE_LEN -gt 60 ] && TITLE_LEN=60
RULE=""
for (( i=0; i<TITLE_LEN; i++ )); do RULE="${RULE}═"; done
printf "%s%s%s\n\n" "$C_DIM" "$RULE" "$C_RESET"

# ---------- Metadata block ----------
printf "%sType:%s       %s\n"         "$C_BOLD" "$C_RESET" "$TYPE"
printf "%sBranch:%s     %s\n"         "$C_BOLD" "$C_RESET" "$BRANCH"
printf "%sStarted:%s    %s\n"         "$C_BOLD" "$C_RESET" "$(started_relative "$STARTED")"
printf "%sLast-touch:%s %s\n"         "$C_BOLD" "$C_RESET" "$LAST_TOUCH"
if [ -n "$RELATED" ] && [ "$RELATED" != "none" ]; then
  printf "%sRelated:%s    %s\n"       "$C_BOLD" "$C_RESET" "$RELATED"
fi
printf "\n"

# ---------- Phase Progress ----------
if [ -z "$PHASES_TABLE" ]; then
  printf "%sPhase Progress: (no phases found in feature.md)%s\n\n" "$C_DIM" "$C_RESET"
else
  printf "%sPhase Progress%s (%s/%s phases — see Metadata for \"current phase\"):\n" \
    "$C_BOLD" "$C_RESET" "$PHASE_N" "$PHASE_M"
  printf '%s\n' "$PHASES_TABLE" | awk -F'\t' -v OFS='' '
    NF>=4 {
      num = $1
      name = $2
      filled = $3
      status = $4

      # pad/truncate name to 20 chars
      if (length(name) > 20) name = substr(name, 1, 19) "…"
      printf "  %2d. %-20s  ", num, name

      # build bar
      bar = ""
      for (i=0; i<filled; i++) bar = bar "█"
      for (i=0; i<12-filled; i++) bar = bar "░"

      printf "%s  %s\n", bar, status
    }
  '
  printf "\n"
fi

# ---------- Last 5 Notes ----------
NOTES_HEADER="%sLast 5 Notes:%s\n"
printf "$NOTES_HEADER" "$C_BOLD" "$C_RESET"

if [ ! -f "$NOTES_MD" ]; then
  printf "  %s(no notes.md yet)%s\n\n" "$C_DIM" "$C_RESET"
else
  # Extract dated entry lines: "YYYY-MM-DD HH:MM [tag] ..."
  NOTES_TAIL=$(grep -E '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2} \[' "$NOTES_MD" 2>/dev/null | tail -5)
  if [ -z "$NOTES_TAIL" ]; then
    printf "  %s(no dated entries yet)%s\n\n" "$C_DIM" "$C_RESET"
  else
    printf '%s\n' "$NOTES_TAIL" | \
      C_DIM="$C_DIM" C_RESET="$C_RESET" \
      C_YELLOW="$C_YELLOW" C_CYAN="$C_CYAN" \
      C_MAGENTA="$C_MAGENTA" C_GRAY="$C_GRAY" \
      perl -CSDA -Mutf8 -ne '
        use strict; use warnings;
        my $line = $_; chomp $line;
        # Portable parse: "YYYY-MM-DD HH:MM [tag] body"
        my $dt = substr($line, 0, 16);
        my $rest = substr($line, 16);
        $rest =~ s/^\s+//;
        my ($tag, $body) = ("", $rest);
        if ($rest =~ /^\[([^\]]+)\]\s*(.*)$/) {
          $tag = $1; $body = $2;
        }
        my %color = (
          invariant => $ENV{C_MAGENTA} // "",
          criteria  => $ENV{C_YELLOW}  // "",
          gotcha    => $ENV{C_CYAN}    // "",
          impl      => $ENV{C_GRAY}    // "",
          refs      => $ENV{C_GRAY}    // "",
        );
        my $c = $color{$tag} // ($ENV{C_GRAY} // "");
        my $dim = $ENV{C_DIM} // "";
        my $rst = $ENV{C_RESET} // "";

        my $ltag = "[" . $tag . "]";
        $ltag .= " " while length($ltag) < 12;

        $body = substr($body, 0, 69) . "\x{2026}" if length($body) > 70;

        printf "  %s%s%s %s%s%s %s\n", $dim, $dt, $rst, $c, $ltag, $rst, $body;
      '
    printf "\n"
  fi
fi

# ---------- Last 3 Decisions ----------
printf "%sLast 3 Decisions:%s\n" "$C_BOLD" "$C_RESET"

DEC_ROWS=$(awk '
  /^## Decisions/ { in_dec = 1; next }
  in_dec && /^## [^#]/ { exit }
  in_dec && /^\|/ {
    # skip header row and separator
    if ($0 ~ /^\|[[:space:]]*Date[[:space:]]*\|/) next
    if ($0 ~ /^\|[-]/) next
    if ($0 ~ /^\|[[:space:]]*[-]/) next
    print
  }
' "$FEATURE_MD" | tail -3)

if [ -z "$DEC_ROWS" ]; then
  printf "  %s(none)%s\n\n" "$C_DIM" "$C_RESET"
else
  # UTF-8-aware truncation via perl. Emits one formatted line per input row.
  printf '%s\n' "$DEC_ROWS" | \
    C_DIM="$C_DIM" C_RESET="$C_RESET" C_CYAN="$C_CYAN" \
    perl -CSDA -Mutf8 -ne '
      use strict; use warnings;
      my $line = $_; chomp $line;
      # Split on "|"; columns: [blank] date | decision | rationale | impact | [blank]
      my @c = split /\|/, $line, -1;
      next unless @c >= 5;
      my $date = $c[1] // ""; $date =~ s/^\s+|\s+$//g;
      my $decision = $c[2] // ""; $decision =~ s/^\s+|\s+$//g;
      my $rationale = $c[3] // ""; $rationale =~ s/^\s+|\s+$//g;
      sub trunc { my ($s, $n) = @_; return length($s) > $n ? substr($s, 0, $n-1) . "\x{2026}" : $s; }
      my $d = trunc($decision, 70);
      my $r = trunc($rationale, 40);
      my $dim = $ENV{C_DIM} // ""; my $rst = $ENV{C_RESET} // "";
      my $cyan = $ENV{C_CYAN} // "";
      printf "  %s%s%s \x{2014} %s %s|%s %s%s%s\n",
        $dim, $date, $rst, $d, $dim, $rst, $cyan, $r, $rst;
    '
  printf "\n"
fi

# ---------- Last 2 Findings ----------
printf "%sLast 2 Findings:%s\n" "$C_BOLD" "$C_RESET"

# Findings section commonly uses numbered paragraph starts OR "### Title"
# Prefer numbered items (actual findings). Fall back to "### Title" subsection headers.
FND_NUMBERED=$(awk '
  /^## (Findings|Findings\/|Findings \/)/ { in_fnd = 1; next }
  in_fnd && /^## [^#]/ { exit }
  in_fnd && /^[0-9]+\.[[:space:]]/ { print }
' "$FEATURE_MD" | tail -2)

FND_HEADERS=$(awk '
  /^## (Findings|Findings\/|Findings \/)/ { in_fnd = 1; next }
  in_fnd && /^## [^#]/ { exit }
  in_fnd && /^### / { print }
' "$FEATURE_MD" | tail -2)

if [ -n "$FND_NUMBERED" ]; then
  FND_LAST="$FND_NUMBERED"
else
  FND_LAST="$FND_HEADERS"
fi

if [ -z "$FND_LAST" ]; then
  printf "  %s(none)%s\n\n" "$C_DIM" "$C_RESET"
else
  printf '%s\n' "$FND_LAST" | perl -CSDA -Mutf8 -ne '
    use strict; use warnings;
    my $s = $_; chomp $s;
    $s =~ s/^###\s+//;
    # Strip basic markdown bold markers for display cleanliness
    $s =~ s/\*\*//g;
    # Strip surrounding backticks for inline code
    # (keep text itself)
    if ($s =~ /^([0-9]+)\.\s*(.*)$/) {
      my ($num, $rest) = ($1, $2);
      $rest = substr($rest, 0, 79) . "\x{2026}" if length($rest) > 80;
      printf "  #%s \x{2014} %s\n", $num, $rest;
    } else {
      $s = substr($s, 0, 83) . "\x{2026}" if length($s) > 84;
      printf "  \x{2022} %s\n", $s;
    }
  '
  printf "\n"
fi

# ---------- notes.md cap status ----------
NOTE_LINES=0
NOTE_BYTES=0
if [ -f "$NOTES_MD" ]; then
  NOTE_LINES=$(wc -l < "$NOTES_MD" | tr -d ' ')
  NOTE_BYTES=$(wc -c < "$NOTES_MD" | tr -d ' ')
fi
NOTE_KB_DEC=$(awk -v b="$NOTE_BYTES" 'BEGIN{ printf "%.1f", b/1024 }')
# percent of cap: 300 lines, 25 KB → whichever is higher
PCT_LINES=$(( NOTE_LINES * 100 / 300 ))
# KB pct as integer
PCT_KB=$(awk -v b="$NOTE_BYTES" 'BEGIN{ printf "%d", (b*100)/(25*1024) }')
PCT=$PCT_LINES
[ "$PCT_KB" -gt "$PCT" ] && PCT=$PCT_KB

CAP_LABEL="OK"
CAP_COLOR="$C_GREEN"
if [ "$PCT" -ge 100 ]; then
  CAP_LABEL="OVER"; CAP_COLOR="$C_RED"
elif [ "$PCT" -ge 80 ]; then
  CAP_LABEL="WARN"; CAP_COLOR="$C_YELLOW"
fi

printf "%snotes.md status:%s  %d lines / %s KB   %s[%s — %d%% of 300/25KB cap]%s\n" \
  "$C_BOLD" "$C_RESET" "$NOTE_LINES" "$NOTE_KB_DEC" \
  "$CAP_COLOR" "$CAP_LABEL" "$PCT" "$C_RESET"

# ---------- feature.md size summary ----------
FM_LINES=$(wc -l < "$FEATURE_MD" | tr -d ' ')
FM_BYTES=$(wc -c < "$FEATURE_MD" | tr -d ' ')
FM_KB=$(awk -v b="$FM_BYTES" 'BEGIN{ printf "%.0f", b/1024 }')

# Count Phases (### Phase), Decisions (rows in Decisions table), Findings (### in Findings), Errors (rows in Errors table)
PHASE_CT=$(grep -c -E '^### Phase [0-9]+:' "$FEATURE_MD" 2>/dev/null || echo 0)
DEC_CT=$(awk '
  /^## Decisions/ { in_dec = 1; next }
  in_dec && /^## [^#]/ { exit }
  in_dec && /^\|/ {
    if ($0 ~ /^\|[[:space:]]*Date[[:space:]]*\|/) next
    if ($0 ~ /^\|[-]/) next
    if ($0 ~ /^\|[[:space:]]*[-]/) next
    c++
  }
  END { print c+0 }
' "$FEATURE_MD")
FND_CT=$(awk '
  /^## (Findings|Findings\/|Findings \/)/ { in_fnd = 1; next }
  in_fnd && /^## [^#]/ { exit }
  in_fnd && /^### / { c++ }
  in_fnd && /^[0-9]+\.[[:space:]]/ { c++ }
  END { print c+0 }
' "$FEATURE_MD")
ERR_CT=$(awk '
  /^## Errors/ { in_err = 1; next }
  in_err && /^## [^#]/ { exit }
  in_err && /^\|/ {
    if ($0 ~ /^\|[[:space:]]*Error[[:space:]]*\|/) next
    if ($0 ~ /^\|[-]/) next
    if ($0 ~ /^\|[[:space:]]*[-]/) next
    c++
  }
  END { print c+0 }
' "$FEATURE_MD")

printf "%sfeature.md size:%s  %d lines / %d KB   %s[Phases: %d | Decisions: %d | Findings: %d | Errors: %d]%s\n" \
  "$C_BOLD" "$C_RESET" "$FM_LINES" "$FM_KB" "$C_DIM" "$PHASE_CT" "$DEC_CT" "$FND_CT" "$ERR_CT" "$C_RESET"
printf "\n"

# ---------- Usage footer ----------
printf "%sUsage:%s\n" "$C_BOLD" "$C_RESET"
printf "  %s/feature-list%s         → all features\n"          "$C_CYAN" "$C_RESET"
printf "  %s/feature-note%s         → log a note\n"            "$C_CYAN" "$C_RESET"
printf "  %s/feature-decision%s     → log a decision\n"        "$C_CYAN" "$C_RESET"
printf "  %s/feature-end%s          → archive on completion\n" "$C_CYAN" "$C_RESET"
echo ""

exit 0
