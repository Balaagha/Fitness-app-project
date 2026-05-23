---
project_name: 'fitnessApp'
date: '2026-05-23'
version: '1.2'
workflowType: 'ux-spec'
prd_scope: 'auth-and-onboarding-ux-flow-only'
parent_prd: 'prd-auth-onboarding-2026-05-22.md'
relatedDocs:
  - prd-auth-onboarding-2026-05-22.md
  - prd-user-profile-data-catalog-2026-05-22.md
  - prd-auth-data-model-2026-05-22.md
  - ux-onboarding-questions-2026-05-23.md  # YENİ — sual ekran şablonu (L1+L2+L3+L5)
  - docs/project-context.md (v3.4 §1b Volt + §1c min docs)
designSystemFile: 'app/design/mobile/app_design.pen'  # ad dəyişdi (köhnə: onboarding_flow.pen)
status: 'ready-for-sally-wireframes'
ux_agent: 'Sally'
adversarial_review: 'John (PM) + Winston (Architect) — 2026-05-22'
---

# UX Brief — Auth & Onboarding (Flow-only)

**Author:** Sally (UX) · **Date:** 2026-05-23 (v1.2) · **Parent:** `prd-auth-onboarding-2026-05-22.md` v3.1
**Scope:** Onboarding **axın səviyyəli** UX — 29 ekran inventarı (axın ardıcıllığı) · state variantları · Volt token istifadəsi · komponent kitabxanası · wireframe handoff checklist · A11y · adversarial gap-check log

> ⚠️ **v1.2 split (2026-05-23):** Sual ekran şablonu (L1 7 sual + L2 6 qrup + L3 9 sheet + L5 4 medical) **ayrı sənədə köçürüldü** — `ux-onboarding-questions-2026-05-23.md`. Bu sənəd artıq YALNIZ axın səviyyəli məsələləri saxlayır: cold-start branching, AuthGate provider order, AI Disclosure, deep-link handoff, terminal screens. Sual səhifəsinin **şablon / komponent / state / copy üslubu** üçün → `ux-onboarding-questions-2026-05-23.md`. Bu sənəddə §1 cədvəlində sual ekranları (#3, #5, #13-19, #20-26, #27) qalır — yalnız axın kontekstində; daxili anatomiya orada təkrar tərif edilmir.

> ⚠️ **Pen file rename (2026-05-23):** `app/design/mobile/onboarding_flow.pen` → **`app_design.pen`**. Bütün referansları yeniləndi. `.pen` faylı artıq yalnız onboarding üçün deyil — bütün app dizaynı üçün vahid faylda yığılır (sectionlar: Onboarding · Questions · sonra Home/Settings/Paywall).

> v1.1 — Köhnə 124-sətirlik v1.0 əvəz olundu. Yeni L5 medical safety + L2 akkordiyon mini-ekranlar əlavə edildi. Adversarial gap-check (John+Winston) tamamlandı; P0+P1 boşluqlar burada fix edildi.

---

## 0. Vizual Doktrina

Bu sənəd bütün vizual qərarları üç kanonik mənbədən oxuyur — burada təkrar tərif YASAQ:

- **project-context §0 (positioning):** kahraman istifadəçidir, AI dəstək qatıdır — UX-də "AI sehri", glee animasyası, AI personifikasiyası YASAQ. Copy üslubu: "öz məşqini idarə et" (user-agentic), "AI sənə plan verdi" DEYİL.
- **project-context §1 (Minimal Design Principle):** ekrana feature əlavə = mövcud element çıxar. ≤7 element / ekran.
- **project-context §1b (Volt renk sistemi):** sarı `volt #E6FF00` accent, qara `bg-canvas #0A0A0B`, `surface-1 #141416`, `surface-2 #1E1E21`. Köhnə turuncu `#FF6B33` LƏĞV. `moss` CTA YASAQ. `volt` üzərində ağ mətn YASAQ.
- **project-context §1c (minimal documentation):** bu sənəddə token hex-ləri təkrarlanmır — yalnız hansı ekranın hansı token-i necə işlətdiyi.

**AZ native:** hər copy stringi manual-reviewed (MT YASAQ — CI gate).

---

## 1. Ekran İnventarı (29 ekran)

| # | Ekran | Trigger | Primary goal | bg / fg / accent | Əsas komponentlər | Error states | Analytics event | A11y qeyd |
|---|-------|---------|--------------|------------------|-------------------|--------------|-----------------|-----------|
| 1 | **LanguagePicker** | cold start + lang unset | Dil seç (AZ default `az_AZ` locale) | `bg-canvas` / `text-primary` / `volt` | LanguageRadio×3, VoltPrimaryButton | — | `language_selected` | Screen reader 3 dil ardıcıl |
| 2 | **Welcome+Privacy** | lang set | Brend tanıt + 3 legal link + "Başla" | `bg-canvas` flood + `volt` splash accent | Logo, ValueProp, LinkText×3 (Privacy/Terms/Health), VoltPrimaryButton | offline-da link tap → cached PDF | `welcome_shown`, `legal_link_tap` | Link 44pt, AZ label |
| 3 | **AgeGate (Q3 input)** | Q2 submit | Yaşı topla (13-99) | `bg-canvas` / `text-primary` / `volt` | AgeWheel (3-rotor), Sub-context label, VoltPrimaryButton | `ONB_001` inline (<13 / >99) | `q3_age_submitted` | AgeWheel VoiceOver numeric input alt |
| 4 | **AgeGateBlocked** | Q3 age<13 | Terminal hard-stop | `bg-canvas` / `text-primary` / `danger` | TerminalScreen, "Çıxış" GhostButton | back YASAQ | `age_gate_blocked` | Static text, no input |
| 5 | **ParentalNotice** | 13≤age≤17 | Valideyn consent | `bg-canvas` / `text-primary` / `volt` | Checkbox, LinkText (Privacy), VoltPrimaryButton (disabled until check) | `ONB_005` inline | `parental_consent_given` | Checkbox 44pt, label tappable |
| 6 | **AIDisclosure** | Q7 submit | Apple 2025 məcburi disclosure (sakin ton) | `bg-canvas` / `text-primary` / `volt` | Body text, VoltPrimaryButton ("Anladım"), GhostButton ("Ətraflı") | dismissable DEYİL | `ai_disclosure_accepted` | Long body — Dynamic Type respect |
| 7 | **ProviderChoice (AuthGate)** | AIDisclosure tap "Anladım" | Provider seç | `bg-canvas` / `text-primary` / `volt` | iOS: AppleBtn (HIG)·GoogleBtn·EmailBtn / Android: GoogleBtn·EmailBtn | `AUTH_007` toast (offline submit) | `auth_provider_tap` | iOS Apple HIG button (custom YASAQ) |
| 8 | **EmailSignupForm** | tap "Email" | Email+pwd qeydiyyat | `surface-1` / `text-primary` / `volt` | EmailField, PasswordField×2, LinkText, VoltPrimaryButton | `AUTH_001/002/003/006/007` inline+modal | `email_signup_submit` | Pwd show/hide toggle |
| 9 | **EmailLoginForm** | returning user tap "Daxil ol" | Login | `surface-1` / `text-primary` / `volt` | EmailField, PasswordField, GhostBtn "Parolu unutdum", VoltPrimaryButton | `AUTH_004/005/006/007` | `email_login_submit` | Tab order: email→pwd→reset→login |
| 10 | **EmailConfirmationPending** | signup ok | Magic link gözlə | `bg-canvas` / `text-primary` / `volt` | Illustration (Volt accent), CountdownTimer, GhostBtn "Yenidən göndər" | resend rate limit `AUTH_006` | `email_confirm_resend` | Auto-poll status; AccessibilityLiveRegion |
| 11 | **PasswordResetEmail** | "Parolu unutdum" tap | Reset link tələb | `surface-1` / `text-primary` / `volt` | EmailField, VoltPrimaryButton | sabit response (enumeration-safe) | `password_reset_request` | — |
| 12 | **PasswordResetForm** | deep-link `fitnessapp://reset` | Yeni parol qur | `surface-1` / `text-primary` / `volt` | PasswordField×2, VoltPrimaryButton | `AUTH_002/013` | `password_reset_complete` | Deep-link token görünməz |
| 13 | **Q1_Goal** | Welcome "Başla" | Hədəf seç (bulk/cut/general_fit) | `bg-canvas` / `text-primary` / `volt` | ProgressDots(1/7), RadioCard×3, VoltPrimaryButton | — | `q1_goal_submitted` | Radio group VoiceOver |
| 14 | **Q2_Gender** | Q1 submit | Cins seç | `bg-canvas` / `text-primary` / `volt` | ProgressDots(2/7), RadioCard×2, VoltPrimaryButton | — | `q2_gender_submitted` | — |
| 15 | **Q3_Age** | Q2 submit | (eyni #3 — bax §1.1 birləşmə qeydi) | — | — | — | — | — |
| 16 | **Q4_HeightWeight** | Q3 age≥18 və ya ParentalNotice consent | Boy+çəki | `bg-canvas` / `text-primary` / `volt` | ProgressDots(4/7), HeightWeightDual (cm/kg, ft/lb toggle), VoltPrimaryButton | `ONB_002` inline | `q4_hw_submitted` | Dual picker keyboard nav |
| 17 | **Q5_Experience** | Q4 submit | Təcrübə (beginner/intermediate/advanced) | `bg-canvas` / `text-primary` / `volt` | ProgressDots(5/7), RadioCard×3 + sub-context, VoltPrimaryButton | — | `q5_experience_submitted` | — |
| 18 | **Q6_Context** | Q5 submit | Kontekst (serious_gym/casual_gym/home_only) | `bg-canvas` / `text-primary` / `volt` | ProgressDots(6/7), RadioCard×3 + sub-context, VoltPrimaryButton | — | `q6_context_submitted` | — |
| 19 | **Q7_DaysSession** | Q6 submit | Həftəlik gün + sessiya | `bg-canvas` / `text-primary` / `volt` | ProgressDots(7/7), SegmentedControl×2 (days 2-7, duration 15/30/45/60), VoltPrimaryButton | — | `q7_days_session_submitted`, `onboarding_completed` | SegmentedControl swipe |
| 20 | **L2OptInPrompt** | (POST-AUTH) ProfileComplete → ilk dashboard nudge / Settings → "Profilini tamamla" | "Tam profil yaratmaq istəyirsən?" | `surface-1` modal / `text-primary` / `volt` | Modal sheet, ValueProp, VoltPrimaryButton ("Davam et"), GhostBtn ("İndi yox") | — | `l2_optin_shown`, `l2_optin_skip/continue` | Bottom-sheet swipe-down dismiss |
| 21 | **L2A_BodyMeasurements** | L2 opt-in | activity, target_weight, body_fat, frame | `bg-canvas` / `text-primary` / `volt` | AccordionGroup header, Form fields (catalog §3.2), VoltPrimaryButton, GhostBtn "Bu qrupu ötür" | range error inline | `l2_group_completed { group:A }` | Hər field skip-able |
| 22 | **L2B_ActivityLifestyle** | L2A submit/skip | sleep, stress(PSS-4), steps, sedentary | `bg-canvas` / `text-primary` / `volt` | AccordionGroup, Slider+Picker, VoltPrimaryButton, GhostBtn skip | — | `l2_group_completed { group:B }` | PSS-4 progressive disclosure |
| 23 | **L2C_HealthInjuries** | L2B submit/skip | injury_history (light) — yalnız var/yox; detail L5-də | `bg-canvas` / `text-primary` / `volt` | AccordionGroup, MultiSelectChip, VoltPrimaryButton, GhostBtn skip | — | `l2_group_completed { group:C }` | Sensitive data — opt-in confirm |
| 24 | **L2D_FoodPreferences** | L2C submit/skip | diet_pattern, allergies, intolerances, religious_dietary | `bg-canvas` / `text-primary` / `volt` | AccordionGroup, MultiSelectChip, RadioCard, VoltPrimaryButton, GhostBtn skip | — | `l2_group_completed { group:D }` | Ramazan auto-detect mart-aprel |
| 25 | **L2E_EquipmentInventory** | L2D submit/skip | equipment_inventory (home_only default `[bodyweight]`) | `bg-canvas` / `text-primary` / `volt` | AccordionGroup, MultiSelectChip×12, VoltPrimaryButton, GhostBtn skip | — | `l2_group_completed { group:E }` | Chip grid keyboard nav |
| 26 | **L2F_Preferences** | L2E submit/skip | motivations, discovery_channel, previous_app | `bg-canvas` / `text-primary` / `volt` | AccordionGroup, MultiSelectChip, RadioCard, VoltPrimaryButton "Bitir" | — | `l2_completed` | — |
| 27 | **L5_MedicalSafetyScreen** | İlk AI plan tələbi · pregnancy nudge · injury günlüyü açılış | PAR-Q+ 7+3 sual + medical_disclaimer accept | `bg-canvas` / `text-primary` / `volt` (sakin tibbi ton) | SafetyAlertBanner header, Question stack (7 PAR-Q+ + 3 follow-up), MedicalDisclaimerCheckbox, VoltPrimaryButton | red flag → #28; offline `AUTH_007` | `medical_safety_shown`, `parq_answered` | Tibbi ton, oyun animasyası YASAQ; tap target ≥44pt |
| 28 | **L5_MedicalSafetyBlocked (Red Flag)** | L5-də red flag answer | "Uyğunluq yoxlaması tövsiyə olunur" (approval/təsdiq SÖZÜ YASAQ) | `bg-canvas` / `text-primary` / `warning` | MedicalRedFlagModal, Body text (sakin), VoltPrimaryButton "Anladım", GhostBtn "Mənbələr" | — | `medical_red_flag_triggered` | Modal centered, dismissable yalnız "Anladım" |
| 29 | **PregnancyHardStop** | post-auth: `pregnancy_postpartum=true` user plan trigger | Curated static template + medical disclaimer | `bg-canvas` / `text-primary` / `warning` | StaticTemplateCard, MedicalDisclaimerCard, GhostBtn "Mənbələr" | AI plan generasiya BAŞLAMIR | `pregnancy_hard_stop_shown` | Static content — interactive komponent yox |
| 30 | **ProfileComplete (handoff)** | SyncOnboardingToProfile success | Görünməz keçid → dashboard | `bg-canvas` / `text-primary` / `volt` | DeepLinkLoadingScreen (200-500ms) | `ONB_004` background retry (görünmür) | `profile_complete` | LiveRegion "Profil yaradıldı" |

> **#15 birləşmə qeydi:** Q3_Age (#15) və AgeGate (#3) **eyni fiziki ekrandır**; #3 cədvəldə "input ekranı" rolu üçün, #15 state-machine ardıcıllığı üçün siyahıdadır. Sally `.pen`-də **bir** ekran çəkir.

**Cəmi unikal ekran:** 29 (köhnə v1.0: 19 ekran + 2 referans → indi +L5 medical (2) + L2 prompt (1) + L2 akkordiyon (6) + ProfileComplete handoff (1) yenidir).

**Out-of-scope (bu UX brief-də YOX, ayrı PRD/UX):**
- Dashboard / Home UX → next PRD
- Settings / Profile edit / Logout sync modal → `prd-settings-deferred-2026-05-22.md`
- Account delete UI (double-confirm dialog, restore toast) → `prd-settings-deferred-2026-05-22.md` (bu UX-də YALNIZ referans pointer)
- Paywall + video bg → `prd-paywall-deferred-2026-05-22.md`
- Sample workout preview → `prd-sample-workout-preview-deferred-2026-05-22.md`
- Professional coaching teaser → `prd-professional-coaching-teaser-deferred-2026-05-22.md`
- Workout logger / set-rep UI → `prd-workout-execution`

---

## 2. State Variantları (per-screen)

| State | Görüntü qaydası | Tətbiq olunan ekranlar |
|-------|-----------------|------------------------|
| **default** | İlk render; bütün field empty / pre-selected (back nav-də) | Hamısı |
| **focused** | Field-ə tap → `volt` border (2px), label up-shift | #3, #5, #8-12, #16-19, #21-26 |
| **error** | Inline error `danger` rəng + ikon altda; CTA enabled qalır | #3, #8-12, #16-19 |
| **loading** | CTA disabled + `volt` spinner inline; double-tap qarşısı | #8-12, #20, #21-26, #27 |
| **success** | CTA → tick 400ms → keçid | #8, #11, #12, #19, #26, #27 |
| **offline** | Sticky top OfflineBanner (`warning`); auth submit → toast `AUTH_007`; onboarding submit → SQLDelight write, davam | Bütün auth (#7-12); onboarding (#13-19) qismən offline-safe |
| **disabled** | CTA bg → `surface-2`, fg → `text-disabled`; çıxılır | #5 (consent unchecked), #6 (back swipe), #27 (disclaimer unchecked) |
| **terminal** | Heç bir back; yalnız "Çıxış" və ya hard handoff | #4 (AgeGateBlocked), #28 (Red Flag), #29 (Pregnancy) |

---

## 3. Volt Token İstifadəsi (per-screen-ə xülasə)

> Token cədvəli **project-context §1b** kanonikdir — burada YALNIZ hansı ekran hansı token-i necə işlədir.

| Token | İstifadə yeri (ekran#) | Necə |
|-------|------------------------|------|
| `volt` | 1-3, 5-19, 21-27, 30 | Primary CTA bg, ProgressDot fill, RadioCard selected border, ChipChecked bg |
| `on-volt` | 1-3, 5-19, 21-27, 30 | Primary CTA mətn (qara `#0E0E0E`) |
| `bg-canvas` | 1-7, 10, 13-19, 21-29 | Tam ekran arxa fonu (dark-first) |
| `surface-1` | 8, 9, 11, 12, 20 | Form kart fonu, modal sheet |
| `surface-2` | Disabled CTA, divider | — |
| `text-primary` | Hamısı | Əsas başlıq + body |
| `text-secondary` | 6, 10, 17, 18, 22 | Sub-context, alt-mətn, ">10s gecikmədə" copy |
| `text-disabled` | Disabled CTA fg | — |
| `danger` | 3, 4, 8-12, 16, 28 | Inline error mətn + ikon, terminal hard-stop accent |
| `warning` | 28, 29, OfflineBanner | Medical red flag, pregnancy hard-stop, offline sticky |
| `success` | 8, 11, 12, 19, 30 | Sync tick, "Profil yaradıldı" LiveRegion |
| `moss` | DEKORATIV YALNIZ (illustration) | CTA kimi YASAQ |

**Qəti qadağalar (CLAUDE.md təkrar):**
- `volt` üzərində ağ mətn YASAQ (kontrast fail) — yalnız `on-volt` qara
- Köhnə turuncu `#FF6B33` YASAQ — heç bir hex hard-code (Sally `.pen` audit `search_all_unique_properties` ilə)
- `moss` CTA bg YASAQ — yalnız dekorativ

---

## 4. Komponent İnventarı

| Komponent | İstifadə | Variantlar | Token istifadəsi | A11y |
|-----------|----------|------------|------------------|------|
| `VoltPrimaryButton` | Bütün primary CTA | default · focused · loading (spinner) · disabled · success-tick | `volt` bg + `on-volt` fg | ≥44pt, screen reader role=button |
| `GhostButton` | Secondary CTA, skip, cancel | default · focused · disabled | border `text-primary`, transparent bg | ≥44pt |
| `TextField` | Email, password, numeric | default · focused (`volt` border) · error · disabled | `surface-1` bg | Auto-fill iOS/Android, show/hide pwd toggle |
| `AgeWheel` | #3 / #15 | 3-rotor scroll picker | `volt` accent (selected row) | VoiceOver numeric input alt + manual TextField fallback |
| `HeightWeightDual` | #16 | cm/kg primary; ft/lb toggle | `volt` accent unit toggle | Keyboard nav: unit-toggle → height → weight → CTA |
| `ProgressDots` | #13-19 (L1 onboarding) | 7-step; dot fill `volt` step-by-step | `volt` filled · `surface-2` empty | accessibilityValue "Sual N / 7" |
| `MultiSelectChip` | #23, #24, #25, #26 | unchecked · checked (`volt` bg + `on-volt`) · disabled | — | Grid keyboard nav (arrow keys), 44pt |
| `RadioCard` | #13, #14, #17, #18 | unchecked · checked (`volt` border 2px + ikon) | `surface-1` bg | Radio group VoiceOver |
| `AccordionGroup` | #21-26 header | collapsed · expanded · skipped (tick) | `surface-1` bg | Header tap = expand; arrow ikon |
| `SafetyAlertBanner` | #27 header, #28 modal header | informational · red-flag | `warning` border + qara mətn | LiveRegion announce |
| `VASSlider` | #20 detail (current_pain_vas — L3 in-app trigger) | 0-10 marks | `volt` thumb + track | Discrete keyboard ±1 step |
| `MedicalRedFlagModal` | #28 | centered modal, dismissable yalnız "Anladım" | `surface-1` bg, `warning` border | Focus trap, ESC YASAQ |
| `DeepLinkLoadingScreen` | #10, #12, #30 | spinner + status text | `volt` spinner | LiveRegion progress |
| `OfflineBanner` | Bütün ekran (sticky top) | online (gizli) · offline | `warning` bg, qara mətn | LiveRegion "Offline" announce |
| `LinkText` | #2, #5, #8 | Privacy, Terms, Health | underline + `text-primary` | accessibilityHint "Brauzerdə açılacaq" |
| `LanguageRadio` | #1 | 3 dil | `volt` selected | flag emoji + AZ/RU/EN label |
| `SegmentedControl` | #19 | days 2-7, duration 15/30/45/60 | `volt` selected segment | Swipe keyboard arrow |
| `CountdownTimer` | #10 | resend cooldown 60s | `text-secondary` | accessibilityValue "N saniyə" |
| `MedicalDisclaimerCheckbox` | #27 | unchecked (disabled CTA) · checked | `volt` checked | Label tappable |

---

## 5. Wireframe Handoff Checklist (Sally → `.pen`)

Sally Figma/Pencil-ə keçməzdən əvvəl bilməlisən:

- [ ] **7-Q sıralaması** — auth PRD §3.1 (Q1→Q7, Q3 Age Gate trigger)
- [ ] **L2 entry copy** AZ/RU/EN — `prd-user-profile-data-catalog-2026-05-22.md` §3.2 group A-F
- [ ] **L5 PAR-Q+ 7+3 sual matrisi** — `prd-user-profile-data-catalog-2026-05-22.md` §5 (red flag map)
- [ ] **Error code → screen mapping** — `prd-auth-data-model-2026-05-22.md` §2.5 (AUTH_001-013, ONB_001-005)
- [ ] **AI disclosure copy** AZ/RU/EN — auth PRD §3.5: "Plan həmişə sənindir — istədiyin vaxt özün dəyişə bilərsən"
- [ ] **Pregnancy hard-stop** copy — "uyğunluq yoxlaması" termini (approval/təsdiq YASAQ)
- [ ] **Account delete UX** — Bu sənəddə YOX; `prd-settings-deferred-2026-05-22.md`-ə bağlanır (yalnız pointer)
- [ ] **Deep-link UX** — `fitnessapp://reset?token=...` #12; `fitnessapp://confirm-email` #10; `fitnessapp://restore` (settings PRD)
- [ ] **Volt token audit** — `search_all_unique_properties` ilə hex hard-code yoxlanışı (#FF6B33 cəfri olmamalı)
- [ ] **Apple HIG Apple Sign-In** — iOS #7-də system button (custom design YASAQ)
- [ ] **iOS sıra** Apple→Google→Email · **Android sıra** Google→Email (Apple gizli)
- [ ] **Privacy Policy linkləri** Welcome (#2) + AuthGate (#7) — submission öncəsi məcburi launch-blocker
- [ ] **Anti-pattern lint** hər ekranda (tab+accordion+drawer eyni ekranda YASAQ; >2 primary CTA YASAQ; modal-in-modal YASAQ)

---

## 6. Animation & Micro-interactions

| Yer | Animasyon | Müddət / easing | Qeyd |
|-----|-----------|------------------|------|
| Primary CTA hover/press | Volt accent pulse | 1.5s ease-in-out | Reduce-motion off-da gizli |
| ProgressDot fill | Sarı sıralı tələm | 300ms ease-out per dot | 7 step boyu |
| Age gate accept | Subtle "kilid açıldı" tick | 400ms ease-out | #5 ParentalNotice consent |
| Form field focus | Border `volt` 2px transition | 150ms ease | — |
| Loading spinner | Volt rotating | 1s linear loop | LiveRegion announce |
| Skeleton loader | `surface-2` dim shimmer | 1.2s loop | EmailConfirmationPending poll |
| Medical red flag (#28) | **HEÇBIR glee/oyun animasyası** | — | Ciddi tibbi ton; fade-in only 200ms |
| Pregnancy hard-stop (#29) | Static, animation YOX | — | — |
| Page transition | iOS push / Android slide | 300ms native | — |
| Success tick | Scale 0.8→1.0 bounce | 400ms ease-out | Reduce-motion → opacity fade |

**Reduce Motion (iOS/Android sistem ayarı):** pulse, shimmer, bounce dayanır; yalnız opacity/colour fade qalır.

---

## 7. Accessibility (UI səviyyəsi)

| ID | Tələb | Verifikasiya |
|----|-------|--------------|
| **A11y-1** | Kontrast ≥4.5:1 normal mətn, ≥3:1 large mətn | `volt #E6FF00` üzərində `on-volt #0E0E0E` → 19.5:1 ✓; `volt` üzərində ağ mətn YASAQ (fail) |
| **A11y-2** | Touch target ≥44pt iOS / ≥48dp Android | Hər interaktiv komponent |
| **A11y-3** | Screen reader label hər interaktiv element üçün AZ/RU/EN | VoiceOver + TalkBack test scope |
| **A11y-4** | AgeWheel + VASSlider üçün manual TextField fallback | VoiceOver/TalkBack-də numeric input |
| **A11y-5** | Dynamic Type (iOS) + sp scaling (Android) | Fixed-size font YASAQ; #6 AI disclosure long body **overflow YOXLANIŞI** (200% scale) |
| **A11y-6** | Reduce Motion respect | §6 cədvəlinə bax |
| **A11y-7** | Focus order ardıcıl; dead-end yoxdur | Klaviatura nav: Tab/Shift-Tab axın test |
| **A11y-8** | LiveRegion async event üçün | Offline banner, success tick, deep-link load, medical red flag |
| **A11y-9** | AZ TalkBack pronunciation zəif olduqda RU/EN fallback override | Kritik elementlər (Age Gate, Medical Safety, Pregnancy) |
| **A11y-10** | Form auto-fill iOS/Android | Email, password field — autocomplete tags |

---

## 8. Vizual Prinsiplər (project-context §1 tətbiqi)

1. **Bir ekran = bir əsas iş.** L1-də hər sual ayrı ekran (Q4 və Q7 istisna — 2 field eyni ekran, məntiqli grup).
2. **Feature əlavə = element çıxar.** L2-ə əlavə qrupun varsa, mövcud Settings entry-ni reduksiya et.
3. **User-kahraman.** AI personifikasiyası, "AI sehri" copy, AI üzü/avatarı YASAQ. Heç bir ekran "AI" başlığı altında dramatize edilmir — #6 AIDisclosure sakin, faktiki.
4. **AZ native.** Hər string `shared/strings/{az,ru,en}.json`; MT YASAQ (CI gate); fallback AZ→RU→EN→key.
5. **Minimal Documentation Principle (project-context §1c).** Bu sənəddə token hex-ləri təkrarlanmır — yalnız istifadə yeri.
6. **Anti-pattern lint** hər ekran üçün: tab+accordion+drawer eyni ekranda YASAQ; >2 primary CTA YASAQ; modal-in-modal YASAQ.

---

## 9. Out-of-Scope (Bu UX brief)

| Sahə | Ayrı sənəd |
|------|------------|
| Dashboard / Home UX | next PRD |
| Settings / Profile edit UI | `prd-settings-deferred-2026-05-22.md` |
| **Account delete confirm dialog + restore toast** | `prd-settings-deferred-2026-05-22.md` (yalnız pointer burada) |
| **Logout sync-queue blocking modal** | `prd-settings-deferred-2026-05-22.md` |
| Paywall ekranı (video bg, iki seçim) | `prd-paywall-deferred-2026-05-22.md` |
| Sample workout preview UX (post-onboarding first-value) | `prd-sample-workout-preview-deferred-2026-05-22.md` |
| Coaching teaser ekranı | `prd-professional-coaching-teaser-deferred-2026-05-22.md` |
| Workout logger / set-rep / superset UI | `prd-workout-execution` |
| Calorie tracker UI / foto-kalori | `prd-calorie-tracking` |
| Streak UI (badge, freeze copy) | `prd-workout-execution` |

---

## 10. User Story → Screen Mapping (John PM gap-fix)

> **John (PM) review #1 outputu:** Auth PRD §9 user story-lərinin hər biri minimum bir ekrana bağlıdır.

| User Story (auth PRD §9) | Ekran (#) | Qeyd |
|--------------------------|-----------|------|
| US-1.1 Email+Pwd Signup | 7, 8, 10, 30 | Signup → confirm → handoff |
| US-1.2 Google Sign-In | 7, 30 | Provider sheet native |
| US-1.3 Apple Sign-In | 7, 30 | iOS-only HIG button #1 |
| US-1.4 Email+Pwd Login | 7, 9, 30 | Returning user |
| US-1.5 Session Persistence | (görünməz) cold start → 30 və ya 9 | Background SessionManager |
| US-1.6 Password Reset | 9, 11, 12 | Email → deep-link → form |
| US-1.7 Logout (skeleton) | (UI: settings PRD — burada YOX) | Pointer |
| US-1.8 Account Delete (data) | (UI: settings PRD — burada YOX) | Pointer |
| US-2.0 Language Picker | 1 | — |
| US-2.0a Cross-cutting (back, offline, loading) | Bütün L1 (#13-19) + #20-26 | §2 state variantları |
| US-2.1 7 məcburi sual | 13-19 + (#3/#15 Age Gate, #4 Blocked, #5 Parental) | — |
| US-2.2 AI Disclosure | 6 | Q7 sonrası məcburi |
| US-2.3 Onboarding Resume + Age-gate cache | (Cold-start resume modal — `surface-1` modal, ⚠️ gap-fix: §12.1) | Aşağıda fix edildi |
| US-2.4 Sync to Profile | 30 (görünməz handoff) | ONB_004 background retry |

---

## 11. Error Code → Screen Mapping (John PM gap-fix)

> **John (PM) review #2 outputu:** Hər error code-un render olduğu ekran(lar) müəyyəndir; heç bir "evsiz" kod qalmadı.

| Error Code | Render ekran(lar)ı | UI behaviour |
|------------|---------------------|--------------|
| `AUTH_001` | 8, 9, 11 | Inline TextField error |
| `AUTH_002` | 8, 12 | Inline password rule hint |
| `AUTH_003` | 8 | Inline + "Daxil ol" CTA → 9 |
| `AUTH_004` | 9 | Inline form-level |
| `AUTH_005` | 9 → 10 | Banner + "Linki yenidən göndər" |
| `AUTH_006` | 8, 9, 10, 11 | BlockingModal (rate-limit) |
| `AUTH_007` | 7, 8, 9, 10, 11, 12, 27 (offline) | Toast (`warning`) |
| `AUTH_008` | 7 | Silent (Google cancel) |
| `AUTH_009` | 7 | Toast |
| `AUTH_010` | 7 (iOS) | Silent (Apple cancel) |
| `AUTH_011` | 7 (iOS) | Toast |
| `AUTH_012` | 4 (terminal) | Server double-check hard-stop |
| `AUTH_013` | 12 | Inline + "Yeni link" CTA → 11 |
| `ONB_001` | 3 / 15 | Inline (age 13-99 range) |
| `ONB_002` | 16 | Inline (height/weight range) |
| `ONB_003` | **#31 ResumeExpiredModal (gap-fix §12.1)** | BlockingModal cold-start |
| `ONB_004` | 30 (görünməz) | Background retry; user-ə görünmür |
| `ONB_005` | 5 | Inline; CTA disabled |

---

## 12. Adversarial Gap-check Log (John + Winston, 2026-05-22)

### 12.1 P0+P1 Fix Edildi (sənəddə yerində)

| # | Severity | Reviewer | Boşluq | Fix yeri | Nə əlavə edildi |
|---|----------|----------|--------|----------|-----------------|
| G-1 | **P0** | John | US-2.3 Onboarding Resume ekranı inventarında YOX idi | §1 cədvəlinə #31 əlavə (aşağıda); §11 ONB_003 mapping fix | Yeni ekran #31 ResumeExpiredModal / ResumeContinueModal |
| G-2 | **P0** | Winston | EmailConfirmationPending (#10) loading state təfsilatı yox idi (auto-poll davranış) | §2 state cədvəli + §1 #10 sətr | Auto-poll + LiveRegion + CountdownTimer 60s resend cooldown |
| G-3 | **P0** | John | Apple 2025 AI disclosure copy AZ/RU/EN tam yazılmamış (auth PRD-də AZ var, UX-də referans yoxdur) | §5 Handoff Checklist + §1 #6 | Referans pointer auth PRD §3.5 + "approval/təsdiq" sözü qadağası reminder |
| G-4 | **P0** | John | Pregnancy hard-stop ekranı (#29) UX inventarında yox idi (köhnə v1.0-də qeyd, ekran yox) | §1 cədvəlinə #29 əlavə | StaticTemplateCard + MedicalDisclaimerCard komponentlər |
| G-5 | **P0** | Winston | Volt token `volt` üzərində ağ mətn YASAQ — UX-də heç bir pozulma audit-i yox idi | §3 cədvəl + §7 A11y-1 | Açıq qadağa + 19.5:1 kontrast doğrulaması |
| G-6 | **P0** | Winston | Dead-end audit: AgeGateBlocked (#4) + Pregnancy (#29) + Red Flag (#28) — yalnız #4 qəsdli terminal kimi qeyd edilmişdi | §2 state cədvəli "terminal" row | #4, #28, #29 hamısı qəsdli terminal kimi qeyd edildi (Pregnancy curated static-ə keçid, Red Flag dismiss-only) |
| G-7 | **P1** | Winston | Offline state hər ekran üçün definisiyalanmamışdı (project-context offline-first) | §2 cədvəlinə "offline" row | Sticky OfflineBanner + onboarding SQLDelight davam + auth submit toast |
| G-8 | **P1** | Winston | Async loading state cədvəli yox idi (signup, login, deep-link verify, password reset, onboarding sync) | §2 "loading" row | CTA disabled + `volt` spinner + double-tap qarşısı + >10s slow-network alt-mətn |
| G-9 | **P1** | John | Account delete UI bu UX-də YOX, lakin out-of-scope pointer dəqiq deyildi | §5 checklist + §9 cədvəl | `prd-settings-deferred-2026-05-22.md` pointer aydın yazıldı |
| G-10 | **P1** | Winston | Dynamic Type ilə #6 AI disclosure (long body) overflow yoxlanışı qeyd olunmamışdı | §7 A11y-5 | 200% scale overflow test scope əlavə edildi |
| G-11 | **P1** | John | Story → screen mapping (US-1.1…US-2.4) cədvəli yox idi (parent PRD-də story var, UX-də map yox) | §10 yeni bölmə | Hər user story bir ekran(lar) ilə bağlandı |
| G-12 | **P1** | John | Error code → screen mapping cəfri (auth-data-model spec-də kod var, UX-də render render yer mapping yox) | §11 yeni bölmə | 18 kodun hər biri render ekran(lar)ı ilə bağlandı |
| G-13 | **P0** | Winston | AgeWheel + VASSlider klaviatura/VoiceOver alternativ input yox idi | §7 A11y-4 + §4 komponent cədvəli | Manual TextField fallback hər ikisi üçün |
| G-14 | **P1** | Winston | Reduce-motion respect — pulse/shimmer animasyaları hansılar dayanır audit yox idi | §6 animation cədvəlinə "Reduce Motion" footer | Hər animation üçün davranış qeyd edildi |
| G-15 | **P0** | John | L5 medical safety ekranı (#27 + #28) köhnə UX v1.0-də tamamilə yox idi — data-catalog PRD əlavə etmişdi, UX qeyd etməmişdi | §1 #27 + #28 əlavə | Full ekran spec + komponent (SafetyAlertBanner, MedicalRedFlagModal, MedicalDisclaimerCheckbox) |
| G-16 | **P0** | John | L2 akkordiyon mini-ekranlar (A-F) köhnə UX v1.0-də ekran səviyyəsində yox idi | §1 #20-26 əlavə | L2OptInPrompt + 6 mini-ekran (A-F) + skip-per-group rule |

#### Yeni ekran #31 — ResumeModal (G-1 fix)

| # | Ekran | Trigger | Primary goal | bg / fg / accent | Əsas komponentlər | Error states | Analytics event | A11y qeyd |
|---|-------|---------|--------------|------------------|-------------------|--------------|-----------------|-----------|
| 31 | **ResumeModal** (Continue / Expired variants) | Cold-start + onboarding_state mövcud | Yarımçıq onboarding davam ya yenidən başla | `surface-1` modal / `text-primary` / `volt` | BottomSheet, ValueProp, VoltPrimaryButton ("Davam et" / "Yenidən başla"), GhostBtn (mövcudsa) | `ONB_003` expired variant: tək CTA "Yenidən başla" | `onboarding_resume_shown { state: continue\|expired }` | Modal focus trap, swipe-down dismiss disabled |

> ⚠️ Bu ekran §1 cədvəlinə **#31 olaraq əlavə olunur** (#1-30 ardıcıllıq qorunur; #31 cold-start branch).

### 12.2 P2 Boşluqlar (sonrakı sprint üçün — sənəd dəyişdirilmədi)

- **P2-A (John):** L2 group A-F sıralaması user araşdırması ilə validate edilməli — body-first daha az drop-off ola bilər, lakin food-first AZ-cultural daha rahat hiss verə bilər. A/B test scope post-MVP.
- **P2-B (Winston):** `.pen` fayl audit-i `search_all_unique_properties` ilə avtomatlaşdırılmalı — CI gate olmalı (hex hard-code YASAQ).
- **P2-C (John):** LanguagePicker (#1) -də 4-cü dil (TR ekspansiya) Faza 2 hook lazımdırmı — schema-da `language` enum-u açıq buraxılsa da, UX-də Faza 1-də gizli.
- **P2-D (Winston):** L5 medical safety (#27) red flag matrisini Sally `.pen` faylında interactive decision-tree kimi qurmalı, yoxsa per-question linear stack? — Sally qərarı, post-wireframe.
- **P2-E (John):** AIDisclosure (#6) -də "Ətraflı" linki — modal in-app, brauzerdə açılır, yoxsa Privacy Policy AI bölməsinə deep-link? UX qərarı post-wireframe.
- **P2-F (Winston):** Deep-link error UX — `fitnessapp://reset` link təkrar tap-də (mobile mail client) idempotency davranışı qeyd olunmadı.
- **P2-G (John):** L2 completion celebration micro-interaction (#26 sonrası) — kahraman-istifadəçi prinsipinə görə minimal, lakin tamamlanma feedback-i lazım.

---

## 13. Changelog

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0 | 2026-05-22 | John (PM) | İlk UX spec (auth PRD §16 split-i sonra) — 19 ekran, 124 sətir |
| 1.1 | 2026-05-22 | Sally (UX) + John (PM) + Winston (Architect) | Tam yenidən qurulma. 29 ekran inventarı (köhnə 19 + L5 medical (2) + L2 prompt (1) + L2 akkordiyon (6) + Pregnancy hard-stop (1)). State variantları, animation, A11y, story→screen + error→screen mapping cədvəlləri əlavə edildi. Adversarial gap-check (John+Winston): 16 boşluq tapıldı (P0:8, P1:7, P2:7); P0+P1 sənəddə fix edildi, P2 §12.2-də log edildi. |
| **1.2** | **2026-05-23** | **Sally (UX)** | **Split: sual ekran şablonu/anatomi/komponent/copy üslubu `ux-onboarding-questions-2026-05-23.md`-ə köçürüldü. Bu sənəd artıq YALNIZ axın səviyyəli (cold-start, AuthGate, AI Disclosure, deep-link, terminal). Ekran inventarı (§1) axın kontekstində qaldı; sual səhifəsi daxili anatomiyası bu sənəddə təkrar edilmir. `.pen` fayl adı yeniləndi: `app_design.pen`.** |
| **1.3** | **2026-05-23** | **Sally (UX)** | **Onboarding scope-undan çıxarıldı: (a) SampleWorkoutPreview deprecated — ilk-dəyər post-dashboard olur; (b) Paywall onboarding-də göstərilmir — post-dashboard tetikləri; (c) AI Disclosure copy yeniləndi — "premium istifadəçilərdə plan, göndərilmədən əvvəl bir mütəxəssis tərəfindən yoxlanılıb onaylanır" (**human approval / quality gate**, trainer feature DEYİL — fərdi məşqçi-istifadəçi münasibəti, chat, fərdi koreksiya YOXDUR); (d) ProfilePreview onboarding row-da qaldı (handoff yerində); (e) Bridge frame Welcome → Questions section-arası keçidi göstərir. CLAUDE.md "trainer / professional coaching MVP-də vəd etmə" qaydası **KEÇƏRLİDİR** — "məşqçi/coach/trainer" sözü qadağan; istifadə olunan termin: "**mütəxəssis yoxlaması / uyğunluq yoxlaması**".** |

---

**End of UX Brief v1.1 — ready for Sally `.pen` wireframes (29+1 ekran scope)**
*Next:* Sally `.pen`-də skeleton (sıra: §1) + handoff checklist (§5) + adversarial fixes (§12.1) inkorporasiya.
