---
stepsCompleted: [1, 2, 3, 4, 5]
inputDocuments:
  - docs/project-context.md
  - CLAUDE.md
workflowType: 'research'
lastStep: 1
research_type: 'market'
research_topic: 'Azərbaycan bazarına yönəlik AI-dəstəkli fitness tətbiqi — funksionallıq və rəqib analizi'
research_goals: 'MVP funksionallıq qərarlarını məlumat əsasında əsaslandırmaq və 4 əsas rəqibə (BetterMe, MyFitnessPal, Nike Training Club, Freeletics) qarşı differensiator-ları doğrulamaq'
user_name: 'Balaagha'
date: '2026-05-12'
web_research_enabled: true
source_verification: true
---

# Market Research: Azərbaycan AI-Fitness Tətbiqi

**Tarix:** 2026-05-12
**Müəllif:** Balaagha
**Növ:** Market Research (BMad)

---

## Research Initialization

### Confirmed Understanding

- **Topic:** Azərbaycan bazarına yönəlik AI-dəstəkli fitness tətbiqi
- **Goals:**
  1. MVP funksionallıq scope-unu (P0/P1) bazar məlumatı ilə doğrulamaq
  2. 4 rəqibin (BetterMe, MyFitnessPal, Nike Training Club, Freeletics) feature, qiymət və zəif tərəflərini sənədləşdirmək
  3. 3 əsas differensiatorun (AZ dili + AI video/3D + ev/zal hibrid) bazar dəyərini ölçmək
  4. 5–10 AZN/ay qiymət hipotezini test etmək
- **Date:** 2026-05-12
- **Context inputs:** `docs/project-context.md`, `CLAUDE.md`

### Scope

**Fokus sahələri:**
- Azərbaycan smartphone/internet/fitness app penetrasiyası
- Müştəri seqmentləri (ev idmanı vs zal, dil tərcihi, ödəmə qabiliyyəti)
- Rəqib feature matrix + qiymət modelləri + AZ market mövcudluğu
- Strateji tövsiyə: hansı feature-lar P0, hansılar P1, hansılar Faza 2

**Coğrafi əhatə:** Azərbaycan (əsas), regional referans üçün Türkiyə/MDB

**Biznes məqsədi:** Market entry — MVP launch 2026 sonu / 2027 əvvəli

### Methodology

- Çoxlu mənbə ilə triangulation (Statista, App Annie/data.ai, Sensor Tower, Google Play, App Store, rəqib veb-saytları)
- Hər kritik iddia üçün ən azı 2 müstəqil mənbə
- Qeyri-müəyyən məlumat üçün Confidence: Low/Medium/High etiketi
- Solo developer + 4 aylıq MVP konteksinə uyğun praktik tövsiyələr

### Workflow

1. ✅ Initialization & scope (cari)
2. Customer insights & behavior
3. Competitive landscape
4. Strategic synthesis & recommendations

---

## Customer Behavior and Segments

### Market Context (Macro)

**Azerbaijan (əsas bazar) — 2025 sonu:**
- 9.27M internet istifadəçisi (penetrasiya **89.0%**) — regional lider
- 12.3M mobile cellular connection (118% population)
- 7.61M social media identities (73.1%)
- Cell phone sahibliyi **84%** (Gürcüstan 93%, Ermənistan 77%)
- Smartphone bazarı 2025-də +2% həcmli böyüyür, 2029-ə qədər 2.3M cihaz/il
- Orta aylıq əmək haqqı: **~1,086 AZN** (~$640), Bakı: **~1,375 AZN** (~$810), YoY +9.4% (2025)
- _Confidence: High — DataReportal, CEIC, Statista_
- _Source: [DataReportal Digital 2026 Azerbaijan](https://datareportal.com/reports/digital-2026-azerbaijan), [Crocusoft Digital 2025](https://crocusoft.com/en/Blog/Details/digital-2025-azerbaijanen), [Report.az salary](https://report.az/en/finance/average-monthly-salary-in-azerbaijan-up-by-9-7-in-jan-aug-2025), [Eurasianet survey](https://eurasianet.org/the-caucasus-and-central-asia-are-well-wired-survey)_

**Global Fitness App Benchmark — 2025:**
- Qlobal fitness app bazarı **$12.1B (2025) → $33.6B (2033)**, CAGR 13.4%
- 2025-də 3.6B downloads (iOS+Play), YoY +6%
- Android dominant — affordability driver
- "Gyms & Fitness" sub-genre top, YoY +3%
- _Confidence: High — Sensor Tower, Grand View, Polaris_
- _Source: [Sensor Tower State of Mobile H&F 2025](https://sensortower.com/blog/state-of-mobile-health-and-fitness-in-2025), [Polaris Fitness App Market](https://www.polarismarketresearch.com/industry-analysis/fitness-app-market), [Grand View Research](https://www.grandviewresearch.com/industry-analysis/fitness-app-market)_

**Regional benchmarks:**
- Turkey: BetterMe top-tier global player kimi qeydiyyatdadır, AZ-spesifik məlumat yoxdur
- Russia/CIS: Smartphone adoption 2024-2030 yüksək, fitness app penetrasiyası qərb bazarlarından geri
- Georgia: Caucasus-da digital adoption-da öndə, lakin əhali kiçik (~3.7M)
- _Source: [Statista CIS smartphone adoption](https://www.statista.com/statistics/1020309/russia-cis-smartphone-adoption/)_

### Customer Behavior Patterns

**Subscription churn (kritik — bizim 5–10 AZN/ay modelimizə birbaşa təsir):**
- Aylıq plan retention: **17.0%** (annual planlarla müqayisədə dramatik aşağı)
- Ümumi fitness app churn: 8.9% (2023) → **7.2% (2025)** — hibrid modellərin yüksəlişi
- 30-günlük retention orta: **27.2%**, top tier: 47.5%
- D1 retention: ~23%; D30: 3–10% (orta apps)
- **Mövsümi swing**: Yanvar pik → yay artım → payız enmə
- _Behavior driver:_ Yeni il qərarları, fəsil dəyişikliyi, yay öncəsi forma
- _Decision habit:_ Aylıq → illik plan upsell **kritik strategiya**
- _Confidence: High — Business of Apps, Lucid, Digital Yield Group_
- _Source: [Business of Apps Health & Fitness Benchmarks 2026](https://www.businessofapps.com/data/health-fitness-app-benchmarks/), [Lucid retention metrics](https://www.lucid.now/blog/retention-metrics-for-fitness-apps-industry-insights/), [Digital Yield Resolutioner churn](https://digitalyieldgroup.com/blog/health-fitness-apps-the-resolutioner-churn-problem/)_

### Demographic Segmentation (Global → AZ Extrapolation)

- **Cins:** Qadınlar fitness app istifadəçilərinin **60%**-i qlobal
- **Yaş:** 30-39 yaş ən aktiv (~41% tracking app istifadə edir, 2016 data, böyüyür)
- **Generation:** Millennials + Gen Z = bütün gymgoers-in 80%-i, online/app workout-larin 89%-i
- **Coğrafi (AZ):** Bakı 84% smartphone, regionlarda daha aşağı — şəhər-mərkəzli go-to-market
- **Gəlir (AZ):** Orta aylıq Bakı maaşı $810 → 10 AZN/ay (~$6) bir Spotify abunə ekvivalentidir, ödənilə bilən
- _Source: [Lifefitness Millennials/GenZ](https://www.lifefitness.com/en-us/customer-support/education-hub/blog/younger-generations-shaping-strength-training), [PMC fitness apps gender age](https://pmc.ncbi.nlm.nih.gov/articles/PMC10469511/)_

### Psychographic Profiles (AZ-spesifik hipotezlər)

- **Values:** Lokal dil + lokal qiymət = etibar; "Bakılı" estetik (görünüş + sağlamlıq dual driver)
- **Lifestyle:** İş-həyat balansı zəif (uzun iş saatları), məhdud zal əlçatanlığı regionlarda
- **Attitudes:** İngilis dilli rəqib apps-a qarşı "anlamadığım üçün davam etmədim" sürtüşməsi (qaynaqsız, ANEC-dotal)
- **Personality:** Status-conscious, social media-da görünmə (Instagram + TikTok yüksək) → fotosessiya / progress paylaşımı driver

### Recommended Customer Segments (3 əsas)

**Segment A — "Şəhərli professional qadın" (PRIMARY)**
- 25-35 yaş, Bakı/Sumqayıt, orta+ gəlir ($600-1200/ay)
- Çəki idarəetməsi, post-pregnancy fitness, esthetic-driven
- Ev idmanı 70%, zal 30% (uşaq baxımı + vaxt məhdudiyyəti)
- AZ dili məcburi, İngilis ikinci, Rus seçim
- **Ödəmə hipotezi:** 7-10 AZN/ay ödəyə bilər
- Acquisition: Instagram, TikTok, Bakılı influencerlər

**Segment B — "Gənc kişi zal-go-er" (SECONDARY)**
- 18-30 yaş, kişi, Bakı + regional mərkəzlər
- Əzələ qazanma, güc artırma, görünüş
- Zal 60%, ev 40% (ev — qışda və ya zaldan istirahət günü)
- Rus + AZ + İngilis qarışıq dil əlçatanlığı
- **Ödəmə hipotezi:** 5-8 AZN/ay (qiymət həssas, premium üçün dəlil tələb edir)
- Acquisition: YouTube AZ/RU fitness, TikTok

**Segment C — "Yaşlı sağlamlıq-driven istifadəçi" (TERTIARY)**
- 35-50 yaş, hər iki cins, "sağlam qalmaq", kilo idarəetmə, qan təzyiqi
- Ev idmanı 85% (zal getmək vərdişi az), aşağı intensity məşqlər
- AZ + Rus dili güclü tələb, İngilis məhdud
- **Ödəmə hipotezi:** 5-7 AZN/ay (dəyər sübutu lazım), illik plan daha cəlbedici
- Acquisition: Facebook, WhatsApp paylaşım, ailə tövsiyəsi

### Behavior Drivers and Influences

- **Emotional:** Görünmək (özgüvən, sosial), "yenidən başlamaq" momenti (yanvar, yay öncəsi, böyük həyat dəyişikliyi)
- **Rational:** Vaxt qənaəti (zal getməyə vaxt yoxdur), pul qənaəti (zal abunəsi 50-150 AZN/ay vs app 5-10 AZN)
- **Social:** Bakı'da fitness influencer ekosistemi yüksəlir, Instagram before/after kontent yüksək paylaşımlı
- **Economic:** Yerli AZN qiymət istifadəçi üçün rəqibin USD qiymətindən psixoloji olaraq qat-qat aşağı görünür

### Customer Interaction Patterns

- **Research:** App Store/Play Store axtarış AZ açar sözlərlə → tək tək — AZ kontent boşluğu
- **Decision:** Pulsuz trial → ilk 7 gün retention kritik; AZ dili görmək convert-i artırır
- **Post-purchase:** Push notifikasiya AZ-da, streak motivasiya, social paylaşım
- **Loyalty:** İllik plana keçiş üçün yanvar discount, progress milestone-ları + cohort streak

---

## Customer Pain Points and Unmet Needs

### Language & Localization Pain Points

- **BetterMe Türk istifadəçiləri menyu və veb dili boşluğu bildirir** — App-da 35+ dil olsa da billing/support səhifələri yarımçıq tərcümə. _Source: [Sikayetvar BetterMe](https://www.sikayetvar.com/en/betterme-us)_ — Confidence: Medium
- **MyFitnessPal dəstəklənən dillərində AZ yoxdur**; Nike Training Club RU və TR var, AZ yox. _Source: [MFP language settings](https://support.myfitnesspal.com/hc/en-us/articles/360032623951), [Nike Help](https://www.nike.com/help/a/device-languages)_ — Confidence: High
- **Russian-translated apps (SOTKA və s.) AZ istifadəçi üçün tərcümə keyfiyyəti zəifdir.** — Confidence: Low-Medium

**Implication:** "Translate-only" lokallaşdırma yetmir; tam native UI + content + support + billing AZ-da real boşluqdur.

### Pricing Pain Points

- **Freeletics:** $34.99/3ay, $59.99/6ay, $74.99/12ay → illik plan ~127 AZN, AZ orta aylıq maaşın **~11.7%-i**. _Source: [Fitness Drum](https://fitnessdrum.com/freeletics-review/)_ — Confidence: High
- **BetterMe Türkiyə şikayətləri:** "148 TL götürdüm, sonra icazəsiz 560 TL çəkildi" — opaque billing dominant şikayət. _Source: [Sikayetvar](https://www.sikayetvar.com/en/betterme-us), [BBB BetterMe](https://www.bbb.org/us/ga/cumming/profile/mobile-apps/betterme-0443-91842647/complaints)_ — Confidence: High
- **AZ orta maaş 1,089 AZN; aşağı-skill 341 AZN.** Qərb apps-ı $10-15/ay = aşağı-skill seqmentin 5-7%-i — premium kateqoriya. — Confidence: High
- **Auto-renewal şikayətləri BetterMe + Freeletics-də sistematik** (£36 → £119.99 sessizcə yenilənmə). _Source: [Trustpilot Freeletics](https://www.trustpilot.com/review/www.freeletics.com)_ — Confidence: High

**Implication:** 5-10 AZN/ay hipotezi düzgün istiqamətdir, **transparent billing + asan iptal** vital differentiator-dur.

### Content Gaps

- **No-equipment home workout apps**: "limited variety, plateau" şikayəti dominantdır — back/biceps zəif təmsil. _Source: [Dr. Muscle review](https://dr-muscle.com/home-workout-no-equipment-app-review/)_ — Confidence: High
- **BetterMe video keyfiyyəti şikayətləri:** "20 dəqiqə fasiləsiz scissors", "instructor video ilə uyğun deyil". Form correction yoxdur. _Source: [Trustpilot BetterMe](https://www.trustpilot.com/review/betterme.world)_ — Confidence: High
- **Home+gym hybrid:** Fitbod equipment-aware amma progression strukturu yox; Freeletics bodyweight güclü, strength zəif. **Hybrid səyahət bir app-da bağlanmır.** — Confidence: Medium
- **3D model talabı user-stated qaynaqdan tapılmadı** — investor narrative cəlbedici, lakin real user pain deyil. **Hypothesis (no strong source).**

### AI-Generated Plan Complaints

- "Plans feel random rather than personalized" — Fitbod 3 ay sonra muscle aşağı, fat yüxarı testeri. _Source: [Sensai blog](https://www.sensai.fit/blog/best-ai-fitness-apps-2026-fitbod-freeletics-future-trainiac-alternatives)_ — Confidence: Medium
- "Generic plans miss recovery, travel, equipment constraints" — day-level adaptation boşdur. — Confidence: Medium
- Freeletics: "repetitive, coaching paywall arxasında". _Source: [Setgraph Reddit picks](https://setgraph.app/ai-blog/best-workout-app-reddit)_ — Confidence: Medium

**Implication:** "AI plan" demək yetmir — real moat **day-level adaptation** (missed session, equipment switch, recovery).

### Onboarding & Activation Friction

- **BetterMe onboarding: 26 sual, "marathon".** Drop-off riski yüksək; "10-15 dəqiqə download → start". _Source: [App Fuel](https://theappfuel.com/examples/bettermefitness_onboarding)_ — Confidence: High
- "Confusing payment process after onboarding" — paywall şəffaf deyil. — Confidence: High

**Implication:** Qısa onboarding (≤8 sual) + value-first preview (paywall öncəsi 1 workout) + transparent pricing.

### Cultural/Local Fit Gaps

- **AZ/TR yeməkləri MFP database-də zəif** — Diyetkolik məhz bu boşluq üçün quruldu, 1.5M Türk istifadəçi topladı. _Source: [PMC Diyetkolik case](https://pmc.ncbi.nlm.nih.gov/articles/PMC7568214/)_ — Confidence: Medium (inference)
- **Ramazan/oruc tracking:** Modern Muslim Fit, DoFasting Ramadan modu təklif edir — mainstream apps-da yoxdur. _Source: [Gulf News Ramadan apps](https://gulfnews.com/lifestyle/ramadan-2026-10-best-health-apps-to-track-calories-sleep-and-workouts-in-the-uae-1.500442153)_ — Confidence: High
- **Modest workout video təmsili:** Modest activewear bazarı (Gymshark Modestwear) artır, fitness apps demo videolarında yox. — Confidence: Medium
- **AZ context qeydi:** AZ laik, hicab tələbatı UAE səviyyəsində kütləvi deyil — over-index etməmək lazımdır.

### Top 5 Unmet Needs (Ranked)

1. **Native AZ keyfiyyəti (UI + content + billing + support)** — BetterMe machine-translated, native quality boşluqdur.
2. **Transparent + lokal qiymət + asan iptal** — BetterMe/Freeletics ən çox şikayət aldığı sahə.
3. **Adaptiv AI plan (missed session, equipment switch, recovery)** — static "AI weekly plan" doymuş.
4. **AZ/TR yemək DB + Ramazan modu** — Diyetkolik TR-də 1.5M user topladı, AZ ekvivalenti yoxdur.
5. **Home+gym hybrid program, real form demonstration** — heç bir app evdən zala keçidi yumşaq idarə etmir.

---

## Competitive Landscape

### Tier 1: Direct Global Competitors

**1. BetterMe — Health Coaching** 🚨 **KRİTİK TƏHDİD**
- Aylıq $9.99-19.99; illik ~$60-80 (effektiv **~$5/ay** = ~8.5 AZN). 7 gün trial.
- **Dil: EN, AZ, RU, TR + 25+ dil** ⚠️ — **AZ artıq dəstəklənir** (machine-translated keyfiyyət)
- AI personalized workouts + meal plans, generic stock videos. **AI-video YOX, 3D YOX**.
- Ukraine-based, Russian/post-Soviet market təcrübəsi yüksək.
- AZ zəiflik: tərcümə keyfiyyəti, local food DB yoxdur, AZN ödəniş yoxdur, "yerli his" yoxdur.
- _Source: [BetterMe pricing](https://wellness.alibaba.com/fitlife/betterme-cost-subscription-guide), [BetterMe languages](https://bettermesupport.zendesk.com/hc/en-us/articles/360019549298)_ — Confidence: High

**2. MyFitnessPal**
- Free / Premium $19.99/ay, $79.99/il / Premium+ $24.99/ay
- Dil: EN, ES, DE, FR, IT, PT, JA, KO, ZH, RU (qismən). **AZ YOX.**
- 15M+ food DB, barcode scan, makro tracking. **Workout zəif, AI yox, video yox, 3D yox**.
- AZ zəiflik: plov/dolma/qutab yoxdur, workout primitiv.
- _Source: [MFP 2026 pricing](https://nutriscan.app/blog/posts/myfitnesspal-pricing-2026-guide-2ff09c399a)_ — Confidence: High

**3. Nike Training Club**
- **Tamamilə PULSUZ** (COVID-dən bəri) — güclü hücum vektoru
- Dil: EN, DE, FR, IT, ES, PT, JA, ZH, KO, RU. **AZ YOX.**
- 200+ professional video, home+gym. AI/3D/personalization zəif.
- AZ zəiflik: dil yox, kalori yox, fərdiləşmə zəif. **Free olduğu üçün freemium tier-i təhdid edir.**
- _Source: [NTC](https://www.nike.com/ntc-app)_ — Confidence: High

**4. Freeletics**
- Coach $34.99/ay, $99.99/il (~$8.33/ay illik) — bahalı
- Dil: EN, DE, FR, ES, IT, PT. **AZ/RU YOX.**
- Adaptive AI Coach (Europe-leader), HIIT/bodyweight, audio coaching. **AI-video YOX, 3D YOX**.
- AZ zəiflik: dil, qiymət ($35/ay = 60 AZN AZ üçün premium).
- _Source: [Freeletics pricing](https://help.freeletics.com/hc/en-us/articles/360020109819)_ — Confidence: High

### Tier 2: Adjacent / Niche

| App | Pricing | AI | Notes |
|---|---|---|---|
| **FitOn** | Free; Pro $29.99/il | Yox | Free tier güclü, celebrity. AZ/RU yox |
| **Hevy** | $2.99/ay, $69.99/il | Yox | Ucuz gym log. AZ yox |
| **Strong** | $4.99/ay, $29.99/il, $99.99 lifetime | Yox | Fastest logging UX. AZ yox |
| **JEFIT** | $12.99/ay, $69.99/il | Yox | Ən böyük exercise DB |
| **Fitbod** | $15.99/ay, $95.99/il | AI workout-level | 1600+ exercise, equipment-aware |
| **Caliber** | $200-500/ay | Hybrid human coach | Premium niche |
| **Centr** | $29.99/ay, $149.99/il | Yox | Hemsworth brand |

### Tier 3: Regional & Local

- **Diyetkolik (TR):** $6.99/ay, AI photo food log, TR-only. 1.5M user. _Source: [App Store](https://apps.apple.com/us/app/diyetkolik-ai-calorie-counter/id558076089)_
- **Fitatu (TR):** UK-based, AZ food DB yox
- **FitStars.ru:** RU+EN video subscription, AZ yox. _Source: [fitstars.ru](https://fitstars.ru/)_
- **Push30 (AZ):** Gym aggregator (267 zal subscription), workout/AI yoxdur — **rəqib deyil, potensial partner**. _Source: [push30.app](https://push30.app/)_
- **Native AZ AI fitness app TAPILMADI** (2026-05-12 itibarilə) — green field

### Feature Comparison Matrix

| App | AZ | RU | TR | Monthly $ | AI Plan | AI Video | 3D | Home | Gym | Calorie | Free Tier |
|---|---|---|---|---|---|---|---|---|---|---|---|
| BetterMe | ✓ (MT) | ✓ | ✓ | $10-20 | ✓ | ✗ | ✗ | ✓ | ✓ | ✓ | trial |
| MyFitnessPal | ✗ | partial | ✗ | $20 | ✗ | ✗ | ✗ | ✗ | ✗ | ✓✓✓ | ✓ |
| Nike Training | ✗ | ✓ | ✗ | $0 | ✗ | ✗ | ✗ | ✓ | ✓ | ✗ | ✓✓✓ |
| Freeletics | ✗ | ✗ | ✗ | $35 | ✓✓ | ✗ | ✗ | ✓ | ~ | ~ | limited |
| FitOn | ✗ | ✗ | ✗ | $2.50/il | ✗ | ✗ | ✗ | ✓ | ✗ | ~ | ✓✓ |
| Hevy | ✗ | ✗ | ✗ | $3 | ✗ | ✗ | ✗ | ✗ | ✓ | ✗ | ✓ |
| Fitbod | ✗ | ✗ | ✗ | $16 | ✓ | ✗ | ✗ | ✓ | ✓ | ✗ | trial |
| Diyetkolik | ✗ | ✗ | ✓ | $7 | ✓ photo | ✗ | ✗ | ✗ | ✗ | ✓✓ | ✓ |
| FitStars | ✗ | ✓ | ✗ | ~$6 | ✗ | ✗ | ✗ | ✓ | ✗ | ✗ | trial |
| Push30 | ✓ | ✓ | ✗ | gym sub | ✗ | ✗ | ✗ | ✗ | ✓ | ✗ | ✗ |
| **BİZ** | ✓✓ native | ✓ | (✓) | 5-10 AZN | ✓ | ✓✓ | ✓✓ | ✓ | ✓ | ✓ | tbd |

### Pricing Landscape Summary

- Median monthly: **~$15** (premium global)
- Range: $0 (NTC, FitOn) → $35 (Freeletics) → $200+ (Caliber)
- Annual effektiv aylıq: $5-10
- **AZN konversiyada qlobal apps 8.5-17 AZN/ay** → bizim 5-10 AZN hipotezi MFP/Hevy xaric hamısından ucuz, BetterMe illik tier-ə bərabər
- **NTC pulsuz olduğu üçün güclü free tier məcburidir**

### Differentiation White Space

1. **AI-generated exercise video** — HEÇ BİR RƏQİB ETMİR. Real moat, lakin keyfiyyət riski
2. **3D model per exercise** — Tempo (hardware) xaric heç kim. Mobile-only 3D boş
3. **Native AZ keyfiyyəti** — Push30 gym-access, BetterMe machine-translated. Boşluq açıqdır
4. **AZ food DB** — Heç kim plov/dolma/AZ markaları
5. **AZN ödəniş + local payment (m10, Pulpal)** — global apps card-only
6. **Home+gym hybrid dual** — heç bir app yumşaq keçid

### Positioning (Price × Localization)

```
                  Full AZ Localization
                          ▲
                          │ ★ BİZ (3-6 USD, native AZ, AI video/3D)
                          │
            Push30 ●      │      ● BetterMe (MT AZ, $5-20)
                          │
  ────────────────────────┼────────────────► Higher Price
                          │
            ● Diyetkolik  │ ● FitStars (RU)
            (TR only)     │ ● Freeletics ● Centr ● Caliber
                          │ ● Fitbod ● MFP
            NTC, FitOn ●  │ ● Hevy/Strong
            (free, EN)    ▼
                  No AZ Localization
```

**Açıq kvadrant:** **High localization + Low price** — yalnız biz və qismən BetterMe (MT keyfiyyət).

### Threats & Defensibility

**Ən böyük təhdid: BetterMe** — AZ dili artıq var, böyük marketing budget. **3-6 ayda AZ-spesifik content (yerli yemək, AZ trainer) əlavə edə bilərlər** əgər bazar isbat olunsa. Confidence: High

**Defensibility lay-erləri** (önəm sırası):
1. **AZ-spesifik content depth** (yerli ərzaq DB, AZ trainer voice, Ramazan, milli mətbəx) — lokal komanda lazımdır, kopyalaması bahalı
2. **AI video pipeline** — $300 GCP limit, Veo/Kling commodity. **Bu moat zəifdir** (12 ay)
3. **AZN + local payment (m10/UnipayGO)** — orta-müddətli moat
4. **Community/network effect** (AZ-mərkəzli sosial) — uzunmüddətli (Faza 3)

**Real risk:** BetterMe AZ paid ads gücləndirsə, məhsul keyfiyyəti orta olsa belə brand recognition ilə qazanır.
**Strategiya:** İlk 6 ayda 1000-5000 organic AZ user, content quality + yerli partnership ilə loyalty.

---

## Strategic Synthesis & Recommendations

### Ən Vacib Strateji Dəyişiklik

**Əvvəlki fərziyyə:** "AZ dil dəstəyi = əsas differensiator" → **YANLIŞ.** BetterMe artıq AZ-da var.
**Yeni pozisiya:** **"Native AZ keyfiyyəti + yerli kontent + adaptiv AI + transparent qiymət"** — 4-lü kombinasiya.

### Yenilənmiş Differensiator Sıralaması

| Differensiator | Əvvəlki güc | Real güc | Müdafiə müddəti |
|---|---|---|---|
| AZ dil dəstəyi | Əsas moat | **Qismən moat** (BetterMe MT var, biz native veririk) | 3-6 ay |
| AI-generated video | Sekonder | **Əsas marketing message**, zəif moat | 12 ay (commodity olur) |
| 3D model | Sekonder | **Aşağı priority** (user-stated talab zəif) | irrelevant |
| Home+gym hybrid | Sekonder | **Real boşluq, orta moat** | 6-12 ay |
| **Yerli content (AZ food DB, Ramazan, trainer voice)** | Qeyd edilməmişdi | **🏆 ƏN GÜCLÜ MOAT** | 12-24 ay |
| **AZN + local payment (m10/Pulpal)** | Qeyd edilməmişdi | **Orta moat, retention driver** | 6-12 ay |
| **Transparent billing + asan iptal** | Qeyd edilməmişdi | **Trust differentiator** | sonsuz (kültur) |

### MVP Scope — Yenilənmiş Prioritet

**P0 — Bunlarsız launch yox:**
1. ✅ Native AZ UI + content (texnoloji tərcümə yox — manual review hər string)
2. ✅ AI həftəlik proqram generasiyası (LLM-based)
3. ✅ Hərəkət kitabxanası (50-100 hərəkət MVP-də) — video + təlimat (3D **çıxar** Faza 2-yə)
4. ✅ Ev + Zal alternativ hərəkətlər (per exercise)
5. ✅ Set/rep logger + rest timer
6. ✅ Kalori hədəfi (BMR/TDEE) + **AZ Top-200 yemək DB** (plov, dolma, qutab, kabab, yerli markalar)
7. ✅ Onboarding ≤8 sual (BetterMe-nin 26 sualından kəsmə)
8. ✅ **Transparent pricing + native iOS/Android subscription** (sızıntısız iptal)
9. ✅ Strong free tier (NTC pulsuz tehdidini qarşıla)

**P1 — Çalışsaq:**
- Progress tracker (çəki, ölçü, foto)
- Push notification AZ-da
- Adaptive plan adjustment (missed session recovery)
- **Ramazan mode** (suhoor/iftar split macros) — TR ekspansiya üçün dəyər

**Çıxarıldı / yenidən prioritet:**
- ❌ **3D model per exercise** — P0-dan çıxar, Faza 2 (real user pain deyil, AI kredit yandırır)
- ⏸ AI form correction (real-time) — Faza 3

### Pricing — Validasiya & Strategiya

- **5-10 AZN/ay (~$3-6) hipotezi DOĞRULANDI** — BetterMe illik effektiv qiymətə yaxın, MFP/Freeletics-dən ucuz
- **Tövsiyə qiymət strukturu:**
  - **Free tier (NTC-ni qarşılamaq üçün):** 5 base workout + 30 hərəkət + 1 plan generasiya/ay + kalori tracker
  - **Premium aylıq: 8 AZN** (yaxınlıqda BetterMe MT-yə)
  - **Premium illik: 60 AZN (~5 AZN/ay)** — 38% endirim, yanvar push
  - **Lifetime: 199 AZN** (Strong-dan ilham, simli loyalty)
- **Annual plan yanvar push absolute** — aylıq retention 17% problemi
- **m10/Pulpal/UnipayGO inteqrasiyası** Faza 1-də araşdır (Apple/Google paydan əlavə)

### Acquisition Strategy

| Kanal | Seqment | Cost | Priority |
|---|---|---|---|
| Instagram (AZ influencer) | A (qadın 25-35) | Orta | P0 |
| TikTok (AZ fitness creator) | A, B | Aşağı | P0 |
| YouTube AZ/RU fitness | B (kişi 18-30) | Orta | P1 |
| App Store ASO (AZ keywords) | hamı | Aşağı | P0 |
| Push30/zal partnership | B, C | Aşağı | P1 |
| Facebook (35-50) | C | Orta | P2 |

### Threat Response (BetterMe risk)

**Əgər BetterMe AZ paid ads başlatsa:**
1. Native quality message üzərində duy ("Google Translate ilə deyil, AZ-da yazılmış")
2. Yerli content göstər (plov kalorisi, Ramazan mode, AZ trainer)
3. Influencer-loyalty: 5-10 AZ fitness creator-la 1+ illik exclusive
4. AZN qiymət şəffaflığı — BetterMe USD billing-i şikayət magneti

### Solo Developer Realism Check

| MVP komponent | 4 aylıq solo realistic? |
|---|---|
| Native AZ UI + content | ✅ Bəli (manual review = vaxt, lakin scope-da) |
| AI plan generation | ✅ Bəli (LLM API + prompt + cost cap) |
| Hərəkət kitabxanası 50-100 + video | ⚠️ **Bottleneck** — AI video generation $300 GCP limitiylə 100 hərəkət riskli; alternativ: Mixamo + manual recording first |
| Ev+zal alternativ per exercise | ✅ Bəli (data model-də artıq var) |
| AZ Top-200 food DB | ⚠️ Manual 1-2 həftə işi (USDA + AZ pəcrə + market) |
| Transparent billing | ✅ Bəli (RevenueCat suggestion) |
| Free tier + premium | ✅ Bəli (RevenueCat) |
| **3D model per exercise** | ❌ **Çıxar — solo dev üçün non-trivial, real ROI sübut olunmayıb** |

### Risk Register (Top 5)

| Risk | Likelihood | Impact | Mitigation |
|---|---|---|---|
| BetterMe AZ marketing pump | Yüksək | Yüksək | Native quality message + influencer loyalty + yerli content depth |
| AI video keyfiyyəti zəif | Orta | Yüksək | İlk 50 hərəkət üçün manual çəkim + Mixamo, sonra AI gradual |
| Free tier (NTC) churn | Yüksək | Orta | Free-də value real, premium-da AI plan + AZ food + ad-free |
| Solo dev 4 ay timeline slip | Yüksək | Yüksək | 3D-ni çıxar, hərəkət kitabxanası 50-yə endir, scope hard-cut |
| GCP $300 kredit yan­ır | Orta | Orta | Hər AI çağırışı cost-log + admin-only trigger + Mixamo fallback |

### Bottom Line — Recommended Strategic Position

> **"Azərbaycanlı üçün hazırlanmış, Azərbaycanda yazılmış AI fitness coach — yerli yemək, yerli qiymət, yerli güvən."**

- AZ dil dəstəyi bir **giriş bileti**, **qələbə deyil**
- Qələbə: **native keyfiyyət + yerli content depth + transparent qiymət + adaptiv AI**
- AI video/3D **marketing hook**, **uzunmüddətli moat deyil**
- 4 aylıq MVP üçün **3D-ni kəs, hərəkət sayını məhdudla, yerli food DB-yə vaxt qoy**

### Confidence Summary

| Tapıntı | Confidence |
|---|---|
| AZ smartphone/internet penetrasiyası | High |
| 5-10 AZN/ay qiymət tolerable | High |
| BetterMe AZ dilini artıq dəstəkləyir | High |
| Subscription monthly retention 17% riski | High |
| 3D model user-stated talab zəif | Medium-High |
| Native AZ AI fitness app rəqibi yoxdur | High |
| Yerli food DB böyük moat | Medium (Diyetkolik inferensiyası) |
| AI video uzunmüddətli moat zəif | Medium |
| Türkiyə/CIS spesifik bazar həcmi | Low (qaynaq seyrək) |

---

## Next Actions

1. ✅ Bu sənədi `_bmad/custom/bmad-agent-pm.toml` üçün `persistent_facts` mənbəyi kimi qeyd et
2. 🔲 `/feature-decision` ilə üç qəti qərar feature.md-ə əlavə et:
   - 3D model MVP-dən kənar (Faza 2)
   - Pricing 8 AZN aylıq / 60 AZN illik (5 AZN effektiv)
   - Yerli food DB Top-200 P0-a daxil
3. 🔲 BetterMe AZ app-ı qur, screenshot al, tərcümə keyfiyyətini sənədləşdir (rəqib monitoring)
4. 🔲 RevenueCat + m10/Pulpal/UnipayGO inteqrasiya araşdırması (Faza 1 P0)
5. 🔲 PM persona (`bmad-agent-pm`) ilə PRD generasiyasını başlat (`/bmad-create-prd`)

---

