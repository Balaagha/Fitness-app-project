# Research: bmalph-integration-research

## Metadata
- **Type**: research
- **Branch**: `n/a`
- **Started**: 2026-05-27
- **Current phase**: 1 / 3
- **Overall status**: in_progress
- **Related features**: none

---

## Question
bmalph və alternativ autonomous loop tool-larını (snarktank/ralph, frankbria/ralph-claude-code, built-in `/loop` + `ScheduleWakeup`, /schedule cron agents) layihənin mövcud BMAD v6.6.0 + customized CLAUDE.md + feature-memory infrastructure-u ilə inteqrasiya riskləri baxımından qiymətləndirmək və ən az migration riski ilə autonomous overnight execution capability-si verən yolu seçmək.

## Why It Matters
Solo developer ~20h/həftə işləyir. Autonomous overnight execution machine resource-larını məhsuldar saxlayır — amma yanlış tool seçimi (məs. bmalph init-in `_bmad/` framework files-ı replace etməsi) 5 custom agent .toml, 3 external BMAD module (bmb/cis/tea), 257 sətirlik AZ market-spesifik CLAUDE.md-ni risk altına atır. Bu qərar həm autonomous execution stack-i, həm də mövcud BMAD planning infrastructure-unun future-proof olub-olmamasını müəyyən edir.


---

## Phases

### Phase 1: Scope the Question
- [ ] Break the question into sub-questions
- [ ] List known constraints and non-goals
- [ ] Decide what "done" looks like (report / POC / decision memo)
- **Status:** in_progress

### Phase 2: Investigation
- [ ] Gather sources (code, docs, prior art, external references)
- [ ] Record each source + takeaway under Findings
- [ ] Prototype if needed (in a scratch branch, not committed)
- **Status:** pending

### Phase 3: Synthesize & Recommend
- [ ] Write recommendation in Decisions with trade-offs
- [ ] Promote reusable findings to `.claude/rules/` during /feature-end
- **Status:** pending

---

## Decisions / Recommendations

| Date       | Decision | Rationale | Impact |
|------------|----------|-----------|--------|
| 2026-05-27 | ~~Recommended: Yol C — `/loop` + `ScheduleWakeup`~~ **SUPERSEDED** | Initial reasoning ignored ScheduleWakeup cancel-API bug (#58235) and single-session context degradation. Acceptable for short interactive runs (≤2h) but unsafe for unattended overnight. | See revised recommendation below. |
| 2026-05-27 | **REVISED RECOMMENDATION (layered)**: BMAD planning unchanged + `/ralph-loop` Anthropic plugin for medium overnight + `claude --print` phased + `/goal` for production overnight. `/loop` retained for short interactive only. | `/ralph-loop` plugin is Anthropic Verified, fresh-session-per-iteration (Ralph's actual core value), Stop-hook intercept. Sidesteps both ScheduleWakeup bug AND single-session context bloat. Custom .toml + CLAUDE.md untouched. Eva Khmelinskaya production pattern available as escalation tier. | Install `/ralph-loop` plugin from Claude marketplace. Zero changes to `_bmad/`, `_bmad-output/`, CLAUDE.md. Trial run on small phase before overnight commitment. |
| 2026-05-27 | **Rejected: Yol A — bmalph adoption** | 5 custom agent .toml files (28 persistent_facts injecting AZ market context) at risk from `_bmad/` framework replace. CLAUDE.md modification scope undocumented. 29 open issues, single maintainer. Bundled BMAD lag (v6.2.0 vs upstream v6.8.0) — `bmalph upgrade` exists but adds release-lag dependency. Wraps `/ralph-loop` Anthropic plugin essentially, with extra abstraction layer. | Avoided wrapper overhead. Direct `/ralph-loop` adoption recovers ~95% of bmalph's value without `_bmad/` replace risk. |
| 2026-05-27 | **Rejected: Cloud agents (Devin, Cursor BA, OpenHands, Ruflo)** | Different paradigm — cloud sandboxed remote execution. Cost ($20+/mo Devin, $$/token Cursor), repo upload privacy concern, less local control. Overkill for solo dev with full-time job (~20h/week). | Revisit if multi-agent swarm becomes a need. Ruflo notable for 84.8% SWE-bench but adds dependency. |
| 2026-05-27 | **Methodology criteria adopted (for future tool research)** | See Findings → "RESEARCH METHODOLOGY GAPS" section. 7 specific mitigation rules to prevent recurrence of the gaps that caused initial Yol C recommendation. | Applies to all future tool/architecture recommendations in this repo. |


## Findings / Sources

**2026-05-27 — Bundled BMAD version (HIGH confidence)**
- Source: `https://github.com/LarsCowe/bmalph/blob/main/bundled-versions.json` → commit `d1163f85` → BMAD v6.2.0
- Takeaway: bmalph v2.11.0 bundles BMAD **v6.2.0** (April 2026). Upstream BMAD is **v6.8.0** (May 25, 2026). Local install is **v6.6.0**. bmalph adoption = 4 minor versions DOWNGRADE.
- Impact: Loses `bmad-automator` (v6.8.0 story automation), `bmad-investigate` (v6.7.0 evidence-graded forensics), `bmad-prd` 3-intent refactor (v6.7.0), non-interactive config flags (v6.6.0), 42-platform expansion (v6.5.0).

**2026-05-27 — TOML customization is a v6.4.0 feature (CRITICAL)**
- Source: BMAD-METHOD CHANGELOG v6.4.0 (April 25)
- Takeaway: `_bmad/custom/*.toml` agent override mechanism was INTRODUCED in v6.4.0. bmalph bundled v6.2.0 predates this.
- Impact: If bmalph init replaces `_bmad/` with v6.2.0 framework, the user's 5 custom .toml files (28 persistent_facts injecting AZ market context into every agent) may become orphaned/unread.

**2026-05-27 — bmalph init writes to platform instructions file (HIGH confidence)**
- Source: bmalph README — "Updates the platform's instructions file with BMAD workflow instructions"
- Takeaway: For Claude Code platform, this means CLAUDE.md is modified. Append vs replace behavior is not documented; dry-run required to verify.
- Impact: User's 257-line AZ-market-specific CLAUDE.md (Volt color system, hard constraints, monetization, qadağalar) at risk.

**2026-05-27 — macOS Bash 3.2 issue partially resolved (MEDIUM confidence)**
- Source: bmalph issue #110 (closed), CHANGELOG v2.8.0
- Takeaway: bmalph v2.8.0 replaced Bash 4+ syntax with POSIX. User has default `/bin/bash` v3.2.57, no Homebrew bash.
- Impact: Should work without `brew install bash`, but other latent bash quirks possible. Recommend `brew install bash` as defensive measure if bmalph adopted.

**2026-05-27 — Built-in /loop + ScheduleWakeup is the native autonomous loop (HIGH confidence)**
- Source: Claude Code docs (scheduled-tasks), `/loop` skill in available-skills, `ScheduleWakeup` tool available
- Takeaway: Anthropic-native autonomous loop, requires Claude Code v2.1.72+. User has v2.1.152 ✓. Self-paces via Claude's own judgment (ScheduleWakeup tool), stops when task complete (skips next wakeup). Cross-session memory via existing `.claude/features/` feature-memory infrastructure.
- Impact: Zero install, zero customization risk, zero 3rd-party dependency. Native solution replicates 80% of bmalph value (autonomous overnight execution) without any of its migration risks.

**2026-05-27 — Cloud autonomous alternatives exist but different paradigm (MEDIUM confidence)**
- Source: Devin ($20/mo + $2.25/ACU), Cursor Background Agents (8 parallel cloud sandboxes), OpenHands (MIT free, pay tokens), Ruflo/Claude Flow v3.6.12 (multi-agent hive-mind, 84.8% SWE-bench)
- Takeaway: These run in remote sandboxes, deliver PR. Different cost/privacy/control model vs local loop.
- Impact: For solo dev with IBAM full-time + KMP fitness app: local loop fits supervision/cost/privacy. Cloud agents = $$ + repo upload + less control. Not recommended now; consider Ruflo if multi-agent swarm becomes a need later.

---

**2026-05-27 — CORRECTION: `/ralph-loop` is an OFFICIAL Anthropic plugin (HIGH confidence — major prior research gap)**
- Source: https://claude.com/plugins/ralph-loop (Anthropic Verified, "Made by Anthropic")
- Takeaway: Anthropic ships an official Ralph implementation as a Claude Code plugin. Uses stop-hook interception → automatically re-feeds prompt while preserving file modifications + git history between iterations. Usage: `/ralph-loop "prompt" --max-iterations 10 --completion-promise "DONE"`.
- Impact: **This invalidates the prior reasoning that "Yol C is the only zero-install Anthropic-native option."** `/ralph-loop` plugin gives you fresh-session-per-iteration (Ralph's actual core value) WITH Anthropic verification, no bash scripting, no bmalph wrapper. Prior recommendation underweighted this because the local available-skills list (which surfaces `/loop` + `/schedule`) was treated as exhaustive — but Claude marketplace plugins must be installed explicitly.

**2026-05-27 — CRITICAL: `/loop` + `ScheduleWakeup` has known production bug for overnight runs (HIGH confidence — prior research gap)**
- Source: anthropics/claude-code Issue #58235 (opened May 12, 2026), labels: enhancement, area:core, platform:macos
- Takeaway: `ScheduleWakeup` has no `cancel_wakeup(id)`, no `list_wakeups()`, no `max_fires=N`. Agent that schedules wakeup before returning, then re-schedules on every wakeup firing, creates an uncancellable infinite loop. Only escape: restart Claude Code or kill underlying process. macOS-specific label flagged.
- Impact: For overnight unattended runs, this is a production-grade blocker. Prior recommendation called `/loop` "perfect" for overnight without searching issue tracker — methodology gap.

**2026-05-27 — CRITICAL: Single-session context degrades long before 200K limit (HIGH confidence — prior research gap)**
- Source: Multiple — morphllm.com/claude-code-auto-compact, codex blog April 2026, Eva Khmelinskaya overnight article
- Takeaway: Context quality begins degrading at ~50% full (~100K tokens). Auto-compact triggers near 95% (~190K). Compaction "dilutes instructions" — even CLAUDE.md rules lose effectiveness after multiple compaction rounds. Compaction creates feedback loop: 1 compact → 3-5 more in rapid succession. Session that works for 30 min may ignore rules at min 90.
- Impact: `/loop` runs in single session — context accumulates across all iterations. For an 8-hour overnight run with ~16+ iterations, /loop will hit compaction degradation. Standalone Ralph (or `/ralph-loop` plugin) sidesteps this entirely by starting fresh session per iteration. Prior recommendation didn't model context budget across iteration count.

**2026-05-27 — Ralph's actual core value is fresh-session-per-iteration, NOT circuit breaker or swarm (HIGH confidence — prior research mischaracterized)**
- Source: knightli.com/en/2026/04/27/ralph-autonomous-agent-loop, snarktank/ralph design philosophy
- Takeaway: Ralph's reason-to-exist is "don't force same agent to work in increasingly long messy context — start brand-new AI coding session for every iteration." State persists via disk (progress.txt, prd.json, git), not session memory. Each iteration: read disk state → implement one story → write disk state → exit. Context never exceeds ~100K per iteration.
- Impact: Prior recommendation treated /loop and Ralph as functionally equivalent. They are NOT equivalent — opposite session models. /loop = same session grows; Ralph = each iteration fresh. For long runs (5h+), Ralph's model is fundamentally safer.

**2026-05-27 — Eva Khmelinskaya production overnight pattern: `claude --print` + `/goal` + STATUS.md (HIGH confidence)**
- Source: https://medium.com/@evekhm/running-claude-code-autonomously-overnight-what-breaks-and-how-to-fix-it-3bee3bd958b5 (May 2026)
- Takeaway: Production pattern that ran 5 phases overnight with zero failures. Uses: (1) `claude --print` CLI mode (not interactive), (2) `--max-budget-usd 10.00` cost guardrail per phase, (3) `/goal` flag for self-healing retry within phase, (4) STATUS.md handoff document between phases, (5) `nohup` + `< /dev/null` for background, (6) CLAUDE.md context rules enforcing output redirection (`>file.log 2>&1 && tail -20`) for "10x context budget" improvement.
- Impact: For true production-grade overnight runs, this pattern beats both /loop and Ralph plugin. More setup but battle-tested.

**2026-05-27 — bmalph upgrade weakens "downgrade" argument (corrected from prior finding)**
- Source: bmalph CHANGELOG, `bmalph upgrade` command behavior
- Takeaway: bmalph DOES ship an `upgrade` command that updates bundled BMAD. The "downgrade" is not permanent — it's a release-lag delta (1-2 weeks typically). Prior recommendation overweighted this risk.
- Impact: Other bmalph risks (CLAUDE.md mutation, 5 custom .toml replace, TOML customization predating v6.4.0 bundled) remain valid. But "BMAD downgrade forever" framing was misleading. Recommendation against bmalph still holds but on different grounds.

---

## RESEARCH METHODOLOGY GAPS (added 2026-05-27 — self-audit)

Prior research recommended Yol C (`/loop` + ScheduleWakeup) confidently and was wrong on critical points. Reasons identified:

1. **No adversarial review of own recommendation** — never asked "how does my preferred option fail in production?"
2. **Treated local available-skills list as Anthropic exhaustive offering** — `/loop`, `/schedule` were available locally; missed `/ralph-loop` plugin in Claude marketplace.
3. **No known-bug search for recommended tools** — never searched "ScheduleWakeup bug", "Claude Code overnight breaks".
4. **Surface comparison instead of mechanism comparison** — treated /loop and Ralph as functionally equivalent because "both iterate" without examining session model (single session grows vs fresh per iteration).
5. **No context budget math** — never calculated context size after N iterations.
6. **No production experience search** — relied on documentation only; missed Eva Khmelinskaya's real-world overnight production article.
7. **"Zero install" bias** — overweighted setup cost vs reliability/context safety.

**Mitigation rules for future tool recommendations (promoted to feature criteria):**
- Run adversarial review pass on own recommendation BEFORE presenting
- Search known issues / bug tracker for recommended tools
- Compare mechanisms (session model, state model, exit detection) not surface features
- Calculate resource budgets (context tokens, time, cost) across full intended use case
- Search production experience reports ("X overnight production", "X gotchas")
- Plugin marketplace check — local available ≠ vendor's exhaustive offering
- "No install" is a weak proxy for "safe" — verify reliability separately


## Errors Encountered

| Error | Attempts | Resolution |
|-------|----------|------------|


---

## Progress Log

- 2026-05-27 13:04 — Research initialized


## Notes for Next Session

