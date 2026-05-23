---
project_name: 'fitnessApp'
date: '2026-05-22'
version: '0.1-stub'
status: 'deferred-stub'
workflowType: 'prd'
prd_scope: 'first-value-preview'
supersedes_section_in: 'prd-auth-onboarding-2026-05-12.md §3.9 / §1.2 G2 / §17.1'
inputDocuments:
  - prd-auth-onboarding-2026-05-12.md
  - docs/project-context.md
---

# Sample Workout Preview — Deferred PRD Stub

**Status:** STUB · MVP auth/onboarding PRD-də trigger nöqtəsi sənədləşib · tam ekran spesifikasiyası gələcək PRD-də.

## Niyə deferred?

Sample Workout Preview "first-value" momentidir — paywall conversion-ın əsas drayveri (G2 hədəfi: ≥95% view-rate). Lakin tam ekran spesifikasiyası exercise library content (50 hərəkət) hazır olmadan finalize edilə bilməz. Auth/onboarding PRD yalnız trigger nöqtəsini (AI disclosure → Sample Preview → Paywall) sıralayır; nümunə hərəkətin seçim məntiqi, media format, narration və persona-cell adaptasiyası ayrıca qərarlaşdırılmalıdır.

## Köhnə PRD-də alınmış qərarlar (saxlanılır)

- **Yerləşmə:** AI disclosure ekranından **sonra**, paywall-dan **əvvəl** — first-value moment.
- **Hədəf metrika:** ≥95% view-rate (G2 goal); skip rate <5%.
- **AI plan generasiyasından əvvəl göstərilir:** instant gratification — user "demək olar ki, hazır" hissini alır; tam AI plan paywall-dan sonra generasiya olunur.
- **1 sample exercise:** Faza 1-də 50 manual+Mixamo hərəkətlik kitabxanadan biri (production keyfiyyəti təmin edilmiş).
- **Copy müqaviləsi:** "Bu yalnız nümunədir — tam planın paywall-dan sonra hazırlanır" (vəd-teslimat boşluğunu sıfıra endirir).
- **AI duygusal kahraman EDİLMİR:** copy "AI sənə plan qurdu" yox, "öz məşqini idarə et" tonunda (CLAUDE.md repositioning).
- **Media triple müqaviləsi (project-context.md §4):** GIF auto-play default; user tap-da Veo MP4 / YouTube short açıla bilər.
- **Trainer/coaching vədi YASAQ:** sample preview "professional trainer hazırladı" tonunda OLA BİLMƏZ (CLAUDE.md).

## Açıq suallar / qeyri-müəyyənliklər

- [ ] Nümunə hərəkət seçim məntiqi: **statik** (hamı üçün eyni — "Bodyweight Squat") vs **persona-cell-ə dinamik** (`{context × sex × goal}` 8-cell matrisinə uyğun seçilir, project-context.md §6)?
- [ ] Media format: video (Mixamo render MP4) vs GIF — bandwidth tradeoff (AZ 4G coverage).
- [ ] Audio narration: AZ voice-over (Faza 1 native quality moat ilə tutarlı) vs susqun + altyazı?
- [ ] AZ + RU + EN dublyaj eyni anda Faza 1-də realistdirmi yoxsa AZ only launch?
- [ ] Set/rep/rest sample dəyərlər user goal-una uyğunlaşdırılırmı yoxsa generic 3×10?
- [ ] "Tam planı görmək üçün davam et" CTA copy A/B variantları.
- [ ] Skip button-u olsun? (G2 view-rate hədəfini qoruyaraq force-view etmək risk-conversion tradeoff).
- [ ] Pregnancy_postpartum=true user üçün sample necə dəyişir (hard-stop curated template-ə bağlı)?
- [ ] Ramazan mode aktiv user-də sample timing copy-si dəyişirmi?
- [ ] Sample exercise duration: 30 saniyəlik teaser vs tam 1 dəqiqəlik?

## Bağlılıqlar

- `prd-workout-execution` (planlanır) — 50 hərəkətlik content library hazır olmalıdır.
- `prd-ai-plan-generation` (planlanır) — preview "tam plana" köprü kimi pozisiyalanır.
- `prd-paywall-deferred-2026-05-22.md` — preview-dən sonra trigger.
- Content production pipeline (manual + Mixamo, Faza 1 ilk 50 hərəkət).

## Suggested next step

Content team 50 hərəkətlik manual+Mixamo library-ni tamamladıqda **Sally + John** (UX + PM) birlikdə PRD açır. Trigger: exercise library content-freeze. A/B test plan (statik vs persona-dynamic seçim) Murat (TEA) ilə hazırlanır — view-rate G2 hədəfini doğrulamaq üçün.
