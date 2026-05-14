# fitnessApp — Claude Code Session Guide

## Proje Kimliği

Azərbaycan bazarına yönəlmiş AI-dəstəkli fitness tracking tətbiqi. Həm ev, həm zal idmanını dəstəkləyir. **Əsas pozisiya:** yerli content depth (AZ food DB, Ramazan, AZ trainer voice) + native AZ keyfiyyəti + transparent billing + adaptiv AI. KMM + Supabase + Google Cloud AI üzərindən qurulur.

> ⚠️ **Strateji düzəliş (2026-05-12 market research):** BetterMe artıq Azərbaycan dilini dəstəkləyir (machine-translated). "AZ dili = əsas moat" hipotezi YANLIŞ. Real moat: **yerli content + native quality + trust (transparent billing)**.

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
- **HEÇVAXT** Faza 2 xüsusiyyətlərini (diet modulu, real-time kamera analizi, sosial, wearable, **3D model**) MVP-yə əlavə etmə
- **HEÇVAXT** internet olmadan işləməyən core məşq funksionallığı yaz — offline-first məcburidir
- **HEÇVAXT** Google AI API-yi cost estimate olmadan çağır — $300 kredit sabit limit
- **HEÇVAXT** machine-translated AZ string ship etmə — hər lokalizasiya stringi manual review tələb edir (BetterMe MT-yə qarşı native quality moat-ımızın əsası)
- **HEÇVAXT** opaque/auto-renewal billing dizayn etmə — in-app cancel button + transparent trial terms məcburidir (BetterMe/Freeletics şikayət vektoru)
- **HEÇVAXT** onboarding sualını 8-i keç — BetterMe 26 sualı drop-off problemidir
- **HEÇVAXT** streak mexanizmi olmadan launch et — domain research ilə doğrulandı: ən güçlü retention aracı; Streak Freeze (ayda 1) ilə birlikdə P0
- **HEÇVAXT** Privacy Policy (AZ+RU+EN) olmadan App Store/Google Play-ə submit et — launch blocker; Google Health Declaration da məcburidir (avqust 2024+)
- **HEÇVAXT** paywall-da yalnız bir seçim göstər — "7 günlük sınaq" VƏ "İndi illik al" iki seçim; video arka plan 2.9x conversion verir
- **HEÇVAXT** onboarding-da AI açıqlaması olmadan gön­dər — Apple 2025 tələbi: "Bu plan AI tərəfindən yaradılır"

---

## Tech Stack (Tentative — Architecture phase-də dəqiqləşdiriləcək)

| Qat | Texnologiya | Qeyd |
|-----|-------------|------|
| Mobile | Kotlin Multiplatform Mobile (KMM) | iOS + Android kod paylaşımı |
| Backend | Supabase | Auth, PostgreSQL, Storage, Realtime, Edge Functions |
| Admin / Web | Next.js → Vercel | Content CMS, landing page |
| AI Content | Google Cloud AI | $300 kredit (b.alihummatov@gmail.com) |
| Billing | RevenueCat + Apple/Google IAP + m10/Pulpal/UnipayGO (Faza 1 araşdırma) | Transparent billing trust differentiator |
| ~~3D Assets~~ | ~~Mixamo/Sketchfab~~ | **Faza 2-yə keçirildi — user pain deyil, AI kredit yandırır** |
| Exercise Video | İlk 50 manual+Mixamo, sonra AI Veo/Kling gradual | $300 GCP limiti və keyfiyyət riski üçün hibrid pipeline |

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
2. **Phase 1 (ilk 50 hərəkət):** Manual çəkim + Mixamo animation — keyfiyyət riski sıfıra endir
3. **Phase 2 (50+ hərəkət):** Edge Function → Google AI API video generasiya + cost log
4. Video → Supabase Storage (signed URL saxlanır)
5. `exercises.video_url` yenilənir
6. ~~3D model~~ — Faza 2-yə keçirildi; `exercises.model_3d_url` schema-da qalsın amma MVP-də null

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
- İstifadəçi onboarding (≤8 sual: məqsəd, profil, avadanlıq)
- Fərdi həftəlik proqram generasiyası (ev + zal variantları)
- Hərəkət kitabxanası 50-100 — AZ/RU/EN, video (3D model **YOX** — Faza 2)
- Set/rep logger + rest timer
- Kalori hədəfi (BMR+TDEE) + su xatırlatması
- **AZ Top-200 yemək DB** (plov, dolma, qutab, kabab, yerli markalar)
- AZ/RU/EN tam **native** lokalizasiya (MT qadağan)
- **Transparent billing**: RevenueCat + in-app cancel + açıq trial terms
- **Free tier**: 5 base workout + 30 hərəkət + 1 plan/ay + kalori (NTC pulsuz təhdidini qarşıla)
- **Streak + Streak Freeze** (araşdırma 2026-05-12 ilə doğrulandı — P0)
- **Privacy Policy** (AZ+RU+EN) + **Google Health Declaration** (launch blocker)
- **In-app hesab silmə** (Apple 2024+ məcburi — launch blocker)
- **Video arka planlı paywall** + iki seçim (trial + illik) (2.9x conversion)
- **Onboarding-da AI açıqlaması** (Apple 2025 tələbi)

**P1 — Mümkünsə release-ə daxil:**
- Progress tracker (çəki, ölçülər, foto)
- Push notification AZ-da (məşq xatırlatması)
- Adaptive plan adjustment (missed session recovery)
- **Ramazan mode** (suhoor/iftar split macros — TR ekspansiya üçün dəyər)
- m10/Pulpal/UnipayGO local payment inteqrasiyası

**Qəti Faza 2 (sonrakı release):** Diet modulu, real-time hərəkət analizi, sosial, wearable, **3D model per exercise**, AI personal coach chat

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
| **BetterMe** 🚨 | Böyük kitabxana, marketing, **AZ MT artıq var**, $5/ay illik effektiv | MT keyfiyyət, yerli food yox, opaque billing | **Native AZ keyfiyyəti, yerli content, transparent billing** |
| MyFitnessPal | 15M food DB, barcode | AZ yox, workout zəif | AZ food DB + vahid həll |
| Nike Training Club | **Pulsuz**, keyfiyyətli video | AZ yox, kalori yox, fərdiləşmə zəif | Güclü free tier + AZ + AI plan |
| Freeletics | Adaptive AI coach | $35/ay, AZ/RU yox | AZ + 5-10 AZN qiymət |
| Diyetkolik (TR) | TR food DB, 1.5M user | TR-only, fitness zəif | AZ food DB + workout birləşmiş |
| Push30 (AZ) | Yerli zal aggregator | AI/workout yox | Potensial partner, rəqib deyil |

**Əsas differensiator (yenidən sıralandı, 2026-05-12 araşdırma):**
1. 🏆 **Yerli content depth** (AZ food DB, Ramazan, AZ trainer voice) — ən güclü moat (12-24 ay)
2. 🥈 **Native AZ keyfiyyəti** (BetterMe MT-ni keç) — 3-6 ay moat
3. 🥉 **Transparent billing + AZN local payment** (m10/Pulpal) — trust differentiator
4. AI video — marketing hook, uzunmüddətli moat **zəif** (commodity 12 ayda)
5. ~~3D model~~ — Faza 2 (user-stated pain deyil)
6. Home+gym hybrid — orta moat (6-12 ay)

---

## Monetizasiya

- **Free tier:** 5 base workout + 30 hərəkət + 1 plan generasiya/ay + kalori tracker (NTC pulsuz təhdidini qarşıla)
- **Premium aylıq: 8 AZN** (BetterMe AZ illik effektiv qiymətə yaxın)
- **Premium illik: 60 AZN** (~5 AZN/ay effektiv, 38% endirim, **yanvar push absolute**)
- **Lifetime: 199 AZN** (Strong-dan ilham, sıxı loyalty)
- **Ödəniş:** RevenueCat → Apple/Google IAP + m10/Pulpal/UnipayGO local card (Faza 1 araşdırma)
- **Faza 2:** Diet modulu premium add-on
- **⚠️ Kritik:** Aylıq retention sektörel orta 17% — illik plana push absolute zərurət (yanvar campaign + onboarding default illik)

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
