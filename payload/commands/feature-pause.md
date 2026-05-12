---
description: Pause the current feature (clear active pointer, keep files)
---

Clear the active feature pointer so the next session starts clean.
The feature.md stays in place; you can resume later with
`/feature-resume <name>`.

## Steps

1. Resolve the active feature via `bash .claude/scripts/get-active.sh`.
2. If empty, tell the user "No active feature; nothing to pause." and stop.
3. Append a "Paused on <date>" line to the Progress Log of the active feature
   so the pause is visible in the record. Keep the edit minimal.
4. Run:
   ```
   bash .claude/scripts/set-active.sh --clear
   ```
5. Confirm:
   ```
   ○ <name>  paused                         [<type>]
     ✓ Progress Log annotated  (Paused on <date>)
     ✓ Active pointer cleared

   Files kept at .claude/features/<name>/
   Resume later with:  /feature-resume <name>
   ```

## Rules

- Do not archive — pause only
- Do not create a new feature
- Confirmation uses `○` glyph (paused) + `✓` prefix — matches /feature-list
