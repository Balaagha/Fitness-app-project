---
project_name: 'fitnessApp'
date: '2026-05-22'
version: '0.1-stub'
status: 'deferred-stub'
workflowType: 'prd'
prd_scope: 'paywall-touchpoint'
supersedes_section_in: 'prd-auth-onboarding-2026-05-12.md §3.10 / §3.9 / §1.2 G2 / §10.7'
inputDocuments:
  - prd-auth-onboarding-2026-05-12.md
  - docs/project-context.md
---

# Paywall Touchpoint — Deferred PRD Stub

**Status:** STUB · MVP auth/onboarding PRD scope-undan kənar · gələcək billing PRD-də genişləndirilməli.

## Niyə deferred?

Paywall touchpoint Sample Workout Preview-dan **sonra** trigger olunur — yəni first-value-preview tamamlandıqdan sonra. Auth/onboarding PRD-si yalnız trigger nöqtəsini sənədləşdirir; tam paywall ekranı, video arka plan production, RevenueCat inteqrasiyası, local payment provider (m10/Pulpal/UnipayGO) seçimi və restore-purchase axını ayrıca billing PRD-də qərarlaşdırılmalıdır. Trust differensiator (transparent billing) bizim 3-cü ən güclü moat-ımız olduğu üçün dedicated PRD vacibdir.

## Köhnə PRD-də alınmış qərarlar (saxlanılır)

- **İki seçim məcburi:** "7 günlük pulsuz sınaq" VƏ "İndi illik al (60 AZN)" — tək seçim YASAQ (CLAUDE.md qəti qadağa).
- **Video arka plan:** 2.9x conversion uplift sənədləşdirilib; statik image fallback yalnız low-bandwidth halında.
- **Trigger nöqtəsi:** Sample Workout Preview tamamlandıqdan sonra (paywall **value-dan sonra**, ondan əvvəl yox).
- **AI disclosure-dan sonra:** Apple 2025 tələbi onboarding-da AI açıqlamasından sonra gəlir.
- **Transparent billing məcburiyəti:** in-app cancel button paywall-da görünən link kimi; opaque/auto-renewal-only billing YASAQ (CLAUDE.md).
- **Trial terms açıq:** "7 gün sonra avtomatik 60 AZN/il" tam dilli, fine-print qadağan.
- **Premium qiymət strukturu:** aylıq 8 AZN, illik 60 AZN (~5 AZN/ay effektiv, 38% endirim), lifetime 199 AZN.
- **Ödəniş infrastrukturu:** RevenueCat orchestration → Apple/Google IAP primary; local payment (m10/Pulpal/UnipayGO) Faza 1 araşdırma.
- **Apple Sign-In bağlılığı:** auth PRD həll edir; billing PRD-də restore-purchase Apple ID-yə bağlanır.
- **Yanvar push absolute:** illik plana migration üçün yanvar campaign sənədləşdirilib (retention sektörel 17%).
- **Free tier kontekst:** paywall 1 plan/ay cap-ə çatdıqda, 6-cı foto-kalori çağırışında, premium-only feature gate-ində trigger ola bilər (project-context.md §9).

## Açıq suallar / qeyri-müəyyənliklər

- [ ] m10 API feasibility — sandbox access, settlement timing, refund flow.
- [ ] Pulpal vs UnipayGO seçimi — komissiya, AZN currency support, RevenueCat compatibility.
- [ ] İllik 60 AZN display formatı: "60 AZN/il" vs "5 AZN/ay (illik faturalanır)" — A/B candidate.
- [ ] Restore-purchase UX: Apple ID-dən logout olmuş user, cross-device restore.
- [ ] Lifetime 199 AZN paywall-da default göstərilirmi yoxsa secondary "Daha çox seçim" altında?
- [ ] Promo code / referral kodu MVP-də varmı?
- [ ] Region-based pricing (TR genişlənmə üçün) — TRY currency, qiymət fərqi.
- [ ] Win-back paywall variant (subscription cancel olunduqda 7 gün sonra).

## Bağlılıqlar

- `prd-billing-revenuecat` (planlanır) — bu stub onun bir alt-bölməsi olacaq.
- `prd-sample-workout-preview-deferred-2026-05-22.md` — trigger nöqtəsi ondan sonra.
- `prd-auth-onboarding-2026-05-12.md` — Apple Sign-In restore-purchase axını üçün.
- Faza 1 local payment provider feasibility araşdırması (analyst — Mary).

## Suggested next step

Billing PRD billing infrastruktur araşdırması tamamlandıqdan sonra **John + Mary** (PM + Analyst) birlikdə açır. Trigger: m10/Pulpal feasibility report finalize olduqda. Architecture review-da Winston RevenueCat webhook + Supabase Edge Function inteqrasiyasını gözdən keçirir.
