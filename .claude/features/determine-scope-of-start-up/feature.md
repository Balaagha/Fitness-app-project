# Research: determine-scope-of-start-up

## Metadata
- **Type**: research
- **Branch**: `n/a`
- **Started**: 2026-05-10
- **Current phase**: 3 / 3
- **Overall status**: in_progress
- **Related features**: none

---

## Question
Azərbaycan bazarı üçün AI-dəstəkli fitness tətbiqinin tam məhsul scope-unu, texniki arxitekturasını və rəqabət mövqeyini müəyyən etmək. **Son çıxış:** `docs/project-context.md` — minimal söz sayı ilə bütün startup kontekstini daşıyan, BMad agentlərinin istifadə edəcəyi konstitusiya faylı.

## Why It Matters
Bu tədqiqat aşağıdakı qərarları açdı (əksəriyyəti tamamlandı):
- ✅ Platform: KMM (iOS + Android birgə) — cross-platform tamamlandı
- ✅ AI stack: GCP AI kredit hibrid pipeline — manual ilk 50, sonra AI gradual
- ✅ MVP feature priority: P0/P1/Faza-2 ayrışdı (15 P0, 6 P1, 6 Faza-2)
- ✅ Monetizasiya: Free + 8 AZN/ay + 60 AZN/il + 199 AZN lifetime
- ✅ Rəqabət mövqeyi: yerli content > native quality > transparent billing
- ⏳ Qalan: KMM+Supabase texniki feasibility (bmad-technical-research)
- 🎯 **Final deliverable:** sıxılmış `project-context.md` (minimal, agent-optimized)

---

## Confirmed Tech Stack

| Qat | Texnologiya | Status |
|-----|-------------|--------|
| Mobile | Kotlin Multiplatform Mobile (KMM) | ✅ Confirmed |
| Backend | Supabase (auth, DB, storage, realtime, Edge Fn) | ✅ Confirmed |
| Admin/Web | Next.js → Vercel | ✅ Confirmed |
| AI Content | Google Cloud AI ($300 kredit — hard limit) | ✅ Confirmed |
| Billing | RevenueCat + Apple/Google IAP | ✅ Confirmed |
| Local Payment | m10 / Pulpal / UnipayGO | ⏳ Faza 1 araşdırma |
| Exercise Video | İlk 50 manual+Mixamo, 50+ AI gradual | ✅ Confirmed |
| 3D Assets | ~~Mixamo/Sketchfab~~ | ❌ Faza 2-yə keçirildi |

---

## Phases

### Phase 1: Scope the Question ✅ COMPLETE
- [x] Proyektin əsas istiqamətini müəyyən et
- [x] Sub-sualları sırala (bazar, texnologiya, rəqabət, monetizasiya)
- [x] "Done" meyarını müəyyən et: `docs/project-context.md` agent-optimized deliverable
- **Status:** ✅ complete

### Phase 2: Investigation ✅ MOSTLY COMPLETE
- [x] Azərbaycan fitness bazarını analiz et — BetterMe AZ-da var (MT), NTC pulsuz, Freeletics $35/ay
- [x] BetterMe və analoji rəqiblərin feature-larını araşdır — opaque billing, 26 sual onboarding, MT keyfiyyəti
- [x] Google AI kredit limitlərini qiymətləndir — $300 sabit, hibrid pipeline qərarı
- [x] 3D hərəkət modeli alternativlərini araşdır — user pain deyil, Faza 2-yə keçirildi
- [x] Ev vs zal idmanı content tələbləri — per-exercise alternativ hərəkət seçimi
- [ ] **KMM + Supabase texniki feasibility** — `bmad-technical-research` ilə tamamlanacaq (növbəti addım)
- **Status:** ⏳ 1 item qalan (KMM+Supabase tech research)

### Phase 3: Multi-Agent Synthesis → Final project-context.md 🎯 AKTİV

> **Hədəf:** ~156 sətirlik (mövcud 272-dən 43% azaldılmış) agent-optimized `docs/project-context.md`.
> Mövcud content 85% hazırdır — yalnız 1 boşluq (KMM tech feasibility) + yenidən strukturlaşdırma + 4 YENİ section (Roadmap, Success Metrics, Full Feature Catalog, Functional Decomposition) lazımdır.

#### Wave A — Input Gap Closure (SEQUENTIAL, ~30 dəq)
- [ ] **`bmad-technical-research`** → `docs/tech-feasibility.md`
  - Prompt: "KMM 1.9+ production iOS/Android with Supabase Kotlin SDK + Ktor; Edge Functions cost for AI proxy; $300 GCP credit lifetime on Veo/Kling for 50 exercise videos."
  - Bu yeganə real research gap-dir.

#### Wave B — Specialist Perspectives (PARALLEL, fresh chats, ~45 dəq hər biri)
4 paralel session, hər biri ayrı agent persona ilə. Hər biri öz section-ları üçün delta md yazır.

- [ ] **`bmad-agent-analyst` (Mary)** → `_bmad-output/perspective-analyst.md` (~150 sətir)
  - Owns: Business Overview, Vision, Problem/Solution, Competitive Context, Differentiator Ranking
  - Prompt: "Read current project-context.md. Critique Business Overview + Competitive. Propose 2-3 NEW feature ideas grounded in BetterMe/NTC/Diyetkolik gaps."
- [ ] **`bmad-agent-pm` (John)** → `_bmad-output/perspective-pm.md` (~150 sətir)
  - Owns: MVP Scope (P0/P1/Faza-2), Full Feature Catalog, Monetization, Success Metrics per phase, Retention strategy
  - Prompt: "Audit MVP P0/P1/Faza-2 for 4-month solo. Validate 8/60/199 AZN vs retention benchmarks. Propose monetization additions (referral/family). Define Alpha/Beta/Launch metric targets."
- [ ] **`bmad-agent-architect` (Winston)** → `_bmad-output/perspective-architect.md` (~150 sətir)
  - Owns: Tech Stack, Hard Constraints, Architecture Patterns, Data Model, AI Hybrid Pipeline, Offline Strategy
  - Input əlavə: tech-feasibility.md
  - Prompt: "Using tech-feasibility, harden Tech Stack + Hard Constraints + Architecture Patterns + Data Model + AI Hybrid + Offline Strategy. Flag KMM/Supabase risks."
- [ ] **`bmad-agent-ux-designer` (Sally)** → `_bmad-output/perspective-ux.md` (~150 sətir)
  - Owns: Onboarding ≤8 sual flow, Video Paywall UX, Streak+Freeze surface, AZ localization QA pipeline
  - Prompt: "Design onboarding ≤8 sual, video paywall layout (2.9x lift), streak+freeze surface, AZ localization QA. Map to 30-day 4-workout activation metric."

#### Wave C — Synthesis (SEQUENTIAL, ~1 saat)
- [ ] **`bmad-distillator`** → `_bmad-output/project-context.draft.md`
  - Input: 4 perspective md + mövcud project-context.md
  - Target 3:1 sıxışdırma, feature.md-dəki hər Decision qorunmalı
  - Output: ~500 sətir draft
- [ ] **`bmad-generate-project-context`** → `_bmad-output/project-context.v2.md`
  - Draft-ı canonical agent-optimized structure-a çevir
  - YAML frontmatter, hər Hard Constraint downstream agent tərəfindən enforceable olmalı

#### Wave D — Quality Gates (PARALLEL where possible, ~45 dəq)
- [ ] **`bmad-review-adversarial-general`** → "v1-dən nə kritik info düşdü? Hansı constraint future agent tərəfindən yanlış başa düşülə bilər?"
- [ ] **`bmad-review-edge-case-hunter`** → "Hər constraint-i gez: BetterMe MT pump, GCP credit exhaustion, KMM Compose interop, RLS leak, App Store rejection paths."
- [ ] **`bmad-editorial-review-structure`** → "length_target: 156 lines (43% reduction)" — CUT/MERGE/CONDENSE önerilər

#### Wave E — Polish & Lock (SEQUENTIAL, ~30 dəq)
- [ ] **`bmad-editorial-review-prose`** → Türk/AZ prose tightening, Communication Prefs tonuna uyğunlaşdır
- [ ] **`bmad-checkpoint-preview`** → Human-in-the-loop diff review (v1 vs final); CUT siyahısını açıq təsdiq et
- [ ] **`bmad-agent-tech-writer` (Paige)** → final `docs/project-context.md` yaz
- [ ] `/feature-finding` — delivery log
- [ ] `/feature-end` — `determine-scope-of-start-up` archive

**Status:** ⏳ in_progress
**Total wall time estimate:** ~3.5 saat (bir focused evening session)

---

## Final Section Structure (target ~156 sətir)

> Multi-agent reviewlərinin synthesis-i. Her bölmənin owner agent-i Wave B-də müəyyəndir.

| # | Section | Lines | Owner Agent | Yeni? |
|---|---------|-------|-------------|-------|
| 1 | `## Vision & Problem` | 6 | Mary | — |
| 2 | `## Current Status & Roadmap` | 14 | John+Paige | 🆕 YENİ |
| 3 | `## Success Metrics (per Phase)` | 10 | John | 🆕 YENİ |
| 4 | `## Full Feature Catalog` | 28 | John+Mary | 🆕 YENİ (P0+P1+Faza2+Idea-Backlog) |
| 5 | `## Functional Decomposition` | 16 | Winston+Sally | 🆕 YENİ (Onboarding/Plan Gen/Workout Player/Nutrition/Progress/Paywall/Admin CMS/AI Pipeline) |
| 6 | `## Tech Stack` | 10 | Winston | — |
| 7 | `## Hard Constraints` | 16 | Winston | — (15 qayda) |
| 8 | `## Architecture & Data Model` | 26 | Winston | MERGE (data model schema duplikatlardan birini sil) |
| 9 | `## Out-of-Scope` | 8 | Paige | — |
| 10 | `## Monetization` | 8 | John | TRIM (kritik emoji və retention prose-u çıxar) |
| 11 | `## Competitive Position` | 6 | Mary | TRIM (full table → `docs/market-research.md`-ə köçür) |
| 12 | `## Solo Dev & Comms` | 8 | Paige | MERGE (Solo Constraints + Communication birləşdir) |

**Total: ~156 sətir** (mövcud 272-dən 43% azalma + 4 yeni section)

### CUT Decisions
- **Naming Conventions** → yalnız CLAUDE.md-də qalsın (duplikat)
- **Retention Benchmarks tam table** → Success Metrics altına 1 sətir footnote
- **Full Competitive table (6 rival)** → `docs/market-research.md`-ə köçür, project-context-də top-3 qalsın
- **AI Workflow steps** → artıq CLAUDE.md-də var, project-context-də sadəcə "Hybrid pipeline: 50 manual → 50+ AI gradual"
- **"araşdırma ilə yenidən tərif olundu" meta-history** → git log onsuz da var, hazırkı tense saxla

---

## Decisions / Recommendations

| Date       | Decision | Rationale | Impact |
|------------|----------|-----------|--------|
| 2026-05-11 | Tentative stack: KMM + Supabase + Vercel + Google AI | İstifadəçi tərəfindən müəyyən edildi; araşdırma mərhələsində dəqiqləşdiriləcək | Yüksək — bütün arxitektura bu stack üzərindən planlanır |
| 2026-05-11 | Diet proqramı ayrı modul kimi saxlanır, fitness app-dan yönləndirmə ilə açılacaq (faza 2) | Modullar arasında aydın sərhəd; ilk fazada fokus itməsin | Orta — interfeys dizaynına təsir edir |
| 2026-05-11 | 3D modellər üçün əvvəlcə Mixamo/Sketchfab kitabxanasından başlamaq, sonra AI generasiya | Google AI kredit limitini qorumaq; hazır kitabxana MVP üçün daha sürətli | Orta — AI kredit büdcəsinə qənaət |
| 2026-05-11 | Supabase RLS hər cədvəl üçün məcburi, video URL-lər signed URL olacaq | Güvenlik əsası — user datası heç vaxt açıq olmamalı | Yüksək — bütün DB dizaynını təsir edir |
| 2026-05-12 | BMad override-ları `_bmad/custom/config.toml` üzərindən ediləcək, `_bmad/config.toml`-a toxunulmayacaq | Installer base config-i read-only elan edir; custom/ katmanı doğru override nöqtəsidir | Yüksək — bütün BMad konfiqurasiya dəyişiklikləri bu qaydaya riayət etməli |
| 2026-05-12 | `user_skill_level=expert`, `adversarial_review_intensity=high`, `default_planning_track=bmad-method` seçildi | Solo dev + 6-7 il təcrübə; human reviewer yoxdur, adversarial bunu kompensasiya etməli | Orta — agent davranışı və review intensivliyinə təsir edir |
| 2026-05-12 | `skillListingBudgetFraction=0.03` `.claude/settings.json`-a əlavə edildi | 67 skill default %1 limiti aşır; %3 skill description drop-unu aradan qaldırır | Aşağı — yalnız Claude Code skill listing-ə təsir edir |
| 2026-05-12 21:56 | **3D model per exercise MVP-dən çıxarıldı, Faza 2-yə keçirildi** | Market research: user-stated 3D talab qaynağı tapılmadı, AI kredit yandırır, solo dev üçün non-trivial; user pain deyil | Yüksək — MVP scope kəsimi, AI kredit qənaəti, hərəkət kitabxanası video-only yetər |
| 2026-05-12 21:56 | **Pricing strukturu: Free tier + 8 AZN aylıq + 60 AZN illik + 199 AZN lifetime** | BetterMe illik effektiv $5/ay → ~8.5 AZN; aylıq retention 17% problemi → illik plan push; NTC pulsuz → güclü free tier məcburidir; lifetime tier Strong-dan ilham | Yüksək — bütün monetization və acquisition strategiyasını müəyyən edir |
| 2026-05-12 21:56 | **AZ Top-200 yemək DB P0-a daxil edildi (plov, dolma, qutab, kabab, yerli markalar)** | Diyetkolik TR-də 1.5M user bu boşluqla topladı, MFP-də AZ yeməkləri yoxdur, BetterMe-nin kopyalaması ən yavaş moat (12-24 ay) | Yüksək — yerli content moat-ın bel sütunu, manual 1-2 həftə işi |
| 2026-05-12 21:56 | **Differensiator yenidən sıralandı: yerli content > native AZ keyfiyyəti > transparent billing > AI video > 3D (kənar)** | BetterMe artıq AZ-da var (machine-translated); "AZ dili = moat" hipotezi YANLIŞ; real moat yerli content + native quality + trust | Yüksək — bütün marketing message və feature priority-ni dəyişir |
| 2026-05-12 21:56 | **Onboarding ≤8 sual limiti (BetterMe-nin 26 sualından kəs)** | "Marathon of questions" drop-off riski; D1 retention 23% sektörel orta; qısa onboarding + value-first preview activation üçün kritik | Orta — UX dizayn qərarı, qualification dərinliyi ilə trade-off |
| 2026-05-12 21:56 | **RevenueCat + m10/Pulpal/UnipayGO local payment Faza 1 P0** | BetterMe/Freeletics opaque billing ən böyük şikayət; AZN local card + asan iptal = trust differentiator; RevenueCat solo dev üçün billing/refund/analytics standart | Yüksək — bütün billing və trust infrastrukturu |
| 2026-05-12 21:56 | **Hərəkət kitabxanası MVP-də 50-100 hərəkət, ilk 50 manual + Mixamo, sonra AI gradual** | AI video keyfiyyət riski yüksək; $300 GCP limit 100+ hərəkət üçün riskli; solo dev 4 ay timeline-da AI video pipeline + hərəkət kitabxanası eyni anda bottleneck olur | Yüksək — content pipeline strategiyası, timeline realism |
| 2026-05-16 | **Multi-agent perspective approach seçildi** (Mary/John/Winston/Sally/Paige paralel, sonra Distillator+Generate-context+Adversarial+Editorial+Checkpoint sequential) | Single-shot `bmad-generate-project-context` 4 ayrı domain-i (analyst/PM/architect/UX) bir agent perspective ilə qarışdırır; section ownership ilə hər perspective öz tonunu qoruyur, synthesis daha güclü olur | Yüksək — bütün project-context.md generation pipeline-ı bu strukturda gedəcək |
| 2026-05-16 | **Target: 156 sətir (43% reduction)** — yeni 4 section (Roadmap, Success Metrics, Full Feature Catalog, Functional Decomposition) əlavə, mövcud bloat CUT | Mövcud 272 sətir agent context budget üçün ağırdır; lakin user "full app vision" istəyir → yeni section-lar lazımdır; trade-off: NEW content qazansın, bloat itsin | Yüksək — file structure və hər bölmənin uzunluq budget-i müəyyən edildi |
| 2026-05-16 | **Naming Conventions + full Retention Benchmarks table + 6-rival Competitive table → başqa fayllara köçür** (CLAUDE.md, market-research.md) | project-context.md hər session agent context-inə yüklənir — duplikat fact-lar agent budget yandırır; CLAUDE.md və market-research.md daha az tez-tez oxunur, detallı content oraya getsin | Orta — content topology dəyişikliyi, agent budget qənaəti |
| 2026-05-16 | **YENİ section: `## Functional Decomposition`** — app modul-larını sırala (Onboarding, Plan Generator, Workout Player, Nutrition, Progress, Paywall, Admin CMS, AI Pipeline) | İndi project-context.md-də feature siyahısı var amma "hara yazılacaq" yox — Winston/Sally bunu birgə doldurar, devs/architects WHERE bilsinlər | Orta — code structure qərarlarına təsir |
| 2026-05-16 | **YENİ section: `## Success Metrics (per Phase)`** — Alpha (10 tester, D1 40%), Beta (100 user, D7 25%), Launch (1K MAU, D30 15%, 5% paid conv, ARPU 4 AZN) | Hər feature debate-i numeric bar-a qarşı yoxlanmalıdır; sektör orta D30 8-12% → bizim 15% hədəfi aggressive amma evidence-based; "industry average" prose-larından imtina | Yüksək — bütün scope qərarları bu hədəflərə qarşı validate ediləcək |

## Findings / Sources

**2026-05-11**

1. Google Cloud-da $300 aktiv kredit var (b.alihummatov@gmail.com) — AI video/3D generasiya ilk mərhələdə bu kredit üzərindən ediləcək. Kredit bitdikdə ödənişli plana keçmək lazımdır. Güvən: Yüksək.
2. Əsas rəqiblər (BetterMe, MyFitnessPal, Nike Training Club, Freeletics) Azərbaycan dilini dəstəkləmir — bu güclü differensiator-dur. Güvən: Yüksək.
3. Azərbaycan bazarı üçün hədəf qiymət 5–10 AZN/ay — BetterMe analoji qiymətlərə uyğun, yerli alıcı gücünü nəzərə alır. Güvən: Tentative (araşdırılacaq).

**2026-05-12**

4. BMad 4-katmanlı TOML merge istifadə edir: `_bmad/config.toml` → `_bmad/config.user.toml` → `_bmad/custom/config.toml` → `_bmad/custom/config.user.toml` — ən yüksək prioritet ən sondadır. Güvən: Yüksək (mənbə: `resolve_config.py`).
5. Agent-spesifik `persistent_facts` `_bmad/custom/{agent-adı}.toml` faylları ilə yüklənir — `resolve_customization.py` 3-katmanlı merge ilə həll edir. Güvən: Yüksək (mənbə: kod araşdırması).
6. `_bmad/bmm/config.yaml` installer-generated YAML-dır (TOML sistemindən ayrıdır) — `user_skill_level` orada deyil, `custom/config.toml`-dakı `[modules.bmm]` ilə override edilir. Güvən: Yüksək.

## Errors Encountered

| Error | Attempts | Resolution |
|-------|----------|------------|

---

## Progress Log

- 2026-05-10 — Research initialized
- 2026-05-10 — Tentative tech stack qeyd edildi: KMM + Supabase + Vercel + Google AI
- 2026-05-11 — CLAUDE.md tam dolduruldu; 4 qərar + 3 finding feature.md-ə yazıldı
- 2026-05-12 — BMad tam konfiqurasiya edildi: custom config, agent toml-lar, project-context.md, skill budget; 3 qərar + 3 finding əlavə edildi
- 2026-05-16 — Feature.md yenidən strukturlaşdırıldı: Phase 1 ✅, Phase 2 mostly ✅, Phase 3 aktiv. Final deliverable müəyyən edildi: sıxılmış agent-optimized `docs/project-context.md`. Növbəti addım: bmad-technical-research → distillator → editorial-review-structure → generate-project-context → adversarial-general.
- 2026-05-16 (v2) — 3 paralel sub-agent (Explore inventory + Plan workflow + Plan structure) ilə dərin analiz aparıldı. Single-step plan multi-agent perspective workflow ilə əvəz edildi (5 wave: Input Gap → 4 Parallel Perspectives → Synthesis → Quality Gates → Polish). Mövcud content 85% hazır olduğu təsdiqləndi (yalnız KMM tech research boşluğu). 4 YENİ section qərarı: Roadmap, Success Metrics, Full Feature Catalog, Functional Decomposition. 156 sətir target (43% azalma). 6 yeni decision feature.md-ə yazıldı.
- 2026-05-17 — Paused on 2026-05-17 (yeni feature: find-and-install-kmm-skill-for-development-ə keçid)

## Notes for Next Session