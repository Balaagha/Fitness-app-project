# Notes: determine-scope-of-start-up
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
2026-05-10 14:00 [impl] feature initialized — notes.md ready for working memory
