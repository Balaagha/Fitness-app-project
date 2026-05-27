# Notes: bmalph-integration-research
<!--
  Working memory for this feature. Short, high-signal one-liners only.
  Lives next to feature.md. Archived together with the feature when done.

  FORMAT (strict — line-level, one line per entry):
      YYYY-MM-DD HH:MM [tag] one-line content

  TAGS (pick exactly one per line):
    [impl]      implementation choice / local convention
    [gotcha]    API quirk, non-obvious behaviour, edge case
    [criteria]  best-implementation criterion (stability/perf/UX rule)
    [refs]      behavioural reference (describe BEHAVIOUR, not file paths)
    [invariant] constraint that must NEVER break

  CAP:
    300 lines OR 25 KB — whichever comes first.
    When exceeded, Claude proposes /feature-compact.
    /feature-compact preserves [invariant] + [criteria], dedupes [impl]/[gotcha],
    moves aged entries to notes.archive.md. It never deletes silently.

  AUTO-WRITE TRIGGERS (Claude writes proactively, one short notification line):
    1. New non-obvious pattern discovered in the codebase
    2. Root cause of a 3-strike-level error
    3. A concrete interpretation of a project-wide code-review criterion
    4. A local convention unique to this module / feature

  DO NOT:
    - Duplicate entries from feature.md (Decisions/Findings/Errors live there)
    - Write file paths — paths rot; describe behaviour instead
    - Write multi-line blocks — one idea per line
    - Remove tag prefix — parser relies on it
-->

<!-- entries below, newest at bottom -->
2026-05-27 13:04 [impl] feature initialized — notes.md ready for working memory
2026-05-27 13:15 [invariant] bmalph init must never run in this repo — would replace _bmad/ v6.6.0 with bundled v6.2.0 (predates v6.4.0 TOML customization hosting our 5 custom .toml overrides)
2026-05-27 13:15 [invariant] Autonomous overnight execution = built-in /loop + ScheduleWakeup tool, NOT bmalph/Ralph
2026-05-27 13:15 [criteria] Any autonomous tool adoption must preserve _bmad/custom/*.toml (28 persistent_facts), CLAUDE.md AZ-market content, 11 PRD/UX artifacts
2026-05-27 13:15 [gotcha] bmalph bundled-versions.json points to BMAD commit d1163f85 = v6.2.0; never upgraded after bmalph v2.8.0
2026-05-27 13:15 [refs] /loop self-paces via ScheduleWakeup clamped [60s, 3600s], stops by skipping next wakeup when task is provably complete
2026-05-27 13:45 [invariant] /ralph-loop (Anthropic Verified plugin) is the medium-tier overnight tool — use this for unattended runs, NOT /loop+ScheduleWakeup (issue #58235 uncancellable wakeup bug)
2026-05-27 13:45 [invariant] Single-session loops (/loop) degrade past ~50% context (~100K tokens); fresh-session-per-iteration (Ralph plugin) is the only safe model for overnight runs
2026-05-27 13:45 [criteria] Before any tool recommendation: adversarial review own pick + search issue tracker + math context budget across iteration count + read production experience reports
2026-05-27 13:45 [criteria] Local available-skills list is NOT exhaustive of Anthropic's plugin offering; Claude marketplace must be checked separately
2026-05-27 13:45 [refs] Eva Khmelinskaya overnight pattern: claude --print + /goal + --max-budget-usd + STATUS.md handoff + nohup + output redirection (>file.log && tail -20) for 10x context budget
2026-05-27 13:45 [gotcha] ScheduleWakeup issue #58235 closed but fix detail not visible — verify mitigation before relying on /loop for unattended runs
