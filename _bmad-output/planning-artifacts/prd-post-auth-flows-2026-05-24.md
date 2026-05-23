---
project_name: 'fitnessApp'
date: '2026-05-24'
version: '1.0'
status: 'design-aligned · ready-for-architecture'
workflowType: 'prd'
prd_scope: 'post-auth-flows'
authors:
  - 'John (PM) — PRD structure + acceptance criteria'
  - 'Sally (UX) — screen contracts + copy'
  - 'Winston (Architect) — state machines + edge cases'
  - 'Mary (Analyst) — conversion / retention rationale'
supersedes_section_in:
  - 'prd-paywall-deferred-2026-05-22.md (trigger + visual decisions promoted)'
  - 'prd-settings-deferred-2026-05-22.md (logout + account delete UX promoted)'
  - 'prd-professional-coaching-teaser-deferred-2026-05-22.md (waitlist copy contract promoted)'
inputDocuments:
  - docs/project-context.md (v3.3, 2026-05-21)
  - prd-auth-onboarding-2026-05-22.md
  - prd-paywall-deferred-2026-05-22.md
  - prd-settings-deferred-2026-05-22.md
  - prd-professional-coaching-teaser-deferred-2026-05-22.md
  - prd-auth-data-model-2026-05-22.md
designReference: 'app_design.pen → POST-AUTH-FLOW section (5260,10920 → 8760,13000)'
screens:
  - '22 · Paywall (post-dashboard)'
  - '23 · Çıxış / Logout Təsdiq'
  - '24 · Hesab Sil — Təsdiq 1 (Soft-archive xəbərdarlıq)'
  - '25 · Hesab Sil — Təsdiq 2 (Type-to-confirm SİL)'
  - '26 · Pro Coaching — Faza 2 Waitlist'
---

# Post-Auth Flows — PRD v1.0

**Status:** Design-aligned · ready for architecture handoff (Winston).
Bu PRD onboarding + auth tamamlandıqdan sonra istifadəçinin qarşılaşdığı **5 ekranı** vahid səviyyəyə qaldırır: monetizasiya touchpoint (paywall), hesab idarə (çıxış + silmə), və Faza 2 teaser (mütəxəssis uyğunluq yoxlaması waitlist). Onboarding/auth PRD-də scope-dan kənar qalmış 3 deferred stub-ı bu PRD birləşdirir və **MVP launch-blocker** səviyyəsinə qaldırır.

> ⚠️ **Faza 2 yox, MVP məcburi:** Pro Coaching ekranı yalnız demand-signal toplama formudur — coaching feature MVP-də VƏD EDİLMİR (CLAUDE.md qəti qadağa). Lakin ekranın özü P0-dır (settings sürfeysində teaser yeri olmalıdır).

> 🎨 **Vizual kimlik:** Volt renk sistemi (`#E6FF00` accent) · dark theme · Inter font. Tam tokenlər: `docs/project-context.md §1b`. Köhnə turuncu `#FF6B33` qadağa.

---

## 0. Yerləşmə xəritəsi (axın diaqramı)

```
[Dashboard 7-günlük tamamlanır] ───trigger 1───> [22 · Paywall]
                                                     │
                            ┌────────────────────────┤
                            ▼ trial başlayır         ▼ rədd
                       [Premium aktiv]          [Free tier davam]
                            │
                            │
[Settings menyu] ───────────┼─────────────> [23 · Çıxış Təsdiq]
                            │                       │
                            │                       ▼ təsdiq
                            │                  [Local session clear]
                            │                       │
                            │                       ▼
                            │                  [02 · Welcome (re-login)]
                            │
                            └─> [24 · Hesab Sil 1] ──təsdiq──> [25 · Hesab Sil 2]
                                                                     │
                                                                     ▼ type-to-confirm
                                                                [Soft-delete + 30g grace]
                                                                     │
                                                                     ▼
                                                                [02 · Welcome (logged out)]

[Settings → "Yaxında"] ─────> [26 · Pro Coaching Waitlist] ──> [interest_signals insert]
```

**Dizayn referansı:** `app_design.pen` → `POST-AUTH-FLOW · Boundary` (id-lərə görə dizayn faylından oxunur). Section bütün 5 ekranı + xref chip-ləri + flow bridge-i + 3 annotation card-ı + dark-theme title block-u əhatə edir.

---

## 1. Niyə bu PRD?

**3 deferred stub-ı niyə birləşdiririk?**

1. **Trigger zənciri vahiddir:** post-dashboard surface-də user 5 ekrandan biri ilə qarşılaşır (paywall trigger 1 · settings menyu yolu · waitlist link). Ayrı PRD-lər state-machine paylaşımını çətinləşdirir.
2. **Trust moat-ımız:** transparent billing + in-app hesab silmə + AI/coaching honesty bir-birinə bağlıdır. Birində fail → bütün trust differensiatoru çatlayır (Mary, 2026-05-12 research).
3. **MVP launch-blocker konsentrasiyası:** 5 ekrandan 4-ü P0 launch-blocker (24/25 Google Play 2024+ tələbi; 22 monetize-in tək giriş nöqtəsi; 23 hesab idarəetmənin minimal səthi).
4. **Dizayn vəziyyəti:** ekranlar `app_design.pen`-də artıq qurulub və section visual treatment finalize olunub (2026-05-24). PRD dizayn vəziyyətini engineering-ə körpü ilə bağlamalıdır.

---

## 2. Scope

### In-scope (bu PRD-də həll olunur)
- **Trigger logic:** hər ekranın hansı user action-dan açılması, hansı state-də göstərilməsi.
- **State machines:** 5 ekran üçün enumeration-safe state diaqramları + error halları.
- **Copy contracts:** AZ/RU/EN tam string siyahısı (manual review tələb edən — MT qadağa).
- **Edge cases:** offline davranış, network failure, double-tap, idempotency.
- **Acceptance criteria:** UX testləri + analytics events.
- **Edge state variants:** rate-limited delete attempt, sync-pending logout, waitlist duplicate email.

### Out-of-scope (digər PRD-lərə defer)
- **RevenueCat inteqrasiyası, IAP webhook, restore-purchase axını** → `prd-billing-revenuecat` (planlanır).
- **Local payment provider (m10/Pulpal/UnipayGO) seçimi** → `prd-billing-local-payment-feasibility` (Mary research davam edir).
- **Background sync queue implementation (WorkManager/BGTaskScheduler)** → `prd-offline-sync` (planlanır).
- **Full settings surface (profile edit, notification prefs, theme override)** → `prd-settings-full-surface` (Faza 1.5).
- **Faza 2 trainer review loop (managed model, freelance contract, AZ regulation)** → `prd-trainer-review-loop-phase2`.

---

## 3. Ekran Müqavilələri

### 3.1 Screen 22 · Paywall (post-dashboard)

**Trigger:** İstifadəçi onboarding + auth + ilk 7-günlük məşq planını tamamladıqdan sonra dashboard-da `post-dashboard sürüşməsi` event-i atılır (auth-onboarding PRD §3.10).

**Goal:** Trial başlat (default) və ya illik plan al — free user 7 gün sonra paywall-ə qayıdır.

**State machine:**
```
INITIAL → SELECTING (trial | illik)
SELECTING → SUBMITTING (RevenueCat IAP)
SUBMITTING → SUCCESS (Premium aktiv)
         → CANCELLED (back to free)
         → ERROR_NETWORK (offline banner + retry)
         → ERROR_IAP (Apple/Google error code)
         → ERROR_RATE_LIMIT (5 attempt/saat → modal)
```

**Components:**
- Status bar (comp/statusbar `Y01Sc`)
- Close button (X) — top-right (36×36 surface circle)
- "PREMIUM" eyebrow chip (accent text)
- Title (28px 800): "Daha dərinə getməyə hazırsan?"
- 5 benefit row: 100+ elmli hərəkət, limitsiz AI plan + haftəlik adaptasiya, mütəxəssis uyğunluq yoxlaması, limitsiz foto-kalori, Streak Freeze + Ramazan
- Iki seçim card:
  - "7 gün pulsuz sınaq" — surface bg, accent stroke 2px
  - "İllik plan — 60 AZN" — accent fill, on-accent text, DEFAULT seçili
- Info-notice (comp/info-notice `wN31r`): "Şəffaf ödəniş — tam taşla ləğv et, gizli ödəniş yoxdur."
- AI disclosure mətn: "AI tərəfindən qurulan · Avtomatik yenilənmə şərtləri açıqdır" (Apple 2025 tələbi)
- Primary CTA (comp/btn-primary `nO5Fr`): "Davam et"
- Footer link row: "Bərpa et · Şərtlər" + ödəniş methodları (Apple Pay · Google Pay · m10 / Pulpal)

**Copy invariants:**
- "7 gün sonra avtomatik 60 AZN/il" — fine-print qadağa, açıq mətn (CLAUDE.md transparent billing).
- "Bu plan AI tərəfindən yaradılır" — Apple 2025 onboarding/paywall tələbi.
- Promise-tone: "Daha dərinə getməyə hazırsan?" — pressure-tone "Bu son şansın!" qadağa.

**Acceptance criteria:**
- ✅ İki seçim həmişə görünür (tək seçim QADAĞA, CLAUDE.md).
- ✅ Default seçim "İllik plan" (yanvar push absolute, sektörel 17% retention).
- ✅ Video arka plan 2.9x conversion (low-bandwidth fallback statik image).
- ✅ Close button həmişə əlçatan; "Davam et" başqa CTA-larla pair olmur.
- ✅ Offline → V4 offline banner + "Davam et" disabled.
- ✅ Rate-limit (5 submit/saat) → V3 modal (auth flow pattern reuse).

**Analytics events:**
- `paywall_shown` (trigger_id, free_tier_state)
- `paywall_option_selected` (trial | annual | lifetime)
- `paywall_cta_tapped` (selection)
- `paywall_iap_success` (revenue, currency, plan)
- `paywall_dismissed` (method: close | back | system)

**Edge cases:**
- User Apple ID dəyişib (restore-purchase) → settings restore link aktiv.
- m10/Pulpal seçilirsə → RevenueCat-bypass axını (Faza 1 araşdırma).
- User pregnancy_postpartum=true → premium AI plan generasiyası bloklanır, paywall yenə göstərilir (premium feature: foto-kalori, content depth).
- 13-17 yaşda user → V5 soft-warning (parental gate auth flow pattern), paywall yenə göstərilə bilər lakin trial CTA disabled (parental).

---

### 3.2 Screen 23 · Çıxış / Logout Təsdiq

**Trigger:** Settings → "Çıxış" tapildığında bu confirm-modal-style ekran açılır.

**Goal:** İstifadəçi hesabdan çıxır; offline-logged data background-da sync olunur; cihazda local session təmizlənir.

**State machine:**
```
INITIAL → CONFIRMING → CONFIRMED → CLEAR_SESSION → NAV_WELCOME
        → CANCELLED  → CONFIRMING (close modal)
```

> 📌 **Kritik:** Logout `pending sync queue`-u BLOKLAMIR (2026-05-23 design review). Sync background-da WorkManager (Android) / BGTaskScheduler (iOS) ilə davam edir. Köhnə "23 · Çıxış Sync Modal" silindi.

**Components:**
- Status bar (comp/statusbar `Y01Sc`)
- Back chevron (36×36 surface circle, top-left)
- Hero icon: 56×56 accent-soft circle, lucide `log-out` icon, accent fill
- Title (28px 800): "Çıxışa hazırsan?"
- Subtitle: "Profilin və 30 günlük offline məlumatın bulutda qalır. Növbəti girişdə avtomatik geri qayıdacaq."
- Info list (surface card, $border 1px):
  - cloud-upload · "Bulud yedəyi: aktiv"
  - wifi-off · "Offline məlumatın 30 gün gözləyir"
  - clock-3 · "Növbəti girişdə avtomatik sync"
- Info-notice: "Cihazdan tam silinsən belə, profil və premium status saxlanılır."
- Primary destructive button (comp/btn-destructive `Q6PdUr`): "Çıxış et"
- Secondary button (comp/btn-secondary `fpMvc`): "İmtina"

**Copy invariants:**
- "Çıxış" ≠ "Hesab silmə" — copy hər iki halda fərqi göstərməlidir.
- Promise-tone: "30 günlük offline məlumatın bulutda qalır" — kritik trust signal.

**Acceptance criteria:**
- ✅ "Çıxış et" CTA → local SQLDelight session clear + Supabase auth.signOut() + nav-to-welcome.
- ✅ Pending sync queue `device_id + user_id` ilə Supabase-ə yazılır; user yenidən login etsə eyni queue resume olunur.
- ✅ Offline halda çıxış mümkündür (queue local-də qalır, online olduqda sync).
- ✅ "İmtina" → modal bağlanır, settings-ə qayıdır.

**Analytics events:**
- `logout_shown`
- `logout_confirmed` (sync_queue_pending_count)
- `logout_cancelled`

**Edge cases:**
- Pending sync count > 0 → user-i bloklamır, lakin alt mətn 1 saniyə daha uzun göstərilir ("X əməliyyat background-da sinxronlaşır").
- Şəbəkə yoxdur + Supabase.auth.signOut() fail → local clear baş verir, server-side cleanup növbəti session-da.
- Premium user → premium status `app_settings`-də qalır, restore-purchase növbəti login-də avtomatik.

---

### 3.3 Screen 24 · Hesab Sil — Təsdiq 1 (Soft-archive xəbərdarlıq)

**Trigger:** Settings → "Hesabı sil" → bu xəbərdarlıq ekranı.

**Goal:** İstifadəçiyə 30-gün soft-delete grace mexanizmini izah et, nəyin silinəcəyini açıqla, geri çəkilmə (cancel) imkanı saxla.

**State machine:**
```
INITIAL → READING → PROCEED (→ Screen 25)
                  → CANCELLED
```

**Components:**
- Status bar + back chevron header
- Warning hero: triangle-alert icon ($danger), surface-2 background
- Title (24px 800): "Hesabı silməyə hazırsanmı?"
- Description: "Aşağıdakı məlumatlar 30 gün ərzində soft-archive olunur. Bu müddətdə geri bərpa olunur, hər şey bərpa olunur."
- Data-loss list (4 row): trophy/dumbbell/camera/user — "100+ məşq və set tarixçən · Ölçü və çəki dinamikası · İrəliləyiş fotoları · Profil və preferenslər"
- Info-notice (moss border): "30 gün ərzində geri qayıtsan — bütün məlumatın bərpa olunur. 30 gündən sonra tamamilə silinir."
- Secondary button: "İmtina et" (large)
- Destructive button: "Davam et" → Screen 25

**Copy invariants:**
- "Soft-archive" sözü Azerbaijan-da xidmət sözü deyil — "geri qayıtmaq mümkündür" şəklində izah olunmalıdır.
- 30 gün grace period explicit göstərilməlidir.

**Acceptance criteria:**
- ✅ "Davam et" → Screen 25-ə nav.
- ✅ "İmtina et" → settings-ə qayıdır.
- ✅ Back chevron tap → settings-ə qayıdır (24-25 keçidində geri sürüşmə də cancel deməkdir).

---

### 3.4 Screen 25 · Hesab Sil — Təsdiq 2 (Type-to-confirm SİL)

**Trigger:** Screen 24 → "Davam et".

**Goal:** İstifadəçi `SİL` sözünü böyük hərflərlə daxil edənə qədər destructive CTA disabled olur. Bu, qəza tap-ı və yanlış silmə riskini azaldır (CLAUDE.md account delete cascade qaydası).

**State machine:**
```
INITIAL → INPUT_EMPTY (CTA disabled)
INPUT_EMPTY → INPUT_INVALID (CTA disabled, redaktor border $danger)
INPUT_INVALID → INPUT_VALID (CTA enabled)
INPUT_VALID → SUBMITTING → SUCCESS (cascade purge start)
                        → ERROR_NETWORK (retry)
                        → ERROR_RATE_LIMIT
```

**Components:**
- Status bar + back chevron + danger-soft bg card
- Trash icon: 56×56 danger-soft circle, lucide `trash-2`, danger fill
- Title (24px 800): "Son təsdiq"
- Description: "Aşağıdakı sahəya SİL yaz ki, hesabı qəti silə bilək."
- Label: "Təsdiq sözü"
- Textfield (comp/textfield `j5YV0`):
  - Placeholder: "SİL"
  - Validation: case-sensitive, exact match "SİL" (3 hərf, böyük), letter-spacing 4px display.
  - Invalid state: border $danger, helper text: "Dəqiq 1 böyük hərflə (latin) yazılmalıdır."
- Info-notice: "30 gün ərzində geri qaytsan, məlumat bərpa olunur."
- Secondary button: "İmtina et"
- Destructive button (large): "Hesabı sil" — disabled until VALID

**Copy invariants:**
- Type-to-confirm sözü "SİL" (3 hərf, böyük, latin) — lokal valoda hərfləri qarışdırmayacaq qədər qısa.
- "Qəti" sözü iki dəfə təkrarlanmamalıdır (Sally feedback: pressure tone azalt).

**Acceptance criteria:**
- ✅ "Hesabı sil" CTA yalnız input "SİL" exact match olduqda enabled.
- ✅ Submit → soft-delete flag aktiv (`users.deleted_at = NOW()`).
- ✅ 30 gün cron (Supabase Edge Function scheduled) → hard-purge cascade:
  `users → user_profiles → onboarding_state → workouts → workout_sessions → workout_exercises → progress_logs → calorie_logs → Storage purge (body photos, AI-generated assets)`.
- ✅ Premium subscription → RevenueCat cancellation (best-effort, async).
- ✅ Re-login 30 gün ərzində → bərpa modalı (təsdiq → soft-delete flag clear).
- ✅ Re-login 30 gündən sonra → "Hesab tapılmadı" (yeni signup tələb).

**Analytics events:**
- `delete_intent_shown` (screen 24)
- `delete_confirm_shown` (screen 25)
- `delete_input_valid`
- `delete_submitted`
- `delete_restored` (re-login during grace)
- `delete_finalized` (cron purge)

**Edge cases:**
- User pending premium subscription → RevenueCat cancel async; user-ə bildiriş: "Premium 30 gündən sonra ləğv olunur."
- Local SQLDelight cache → logout-da clear (data buludda soft-archive-də qalır).
- Cascade failure mid-purge → idempotent retry; partial-delete state-i log → admin manual review.
- pregnancy_postpartum + active plan → cascade-də heç bir fərq yox (medical disclaimer-lər anonymized telemetry-də qalır).

---

### 3.5 Screen 26 · Pro Coaching — Faza 2 Waitlist

**Trigger:** Settings → "Mütəxəssis uyğunluq yoxlaması" link (settings sürfeysində teaser).

**Goal:** Faza 2 demand-signal toplama — sıfır mühəndislik, statik form. **MVP-də coaching feature VƏD EDİLMİR.**

**State machine:**
```
INITIAL → INPUT_EMPTY (CTA disabled)
INPUT_EMPTY → INPUT_VALID (CTA enabled)
INPUT_VALID → SUBMITTING → SUCCESS (təşəkkür ekranı)
                        → ERROR_NETWORK
                        → ERROR_DUPLICATE (email artıq mövcuddur)
```

**Components:**
- Status bar + back chevron
- "YAXINDA" eyebrow chip ($accent-soft / $accent border)
- Hero icon: 56×56 surface-2 circle, lucide `graduation-cap`, accent fill
- Title (24px 800): "Mütəxəssis uyğunluq yoxlaması"
- Description: "AI plan göndərilmədən əvvəl mütəxəssis tərəfindən təhlükəsizlik və uyğunluq baxımından açılır. **Faza 2-də açılır.**"
- Bullet list (4 row): "Plan strukturu və haftəlik yük balansı · Hərəkət təhlükəsizliyi və forma riski · Şikayət, yaralanma və xəstəlik uyğunluğu · 1-2 əl-seçimli mütəxəssis · 2026 4-cü rüb"
- Label: "E-poçt"
- Textfield (comp/textfield `j5YV0`):
  - Placeholder: "balaagha@nümunə.az"
  - Validation: standard email regex.
- Primary CTA (comp/btn-primary `nO5Fr`): "Məni xəbərdar et"
- Footer note: "E-poçtun yalnız bu məqsədlə saxlanılır."

**Copy invariants (hüquqi və ton):**
- "Trainer", "məşqçi", "coach", "personal coach" sözləri **QADAĞA** (CLAUDE.md). Düzgün termin: **"mütəxəssis uyğunluq yoxlaması"** və ya "uyğunluq yoxlaması".
- "Approval", "təsdiq" sözləri hüquqi mətndə qadağa — "uyğunluq yoxlaması" (safety-fit review) məcburi (mesleki sorumluluk dilini azaldır).
- Promise tone QADAĞA: "Faza 2-də açılır" — signal tone OK, "yaxında istifadəçi alarsan" promise YOX.

**Acceptance criteria:**
- ✅ Form submit → `interest_signals` cədvəlinə insert (email, locale, created_at, source='pro_coaching_teaser').
- ✅ Duplicate email → "Sən artıq bu siyahıdasan" mesajı (gizli, server-side check; enumeration-safe).
- ✅ Submit → təşəkkür ekranı + "Geri qayıt" button → settings.
- ✅ Form analytics → `coaching_interest_submitted` (locale).

**Analytics events:**
- `coaching_teaser_shown`
- `coaching_email_submitted`
- `coaching_form_dismissed`

---

## 4. State Machine Konsolidasyonu

| Ekran | States | Critical Edge Variants |
|-------|--------|------------------------|
| 22 Paywall | 7 (INITIAL · SELECTING · SUBMITTING · SUCCESS · CANCELLED · ERROR_NETWORK · ERROR_IAP · ERROR_RATE_LIMIT) | V4 offline banner · V3 rate-limit modal · pregnancy_postpartum AI plan blocked |
| 23 Logout | 5 (INITIAL · CONFIRMING · CONFIRMED · CLEAR_SESSION · NAV_WELCOME) | Pending sync no-block · offline graceful · premium status preserved |
| 24 Delete-1 | 3 (INITIAL · READING · PROCEED/CANCELLED) | Back chevron = cancel |
| 25 Delete-2 | 7 (INPUT_EMPTY · INPUT_INVALID · INPUT_VALID · SUBMITTING · SUCCESS · ERROR_NETWORK · ERROR_RATE_LIMIT) | Cascade purge async · soft-delete grace · RevenueCat cancel |
| 26 Pro Coaching | 5 (INPUT_EMPTY · INPUT_VALID · SUBMITTING · SUCCESS · ERROR_DUPLICATE/NETWORK) | Email enumeration-safe |

**Cross-screen invariants:**
- Bütün error halları **enumeration-safe** (auth flow pattern reuse — "düzgün e-poçt + şifrə kombinasiyası tapılmadı" generic).
- Offline davranış hər ekranda dəstəklənir (V4 banner pattern).
- Back chevron = ya cancel, ya da prev step — heç vaxt "submit and back".

---

## 5. Analytics & Funnel

**Funnel 1 — Paywall conversion:**
```
paywall_shown → paywall_option_selected → paywall_cta_tapped → paywall_iap_success
                                                              → paywall_iap_error
```

**Funnel 2 — Account lifecycle:**
```
logout_shown → logout_confirmed (sync_queue_count)
delete_intent_shown → delete_confirm_shown → delete_input_valid → delete_submitted
                                                                → delete_restored (within grace)
                                                                → delete_finalized (after grace)
```

**Funnel 3 — Faza 2 demand:**
```
coaching_teaser_shown → coaching_email_submitted (locale)
```

**Mary research thresholds:**
- Paywall trial start rate ≥ 30% (sektörel: 25-35%).
- Trial → paid conversion ≥ 40% (BetterMe ~50%).
- Delete intent → confirm rate ≤ 15% (yüksək rate = problem signal).
- Coaching demand: 3-ay-da ≥ 500 email submission = Faza 2 yaşıl işıq.

---

## 6. Hüquqi və compliance

- **Apple 2025 AI disclosure:** Paywall + onboarding-da "Bu plan AI tərəfindən yaradılır" mətni məcburi.
- **Google Play 2024+ in-app account deletion:** Screen 24-25 launch-blocker.
- **GDPR-style data export (defer):** Settings → "Məlumatlarımı yüklə" link → `prd-data-export-deferred`.
- **Coaching marketing qadağası:** App Store description, landing page, paywall-da "trainer/coaching" sözü QADAĞA (CLAUDE.md trust moat).
- **Transparent billing:** in-app cancel link paywall-da görünməlidir (RevenueCat → Apple/Google subscription management).
- **30-gün soft-delete grace UX copy:** AZ/RU/EN-də tam manual review (MT qadağa).

---

## 7. Açıq suallar (Architecture review-da Winston həll edir)

- [ ] **Pending sync queue idempotency key seçimi:** `device_id + user_id` kifayətdirmi yoxsa `request_uuid` lazımdırmı?
- [ ] **RevenueCat webhook delete cascade timing:** premium-li hesab silindikdə subscription dərhal cancel olunurmu yoxsa 30g grace-da qalır?
- [ ] **Paywall A/B variant:** "İllik 60 AZN" vs "5 AZN/ay (illik faturalanır)" — Mary thresholds.
- [ ] **Local payment paywall axını:** m10/Pulpal seçildikdə paywall UI dəyişirmi (RevenueCat bypass)?
- [ ] **Restore-purchase paywall-da görünür mü:** secondary footer link, yoxsa settings-də ayrıca?
- [ ] **Delete cascade partial-failure rollback strategy:** idempotent retry vs admin manual review thresholds.
- [ ] **Pro Coaching teaser yerləşməsi:** settings-də ayrıca tab, paywall-dan sonra cross-sell, ya da bottom-nav badge?
- [ ] **Pregnancy_postpartum + paywall:** AI plan generasiyası bloklandığı user-ə paywall qiyməti dəyişirmi (content depth premium gate-i qalır)?

---

## 8. Dependencies

- ✅ **`prd-auth-onboarding-2026-05-22.md` v3.0** — schema (`users`, `user_profiles`, `app_settings`, `onboarding_state`) finalize olunub.
- ⏳ **`prd-billing-revenuecat`** — paywall IAP webhook + restore-purchase + subscription state.
- ⏳ **`prd-offline-sync`** — WorkManager/BGTaskScheduler axını + retry.
- ⏳ **`prd-settings-full-surface`** — tam settings UI (profile edit, notifications, theme).
- 🔵 **`prd-trainer-review-loop-phase2`** — Faza 2, demand-signal threshold sonra açılır.
- ✅ **Design:** `app_design.pen` POST-AUTH-FLOW section finalize (2026-05-24).

---

## 9. Acceptance summary

| Ekran | P0 | Launch blocker? | Design hazır? | Engineering hazır? |
|-------|----|-----------------|--------------|---------------------|
| 22 Paywall | ✅ | ✅ (monetize giriş nöqtəsi) | ✅ | ⏳ (RevenueCat PRD gözləyir) |
| 23 Logout | ✅ | ✅ (account mgmt minimal) | ✅ (bu sprint) | ⏳ (offline-sync PRD ilə) |
| 24 Delete-1 | ✅ | ✅ (Google Play 2024+) | ✅ | ⏳ (cascade Edge Function) |
| 25 Delete-2 | ✅ | ✅ (Google Play 2024+) | ✅ | ⏳ (cascade Edge Function) |
| 26 Pro Coaching | ✅ | ❌ (P0 sürfeys olaraq, lakin trigger Faza 2-də) | ✅ | ⏳ (interest_signals table + form) |

---

## 10. Suggested next step

**Winston (Architect)** bu PRD-ni `prd-billing-revenuecat` + `prd-offline-sync` + delete cascade Edge Function arxitekturası ilə uyğunlaşdırır → **Amelia (Dev)** screen-by-screen story-ları yaradır:

```
/bmad-create-story "22 Paywall — RevenueCat trial flow"
/bmad-create-story "23 Logout — local session clear + bg sync"
/bmad-create-story "24-25 Account delete — type-to-confirm + cascade"
/bmad-create-story "26 Pro Coaching — interest_signals form"
```

**Mary (Analyst)** funnel thresholds-i validate edir, post-launch 3-ay window üçün dashboard tələbləri qurur.

**Murat (TEA — Test Architect)** screen-by-screen acceptance tests + edge case matrix-i yaradır (offline · rate-limit · cascade partial-failure · email enumeration-safe).

---

**Revision history:**
- v1.0 (2026-05-24) — Initial PRD. 3 deferred stub-ı birləşdirir, dizayn vəziyyəti ilə uyğun. Section visual treatment `app_design.pen` POST-AUTH-FLOW Boundary-də finalize.
