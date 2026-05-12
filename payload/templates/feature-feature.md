# Feature: {{FEATURE_NAME}}

## Metadata
- **Type**: feature
- **Branch**: `{{BRANCH}}`
- **Started**: {{DATE}}
- **Current phase**: 1 / 5
- **Overall status**: in_progress
- **Related features**: {{RELATED}}

---

## Goal
<!-- One sentence describing the end state. Fill this in first. -->


## Context & Constraints
<!--
  - What the user asked for (1-2 sentences)
  - Scope (bullet list)
  - Out-of-scope (bullet list — prevents scope creep)
  - UX / accessibility rules specific to this feature
-->


---

## Feature Spec
<!--
  Cross-layer contract for this feature. Fill in only the sections
  relevant to this feature's scope (delete or leave the rest blank).
-->

### API Contract
<!-- endpoints, request/response DTO field names + types -->


### State Contract
<!-- State fields (ImmutableList<T>), Effect variants, Intents -->


### UI Contract
<!-- screens, component list, layout description -->


### DI Contract
<!-- dependencies needed, what is exposed -->


### Navigation
<!-- nav graph, fragments, deep links -->


---

## Phases

### Phase 1: Research & Discovery
- [ ] Read existing related code (note files in Findings)
- [ ] Identify constraints and dependencies
- [ ] Check `.claude/features/_archive/` for related archived work
- **Status:** in_progress

### Phase 2: Design / Spec
- [ ] Fill in Feature Spec above
- [ ] List files to create / modify
- [ ] Confirm Spec with user before Phase 3
- **Status:** pending

### Phase 3: Implementation
- [ ] <sub-task>
- [ ] <sub-task>
- **Status:** pending

### Phase 4: Testing & Verification
- [ ] Unit tests pass
- [ ] UI / integration tests (if applicable)
- [ ] Manual verification on device
- **Status:** pending

### Phase 5: Delivery
- [ ] Run project code-review (lint, formatter, review skill if available) → zero blocking issues
- [ ] PR description drafted
- [ ] Merged or ready for review
- **Status:** pending

---

## Decisions
<!--
  Append-only log. The Stop hook populates this via propose-confirm.
  Impact: low = style/naming, med = module-level, high = cross-cutting
-->

| Date       | Decision | Rationale | Impact |
|------------|----------|-----------|--------|


## Findings / Research
<!-- What was learned during investigation: API quirks, library limits, tricks -->


## Errors Encountered
<!--
  3-strike protocol: if the same approach fails 3 times, escalate to user
  and document the full chain here so it never repeats.
-->

| Error | Attempts | Resolution |
|-------|----------|------------|


---

## Progress Log
<!-- Chronological. One line per session or significant milestone. -->

- {{TIME}} — Feature initialized


## Notes for Next Session
<!--
  End-of-session handoff — what's blocking, what to do first next time.
  Overwrite this each session; it's a pointer to the NEXT concrete step.
-->

