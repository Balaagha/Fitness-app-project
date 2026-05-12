#!/usr/bin/env bash
# Feature-Memory System Installer
# Installs into <target>/.claude/  (default: current directory)
# Usage:
#   ./install.sh                 # install into $PWD
#   ./install.sh /path/to/proj   # install into given project root
#   ./install.sh --uninstall     # remove installed files (preserves features/)

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PAYLOAD="$SCRIPT_DIR/payload"

UNINSTALL=0
TARGET="${PWD}"

for arg in "$@"; do
  case "$arg" in
    --uninstall) UNINSTALL=1 ;;
    -h|--help)
      sed -n '2,8p' "$0"; exit 0 ;;
    *) TARGET="$arg" ;;
  esac
done

if [[ ! -d "$TARGET" ]]; then
  echo "ERROR: target directory does not exist: $TARGET" >&2
  exit 1
fi

CLAUDE_DIR="$TARGET/.claude"

# ---------- helpers ----------
have_jq() { command -v jq >/dev/null 2>&1; }

backup() {
  local f="$1"
  [[ -f "$f" ]] || return 0
  cp "$f" "$f.bak.$(date +%Y%m%d-%H%M%S)"
}

# ---------- uninstall ----------
if [[ $UNINSTALL -eq 1 ]]; then
  echo "Uninstalling feature-memory from: $CLAUDE_DIR"
  for f in commands/feature-*.md \
           hooks/session-start.sh hooks/stop.sh hooks/pre-compact.sh \
           scripts/check-complete.sh scripts/ensure-notes.sh \
           scripts/format-status.sh scripts/get-active.sh \
           scripts/init-feature.sh scripts/json-extract.sh \
           scripts/list-features.sh scripts/resolve-feature.sh \
           scripts/resume-feature.sh scripts/set-active.sh \
           templates/feature-*.md templates/notes.md; do
    rm -f "$CLAUDE_DIR"/$f 2>/dev/null || true
  done
  echo "Done. Note: .claude/features/ kept intact."
  exit 0
fi

# ---------- install ----------
echo "Installing feature-memory system into: $CLAUDE_DIR"
mkdir -p "$CLAUDE_DIR"/{commands,hooks,scripts,templates,features,backups/transcripts,backups/stops}

# Copy payload
cp -f "$PAYLOAD"/commands/*.md       "$CLAUDE_DIR/commands/"
cp -f "$PAYLOAD"/hooks/*.sh          "$CLAUDE_DIR/hooks/"
cp -f "$PAYLOAD"/scripts/*.sh        "$CLAUDE_DIR/scripts/"
cp -f "$PAYLOAD"/templates/*.md      "$CLAUDE_DIR/templates/"

# CLAUDE.md (workflow docs) — only write if missing, otherwise leave note
if [[ -f "$CLAUDE_DIR/CLAUDE.md" ]]; then
  echo "  · .claude/CLAUDE.md already exists — leaving untouched"
  echo "    Reference docs available at:  $PAYLOAD/CLAUDE.md"
else
  cp -f "$PAYLOAD/CLAUDE.md" "$CLAUDE_DIR/CLAUDE.md"
  echo "  · wrote .claude/CLAUDE.md (feature-memory workflow docs)"
fi

# Make hooks/scripts executable
chmod +x "$CLAUDE_DIR"/hooks/*.sh "$CLAUDE_DIR"/scripts/*.sh

# ---------- merge settings.json hooks ----------
SETTINGS="$CLAUDE_DIR/settings.json"

read -r -d '' HOOKS_JSON <<'EOF' || true
{
  "hooks": {
    "SessionStart": [
      {
        "matcher": "startup|resume|clear|compact",
        "hooks": [
          { "type": "command", "command": "$CLAUDE_PROJECT_DIR/.claude/hooks/session-start.sh", "timeout": 10000 }
        ]
      }
    ],
    "Stop": [
      {
        "hooks": [
          { "type": "command", "command": "$CLAUDE_PROJECT_DIR/.claude/hooks/stop.sh", "timeout": 5000 }
        ]
      }
    ],
    "PreCompact": [
      {
        "hooks": [
          { "type": "command", "command": "$CLAUDE_PROJECT_DIR/.claude/hooks/pre-compact.sh", "timeout": 5000 }
        ]
      }
    ]
  }
}
EOF

if [[ -f "$SETTINGS" ]]; then
  backup "$SETTINGS"
  if have_jq; then
    tmp="$(mktemp)"
    # Deep merge: incoming hooks override matching keys but other settings preserved
    jq --argjson add "$HOOKS_JSON" '
      .hooks = ((.hooks // {}) as $cur
        | $cur
        | .SessionStart = ((.SessionStart // []) + $add.hooks.SessionStart)
        | .Stop         = ((.Stop // [])         + $add.hooks.Stop)
        | .PreCompact   = ((.PreCompact // [])   + $add.hooks.PreCompact)
      )' "$SETTINGS" > "$tmp" && mv "$tmp" "$SETTINGS"
    echo "  · merged hooks into existing settings.json (backup saved)"
  else
    echo "  · WARN: jq not found — settings.json NOT merged."
    echo "    Manually add the following 'hooks' block to $SETTINGS:"
    echo "$HOOKS_JSON"
  fi
else
  echo "$HOOKS_JSON" | (have_jq && jq . || cat) > "$SETTINGS"
  echo "  · created settings.json with hook config"
fi

# ---------- gitignore ----------
GITIGNORE="$TARGET/.gitignore"
if [[ -f "$GITIGNORE" ]] && ! grep -qE '^\.claude/features/\.active$' "$GITIGNORE"; then
  {
    echo ""
    echo "# feature-memory"
    echo ".claude/features/.active"
    echo ".claude/backups/"
  } >> "$GITIGNORE"
  echo "  · appended feature-memory entries to .gitignore"
fi

cat <<'EOF'

✓ Installation complete.

Next steps:
  1. Open the project in Claude Code.
  2. Run:  /feature-start <name>
  3. New chats will auto-load the active feature via the SessionStart hook.

Available slash commands:
  /feature-start, /feature-status, /feature-note, /feature-decision,
  /feature-finding, /feature-compact, /feature-pause, /feature-resume,
  /feature-list, /feature-end

Workflow docs: see .claude/CLAUDE.md
EOF
