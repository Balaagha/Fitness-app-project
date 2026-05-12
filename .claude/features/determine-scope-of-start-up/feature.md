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

## Findings / Sources

**2026-05-11**

1. Google Cloud-da $300 aktiv kredit var (b.alihummatov@gmail.com) — AI video/3D generasiya ilk mərhələdə bu kredit üzərindən ediləcək. Kredit bitdikdə ödənişli plana keçmək lazımdır. Güvən: Yüksək.
2. Əsas rəqiblər (BetterMe, MyFitnessPal, Nike Training Club, Freeletics) Azərbaycan dilini dəstəkləmir — bu güclü differensiator-dur. Güvən: Yüksək.
3. Azərbaycan bazarı üçün hədəf qiymət 5–10 AZN/ay — BetterMe analoji qiymətlərə uyğun, yerli alıcı gücünü nəzərə alır. Güvən: Tentative (araşdırılacaq).

## Errors Encountered

| Error | Attempts | Resolution |
|-------|----------|------------|

---

## Progress Log

- 2026-05-10 — Research initialized
- 2026-05-10 — Tentative tech stack qeyd edildi: KMM + Supabase + Vercel + Google AI
- 2026-05-11 — CLAUDE.md tam dolduruldu; 4 qərar + 3 finding feature.md-ə yazıldı

## Notes for Next Session