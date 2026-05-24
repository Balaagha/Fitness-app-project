# Notes: finalize-onboarding-flow
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
2026-05-23 20:28 [impl] feature initialized — notes.md ready for working memory

2026-05-24 01:52 [impl] Post-auth section visual frame (boundary + corner badge + xref chips) follows ONBOARDING-FLOW / PREGNANCY-FLOW pattern: boundary rect #E6FF0008 fill + $accent stroke 4px + corner badge with slight overhang (boundary.x+24, boundary.y-24)
2026-05-24 01:52 [refs] Post-auth flows PRD consolidates 3 deferred stubs (paywall, settings, pro coaching teaser) into prd-post-auth-flows-2026-05-24.md — 5 screens with state machines, copy contracts, edge cases

2026-05-24 02:18 [gotcha] Pencil spawn_agents may leave placeholder containers empty if agent fails silently — verify container children with batch_get after spawn; re-spawn with more explicit child-build instructions if empty
2026-05-24 02:18 [impl] POST-AUTH section finalized: dark title block (RACEH) with 5 screen cards replaces Mkjop light label (now enabled:false); 3 right annotations (FFxxl) MONETIZE/HESAB İDARƏ/FAZA 2; flow bridge (hdmIK) DASHBOARD→KAPI→PAYWALL→TRIAL(highlight)→IAP→PRO→PREMIUM

2026-05-24 03:05 [gotcha] Pencil I() operation silently drops layout:"horizontal" property — created frames default to absolute positioning. Workaround: use C() to copy a known-working horizontal frame, then U() text/icon contents inside the copy
2026-05-24 03:05 [gotcha] Pencil U() operation may wipe children if you set certain props (specifically observed: width:fill_container + name change on a parent frame). Safer: only update simple props like content, fill, iconFontName on leaf nodes
2026-05-24 03:05 [impl] POST-AUTH section finalized via C() copy strategy — top row from f4rxlq, bottom row from Q3QNK, 3 annotation cards from efOBn. All layouts verified clean via snapshot_layout problemsOnly:true
