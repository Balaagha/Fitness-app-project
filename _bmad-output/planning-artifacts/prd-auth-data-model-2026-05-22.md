---
project_name: 'fitnessApp'
user_name: 'Balaagha'
date: '2026-05-22'
version: '1.0'
workflowType: 'technical-spec'
parent_prd: 'prd-auth-onboarding-2026-05-22.md'
phase: 'MVP / Faza 1'
relatedPRDs:
  - prd-auth-onboarding-2026-05-22 (parent — funksional kontrakt)
  - prd-auth-onboarding-analytics-2026-05-22 (telemetry split)
  - ux-auth-onboarding-2026-05-22 (UI handoff split)
hardConstraintsRef: 'CLAUDE.md → Qəti Qadağalar + docs/project-context.md §11'
---

# Technical Spec — Auth & Onboarding Data Model + API Contracts

**Author:** Balaagha · **PM Agent:** John · **Date:** 2026-05-22 (v1.0)
**Parent:** `prd-auth-onboarding-2026-05-22.md` (v3.1)
**Scope:** Data model (Postgres + SQLDelight) · Supabase Auth SDK · Edge Functions · Postgres trigger · Deep link scheme · Client-side error codes · KMP module layout · State container · Test strategy.

Bu sənəd parent PRD-nin **§7 Data Model**, **§8 API Contracts**, **§17 Implementation Notes** bölmələrinin tam köçürülmüş versiyasıdır. Funksional davranış, FR/AC, state machine və axın qaydaları parent PRD-də qalır.

---

## 1. Data Model

### 1.1 `auth.users` (Supabase-managed)
Dəyişiklik yox.

### 1.2 `public.users`

```sql
CREATE TABLE public.users (
  id uuid PRIMARY KEY REFERENCES auth.users(id),
  email text UNIQUE NOT NULL,
  auth_provider text NOT NULL,  -- 'email' | 'google' | 'apple'
  created_at timestamptz NOT NULL DEFAULT now(),
  deleted_at timestamptz NULL,
  delete_grace_until timestamptz NULL
);

CREATE INDEX idx_users_email ON public.users(email);
CREATE INDEX idx_users_deleted ON public.users(deleted_at) WHERE deleted_at IS NOT NULL;
```

**RLS:**
```sql
ALTER TABLE public.users ENABLE ROW LEVEL SECURITY;
CREATE POLICY "user owns own row" ON public.users FOR ALL
  USING (auth.uid() = id) WITH CHECK (auth.uid() = id);
```

### 1.3 `public.user_profiles` — minimal (yalnız 7 məcburi + privacy fields)

```sql
CREATE TABLE public.user_profiles (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid UNIQUE NOT NULL REFERENCES public.users(id) ON DELETE CASCADE,

  -- 7 mandatory onboarding answers
  goal text NOT NULL CHECK (goal IN ('bulk','cut','general_fit')),
  gender text NOT NULL CHECK (gender IN ('male','female')),
  age int NOT NULL CHECK (age BETWEEN 13 AND 120),
  height_cm int NOT NULL CHECK (height_cm BETWEEN 100 AND 230),
  weight_kg decimal(5,1) NOT NULL CHECK (weight_kg BETWEEN 30 AND 250),
  experience_level text NOT NULL CHECK (experience_level IN ('beginner','intermediate','advanced')),
  context text NOT NULL CHECK (context IN ('serious_gym','casual_gym','home_only')),
  weekly_days int NOT NULL CHECK (weekly_days BETWEEN 2 AND 7),
  session_duration_min int NOT NULL CHECK (session_duration_min IN (15,30,45,60)),

  -- Resolved persona
  persona_cell text NOT NULL,  -- e.g. 'serious_gym.male.bulk'

  -- Privacy / compliance
  ai_disclosure_accepted_at timestamptz NOT NULL,
  parental_consent_at timestamptz NULL,
  pregnancy_postpartum boolean NULL,  -- F-only; NULL → not asked / declined
  language_at_signup text NOT NULL,    -- 'az' | 'ru' | 'en'

  -- Modifiers (jsonb; written by plan PRD, schema reserved here)
  modifiers jsonb NOT NULL DEFAULT '[]'::jsonb,

  -- Audit
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX idx_user_profiles_persona ON public.user_profiles(persona_cell);
CREATE INDEX idx_user_profiles_pregnancy ON public.user_profiles(pregnancy_postpartum) WHERE pregnancy_postpartum = true;
```

**RLS:** standart `auth.uid() = user_id` SELECT/INSERT/UPDATE/DELETE policy-ləri.

> **19 opsiyonel sahə (injury_history, allergies, sleep_h_per_night, target_weight, vs.):** `user_profiles` cədvəlinə **NULLABLE əlavə edilir kataloq PRD-də** — bu sənəd schema yaratmır. Streak/workout sütunları → workout PRD.

### 1.4 `onboarding_state` (SQLDelight local-only)

```sql
CREATE TABLE onboarding_state (
  device_id TEXT PRIMARY KEY,
  language TEXT,
  goal TEXT,
  gender TEXT,
  age INTEGER,
  height_cm INTEGER,
  weight_kg REAL,
  experience_level TEXT,
  context TEXT,
  weekly_days INTEGER,
  session_duration_min INTEGER,
  parental_consent BOOLEAN,
  ai_disclosure_accepted_at INTEGER,
  current_question INTEGER,
  started_at INTEGER,
  updated_at INTEGER,
  expires_at INTEGER  -- started_at + 7 days
);
```

**Auth-dan sonra:** Edge Fn `migrate_onboarding(device_id, user_id)` oxuyur → `user_profiles` upsert → local qeyd silinir.

### 1.5 `age_gate_blocked`

**Local (SQLDelight):**
```sql
CREATE TABLE age_gate_blocked (
  email_hash TEXT PRIMARY KEY,
  blocked_at INTEGER NOT NULL,
  expires_at INTEGER NOT NULL,
  attempts INTEGER NOT NULL DEFAULT 1
);
```

**Supabase (server-side double-check):**
```sql
CREATE TABLE public.age_gate_blocked (
  email_hash text PRIMARY KEY,
  blocked_at timestamptz NOT NULL DEFAULT now(),
  expires_at timestamptz NOT NULL DEFAULT now() + interval '30 days',
  attempts int NOT NULL DEFAULT 1
);
-- Daily cleanup: DELETE WHERE expires_at < now()
```

**RLS:** **None — service-role only** (Edge Function bypass-RLS).

### 1.6 Account delete cascade qaydası (DATA — UI ayrı PRD)

Edge Function `delete_account(user_id)` aşağıdakı sıra ilə:
1. `progress_logs` DELETE WHERE user_id
2. `calorie_logs` DELETE WHERE user_id
3. `workout_exercises` (via FK)
4. `workout_sessions` (via FK)
5. `workouts` DELETE WHERE user_id
6. `user_profiles` DELETE WHERE user_id
7. `users` UPDATE SET `deleted_at = now(), delete_grace_until = now() + interval '30 days'`
8. Storage purge: `storage.objects` userId/* — Edge Fn batch
9. Cron hard-purge at `delete_grace_until`

**30-day grace:** Re-login → `restore_account(email)` clears `deleted_at` → user-ə Home (data preserved).

> **UI flow (double-confirm, restore toast):** `prd-settings-deferred-2026-05-22.md`. Bu sənəd yalnız cascade qaydasını və Edge Fn kontraktını bağlayır.

---

## 2. API Contracts

### 2.1 Supabase Auth SDK (KMP shared)

| Method | SDK Call | Notes |
|--------|----------|-------|
| Email signup | `supabase.auth.signUpWith(Email) { email, password }` | Email confirmation məcburi |
| Email login | `supabase.auth.signInWith(Email)` | Rate limit 5 / 15 dəq / IP+email |
| Google OAuth | `supabase.auth.signInWith(Google)` | Android native; iOS GIDSignIn → ID token |
| Apple OAuth | `supabase.auth.signInWith(IdentityProvider.Apple)` | iOS `ASAuthorizationController` |
| Password reset | `supabase.auth.resetPasswordForEmail(email, redirectTo)` | `redirectTo=fitnessapp://reset` |
| Session refresh | `supabase.auth.refreshSession()` | `SessionManager`; 401 və ya 1h |
| Logout | `supabase.auth.signOut()` | Pre-check sync queue (settings PRD) |
| Delete account | Edge Fn `delete_account` | UI: settings PRD |

### 2.2 Edge Functions

| Function | Purpose | Auth | Input | Output |
|----------|---------|------|-------|--------|
| `check_age_gate` | §3.6.3 (parent) server-side double-check | service-role | `{email_hash, age}` | `{allowed, reason?}` |
| `migrate_onboarding` | onboarding_state → user_profiles | user JWT | `{device_id}` | `{profile_id, persona_cell}` |
| `delete_account` | §1.6 cascade | user JWT | `{}` | `{grace_until}` |
| `restore_account` | grace restore | user JWT | `{}` | `{restored: bool}` |

### 2.3 Postgres trigger

```sql
CREATE OR REPLACE FUNCTION public.handle_new_user()
RETURNS trigger AS $$
BEGIN
  INSERT INTO public.users (id, email, auth_provider, created_at)
  VALUES (
    new.id,
    new.email,
    COALESCE(new.raw_app_meta_data->>'provider', 'email'),
    now()
  );
  RETURN new;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

CREATE TRIGGER on_auth_user_created
  AFTER INSERT ON auth.users
  FOR EACH ROW EXECUTE FUNCTION public.handle_new_user();
```

### 2.4 Deep Link Scheme

| Scheme | Yol | Məqsəd | TTL |
|--------|-----|--------|-----|
| `fitnessapp://reset?token={t}` | iOS UL + Android AL | Password reset | 15 dəq |
| `fitnessapp://confirm-email?token={t}` | iOS UL + Android AL | Email confirmation | 24h |
| `fitnessapp://restore?token={t}` | iOS UL + Android AL | Account restore | 30 gün |

### 2.5 Client-side error codes

| Code | Meaning | Target screen | UI behaviour | AZ message |
|------|---------|---------------|--------------|-----------|
| `AUTH_001` | Invalid email format | EmailSignupForm · EmailLoginForm · PasswordResetEmail | Inline | "Email formatı düzgün deyil" |
| `AUTH_002` | Password too weak (<8 / no digit) | EmailSignupForm · PasswordResetForm | Inline | "Parol ən azı 8 simvol və 1 rəqəm olmalıdır" |
| `AUTH_003` | Email already exists | EmailSignupForm | Inline + "Daxil ol" CTA | "Bu email artıq qeydiyyatdadır" |
| `AUTH_004` | Invalid credentials | EmailLoginForm | Inline (form-level) | "Email və ya parol səhvdir" |
| `AUTH_005` | Email not confirmed | EmailLoginForm → EmailConfirmationPending | Banner + "Linki yenidən göndər" | "Email təsdiq olunmayıb" |
| `AUTH_006` | Rate limited | EmailLoginForm · EmailSignupForm · PasswordResetEmail | BlockingModal | "Çox cəhd. 15 dəqiqə sonra cəhd et" |
| `AUTH_007` | Network error | İstənilən auth ekranı | Toast | "Bağlantı problemi. Yenidən cəhd et" |
| `AUTH_008` | Google cancelled | AuthGate | Silent | — |
| `AUTH_009` | Google failed | AuthGate | Toast | "Google ilə bağlantı alınmadı" |
| `AUTH_010` | Apple cancelled | AuthGate (iOS) | Silent | — |
| `AUTH_011` | Apple failed | AuthGate (iOS) | Toast | "Apple ilə bağlantı alınmadı" |
| `AUTH_012` | Age gate blocked (server) | AgeGateBlocked (terminal) | Hard-stop | parent §3.6.1 |
| `AUTH_013` | Reset link expired | PasswordResetForm | Inline + "Yeni link" | "Link vaxtı keçib. Yenidən cəhd et" |
| `ONB_001` | Invalid age range (Q3) | Q3_Age | Inline | "Yaşı 13-99 arası daxil et" |
| `ONB_002` | Invalid height/weight range | Q4_HeightWeight | Inline | "Boy 120-220 sm, çəki 30-200 kq arası" |
| `ONB_003` | Onboarding state expired (>7 day) | Cold-start resume modal | BlockingModal + "Yenidən başla" | "Yarımçıq qeydiyyat vaxtı keçib" |
| `ONB_004` | Sync to profile failed | SyncOnboardingToProfile (görünməz) | Background retry; user-ə görünmür | (görünmür) |
| `ONB_005` | Parental consent not given | ParentalNotice | Inline; "Davam et" disabled | "Davam etmək üçün təsdiq et" |

---

## 3. Implementation Notes

### 3.1 KMP module layout

```
shared/
├── auth/
│   ├── SessionManager.kt           (expect)
│   ├── AuthRepository.kt
│   ├── tokens/SecureStorage.kt     (expect)
│   └── providers/{Apple,Google,Email}AuthProvider.kt
├── onboarding/
│   ├── OnboardingStateRepository.kt
│   ├── OnboardingViewModel.kt
│   └── PersonaResolver.kt
├── db/
│   ├── onboarding_state.sq
│   ├── app_settings.sq
│   └── age_gate_blocked.sq
├── strings/{az,ru,en}.json
└── network/SupabaseClient.kt

iosApp/
├── auth/{Apple,Google}SignInBridge.swift   (actual)
└── secureStorage/KeychainStorage.swift     (actual)

androidApp/
├── auth/GoogleSignInBridge.kt              (actual)
└── secureStorage/EncryptedPrefsStorage.kt  (actual)
```

### 3.2 State container

- KMP shared `OnboardingViewModel` (thin wrapper)
- iOS: `@StateObject` wraps shared VM
- Android: `ViewModel()` consumes shared `StateFlow`
- State persistence: SQLDelight write on every transition

### 3.3 Test strategy

- **E2E (Maestro/Detox):** US-1.1 signup · US-1.6 reset · US-2.3 resume · US-1.8 delete + restore
- **RLS Postgres test:** `pg_tap` hər policy üçün
- **Offline onboarding:** KMP unit test (write/read without network)
- **Age Gate:** unit test (`<13`/`13-17`/`≥18`)
- **Persona resolver:** parameterized 18-cell test (3 context × 2 sex × 3 goal + pregnancy edge)
- **Localization:** snapshot test fallback chain

### 3.4 Cost guard
- AI çağırışı bu PRD-də **YOX**
- Edge Functions: `check_age_gate`, `migrate_onboarding`, `delete_account`, `restore_account` — hamısı cheap
- Supabase Auth: free tier

### 3.5 Performance NFR detalları (parent §10.1)
- **NFR-P3:** Cold start → ilk interaktiv ekran <2s (orta Android, P50) — KMP init + SQLDelight open + Supabase client lazy
- **NFR-P4:** Supabase Auth API P95 <1.5s (AZ → EU) — Cloudflare edge fallback Sprint 1 monitoring
- **NFR-P5:** Profil sync Edge Fn P95 <2s — `migrate_onboarding` cold start <500ms hədəfi

### 3.6 Security NFR detalları (parent §10.3)
- **NFR-S4:** JWT token rotation 1h access / 30 gün refresh
- **NFR-S5:** Deep link TTL 15 dəq (reset), 24h (confirm), 30 gün (restore)
- **NFR-S6:** Age Gate server-side double-check (`check_age_gate`) — 30 gün TTL `age_gate_blocked` Supabase table
- **NFR-S7:** AZ Personal Data Protection Law + GDPR uyğunluğu

---

**End of Technical Spec v1.0**
