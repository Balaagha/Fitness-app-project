---
description: Append a finding to the active feature's feature.md Findings section
argument-hint: <one-line finding>
---

Append a finding directly to the **Findings / Research** section of
`feature.md`. One-shot — no propose-confirm, no Stop-hook involvement.

Findings are high-signal research results / API quirks / reusable insights
that deserve a permanent home in feature.md (unlike `notes.md` working
memory, which is short-term and may be compacted away).

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

2. Open `.claude/features/<feature>/feature.md`.

3. Locate the Findings heading. Templates vary — the heading always
   **starts with** `## Findings` and may have a suffix:
   - `## Findings / Research` (feature)
   - `## Findings / Root Cause` (bug-fix)
   - `## Findings / Current State` (refactor)
   - `## Findings / Sources` (research)
   - `## Findings / Evidence` (investigation)

   Match by the `^## Findings` prefix — do NOT hardcode the suffix.
   Cases to handle:
   - Heading exists with comment + blank body → append under the comment
   - Heading exists with existing bullets → append a new bullet at the end
     of that section (before the next `## ` heading)
   - Heading missing (shouldn't happen, but be defensive) → insert
     `## Findings / Notes` after the `## Decisions` block, or before
     `## Errors Encountered` / `## Progress Log` — whichever comes first

4. Append a new bullet in this exact shape:
   ```
   - YYYY-MM-DD — $ARGUMENTS
   ```

5. Keep the edit minimal — do NOT reformat surrounding sections.

6. Confirm:
   ```
   ✓ finding logged → feature.md
   ```

## Rules

- Do NOT ask follow-up questions
- Do NOT write to notes.md — this command is feature.md only
- Do NOT overwrite existing findings — always append
- Do NOT add Decisions / Errors entries here — wrong tools for those
- If the body is empty, tell the user "empty finding, nothing written" and stop
