---
description: Manually log a decision to the current feature mid-chat
argument-hint: <decision text>
---

Append a new decision to the active feature's Decisions table.

## Steps

1. Resolve the active feature via `bash .claude/scripts/get-active.sh`.
   If empty, tell the user:
   ```
   No active feature. Use /feature-start or /feature-resume first.
   ```
   and stop.

2. Locate `.claude/features/<feature>/feature.md`.

3. Ask the user two short follow-up questions in a single message:
   - "Rationale? (one line)"
   - "Impact? (low / med / high)"

4. After the user replies, edit feature.md to append a row to the Decisions table:

   ```
   | YYYY-MM-DD HH:MM | $ARGUMENTS | <rationale> | <impact> |
   ```

5. If an identical decision text already exists in the table, do not append —
   tell the user "already logged" and show the existing row instead.

## Rules

- Do not add to Findings or Errors in this command
- Keep edits minimal — don't reformat unrelated parts of feature.md
