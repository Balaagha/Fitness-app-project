---
description: Resume a paused or archived feature
argument-hint: <feature-name>
---

Resume feature "$ARGUMENTS". Works for both paused (live but inactive) and archived features.

## Steps

1. If `$ARGUMENTS` is empty, run `bash .claude/scripts/list-features.sh` and
   ask the user which feature to resume. Then re-run this command with the
   chosen name.

2. Normalise the name (lowercase, dashes) and run:
   ```
   bash .claude/scripts/resume-feature.sh "<name>"
   ```
   The script:
   - Moves the feature from `_archive/` back to `features/` if it was archived
   - Updates the active pointer
   - Appends a "Resumed on <date>" line to the Progress Log
   - Fails with a clear message if the feature does not exist

3. If the script reports success, read the restored feature.md and tell the
   user in this exact format:
   ```
   ● <name>  resumed                        [<type>]
     ✓ Active pointer set  (next chat auto-loads this feature)
     ✓ Progress Log annotated  (Resumed on <date>)

     Phase  X / Y — <current phase status>
     Next   <first unchecked item, or Notes-for-Next-Session line, or "—">
   ```

4. If the script reports failure, relay the error verbatim and suggest:
   ```
   ✗ Resume failed: <error>

   Use /feature-list to see available features.
   ```

## Ask the user (optional)

If a bug/follow-up is the reason for resuming an archived feature, ask:
```
Why resume? (bug | follow-up | missed-scope | other) — one line will be
appended to the Decisions table.
```
If the user answers, append a row to the Decisions table with:
`resume-reason: <answer>`. If the user skips, don't add anything.

## Rules

- Do not create a new feature — resume only
- Do not modify the archived feature.md in any way during the move
- Do not checkout a different git branch automatically — leave branching to the user
