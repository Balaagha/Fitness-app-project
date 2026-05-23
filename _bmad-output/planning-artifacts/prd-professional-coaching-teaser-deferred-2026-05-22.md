---
project_name: 'fitnessApp'
date: '2026-05-22'
version: '0.1-stub'
status: 'deferred-stub'
workflowType: 'prd'
prd_scope: 'professional-coaching-teaser'
supersedes_section_in: 'prd-auth-onboarding-2026-05-12.md §3.11 / FR-36 / Screen #28'
inputDocuments:
  - prd-auth-onboarding-2026-05-12.md
  - docs/project-context.md
---

# Professional Coaching Teaser — Deferred PRD Stub

**Status:** STUB · Faza 2 demand-signal surface · MVP-də yalnız sıfır-mühəndislik teaser ekran formatında qalır.

## Niyə deferred?

Professional coaching (gerçək trainer ilə "uyğunluq yoxlaması") qəti **Faza 2** xüsusiyyətidir (CLAUDE.md). MVP-də yalnız demand-signal toplama surface-i kimi mövcuddur — e-mail toplayır, "yaxında" mesajı göstərir, sıfır backend logic. Tam coaching marketplace / managed model qərarı və PRD-si Faza 2-də açılır. Bu stub MVP teaser surface-in copy/UX-ini gələcək tam coaching PRD-yə körpü kimi arxivləyir.

## Köhnə PRD-də alınmış qərarlar (saxlanılır)

- **Faza 2 demand-signal:** MVP-də yalnız e-mail toplama formu; coaching feature MVP-də VƏD EDİLMİR.
- **Sıfır mühəndislik:** statik ekran + form → Supabase `interest_signals` cədvəli (sadə insert).
- **MVP marketinqində trainer/coaching VƏD ETMƏ** (CLAUDE.md qəti qadağa) — App Store description, onboarding, landing page-də qadağan.
- **Vəd-teslimat boşluğu trust moat-ı zədələyir** — transparent-billing differensiatorumuz ilə uyumsuzdur.
- **Copy ton:** "Yaxında — peşəkar məşqçi dəstəyi" / "Maraq bildirimi göndər" — promise tone YOX, signal tone VAR.
- **Hüquqi termin müqaviləsi:** "**uyğunluq yoxlaması**" (safety-fit review) — "approval/təsdiq" sözü YASAQ (CLAUDE.md; mesleki sorumluluk dilini azaldır).
- **Yerləşmə:** post-paywall settings/discovery surface-də; onboarding-da göstərilmir (onboarding 7+20 sual müqaviləsi pozulmamalıdır).
- **AI ilə əlaqə:** Faza 2-də axın "AI plan → user edit → trainer uyğunluq yoxlaması" managed model (1-2 əl-seçimli trainer); açıq marketplace = Faza 3.
- **Teaser FR-36:** form submit → e-mail + locale + (optional) "nə üçün maraqlısan" çoxseçimli → `interest_signals` cədvəli.

## Açıq suallar / qeyri-müəyyənliklər

- [ ] **Managed model vs marketplace:** 1-2 əl-seçimli trainer (keyfiyyət nəzarəti, scale məhdud) vs açıq marketplace (scale, keyfiyyət riski). Faza 2 başlanğıc qərarı.
- [ ] AZ-da lisenziyalı / sertifikatlı PT-lərin tapılması: necə neçə trainer var, recruiting strategiyası.
- [ ] Qiymət strategiyası: AI-suz birbaşa-trainer paketi (yüksək qiymət, CLAUDE.md Faza 2) vs AI plan + trainer review add-on.
- [ ] Trainer ilə user kommunikasiya kanalı: in-app chat, async video review, sync session?
- [ ] Hüquqi struktur: trainer freelance contract, sorumluluk sığortası, AZ-da fitness consulting regulation.
- [ ] Demand-signal analiz threshold: neçə e-mail toplandıqda Faza 2-yə yaşıl işıq?
- [ ] Teaser-in görünmə yeri: settings-də ayrıca tab, paywall-dan sonra cross-sell, ya da bottom-nav badge?
- [ ] Coaching-i kim test edəcək: closed beta (50 user) vs açıq launch?
- [ ] Pregnancy/injury hard-stop user-lərə trainer teaser daha aqressiv göstərilirmi (medical fit yönü)?

## Bağlılıqlar

- Faza 2 trainer review loop PRD (`prd-trainer-review-loop-phase2` — planlanır).
- `prd-ai-plan-generation` (planlanır) — trainer review axını "AI plan → user edit → trainer review" sıralanmasından asılıdır.
- `prd-settings-deferred-2026-05-22.md` — teaser surface settings-də yerləşə bilər.
- Faza 1 launch metric-ləri (real user demand validation).

## Suggested next step

Faza 2 başladıqda **Mary (Analyst)** demand-signal data analysis (e-mail toplama hacmi + niyyət sorğusu) edir → John PRD açır → Winston managed vs marketplace architecture qərarı verir. Trigger: MVP launch + 3 ay user data + ≥X demand signal threshold (Mary təyin edəcək). Hüquqi araşdırma paralel başlayır.
