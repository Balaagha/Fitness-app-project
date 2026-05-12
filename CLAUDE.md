# fitnessApp — Claude Code Session Guide

## Proje Kimliği

Azərbaycan bazarına yönəlmiş AI-dəstəkli fitness tracking tətbiqi. Həm ev, həm zal idmanını dəstəkləyir. Azərbaycan dilini dəstəkləməyən böyük rəqiblərin (BetterMe, MFP, NTC, Freeletics) boşluğunu doldurur. KMM + Supabase + Google Cloud AI üzərindən qurulur.

**Status:** Active research/scope phase (2026-05-12) — Solo developer, ~20h/həftə, IBAM full-time yanında.

---

## Claude Code Davranış Qaydaları

### Dil
- Bütün kommunikasiya: **Türkçe** (texniki terminlər İngiliscə ola bilər)
- Kod şərhlər: English
- İstifadəçiyə görünən stringlər: AZ (əsas), RU, EN

### BMad Workflow-da
- BMad agent persona-sını tam benimsə (Amelia, Winston, John, Sally, Mary)
- `docs/project-context.md` həmişə agent konteksi üçün əsasdır
- Hər workflow üçün fresh chat — context overflow riskini azaldır
- Workflow sonunda `bmad-help` növbəti addımı söyləyir

### Plain Chat-da (BMad xaricində)
- Birbaşa, texniki cavablar — "generic best practices" vermə
- Production-proven həllər, experimental yox
- Scope creep-i aqressiv sorğula — 4 aylıq MVP timeline müqəddəsdir
- KMM/Supabase/SwiftUI/Compose doc üçün Context7 MCP istifadə et

### Qəti Qadağalar
- **HEÇVAXT** Supabase cədvəli üçün RLS atlama
- **HEÇVAXT** video/media URL-lərini public et — signed URL (TTL ≤ 1h) məcburidir
- **HEÇVAXT** Faza 2 xüsusiyyətlərini (diet modulu, real-time kamera analizi, sosial, wearable) MVP-yə əlavə etmə
- **HEÇVAXT** internet olmadan işləməyən core məşq funksionallığı yaz — offline-first məcburidir
- **HEÇVAXT** Google AI API-yi cost estimate olmadan çağır — $300 kredit sabit limit

---

## Tech Stack (Tentative — Architecture phase-də dəqiqləşdiriləcək)

| Qat | Texnologiya | Qeyd |
|-----|-------------|------|
| Mobile | Kotlin Multiplatform Mobile (KMM) | iOS + Android kod paylaşımı |
| Backend | Supabase | Auth, PostgreSQL, Storage, Realtime, Edge Functions |
| Admin / Web | Next.js → Vercel | Content CMS, landing page |
| AI Content | Google Cloud AI | $300 kredit (b.alihummatov@gmail.com) |
| 3D Assets | Mixamo/Sketchfab → AI gen | MVP: kitabxana; sonra: AI generasiya |

---

## Arxitektura Əsasları

### KMM Strukturu
- **shared/** — biznes məntiqi, data layer, Ktor network, SQLDelight local DB
- **iosApp/** — SwiftUI UI layer
- **androidApp/** — Jetpack Compose UI layer
- Platform-specific kod minimum; shared-də saxla
- State management: per-platform native (SwiftUI @State/@StateObject, Compose ViewModel)

### Supabase Təhlükəsizlik
- RLS hər cədvəl üçün schema ilə birlikdə dizayn edilir — sonradan əlavə edilmir
- Video/3D model URL → signed URL, TTL ≤ 1 saat
- Edge Functions: AI API proxy, payment webhook, RLS bypass logic (server-side only)

### AI Content Pipeline
1. Admin panel (Vercel) → yeni hərəkət əlavə
2. Edge Function → Google AI API video generasiya sorğusu + cost log
3. Video → Supabase Storage (signed URL saxlanır)
4. 3D model: əvvəlcə Mixamo kitabxanası; kredit varsa AI generasiya
5. `exercises.video_url` + `exercises.model_3d_url` yenilənir

### Offline-First
- Məşq sessiyaları SQLDelight-da lokal saxlanır
- Network bağlantısı bərpa olduqda Supabase-ə sync edilir
- Core məşq funksionallığı (plan göstər, set/rep log) offline işləməlidir

---

## Data Model (İlkin — Supabase/PostgreSQL)

```sql
users               (id, email, created_at)
user_profiles       (id, user_id, goal, gender, age, height_cm, weight_kg,
                     experience_level, equipment_type, weekly_days, session_duration_min)
exercises           (id, name_az, name_ru, name_en, category, muscle_groups[],
                     equipment_required, video_url, model_3d_url, difficulty,
                     instructions_az, instructions_ru, instructions_en)
workouts            (id, user_id, week_number, created_at)
workout_sessions    (id, workout_id, day_of_week, session_type) -- warmup/main/cooldown
workout_exercises   (id, session_id, exercise_id, sets, reps, rest_sec, order_index)
progress_logs       (id, user_id, date, weight_kg, body_measurements jsonb, photos[])
calorie_logs        (id, user_id, date, target_kcal, consumed_kcal, protein_g,
                     carbs_g, fat_g, water_ml)
```

---

## MVP Scope (Faza 1 — ~4 ay)

**P0 — Olmadan release yoxdur:**
- İstifadəçi onboarding (məqsəd, profil, avadanlıq)
- Fərdi həftəlik proqram generasiyası (ev + zal variantları)
- Hərəkət kitabxanası — AZ/RU/EN, video, 3D model
- Set/rep logger + rest timer
- Kalori hədəfi (BMR+TDEE) + su xatırlatması
- AZ/RU/EN tam lokalizasiya

**P1 — Mümkünsə release-ə daxil:**
- Progress tracker (çəki, ölçülər, foto)
- Sadə yemək logu (kalori sayma — diet modulu deyil)
- Push notification (məşq xatırlatması)

**Qəti Faza 2 (sonrakı release):** Diet modulu, real-time hərəkət analizi, sosial, wearable

---

## Coding Standards

### Ümumi
- KMM shared module-da `expect/actual` yalnız platform API üçün
- Lokalizasiya stringləri shared module-da mərkəzləşdirilmiş resurs faylında
- Hər Supabase sorğusunun yanında RLS policy sənədi

### Naming
- DB cədvəlləri / sütunlar: `snake_case`
- Kotlin: `camelCase` (dəyişən/funksiya), `PascalCase` (sinif/interface)
- Swift: `camelCase` (dəyişən/funksiya), `PascalCase` (struct/class)
- Fayllar: `kebab-case` (docs), `PascalCase` (kod faylları)

### Xərc Nəzarəti
- Hər Google AI API çağırışı yanında `// cost: ~$X per call` şərhi
- AI content generation yalnız admin panel üzərindən (istifadəçi tərəfindən tetiklənmir)

---

## Rəqabət Mövqeyi

| Rəqib | Güclü | Zəif | Bizim üstünlük |
|-------|-------|------|----------------|
| BetterMe | Böyük kitabxana, güclü marketing | AZ dili yox, AI yox | Tam AZ, AI video/3D |
| MyFitnessPal | Kalori tracker | Workout planlama zəif | Vahid həll |
| Nike Training Club | Keyfiyyətli video | Baha, az fərdiləşmə | Yerli bazar, AI fərdiləşmə |
| Freeletics | AI coach | Baha, AZ dili yox | AZ dili, ev idmanı focus |

**Əsas differensiator:** Azərbaycan dili + AI generasiya + ev/zal hibrid — heç bir rəqib üçü birlikdə təklif etmir.

---

## Monetizasiya

- **Freemium:** Əsas proqram pulsuz (limitli AI generasiya)
- **Premium (5–10 AZN/ay):** Tam AI proqram, qabaqcıl analitika, sonsuz generasiya
- **Ödəniş:** Visa/MC + local ödəniş (araşdırılacaq)
- **Faza 2:** Diet modulu premium add-on

---

## BMad Setup Referansı

```
_bmad/custom/
├── config.toml              ← user_skill_level = "expert" (yeganə override)
├── bmad-agent-analyst.toml  ← Phase 1 araşdırma faktları
├── bmad-agent-architect.toml
├── bmad-agent-dev.toml
├── bmad-agent-pm.toml
└── bmad-agent-ux-designer.toml

docs/
└── project-context.md       ← BMad agents-in yüklediyi əsas kontekst faylı
```

**Qaydalar:**
- `_bmad/config.toml` — installer-managed, **READ-ONLY**
- `_bmad/custom/config.toml` — sənin override faylın
- `skillListingBudgetFraction: 0.03` — `.claude/settings.json`-da (67 skill üçün)

---

## Aktiv Tədqiqat

- **[determine-scope-of-start-up]** — Proyektin tam scope-u, stack, rəqabət mövqeyi (aktiv — 2026-05-12)
