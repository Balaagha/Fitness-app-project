---
project_name: 'fitnessApp'
user_name: 'Balaagha'
date: '2026-05-23'
version: '1.0'
workflowType: 'ux-spec'
ux_scope: 'questions-pages-all-layers'
phase: 'MVP / Faza 1'
parent_prd:
  - prd-auth-onboarding-2026-05-22.md (v3.1 — onboarding skeleton)
  - prd-user-profile-data-catalog-2026-05-22.md (v1.0 — L1+L2+L3+L4+L5 field master)
relatedDocs:
  - ux-auth-onboarding-2026-05-22.md (v1.1 — onboarding flow ekran inventarı)
  - ux-design-specification.md (v1.4 → v1.5 — kanonik dizayn dili)
  - docs/project-context.md (v3.3 §0, §1, §1b, §3, §6)
  - CLAUDE.md (Qəti Qadağalar)
designSystemFile: 'app/design/mobile/app_design.pen'
status: 'draft — ready-for-Sally-wireframes'
ux_agent: 'Sally'
---

# UX Spec — Questions Pages (Bütün Qatlar: L1 + L2 + L3 + L5)

**Author:** Sally (UX) · **Date:** 2026-05-23 (v1.0)
**Scope:** `fitnessApp`-də **form-bazlı sual ekranlarının** kanonik dizayn müqaviləsi — onboarding axınından ASILI DEYİL. Bu sənəd hər yerdə yenidən istifadə olunan sual səhifələrinin (L1 onboarding 7, L2 opsiyonel akkordiyon, L3 in-app progressive, L5 medical safety + gələcəkdə weekly check-in / settings yenilənmə) **vahid dizayn dili**dir.

> **Bu sənəd niyə ayrı?** Onboarding axını və sual səhifələri **iki ayrı UX məsələsidir**:
> - **Onboarding** (`ux-auth-onboarding-2026-05-22.md`) = qapı + cold-start branching + auth provider seçimi + AI disclosure + handoff. Axın və state-machine.
> - **Questions** (bu sənəd) = "user-ə bir şey soruşulan istənilən ekran" üçün **şablon / komponent / state müqaviləsi**. Onboarding burada yalnız **istifadəçi** rolundadır; eyni şablon L2/L3/L5/weekly check-in/settings re-prompt üçün də istifadə olunur.
>
> Bu ayrılma `prd-user-profile-data-catalog-2026-05-22.md` §1-də sabitlənmiş 5 qatlı toplama strategiyasına (L1-L5) uyğundur — sual səhifəsi onboarding-in qulu deyil, müstəqil bir UI rejimidir.

---

## 0. Kanonik İstinad & Doktrina

Bu sənəd vizual qərarları **təkrar tərif etmir** — kanonik mənbələrdən oxuyur:

| Mənbə | Nə üçün |
|-------|---------|
| `docs/project-context.md` **§0** | Kullanıcı kahraman, AI dəstək qatı — copy üslubu ("öz cavabını anlat") |
| `docs/project-context.md` **§1** | Minimal Design Principle — ≤7 element / ekran; bir ekran = bir sual |
| `docs/project-context.md` **§1b** | Volt renk sistemi — `volt #E6FF00` accent, `on-volt #0E0E0E`, qara taban |
| `docs/project-context.md` **§3** | 7 məcburi sual + 19 opsiyonel progressive müqaviləsi |
| `docs/project-context.md` **§6** | Persona-cell `{context × sex × goal}` — sual cavablarının arxa-uç təsiri |
| `prd-user-profile-data-catalog-2026-05-22.md` | Hər field üçün copy AZ/RU/EN + validation + DB target + AI input flag |
| `CLAUDE.md` Qəti Qadağalar | MT YASAQ, `volt` üzərində ağ mətn YASAQ, ≤7 məcburi sual, pregnancy hard-stop |
| `ux-design-specification.md` §3 | Komponent kitabxanası (RadioCard, StepperField, WheelPicker, və s.) |

**AZ native:** hər copy stringi manuel review (MT YASAQ — CI gate).

---

## 1. Sual Ekranı Anatomiyası — Vahid Şablon

> Hər sual ekranı eyni iskeletə oturur. Variantlar (RadioCard vs Wheel vs Stepper vs MultiSelectChip) yalnız **input zonasında** dəyişir; qalan hər şey sabitdir.

```
┌─────────────────────────────────────┐
│ StatusBar (62px, OS)                │
├─────────────────────────────────────┤
│ [←]  ProgressIndicator (opsional)   │ ← Header zonası (44pt)
├─────────────────────────────────────┤
│                                     │
│  STEP CHIP "SUAL N / M" (caption)   │ ← Konteks etiketi (opsional)
│                                     │
│  Title (25/700) — sual mətni        │ ← `title` token
│  Sub-context (13/500 text-secondary)│ ← ≤1 sətir, niyə soruşulur
│                                     │
│  ──────────────────────────────     │
│                                     │
│  [INPUT ZONASI]                     │ ← Variant (§3 cədvəlinə bax)
│   • RadioCard ×N                    │
│   • WheelPicker                     │
│   • Stepper + UnitToggle            │
│   • SegmentedControl                │
│   • MultiSelectChip qrid            │
│   • Slider (VAS, range)             │
│   • TextField (yaş, çəki manual)    │
│                                     │
│  ──────────────────────────────     │
│                                     │
│  [InlineError] (alan altı, opsional)│ ← `danger` ikon + mətn
│                                     │
├─────────────────────────────────────┤
│  [Skip GhostBtn] (L2/L3/L5 yalnız)  │
│  [PrimaryButton "Davam et"]         │ ← Footer CTA, alt padding 44
└─────────────────────────────────────┘
```

### 1.1 Zonalar — sabit qaydalar

| Zona | Hündürlük | Padding | Token | Variasiya icazəsi |
|------|-----------|---------|-------|-------------------|
| StatusBar | 62 | OS | OS chrome | Yox — OS native |
| Header | 44 | yatay 28 | back ikon `text-primary`, progress `volt` filled / `surface-2` empty | Progress göstəril**mə**yə bilər (settings re-prompt) |
| Konteks (title + sub) | fit | yatay 28, dikey 24 | title `text-primary`, sub `text-secondary` | Sub-context məcburidir (niyə soruşulur) |
| Input | fit | yatay 28 | input variantı | Yalnız bu zona dəyişir |
| Inline error | fit (≤32) | yatay 28 | `danger` | Görünər yalnız error halında |
| Footer CTA | 58 + 44 alt | yatay 28 | Primary `volt`/`on-volt` · GhostBtn border `text-primary` | Skip yalnız opsional sualda |

### 1.2 Zona prinsipləri

- **Bir ekran = bir sual.** İstisna: məntiqli cüt (Q4 boy+çəki, Q7 günlər+sessiya) bir ekrandadır — `prd-auth-onboarding §3.1`.
- **Sub-context məcburidir.** "Niyə soruşulur" cavabı 1 sətirdə — sürtünməni azaldır (PRD R-5 mitigation).
- **Skip CTA L1-də YOXDUR.** Yalnız L2/L3/L5 opsional suallarda. L1 hard-required.
- **CTA disabled state** — input boş ya da invalidsa CTA `surface-2` zəmindədir.
- **Anti-pattern:** modal-in-modal, eyni ekranda tab+accordion, >2 primary CTA — YASAQ (project-context §1).

---

## 2. State Müqaviləsi (sual ekranı üçün universal)

| State | Görünüş | Tətbiq |
|-------|---------|--------|
| **empty / default** | İlk render; CTA disabled; placeholder göstərilir | İlk daxilolma |
| **focused** | Input border `volt` 2px, label up-shift | Tap / klaviatura |
| **valid** | CTA enabled (`volt`), tick mikro-animasyon yox | Validation pass |
| **error** | InlineError `danger` ikon + mətn; CTA enabled qalır (təkrar cəhd üçün) | Range/format pozulma |
| **loading** | CTA `volt` + inline spinner, double-tap qarşısı alınır | Network çağırışı (yalnız L3/L5 təklif olunduqda) |
| **success** | Tick 400ms → keçid | Submit ok |
| **offline** | Sticky top OfflineBanner (`warning`); SQLDelight-ə davam yazılır; submit toast `AUTH_007` (auth ilə bağlıdırsa) | NFR-O1 |
| **disabled** | Field disabled — yalnız pre-condition pozulduqda (məs. parental consent unchecked) | Nadir hal |
| **terminal** | Geri yox; tək CTA "Çıxış" və ya hard handoff | Q3 `<13` → AgeGateBlocked |
| **skipped** | İz: accordion header-də tick ikonu yerinə "Ötürüldü" rozeti | Yalnız L2 group skip |

> **Animasyon qaydası (project-context §1):** kahraman animasyon yox. RadioCard seçimi 150ms; CTA enable fade; progress dot fill 300ms ease-out — kifayət. Glee / sparkles / AI sehri YASAQ.

---

## 3. Input Variantları (komponent → istifadə yeri)

> Komponent tam anatomik təfsilat: `ux-design-specification.md` §3.5. Burada YALNIZ "hansı sual üçün hansı variant" mapping.

| Variant | Komponent | İstifadə (field nümunəsi) | A11y notu |
|---------|-----------|---------------------------|-----------|
| **RadioCard ×2-4** | `comp/option` | Q1 goal · Q2 gender · Q5 experience · Q6 context · diet_pattern · trainer_tone · smoker · religious_dietary | `radiogroup` / `radio` rolü; aktivlər screen reader-də sırayla |
| **WheelPicker** | `WheelPicker` | Q3 age (13-99) | `adjustable` rolü; **manual TextField fallback** (VoiceOver) |
| **Stepper + UnitToggle** | `StepperField` + `UnitToggle` | Q4 height_cm + weight_kg; target_weight; hydration_goal_ml; step_goal | Klaviatura: unit-toggle → değer1 → değer2 → CTA |
| **SegmentedControl** | `SegmentedControl` | Q7 weekly_days (2-7), session_duration_min (15/30/45/60); meal_timing slots | Swipe + klaviatura ok |
| **MultiSelectChip qrid** | `MultiSelectChip` | motivations · allergies · food_intolerances · equipment_inventory (12) · medical_conditions · medications · injury_history sites · disliked_foods | Grid keyboard nav (arrow keys); 44pt; cap "≤5 seçim" göstərilməli (varsa) |
| **Slider (discrete)** | `VASSlider` | current_pain_vas (0-10); stress_pss4 questions (0-4 each); sleep_quality_1_5 | Discrete ±1 step keyboard; **manual TextField fallback** |
| **CheckboxList** | `Checkbox` | medical safety multi-flag (`yes/no` per item × N); parental consent | Hər checkbox 44pt, label tappable |
| **DatePicker** | native (OS) | target_deadline; last_period_date (Faza 2); medical_clearance_date | OS native a11y |
| **TextField (numeric)** | `TextInputField` | resting_hr (manual); power_milestones (squat/bench kg); free-text body comments | Numeric keyboard; auto-fill `none` |
| **PhotoCardPicker** | `RadioCard` (5 illustration) | body_fat_visual_estimate (5 illustrated buckets) | Hər kart üçün AZ alt-text |
| **AccordionGroup header** | `AccordionGroup` | L2 6 qrup başlığı (A-F) — qrupu açar | Header tap = expand; arrow ikon |

### 3.1 Variant seçim qaydası

- **≤3 seçim, ikon vacib** → RadioCard
- **Continuous range** → WheelPicker (yaş) və ya Stepper (boy/çəki — ±1 yumşaqlığı)
- **Çox seçim mümkün, qısa label** → MultiSelectChip
- **Sadə 0-10 və ya likert** → Slider
- **Tarix** → OS native DatePicker (custom dizayn YASAQ — A11y risk)
- **Brand kontrol vacib + 2-4 dəyər** → SegmentedControl

---

## 4. Sual Ekran İnventarı (bütün qatlar)

> Bu cədvəl `prd-user-profile-data-catalog-2026-05-22.md` Master Field-dən törəyən **ekran səviyyəli** mənzərədir. Hər sətr **bir sual ekranı** — field birləşmələri qruplandırılıb.

### 4.1 L1 — Onboarding məcburi (7 sual ekranı)

| Q# | Ekran | Field(lər) | Variant | Validation kodu | Skip? | Persona impact |
|----|-------|------------|---------|-----------------|-------|----------------|
| Q1 | **Goal** | goal | RadioCard ×3 (bulk/cut/general_fit) | — | ❌ | axis |
| Q2 | **Gender** | gender | RadioCard ×2 (male/female) | — | ❌ | axis (BMR, persona) |
| Q3 | **Age** | age | WheelPicker (13-99) | `ONB_001` (<13 hard, >99 soft) | ❌ | modifier; Age Gate trigger |
| Q4 | **HeightWeight** | height_cm + weight_kg | Stepper ×2 + UnitToggle | `ONB_002` | ❌ | BMR, BMI |
| Q5 | **Experience** | experience_level | RadioCard ×3 (beginner/intermediate/advanced) | — | ❌ | modifier (load) |
| Q6 | **Context** | context | RadioCard ×3 (serious_gym/casual_gym/home_only) | — | ❌ | axis (equipment) |
| Q7 | **DaysSession** | weekly_days + session_duration_min | SegmentedControl ×2 | — | ❌ | split, TDEE |

**L1 qaydaları:**
- Median tamamlanma ≤90 sn (G1)
- Cap **7** — 8-ci əlavə hard-prohibited (CLAUDE.md)
- Hər cavab SQLDelight `onboarding_state`-ə dərhal yazılır
- Q3 cavabı §3.6 Age Gate axın dəyişdirir (hard-stop / parental notice)
- Q7 submit → `persona_cell` hesablanır, AI Disclosure ekranı göstərilir (axın detalı: `ux-auth-onboarding`)

### 4.2 L2 — Opsiyonel onboarding akkordiyon (6 qrup, 23 field)

> Q7 sonrası modal: "Tam profil yaratmaq istərdin? (3-4 dəq, hər vaxt skip)" — opt-in.

| Qrup | Ekran (qrup başlığı) | Field-lər | Variant qarışığı | Skip-per-group |
|------|----------------------|-----------|------------------|----------------|
| **A** | **Body & Metabolism** | activity_level_daily, target_weight (+deadline), body_fat_visual_estimate, frame_size | RadioCard + Stepper + DatePicker + PhotoCardPicker | ✅ |
| **B** | **Activity & Lifestyle** | sleep_h_per_night, stress_pss4_score (4-sual sub-stack), sedentary_hours_per_day, step_goal | Slider + Likert + Stepper | ✅ |
| **C** | **Health & Injuries (light)** | injury_history (`var/yox` + site multi-select), movement_restrictions | MultiSelectChip qrid | ✅ (sensitive data — opt-in confirm) |
| **D** | **Food Preferences** | diet_pattern, allergies, food_intolerances, religious_dietary, ramazan_active (mart-aprel auto-detect) | RadioCard + MultiSelectChip ×3 | ✅ |
| **E** | **Equipment Inventory** | equipment_inventory (12 item) — home_only kontekstdə default `[bodyweight]` | MultiSelectChip qrid ×12 | ✅ |
| **F** | **Preferences** | motivations, discovery_channel, previous_app_used, trainer_voice, trainer_tone, preferred_training_time, notification_cadence, music_during_workout | MultiSelectChip + RadioCard | ✅ |

**L2 qaydaları:**
- Hər qrup tam bir ekran (AccordionGroup header → field stack)
- Hər ekranda **Skip GhostBtn** + Primary CTA "Davam et" / sonuncu qrupda "Bitir"
- Skip → analytics `l2_group_skipped { group: A|B|C|D|E|F }`
- Tamamlama → `l2_completed` event + dashboard "+15% personalizasiya" mikro-rozet (kahraman copy YASAQ)

### 4.3 L3 — In-app progressive (kontekstli trigger)

> Bu suallar onboarding-də DEYİL — feature ilk açılışında və ya settings-də soruşulur. Trigger-ə bağlıdır.

| Ekran | Field-lər | Trigger | Variant | Skip? |
|-------|-----------|---------|---------|-------|
| **MeasurementSheet** | neck/waist/hip/chest/shoulder/biceps×2/forearm×2/wrist/thigh×2/calf×2_cm | "Ölçü trekeri" ilk açıl | Stepper grid | ✅ (bir-bir) |
| **TargetWeightSheet** | target_weight + target_deadline + target_body_fat_pct (opsional) | Kalori ekranı; cut/bulk user | Stepper + DatePicker | ✅ |
| **InjuryLogSheet** | injury_history detail (per-injury: site, side, type, onset, current_pain_vas, movement_restrictions) | Yaralanma günlüyü "+ əlavə et" | MultiSelect + Slider + DatePicker | ❌ (open) / ✅ (close) |
| **PainCheckin** | current_pain_vas (mövcud yaralanma üçün) | Hər məşq başlamadan; daily | Slider 0-10 | ✅ (1 gün ertələ) |
| **HealthSnapshot** | resting_hr, sleep_h_per_night, stress_pss4_score | Day-3 nudge (1 dəfə) | Stepper + Slider + Likert | ✅ |
| **CycleSheet (Faza 2)** | cycle_tracking_opt_in, last_period_date | F-only Settings | RadioCard + DatePicker | ✅ (rezerv) |
| **DislikeFoodSheet** | disliked_foods (foto-kalori "bəyənmədim" tap-dan) | Foto-kalori meal log | MultiSelectChip | ✅ |
| **PowerMilestoneSheet** | power_milestones `{squat,bench,deadlift,ohp}` | Advanced user — Q5=advanced sonrası optional | Stepper qrid | ✅ |
| **EventTargetSheet** | event_target `{type, date}` | Plan trigger #3 (event prep) | RadioCard + DatePicker | ✅ |

### 4.4 L5 — Medical Safety (məcburi əgər tetiklənərsə)

> Trigger: ilk AI plan tələbi · pregnancy nudge · injury günlüyü açılış · advanced training intensity unlock.

| Ekran | Field-lər | Variant | Skip? |
|-------|-----------|---------|-------|
| **MedicalSafetyScreen** (PAR-Q+ 7+3) | medical_conditions (multi), condition_controlled (per-cond. follow-up), medications_current, medical_disclaimer_accepted_at | CheckboxList + per-row follow-up + MultiSelectChip | ❌ (məcburi — skip = curated static template) |
| **SCOFFScreen (sensitive)** | eating_disorder_history, scoff_score (5 sual) | RadioCard ×5 (yes/no) | ✅ (sensitive consent əvvəl) |
| **MedicalRedFlagModal** | (heç bir field — terminal modal) | Static | terminal |
| **HealthDataConsent** | health_data_consent_at | Checkbox + LinkText "GDPR Art.9" | ❌ (məcburi əgər L5 / L2 health açılır) |

**L5 qaydaları (CLAUDE.md):**
- "approval / təsdiq" SÖZÜ YASAQ — "**uyğunluq yoxlaması**" işlədilir
- Tibbi ton: sakin, oyun animasyası YASAQ
- pregnancy_postpartum=true → AI plan generasiyası **bloklanır** (modifier precedence: pregnancy > injury > Ramazan > home_only_F > cut)
- Red flag triggered → modal dismiss-only "Anladım" + GhostBtn "Mənbələr"
- Sensitive data → her ekranda LinkText "Bu məlumat necə işlənir" → Privacy Policy AZ/RU/EN

### 4.5 Gələcək: Settings re-prompt + Weekly check-in (eyni şablon)

- **Settings field edit:** istənilən field-ə tap → eyni sual ekranı modal kimi açılır (header `[×]` close, geri yox). State machine yox — single field update.
- **Weekly check-in (Faza 2 ipucu):** çəki, sleep_h, stress, pain_vas — 4 ekran ardıcıllığında eyni şablon. Skip-able. Bu sənəd yalnız **şablon müqaviləsi** verir; içəriği `prd-weekly-checkin` (planlanır).

---

## 5. Validation & Error Catalog (sual ekranları üçün)

> Tam kod siyahısı (AUTH_001-013, ONB_001-005): `prd-auth-data-model-2026-05-22.md` §2.5. Burada YALNIZ sual ekranlarına aid olan + render qaydası.

| Kod | Field | Range / pozulma | Render | İcra |
|-----|-------|------------------|--------|------|
| `ONB_001` | age | <13 hard-stop | (terminal screen — `ux-auth-onboarding` #4) | Terminal — back yox |
| `ONB_001` | age | >99 soft warn | InlineError "Yaş 99-dan böyük olmaz" | CTA enabled qalır |
| `ONB_002` | height_cm | <120 və ya >220 | InlineError "Range: 120-220 cm" | CTA enabled |
| `ONB_002` | weight_kg | <30 və ya >200 | InlineError "Range: 30-200 kg" | CTA enabled |
| `ONB_005` | parental_consent | unchecked + tap "Davam" | InlineError "Davam etmək üçün təsdiq lazımdır"; CTA disabled qalır | Pre-condition |
| `L2_RANGE_*` | hər L2 numeric | range pozulma | InlineError | Per-field |
| `L3_PAIN_RANGE` | current_pain_vas | <0 və ya >10 | (Slider clamp; error baş vermir) | UI prevention |
| `L5_RED_FLAG` | medical multi | qırmızı bayraq cavabı | MedicalRedFlagModal | Plan generation bloklanır |
| `L5_DISCLAIMER_REQ` | medical_disclaimer | unchecked + tap CTA | InlineError + CTA disabled | Pre-condition |

**Anti-frustrasiya qaydası:** CTA error halında **enabled qalır** (təkrar cəhd üçün) — istisna: pre-condition pozulması (parental consent, medical disclaimer). Bu CLAUDE.md UX-incident-1 (G-4 fix) qaydasıdır.

---

## 6. Copy Üslubu (sual ekranı)

> Tam AZ/RU/EN copy hər field üçün → `prd-user-profile-data-catalog-2026-05-22.md` (master). Bu bölmə ÜSLUB müqaviləsidir.

### 6.1 Title üslubu

- **Birinci şəxs** istifadəçi tərəfindən: "Əsas **hədəfin** nədir?" — "**Sənin** hədəfin nədir?" DEYİL (project-context §0 — user-agentic).
- ≤6 söz, sadə cümlə.
- Sual işarəsi məcburidir.
- AI sözü işlədilməz (yalnız AIDisclosure ekranı istisna).

| ✅ Doğru | ❌ Yanlış |
|----------|-----------|
| "Əsas hədəfin nədir?" | "AI planını qurması üçün hədəfini seç" |
| "Boyun və çəkin" | "Sənin haqqında bir az daha danışaq" |
| "Yaşın neçədir?" | "Sənin yaşını bilmək istəyirik" |
| "Harada məşq edəcəksən?" | "İdmana hara üçün hazırlaşırsan?" |

### 6.2 Sub-context üslubu

- **"Niyə soruşulur"** — 1 sətirdə, ≤12 söz.
- Faydanı user-mərkəzli izah et — "**sənin** üçün doğru kalori hesablayaq", "AI üçün lazımdır" DEYİL.

| ✅ Doğru | ❌ Yanlış |
|----------|-----------|
| "Məşqini bu hədəfə görə qururuq." | "AI plan generasiyası üçün lazımdır" |
| "BMR və TDEE-ni hesablayacağıq." | "Sistem hesablama edir" |
| "Hərəkətləri buna görə uyğunlaşdırırıq." | "Plan AI-yə göndəriləcək" |

### 6.3 Error üslubu

- **Suçlamaz, eylem önerir.**
- Range göstər: "Range: 13-99"
- "Yanlış daxil etdin" YASAQ.

### 6.4 Skip üslubu (L2/L3)

- "Sonra" və ya "Bu qrupu ötür" — neytral.
- "İstəmirəm" / "Vacib deyil" YASAQ (kullanıcının seçimini kiçildir).

### 6.5 Medical (L5) üslubu

- Sakin, tibbi, oyun YASAQ.
- "approval / təsdiq" → "**uyğunluq yoxlaması**".
- "Bu məlumat tibbi məsləhət deyil" disclaimer hər L5 ekranda görünür.

---

## 7. Accessibility (sual ekranı üçün universal)

| ID | Tələb | Verifikasiya |
|----|-------|--------------|
| QA11y-1 | Kontrast `volt`/`on-volt` 17:1; `text-primary`/`bg` 19.5:1 ✓ | §1b token |
| QA11y-2 | Touch target ≥44pt iOS / 48dp Android — hər input, chip, ster | Manual test |
| QA11y-3 | Screen reader label AZ/RU/EN hər input + variant üçün | VoiceOver + TalkBack |
| QA11y-4 | WheelPicker + Slider üçün **manual TextField fallback** | A11y-da görünür |
| QA11y-5 | Dynamic Type — fixed-size font YASAQ; ekranın 200% scale-də overflow YOXLANIŞI (xüsusilə Q3 wheel + L5 long body) | Manual test |
| QA11y-6 | Reduce Motion respect — pulse/bounce dayanır; opacity-fade qalır | OS ayar |
| QA11y-7 | Focus order: Header back → input(lar) → InlineError → Skip → Primary CTA | Klaviatura test |
| QA11y-8 | LiveRegion async event — InlineError görünəndə announce; offline banner | NVDA/VoiceOver test |
| QA11y-9 | AZ TalkBack pronunciation zəif olduqda override (kritik element — Q3 age, L5 medical) | Per-platform |
| QA11y-10 | Form auto-fill **YASAQ** (sual ekranları profil, password deyil — heç bir auto-fill tag) | iOS `textContentType=none` |
| QA11y-11 | Sensitive field (L2-C, L5) — screen reader-də "həssas məlumat" hint | Manual test |

---

## 8. Hareket & Mikro-interactions

> project-context §0 — AI sehri YASAQ. Hareket minimal, funksional.

| Yer | Hareket | Müddət / easing |
|-----|---------|------------------|
| RadioCard seçimi | Border `volt` 2px → ikon fill | 150ms ease-out |
| MultiSelectChip toggle | bg `volt-glow` → `volt` (checked) | 120ms ease |
| Stepper +/− press | Sayaç tick + opacity pulse | 100ms; basılı tutma 50ms repeat |
| WheelPicker scroll | Native momentum | OS |
| Slider drag | Thumb pulse `volt-glow` | drag boyu |
| ProgressDot fill | Sıralı dolma | 300ms ease-out / dot |
| CTA enable transition | bg `surface-2 → volt` fade | 180ms ease |
| InlineError appear | Slide-down 8px + fade | 150ms ease-out |
| LiveRegion announce | (səssiz — screen reader) | — |
| Tibbi modal (L5 red flag) | Fade-in only — bounce YASAQ | 200ms |
| L2 group skip animation | Header tick "Ötürüldü" rozeti yumşaq | 200ms |

**Reduce Motion:** pulse, bounce dayanır; yalnız opacity fade qalır.

---

## 9. Analytics (sual ekranı üçün universal)

> Tam event spec: `prd-auth-onboarding-analytics-2026-05-22.md`. Bu cədvəl ÜMUMI matrisdir.

| Event | Trigger | Properties | Layer |
|-------|---------|------------|-------|
| `question_viewed` | Ekran render | `{ field_key, layer, screen_index }` | hamısı |
| `question_answered` | Submit ok | `{ field_key, value_bucket, layer, duration_ms }` | hamısı |
| `question_skipped` | Skip GhostBtn | `{ field_key, layer, group? }` | L2/L3 |
| `question_validation_error` | InlineError görünür | `{ field_key, error_code, attempt }` | hamısı |
| `l2_optin_shown` | Q7 sonrası modal | — | L2 |
| `l2_optin_response` | "Davam et" / "İndi yox" | `{ response }` | L2 |
| `l2_group_completed` | Qrup bitir | `{ group: A-F, fields_completed }` | L2 |
| `l2_completed` | Sonuncu qrup submit | `{ groups_completed, fields_total }` | L2 |
| `l5_medical_shown` | L5 trigger | `{ trigger_source }` | L5 |
| `l5_red_flag_triggered` | red flag answer | `{ condition }` | L5 |
| `l5_medical_completed` | Disclaimer accept | `{ conditions_count, ai_plan_unlocked: bool }` | L5 |
| `cohort_persona_cell` | Q1+Q2+Q6 tamamlandıqda compute | `{ persona_cell }` | L1 |

**PII qoruması:** value bucket (yaş bucket 13-17/18-24/25-34/...) yox raw; height bucket 5cm; weight bucket 5kg. Free-text field-lər (medications_current və s.) **value göndərilmir** — yalnız `has_value: bool`.

---

## 10. Sally → `.pen` Handoff Checklist

> `app/design/mobile/app_design.pen` — yeni ad (köhnə: `onboarding_flow.pen`). Bu sənədin §4 ekran inventarı `.pen` "Questions" sectionunun **build siyahısıdır**.

- [ ] **§1 Anatomy** master frame `.pen`-də 1 dəfə qurulur (`question_template`)
- [ ] **§3 Variant** hər biri component-level (`comp/radiocard`, `comp/stepper`, `comp/wheelpicker`, `comp/segmented`, `comp/multichip`, `comp/slider`, `comp/photocard`) — variant property ilə
- [ ] **§4.1 L1** — 7 ekran (Q1-Q7); Q3 special (Age Gate trigger)
- [ ] **§4.2 L2** — 6 qrup ekranı (A-F) + L2 opt-in modal
- [ ] **§4.3 L3** — 9 progressive sheet
- [ ] **§4.4 L5** — 4 medical screen + red flag modal
- [ ] **§5 Validation** — InlineError her variant üzərində audit (`search_all_unique_properties`)
- [ ] **§6 Copy** — `prd-user-profile-data-catalog` field-by-field copy AZ/RU/EN — `.pen` text node-larında **AZ default**
- [ ] **§7 A11y** — Dynamic Type 200% overflow audit
- [ ] **§8 Animation** — Reduce Motion variant property
- [ ] **Volt token audit** — `search_all_unique_properties` ilə hex hard-code yox; köhnə `#FF6B33` yox
- [ ] **Cross-link** — `ux-auth-onboarding §1 ekran #3, #5, #13-19, #20-26, #27, #28` "Questions" frame-lərinə pointer

---

## 11. Out-of-Scope (bu UX brief)

| Sahə | Ayrı sənəd |
|------|------------|
| Onboarding axın state machine, AuthGate, AIDisclosure | `ux-auth-onboarding-2026-05-22.md` |
| Dashboard / Home UX | next PRD |
| Settings ekranı tam dizayn (Settings hub, profile edit menu) | `prd-settings-deferred-2026-05-22.md` |
| Paywall video bg, iki seçim | `prd-paywall-deferred-2026-05-22.md` |
| Sample workout preview | `prd-sample-workout-preview-deferred-2026-05-22.md` |
| Workout logger / set-rep / superset UI | `prd-workout-execution` |
| Calorie tracker UI / foto-kalori | `prd-calorie-tracking` |
| Streak UI | `prd-workout-execution` |
| Weekly check-in PRD (gələcəkdə bu şablonu istifadə edəcək) | `prd-weekly-checkin` (planlanır) |

---

## 12. Changelog

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| **1.0** | **2026-05-23** | **Sally** | **İlk draft. `fitnessApp`-də form-bazlı bütün sual ekranları üçün vahid dizayn müqaviləsi. L1 (7), L2 (6 qrup), L3 (9), L5 (4) inventarı + Anatomy + State + Variant + Validation + Copy + A11y + Animation + Analytics. `app_design.pen` Questions section build siyahısı (`ux-auth-onboarding` ekran-axın siyahısından ayrı)** |

---

**End of UX Spec v1.0 — Questions Pages** · ready for `app_design.pen` "Questions" section wireframes (§4 ekran inventarı + §10 handoff checklist)
