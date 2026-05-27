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
