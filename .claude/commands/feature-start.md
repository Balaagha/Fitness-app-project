---
description: Initialize feature memory file + branch
argument-hint: <feature-name>
---

Initialize a feature memory file for "$ARGUMENTS".

## Interactive flow

Ask the user these questions IN ORDER, one message each. Wait for the answer
before moving to the next. If `$ARGUMENTS` is empty, ask for the name first.

### Q1 — Type
```
Type? [feature | bug-fix | research | refactor | investigation]
(default: feature)
```
Validate the answer. Default to `feature` on blank.

### Q2 — Related feature (optional)
```
Is this related to an existing/archived feature? (name or "no")
```
If a name is given, verify it exists under `.claude/features/` OR
`.claude/features/_archive/` before accepting. If not found, tell the user
and re-ask. If the user actually wants to RESUME instead of relate, suggest
`/feature-resume <name>` and stop.

### Q3 — Goal
```
Goal in one sentence:
```

### Q4 — Branch (Y/N)
```
Create and check out branch `feature/<normalized-name>`? (y/n)
```

## After answers are collected

1. Run the init script (pass `"n/a"` explicitly if the user answered "no" to related,
   and empty string for branch if they answered "n"):
   ```
   bash .claude/scripts/init-feature.sh \
     "<normalized-name>" \
     "<type>" \
     "<branch-or-n/a>" \
     "<related-or-'none'>"
   ```

2. Read the created `feature.md`. Write the Goal into the **Goal** section
   (minimal edit — don't reformat anything else).

3. If the user answered **y** to Q4, run:
   ```
   git checkout -b feature/<normalized-name>
   ```
   If the branch already exists, just check it out.

4. Confirm to the user in this exact format:
   ```
   ● <name>                                 [<type>]
     ✓ feature.md + notes.md created
     ✓ Active pointer set  (next chat auto-loads this feature)
     ✓ Branch  <created | switched | not created>

   Next:
     → Fill Context & Constraints in feature.md
     → Move on to Phase 2 when ready
   ```

## Rules

- Do NOT start implementing the feature in this turn
- Do NOT overwrite an existing `features/<name>/feature.md`
- If the name exists in `_archive/`, tell the user and suggest `/feature-resume`
- Keep edits minimal; don't reformat unrelated parts of feature.md
- If the user aborts at any question, do not create the feature at all
- Do NOT manually list project skills here — they auto-trigger based on the
  work Claude performs (any installed `.claude/skills/*` with keyword triggers
  fire automatically). Feature.md has no `Recommended Skills` block.
- Confirmation uses `●` glyph + `✓` prefix + `→` for next-steps — matches
  /feature-list and /feature-status output conventions
