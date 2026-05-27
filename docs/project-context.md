---
project_name: 'fitnessApp'
user_name: 'Balaagha'
date: '2026-05-22'
version: 3.4
supersedes: '2026-05-12 (v1) · 2026-05-16 (v2 · v3) · 2026-05-17 (v3.1) · 2026-05-21 (v3.2) · 2026-05-21 (v3.3)'
companion_doc: '/CLAUDE.md'
---

# fitnessApp — Agent Constitution

> Bu sənəd `/CLAUDE.md` ilə birgə oxunur. **Tech stack · hard constraints · naming · data model SQL · monetization qiymət · competitive context · out-of-scope** — `CLAUDE.md`-dədir, burada **TƏKRARLANMIR**. Bu fayl: product UX müqaviləsi · auth & plan kontraktları · persona davranış matrisi · edge-case qaydaları.
>
> **v3.1 patch (2026-05-17):** §3.6 age gate · §3.7 localization fallback · §5.6 AI prompt blueprint · §5.7 streak + modifier lifecycle · §10.6 data loss + PII · 6 mechanical fix · cycle_tracking → Faza 2 deferred. Tetik: 2 paralel review (validator + drafter).
>
> **v3.2 patch (2026-05-21):** §0 Positioning əlavə olundu — AI dəstək qatı, headline deyil; repositioning "uçtan-uca məşq idarəetməsi". Trainer review loop + professional-direct → Faza 2. Super-set MVP-də. Tetik: BMad party-mode müzakirəsi (Mary/John/Winston).
>
> **v3.3 patch (2026-05-21):** §1b Vizual Kimlik — Renk Sistemi (**Volt**) əlavə olundu — kanonik renk müqaviləsi; bütün flow-lar (UX spec, `.pen`, gələcək PRD-lər) buradan oxuyur. Köhnə turuncu accent (`#FF6B33`) ləğv edildi → Volt sarısı (`#E6FF00`, Ladder-ilhamlı). Tetik: owner design qərarı + BMad (Sally/Winston).
>
> **v3.4 patch (2026-05-22):** §1c Minimal Documentation Principle əlavə + §12 Planlama Sənədləri İndeksi əlavə + köhnə sənəd referansları yeniləndi. Hər sənəd = bir UI/feature müqaviləsi; mega-PRD qadağan. Tetik: owner qərarı + sənəd qrafının böyüməsi (auth + onboarding scope split).

---

## 0. Məhsul Mövqeləndirməsi (Positioning) — məcburi

fitnessApp **"AI plan üreten uygulama" DEYİL.** Əsas vəd: istifadəçi öz məşq prosesini **uçtan-uca** buradan idarə edir — planla, izlə, ölç. AI bir **dəstək qatıdır** (plan təklifi üçün), headline deyil. Rəqabət üstünlüyü: AZ-da Strong/Hevy yox + native AZ + yerli content + transparent billing.

**UX framing qaydası (məcburi — hər ekran bundan keçir):** AI duygusal kahraman edilmir; kahraman istifadəçidir.

| Element | ❌ Yanlış (AI-kahraman) | ✅ Doğru (user-kahraman) |
|---------|------------------------|--------------------------|
| Welcome copy | "Sənə uyğun AI fitness planı" | "Öz məşqini idarə et" |
| Profil önizləməsi | "AI sənin üçün hesabladı" + sayğac animasiyası | "Səni belə tanıdıq — düzdürmü?" (cavab özeti) |
| AI disclosure (Apple 2025 məcburi) | Ayrı duygusal kahraman ekran | Plan-hazır ekranında sakin, görünür bilgi satırı; ton şəffaflıq, "AI sehri" yox |
| Sample workout | Canlı AI çıxışı vurğusu | goal+context-seçimli curated template (runtime AI maliyəti 0) |
| Son ekran | "Planın hazırdır — AI tərəfindən yaradılıb" | "Birinci məşqin hazırdır. Başlayaq?" |

**Scope qərarı (BMad party-mode, 2026-05-21):**
- **MVP (Faza 1):** logger + management + cue/tüyo + **super-set & ileri set-tipləri** + kalori/protein + AI plan təklifi + manual builder + lokal bildiriş.
- **Premium plan yaratma zənciri (MVP):** AI plan generation → **human approval gate (premium only — mütəxəssis uyğunluq yoxlaması)** → user delivery. Free plan: yalnız AI plan (human gate yox). Bu gate **trainer feature DEYİL** — mütəxəssis fərdi məşqçi rolunda çıxış etmir, plan üzərindən chat / fərdi koreksiya / münasibət yoxdur; yalnız təhlükəsizlik/uyğunluq yoxlaması (1-2 əl-seçimli AZ mütəxəssis, queue-based managed model). Copy-də termin: "mütəxəssis yoxlaması" / "uyğunluq yoxlaması" — "məşqçi/coach/trainer" sözü qadağan.
- **Faza 2:** trainer review loop (AI → user edit → gerçek trainer fərdi koreksiya, açıq marketplace yox), professional-direct coaching (AI-suz, birbaşa trainer ilə fərdi münasibət), server-tetikli push. MVP-də yalnız "professional coaching yaxında" ekranı (e-mail toplama).
- **Onboarding dəyişmir:** 7 məcburi sual / ≤90 sn kutsal. Sakatlık → opsiyonel progressive profiling (§3.2), zorunlu sual deyil. "Program nasıl oluşturulsun" path seçimi onboarding-ə GİRMİR — sonrakı ekranda kart siyahısı (Faza 2-də 3-cü kart).
- **Data model gələcəyə hazırlanır:** trainer-loop üçün kolonlar rezerv (`workouts.status`, `reviewed_by`, `source`; `users.role`; ayrı `health_constraints`) — axın/RLS kodu YAZILMIR, detal architecture phase.
- **Pozisyon ikiyə ayrılır:** App Store təsviri = MVP cümləsi; pitch/yatırımcı = tam vizyon. Vəd-teslimat boşluğu YASAQ.

---

## 1. Minimal Design Principle (məcburi)

Hər ekran yalnız **indi gərək olan** sahəni göstərir; dərinlik istəyə əsasən açılır.

- Default state: ≤7 element / ekran; genişləndirmə "Daha çox" və ya tap-to-expand ilə
- **Tradeoff qaydası:** Yeni feature girəcəksə, mövcud element **çıxarılmalı və ya gizlədilməli** — sadəcə əlavə etmə YASAQ
- Anti-paternlər: tab+accordion+drawer eyni ekranda · >2 primary CTA · >2 səviyyəli nav · modal içində modal

## 1b. Vizual Kimlik — Renk Sistemi (Volt) — məcburi

> **Kanonik renk müqaviləsi.** Bütün ekranlar, komponentlər, `.pen` design faylı, UX spec §3.1 və gələcək bütün PRD/flow-lar **bu cədvəldən** oxuyur — renk dəyəri başqa heç bir yerdə təyin edilmir. Token-lər **semantic** adlanır (rol, ham renk deyil). Tema: **dark-first** (default), light gələcək tema.

**Fəlsəfə.** Sistem enerjik, motivasiyaverici, minimalist. İlham mənbəyi: Ladder (joinladder.com) — koyu taban + tək güclü elektrik-sarı accent. Sarı (`volt`) **daşıyıcı rengdir** — bol və cəsarətli işlədilir (rasionlanmır): splash, CTA, seçili durum, progress, logo, vurğu. Yaşıl (`success`) yardımçı/chip-lərdə işlədilir, accent deyil. Yaşıl-zeytun (`moss`) yalnız dekorativ.

**Sarı işlətmə doktrinası (məcburi):**
- **Splash ekran tam sarı** — `volt` zəmin (`#E6FF00`), `on-volt` logo. Ladder-paterni; marka anı.
- Sarı `bg` üzərində bütün primary CTA, seçili checkbox/radio dolğusu, progress dolu segment, aktif gün/state, link metni, milestone/streak/kutlama anları, ikon vurğuları.
- "10% accent" qaydası **YOXDUR** — sarı flow boyu davamlı və görünür işlədilir; lakin oxunan koyu ekranlarda **taban koyu qalır** (sarı flood yalnız splash + kutlama/celebration + hero anları).
- `volt` zəmin üzərində metin **HƏMİŞƏ** `on-volt` (`#0E0E0E` — qara), ağ deyil. Kiçik gövdə metni sarı üzərində yazılmır.

### Renk token cədvəli (semantic — dark default / light gələcək)

| Token | Dark (default) | Light (gələcək) | İşlədilir |
|-------|----------------|-----------------|-----------|
| `bg` | `#0A0A0B` | `#FFFFFF` | Ekran zəmini (saf qara deyil — OLED halo azaltma) |
| `surface` | `#141416` | `#F4F5F7` | Kart, input, bottom sheet, pasif segment |
| `surface-elevated` | `#1E1E21` | `#FFFFFF` | Modal, "bugünkü məşq" tile, timer kartı — yüksəldilmiş katman |
| `border` | `#2A2A2E` | `#E5E7EB` | Kart kənarı, ayraç, progress boş segment |
| `border-strong` | `#3E3E44` | `#CBD0D8` | Seçilməmiş checkbox/radio ring |
| `volt` (`accent`) | `#E6FF00` | `#E6FF00` | Primary CTA, splash zəmin, logo, seçili dolğu, progress dolu, aktif state, link, vurğu — **daşıyıcı renk** |
| `volt-pressed` | `#C9E000` | `#C9E000` | Basılı buton durumu |
| `volt-glow` | `rgba(230,255,0,0.12)` | `#F4FFB0` | Seçili kart zəmini, hero glow, banner dolğusu |
| `on-volt` (`on-accent`) | `#0E0E0E` | `#0E0E0E` | `volt` zəmin üzərində metin/ikon — **həmişə qara** |
| `moss` | `#A4B82B` | `#7E8C2E` | Dekorativ ikon, ikincil outline, qrafik ikincil xətt — **CTA OLMAZ** |
| `moss-dim` | `#5C6B1A` | `#A8B36B` | Çox sakit dekorativ stroke, disabled-accent |
| `text-primary` | `#FFFFFF` | `#0E0E0E` | Başlıq, birincil metin |
| `text-secondary` | `#A1A1A8` | `#5B6068` | İkincil metin, alt-açıqlama, label |
| `text-tertiary` | `#6A6A72` | `#9AA0AC` | Placeholder, pasif, yardımçı metn |
| `success` | `#3DD68C` | `#1FA85A` | Onay, sync tick, streak qorundu, **yaşıl chip-lər** |
| `warning` | `#FFB020` | `#B97400` | Xəbərdarlıq banner, offline durumu |
| `danger` | `#FF4D4D` | `#DC2626` | Destructive aksiyon, inline error |
| `danger-soft` | `rgba(255,77,77,0.12)` | `#FEECEC` | Destructive onay modalı zəmini |

**Kontrast (WCAG — dark):** `volt`/`bg` ≈ 17.6:1 ✅ AAA · `on-volt`/`volt` ≈ 17:1 ✅ AAA · `text-primary`/`bg` ≈ 19.8:1 ✅ AAA · `text-secondary`/`bg` ≈ 7.6:1 ✅ AAA · `success`/`bg` ≈ 9:1 ✅. Renk heç vaxt tək bilgi daşıyıcısı deyil — seçili durum renk + `check` ikon + stroke ilə birlikdə.

**Elevation prinsipi:** Dark mode hiyerarşini gölgə ilə deyil **elevation katmanı** ilə anladır: `bg` → `surface` → `surface-elevated`. Gölgə tək başına yetərsiz; ton fərqi əsas ayraçdır.

**Qadağalar:** Köhnə turuncu accent (`#FF6B33`) **ləğv** — Volt kanonikdir. `moss` heç vaxt buton/CTA olmaz. `volt` üzərində ağ metin yazılmaz. Hər renk dəyəri yalnız bu cədvəldə dəyişdirilir — komponentlər token adı ilə bağlanır.

## 1c. Minimal Documentation Principle (məcburi)

Hər planlama sənədi **bir UI/feature müqaviləsinə** bağlı olmalıdır — generic "hər şeyi əhatə edən mega-PRD" YASAQ.

### Qaydalar
1. **Bir sənəd = bir scope.** PRD yalnız öz scope-undakı funksional müqaviləni saxlayır (FR, AC, axın, state).
2. **Texniki spec ayrı.** Data model, SQL DDL, API contracts, error codes, implementation notes → ayrı `*-data-model-*.md` / `*-api-*.md` faylına. PRD-də yalnız 1-cümləlik pointer.
3. **UX brief ayrı.** Ekran inventarı, Volt token istifadəsi, komponent inventarı, accessibility annotations → ayrı `ux-*.md` faylına. Parent PRD-də pointer.
4. **Analytics ayrı.** Event catalog + success metrics → ayrı `*-analytics-*.md`. Parent PRD-də pointer.
5. **Data catalog ayrı.** User-profile field-ləri kimi cross-feature data → vahid `*-data-catalog-*.md`. Parent PRD-yə bağlı, amma şərikli (auth + AI plan + calorie + workout PRD-lər ondan oxuyur).
6. **Hər sənəd frontmatter-də `parent_prd` + `relatedDocs` saxlayır** — qrafik aydın olmalıdır.
7. **Tək sənəd > 1000 sətir → boş.** Bu boşluq deyil, **dizayn xətasıdır**. Bölünməyə işarə.
8. **Deferred-stub şablonu** — out-of-scope mövzular üçün 50-150 sətirlik `prd-X-deferred-*.md` stub yaradılır (köhnə PRD-də alınmış qərarları arxivləyir). Real PRD sonra açılır.

### Sənəd növləri (Faza 1)
| Növ | Pattern | Misal |
|-----|---------|-------|
| PRD (funksional) | `prd-<scope>-YYYY-MM-DD.md` | `prd-auth-onboarding-2026-05-22.md` |
| Texniki spec | `prd-<scope>-data-model-YYYY-MM-DD.md` | `prd-auth-data-model-2026-05-22.md` |
| Analytics spec | `prd-<scope>-analytics-YYYY-MM-DD.md` | `prd-auth-onboarding-analytics-2026-05-22.md` |
| UX spec | `ux-<scope>-YYYY-MM-DD.md` | `ux-auth-onboarding-2026-05-22.md` |
| Data catalog (cross-feature) | `prd-<entity>-data-catalog-YYYY-MM-DD.md` | `prd-user-profile-data-catalog-2026-05-22.md` |
| Deferred stub | `prd-<scope>-deferred-YYYY-MM-DD.md` | `prd-paywall-deferred-2026-05-22.md` |

### Hədəf həcm
- PRD funksional: 400-700 sətir
- Texniki spec: 200-400 sətir
- UX spec: 200-500 sətir
- Analytics spec: 80-150 sətir
- Data catalog: 600-1000 sətir (cross-feature olduğu üçün böyükdür)
- Deferred stub: 50-150 sətir

### Qadağa
- HEÇVAXT bir PRD-yə həm scope, həm data model, həm UX, həm analytics, həm implementation notes tıxa — bölmə məcburidir.
- HEÇVAXT parent backlink olmayan sənəd yarat — orphan documents discoverable deyil.
- HEÇVAXT bir UI ekranı 2 PRD-də paralel təsvir et — tək həqiqət mənbəyi.

## 2. UX Akışı (MVP)

```
Login/Register (§3.4)
  └─ Age Gate (§3.6) → Onboarding (7 məcburi sual, ≤90 sn)
       └─ Dashboard  ─── primary tile: "Bugün Məşqi" {var | yox | tamam}
            │            secondary: streak (§5.7) · kalori qalan · su qalan
            ├─ Hərəkət Kitabxanası (filtr + axtarış)        →  Hərəkət Detalı (GIF auto · MP4 tap · YouTube · cues)
            ├─ Məşq Planı   (Manual yığ §5.1 ↔ AI yarat §5.2) →  Bugün Sessiyası (set/rep logger + rest timer)
            ├─ Kalori & Hidrasiya (foto §7 · su · protein) +  Light Meal Suggest (§7.2)
            └─ Tərəqqi (çəki · ölçü · foto · streak · performans metric §5.5)
```

## 3. Onboarding & Identity

### 3.1 Məcburi 7 sual (≤90 sn, dashboard-dan əvvəl)
1. **Əsas hədəf** — `bulk` | `cut` | `general_fit`
2. **Cins** — `male` | `female`
3. **Yaş** (→ §3.6 age gate)
4. **Boy + çəki** (eyni ekran, iki sahə = 1 sual)
5. **Təcrübə** — `beginner` | `intermediate` | `advanced`
6. **Kontekst** — `serious_gym` | `casual_gym` | `home_only`
7. **Həftəlik gün × sessiya dəqiqəsi** (eyni ekran)

### 3.2 Opsiyonel 19 sahə (progressive profiling)

Settings → Profile-də tam list. Dashboard nudge: "Planın +%23 dəqiqlik üçün 2 dəqiqən var?"

`injury_history` · `movement_restrictions` · `resting_hr` · `body_fat_visual` · `target_weight` · `target_deadline` · `sleep_h_per_night` · `stress_1_5` · `diet_pattern` (`omnivore|vegetarian|halal_strict|ramazan_active`) · `allergies` · `cardio_preference` (`hiit|steady|none`) · `hated_exercises[]` · `preferred_training_time` · `meal_timing` · **`pregnancy_postpartum`** (F) · `step_goal` · `trainer_voice` (`m|f|neutral`) · `notification_cadence` (`daily|3x|weekly`) · `equipment_inventory[]`

> **Faza 2-yə deferred:** `cycle_tracking_opt_in` — solo MVP scope; BetterMe/Freeletics/Fitbod-da yoxdur (parity yoxdur); AZ-market cultural sensitivity (onboarding nudge drop-off riski); 12 ay sonra yenidən qiymətləndir.

### 3.3 Trigger qaydaları (drop-off-u minimize)

| Sahə | Nə vaxt soruşulur |
|------|-------------------|
| `injury_history` | İlk plan generasiyasından **dərhal əvvəl** (1 ekran inject) |
| `sleep_h_per_night` · `stress_1_5` | Day-3 retention milestone |
| `pregnancy_postpartum` | F user-ə dashboard nudge **bir dəfə**, rədd-də Settings-də qalır |
| Qalanlar | Settings → Profile, user özü tamamlayır |

### 3.4 Auth & Identity (MVP)

| Provider | Platform | Qeyd |
|---|---|---|
| Email + Password | hər ikisi | Supabase Auth; email verification məcburi |
| **Apple Sign-In** | iOS | App Store qaydası — başqa social provider varsa, Apple məcburi |
| **Google Sign-In** | Android primary | iOS-da opsiyonel |
| Phone OTP | — | **Faza 2** |

**Logout:** §10.6 sync-queue check; pending sessions > 0 → blocking modal. Local SQLDelight wipe (`progress_logs` qaçırma — server-də qalır) + RevenueCat anonymous switch. Re-login = re-sync.
**Account delete:** §10.3 (cascade + grace + re-login).

### 3.5 Profil dəyişdirmə müqaviləsi
- `goal` · `context` · `equipment_inventory` · `weekly_days` Settings → Profile-dən dəyişir
- **Throttle:** `goal` üçün 14 gündə 1 dəfə (yo-yo plan generation-u kəsər)
- Dəyişiklik → §5.2 trigger #3 aktiv; §5.5 protein/su hədəfləri re-hesab; §6 persona-cell yenilənir
- **Persona-cell switch MVP-də user-only** — behavioral auto-promote (`casual → serious` 5x/həftə 4 həftə) yalnız analytics event kimi log olunur, otomatik dəyişmir (yanlış promote → həcm artımı → injury riski)

### 3.6 Age Gate (launch-blocker, COPPA/GDPR-K compliance)

- **Minimum yaş: 13** (Apple App Store + Google Play uşaq data qaydaları)
- §3.1 Q3 (Yaş) <13 → **HARD-STOP:** "Bu app 13 yaşından kiçik istifadəçilərə icazə vermir" — register flow tamamlanmır, user yaradılmır
- **13-17** → parental notice ekranı: Privacy Policy + data usage özet + "Davam etmək üçün valideyn icazənizi təsdiqləyirəm" checkbox
- Email/phone hash 30 gün cache-də saxlanır → fərqli yaşla **bypass YASAQ** (analytics flag `age_gate_retry`)
- **Privacy Policy (AZ+RU+EN)** parental notice metnindən link açılır

### 3.7 Localization & String Fallback (runtime qaydası)

- **Resource keys:** `shared/strings/{az,ru,en}.json`; key naming `feature.subfeature.action_label`
- **Fallback chain:** **AZ → RU → EN → key-itself** (silent miss YASAQ; key-itself fallback red placeholder kimi UI-də gözə çarpsın)
- **Missing key analytics:** runtime-da hər fallback hadisəsi → `missing_strings` event (admin CMS-də fix queue-su)
- **MT detection:** translation memory hash diff > %30 → manual review queue (CLAUDE.md MT YASAQ-ı təmin etmək üçün CI gate)
- **Pluralization:** CLDR rules (AZ singular/plural; RU 3-form; EN 2-form) — hardcoded numeric format YASAQ
- **RTL:** MVP-də YOX (AZ/RU/EN hamısı LTR)

## 4. Hərəkət Kitabxanası

### 4.1 Filtr taksonomiyası (5 ölçü, Hevy-paterni)

| Ölçü | Dəyərlər |
|------|----------|
| **Əzələ bölgəsi** | sinə · kürək · çiyin · qol · ayaq · mərkəz · dabaq · tam-bədən |
| **Avadanlıq** | bodyweight · dumbbell · barbell · machine · lent · kettlebell |
| **Hərəkət tipi** | compound · isolation |
| **Çətinlik** | beginner · intermediate · advanced |
| **Yer** | home · gym (avadanlıqdan törəyir, sürətli toggle) |

**UI patern (Hevy qalibi):** üst chip-row = **yalnız "Bölgə" + "Avadanlıq"**; "Filtr" ikonu → bottom-sheet → qalan 3 ölçü + "Təmizlə". **YASAQ:** Strong-tipi side drawer (bir-əl ergonomika qırır), JEFIT-tipi full-page (50-100 item üçün overload).

### 4.2 Hərəkət Detalı — media triple

| Slot | Format | Davranış | Mənbə / Cost |
|------|--------|----------|--------------|
| **Preview GIF** | <500 KB, 12 fps, 3-5 sn loop | **Avtomatik oynayır** (list və detail header) | MP4-dən ffmpeg ilə törət (`-vf "fps=12,scale=320:-1" -loop 0`), cost $0 |
| **Demo MP4** | 1080p, 8 sn, branded overlay | **User tap → play** | **Veo 3.1 Fast: $0.15/s × 8s = $1.20/hərəkət** → 100 hərəkət = $120 ($180 reserve) |
| **YouTube short** | embed, ≤60 sn | User tap → play | Manual curate (kanal whitelist: Mike Israetel, Jeff Nippard, AZ trainerlər) |

**Texnique cues:** AZ/RU/EN, ≤5 bullet, **"Səhv: ... / Düzgün: ..." kontrastı**, breath cue.

**Cost qaydası:** AI media generasiyası **yalnız admin panel-dən manual** tetiklənir (runtime user-tetiklənmir); hər çağırış `// cost: $1.20/exercise` şərhi ilə commit; Veo non-Ultra "Made with Veo" stamp App Store red riski → **branded overlay crop məcburi**; failed Veo call → max 2 retry exponential backoff, billed-only failures `ledger.failed_cost_logged=true` ilə qeyd.

## 5. Məşq Planı Müqaviləsi

### 5.1 Manual Plan Builder (user-built)

Dashboard → "Plan Yarat" → **manual** seçimi:
1. Boş həftəlik şablon (7 gün slot)
2. Hər günə "Hərəkət əlavə et" → §4 kitabxana picker (filter retain)
3. Set/rep/rest input (default: 3 × 10 × 60s)
4. "Şablon kimi saxla" toggle (user gələcəkdə yenidən istifadə edə bilər)

**Qayda:** Manual plan da §5.3 JSON kontraktına yazılır, `source: "manual"`. §5.2 AI trigger 1-2 (adaptation, recovery) **deaktiv**; trigger 3-4-5 aktiv qalır (profil dəyişikliyi və ya "yenilə" tap = AI təklifi).

### 5.2 AI Plan — sparse cached (Freeletics adaptasiyası)

```
Trigger → Edge Function → LLM → JSON →
   ├─ [PREMIUM only] human approval gate (mütəxəssis uyğunluq yoxlaması, queue)
   │                  → onaylanmış JSON
   ├─ Supabase `workouts` yaz (status: free=ready · premium=pending_review→ready)
   └─ App-ə qayıt → SQLDelight cache → gündəlik məşq local-dan
```

**Human approval gate (premium only) — quality gate, trainer DEYİL:**
- Free istifadəçi: AI JSON birbaşa `ready` → app-ə.
- Premium istifadəçi: AI JSON `pending_review` statusuna düşür → 1-2 əl-seçimli AZ mütəxəssis queue-dan götürür → təhlükəsizlik/uyğunluq yoxlaması (qadağan kombinasiyalar, intensity vs persona, pregnancy/injury modifier riayəti) → `ready`. Mütəxəssis fərdi koreksiya etmir, chat yoxdur, istifadəçi ilə birbaşa təması yoxdur — yalnız onay/red + standartlaşdırılmış reject reason.
- SLA: premium ilk plan üçün ≤24 saat; bu müddətdə "Planın yoxlanılır — tezliklə hazır olacaq" placeholder + sample workout aktiv.
- Onboarding-də vəd dili: "**plan, göndərilmədən əvvəl bir mütəxəssis tərəfindən yoxlanılıb onaylanır**" — "məşqçi/coach/trainer" sözü QADAĞAN.

**AI re-call yalnız bu 5 trigger-də (Edge Function-da post-sync hesablanır, client deyil):**
1. Cari həftədə **≥3 sessiya log** olundu → adaptation
2. `expires_at` keçdi → default **4 həftə**
3. User profil dəyişdi (goal | equipment | context | weekly_days)
4. **≥2 ardıcıl missed sessiya** → recovery plan
5. User manual "planı yenilə" tap etdi

**"Missed sessiya" tərifi:** scheduled date keçdi + 36 saat pəncərədə 0 set log; skipped-tap explicit dərhal sayılır.

**Anti-thrash qaydaları:**
- **24h debounce:** eyni user-də gün ərzində maks 1 re-gen — `pregnancy_postpartum` istisna (safety-critical, dərhal regenerate)
- **Aylıq cap (SYSTEM-AUTO triggers 1-4):** free **4**/ay, premium **12**/ay (GCP $300 cost guard)
- **User-initiated regenerate (trigger #5):** §9 quota-ya tabe — ayrı sayğac, system-auto cap-dən asılı deyil
- **Token cap:** plan generation per call hard limit; aşıldıqda 1 retry-də scope truncation (sıra: §5.6); partial JSON qaytarma YASAQ
- **`expires_at` grace:** stale plan render edilməyə davam edir + "Yeni plan hazırlanır" banner; new plan ISO həftə sonunda aktivləşir (cliff cutover YASAQ)
- **Profile change effect:** cari həftə köhnə plan-dan render olunur, yeni plan **növbəti ISO həftə**-dən aktivləşir

### 5.3 JSON Şeması (kanonik kontrakt — major bump = `schema_version` MAJOR)

```json
{
  "plan_id": "uuid",
  "user_id": "uuid",
  "schema_version": "1.0.0",
  "source": "ai|manual",
  "generated_at": "ISO8601",
  "expires_at": "ISO8601",
  "persona": {
    "context": "serious_gym|casual_gym|home_only",
    "sex": "male|female",
    "goal": "bulk|cut|general_fit"
  },
  "modifiers": ["ramazan_active", "pregnancy_postpartum", "injury:lower_back", "..."],
  "weeks": [{
    "week_number": 1,
    "sessions": [{
      "day_of_week": 1,
      "session_type": "strength|conditioning|technique|rest",
      "estimated_duration_min": 45,
      "blocks": [{
        "block_type": "warmup|main|cooldown",
        "exercises": [{
          "exercise_id": "uuid",
          "load": {"type": "bodyweight|percent_1rm|rpe_anchored|absolute_kg", "value": null},
          "alternatives": ["exercise_id"],
          "sets": 3, "reps": 10, "rest_sec": 60,
          "tempo": "2-0-2-0", "rpe_target": 7,
          "notes_az": "form cue — opsiyonel",
          "order_index": 1
        }]
      }]
    }]
  }],
  "staggered_insights": [{
    "trigger": {"type": "after_session_complete|day_of_week|elapsed_days", "value": 3},
    "category": "form_cue|tempo|overload|deload",
    "exercise_id": "uuid|null",
    "body_az": "..."
  }],
  "motivational_pool": [{
    "text_az": "...",
    "tone": "celebrate|nudge|recover|streak_save",
    "persona_filter": ["home_only.female.cut", "..."]
  }]
}
```

**Manual source qaydası:** `source: "manual"` olduqda `staggered_insights = []` (boş) və `motivational_pool` static curated AZ pool-dan fallback ilə doldurulur (AI generasiya YOX — cost guard). AI trigger 3-4-5 manual planda da §5.2 qaydası ilə işə düşə bilər.

**Schema migration:** client N-1 versiyaya qədər oxuyur; MAJOR bump-da cache `stale=true` markala → trigger #2 (expiry) ilə lazy regenerate (forced refresh YASAQ — kütlə ilə LLM cost spike). Server son 2 schema deserializer-i 90 gün boyu aktiv saxlayır.

### 5.4 Hesablama bölgüsü

| Layer | Məsuliyyəti |
|-------|-------------|
| **Local (app, `shared/calc/`)** | Set/rep log, rest timer, streak counter (§5.7), gündəlik kcal/su/protein progress, BMR/TDEE math, water/protein target (§5.5), plan render, local push schedule, `session_completion_pct`, `session_tonnage_kg`, `session_rpe_avg` |
| **Backend Edge Fn (AI-suz)** | Həftəlik volume xülasə, **Volume PR** detection, trigger evaluation post-sync, foto-kalori AZ DB lookup, persona-switch signal log (auto-promote YASAQ) |
| **Backend + AI** | Plan generasiyası (5 trigger), adaptive re-write, `staggered_insights` pool, `motivational_pool`, GPT-4o ingredient extraction (foto-kalori) |

**Idempotency:** offline log sync = `(user_id, session_id)` key; trigger evaluation server-only post-sync; client yalnız trigger #5 (manual) çağırır. Sync endpoint cavabında `{triggers_fired: [...]}` qaytarır.

### 5.5 Kanonik formulalar (`shared/calc/`)

| Metrik | Formula | Layer |
|--------|---------|-------|
| **BMR** | Mifflin-St Jeor | local |
| **TDEE** | BMR × activity factor (`weekly_days × session_min` → 1.2 / 1.375 / 1.55 / 1.725) | local |
| **Target kcal** | `bulk`: TDEE +300 · `cut`: TDEE −500 · `general_fit`: TDEE | local |
| **Protein hədəfi** | `bulk`: 1.6-2.0 g/kg LBM · `cut`: 2.0-2.4 · `general_fit`: 1.4-1.6 (LBM yoxdursa BW × 0.85) | local |
| **Su hədəfi** | 35 ml × kg, max 4 L | local |
| **`session_completion_pct`** | logged_sets / planned_sets | local |
| **`session_tonnage_kg`** | Σ(weight × reps) | local |
| **`session_rpe_avg`** | mean(set.rpe) | local |
| **`streak_day_boundary`** | device-local TZ, 03:00 cutoff (§5.7) | local |
| **`weekly_volume_per_muscle`** | Σ working_sets / muscle_group | backend |
| **Volume PR** | exercise-də session_tonnage_kg max-ı | backend |

### 5.6 AI Prompt Blueprint

LLM prompt mühəndisliyi qaydaları (plan generasiyası — §5.2 trigger-ləri ilə işə düşür). Edge Function-da template render edilir, **client-də YASAQ**.

**Periodization modeli (MVP):**
- **Linear progression (default):** sets/reps `week_number`-ə anchor olunur (1→2→3 set və ya 8→10→12 rep tədricən)
- **Undulating (P1):** ilk 4 həftəlik blok tamamlandıqdan sonra intra-week variasiya
- **Block periodization (accumulation/intensification/realization):** **Faza 2**

**Split logic (`weekly_days` → split type):**

| weekly_days | Split |
|---|---|
| 2 | Full Body |
| 3 | Full Body (default) **VƏ YA** PPL-condensed (yalnız `serious_gym`) |
| 4 | Upper / Lower |
| 5 | PPL + 2 accessory (arms/glutes) |
| 6 | PPL ×2 |

**Warmup/cooldown qaydaları (məcburi):**
- **Warmup block:** ≥2 dynamic mobility + 1 movement-specific (yalnız compound üçün loaded; isolation-da yüksüz)
- **Cooldown block:** ≥3 static stretch, hər biri **≥30s hold** (`rest_sec` field-i hold duration kimi)

**`hated_exercises[]` injection qaydası:**
- Prompt-a `EXCLUDE: [exercise_id, ...]` blok-u məcburi
- Listdəki id-lər `weeks[].sessions[].blocks[].exercises[]`-də **çıxa BİLMƏZ**
- Exclusion valid plan-ı imkansız edirsə → ən yaxın alternativ + `notes_az: "Seçilmiş hərəkət xaric edilə bilmədi — alternativ təklif"` flag

**`alternatives[]` törətmə qaydası:**
- Hər `compound` hərəkət üçün **≥1 alt məcburi**
- `context = home_only` → **bodyweight alt məcburi** (`equipment_required = bodyweight`)
- `context = casual_gym | serious_gym` → equipment-based alt (dumbbell ↔ barbell ↔ machine)
- Isolation-da alt opsiyonel (boş array qəbul olunur)

**`load.type` seçimi:**

| Təcrübə × Hədəf | load.type | Səbəb |
|---|---|---|
| `beginner` × any | `bodyweight` **VƏ YA** `rpe_anchored` | 1RM bilinmir, absolute weight YASAQ |
| `intermediate` × `bulk` | `percent_1rm` | 1RM seed lazımdır → ilk həftə soruşulur |
| `intermediate \| advanced` × `cut` | `rpe_anchored` | dəfisitdə strength qoru |
| `advanced` × `bulk` | `absolute_kg` **VƏ YA** `percent_1rm` | data-driven progression |

**Prompt budget guard (token overrun truncation sırası):**
1. `motivational_pool` (ən az kritik)
2. `staggered_insights`
3. `alternatives[1+]` (yalnız ilk alt saxlanır)
4. **HEÇVAXT** `weeks[].sessions[].blocks[].exercises[].sets / reps / order_index` truncate edilməz — plan-ın canı

### 5.7 Streak + Modifier Lifecycle

#### Streak tam tərifi

| Sahə | Qayda |
|---|---|
| **Day boundary (client)** | device-local TZ, **03:00 cutoff** (02:55 log = dünən; 03:05 log = bugün) |
| **Server storage** | UTC normalize; `last_workout_at_utc` (timestamptz) + `device_tz` (text, IANA) per user |
| **Streak increment** | Day boundary daxilində ≥1 session log; plan üzrə rest day = "kept" (sayılır) |
| **Streak break** | 2 ardıcıl **scheduled** day miss (rest day istisna) |
| **Streak Freeze** | Ayda 1 dəfə **otomatik** ilk miss-də auto-apply; 2-ci miss → copy: `"Bu ay Freeze istifadə olundu — yenisi <date>"` (§9 ilə eyni) |

#### Modifier lifecycle

| Modifier | Aktivləşmə | Eviction | Plan effect |
|---|---|---|---|
| `ramazan_active` | `diet_pattern=ramazan_active` set **VƏ YA** user toggle **VƏ YA** auto-detect (regional + hicri tarix) | user manual off **VƏ YA** Ramazan bitir | §6.2 saat dəyişikliyi + intensity −%20 |
| `pregnancy_postpartum` | user opt-in (§3.2) | user manual off; postpartum threshold **12 ay** | §6.3 hard-stop (curated template) |
| `injury_history[*].active=true` | user toggle | user `healed=true` **VƏ YA** 6 ay keçir (auto-archive) | həcm −%50 + `alternatives[]` məcburi |

**Modifier eviction qaydası:**
- Active modifier dəyişdikdə §5.2 trigger #3 (profile change) işə düşür
- Growth-fast trigger 24h debounce-a tabe (§5.2 anti-thrash)
- **İSTİSNA:** `pregnancy_postpartum` modifier dəyişikliyi debounce-dan istisna (safety-critical, dərhal regenerate)

## 6. Persona Davranış Matrisi (default rule-set)

`{context} × {sex} × {goal}` = 8 əsas hüceyrə. AI plan və copy generasiyasında **default**.

| Hüceyrə | Ton | Rep | Dincəlmə | Həcm/həftə | Kardio | Copy nümunəsi |
|---|---|---|---|---|---|---|
| serious_gym × M × bulk | texniki, data | 6-10 | 90-150s | 16-20 set/əzələ | aşağı | "Mexaniki gərginlik — RPE 8" |
| serious_gym × M × cut | analitik, deficit-aware | 8-12 | 60-90s | 12-16 | mod HIIT 2× | "Həcm qoru, kalori dəfisitdə" |
| serious_gym × F × bulk | texniki + komp framing | 8-12 | 90s | 14-18 | aşağı | "Glute volume +2 set" |
| serious_gym × F × cut | analitik, recomp framing | 10-15 | 60s | 12-16 | HIIT 2-3× | "Şəkilləndirmə — protein 1.8 g/kg" |
| casual_gym × M × general_fit | həvəsləndirici + yüngül data | 8-12 | 60-90s | 10-12 | mod 2× | "Bu həftə 3 məşq — davam!" |
| casual_gym × F × general_fit | isti, foto-tərəqqi fokus | 10-15 | 60s | 10-12 | mod 2× | "Aferin — bədənin güclənir" |
| home_only × M × any | minimal-equip həvəs | 10-15 + tempo | 45-60s | 8-12 | bodyweight HIIT | "Avadanlıq yox — tempo işə qoş" |
| home_only × F × general_fit\|cut | isti, privacy-aware, qısa sessiya | 12-20 | 30-45s | 8-10 | low-impact + walk | "20 dəq, səssiz hərəkətlər" |

### 6.1 Cross-cell qaydalar (JMIR 2024 evidence)
- **F:** cooperation/progress framing > competition; **global leaderboard default GİZLİ**, yalnız personal best; habit framing
- **M:** tracking + challenge framing; streak + 1RM trend prominent; performance-expectancy framing

### 6.2 AZ-kültür adapterləri (məcburi)
- **Trainer voice default:** F→female, M→male; Settings-də opt-out
- **Modesty copy:** "bədəninizi göstərin" **YASAQ** → "güclən, formanı qoru"
- **Ramazan mode (P1):** F home_only post-iftar (20:30-22:00) + pre-suhoor mobility; oruc saatlarında intensity **-%20**
- **Home_only F:** jumping/noisy hərəkətlər **default GİZLİ**, "səssiz rejim" toggle
- **Female onboarding social proof:** yalnız same-sex testimonial imagery (mixed-sex YASAQ)

### 6.3 Modifier precedence + safety hard-stops

Bir neçə modifier eyni anda aktiv olduqda **azalan prioritet**:
1. **`pregnancy_postpartum=true` → HARD-STOP:** AI plan generasiyası işə düşmür; user curated "prenatal/postpartum" static template-ə yönləndirilir + medical disclaimer ekranı məcburi (legal/launch-blocker)
2. **`injury_history[*].active=true`** → injured muscle group həcmi -%50, alternative exercise məcburi
3. **`ramazan_active`** → oruc saatlarında intensity -%20
4. **`home_only` + F** → "səssiz rejim" qaydaları
5. **`cut` deficit** → ən aşağı, yuxarı modifier-lərin sərhədi içində

## 7. Foto Kalori + Light Meal Engine

### 7.1 Foto tanıma
- **Stack:** **GPT-4o Vision** (~$0.005/img) → ingredient list + portion grams
- Backend Edge Fn ingredient-ləri **AZ Top-200 yemək DB**-də lookup → macros + kcal
- **Confidence < 0.7** → user-ə "düzəlt" UI (3 candidate suggestion)
- **Confidence ≥ 0.7 + AZ DB-də 0 match** → eyni "düzəlt" UI-yə düş + admin gap-log (DB genişlətmək üçün)
- **Foto-da yemək tapılmadı** → "təkrar çək" + manual macro entry seçimi
- **Niyə LogMeal/Foodvisor/Calorie Mama RƏDD:** AZ food coverage 0, qiymət ≥ GPT-4o, Western portion bias kcal-ı yalan qaytarır

### 7.2 Light Meal Suggestion + Future Diet App Bridge
- Gündəlik deficit hesablandıqdan sonra (§5.5) dashboard kart: **"Bu gün üçün 3 yüngül yemək təklifi"** → AZ Top-200 DB-dən `kcal_per_100g ≤ 130` filtr + macro fit
- **Future diet app deep-link:** `fitnessdiet://meal/<meal_id>` probe; quraşdırılmayıb-sa Store link, varsa kontekst pass
- MVP-də full diet planning **YASAQ** (Faza 2) — yalnız "təklif + redirect" stub
- **Cost guard UX:** free user-də 6-cı foto cəhdi → **soft upsell modal** (kilidli düymə YASAQ) + manual macro entry fallback + quota reset vaxtı göstər

## 8. Bildirişlər və Staggered Insight

### 8.1 Kanal müqaviləsi (kanonik allowlist — duplikasiya YASAQ)

| Event | Kanal | Səbəb |
|-------|-------|-------|
| `plan_ready` | remote-only | server-side trigger |
| `workout_reminder` · `streak_risk` · `water_reminder` | local-only | offline çalışmalı |
| `billing_event` | remote-only | server-side state |
| `insight_delivery` | local (offline OK) | scheduled in plan generasiyası |

- Local push **4 saat sonra self-expire** (stale "Bu səhər məşq" toast at noon = YASAQ)
- **Permission denied** → in-app dashboard banner streak-risk üçün; re-prompt yalnız streak-break event-də, ayda 1 dəfə
- Stack: WorkManager (Android) + UNUserNotification (iOS), `shared`-də `expect/actual` interface

### 8.2 Staggered insight qaydası

Bütün insight-lar plan generasiyasında AI tərəfindən bir dəfə çıxır (`staggered_insights[]`), amma **eyni gün hamısı verilmir**. Default cədvəl (override JSON-da `trigger` object-i ilə):

| Həftə | İçerik | Say |
|-------|--------|-----|
| 1 | Hərəkət forma cue-ları | 3 |
| 2 | Nəfəs + tempo cue-ları | 2 |
| 3 | Progressive overload məsləhəti | 2 |
| 4 | Deload / recovery insight | 1 |

Hər insight notification AZ copy persona-cell-ə uyğun seçilir (`motivational_pool` → `tone + persona_filter`-ə görə).

## 9. Free vs Premium Davranış Sərhədi

CLAUDE.md monetization qiymət cədvəlini **təkrarlamır** — bu, **kod-davranış** qaydaları:

| Sahə | Free | Premium |
|------|------|---------|
| Hərəkət kitabxanası | 30 hərəkət (CMS-də `exercises.is_free_tier=true`) | tam (50-100) |
| **"Base workout" set** | **5 sabit** (CMS-də `exercises.is_free_tier_base=true`) — squat, push-up, row, hip-hinge, plank; **rotation YASAQ** (predictability + caching) | tam variant |
| Plan generasiyası (USER-INITIATED "yenilə", trigger #5) | **1 / ay** | sınırsız (system-auto cap §5.2: 12/ay) |
| Foto kalori tanıma | 5 / gün → 6-cı soft upsell | sınırsız |
| Streak Freeze | 1 / ay (qeyri-monetar; 2-ci miss copy §5.7) | 1 / ay |
| Adaptive re-generate (triggers 1-2) | mövcud deyil | aktiv |
| AZ trainer voice-over | bir səs | M+F hər ikisi |
| Ramazan mode | gizli | aktiv (P1) |

**Free → Premium upsell trigger (analytics event):**
- İlk plan tamamlandı (cohort: high-intent)
- Foto kalori 5-ci dəfə eyni gün
- Premium-only hərəkət list-də önə düşdü
- Ay sonu plan generasiya quota bitdi

**Downgrade semantics (Premium → Free):**
- Foto log over-quota: **silinmir** (retain + read-only "Premium tələb olunur" overlay)
- 1/ay user-initiated plan-gen counter: növbəti billing cycle-da reset
- Premium-only `staggered_insights`: mövcudları oxunur, yeni trigger olmur
- AZ trainer voice ikinci səs: yalnız ilk seçilən səs aktiv qalır

## 10. Edge Case & Data Integrity Qaydaları

### 10.1 Offline ↔ online conflict
- Offline log → sync zamanı **`(user_id, session_id)` idempotency key**; server log-u `plan_id` ilə qəbul edir, version mismatch-də RƏDD ETMƏZ
- Trigger evaluation **yalnız server-side post-sync** — client direct çağırmır (trigger #5 manual istisna)

### 10.2 Schema versiyalama
- **Client tolerance:** N-1 MAJOR oxuya bilir; MAJOR uyğun-sa render, deyilsə `stale=true` markala
- **Lazy regenerate:** yalnız §5.2 trigger #2 (expiry) ilə — forced refresh YASAQ (kütləvi LLM cost spike)
- **Server retention:** son 2 schema_version deserializer 90 gün canlı saxlanır
- **MAJOR+1 deploy öncəsi:** analytics ilə N-1 client adoption monitoring məcburi; >%5 N-1 traffic-də deploy bloklanır

### 10.3 Account delete cascade (CLAUDE.md launch-blocker)
- Cascade order: `users → user_profiles → workouts → workout_sessions → workout_exercises → progress_logs → calorie_logs`
- Storage `userId/*` Edge Fn ilə purge (foto retention §10.6)
- **30 gün soft-delete grace** → hard purge
- **Re-login grace ərzində:** account restore (deletion ləğv) + analytics flag `delete_aborted_reopen`; grace bitdikdən sonra re-register **fərqli user_id** ilə (data bərpa olunmaz)

### 10.4 Reinstall rehydration
- İlk launch-da SQLDelight `workouts` + 30 günlük `progress_logs`-dan rehydrate olunur
- Pre-install offline-only loglar **bərpa edilmir** — uninstall öncəsi sync queue > 0 olduqda in-app banner xəbərdarlığı məcburi

### 10.5 Veo / GPT-4o failure
- Veo timeout → max 2 retry exponential backoff; billed-only failures `ledger.failed_cost_logged=true`; admin UI per-batch $-budget göstərir
- LLM token cap aşılarsa → 1 retry truncated scope (§5.6 truncation sırası); partial JSON qaytarma YASAQ

### 10.6 Data Loss Warnings + PII

- **Logout sync-queue check:** pending sync queue `> 0` olduqda logout-da **blocking modal**: `"<N> sessiya hələ sync olunmayıb — gözlə ya da göndər?"`. Default action = "Göndər və çıx" (loss-averse). Force-logout → unsynced log silinir (user explicit consent, "Anladım, sil" düyməsi).
- **Uninstall pre-warning:** §10.4 qaydası (in-app banner sync queue > 0 olduqda) — eyni qayda, təkrarlanmır.
- **Body photo retention:**
  - Storage path: `userId/photos/*`
  - Encryption-at-rest: Supabase default **AES-256**
  - Signed URL TTL **≤1h** (CLAUDE.md məcburi)
  - **Retention: 365 gün** post-upload → thumbnail oto-archive + user-ə full delete option
  - User-initiated delete → §10.3 cascade + **24h Storage purge**
- **Privacy Policy mətnində açıq qeyd MƏCBURİ:** niyə saxlanır (progress comparison) · kim görür (yalnız user, server-side admin access **YASAQ**) · retention period (365 gün) · delete hüququ
- **Foto upload PII:** EXIF metadata (GPS, device model, timestamp) **server-side strip məcburi** — Edge Function `photo-upload-sanitize` (client-side strip etibarsız); strip uğursuz olarsa upload RƏDD

## 11. Agent Karar Hierarchy

Hər yeni feature/dəyişiklik təklifində agent **bu sırada** yoxlayır; ilk match-də DAYAN:

1. `CLAUDE.md` **Qəti Qadağalar / Out-of-Scope** → `hard reject`
2. **§1 Minimal Design** → "mövcud bir şey çıxarılır?" — yox-sa reject
3. **§6.3 Safety hard-stops** (pregnancy/injury) → məcburi yoxla
4. **§3.6 Age Gate** → COPPA/13+ doğrulamasından keçirmi?
5. **§6 Persona Matrix** → "hansı hüceyrə üçün qurulur?" — generic-sə reject
6. **Solo 20h/həftə + 4 ay** constraint → shippable? — yox-sa Faza 2-yə defer
7. **GCP $300 + LLM cost** → hər çağırış commented + cost-tracked? — yox-sa reject

---

## 12. Planlama Sənədləri İndeksi

### Faza 1 — Auth & Onboarding (active)
- **PRD:** `_bmad-output/planning-artifacts/prd-auth-onboarding-2026-05-22.md` (v3.1)
- **Data model + API + impl:** `prd-auth-data-model-2026-05-22.md`
- **Analytics + metrics:** `prd-auth-onboarding-analytics-2026-05-22.md`
- **UX brief:** `ux-auth-onboarding-2026-05-22.md`

### Cross-feature data
- **User profile data catalog:** `prd-user-profile-data-catalog-2026-05-22.md` (v1.0 — 75 field, 5 layer)

### Deferred PRD stub-lar (Faza 1 sonu / Faza 2)
- `prd-paywall-deferred-2026-05-22.md`
- `prd-settings-deferred-2026-05-22.md`
- `prd-sample-workout-preview-deferred-2026-05-22.md`
- `prd-professional-coaching-teaser-deferred-2026-05-22.md`

### Gözlənilən (planlanır, hələ açılmayıb)
- `prd-ai-plan-generation-*.md`
- `prd-workout-execution-*.md`
- `prd-calorie-tracking-*.md`
- `prd-billing-revenuecat-*.md`

### Konvensiyalar
Yeni sənəd yaradanda §1c (Minimal Documentation Principle) qaydalarına riayət et.

---

> v3.1 — 2026-05-17. Validation: 2 paralel agent (validator: 9/10 NOW confirmed + 3 PRD→NOW promote + cycle Faza 2; drafter: §5.6 prompt blueprint + §5.7 streak/modifier + §10.6 data loss + 6 mechanical fix). Cycle tracking deferred to Faza 2 (BetterMe/Freeletics/Fitbod parity yoxdur, AZ kültür hassasiyyəti). Növbəti faza: PRD (`bmad-prd` create intent) → Architect (`bmad-create-architecture`).
> v3.2 — 2026-05-27. BMad 6.6.0 → 6.8.0 upgrade: deprecated `bmad-create-prd`/`bmad-edit-prd`/`bmad-validate-prd` → unified `bmad-prd` (Create/Update/Validate intents auto-detect); `bmad-create-ux-design` → `bmad-ux` (DESIGN.md + EXPERIENCE.md spine); `bmad-distillator` → `bmad-spec` (5-field kernel). Shims v7-ə qədər qalır, migration tamamlandı.
