---
project_name: 'fitnessApp'
user_name: 'Balaagha'
date: '2026-05-23'
version: '0.1'
workflowType: 'prd'
prd_scope: 'pregnancy-postpartum-flow'
phase: 'MVP / Faza 1 — safety-critical'
inputDocuments:
  - CLAUDE.md (Qəti Qadağalar)
  - docs/project-context.md (v3.3 §3.2, §6.3, §10)
  - prd-auth-onboarding-2026-05-22.md (v3.2 §3.8 — hard-stop data qaydası)
  - prd-user-profile-data-catalog-2026-05-22.md (sahə 25)
  - app_design.pen (M52XdD, pqupj, C6Ya4A, o0BUd, QHsnW, YZ38M, vUAuh)
relatedPRDs:
  - prd-auth-onboarding-2026-05-22 (data qaydası schema burada)
  - prd-user-profile-data-catalog-2026-05-22 (sahə 25 katalog)
  - prd-ai-plan-generation (planlanır — hard-stop tətbiqi)
  - prd-workout-execution (planlanır — exercise rendering)
  - prd-settings-deferred-2026-05-22 (Settings entry point)
hardConstraintsRef: 'CLAUDE.md → Qəti Qadağalar #6 + project-context.md §6.3'
uiHandoffReady: false
designReady: true
designRefs:
  - 'app_design.pen: M52XdD (20 · Hamiləlik Nudge — dashboard entry)'
  - 'app_design.pen: pqupj (21 · Hamiləlik — Təsdiq — what we offer)'
  - 'app_design.pen: C6Ya4A (22 · Trimester / Postpartum selection)'
  - 'app_design.pen: o0BUd (23 · Safe 4-week Plan overview)'
  - 'app_design.pen: QHsnW (24 · Today''s Safe Workout list)'
  - 'app_design.pen: YZ38M (25 · Exercise Detail — pregnancy variant)'
  - 'app_design.pen: vUAuh (26 · Settings · Pregnancy Mode management)'
---

# Product Requirements Document — Pregnancy & Postpartum Flow

**Author:** Balaagha · **Date:** 2026-05-23 (v0.1) · **PM Agent:** John (BMad)
**Product:** fitnessApp — Azərbaycan bazarına yönəlmiş fitness tətbiqi
**PRD Scope:** Hamiləlik və postpartum istifadəçilər üçün təhlükəsiz curated workout flow + safety-critical AI plan hard-stop tətbiqi.
**Canonical reference:** `CLAUDE.md` Qəti Qadağalar #6 + `docs/project-context.md` §3.2, §6.3, §10

> **Niyə bu PRD?** `prd-auth-onboarding §3.8` yalnız **data qaydasını** (`pregnancy_postpartum=true` → AI plan bloklanır) bağlayır. Real funksional flow (entry, trimester capture, curated template render, settings management, postpartum auto-archive) ayrı PRD-də tam dizayn olunur. Bu, dizayn-handoff-a hazır olan **ilk full safety-critical flow** sənədidir.

---

## 0. Executive Summary

Hamiləlik və postpartum dövründə fitnessApp **AI tərəfindən generasiya olunan plan vermir** (Qəti Qadağa #6 + WHO/ACOG təhlükəsizlik prinsipləri). Bunun yerinə **3 əl-kürət curated template** (1-ci/2-ci/3-cü trimester + postpartum 4 variant) statik olaraq shared module-da paketlənir, F-only user-lərə post-auth dashboard nudge ilə təklif olunur, və **medical disclaimer** məcburi göstərilir.

Bu PRD üç problemi həll edir:
1. **Safety**: AI plan generasiyası hard-stoplanır, hamilə user təsadüfən təhlükəli plan almır.
2. **Trust**: Sensitive topic-ə yanaşma yumşaq (skip-able nudge, sonradan opt-in), kültürel həssaslıq qorunur.
3. **Funksional value**: User boş qalmasın — curated 4-həftəlik template + trimester-specific exercise modifications.

**Scope:** F-only user-lər, post-auth dashboard nudge → trimester capture → curated 4-week template → daily workout → exercise detail → settings management. Postpartum 12 ay threshold-dən sonra auto-archive.

**MVP minimum:** 7 ekran (artıq dizayn olunub) + 3 static curated template (~50 exercise modification metadata) + 1 schema migration + 5 funksional rule.

---

## 1. Goals & Success Metrics

### 1.1 Business goals

| ID | Goal | KPI | Hədəf |
|----|------|-----|-------|
| G1 | F-user retention (pregnancy cohort) | D30 retention | ≥35% (kişi cohort baseline 28%-dən yüksək — value perception) |
| G2 | Trust signal güclənməsi | App Store review-larda "pregnancy/baby" mention sentiment | ≥4.5/5 (negative review yox) |
| G3 | Safety incident sıfır | medical complaints / 1000 F-user | 0 (launch+12 ay) |
| G4 | Sensitive topic accept rate | Nudge "Bəli, bildir" tap rate | ≥18% (qalanı "İndi yox" və ya dismiss) |

### 1.2 User goals

- **Hamilə user:** "Mənim üçün təhlükəsiz olan hərəkətləri görmək istəyirəm, AI-nın səhv plana məni vurmasından qorxmadan."
- **Postpartum user:** "Doğumdan sonra yumşaq bərpa rejimi istəyirəm, bir-iki həftə sonra normala qayıtmaq üçün."
- **Health-conscious F-user (hələ hamilə deyil):** "Belə bir mod olduğunu bilmək app-ə inamı artırır."

### 1.3 Non-goals (Faza 2-yə deferred)

- Cycle tracking (ciklus izləmə) → ayrı feature
- Pregnancy week-by-week content (haftada bir baby size, fetal development) — başqa app domain
- Lactation/breastfeeding nutrition module
- Pregnancy diet PRD (Faza 2 diet modulu)
- Personalized birth plan / contractions tracker

---

## 2. Personas

Project-context §6 8-cell matrisinə əlavə **D persona** (hamilə/postpartum):

| Persona | Profil | Davranış |
|---------|--------|----------|
| **D1** | İlk hamiləlik, 25-32, 1-ci trimester | Çox ehtiyatlı, az hərəkət, lakin app-i "təhlükəsiz olduğuna əmin olmaq üçün" yoxlayır |
| **D2** | İkinci/üçüncü hamiləlik, 28-38, 2-3 trimester | Daha rahat, "öncə fitnessdə idim, indi sadəcə bərpa istəyirəm" |
| **D3** | Postpartum 0-3 ay | Yorğun, az motivasiya, **kürt qaydaları**: pelvic floor, posture |
| **D4** | Postpartum 3-12 ay | Bərpaya start, "köhnə formama qayıtmaq istəyirəm" — auto-archive trigger gözləyir |

**Persona-cell impact:** AI plan generasiyası bloklanır → 8-cell matrisi bypass olur. Yalnız curated template seçimi (4 variant) işləyir.

---

## 3. Functional Requirements

### 3.1 Entry Point — Dashboard Nudge (FR-P1)

| ID | Tələb | Priority | Reference |
|----|-------|----------|-----------|
| **FR-P1.1** | Post-auth dashboard ilk yüklənməsində, gender=F user-lərə **1500ms delay** sonra `M52XdD` bottom sheet nudge açılır | P0 | data-catalog §sahə 25, design `M52XdD` |
| **FR-P1.2** | Nudge **bir dəfə** göstərilir. "İndi yox" → flag set olmur, **yenidən nudge göstərilmir**, lakin Settings → Profile-də manual opt-in mümkündür | P0 | project-context §3.2 |
| **FR-P1.3** | "Bəli, bildir" tap → `pqupj` (21 · Hamiləlik — Təsdiq) screen açılır | P0 | design `pqupj` |
| **FR-P1.4** | Nudge gender ≠ F user-lərə **heç vaxt** göstərilmir. Backend gender check + frontend conditional render — iki səviyyəli müdafiə | P0 | safety-critical |
| **FR-P1.5** | Nudge analytics: `pregnancy_nudge_shown {gender, age_bucket}`, `pregnancy_nudge_response {action: yes\|no\|dismiss}` | P0 | analytics |

### 3.2 Confirmation Screen (FR-P2)

| ID | Tələb | Priority | Reference |
|----|-------|----------|-----------|
| **FR-P2.1** | `pqupj` ekranı user-ə **NƏ təklif edilir** izah edir: yumşaq mobility, nəfəs, pelvic awareness, stretching — 4 madda kart kimi | P0 | design `pqupj` |
| **FR-P2.2** | Tibbi disclaimer notice **məcburi** görünür: "Hər hansı məşqə başlamadan əvvəl həkim və ya ginekoloqunla məsləhətləş. Bu plan tibbi tövsiyə deyil." | P0 | safety-critical, hüquqi |
| **FR-P2.3** | Primary CTA: "Təhlükəsiz şablonu göstər" → `C6Ya4A` (trimester selection) | P0 | flow design |
| **FR-P2.4** | "AI", "AI plan", "akıllı plan" SÖZLƏRİ qadağa — copy-də "təhlükəsiz şablon", "xüsusi plan", "curated plan" işlədilir | P0 | CLAUDE.md Qəti Qadağa |
| **FR-P2.5** | Secondary helper: "Sonradan Tənzimləmələrdən dəyişdirə bilərsən" — user-ə reversibility hissi verir | P0 | trust |

### 3.3 Trimester / Postpartum Selection (FR-P3)

| ID | Tələb | Priority | Reference |
|----|-------|----------|-----------|
| **FR-P3.1** | `C6Ya4A` ekranı 4 single-select option göstərir: **1-ci trimester (1-12 həftə)**, **2-ci trimester (13-26 həftə)**, **3-cü trimester (27-40 həftə)**, **Postpartum (1-12 ay)** | P0 | design `C6Ya4A` |
| **FR-P3.2** | Seçim user-i `o0BUd` (4-week plan overview) ekranına aparır | P0 | flow |
| **FR-P3.3** | Backend: `user_profiles` cədvəlində yeni sahələr (§5 schema):<br>- `pregnancy_stage: enum('t1'\|'t2'\|'t3'\|'postpartum')`<br>- `pregnancy_start_date: date` (auto-calculate trimester-ə əsasən, user manual edit edə bilər)<br>- `postpartum_start_date: date` (yalnız postpartum stage seçilərsə) | P0 | schema migration |
| **FR-P3.4** | Default seçim YOXDUR — user manual seçməlidir. Default seçim olduqda user-ə "səhv seçim" riskli olur | P0 | safety |
| **FR-P3.5** | Stage dəyişikliyi `pregnancy_postpartum=true` flag-ı dəyişdirmir — yalnız stage update olur. Flag yalnız Settings → "Rejimi söndür" ilə false olur | P0 | data integrity |

### 3.4 Safe 4-week Plan Overview (FR-P4)

| ID | Tələb | Priority | Reference |
|----|-------|----------|-----------|
| **FR-P4.1** | `o0BUd` ekranı user-ə **statik curated template**-i göstərir: 4 həftə, həftədə 3 gün, 20 dəq/sessiya (default) | P0 | design `o0BUd` |
| **FR-P4.2** | Template **stage-specific** seçilir: 4 variant (t1, t2, t3, postpartum-erken[0-3ay], postpartum-orta[3-12ay]). Cəmi **5 static template** (1+1+1+2) | P0 | shared module |
| **FR-P4.3** | 3 metric kart: ümumi həftə sayı, sessiya sayı, orta dəqiqə | P1 | design |
| **FR-P4.4** | Həftəlik 7-day view: rest / workout günlərini göstərir, **bugün** highlight olunur (Volt accent) | P0 | design |
| **FR-P4.5** | "Bu həftənin fokusu" yumşaq card — stage-specific text (məs: 2-ci trimesterdə "postural ayar + diafraqmal nəfəs") | P1 | design |
| **FR-P4.6** | Primary CTA: "Bu günkü məşqi aç" → `QHsnW`. Secondary: "Plan haqqında məlumat" → static info screen (Faza 1.5) | P0 | flow |
| **FR-P4.7** | Hafta keçdikcə template progress saxlanır: `pregnancy_template_progress {week:1-4, day:1-7, completed_at}` jsonb sahəsi `user_profiles`-də | P1 | progress tracking |
| **FR-P4.8** | 4 həftə bitdikdə user-ə "Yenidən başla" və ya "Daha çətin variant" (yalnız 2-ci trimester user-lər üçün) seçimi verilir | P1 | engagement |

### 3.5 Today's Safe Workout (FR-P5)

| ID | Tələb | Priority | Reference |
|----|-------|----------|-----------|
| **FR-P5.1** | `QHsnW` ekranı bugünkü 5-7 exercise-i göstərir: nömrə + icon + ad + dəst/təkrar + opsiyonel modifier badge | P0 | design `QHsnW` |
| **FR-P5.2** | İntro card: "BUGÜN · Aşağı intensivlik" + dəq + hərəkət sayı + RPE 3-5 (low-moderate) | P0 | design |
| **FR-P5.3** | "Pregnancy-safe" rozetə hər siyahıda görünür — trust signal | P0 | design |
| **FR-P5.4** | Hər exercise tap → `YZ38M` (exercise detail) | P0 | flow |
| **FR-P5.5** | Primary CTA: "Məşqi başla" → workout execution mode (workout PRD-də) | P0 | flow |
| **FR-P5.6** | Safety reminder altda: "Hər hansı diskomfortda dayan və həkimlə danış" | P0 | safety |
| **FR-P5.7** | Trimester transition: əgər user `pregnancy_start_date`-ə əsasən stage dəyişməlidirsə (məs t1→t2), bugünkü workout açılmamışdan əvvəl `C6Ya4A` "Trimester yenilənsin?" prompt-i göstərilir | P0 | safety |

### 3.6 Exercise Detail — Pregnancy variant (FR-P6)

| ID | Tələb | Priority | Reference |
|----|-------|----------|-----------|
| **FR-P6.1** | `YZ38M` ekranı standart exercise detail-dən fərqli olaraq **3 əlavə element** saxlayır: **(a) Pregnancy-safe rozet (media üstündə)**, **(b) Trimester modifikasiya card-ı (sarı, accent-soft)**, **(c) "Daha asan variant" link** | P0 | design `YZ38M` |
| **FR-P6.2** | Modifikasiya card-ı stage-specific copy: 2-ci trimesterdə "qarın üstə durmaq olmaz, yan duruşda diz-dirsək pozisiyasında..." | P0 | content |
| **FR-P6.3** | "Diqqət nöqtələri" siyahısı — minimum 3 madda: nəfəs, dayanma siqnalı (alarm), postur | P0 | safety cues |
| **FR-P6.4** | "Alternativ" düyməsi top-bar-da — daha asan variant göstərir (RPE -1 səviyyəsində eyni stage-də başqa exercise) | P1 | flexibility |
| **FR-P6.5** | Video/GIF media `Veo 3.1 Fast` ilə **GENERATE OLUNMUR** — manual çəkim + Mixamo animation (pregnancy-specific 50 exercise üçün) | P0 | cost guard, project-context §1b |
| **FR-P6.6** | "Tamamladım" CTA → progress mark + növbəti exercise | P0 | flow |

### 3.7 Settings Management (FR-P7)

| ID | Tələb | Priority | Reference |
|----|-------|----------|-----------|
| **FR-P7.1** | `vUAuh` ekranı Settings → "Hamiləlik rejimi" entry point-dan açılır | P0 | design `vUAuh` |
| **FR-P7.2** | Status card user-in cari stage, həftə, başlama tarixini göstərir | P0 | transparency |
| **FR-P7.3** | "Standart plan generasiyası dayandırılıb · təhlükəsiz şablon aktivdir" reminder card-ı yumşaq tonda | P0 | trust |
| **FR-P7.4** | 4 action row: (a) Dövrü dəyişdir, (b) Postpartum-a keç, (c) Yumşaq xatırlatma, (d) Həkim qeydləri | P0 | design |
| **FR-P7.5** | "Sil" destructive CTA — pregnancy mode-u söndürür. Confirmation modal məcburi: "Standart plan açılacaq. Davam edək?" | P0 | safety |
| **FR-P7.6** | Pregnancy mode söndürüldükdə `pregnancy_postpartum=false`, `pregnancy_stage=null`, `pregnancy_start_date=null` set olunur. Standart AI plan re-generation §5.2 trigger #3 (profile change) işə düşür | P0 | flow |
| **FR-P7.7** | Tibbi disclaimer reminder altda: "Bu tətbiq tibbi tövsiyə vermir..." | P0 | hüquqi |

### 3.8 Postpartum Auto-archive (FR-P8)

| ID | Tələb | Priority | Reference |
|----|-------|----------|-----------|
| **FR-P8.1** | `postpartum_start_date` + 12 ay keçdikdə **server-side cron** user-ə notification göndərir: "Postpartum dövrün başa çatdı. Standart planına qayıdaq?" | P0 | server cron, FR planlanır |
| **FR-P8.2** | User accept → `pregnancy_postpartum=false`, plan re-generation. User dismiss → flag aktiv qalır, lakin **30 gün sonra** yenidən nudge | P1 | flow |
| **FR-P8.3** | 18 ay keçdikdə server force-archive (flag false). User-ə bildiriş: "Postpartum dövrü avtomatik tamamlandı" | P1 | data hygiene |
| **FR-P8.4** | Auto-archive analytics: `postpartum_autoarchive {user_action: accept\|dismiss\|forced, months_active}` | P1 | analytics |

### 3.9 Edge Cases (FR-P9)

| ID | Tələb | Priority |
|----|-------|----------|
| **FR-P9.1** | **Miscarriage / pregnancy loss**: user Settings → "Rejimi söndür" istifadə edir; app proaktiv soruşmur. Gələcəkdə (Faza 2) yumşaq "Vəziyyət dəyişdimi?" check-in 4 həftə pause-dan sonra | P0 |
| **FR-P9.2** | **Birden çox uşaq (twins, triplets)**: MVP-də fərq yox, eyni curated template. Faza 2 sahə əlavə (`pregnancy_multiples: bool`) | P1 |
| **FR-P9.3** | **Trimester transition mid-week**: workout açılmamışdan əvvəl prompt göstərilir (FR-P5.7) | P0 |
| **FR-P9.4** | **User stage səhv seçər**: Settings-də dəyişdirə bilər; data-da düzəliş əks olunur (template re-seçilir, progress reset olunmur) | P0 |
| **FR-P9.5** | **Gender = M user-i flag set etməyə çalışar (DB injection / API direct)**: backend validation gender=F olmadan flag-ı set etmir, 422 qaytarır | P0 |
| **FR-P9.6** | **High-risk medical condition (PAR-Q+ red flag) + pregnancy = true**: pregnancy hard-stop precedence-i saxlanır (project-context §6.3 modifier precedence: pregnancy > injury > Ramazan...) | P0 |
| **FR-P9.7** | **Onboarding bitmədən hamilə user manual `pregnancy_postpartum=true` set edərsə (test path)**: nudge atlanır, axın birbaşa `pqupj` → `C6Ya4A`-a yönəlir | P1 |

---

## 4. State Machine

```
[Dashboard load (gender=F)]
        ↓ 1500ms delay
[M52XdD nudge]
   ├─ "Bəli, bildir" → [pqupj] → "Təhlükəsiz şablonu göstər" → [C6Ya4A]
   ├─ "İndi yox" → [Dashboard] (Settings entry qalır)
   └─ Dismiss (swipe down) → [Dashboard] (Settings entry qalır)

[C6Ya4A] → trimester/postpartum seç → "Davam et" → set DB flags → [o0BUd]

[o0BUd] (overview)
   ├─ "Bu günkü məşqi aç" → [QHsnW]
   ├─ "Plan haqqında məlumat" → [Static info screen]
   └─ Settings icon → [vUAuh]

[QHsnW] (today's workout)
   ├─ Exercise tap → [YZ38M]
   ├─ "Məşqi başla" → [Workout execution mode (workout PRD)]
   └─ Back → [o0BUd]

[YZ38M] (exercise detail)
   ├─ "Tamamladım" → progress mark → next exercise OR [QHsnW]
   ├─ "Alternativ" → [Alternative exercise variant]
   ├─ "Daha asan variant" → [Same with RPE -1]
   └─ Back → [QHsnW]

[vUAuh] (settings)
   ├─ "Dövrü dəyişdir" → [C6Ya4A]
   ├─ "Postpartum-a keç" → [C6Ya4A pre-selected postpartum]
   ├─ "Sil" → [Confirmation modal] → false flag → [Standart plan re-gen]
   └─ Back → [Settings list]

[Postpartum +12 ay] (server cron)
   → Push notification → "Standart planına qayıdaq?"
   ├─ Accept → false flag → re-gen
   └─ Dismiss → 30 gün pause → repeat (max 2 cycle, sonra force at 18 ay)
```

---

## 5. Data Model (Schema additions)

### 5.1 `user_profiles` cədvəlinə əlavə sahələr

```sql
ALTER TABLE user_profiles ADD COLUMN pregnancy_stage TEXT
  CHECK (pregnancy_stage IN ('t1', 't2', 't3', 'postpartum'));

ALTER TABLE user_profiles ADD COLUMN pregnancy_start_date DATE;

ALTER TABLE user_profiles ADD COLUMN postpartum_start_date DATE;

ALTER TABLE user_profiles ADD COLUMN pregnancy_template_progress JSONB
  DEFAULT '{"current_week": 1, "current_day": 1, "completed_days": []}'::jsonb;

ALTER TABLE user_profiles ADD COLUMN pregnancy_nudge_shown_at TIMESTAMPTZ;
ALTER TABLE user_profiles ADD COLUMN pregnancy_nudge_response TEXT
  CHECK (pregnancy_nudge_response IN ('yes', 'no', 'dismiss'));

ALTER TABLE user_profiles ADD COLUMN postpartum_autoarchive_attempts INT DEFAULT 0;
```

### 5.2 RLS

- `pregnancy_*` sahələrini yalnız user özü oxuya/yaza bilər (`auth.uid() = id`).
- Server-side function `set_pregnancy_stage(stage, start_date)` gender=F validation edir; F olmayan user 422 alır.
- `pregnancy_template_progress` JSONB-i user yaza bilər, lakin schema validation server-side yoxlanılır (week 1-4, day 1-7).

### 5.3 Static template data (shared module)

```kotlin
// shared/src/commonMain/kotlin/com/fitnessapp/pregnancy/PregnancyTemplate.kt
data class PregnancyTemplate(
  val stage: PregnancyStage,
  val weeks: List<TemplateWeek>,
  val focusByWeek: Map<Int, String>,
  val safetyDisclaimer: LocalizedString
)

data class TemplateWeek(
  val weekNumber: Int,
  val days: List<TemplateDay>
)

data class TemplateDay(
  val dayOfWeek: Int,
  val type: DayType, // REST | WORKOUT
  val exercises: List<TemplateExercise>?,
  val durationMin: Int
)

data class TemplateExercise(
  val exerciseId: String, // mapping to exercises table
  val sets: Int,
  val reps: Int?, // null for time-based
  val timeSeconds: Int?, // null for rep-based
  val rpeTarget: Int, // 3-5 for pregnancy
  val modificationNote: LocalizedString, // trimester-specific
  val easierVariantId: String? // optional easier alternative
)
```

5 static template JSON files (təxminən):
- `t1_template.json` (1-ci trimester, çox yumşaq, ~12 exercise)
- `t2_template.json` (2-ci trimester, stabil, ~16 exercise)
- `t3_template.json` (3-cü trimester, hazırlıq, ~10 exercise — daha çox stretching)
- `postpartum_early_template.json` (0-3 ay, pelvic floor focus, ~8 exercise)
- `postpartum_mid_template.json` (3-12 ay, gradual return, ~14 exercise)

Cəmi ~60 unique exercise metadata + 5 templates. Manual content curation + medical reviewer onayı tələb olunur (launch blocker).

---

## 6. NFR (Non-Functional Requirements)

| Kateqoriya | Hədəf | Detal |
|------------|-------|-------|
| **Safety** | 0 medical incident | Curated template medical reviewer onayı (launch blocker), disclaimer hər ekranda |
| **Performance** | Template load <100ms | Static data shared module-da paketli, network call yox |
| **Offline** | 100% offline işləyir | Bütün template data lokal SQLDelight-də, progress lokal write + background sync (FR-24 idempotent) |
| **Privacy** | Pregnancy data PII | RLS strict, analytics PII-siz bucket (məs `gender_bucket`, `pregnancy_stage_bucket` — date YOX) |
| **Compliance** | Apple/Google sensitive health data | Health Declaration form-da "pregnancy support content" deklarasiyası məcburi |
| **Localization** | AZ + RU + EN tam native | MT qadağa; medical disclaimer hər dildə hüquqi review |
| **Accessibility** | VoiceOver/TalkBack tam | "Pregnancy-safe" rozetə alt text "təhlükəsiz hamiləlik rejimi" |
| **Cost** | $0 AI cost | AI plan generation bypass — Veo video yox, manual content |

---

## 7. Out-of-Scope (Bu PRD)

| Out-of-scope | Hand-off |
|--------------|----------|
| Workout execution mode (timer, set tracking, rest cue) | → `prd-workout-execution` |
| Exercise library data + video pipeline | → `prd-exercise-library` (planlanır) |
| AI plan generation (bypass edilir, bu PRD-də yalnız hard-stop tətbiqi göstərilir) | → `prd-ai-plan-generation` |
| Settings list UI (entry point kateqoriyalanması) | → `prd-settings-deferred-2026-05-22` |
| Privacy Policy mətnində pregnancy data clause | → Privacy Policy doc (legal review) |
| Push notification infrastruktur (postpartum auto-archive bunu istifadə edir) | → `prd-notifications` (planlanır) |
| Pregnancy week-by-week content (baby size, week summary) | Faza 2 / başqa app domain |
| Lactation/breastfeeding nutrition | Faza 2 |
| Pregnancy diet PRD | Faza 2 diet modulu |

---

## 8. Dependencies

| Dep | Type | Status | Risk |
|-----|------|--------|------|
| Curated template content (5 file) | Content | YAZILMALI | **Yüksək — medical reviewer onayı launch blocker** |
| Pregnancy-safe exercise media (manual çəkim) | Content/asset | 50 exercise plan | Yüksək — Faza 1.5 |
| Medical reviewer (AZ ginekoloq) | External hire | TƏMİN OLUNMALI | Yüksək — vacib professional review |
| Schema migration (5 sahə) | Backend | YENI | Aşağı |
| `set_pregnancy_stage` server function + RLS | Backend | YENI | Orta |
| Postpartum auto-archive cron (server-side) | Infra | YENI | Orta — Faza 1.5 |
| Push notification SDK | Library | TBD | Aşağı (auto-archive üçün) |
| Privacy Policy clause (pregnancy data) | Content | YAZILMALI | Yüksək — legal |
| Health Declaration form (Google Play) | Admin task | YENİLƏNMƏLİ | Yüksək |

---

## 9. Analytics Events

| Event | Properties | Purpose |
|-------|------------|---------|
| `pregnancy_nudge_shown` | `gender, age_bucket, days_since_signup` | Nudge görünmə rate |
| `pregnancy_nudge_response` | `action: yes\|no\|dismiss, duration_ms` | Accept rate (G4) |
| `pregnancy_stage_selected` | `stage: t1\|t2\|t3\|postpartum, age_bucket` | Stage distribution |
| `pregnancy_stage_changed` | `from_stage, to_stage, source: settings\|prompt` | Transition analysis |
| `pregnancy_template_day_completed` | `stage, week, day, duration_min, exercises_completed` | Engagement |
| `pregnancy_exercise_swapped` | `exercise_id, reason: alternative\|easier` | Modification need |
| `pregnancy_mode_turned_off` | `duration_active_days, reason: settings\|autoarchive` | Lifecycle |
| `postpartum_autoarchive_prompted` | `attempt_number, months_active` | Auto-archive funnel |
| `postpartum_autoarchive_response` | `action: accept\|dismiss, attempt_number` | Acceptance |

PII strict: heç bir tarixinin spesifik dəyəri (`pregnancy_start_date`) analytics-ə getmir, yalnız bucket (`week_bucket`, `months_active`).

---

## 10. Acceptance Criteria (sample)

### AC-P1 — Dashboard Nudge

- Given gender=F user dashboard-a ilk dəfə daxil olur, Then 1500ms sonra `M52XdD` nudge bottom sheet açılır.
- Given gender=M user dashboard-a daxil olur, Then nudge **heç vaxt** açılmır.
- Given F user "İndi yox" tap edir, Then `pregnancy_nudge_response='no'` set olunur, dashboard-da Settings entry qalır, nudge yenidən açılmır.
- Given F user "Bəli, bildir" tap edir, Then `pqupj` ekranı açılır.

### AC-P3 — Trimester Selection

- Given user `C6Ya4A`-da "2-ci trimester" seçir, Then `pregnancy_stage='t2'`, `pregnancy_start_date=today - 19weeks` (approx) set olunur, `o0BUd` açılır.
- Given user heç bir option seçmədən "Davam et" tap edir, Then CTA disabled qalır.
- Given user "Postpartum" seçir, Then `pregnancy_stage='postpartum'`, `postpartum_start_date=today` set olunur (default; user manual edit edə bilər Settings-də).

### AC-P5 — Today's Workout (transition prompt)

- Given user `pregnancy_start_date=today-90days` (t2 sonu) və "Məşqi başla" tap edir, Then prompt göstərilir: "Sən artıq 3-cü trimester-dəsən. Yenilə?"
- Given user prompt-da "Bəli" tap edir, Then `pregnancy_stage='t3'` update olunur, template yenilənir, sonra workout açılır.

### AC-P7 — Settings: Turn off

- Given user `vUAuh`-da "Sil" tap edir, Then confirmation modal açılır: "Standart plan açılacaq. Davam edək?"
- Given user təsdiqləyir, Then `pregnancy_postpartum=false`, `pregnancy_stage=null`, `pregnancy_start_date=null` set olunur, AI plan re-generation §5.2 trigger #3 işə düşür, user dashboard-a qayıdır.

### AC-P9 — Edge: Gender mismatch

- Given gender=M user-in `pregnancy_postpartum=true` set etmək cəhdi (API direct call), Then backend 422 qaytarır: `{"error": "gender_validation_failed"}`.

---

## 11. Open Questions

- [ ] **Medical reviewer kim olacaq?** AZ-da hamiləlik fitness sahəsində kvalifikasiyalı specialist (ginekoloq + fizioterapevt) lazım. Hire / partnership / consultation budget?
- [ ] **5 template hazırlanma müddəti?** Manual content + 50 exercise media (Mixamo / manual çəkim) → təxminən 6-8 həftə.
- [ ] **Postpartum auto-archive copy ton?** "Postpartum dövrün başa çatdı" qəbuledilməz ola bilər (bəzi user-lər hələ rahat hiss etməyə bilər). UX research lazım.
- [ ] **Trimester start date hesablamaq necə?** User manual seçə bilməlidir (date picker) yoxsa estimate yetərlidir? — UX call.
- [ ] **Twins / multiples flag MVP-də vacibdirmi?** Statistically rare (~3% birth) lakin safety açısından sönr-dən vacib ola bilər.
- [ ] **High-risk pregnancy (preeklampsiya, gestational diabetes) flag?** Faza 2-yə deferred amma onboarding-də ümumi `medical_clearance_obtained` field var (data-catalog sahə 26).
- [ ] **Pregnancy mode-da hazır kalori target?** Hamiləlik kalori needs +300-450 kcal (trimester-ə görə) — diet PRD-də olmalıdır, MVP-də skip.

---

## 12. Implementation Phases

### Phase 1 (Launch blocker) — ~4-6 həftə
- Schema migration (5 sahə + RLS)
- `set_pregnancy_stage` server function + gender validation
- 7 ekran (artıq dizayn olunub) implementasiyası (KMP shared + Compose/SwiftUI)
- 5 curated template content (medical review onayı ilə)
- 50 pregnancy-safe exercise modification metadata
- Privacy Policy update + Health Declaration

### Phase 1.5 (post-launch ilk 4 həftə)
- Postpartum auto-archive cron + push notification
- Trimester transition prompt
- Template progress tracking analytics

### Phase 2 (deferred)
- Twins / multiples flag
- Pregnancy diet integration
- Cycle tracking (post-postpartum)
- Week-by-week content (əgər user research dəstəkləyirsə)

---

## 13. Risks & Mitigations

| Risk | Severity | Mitigation |
|------|----------|------------|
| Medical incident (user safe template-ə güvənir, lakin yaralanır) | **CRITICAL** | Disclaimer hər ekranda, medical reviewer onayı, "həkimə danış" reminder, low-RPE limit |
| App Store rejection (sensitive health data) | Yüksək | Health Declaration form düzgün doldur, AI disclosure-da "pregnancy bypass" qeydi |
| User pregnancy loss-dən sonra app-i silər (negative emotion) | Orta | Proaktiv soruşmuruq; user özü Settings-də söndürür; copy həssas |
| Curated template "boring" görünür (AI plan-dan az motivasiya) | Orta | "Bu həftənin fokusu" personalization, milestone progress |
| 5 template kifayət etmir (advanced fit pregnant user "çox asan" deyir) | Aşağı | Faza 2 fitness-level adaptation; MVP-də safety priorit |
| Postpartum auto-archive 12 ay sonra premature (uzunmüddətli bərpa lazım olan user) | Orta | User accept/dismiss seçimi var; force-archive 18 ay-da |
| Translation MT keyfiyyət (RU/EN medical disclaimer) | Orta | Manual review hər lokalizasiya stringi (CLAUDE.md qaydası) |

---

## 14. Changelog

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 0.1 | 2026-05-23 | John (BMad PM) | Initial draft. 7 ekran dizayn olunub (M52XdD, pqupj, C6Ya4A, o0BUd, QHsnW, YZ38M, vUAuh). 5 schema sahəsi əlavə. 5 curated template plan. 4 persona (D1-D4). Postpartum auto-archive 12 ay threshold. |

---

**End of PRD v0.1 — Pregnancy & Postpartum Flow**

*Status:* Design ready (7 ekran `app_design.pen`-də canlıdır). Backend/content review tələb olunur.
*Next:* Medical reviewer engagement → curated template content yazılması → schema migration → KMP implementation.
*Related design refs:* `app_design.pen` PREGNANCY-FLOW · Section (v6HijW); screens M52XdD → pqupj → C6Ya4A → o0BUd → QHsnW → YZ38M; Settings vUAuh.
