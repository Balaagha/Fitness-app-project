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
  - market-research-2026-05-12
---

# fitnessApp — Project Context

## Business Overview

Azərbaycan bazarına yönəlmiş AI-dəstəkli fitness tracking tətbiqi. Azərbaycanlı istifadəçilərə tam lokallaşdırılmış (AZ/RU/EN) fitness təcrübəsi, fərdi AI proqramlar, hərəkət videoları və 3D modellər təklif edir.

**Problem:** ⚠️ **(2026-05-12 araşdırma ilə yenidən tərif olundu)** BetterMe artıq AZ dilini dəstəkləyir (machine-translated), MFP/NTC/Freeletics-də AZ yoxdur. Real problem: **native keyfiyyətli AZ kontent + yerli yemək DB + transparent qiymət + adaptiv AI** kombinasiyası heç bir rəqibdə yoxdur. AZ istifadəçi BetterMe-nin maşın-tərcüməsində "yerli" hiss almır, yerli yemək tapmır, opaque billing-dən şikayət edir.

**Həll:** Native AZ keyfiyyəti (manual review hər string) + AZ Top-200 yemək DB (plov/dolma/qutab/yerli markalar) + AI həftəlik proqram (ev/zal hibrid alternativ) + transparent RevenueCat billing + 5-10 AZN/ay qiymət. **3D model Faza 2-yə keçirildi** (user pain deyil).

## Tech Stack (TENTATIVE — Architecture phase-də dəqiqləşdiriləcək)

| Qat | Texnologiya | Qeyd |
|-----|-------------|------|
| Mobile | Kotlin Multiplatform Mobile (KMM) | iOS + Android kod paylaşımı |
| Backend | Supabase | Auth, PostgreSQL, Storage, Realtime, Edge Functions |
| Admin/Web | Next.js on Vercel | Content CMS, admin panel, landing page |
| AI Content | Google Cloud AI | $300 kredit (b.alihummatov@gmail.com) — hard budget limit |
| Billing | RevenueCat + Apple/Google IAP | Transparent billing trust differentiator; in-app cancel + açıq trial |
| Local Payment | m10 / Pulpal / UnipayGO | Faza 1 araşdırma — AZN local card desteği |
| Exercise Video | İlk 50 manual + Mixamo, sonra AI Veo/Kling gradual | Hibrid pipeline — kredit risk + keyfiyyət risk birlikdə idarə |
| ~~3D Assets~~ | ~~Mixamo/Sketchfab~~ | **Faza 2-yə keçirildi (2026-05-12)** — user-stated pain yoxdur |

## Hard Constraints (Pozulmaz Qaydalar)

1. **Supabase RLS** — hər cədvəl üçün məcburi; schema ilə birlikdə dizayn et, sonradan əlavə etmə
2. **Signed URL** — video/media URL-lər public olmamalı; TTL ≤ 1 saat
3. **Offline-first** — core məşq funksionallığı (plan, set/rep log) internet olmadan işləməlidir
4. **GCP $300 kredit limiti** — hər AI API çağırışı cost-tracked olmalıdır; admin-only tetikle
5. **Solo developer** — minimal infrastructure; managed services > custom infra
6. **4-aylıq MVP** — hər yeni feature scope sorusuna cavab: "4 ayda solo developer bunu edə bilərmi?"
7. **Native AZ məcburi** — heç bir machine-translated AZ string ship edilməz; manual review hər lokalizasiya stringi üçün
8. **Transparent billing məcburi** — in-app cancel button, açıq trial terms, opaque auto-renewal qadağan (BetterMe/Freeletics şikayət vektoru)
9. **Onboarding ≤8 sual** — BetterMe-nin 26 sualı drop-off problemi; qualification dərinliyi ilə trade-off
10. **3D model MVP-də YOX** — Faza 2-yə keçirildi; `exercises.model_3d_url` schema-da qalsın amma null
11. **Annual plan default push** — aylıq retention 17% sektörel ortadır, yanvar campaign + onboarding-də illik plan ön sıra
12. **Streak + Streak Freeze P0** — araşdırma ilə doğrulandı: fitness app-da ən güclü retention mexanizması; Freeze = ayda 1 (suçluluk döngüsünü kırar); streak olmadan launch yasak
13. **Privacy Policy (AZ+RU+EN) + Google Health Declaration** — App Store/Google Play submission öncəsi məcburi; bunlar olmadan red alınır (launch blocker)
14. **In-app hesab silmə** — Apple 2024+ zorunluluğu; Settings-də "Hesabı sil" + data cascade delete; ayrı sub-spec, lakin launch blocker
15. **AI plan açıqlaması** — Apple 2025 tələbi: onboarding-də "Bu plan AI tərəfindən yaradılır" bildirişi məcburi

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

### AI Content Workflow (Hibrid Pipeline)
1. Admin panel (Vercel) → yeni hərəkət əlavə et
2. **Phase 1 (ilk 50 hərəkət):** Manual çəkim + Mixamo animation — keyfiyyət riski sıfır, AI kredit qoruma
3. **Phase 2 (50-100 hərəkət):** Edge Function → Google Cloud AI video generasiya → cost log
4. Video → Supabase Storage → signed URL → `exercises.video_url`
5. ~~3D model~~ — **Faza 2-yə keçirildi** (2026-05-12), `model_3d_url` MVP-də null

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
- Onboarding **≤8 sual**: məqsəd, cins, yaş, boy, çəki, təcrübə, avadanlıq, haftalık gün
- AI proqram generasiyası (LLM → həftəlik proqram, istiləşmə/əsas/soyuducu)
- Hərəkət kitabxanası 50-100: ad (AZ/RU/EN), **video** (3D YOX — Faza 2), set/rep/istirahət
- Ev + Zal alternativ hərəkətlər (per exercise)
- Set/rep logger + rest timer
- Kalori hədəfi hesablaması (BMR + TDEE) + su xatırlatması
- **AZ Top-200 yemək DB** (plov, dolma, qutab, kabab, AZ markaları)
- AZ/RU/EN tam **native** lokalizasiya (MT qadağan, manual review hər string)
- **Transparent billing**: RevenueCat + Apple/Google IAP + in-app cancel + açıq trial
- **Free tier**: 5 base workout + 30 hərəkət + 1 plan/ay + kalori tracker (NTC pulsuz təhdidi)
- **Streak + Streak Freeze** — araşdırma ilə doğrulandı; ən güclü retention mexanizması; Freeze = ayda 1
- **Privacy Policy (AZ+RU+EN)** — App Store/Google Play submission öncəsi məcburi (launch blocker)
- **Google Play Health Declaration** — ağustos 2024-dən məcburi doldurulmalı (launch blocker)
- **In-app hesab silmə** — Apple 2024+ tələbi (launch blocker)
- **Onboarding-da AI açıqlaması** — "Bu plan AI tərəfindən yaradılır" bildirişi (Apple 2025)
- **Video arka planlı paywall** — 2.9x conversion artışı (RevenueCat 2025 məlumatı)

### P1 — Release-ə daxil etməyə çalışaq
- Progress tracker (çəki, ölçülər, foto)
- Push notification AZ-da (məşq xatırlatması)
- Adaptive plan adjustment (missed session recovery)
- **Ramazan mode** (suhoor/iftar split macros — TR ekspansiya hazırlıq)
- m10/Pulpal/UnipayGO local payment inteqrasiyası
- İlk 30 günda 4 antrenman mini-hədəfi UI (habit formation eşiği)

### Qəti Faza 2 (bu release-ə əlavə etmə)
- Diet modulu (ayrı app/module kimi)
- **3D model per exercise** (2026-05-12 araşdırma ilə P0-dan çıxarıldı)
- Real-time hərəkət analizi (kamera)
- Sosial xüsusiyyətlər / leaderboard
- Apple Watch / Wear OS inteqrasiyası
- AI personal coach chat

## Competitive Context (2026-05-12 araşdırma ilə yenilənib)

| Rəqib | Güclü | Zəif | Bizim üstünlük |
|-------|-------|------|----------------|
| **BetterMe** 🚨 | Böyük kitabxana, marketing, **AZ MT artıq dəstəklənir**, illik effektiv $5/ay | MT keyfiyyət, yerli food DB yox, opaque billing şikayətləri (BBB+Trustpilot+Sikayetvar) | **Native AZ keyfiyyəti, yerli food DB, transparent billing** |
| MyFitnessPal | 15M food DB, barcode scan | AZ yox, workout planlama primitiv | AZ food DB + vahid həll (idman+kalori) |
| Nike Training Club | **Tamamilə pulsuz**, professional video | AZ yox, kalori yox, fərdiləşmə zəif | Güclü free tier + AZ + AI plan + kalori |
| Freeletics | Adaptive AI Coach (Europe leader) | $35/ay, AZ/RU yox | AZ + 5-10 AZN qiymət + ev fokus |
| Diyetkolik (TR) | TR food DB, 1.5M user | TR-only, fitness komponent yox | AZ food DB + workout birləşmiş |
| Push30 (AZ) | Yerli zal aggregator (267 zal) | AI/workout funksiyası yox | **Rəqib deyil, potensial partner** |

**Reference əsas global apps:** FitOn (pulsuz, AZ yox), Hevy ($3/ay gym log), Strong, JEFIT, Fitbod ($16 AI), Centr ($30 Hemsworth), Caliber ($200+ human coach), FitStars.ru (RU video)

**Native AZ AI fitness app TAPILMADI** (2026-05-12 itibarilə) — green field

### Yenilənmiş Differensiator Sıralaması

1. 🏆 **Yerli content depth** (AZ Top-200 food DB, Ramazan modu, AZ trainer voice-over) — **ən güclü moat (12-24 ay)** — BetterMe-nin kopyalaması üçün yerli komanda lazımdır
2. 🥈 **Native AZ keyfiyyəti** (BetterMe MT-ni keç, manual review hər string) — orta moat (3-6 ay)
3. 🥉 **Transparent billing + AZN local payment** (RevenueCat + m10/Pulpal) — trust differentiator, sektör problemini həll edir
4. **Adaptiv AI plan** (missed session recovery, equipment switch) — day-level adaptation real boşluq
5. **AI-generated exercise video** — marketing hook, **uzunmüddətli moat zəif** (commodity 12 ayda)
6. **Home+gym hybrid alternativ** (per exercise) — orta moat (6-12 ay)
7. ~~**3D model**~~ — Faza 2 (user-stated pain qaynağı yoxdur)

### Threat Response: BetterMe AZ Marketing Pump

Ən böyük real risk: BetterMe Azərbaycan-da paid ads gücləndirsə, brand recognition ilə qazanır. Mitigation:
- Native quality message ("Maşın tərcüməsi deyil, Azərbaycan üçün yazılmış")
- 5-10 AZ fitness influencer ilə 1+ illik exclusive
- AZ-spesifik content göstər (plov kalorisi, Ramazan mode, AZ trainer)
- AZN qiymət şəffaflığı (BetterMe USD billing şikayət magneti)

## Monetization

- **Free tier:** 5 base workout + 30 hərəkət + 1 plan generasiya/ay + kalori tracker (NTC pulsuz təhdidini qarşıla)
- **Premium aylıq:** **8 AZN** (BetterMe AZ illik effektiv qiymətə yaxın)
- **Premium illik:** **60 AZN (~5 AZN/ay effektiv, 38% endirim)** — yanvar campaign push absolute
- **Lifetime:** **199 AZN** (Strong-dan ilham, sıxı loyalty seqmenti üçün)
- **Premium content:** Sonsuz AI plan generasiyası, qabaqcıl analitika, tam hərəkət kitabxanası, Ramazan mode (Faza 1-də əgər var), diet modulu (Faza 2)
- **Ödəniş:** RevenueCat → Apple/Google IAP (P0) + m10/Pulpal/UnipayGO local card (Faza 1 araşdırma)
- **⚠️ Kritik retention strategiyası:** Sektörel aylıq retention 17%; onboarding default illik plan, yanvar campaign + summer pre-bikini push
- **Paywall optimizasiya (araşdırma 2026-05-12):**
  - Video arka planlı paywall → **2.9x** yüksək conversion (2025 məlumatı)
  - Onboarding sonunda iki seçim: "7 günlük sınaq" VƏ "İndi %38 endirimlə illik al" — yüksək niyyətli user-ları tutar
  - LLM plan generasiya maliyəti 10K user-da ~$32-80/ay — premium-da sınırsız plan vermək mümkün

## Retention & Aktivasiya Benchmark-ları (Domain Research 2026-05-12)

> Bu rəqəmlər MVP metrics hədəflərinin əsaslandırılması üçündür. Mənbə: RevenueCat State of Subscription 2024+2025, UXCam, Business of Apps.

| Metrik | Sektör Ortası | Ən Yaxşı Sinif | fitnessApp Hədəfi |
|--------|--------------|----------------|-------------------|
| Day-1 Retention | %20-35 | %45+ | **%40+** |
| Day-7 Retention | %15-20 | %30+ | %25+ |
| Day-30 Retention | %8-12 | %25-47 | %15+ |
| 12-ay (Aylıq plan) | %13-17.5 | — | — |
| 12-ay (İllik plan) | **%44.1** | — | — |

**Kritik activation insight:** İlk 30 günda **4 tamamlanmış antrenman** = habit formation eşiği — bu cohort qalıcı istifadəçiyə çevrilir. D1-də anlamlı birinci aksiyon tamamlanma oranı **2-3x** daha yaxşı D30 retention verir.

**Yanvar Cohort Problemi:** Fitness app-ların ən yüksək CAC, ən aşağı LTV cohort-u Yanvardır (New Year resolution churn). Mitigation: yanvar kampaniyasını birbaşa illik plana yönləndir + ilk 30 gün aktivasyon yoğunlaşdır.

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
