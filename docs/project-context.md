---
project_name: 'fitnessApp'
user_name: 'Balaagha'
date: '2026-05-12'
sections_completed:
  - tech-stack
  - hard-constraints
  - naming-conventions
  - architecture-patterns
  - data-model
  - mvp-scope
  - competitive-context
  - monetization
  - out-of-scope
  - solo-constraints
  - communication
---

# fitnessApp — Project Context

## Business Overview

Azərbaycan bazarına yönəlmiş AI-dəstəkli fitness tracking tətbiqi. Azərbaycanlı istifadəçilərə tam lokallaşdırılmış (AZ/RU/EN) fitness təcrübəsi, fərdi AI proqramlar, hərəkət videoları və 3D modellər təklif edir.

**Problem:** Bazardakı bütün böyük fitness tətbiqləri (BetterMe, MyFitnessPal, Nike Training Club, Freeletics) Azərbaycan dilini dəstəkləmir. Azərbaycanlı istifadəçi lokalizasiya, yerli bazar qiyməti və fərdi proqram tapa bilmir.

**Həll:** Tam AZ lokalizasiya + AI-generasiya edilmiş hərəkət videoları/3D modellər + ev/zal hibrid proqramlar + Azərbaycan bazarına uyğun qiymət (5–10 AZN/ay).

## Tech Stack (TENTATIVE — Architecture phase-də dəqiqləşdiriləcək)

| Qat | Texnologiya | Qeyd |
|-----|-------------|------|
| Mobile | Kotlin Multiplatform Mobile (KMM) | iOS + Android kod paylaşımı |
| Backend | Supabase | Auth, PostgreSQL, Storage, Realtime, Edge Functions |
| Admin/Web | Next.js on Vercel | Content CMS, admin panel, landing page |
| AI Content | Google Cloud AI | $300 kredit (b.alihummatov@gmail.com) — hard budget limit |
| 3D Assets | Mixamo/Sketchfab → AI gen | MVP: hazır kitabxana; sonra: AI generasiya |

## Hard Constraints (Pozulmaz Qaydalar)

1. **Supabase RLS** — hər cədvəl üçün məcburi; schema ilə birlikdə dizayn et, sonradan əlavə etmə
2. **Signed URL** — video/media URL-lər public olmamalı; TTL ≤ 1 saat
3. **Offline-first** — core məşq funksionallığı (plan, set/rep log) internet olmadan işləməlidir
4. **GCP $300 kredit limiti** — hər AI API çağırışı cost-tracked olmalıdır; admin-only tetikle
5. **Solo developer** — minimal infrastructure; managed services > custom infra
6. **4-aylıq MVP** — hər yeni feature scope sorusuna cavab: "4 ayda solo developer bunu edə bilərmi?"

## Naming Conventions

- Database sütunları: `snake_case`
- Kotlin: `camelCase` (dəyişən/funksiya), `PascalCase` (sinif/interface)
- Swift: `camelCase` (dəyişən/funksiya), `PascalCase` (struct/class/enum)
- Fayllar: `kebab-case` (sənədlər), `PascalCase` (kod faylları)
- Lokalizasiya sütunları: `name_az`, `name_ru`, `name_en` (trilingval pattern)

## Architecture Patterns

### KMM Strukturu
- `shared/` — biznes məntiqi, data layer, Ktor (network), SQLDelight (local DB)
- `iosApp/` — SwiftUI UI (platform-specific only)
- `androidApp/` — Jetpack Compose UI (platform-specific only)
- `expect/actual` — yalnız gerçek platform API fərqləri üçün
- State: per-platform native (SwiftUI @StateObject, Compose ViewModel)
- Supabase Kotlin SDK → shared module-da istifadə edilir

### Supabase Patterns
- Auth: email + Google Sign-In + Apple Sign-In
- Storage: Supabase Storage + signed URL (media files)
- Edge Functions: AI API proxy, payment webhook, RLS-bypass server logic
- Realtime: progress sync, live workout tracking (optional MVP)

### AI Content Workflow
1. Admin panel (Vercel) → yeni hərəkət əlavə et
2. Edge Function → Google Cloud AI video generasiya → cost log
3. Video → Supabase Storage → signed URL → `exercises.video_url`
4. 3D model: Mixamo/Sketchfab kitabxanasından başla (cost-effective MVP)
5. AI 3D generasiya: kredit varsa, sonrakı mərhələdə

### Offline Strategy
- SQLDelight local DB → məşq sessionları offline saxlanır
- Network bərpa olduqda Supabase-ə background sync
- Core flow offline: onboarding data, workout plan, set/rep logging

## Data Model (Supabase/PostgreSQL)

```sql
users
  id uuid PK, email text UNIQUE, created_at timestamptz

user_profiles
  id uuid PK, user_id uuid FK → users,
  goal text,              -- lose_weight | build_muscle | maintain | strengthen
  gender text, age int, height_cm int, weight_kg decimal,
  experience_level text,  -- beginner | intermediate | advanced
  equipment_type text,    -- none | minimal | full_gym
  weekly_days int, session_duration_min int

exercises
  id uuid PK,
  name_az text, name_ru text, name_en text,
  category text, muscle_groups text[],
  equipment_required text,
  video_url text,         -- signed URL to Supabase Storage
  model_3d_url text,      -- signed URL to Supabase Storage
  difficulty text,        -- beginner | intermediate | advanced
  instructions_az text, instructions_ru text, instructions_en text

workouts
  id uuid PK, user_id uuid FK → users,
  week_number int, created_at timestamptz

workout_sessions
  id uuid PK, workout_id uuid FK → workouts,
  day_of_week int,
  session_type text        -- warmup | main | cooldown

workout_session_exercises
  id uuid PK, session_id uuid FK → workout_sessions,
  exercise_id uuid FK → exercises,
  sets int, reps int, rest_seconds int, order_index int

progress_logs
  id uuid PK, user_id uuid FK → users,
  date date, weight_kg decimal,
  body_measurements jsonb, notes text, photos text[]

calorie_logs
  id uuid PK, user_id uuid FK → users,
  date date, target_kcal int, consumed_kcal int,
  protein_g decimal, carbs_g decimal, fat_g decimal, water_ml int
```

## MVP Scope (Faza 1 — ~4 ay, Solo Developer)

### P0 — Release blocker (bunlar olmadan ship edilmir)
- Onboarding: məqsəd, cins, yaş, boy, çəki, təcrübə, avadanlıq, haftalık gün
- AI proqram generasiyası (LLM → həftəlik proqram, istiləşmə/əsas/soyuducu)
- Hərəkət kitabxanası: ad (AZ/RU/EN), video, 3D model, set/rep/istirahət
- Ev + Zal alternativ hərəkətlər
- Set/rep logger + rest timer
- Kalori hədəfi hesablaması (BMR + TDEE) + su xatırlatması
- AZ/RU/EN tam lokalizasiya (dil keçidi)

### P1 — Release-ə daxil etməyə çalışaq
- Progress tracker (çəki, ölçülər, foto)
- Sadə yemək logu (kalori sayma — tam diet modulu deyil)
- Push notification (məşq xatırlatması)
- Workout tamamlama streak

### Qəti Faza 2 (bu release-ə əlavə etmə)
- Diet modulu (ayrı app/module kimi)
- Real-time hərəkət analizi (kamera)
- Sosial xüsusiyyətlər / leaderboard
- Apple Watch / Wear OS inteqrasiyası
- AI personal coach chat

## Competitive Context

| Rəqib | Güclü | Zəif | Bizim üstünlük |
|-------|-------|------|----------------|
| BetterMe | Böyük content kitabxanası, güclü marketing | AZ dili yox, AI generasiya yox | Tam AZ, AI video/3D |
| MyFitnessPal | Kalori tracker güclü | Workout planlama zəif | Vahid həll (idman + kalori) |
| Nike Training Club | Yüksək keyfiyyətli videolar | Baha, az fərdiləşmə | Yerli qiymət, AI fərdiləşmə |
| Freeletics | AI coach | Baha, AZ dili yox | AZ dili, ev idmanı focus |

**Əsas differensiator:** Heç bir rəqib eyni anda 3 üstünlüyü birlikdə təklif etmir:
1. Tam Azərbaycan dili dəstəyi
2. AI-generasiya edilmiş hərəkət videoları + 3D modellər
3. Ev + Zal hibrid proqramlar

## Monetization

- **Freemium:** Əsas proqram pulsuz (limitli AI generasiya — aylıq 3 proqram)
- **Premium:** 5–10 AZN/ay (BetterMe analoji, yerli alıcı gücünə uyğun)
  - Sonsuz AI proqram generasiyası
  - Qabaqcıl progress analitikası
  - Tam hərəkət kitabxanası (premium hərəkətlər)
  - Faza 2-də: diet modulu inteqrasiyası
- **Ödəniş:** Visa/MC + local Azərbaycan ödəniş metodları (araşdırılacaq)

## Out-of-Scope (Bu Release-ə Əlavə Etmə)

Agentlər bu xüsusiyyətləri MVP-yə daxil etməyi TƏKLİF ETMƏSİN:

- Diet modulu / tam qidalanma planlama
- Real-time hərəkət korreksiya analizi (kamera ilə)
- Sosial xüsusiyyətlər (dostlar, leaderboard, challenges)
- Wearable inteqrasiyası (Apple Watch, Wear OS, Fitbit)
- AI personal coach chat interface
- Video upload (istifadəçi tərəfindən)
- Premium subscription billing sistemi (Faza 2)

## Solo Developer Constraints

- Həftəlik ~20 saat (IBAM full-time yanında)
- Human code reviewer yox → hər PR-da `bmad-review-adversarial-general` məcburidir
- PM yox → `bmad-agent-pm` (John) bu rolu oynayır
- Designer yox → `bmad-agent-ux-designer` (Sally) həqiqi dizayn rolu oynayır
- Test engineer yox → `bmad-tea` (Murat) test strategiyasını idarə edir

## Communication Preferences

- Dil: Türkçe (texniki terminlər İngiliscə ola bilər)
- Birbaşa, texniki cavablar — izahat şişirtmə, sugarcoat etmə
- Hər qərar reasoning ilə gəlsin
- Production-proven həllər > experimental
- Scope genişlənməsinə qarşı aqressiv ol
