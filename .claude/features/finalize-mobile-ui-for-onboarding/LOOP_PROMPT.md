## NIGHT LOOP PROMPT — finalize-mobile-ui-for-onboarding

Aşağıdakı blok-u Claude Code-da yapışdır (YOLO mode-da: `claude --dangerously-skip-permissions --model claude-opus-4-7[1m]`).

---

/loop

Sən onboarding UI-nin **finalize-mobile-ui-for-onboarding** feature-i üzərində autonomous işləyirsən. Bu mesaj LOOP CONTRACT-dır — hər iterasiyada bu qaydaları yenidən oxu.

═══════════════════════════════════════════════════════════════
## CONSTANTS (heç vaxt dəyişmir)
═══════════════════════════════════════════════════════════════

- **Gradle root:** `/Users/balaaghaalihumatov/Desktop/fitnessApp/app/front/mobile`
- **Package:** `org.betech.fitnes`
- **Design source:** `/Users/balaaghaalihumatov/Desktop/fitnessApp/app/design/mobile/app_design.pen` (Pencil MCP məcburi — Read tool ilə **qadağa**)
- **Active feature:** finalize-mobile-ui-for-onboarding
- **Feature spec:** `.claude/features/finalize-mobile-ui-for-onboarding/feature.md` — Context & Constraints + Definition of Done bağlayıcıdır
- **Notes (state of progress):** `.claude/features/finalize-mobile-ui-for-onboarding/notes.md`
- **Screenshots target:** `.claude/features/finalize-mobile-ui-for-onboarding/screenshots/`
- **Android AVD:** `Pixel_Fold_API_35`
- **iOS verification:** sadəcə framework link (`:shared:linkDebugFrameworkIosSimulatorArm64`) — runtime test YOX

═══════════════════════════════════════════════════════════════
## CANONICAL DOCS (hər iterasiyada referansla)
═══════════════════════════════════════════════════════════════

1. `CLAUDE.md` — qadağalar (Volt rəng sistemi, MT qadağası, "trainer" sözü qadağası, AI duygusal kahraman qadağası, Apple AI disclosure)
2. `docs/project-context.md` v3.3 — §1b Volt token cədvəli (kanonik rəng/typography), §3 onboarding 7 məcburi + 20 opsiyonel sahə, §5 plan kontraktı, §6 persona matrisi, §9 free tier
3. `_bmad-output/planning-artifacts/prd-auth-onboarding-2026-05-22.md` — onboarding axını PRD
4. `_bmad-output/planning-artifacts/ux-auth-onboarding-2026-05-22.md` — UX detalları
5. `_bmad-output/planning-artifacts/ux-phase3-design-system-spec-2026-05-23.md` — design system spec
6. `_bmad-output/planning-artifacts/ux-phase4-question-screens-spec-2026-05-23.md` — sual ekranları
7. `_bmad-output/planning-artifacts/ux-onboarding-questions-2026-05-23.md` — sual matrisi
8. `_bmad-output/planning-artifacts/ux-phase2-copy-bank-2026-05-23.md` — AZ/RU/EN copy bank
9. `_bmad-output/planning-artifacts/prd-pregnancy-postpartum-flow-2026-05-23.md` — safety hard-stop
10. `_bmad-output/planning-artifacts/prd-auth-data-model-2026-05-22.md` — mock model field-ləri
11. `_bmad-output/planning-artifacts/prd-auth-onboarding-analytics-2026-05-22.md` — analytics interface (no-op impl)

PRD-ləri yalnız LAZIM olanda oxu (token saxla). Hər ekran-iterasiyasında uyğun bölmə Grep et.

═══════════════════════════════════════════════════════════════
## PHASE PLAN (sıralı, hər faza tam keçməlidir)
═══════════════════════════════════════════════════════════════

### PHASE 0 — Bootstrap (təkrar etmə — notes-də "[impl] phase-0 done" varsa atla)
1. `gradle/libs.versions.toml`-a əlavə et:
   - **Koin Multiplatform** (koin-core, koin-compose, koin-compose-viewmodel)
   - **Voyager** (navigator, screenmodel, transitions)
   - **Orbit MVI** v10.0.0 (orbit-core, orbit-compose, orbit-viewmodel-compose, orbit-test)
   - **kotlinx-serialization-json**
   - **kotlinx-collections-immutable**
   - **kotlinx-datetime**
   - **Coil 3** (coil-compose, coil-network-ktor)
   - **Turbine**
2. `:shared/build.gradle.kts`-i yenilə — commonMain və commonTest dependency-ləri əlavə et, kotlin-serialization plugin-i tətbiq et.
3. `shared/src/commonMain/kotlin/org/betech/fitnes/` altında qovluq skeleton-u yarat:
   - `domain/model/`, `domain/repository/`, `domain/usecase/`
   - `data/source/remote/mock/`, `data/dto/`, `data/mapper/`, `data/repository/`
   - `presentation/onboarding/<each-screen>/`
   - `designsystem/color/`, `designsystem/typography/`, `designsystem/spacing/`, `designsystem/components/`
   - `navigation/`
   - `di/`
   - `localization/`
4. **Volt rəng token-ləri** (project-context.md §1b-dən kopya): `designsystem/color/VoltColors.kt` — bütün ton (volt, on-volt, surface-0/1/2, moss, success, danger, və s.). Hardcoded hex hər yerdən təmizlə.
5. `presentation/App.kt` — root composable, Voyager navigator, MaterialTheme(VoltTheme).
6. `di/initKoin.kt` — `domainModule`, `dataModule`, `presentationModule`, `platformModule` (expect/actual).
7. Build keç: Android `:androidApp:assembleDebug` + iOS `:shared:linkDebugFrameworkIosSimulatorArm64`. İkisi də keçməyincə davam etmə.
8. notes.md: `[impl] phase-0 done — bootstrap deps + skeleton + Volt tokens + Koin init`

### PHASE 1 — Design system core (notes-də "[impl] phase-1 done" varsa atla)
1. `app_design.pen`-də `get_variables` ilə token-ləri çıxar → §1b ilə cross-check.
2. `app_design.pen`-də `search_all_unique_properties` ilə komponent inventarı al.
3. `designsystem/components/`-də yarat (minimum): `VoltButton` (primary/secondary/ghost), `VoltTextField`, `VoltChip`, `VoltProgressBar`, `VoltCheckbox`, `VoltRadio`, `VoltSlider`, `VoltCard`, `VoltAppBar`, `VoltBottomSheet`, `VoltDisclaimer` (AI disclosure / pregnancy stop üçün).
4. Tipoqrafiya: `designsystem/typography/VoltType.kt` — §1b-dəki ölçü/weight cədvəli.
5. Spacing: 4/8/12/16/24/32/48 dp scale.
6. `localization/Strings.kt` — AZ/RU/EN registry skeleton. Hər string-də `// TODO: native review — <context>` markeri.
7. Build keç (Android + iOS).
8. notes.md: `[impl] phase-1 done — design system core`

### PHASE 2 — Mock data + repository qatı (notes-də "[impl] phase-2 done" varsa atla)
1. `prd-auth-data-model-2026-05-22.md` + `prd-user-profile-data-catalog-2026-05-22.md` oxu — sahə siyahısı çıxar.
2. Domain model-ləri: `UserProfile`, `OnboardingAnswer`, `GoalType`, `EquipmentInventory`, `ActivityLevel`, `DietPreference`, `Persona`, `OnboardingStep` və s. — pure Kotlin, `@Serializable`, immutable.
3. Repository interface-ləri `domain/repository/`-də: `UserProfileRepository`, `OnboardingRepository`, `AnalyticsRepository`, `AuthRepository`.
4. `data/source/remote/RemoteSource.kt` interface + `data/source/remote/mock/MockRemoteSource.kt` impl. JSON fixture string-ləri içəridə (`@Language("JSON")` kommenti ilə).
5. `*RepositoryImpl` — RemoteSource constructor parametri, `delay(300..800)` random latency.
6. Koin DI-də `single<RemoteSource> { MockRemoteSource() }` bind — bu yeganə nöqtə backend gələndə dəyişəcək.
7. Analytics interface — impl no-op (`println("analytics: $event")`).
8. Build keç (Android + iOS).
9. notes.md: `[impl] phase-2 done — domain + repos + mock source`

### PHASE 3 — Ekran iterasiyası (HƏR ITERASIYA = 1 EKRAN)
**Bu faza loop-un əsasıdır. ScheduleWakeup ilə özünü təkrarlayır.**

Hər iterasiyada bunları sıra ilə et:

1. **Növbəti ekranı seç:**
   - notes.md oxu → `[impl] <screen> done` markerlərini topla
   - Pencil MCP `batch_get` / `snapshot_layout` ilə `app_design.pen`-də onboarding qrupundakı bütün ekran node-ları siyahısını çıxar
   - Birinci HAZIR OLMAYAN ekranı seç
   - Əgər siyahı boşdursa → STOP CONDITION-a keç

2. **Ekranı oxu:**
   - `mcp__pencil__get_screenshot` — referans şəkli
   - `mcp__pencil__snapshot_layout` — layout struktur
   - Lazımi PRD bölməsini Grep et (ekran adı → uyğun spec)

3. **Implementation:**
   - `presentation/onboarding/<screen>/` qovluğunda 5 fayl:
     - `<Screen>State.kt` — immutable, ImmutableList
     - `<Screen>SideEffect.kt` — `Navigate`, `ShowError`, `Toast` variants
     - `<Screen>Intent.kt` — sealed interface, hər user action
     - `<Screen>ViewModel.kt` — Orbit `ContainerHost`, `intent { reduce { } / postSideEffect { } }`, SavedStateHandle
     - `<Screen>Screen.kt` — Composable, `collectAsState`, VoltTheme tokenləri, design system component-lər
   - `navigation/OnboardingNavGraph.kt`-i yenilə — Voyager-də ekran qeydiyyatı
   - DI module-da ViewModel `factory { ... }` qeydiyyatı

4. **Rule check (qadağaları yoxla):**
   - Hardcoded hex YOX (yalnız VoltColors token)
   - "trainer" / "coach" / "personal coach" sözü YOX
   - AI-nı kahraman edən copy YOX
   - Volt üzərində ağ text YOX
   - Moss CTA kimi işlədilməyib
   - Pregnancy ekranı varsa: hard-stop UI + medical disclaimer

5. **Build:**
   ```bash
   cd /Users/balaaghaalihumatov/Desktop/fitnessApp/app/front/mobile
   ./gradlew :androidApp:assembleDebug :shared:linkDebugFrameworkIosSimulatorArm64
   ```
   İkisi də keçməlidir.

6. **Android run + skrinşot:**
   - Emulator hazırdırsa: `adb wait-for-device` (timeout 30s)
   - Hazır deyilsə: `emulator -avd Pixel_Fold_API_35 -no-snapshot-save &` + 60s timeout
   - APK install: `adb install -r androidApp/build/outputs/apk/debug/androidApp-debug.apk`
   - Activity launch: `adb shell am start -n org.betech.fitnes/.MainActivity` + deep-link nav arg ilə ekrana keç (loop-a hazırlanmış developer-only `?devScreen=<id>` query parameter MainActivity-də handle olunsun — Phase 0-da əlavə et)
   - 3 san gözlə
   - Skrinşot: `adb exec-out screencap -p > .claude/features/finalize-mobile-ui-for-onboarding/screenshots/<screen>.png`
   - Read tool ilə skrinşotu aç → design-a vizual müqayisə (rəng, layout, text)

7. **Yazğı:**
   - notes.md: `[impl] <screen> done — apk: <path>, shot: <path>`
   - Vizual divergens varsa: `[gotcha] <screen> — <konkret problem>` + növbəti iterasiyada yenidən cəhd

8. **Növbəti tur planla:** `ScheduleWakeup(delaySeconds=60, prompt="<bu loop input-u eyni ilə>", reason="next onboarding screen iteration")`

═══════════════════════════════════════════════════════════════
## STOP CONDITION
═══════════════════════════════════════════════════════════════

Bütün bu şərtlər ödənəndə **ScheduleWakeup ÇAĞIRMA** — loop bitir:
- Pencil-də onboarding qrupundakı hər ekran üçün notes.md-də `[impl] <screen> done` markeri var
- Son Android build keçib (`:androidApp:assembleDebug` SUCCESS)
- Son iOS framework link keçib (`:shared:linkDebugFrameworkIosSimulatorArm64` SUCCESS)
- `.claude/features/finalize-mobile-ui-for-onboarding/screenshots/` qovluğunda hər ekran üçün PNG mövcuddur
- feature.md → Progress Log-a `<timestamp> — Phase 3 complete (N screens)` əlavə olunub
- feature.md → Phase 3 status `done`, Phase 4 status `in_progress` markerlənib

═══════════════════════════════════════════════════════════════
## 3-STRIKE PROTOKOLU
═══════════════════════════════════════════════════════════════

Eyni ekran 3 dəfə fail olarsa (build error, runtime crash, vizual divergens 3-cü cəhddə də qalır):
- notes.md: `[gotcha] <screen> SKIPPED — <root cause>`
- feature.md → "Errors Encountered" cədvəlinə əlavə et: `| <error> | 3 | SKIPPED, manual review needed |`
- Növbəti ekrana keç. LOOP-U DAYANDIRMA.

═══════════════════════════════════════════════════════════════
## NOTES COMPACTION
═══════════════════════════════════════════════════════════════

notes.md 250 sətirə çatanda `/feature-compact` çağır → "apply" cavabı ver → davam et.

═══════════════════════════════════════════════════════════════
## INVARIANT QAYDALAR (POZULARSA SƏHV)
═══════════════════════════════════════════════════════════════

- ❌ `.pen` faylına Read/Grep ilə baxma — yalnız Pencil MCP
- ❌ Hardcoded hex rəng yazma — yalnız `VoltColors.*` token
- ❌ Machine-translated AZ string ship etmə — placeholder + `// TODO: native review`
- ❌ "trainer/coach/personal coach" sözü işlətmə — "uyğunluq yoxlaması"
- ❌ Pregnancy ekranında AI plan trigger etmə — hard-stop UI
- ❌ Apple AI Disclosure copy-siz onboarding ekranı ship etmə
- ❌ Hər iterasiyada >1 ekran toplama — scope creep
- ❌ Domain layer-də platform və ya UI import-u
- ❌ Presentation layer-də mock data referansı (yalnız repository interface)
- ❌ Loop-u build fail-də dayandırma — 3-strike protokoluna gir
- ❌ Real Supabase çağırışı yaz — mock fixtures kifayət edir
- ❌ Scope-dan kənar refactor (auth, paywall, AI runtime, photo calorie toxunulmaz)

═══════════════════════════════════════════════════════════════
## SƏHƏR TƏHVİL FORMATı
═══════════════════════════════════════════════════════════════

Loop dayanandan sonra (və ya kullanıcı oyandıqda) feature.md → "Notes for Next Session"-da:
- Bitmiş ekranların sayı / Cəmi
- Skip olunmuş ekranlar (varsa) + səbəbi
- Son Android+iOS build statusu
- Skrinşot qovluğunda nə var (sayı)
- "Manual review needed:" list (vizual divergens flagləri)

═══════════════════════════════════════════════════════════════
## BAŞLA
═══════════════════════════════════════════════════════════════

İndi Phase 0-dan başla. Hər mərhələnin sonunda mütləq build (Android+iOS) keç, sonra notes.md-yə yazğı qoy, sonra ScheduleWakeup ilə növbəti turu planla.
