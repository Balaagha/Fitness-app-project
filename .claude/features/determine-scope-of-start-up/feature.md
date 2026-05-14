# Research: determine-scope-of-start-up

## Metadata
- **Type**: research
- **Branch**: `n/a`
- **Started**: 2026-05-10
- **Current phase**: 1 / 3
- **Overall status**: in_progress
- **Related features**: none

---

## Question
Azərbaycan bazarı üçün AI-dəstəkli fitness tətbiqinin tam məhsul scope-unu, texniki arxitekturasını və rəqabət mövqeyini müəyyən etmək.

## Why It Matters
Bu tədqiqat aşağıdakı qərarları açacaq:
- Hansı platformadan başlamaq lazımdır (iOS-first, Android-first, ya cross-platform KMM)
- AI video/3D model generasiyası üçün hansı texnologiya stack seçilməli
- MVP-dən başlayaraq hansı feature-lar priority olmalı
- Azərbaycan bazarının xüsusiyyətlərini nəzərə alan monetizasiya modeli
- Diet proqramı ilə gələcək inteqrasiya arxitekturası (faza 2)

---

## Tentative Tech Stack (dəqiqləşdiriləcək)

| Qat | Texnologiya | Status |
|-----|-------------|--------|
| Mobile | Kotlin Multiplatform Mobile (KMM) | Tentative |
| Backend | Supabase (auth, DB, storage, realtime) | Tentative |
| Frontend/Web | Vercel | Tentative |
| AI Content (video, 3D) | Google Cloud AI (aktiv $300 kredit) | Tentative |

> Qeyd: Bütün seçimlər tentative-dir, araşdırma mərhələsində dəqiqləşdiriləcək.

---

## Phases

### Phase 1: Scope the Question
- [x] Proyektin əsas istiqamətini müəyyən et
- [ ] Sub-sualları sırala (bazar, texnologiya, rəqabət, monetizasiya)
- [ ] "Done" meyarını müəyyən et: detallı decision memo
- **Status:** in_progress

### Phase 2: Investigation
- [ ] Azərbaycan fitness bazarını analiz et (istifadəçi sayı, rəqiblərin penetrasiyası)
- [ ] BetterMe və analoji rəqiblərin feature-larını araşdır
- [ ] KMM + Supabase inteqrasiyasının feasibility-sini yoxla
- [ ] Google AI free tier limitlərini (video generasiya) qiymətləndir
- [ ] AI-generasiya edilmiş 3D hərəkət modellərinin alternativlərini araşdır
- [ ] Ev vs zal idmanı feature-ları üçün content tələblərini müəyyən et
- **Status:** pending

### Phase 3: Synthesize & Recommend
- [ ] MVP feature siyahısını prioritetlər üzrə yaz
- [ ] Texniki arxitektura qərarlarını əsaslandır
- [ ] Monetizasiya modelini (freemium/subscription) tövsiyə et
- [ ] Diet proqramı inteqrasiyası üçün interfeys planla
- **Status:** pending

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

## Notes for Next Session