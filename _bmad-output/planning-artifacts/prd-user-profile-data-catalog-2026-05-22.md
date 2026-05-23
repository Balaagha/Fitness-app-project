---
project_name: 'fitnessApp'
date: '2026-05-22'
version: '1.0'
workflowType: 'data-catalog'
prd_scope: 'user-profile-data-catalog'
supersedes: 'prd-onboarding-questions-catalog-2026-05-22.md (silindi)'
parent_prd: 'prd-auth-onboarding-2026-05-22.md'
author: 'John (BMad PM) + Mary (BMad Analyst) — birgə imza'
status: 'draft'
language: 'AZ primary; copy AZ/RU/EN'
relatedPRDs:
  - prd-auth-onboarding-2026-05-22.md
  - prd-auth-data-model-2026-05-22.md
  - prd-ai-plan-generation (planlanır)
  - prd-calorie-tracking (planlanır)
  - prd-workout-execution (planlanır)
relatedDocs:
  - docs/project-context.md (v3.3 — §3 onboarding, §5 plan, §6 persona, §7 kalori, §10 hesab silmə)
  - CLAUDE.md (qəti qadağalar)
---

# User Profile Data Catalog

**Author:** John (PM) + Mary (Analyst) · **Date:** 2026-05-22 · **Version:** 1.0
**Sələfi:** `prd-onboarding-questions-catalog-2026-05-22.md` (silindi — bu fayl tam əvəz edir)

---

## 0. Niyə Bu Sənəd?

Köhnə kataloq adı (`onboarding-questions-catalog`) yanıltıcı idi: profil data-sının yalnız **bir hissəsi** onboarding-də toplanır. Reallıq:

- **L1 (7 məcburi):** onboarding `≤90 sn` axında — auth PRD §3.1-də
- **L2 (opsiyonel akkordiyon):** onboarding-də user istəsə əlavə 19 sahə
- **L3 (in-app progressive):** ölçü trekeri açılanda boyun ölçüsü, kalori hədəfi düşənddə target weight, yaralanma günlüyü açılanda VAS ağrı şkalası — **kontekstli triggerlər**
- **L4 (future-room):** Faza 2 sxema rezervi
- **L5 (medical safety):** PAR-Q+ ilhamlı tibbi sual ekranı — AI plan generasiyasını **bloklaya bilər** (yeni qat, köhnə kataloqda yox idi)

Bu sənəd hər field üçün **tək həqiqət mənbəyidir**: (a) nə zaman soruşulur, (b) hara yazılır (DB cədvəli + sütun), (c) hansı feature-i təsir edir, (d) AI plan-a input olub-olmadığı, (e) privacy klassifikasiyası, (f) AZ/RU/EN copy.

**Mənbə fərqi köhnə kataloqdan:**
- Köhnə: 32 sahə (7 + 19 + 6)
- Yeni: **~75 sahə** (7 + 23 + 27 + 8 L4 + 10 L5 medical safety) — internet araşdırması ilə yaralanma, tibbi safety, body composition ölçüləri, davranış sahələri əlavə olundu.

**Out-of-scope:** UI dizayn (Sally), state machine (auth PRD §4), AI plan prompt blueprint detayları (`prd-ai-plan-generation`), paywall.

---

## 1. Toplama Qatları (Where-Asked)

| Layer | Adı | Sahə sayı | Tetik | Skip? | Persistence |
|---|---|---|---|---|---|
| **L1** | Onboarding məcburi | 7 | İlk launch, auth-dan əvvəl | ❌ Yox | `onboarding_state` SQLDelight → `user_profiles` Supabase |
| **L2** | Onboarding opsiyonel akkordiyon | 23 | Q7 sonrası modal: "Tam profil yaratmaq istərdin?" | ✅ Hər ekran skip-able | Eyni |
| **L3** | In-app progressive | 27 | Kontekstli (ölçü trekeri, kalori, foto-kalori, yaralanma günlüyü, Settings) | ✅ Hər trigger dismissable | `user_profiles` + `user_measurements` + `user_injuries` + `user_health_conditions` |
| **L4** | Future-room rezerv | 8 | — | — | DB sxemasında yox (Faza 2 migration) |
| **L5** | Medical safety screen | 10 | İlk AI plan generasiyası tələbində; pregnancy nudge; injury günlüyü | ✅ Skip → curated static template | `user_health_conditions` + `user_medications` |

**Qaida:** L1 + L2 cəmi məcburi cavab sayı ≤ 7 (CLAUDE.md qəti qadağa). L2 tamamilə opsiyoneldir, hər sahə-sahə skip mümkün.

**Modern best practice doğrulaması (zigpoll/userpilot 2026 araşdırma):** Progressive profiling konversiyanı +20%-ə qədər artırır; məcburi onboarding adımları 3-5 ilə məhdudlaşdırılmalıdır. Kontekstual triggerlər bir feature-ə bağlı olduqda opt-in nisbəti əhəmiyyətli artır. → bizim L2/L3 strategiyamızı doğrulayır.

---

## 2. Master Field Cədvəli (TAM — ~75 field)

> **Notation:** Layer = L1/L2/L3/L4/L5 · AI input: ✅ məcburi · 🟡 modifier · ⚪ yox · ⛔ blok · Sensitive: ❌ standart · ⚠️ sensitive · 🔒 GDPR Art.9 health
> **DB target prefix:** `up` = `user_profiles`, `um` = `user_measurements`, `ui` = `user_injuries`, `uh` = `user_health_conditions`, `umed` = `user_medications`, `up_pref` = `user_preferences`

| # | Field | Type | Layer | Where asked | DB target | Validation | Required-if | AI input | Affects | Sensitive |
|---|---|---|---|---|---|---|---|---|---|---|
| 1 | `goal` | enum 3 | L1 | Q1 | up.goal | məcburi | onboarding | ✅ axis | Plan, kalori | ❌ |
| 2 | `gender` | enum 2 | L1 | Q2 | up.gender | məcburi | onboarding | ✅ axis | BMR, persona | ❌ |
| 3 | `age` | int 13-99 | L1 | Q3 | up.age | <13 hard-stop | onboarding | ✅ | BMR | ⚠️ |
| 4 | `height_cm` | int 120-220 | L1 | Q4 | up.height_cm | məcburi | onboarding | ✅ | BMR, BMI | ❌ |
| 5 | `weight_kg` | dec 30-200 | L1 | Q4 | up.weight_kg | məcburi | onboarding | ✅ | BMR, TDEE | ❌ |
| 6 | `experience_level` | enum 3 | L1 | Q5 | up.experience_level | məcburi | onboarding | ✅ modifier | Load.type | ❌ |
| 7 | `context` | enum 3 | L1 | Q6 | up.context | məcburi | onboarding | ✅ axis | Equipment | ❌ |
| 8 | `weekly_days` | int 2-7 | L1 | Q7 | up.weekly_days | məcburi | onboarding | ✅ | Split | ❌ |
| 9 | `session_duration_min` | enum 4 | L1 | Q7 | up.session_duration_min | məcburi | onboarding | ✅ | TDEE | ❌ |
| 10 | `activity_level_daily` | enum 5 | L2 | Body & metabolism qrupu | up.activity_level_daily | opsiyonel; default `lightly_active` | Plan gen | ✅ | TDEE çarpan | ❌ |
| 11 | `target_weight` | dec 30-200 | L2/L3 | Body qrupu / Kalori ekranı | up.target_weight | ±50% current bound | cut/bulk | ✅ (cut/bulk) | Kalori dəqiqliyi | ⚠️ |
| 12 | `target_deadline` | date | L2/L3 | target_weight pair | up.target_deadline | today+30d…today+2y | target_weight ilə | ✅ | Plan tempo | ⚠️ |
| 13 | `target_body_fat_pct` | dec 5-50 | L2/L3 | Body comp ekranı | up.target_body_fat_pct | opsiyonel | bulk/cut adv user | 🟡 | Goal calibration | ⚠️ |
| 14 | `body_fat_visual_estimate` | enum 5 (foto seçimi) | L2/L3 | Body qrupu | up.body_fat_visual | opsiyonel | settings | 🟡 | Goal calibration | ⚠️ |
| 15 | `frame_size` | enum 3 (small/medium/large) | L2 | Body qrupu | up.frame_size | opsiyonel; wrist_cm-dən törəmə | settings | 🟡 | Realistic target weight | ❌ |
| 16 | `motivations` | text[] (canonical) | L2 | Discover qrupu | up.motivations | opsiyonel; multi-select | settings | 🟡 | Trainer voice, copy | ❌ |
| 17 | `discovery_channel` | enum 8 | L2 | Discover qrupu | up.discovery_channel | opsiyonel | settings | ⚪ | Marketing attribution | ❌ |
| 18 | `previous_app_used` | text[] | L2 | Discover qrupu | up.previous_app_used | opsiyonel | settings | ⚪ | Competitive intel | ❌ |
| 19 | `injury_history` | jsonb [] | L2/L3/L5 | Səhhət qrupu / Yaralanma günlüyü | ui.* (ayrı cədvəl) | jsonb schema | İlk plan gen | 🟡 modifier | Hard-stop precedence | 🔒 |
| 20 | `current_pain_vas` | int 0-10 | L3/L5 | Yaralanma günlüyü; gündəlik check-in | ui.current_pain_vas | 0-10 | settings; per-injury | 🟡 | Plan tempo | 🔒 |
| 21 | `movement_restrictions` | text[] canonical | L2/L3/L5 | injury detail | up.movement_restrictions | opsiyonel | İlk plan gen | 🟡 | Exercise filter | 🔒 |
| 22 | `medical_conditions` | text[] canonical (PAR-Q+ ilhamlı) | L5 | Medical safety ekranı | uh.* (ayrı cədvəl) | opsiyonel | İlk plan gen | 🟡 modifier | Hard-stop / disclaimer | 🔒 |
| 23 | `condition_controlled` | jsonb per-condition | L5 | Medical safety follow-up | uh.controlled | opsiyonel; per cond. | medical_conditions varsa | 🟡 | Hard-stop intensity | 🔒 |
| 24 | `medications_current` | text[] | L5 | Medical safety ekranı | umed.* | opsiyonel | settings | 🟡 | Beta-blocker → HR zone | 🔒 |
| 25 | `pregnancy_postpartum` | bool (F-only) | L2/L3/L5 | F user dashboard nudge | up.pregnancy_postpartum | F=true only | F user üçün | ⛔ BLOK | Curated static template | 🔒 |
| 26 | `medical_clearance_obtained` | bool | L5 | Yüksək riskli sual cavabı | up.medical_clearance_obtained | opsiyonel | red flag varsa məcburi | 🟡 | AI plan unlock | 🔒 |
| 27 | `medical_disclaimer_accepted_at` | timestamptz | L5 | Medical safety ekranı | up.medical_disclaimer_accepted_at | məcburi əgər L5 görünürsə | L5 ekranı görünür | — | Hüquqi proof | ⚠️ |
| 28 | `resting_hr` | int 30-200 | L2/L3 | Səhhət qrupu / Settings | up.resting_hr | 30-200 | settings | 🟡 | Future HR-zone | 🔒 |
| 29 | `sleep_h_per_night` | dec 0-14 | L2/L3 | Səhhət qrupu / Day-3 nudge | up.sleep_h_per_night | 0-14 | day-3 nudge | 🟡 | Recovery | 🔒 |
| 30 | `sleep_quality_1_5` | int 1-5 | L4 | — | up.sleep_quality_1_5 | rezerv | — | ⚪ | Future sleep coach | 🔒 |
| 31 | `stress_pss4_score` | int 0-16 | L2/L3 | Səhhət qrupu / Day-3 nudge | up.stress_pss4_score | PSS-4 toplamı | day-3 nudge | 🟡 | Deload suggestion | 🔒 |
| 32 | `smoker` | enum 3 (`never\|former\|current`) | L2 | Lifestyle qrupu | up.smoker | opsiyonel | settings | 🟡 | Cardio caution | 🔒 |
| 33 | `alcohol_freq` | enum 4 | L2 | Lifestyle qrupu | up.alcohol_freq | opsiyonel | settings | ⚪ | Recovery copy | 🔒 |
| 34 | `caffeine_freq` | enum 4 | L2 | Lifestyle qrupu | up.caffeine_freq | opsiyonel | settings | ⚪ | Pre-workout copy | ❌ |
| 35 | `sedentary_hours_per_day` | int 0-16 | L2 | Lifestyle qrupu | up.sedentary_hours_per_day | opsiyonel | settings | 🟡 | Activity nudge | ❌ |
| 36 | `step_goal` | int 1000-30000 | L2/L3 | Settings; daily | up.step_goal | default 8000 | settings | ⚪ | Activity nudge | ❌ |
| 37 | `hydration_goal_ml` | int 500-6000 | L2/L3 | Kalori ekranı | up.hydration_goal_ml | 35ml/kg default | settings | ⚪ | Water tracker | ❌ |
| 38 | `body_temp_typical` | enum 3 (`runs_cold\|normal\|runs_hot`) | L4 | — | up.body_temp_typical | rezerv | — | ⚪ | Future climate adj | ❌ |
| 39 | `eating_disorder_history` | bool | L5 | SCOFF screen (sensitive) | uh.* (flag) | opsiyonel; sensitive consent | settings | 🟡 modifier | Aggressive cut bloku | 🔒 |
| 40 | `scoff_score` | int 0-5 | L5 | SCOFF questionnaire | uh.scoff_score | opsiyonel | settings | 🟡 | Aggressive cut bloku | 🔒 |
| 41 | `diet_pattern` | enum 8 | L2/L3 | Qida qrupu | up.diet_pattern | opsiyonel; Ramazan auto-prompt | settings | ✅ | Light meal, kalori | ⚠️ |
| 42 | `allergies` | text[] canonical | L2/L3 | Qida qrupu / Foto-kalori nudge | up.allergies | opsiyonel | settings | ✅ | Food filter | 🔒 |
| 43 | `food_intolerances` | text[] | L2 | Qida qrupu | up.food_intolerances | opsiyonel | settings | ✅ | Food filter | 🔒 |
| 44 | `disliked_foods` | text[] | L3 | Foto-kalori dislike log | up.disliked_foods | opsiyonel | foto-kalori dislike | ⚪ | Meal suggestion | ❌ |
| 45 | `meal_timing` | jsonb | L2/L3 | Qida qrupu / Settings | up.meal_timing | opsiyonel | settings | 🟡 | Macro split | ❌ |
| 46 | `ramazan_active` | bool | L2/L3 | Mart-aprel auto-nudge | up.ramazan_active | auto-detect | mart-aprel | ✅ modifier | suhoor/iftar split | ⚠️ |
| 47 | `religious_dietary` | enum 4 (`none\|halal_strict\|kosher\|other`) | L2 | Qida qrupu | up.religious_dietary | opsiyonel | settings | ✅ | Food filter | ⚠️ |
| 48 | `equipment_inventory` | text[] canonical (12 item) | L2/L3 | Q6 sonrası avto / Settings | up.equipment_inventory | home_only default `[bodyweight]` | settings | ✅ | Exercise filter | ❌ |
| 49 | `cardio_preference` | enum 3 | L2/L3 | Tərcih qrupu / Plan trigger #5 | up.cardio_preference | opsiyonel | settings | ✅ | Cardio block | ❌ |
| 50 | `hated_exercises` | uuid[] | L2/L3 | Hərəkət "Sevmirəm" log | up.hated_exercises | opsiyonel | in-app log | ✅ | Alternatives | ❌ |
| 51 | `loved_exercises` | uuid[] | L3 | "Favorilərə əlavə et" log | up.loved_exercises | opsiyonel | in-app log | ✅ | Plan priorities | ❌ |
| 52 | `preferred_training_time` | enum 4 | L2/L3 | Tərcih qrupu / Settings | up_pref.preferred_training_time | opsiyonel | settings | ⚪ | Push timing | ❌ |
| 53 | `notification_cadence` | enum 3 | L2/L3 | Tərcih / Push ilk açıl | up_pref.notification_cadence | default `3x` | settings | ⚪ | Push throttle | ❌ |
| 54 | `trainer_voice` | enum 3 | L2/L3 | Tərcih / Settings | up_pref.trainer_voice | F→f, M→m default | settings | ⚪ | TTS voice | ❌ |
| 55 | `trainer_tone` | enum 3 (`motivational\|calm\|drill_sergeant`) | L2/L3 | Tərcih qrupu | up_pref.trainer_tone | default `motivational` | settings | 🟡 | Copy generation | ❌ |
| 56 | `music_during_workout` | bool | L2 | Tərcih qrupu | up_pref.music_during_workout | opsiyonel | settings | ⚪ | UI feature flag | ❌ |
| 57 | `language` | enum 3 (`az\|ru\|en`) | L1 (pre-Q1) | İlk launch | up.language | məcburi | onboarding | ✅ | Localization | ❌ |
| 58 | `unit_system` | enum 2 (`metric\|imperial`) | L2 | Settings | up_pref.unit_system | default `metric` | settings | ⚪ | UI conversion | ❌ |
| 59 | `device_tz` | text | L1 (auto) | İlk launch (system) | up.device_tz | auto-detect | onboarding | 🟡 | Ramazan, push | ❌ |
| 60 | `neck_cm` | dec 25-60 | L3 | Ölçü trekeri | um.neck_cm | 25-60 | settings | ⚪ | Navy body-fat | ⚠️ |
| 61 | `waist_cm` | dec 50-200 | L3 | Ölçü trekeri | um.waist_cm | 50-200 | settings | ✅ (Navy F+M) | Body-fat, WHR | ⚠️ |
| 62 | `hip_cm` | dec 60-180 | L3 | Ölçü trekeri (F) | um.hip_cm | 60-180; F üçün | settings F | ✅ (Navy F) | Body-fat F | ⚠️ |
| 63 | `chest_cm` | dec 60-180 | L3 | Ölçü trekeri | um.chest_cm | 60-180 | settings | ⚪ | Hypertrophy tracking | ⚠️ |
| 64 | `shoulder_cm` | dec 80-200 | L3 | Ölçü trekeri | um.shoulder_cm | 80-200 | settings | ⚪ | Hypertrophy tracking | ⚠️ |
| 65 | `biceps_l_cm` | dec 15-60 | L3 | Ölçü trekeri | um.biceps_l_cm | 15-60 | settings | ⚪ | Hypertrophy tracking | ⚠️ |
| 66 | `biceps_r_cm` | dec 15-60 | L3 | Ölçü trekeri | um.biceps_r_cm | 15-60 | settings | ⚪ | Asymmetry detection | ⚠️ |
| 67 | `forearm_l_cm` | dec 15-50 | L3 | Ölçü trekeri | um.forearm_l_cm | 15-50 | settings | ⚪ | Hypertrophy tracking | ⚠️ |
| 68 | `forearm_r_cm` | dec 15-50 | L3 | Ölçü trekeri | um.forearm_r_cm | 15-50 | settings | ⚪ | Asymmetry detection | ⚠️ |
| 69 | `wrist_cm` | dec 12-25 | L3 | Ölçü trekeri (bir dəfə) | um.wrist_cm | 12-25 | settings | 🟡 | Frame size index | ⚠️ |
| 70 | `thigh_l_cm` | dec 30-90 | L3 | Ölçü trekeri | um.thigh_l_cm | 30-90 | settings | ⚪ | Hypertrophy tracking | ⚠️ |
| 71 | `thigh_r_cm` | dec 30-90 | L3 | Ölçü trekeri | um.thigh_r_cm | 30-90 | settings | ⚪ | Asymmetry detection | ⚠️ |
| 72 | `calf_l_cm` | dec 25-60 | L3 | Ölçü trekeri | um.calf_l_cm | 25-60 | settings | ⚪ | Hypertrophy tracking | ⚠️ |
| 73 | `calf_r_cm` | dec 25-60 | L3 | Ölçü trekeri | um.calf_r_cm | 25-60 | settings | ⚪ | Asymmetry detection | ⚠️ |
| 74 | `body_fat_pct_method` | enum 4 (`navy\|visual\|bia\|dexa`) | L3 | Body fat tracker | up.body_fat_pct_method | default `navy` (Navy formula avail) | settings | 🟡 | Calibration | ⚠️ |
| 75 | `body_fat_pct_value` | dec 3-60 | L3 | Body fat tracker | up.body_fat_pct_value | computed or manual | settings | 🟡 | Goal calibration | ⚠️ |
| 76 | `progress_photos_enabled` | bool | L3 | Progress ekranı | up_pref.progress_photos_enabled | opt-in | settings | ⚪ | Photo UI | 🔒 |
| 77 | `cycle_tracking_opt_in` | bool (F-only) | L4 | — (Faza 2) | up.cycle_tracking_opt_in | rezerv | — | ⚪ | Future menstrual | 🔒 |
| 78 | `last_period_date` | date (F-only) | L4 | — (Faza 2) | up.last_period_date | rezerv | — | ⚪ | Future menstrual | 🔒 |
| 79 | `power_milestones` | jsonb `{squat,bench,deadlift,ohp}` | L3 | Advanced lifter onboarding optional | up.power_milestones | opsiyonel | advanced user | 🟡 | Plan starting weights | ❌ |
| 80 | `event_target` | jsonb `{type, date}` | L3 | Plan trigger #3 (event prep) | up.event_target | opsiyonel | settings | 🟡 | Plan periodization | ❌ |
| 81 | `parental_consent_at` | timestamptz | L1 (13-17 only) | Age gate | up.parental_consent_at | 13-17 məcburi | age 13-17 | — | Hüquqi proof | 🔒 |
| 82 | `ai_disclosure_accepted_at` | timestamptz | L1 (məcburi) | Onboarding intro | up.ai_disclosure_accepted_at | Apple 2025 tələbi | onboarding | — | Hüquqi proof | ⚠️ |
| 83 | `privacy_policy_version_accepted` | text | L1 (auth) | Auth ekranı | up.privacy_policy_version_accepted | məcburi | onboarding | — | Hüquqi proof | ⚠️ |
| 84 | `health_data_consent_at` | timestamptz | L5 / Səhhət qrupu giriş | Health data consent ekranı | up.health_data_consent_at | məcburi əgər L5 vəya L2 health qrupu açılır | health field doldurmaq | — | GDPR Art.9 proof | 🔒 |

**Cəm:** 84 sahə (köhnə kataloqda 32 → +52 yeni sahə). Breakdown:

| Layer | Sayı |
|---|---|
| L1 (məcburi onboarding) | 9 (7 sual sayı + language + device_tz auto) + 3 hüquqi timestamp = 12 |
| L2 (opsiyonel onboarding akkordiyon) | 23 |
| L3 (in-app progressive) | 27 (overlap L2 ilə — eyni sahə iki giriş nöqtəsi) |
| L4 (future-room) | 8 |
| L5 (medical safety) | 10 |

Net unique = ~75 distinct fields (overlaps endirilmiş).

---

## 3. Layer Detalları

### 3.1 L1 — 7 Məcburi (auth PRD referansı)

Tam sual copy AZ/RU/EN + validation kodları + analytics event-ləri **`prd-auth-onboarding-2026-05-22.md`** §3.1.1-§3.1.7-də. Burada təkrarlanmır. Bu kataloqun rolu: hər L1 sahə üçün DB mapping, persona impact, AI input flag.

**Xülasə cədvəl:**

| Q# | Field | Persona rolu | AI input | Validation köhnə |
|---|---|---|---|---|
| Q1 | goal | axis | ✅ | ONB_001 |
| Q2 | gender | axis | ✅ | ONB_002 |
| Q3 | age | modifier (age band) | ✅ | ONB_003 (hard-stop <13), ONB_004 soft warn |
| Q4 | height_cm, weight_kg | — | ✅ | ONB_005, ONB_006 |
| Q5 | experience_level | modifier (load) | ✅ | ONB_007 |
| Q6 | context | axis | ✅ | ONB_008 |
| Q7 | weekly_days, session_duration_min | — | ✅ | ONB_009, ONB_010 |

**Persona-cell resolver** (Q1+Q2+Q6 → 18 cell, default 8 canonical) — auth PRD §6 + project-context §6.

---

### 3.2 L2 — Opsiyonel Onboarding Akkordiyonu (23 sahə, 6 mini-ekran qrup)

**Tetik:** Q7 sonrası modal "Tam profil yaratmaq istərdin? (3-4 dəq, hər vaxt skip)" — auth PRD §3.4-də copy.

**6 qrup:**

#### Qrup A — Aktivlik & Metabolizm (1 sahə)
| # | Field | Niyə soruşuruq (AZ) |
|---|---|---|
| 10 | `activity_level_daily` (enum 5: sedentary/lightly/moderately/very/extra) | "TDEE çarpanı — kalori hədəfin bu qədər dəqiq olar" |

> **Elmi referans:** Mifflin-St Jeor (1990) — `BMR × activity_factor` = TDEE. Çarpanlar: 1.2 / 1.375 / 1.55 / 1.725 / 1.9 (yaygın endokrinolog standartı).

#### Qrup B — Body Composition & Goals (4 sahə)
| # | Field | Niyə soruşuruq (AZ) |
|---|---|---|
| 11 | `target_weight` | "Realist hədəf — plan tempo-suna birbaşa təsir edir" |
| 12 | `target_deadline` | "Hədəf üçün vaxt qoyaq, AI tempo-nu uyğunlaşdırsın" |
| 13 | `target_body_fat_pct` | "Aesthetic hədəf üçün (opsiyonel — bilmirsənsə skip)" |
| 14 | `body_fat_visual_estimate` | "5 referans foto-dan birini seç — tez calibration" |

#### Qrup C — Səhhət (5 sahə — **GDPR Art.9 consent ekranı qabaq**)
| # | Field | Niyə soruşuruq (AZ) |
|---|---|---|
| 19 | `injury_history` (jsonb) | "Sənə zərər verə biləcək hərəkətləri çıxaracağıq" |
| 21 | `movement_restrictions` (canonical: no_overhead/no_squat_deep/no_jump/no_grip_load/no_twist/no_jump_landing/no_plyo) | "Hansı hərəkətdən qaçınmalısan?" |
| 28 | `resting_hr` | "Recovery dəqiqliyi üçün (sabah səhər ölç)" |
| 29 | `sleep_h_per_night` | "Yuxu plan tempo-suna təsir edir" |
| 31 | `stress_pss4_score` (PSS-4 — 4 sual, 0-16 toplam) | "Stress səviyyəsi recovery-ə təsir edir" |

> **PSS-4 (Cohen 1983, validated):** 4 sual, 5-point Likert (0-4); total 0-16. >9 = high perceived stress → deload suggestion.

**Consent copy (AZ):** "Aşağıdakı suallar sağlamlıqla bağlıdır (GDPR special category data). Cavabların **yalnız** sənin planını qurmaq üçündür — heç vaxt üçüncü tərəflə paylaşılmır. Sən hər vaxt Settings-də silə bilərsən." + Checkbox "Anladım, davam et" → `health_data_consent_at` yazılır.

#### Qrup D — Lifestyle (4 sahə)
| # | Field | Niyə (AZ) |
|---|---|---|
| 32 | `smoker` (never/former/current) | "Cardio capacity-yə təsir edir" |
| 33 | `alcohol_freq` (never/occasional/weekly/daily) | "Recovery copy üçün" |
| 34 | `caffeine_freq` | "Pre-workout tövsiyəsi üçün" |
| 35 | `sedentary_hours_per_day` | "Oturaq iş varsa activity nudge dəyişir" |

#### Qrup E — Qida (5 sahə)
| # | Field | Niyə (AZ) |
|---|---|---|
| 41 | `diet_pattern` | "Yerli yemək tövsiyələri üçün" |
| 42 | `allergies` (canonical: nuts/dairy/gluten/eggs/shellfish/soy/custom) | "Foto-kalori və meal filter üçün" |
| 43 | `food_intolerances` | "Allergi olmasa da intolerans varsa filter edək" |
| 45 | `meal_timing` (jsonb) | "Macro split-i sənin günündə uyğun qoyacağıq" |
| 47 | `religious_dietary` (none/halal_strict/kosher/other) | "Halal-strict isə daha sıxı filter" |

**`diet_pattern` enum 8:** `omnivore` / `vegetarian` / `vegan` / `pescetarian` / `keto` / `mediterranean` / `halal_strict` / `ramazan_active`

#### Qrup F — Tərcih & Discover (8 sahə)
| # | Field | Niyə (AZ) |
|---|---|---|
| 16 | `motivations` (multi-select: aesthetic/strength/health/event_prep/stress_relief/social/longevity) | "Səni nə hərəkətə gətirir?" |
| 17 | `discovery_channel` (app_store/instagram/tiktok/youtube/friend/google/influencer/other) | "Bizi haradan tapdın? (marketing üçün)" |
| 48 | `equipment_inventory` (canonical 12 item) | "Hansı avadanlığın var? home_only-də auto bodyweight" |
| 49 | `cardio_preference` (hiit/steady/none) | "AI plan cardio blokunu sənə görə qurar" |
| 52 | `preferred_training_time` | "Bildirişləri sənin saatına uyğun göndəririk" |
| 53 | `notification_cadence` | "Nə qədər tez-tez xəbərdar edək?" |
| 54 | `trainer_voice` | "TTS səs tərcihi (F/M/neutral, AZ accent)" |
| 55 | `trainer_tone` | "Motivasion / sakit / sərt" |

**`equipment_inventory` canonical 12:** `bodyweight`, `dumbbells`, `barbell`, `bench`, `pullup_bar`, `bands`, `kettlebell`, `cables`, `machines`, `treadmill`, `bike`, `rower`

---

### 3.3 L3 — In-app Progressive Trigger Matrisi

L2-də skip edilən sahələr kontekstli triggerlərlə soruşulur. Hər trigger **modal / inline bottom-sheet**, dismissable.

| Trigger ekranı | Soruşulan sahə(lər) | Dismiss | Re-prompt |
|---|---|---|---|
| İlk plan generasiyası | `injury_history`, `movement_restrictions` → əgər red flag → L5 escalate | "Sonra" → settings badge | 30 gün sonra |
| Kalori hədəfi ekranı ilk açılır | `target_weight`, `target_deadline`, `activity_level_daily` | Skip → badge | 14 gün sonra |
| Foto-kalori ilk istifadə | `allergies` | Skip → badge | Hər match-də inline |
| Foto-kalori dislike tap | `disliked_foods` (auto-append) | "Geri al" 5 san | Tap-based |
| Day-3 retention | `sleep_h_per_night`, `stress_pss4_score` (PSS-4 mini-quiz) | Day-7 təkrar | Max 2 dəfə |
| Day-7 retention | `body_fat_visual_estimate` | Settings → badge | Max 1 dəfə |
| Plan re-gen trigger #5 | `cardio_preference` | Settings | 30 gün |
| Hərəkət detalı "Sevmirəm" | `hated_exercises` auto-log | "Geri al" 5 san | Tap |
| Hərəkət detalı "Sevirəm" | `loved_exercises` auto-log | Tap | — |
| Ölçü trekeri açılır | `neck_cm`, `waist_cm`, `hip_cm` (F), `wrist_cm` (bir dəfə) — Navy body-fat üçün | "Sonra" | Aylıq nudge |
| Ölçü trekeri "Detallı ölçü" | `chest_cm`, `shoulder_cm`, `biceps_l/r_cm`, `forearm_l/r_cm`, `thigh_l/r_cm`, `calf_l/r_cm` | User-controlled | N/A |
| Body fat tracker | `body_fat_pct_method`, `body_fat_pct_value` | Settings | — |
| Settings → Profil tamamlama | Bütün L2-də skip edilmiş sahələr | User-controlled | N/A |
| Ramazan auto-detect (mart-aprel TZ-based) | `ramazan_active=true`, `meal_timing` suhoor/iftar | "Bu il yox" | 1 dəfə / il |
| Push notification ilk açıl | `notification_cadence`, `preferred_training_time` | Default qalır | Bir dəfə |
| Settings → Avadanlıq | `equipment_inventory` genişləndirmə | User | N/A |
| Post-auth dashboard (F-only, 1500ms delay) | `pregnancy_postpartum` (sensitive copy) | "Sonra" → Settings | Bir dəfə (yenidən soruşulmur) |
| Settings → Səs | `trainer_voice`, `trainer_tone` | User | N/A |
| Yaralanma günlüyü | `current_pain_vas` (VAS 0-10), per-injury status update | Skip | Gündəlik check-in |
| Advanced user onboarding L2 (experience=advanced) | `power_milestones` (squat/bench/deadlift/ohp 1RM) | Skip | Settings |
| Plan trigger #3 (event prep) | `event_target` (event type + date) | Skip | — |
| Body composition modülü | `frame_size` (wrist_cm-dən törəmə hesablanır) | Auto | — |
| Progress photos toggle | `progress_photos_enabled` (opt-in) | Default off | Settings |

**Profile completion rozeti:** Settings-də progress bar `(filled_optional_count / 23) × 100`. %100 olduqda gizlənir.

---

### 3.4 L4 — Future-room Rezerv

Bu sahələr **MVP DB sxemasında yoxdur**, Faza 2 migration-da əlavə olunur.

| # | Field | Niyə rezerv | Faza 2 feature |
|---|---|---|---|
| 30 | `sleep_quality_1_5` | PSQI-ilhamlı quality skor | Sleep coaching |
| 38 | `body_temp_typical` | Climate-adjusted plan | Future climate adj |
| 77 | `cycle_tracking_opt_in` (F-only) | Menstrual cycle integration | Faza 2 cycle tracking |
| 78 | `last_period_date` (F-only) | Cycle phase plan adjust | Faza 2 cycle tracking |
| L4a | `wearable_device_id` | Wearable inteqrasiyası | Faza 2 wearable |
| L4b | `vo2_max` | Cardio fitness skor | Faza 2 HR-zone |
| L4c | `hrv_baseline` | Recovery sensor | Faza 2 wearable |
| L4d | `dietary_restrictions_detail` | diet_pattern üstündə genişlənmiş list | Faza 2 diet modulu |

**Sxema strategiyası:** L4 sahələri MVP migration-da yoxdur. `ALTER TABLE … ADD COLUMN … NULL` Faza 2-də. MVP kodu bu sahələrə referans verməməlidir.

---

### 3.5 L5 — Medical Safety Screen (yeni qat — PAR-Q+ ilhamlı)

**Niyə yeni qat:** Köhnə kataloq yalnız `injury_history` (jsonb) ilə kifayətlənirdi. İnternet araşdırması (PAR-Q+ 2024, ACSM Pre-Participation Screening) göstərdi ki, **medical clearance** suallar AI plan-ı **bloklamalı** ola bilər. Bu qat tibbi məsuliyyət-ni transparent edir.

**Tetik:** İlk AI plan generasiyası tələbi (manual button / auto-gen). 7 sual + 3 follow-up.

**Sual seti (PAR-Q+ 2024 ilhamlı, AZ adapted):**

| # | Field | Sual (AZ) | Yes → davranış |
|---|---|---|---|
| 22a | `medical_conditions` checkbox | "Həkim sənə ürək xəstəliyi və ya yüksək təzyiq olduğunu deyibmi?" | Follow-up §6 |
| 22b | eyni | "İstirahətdə və ya fiziki aktivlik zamanı sinəndə ağrı hiss edirsən?" | Hard-stop intensity → orta load |
| 22c | eyni | "Son 12 ayda baş gicəllənmə və ya huşunu itirmisənmi?" | Hard-stop → medical_clearance tələbi |
| 22d | eyni | "Sənə xroniki tibbi xəstəlik (artrit, osteoporoz, xərçəng, diabet, böyrək, ağciyər) diaqnozu qoyulubmu?" | Follow-up §22e |
| 22e | `medications_current` | "Xroniki xəstəlik üçün dərman qəbul edirsənmi?" | Beta-blocker → HR zone disable |
| 22f | `injury_history` | "Fiziki aktivliyə təsir edə biləcək sümük/oynaq problemin var?" | injury detail ekranı |
| 22g | `medical_conditions` | "Həkim sənə fiziki aktivlikdən qaçınmağı tövsiyə edibmi?" | **HARD-STOP** → curated static template |

**Follow-up suallar (hər YES üçün):**
- 23a: `condition_controlled` — "Xəstəliyini dərmanla/həkim nəzarəti altında saxlaya bilirsən?" (yox → əlavə xəbərdarlıq)
- 26: `medical_clearance_obtained` — "Həkim sənə fiziki aktivlik üçün icazə veribmi?" (yox → AI plan blok)

**Pregnancy axını (ayrı, sahə 25):** F-only post-auth dashboard nudge, sensitive copy. Yes → **HARD-STOP** (CLAUDE.md), curated static prenatal template + medical disclaimer.

**SCOFF screening (sahələr 39, 40 — opsiyonel, AZ kültür sensitiv):**
- 5 sual (Sick/Control/One stone/Fat/Food)
- ≥2 yes → aggressive cut bloku, "həkim ilə danış" disclaimer
- AZ kültür adapter: copy daha incə, "yeyəcək davranışı" termini

**Disclaimer və hüquqi:**
- `medical_disclaimer_accepted_at` məcburi əgər L5 ekranı görünür
- Copy (AZ): "Bu app **tibbi cihaz deyil** və həkim məsləhətini əvəz etmir. Hər hansı tibbi şərt varsa **həkim ilə danış**."
- Apple Health Declaration + Google Health Declaration üçün məcburi proof

> **Kritik dil seçimi (CLAUDE.md):** Trainer onay üçün "**uyğunluq yoxlaması**" (safety-fit review) — heç vaxt "təsdiq/approval" sözü işlədilmir.

---

## 4. Bədən Ölçüləri Detalı

**Mənbə:** Hevy app body measurement set + US Navy formula (Hodgdon-Beckett) tələbi.

| Field | AZ copy | RU | EN | Vahid | Niyə | Məcburi? | Retest |
|---|---|---|---|---|---|---|---|
| `weight_kg` | "Çəki" | "Вес" | "Weight" | kq | BMR, progress | L1 məcburi | Həftəlik |
| `height_cm` | "Boy" | "Рост" | "Height" | sm | BMR, BMI | L1 məcburi | Bir dəfə |
| `neck_cm` | "Boyun çevrəsi" | "Окружность шеи" | "Neck" | sm (Adam alma altından) | Navy body-fat (M+F) | Body-fat üçün | Aylıq |
| `waist_cm` | "Bel çevrəsi" | "Талия" | "Waist" | sm (M göbək, F ən dar) | Navy body-fat, WHR | Body-fat üçün | Aylıq |
| `hip_cm` | "Kalça çevrəsi" | "Бедра" | "Hip" | sm (ən geniş) | Navy F formula, WHR | Body-fat üçün (F) | Aylıq |
| `wrist_cm` | "Bilək çevrəsi" | "Запястье" | "Wrist" | sm | Frame size index | Bir dəfə | — |
| `chest_cm` | "Döş çevrəsi" | "Грудь" | "Chest" | sm | Hypertrophy | Opsiyonel | Aylıq |
| `shoulder_cm` | "Çiyin çevrəsi" | "Плечо (через дельты)" | "Shoulder" | sm | Hypertrophy | Opsiyonel | Aylıq |
| `biceps_l/r_cm` | "Biceps (sol/sağ)" | "Бицепс (л/п)" | "Bicep (L/R)" | sm | Hypertrophy + asymmetry | Opsiyonel | Aylıq |
| `forearm_l/r_cm` | "Saidə (sol/sağ)" | "Предплечье (л/п)" | "Forearm (L/R)" | sm | Hypertrophy + asymmetry | Opsiyonel | Aylıq |
| `thigh_l/r_cm` | "Bud (sol/sağ)" | "Бедро (л/п)" | "Thigh (L/R)" | sm | Hypertrophy + asymmetry | Opsiyonel | Aylıq |
| `calf_l/r_cm` | "Baldır (sol/sağ)" | "Икра (л/п)" | "Calf (L/R)" | sm | Hypertrophy + asymmetry | Opsiyonel | Aylıq |

**Default retest cadence:** aylıq (yumşaq nudge). User Settings-də həftəlik/2-həftəlik/aylıq seç bilər.

**US Navy formula (sahə 75 `body_fat_pct_value` auto-compute):**
- M: `BF% = 86.010 × log10(waist − neck) − 70.041 × log10(height) + 36.76`
- F: `BF% = 163.205 × log10(waist + hip − neck) − 97.684 × log10(height) − 78.387`

Accuracy: ±3-4% DEXA-ya qarşı (acceptable for trend tracking).

---

## 5. Yaralanma & Ağrı Detalı

User xüsusi vurğuladı — bu bölmə **çox kritikdir**.

### 5.1 Yaralanma kataloqu (canonical list, anatomik bölgələrə görə)

| Bölgə | Yaralanma | Bloklanan hərəkətlər |
|---|---|---|
| **Bel/Onurğa** | Lumbar disk hernisi | heavy squat, deadlift (heavy), good morning, bent-over row (heavy), jumping |
| | Spondylolisthesis/pars defect | back extension (loaded), heavy axial load |
| | Akut bel ağrısı | bütün axial load (1-2 həftə) |
| **Boyun/Servikal** | Disk problemi | overhead press (heavy), behind-neck pulldown, neck bridge |
| **Çiyin** | Rotator cuff (tear/tendinopati) | overhead press, lateral raise (heavy), upright row, dips, bench (wide grip) |
| | İmpinjment | overhead press, bench dips |
| | AC joint | bench press (heavy), push-up (deep), front raise |
| **Dirsək** | Tennis elbow (lateral epicondylitis) | reverse curl, wrist extension load, pull-up (chin under) |
| | Golfer's elbow (medial) | wrist curl, hammer curl (heavy) |
| **Bilək** | Carpal tunnel / sprain | push-up (flat hand → fist alt), front squat (rack pos), heavy grip |
| **Diz** | ACL re-construction post-op | jumping, plyometric, deep squat (>parallel), pivoting, BFR-only allowed first 12 weeks |
| | Meniscus | deep squat, lunge (deep), pivoting |
| | Patellofemoral pain | step-up (high), deep squat, lunge (deep) |
| | IT band syndrome | running (high mileage), lateral squat |
| **Topuq/Ankle** | Sprain (akut/sub-akut) | jumping, lateral movement, single-leg balance (heavy) |
| | Achilles tendinopati | sprint, jumping, heavy calf raise |
| **Kalça/Hip** | Labral tear | deep squat, lunge (deep), pigeon stretch |
| | Bursit | side-lying hip abduction (heavy) |
| **Qarın divarı** | Hernia | heavy compound, valsalva-heavy lifts |

### 5.2 Yaralanma severity & status

| Field | Type | Dəyər |
|---|---|---|
| `injury_history[].body_region` | enum | yuxarıdakı bölgələr |
| `injury_history[].injury_type` | enum | rotator_cuff / disc / acl / meniscus / ... |
| `injury_history[].severity` | int 1-3 | 1=mild, 2=moderate, 3=severe |
| `injury_history[].status` | enum | `acute` (<6 həftə) / `subacute` (6-12 həftə) / `chronic` (>12 həftə) / `resolved` |
| `injury_history[].active` | bool | hazırda aktiv ağrı varmı |
| `injury_history[].vas_pain` | int 0-10 | VAS hazırki ağrı |
| `injury_history[].healed_at` | date | resolved ise tarix |
| `injury_history[].medical_clearance` | bool | həkim icazəsi varmı |
| `injury_history[].notes` | text (200 char) | user qeydi |

### 5.3 VAS Pain Scale (sahə 20 `current_pain_vas`)

- **0-3:** mild — normal training davam
- **4-6:** moderate — intensity 20% azalt, problematik hərəkətləri skip
- **7-10:** severe — **HARD-STOP**, "həkim ilə danış" disclaimer, AI plan rest day önərir

**Tetik:** Yaralanma günlüyü açılır + active=true injury varsa hər workout-dan əvvəl mini "Ağrı necədir?" sualı (0-10 slider, ≤3 san).

### 5.4 Medical clearance flow

Əgər user severity=3 və ya status=acute injury qeyd edirsə:
1. Modal: "Bu yaralanma üçün həkim icazəsi var?"
2. Yes → `medical_clearance_obtained=true` + AI plan blokdan azad
3. No → `medical_clearance_obtained=false` + curated rehab template (passive — 1-2 əl-seçimli static template, AI yox)

---

## 6. Sağlamlıq Sualları (L5 + L2 hibrid detayı)

### 6.1 PAR-Q+ 2024 ilhamlı sual seti

7 əsas sual yuxarıda §3.5-də cədvəldə. Hər YES → follow-up. Follow-up algoritmi:

```
YES sayı = 0 → AI plan unlock
YES sayı = 1 (heart/CV)            → medical_clearance tələbi
YES sayı = 1 (chronic controlled)  → AI plan unlock + warning copy
YES sayı = 1 (chronic uncontrolled)→ medical_clearance tələbi
YES sayı ≥ 2                       → medical_clearance tələbi
"avoid physical activity" YES      → HARD-STOP, curated static template only
Pregnancy YES                       → HARD-STOP (CLAUDE.md), prenatal curated template
```

### 6.2 Dərmanlar (sahə 24)

Canonical list (text[] checkbox-lar):
- `beta_blocker` → ürək tezliyi enmə, HR-zone cardio disable
- `corticosteroid` → əzələ kayba potensialı, recovery copy
- `stimulant` (ADHD med) → caffeine warning
- `blood_thinner` → kontakt sport / yıxılma riski warning
- `insulin` (T1/T2 diabet) → kalori timing warning
- `other` → free-text 100 char

### 6.3 SCOFF screening (sahə 39, 40)

5 sual, 0-1 hər biri:
1. Sick — "Mide bulanışı hiss edənə qədər yeyirsənmi?"
2. Control — "Yeyəcəyə nəzarətini itirdiyini düşünürsən?"
3. One stone — "Son 3 ayda 6 kq+ itirmisənmi?"
4. Fat — "Başqaları arıq desə də, özünü kök görürsənmi?"
5. Food — "Yeyəcək həyatını üstələyirmi?"

**AZ kültür sensitiv adapter:** copy daha incə, "yeyəcək pozğunluğu" sözü əvəzinə "yeyəcək davranışı". Skor ≥2 → aggressive cut bloku (TDEE -500 qadağan, max TDEE -250) + "həkim ilə danış" disclaimer.

### 6.4 Lifestyle (L2 qrup D — sahə 32-35)

| Field | Validation | Plan-a təsir |
|---|---|---|
| `smoker` | never/former/current | current → cardio capacity warning |
| `alcohol_freq` | never/occasional/weekly/daily | daily → recovery copy |
| `caffeine_freq` | never/<2/2-4/>4 cup/day | pre-workout caffeine tövsiyə adjust |
| `sedentary_hours_per_day` | 0-16 | >10 → activity nudge increase |

---

## 7. AI Plan Input Prioritizasiyası

### 7.1 Plan generasiyasını **dəyişən** sahələr (AI input ✅)

| Field | Necə təsir edir |
|---|---|
| `goal` (1) | axis — bulk/cut/general_fit branş |
| `gender` (2) | axis — BMR formula, F-only modifiers |
| `age` (3) | BMR, age-band copy, recovery time |
| `height_cm` (4), `weight_kg` (5) | BMR, TDEE |
| `experience_level` (6) | load.type (light/moderate/heavy) |
| `context` (7) | axis — equipment, alternative selection |
| `weekly_days` (8) | split table (Full Body / U/L / PPL) |
| `session_duration_min` (9) | TDEE activity factor |
| `activity_level_daily` (10) | TDEE çarpan 1.2-1.9 |
| `target_weight` (11), `target_deadline` (12) | tempo (kg/həftə, kalori delta) |
| `language` (57) | localization |

### 7.2 Plan-a **modifier** kimi təsir edən (🟡)

| Field | Modifier davranışı |
|---|---|
| `injury_history` (19) | exclusion list AI prompt-da |
| `movement_restrictions` (21) | exercise filter |
| `medical_conditions` (22) | hard-stop / intensity cap |
| `medications_current` (24) | beta-blocker → HR disable; insulin → carb timing |
| `pregnancy_postpartum` (25) | ⛔ **BLOK** — curated static template |
| `eating_disorder_history` (39), `scoff_score` (40) | aggressive cut bloku |
| `ramazan_active` (46) | suhoor/iftar macro split; intensiv session iftar +2h |
| `diet_pattern` (41), `allergies` (42) | meal suggestion filter |
| `religious_dietary` (47) | halal_strict filter |
| `cardio_preference` (49) | cardio block tipi |
| `hated_exercises` (50), `loved_exercises` (51) | alternatives priority |
| `sleep_h_per_night` (29), `stress_pss4_score` (31) | deload suggestion |
| `trainer_tone` (55) | copy generation tone |
| `power_milestones` (79) | starting weight calibration (advanced) |
| `event_target` (80) | periodization (peak date) |

### 7.3 Plan-a təsir **etməyən** (⚪ — yalnız UX/personalization)

`discovery_channel`, `previous_app_used`, `progress_photos_enabled`, `music_during_workout`, `unit_system`, `preferred_training_time`, `notification_cadence`, `trainer_voice`, `step_goal`, `disliked_foods`, `body fat visual` (cosmetic), bütün L3 measurement-lər (yalnız tracking — plan dəyişmir).

**AI plan PRD üçün hazırlıq:** §7.1 sahələri AI prompt blueprint-də **məcburi input**, §7.2 sahələri **şərti modifier**.

---

## 8. Feature → Required Fields Cross-Reference

| Feature | Lazımi field-lər | Toplama qatı |
|---|---|---|
| **Mifflin-St Jeor BMR** | goal, gender, age, height_cm, weight_kg | L1 |
| **TDEE hesablama** | BMR + activity_level_daily + session_duration_min | L1 + L2 |
| **Protein hədəfi** (1.6-2.2 g/kg bulk, 1.8-2.7 cut, 1.2-1.6 maintain) | goal + weight_kg | L1 |
| **Hidrasyon hədəfi** (35 ml/kg default) | weight_kg | L1 (auto-compute, sahə 37) |
| **Navy body-fat formula** | gender, height_cm, neck_cm, waist_cm, hip_cm (F) | L1 + L3 ölçü trekeri |
| **AI plan generasiyası** | §7.1 bütün sahələr + §7.2 modifier-lər | L1 + L2 (opsiyonel ilk plan üçün) + L5 medical check |
| **Persona-cell resolver** | goal, gender, context | L1 |
| **Hərəkət filter (equipment)** | context, equipment_inventory | L1 + L2 |
| **Hərəkət filter (injury)** | injury_history, movement_restrictions | L2/L3/L5 |
| **Foto-kalori filter (allergies)** | allergies, food_intolerances, religious_dietary | L2/L3 |
| **Ramazan macro split** | ramazan_active, meal_timing | L2/L3 (auto-nudge mart-aprel) |
| **Push notification timing** | preferred_training_time, notification_cadence, device_tz | L2/L3 |
| **Trainer voice TTS** | trainer_voice, trainer_tone, language | L2/L3 |
| **Body fat % auto-compute** | body_fat_pct_method=navy + neck+waist+hip+height | L3 |
| **Hypertrophy progress chart** | weight_kg + biceps/chest/thigh/calf measurements | L3 |
| **Asymmetry detection** | biceps_l vs biceps_r, forearm_l/r, thigh_l/r, calf_l/r | L3 |
| **Power milestone calibration** | power_milestones (squat/bench/deadlift/ohp) | L3 (advanced only) |
| **Event-prep periodization** | event_target | L3 |
| **Pregnancy hard-stop** | pregnancy_postpartum | L2/L3/L5 |
| **Medical clearance unlock** | medical_clearance_obtained + condition_controlled | L5 |
| **Sleep coaching** (Faza 2) | sleep_quality_1_5 + sleep_h_per_night | L4 + L2 |
| **Wearable sync** (Faza 2) | wearable_device_id + vo2_max + hrv_baseline | L4 |
| **Cycle phase adjust** (Faza 2) | cycle_tracking_opt_in + last_period_date | L4 |

---

## 9. DB Mapping (Genişləndirilmiş)

Auth data model PRD-də `user_profiles` cədvəli var. Bu kataloq **yeni ayrı cədvəllər** təklif edir (1:N əlaqələr üçün — measurement timeseries, injury history, medications, conditions).

### 9.1 `public.user_profiles` (auth PRD §7.3 → genişləndirildi)

Sahə 1-9 (L1) NOT NULL, qalanlar NULL. Köhnə kataloq §8 ilə eyni + yeni sahələr:

```sql
-- Yeni L2 sahələri
activity_level_daily text NULL CHECK (activity_level_daily IN ('sedentary','lightly_active','moderately_active','very_active','extra_active'))
target_body_fat_pct decimal(4,1) NULL CHECK (target_body_fat_pct BETWEEN 3 AND 60)
frame_size text NULL CHECK (frame_size IN ('small','medium','large'))
motivations text[] NULL
discovery_channel text NULL
previous_app_used text[] NULL
smoker text NULL CHECK (smoker IN ('never','former','current'))
alcohol_freq text NULL CHECK (alcohol_freq IN ('never','occasional','weekly','daily'))
caffeine_freq text NULL CHECK (caffeine_freq IN ('never','low','medium','high'))
sedentary_hours_per_day int NULL CHECK (sedentary_hours_per_day BETWEEN 0 AND 16)
hydration_goal_ml int NULL  -- auto: 35 × weight_kg
food_intolerances text[] NULL
disliked_foods text[] NULL
religious_dietary text NULL CHECK (religious_dietary IN ('none','halal_strict','kosher','other'))
ramazan_active boolean NULL DEFAULT false
loved_exercises uuid[] NULL
trainer_tone text NULL CHECK (trainer_tone IN ('motivational','calm','drill_sergeant'))
body_fat_pct_method text NULL
body_fat_pct_value decimal(4,1) NULL
power_milestones jsonb NULL  -- {squat,bench,deadlift,ohp}
event_target jsonb NULL  -- {type, date}
medical_clearance_obtained boolean NULL DEFAULT false
medical_disclaimer_accepted_at timestamptz NULL
health_data_consent_at timestamptz NULL
language_at_signup text NOT NULL CHECK (language_at_signup IN ('az','ru','en'))
unit_system text NULL DEFAULT 'metric'
```

### 9.2 `public.user_measurements` (yeni cədvəl — 1:N timeseries)

```sql
CREATE TABLE public.user_measurements (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
  measured_at timestamptz NOT NULL DEFAULT now(),
  weight_kg decimal(5,1) NULL,
  neck_cm decimal(4,1) NULL,
  waist_cm decimal(5,1) NULL,
  hip_cm decimal(5,1) NULL,
  wrist_cm decimal(4,1) NULL,
  chest_cm decimal(5,1) NULL,
  shoulder_cm decimal(5,1) NULL,
  biceps_l_cm decimal(4,1) NULL,
  biceps_r_cm decimal(4,1) NULL,
  forearm_l_cm decimal(4,1) NULL,
  forearm_r_cm decimal(4,1) NULL,
  thigh_l_cm decimal(4,1) NULL,
  thigh_r_cm decimal(4,1) NULL,
  calf_l_cm decimal(4,1) NULL,
  calf_r_cm decimal(4,1) NULL,
  body_fat_pct_computed decimal(4,1) NULL,  -- Navy auto-compute
  notes text NULL
);
CREATE INDEX idx_user_measurements_user_date ON public.user_measurements (user_id, measured_at DESC);
```

### 9.3 `public.user_injuries` (yeni — 1:N)

```sql
CREATE TABLE public.user_injuries (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
  body_region text NOT NULL,  -- canonical
  injury_type text NOT NULL,
  severity int CHECK (severity BETWEEN 1 AND 3),
  status text CHECK (status IN ('acute','subacute','chronic','resolved')),
  active boolean DEFAULT true,
  current_pain_vas int CHECK (current_pain_vas BETWEEN 0 AND 10),
  medical_clearance boolean DEFAULT false,
  reported_at timestamptz DEFAULT now(),
  healed_at date NULL,
  notes text NULL
);
CREATE INDEX idx_user_injuries_user_active ON public.user_injuries (user_id) WHERE active = true;
```

### 9.4 `public.user_health_conditions` (yeni — 1:N)

```sql
CREATE TABLE public.user_health_conditions (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
  condition_code text NOT NULL,  -- canonical: heart_cv, hypertension, diabetes_t1, diabetes_t2, asthma, ...
  controlled boolean NULL,
  diagnosed_year int NULL,
  scoff_score int NULL CHECK (scoff_score BETWEEN 0 AND 5),
  reported_at timestamptz DEFAULT now(),
  notes text NULL
);
```

### 9.5 `public.user_medications` (yeni — 1:N)

```sql
CREATE TABLE public.user_medications (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
  medication_code text NOT NULL,  -- canonical: beta_blocker, corticosteroid, ...
  notes text NULL,
  reported_at timestamptz DEFAULT now()
);
```

### 9.6 `public.user_preferences` (yeni — 1:1)

```sql
CREATE TABLE public.user_preferences (
  user_id uuid PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
  preferred_training_time text NULL,
  notification_cadence text NULL DEFAULT '3x',
  trainer_voice text NULL,
  trainer_tone text NULL DEFAULT 'motivational',
  music_during_workout boolean NULL,
  progress_photos_enabled boolean NULL DEFAULT false,
  unit_system text NULL DEFAULT 'metric'
);
```

**RLS:** Bütün yeni cədvəllər `auth.uid() = user_id` policy. Cascade DELETE auth.users → bütün təbii silinmə.

---

## 10. Privacy & Compliance

### 10.1 GDPR Article 9 (special category health data) sahələr

| Sahə | Cədvəl | Consent |
|---|---|---|
| `injury_history`, `current_pain_vas`, `movement_restrictions` | user_injuries | health_data_consent_at |
| `medical_conditions`, `condition_controlled`, `medications_current` | user_health_conditions / user_medications | health_data_consent_at + medical_disclaimer_accepted_at |
| `pregnancy_postpartum` | user_profiles | Apart consent ekranı |
| `eating_disorder_history`, `scoff_score` | user_health_conditions | Sensitive consent + AZ kültür copy |
| `resting_hr`, `sleep_h_per_night`, `stress_pss4_score`, `smoker`, `alcohol_freq` | user_profiles | Qrup C consent |
| `allergies`, `food_intolerances` | user_profiles | health_data_consent_at |
| Bütün measurement-lər | user_measurements | health_data_consent_at |
| `cycle_tracking_opt_in`, `last_period_date` (Faza 2) | user_profiles | Apart explicit (Faza 2) |

**Consent strategy (mənbə: EDPB 2023 — fitness app-lər üçün üstünlük verilən legal basis):** Explicit consent (Article 9(2)(a)). Bundled consent qadağan — hər qrup üçün ayrı consent.

### 10.2 GDPR Article 9 olmayan sensitive (⚠️) sahələr

`target_weight`, `target_body_fat_pct`, `body_fat_visual_estimate`, `religious_dietary` (halal/Ramazan), bütün measurement-lər → standart personal data, lakin analytics-də plain text qadağan, bucket/hash.

### 10.3 Account delete cascade (CLAUDE.md launch-blocker)

Hər yeni cədvəl `ON DELETE CASCADE` ilə auth.users-ə bağlıdır. 30 gün soft-delete grace + hard delete cron. Storage purge: progress_photos (Supabase Storage) + body_fat_visual referans foto link.

### 10.4 Google Play / Apple App Store Health Declaration (CLAUDE.md launch-blocker)

Bu kataloq Health Declaration tələb edir (Article 9 data var):

- "Health data collected: yes — injury history, sleep, stress (PSS-4), medications, chronic conditions, pregnancy status, body measurements, eating disorder screening"
- "Used only for: personalized fitness/nutrition plan; never shared with third parties"
- "User can delete in-app: Settings → Account → Delete"

### 10.5 Anonymized analytics

Bu sahələr anonim analytics-ə (event-level) gedə bilər:
- L1 enum sahələr (goal, gender, context, experience_level) — agregat
- age → age_bucket (8 bin)
- height/weight → 5cm/5kg bucket
- discovery_channel — agregat
- equipment_inventory — agregat (popular item-lar)

Bu sahələr **heç vaxt** analytics-ə getmir (plain və ya hashed):
- Bütün injury, condition, medication, SCOFF, pregnancy, allergies
- Bütün measurement-lər
- target_weight, target_body_fat_pct

---

## 11. Lokalizasiya (AZ / RU / EN)

**Mənbə:** project-context §3.7 + CLAUDE.md MT qadağası.

**Resource keys:** `shared/strings/{az,ru,en}.json`. Convention:

```
profile.field.<field_name>.label
profile.field.<field_name>.help
profile.field.<field_name>.error.<code>
profile.enum.<field_name>.<value>
profile.group.<group_name>.title
profile.group.<group_name>.consent
profile.trigger.<trigger_name>.title
profile.trigger.<trigger_name>.body
```

**Fallback chain:** AZ → RU → EN → key-itself (debug border).

**MT qadağası gate:**
- Hər yeni key 3 dildə manual-reviewed olmadan PR merge qadağan
- CI: translation memory hash diff > 30% → manual review queue (CI fail)

**Pluralization (CLDR):** AZ 2-form, RU 3-form, EN 2-form.

**Sample copy (key sahələr):**

| Field/event | AZ | RU | EN |
|---|---|---|---|
| L2 modal title | "Tam profil yaratmaq istərdin?" | "Хочешь создать полный профиль?" | "Want to create a full profile?" |
| L5 medical screen title | "Tibbi təhlükəsizlik yoxlaması" | "Проверка медицинской безопасности" | "Medical safety check" |
| Pregnancy hard-stop | "Hamiləlik dövründə təhlükəsiz plan üçün... həkim ilə danışmağını tövsiyə edirik" | "В период беременности рекомендуем... обратись к врачу" | "During pregnancy... please consult your doctor" |
| Injury VAS prompt | "Bugün ağrın necədir? (0-10)" | "Как боль сегодня? (0-10)" | "How's your pain today? (0-10)" |
| Health consent | "Aşağıdakı suallar sağlamlıqla bağlıdır..." | "Эти вопросы касаются здоровья..." | "These questions concern health data..." |
| Medical disclaimer | "Bu app tibbi cihaz deyil və həkim məsləhətini əvəz etmir" | "Это приложение не медицинское устройство..." | "This app is not a medical device..." |

---

## 12. Analytics

### 12.1 `field_collected` event schema

```json
{
  "event": "field_collected",
  "properties": {
    "field_name": "neck_cm",
    "layer": "L3",
    "value_anonymised": "<bucket or null>",
    "trigger_screen": "measurement_tracker",
    "time_to_answer_ms": 3120,
    "skip": false,
    "retry_count": 0,
    "language": "az",
    "device_tz": "Asia/Baku",
    "session_id": "uuid"
  }
}
```

### 12.2 PII guard rules

- `age` → `age_bucket` (8 bin); exact YASAQ
- `height_cm`, `weight_kg`, `target_weight` → 5cm/5kg bucketed
- `allergies` custom text → hash
- `injury_history` → muscle_region + severity only
- `medications_current` → canonical code only (text notes YASAQ)
- `medical_conditions` → canonical code only
- `scoff_score`, `pregnancy_postpartum`, `eating_disorder_history` → **analytics-də yox** (sırf model-side)
- Bütün measurement-lər → analytics-də yox

### 12.3 L1 üçün xüsusi event-lər (auth PRD §11-də qalır)

`onboarding_q1_answered` … `onboarding_q7_answered`. Bu kataloq əlavə olaraq L2/L3/L5 üçün ümumi `field_collected` event-i tələb edir.

---

## 13. Open Questions

1. **PAR-Q+ AZ rəsmi tərcümə var?** — Tapılmadı. Tibbi review tələb (lokal kliniki məsləhətçi ilə) lazımdır. Mənbə: əgər yoxsa, in-house manual translation + medical review imza ilə.
2. **SCOFF AZ kültür uyğunluğu?** — Persian validation var (mənbə §14), Türk dilində adaptasiya var; AZ üçün psixoloq review tövsiyə olunur.
3. **Ölçü retest cadence default** — Aylıq seçildi; user A/B test ilə həftəlik/2-həftəlik test edilməlidir.
4. **Body-fat method seçimi** — Navy default olur (yalnız tape lazımdır); BIA/DEXA inteqrasiyası Faza 2.
5. **`stress_pss4_score` mini-quiz onboarding-də uyğun?** — 4 sual, 30 san; məqbul, lakin Day-3 nudge-da soruşmaq daha yüksək cavab nisbəti verə bilər (test gözləyir).
6. **`scoff_score` MVP-də mi, yoxsa Faza 2?** — MVP-də opsiyonel + AZ kültür sensitiv copy ilə daxil edildi; əgər kültür araşdırması göstərirsə deferred et.
7. **`power_milestones` (squat/bench/deadlift 1RM)** — Yalnız advanced experience user-lərə göstərilir; intermediate üçün də açmaq A/B test gözləyir.
8. **`event_target` (marathon/competition prep) MVP-də?** — Optional, Plan trigger #3-də (event prep) — MVP-də qalsın.

---

## 14. Internet Mənbələri (Araşdırma audit trail)

### PAR-Q+ və ACSM
- PAR-Q+ 2024 PDF — https://eparmedx.com/wp-content/uploads/2023/12/PARQPlus2024Fillable.pdf
- PAR-Q+ Surrey city — https://www.surrey.ca/sites/default/files/media/documents/ParQ-Plus-2024-pdf.pdf
- NASM PAR-Q overview — https://blog.nasm.org/everything-you-need-to-know-about-the-par-q
- APTA PAR-Q reference — https://www.apta.org/patient-care/evidence-based-practice-resources/test-measures/physical-activity-readiness-questionnaire-par-q-par-q
- ACSM Pre-Participation Screening — https://www.exerciseismedicine.org/assets/page_documents/Appendix%20D%20-%20ACSM%20Risk%20Stratification%20Q.pdf
- ACSM CH02 PDF — https://downloads.lww.com/wolterskluwer_vitalstream_com/sample-content/9780781769037_ACSM/samples/CH02.pdf
- Studocu PAR-Q+ example (chronic conditions follow-up) — https://www.studocu.com/en-us/document/college-of-charleston/neuromuscular-assessment-and-exercise-prescription/par-q-plus-example-dr-saracino/54030808

### SCOFF, PSS-4, VAS, Borg
- SCOFF original validation (BMJ 1999) — https://pmc.ncbi.nlm.nih.gov/articles/PMC28290/
- SCOFF NHS PDF — https://www.rightdecisions.scot.nhs.uk/media/go5jp0zr/scoff-questionnaire.pdf
- PSS-4 Cohen original — https://scholar.harvard.edu/files/bettina.hoeppner/files/pss-4.pdf
- PSS-4 properties — https://www.ncbi.nlm.nih.gov/pmc/articles/PMC5791241/
- VAS Pain — https://painclinics.com/blog/visual-analog-scale
- Borg RPE — https://www.physio-pedia.com/Borg_Rating_Of_Perceived_Exertion
- Borg WHOOP — https://www.whoop.com/us/en/thelocker/borg-scale-perceived-exertion-rpe/

### Body composition formulas
- Navy body-fat (Hodgdon-Beckett) — https://www.omnicalculator.com/health/navy-body-fat
- Navy formula details — https://med.libretexts.org/Courses/Irvine_Valley_College/Physiology_Labs_at_Home/03:_Anthropometrics/3.02:_Part_B-_Circumference_Measures/3.2.04:_Part_B4-_The_U.S._Navy_body_fat_estimation_formula
- Mifflin-St Jeor coaches guide — https://www.promealplan.com/en/blog/mifflin-st-jeor-equation-coaches-guide
- TDEE activity factors — https://www.calculatorian.com/en/articles/health/activity-factors-tdee

### Body measurements
- Hevy body measurements list — https://www.hevyapp.com/features/track-body-measurements/
- MacroFactor body metrics — https://macrofactorapp.com/progress-photos-and-body-measurement-tracker/

### Competitor onboarding
- BetterMe onboarding 26-questions — https://theappfuel.com/examples/bettermefitness_onboarding
- BetterMe MNT review — https://www.medicalnewstoday.com/articles/betterme-review
- BetterMe body type quiz — https://betterme.world/articles/body-type-quiz/
- Fitbod onboarding — https://www.theappfuel.com/examples/fitbod_onboarding
- Fitbod Page Flows — https://pageflows.com/post/ios/onboarding/fitbod/
- Freeletics onboarding (6 q) — https://www.freeletics.com/en/blog/posts/AI-and-your-Coach/
- Freeletics get started — https://help.freeletics.com/hc/en-us/articles/115004675229
- Nike Training Club onboarding — https://medium.com/@stephsutanto/nike-training-club-app-user-onboarding-teardown-66d72e29e9c9
- Centr (Hemsworth) review — https://www.bluelabellabs.com/blog/app-rundown-centr/
- Centr T3 review — https://www.t3.com/features/chris-hemsworth-centr-review
- Caliber premium intake — https://barbend.com/caliber-fitness-app-review/
- Caliber Fitness Drum — https://fitnessdrum.com/caliber-app-review/
- Hevy onboarding 10 steps — https://www.findyouredge.app/news/best-strength-training-apps-2026
- Strong minimal logger — https://repreturn.com/strong-app-review/
- JEFIT — https://www.jefit.com/wp/guide/best-strength-training-apps-for-2026-7-options-tested-by-lifters/
- MyFitnessPal goal setup — https://support.myfitnesspal.com/hc/en-us/articles/360032625391
- MyFitnessPal initial goals — https://fitblissfitness.com/2020/08/07/how-to-set-your-myfitnesspal-goals/

### Injury contraindications
- Bulging disc contraindications — https://www.njspineandortho.com/the-right-and-wrong-exercises-for-individuals-with-a-bulging-disc/
- ACL rehab BFR — https://www.ncbi.nlm.nih.gov/pmc/articles/PMC12847322/
- MNT herniated disc safe exercises — https://www.medicalnewstoday.com/articles/324311
- Knee BFR review — https://www.ncbi.nlm.nih.gov/pmc/articles/PMC12387686/

### Privacy / GDPR
- GDPR Art.9 explainer — https://gdpr-info.eu/art-9-gdpr/
- Exabeam Art.9 — https://www.exabeam.com/explainers/gdpr-compliance/gdpr-article-9-special-personal-data-categories-and-how-to-protect-them/
- Health data EDPB consent — https://www.themomentum.ai/blog/gdpr-consent-requirements-health-data
- Fitness apps GDPR — https://dev.to/custodiaadmin/gdpr-for-gyms-and-fitness-businesses-member-data-health-information-and-biometrics-1d0e

### Apple Health / HealthKit
- HealthKit authorizing — https://developer.apple.com/documentation/healthkit/authorizing-access-to-health-data
- Apple Health fit apps — https://developer.apple.com/health-fitness/
- HealthKit Swift 2026 — https://medium.com/@garejakirit/apple-healthkit-in-ios-2026-the-complete-swift-guide-step-by-step-0d4215b54412

### Onboarding UX (progressive profiling)
- Zigpoll progressive profiling +20% — https://www.zigpoll.com/content/what-are-the-best-practices-for-optimizing-user-onboarding-flows-to-reduce-dropoff-rates-in-mobile-apps
- Userpilot 12 best practices — https://userpilot.com/blog/app-onboarding-best-practices/
- Appcues mobile onboarding — https://www.appcues.com/blog/mobile-onboarding-best-practices

### Nutrition / hydration / protein
- Protein ISSN position stand — https://www.ncbi.nlm.nih.gov/pmc/articles/PMC5477153/
- Protein examine guide — https://examine.com/guides/protein-intake/
- Water 35 ml/kg — https://www.omnicalculator.com/health/water-intake
- Ramadan fitness LesMills — https://www.lesmills.com/us/fit-planet/health/ramadan-ready/
- Ramadan bodybuilding — https://athenaeumpub.com/exploring-the-intersection-of-bodybuilding-and-ramadan-strategies-for-maintaining-muscle-mass-and-performance-during-fasting-2/

### Fitness coach intake best practices
- Trainerize 2026 onboarding guide — https://www.trainerize.com/blog/the-ultimate-guide-to-onboarding-new-fitness-clients/
- MyPTHub questionnaire — https://www.mypthub.net/blog/personal-training-client-questionnaire/

---

## 15. Changelog

| Version | Date | Author | Change |
|---|---|---|---|
| 1.0 | 2026-05-22 | John (PM) + Mary (Analyst) | İlk yaradılış. Köhnə `prd-onboarding-questions-catalog-2026-05-22.md` (32 sahə) **silindi və əvəzləndi**. Ad dəyişdirildi — bu data təkcə onboarding-də deyil, app-wide kontekstli triggerlərdə toplanır. İnternet araşdırması (15+ WebSearch, 3 WebFetch, mənbə §14): PAR-Q+ 2024, ACSM screening, SCOFF, PSS-4, VAS, Borg, Navy body-fat formula, Hevy/Fitbod/BetterMe/Freeletics/Centr/Caliber/Nike onboarding muqayisəsi. **Yeni qatlar:** L5 medical safety screen (10 sahə) — AI plan-ı bloklaya bilər. **Yeni cədvəllər:** user_measurements, user_injuries, user_health_conditions, user_medications, user_preferences. **Yeni sahələr:** activity_level_daily, body_fat_pct, frame_size, motivations, lifestyle (smoker/alcohol/caffeine/sedentary), medications, SCOFF, religious_dietary, ramazan_active, loved_exercises, trainer_tone, body_fat_pct_method, power_milestones, event_target, medical_clearance_obtained, 12 measurement field-i. Cəm: ~75 distinct fields. |

---

**END OF PRD — User Profile Data Catalog v1.0**
