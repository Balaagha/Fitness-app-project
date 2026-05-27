# Refactor: polish-onboarding-clean-android

## Metadata
- **Type**: refactor
- **Branch**: `main`
- **Started**: 2026-05-28
- **Current phase**: 1 / 6
- **Overall status**: in_progress
- **Related features**: finalize-mobile-ui-for-onboarding (predecessor — 40/40 1:1 Pencil fidelity baseline)
- **Execution mode**: ralph-loop / autonomous overnight (Android-only, mock backend)

---

## Motivation
Predecessor feature `finalize-mobile-ui-for-onboarding` brought all 42 Pencil onboarding screens to 1:1 visual fidelity (verified across Phase A–G remediation loop). What's still NOT clean:

- Hard-coded strings still leak into composables (e.g. `"Elm"` pillar label, per-variant Welcome eyebrows `"İZLƏ"/"ÖLÇ"/"ÇAT"` literal in code)
- TODO markers in VMs (`Q3AgeSoftWarn` routing branch, `welcomePillScience` key absence)
- Mock backend stubs are structurally present but contract surface is uneven across repos (signatures vs. callable shape)
- Localization (AZ canonical, RU/EN parity) lacks a 3-way coverage diff
- Reusable design-system component APIs are inconsistent (parameter order, default values, KDoc absent)
- Lingering build gotchas (`kotlinx.datetime.Clock.System` unresolved on Kotlin 2.3, emulator default-locale=en) make capture noisy
- A handful of glyphs (lucide-style icons) are Canvas-approximated with no documented intent → future contributors will be confused

This refactor — driven overnight via Ralph loop — takes every screen from "visually correct" to "production-grade clean."

## Non-goals
- Backend (Supabase) wiring — mock `RemoteSource` stays; JSON-string symmetric contract preserves one-file swap path
- iOS verification — Android-only this pass (user explicit)
- Pencil design edits — read-only `mcp__pencil__*` access; never `set_variables` / `batch_design`
- New screens — only the 40 already-mapped Pencil frames (WXUwF Pro Coaching = Faza 2, skip)
- AI runtime triggers — none of the 5 plan-AI triggers exercised; admin-only Veo path untouched
- `material-icons-extended` dependency — Canvas glyphs stay
- 3D model assets, real-time camera, social, wearable — Faza 2

---

## Tooling Rules (Ralph üçün məcburi — hər iterasiya əvvəli oxu)

1. **Pencil .pen file**: first iteration `mcp__pencil__get_editor_state(include_schema:true)`, then `batch_get` + `snapshot_layout` + `get_variables` for structural data. **`get_screenshot` BANNED** — predecessor pass already verified 1:1 fidelity; screenshots burn tokens.
2. **Android build**: `./gradlew :androidApp:assembleDebug -x test`; clean build at end of each phase. Use `mcp__gradle__inspect_build` only for error triage.
3. **Android launch verify**: `adb shell am start -W -a android.intent.action.VIEW -d "fitnes://app?devScreen=<id>" org.betech.fitnes.android` — only when a screen has visual risk (rare; predecessor verified visuals).
4. **No iOS**: never invoke `mcp__xcodebuild__*`. If iOS Koin init changes, leave a TODO line in Findings and move on.
5. **Localization rule**: every new / modified user-visible string lands in `Strings.kt` + AZ/RU/EN variants in the same edit. Hard-coded `Text("xxx")` is a refactor failure.
6. **Mock backend**: `MockRemoteSource` + `data/repository/*Impl.kt` is the contract surface. JSON-string symmetric — Supabase swap is one file.
7. **Commit policy**: per-screen atomic commits — `polish(<screen>): <one-line>`. Never commit half-done state. Never `git push` (user pushes after morning review).

---

## Phases

### Phase 1: Audit & Inventory
- [ ] Open `mcp__pencil__get_editor_state(include_schema:true)` once; cache schema in this iteration's batch_get calls
- [ ] Walk Compose source tree `presentation/onboarding/` — list every screen file + matching VM + matching state class
- [ ] For each screen, mark issues found: hardcoded strings, magic dp/sp values, unused imports, dead state fields, missing analytics calls, missing AZ/RU/EN keys, TODO markers, deprecated API usage
- [ ] Catalogue reusable components: VoltButton, VoltOptionCard, VoltSlider, VoltMeasurementCard, VoltSegmentedRow, QuestionScaffold, QuestionProgressBar, HexagonLogo, VoltAppBar, VoltTextField, DestructiveButton, BackChevron — record each's parameter signature + KDoc presence
- [ ] Review mock repo surface: AuthRepository, UserProfileRepository, OnboardingRepository, AnalyticsRepository, ExerciseRepository — record every method, return shape, success/failure paths
- [ ] Write audit table into `## Findings / Current State` below (one row per issue, with screen+severity)
- **Status:** in_progress

### Phase 2: Design System Hardening
- [ ] `mcp__pencil__get_variables` — pull full Pencil token table
- [ ] Cross-check `VoltColors` hex values against Pencil; log mismatches into Decisions, fix afterwards
- [ ] Cross-check `VoltType` size + weight scale against Pencil typography styles
- [ ] Cross-check `VoltSpacing` 4/8/12/16/24/32/48 scale against Pencil paddings
- [ ] Standardize reusable component APIs: `modifier: Modifier = Modifier` first, content lambdas last, KDoc on every public composable
- [ ] Add KDoc to HexagonLogo, VoltOptionCard, VoltMeasurementCard, VoltSegmentedRow, QuestionScaffold — describe purpose + selection state contract + accepted parameter ranges
- [ ] Run `./gradlew :androidApp:assembleDebug -x test` — must remain green
- [ ] Commit: `polish(designsystem): KDoc + parameter-order standardization`
- **Status:** pending

### Phase 3: Auth Flow Clean Pass
- [ ] AuthGate (K1n7u5): copy review, Apple/Google/Email/Skip stack, legal footer i18n
- [ ] EmailSignup (ZJFFO): 4 validation chip live, mock signup → EmailVerify nav, error path
- [ ] EmailLogin (O8lWVO): forgot-pwd link, signup link, mock login error → LoginError nav
- [ ] EmailVerify (zREhj): 6-digit OTP overlay, 45s resend cooldown, wrong-email back chevron
- [ ] PwdResetEmail (rzAPa): email + 15min info disclaimer + send-link mock
- [ ] PwdResetForm (vA9Tb): 4 validation chip → navigate to login on submit
- [ ] LoginError (u27ve): retry CTA, "şifrəni unutdum" link
- [ ] SignupEmailExists (cYfk5): "bu e-poçt artıq qeydiyyatdadır" + login nav
- [ ] RateLimit (IFSQ3): cool-down message + try-again CTA
- [ ] ResetLinkExpired (a3Vwh6): re-request link CTA
- [ ] Build green after each screen; commit per-screen `polish(<screen>): clean pass`
- **Status:** pending

### Phase 4: Q-Screens + Onboarding Spine Clean Pass
- [ ] Splash (s7yM8w): 1.4s delay → LanguageSelect nav, Canvas hexagon, no static asset
- [ ] LanguageSelect (X2Pu6z): 3 options + INFO native-translation notice + persist preferredLanguage
- [ ] Welcome BPoym (main): 1:1 with 168/132/96 emblem + ELM İLƏ eyebrow
- [ ] Welcome tneyd (soft): 196/154/112 emblem + breadcrumb row + pillar row + Science pillar
- [ ] Welcome dRTLR (qida): apple glyph + İZLƏ eyebrow + science claim subtitle
- [ ] Welcome gjmPD (enerji): zap glyph + ÖLÇ eyebrow + science claim subtitle
- [ ] Welcome OKg7W (hədəf): target glyph + ÇAT eyebrow + science claim subtitle
- [ ] AiDisclosure (eQcvv): Apple 2025 mandatory disclosure + 4 user-agency bullets + "mütəxəssis uyğunluq yoxlaması" term — COPY MUST NOT CHANGE
- [ ] Q1 Goal (S5QT23): 4-option QuestionScaffold, GoalChoice → GoalType mapping, analytics + SingleChoice answer
- [ ] Q2 Sex (ObxuP): 3 UI options, binary domain Sex enum + null for prefer-not-to-say
- [ ] Q3 Age (owS2i): stepper + slider + keyboard tap-dialog
- [ ] Q3 Age SoftWarning (iNSs8): out-of-range branch from Q3 — wire from current TODO marker
- [ ] Q4 HeightWeight (qXLw8): dual measurement card with cm↔ft, kg↔lb, internal storage metric
- [ ] Q5 Experience (i1Vu9): 4 options Beginner..Athlete (ATHLETE 4th-value preserved)
- [ ] Q6 Context (F16e8): 4 UI options → 3 schema values mapping
- [ ] Q7 DaySession (H0uZ0e): 2 segmented rows (days 3-7, mins 20-60), dynamic summary chip
- [ ] ProfileSummary (u1cEVR): 7 review rows, edit affordance, "Profilimi yarat" CTA persists UserProfile
- [ ] Paywall (ij7jR): 2-option layout invariant (trial + annual), payment-badge row (Apple/Google/m10/Pulpal), transparent billing notice
- **Status:** pending

### Phase 5: Pregnancy + Parental + Account Branch Clean Pass
- [ ] PregnancyNudge (M52XdD): 64dp shield-check tile + 3 benefit rows + INFO privacy — NOT AI-trigger
- [ ] PregnancyConfirm (pqupj): 4 surface1 safe-item cards (mobility/breath/pelvic/stretch) + INFO medical note + "həkim/mütəxəssis" wording — NOT AI-trigger
- [ ] TrimesterPostpartum (C6Ya4A): 1/3 progress + 4 period options (T1/T2/T3/POSTPARTUM), default=T2
- [ ] SafePlan (o0BUd): curated 4-week static template — zero repo deps, hard-stop enforced
- [ ] TodaySafeWorkout (QHsnW): curated 5-row static template — VM routes intents to side-effects only
- [ ] ExerciseDetailPreg (YZ38M): VM-empty-state pattern (`data object State`), ShowToast carries semantic key only
- [ ] SettingsPregMode (vUAuh): surface1 list + 36dp surface2 RoundedCornerShape(10dp) icon tiles + Canvas glyphs
- [ ] ParentalNotice (XG54w): consent gate with checkbox + ZERO analytics until schema approved
- [ ] ParentalBottomSheet (P5mDxB): full-screen Voyager Screen, 0.88 height bottom-anchored card, 0xCC black scrim, swallow taps
- [ ] OfflineBanner (K2TtZa): inline banner pattern (not full screen)
- [ ] SignoutConfirm (SCKUA): 3 ✓ backup-status rows + DestructiveButton (inline danger Button, not VoltButton)
- [ ] DeleteAcc1 (e74FR): destructive intro + BackChevron pattern, no repo calls
- [ ] DeleteAcc2 (GauGs): type-to-confirm "SİL" gate, case-sensitive exact match, no trim, danger-outlined OutlinedTextField
- **Status:** pending

### Phase 6: Localization + Mock Repo + QA Final Pass
- [ ] `grep -rn 'Text("' app/front/mobile/shared/src/commonMain` — every literal must be Strings.<key>; fix violations
- [ ] `grep -rn '"İZLƏ"\|"ÖLÇ"\|"ÇAT"\|"Elm"' app/front/mobile/shared/src/commonMain` — promote to Strings keys (`welcomeEyebrowFood/Energy/Goal`, `welcomePillScience`)
- [ ] StringsAz / StringsRu / StringsEn 3-way coverage diff — every Az key has Ru + En sibling; no empty string values
- [ ] Mock repository sweep: every public method returns `Result.success(...)` or `Result.failure(...)`; never throws; all flows expose `StateFlow` not `MutableStateFlow`
- [ ] `./gradlew :androidApp:clean :androidApp:assembleDebug -x test` — 0 errors, 0 deprecation warnings if possible
- [ ] Deeplink smoke test 6 routes: splash / languageselect / welcome / q1goal / login / authgate — emulator launch + 1.8s sleep + screencap to local disk (no PR commit of screenshots)
- [ ] Final commit: `polish: complete onboarding clean pass — AZ/RU/EN locale, mock repos, 0 hardcode strings`
- [ ] Update Progress Log + write summary
- [ ] Mark feature complete; HALT loop
- **Status:** pending

---

## Decisions

| Date       | Decision | Rationale | Impact |
|------------|----------|-----------|--------|
| 2026-05-28 | Use `refactor` type + 6 sequential phases | Predecessor brought 40/40 screens to 1:1 fidelity; this pass is clean-up, not greenfield | med |
| 2026-05-28 | Android-only testing this pass | User explicit; iOS Koin init is wired (commit 4dad889) but app verification overnight is Android emulator-driven | low |
| 2026-05-28 | Mock backend stays | User explicit; MVP scope; Supabase swap is one-file change via `MockRemoteSource` → `SupabaseRemoteSource` | high |
| 2026-05-28 | Pencil reads via `snapshot_layout` + `batch_get` + `get_variables` only — NEVER `get_screenshot` | Token-burn cost; predecessor already verified visual fidelity | med |
| 2026-05-28 | Per-screen atomic commits, no push | Bisect-friendly; user reviews + pushes in morning | low |
| 2026-05-28 | Banned-term enforcement preserved | CLAUDE.md hard rule — "trainer/coach/məşqçi" → use "mütəxəssis uyğunluq yoxlaması" | high |

## Findings / Current State
<!-- Ralph fills this during Phase 1 audit. One row per issue, one paragraph per architectural finding. -->

## Errors Encountered
<!-- 3-strike protocol: same approach failing 3 times → log full chain, halt loop. -->

| Error | Attempts | Resolution |
|-------|----------|------------|

---

## Progress Log

- 2026-05-28 00:35 — Feature initialized for overnight Ralph-loop autonomous polish pass; predecessor `finalize-mobile-ui-for-onboarding` provides 40/40 1:1 Pencil-fidelity baseline


## Notes for Next Session
- Predecessor: `.claude/features/finalize-mobile-ui-for-onboarding/` — baseline visual fidelity; notes.md line 49 carries the full 42-screen Pencil node-ID inventory
- Build command: `./gradlew :androidApp:assembleDebug -x test`
- Run command: `adb shell am start -W -a android.intent.action.VIEW -d "fitnes://app?devScreen=<id>" org.betech.fitnes.android`
- Active emulator default locale=en-US — for AZ capture, run `adb shell settings put system system_locales az` first
- iOS Koin init wired (commit 4dad889 on main) — leave untouched unless commonMain DI changes leak through `expect/actual`
