# fitnessApp — Claude Code Session Guide

## Proje Kimliği

Azərbaycan bazarına yönəlmiş AI-dəstəkli fitness tracking tətbiqi. Həm ev, həm zal idmanını dəstəkləyir. **Əsas pozisiya:** yerli content depth (AZ food DB, Ramazan, AZ trainer voice) + native AZ keyfiyyəti + transparent billing + adaptiv AI. KMP + Supabase + Google Cloud AI üzərindən qurulur.

> ⚠️ **Strateji düzəliş (2026-05-12 market research):** BetterMe artıq Azərbaycan dilini dəstəkləyir (machine-translated). "AZ dili = əsas moat" hipotezi YANLIŞ. Real moat: **yerli content + native quality + trust (transparent billing)**.

> 🎯 **Repositioning (2026-05-21, BMad party-mode: Mary/John/Winston):** AI plan generasiyası **headline DEYİL** — dəstək qatıdır. Əsas vəd: istifadəçi məşq prosesini **uçtan-uca** idarə edir (logger + management + trainer-grade content). UX-də AI duygusal kahraman edilmir — kahraman istifadəçidir.
> - **MVP pozisiyası (App Store-da satılan vəd):** "Antrenmanını ileri teknika & super-set ilə planlayıb-izləyib-ölçən; AI proqram *təklif edən*, son sözü istifadəçiyə buraxan — AZ dilində, yerli yeməklər & şəffaf qiymətlə qurulmuş ilk uçtan-uca fitness tətbiqi."
> - **Tam vizyon (pitch/marketing — MVP-də VƏD EDİLMİR):** AI proqram qurur → istifadəçi uyğunlaşdırır → gerçək məşqçi uyğunluğu yoxlayır → uçtan-uca fitness platforması.

**Status:** Active scope phase — Solo developer, ~20h/həftə, IBAM full-time yanında.

> 📘 **Kanonik referans:** Product UX müqaviləsi · auth · AI plan JSON kontraktı · persona matrisi · foto-kalori stack · edge case qaydaları · **vizual kimlik / Volt renk sistemi (§1b)** → **`docs/project-context.md` v3.3 (2026-05-21)**. Bu fayl yalnız tech stack, hard constraints, qiymət, competitive context saxlayır — agentlər ikisini birlikdə oxuyur.

> 🎨 **Vizual kimlik (2026-05-21):** Renk sistemi **Volt** — Ladder-ilhamlı elektrik-sarı accent `#E6FF00` (`on-volt` `#0E0E0E`), koyu taban `#0A0A0B`/`#141416`/`#1E1E21`, dekorativ yaşıl-zeytun `moss` `#A4B82B`, yaşıl chip `success` `#3DD68C`. Enerjik · motivasiyaverici · minimalist. Sarı daşıyıcı renkdir (bol işlənir, splash tam-sarı). Tam token cədvəli + doktrinası: **project-context.md §1b** (kanonik). Köhnə turuncu `#FF6B33` **ləğv**.

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
- KMP/Supabase/SwiftUI/Compose doc üçün Context7 MCP istifadə et

### Qəti Qadağalar
- **HEÇVAXT** Supabase cədvəli üçün RLS atlama
- **HEÇVAXT** video/media URL-lərini public et — signed URL (TTL ≤ 1h) məcburidir
- **HEÇVAXT** Faza 2 xüsusiyyətlərini (diet modulu, real-time kamera analizi, sosial, wearable, **3D model**) MVP-yə əlavə etmə
- **HEÇVAXT** internet olmadan işləməyən core məşq funksionallığı yaz — offline-first məcburidir
- **HEÇVAXT** Google AI API-yi cost estimate olmadan çağır — $300 kredit sabit limit
- **HEÇVAXT** machine-translated AZ string ship etmə — hər lokalizasiya stringi manual review tələb edir (BetterMe MT-yə qarşı native quality moat-ımızın əsası)
- **HEÇVAXT** opaque/auto-renewal billing dizayn etmə — in-app cancel button + transparent trial terms məcburidir (BetterMe/Freeletics şikayət vektoru)
- **HEÇVAXT** onboarding **məcburi** suallarını 7-dən artıq et — qalan 19 sahə progressive profiling kimi opsiyoneldir (kontrakt: project-context.md §3.2; cycle_tracking Faza 2-yə deferred)
- **HEÇVAXT** streak mexanizmi olmadan launch et — domain research ilə doğrulandı: ən güçlü retention aracı; Streak Freeze (ayda 1) ilə birlikdə P0
- **HEÇVAXT** Privacy Policy (AZ+RU+EN) olmadan App Store/Google Play-ə submit et — launch blocker; Google Health Declaration da məcburidir (avqust 2024+)
- **HEÇVAXT** paywall-da yalnız bir seçim göstər — "7 günlük sınaq" VƏ "İndi illik al" iki seçim; video arka plan 2.9x conversion verir
- **HEÇVAXT** onboarding-da AI açıqlaması olmadan gön­dər — Apple 2025 tələbi: "Bu plan AI tərəfindən yaradılır"
- **HEÇVAXT** user-tap-i AI runtime çağırışına bağla — plan AI yalnız §5.2-dəki 5 trigger-də işə düşür; media generasiyası yalnız admin panel
- **HEÇVAXT** foto-kalori GPT-4o çağırışını free user üçün cap olmadan burax — gündə 5 hard limit, 6-cı soft upsell (project-context.md §7, §9)
- **HEÇVAXT** persona-cell-siz generic plan/copy generasiya et — `{context × sex × goal}` 8-cell matrisi default (project-context.md §6)
- **HEÇVAXT** pregnancy_postpartum=true user-də AI plan generasiya et — hard-stop: curated static template + medical disclaimer məcburi (project-context.md §6.3)
- **HEÇVAXT** account delete-də cascade tamamlanmadan return et — `users → … → calorie_logs` + Storage purge + 30 gün soft-delete grace (project-context.md §10.3)
- **HEÇVAXT** MVP marketinqində / App Store təsvirində / onboarding-da trainer və ya professional coaching xüsusiyyətini vəd etmə — Faza 2; vəd-teslimat boşluğu transparent-billing trust moatını zədələyir
- **HEÇVAXT** premium "mütəxəssis yoxlaması"nı "trainer / canlı məşqçi / personal coach" kimi təqdim etmə — bu **human approval / quality gate**-dir (AI plan göndərilmədən əvvəl təhlükəsizlik/uyğunluq yoxlaması); fərdi məşqçi-istifadəçi münasibəti, chat, fərdi koreksiya YOXDUR. "məşqçi/coach/trainer" sözü copy/marketing-də qadağan; düzgün termin: "**mütəxəssis yoxlaması**" və ya "**uyğunluq yoxlaması**" (AI Disclosure copy: prd-auth-onboarding §3.5)
- **HEÇVAXT** onboarding/UX-də AI-ı duygusal kahraman et — AI dəstək qatıdır; copy "AI sənə plan qurdu" yox, "öz məşqini idarə et" (project-context.md §0)
- **HEÇVAXT** "trainer onayı" üçün hüquqi mətndə "approval/təsdiq" sözünü işlət — "**uyğunluq yoxlaması**" (safety-fit review) məcburi; mesleki sorumluluk dilini azaldır
- **HEÇVAXT** köhnə turuncu accent (`#FF6B33`) və ya project-context §1b xaricində renk dəyəri işlət — **Volt renk sistemi** (`#E6FF00`) kanonikdir; bütün token-lər yalnız §1b cədvəlindən oxunur; `volt` üzərində ağ metin və `moss`-u CTA kimi işlətmək qadağandır

---

## Tech Stack (Tentative — Architecture phase-də dəqiqləşdiriləcək)

| Qat | Texnologiya | Qeyd |
|-----|-------------|------|
| Mobile | Kotlin Multiplatform (KMP) | iOS + Android (rebrand 2023 — "Mobile" suffix-i çıxdı; CMP 1.11.0 May 2026) |
| Backend | Supabase | Auth, PostgreSQL, Storage, Realtime, Edge Functions |
| Admin / Web | Next.js → Vercel | Content CMS, landing page |
| AI Video | Google Cloud — **Veo 3.1 Fast** ($0.15/s × 8s = **$1.20/hərəkət**) | $300 kredit; admin-only manual trigger; 100 hərəkət = $120 ($180 reserve) |
| AI Plan + Insights | LLM via Supabase Edge Function (sparse-cached, 5 trigger) | JSON kontrakt: project-context.md §5.3 |
| Photo Food Recognition | **GPT-4o Vision** (~$0.005/img) + AZ Top-200 DB lookup | Free 5/gün cap; food-specific API-lər (LogMeal/Foodvisor) AZ coverage 0 → rədd |
| Billing | RevenueCat + Apple/Google IAP + m10/Pulpal/UnipayGO (Faza 1 araşdırma) | Transparent billing trust differentiator |
| ~~3D Assets~~ | ~~Mixamo/Sketchfab~~ | **Faza 2-yə keçirildi — user pain deyil, AI kredit yandırır** |
| Exercise Video | İlk 50 manual+Mixamo, 50+ Veo Fast gradual | Hibrid pipeline; non-Ultra Veo watermark → branded overlay crop məcburi |

---

## Arxitektura Əsasları

### KMP Strukturu
- **shared/** — biznes məntiqi, data layer, Ktor network, SQLDelight local DB
- **iosApp/** — SwiftUI UI layer
- **androidApp/** — Jetpack Compose UI layer
- Platform-specific kod minimum; shared-də saxla
- State management: per-platform native (SwiftUI @State/@StateObject, Compose ViewModel)

### Supabase Təhlükəsizlik
- RLS hər cədvəl üçün schema ilə birlikdə dizayn edilir — sonradan əlavə edilmir
- Video/3D model URL → signed URL, TTL ≤ 1 saat
- Edge Functions: AI API proxy, payment webhook, RLS bypass logic (server-side only)

### Supabase MCP — Autonomous Agent Mode (2026-05-23)
**Qoşulu project:** `hhncxbmzmuemqvbajpog` (dev-only — production-a heç vaxt bağlama).

**Permission modeli — qəsdən geniş:** Agent autonomous işləməlidir, ona görə MCP konfigi **read-write + bütün feature qrupları açıq** (`docs, account, database, debugging, development, functions, branching, storage`). `read_only=true` **işlədilmir**. Agent SQL exec, schema modify, migration, edge function deploy, branch yaratma — hamısını insan onayı gözləmədən etməlidir.

**Bu autonomluğa bağlı qaydalar (toxunulmaz):**
1. **Yalnız dev project** — istənilən production ref-ə MCP bağlamaq qadağa. Production schema dəyişikliyi: manual `supabase db push` + insan review.
2. **Hər DDL-dən sonra advisor məcburi** — `mcp_supabase_get_advisors` (security + performance) çağırışı yeni table/view/policy yaradanda dərhal işlədilir; warning varsa qaydalanmadan davam etmə.
3. **RLS qaydası dəyişməz** — Supabase project-də "automatic RLS" on (yeni table avtomatik RLS-li), "auto-expose new tables" off. Agent table yaradanda RLS policy-ni eyni transaction-da yazmalıdır.
4. **`security_invoker=true` views üçün məcburidir** — agent-skills pluginin qaydası; views default RLS bypass edir.
5. **Schema akışı:** `execute_sql` ilə birbaşa iteration → `get_advisors` keç → yalnız stabil olanda `apply_migration` ilə migration commit (hər DDL üçün migration yaratma).
6. **Destructive əməliyyat** (DROP TABLE, DELETE FROM \<table\> WHERE-siz, TRUNCATE) — dev project-də belə agent əvvəlcə backup snapshot yaratmalı və ya istifadəçi onayı istəməlidir.

**Auth modeli:** OAuth (PAT yox) — `.mcp.json` token saxlamır, hər developer öz Supabase hesabı ilə `/mcp` → Authenticate edir. `.mcp.json` git-ə commit oluna bilər (təhlükəsizdir).

### AI Content Pipeline
1. Admin panel (Vercel) → yeni hərəkət əlavə
2. **Phase 1 (ilk 50 hərəkət):** Manual çəkim + Mixamo animation — keyfiyyət riski sıfıra endir
3. **Phase 2 (50+ hərəkət):** Edge Function → **Veo 3.1 Fast** ($1.20/hərəkət) + cost log + max 2 retry exponential backoff (billed-only failures `ledger.failed_cost_logged=true`)
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
                     experience_level, equipment_inventory text[], weekly_days,
                     session_duration_min, device_tz, last_workout_at_utc)
exercises           (id, name_az, name_ru, name_en, category, muscle_groups[],
                     equipment_required, video_url, model_3d_url, difficulty,
                     instructions_az, instructions_ru, instructions_en)
workouts            (id, user_id, week_number, created_at)
workout_sessions    (id, workout_id, day_of_week, session_type) -- warmup/main/cooldown
workout_exercises   (id, session_id, exercise_id, sets, reps, rest_sec, order_index,
                     load_type, load_value, alternatives uuid[], tempo, rpe_target, notes_az)
progress_logs       (id, user_id, date, weight_kg, body_measurements jsonb, photos[])
calorie_logs        (id, user_id, date, target_kcal, consumed_kcal, protein_g,
                     carbs_g, fat_g, water_ml)
```

---

## MVP Scope (Faza 1 — ~4 ay)

**P0 — Olmadan release yoxdur:**
- **Auth:** Email+Pwd · Apple Sign-In (iOS məcburi) · Google Sign-In (Android primary) — project-context.md §3.4
- **Onboarding:** 7 məcburi sual ≤90 sn + 20 opsiyonel progressive (kontrakt: project-context.md §3)
- **Minimal Design Principle**: feature əlavə = mövcud element çıxarılır (project-context.md §1)
- **Persona davranış matrisi**: `{context × sex × goal}` 8-cell + AZ-kültür adapterləri (project-context.md §6)
- **Manual Plan Builder** + **AI Plan** sparse-cached JSON kontraktı (project-context.md §5)
- Hərəkət kitabxanası 50-100 — AZ/RU/EN, Hevy-paterni filtr, media triple (GIF auto · Veo MP4 tap · YouTube short) (project-context.md §4)
- Set/rep logger + rest timer + **super-set & ileri set-tipləri (drop-set, rest-pause, tempo)** + performance metrics (`completion_pct`, `tonnage_kg`, `rpe_avg`) (project-context.md §5.5) — ⚠️ super-set logger state-machine-ə varyant əlavə edir; sprint planning-də kompanzasyon tradeoff məcburi (P0 dolu)
- Kalori hədəfi (Mifflin-St Jeor BMR + TDEE) + su (35 ml/kg) + protein (per-goal cədvəl) (project-context.md §5.5)
- **AZ Top-200 yemək DB** + **foto-kalori** (GPT-4o Vision → AZ DB lookup) + **light meal suggestion** (project-context.md §7)
- AZ/RU/EN tam **native** lokalizasiya (MT qadağan)
- **Transparent billing**: RevenueCat + in-app cancel + açıq trial terms
- **Free tier**: 5 base workout + 30 hərəkət + 1 plan/ay + foto-kalori 5/gün + Streak Freeze 1/ay (tam cədvəl: project-context.md §9)
- **Streak + Streak Freeze** (2-ci miss → "Bu ay Freeze istifadə olundu" copy)
- **Privacy Policy** (AZ+RU+EN) + **Google Health Declaration** (launch blocker)
- **In-app hesab silmə** + cascade purge + 30 gün soft-delete grace (project-context.md §10.3 — launch blocker)
- **Video arka planlı paywall** + iki seçim (trial + illik) (2.9x conversion)
- **Onboarding-da AI açıqlaması** (Apple 2025 tələbi)
- **Safety hard-stops:** pregnancy_postpartum → AI plan generasiyası bloklanır; modifier precedence pregnancy > injury > Ramazan > home_only_F > cut (project-context.md §6.3)

**P1 — Mümkünsə release-ə daxil:**
- Progress tracker (çəki, ölçülər, foto)
- Push notification AZ-da (məşq xatırlatması)
- Adaptive plan adjustment (missed session recovery)
- **Ramazan mode** (suhoor/iftar split macros — TR ekspansiya üçün dəyər)
- m10/Pulpal/UnipayGO local payment inteqrasiyası
- Professional coaching "yaxında" ekranı (e-mail toplama, talep sinyali — sıfır mühendislik, Faza 2 hazırlığı)

**Qəti Faza 2 (sonrakı release):** Diet modulu, real-time hərəkət analizi, sosial, wearable, **3D model per exercise**, AI personal coach chat, **trainer review loop** (AI plan → user edit → gerçek trainer "uyğunluq yoxlaması"; managed model — 1-2 əl-seçimli trainer; açıq marketplace = Faza 3), **professional-direct coaching** (AI-suz, birbaşa trainer ilə, yüksək qiymət), **server-tetikli push notification**

---

## Coding Standards

### Ümumi
- KMP shared module-da `expect/actual` yalnız platform API üçün
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

- **Free tier:** 5 base workout + 30 hərəkət + 1 plan generasiya/ay + kalori tracker + **foto-kalori 5/gün** + **Streak Freeze 1/ay** (tam davranış cədvəli: project-context.md §9; AI plan re-gen aylıq cap free 4 · premium 12)
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

- **[determine-scope-of-start-up]** — Proyektin tam scope-u, stack, rəqabət mövqeyi (aktiv — 2026-05-16; project-context.md v3 ilə tamamlandı)
