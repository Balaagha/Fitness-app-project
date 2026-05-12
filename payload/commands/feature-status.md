---
description: Show current feature progress at a glance (visual — progress bars, notes tail, decisions/findings tail, cap status)
---

Render a visual status report for the currently active feature. Progress
bars, recency-ordered notes, last decisions and findings, plus notes.md /
feature.md cap status.

## Steps

1. Run:
   ```bash
   bash "${CLAUDE_PROJECT_DIR:-$(git rev-parse --show-toplevel 2>/dev/null || pwd)}/.claude/scripts/format-status.sh"
   ```
2. Capture the output verbatim and present it to the user. The script is
   read-only and handles all edge cases internally (no active feature,
   missing notes.md, no decisions yet, etc.).
3. Do **not** re-interpret or summarise — the rendered block IS the
   deliverable. Claude should only add a one-line observation if something
   in the report changed materially since last session.

## Output format (rendered by `format-status.sh`)

```
Feature: <name>
═══════════════

Type:       <type>
Branch:     <branch or n/a>
Started:    <YYYY-MM-DD> (<relative>)
Last-touch: <relative>
Related:    <other features or omitted>

Phase Progress (N/M phases — see Metadata for "current phase"):
  1. <phase name>          ████████████  completed
  2. <phase name>          ████████░░░░  conditional-pass
  3. <phase name>          ███████░░░░░  7 done / 3 open
  ...

Last 5 Notes:
  YYYY-MM-DD HH:MM [tag]       one-line body
  ...

Last 3 Decisions:
  YYYY-MM-DD — decision summary | rationale
  ...

Last 2 Findings:
  #29 — finding title
  #30 — finding title

notes.md status: <L> lines / <KB> KB   [OK|WARN|OVER — <PCT>% of 300/25KB cap]
feature.md size: <L> lines / <KB> KB   [Phases: P | Decisions: D | Findings: F | Errors: E]

Usage:
  /feature-list         → all features
  /feature-note         → log a note
  /feature-decision     → log a decision
  /feature-end          → archive on completion
```

## Options

- **Default:** full rendered block (≈25–35 lines depending on phases).
- **`--short`** — one-line compact mode, e.g. `● agents-pipeline-improve  [refactor]  Phase 5/5  9m ago`.
  Invoke via:
  ```bash
  bash "${CLAUDE_PROJECT_DIR:-$(git rev-parse --show-toplevel 2>/dev/null || pwd)}/.claude/scripts/format-status.sh" --short
  ```

## If no active feature

The script prints:
```
No active feature. /feature-start <name> to begin.
```
and exits 0. No further action required from Claude.

## Rules

- **Read-only** — the script never writes to `feature.md` or `notes.md`.
- Unicode-aware — Azerbaijani / Turkish text in Decisions / Findings is
  truncated at character boundaries (never mid-byte).
- Phase status heuristic (in order of precedence):
  1. Title contains `CONDITIONAL PASS` → "conditional-pass" (bar: 8/12)
  2. Title contains `IN PROGRESS`       → partial bar from checkbox/task-marker ratio
  3. `**Status:** done | completed | tamamlandı` → "completed" (bar: 12/12)
  4. `**Status:** in progress`          → partial bar
  5. Title contains `AÇIQ | AÇIK | OPEN` → bar from checkbox/task-marker ratio, status `N done / M open`
  6. `DOCUMENTED PROPOSALS`             → "proposals documented" (bar: 0/12)
  7. Fallback: raw `[x]` / `[ ]` checkbox ratio inside the phase block
- Cap status colours: `OK` < 80 % · `WARN` 80–99 % · `OVER` ≥ 100 %.
- Timestamps prefer `git log -1 --format=%cr` (nicer granularity). Fall
  back to `stat` when git is unavailable or the file is untracked.

## See also

- `bash .claude/scripts/list-features.sh` → `/feature-list` renderer
- `bash .claude/scripts/format-status.sh` → the script this command runs
