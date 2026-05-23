---
project_name: 'fitnessApp'
user_name: 'Balaagha'
date: '2026-05-23'
version: '1.5'
workflowType: 'ux-design'
ux_scope: 'auth-and-onboarding'
phase: 'MVP / Faza 1'
stepsCompleted: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14]
inputDocuments:
  - _bmad-output/planning-artifacts/prd-auth-onboarding-2026-05-12.md (v2.1, 2026-05-21)
  - docs/project-context.md (v3.3, 2026-05-21 — §0 Positioning · §1b Volt renk sistemi)
  - CLAUDE.md (Repositioning bloku, 2026-05-21)
  - app/design/mobile/app_design.pen (kanonik design system — köhnə ad: onboarding_flow.pen)
  - _bmad-output/planning-artifacts/ux-onboarding-questions-2026-05-23.md (sual ekran şablonu)
  - _bmad-output/planning-artifacts/ux-auth-onboarding-2026-05-22.md (v1.2 — axın səviyyəli)
sourcePRD: 'prd-auth-onboarding (v2.1)'
designSystemFile: 'app/design/mobile/app_design.pen'
handoffReady: true
---

# UX Design Specification — Authentication & Onboarding

**Author:** Balaagha
**UX Agent:** Sally (bmad-agent-ux-designer)
**Date:** 2026-05-21 (v1.4)
**Product:** fitnessApp — Azərbaycan bazarına yönəlik fitness tətbiqi
**Kapsam:** Age Gate · Auth (signup/login/session/logout/reset/delete) · First-run Onboarding (7 məcburi sual + opsiyonel progressive) · Profil önizləməsi · AI Disclosure · Sample Workout Preview · Paywall touchpoint · Professional Coaching teaser
**Kanonik girdi:** `prd-auth-onboarding` v2.1 · `docs/project-context.md` v3.3 (**§0 Positioning · §1b Volt renk sistemi**) · `CLAUDE.md` Repositioning + Qəti Qadağalar
**Design system referansı:** `app/design/mobile/app_design.pen` (deneysel — bu spec onu **dark-first** kanonikleştirir)

> **v1.5 (2026-05-23):** (a) `.pen` faylı `onboarding_flow.pen` → **`app_design.pen`** olaraq adlandırıldı — fayl artıq yalnız onboarding üçün deyil, bütün app dizaynı üçündür (sectionlar: Onboarding · Questions · sonra Home/Settings/Paywall). Bütün referanslar yeniləndi. (b) Sual ekran şablonu (anatomy, variant, state, copy üslubu, A11y) ayrı sənədə köçürüldü → **`ux-onboarding-questions-2026-05-23.md`** (L1+L2+L3+L5 vahid müqaviləsi). Bu sənəddə §6 ekran-ekran spec hələ qalır (axın kontekstində); sual səhifəsinin **şablon** səviyyəli detalı artıq questions UX-də. (c) `ux-auth-onboarding-2026-05-22.md` v1.2-yə bumped (axın-yalnız scope).

> **v1.4 (2026-05-21):** Scope hizalama + boşluk kapatma geçişi — kapsam **26 build edilebilir ekran** olarak netleştirildi (PRD §17.1'deki 28 inventardan Home #23 + Settings #24 scope dışı referans). §6 başlığı + §0b kapsam cümlesi güncellendi. Hata kodu izlenebilirliği yeni §8.6 tablosuyla tamamlandı — 18 kodun (AUTH_001-013 / ONB_001-005) tümü bir ekran durumuna bağlandı; AuthGate'e AUTH_008-012 durumları, AgeGateBlocked'a AUTH_012, cold-start'a ONB_003 eklendi. Form/ağ ekranlarına eksik durumlar (CTA disabled/loading, offline Banner, AUTH_007 toast) eklendi. §13 `.pen` handoff bölümü Stage B **tamamlandı** olarak güncellendi (26 ekran, Volt token'lar, komponent kütüphanesi mevcut).

> **v1.3 (2026-05-21):** Renk sistemi **Volt**'a geçti — turuncu accent (`#FF6B33`) → Ladder-ilhamlı elektrik-sarı (`#E6FF00`). §3.1 token tablosu project-context.md **§1b**'ye (yeni kanonik renk kaynağı) hizalandı; `moss`/`moss-dim`/`text-tertiary`/`border-strong`/`warning` token'ları eklendi; splash tam-sarı doktrini; UX-4 açık sorusu kapatıldı. Renk değeri artık yalnız project-context §1b'de tanımlanır — bu spec ona referans verir.

> **v1.2 (2026-05-21):** Cross-document tutarlılık geçişi — PRD v2.1'e hizalandı (PRD artık v3.2 §0 ile uyumlu; spec'in dayandığı PRD versiyonu güncellendi). Context ikonografisi düzeltildi (§3.4 ↔ Ekran 10). Marka adı "FitAz" açık soru olarak işaretlendi (UX-10).

> **v1.1 değişiklik özeti (v1.0 → v1.1, 2026-05-21):** BMad party-mode (Mary/John/Winston) repositioning müzakeresinin sonucu spec'e işlendi:
> - **AI artık duygusal kahraman DEĞİL** — kullanıcı kahraman. Welcome / ProfilePreview / AIDisclosure / SampleWorkout / son ekran reframe edildi (project-context §0 UX framing tablosu).
> - **Design system dark-first** — semantic token tablosu (dark default / light future), accent dark-kalibre, accent-soft → accent-glow.
> - **AIDisclosure** kahraman ekrandan → minimal, sakin uyumluluk ekranına indirildi.
> - **SampleWorkoutPreview** — "AI video" vurgusu kaldırıldı; antrenmanın kendisi kahraman; curated template (canlı AI değil).
> - **Yeni Ekran 28** — ProfessionalCoachingTeaser (Faza 2 demand-signal, "yakında" ekranı).
> - Konumlandırma: "uçtan uca antrenman yönetimi", AI destek katı.

---

## 0. Konumlandırma & Bu Belgenin Çerçevesi

> Bu bölüm `docs/project-context.md` **§0 Məhsul Mövqeləndirməsi**'nin UX'e bağlanmasıdır. Çelişki halinde project-context §0 + CLAUDE.md Repositioning bloku kanondur.

**fitnessApp "AI plan üreten uygulama" DEĞİLDİR.** Çekirdek vaat: kullanıcı kendi antrenman sürecini **uçtan uca** buradan yönetir — planlar, izler, ölçer. AI bir **destek katıdır** (plan önerisi için), headline değil.

**Bu, her ekran tasarımını bağlayan UX framing kuralıdır:**

| Element | ❌ AI-kahraman (v1.0 hatası) | ✅ Kullanıcı-kahraman (v1.1) |
|---------|------------------------------|-------------------------------|
| Welcome copy | "Sənə uyğun AI fitness planı" | "Öz məşqini idarə et" |
| ProfilePreview | "AI sənin üçün hesabladı" + AI sayaç tiyatrosu | "Səni belə tanıdıq — düzdürmü?" (cevap özeti + ölçülen hedefler) |
| AIDisclosure | Pırıltılı duygusal kahraman ekran | Minimal, sakin uyumluluk bilgisi + "kendin değiştirebilirsin" |
| SampleWorkout | "AI-generated video" vurgusu | Antrenmanın kendisi kahraman; hareket videosu (AI değil, içerik) |
| Son ekran / Home köprüsü | "Planın hazırdır — AI tərəfindən yaradılıb" | "Birinci məşqin hazırdır. Başlayaq?" |

**Scope (BMad party-mode 2026-05-21):** Bu spec yalnız **MVP** akışını tasarlar. Trainer review loop ve professional-direct coaching **Faza 2** — bu spec'te yalnız (a) Ekran 28 "yakında" teaser'ı ve (b) gelecek path-seçim kartına ayrılan UI iskeleti olarak görünür. MVP marketinqi/onboarding trainer özelliğini **vaat etmez**.

---

## 0b. Executive Summary

fitnessApp'in giriş kapısı tek bir vaadi taşır: **kullanıcı uygulamayı açtıktan ~90 saniye sonra ilk değerini görür** — kendi antrenmanını yönetebileceği bir aracı kalibre etmiş, opaque billing prompt'u olmadan, native AZ kalitesinde, güvenlik hard-stop'ları görünmeden koruyan bir akışta.

Dört tasarım problemi:

1. **Sürtünmesiz activation** — 7 ekran soru, ekran başına tek soru, her geçiş <200ms, ilerleme her an görünür. Hedef: median ≤90 sn.
2. **Trust gradient** — Para hiçbir onboarding ekranında konuşulmaz; değer önce gelir. BetterMe/Freeletics'in en büyük şikayet vektörünü yapısal kaldırır.
3. **Native AZ ilk izlenim** — İlk üç ekranda 0 machine-translated string.
4. **Görünmez güvenlik & uyumluluk** — Age gate, parental notice, AI disclosure, pregnancy nudge engel gibi değil, doğal adım gibi.

**Tasarım dili:** `onboarding_flow.pen` design system'i bu spec ile **dark-first** kanonikleşir — **Volt renk sistemi** (Ladder-ilhamlı elektrik-sarı accent `#E6FF00`, koyu taban `#0A0A0B` / `#141416` / `#1E1E21`, project-context §1b kanonik), Inter tipografi, 16/24px köşe yarıçapı. Sarı daşıyıcı renktir — bol kullanılır; splash tam-sarı. AI hiçbir ekranda kahraman değil — kullanıcı kahraman.

**Kapsam:** PRD §17.1'deki 28 ekran inventarından **26 build edilebilir ekran** bu spec'in kapsamıdır. Ekran 23 (Home) ve Ekran 24 (Settings) onboarding sonrası **scope dışı referans** ekranlardır — bu spec'te yalnızca akış bağlamı (köprü/giriş noktası) olarak görünürler, build edilmezler. `.pen` design dosyası bu 26-ekran kapsamına göre yeniden kurulmuştur.

---

## 1. Tasarım Prensipleri

### P1 — Ekran başına tek niyet
Her ekran tek bir "şimdi ne yapmalıyım" sorusu sorar. Onboarding'de: **bir ekran = bir soru**. Q4 ve Q7 iki alan içerir ama tek karar olduğu için tek ekran.

### P2 — ≤7 element kuralı + tradeoff
Default ekran ≤7 görsel element. Yeni element eklenince mevcut biri çıkarılır/gizlenir.

### P3 — Trust gradient
Taahhüt yavaş artar: ücretsiz değer → hesap → para. Billing prompt onboarding'in hiçbir noktasında görünmez (FR-12 / NFR-T1).

### P4 — Görünmez güvenlik
Age gate, parental notice, AI disclosure sakin, suçlamayan tonda — akışın doğal parçası.

### P5 — Native AZ kalitesi & kültürel duyarlılık
Her string manuel review (MT yasak). Female persona için modesty copy, female-only social proof.

### P6 — Kullanıcı kahraman, AI destek (v1.1 — repositioning)
Hiçbir ekran AI'ı duygusal kahraman yapmaz. Kullanıcı "ben yöneteceğim, bu araç benimle" hisseder — "ben yeterli değilim, iyi ki AI var" değil. AI sahne arkasında sessiz bir asistandır; UI'da, copy'de, animasyonda öne çıkarılmaz. project-context §0 + CLAUDE.md Qəti Qadağa.

---

## 2. Deneyim Hedefleri & Duygusal Yanıt

### 2.1 Deneyim hedefleri

| # | Hedef | UX karşılığı | Ölçüt (PRD §12) |
|---|-------|--------------|------------------|
| EX1 | Sürtünmeyi minimize et | Tek-soru ekranlar, büyük dokunma hedefleri | Median ≤90 sn |
| EX2 | Güven inşa et | Şeffaf dil, billing-free akış, privacy linkleri | Paywall öncesi value view ≥95% |
| EX3 | Profil kalitesi | Net soru, alt-bağlam etiketi | 7 alan doldurma ≥98% |
| EX4 | Platform-yerli his | iOS HIG + Android Material | iOS/Android %100 paritet |
| EX5 | Offline güvenlik | Her cevap anında lokal yazılır | Offline tamamlama ≥99% |
| EX6 | Sahiplik hissi (v1.1) | Kullanıcının cevapları ona geri yansıtılır; AI gölgede | "planı düzenle" etkileşim oranı (Faza 2 metrik) |

### 2.2 Duygusal yolculuk haritası (v1.1 — yeniden çizildi)

Eski yay AI'ı kahraman yapıyordu ("AI vay"). Yeni yay kullanıcının **yetkinlik ve sahiplik** duygusunu inşa eder:

```
Welcome        →  "Bu uygulama benim antrenmanım için, AZ dilinde, temiz"   [niyet: ben yöneteceğim]
Q1–Q7          →  "Beni anlatıyorum, gereksiz soru yok, hızlı"               [ifade: ben anlatıyorum]
ProfilePreview →  "Söylediklerimi ciddiye aldılar, beni tanıdılar"           [tanınma: dinlendim]
AIDisclosure   →  "Dürüstçe söylüyorlar, kontrol bende kalıyor"              [güven: sakin]
AuthGate       →  "Değeri gördüm, kaydolmaya değer"                          [istekli taahhüt]
SampleWorkout  →  "İşte bir antrenman — bunu ben yapacağım"                  [arzu: eylem]
Son ekran/Home →  "Birinci antrenmanım hazır, başlıyorum"                    [eylem: başlangıç]
Paywall        →  "Şeffaf, iki seçenek, iptal görünür — kandırılmıyorum"     [rahat karar]
```

**AI bu yayda hiçbir beat'in kahramanı değildir.** ProfilePreview'da kahraman, kullanıcının kendi cevapları; SampleWorkout'ta kahraman, antrenmanın kendisi.

**Anti-duygular:** panik (age gate), suçluluk (hard-stop), tuzağa düşmüşlük (paywall), yetersizlik ("iyi ki AI var"), kafa karışıklığı (sync hatası).

### 2.3 Ton & ses
- **Genel ton:** Sıcak ama yetkin — antrenör gibi, motive eder küçümsemez.
- **Persona uyarlaması (§6.2):** Female → işbirliği/ilerleme; male → takip/meydan okuma. Mikrocopy seviyesinde.
- **Hata tonu:** Suçlamaz, eylem önerir.
- **AI'dan bahsederken:** Sakin, şeffaf, billing-transparency dili ile aynı — "AI sehri" değil, "bu nasıl çalışıyor" dürüstlüğü.

---

## 3. Design System (Kanonik — Dark-First)

> Kaynak: `app/design/mobile/app_design.pen` (şu an light — Stage B'de dark'a taşınacak). Bu bölüm **dark mode'u default**, light'ı gelecek tema olarak sabitler. Architect token'ları `shared/` design-token katmanına; iOS `Color`/`Font` extension, Android `Theme.kt`. Token'lar **semantic** isimlendirilir — ham renk değil, rol.

### 3.1 Renk tokenleri — Volt sistemi (semantic, iki-tema)

> **Kanonik kaynak: `docs/project-context.md` §1b.** Renk değeri yalnız orada tanımlanır; bu tablo onun spec-içi kopyasıdır. Çelişki halinde §1b kanondur. Token adları **semantic** — komponentler ada bağlanır, ham renge değil. `accent` = `volt`, `on-accent` = `on-volt` (geriye uyumlu takma adlar; komponent referansları korunur).

| Token | **Dark (default)** | Light (gelecek) | Kullanım |
|-------|--------------------|-----------------|----------|
| `bg` | `#0A0A0B` | `#FFFFFF` | Ekran zemini (saf siyah değil — OLED halo azaltma) |
| `surface` | `#141416` | `#F4F5F7` | Kart, input alanı, bottom sheet, pasif segment |
| `surface-elevated` | `#1E1E21` | `#FFFFFF` | Modal, "bugünkü antrenman" tile, timer kartı — yükseltilmiş katman |
| `border` | `#2A2A2E` | `#E5E7EB` | Kart kenarı, ayraç, progress boş segment |
| `border-strong` | `#3E3E44` | `#CBD0D8` | Seçilmemiş checkbox/radio ring |
| `accent` (`volt`) | `#E6FF00` | `#E6FF00` | Primary CTA, splash zemini, logo, seçili dolgu, progress dolu, aktif durum, link, vurgu — **taşıyıcı renk, bol kullanılır** |
| `accent-pressed` (`volt-pressed`) | `#C9E000` | `#C9E000` | Basılı buton durumu |
| `accent-glow` (`volt-glow`) | `rgba(230,255,0,0.12)` | `#F4FFB0` | Seçili kart zemini, hero glow, banner dolgusu — koyu üstünde volt parıltı |
| `on-accent` (`on-volt`) | `#0E0E0E` (**siyah**) | `#0E0E0E` | `accent` zemin üzeri metin/ikon — **her zaman siyah** |
| `moss` | `#A4B82B` | `#7E8C2E` | Dekoratif ikon, ikincil outline, grafik ikincil çizgi — **CTA OLMAZ** |
| `moss-dim` | `#5C6B1A` | `#A8B36B` | Çok sakin dekoratif stroke, disabled-accent |
| `text-primary` (`ink`) | `#FFFFFF` | `#0E0E0E` | Birincil metin, başlık |
| `text-secondary` (`ink-muted`) | `#A1A1A8` | `#5B6068` | İkincil metin, alt-açıklama, label |
| `text-tertiary` | `#6A6A72` | `#9AA0AC` | Placeholder, pasif, yardımcı metin |
| `success` | `#3DD68C` | `#1FA85A` | Onay, sync tick, streak korundu, **yeşil chip'ler** |
| `warning` | `#FFB020` | `#B97400` | Uyarı banner, offline durumu |
| `danger` | `#FF4D4D` | `#DC2626` | Destructive aksiyon, inline error |
| `danger-soft` | `rgba(255,77,77,0.12)` | `#FEECEC` | Destructive onay modalı zemini |

**Kontrast doğrulaması (NFR-A4, WCAG — dark):**
- `accent`/`bg` ≈ 17.6:1 ✅ AAA · `on-accent`(siyah)/`accent` ≈ 17:1 ✅ AAA · `text-primary`/`bg` ≈ 19.8:1 ✅ AAA · `text-secondary`/`bg` ≈ 7.6:1 ✅ AAA · `success`/`bg` ≈ 9:1 ✅
- **Kural:** `accent` üzerindeki metin **her zaman siyah** (`on-accent`), beyaz asla. `accent` küçük gövde metni olarak kullanılmaz — yalnız büyük metin / UI elemanı / dolgu. `moss` asla buton/CTA değil — yalnız dekoratif.
- **Sarı doktrini (project-context §1b):** Sarı taşıyıcı renktir, rasyonlanmaz — CTA/seçili/progress/aktif/link her yerde. Splash tam-sarı (`accent` zemin + `on-accent` logo). Okunan koyu ekranlarda taban koyu kalır; tam-sarı flood yalnız splash + kutlama anları.

**Dark mode derinlik prensibi:** Dark mode hiyerarşiyi gölge ile değil **elevation katmanları** ile anlatır: `bg` (en arka) → `surface` (kartlar) → `surface-elevated` (modal, tile, timer kartı). Minimal Design ≤7 element kuralı dark'ta avantaja döner — az element, net elevation farkı.

### 3.2 Tipografi

**Font:** Inter (tek aile, AZ diakritikleri tam). Status bar OS native (iOS SF Pro / Android sistem).

| Stil | Boyut | Ağırlık | Satır yük. | Kullanım |
|------|-------|---------|------------|----------|
| `display` | 28 | 700 | 1.2 | Welcome başlığı |
| `title` | 25 | 700 | 1.25 | **Tüm soru ekranı başlıkları** (uygulama geneli sabit) |
| `headline` | 22 | 700 | 1.3 | ProfilePreview metrik değerleri, kart başlıkları |
| `body` | 15 | 400 | 1.45 | Açıklama paragrafları |
| `body-strong` | 16 | 600 | 1.4 | Seçim kartı başlığı |
| `label` | 13 | 500 | 1.4 | Alt-açıklama, input label, kart sub-metni |
| `caption` | 12 | 700 | 1.35 | Step etiketi, disclosure metni |
| `cta` | 17 | 600 | 1.0 | Buton etiketi |

**Dynamic Type (NFR-A3):** %85–%130 desteklenir; buton/kart yükseklikleri `fit_content` ile esner.

### 3.3 Boşluk, yarıçap, gölge

**Boşluk skalası:** 4 · 8 · 12 · 16 · 20 · 24 · 28 · 32 · 44

| Token | Değer | Kullanım |
|-------|-------|----------|
| Ekran yatay padding | 28 | Content wrapper (tek seviye) |
| Bölüm dikey gap | 24 | Header–içerik–CTA arası |
| İlişkili öğe gap | 12 | Seçim/alan kartları arası |
| Sıkı gap | 8 | Başlık–alt-açıklama, ikon-metin |
| İçerik alt padding | 44 | CTA'nın home indicator'dan ayrılması |

**Yarıçap:** `radius-lg` 24 · `radius-md` 16 · `radius-sm` 12 · `radius-round` 50%.

**Gölge / elevation:** Dark mode gölge yerine **elevation rengiyle** çalışır — `surface` ve `surface-elevated` ton farkı katmanı anlatır. Bottom sheet / modal hafif bir gölge alır (`offset(0,−4) blur 32 color #00000066`) + üst kenar `border` ile ayrışır. Dark'ta gölge tek başına yetersiz — renk farkı asıl ayraçtır.

### 3.4 İkonografi

- **Kütüphane:** `lucide` (tek set), 18–22px, stroke.
- **Eşlemeler:** goal → `trending-down`/`dumbbell`/`heart`; gender → `user`; body → `calendar-days`/`ruler`/`scale`; level → `sprout`/`activity`/`award`; context → `building-2` (serious_gym) / `dumbbell` (casual_gym) / `house` (home_only); sistem → `chevron-left`/`check`/`minus`/`plus`/`flame`/`timer`/`eye`/`eye-off`/`alert-triangle`/`trash-2`/`globe`/`info`/`mail`/`award`.
- **AI ikonu (`sparkles`) kullanımı kısıtlı** — yalnız AIDisclosure'da, küçük ve sakin; başka hiçbir ekranda AI'ı işaret eden ikon yok (repositioning).
- **`.pen` gotcha'ları:** `lucide`'da `clock` yok → `timer`; status bar fontu OS native.

### 3.5 Komponent kütüphanesi

`.pen`'de 3 reusable tanımlı (`comp/statusbar`, `comp/btn-primary`, `comp/option`); kalanı bu spec tanımlar. **Tüm komponentler dark token'larla yeniden kurulur** (Stage B).

| Komponent | .pen | Anatomi & dark notu |
|-----------|------|---------------------|
| **StatusBar** | ✅ | 62px, OS chrome. Dark'ta açık ikon/metin. |
| **PrimaryButton** | ✅ | 58px, `accent` zemin, `radius-md`, `on-accent` (siyah) 17/600 etiket. Durumlar: default · pressed (`accent-pressed`) · disabled (`surface` zemin + `text-secondary`) · loading (spinner). |
| **SecondaryButton** | ✗ | `bg` zemin + `accent` 2px stroke + `accent` etiket. "Sonra", "Ləğv et". |
| **DestructiveButton** | ✗ | `danger` zemin + `on-accent` etiket VEYA text-only `danger`. Yalnız hesap sil / "Sil və çıx". Primary'den görsel ayrık. |
| **RadioCard** (`comp/option`) | ✅ | 76px min, ikon kutusu (44px) + title `body-strong` + sub `label` + `check`. Default: `surface` zemin. **Selected: `accent-glow` zemin (koyu üstünde volt parıltı), `accent` 2px stroke, ikon+check `accent`.** Pressed opacity 0.9 · disabled 0.5. |
| **SegmentedControl** | ✗ | `surface` track, eşit `fill_container` segment. Seçili: `surface-elevated` zemin + `text-primary`; pasif `text-secondary`. Q7. |
| **StepperField** | ✗ | Yatay kart: ikon kutusu + label/değer + `−`/`+` (36px; `+` accent dolu, `−` `surface-elevated`). Q4. |
| **WheelPicker** | ✗ | iOS `UIPickerView` / Android Material picker. Q3 yaş. Free-text yasak. |
| **UnitToggle** | ✗ | İki-segment mini control (cm/ft · kg/lbs). Backend hep cm+kg. |
| **TextInputField** | ✗ | 56px, `surface` zemin, label üstte, focus'ta `accent` 2px stroke. Parolada `eye`/`eye-off`. |
| **InlineError** | ✗ | Alan altında `alert-triangle` 14px + `danger` `label`. Kodlar PRD §8.5. |
| **Banner** | ✗ | Ekran üstü; `accent-glow`/`danger-soft` zemin, ikon + metin + opsiyonel aksiyon. AUTH_005, offline. |
| **BottomSheet** | ✗ | `surface-elevated` panel, 36px drag-handle, `radius-lg` üst köşe, `#000000A6` scrim. Pregnancy nudge, soft paywall. |
| **BlockingModal** | ✗ | `surface-elevated` kart, scrim, dışarı dokunma kapatmaz. 2–3 dikey CTA. |
| **TerminalScreen** | ✗ | Tam ekran, geri yok, tek CTA. Age gate. Sakin ton. |
| **ProgressIndicator** | ✅ | 7 segment, 5px yükseklik, `radius` 3. Dolu `accent`, boş `border`. |
| **LinkText** | ✗ | `accent` renk, alt-çizgisiz, 44px etkin hedef. |
| **LegalDisclaimer** | ✗ | `caption`, `text-secondary`, italik. |
| **InfoNotice** (v1.1 yeni) | ✗ | Sakin bilgi kartı — `surface` zemin, küçük `info`/`sparkles` ikon + `caption` metin. AIDisclosure ve "bu nasıl çalışıyor" notları için. Kahraman değil, dipnot. |
| **HeroBlock** | ✅ | `accent-glow` zemin (koyu üstünde volt parıltı), `radius-lg`, ortada `accent` daire + ikon. Welcome'da. |

### 3.6 Dokunma hedefi
İkon kutuları 44×44px. 36px butonlar görünmez padding ile 44px+. LinkText 44px etkin alan.

---

## 4. Görsel Temel & Layout Sistemi

### 4.1 Ekran iskeleti
StatusBar (62px, OS) → Content Wrapper (tek dikey kolon, yatay padding 28, `justifyContent: space_between`, alt padding 44). **Onboarding/auth'ta bottom tab bar YOK.** CTA içeriğin en altında.

### 4.2 Referans cihaz & breakpoint
- **Referans:** 375×812. `fill_container`/`fit_content` ile responsive.
- **Küçük (≤375×667):** İçerik scroll'a düşebilir; CTA sticky footer.
- **Büyük (≥430):** Content wrapper max 480px ortalı.
- **Tablet:** MVP'de optimize edilmez.

### 4.3 Safe area & scroll
Status bar inset 62px, home indicator inset 44px padding'e dahil. Soru ekranları scroll'suz; Dynamic Type %130'da tek dikey scroll + CTA sticky. Klavye açılınca içerik yukarı iter.

---

## 5. Bilgi Mimarisi & Navigasyon

### 5.1 Akış haritası

```
[Cold Start]
   ├ session geçerli ──────────────────────────→ [Home]
   └ session yok
       ↓
   (1) LanguagePicker ──→ (2) Welcome
       ↓
   (3) Q1 Goal → (4) Q2 Gender → (5) Q3 Age
                                     ├ <13 ──→ (6) AgeGateBlocked  [TERMINAL]
                                     ├ 13–17 ─→ (7) ParentalNotice ─┐
                                     └ ≥18 ───────────────────────┐│
                                                                  ↓↓
   (8) Q4 HeightWeight → (9) Q5 Experience → (10) Q6 Context → (11) Q7 DaysSession
       ↓
   (12) ProfilePreview → (13) AIDisclosure → (14) AuthGate
                                                ├ Email ─→ (15) EmailSignupForm → (17) EmailConfirmationPending
                                                │          (16) EmailLoginForm  (returning)
                                                │          (18) PasswordResetEmail → (19) PasswordResetForm
                                                ├ Google ─┐
                                                └ Apple ──┤
                                                          ↓
  [SyncOnboardingToProfile] (görünmez, arka plan — ONB_004 sync-fail = sessiz background retry, UI ilerler)
       ↓
   (20) PregnancyNudge (F-only) → (21) SampleWorkoutPreview
                                       ├ "Tam planı al" ─→ (22) Paywall
                                       └ "Sonra" ────────→ [Home (23)]

  [Settings — referans] (24)
       ├ Logout ──→ (25) LogoutSyncModal
       ├ Hesabı sil ─→ (26) DeleteAccountConfirm-1 → (27) DeleteAccountConfirm-2
       └ "Peşəkar məşqçi" ─→ (28) ProfessionalCoachingTeaser  [Faza 2 demand-signal]
```

### 5.2 Navigasyon kuralları
- **İleri:** Primary CTA. Soru ekranlarında seçim yapılana kadar disabled.
- **Geri:** Header `chevron-left`; önceki cevap korunur. Q1'de geri = Welcome.
- **Geri kısıtları:** AIDisclosure'dan geri YASAK (system back disabled). AgeGateBlocked terminal. ParentalNotice'tan geri = Q3. AuthGate'ten geri YOK.
- **Atlama:** Onboarding sorularında YOK. AIDisclosure skip YOK (ama minimal). PregnancyNudge ve SampleWorkout "Sonra" ile atlanabilir.
- **Path seçimi (Faza 2 hazırlığı):** "Program nasıl oluşturulsun" seçimi onboarding'e GİRMEZ — onboarding sonrası ilk program ekranında kart listesi (MVP: AI plan + Manuel; Faza 2: 3. kart "Trainer ile çalış"). Bu spec'in kapsamı dışı, sonraki PRD.

---

## 6. Ekran-Ekran UX Spesifikasyonları (26 build edilebilir + 2 referans)

> **Kapsam notu:** Ekran 1–22 ve 25–28 = **26 build edilebilir ekran** (bu spec'in tasarım kapsamı). Ekran 23 (Home) ve 24 (Settings) **scope dışı referans** — akış bütünlüğü için listelenir, build edilmez; tam tasarımları sonraki PRD'lerde.

> Her ekran: **Amaç · Layout · Komponentler · Durumlar · Copy referansı · Hareket · Telemetri · Erişilebilirlik · Edge.** Copy'nin AZ/RU/EN tam metni PRD §3 + §9'da — burada `copy-key` ve **v1.1 reframe** notuyla.

### Ekran 1 — LanguagePicker
**Amaç:** İlk açılışta dil seçimi (Q0). PRD US-2.0.
**Layout:** Header yok. Title "Dilini seç". 3 RadioCard — Azərbaycanca / Русский / English (dil kendi adıyla). Locale `az_AZ` → AZ önceden seçili. CTA PrimaryButton "Davam et" (her zaman aktif).
**Durumlar:** default · pressed · selected. Offline'da içerik aynı (lokal, ağ gerekmez).
**Copy:** `onboarding.q0.title`.
**Hareket:** Kart seçimi 150ms. CTA → Welcome slide-in 200ms.
**Telemetri:** `language_selected { language, system_locale_match }`.
**Erişilebilirlik:** `radiogroup`/`radio`; dil adları kendi dilinde okunur.
**Edge:** Settings'ten dil değişimi runtime swap, snackbar "Dil dəyişdirildi".

### Ekran 2 — Welcome
**Amaç:** Marka ilk izlenimi + privacy şeffaflığı. PRD §3.3.
**Layout (`.pen` 01-welcome, dark'a taşınacak):** Header — `accent` dolu 34px kare (`flame`) + wordmark (**marka adı açık soru UX-10 — `.pen`'de "FitAz" placeholder; CLAUDE.md/PRD "fitnessApp" kullanır; karar verilene kadar wordmark placeholder**). HeroBlock `accent-glow` zemin (koyu üstünde volt parıltı) + `accent` daire + `dumbbell` ikonu. Alt blok (ortalı): `display` başlık + `body` alt-metin + PrimaryButton "Başla" + "Hesabın var? **Daxil ol**" LinkText. En altta 3 LinkText: Privacy / Terms / Health.
**🔄 v1.1 reframe:** Başlık "Sənə uyğun AI fitness planı" → **"Öz məşqini idarə et"** (veya "Antrenmanın, tam sənin nəzarətində"). Alt-metin "AI sənin üçün plan qurur" değil → **"Planla, izlə, ölç — hamısı bir yerdə."** **AI kelimesi Welcome'da geçmez.** Görsel vurgu: bir kişinin antrenmanını yönetmesi/kaydetmesi.
**Durumlar:** default. Offline'da içerik aynı.
**Copy:** `welcome.title/subtitle/cta_start/login_link`, `legal.*`.
**Hareket:** Hero ikonu scale-in (0.9→1.0, 300ms). "Başla" → Q1 slide-in.
**Telemetri:** `welcome_viewed`, `privacy_link_tapped { which }`.
**Erişilebilirlik:** Hero dekoratif. Başlık `header`. Linkler ayrı hedefler.
**Edge:** "Daxil ol" → doğrudan AuthGate login modu.

### Ekran 3 — Q1: Əsas hədəf (Goal)
**Amaç:** `goal` enum `bulk|cut|general_fit`. PRD §3.1.1.
**Layout (`.pen` 02-goal — kanonik soru şablonu):** Header — geri butonu + ProgressIndicator (7 segment, 1 dolu). Step "SUAL 1 / 7". Title "Əsas hədəfin nədir?". Alt-açıklama "Məşqini bu hədəfə görə qururuq." 3 RadioCard: `trending-down` Çəki itirmək / `dumbbell` Əzələ qazanmaq / `heart` Formada qalmaq. CTA "Davam et" — seçim yapılana kadar disabled.
**🔄 v1.1 reframe:** Alt-açıklama özne kullanıcı — "AI hədəfini öğrensin" değil, "**məşqini** bu hədəfə görə qururuq" (uygulama kullanıcının söylediğini ciddiye alıyor). `.pen`'deki 4. seçenek ("Güc artırmaq") **kaldırılır** — PRD enum 3 değer.
**Durumlar:** CTA disabled→enabled · kart selected.
**Copy:** `onboarding.q1.*`.
**Hareket:** Kart seçimi 150ms (radyo). CTA enable fade.
**Telemetri:** `onboarding_q1_viewed/answered { value }`; ayrıca `onboarding_started`.
**Erişilebilirlik:** `radiogroup`; progress "1/7".
**Edge:** Default seçim yok. Geri = Welcome.

### Ekran 4 — Q2: Cins (Gender)
**Amaç:** `gender` enum `male|female`. PRD §3.1.2.
**Layout:** Q1 şablonu — Progress 2, "SUAL 2 / 7". Title "Cinsin". Alt-açıklama "Kalori və protein hesabı üçün lazımdır." 2 RadioCard: Qadın / Kişi (`user`).
**Copy:** `onboarding.q2.*`. **Telemetri:** `onboarding_q2_answered { value }`.
**Edge:** Female seçimi → akışta sonra PregnancyNudge'ı tetikler.

### Ekran 5 — Q3: Yaş (Age) + Age Gate
**Amaç:** `age` int, Age Gate tetikleyici. PRD §3.1.3.
**Layout:** Q1 şablonu — Progress 3, "SUAL 3 / 7". Title "Yaşın neçədir?". Alt-açıklama "Yaş kalori hesabımıza təsir edir." WheelPicker (13–99). CTA "Davam et".
**Durumlar:** default · `>99` soft-warning Banner.
**Copy:** `onboarding.q3.*`, hata `ONB_001`.
**Hareket:** `<13` → hard-stop ekranına **fade** (slide değil).
**Telemetri:** `onboarding_q3_answered { age_bucket }`.
**Erişilebilirlik:** Wheel `adjustable`.
**Edge:** `<13` Ekran 6, `13–17` Ekran 7, `≥18` Ekran 8.

### Ekran 6 — AgeGateBlocked (TerminalScreen)
**Amaç:** `<13` hard-stop. PRD §3.6.1.
**Layout:** TerminalScreen. Ortada nötr ikon (`info` — `text-secondary`, kırmızı YOK). Title (ortalı). Sakin alt-metin. Tek CTA SecondaryButton "Çıxış".
**Durumlar:** Terminal — geri yok, retry yok. Tek durum (client `<13` ya da sunucu `AUTH_012` tetiklemesi — görünüm aynı).
**Copy:** `agegate.blocked.*`. Hata kodları: client tetik `ONB_001`'in `<13` dalı + sunucu tetik `AUTH_012`.
**Hareket:** Önceki ekrandan fade 250ms.
**Telemetri:** `age_gate_blocked { trigger: under_13, attempt }`.
**Erişilebilirlik:** Açılışta title `announce`.
**Edge:** Email/device hash 30 gün cache (§3.6.3).

### Ekran 7 — ParentalNotice (13–17)
**Amaç:** Ebeveyn izni. PRD §3.6.2.
**Layout:** Header geri (→ Q3). Title "Valideyn icazəsi tələb olunur". `body` açıklama. Checkbox satırı. 2 LinkText. CTA "Davam et" — checkbox işaretlenene kadar disabled.
**Durumlar:** unchecked → checked. Hata `ONB_005`.
**Copy:** `parental.*`. **Telemetri:** `parental_notice_viewed/accepted`.
**Erişilebilirlik:** Checkbox `checkbox` rolü.
**Edge:** Onay `parental_consent_pending=true`.

### Ekran 8 — Q4: Boy + Çəki (HeightWeight)
**Amaç:** `height_cm` + `weight_kg`. PRD §3.1.4.
**Layout:** Q1 şablonu — Progress 4, "SUAL 4 / 7". Title "Boyun və çəkin". Alt-açıklama "BMR və TDEE-ni hesablayacağıq." UnitToggle. 2 StepperField (ikon + label + değer + `−`/`+`). CTA "Davam et".
**Durumlar:** default (170cm/70kg) · out-of-range soft-warning. Hata `ONB_002`.
**Copy:** `onboarding.q4.*`.
**Hareket:** Stepper 100ms sayaç; basılı tutma hızlanır.
**Telemetri:** `onboarding_q4_answered { height_bucket, weight_bucket }`.
**Edge:** Backend hep cm+kg.

### Ekran 9 — Q5: Təcrübə (Experience)
**Amaç:** `experience_level` enum. PRD §3.1.5.
**Layout:** Q1 şablonu — Progress 5, "SUAL 5 / 7". Title "Təcrübə səviyyən". Alt-açıklama "Çətinlik dərəcəsini buna görə seçirik." 3 RadioCard: `sprout`/`activity`/`award`, sub-context'lerle.
**🔄 v1.1 reframe:** Alt-açıklama "AI load.type seçər" → **"Çətinlik dərəcəsini buna görə seçirik"** (AI'a referans yok).
**Copy:** `onboarding.q5.*`. **Telemetri:** `onboarding_q5_answered`.
**Edge:** RadioCard `fit_content` (uzun sub-context).

### Ekran 10 — Q6: Kontekst (Context)
**Amaç:** `context` enum. PRD §3.1.6.
**Layout:** Q1 şablonu — Progress 6, "SUAL 6 / 7". Title "Harada məşq edəcəksən?". Alt-açıklama "Hərəkətləri buna görə uyğunlaşdırırıq." 3 RadioCard: `building-2` Ciddi zal / `dumbbell` Adi zal / `house` Yalnız evdə (§3.4 ikon eşlemesi).
**Copy:** `onboarding.q6.*`. **Telemetri:** `onboarding_q6_answered`.
**Edge:** `home_only` → implicit `equipment_inventory=["bodyweight"]`.

### Ekran 11 — Q7: Həftəlik gün × sessiya (DaysSession)
**Amaç:** `weekly_days` + `session_duration_min`. PRD §3.1.7.
**Layout:** Q1 şablonu — Progress 7 (tam), "SUAL 7 / 7". Title. Alt-açıklama "Həftəlik proqramı buna görə qururuq." 2 SegmentedControl. CTA "Davam et".
**Copy:** `onboarding.q7.*`.
**Hareket:** Segment 180ms spring.
**Telemetri:** `onboarding_q7_answered`, ardından `onboarding_completed { duration_seconds, persona_cell }`.
**Edge:** Q7 submit → local BMR/TDEE compute.

### Ekran 12 — ProfilePreview
**Amaç:** Auth'tan ÖNCE değer göster — kullanıcının tanındığı an. PRD §3.4.
**🔄 v1.1 reframe (en kritik değişiklik):** Eski versiyon "AI sənin üçün hesabladı" + AI sayaç tiyatrosuydu. Yeni versiyon kahramanı **kullanıcının kendi cevapları** yapar.
**Layout:** Header — geri (→ Q7), Progress yok. Title **"Səni belə tanıdıq"** (`title`).
- **Cevap özeti kartı (yeni):** kullanıcının 7 cevabı `surface` kart içinde özetlenir — "Hədəf: Çəki itirmək · Səviyyə: Başlanğıc · Həftədə 3 gün · Evdə". Bu kullanıcının kendi seçimleri, ona geri yansıtılıyor. Alt satır LinkText "Düzdürmü? Dəyiş" (düzeltme imkanı).
- **Ölçülen hedefler:** 3 metrik kartı — Gündəlik kalori `~XXXX kcal` · Protein `~XX–XX q` · Su `X.X L`. Bunlar AI değil — Mifflin-St Jeor lokal formül; "ölç" vaadinin parçası. İkon + `headline` değer + `label` etiket.
- **LegalDisclaimer:** "Bu rəqəmlər təxminidir, tibbi məsləhət deyil. Sağlamlıq probleminiz varsa həkimə müraciət edin."
- CTA PrimaryButton "Hesabımı yarat".
**🔄 Hareket:** AI sayaç tiyatrosu **kaldırıldı**. Yerine: kartların yumuşak fade-in'i (200ms, kademeli) — "hesaplanıyor" değil "işte sen" hissi. Metrik değerleri animasyonsuz, doğrudan görünür.
**Durumlar:** default · compute-fail fallback (TDEE=2000, sessizce). Offline'da içerik aynı — BMR/TDEE lokal (`shared/calc/`), ağ gerekmez.
**Copy:** `profile_preview.*`, `legal.medical_disclaimer`.
**Telemetri:** `profile_preview_shown { kcal_bucket, protein_bucket, water_bucket, persona_cell }`.
**Erişilebilirlik:** Cevap özeti ve metrikler sırayla okunur. Disclaimer atlanmaz.
**Edge:** Activation milestone — hesap açmadan değer + tanınma.

### Ekran 13 — AIDisclosure (v1.1 — minimal)
**Amaç:** Apple 2025 zorunlu AI açıklaması — yasal, ama **kahraman değil**. PRD §3.5 / FR-20.
**🔄 v1.1 reframe:** Eski versiyon pırıltılı duygusal kahraman ekrandı. Yeni versiyon **minimal, sakin uyumluluk ekranı** — bir özellik vitrini değil, dürüst bir bilgilendirme.
**Layout:** Header — geri YOK (system back disabled). Üstte küçük, sakin `sparkles` ikonu (animasyonsuz, `text-secondary` tonunda — kahraman renk değil). Title **"Planın necə hazırlanır"** (`title` — "AI" kelimesini başlığa koymadan). InfoNotice komponenti içinde gövde: "Məşq planının bir hissəsi sənin cavablarına əsasən süni intellekt köməyi ilə hazırlanır. **Plan həmişə sənindir — istədiyin vaxt özün dəyişə bilərsən.** Bu, tibbi məsləhət deyil." CTA-1 PrimaryButton "Anladım". CTA-2 LinkText "Ətraflı" → AI usage policy.
**Durumlar:** Tek durum — dismissable değil, skip yok. **Animasyon yok, pırıltı yok.**
**Copy:** `ai_disclosure.*` — "kendin değiştirebilirsin" cümlesi kontrolü kullanıcıya verir (repositioning'i destekler).
**Hareket:** Geçiş sade fade-in. İkonda animasyon YOK.
**Telemetri:** `ai_disclosure_viewed`, `ai_disclosure_accepted { duration_ms }`.
**Erişilebilirlik:** Geri jest devre dışı — screen reader'a bildirilir. Metin tam okunur.
**Edge:** Uygulama kapatılıp açılırsa tekrar gösterilir. Kabul → `ai_disclosure_accepted_at`. **Not:** Legal review "ayrı atlanamaz ekran" şart koşmazsa, bu ekran ProfilePreview'in alt InfoNotice'ına gömülebilir — açık UX sorusu UX-3'e bakınız.

### Ekran 14 — AuthGate
**Amaç:** Hesap oluşturma/giriş yöntemi. PRD §3.4.
**Layout:** Header — geri YOK. Title "Hesabını yarat". Alt-açıklama "Məlumatların təhlükəsiz saxlanılır." Provider butonları: **iOS** — Apple #1, Google #2, "Email ilə davam et" #3. **Android** — Google #1, Email #2 (Apple yok). Altta "Hesabın var? **Daxil ol**". En altta Privacy/Terms.
**Durumlar:** default · provider loading (buton spinner) · disabled (loading sırasında diğer butonlar) · offline (üst Banner — provider butonları pasif) · hata: `AUTH_007` (network) toast · `AUTH_009` (Google fail) toast · `AUTH_011` (Apple fail) toast · `AUTH_008`/`AUTH_010` (Google/Apple cancel) sessiz, durum değişmez · `AUTH_012` (server age gate) → Ekran 6'ya yönlendirir.
**Copy:** `auth.gate.*`.
**Telemetri:** `auth_gate_viewed { platform }`, `auth_provider_selected/cancelled`.
**Erişilebilirlik:** Apple butonu sistem komponenti.
**Edge:** Cancel sessiz (`AUTH_008`/`AUTH_010`). `is_new_user=false` → Home. Sunucu age-gate red'i (`AUTH_012`) sosyal sign-in sonrası tetiklenebilir → AgeGateBlocked (Ekran 6).

### Ekran 15 — EmailSignupForm
**Amaç:** Email+parol kayıt. PRD US-1.1.
**Layout:** Header geri. Title "Email ilə qeydiyyat". 3 TextInputField: Email · Parol (eye) · Təsdiq. Parola gücü ipucu. Privacy LinkText. CTA "Qeydiyyatdan keç".
**Durumlar:** default · focus · CTA disabled (alanlar boş/geçersiz) · InlineError (`AUTH_001/002/003`) · CTA loading · `AUTH_006` BlockingModal · offline (CTA pasif + Banner) · `AUTH_007` toast.
**Copy:** `auth.signup.*`.
**Hareket:** InlineError 150ms açılır. Klavye açılınca içerik yukarı iter.
**Telemetri:** `auth_signup_started/completed/error`.
**Edge:** Başarı → EmailConfirmationPending. `AUTH_003` → login CTA.

### Ekran 16 — EmailLoginForm
**Amaç:** Returning user girişi. PRD US-1.4.
**Layout:** 2 TextInputField. Title "Daxil ol". "Parolu unutdum" LinkText. CTA "Daxil ol".
**Durumlar:** default · focus · CTA disabled (alanlar boş) · CTA loading · `AUTH_001` InlineError · `AUTH_004` InlineError · `AUTH_005` Banner + "Linki yenidən göndər" · `AUTH_006` BlockingModal · offline (CTA pasif + Banner) · `AUTH_007` toast.
**Copy:** `auth.login.*`.
**Edge:** `user_profiles` eksikse → onboarding Q1.

### Ekran 17 — EmailConfirmationPending
**Amaç:** Email onay bekleme. PRD US-1.1.
**Layout:** Üstte `mail` ikonu `accent-glow` daire. Title "Email-ini təsdiqlə". Gövde. CTA-1 SecondaryButton "Linki yenidən göndər" (60 sn cooldown). CTA-2 LinkText "Email-i dəyiş".
**Durumlar:** default · cooldown sayaç (resend disabled 60 sn) · resend loading · resend toast (başarı) · offline (resend butonu pasif + üst Banner) · `AUTH_007` toast.
**Copy:** `auth.confirm_pending.*`.
**Telemetri:** `auth_confirmation_resent`.
**Edge:** Deep link `://confirm-email` (24h).

### Ekran 18 — PasswordResetEmail
**Amaç:** Parola sıfırlama — email. PRD US-1.6.
**Layout:** Header geri. Title "Parolu unutdum". 1 TextInputField. CTA "Linki göndər".
**Durumlar:** default · CTA disabled (email boş/geçersiz) · CTA loading · sabit güvenli mesaj (enumeration koruması — başarı/hata aynı metin) · `AUTH_001` InlineError · offline (CTA pasif + Banner) · `AUTH_007` toast.
**Copy:** `auth.reset_request.*`.
**Telemetri:** `password_reset_requested`.

### Ekran 19 — PasswordResetForm (deep link)
**Amaç:** Yeni parola. PRD US-1.6.
**Layout:** Title "Yeni parol təyin et". 2 TextInputField. CTA "Parolu yenilə".
**Durumlar:** default · focus · CTA disabled (alanlar boş) · CTA loading · `AUTH_002` InlineError (zayıf parol) · uyuşmazlık InlineError · `AUTH_013` InlineError + "Yeni link" CTA (link süresi doldu) · offline (CTA pasif + Banner) · `AUTH_007` toast.
**Copy:** `auth.reset_form.*`.
**Telemetri:** `password_reset_completed`.

### Ekran 20 — PregnancyNudge (BottomSheet, F-only)
**Amaç:** Hamilelik/postpartum opt-in. PRD §3.8.
**Layout:** BottomSheet (`surface-elevated`) — Home'a geçişten 1500ms sonra. Drag-handle. Nötr ikon. Title "Hamiləliyini bizə bildir". Gövde. CTA-1 "Hə, qeyd et". CTA-2 "Xeyr / Sonra".
**Durumlar:** Tek durum, dismissable.
**Copy:** `pregnancy_nudge.*`.
**Hareket:** Sheet 280ms spring.
**Telemetri:** `pregnancy_nudge_shown/response`.
**Edge:** Yalnız F user. "Hə" → `pregnancy_postpartum=true` → §6.3 hard-stop pipeline.

### Ekran 21 — ~~SampleWorkoutPreview~~ ⛔ DEPRECATED (2026-05-23)

> ⛔ **Bu ekran onboarding axınından çıxarıldı (2026-05-23).** İlk-dəyər təqdimatı **post-dashboard** olur (istifadəçi dashboard-a girdikdən sonra), onboarding-də deyil. Paywall da onboarding-də göstərilmir — dashboard-dan sonra trigger olunur (feature lock, foto-kalori cap, plan-regen cap və s.). `prd-sample-workout-preview-deferred-2026-05-22.md` faza-2 üçün arxivlənir; konkret yeri (dashboard-da hansı kart, hansı sürət) sonrakı PRD-də.


**Amaç:** İlk değer — paywall öncesi örnek antrenman. PRD §3.9.
**🔄 v1.1 reframe:** Eski versiyon "AI-generated video"yu satıyordu. Yeni versiyonda **kahraman antrenmanın kendisi** — kullanıcının yapacağı somut iş. AI'a referans yok.
**Layout:** Header — geri yok; kapatma `×` → "Sonra". Üst alan: **hareket videosu** otomatik oynatma (ses kapalı) — "AI video" değil, "hərəkət nümayişi". Yüklenemezse GIF fallback. Exercise adı (`headline`). Detay "3 set × 10 təkrar × 60 san dincəlmə". "Texnika məsləhətləri" 3 bullet, "Səhv / Düzgün" kontrastı. CTA-1 PrimaryButton "Tam planı al" → Paywall. CTA-2 SecondaryButton "Sonra" → Home.
**🔄 İçerik kararı:** Örnek antrenman **canlı AI çıktısı DEĞİL** — `exercises.is_first_value=true` flag'li, goal+context'e göre seçilmiş **curated template** (runtime AI maliyeti 0, offline-güvenli — Karar 3).
**Durumlar:** default · video yükleniyor · video fail (GIF) · 30 sn engagement → soft-paywall BottomSheet.
**Copy:** `sample_workout.*` — "AI sənə hazırladı" YOK.
**Hareket:** Video fade-in. 30 sn sonra soft paywall yumuşak yükselir.
**Telemetri:** `sample_workout_shown { exercise_id, persona_cell }`, `sample_workout_engaged`, `sample_workout_skipped`.
**Edge:** Persona_cell'e göre seçim (home→bodyweight, casual→goblet, serious→barbell squat).

### Ekran 22 — Paywall (post-dashboard touchpoint — 2026-05-23 yenilənmə)

> ⚠️ **Position dəyişdi (2026-05-23):** Paywall artıq onboarding axınında **GÖSTƏRİLMİR**. Post-dashboard tetiklər: (a) feature lock (premium məşq açılışı), (b) foto-kalori 5/gün capi keçildi, (c) plan re-gen aylıq cap keçildi, (d) settings → "Premium-a keç". Aşağıdakı spec bu touchpoint-lər üçündür. Onboarding boyunca billing prompt görünməz qaydası dəyişməz (P3 Trust gradient).


**Amaç:** Şeffaf abonelik. PRD §3.10. Tam implementasyon `prd-billing-revenuecat`.
**Layout:** Edge-to-edge video arka plan + alt gradient. Kapatma `×`. Title. 3–4 fayda bullet. **İki seçenek kartı (zorunlu):** "7 günlük pulsuz sınaq" · "İllik plan — 60 AZN" (rozet "38% qənaət"). Trial şartı açık metin. **Auto-renewal toggle görünür.** CTA "Davam et". Altta "Bərpa et" + Terms.
**🔄 v1.1 reframe:** Paywall copy **AI'ı satmaz** — sonucu/içeriği satar: "tam məşq kitabxanası", "bütün hərəkət videoları", "uçtan-uca izləmə". "AI koçun kilidini aç" YASAK.
**Durumlar:** default · seçili · loading · hata.
**Copy:** `paywall.*` (billing PRD).
**Telemetri:** `paywall_triggered { source }`, `paywall_option_viewed`.
**Edge:** Onboarding boyunca hiçbir billing prompt yok. İptal serbest → Home.

### Ekran 23 — Home (post-onboarding — referans)
**Amaç:** Onboarding bitişi. **Bu PRD scope'u dışı** — project-context §2.
**🔄 v1.1 not:** Onboarding'in son karesi Home'a köprü olmalı. Primary tile "Bugün Məşqi" dolu gelir; kullanıcı "ne yapacağımı biliyorum" hisseder. `.pen` 07-ready ekranındaki "Bu plan AI tərəfindən yaradılıb" copy'si **kaldırılır** → "Birinci məşqin hazırdır. Başlayaq?" Tab bar burada ilk kez görünür.
**Edge:** Pregnancy `true` ise "curated template" banner'ı.

### Ekran 24 — Settings / Logout & Delete giriş noktası (referans)
**Amaç:** Logout, Delete, ve **Professional Coaching teaser** giriş noktası. Settings tam ekranı scope dışı.
**İçerik:** "Çıx" satırı (nötr). "Hesabı sil" satırı (`danger`, en altta, ayrık). **"Peşəkar məşqçi ilə çalış" satırı** → Ekran 28 (yeni — Faza 2 teaser).
**Edge:** "Çıx" → Ekran 25. "Hesabı sil" → Ekran 26.

### Ekran 25 — LogoutSyncModal (BlockingModal)
**Amaç:** Senkronize edilmemiş veri kaybını önle. PRD US-1.7.
**Tetik:** "Çıx" + sync queue > 0.
**Layout:** BlockingModal (`surface-elevated`). `alert-triangle` (`accent`). Title "{N} sessiya hələ sync olunmayıb". Gövde. 3 dikey CTA: PrimaryButton "Göndər və çıx" (**default, en üstte**) · DestructiveButton "Sil və çıx" (ayrık, `danger`) · SecondaryButton "Ləğv et".
**Durumlar:** default · flush loading.
**Copy:** `logout.sync_modal.*`.
**Hareket:** Modal scale-in 200ms.
**Telemetri:** `logout_initiated`, `logout_completed`, `forced_logout_data_loss`.
**Erişilebilirlik:** Focus trap; ilk focus güvenli CTA. `alertdialog`.
**Edge:** Queue boşsa modal görünmez. R-15: destructive CTA asla default/en üstte değil.

### Ekran 26 — DeleteAccountConfirm-1
**Amaç:** Hesap silme — ilk onay. PRD US-1.8.
**Layout:** BlockingModal. Title "Hesabını silmək istəyirsən?". 2 CTA: SecondaryButton "Ləğv et" (default) · DestructiveButton "Sil".
**Copy:** `delete.confirm_1.*`. **Telemetri:** `account_delete_initiated`.
**Edge:** "Sil" → Ekran 27.

### Ekran 27 — DeleteAccountConfirm-2
**Amaç:** Son onay, sonuçlar açık. PRD US-1.8.
**Layout:** BlockingModal (`danger-soft` ton). `trash-2` `danger`. Title "Bütün məlumatın silinəcək". Gövde — "30 gün ərzində bərpa edə bilərsən" vurgulu. 2 CTA: SecondaryButton "Ləğv et" (default) · DestructiveButton "Bəli, sil".
**Durumlar:** default · loading · başarı → AuthGate + toast.
**Copy:** `delete.confirm_2.*`.
**Telemetri:** `account_delete_confirmed`, `delete_aborted_reopen`.
**Edge:** 30 gün grace → restore.

### Ekran 28 — ProfessionalCoachingTeaser (v1.1 yeni — Faza 2 demand-signal)
**Amaç:** Faza 2'deki professional-direct coaching için **talep sinyali toplamak** — sıfır mühendislik, marketplace kurmadan. BMad party-mode kararı (CLAUDE.md P1).
**Layout:** Header geri (→ Settings). Üstte sakin ikon (`user`/`award` — `accent-glow` daire). Title **"Peşəkar məşqçi ilə çalışmaq"**. Gövde: "Tezliklə — gerçək sertifikatlı məşqçi ilə birbaşa çalışma imkanı. Maraqlanırsansa, hazır olanda sənə bildirək." 1 TextInputField: Email (ön-doldurulmuş, hesap email'i). CTA PrimaryButton "Məni xəbərdar et". Altta küçük `caption`: "Bu xüsusiyyət hələ mövcud deyil."
**Durumlar:** default · gönderim sonrası success state ("Təşəkkürlər — hazır olanda yazacağıq") · zaten kayıtlı.
**🔑 Kritik UX kuralı:** Bu ekran **bir özellik vaadi vermez** — "yakında" net biçimde yazılır. MVP marketinqi/onboarding bu ekrana yönlendirmez; yalnız Settings'ten meraklı kullanıcı bulur. CLAUDE.md Qəti Qadağa: trainer özelliği MVP'de vaat edilmez.
**Copy:** `professional_teaser.*` (yeni copy-key grubu — AZ/RU/EN).
**Hareket:** Submit sonrası success fade.
**Telemetri:** `pro_coaching_teaser_viewed`, `pro_coaching_waitlist_joined` — Faza 2 önceliklendirmesi için talep ölçümü.
**Erişilebilirlik:** Standart form erişilebilirliği.
**Edge:** Faza 2'de bu ekran gerçek akışa (path seçimi 3. kart) evrilir; waitlist email'leri lansman bildirimine kullanılır.

---

## 7. Kullanıcı Yolculukları

### J1 — Persona A "Şəhərli professional qadın" (iOS, Apple Sign-In)

| Adım | Ekran | Duygu (v1.1) | Tasarım yanıtı |
|------|-------|--------------|----------------|
| Açılış | LanguagePicker | "AZ var" | AZ önceden seçili |
| Welcome | Welcome | "Bu benim antrenmanım için" | "Öz məşqini idarə et" — AI yok |
| Q1–Q7 | Sorular | "Beni anlatıyorum, hızlı" | Tek-soru, ≤90 sn |
| Değer | ProfilePreview | "Beni tanıdılar, dinlendim" | Cevap özeti + ölçülen hedefler, AI tiyatrosu yok |
| Disclosure | AIDisclosure | "Dürüstçe söylüyorlar, kontrol bende" | Minimal, sakin |
| Auth | AuthGate | "Apple ile tek dokunuş" | Apple #1 |
| Sample | SampleWorkout | "İşte yapacağım antrenman" | Hareket videosu, AI vurgusu yok |
| Paywall | Paywall | "Şeffaf, kandırılmıyorum" | İki seçenek, AZN |

### J2 — Persona B "Gənc kişi zal-go-er" (Android, Google)
Sürtünme: "Önce parayı görmek istemiyorum." → Sample workout paywall'dan önce; **antrenmanın kendisi** ikna eder, "AI sihri" değil. Google #1.

### J3 — Persona C "Yaşlı sağlamlıq-driven" (Email)
Sürtünme: "Niyə soruşur?" → Her soru altında bağlam etiketi. Büyük dokunma hedefleri, Dynamic Type %130. Dark mode kontrastı yaşlı göz için net (yüksek `text-primary`/`bg` oranı).

### J4 — Persona D "Hamilə / postpartum" (güvenlik-kritik)
Q1–Q7 → Home → PregnancyNudge → "Hə" → "curated template" çerçevesi (ceza değil, özen).

### J5 — Age Gate `<13`
Sakin kapanış — nötr ikon, suçlamayan dil, fade geçiş.

### J6 — Offline onboarding
Uçak modunda Q1–Q7; her cevap SQLDelight'a anında. Auth'ta ağ gerekir → Banner; cevaplar kaybolmaz.

### J7 — Meraklı kullanıcı: professional coaching (v1.1)
Kullanıcı Settings'i kurcalar, "Peşəkar məşqçi" satırını bulur → Ekran 28. "Yakında" görür, email bırakır. Hayal kırıklığı YOK çünkü hiçbir yerde vaat edilmemişti — keşif bir bonus, eksik bir söz değil.

---

## 8. UX Pattern'leri

### 8.1 Form pattern'leri
Label alanın üstünde. Focus `accent` 2px stroke. Hata: stroke `danger` + InlineError 150ms. Parola `eye` toggle. CTA submit'te loading.

### 8.2 Seçim pattern'leri
2–4 seçenek sub-context'li → RadioCard. Sayısal sıralı → SegmentedControl / WheelPicker / StepperField. Seçim anında geri bildirim 150ms.

### 8.3 Durum pattern'leri

| Durum | Görünüm |
|-------|---------|
| Loading | CTA spinner / video poster+spinner. Tam ekran spinner yalnız cold-start. |
| Empty | Onboarding'de yok. |
| Error | InlineError (form) · Banner (AUTH_005) · Toast (AUTH_007/009) · BlockingModal (AUTH_006). |
| Success | Toast · `success` ikon · ProfilePreview kart belirme · Ekran 28 success state. |
| Offline | Üstte kalıcı Banner. |

### 8.4 Modal hiyerarşisi
Modal içinde modal YASAK. DeleteAccount iki ekranlı ama sıralı. BottomSheet = dismissable. BlockingModal = kritik. TerminalScreen = dönüşsüz.

### 8.5 Hata kurtarma
Her hata düzeltilebilir adım sunar: `AUTH_003`→"Daxil ol"; `AUTH_005`→"yenidən göndər"; `AUTH_013`→"Yeni link"; `AUTH_009`/`AUTH_011`→toast + "Yenidən cəhd et"; `ONB_003`→"Yenidən başla".

### 8.6 Onboarding hata kodu izlenebilirliği (PRD §8.5)

Her hata kodu bir ekran durumuna bağlıdır:

| Kod | Ekran / Durum |
|-----|---------------|
| `AUTH_001/002/003` | Ekran 15 EmailSignupForm — InlineError |
| `AUTH_002` | Ekran 19 PasswordResetForm — InlineError |
| `AUTH_004` | Ekran 16 EmailLoginForm — InlineError |
| `AUTH_005` | Ekran 16 EmailLoginForm — Banner + "Linki yenidən göndər" |
| `AUTH_006` | Ekran 15 / Ekran 16 — BlockingModal |
| `AUTH_007` | Ekran 14 AuthGate (+ form ekranları) — Toast |
| `AUTH_008/010` | Ekran 14 AuthGate — sessiz, durum değişmez |
| `AUTH_009/011` | Ekran 14 AuthGate — Toast |
| `AUTH_012` | Ekran 6 AgeGateBlocked — sunucu tetiklemesi |
| `AUTH_013` | Ekran 19 PasswordResetForm — InlineError + "Yeni link" |
| `ONB_001` | Ekran 5 Q3_Age — InlineError; `<13` dalı → Ekran 6 |
| `ONB_002` | Ekran 8 Q4_HeightWeight — InlineError |
| `ONB_003` | Cold-start (LanguagePicker/Welcome öncesi) — "Yarımçıq qeydiyyat vaxtı keçib" BlockingModal + "Yenidən başla" → Q1'den temiz başlangıç |
| `ONB_004` | SyncOnboardingToProfile (görünmez arka plan) — UI ilerler, sessiz background retry; kullanıcıya banner/hata gösterilmez |
| `ONB_005` | Ekran 7 ParentalNotice — InlineError |

---

## 9. Hareket & Mikroetkileşimler

> NFR-P2: her geçiş <200ms.

| Etkileşim | Hareket | Süre |
|-----------|---------|------|
| Ekran-ileri/geri | Yatay slide | 200ms ease-in-out |
| Age gate hard-stop'a | Fade (kopuş) | 250ms |
| RadioCard seçim | Zemin + stroke | 150ms |
| CTA disabled→enabled | Renk fade | 150ms |
| SegmentedControl | Gösterge kayma | 180ms spring |
| BottomSheet | Alttan yükselme | 280ms spring |
| BlockingModal | Scale-in | 200ms |
| ProfilePreview kartlar | Kademeli fade-in (AI sayaç YOK) | 200ms |
| Buton press | Opacity → pressed | 80ms |
| AIDisclosure | Sade fade-in, **ikonda animasyon YOK** | 200ms |
| InlineError | Aşağı açılma | 150ms |

**🔄 v1.1:** AI sayaç animasyonu ve `sparkles` pırıltısı **kaldırıldı** — AI'ı öne çıkaran hiçbir mikroetkileşim yok.
**Reduce Motion:** slide → cross-fade, spring → linear, fade-in → anında.

---

## 10. Responsive & Adaptif Davranış

### 10.1 Platform farkları

| Öğe | iOS | Android |
|-----|-----|---------|
| AuthGate sırası | Apple #1, Google #2, Email #3 | Google #1, Email #2 |
| Apple butonu | Sistem komponenti | — |
| WheelPicker | `UIPickerView` | Material picker |
| Geçiş | `.easeInOut(0.2)` | `tween(200)` |
| Dokunma hedefi | 44pt | 48dp |

Ortak (KMP shared): akış, state machine, copy, token, validation.

### 10.2 Cihaz boyutu
Küçük → scroll + sticky CTA. Standart 375×812 referans. Büyük → max 480px ortalı. Tablet optimize edilmez.

### 10.3 Dynamic Type
%85–%130 bozulmasız; %130 üzeri scroll + sticky CTA.

---

## 11. Erişilebilirlik (NFR-A1…A6)

| Kural | Uygulama |
|-------|----------|
| A1 Screen reader | VoiceOver + TalkBack; sıra header → step → title → açıklama → seçenekler → CTA. Dekoratif öğeler gizli. |
| A2 Dokunma hedefi | 44pt / 48dp min. |
| A3 Dynamic Type | %130'a kadar bozulmasız. |
| A4 Kontrast | WCAG AA — dark token'lar §3.1'de doğrulandı; `accent` üstü siyah metin. |
| A5 AZ telaffuz | AZ TalkBack sınırlı → kritik elemanlarda RU fallback. |
| A6 Klavye/jest | Swipe + tab; modal'larda focus trap. |

**Ek kararlar:**
- ProfilePreview: kartlar belirince son içerik okunur.
- AIDisclosure: geri devre dışı — bildirilir.
- BlockingModal/BottomSheet: `alertdialog`/`dialog`, focus trap, ilk focus güvenli aksiyon.
- Renk asla tek bilgi taşıyıcısı değil — seçili kart renk + `check` + stroke; hata renk + ikon + metin.
- **Dark mode özel:** Yüksek `text-primary`/`bg` kontrastı yaşlı/düşük görüş için avantaj; ama saf siyah (`#000`) değil `#0A0A0B` — halo/parlama azaltma. "Reduce Motion" tam destek.

---

## 12. Lokalizasyon UX (AZ / RU / EN)

- **Diller:** AZ (default+birincil), RU, EN. LTR.
- **Fallback:** AZ → RU → EN → key. Eksik key debug'da kırmızı border.
- **Metin genişlemesi:** RU ~%15–30 uzun → `fill_container`+`fixed-width`; buton `fit_content`.
- **Çoğul:** AZ tekil/çoğul, RU 3-form, EN 2-form (CLDR).
- **Runtime dil değişimi:** re-launch yok.
- **MT yasağı:** Manuel review + CI gate. `copy-bundle-onboarding-auth-v2.json` — AZ %100, RU/EN %80+.
- **v1.1 yeni copy-key grubu:** `professional_teaser.*` (Ekran 28) AZ/RU/EN.

---

## 13. `.pen` Design System Durumu & Handoff

### 13.1 Mevcut durum — Stage B ✅ TAMAMLANDI (2026-05-21)

`onboarding_flow.pen` **Stage B'de bu spec'in 26-ekran kapsamına göre yeniden kuruldu.**

- **26 build edilebilir ekran** `.pen`'de mevcut — PRD §17.1 sırasıyla (Ekran 1–22, 25–28). Ekran 23 (Home) ve 24 (Settings) scope dışı referans olduğu için `.pen`'e dahil edilmedi.
- **Volt renk sistemi uygulandı** — `set_variables` ile dark-first semantic token'lar (project-context §1b): `accent`/`volt` `#E6FF00`, `bg` `#0A0A0B`, `surface` `#141416`, `surface-elevated` `#1E1E21`, `border` `#2A2A2E`, `on-accent` `#0E0E0E`, `accent-glow` `rgba(230,255,0,0.12)`, `moss`/`moss-dim`, `text-tertiary`, `border-strong`, `warning`. Eski turuncu accent (`#FF6B33`) tamamen kaldırıldı.
- **Dark-first** — light token'lar gelecek tema olarak tanımlı, dark default.
- **Komponentler mevcut** — §3.5'teki komponent kütüphanesi (PrimaryButton, SecondaryButton, DestructiveButton, RadioCard, SegmentedControl, StepperField, WheelPicker, UnitToggle, TextInputField, InlineError, Banner, BottomSheet, BlockingModal, TerminalScreen, ProgressIndicator, LinkText, LegalDisclaimer, InfoNotice, HeroBlock, StatusBar) `.pen`'de reusable olarak kurulu, Volt token'larına bağlı.

### 13.2 Stage B'de kapatılan boşluklar (✅ tamam)

| Boşluk | Durum |
|--------|-------|
| `.pen` LIGHT mode → dark-first | ✅ Dark-first yeniden kuruldu — §3.1 semantic token tablosu. |
| `.pen` eski `accent` (#FF6B33 turuncu) → Volt | ✅ `accent` `#E6FF00`, `accent-glow` `rgba(230,255,0,0.12)`, splash tam-sarı. |
| Tek `surface` katmanı → elevation | ✅ `surface` + `surface-elevated` ayrıldı. |
| `.pen` 7 ekran → 26 ekran | ✅ 26 build edilebilir ekran PRD §17.1 sırasıyla eklendi. |
| `.pen` 02-goal 4 seçenek ("Güc artırmaq") | ✅ 3 seçeneğe indirildi (`bulk/cut/general_fit`). |
| `.pen` step numaraları (PRD sırası) | ✅ Q4=4/7, Q5=5/7, Q6=6/7, Q7=7/7. |
| `.pen` 07-ready "AI tərəfindən yaradılıb" copy | ✅ "Birinci məşqin hazırdır" (AI vurgusu kaldırıldı). |
| `.pen` Welcome "AI fitness planı" copy | ✅ "Öz məşqini idarə et" (AI vurgusu kaldırıldı). |
| Reusable component 3 → tam kütüphane | ✅ §3.5 komponentleri + InfoNotice reusable tanımlandı. |

### 13.3 Handoff paketi
1. **Stage B — `.pen` dark-mode iterasyonu:** ✅ Tamamlandı (26 ekran, Volt token'lar, komponent kütüphanesi).
2. **Copy bundle:** `copy-bundle-onboarding-auth-v2.json` + `professional_teaser.*`.
3. **Architecture:** `bmad-create-architecture` (Winston) — dark/light token'lar `shared/`'a; data model trainer-loop rezervi (`workouts.status/reviewed_by/source`, `users.role`, `health_constraints`).
4. **Dev:** PRD + bu spec + `.pen` üçlüsü.

---

## 14. Açık UX Soruları

| # | Soru | Sahip | Çözüm zamanı |
|---|------|-------|--------------|
| UX-1 | Q3 yaş: WheelPicker mi StepperField mi? | Sally | Tasarım iterasyonu |
| UX-2 | Q4: StepperField vs çift WheelPicker — yaşlı persona için? | Sally | Tasarım iterasyonu |
| UX-3 | AIDisclosure: ayrı minimal ekran mı, ProfilePreview alt InfoNotice'ı mı? Legal review "ayrı atlanamaz ekran" şart koşuyor mu? | Sally + Legal | Sprint 1 (legal review) |
| ~~UX-4~~ | ~~Dark mode accent kalibrasyonu~~ — **KAPANDI v1.3:** Volt sistemi (`#E6FF00`, Ladder-ilhamlı) benimsendi; project-context §1b kanonik. | Sally | ✅ 2026-05-21 |
| UX-5 | PregnancyNudge 1500ms gecikme doğru mu? | Sally | Sprint 1 |
| UX-6 | Soft paywall (30 sn) agresif algılanır mı? A/B. | Sally + PM | Post-MVP A/B |
| UX-7 | Light mode v1.1'de eklensin mi? Token'lar hazır. | PM | Post-MVP |
| UX-8 | Ekran 28 teaser Settings dışında bir yerden de erişilebilir olmalı mı (örn. Dashboard kartı)? Yoksa "vaat etme" kuralını zedeler mi? | Sally + PM | Sprint 2 |
| UX-9 | Super-set MVP'de — logger UI bu spec'in scope'u dışı (workout PRD); ama onboarding'de super-set'e referans verilmeli mi (örn. SampleWorkout)? | Sally + PM | Workout PRD ile |
| UX-10 | Marka adı — `.pen` ve bu spec "FitAz" kullanıyor; CLAUDE.md/project-context/PRD "fitnessApp". Kanonik kullanıcıya görünür marka adı hangisi? Stage B `.pen` wordmark'ı bu karara bağlı. | Balaagha + PM | Stage B öncesi |

---

## 15. Onay & İmza

| Rol | İsim | Durum | Tarih |
|-----|------|-------|-------|
| UX | Sally (bmad-agent-ux-designer) | **Taslak v1.4 (26-ekran scope · hata kodu izlenebilirliği · Stage B `.pen` tamam)** | 2026-05-21 |
| PM | John (bmad-agent-pm) | PRD v2.1 (v3.2 §0 align) onaylı | 2026-05-21 |
| Analyst | Mary (bmad-agent-analyst) | Repositioning onaylı | 2026-05-21 |
| Architect | Winston | Repositioning onaylı; Volt token'ları `shared/` design-token katmanına; data model rezervi bekliyor | 2026-05-21 |
| Owner | Balaagha | Sign-off bekleniyor | — |

---

## 16. Changelog

| Versiyon | Tarih | Yazar | Değişiklik |
|----------|-------|-------|------------|
| 1.0 | 2026-05-20 | Sally | İlk UX design specification — 27 ekran, design system (light), user journey, UX pattern, hareket, responsive, erişilebilirlik, lokalizasyon. |
| **1.1** | **2026-05-21** | **Sally** | **Repositioning (BMad party-mode Mary/John/Winston): AI duygusal kahraman → kullanıcı kahraman. Welcome/ProfilePreview/AIDisclosure/SampleWorkout/son ekran reframe. Design system dark-first (semantic token, accent-glow, surface-elevated). AIDisclosure minimal. SampleWorkout curated template. Yeni Ekran 28 ProfessionalCoachingTeaser. project-context §0 + CLAUDE.md Repositioning'e hizalandı.** |
| **1.2** | **2026-05-21** | **Sally** | **Cross-document tutarlılık geçişi: PRD v2.1'e hizalandı (PRD artık v3.2 §0 ile uyumlu — AI disclosure/ProfilePreview/SampleWorkout reframe + Ekran 28/FR-36 PRD'ye işlendi). Context ikonografisi düzeltildi (§3.4 ↔ Ekran 10 — casual_gym `dumbbell`). Marka adı "FitAz" açık soru UX-10 olarak işaretlendi; Welcome wordmark placeholder.** |
| **1.3** | **2026-05-21** | **Sally + Winston** | **Volt renk sistemi: turuncu accent `#FF6B33` → Ladder-ilhamlı elektrik-sarı `#E6FF00`. §3.1 token tablosu yeniden yazıldı, kanonik kaynak project-context.md §1b'ye taşındı. Yeni token: `moss`/`moss-dim` (dekoratif yeşil-zeytun), `text-tertiary`, `border-strong`, `warning`. Koyu taban `#0A0A0B`/`#141416`/`#1E1E21`. Splash tam-sarı doktrini. `success` yeşili chip'ler için ayrıldı. UX-4 kapandı. Stale "sıcak parıltı/pembe" ifadeleri "volt parıltı"ya çevrildi.** |
| **1.5** | **2026-05-23** | **Sally** | **(a) `.pen` rename: `onboarding_flow.pen` → `app_design.pen` (tüm app — multi-section). (b) Sual ekran şablonu ayrı sənədə split: `ux-onboarding-questions-2026-05-23.md` (L1+L2+L3+L5 anatomy/variant/state/copy/A11y). Bu sənəd ekran-akış spec'i kalır; sual sayfası şablon detayları artık questions UX'te. (c) `ux-auth-onboarding` v1.2 (axın-yalnız) cross-ref eklendi.** |
| **1.4** | **2026-05-21** | **Sally** | **Scope hizalama + boşluk kapatma: (1) Kapsam **26 build edilebilir ekran** olarak netleştirildi — Ekran 23 Home + 24 Settings scope dışı referans; §0b kapsam cümlesi + §6 başlığı düzeltildi. (2) Yeni §8.6 hata kodu izlenebilirlik tablosu — 18 kodun (AUTH_001-013 / ONB_001-005) tümü bir ekran durumuna bağlandı. (3) AuthGate'e AUTH_008/009/010/011/012 durumları, AgeGateBlocked'a AUTH_012, cold-start'a ONB_003 modalı, flow map'e ONB_004 notu eklendi. (4) Form/ağ ekranlarına (Signup/Login/ConfirmPending/ResetEmail/ResetForm) eksik durumlar eklendi — CTA disabled/loading, offline Banner, AUTH_007 toast; LanguagePicker/ProfilePreview offline-safe notu. (5) §13 `.pen` handoff Stage B **tamamlandı** olarak güncellendi — 26 ekran PRD sırasıyla, Volt token'lar, komponent kütüphanesi mevcut. AI-framing ve renk sistemi denetlendi: stale referans yok.** |

---

**UX Design Specification — Authentication & Onboarding v1.4 sonu.**
*Sonraki adım:* Stage B `.pen` iterasyonu tamamlandı (26 ekran, Volt token'lar) → `copy-bundle` finalize → `bmad-create-architecture` (Winston).
