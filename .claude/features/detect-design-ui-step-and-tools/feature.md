# Research: detect-design-ui-step-and-tools

## Metadata
- **Type**: research
- **Branch**: `n/a`
- **Started**: 2026-05-16
- **Current phase**: 2 / 3
- **Overall status**: in_progress
- **Related features**: [[determine-scope-of-start-up]]

---

## Question
fitnessApp üçün UI/UX mərhələsində hansı alətləri (BMad UX agent + Pencil MCP) hansı sırada işlədək ki, PRD-dən production-ready Compose/SwiftUI ekranlarına ən qısa, ən az təkrar işlə çataq?

## Why It Matters
- BMad-i UX üçün yarımçıq istifadə (məsələn, Pencil-ı planning'dən əvvəl açmaq) tipik 1-2 həftə geri-dönüş işi yaradır.
- KMM context-də design tokens tək mənbəli olmalıdır — Pencil `.pen` faylı bu tək mənbə olmaq üçün uyğundur, amma əvvəl BMad ux-spec.md razılığı lazımdır.
- AZ-RU-EN native string + offline-first state'lər Sally-yə **hard input** kimi verilməsə default ux-spec-də yoxa çıxır → MVP scope-u risk altında.

---

## Phases

### Phase 1: Scope the Question ✅ COMPLETE
- [x] BMad UX ayağının nə verdiyini müəyyən et (Sally agent + workflow + output)
- [x] Pencil MCP-nin nə verdiyini müəyyən et (canvas ↔ Claude bağı, swarm, design-to-code)
- [x] "Done" tərifi: addım-addım pipeline + hər addımda hansı artefakt üretilir
- **Status:** complete

### Phase 2: Investigation ⏳ IN PROGRESS
- [x] BMad docs (bmad-guide skill references) oxundu — Sally workflow `ux-spec.md` üretir
- [x] Web search: Pencil + Claude Code workflow case-study'ləri oxundu
- [x] Web search: BMad Sally agent + bmad-create-ux-design workflow detalları
- [x] Pencil MCP-ni proyektə real bağla, kiçik POC frame ilə test et — `app/design/mobile/app_design.pen` bağlandı, batch_design + screenshot işləyir
- [x] `.pen` design library üçün ilkin tokens siyahısı çıxar — Volt sistemi (`#E6FF00` accent, dark-first, 21 token) project-context.md §1b-də kanonik
- [x] Real .pen restructure: 4 section (00 Cover / 01 Design System / 02 Onboarding / 03 Questions / 04 States) + 33 ekran reposition + cross-ref chip
- [x] UX brief split: ux-auth-onboarding (axın) və ux-onboarding-questions (sual şablonu) ayrı sənədlər
- **Status:** in_progress (Phase 2 .pen icrası — AZ copy polish + DS expansion + L2/L3/L5 ekranları davam edir)

### Phase 3: Synthesize & Recommend
- [ ] Final pipeline-ı `docs/ux-workflow.md` kimi yaz (Sally + Pencil hand-off konkret addımlar)
- [ ] Hər ekran üçün "Pencil frame URL → story → kod" trace pattern-i təsbit et
- **Status:** pending

---

## Decisions / Recommendations

| Date       | Decision | Rationale | Impact |
|------------|----------|-----------|--------|
| 2026-05-16 | İlkin tövsiyə edilən sıra: `bmad-create-prd` → `bmad-create-ux-design` (Sally) → Pencil `.pen` design system → ekran-ekran frame → `bmad-create-architecture` → `bmad-dev-story` (Pencil MCP açıq) | BMad fresh-chat / phase boundary dizaynına uyğun; Pencil-ı boş canvas paralizi olmadan başlatmaq üçün ux-spec əvvəlcə lazım; tokens dev mərhələsində Compose+SwiftUI-ya tək nöqtədən axır | Pipeline-ı bu sıra ilə getməsək, ekran yenidən-çəkmə + token uyğunsuzluğu riski real |
| 2026-05-23 | UX brief 2-yə split: `ux-auth-onboarding` (axın səviyyəli) + `ux-onboarding-questions` (sual ekranı şablonu) ayrı sənədlər. `.pen` faylı `app_design.pen` adına dəyişdi (multi-section: cover/DS/onboarding/questions/states) | Sual ekranı şablonu onboarding axınından əslində ayrı UX məsələsidir — eyni şablon L2/L3/L5/weekly check-in/settings re-prompt-da yenidən istifadə olunur. Tək sənəddə saxlamaq sual variantlarının uzanması ilə oxunmazlıq yaradırdı | Yeni feature ekranı (məs. weekly check-in) gələndə sual UX-i təkrar yazmadan ux-onboarding-questions-dən referans alır |
| 2026-05-23 | `.pen` paralel agent strategiyası: 10 agent eyni canvas-a yaza bilməz — Pencil MCP tək-yazandır. Hibrit yanaşma: 4-5 paralel research agent yalnız MƏTN spec hazırlayır, .pen icrası tək (main) sıralı | Pencil server tək document açıq saxlayır, batch_design konflikt yaradar. Mətn artefaktları (copy bank, layout coord, design system spec) paralel yarana bilər | Gələcəkdə .pen iş workflow-u: agent paralelliyini həmişə spec-yazma fazasına aid, icra fazasına yox |
| 2026-05-23 | Cross-section pointer pattern: section-arası keçid üçün `bridge/NN-MM` frame (1100×340) — chip "NN → MM" + display-weight title + 7-dot strip + arrow + target chip. Welcome → Questions bridge ilə test edildi | Sual ekranları onboarding axınından struktur olaraq ayrıdır, lakin görsel keçid lazımdır. Bridge frame istifadəçinin (designer / dev / stakeholder) section-lar arası əlaqəni dərhal görməsini təmin edir | Bütün section-arası keçidlərdə eyni bridge pattern (Welcome→Questions işləndi; Settings→AuthGate, Calorie→TargetWeightSheet gələcəkdə tətbiq olunacaq) |
| 2026-05-23 | Cross-ref chip simvol dili kanoniləşdi: `→` (settings/inter-section reference) · `↔` (cross-section dependency) · `●` (single entry point) · `■` (terminal, `danger` rəng) · `⚠` (safety stop, `warning` rəng) · `↻` (re-trigger, Faza 2 hook) · `◐` (variant / state) | Annotation chip-ləri project-wide oxunaqlı olması üçün simvol semantikası konsistent qalmalı. 22 chip ilə test edildi, oxunma rahat | Bütün yeni `.pen` annotation və UX sənədlərində eyni simvol istifadə olunmalı (legend-də Cover frame-də sənədləşdi) |
| 2026-05-23 | `.pen` 4-section layout standartı: 00 Cover → 01 Design System → 02 Onboarding → 02→03 Bridge → 03 Questions → 04 States. Section spacing: ~6000px gap between functional sections | Gələcək feature-lər (workout, calorie, settings) bu pattern-ə §5+ kimi əlavə olunur — yenidən layout düşünmək lazım olmur | Bütün yeni section-lar eyni label format (2-digit caption + 54/800 mega title + 17/normal subtitle) və eyni screen grid (375 wide + 45 gap) istifadə etməli |
| 2026-05-23 (axşam — DÜZƏLİŞ) | Premium plan kontrakta **human approval gate** kimi yenidən ifadə edildi (köhnə "managed trainer review" çərçivəsini ləğv edir). Plan AI-dən gəlir; premium-da göndərilmədən əvvəl bir mütəxəssis yalnız təhlükəsizlik/uyğunluq yoxlaması edir — fərdi məşqçi-istifadəçi münasibəti, chat, fərdi koreksiya YOXDUR | Səhər versiyasında "canlı məşqçi" frazası trainer feature kimi səslənirdi və CLAUDE.md "trainer vəd etmə" qaydasına ziddiyət təsiri verirdi. Düzgün strateji çərçivə: AI plan + human-in-the-loop quality gate. Trainer = Faza 2 (açıq); insan onayı = MVP premium (qapalı queue, 1-2 mütəxəssis) | 5 yerdə yenilənmə icra edildi: (a) `.pen` #13 AI Disclosure body (eQcvv/Cs4v3/z2ZaHx); (b) `prd-auth-onboarding` §3.5 v3.3; (c) `ux-auth-onboarding` v1.3 changelog reword; (d) `CLAUDE.md` Qəti Qadağalar — yeni human-approval bəndi əlavə, "trainer vəd etmə" qaydası KEÇƏRLİ qaldı; (e) `project-context.md` §0 + §5.2 plan zənciri yeniləndi (free=direct, premium=approval gate). Copy-də termin sabit: "**mütəxəssis yoxlaması / uyğunluq yoxlaması**" — "məşqçi/coach/trainer" qadağan |
| 2026-05-23 (axşam) | "məşqçi/coach/trainer" sözü copy/marketing/onboarding-də qadağan — CLAUDE.md Qəti Qadağalar-da yeni bənd kimi sənədləşdi | Vəd-teslimat boşluğunu önləmək; trust moat (transparent billing) ilə uyğunluq; "trainer vəd etmə" qaydası KEÇƏRLİ qaldı (Faza 2 açıq marketplace üçün rezerv) | Bütün gələcək copy/marketing/onboarding/App Store dilini bu termin filterinə tabe et; düzgün termin: "mütəxəssis / uyğunluq yoxlaması" |
| 2026-05-23 (axşam) | Plan zənciri ikiyə ayrıldı: **free = AI → ready (direct)**, **premium = AI → pending_review → mütəxəssis onayı → ready** (project-context.md §5.2) | Premium-ın görünən dəyər artımı approval gate-dən gəlir, AI quota artırışından deyil — quality differentiator + trust signal | Premium SLA ≤24 saat + "Planın yoxlanılır" placeholder + sample workout active; reject reason standartlaşdırılır; mütəxəssis fərdi koreksiya etmir |
| 2026-05-23 (resumed) | Phase 4 spec v1 (səhər) master inventardan divergent idi (L2 "Bədən tipi/Yuxu/Stres" + L3 "equipment-home/gym/language-pref" + L5 "pregnancy-postpartum/chronic-condition") — master (`ux-onboarding-questions §4.2/4.3/4.4`) kanonik qəbul edildi, spec **v2** kimi tam yenidən yazıldı: L2 A=Body&Metab, B=Activity&Lifestyle, C=Health&Injuries, D=Food, E=Equipment, F=Preferences · L3 ×9 (Measurement/TargetWeight/Injury/Pain/HealthSnapshot/Cycle-F2/DislikeFood/PowerMilestone/EventTarget) · L5 ×4 (MedicalSafety-PARQ+/SCOFF/RedFlagModal/HealthDataConsent) | Master `prd-user-profile-data-catalog` Master Field-dən törəyir → tək həqiqət mənbəyi. Spec divergensi PRD ilə ziddiyət yaradardı + .pen icrasında 19 frame səhv field-lərlə qurulurdu | Növbəti addım: .pen 03 Questions section altında 19 frame icrası (L2 row y=11500, L3 row y=12500, L5 row y=13500). `.pen build qeydləri v2` spec sənədin sonundadır |
| 2026-05-23 (resumed) | Phase 5 (strateji nüans 5-checklist) verify-driven status update: handoff §-də ⏳ NEW → ✅ COMPLETE. `.pen` `Cs4v3/z2ZaHx` body real verify edildi (`readDepth:4` ilə parent `eQcvv`-dən override görünür) + 4 sənəd grep ilə təsdiq: PRD §3.5 v3.3, project-context §0+§5.2, CLAUDE.md Qəti Qadağalar (3 yeni bənd), ux-phase2-copy-bank qadağa siyahısı. "canlı məşqçi" frazası heç bir copy-də qalmadı | Status verify olmadan handoff-da "yenidən düzəliş tələb edir" ⚠️ qeydi qalardı və növbəti session səhv yerdən başlayardı | Phase 4 spec v2 işinə birbaşa keçid mümkün oldu; gələcəkdə "strateji nüans 5-checklist patterni" tətbiq olunan kimi verify gate məcburi |
| 2026-05-23 | Supabase tooling stack: **MCP server (rəsmi, project-scoped + `read_only=true`) + `supabase/agent-skills` plugin (Anthropic-curated marketplace)** birlikdə qurulacaq | MCP = runtime icra (SQL/schema/edge func/advisors), agent-skills = RLS guardrail (security_invoker, exposed-schema bypass, SELECT-less UPDATE). Tək başına heç biri yetmir; CLAUDE.md "RLS atlama qadağa" qaydası ilə təbii uyğun | MCP yalnız dev project-ə bağlanır (production-a yox); supabase-kt KMP SDK ayrıca skill deyil — sonra Gradle dep kimi əlavə (Ktor 3.0+, Android min SDK 26). Rədd edilən alternativlər: composio wrapper, mcpmarket ayrı skill-lər, community supabase-sdk-patterns (JS-only) |
| 2026-05-23 (gec axşam) | Onboarding ön-axın canonical sıra: **`0.1 Splash → 0.2 Welcome → 01 Dil Seçimi → ...`** soldan-sağa. Splash brand-only (button/spinner/statusbar YOX). Welcome top-right `langPill` (globe + AZ + chevron) dil seçimi ekranına yeganə giriş nöqtəsidir | Splash = pure brand moment (PO-nun "dark + sarı kilidlə" qərarına uyğun); welcome-də header pill ayrı bottom-sheet/menu-dan üstündür — həm görünüş, həm affordance verir | Gələcək onboarding-prefix ekranları (məs. legal/version gate) eyni soldan-sağa axına `0.x` prefiks ilə əlavə olunur. Pill = dil keçidi default pattern |
| 2026-05-23 (gec axşam) | Welcome ekranı başlığı: **"MƏŞQİNƏ SAHİB ÇIX"** (uppercase 2-sətirli, accent eyebrow "FITAZ · MƏŞQ İDARƏÇİSİ") | User-as-hero kopi doktrinası (CLAUDE.md §0) — AI duygusal kahraman edilmir; istifadəçi məşq prosesini idarə edir | Bütün welcome / onboarding-açılış / paywall headline-larında bu səslə kopi yazılır; "AI sənə plan qurdu" tipi copy QADAĞAN |
| 2026-05-23 (gec axşam) | Canvas inter-screen transition pattern: **annotation pill** (`arrow-right` ikon + uppercase target ekran adı) — ekranlar arasındakı boşluğa qoyulur | Designer/dev/stakeholder section-arası keçidi dərhal görür; klikli komponent yox, sırf dizayn dokumentasiya. Bridge frame-dən kiçik versiya (single-pill, tək ox göstərir) | Yeni navigation pattern-lər üçün təkrar istifadə: welcome→language (icra edildi), paywall→checkout, calorie→sheet və s. |

## Findings / Sources

### 2026-05-16 — BMad UX ayağı
- **Sally (`bmad-agent-ux-designer`)** Phase 2 (Planning) agent-idir; PRD-dən sonra, Architecture-dan əvvəl işləyir. Pixel çəkmir — yazılı UX kontratı üretir.
- **Workflow:** `bmad-create-ux-design` → output `docs/ux-spec.md` (Information Architecture, user flows, screen list, interaction states, low-fi wireframe tarifləri, design system kararları).
- **Validation:** ux-spec-i PRD-yə qarşı doğrulayır — hər user story-nin design coverage-i var? primary journey'lər haritalanıb? tech stack tokens ilə uyumlu?
- **Source:** bmad-guide skill (`reference.md:18`, `reference.md:689`), Mintlify Sally agent page, BMad GitHub `getting-started.md`
- **Confidence:** high

### 2026-05-16 — Pencil MCP imkanları
- Claude Code-dan `/mcp` ilə bağlanır; çift istiqamətli canvas ↔ terminal axını.
- **Agent swarm** (6 paralel agent) — bir frame üçün eyni anda 3-6 varyant üretib müqayisə imkanı.
- **`.pen` library** — color/typography/spacing/component tokens üçün tək mənbə; yeni layihələrdə import edilir.
- **Design-to-code** — frame Claude-a oxudulub hədəf framework-də (Compose / SwiftUI / React) production kodu generasiya edilir; "pixel-perfect" reproduction iddiası case-study'lərdə təsdiqlənib.
- Hazırda **pulsuzdur**.
- **Source:** jeradbitner.com case-study, atalupadhyay.wordpress.com workflow guide, zenn.dev pixel-perfect article, geeky-gadgets review
- **Confidence:** high

### 2026-05-16 — Pipeline gotcha'ları (KMM + AZ context)
- Pencil-ı planning'dən əvvəl açmaq tipik anti-pattern: tokens olmadan ekran çəkib geri dönmək 1-2 həftə yeyir. → Əvvəl Sally, sonra Pencil.
- AZ string-lər: Pencil mockup-larında lorem yox, **real AZ string** istifadə edilməli; uzun Azerice sözlər ("təlimatlar", "müvəffəqiyyət") layout-u qırır — tasarım mərhələsində yaxalanmalı, dev'də yox.
- Sally default-da **offline-first state'ləri** və **≤8 onboarding ekranı** məhdudiyyətini avtomatik tətbiq etmir — `project-context.md`-dəki bu qaydaları Sally-yə hard input kimi qeyd vermək lazım.
- Pencil tokens → KMM körpüsü: Compose `Theme` + SwiftUI `Color/Font` extension-ları `shared/` resource-dan generasiya edilməli ki Pencil-da dəyişən token avtomatik hər iki platformaya axsın.

### 2026-05-16 — MVP üçün kritik ekran qrupu (Pencil swarm üçün ilkin namizədlər)
- Onboarding flow (8 step) — ≤8 sual invariant
- Paywall — video background + 2 seçim (trial / illik), 2.9x conversion rule
- Workout session — offline-first feedback, set/rep logger, rest timer
- Streak earned modal + Streak Freeze flow
- Calorie log + AZ food search
- Account delete + cancel subscription (Apple/Google launch blocker)
- "AI tərəfindən oluşturuldu" disclosure (Apple 2025)

### 2026-05-19 — Design-reference MCP ekosistemi (referans-alma alətləri)

**1) Mobbin MCP** — `claude mcp add mobbin --transport http https://api.mobbin.com/mcp`
- 621,500+ real app screen + 142,200+ flow + 1,651+ shipped app (rəsmi, May 12 2026 launch)
- Hədəfli sorğu: "43 fintech paywall göstər", "social app pull-to-refresh", "notification permission flow"
- Plan: €10/ay (yearly) — paid only, free plan-da MCP yoxdur. Design fazasında 2 ay = ~36 AZN
- **Use case fitnessApp üçün:** "12 fitness paywall with video background", "streak celebration modal patterns", "workout set logger UX (Strong + Hevy + Hevy Pro)", "food search empty state (MyFitnessPal + Yazio + LifeSum)", "onboarding height/weight input UX"
- **Confidence:** high. Sally + Pencil-dan əvvəl çalıştır → "boş canvas paralizi" tamamilə ölür

**2) Pencil MCP** (mövcud kararda var) — pulsuz, `.pen` git-tracked, 6 agent swarm, bidirectional canvas ↔ Claude, Figma paste-in dəstəyi.
- Yeni tapıntı: design rollback = code rollback (git commit ilə eyni snapshot)

**3) Figma MCP** — Feb 2026 bidirectional: `Code-to-Canvas` (Claude UI screenshot → editable Figma layers).
- Dev Mode required + remote server (most users) vs desktop (enterprise)
- **Tövsiyə:** MVP üçün SKIP. Pencil .pen + git workflow Figma-dan üstün (single-source-of-truth, no sync drift). Yalnız investor deck / marketing asset üçün lazım olarsa Faza 2.

**4) Design Inspiration MCP (YonasValentin, GitHub)** — pulsuz Mobbin alternativi
- Dribbble + Behance + Awwwards + Mobbin (public) + Pinterest axtarışı (Serper API ilə)
- **Bonus feature:** live website-dən token extraction (headless browser) — kompetitorun rəng paleti / typography-ni 1 sorğu ilə çıxara bilir
- **Use case:** BetterMe / Freeletics / Strong site-larından design token snapshot → Sally-yə input
- **Confidence:** medium (community, official deyil)

**5) UI Inspiration Search Server (TriangleLabs, Smithery)** — Dribbble-only keyword axtarış. Mobbin və ya YonasValentin varsa lazım deyil.

**6) AIDesigner MCP** — generic AI UI generation. Pencil swarm daha yaxşı, SKIP.

### 2026-05-19 — "Referans → Production code" pipeline (fitnessApp-spesifik)

```
A. REFERENCE INTAKE
   Mobbin MCP "fitness paywall 12 examples" → Claude screenshot batch
   ↓
B. CRITIQUE & FILTER
   Claude vs project-context.md invariants
   (≤8 onboarding, 2 paywall seçim, AI disclosure, AZ string uzunluğu)
   → "uyğun 4 referans + rejected 8 + reasoning"
   ↓
C. UX SPEC (Sally / bmad-create-ux-design)
   Hard inputs: referans seçimləri + ≤8 onboarding + offline-first state'lər
   → docs/ux-spec.md
   ↓
D. PENCIL .PEN TOKENS (canvas-ə əlavə)
   Color/Typography/Spacing → fitness-design-system.pen (git-tracked)
   ↓
E. PENCIL SWARM (6 agent × kritik ekran)
   Real AZ string ("təlimatlar", "müvəffəqiyyət") — lorem qadağan
   → 3-6 varyant per ekran → Claude review → seç
   ↓
F. DESIGN-TO-CODE (Pencil MCP)
   .pen frame → Compose Multiplatform composable
   → SwiftUI struct (Compose Multiplatform shared, native fallback)
   ↓
G. CODE-TO-CANVAS (opsiyonel, Faza 2)
   Compose screenshot → Pencil (drift check)
```

### 2026-05-19 — "Pencil ilə dizayn edərkən referans alma" use-case-ləri

1. **"Bu kimi olsun"** — Mobbin-dən 3 fitness paywall çəkir → Pencil canvas-a paste → "swarm: bu 3-ün strong elements-ni birləşdir, AZ video background ilə, 2 plan seçimi"
2. **"Rəqib paleti"** — Design Inspiration MCP live-extract → BetterMe.com → primary `#FF6B35` / secondary `#1A1F36` → `.pen` palette-ə əlavə → Sally-yə "burdan başla, dəyiş"
3. **"Edge case ekranları"** — Mobbin: "offline states fitness app" → 8 nümunə → Pencil-da AZ "İnternet yoxdur, son sessiyanız lokal saxlanır" string ilə yenidən
4. **"Streak earned modal"** — Mobbin: "duolingo streak + strava kudos + apple fitness rings" → 5 referans → swarm 6 varyant → AZ-kültür adapter ("Möhkəm dur!" copy)
5. **"Empty state"** — Mobbin: "food log empty state" → seç → AZ Top-200 yemək promosyonu ilə yenidən
6. **"Onboarding 7-sual"** — Mobbin: "fitness onboarding ≤8 steps" → 12 nümunə → keyfiyyət sıralama → swarm 6 varyant ≤8 step invariant ilə
7. **"Paywall A/B"** — Mobbin: "fitness paywall video background trial+annual" → swarm 6 varyant → 2.9x conversion rule check
8. **"Drift detection"** — Production Compose ekran screenshot → Claude vs orijinal `.pen` → "padding 16dp olmalı idi, 12 görünür"
9. **"Token sync"** — `.pen` rəng dəyişdi → Claude Code → `shared/commonMain/Theme.kt` + iOS `Color+Extensions.swift` auto-update
10. **"Layout yoxlama AZ string ilə"** — Pencil-da `replace lorem with az_strings.csv` → "Hesabınızı silməyə əminsinizmi?" 28-simvol → 360dp-də iki sətirə düşür → buton ölçüsünü artır

### 2026-05-19 — Tövsiyə edilən MCP qurğusu (fitnessApp)

| MCP | Status | Niyə | Quraşdırma |
|-----|--------|------|------------|
| Pencil MCP | ✅ ƏSAS | canvas + design-to-code + git-tracked + pulsuz | mövcud kararda |
| Mobbin MCP | ✅ TÖVSİYƏ | 621k real screen referans, paywall/streak/onboarding üçün ground truth, ~36 AZN 2 ay üçün ucuzdur | `claude mcp add mobbin --transport http https://api.mobbin.com/mcp` |
| Context7 | ✅ ARTIQ VAR | Compose MP / KMP / Supabase docs hallucination-a qarşı | mövcud |
| Design Inspiration MCP (YonasValentin) | ⚙️ OPSIYONEL | Mobbin paid almasaq pulsuz fallback + live-site token extract | `npx -y @yonasvalentin/design-inspiration-mcp` |
| Figma MCP | ❌ SKIP MVP | Pencil .pen ilə tək-mənbə işləyir; bidirectional Figma drift gətirir | Faza 2-də marketing assets üçün |
| AIDesigner / TriangleLabs UI | ❌ SKIP | Pencil swarm-dan zəif | — |

### 2026-05-19 — Best-implementation invariants (referansla işləyəndə)

- **Referansı kor-koranə kopyalama** — hər Mobbin sorğusundan sonra Claude `project-context.md` invariants-i checklist kimi keçməlidir (≤8 onboarding, AI disclosure, in-app cancel, ≥2 paywall seçim, AZ string-len)
- **Lorem qadağan** — bütün Pencil frame-lərdə real AZ string (Sally-nin ux-spec.md-dəki content draft-ından çəkilir)
- **Token tək-mənbə** — `.pen` → `shared/commonMain` resource → Compose `Theme` + SwiftUI extension auto-generated. Heç vaxt platform-da manual rəng/typograhy yazma
- **Swarm output-u review olmadan ship etmə** — 6 varyantdan ən az 1-i AZ string + offline state + AI disclosure-u sındırır
- **Cost log** — Mobbin sorğusu pulsuzdur (paid plan ilə), amma Pencil swarm hər agent run-ı LLM çağırışıdır → Claude Code session budget içində izlə

### 2026-05-23 (resumed) — Spec-vs-master divergensi şablonu
- Phase 4 spec səhər versiyası `ux-onboarding-questions §4.2/4.3/4.4` master inventardan ayrılmışdı: L3 qatında **0 ekran üst-üstə düşmə**, L2 qrup mövzuları və L5 ekran adları konseptual yaxın amma struktur fərqli.
- Spec-müəllifin yenidən-qruplaşdırması master sənədə back-port olunmamışdı → iki paralel həqiqət mənbəyi.
- **Qayda:** yeni spec sənədi yazılan kimi master inventar ilə **field-by-field cross-check** məcburi; spec qruplaşmasını dəyişirsə ya master yenilənir, ya da spec geri çəkilir.
- Grep verify (2026-05-23 resumed): köhnə v1 sözləri (`Bədən tipi`, `equipment-home`, `language-pref`, `pregnancy-postpartum` standalone) başqa heç bir planning artifact-də yoxdur — divergensiya yalnız spec faylında qalmışdı.
- **Confidence:** high

### 2026-05-23 (resumed) — .pen ref descendant override verify pattern (re-confirmed)
- Override-i (məs. AI Disclosure body `Cs4v3/z2ZaHx`) düzgün oxumaq üçün **parent ref ID-ni** (`eQcvv`) `readDepth:4` ilə oxu — `descendants` object orada görünür.
- Bare `z2ZaHx` (descendant ID-si) oxunsa **reusable component-in orijinal content-i** görünür, override-lər YOX.
- Update yazısı `eQcvv/Cs4v3/z2ZaHx` path-style ilə doğru işləyir (bax 2026-05-23 axşam entry — eyni pattern).
- Phase 5 verify-də işlədildi: 1 batch_get çağırışı = strateji düzəlişin .pen-də doğru tətbiq olunduğunu təsdiqlədi.
- **Confidence:** high

### 2026-05-23 (resumed) — L5 frame anatomy invariant
- L5 medical ekranları (#16-#19) **L2/L3 şablonundan struktur olaraq fərqlənir**:
  - Progress strip YOX (onboarding axın hissəsi deyil — istənilən anda tetiklənə bilər)
  - Üstdə sticky `$warning` 36pt banner (ikon 20 + 13/500 "Təhlükəsizlik yoxlaması — tibbi məsləhət deyil")
  - Skip YALNIZ sensitive-consent guard ilə (SCOFF #17); RedFlagModal terminal-dismiss-only; HealthDataConsent + MedicalSafety hard-məcburi
  - Back ikon var (modal yox, ekrandır)
- **`.pen` icra implikasiyası:** master `question_template` üzərindən L5 üçün ayrıca variant clone et — L2/L3 frame copy-edib variant dəyişdirmək yetməz (sticky banner + progress strip silinmə + skip qaydası).
- **Confidence:** high

### 2026-05-23 (gec axşam) — Pencil tooling gotchas (splash/welcome session)
- **Cross-file komponent/variable import dəstəklənmir** — `imports` sahəsi schema-da olsa da `batch_design`-a yeni filePath verəndə aktiv sənədə yazır. "Design system ayrı .pen faylda" istəyi → eyni faylda dedicated section pattern-i ilə qarşılanmalıdır.
- **Yeni .pen faylı CLI-dən yarana bilməz** — `touch`-la boş fayl ya da `Write` ilə plaintext invalid (binary/encrypted format). Yeni .pen yalnız Pencil tətbiqində manual yaradılır.
- **Themed variables array formatı:** `set_variables`-də `value: [{value, theme: {axis: "name"}}...]` — theme axis avtomatik registr olur. Frame-ə `theme:{mode:"light"}` qoyulanda bütün descendant `$variable` ref-ləri o mod-a resolve olur. Light/dark kopya üçün yalnız theme override yetir.
- **`replace:true` ilə theme axis-ə qayıt:** dark-only-a qayıtmaq üçün `set_variables({...}, replace:true)` ilə tam variable seti single-value göndər. `document.themes` map-də köhnə axis qeydi qala bilər (ignored, zərərsiz).
- **Section frame transparent fill → ağ kanvasa render:** ağ mətnli (`$ink` dark mode-da `#FFFFFF`) section-u standalone screenshot edirsən ağ-ağ görünür. Bu screenshot artefaktıdır — `batch_get` ilə struktur təsdiqi etibarlı.
- **`accent` təkliyi light mode-da kontrast yaradır:** lime accent həm button fill (lime qalmalı), həm mətn rəngi kimi işlədiləndə ağ fonda mətn görünməz. Gələcəkdə ayrı `accent-text` themed token tələb olunur. PO dark-only qərarı ilə hazırda qapandı, amma kontrast pattern məsələsi qeyd dəyərlidir.
- **Spawn-edilmiş agent frame copy + theme override-i etibarsız yerinə yetirir:** 7 light-kopya tapşırığı uğursuz icra edildi (sıfır kopya yarandı). Orchestrator özü 7 sadə `C()` çağırışını daha sürətli + etibarlı edir. Pencil-yazma fazasında agent paralelliyini sadə struktur-əməliyyatlar üçün istifadə etmə.
- **`alert-triangle` lucide-də yox — `triangle-alert` doğrudur.** Splash + warning banner-lərdə bu fərq saatlar yeyə bilər. CI gate dəyəri var: .pen save-də lucide ikon adlarını manifest-ə qarşı yoxla.
- **Volt brand tokens iki .pen faylında fərqlidir:** `app_design.pen`-də canonical `accent #E6FF00` (saf elektrik-sarı), köhnə `onboarding_flow.pen`-də `#DAFB3E` (lime). Canonical referans: `project-context.md §1b`. Yeni .pen yaradılanda token-lər oradan oxunur.
- **PO qərarı (2026-05-23):** dark-only brand kimi kilidləndi. Light mode rədd. Gələcəkdə hər ekran/komponent yalnız dark variant. Token-lər single-value (themed array yox), `theme:{}` property frame-lərdən təmizlənə bilər (harmless qalsa da).

## Errors Encountered

| Error | Attempts | Resolution |
|-------|----------|------------|
| Design system frame böyüdükdə (themes section əlavə → 4675→5144) altda yerləşmiş onboarding ekranları çərçivənin altına düşdü ("dark in üstüne gelir" geri-bildirimi) | 1 (visual report) | Agent-yaratmadan sonra `snapshot_layout maxDepth:0` ilə frame hündürlüyünü yenidən ölç, altdakı top-level node-ları (label, pill, ekran sırası) yeni y-koordinatlara repozisiya et |

---

## Progress Log

- 2026-05-16 23:31 — Research initialized (parallel to active feature [[determine-scope-of-start-up]] — pointer NOT switched)
- 2026-05-16 23:31 — Phase 1 + Phase 2 partial complete from initial chat session
- 2026-05-19 — Referans-MCP ekosistemi tam araşdırıldı (Mobbin, Pencil, Figma, YonasValentin, AIDesigner). Tövsiyə: Mobbin + Pencil + Context7. Figma MVP skip. 10 referans-istifadə case-i çıxarıldı.
- 2026-05-23 15:35 — Resumed
- 2026-05-23 — Paused on 2026-05-23

## Notes for Next Session

- Pencil MCP-ni proyektə real bağla (`/mcp` ilə test et)
- İlkin `.pen` tokens siyahısını çıxar (fitness-design-system.pen)
- `bmad-create-prd` faktiki çağırışından əvvəl `project-context.md`-dəki UX-relevant qaydaları (≤8 onboarding, paywall 2 seçim, streak, AI disclosure, offline-first) Sally üçün ayrıca "hard input checklist" kimi hazırla

### 2026-05-23 — Pencil MCP icra reallıqları (real .pen iş təcrübəsi)
- **Tək-yazan model:** Pencil MCP eyni anda bir document open saxlayır; paralel agent yaza bilməz. Agent paralelliyi yalnız mətn spec hazırlama fazasında istifadə olunmalıdır (copy bank, layout plan, cross-ref matrix).
- **Layout konvensiyaları:** Top-level frame-lər mütləq koordinatlarda yaşayır (flexbox yox); screen-lər row-larda 420px addım ilə (375 wide + 45 gap). Section label format: 2-digit caption + 54/800 mega title + 17/normal subtitle.
- **Schema gotcha-ları:** (a) lucide-də `alert-triangle` yox, `triangle-alert`. (b) Horizontal layout-da iki child width:fill_container sıxışdırır — yalnız biri fill_container ola bilər. (c) Frame-də fill child + fit parent dövrü → 0 collapse.
- **Screenshot caching:** batch_design-dan dərhal sonra yeni frame screenshot-da qara render olunur; batch_get isə content-i doğru göstərir. Pencil app-də manual refresh lazım.
- **Volt token implementation:** color vars (`$accent`, `$bg`, `$surface`, `$ink`, `$danger`, `$warning`, `$moss`...) batch_design-da string referans kimi (`fill:"$accent"`) işləyir — kanonik mənbə project-context.md §1b.
- **Source:** real .pen redaktə təcrübəsi (33 ekran reposition, Cover frame, bridge frame, 22 cross-ref chip)
- **Confidence:** high

### 2026-05-23 — UX sənəd arxitekturası (yenidən qurulma)
- **Üç sənəd modelinə keçid:**
  - `prd-auth-onboarding` v3.1 (funksional-yalnız PRD — FR/AC/state machine)
  - `ux-auth-onboarding` v1.2 (UX brief — axın səviyyəli; ekran inventarı yalnız axın kontekstində)
  - `ux-onboarding-questions` v1.0 (YENİ — sual ekranı şablonu, anatomy, variant matrisi, L1/L2/L3/L5 inventarı)
- **Niyə split:** sual ekranı şablonu onboarding axınından struktur olaraq müstəqildir; weekly check-in (Faza 2), settings re-prompt, L3 progressive triggerlər eyni şablonu istifadə edir. Tək sənəddə saxlamaq oxunmazlıq yaradırdı.
- **`.pen` reflection:** fayl sectionları artıq sənəd arxitekturasını mirror edir (Onboarding section ↔ ux-auth-onboarding · Questions section ↔ ux-onboarding-questions).
- **Source:** ux-auth-onboarding v1.2 changelog, ux-onboarding-questions v1.0
- **Confidence:** high

### 2026-05-23 (axşam) — Pencil `descendants` override verification pattern
- Ref instance içində bare descendant node ID-ni (`z2ZaHx`) `batch_get` ilə oxuyanda **override-i yox, reusable component-in orijinal content-i** görünür.
- Verification üçün **parent ref ID**-ni (məs. `Cs4v3`) oxumaq lazımdır — `descendants` object orada görünür.
- `eQcvv/Cs4v3/z2ZaHx` path-style `U(...)` update doğru işləyir; bare `z2ZaHx`-ə update reusable component-i dəyişər (ref instance-ları yox).
- **Source:** Phase 5 AI Disclosure copy düzəliş təcrübəsi
- **Confidence:** high

### 2026-05-23 (axşam) — Strateji nüans 5-checklist patterni
- Bir strateji çərçivə düzəlişi (məs. trainer → human approval) **5 sənədə** yayılır: `.pen` body + PRD §X + UX brief changelog + CLAUDE.md prohibitions + project-context.md §0 + §5.
- Yalnız birində dəyişiklik = digər sənədlərdə köhnə çərçivə sızır → mesaj uyğunsuzluğu.
- Gələcək copy/strategy düzəlişləri üçün bu 5-checklist patterni təkrar istifadə olunmalı.
- **Confidence:** high

### 2026-05-23 — Supabase tooling ekosistemi (research)
- **MCP server (rəsmi, Feb 2026 Claude connector):** 32 alət — SQL exec, schema modify, table/extension list, edge functions deploy, branches, **database advisors** (security/performance lint), TypeScript types gen. Config: `https://mcp.supabase.com/mcp?project_ref=<REF>&read_only=true&features=database,docs,debugging,...`. **Production-a bağlama** (rəsmi xəbərdarlıq).
- **supabase/agent-skills v0.1 (Mar 2026):** RLS pitfall-larını catch edir — (a) views default RLS bypass → `security_invoker=true` məcburi, (b) UPDATE policy-də SELECT olmadan səssiz 0 row, (c) exposed schema-da RLS atlama. Opinionated schema akışı: `execute_sql` ilə birbaşa iteration → `database advisors` → yalnız sonra migration commit. **İki marketplace:** Anthropic-curated (`anthropics/claude-plugins-official` → `supabase@claude-plugins-official`, tövsiyə) və Supabase community (`supabase/agent-skills`, eyni içərik tez yenilənir).
- **supabase-kt:** community KMP client (rəsmi tövsiyə), Gradle dep — skill deyil. Modullar: auth-kt, postgrest-kt, functions-kt, storage-kt, realtime-kt, compose-auth, compose-auth-ui, coil-integration. Ktor ≥3.0, Android min SDK 26.
- **Rədd edilən alternativlər:** composio Supabase MCP (wrapper, layer əlavə), mcpmarket ayrı supabase-migrations/rls-audit skill-ləri (agent-skills əhatə edir), `supabase-sdk-patterns` claudecodeplugins.io (JS-only).
- **Source:** supabase.com/blog/supabase-is-now-an-official-claude-connector, supabase.com/blog/supabase-agent-skills, supabase.com/docs/guides/getting-started/mcp, supabase.com/docs/guides/ai-tools/plugins
- **Confidence:** high

### 2026-05-23 — Pencil schema gotcha-ları (verification)
- **Screenshot caching bug:** `batch_design`-dan dərhal sonra yeni frame `get_screenshot`-da qara render olunur (Pencil render cache yenilənmir). `batch_get` content-i doğru göstərir. Workaround: visual verification üçün `batch_get` + `snapshot_layout` istifadə et; screenshot yalnız mövcud frame-lər üçün etibarlı.
- **`width:"fill_container"` antipattern:** horizontal layout-da iki sibling-də `fill_container` + üçüncüsü `fit_content` istifadə olunsa, sıxışdırma yaranır — birinci fill child bütün space-i alır, sonrakı sibling clip olunur. Qayda: bir axis-də yalnız BİR `fill_container` child.
- **lucide ikon adlandırma:** `alert-triangle` mövcud deyil — doğru ad `triangle-alert`. CI gate dəyəri var: `.pen` save-də ikon adlarını lucide manifest-ə qarşı yoxla.
- **Source:** real .pen icra təcrübəsi (Cover frame, bridge frame yaratma)
- **Confidence:** high

---

## Session Handoff (2026-05-23 — yeni session-da davam üçün)

> Bu bölmə fərqli bir Claude Code sessionunda işin davam etdirilməsi üçün lazım olan tam kontekstdir. Mövcud feature.md + notes.md SessionStart hook-u ilə avtomatik yüklənir.

### Hara qaldıq — son durum (2026-05-23 axşam)

**`.pen` faylının cari layout (kanonik):**

| y koord | Sahə | Vəziyyət |
|---------|------|----------|
| y=−2440 | label/cover ("00 İçindəkilər") | ✅ qurulub |
| y=−2270, h=920 | 00 Cover & Index frame (1360w) | ✅ qurulub — hero + 4 section card + legend + footer |
| y=−1810 | label/design-system ("01") | ✅ qurulub |
| y=0, h=4697 | 01 Design System frame (1360w, mövcud) | ⚠️ Phase 3 expand qalır (9 sub-section spec hazır) |
| y=0, x=1480 | Reusable components column (7 component) | ✅ |
| y=0, x=1820 | V7 Button State Variants (DS-ə daxil edildi) | ✅ |
| y=5930 | label/onboarding ("02") | ✅ |
| y=6126 row 1 | 6 ekran: Lang·Welcome·AgeGate·Parental·**ProfilePreview**·AIDisclosure | ✅ |
| y=7138 row 2 | 6 auth ekranı: AuthGate·Signup·Login·Confirm·ResetEmail·ResetForm | ✅ |
| y=7960 row 3 | 5 state variant inline (V4·V2·V1·V3·V6) | ✅ |
| y=8870, h=340 | Bridge 02→03 (1100w) | ✅ |
| y=9230 | label/questions ("03 SUALLAR") | ✅ |
| y=9450 row | 8 ekran: Q1·Q2·Q3·V5·Q4·Q5·Q6·Q7 | ✅ |
| y=10380 | label/post-onboarding ("04 POST-ONBOARDING & SETTINGS") | ✅ |
| y=10580 row | 6 ekran: Pregnancy·Paywall·Logout·DeleteConf1·DeleteConf2·ProCoach | ✅ |

**Silindi:**
- 21 Sample Workout Preview (m8M6cD) — paywall post-dashboard olduğu üçün artıq yer yoxdur
- Köhnə standalone "States" section label — V variantları parent-lərinin yanına inline yerləşdi

### ⚠️ KRİTİK NÜANS — Növbəti session-da düzəliş tələb edir

**Mövzu:** AI Disclosure (#13) və premium feature framing-i.

- **Yanlış (cari .pen + PRD-də yazılıb):** "Plan canlı məşqçinin uyğunluq yoxlamasından keçir" — bu "trainer" feature kimi səslənir.
- **Düz mövqe (user 2026-05-23 axşam dəqiqləşdirdi):** AI elmi-əsaslı plan qurur. Premium istifadəçilərdə plan göndərilmədən əvvəl bir **insan (mütəxəssis) yoxlayıb onaylayır**. Bu **trainer feature DEYİL** — bu human-in-the-loop **quality gate / approval**-dır. Plan AI-dən gəlir, insan onayı təsdiqdən ibarətdir, fərdi trainer-istifadəçi münasibəti yoxdur.

**Növbəti session-da edilməli (NÜANSI DÜZGÜN OTURDA):**

1. `.pen` AI Disclosure (#13 / eQcvv) body copy yenilə (mütləq):
   - **YENİ AZ body:** "Plan AI tərəfindən elmi əsaslarla qurulur — son qərar səndədir, istədiyin vaxt dəyişə bilərsən. **Premium istifadəçilərdə plan, göndərilmədən əvvəl bir mütəxəssis tərəfindən yoxlanılıb onaylanır.** Bu tibbi məsləhət deyil — sağlamlıq probleminiz varsa məşqdən əvvəl həkimə müraciət edin."
   - Node ID: `eQcvv/Cs4v3/z2ZaHx` (info-notice text descendant)

2. `prd-auth-onboarding-2026-05-22.md` §3.5 AZ body yenidən yenilə — "canlı məşqçi" sözü silinsin, "mütəxəssis onayı / yoxlaması" gəlsin.

3. `ux-auth-onboarding-2026-05-22.md` v1.3 changelog-ı düzəlt — "managed trainer review" frazası "human approval / quality gate" frazasına dəyişsin.

4. `CLAUDE.md` Qəti Qadağalar bölməsi:
   - **Saxla:** "trainer və ya professional coaching xüsusiyyətini vəd etmə" qaydası — hələ keçərlidir (trainer ≠ human approval).
   - **Əlavə et:** "**Premium plan göndərilmədən əvvəl insan-onayı** (mütəxəssis yoxlaması, trainer ilə fərdi münasibət DEYİL) MVP scope-undadır. Onboarding-də şəffaf qeyd olunur (AI Disclosure §3.5)."

5. `project-context.md` §0 və §5 yeniləmə (gerek):
   - Plan yaratma axınında "AI plan generation → human approval gate (premium only) → user delivery" zənciri.
   - Free plan: yalnız AI plan (human gate yox).
   - Premium: AI + human approval.

6. `feature.md` "Decisions" cədvəlində 2026-05-23 entry (trainer review) → yenidən ifadə et: "Premium-da human approval gate (quality check, trainer DEYİL)".

### Phase status

| Phase | Status | Detal |
|-------|--------|-------|
| **Phase 1** | ✅ COMPLETE | Section structure, screen relocation, cover frame, bridge, cross-ref chips, AZ screen names, AI disclosure ilk copy (yenidən düzəliş tələb edir — bax yuxarı) |
| **Phase 2** | ⏳ PENDING | AZ copy bank-ı 25+ ekrana tətbiq (agent output hazır — `notes.md`-də [refs] entry-ə bax). Welcome ekranı artıq polished. Qalan: Q1–Q7, ProfilePreview, AuthGate, Email forms, V variants, Pregnancy nudge, Paywall, Delete confirms, Pro coaching. |
| **Phase 3** | ⏳ PENDING | Design System 9 sub-section expand: cover · colors (18 swatch) · typography (8 stil) · spacing+radius · iconography (16 icon grid) · buttons (5 state ×3 button type) · selection (7 komponent) · forms+feedback · principles+a11y kontrast cədvəli. Agent spec hazır. |
| **Phase 4** | ⏳ PENDING | 19 yeni L2/L3/L5 sual ekranı yarat: L2 A-F akkordeon (6) · L3 sheets (9) · L5 medical safety (4). Şablon: §1 anatomy (ux-onboarding-questions §1). |
| **Phase 5** | ✅ COMPLETE (2026-05-23 verified) | Strateji nüans düzəliş 5/5 yerdə icra olunub: (a) `.pen` #13 `Cs4v3/z2ZaHx` body — "mütəxəssis tərəfindən yoxlanılıb onaylanır", "canlı məşqçi" frazası yox; (b) `prd-auth-onboarding` §3.5 v3.3; (c) `ux-auth-onboarding` v1.3; (d) `CLAUDE.md` Qəti Qadağalar; (e) `project-context.md` §0 + §5.2; (f) `ux-phase2-copy-bank` qadağa siyahısı. Termin sabit: "mütəxəssis yoxlaması" / "uyğunluq yoxlaması". |

### Agent output-ları (notes.md-də [refs] kimi yazılıb, hələ tətbiq olunmayıb)

Phase 1-də 5 paralel research agent göndərildi, output-lar hazırdır:

1. **Cover & Index spec** — qurulub və .pen-də tətbiq olundu ✅
2. **Design System max-detail spec** — 9 sub-section content spec, hələ .pen-ə qoyulmayıb (Phase 3 işi)
3. **Cross-ref matrix** — 30 sətirlik cədvəl, chip mətnləri .pen-də tətbiq olundu ✅
4. **AZ copy polish bank** — 26 ekran üçün title/sub/CTA/visual polish notu, hələ ekran-ekran tətbiq olunmayıb (Phase 2 işi)
5. **Visual refactor catalog** — 12 non-AI mockup pattern (real AZ names, off-round numerics, asymmetric rhythm), hələ tətbiq olunmayıb (Phase 2 dövründə per-screen)

Agent output-larını yenidən görmək üçün — onlar bu transcript-də mövcuddur, lakin yeni session-da yenidən dispatch etmək lazım gələ bilər (yuxarıda dispatched prompt-lar feature.md-də yox; ehtiyac olarsa Agent çağırışlarını re-issue et).

### Test edilməli (yeni session-da)

- `.pen` faylı açıq olmalı (Pencil desktop app) — yeni session-da `mcp__pencil__open_document` ilə `app/design/mobile/app_design.pen` aç
- Screenshot ilə Cover frame (lrFvm), bridge (tGzFk), questions row, post-onboarding row visual yoxla
- AI Disclosure (#13 / eQcvv) — copy yenilənibmi (nüansa görə)
- 02 → 03 bridge — 7 Q-dot strip + ox + Section 03 pointer doğru render olunurmu

### Açıq qərar/sual

- **Strateji nüans (insan onayı vs trainer):** user 2026-05-23 axşam aydınlaşdırdı. Yenilənmə icra edilməlidir (Phase 5).
- **L2 group sırası A-F:** user araşdırması ilə validate olunmalı (P2-A, ux-auth-onboarding §12.2-də).
- **Marka adı:** `.pen`-də "FitAz" placeholder, PRD-də "fitnessApp" — UX-10 açıq sual.
