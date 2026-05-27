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
- [x] Open `mcp__pencil__get_editor_state(include_schema:true)` once; cache schema in this iteration's batch_get calls
- [x] Walk Compose source tree `presentation/onboarding/` — list every screen file + matching VM + matching state class
- [x] For each screen, mark issues found: hardcoded strings, magic dp/sp values, unused imports, dead state fields, missing analytics calls, missing AZ/RU/EN keys, TODO markers, deprecated API usage
- [x] Catalogue reusable components: VoltButton, VoltOptionCard, VoltSlider, VoltMeasurementCard, VoltSegmentedRow, QuestionScaffold, QuestionProgressBar, HexagonLogo, VoltAppBar, VoltTextField, DestructiveButton, BackChevron — record each's parameter signature + KDoc presence
- [x] Review mock repo surface: AuthRepository, UserProfileRepository, OnboardingRepository, AnalyticsRepository, ExerciseRepository — record every method, return shape, success/failure paths
- [x] Write audit table into `## Findings / Current State` below (one row per issue, with screen+severity)
- **Status:** complete (2026-05-28)

### Phase 2: Design System Hardening
- [x] `mcp__pencil__get_variables` — pull full Pencil token table
- [x] Cross-check `VoltColors` hex values against Pencil; log mismatches into Decisions, fix afterwards
- [x] Cross-check `VoltType` size + weight scale against Pencil typography styles
- [x] Cross-check `VoltSpacing` 4/8/12/16/24/32/48 scale against Pencil paddings
- [x] Standardize reusable component APIs: `modifier: Modifier = Modifier` first, content lambdas last, KDoc on every public composable
- [x] Add KDoc to HexagonLogo, VoltOptionCard, VoltMeasurementCard, VoltSegmentedRow, QuestionScaffold — describe purpose + selection state contract + accepted parameter ranges
- [x] Run `./gradlew :androidApp:assembleDebug -x test` — must remain green
- [x] Commit: `polish(designsystem): KDoc + parameter-order standardization`
- **Status:** complete (2026-05-28)

### Phase 3: Auth Flow Clean Pass
- [x] AuthGate (K1n7u5): copy review, Apple/Google/Email/Skip stack, legal footer i18n — clean (Google brand hex is intentional, KDoc justifies)
- [x] EmailSignup (ZJFFO): 4 validation chip live, mock signup → EmailVerify nav, error path — clean (zero hardcode / TODO / banned-term hits)
- [x] EmailLogin (O8lWVO): forgot-pwd link, signup link, mock login error → LoginError nav — clean
- [x] EmailVerify (zREhj): 6-digit OTP overlay, 45s resend cooldown, wrong-email back chevron — clean (Strings.kt drives every visible label)
- [x] PwdResetEmail (rzAPa): email + 15min info disclaimer + send-link mock — clean
- [x] PwdResetForm (vA9Tb): 4 validation chip → navigate to login on submit — clean
- [x] LoginError (u27ve): retry CTA, "şifrəni unutdum" link — clean
- [x] SignupEmailExists (cYfk5): "bu e-poçt artıq qeydiyyatdadır" + login nav — clean
- [x] RateLimit (IFSQ3): cool-down message + try-again CTA — scrim hex `0xCC000000` promoted to `VoltColors.scrim` (token added to design system)
- [x] ResetLinkExpired (a3Vwh6): re-request link CTA — clean
- [x] Build green after each screen; commit per-screen `polish(<screen>): clean pass`
- **Status:** complete (2026-05-28). Only RateLimit needed a code change (scrim token unification); other 9 screens were already structurally clean from predecessor pass and check off without commits.

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
| 2026-05-28 | Phase 2: fix 3 silent hex drifts (`danger 4F→4D`, `outlineStrong 3D→3E`, `onSurfaceMuted 9A→A1`); add 6 missing tokens (`voltPressed`, `voltSoft`, `onSurfaceFaint`, `mossDim`, `dangerSoft`, plus `VoltRadius` sm/md/lg) | Align with Pencil source-of-truth from `get_variables` | med |
| 2026-05-28 | Keep `onSurface = 0xFFF2F2F2` divergent from Pencil's pure `#FFFFFF` | Deliberate softening for long-form dark UI; documented in VoltColors KDoc | low |
| 2026-05-28 | Defer VoltType `lineHeight` ratio adjustment (current 1.25–1.29 vs Pencil 1.15) | Predecessor verified visual fidelity; refactoring lineHeight risks regressions across all 40 screens for a sub-pixel delta | low |
| 2026-05-28 | Remove unused `total: Int?` parameter from `VoltProgressBar` | Dead code suppressed `UNUSED_PARAMETER` warning — clearer surface without it | low |
| 2026-05-28 | Defer uniform `Result<T>` wrapping for non-Auth repos to Phase 6 | Existing pattern is "Auth wraps, foundation reads throw" — needs explicit policy call before mass refactor | low |

## Findings / Current State

### 1. Onboarding screen inventory (40 screens, all MVI-quintet — Intent/State/SideEffect/VM/Screen)

40 onboarding screens present under `presentation/onboarding/`. Two non-onboarding placeholders (`home/HomeScreen.kt`, `login/LoginScreen.kt`) are post-onboarding destinations — out of scope for this refactor (will become Faza 2 entry points). All 40 in-scope screens have the canonical MVI quintet structure.

Pencil node-ID ↔ folder mapping (verified in notes.md `[refs]` line 60):
- splash, languageselect, welcome, aidisclosure, q1goal..q7daysession, q3agesoftwarning, profilesummary, paywall, authgate, emailsignup, emaillogin, emailverify, pwdresetemail, pwdresetform, loginerror, signupemailexists, ratelimit, resetlinkexpired, pregnancynudge, pregnancyconfirm, trimesterpostpartum, safeplan, todaysafeworkout, exercisedetailpreg, settingspregmode, parentalnotice, parentalbottomsheet, offlinebanner, signoutconfirm, deleteacc1, deleteacc2.

### 2. Hardcode-string scan (`Text(".."`) → 3 in-scope hits, all acceptable

| Screen | Line | Literal | Severity | Action |
|--------|------|---------|----------|--------|
| OfflineBannerScreen | 177 | `Text("📵", ...)` | low | OK — emoji glyph, not localizable copy |
| Q3AgeSoftWarningScreen | 129 | `Text("13", ...)` | low | OK — numeric range marker (locale-invariant) |
| Q3AgeSoftWarningScreen | 130 | `Text("90", ...)` | low | OK — numeric range marker (locale-invariant) |
| HomeScreen | 41 | `text = "Home — TODO"` | n/a | out-of-scope placeholder (post-onboarding) |
| LoginScreen | 28 | `text = "Login — TODO"` | n/a | out-of-scope placeholder (post-onboarding) |

Predecessor pass already promoted `"İZLƏ"/"ÖLÇ"/"ÇAT"/"Elm"` → Strings.welcomeV2/V3/V4Title + welcomePillScience etc. **No in-scope user-visible copy literals leak from composables.**

### 3. TODO marker inventory (excluding "TODO: native review" benign markers)

| File | Line(s) | Marker | Resolution path |
|------|---------|--------|-----------------|
| Q3AgeViewModel.kt | 19, 67 | V5 iNSs8 soft-warning routing not wired | Phase 4 (Q3 Age SoftWarning task) — wire `if (age < 16 \|\| age > 65) → SoftWarning` branch |
| Q6ContextViewModel.kt | 20, 21, 23, 77, 81 | 4 UI options collapse to 3 schema values (HOME_ONLY × 2, CASUAL_GYM ← HYBRID) | Documented; PRD revision out-of-scope. Add note clarifying mapping is intentional, demote TODO to KDoc note |
| Q1GoalViewModel.kt | 64; Q1GoalState.kt:14 | Schema may add a strength sub-goal | Documented; PRD revision out-of-scope. Demote TODO to KDoc note |
| DeleteAcc2ViewModel.kt | 41 | `AuthRepository.deleteAccount()` not yet present | Phase 5 (DeleteAcc2) — keep TODO; deferred to Supabase-wired phase |
| LanguageSelectScreen.kt | 77 | `ShowError` side effect ignored — no toast/snackbar surface | Phase 4 (LanguageSelect) — wire to a shared SnackbarHost or in-line dismissible banner |
| AuthGateSideEffect.kt:13, AuthGateViewModel.kt:86, AuthGateScreen.kt:94 | — | `LEGAL_TODO` constant + `legalTodoToast` Strings key — placeholder until legal pages land | OK as-is; canonical pattern for "not-yet-built" toasts |

### 4. Volt vs Pencil token parity (Pencil `get_variables` cross-check)

| Token | Pencil | VoltColors.kt | Severity |
|-------|--------|----------------|----------|
| accent | `#E6FF00` | `volt = 0xFFE6FF00` | ✅ match |
| on-accent | `#0E0E0E` | `onVolt = 0xFF0E0E0E` | ✅ match |
| bg | `#0A0A0B` | `surface0 = 0xFF0A0A0B` | ✅ match |
| surface | `#141416` | `surface1 = 0xFF141416` | ✅ match |
| surface-2 | `#1E1E21` | `surface2 = 0xFF1E1E21` | ✅ match |
| moss | `#A4B82B` | `moss = 0xFFA4B82B` | ✅ match |
| success | `#3DD68C` | `success = 0xFF3DD68C` | ✅ match |
| warning | `#FFB020` | `warning = 0xFFFFB020` | ✅ match |
| border | `#2A2A2E` | `outline = 0xFF2A2A2E` | ✅ match |
| **danger** | `#FF4D4D` | `danger = 0xFFFF4D4F` | ⚠️ **MISMATCH** (4D vs 4F last byte) — fix to `0xFFFF4D4D` |
| **border-strong** | `#3E3E44` | `outlineStrong = 0xFF3D3D44` | ⚠️ **MISMATCH** (3D vs 3E) — fix to `0xFF3E3E44` |
| **ink** | `#FFFFFF` | `onSurface = 0xFFF2F2F2` | ⚠️ MISMATCH — Pencil is pure white; current "dim white" is intentional dark-mode softening. Keep as-is **but document** in VoltColors KDoc |
| **ink-muted** | `#A1A1A8` | `onSurfaceMuted = 0xFF9A9AA0` | ⚠️ MISMATCH (subtle) — align to Pencil `0xFFA1A1A8` |
| text-tertiary | `#6A6A72` | — | **MISSING** — add as `onSurfaceFaint = 0xFF6A6A72` for placeholder/tertiary text |
| accent-pressed | `#C9E000` | — | **MISSING** — add as `voltPressed = 0xFFC9E000` for primary button pressed state |
| accent-soft | `#E6FF001E` | — | **MISSING** — add as `voltSoft = 0x1EE6FF00` for accent chip backgrounds |
| danger-soft | `#FF4D4D1E` | — | **MISSING** — add as `dangerSoft = 0x1EFF4D4D` for inline error chip backgrounds |
| moss-dim | `#5C6B1A` | — | **MISSING** — add as `mossDim = 0xFF5C6B1A` for decorative-disabled state |
| radius-sm / md / lg | 12 / 16 / 24 | — | **MISSING** — add `VoltRadius` object with `sm = 12.dp`, `md = 16.dp`, `lg = 24.dp` (radius scale not yet centralized) |
| font | `"Inter"` | `FontFamily.Default` | ⚠️ Custom font load deferred (already documented in VoltType KDoc) — no action this pass |

**Verdict:** 2 silent hex drift bugs (danger, border-strong), 1 alignment drift (ink-muted), 6 missing tokens needed for hover/pressed/chip states. Phase 2 work.

### 5. Reusable component KDoc + signature audit

| Component | KDoc present | Signature follows Compose `modifier`-first-optional convention | Action |
|-----------|--------------|-----------------|--------|
| VoltButton | ✓ | ✓ | none |
| VoltOptionCard + VoltOptionIconTile | ✓ on `VoltOptionCard`, ✓ on `VoltOptionIconTile`, ✓ on `CheckGlyph` (private) | ✓ (modifier last after all-required params) | none |
| VoltMeasurementCard | ✓ | ✓ | none |
| VoltSegmentedRow | ✓ | ✓ | none |
| VoltSlider | ✓ | needs verification in Phase 2 | verify |
| VoltCard | ✓ | needs verification | verify |
| VoltCheckbox | ✓ | needs verification | verify |
| VoltChip | ✓ | needs verification | verify |
| VoltDisclaimer | ✓ | needs verification | verify |
| VoltRadio | ✓ | needs verification | verify |
| VoltAppBar | ✓ | needs verification | verify |
| VoltBottomSheet | ✓ | needs verification | verify |
| VoltProgressBar | ✓ | needs verification | verify |
| VoltTextField | ✓ | needs verification | verify |
| HexagonLogo | ✓ | ✓ | none |
| QuestionScaffold | ✓ | ⚠️ **MISSING `modifier` parameter entirely** | Phase 2 — add `modifier: Modifier = Modifier` |
| QuestionProgressBar | needs verification | needs verification | verify |

KDoc coverage is broadly strong (all 15 design-system component files contain at least one `/**` block). Phase 2 work: verify the lower-volume components (Slider/Card/Checkbox/Chip/Radio/AppBar/BottomSheet/ProgressBar/TextField) follow the same conventions; backfill missing `modifier` parameter on QuestionScaffold; harden parameter ordering across all reusables.

### 6. Mock repo contract surface (5 domain interfaces × Impl)

| Repository | Methods | Return shape | Throws? | StateFlow exposure |
|------------|---------|--------------|---------|--------------------|
| `AuthRepository` | signInWithEmail · signUpWithEmail · signInWithApple · signInWithGoogle · signOut · observeSession | `Result<AuthSession>` for 4 sign-in methods, `Unit` for signOut, `Flow<AuthSession?>` for observe | ✗ — wraps in `runCatching` | ✓ uses `.asStateFlow()` |
| `OnboardingRepository` | getProgress · saveAnswer · advanceTo · reset · observeProgress | `OnboardingProgress` / `Unit` / `Flow<OnboardingProgress>` | ⚠️ throws on JSON decode failure (no `Result` wrap) | ✓ |
| `UserProfileRepository` | getProfile · saveProfile · observeProfile · setPreferredLanguage · observePreferredLanguage | `UserProfile?` / `Unit` / `Flow<UserProfile?>` / `Flow<String>` | ⚠️ throws on JSON decode failure | ✓ |
| `OnboardingQuestionRepository` | getQuestions | `ImmutableList<OnboardingQuestion>` | ⚠️ throws on JSON decode failure | n/a (no observe) |
| `AnalyticsRepository` | track | `Unit` | ✗ — fire-and-forget print + emit | n/a |
| `RemoteSource` (Mock impl) | 14 methods | `String` / `Flow<String?>` | ✗ — never throws; uses `MutableStateFlow` + `delay(300..800ms)` | ✓ via `.asStateFlow()` |

**Verdict:** Only `AuthRepository` follows the "every public method returns `Result.success/failure`" criterion. The other 3 repos (`Onboarding`/`UserProfile`/`OnboardingQuestion`) throw at the JSON-decode boundary. Per the Phase 6 criterion this is non-conformant; however, the current pattern is "wrap only when failure is user-recoverable" (Auth → can show error toast; the others → app must always succeed cold-start). **Decision deferred to Phase 6** — revisit whether to enforce uniform `Result<T>` shape repo-wide, OR to relax the criterion to "Auth-only `Result`, foundation reads throw (caught by Koin scope handler)."

`MockRemoteSource` is contract-clean: every method completes, never throws, `MutableStateFlow` is internal (only `.asStateFlow()` exposed), JSON-string surface mirrors what a future `SupabaseRemoteSource` would emit. One-file swap path is preserved.

### 7. Other observations

- **No banned-term leaks** — `grep "trainer"\|"coach"\|"məşqçi"` returns only the doctrine comments in `Strings.kt:14` and `AiDisclosureScreen.kt:60` (both KDoc warnings ABOUT the banned terms, not user copy).
- **No legacy orange `#FF6B33`** — `grep` returns only the doctrine comment in `VoltColors.kt:24` (recording the historical removal).
- **MutableStateFlow never leaked externally** from any VM — `grep MutableStateFlow presentation/` returns zero hits, confirming the criterion holds.
- **Build baseline green** — `:androidApp:assembleDebug -x test` finishes in 43s with 0 problems on commit `0376a4e` (clean tree).
- **Localization key surface** — `Strings.kt` is 2515 lines; AZ canonical is fully populated; RU/EN coverage diff deferred to Phase 6 (per plan).

## Errors Encountered
<!-- 3-strike protocol: same approach failing 3 times → log full chain, halt loop. -->

| Error | Attempts | Resolution |
|-------|----------|------------|

---

## Progress Log

- 2026-05-28 00:35 — Feature initialized for overnight Ralph-loop autonomous polish pass; predecessor `finalize-mobile-ui-for-onboarding` provides 40/40 1:1 Pencil-fidelity baseline
- 2026-05-28 01:10 — **Phase 1 complete.** Audit findings written (7 sections: inventory, hardcode scan, TODO inventory, Volt↔Pencil parity, KDoc surface, mock-repo contract, observations). 0 in-scope user-visible string leaks; predecessor cleanup already ate the obvious ones. 2 silent hex drift bugs (danger, border-strong) + 6 missing tokens are the main Phase 2 fix list. Build baseline `:androidApp:assembleDebug -x test` green (43s, 0 problems).
- 2026-05-28 01:35 — **Phase 2 complete.** VoltColors hex drifts fixed (danger, outlineStrong, onSurfaceMuted). 5 new color tokens added (voltPressed, voltSoft, onSurfaceFaint, mossDim, dangerSoft). `VoltRadius` object created mirroring Pencil's sm/md/lg radius variables. `QuestionScaffold` gained the missing `modifier` parameter; `QuestionProgressBar` gained per-composable KDoc; `VoltProgressBar`'s dead `total` parameter removed (+ callsite fix). Build green (23s, 0 problems).
- 2026-05-28 01:55 — **Phase 3 complete.** Audit found 9/10 auth screens already structurally clean (no hardcoded strings, no banned terms, no legacy hex, no orphan TODOs); only RateLimit (IFSQ3) had a raw `Color(0xCC000000)` scrim hex which was promoted to a new `VoltColors.scrim` token (also future-ready for ParentalBottomSheet in Phase 5). Single commit `polish(ratelimit): unify scrim token`.


## Notes for Next Session
- Predecessor: `.claude/features/finalize-mobile-ui-for-onboarding/` — baseline visual fidelity; notes.md line 49 carries the full 42-screen Pencil node-ID inventory
- Build command: `./gradlew :androidApp:assembleDebug -x test`
- Run command: `adb shell am start -W -a android.intent.action.VIEW -d "fitnes://app?devScreen=<id>" org.betech.fitnes.android`
- Active emulator default locale=en-US — for AZ capture, run `adb shell settings put system system_locales az` first
- iOS Koin init wired (commit 4dad889 on main) — leave untouched unless commonMain DI changes leak through `expect/actual`
