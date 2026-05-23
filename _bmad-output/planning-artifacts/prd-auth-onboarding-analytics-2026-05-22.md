---
project_name: 'fitnessApp'
user_name: 'Balaagha'
date: '2026-05-22'
version: '1.0'
workflowType: 'analytics-spec'
parent_prd: 'prd-auth-onboarding-2026-05-22.md'
phase: 'MVP / Faza 1'
relatedPRDs:
  - prd-auth-onboarding-2026-05-22 (parent — funksional kontrakt)
  - prd-auth-data-model-2026-05-22 (data model split)
  - ux-auth-onboarding-2026-05-22 (UI handoff split)
hardConstraintsRef: 'CLAUDE.md → Qəti Qadağalar + docs/project-context.md §11'
---

# Analytics Spec — Auth & Onboarding Event Catalog + Success Metrics

**Author:** Balaagha · **PM Agent:** John · **Date:** 2026-05-22 (v1.0)
**Parent:** `prd-auth-onboarding-2026-05-22.md` (v3.1)
**Scope:** Event catalog (property names, stages) · North Star + funnel hədəfləri · Quality metrikləri · Compliance KPI.

Bu sənəd parent PRD-nin **§11 Analytics Event Catalog** və **§12 Success Metrics** bölmələrinin tam köçürülmüş versiyasıdır.

---

## 1. Analytics Event Catalog

Property naming: `snake_case`. Scope: yalnız **auth + age_gate + onboarding-skeleton** event-ləri. `sample_workout_*`, `paywall_*`, `pregnancy_nudge_*`, `pro_coaching_teaser_*` event-ləri ayrı PRD-lərdə.

| Event | Stage | Properties |
|-------|-------|------------|
| `app_launched` | bootstrap | `{cold_start, app_version}` |
| `language_selected` | Q0 | `{language, system_locale_match}` |
| `welcome_viewed` | welcome | `{}` |
| `privacy_link_tapped` | welcome | `{which: privacy\|terms\|health}` |
| `onboarding_started` | Q1 | `{timestamp}` |
| `onboarding_q{N}_viewed` | Q1-Q7 | `{question_index}` |
| `onboarding_q{N}_answered` | Q1-Q7 | `{question_index, value or bucket}` |
| `age_gate_blocked` | Q3 | `{trigger: under_13, attempt}` |
| `age_gate_retry` | Q3 | `{email_hash_collision: bool}` |
| `parental_notice_viewed` | Q3 (13-17) | `{}` |
| `parental_notice_accepted` | Q3 (13-17) | `{duration_ms}` |
| `ai_disclosure_viewed` | post-Q7 | `{}` |
| `ai_disclosure_accepted` | post-Q7 | `{duration_ms}` |
| `auth_gate_viewed` | post-disclosure | `{platform}` |
| `auth_provider_selected` | auth | `{method}` |
| `auth_provider_cancelled` | auth | `{method}` |
| `auth_signup_started` | auth | `{method}` |
| `auth_signup_completed` | auth | `{method, duration_ms, is_new_user}` |
| `auth_login_attempt` | auth | `{method}` |
| `auth_login_success` | auth | `{method, is_new_user: false}` |
| `auth_signup_error` | auth | `{code}` |
| `auth_login_error` | auth | `{code}` |
| `auth_apple_relay_used` | auth | `{yes\|no}` |
| `password_reset_requested` | reset | `{}` |
| `password_reset_completed` | reset | `{duration_min}` |
| `profile_sync_started` | post-auth | `{}` |
| `profile_sync_success` | post-auth | `{duration_ms}` |
| `profile_sync_failed` | post-auth | `{reason, retry_count}` |
| `onboarding_completed` | Q7 submit | `{duration_seconds, persona_cell}` |
| `onboarding_abandoned` | any | `{at_step}` |
| `onboarding_resume_shown` | cold-start | `{at_step}` |
| `onboarding_resume_accepted` | cold-start | `{at_step}` |
| `onboarding_resume_expired` | cold-start | `{}` |
| `logout_initiated` | post-auth | `{}` |
| `logout_completed` | post-auth | `{}` |
| `account_delete_initiated` | settings | `{}` |
| `account_delete_confirmed` | settings | `{}` |
| `delete_aborted_reopen` | re-login | `{days_since_delete}` |
| `account_hard_purged` | cron | `{user_id_hash}` |
| `missing_strings` | runtime | `{key, language_requested, fallback_used}` |

---

## 2. Success Metrics

### 2.1 North Star (bu PRD-yə uyğun)
**Onboarding completion rate ≥ 65%** — `onboarding_started` → `onboarding_completed` (Q7 submit) + auth_success.

### 2.2 Funnel

| Step | Hədəf |
|------|-------|
| install → `language_selected` | ≥98% |
| `language_selected` → `onboarding_started` | ≥95% |
| Q1 → Q7 answered | ≥65% |
| Age Gate (Q3) keçid (≥18 cohort) | ≥90% |
| Q3 (13-17) parental consent | ≥85% |
| Q7 → `ai_disclosure_accepted` | ≥98% |
| `ai_disclosure` → `auth_gate_viewed` | ≥99% |
| AuthGate → auth_success | ≥70% |
| auth_success → `profile_sync_success` | ≥99% |
| Onboarding median müddət | **≤90 sn** |

### 2.3 Quality
- Onboarding crash-free **≥99.5%**
- Auth API error rate **<2%** (network exclude)
- Password reset delivery **≥98%**
- Sync to profile success **≥99%**

### 2.4 Compliance
- App Store / Play Store rədd **0**
- AZ string quality review **≥90% native**

### 2.5 Privacy NFR — telemetry-ə təsir (parent §10.6)
- **NFR-PR1:** Onboarding analytics PII-siz — yaş `bucket` (13-17, 18-24, 25-34, 35-49, 50+), çəki/boy bucket; raw qiymət göndərilmir
- **NFR-PR3:** Apple ATT prompt MVP-də skip — anonymous device_id default

---

**End of Analytics Spec v1.0**
