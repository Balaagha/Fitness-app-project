---
project_name: 'fitnessApp'
date: '2026-05-22'
version: '0.1-stub'
status: 'deferred-stub'
workflowType: 'prd'
prd_scope: 'settings-and-profile-edit'
supersedes_section_in: 'prd-auth-onboarding-2026-05-12.md §3.4 / §7.6 / §7.7 / US-1.x / §10.3'
inputDocuments:
  - prd-auth-onboarding-2026-05-12.md
  - docs/project-context.md
---

# Settings & Profile Edit — Deferred PRD Stub

**Status:** STUB · MVP auth/onboarding flow-undan kənar (post-onboarding settings surface) · gələcək settings PRD-də genişləndirilməli.

## Niyə deferred?

Auth/onboarding PRD yalnız hesab silmə cascade-ı və profile preview-u sənədləşdirir (launch-blocker olduğu üçün schema və backend axını qərarlaşdırılıb). Lakin settings surface-in tam UX-i, profile field-lərinin in-app edit axını, password/email change re-verification, notification preferences UI-ı ayrı dedicated PRD tələb edir. Bu, post-onboarding "qurulmuş user" konteksindəki davamlı sahədir.

## Köhnə PRD-də alınmış qərarlar (saxlanılır)

- **In-app hesab silmə launch-blocker** (Google Play 2024+ tələbi, App Store policy).
- **30 gün soft-delete grace period:** user re-login etsə hesab bərpa olunur; 30 gün sonra hard purge.
- **Cascade purge sırası:** `users → user_profiles → onboarding_state → workouts → workout_sessions → workout_exercises → progress_logs → calorie_logs → Supabase Storage purge (body photos, AI-generated assets)`.
- ~~**Logout sync-queue blocking modal:** local SQLDelight-də pending sync varsa "X əməliyyat sinxronlaşdırılır, gözləyin" modal; force-logout yalnız user explicit confirm ilə.~~ **(LƏĞV — 2026-05-23, design review)**
  - **Yeni qayda — background sync, blocker yox:** Logout/çıxış zamanı pending operations bloklamır. Sync queue avtomatik background-da işləyir:
    - **Android:** `WorkManager` (CoroutineWorker) `OneTimeWorkRequest` + `NetworkType.CONNECTED` constraint + exponential backoff (1/2/4/8/16s, max 5 cəhd). KMP shared sync logic, Android-specific worker.
    - **iOS:** `BGTaskScheduler` (`BGProcessingTaskRequest`) + `URLSession.background` for upload; eyni KMP shared logic.
    - **Common:** `device_id + user_id` idempotency key — duplicate prevented. Pending queue `SQLDelight`-də saxlanır; user yenidən login etsə eyni queue resume olunur.
  - **UX nəticəsi:** user logout-da gözləməyə məcbur deyil. Çıxış dərhal baş verir; sync background-da tamamlanır. Növbəti session-da queue avtomatik flush.
  - **Riskli case (account delete):** delete cascade BAŞQA məsələdir — burda blocker modal mövcuddur ("hesabınız 30 gün ərzində bərpa edilə bilər" + cascade purge progress). Logout ≠ delete.
  - **Design ref:** köhnə screen `app_design.pen` node `y4JLHj` (23 · Çıxış Sync Modal) silindi (2026-05-23).
  - **Implementation owner:** KMP shared `SyncQueueRepository` + platform `WorkManager`/`BGTaskScheduler` adaptors → planlanır `prd-workout-execution` və ya yeni `prd-offline-sync` PRD-də detalize olunsun.
- **Body photo retention:** 365 gün rolling window; istifadəçi əvvəlcədən manual silə bilər.
- **Language override:** device locale-dan müstəqil — user AZ/RU/EN seçə bilər; persisted in `app_settings`.
- **Notification preferences:** workout reminder, streak risk, weekly recap toggle-ları `app_settings`-də.
- **Schema mövcudluğu:** `app_settings` cədvəli (§7.6) və account delete cascade trigger (§7.7) auth PRD-də finalize olunub.
- **30-gün grace UX copy:** "Hesabınız 30 gün ərzində bərpa edilə bilər" — re-login zamanı bərpa modalı göstərilir.

## Açıq suallar / qeyri-müəyyənliklər

- [ ] Profile-edit ekran UI dizayn (Sally) — hansı field-lər inline edit, hansılar wizard?
- [ ] Password change flow: current password + new password + confirmation; email re-verification məcburidirmi?
- [ ] Email change re-verification: yeni email-ə link, köhnə email-ə bildiriş, 24h pending window?
- [ ] 2FA gələcək versiya — TOTP vs SMS (AZ SMS qiyməti yüksək); MVP-də yox amma schema hooks?
- [ ] Subscription state display (settings-də "Premium aktiv · 2026-12-15-də yenilənir" göstərmək) — billing PRD ilə kəsişmə.
- [ ] Data export (GDPR-style) — JSON download MVP-də varmı yoxsa "yaxında" placeholder?
- [ ] Equipment inventory edit (`user_profiles.equipment_inventory`) post-onboarding necə təzələnir?
- [ ] Weight/goal change AI plan-ı re-trigger edirmi (project-context.md §5.2 5 trigger-dən biri)?
- [ ] Theme override (light/dark/system) `app_settings`-də saxlanırmı?
- [ ] Connected accounts (Apple/Google linked) görünüşü və unlink axını.

## Bağlılıqlar

- `prd-auth-onboarding-2026-05-12.md` v3.0 — schema və backend cascade burada finalize olunub.
- `prd-billing-revenuecat` (planlanır) — subscription state və cancel link settings-də göstərilməlidir.
- `prd-workout-execution` (planlanır) — equipment_inventory dəyişikliyi workout filtering-i təsirləyir.
- `prd-paywall-deferred-2026-05-22.md` — restore-purchase link settings-də.

## Suggested next step

Sally (UX) settings surface wireframe-ni hazırladıqdan **sonra** John PRD açır. Trigger: MVP auth/onboarding launch-undan sonra ilk 4 həftəlik user feedback toplandıqda (real pain-point-ləri sıralamaq üçün). Architecture review minimal — schema artıq mövcuddur.
