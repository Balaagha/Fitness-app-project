# Phase 3 — Design System 9 Sub-Section Spec

Hazırlanma tarixi: 2026-05-23
Mənbə: feature `detect-design-ui-step-and-tools` · Phase 3 paralel agent output
Şamil: `.pen` `01 Design System` frame altına 9 sub-section batch_design əlavə

---

## Global frame qaydaları
- width: 1280, padding: 40, gap: 24, direction: vertical, fill `$surface`, cornerRadius 24, stroke `$outline` 1px
- y-stack: bir-birinin altına +32 gap
- Heading hər frame başında: `h2/600` + `caption/500 $ink-muted`
- Token-only — literal hex yalnız §2 swatch label-larında

## Sıra (yazılma)
1 → 2 → 3 → 4 → 5 → 6 → 7 → 8 → 9
Toplam ~6448 px əlavə hündürlük → 01 DS frame yeni h: ~11145

---

## Sub-section #1 — `ds-01-cover` (1280×320, horizontal padding 48 gap 32, fill `$bg`, stroke `$volt` 2px, radius 24)

- `col-left` (fill_container W, vertical, gap 16):
  - eyebrow "VOLT DESIGN SYSTEM" — label/700 `$volt` LS 0.12em
  - title "Məşqini idarə et — vizual dil" — display/700 `$ink`
  - subtitle "Elektrik-sarı accent, koyu taban, minimalist enerji." — body-lg/400 `$ink-muted`
  - `meta-row` (horizontal, gap 12): chip "v1.0" `$surface-2` · chip "2026-05-23" `$surface-2` `$ink-muted` · chip "Maintainer: @balaaghaalihumatov" `$volt` `$on-volt`
- `col-right` (240w fixed): `volt-mark` 160×160 `$volt` r32 + text "⚡" (lucide `zap`) display/700 `$on-volt`

## Sub-section #2 — `ds-02-colors` (1280×~880, vertical gap 32)

- heading "Rənglər" + sub "18 token — Volt sistemi"
- `swatch-grid` (1200w, 6×3 grid gap 16) — hər card 184×140:
  - swatch chip 160×72 r12 fill=token
  - name `label/600 $ink`
  - hex `caption/500 $ink-muted`

18 token sırası:
- Row 1 (Tabanlar): `$bg` #0A0A0B · `$surface` #141416 · `$surface-2` #1E1E21 · `$surface-3` #26262A · `$outline` #2E2E33 · `$overlay` rgba(0,0,0,.6)
- Row 2 (Mətn+Accent): `$ink` #F5F5F7 · `$ink-muted` #A0A0A8 · `$ink-disabled` #5A5A62 · `$volt` #E6FF00 · `$on-volt` #0E0E0E · `$volt-pressed` #C9DF00
- Row 3 (Semantic+Dekor): `$success` #3DD68C · `$warning` #FFB020 · `$danger` #FF4D4F · `$info` #4DA3FF · `$moss` #A4B82B · `$focus-ring` #E6FF00@40%

`$moss` cell-də badge "decorative only — CTA QADAĞA" `$danger`.

## Sub-section #3 — `ds-03-typography` (1280×~720, vertical gap 20)

- heading "Tipoqrafiya" + sub "Inter / SF Pro — 8 stil"
- `type-table` 8 sətr (1200w, vertical gap 16):
  - sətr: horizontal gap 24 padding-y 16 border-bottom `$outline`
    - col-name 200w (label/600 `$ink`)
    - col-sample fill_container ("Məşqini idarə et" stilin özü ilə)
    - col-meta 240w ("size/weight · LH · LS" caption/500 `$ink-muted`)

8 stil:
1. display 48/700 LH56 LS-0.02em
2. h1 32/700 LH40 LS-0.01em
3. h2 24/600 LH32 LS-0.01em
4. h3 20/600 LH28 LS0
5. body-lg 18/400 LH28 LS0
6. body 16/400 LH24 LS0
7. caption 13/500 LH18 LS0.01em
8. label 12/600 LH16 LS0.04em UPPERCASE

## Sub-section #4 — `ds-04-spacing-radius` (1280×~520, vertical gap 32)

- heading "Spacing & Radius" + sub "8-pt scale + 5 radius"
- `spacing-block`: sub-heading "Spacing scale" + horizontal row 10 tile (W×80h fill `$volt` r4 + altda caption ölçü etiketi): 4·8·12·16·20·24·32·40·48·64
- `radius-block`: sub-heading "Radius tokens" + horizontal row 6 tile 120×120 `$surface-2` stroke `$volt` 2px r=token: `$radius-sm` 8 · `$radius-md` 12 · `$radius-lg` 16 · `$radius-xl` 20 · `$radius-2xl` 24 · `$radius-pill` 999

## Sub-section #5 — `ds-05-iconography` (1280×~520, vertical gap 24)

- heading "Iconography" + sub "lucide-react · 24px · stroke 2 · `$ink`"
- `icon-grid` 1200w 8×2 gap 24, hər cell 140×100:
  - icon-box 56×56 `$surface-2` r12 + icon 24px `$ink`
  - text icon-name caption/500 `$ink-muted`

16 ikon (DOĞRU lucide adları):
- Row 1: dumbbell · flame · calendar · target · droplet · apple · scale · footprints
- Row 2: timer · repeat-2 · info · **triangle-alert** · check · x · arrow-right · sparkles

triangle-alert cell-də badge "warning state" `$warning` fill, icon `$on-volt`.

## Sub-section #6 — `ds-06-buttons` (1280×~720, vertical gap 32)

- heading "Buttons" + sub "3 tip × 5 state"
- 3 sıra, hər biri horizontal gap 16:
  - row-label 160w label/600 `$ink`
  - 5 düymə 180×56 r=`$radius-md` padding-x 24 text body/600

**btn-primary "Davam et"**: default fill `$volt` text `$on-volt` · hover opacity 0.92 stroke `$volt` 2px outset · pressed `$volt-pressed` · disabled `$surface-2` `$ink-disabled` · loading + `loader-2` 16px spin

**btn-secondary "Ləğv et"**: default `$surface` stroke `$outline` text `$ink` · hover `$surface-2` stroke `$ink-muted` · pressed `$surface-3` · disabled text `$ink-disabled` · loading spinner

**btn-destructive "Sil"**: default transparent stroke `$danger` text `$danger` · hover `$danger`@12% · pressed fill `$danger` text `$ink` · disabled text `$ink-disabled` · loading spinner

⚠️ primary-də `$on-volt` (qara) — ağ metin yasaq.

## Sub-section #7 — `ds-07-selection` (1280×~960, vertical gap 32)

- heading "Selection components" + sub "7 component × default/selected"
- `sel-grid` 2×4 gap 24, hər cell 588×200 `$surface-2` r16 padding 20:
  - comp-title label/600 `$ink`
  - comp-preview horizontal gap 16: 2 instance (default + selected)

7 komponent:
1. **option** (radio): 280×56 stroke `$outline`, sol 20×20 dairə; selected stroke `$volt` 2px fill `$volt`+inner dot `$on-volt`; "Ev məşqi"
2. **option-multi** (checkbox): kvadrat r6; selected fill `$volt` + check icon `$on-volt`; "Dumbbell"
3. **Segmented**: 280×44 `$surface-3` r=pill 3 seqment (AZ/RU/EN); aktiv `$volt` `$on-volt`
4. **Chip**: 3 chip — "Üst" (default `$surface-3` `$ink-muted`) · "Sinə" (selected `$volt` `$on-volt`); h32 padding-x 12 r=pill
5. **Tag**: h24 padding-x 8 r6 — "yeni" (`$volt`@16% text `$volt` stroke `$volt`) · "premium" (`$warning`@16% text `$warning`)
6. **Switch**: 44×24 track + 20×20 thumb; off track `$surface-3` thumb `$ink-muted`; on track `$volt` thumb `$on-volt`
7. **Stepper**: 140×44 — [-] 32×32 `$surface-3` `minus` `$ink` · "3" body/600 `$ink` · [+] `$volt` `plus` `$on-volt`; label "Set sayı"

## Sub-section #8 — `ds-08-forms-feedback` (1280×~880, vertical gap 32)

- heading "Forms & Feedback" + sub "Textfield · Notice · Empty · Skeleton"

**Block 8.1 textfield (4 state)** horizontal gap 16, hər biri 280×80:
- default stroke `$outline` placeholder "ad@nümunə.az"
- focus stroke `$volt` 2px focus-ring 4px dəyər "ali@volt.az" `$ink`
- error stroke `$danger` dəyər "ali@" + caption `$danger` "Yanlış format" + triangle-alert
- disabled fill `$surface` text `$ink-disabled`

**Block 8.2 info-notice (4 variant)** vertical gap 12, hər biri 1200×64 r=`$radius-md` padding 16:
- info `$info`@12% stroke `$info` icon `info` text "Plan AI tərəfindən yaradılır"
- success `$success`@12% icon `check` text "Məşq qeyd olundu"
- warning `$warning`@12% icon `triangle-alert` text "Streak Freeze bu ay istifadə olundu"
- danger `$danger`@12% icon `x` text "Hesabınız silinəcək"

**Block 8.3 empty-state** 1200×200 `$surface-2` r20 vertical center gap 16:
- icon 48px dumbbell `$ink-muted`
- title "Hələ məşq qeydi yoxdur" h3/600 `$ink`
- subtitle "İlk məşqini başlat və burada gör." body/400 `$ink-muted`
- btn-primary "Məşq başlat" 180×48

**Block 8.4 loading-skeleton** 1200×120 `$surface-2` r16:
- 3 sətr skeleton: 60%/90%/40% width h16 `$surface-3` r8 + sağda "shimmer 1.4s"

## Sub-section #9 — `ds-09-principles-a11y` (1280×~640, vertical gap 32)

- heading "Principles & A11y" + sub "Minimal Design + WCAG AA"

**Block 9.1 principle card** 1200×140 `$surface-2` r20 stroke `$volt` 2px padding 24:
- eyebrow "PRİNSİP 01" label/700 `$volt`
- title "Feature əlavə = element çıxarılır" h3/600 `$ink`
- body "Hər yeni element üçün ekrandan biri çıxır. Vizual yığılma rədd edilir." body/400 `$ink-muted`

**Block 9.2 contrast table** 1200w:
- header row h40 `$surface-3` padding-x 16: Foreground 240w · Background 240w · Nümunə 400w · Ratio 160w · WCAG AA 160w
- 4 sətr h56 border-bottom `$outline`:
  1. `$ink` on `$bg` → "Məşqini idarə et" → "18.5:1" → "AA ✓" (`$success`@16%)
  2. `$ink` on `$surface` → "Set 3 / Təkrar 12" → "15.8:1" → "AA ✓"
  3. `$on-volt` on `$volt` → "Davam et" → "14.2:1" → "AA ✓"
  4. `$ink-muted` on `$surface` → "Caption text" → "5.4:1" → "AA ✓"

**Block 9.3 forbidden combos** horizontal gap 16, 2 card 588×100 `$danger`@8% stroke `$danger` r16:
- Card 1: icon `x` `$danger` + "Ağ metin `$volt` üzərində — `$on-volt` (qara) istifadə et"
- Card 2: icon `x` `$danger` + "`$moss` CTA kimi qadağa — yalnız dekorativ"

---

## Pencil schema gotcha-ları
- Bir axis-də yalnız BİR `fill_container` child
- Lucide: `triangle-alert` (NOT `alert-triangle`), `repeat-2`, `arrow-right`
- Token referansı `$name` ilə
- cornerRadius token (`$radius-*`)
