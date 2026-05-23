# Feature: finalize-mobile-ui-for-onboarding

## Metadata
- **Type**: feature
- **Branch**: `n/a`
- **Started**: 2026-05-24
- **Current phase**: 1 / 5
- **Overall status**: in_progress
- **Related features**: none

---

## Goal
app_design.pen-dəki bütün onboarding ekranlarını Compose Multiplatform-da hazırlamaq (həmçinin design kit-ə uyğun core elementlər, renk tokenləri və lokalizasiya daxil).


## Context & Constraints

### Project location
**Gradle root:** `app/front/mobile/` (rootProject `Fitnes`, package `org.betech.fitnes`)
- Modules: `:shared` (CMP, iosArm64+iosSimulatorArm64+androidLibrary), `:androidApp`
- Kotlin 2.3.21 · Compose Multiplatform 1.11.0 · Material3 1.11.0-alpha07 · AGP 9.0.1
- iOS app: `iosApp/iosApp.xcodeproj` (framework name: `Shared`, static)
- Bütün Gradle əmrləri `app/front/mobile/`-dən işlədilir.

### What the user asked for
`app/design/mobile/app_design.pen`-dəki **bütün onboarding ekranlarını** (30+ ekran, L2/L3/L5 daxil) Compose Multiplatform-da **production-ready** keyfiyyətdə qurmaq. Backend hələ qoşulmur — repository qatı tam hazırdır, amma Supabase çağırışlarının yerinə **JSON mock data** (object-based fixture) qaytarılır. Səhər istifadəçi ekranları yoxlayacaq → təsdiq olunarsa, backend inteqrasiyası ayrı feature kimi başlayacaq.

### Architecture (Clean Architecture, sıxı)
- **shared/commonMain** layiheteyl təşkilatı:
  - `domain/` — pure Kotlin: `model/`, `repository/` (interface), `usecase/` (single-responsibility)
  - `data/` — `repository/` (impl), `source/local/`, `source/remote/` (mock impl indi), `dto/`, `mapper/`
  - `presentation/` — `<screen>/` qovluqları: `*ViewModel.kt` (Orbit `ContainerHost`), `*State.kt`, `*SideEffect.kt`, `*Intent.kt`, `*Screen.kt` (Composable)
  - `designsystem/` — token-lər (Volt renk sistemi §1b), tipoqrafiya, spacing, `components/` (Button, TextField, Chip, ProgressBar, GoalCard, və s.)
  - `navigation/` — Voyager və ya Decompose ilə tam onboarding nav graph
  - `di/` — Koin module-ları (`domainModule`, `dataModule`, `presentationModule`, `platformModule` expect/actual)
  - `localization/` — moko-resources və ya Lyricist; AZ + RU + EN tam (machine-translated qadağa — placeholder string-lər `// TODO: native review` markerli)
- **Dependency rule:** presentation → domain ← data. Domain heç kimə bağlı deyil. Mock source data layer-də gizlənir — presentation mock-dan xəbərsizdir.

### State management
- **Orbit MVI v10.0.0** (orbit-mvi-kmp skill pattern-i məcburi): `ContainerHost<State, SideEffect>`, `intent { reduce { } / postSideEffect { } }`, `SavedStateHandle` ilə state persistence.
- State immutable + `ImmutableList<T>` (kotlinx.collections.immutable).
- Side effect: yalnız `Navigate`, `ShowError`, `Toast` kimi one-shot eventlər.
- Hər ViewModel üçün orbit-test ilə minimum 1 happy-path testi.

### Mock data strategy
- `data/source/remote/mock/` qovluğunda `*MockSource.kt` fayllar; içəridə `@Serializable object` və ya JSON string fixture.
- `*RepositoryImpl` constructor-da `RemoteSource` interface qəbul edir → mock impl Koin DI-də bind olunur (`single<RemoteSource> { MockRemoteSource() }`).
- Backend dəyişiklik **DI-də 1 sətir** olmalıdır — repository və presentation toxunulmaz qalır.
- Realistik latency simulyasiyası: `delay(300..800ms)` random.

### Build & test
- **Hər iterasiyada hər iki platform build olunmalıdır:**
  - Android: `./gradlew :composeApp:assembleDebug` (və ya layihə struktur-una uyğun module adı)
  - iOS: `./gradlew :shared:linkDebugFrameworkIosSimulatorArm64` (framework link build verification)
  - **İkisi də keçməsə**, iterasiya fail sayılır (3-strike protokoluna daxil).
- **Yalnız Android emulator-da real run + skrinşot test** edilir (codesigning qarışıqlığından qaçmaq üçün).
- iOS üçün yalnız "compiles + framework links" zəmanəti — runtime test səhər manual.

### UX / Design rules
- **Volt renk sistemi** kanonik (CLAUDE.md §1b və project-context.md §1b). Token-lər `designsystem/Color.kt`-dən oxunur. Hardcoded hex qadağa.
- `#E6FF00` (volt) üzərində ağ text qadağa — yalnız `on-volt` `#0E0E0E`.
- `moss` `#A4B82B` CTA kimi işlədilməz (yalnız dekorativ).
- Köhnə turuncu `#FF6B33` hər yerdən təmizlənir.
- AI duygusal kahraman edilmir — copy "öz məşqini idarə et" tonunda (CLAUDE.md doktrinası).
- "Trainer / coach / personal coach" sözü qadağa — yerinə "uyğunluq yoxlaması".
- Onboarding-da Apple 2025 AI Disclosure copy mövcud olmalıdır.
- 7 məcburi sual ≤ 90 san (project-context.md §3); qalan 20 progressive profiling (opsiyonel).
- Accessibility: hər interaktiv element `contentDescription`, min touch target 48dp, dynamic type dəstəyi.

### Lokalizasiya
- AZ (default), RU, EN — üç dil. Hər string `localization/Strings.kt` registry-də. Machine translation **qadağa** — placeholder kimi qoyulur, `// TODO: native review — <context>` markerli.
- Tarix/rəqəm formatları locale-aware (kotlinx-datetime).

### Out-of-scope (bu feature-də toxunulmaz)
- Real Supabase inteqrasiyası (auth daxil)
- Analytics event göndərmə (yalnız interface + no-op impl)
- Push notification setup
- Paywall ekranı (ayrı feature — `finalize-onboarding-flow` davam edir)
- AI plan generation runtime
- Photo calorie capture
- iOS real device test (yalnız framework link verification)
- Performance profiling / R8 optimization
- Unit test coverage backend-bağlı use case-lər üçün (yalnız mock üzərində)

### Library tövsiyələri (CLAUDE.md tech stack ilə uyğun)
- DI: **Koin Multiplatform** (kmp-di skill)
- Navigation: **Voyager** (kmp-navigation skill — sadə onboarding flow üçün ideal)
- Image: **Coil 3** (CMP dəstəyi olan)
- Serialization: **kotlinx.serialization**
- Immutable: **kotlinx.collections.immutable**
- Date: **kotlinx-datetime**
- Lokalizasiya: **moko-resources** (KMP-də sınanmış)
- Testing: **orbit-test** + **kotlin.test** + **turbine** (Flow test)

### Definition of Done (gecə sonu)
1. ✅ `app_design.pen`-də onboarding qrupunda olan **hər ekran** Compose-da mövcud
2. ✅ Hər ekran üçün ViewModel (Orbit) + State + SideEffect + Intent
3. ✅ Repository qatı tam interfaces + mock impl ilə
4. ✅ DI Koin module-ları qurulmuş, `initKoin()` chamarına hazır
5. ✅ Navigation graph tam birləşmiş — onboarding entry-dən paywall trampoline-ə qədər
6. ✅ Volt design system token-ləri + core component-lər
7. ✅ AZ/RU/EN string-lər (placeholder qəbul, `// TODO: native review`)
8. ✅ Android: `assembleDebug` keçir, APK install + emulator skrinşot hər ekran üçün `.claude/features/finalize-mobile-ui-for-onboarding/screenshots/` qovluğunda
9. ✅ iOS: `linkDebugFrameworkIosSimulatorArm64` keçir
10. ✅ `notes.md`-də hər ekran üçün `[impl] <ekran> done` markeri


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

- 2026-05-24 02:00 — Feature initialized


## Notes for Next Session
<!--
  End-of-session handoff — what's blocking, what to do first next time.
  Overwrite this each session; it's a pointer to the NEXT concrete step.
-->

