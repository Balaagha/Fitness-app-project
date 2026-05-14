---
project_name: 'fitnessApp'
user_name: 'Balaagha'
date: '2026-05-12'
workflowType: 'prd'
prd_scope: 'auth-and-onboarding'
phase: 'MVP / Faza 1'
stepsCompleted: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
inputDocuments:
  - CLAUDE.md
  - docs/project-context.md
  - _bmad-output/planning-artifacts/research/market-azerbaijan-ai-fitness-app-research-2026-05-12.md
  - .claude/features/determine-scope-of-start-up/feature.md
relatedPRDs:
  - prd-ai-plan-generation (planlanır)
  - prd-workout-execution (planlanır)
  - prd-calorie-tracking (planlanır)
  - prd-billing-revenuecat (planlanır)
hardConstraintsRef: 'CLAUDE.md → Hard Constraints'
---

# Product Requirements Document — Authentication & Onboarding

**Author:** Balaagha
**Date:** 2026-05-12
**PM Agent:** John (bmad-agent-pm)
**Product:** fitnessApp — Azərbaycan bazarına yönəlmiş AI-dəstəkli fitness tətbiqi
**PRD Scope:** Auth (signup/login/session) + First-run Onboarding (≤8 sual) + Profile creation + First-value preview

---

## 0. Executive Summary

Bu PRD `fitnessApp` MVP-nin **giriş qapısını** təyin edir: istifadəçinin app-ı açdığı andan **ilk dəyəri (sample workout preview)** gördüyü ana qədər olan axın. Üç problemi həll edir:

1. **Drop-off riski** — Rəqib BetterMe 26 sualla onboarding edir; D1 retention sektörel orta 23%-dir. Bizim ≤8 sual + value-first preview = activation funnel-i fərqləndirir.
2. **Trust differensiator** — Onboarding zamanı opaque billing prompt yox; **paywall preview-dan SONRA** gəlir. BetterMe/Freeletics-in ən böyük şikayət vektorunu (BBB + Trustpilot + Sikayetvar) struktur olaraq aradan qaldırır.
3. **Native AZ keyfiyyəti ilk təəssürat** — Dil seçimi default AZ, bütün stringlər manual-reviewed; istifadəçi ilk 3 ekranda BetterMe MT-dən fərqi hiss edir.

**Solo dev realism:** Supabase Auth + KMM shared module + ≤8 sual = ~3-4 həftə implementasiya. Custom backend yox, third-party auth provider zoo yox.

---

## 1. Vision & Goals

### 1.1 Product Vision (bu PRD parçası üçün)

> "Azərbaycanlı istifadəçi tətbiqi açır, **90 saniyə içində** ilk məşqini önizləməsi olur — opaque billing prompt-suz, 26 sual marathon-suz, native AZ keyfiyyətində."

### 1.2 Goals (Bu PRD üçün)

| # | Goal | Ölçü |
|---|------|------|
| G1 | Activation-a sürtüşməni minimuma endir | Onboarding median tamamlanma müddəti **≤90 saniyə** |
| G2 | Trust gradient qurmaq | Paywall-dan ƏVVƏL ilk value (sample workout) göstərmək |
| G3 | Profil keyfiyyəti | ≥95% istifadəçinin AI plan generasiya üçün lazımi 8 sahəni doldurması |
| G4 | Cross-platform tutarlılıq | iOS + Android arasında onboarding state %100 paritet |
| G5 | Offline-safe başlanğıc | Profil cavabları lokal saxlanır, internet bərpa olunca sync edir |
| G6 | Native AZ etibar | İlk 3 ekranda 0 machine-translated string |

### 1.3 Non-Goals (bu PRD-də ETMƏYECƏYIK)

- AI workout plan generasiyası (ayrı PRD)
- Workout execution / exercise library (ayrı PRD)
- Calorie tracker UI / food DB (ayrı PRD)
- RevenueCat paywall ekranı tam implementasiya (ayrı PRD — burada yalnız **touchpoint**: sample workout preview-dən sonra paywall trigger)
- Sosial login provider-lər (Facebook, X, LinkedIn) — out-of-scope
- Phone-OTP auth — out-of-scope (SMS cost + AZ telco fragmentation)
- Multi-factor authentication (2FA) — Faza 2
- Account deletion / GDPR data export UI — Faza 1 P1 (mütləq ship, lakin ayrı sub-spec)

---

## 2. Personas (Market Research-dən törəyən)

### Persona A — "Şəhərli professional qadın" (PRIMARY)
- 25-35 yaş, Bakı/Sumqayıt, orta+ gəlir
- Cihaz: iPhone 12+ (yüksək nisbət), bəzən Android orta-yuxarı
- Dil: AZ (default), İngilis ikinci, Rus seçim
- Auth tərcihi: **Apple Sign-In** üstünlük (iPhone), Email backup
- Davranış: Marathon onboarding-i tərk edir; "uşaq yatdı, 5 dəqiqəm var" pattern
- Acceptance pain point: BetterMe-də USD billing + opaque trial → bizdə AZN şəffaflıq

### Persona B — "Gənc kişi zal-go-er" (SECONDARY)
- 18-30 yaş, kişi, Bakı + regional mərkəzlər
- Cihaz: Android dominant (affordability)
- Dil: AZ + Rus qarışıq, İngilis OK
- Auth tərcihi: **Google Sign-In** üstünlük
- Davranış: Qiymət-həssas, free tier-ı test edir, sübut istəyir → sample workout preview konversiya driver-i
- Acceptance pain point: Subscription forced before value = instant uninstall

### Persona C — "Yaşlı sağlamlıq-driven" (TERTIARY)
- 35-50 yaş, hər iki cins
- Cihaz: Android (regional), iPhone (Bakı sub-set)
- Dil: AZ + Rus güclü, İngilis məhdud
- Auth tərcihi: **Email + parol** (sosial sign-in-ə şübhəli)
- Davranış: Yavaş, anlamaq istəyir, "niyə sual verirsən?" sorgular → kontekst label-ları vacib
- Acceptance pain point: Kiçik font, mürəkkəb terminologiya

---

## 3. User Journeys (high-level)

### 3.1 Happy Path — Yeni İstifadəçi
```
App-ı aç → Dil seçimi (AZ default) → "Başla" CTA
  → Onboarding Q1-Q8 (single-screen-per-question, swipeable)
  → Profil hesablanır (BMR/TDEE preview göstərilir)
  → Auth gate: "Nəticələrini saxlamaq üçün hesab yarat"
     ├─ Apple Sign-In  ┐
     ├─ Google Sign-In ├─→ Supabase Auth → JWT → session
     └─ Email + parol  ┘
  → Sample workout preview (1 ekzersiz, ad+video+set/rep)
  → Paywall trigger (ayrı PRD) — value-first, sürtüşmə az
```

### 3.2 Returning User — Login
```
App-ı aç → Splash → Local session check (KMM Keychain/Keystore)
  ├─ Valid session → Home (onboarded profil var)
  └─ Expired/missing → Login ekranı (Apple/Google/Email) → Home
```

### 3.3 Password Reset
```
Login ekranı → "Parolu unutdum" → Email input → Supabase magic link
  → Email-də link → Deep link app-a açır → Yeni parol formu → Login
```

### 3.4 Offline Onboarding
```
Internet yox → Onboarding eyni → cavablar SQLDelight-də saxlanır
  → Auth ekranı: "Internetə qoşul və hesab yarat" deferred
  → Internet gələndə → Auth → Supabase-də user_profiles upsert (offline state-dən sync)
```

---

## 4. User Stories & Acceptance Criteria

### Epic 1 — Authentication

**US-1.1** — Email + Parol Signup
> Persona C kimi, mən **email və parol ilə hesab yaratmaq istəyirəm**, çünki **sosial sign-in-ə şübhəliyəm və data-mın harada olduğunu bilmək istəyirəm**.

**Acceptance:**
- Given Login/Signup ekranı, When "Email ilə qeydiyyat" seçilir, Then email + parol + parol təsdiq sahələri görünür
- Given valid email + parol (≥8 char, ≥1 rəqəm), When "Qeydiyyatdan keç" basılır, Then Supabase Auth `signUp` çağırılır
- Given Supabase email confirmation aktiv, When signup uğurlu, Then "Email-ini təsdiqlə" ekranı + magic link göndərilir
- Given təsdiq olunmamış email, When login cəhd edilir, Then "Email təsdiq olunmayıb, yenidən link göndər" CTA göstərilir
- Given parol < 8 char və ya rəqəmsiz, When submit, Then inline error AZ-da: "Parol ən azı 8 simvol və 1 rəqəm olmalıdır"

---

**US-1.2** — Google Sign-In
> Persona B kimi, mən **Google ilə tək toxunuşla daxil olmaq istəyirəm**, çünki **Android cihazımda Google hesabım onsuz da aktivdir**.

**Acceptance:**
- Given Login ekranı, When "Google ilə davam et" basılır, Then native Google Sign-In sheet açılır
- Given user Google hesabı seçir, Then Supabase Auth OAuth callback işləyir, JWT alınır
- Given ilk dəfə Google login, Then `users` cədvəlinə yeni qeyd insert edilir (email + provider=google)
- Given user cancel edir, Then Login ekranına error-suz qayıdır (silent)
- Given Google API failure, Then AZ error: "Google ilə bağlantı alınmadı, yenidən yoxla"

---

**US-1.3** — Apple Sign-In (iOS məcburi)
> Persona A kimi, mən **iPhone-da Apple ID ilə daxil olmaq istəyirəm**, çünki **App Store qaydası budur və email-imi paylaşmaq istəmirəm**.

**Acceptance:**
- Given Login ekranı iOS-da, Then "Apple ilə davam et" düyməsi **birinci** sıradadır (App Store HIG)
- Given user Apple Sign-In tamamlayır, Then Supabase Auth OAuth callback işləyir
- Given Apple "Hide my email" seçimi, Then `users.email` Apple relay email saxlayır (e.g. `xyz@privaterelay.appleid.com`)
- Given Android-da, Then Apple Sign-In düyməsi **göstərilmir** (cross-platform Apple Sign-In OAuth web flow MVP-dən kənar)

---

**US-1.4** — Session Persistence
> İstənilən persona kimi, mən **app-ı yenidən açdığımda yenidən login etmək istəmirəm**, çünki **session yaddaşda qalmalıdır**.

**Acceptance:**
- Given uğurlu login, Then Supabase JWT refresh token cihazın secure storage-da saxlanır (iOS Keychain, Android EncryptedSharedPreferences)
- Given app cold start, Then KMM `SessionManager` token-i oxuyur, expired-sə refresh edir
- Given refresh token 30 gündən köhnədir VƏ ya invalid-dir, Then user Login ekranına yönləndirilir
- Given user logout edir, Then secure storage-dakı tokenlər **sıfırlanır**, local SQLDelight profile cache `user_profiles` cədvəli **saxlanılır** (re-login üçün)

---

**US-1.5** — Password Reset (Magic Link)
> Persona C kimi, mən **parolumu unutdum, sıfırlamaq istəyirəm**, çünki **3 ay sonra geri qayıtmışam**.

**Acceptance:**
- Given Login ekranı, When "Parolu unutdum" basılır, Then email input ekranı açılır
- Given valid email, When submit, Then Supabase `resetPasswordForEmail` çağırılır, AZ confirmation toast
- Given email-də magic link, When tapılır, Then app deep link (`fitnessapp://reset?token=...`) açılır
- Given deep link açılır, Then yeni parol forması (parol + təkrar) göstərilir, submit → login
- Given mövcud olmayan email, Then security-conscious: "Əgər email qeydiyyatdadırsa, link göndərildi" (enumeration qoruması)

---

**US-1.6** — Logout
> İstənilən persona kimi, mən **hesabımdan çıxa bilmək istəyirəm**, çünki **paylaşılan cihazdan istifadə edirəm və ya hesabı silməyi planlaşdırıram**.

**Acceptance:**
- Given Settings (post-MVP screen) və ya `/account` deep link, When "Çıx" basılır, Then təsdiq dialoqu AZ-da
- Given təsdiq, Then Supabase `signOut` + local secure storage təmizliyi + Login ekranına route
- Given offline logout cəhd, Then yerli storage təmizlənir, Supabase signOut growable retry queue-da saxlanır

---

### Epic 2 — Onboarding (≤8 Question Funnel)

**US-2.1** — Dil Seçimi (Q0, sual sayılmır)
> İstənilən persona kimi, mən **ilk açılışda dilimi seçmək istəyirəm**, çünki **Rus və ya İngilis ola bilərəm**.

**Acceptance:**
- Given app cold start və language unset, Then dil seçimi ekranı göstərilir (3 düymə: AZ, RU, EN)
- Given cihazın system locale AZ, Then **AZ default-seçilmiş** vəziyyətdə (radio pre-selected), istifadəçi "Davam et" basa bilər
- Given seçim, Then SQLDelight `app_settings.language` saxlanır, bütün UI stringləri yenilənir
- Given dil dəyişdirilir (Settings-dən), Then app re-launch tələb etmir, runtime swap işləyir

---

**US-2.2** — Q1: Məqsəd
> Persona A kimi, mən **niyə fitnessApp istifadə etdiyimi seçmək istəyirəm**, çünki **AI proqramı bu məqsədə görə qurulmalıdır**.

**Acceptance:**
- Given Q1 ekranı, Then 4 seçim göstərilir: Arıqlamaq / Əzələ qazanmaq / Forma saxlamaq / Güclənmək
- Given seçim, Then `onboarding_state.goal` enum-a yazılır (lose_weight | build_muscle | maintain | strengthen)
- Given seçim yoxdur, Then "Davam et" disabled

---

**US-2.3** — Q2: Cins
**Acceptance:**
- 3 seçim: Qadın / Kişi / Demək istəmirəm
- "Demək istəmirəm" → BMR hesabı orta formula (Mifflin-St Jeor neutral)

**US-2.4** — Q3: Yaş
**Acceptance:**
- Numeric picker, 13-99 məhdudlaşdırılıb (under-13 = COPPA risk → bloklanır)
- < 13 yaş: "Bu tətbiq 13 yaşdan kiçik istifadəçilər üçün nəzərdə tutulmayıb" → Auth-a buraxılmır

**US-2.5** — Q4: Boy
**Acceptance:**
- Slider və ya picker, sm default (120-220 cm)
- Unit toggle (cm/ft) — AZ default cm

**US-2.6** — Q5: Çəki
**Acceptance:**
- Slider və ya picker, kq default (30-200 kg)
- Unit toggle (kg/lbs) — AZ default kg

**US-2.7** — Q6: Təcrübə səviyyəsi
**Acceptance:**
- 3 seçim: Yeni başlayan / Orta / Qabaqcıl
- Hər seçim altında 1 sətirlik kontekst (e.g. "Yeni başlayan — son 6 ayda müntəzəm məşq etməmişəm")

**US-2.8** — Q7: Avadanlıq
**Acceptance:**
- 3 seçim: Avadanlığım yoxdur (ev) / Minimal (rezin, dumbbell) / Tam zal
- Bu seçim sonra workout alternativlərini filtirləmək üçün istifadə olunur

**US-2.9** — Q8: Həftəlik gün sayı + sessiya müddəti
**Acceptance:**
- İki sual eyni ekranda (UI compactness): 2-7 gün toggle + 15/30/45/60 dəq toggle
- Hər ikisi məcburi

---

**US-2.10** — Profil Önizləməsi (Q-sonrası)
> Persona B kimi, mən **cavablarımdan nə alacağımı görmək istəyirəm**, çünki **hələ qeydiyyatdan keçməmişəm**.

**Acceptance:**
- Given 8 sual tamamlanıb, Then "Səni tanıdıq" ekranı göstərilir:
  - Hesablanmış gündəlik kalori hədəfi (BMR×activity)
  - Tövsiyə edilən həftəlik fəaliyyət saatları
  - 1 sample workout başlıq + kıs təsvir (full execution NOT yet — gating point)
- Given user "Davam et" basır, Then Auth gate (US-1.1/1.2/1.3)

---

**US-2.11** — First-Value Preview (Auth-dan sonra, paywall-dan əvvəl)
> Persona B kimi, mən **bir məşqi pulsuz görmək istəyirəm**, çünki **ödəniş etməzdən əvvəl keyfiyyəti yoxlamaq istəyirəm**.

**Acceptance:**
- Given uğurlu auth, Then 1 sample exercise göstərilir: ad (AZ) + AI video preview + 3 set × 10 rep nümunəsi
- Given user video-nu izləyir VƏ ya 30 saniyə keçir, Then "Tam planı al" CTA görünür → paywall PRD trigger
- Given user "skip" / "sonra" basır, Then home screen (free tier limit) → 5 base workout / 1 plan/ay limit
- **Paywall MVP-də soft**: free tier real value verir, user iptal sürtüşməsi sıfır (transparent billing pillar)

---

**US-2.12** — Onboarding Resume (Yarımçıq qoyulmuş)
> Persona A kimi, mən **uşağıma görə yarıda qaldığımda qayıtmaq istəyirəm**, çünki **vaxtım azdır**.

**Acceptance:**
- Given user Q3-də app-ı bağlayır (auth-dan əvvəl), Then SQLDelight-də `onboarding_state` saxlanır (anonymous, device_id ilə bağlı)
- Given app yenidən açılır, Then "Qaldığın yerdən davam et" CTA göstərilir, Q4-ə atılır
- Given user "yenidən başla" seçir, Then `onboarding_state` təmizlənir, Q1-dən
- Given onboarding 7 gündən köhnədir, Then state expired sayılır, yenidən Q1-dən

---

## 5. Functional Requirements

| ID | Tələb | Priority |
|----|-------|----------|
| FR-1 | App ilk açılışda dil seçimi göstərməli (AZ default əgər system locale az_AZ) | P0 |
| FR-2 | Onboarding **dəqiq 8 sualla** məhdudlaşmalı; 9-cu sual əlavə etmək hard-prohibited | P0 |
| FR-3 | Hər onboarding cavabı SQLDelight `onboarding_state` cədvəlinə real-time yazılmalı (offline-safe) | P0 |
| FR-4 | Supabase Auth 3 metodu dəstəkləməli: Email/parol, Google OAuth, Apple Sign-In (iOS) | P0 |
| FR-5 | iOS-da Apple Sign-In düyməsi Login ekranında **birinci** sıradadır (HIG compliance) | P0 |
| FR-6 | JWT refresh token iOS Keychain / Android EncryptedSharedPreferences-də şifrəli saxlanmalı | P0 |
| FR-7 | Onboarding completion → `user_profiles` cədvəlinə Supabase upsert (RLS-ə uyğun) | P0 |
| FR-8 | Anonymous onboarding state device_id ilə əlaqəli, auth zamanı `user_id`-ə miqrasiya | P0 |
| FR-9 | Password reset Supabase magic link + custom deep link (`fitnessapp://reset`) işləməli | P0 |
| FR-10 | Logout secure storage-dakı tokenləri tam silməli, local profile cache saxlanmalı | P0 |
| FR-11 | Sample workout preview (1 hərəkət) paywall-dan əvvəl göstərilməli | P0 |
| FR-12 | Onboarding-də heç bir billing/subscription prompt görünməməli | P0 |
| FR-13 | < 13 yaş seçimi onboarding-i bloklamalı (COPPA + AZ data protection) | P0 |
| FR-14 | Dil runtime swap-ı app re-launch tələb etməməli | P0 |
| FR-15 | Onboarding resume (7 gün TTL) yarımçıq sessiyaları davam etdirməli | P1 |
| FR-16 | Apple "Hide my email" relay email-i `users.email`-də problemsiz saxlanmalı | P0 |
| FR-17 | Onboarding-dən sonra BMR/TDEE hesablama göstərilməli (Mifflin-St Jeor formula) | P0 |
| FR-18 | Streak mexanizmi: hər tamamlanmış antrenman streak sayğacını artırır; UI-da göstərilir | P0 |
| FR-19 | Streak Freeze: istifadəçi ayda 1 dəfə "Freeze" aktivləşdirə bilər — streak qırılmır | P0 |
| FR-20 | Privacy Policy (AZ+RU+EN) linkləri Login ekranında məcburi görünür | P0 |
| FR-21 | Google Play Health Declaration formu submission öncəsi doldurulmalı (admin task, not code) | P0 |
| FR-22 | In-app hesab silmə axını mövcud olmalı (Settings → "Hesabı sil" → cascade delete) | P0 |
| FR-23 | Onboarding-da AI açıqlaması: Q8 tamamlandıqdan sonra "Planın AI tərəfindən yaradılacaq" bildirişi | P0 |

---

## 6. Non-Functional Requirements

### 6.1 Performance
- **NFR-P1**: Onboarding 8 sual median tamamlanma müddəti **≤90 saniyə** (analytics ilə ölçülür)
- **NFR-P2**: Hər sual ekranı keçidi **<200ms** (Compose/SwiftUI animation budget)
- **NFR-P3**: App cold start → ilk interaktiv ekran (dil seçimi və ya home) **<2 saniyə** (orta Android)
- **NFR-P4**: Supabase Auth API call P95 latency **<1.5 saniyə** (AZ region → EU Supabase)

### 6.2 Offline & Reliability
- **NFR-O1**: Bütün onboarding sualları **internet olmadan tamamlana bilir**
- **NFR-O2**: Auth-dan sonra ilk Supabase sync **idempotent** olmalı (retry-safe)
- **NFR-O3**: Network sync failure → user üçün görünməz (background retry queue, exp backoff)

### 6.3 Security
- **NFR-S1**: Supabase RLS `user_profiles` cədvəlinə **məcburi**: yalnız `auth.uid() = user_id` row-larını oxuya/yaza bilər
- **NFR-S2**: Email enumeration qoruması password reset-də (sabit response message)
- **NFR-S3**: Parol minimumu: 8 simvol + 1 rəqəm (Supabase Auth default-ından yüksək yox — UX trade-off)
- **NFR-S4**: JWT secure storage-da; logout-da silinir; token rotation Supabase default (1 saat access, 30 gün refresh)
- **NFR-S5**: Deep link token-ləri 15 dəqiqəlik TTL (Supabase magic link default)
- **NFR-S6**: AZ Personal Data Protection Law (yerli ekvivalent) + GDPR uyğunluğu (AZ user EU-ya səyahət edə bilər)

### 6.4 Accessibility
- **NFR-A1**: VoiceOver (iOS) + TalkBack (Android) bütün onboarding sualları üçün label-lı
- **NFR-A2**: Minimum touch target 44×44pt (HIG) / 48×48dp (Material)
- **NFR-A3**: Font scaling cihaz Dynamic Type-a respect edir
- **NFR-A4**: Color contrast WCAG AA (4.5:1 mətn üçün)
- **NFR-A5**: AZ screen reader pronunciation testləri (TalkBack az_AZ dəstəyi — test scope-da, fallback ru_RU)

### 6.5 Localization
- **NFR-L1**: Bütün stringlər mərkəzləşdirilmiş resurs faylında (KMM shared module: `MR.strings` və ya analog)
- **NFR-L2**: Heç bir string machine-translated deyil — manual review hər `name_az` üçün
- **NFR-L3**: Plural rules AZ üçün düzgün (1 sual / 2 sual / 5 sual)
- **NFR-L4**: RTL dəstək lazım deyil (AZ Latin, RU Cyrillic, EN Latin — hamısı LTR)

### 6.6 Privacy & Compliance
- **NFR-PR1**: Onboarding zamanı analytics event-lərində PII yox (yaş, çəki, boy əvəzinə range bucket)
- **NFR-PR2**: Privacy policy (AZ+RU+EN) + terms link Login ekranında məcburi; hər dil öz policy mətnini göstərir
- **NFR-PR3**: Apple App Tracking Transparency prompt — yalnız attribution lazımdırsa (MVP-də skip)
- **NFR-PR4**: App Store/Google Play submission öncəsi Privacy Policy yayınlanmalı (URL məcburi) və Google Play Health apps declaration formu doldurulmalı — bu ikisi launch blocker-dir
- **NFR-PR5**: Apple 2025 tələbi: AI plan generasiyası istifadə edən app-lar bunu açıqlamalıdır; onboarding-da məcburi bildiriş

---

## 7. Data Model Deltas

### 7.1 `users` (Supabase Auth managed + custom column-lar)
Mövcud `users` cədvəli (project-context.md-dən) saxlanır. Əlavə yox.

```sql
users
  id uuid PK,                  -- Supabase Auth managed
  email text UNIQUE,
  created_at timestamptz
```

### 7.2 `user_profiles` (mövcud — heç bir yeni sütun lazım deyil)
PRD scope-da olan onboarding sahələri **artıq mövcud schema-dadır**:
```sql
user_profiles
  id uuid PK,
  user_id uuid FK → users,
  goal text,                   -- Q1
  gender text,                 -- Q2
  age int,                     -- Q3
  height_cm int,               -- Q4
  weight_kg decimal,           -- Q5
  experience_level text,       -- Q6
  equipment_type text,         -- Q7
  weekly_days int,             -- Q8a
  session_duration_min int,    -- Q8b
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now()
```

**RLS Policy (yeni):**
```sql
ALTER TABLE user_profiles ENABLE ROW LEVEL SECURITY;

CREATE POLICY "user owns profile"
  ON user_profiles
  FOR ALL
  USING (auth.uid() = user_id)
  WITH CHECK (auth.uid() = user_id);
```

### 7.3 `onboarding_state` (YENİ — local SQLDelight + opsional Supabase mirror)

KMM lokal SQLDelight cədvəli — anonymous progress üçün:

```sql
-- SQLDelight (local-only, device-bound)
CREATE TABLE onboarding_state (
  device_id TEXT PRIMARY KEY,
  language TEXT,
  goal TEXT,
  gender TEXT,
  age INTEGER,
  height_cm INTEGER,
  weight_kg REAL,
  experience_level TEXT,
  equipment_type TEXT,
  weekly_days INTEGER,
  session_duration_min INTEGER,
  current_question INTEGER,   -- 1-8
  started_at INTEGER,         -- epoch
  updated_at INTEGER
);
```

**Auth-dan sonra:** `onboarding_state` cədvəli oxunur, `user_profiles` cədvəlinə upsert edilir, lokal qeyd silinir.

### 7.4 `app_settings` (YENİ — local SQLDelight)
```sql
CREATE TABLE app_settings (
  key TEXT PRIMARY KEY,
  value TEXT
);
-- Keys: language, last_login_method, onboarding_completed_at
```

---

## 8. API Contracts

### 8.1 Supabase Auth Endpoints (SDK çağırışları — KMM shared module)

| Method | SDK Call | Trigger | Notes |
|--------|----------|---------|-------|
| Email signup | `supabase.auth.signUpWith(Email) { email, password }` | US-1.1 | Email confirmation required (Supabase setting) |
| Email login | `supabase.auth.signInWith(Email) { email, password }` | US-1.1 | Returns Session |
| Google OAuth | `supabase.auth.signInWith(Google)` | US-1.2 | Native SDK (Google Sign-In for Android, GIDSignIn for iOS) → ID token → Supabase exchange |
| Apple OAuth | `supabase.auth.signInWith(IdentityProvider.Apple)` | US-1.3 | iOS native `ASAuthorizationController` → identity token → Supabase exchange |
| Password reset | `supabase.auth.resetPasswordForEmail(email, redirectTo)` | US-1.5 | `redirectTo = fitnessapp://reset` |
| Session refresh | `supabase.auth.refreshSession()` | Auto (KMM SessionManager) | 1h interval və ya 401 trigger |
| Logout | `supabase.auth.signOut()` | US-1.6 | Local secure storage cleanup paralel |

### 8.2 Edge Functions (BU PRD üçün TƏLƏB OLUNMUR)

**Açıq qərar:** Auth + onboarding üçün **custom Edge Function lazım deyil**. Supabase managed auth + RLS kifayətdir. Edge Function-lar AI proqram generasiyası PRD-sində gəlir.

### 8.3 Postgres Function (opsional, P1)

`handle_new_user()` — Auth trigger üzərinə qoyula bilər ki, `users` cədvəlinə yeni qeyd insert edilsin (Supabase default davranışı çatışmazsa).

```sql
CREATE OR REPLACE FUNCTION public.handle_new_user()
RETURNS trigger AS $$
BEGIN
  INSERT INTO public.users (id, email, created_at)
  VALUES (new.id, new.email, now());
  RETURN new;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

CREATE TRIGGER on_auth_user_created
  AFTER INSERT ON auth.users
  FOR EACH ROW EXECUTE FUNCTION public.handle_new_user();
```

### 8.4 Deep Link Scheme

| Scheme | Yol | Məqsəd |
|--------|-----|--------|
| `fitnessapp://reset?token={t}` | iOS Universal Links + Android App Links | Password reset |
| `fitnessapp://onboarding?resume=true` | (P2) | Push notification → onboarding resume |

---

## 9. Success Metrics

### 9.1 North Star
**D1 activation rate ≥ 40%** — install → onboarding tamamlama + auth + ilk sample workout view 24 saat içində.

(Domain research 2026-05-12: Sektörel D1 orta %20-35, ən yaxşı sinif %45+. BetterMe 26-sual onboarding ~30-35%; biz ≤8 sual + value-first + ilk oturumda anlamlı aksiyon ilə %40-i hədəfləyirik. Day-30 sektörel orta %8-12; hədəfimiz %15+.)

### 9.2 Funnel Metrics

| Metric | Hədəf | Ölçü mənbəyi |
|--------|-------|--------------|
| Dil seçimi → Q1 başlama | ≥95% | Analytics event |
| Q1 → Q8 tamamlama | ≥60% | Analytics funnel |
| Q8 → Auth ekranı | ≥85% (mütləq high — profil preview cəlbedici olmalı) | Analytics |
| Auth ekranı → Uğurlu signup | ≥70% | Supabase Auth event |
| Auth → Sample workout view | ≥95% | App event |
| Sample workout view → Home (next session) | ≥35% (D1) | Cohort analytics |
| First workout started ≤24h | ≥35% | App event |
| Onboarding median müddət | ≤90 saniyə | Analytics duration histogram |
| İlk 30 günda 4 antrenman tamamlama | ≥35% | App event (habit formation threshold) |

### 9.3 Quality Metrics
- Onboarding crash-free rate **≥99.5%**
- Auth API error rate **<2%** (network errors xaric)
- Password reset email delivery success **≥98%**

### 9.4 Trust Signals (qualitative)
- Onboarding zamanı App Store / Play Store review-larda "opaque billing" şikayəti — **0**
- Dil keyfiyyəti review feedback — pozitiv ("yaxşı tərcümə" / "yerli his")

---

## 10. Out-of-Scope (Bu PRD)

Bu PRD AŞAĞIDAKILARI KAPSAMIYOR:

- AI workout plan generation prompt design və LLM çağırışı
- Workout execution UI (set/rep logger, rest timer)
- Exercise library (50-100 hərəkət, video pipeline)
- Calorie tracker / Food DB / makro hesabı UI
- RevenueCat paywall ekranının tam dizaynı və billing flow (yalnız trigger nöqtəsi qeyd olunur)
- Phone OTP authentication
- 2FA / MFA
- Facebook / X / LinkedIn social login
- Account deletion UI (Faza 1 sub-spec)
- GDPR data export self-service
- Email change flow (post-signup)
- Profile edit UI (Q1-Q8 cavablarını dəyişdirmə) — P1, ayrı PRD
- Push notification setup (FCM/APNs registration) — workout reminder PRD-də
- Anonymous → Email account merge (anonymous mode hələ mövcud deyil)
- Subscription state-i auth-a bağlama (billing PRD-də)

---

## 11. Open Questions

| # | Question | Owner | Resolution by |
|---|----------|-------|---------------|
| OQ-1 | Supabase email confirmation **məcburi** ediləcəkmi yoxsa optional? Məcburi → friction artır, opt → spam riski. **Tövsiyə: məcburi, lakin pre-confirm-də onboarding state lokal saxlanır** | Balaagha | Pre-build |
| OQ-2 | Google Sign-In + Apple Sign-In üçün AZ developer hesabı maliyyəti (Apple $99/il) artıq müəyyəndir, **Google Play Console $25 one-time** artıq alınıb? | Balaagha | Pre-build |
| OQ-3 | TalkBack AZ dəstəyi məhduddur; AZ screen reader testləri RU fallback ilə kifayət edirmi yoxsa AZ TTS engine inteqrasiyası lazımdır? | UX (Sally) | Sprint 1 |
| OQ-4 | "< 13 yaş bloklama": AZ Personal Data Protection Law (2024 yenilənmiş qaralama) sağlamlıq verisi gizli kateqoriyaya aid edir; yaş limit araşdırılmalı. COPPA yalnız US-dir amma təcrübə baxımından 13+ threshold standartdır. **Hərəkət:** Pre-launch yerel hüquq müşavirəsi tələb olunur. | Legal review | Pre-launch |
| OQ-5 | Anonymous onboarding state device_id necə generasiya edilir? UUID v4 + Keychain/Keystore-da saxlanır? Yenidən install-da silinir? | Architect (Winston) | Sprint 1 |
| OQ-6 | Onboarding tərk edilmə nöqtəsində push notification "qayıt" göndərilməlidir? **MVP-də skip, P1** | PM | Post-MVP |
| OQ-7 | Apple Sign-In Android-da OAuth web flow ilə dəstəklənsinmi? **MVP-də NO** — yalnız iOS native | Confirmed | — |
| OQ-8 | RU/EN dilləri MVP-də 100% native review olunsunmu yoxsa AZ priority + RU/EN good-enough? **Tövsiyə: AZ 100%, RU/EN 80% acceptable** | PM | Pre-build |
| OQ-9 | BMR/TDEE preview-də göstərilən rəqəmin doğruluq disclaimer-i nədir? "Bu rəqəm təxminidir, həkim tövsiyəsi deyil" | Legal | Pre-launch |

---

## 12. Dependencies

| Dep | Type | Status | Risk |
|-----|------|--------|------|
| Supabase Project (EU region) | External service | Provision lazım | Aşağı — managed |
| Supabase Auth Email provider config | Config | Setup lazım (SMTP və ya Supabase default) | Aşağı |
| Apple Developer Program enrollment | External | Required iOS build üçün | Orta — $99/il, KYC vaxt aparır |
| Google Play Console | External | One-time $25 | Aşağı |
| Google Cloud OAuth 2.0 Client ID (Android + iOS) | Config | Setup lazım | Aşağı |
| Apple Sign-In Service ID + Key | Config | Setup lazım | Orta — sertifikat rotation |
| KMM proyekt skeleton + Supabase Kotlin SDK | Codebase | YENİ — sıfırdan qurulur | Yüksək — feasibility hələ test edilməyib |
| Localization framework (Moko Resources və ya analog) | Library | Seçim lazım | Aşağı |
| Analytics SDK (Mixpanel / PostHog / Amplitude — TBD) | Library | Seçim lazım | Aşağı — onboarding metrics-i üçün kritik |
| Crash reporting (Sentry / Crashlytics) | Library | Seçim lazım | Orta |
| Privacy Policy + Terms of Service (AZ/RU/EN) | Content | YAZILMALI | Yüksək — legal review |
| RevenueCat SDK | Library | Bu PRD-də yalnız stub — paywall trigger | Aşağı (bu PRD-də) |
| Google Play Health Declaration Form | Admin task | Tamamlanmalı | Yüksək — submission blocker (avqust 2024+) |
| Privacy Policy hosting URL | Content | Yazılıb yayınlanmalı | Yüksək — launch blocker; App Store URL tələb edir |

---

## 13. Risks & Mitigations

| # | Risk | Likelihood | Impact | Mitigation |
|---|------|------------|--------|------------|
| R-1 | KMM + Supabase Kotlin SDK iOS-da production-stability problemləri (Kotlin/Native interop) | Orta | Yüksək | Sprint 0-da spike test: minimal auth flow KMM-də end-to-end; backup plan: native auth per platform, shared SQLDelight |
| R-2 | Apple Sign-In setup mürəkkəbliyi (Service ID + Key rotation + Supabase config) ship-i gecikdirir | Orta | Orta | İlk həftədə setup, blocker olarsa Email-only ilə soft launch + Apple Sign-In v1.1 |
| R-3 | AZ TalkBack pronunciation keyfiyyəti zəif, accessibility iddiası boş | Orta | Orta | Test scope-a AZ TTS daxil et, fallback strategiyası: kritik elementlər (düymə label-ları) RU/EN ilə overrideable |
| R-4 | Email enumeration attack reset endpoint-da | Aşağı | Orta | Constant response message (US-1.5 acceptance criteria) |
| R-5 | Onboarding-də user istefa edir Q3-də (yaş) çünki "niyə soruşur?" — privacy concern | Orta | Orta | Hər sual altında 1-sətirlik kontekst label ("Yaş — kalori hədəfin üçün") |
| R-6 | Supabase EU region AZ-dan latency yüksək olur (>1.5s P95) | Aşağı | Orta | Network monitoring sprint 1; lazımsa AWS/Cloudflare edge cache |
| R-7 | < 13 yaş user yalan yaş daxil edir → COPPA-style data toplandı | Yüksək | Orta | Yaş self-attested, T&C-də yaş klauzu, parental account future |
| R-8 | Native AZ keyfiyyət iddiası boş çıxır — manual review yetmir, idiomatic error | Orta | Yüksək | 2 native AZ speaker peer review hər localization PR-da; community beta test öncəsi |
| R-9 | Onboarding 8 sual qualification dərinliyi AI plan generasiyası üçün yetmir (FR-2 push-back) | Orta | Yüksək | Onboarding-dən sonra post-MVP "advanced profile" optional ekranı (P1) — workout plan PRD-də həll olunur |
| R-10 | Apple "Hide my email" relay email-i Supabase webhook-larında problem yaradır (e.g. email-i yenidən təsdiqləmək) | Aşağı | Aşağı | Test scope-da; documented quirk |
| R-11 | İlk 30 günda istifadəçilər 4 antrenman tamamlamazsa habit formation baş tutmur → churn ehtimalı yüksək | Yüksək | Yüksək | Onboarding sonrası "4 antrenman mini-hədəf" UI badge; push notification D3/D7/D14-də |
| R-12 | Privacy Policy + Google Health Declaration olmadan App Store/Google Play rədd edir — launch gecikir | Orta | Çox yüksək | Sprint 1-də legal review başlat; template-dən istifadə et (GDPR/AZ uyğun) |

---

## 14. Implementation Notes (Engineer-facing)

> Bu PRD spec-dir, code deyil. Aşağıdakılar **memorialize** etmək üçündür ki, architecture PRD-yə geri pointer var.

- **KMM module layout:** `shared/auth/` (SessionManager, AuthRepository), `shared/onboarding/` (OnboardingStateRepository), `shared/db/` (SQLDelight schema), `iosApp/auth/` (Apple/Google native bridges), `androidApp/auth/` (Google native bridge)
- **State container:** Per-platform (iOS @StateObject, Android ViewModel) — shared `OnboardingViewModel` (KMM) ya da hər platformda thin wrapper
- **Test strategy:** US-1.1, US-1.4, US-2.10 üçün E2E test (Maestro və ya analog); RLS policy üçün Postgres test (`pg_tap` və ya analog); offline onboarding kritik path KMM unit test
- **Telemetry events (qısa siyahı):**
  - `onboarding_language_selected` {language}
  - `onboarding_question_viewed` {question_index, question_id}
  - `onboarding_question_answered` {question_index}
  - `onboarding_completed` {duration_seconds}
  - `auth_method_selected` {method: email|google|apple}
  - `auth_success` {method, is_new_user}
  - `auth_error` {method, error_code}
  - `sample_workout_viewed`
  - `paywall_triggered`

---

## 15. Approval & Sign-off

| Role | Name | Status |
|------|------|--------|
| PM | John (bmad-agent-pm) | Drafted |
| Architect | Winston (bmad-agent-architect) | Pending review |
| UX | Sally (bmad-agent-ux-designer) | Pending UX spec |
| QA | Murat (bmad-tea) | Pending test plan |
| Owner | Balaagha | Pending |

---

**End of PRD — Auth & Onboarding**
*Versiya:* 1.1 (2026-05-12 — domain research ilə yeniləndi)
*Növbəti adım:* `/bmad-create-ux-design` ilə Sally UX spec-i, `/bmad-create-architecture` ilə Winston technical design.
