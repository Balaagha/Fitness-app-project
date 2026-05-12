# Bug Fix: {{FEATURE_NAME}}

## Metadata
- **Type**: bug-fix
- **Branch**: `{{BRANCH}}`
- **Started**: {{DATE}}
- **Current phase**: 1 / 3
- **Overall status**: in_progress
- **Related features**: {{RELATED}}

---

## Bug Summary
<!-- One sentence: what is broken, observed symptom -->


## Reproduction
<!--
  - Steps to reproduce (numbered)
  - Expected behaviour
  - Actual behaviour
  - Environment (device, OS, build variant, backend env)
-->


---

## Phases

### Phase 1: Reproduce & Isolate
- [ ] Reproduce locally with documented steps
- [ ] Identify the exact commit/change that introduced it (if applicable)
- [ ] Narrow to the smallest failing input
- **Status:** in_progress

### Phase 2: Root Cause & Fix
- [ ] Document root cause under Findings (not just the fix)
- [ ] Implement fix — smallest change that resolves it
- [ ] Verify no regression in adjacent flows
- **Status:** pending

### Phase 3: Regression Guard & Delivery
- [ ] Add regression test (unit / UI) so this can't recur silently
- [ ] Run project code-review (lint, formatter, review skill if available) → zero blocking issues
- [ ] PR description references original incident
- **Status:** pending

---

## Decisions

| Date       | Decision | Rationale | Impact |
|------------|----------|-----------|--------|


## Findings / Root Cause
<!-- WHY it broke, not just what the fix is -->


## Errors Encountered

| Error | Attempts | Resolution |
|-------|----------|------------|


---

## Progress Log

- {{TIME}} — Bug fix initialized


## Notes for Next Session

