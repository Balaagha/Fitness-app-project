# Notes: polish-onboarding-clean-android
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
2026-05-28 00:35 [impl] feature initialized — Ralph-loop autonomous overnight polish pass (refactor, 6 phases, ~40 screen tasks)
2026-05-28 00:35 [invariant] No Pencil get_screenshot calls — use snapshot_layout + batch_get + get_variables only; predecessor pass already verified 1:1 fidelity for 40/40 screens
2026-05-28 00:35 [invariant] Android-only test — never invoke xcodebuild MCP; iOS Koin init already wired in main (commit 4dad889)
2026-05-28 00:35 [invariant] Mock backend stays — MockRemoteSource symmetric JSON contract; no Supabase calls during this pass
2026-05-28 00:35 [invariant] App brand name is "FitLab" — Strings.appName, identical AZ/RU/EN (not translatable, not localized)
2026-05-28 00:35 [invariant] AI disclosure copy (eQcvv) MUST reach App Store submission unchanged — Apple 2025 review item
2026-05-28 00:35 [invariant] Paywall must always show 2 options (7-day trial + annual default) — single-option layout breaks CLAUDE.md transparent-billing rule
2026-05-28 00:35 [invariant] Pregnancy branch (SafePlan/TodaySafeWorkout/ExerciseDetailPreg/SettingsPregMode) MUST NOT bind to AI use-cases — hard-stop curated static templates only
2026-05-28 00:35 [invariant] Marketing/UX copy banned terms: "trainer", "coach", "məşqçi" — always replace with "mütəxəssis uyğunluq yoxlaması" or "uyğunluq yoxlaması"
2026-05-28 00:35 [invariant] Volt color tokens canonical — #E6FF00 volt, #0A0A0B/#141416/#1E1E21 surfaces, #A4B82B moss, #3DD68C success; old orange #FF6B33 BANNED everywhere
2026-05-28 00:35 [invariant] ParentalNotice + ParentalBottomSheet VMs have ZERO analytics calls until under-18 cohort tracking schema approved
2026-05-28 00:35 [invariant] DeleteAcc2 "SİL" keyword is locale-invariant constant, case-sensitive exact match — not trimmed
2026-05-28 00:35 [invariant] Splash (s7yM8w) Canvas-rendered FitLab hexagon — no raster asset shipped, no static drawable substitute
2026-05-28 00:35 [invariant] Welcome 5 variants are FULLY-DESIGNED alternates not copy swaps — per-variant body composables (BPoymBody/TneydBody/etc) required when modifying
2026-05-28 00:35 [invariant] Half-done screen never committed — single-screen commit only when build green
2026-05-28 00:35 [invariant] Never git push — user pushes after morning review
2026-05-28 00:35 [criteria] Every user-visible Text() reads from Strings.<key> for current locale — string literals in Compose composables = refactor failure
2026-05-28 00:35 [criteria] Domain models (UserProfile/AuthSession/OnboardingProgress) are pure: no @Serializable when Instant/LocalDate present — DTOs own wire format
2026-05-28 00:35 [criteria] Mock RemoteSource returns Result.success() or Result.failure() — never throws; null-safe at boundaries
2026-05-28 00:35 [criteria] Material-icons-extended dep NOT added — Canvas-rendered glyphs (lucide-approximated) acceptable; document intent in KDoc when introduced
2026-05-28 00:35 [criteria] OnboardingNavGraph routes named consistent — either Pencil node ID OR semantic name, never mixed within a graph
2026-05-28 00:35 [criteria] Reusable component param order standard — modifier:Modifier=Modifier first, label/text next, content lambda last, KDoc required
2026-05-28 00:35 [criteria] Q1-Q7 QuestionScaffold contract: progress 1..7 of 7, eyebrow string (e.g. "1/7 · HƏDƏF"), title, options/sliders, no scaffold for post-Q7 ProfileSummary
2026-05-28 00:35 [criteria] Single-screen atomic commit format: `polish(<screen-name>): <one-line summary>` — match predecessor commit style
2026-05-28 00:35 [refs] Pencil onboarding inventory: s7yM8w=splash, tneyd=welcome-soft, X2Pu6z=lang-select, BPoym=welcome-main, dRTLR=welcome-qida, gjmPD=welcome-enerji, OKg7W=welcome-hedef, XG54w=parental-notice, P5mDxB=parental-bottomsheet, u1cEVR=profile-summary, eQcvv=ai-disclosure, K1n7u5=auth-gate, ZJFFO=email-signup, O8lWVO=email-login, zREhj=email-verify, rzAPa=pwd-reset-email, vA9Tb=pwd-reset-form, M52XdD=pregnancy-nudge, pqupj=pregnancy-confirm, C6Ya4A=trimester-postpartum, o0BUd=safe-plan, QHsnW=todays-safe-workout, YZ38M=ex-detail-preg, vUAuh=settings-preg-mode, u27ve=login-err, cYfk5=signup-email-exists, IFSQ3=rate-limit, K2TtZa=offline-banner, a3Vwh6=reset-expired, S5QT23=q1-goal, ObxuP=q2-sex, owS2i=q3-age, iNSs8=q3-age-soft-warn, qXLw8=q4-height-weight, i1Vu9=q5-experience, F16e8=q6-context, H0uZ0e=q7-days-session, ij7jR=paywall, SCKUA=signout-confirm, e74FR=delete-acc-1, GauGs=delete-acc-2, WXUwF=pro-coaching-faza2-SKIP
2026-05-28 00:35 [refs] Build command: `./gradlew :androidApp:assembleDebug -x test`
2026-05-28 00:35 [refs] Deeplink launch: `adb shell am start -W -a android.intent.action.VIEW -d "fitnes://app?devScreen=<id>" org.betech.fitnes.android`
2026-05-28 00:35 [refs] Emulator locale swap: `adb shell settings put system system_locales az` (or `ru` / `en`); persists across cold start
2026-05-28 00:35 [refs] adb screencap multi-display fix: `adb shell screencap -p /sdcard/x.png && adb pull /sdcard/x.png` (pipe form corrupts on multi-display devices like Pixel_Fold)
2026-05-28 00:35 [refs] Cold-start screenshot timing: sleep ≥1.8s after launch to land in Compose splash frame (avoids OS window placeholder)
2026-05-28 00:35 [gotcha] Kotlin 2.3 native: kotlinx.datetime.Clock.System unresolved — use `kotlin.time.Clock` + `@OptIn(ExperimentalTime)` for Instant.now()
2026-05-28 00:35 [gotcha] kotlinx.datetime.Instant typealiased to kotlin.time.Instant in 2.3 — deprecation warnings everywhere, non-blocking
2026-05-28 00:35 [gotcha] Json classDiscriminator="type" applied globally — sealed hierarchies (OnboardingStep/Answer/AnalyticsEvent) must keep @SerialName on every variant
2026-05-28 00:35 [gotcha] CMP Preview import path is `androidx.compose.ui.tooling.preview.Preview` even when dep is `org.jetbrains.compose.ui:ui-tooling-preview`
2026-05-28 00:35 [gotcha] mcp__pencil__get_screenshot returns 2208×1840 PNGs — batching >4 large images at once trips Anthropic 2000px cap (all dropped); HARD BAN this pass anyway
2026-05-28 01:10 [refs] Phase 1 audit complete — 40 in-scope screens (MVI quintet structure uniform), 2 out-of-scope placeholders (home/login), 3 acceptable Text() literals (emoji + numeric range), 6 follow-up TODO markers logged into Findings table
2026-05-28 01:10 [gotcha] Volt vs Pencil hex drift — `danger` is `0xFFFF4D4F` in code but `#FF4D4D` in Pencil (last byte differs); `outlineStrong` is `0xFF3D3D44` in code but `#3E3E44` in Pencil — both are silent bugs that pass visual review because the eye can't distinguish 1-bit channel shifts in dim values
2026-05-28 01:10 [criteria] "modifier first" criterion interpretation — follow Compose API guideline: `modifier: Modifier = Modifier` is the FIRST OPTIONAL parameter (after all required ones), before any defaulted content lambdas. Not "first overall". Current reusables already conform; the gap is QuestionScaffold which has no modifier parameter at all
2026-05-28 01:10 [impl] Mock repo Result-wrap policy — only AuthRepository wraps in `runCatching`; Onboarding/Profile/Question repos throw on JSON decode (caught by Koin scope). Phase 6 sweep will decide: enforce uniform Result-shape OR codify "Auth only wraps" exception
2026-05-28 01:35 [impl] VoltColors / Pencil parity policy — keep `onSurface = #F2F2F2` divergent from Pencil's pure white (deliberate dark-mode softening); align all other tokens 1:1 to Pencil source-of-truth and document the divergence in VoltColors KDoc
2026-05-28 01:35 [impl] `VoltRadius` is the new home for corner-radius tokens — `sm = 12.dp`, `md = 16.dp`, `lg = 24.dp` (mirrors Pencil `$radius-sm/md/lg`). Component-specific exotic radii (e.g. 8dp pills, 20dp option cards) stay inline when they don't appear in the Pencil scale
2026-05-28 01:35 [criteria] Compose `modifier` convention codified — modifier is FIRST optional parameter (after all required positional params), before any defaulted trailing content lambda; QuestionScaffold now follows it, all 15 DS components already did
2026-05-28 01:35 [gotcha] When removing a parameter from a Compose composable always grep callsites first — `DesignSystemPreviewScreen.kt:99` passed `total = 7` to VoltProgressBar; remove silently → build red
2026-05-28 01:55 [criteria] Scrim is `VoltColors.scrim = 0xCC000000` (80% alpha black) — every full-screen modal/dialog overlay reads from this token. Inline `Color(0xCC000000)` is a refactor failure
2026-05-28 01:55 [impl] AuthGate Google "G" glyph uses 4 raw Google brand hex (4285F4 / 34A853 / FBBC05 / EA4335) — this is INTENTIONAL: the colors are external brand assets, not Volt tokens. KDoc on `drawGoogleGlyph()` justifies the inline literals
2026-05-28 01:55 [refs] EmailVerify / EmailSignup / AuthGate share a private `BackChevronButton` composable each — future Phase 6 candidate for promotion to a shared `BackChevron` design-system atom (skipped this pass — predecessor declined; trio costs 12 lines × 3 = 36 lines of harmless duplication)
2026-05-28 02:30 [criteria] Q3Age soft-warning routing — out-of-suggested-range (age < 16 OR age > 65) routes to Q3AgeSoftWarning **instead of** Q4 (not as an extra interstitial). Q3AgeState owns the SOFT_MIN_AGE/SOFT_MAX_AGE constants so the policy lives in one place
2026-05-28 02:30 [impl] Cross-screen range constants pattern — when a sibling screen needs the same range as a primary Q-screen, import the primary's State companion (e.g. `Q3AgeState.MIN_AGE`) rather than re-declaring. Keeps semantic constants in one file even when the UI is split
2026-05-28 02:30 [impl] PRD-revision items belong in KDoc, not `// TODO:` — code TODOs imply "we'll fix this in code"; PRD revisions imply "the contract may change". Use neutral prose ("pending", "until") rather than `TODO:` so they don't pollute future grep sweeps
