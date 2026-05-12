# Feature-Memory System

**Scope of this file**: workflow-only. Project-specific architecture, coding
standards, skills, agents, and review rules belong in `/CLAUDE.md` at repo
root (or wherever your project keeps them). Both files load every session
— do not duplicate rules here.

---

## What it does

Each piece of work — feature, bug-fix, research, refactor, investigation —
gets a folder that carries the full story across sessions:

```
.claude/features/<name>/
├── feature.md        ← curated ledger (Goal, Spec, Phases, Decisions, Findings, Errors)
├── notes.md          ← working memory (short one-liners, Claude writes proactively)
└── notes.archive.md  ← created on first /feature-compact (dedupe + archive dump)
```

An **active pointer** (`.claude/features/.active`) decides which files
auto-load on every new chat, `/clear`, or after compaction. Pointer-based,
not branch-based: switch freely, pause, resume archived work — all without
changing git branches.

Two-tier separation is intentional:
- `feature.md` = permanent record. Goal, Spec, confirmed Decisions / Findings / Errors.
- `notes.md`   = working scratch. Quick one-liners, low friction, compacted over time.

---

## Commands

| Command | Purpose |
|---|---|
| `/feature-start <name>` | Create feature.md + notes.md; asks type, related, goal, branch (y/n); sets active pointer |
| `/feature-status` | Compact status report for the active feature |
| `/feature-decision <text>` | Append a manual decision row to feature.md (Decisions table) |
| `/feature-finding <text>` | Append a finding to feature.md (Findings section), one-shot |
| `/feature-note [tag] <text>` | Append a one-line entry to notes.md (tags: impl \| gotcha \| criteria \| refs \| invariant) |
| `/feature-compact` | Dedupe + archive notes.md (dry-run first, user must reply "apply") |
| `/feature-pause` | Clear the active pointer (files kept) |
| `/feature-resume <name>` | Re-activate a paused or archived feature; restores archived files |
| `/feature-list` | List Active / Paused / Archived features |
| `/feature-end` | Auto code-review → pattern promotion → archive → clear pointer |

---

## notes.md — working memory

### Format

One line per entry, timestamp + tag + body:

```
YYYY-MM-DD HH:MM [tag] one-line content
```

### Tags (strict set)

| Tag          | Meaning                                              | Survives compaction |
|--------------|------------------------------------------------------|---------------------|
| `[invariant]`| Constraint that must never break                     | Always — sacred     |
| `[criteria]` | Best-implementation criterion (stability/perf/UX)    | Always — sticky     |
| `[gotcha]`   | API quirk, edge case, non-obvious behaviour          | By recency          |
| `[impl]`     | Implementation choice / local convention             | By recency          |
| `[refs]`     | Behavioural reference — NOT file paths               | By recency          |

### Cap

300 lines OR 25 KB — whichever comes first. When exceeded, Claude proposes
`/feature-compact`. `/feature-compact` preserves `[invariant]` + `[criteria]`,
dedupes `[impl]` / `[gotcha]` / `[refs]`, and moves aged entries to
`notes.archive.md`. It **never deletes silently** — archive is append-only.

### Claude writes proactively

During any session on an active feature, Claude appends a one-line entry to
notes.md — **without asking** — when one of these four conditions is met:

1. A new **non-obvious pattern** is discovered in the codebase (not re-readable from the files).
2. A **root cause** of a 3-strike-level error is identified.
3. A concrete interpretation of a **project-wide code-review criterion** is decided for this feature.
4. A **local convention unique to this module / feature** is observed.

After writing, Claude prints a single short line to the user:
`📝 notes.md: [tag] <body>`. The user may undo by saying "revert that note"
— Claude removes the last-appended line from notes.md.

**Do not** use proactive writing for:
- Trivial edits, step-by-step reasoning, superseded ideas
- Anything already in `feature.md` (Decisions / Findings / Errors)
- File paths — describe behaviour, not locations (paths rot)

### Anti-patterns (auto-reject)

- Multi-line entries (one idea per line)
- Missing tag prefix (`[tag]` is required — parser + compaction rely on it)
- File paths in `[refs]` body (describe behaviour: "Repo maps ApiResult→Result<T>")
- Duplicating feature.md content
- Writing to notes.md during `/feature-end` archive flow (wrong phase)

---

## Feature types

`/feature-start` asks the **type** up front so the template matches the work:

| Type | Phases | Template focus |
|---|---|---|
| `feature` | 5 | Full implementation with Feature Spec (API/State/UI/DI) |
| `bug-fix` | 3 | Repro → Root Cause → Fix + Regression Test |
| `refactor` | 4 | Current State → Target → Migration → Verification |
| `research` | 3 | Question → Investigation → Recommendation |
| `investigation` | 2 | Signals → Conclusion (stays in archive as record) |

**Skills are NOT hard-coded into feature.md.** Whatever skills your project
ships (in `.claude/skills/` or globally) auto-trigger on their own keywords
— Claude loads them on its own as the work unfolds. No scope question, no
skill list in the template — that would be redundant noise.

---

## Daily flow

1. **Start**: `/feature-start <name>` — answer 4 questions → feature.md + notes.md created, pointer set
2. **Work**: every new chat auto-loads feature.md + last 50 lines of notes.md (SessionStart hook)
3. **Log mid-chat**:
   - `/feature-decision "<text>"` → feature.md Decisions (asks rationale + impact)
   - `/feature-finding  "<text>"` → feature.md Findings (one-shot, no follow-up)
   - `/feature-note [tag] "<text>"` → notes.md (zero friction, no follow-up)
   - Claude may auto-append to notes.md (see rules above)
4. **Compact notes**: if notes.md > 300 lines, run `/feature-compact` — dry-run first, then `apply`
5. **Session end**: Stop hook proposes decisions/findings from the chat;
   reply `y` / `n` / `1,3` / `d1 f2`; approved entries written to feature.md
6. **Switch mid-work**: `/feature-pause`, then `/feature-start <other>` or `/feature-resume <other>`
7. **Finish**: `/feature-end` — auto code-review (when code was touched) → `[criteria]` promotion → archive

### Resuming for a follow-up

An archived feature is not frozen. A bug comes back six weeks later:

```
/feature-resume <name>
```

The archive is restored to `features/<name>/` (including notes.md and
notes.archive.md if they existed), the active pointer updates, a
`Resumed on <date>` line is appended to the Progress Log, and the Stop
hook optionally captures why you resumed in the Decisions table.

---

## Hook semantics

### SessionStart
- Injects feature.md in full
- Injects **last 50 lines** of notes.md (recency window)
- Injects **all `[invariant]` + `[criteria]`** lines from notes.md regardless of age (sticky signal)
- Shows last 10 rows of `decisions.jsonl` if parallel-session log exists

### Stop
- Fires **once per session** (session-id marker in `.claude/backups/stops/`)
- Only fires when an active feature is set
- Propose-confirm flow for DECISIONS / FINDINGS / ERRORS into feature.md
- `n` / `none` → nothing written, hook silent for this session (next session re-triggers)

### PreCompact
- Backs up the raw transcript to `.claude/backups/transcripts/` (last 20 kept)
- Dumps the **full notes.md** into the conversation
- Re-emphasises `[invariant]` + `[criteria]` lines in a STICKY block so they survive compaction
- Reminds Claude to flush any un-persisted state to feature.md before summarisation

---

## Parallel sessions

- **Different features in parallel**: one git worktree per feature branch,
  each worktree has its own `.active` pointer. Zero conflicts. Preferred pattern.
- **Same feature in parallel**: avoid terminal-to-terminal. Use the
  orchestrator + sub-agents described in `/CLAUDE.md` instead.
- **Must run same-feature in parallel**: append to
  `.claude/features/<name>/decisions.jsonl` — POSIX-atomic under 4 KB per write.
  The SessionStart hook shows the last 10 entries automatically.
- `notes.md` is **single-writer**: do not share it across concurrent sessions.
  Use `decisions.jsonl` for that case.

---

## File map

```
.claude/
├── features/
│   ├── .active                     ← pointer (gitignored)
│   ├── <name>/
│   │   ├── feature.md              ← curated ledger
│   │   ├── notes.md                ← working memory
│   │   ├── notes.archive.md        ← (created by /feature-compact)
│   │   └── decisions.jsonl         ← (only for parallel-session writes)
│   └── _archive/<name>/            ← same layout, moved by /feature-end
├── templates/
│   ├── feature-feature.md
│   ├── feature-bug-fix.md
│   ├── feature-research.md
│   ├── feature-refactor.md
│   ├── feature-investigation.md
│   └── notes.md                    ← shared across all feature types
├── commands/                       ← 10 slash commands
├── hooks/                          ← session-start, stop, pre-compact
├── scripts/                        ← set-active, get-active, resolve-feature,
│                                     init-feature, check-complete, resume-feature,
│                                     list-features, json-extract
└── backups/                        ← transcripts + stop markers (gitignored)
```

---

## What NOT to do

- Do not `@-import` large files here — it bloats every session
- Do not overwrite feature.md sections — always append or edit in place
- Do not start implementing before Goal and the first phase's sub-tasks are filled
- Do not stop a session without going through the Stop hook's propose-confirm
- Do not duplicate project architecture or coding rules here — they belong in your project's main `CLAUDE.md`
- Do not write multi-line entries to notes.md — one line, one idea
- Do not write file paths in `notes.md [refs]` — describe behaviour
- Do not delete entries from `notes.md` manually — use `/feature-compact` (moves to archive)
