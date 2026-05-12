---
description: Finalize and archive a completed feature
---

Wind down the active feature: completion check → optional auto code-review →
pattern promotion → archive → clear pointer.

## Steps

1. Resolve feature via `bash .claude/scripts/get-active.sh`. If empty, tell
   the user "No active feature" and stop.

2. Run `bash .claude/scripts/check-complete.sh` — capture the status.

3. If not all phases are complete, ask:
   ```
   Phases X, Y, Z are incomplete. Archive anyway? (y / N)
   ```
   Exit if "N" (or any non-y answer).

4. Read the feature.md fully (all sections). Read the **Type** field from
   Metadata.

5. **Auto code-review** (only when `type ∈ {feature, bug-fix, refactor}`
   AND the feature touched code):
   - If the project ships a code-review skill (e.g. a `.claude/skills/*-code-review`
     skill, or one whose description matches "code review"), tell the user
     "Running <skill-name> before archive..." and load it via the Skill tool.
   - If no such skill exists, run whatever lint/format/test commands the
     project documents in its main `CLAUDE.md`, or skip this step.
   - Produce a violations / blocker report.
   - If any blocking issues exist, ask:
     ```
     <N> issues found. Archive anyway, or fix first? (archive / fix)
     ```
     If "fix" → stop here; do not archive.

6. **Pattern promotion check** — scan in this order:
   (a) `feature.md` Decisions + Findings
   (b) `notes.md` entries tagged `[criteria]` and `[invariant]` —
       these are the sticky signals most likely to generalise.

   For each entry, ask yourself: *"Would this be useful on a future,
   unrelated feature?"* If yes, it's a candidate for `.claude/rules/`.

   Present candidates as a numbered list with source annotation:
   ```
   Promotion candidates:
     1. [notes.md / criteria] <body> — proposed rule file: orbit-io-dispatcher.md
     2. [feature.md / decision] <decision> — proposed rule file: mvi-reducer-idempotency.md
     3. ...
   Reply with numbers to promote, or "none".
   ```

7. For each approved candidate, create or extend `.claude/rules/<name>.md` —
   write a concise general rule (not the full decision / note). Keep each
   rule under ~50 lines. Strip the timestamp and tag when promoting from
   notes.md — rules are tag-less.

8. Move the feature folder to archive (includes feature.md, notes.md, and
   notes.archive.md if present):
   ```
   mv .claude/features/<name> .claude/features/_archive/<name>
   ```

9. Clear the active pointer:
   ```
   bash .claude/scripts/set-active.sh --clear
   ```

10. Confirm in this exact format:
    ```
    🗄 <name>  archived                       [<type>]
      ✓ Moved to .claude/features/_archive/<name>/
      ✓ Active pointer cleared
      ✓ Auto-review:  <N> violations  (0 blocking / N minor)   ← when ran
      ✓ Promoted to .claude/rules/:  <count> pattern(s)
         • <rule-file-1>.md
         • <rule-file-2>.md

    Branch feature/<name> untouched — merge or delete manually.
    Resume with:  /feature-resume <name>
    ```
    If no review ran (type not in {feature,bug-fix,refactor}) omit that row.
    If no patterns promoted, show "✓ Promoted: (none)" without the bullet list.

## Rules

- Do NOT delete the branch
- Do NOT make any git commits
- If a rule file already exists, show existing content and ask whether to
  append / replace / skip
- Preserve the full feature.md in the archive — never edit it during archive
- Auto code-review runs only for code-touching types (`feature`, `bug-fix`, `refactor`)
- Confirmation uses `🗄` glyph (archived) + `✓` prefix — matches /feature-list
