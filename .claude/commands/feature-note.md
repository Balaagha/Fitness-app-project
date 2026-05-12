---
description: Append a one-line working-memory note to the active feature's notes.md
argument-hint: [tag] <one-line text>
---

Append a single-line entry to the active feature's `notes.md`. Zero-friction:
no follow-up questions, no rationale/impact prompts — you type, it writes.

## Argument parsing

`$ARGUMENTS` is the raw text the user typed after the command name.

1. If the first whitespace-delimited token is `impl`, `gotcha`, `criteria`,
   `refs`, or `invariant` → that is the **tag**. Strip it from the body.
2. Otherwise → default tag is `impl` and the whole `$ARGUMENTS` is the body.
3. Tags MUST be written in the file as `[tag]` (square brackets).

Never ask the user for the tag. A wrong default is better than a prompt.

## Steps

1. Resolve the active feature:
   ```
   bash .claude/scripts/get-active.sh
   ```
   If empty, tell the user:
   ```
   No active feature. /feature-start or /feature-resume first.
   ```
   and stop.

2. Target file: `.claude/features/<feature>/notes.md`.
   If it doesn't exist (legacy feature created before notes.md existed),
   create it by running:
   ```
   bash .claude/scripts/ensure-notes.sh <feature>
   ```
   The script is idempotent — safe to call unconditionally.

3. Build the entry line:
   ```
   YYYY-MM-DD HH:MM [tag] <body>
   ```
   - Timestamp format matches existing notes.
   - Body must be a single physical line — collapse newlines to spaces.
   - Body must not exceed 200 characters. If longer, truncate with `…` and
     tell the user "truncated to 200 chars".

4. Append the line to `notes.md`. Preserve trailing newline.

5. Check line count. If `notes.md` exceeds 300 lines, print:
   ```
   ✓ noted: [tag] <body>
   ⚠ notes.md now has <N> lines (cap 300). Run /feature-compact to consolidate.
   ```
   Otherwise just:
   ```
   ✓ noted: [tag] <body>
   ```

## Rules

- Do NOT ask rationale / impact / any follow-up — this command's whole
  point is friction removal
- Do NOT write to feature.md — this is notes.md only
- Do NOT add a blank line between entries — one line per entry, nothing else
- Do NOT edit or reformat existing entries
- Do NOT deduplicate — /feature-compact is where that happens
- Valid tags only: `impl` | `gotcha` | `criteria` | `refs` | `invariant`.
  If the user writes an unknown tag as the first token, treat it as body
  (don't reject).
