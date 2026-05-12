---
description: Consolidate the active feature's notes.md — dedupe, preserve invariants, archive aged entries
---

Compact `notes.md` for the active feature. Goal: keep signal, drop noise,
never silently delete anything important. Aged and duplicate entries move to
`notes.archive.md` — never to `/dev/null`.

## Inputs

- Active feature (via `bash .claude/scripts/get-active.sh`).
- `.claude/features/<feature>/notes.md` — the live working memory.
- `.claude/features/<feature>/notes.archive.md` — append-only archive (created if absent).

If no active feature → tell the user and stop.
If `notes.md` missing or has zero non-comment entries → tell the user
"nothing to compact" and stop.

## When to offer this

- User invokes it explicitly.
- `notes.md` exceeds 300 lines OR 25 KB (whichever first).
- User reports noise, contradictions, or drift in notes.

## Algorithm (deterministic, in this order)

### 1. Parse entries

Every non-comment line matching:
```
^YYYY-MM-DD HH:MM [tag] body$
```
is one entry. Preserve the raw text; never reflow.

### 2. Tag priority (strict)

| Tag          | Priority | Compact behaviour |
|--------------|----------|--------------------|
| `invariant`  | P0 — sacred | Always keep, never merge, never move. Duplicates of the same invariant: keep newest, move older duplicates to archive. |
| `criteria`   | P1 — sticky | Always keep. Near-duplicates (same spirit, different wording): merge into the clearer one, move the other to archive. |
| `gotcha`     | P2 — keep by recency | Keep newest N per concept cluster (default: 1). Older dupes → archive. |
| `impl`       | P3 — keep by relevance | If an `impl` line is contradicted by a newer entry (same subject), KEEP the newer, move the older to archive with an `[obsolete]` suffix appended to its body. |
| `refs`       | P3 — keep by relevance | Dedupe by normalised body (lowercase, whitespace-collapsed). Keep one; move others to archive. |

**Sacred rule**: if two entries disagree (contradiction), the older one moves
to archive with `[obsolete]` appended to its body — never deleted.

### 3. Cluster near-duplicates (only within the same tag)

Heuristic clustering — you (Claude) read the bodies and judge:
- same subject + same conclusion → duplicates
- same subject + different conclusion → contradiction (see §2 `impl`)
- different subject → leave alone

Do NOT use fuzzy string matching alone; semantics matter more.

### 4. Produce a dry-run diff FIRST

Before any file write, show the user in this exact format:

```
● Compact plan — notes.md                   <before>L / <kb>KB → <after>L
  Keep      <N>
  Merge     <M>   (pairs collapsed)
  Archive   <K>   (aged / duplicate)
  Flag      <C>   (contradiction → [obsolete])

✓ Keep  (<N>)
  <each line that stays, verbatim with tag>

◐ Merge  (<M>)
  ◆ <kept line>
    ⟵ absorbs:  <merged line>

🗄 Archive  (<K>)
  <each line>
    reason:  duplicate | obsolete | aged

⚠ Contradictions flagged  (<C>)
  [kept]      <newer entry>
  [obsolete]  <older entry — [obsolete] will be appended>

Reply:
  apply        → execute the plan above
  skip         → abort, no changes
  edit <n>     → adjust entry #n before deciding
```

If the user replies anything other than `apply`, STOP and make no changes.

### 5. Apply (only after `apply`)

1. Rewrite `notes.md`:
   - Preserve the top comment block verbatim.
   - Write surviving entries in original chronological order (not re-sorted).
2. Append removed entries to `notes.archive.md`:
   - Create `notes.archive.md` with a short header if absent (frontmatter-free).
   - Under a new section header `## Compacted YYYY-MM-DD HH:MM`, list the
     archived entries verbatim, each preceded by `- ` and an inline reason
     tag: ` <!-- reason: duplicate -->` etc.
3. Report in this exact format:
   ```
   ● Compact applied
     ✓ notes.md          <before>L → <after>L   (−<Δ> lines)
     ✓ notes.archive.md  +<K> entries under section "Compacted <timestamp>"
   ```

### 6. Never do these

- Never delete an entry outside of `notes.archive.md`.
- Never rewrite the body of a kept entry (only append `[obsolete]` when
  marking a contradicted line being moved to archive).
- Never change timestamps.
- Never touch `feature.md` from this command.
- Never run without the dry-run step, even if the file is huge.

## Edge cases

- **Less than 50 entries and no duplicates** → print "no compaction needed"
  and stop.
- **All entries are `[invariant]` or `[criteria]` with no duplicates** →
  print "nothing to archive — keeping as-is" and stop.
- **Malformed lines** (no tag, bad timestamp) → list them at the end of the
  dry-run under "malformed (left untouched)" and ask the user to fix or delete.
- **notes.archive.md is older than 90 days** → mention this but do not auto-rotate.
