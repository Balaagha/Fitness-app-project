# Phase 4 — 19 L2/L3/L5 Sual Ekranı Spec (v2 — master-aligned)

Hazırlanma tarixi: 2026-05-23 · v2 yenidən yazılma: 2026-05-23 (master `ux-onboarding-questions §4.2/4.3/4.4` ilə uyğunlaşdırıldı)
Mənbə: feature `detect-design-ui-step-and-tools` · Phase 4
Şamil: `.pen` 03 Questions section altına yeni row-larda 19 frame

> **Kanonik mənbə:** `ux-onboarding-questions-2026-05-23.md` §1 anatomy + §3 input variant mapping + §4 inventar. Bu sənəd o inventardan **field/variant-by-field** törəyən .pen build təlimatıdır.

---

## Şablon kontraktı (hər ekran)

`statusbar 62` + `header 44` (back + progress strip — **L5 və L3-də progress YOXDUR**) + step chip (eyebrow caption) + display `25/700` + subtitle `13/500 $ink-muted` + input zona (variant) + helper/disclaimer + footer CTA pair (Skip Ghost solda · Primary `$volt` sağda).

- **L2 ekranları:** tam-screen frame (375×812), `$bg` zəmin, progress strip GÖSTƏRİLİR (L2 daxili: A→F qrup nömrəsi 1/6 … 6/6).
- **L3 ekranları:** bottom sheet, `$surface-elevated` zəmin, üst corner radius 24, header `[×]` close (back yox), progress strip YOXDUR.
- **L5 ekranları:** tam-screen frame, sticky `$warning` 36pt banner üstdə, progress strip YOXDUR, back ikon var, **skip ya yox ya da sensitive-consent guard ilə**.

## Voice

- Birinci-şəxs sual ("Hədəfin nədir?" yox "Səndə hansı...")
- L5: sakin tibbi ton, oyun animasiyası YASAQ, disclaimer "Bu məlumat tibbi məsləhət deyil"
- "**Mütəxəssis yoxlaması**" və ya "**uyğunluq yoxlaması**" — `məşqçi/coach/trainer` qadağan (CLAUDE.md)
- "approval/təsdiq" SÖZÜ tibbi kontekstdə YASAQ — `uyğunluq yoxlaması` (`ux-onboarding-questions §6.5`)

## Cross-ref konvensiya

- `data-catalog §X.Y` = `prd-user-profile-data-catalog-2026-05-22.md`
- `questions §X.Y` = `ux-onboarding-questions-2026-05-23.md`
- `onb §X.Y` = `prd-auth-onboarding-2026-05-22.md`
- `pc §X` = `project-context.md`

---

## L2 Akkordeon (A-F) — 6 ekran · opsional progressive · Skip Ghost aktiv

> Q7 sonrası opt-in modal: "Tam profil yaratmaq istərdin? (3-4 dəq, hər vaxt skip)". L2 daxili progress: hər tamamlanan qrup üçün `+15% personalizasiya` mikro-rozet — kahraman copy YASAQ (`questions §4.2`).

### #1 · L2-A · Bədən & Metabolizm

- **Trigger:** L2 opt-in modal → A
- **Variant:** RadioCard ×5 (activity) + Stepper (target weight) + DatePicker (deadline opt) + PhotoCardPicker ×5 (body fat visual) + RadioCard ×3 (frame size)
- **Display:** "Bədənin haqqında bir az daha"
- **Subtitle:** "Kalori hədəfini və plan intensivliyini buna görə dəqiqləşdiririk."
- **Answer (sıralı stack):**
  1. **activity_level_daily** — RadioCard ×5 kompakt 64pt: Sədəntər · Yüngül · Orta · Çox aktiv · Ekstrem (`questions §3` RadioCard mapping)
  2. **target_weight** — Stepper [30-200 kg] + UnitToggle (metric/imperial)
  3. **target_deadline** — DatePicker (opt, "Tarix qoyma" Ghost link altda)
  4. **body_fat_visual_estimate** — PhotoCardPicker ×5 illustrated buckets (10-15% / 15-20% / 20-25% / 25-30% / 30%+; per `data-catalog §body_fat_visual`)
  5. **frame_size** — RadioCard ×3: İncə · Orta · Geniş
- **Helper:** "Yalnız özünü qiymətləndirmədir — istənilən vaxt yenilə."
- **CTA:** Davam et (Primary `$volt`) / Sonra (Ghost)
- **Cross-ref:** `data-catalog §body_comp` · `pc §5.5 BMR/TDEE` · `questions §4.2 group-A`
- **Safety:** `body_fat_visual_estimate` heç bir tibbi qiymət vermir — disclaimer micro-text

### #2 · L2-B · Aktivlik & Həyat tərzi

- **Trigger:** L2-A "Davam et" və ya A skip → B
- **Variant:** Stepper (sleep) + Likert/Slider qrid (PSS-4) + Stepper (sedentary) + Stepper (step goal)
- **Display:** "Gündəlik ritmin necədir?"
- **Subtitle:** "Bərpa keyfiyyəti və TDEE düzgünlüyü üçündür."
- **Answer (sıralı stack):**
  1. **sleep_h_per_night** — Stepper 3-12 saat, ±0.5, default 7
  2. **stress_pss4_score** — 4 ardıcıl Likert chip 0-4 (PSS-4, hər sual: "Son ayda neçə tez-tez...") (`data-catalog §stress_pss4_score`)
  3. **sedentary_hours_per_day** — Stepper 0-16 saat, ±1
  4. **step_goal** — Stepper 3000-15000, ±500, default 8000 + UnitToggle yox
- **Helper:** "Orta gecəni / orta günü düşün — istisnaları deyil."
- **CTA:** Davam et / Sonra
- **Cross-ref:** `data-catalog §sleep_h_per_night` · `data-catalog §stress_pss4_score` · `data-catalog §sedentary_hours` · `data-catalog §step_goal` · `questions §4.2 group-B`

### #3 · L2-C · Sağlamlıq & Yaralanmalar (yüngül)

- **Trigger:** L2-B sonrası — **sensitive opt-in confirm** modal ("Sağlamlıq məlumatın yalnız planı uyğunlaşdırmaq üçündür. Davam edək?") → Confirm → C ekranı
- **Variant:** RadioCard ×2 (var/yox) → conditional MultiSelectChip qrid (site) + MultiSelectChip (movement restrictions)
- **Display:** "Yaralanma və ya hərəkət məhdudiyyətin varmı?"
- **Subtitle:** "Bu, AI planın güvənli sıralanmasını təmin edir."
- **Answer:**
  1. **injury_history (var/yox)** — RadioCard ×2; "Yox" → digər field-lər disabled
  2. **injury site** — MultiSelectChip qrid 12 zona (boyun · çiyin × 2 · bel · diz × 2 · ayaq biləyi × 2 · bilek × 2 · dirsək × 2)
  3. **movement_restrictions** — MultiSelectChip 8 hərəkət (squat dərinliyi · overhead press · döşəmə üzərində uzanma · ağır deadlift · yan plank · oturmaq · yerimək · qaçmaq)
- **Helper:** "Sonra Settings-də yenilə bilərsən. **Tibbi məsləhət deyil** — şübhən varsa həkimə müraciət et."
- **CTA:** Davam et / Sonra
- **Cross-ref:** `data-catalog §injury_history` · `data-catalog §movement_restrictions` · `questions §4.2 group-C` · `questions §6.5 medical-üslub`
- **Safety:** sensitive consent əvvəl; rejected → L2-D-yə skip; LinkText "Bu məlumat necə işlənir" → Privacy Policy

### #4 · L2-D · Yemək tərcihləri

- **Trigger:** L2-C sonrası → D
- **Variant:** RadioCard ×4 (diet_pattern) + MultiSelectChip ×3 (allergies · intolerances · religious) + RadioCard ×2 (ramazan auto-detect override)
- **Display:** "Yemək vərdişlərin necə?"
- **Subtitle:** "AZ Top-200 yemək DB və kalori təkliflərini buna görə filtr edirik."
- **Answer (sıralı stack):**
  1. **diet_pattern** — RadioCard ×4: Hamısı · Vegeterian · Halal sərt · Ramazan aktiv (mart-aprel auto-detect)
  2. **allergies** — MultiSelectChip 14 (süd · qoz · yumurta · soya · qabıqlı dəniz · balıq · taxıl/qluten · kivi · seder · xardal · küncüt · şaftalı · banan · digər)
  3. **food_intolerances** — MultiSelectChip 6 (laktoza · qluten həssaslıq · FODMAP · histamin · fruktoza · digər)
  4. **religious_dietary** — MultiSelectChip 3 (halal · kashrut · oruc dövrü); avtomatik məntiqdən asılı
  5. **ramazan_active override** — RadioCard ×2 (yox/var); mart-aprel default detection
- **Helper:** "Heç bir məhdudiyyət varmı? Hamısını boş burax."
- **CTA:** Davam et / Sonra
- **Cross-ref:** `data-catalog §diet_pattern` · `data-catalog §allergies` · `data-catalog §religious_dietary` · `pc §7 AZ food DB`

### #5 · L2-E · Avadanlıq inventarı

- **Trigger:** L2-D sonrası → E. **Default state:** `context = home_only` ⇒ `[bodyweight]` seçili gəlir; `context = serious_gym/casual_gym` ⇒ heç biri seçili deyil (zal avadanlığı tam mövcud sayılır).
- **Variant:** MultiSelectChip qrid ×12 (4 sütun × 3 sıra)
- **Display:** "Hansı avadanlıqdan istifadə edə bilərsən?"
- **Subtitle:** "Yalnız əldə olanı seç — qalanı plan üçün gizlədəcəyik."
- **Answer:**
  - bodyweight · dumbbells · barbell · kettlebell · pull-up bar · rezistans bantı · suspension trainer (TRX) · benç (düz/əyri) · squat rack · cable machine · cardio (treadmill/bike) · zal abone (tam erişim)
- **Helper:** "Heç nə yoxdur? Sadəcə `bodyweight` seçili saxla."
- **CTA:** Davam et / Sonra
- **Cross-ref:** `data-catalog §equipment_inventory` · `questions §4.2 group-E` · `pc §1 Minimal Design — context-driven default`

### #6 · L2-F · Üslub & Bildiriş

- **Trigger:** L2-E sonrası → F (sonuncu qrup — CTA "Bitir")
- **Variant:** MultiSelectChip (motivations · discovery · previous_app) + RadioCard (trainer_voice · trainer_tone · preferred_training_time · notification_cadence) + RadioCard (music)
- **Display:** "Üslub və xəbərdarlıqlar"
- **Subtitle:** "Səsi, vaxtı və motivasiya tonunu özünə uyğun düz."
- **Answer (sıralı stack):**
  1. **motivations** — MultiSelectChip 6 (sağlamlıq · görünüş · enerji · stress · idman performans · digər); cap "≤3"
  2. **discovery_channel** — RadioCard ×6 (App Store · Google Play · Instagram · TikTok · dost · digər)
  3. **previous_app_used** — MultiSelectChip 5 (BetterMe · MyFitnessPal · Nike TC · Freeletics · digər/heç biri)
  4. **trainer_voice** — RadioCard ×3 (Kişi · Qadın · Neytral)
  5. **trainer_tone** — RadioCard ×3 (motivasion · sakit-rəhbər · faktiki-müəllim)
  6. **preferred_training_time** — RadioCard ×4 (səhər · günorta · axşam · gec)
  7. **notification_cadence** — RadioCard ×3 (gündəlik · 3 günə bir · həftəlik)
  8. **music_during_workout** — RadioCard ×2 (var · yox)
- **Helper:** "Bunlar Settings-də həmişə dəyişdirilə bilər."
- **CTA:** **Bitir** (Primary `$volt`) / Sonra (Ghost)
- **Cross-ref:** `data-catalog §motivations` ... `data-catalog §notification_cadence` · `questions §4.2 group-F`
- **Notes:** L2 tamamlanma → `l2_completed` analytics event + dashboard "+15% personalizasiya" rozet (kahraman copy yasaq — `questions §4.2`)

---

## L3 Bottom Sheet — 9 ekran · `$surface-elevated` · header `[×]` close · progress strip YOXDUR

> Bu ekranlar onboarding-də DEYİL — feature ilk açılışında və ya settings-də tap ilə tetiklənir. Hər biri bir field grouping-i təmsil edir.

### #7 · L3 · MeasurementSheet

- **Trigger:** "Ölçü trekeri" ilk açılış (Profile → Body)
- **Variant:** Stepper qrid (12 sahə) + UnitToggle (cm)
- **Display:** "Ölçülərini əlavə et"
- **Subtitle:** "İlerleme trekinq üçün — boş burax, sonra qayıt."
- **Answer (qrid):** neck · waist · hip · chest · shoulder · biceps L · biceps R · forearm L · forearm R · wrist · thigh L · thigh R · calf L · calf R; hər biri Stepper [10-200 cm ±0.5]
- **Helper:** "Ən vacibi: bel + hip. Qalanı opt."
- **CTA:** Yadda saxla / Bağla `[×]`
- **Cross-ref:** `data-catalog §body_measurements_jsonb` · `questions §4.3`
- **Skip:** ✅ field-by-field

### #8 · L3 · TargetWeightSheet

- **Trigger:** Kalori ekranında goal = cut/bulk user; "Hədəf qoy" tap
- **Variant:** Stepper + DatePicker + Stepper (opt body fat %)
- **Display:** "Hədəfini təyin et"
- **Subtitle:** "Real, sağlam tempə uyğun gör — həftəlik %0.5-0.75 dəyişiklik tövsiyə."
- **Answer:**
  1. **target_weight** — Stepper [30-200 kg ±0.1]
  2. **target_deadline** — DatePicker (min today+30g, max today+365g)
  3. **target_body_fat_pct** (opt) — Stepper [5-50 % ±0.5]
- **Helper:** Real-time mikro-feedback: "Həftəlik ~ X kg/ay — bu sağlam tempdir / aqressivdir / yavaşdır."
- **CTA:** Hədəfi qur / Bağla
- **Cross-ref:** `data-catalog §target_weight` · `pc §5.5 nutrition`

### #9 · L3 · InjuryLogSheet

- **Trigger:** "Yaralanma günlüyü → + əlavə et" (per-injury formu)
- **Variant:** RadioCard (site) + RadioCard (side: L/R/bilateral) + RadioCard (type: acute/chronic/post-op) + DatePicker (onset) + Slider 0-10 (current_pain_vas) + MultiSelectChip (movement_restrictions)
- **Display:** "Yaralanma əlavə et"
- **Subtitle:** "Plan bu zonanı yumşaq keçəcək."
- **Answer:** site (12 zona — bax #3) · side · type · onset date · current pain VAS 0-10 · restrictions (8 hərəkət)
- **Helper:** "İstənilən vaxt redaktə etmək olar — Settings → Sağlamlıq."
- **CTA:** Yaralanma əlavə et / Bağla (acu/imzasız save)
- **Cross-ref:** `data-catalog §injury_history` · `questions §4.3`
- **Skip:** ❌ ekranda açıqkən (acu close = imzasız) — `[×]` ilə bağla

### #10 · L3 · PainCheckin

- **Trigger:** Hər məşq başlanğıcı (məşqdə açıq yaralanma var); gündə 1 dəfə
- **Variant:** Slider 0-10 (per injury — birdən artıq aktiv olarsa cədvəl ardıcıllıq)
- **Display:** "Ağrı səviyyəsi"
- **Subtitle:** "Bu məşqdən əvvəl son ağrı dərəcən."
- **Answer:** VAS Slider 0-10 (etiketlər: 0 = yox · 5 = orta · 10 = ən pis)
- **Helper:** ≥7 → terminal warning: "Bu intensivlikdə məşq tövsiyə olunmur. **Mütəxəssis yoxlaması** lazımdır."
- **CTA:** Davam et / 1 günə ertələ (Ghost)
- **Cross-ref:** `data-catalog §current_pain_vas` · `questions §4.3`
- **Safety:** VAS ≥7 → məşq launch BLOCKED + insan-onayı queue-ya bildiriş (premium)

### #11 · L3 · HealthSnapshot

- **Trigger:** Day-3 nudge (1 dəfə); user dismiss "1 həftəyə ertələ" sçimi var
- **Variant:** Stepper + Slider + Likert
- **Display:** "Sağlamlıq snapshot"
- **Subtitle:** "3 sualın 60 saniyəlik mənzərəsi — sonra daha dəqiq plan."
- **Answer:**
  1. **resting_hr** — Stepper 40-120 bpm, default 70 (TextField fallback üçün VoiceOver)
  2. **sleep_h_per_night** — Stepper 3-12 h ±0.5 (L2-B ilə eyni; pre-fill əgər L2 doldurulubsa)
  3. **stress_pss4_score** — Likert 4-stop qısa variant (1 sual qənaət; "Son həftə nə qədər streslə üzləşdin?" 0-4)
- **Helper:** "Hər sual opt. Boş burax → sonra."
- **CTA:** Yadda saxla / 1 həftəyə ertələ
- **Cross-ref:** `data-catalog §resting_hr` · `pc §5.2 plan re-gen trigger`

### #12 · L3 · CycleSheet — **F-only · Faza 2 rezerv**

- **Trigger:** F user, Settings → Sağlamlıq → "Sikl izləməsi" toggle (default off)
- **Variant:** RadioCard ×2 (opt-in/out) + DatePicker (last_period_date)
- **Display:** "Menstrual sikl izləməsi (opsional)"
- **Subtitle:** "Plan luteal fazada intensivliyi avtomatik aşağı sala bilər."
- **Answer:**
  1. **cycle_tracking_opt_in** — RadioCard ×2 (var/yox); default no
  2. **last_period_date** — DatePicker (yalnız opt-in = var)
- **Helper:** "Faza 2 funksiyası — MVP-də yalnız field-i rezerv edirik."
- **CTA:** Yadda saxla / Bağla
- **Cross-ref:** `data-catalog §cycle_tracking_opt_in` · `questions §4.3 (F2 reserved)`
- **Status:** schema-da var, UI MVP-də deaktiv (skill flag)

### #13 · L3 · DislikeFoodSheet

- **Trigger:** Foto-kalori meal log → "Bəyənmədim" tap
- **Variant:** MultiSelectChip (current detected food) + free TextField (digər)
- **Display:** "Bəyənmədiyin yeməklər"
- **Subtitle:** "Növbəti dəfə kalori təkliflərində göstərməyəcəyik."
- **Answer:** MultiSelectChip dynamic — AZ Top-200 DB-dən son 10 detect olunmuş yemək + TextField "Digər yemək yaz..."
- **Helper:** "İstənilən vaxt Settings → Yemək tərcihlərində dəyişdir."
- **CTA:** Yadda saxla / Bağla
- **Cross-ref:** `data-catalog §disliked_foods` · `pc §7 AZ Top-200`

### #14 · L3 · PowerMilestoneSheet

- **Trigger:** Q5 = advanced user, dashboard "+əlavə et güc rekordu" tap (opt, ilk açılışda göstərilir)
- **Variant:** Stepper qrid (4 sahə) + UnitToggle (kg/lb)
- **Display:** "Güc rekordlarını qeyd et"
- **Subtitle:** "Plan intensivliyini bu rekordlara görə kalibrəyə salar."
- **Answer (qrid):** squat 1RM · bench 1RM · deadlift 1RM · overhead press 1RM; hər biri Stepper [20-300 kg ±2.5]
- **Helper:** "Yalnız real test etdiklərini yaz — təxmin etmə."
- **CTA:** Yadda saxla / Bağla
- **Cross-ref:** `data-catalog §power_milestones`

### #15 · L3 · EventTargetSheet

- **Trigger:** Plan trigger #3 (event prep) — settings və ya plan dashboard "Hadisə hazırlığı" tap
- **Variant:** RadioCard (event type) + DatePicker
- **Display:** "Hadisəyə hazırlaşırsanmı?"
- **Subtitle:** "Plan zəmin və faza vurğusunu hadisəyə uyğunlaşdıracaq."
- **Answer:**
  1. **event_type** — RadioCard ×5: Foto-sessiya · Toy · Sahil tətili · İdman yarış · Digər
  2. **event_date** — DatePicker (min today+14g, max today+365g)
- **Helper:** "Hədəf tarixi keçərsə avtomatik söndürülür."
- **CTA:** Hadisəni qur / Bağla
- **Cross-ref:** `data-catalog §event_target` · `pc §5.2 plan trigger`

---

## L5 Medical Safety — 4 ekran · sticky `$warning` 36pt banner · sakin tibbi ton · progress strip YOXDUR

> Trigger şərtləri: ilk AI plan tələbi · pregnancy nudge · injury günlüyü ilk açılış · advanced training intensity unlock. Hər biri bir-birini tetik edə bilər (gate-order: pregnancy > eating-disorder > medical_safety > consent).

### #16 · L5 · MedicalSafetyScreen (PAR-Q+ 7+3)

- **Trigger:** İlk AI plan tələbi və ya L2-C "var" cavabı
- **Variant:** CheckboxList ×7 (PAR-Q əsas) + per-checked row follow-up (CheckboxList ×3 alt-sual) + MultiSelectChip (medications_current) + Checkbox (medical_disclaimer_accepted)
- **Display:** "Təhlükəsizlik yoxlaması"
- **Subtitle:** "PAR-Q+ standartı. **Tibbi məsləhət deyil** — sadəcə güvənli plan üçün."
- **Answer:**
  1. **PAR-Q 7 sual** — Checkbox `yes/no` per: ürək problemi · göğüs ağrısı · halsızlıq epizodları · sümük/oynaq problem · qan təzyiqi/ürək dərmanı · son 12 ay ağır xəstəlik · digər hər hansı...
  2. **per `yes` follow-up 3 sual** — hər `yes` cavabı üçün açılır: "Necə kontrol edilir?" (CheckboxList: dərman · həkim nəzarəti · həyat tərzi · həkim tövsiyəsi yox)
  3. **medications_current** — MultiSelectChip 12 kateqoriya (qan-təzyiqi · qan-incəltici · diuretik · ürək · diabet · psixiatrik · qida əlavəsi · digər...) + TextField "ad yaz"
  4. **medical_disclaimer_accepted_at** — Checkbox (məcburi)
- **Helper:** "Heç bir cavab heç kimlə paylaşılmır. Plan AZ mütəxəssisi yalnız təhlükəsizlik üçün baxar."
- **CTA:** Davam et (məcburi disclaimer + heç bir yes / və ya bütün follow-up dolu) / Geri
- **Cross-ref:** `data-catalog §medical_conditions` · `data-catalog §medications_current` · `data-catalog §medical_disclaimer` · `pc §6.3 hard-stops` · `questions §4.4 + §6.5`
- **Safety:** Skip = ❌ məcburi. Skip cəhdi → curated static template (AI generasiyası bloklanır). ≥2 "yes" əgər "həkim tövsiyəsi yox" → **MedicalRedFlagModal** (#18) tetiklənir.

### #17 · L5 · SCOFFScreen (sensitive)

- **Trigger:** Q5 (experience) advanced + Q1 (goal) = cut + L2-A target_weight delta >5 kg azalma kombinasiyası; və ya L2-B stress_pss4 ≥ 12
- **Pre-screen:** sensitive consent modal — "Sualların bəzisi yemə davranışı haqqındadır. Cavablar gizli qalır. Davam edirsənmi?" → Davam et / Skip ✅
- **Variant:** RadioCard ×5 (SCOFF — 5 yes/no sual)
- **Display:** "Bir neçə sual yemə vərdişləri ilə bağlıdır"
- **Subtitle:** "Bu, planın güvənli sıralanması üçündür. Tibbi məsləhət deyil."
- **Answer:**
  1. Çox dolmuş hiss edənə qədər yedikdən sonra ürək bulanmasını məcbur edirsənmi?
  2. Yemək yeməyi nə vaxt yedirdiyini idarə edə bilmədiyindən narahatsanmı?
  3. Son 3 ayda 6 kg-dan çox itirmisənmi?
  4. Başqaları arıq olduğunu desə də özünü piy hiss edirsənmi?
  5. Yemək həyatına dominant rol oynayır?
- **Helper:** "≥2 'bəli' cavabı varsa, sənə **mütəxəssis yoxlaması** təklif edirik."
- **CTA:** Davam et / Sonra (Ghost — sensitive-consent skip mövcuddur)
- **Cross-ref:** `data-catalog §eating_disorder_history` · `data-catalog §scoff_score`
- **Safety:** Skor ≥2 → plan AI generasiyası **deferred to queue + mütəxəssis yoxlaması məcburi** (premium); free user üçün → curated static template + LinkText resurslar (Azərbaycan Psixologiya Cəmiyyəti / həkim tap)
- **Skip:** ✅ pre-consent rejection ilə (analytics `l5_scoff_declined`)

### #18 · L5 · MedicalRedFlagModal — terminal

- **Trigger:** PAR-Q ≥2 yes + "həkim tövsiyəsi yox" KOMB və ya VAS ≥7 və ya pregnancy_postpartum=true (bütün hard-stop birləşmələri)
- **Variant:** static terminal modal (no field input)
- **Display:** "⚠ Mütəxəssis yoxlaması tələb olunur"
- **Subtitle:** "Cavabların əsasında plan AI generasiyası dayandırıldı."
- **Body:** "Sənin sağlamlıq vəziyyətin AI plan generasiyası üçün uyğun deyil. **Mütəxəssis uyğunluq yoxlaması** lazımdır. Bu **tibbi məsləhət deyil** — həkim ilə əlaqəyə keç və plan istəyini yenilə."
- **Helper:** AZ Səhiyyə Nazirliyi tövsiyə linki + "Tap həkim" CTA secondary
- **CTA:** "Anladım" (Primary, dismiss-only) / "Mənbələr" (Ghost link)
- **Cross-ref:** `pc §6.3` · `questions §4.4` · `CLAUDE.md` Qəti Qadağalar (pregnancy hard-stop)
- **Behavior:** terminal — back yox, dismiss-only. Account state-də `plan_status = blocked_medical`. Premium üçün insan-onayı queue-da `red_flag = true` tag.

### #19 · L5 · HealthDataConsent

- **Trigger:** L5-16 və ya L2-C ilk açılış əvvəli — sensitive data toplamaq üçün hard-gate
- **Variant:** Checkbox (məcburi) + LinkText (GDPR Art.9) + LinkText (Privacy Policy AZ/RU/EN)
- **Display:** "Sağlamlıq məlumatı icazəsi"
- **Subtitle:** "GDPR Art.9 (sensitive məlumat) altında açıq icazəni təsdiqlə."
- **Body (numbered list):**
  1. **Hansı məlumat:** yaralanmalar · dərmanlar · sikl (opt-in) · stress · yuxu · pain VAS
  2. **Necə işlənir:** plan AI input-u + mütəxəssis yoxlaması (premium) + lokal SQLDelight saxlama
  3. **Kimə açıq:** sənin akkauntundan başqa heç kim (mütəxəssis isimsiz queue snapshot görür)
  4. **İstənilən vaxt geri çək:** Settings → Hesab → Məlumat → "Sağlamlıq məlumatımı sil"
- **Answer:** Checkbox **"Razıyam — GDPR Art.9 altında sağlamlıq məlumatımın yuxarıdakı qaydalarla işlənməsinə icazə verirəm"** (məcburi)
- **CTA:** Davam et (yalnız checkbox aktiv) / İcazəni rədd et (Ghost — L5 axını terminallaşdırır, plan generasiyası static template-ə düşər)
- **Cross-ref:** `data-catalog §health_data_consent_at` · `pc §10.3 hesab silmə` · `onb §3.7 privacy gates`
- **Skip:** ❌ məcburi; rejection → static template fallback + analytics `l5_consent_declined`

---

## `.pen` build qeydləri (v2 — master-aligned)

1. **Şablon master frame:** Q1-Q7-dən `question_template` master klonla; variant property dəyişdir. L3 üçün bottom-sheet variant (yuxarı corner-radius 24, header `[×]`), L5 üçün sticky `$warning` banner variant.
2. **Layout — 03 Questions section altında:**
   - L2 row (6 frame): y = 11500 (Q7 row-dan ~2000px aşağı), 6 frame horizontal (375w + 45 gap)
   - L3 row (9 frame): y = 12500, 9 frame horizontal — overflow olarsa 5+4 iki sırada
   - L5 row (4 frame): y = 13500, 4 frame horizontal
3. **Token-only.** Bütün rəng/typo `project-context.md §1b` Volt token-lərindən. L5-də `$warning` banner + `$danger` red-flag. Hard-coded hex YASAQ.
4. **L5 frame fərqi:**
   - Progress dot strip YOX
   - Üstdə sticky `$warning` 36pt banner (ikon 20 + 13/500 metn "Təhlükəsizlik yoxlaması — tibbi məsləhət deyil")
   - Back ikon var, **skip yalnız L5-17 (SCOFF sensitive-consent ilə)**
5. **L3 bottom sheet:** header `[×]` close, zəmin `$surface-elevated`, üst corner radius 24, alt corner radius 0, progress strip YOXDUR.
6. **L2 accordion entry frame DAXİL DEYİL** — onsuz da `ux-auth-onboarding` ekran #20-də mövcud.
7. **Hər input variant üçün error klon:** `$danger` border + InlineError `13/500 $danger` + ikon 16 (`lucide: triangle-alert` — `alert-triangle` YOX). Kodlar: `L2_RANGE_*` `L3_RANGE_*` `L5_DISCLAIMER_REQ` `L5_RED_FLAG` (`prd-auth-data-model §2.5`).
8. **AZ default** — RU/EN locale variant property sonradan (theme variable).
9. **L5-#16 və L5-#17 üzərində `MedicalRedFlagModal` (#18) referansı** annotation kimi (modal terminal, ekran sayılmır lakin overlay olaraq açılır).
10. **L3 sheet height:** dynamic — minimum 480, maksimum 720 (812-status-handle-zona). Tək field 480, qrid 4+ field 720.
11. **L3-#12 (CycleSheet) muted dim 50% opacity** — "Faza 2 rezerv" placeholder etiketi ilə.
12. **Cross-section pointer:** L5 row başlığında `04 Post-onboarding` chip ([[bridge frame ux-onboarding-questions §10]] kimi) — istifadəçi tappable yox, designer reference.

**Inventory:** 6 (L2 A-F) + 9 (L3) + 4 (L5) = **19 ekran** ✓

---

## Changelog

- **v1 (2026-05-23 səhər):** ilkin spec — Phase 4 paralel agent output, L2/L3/L5 inventarı master-dən divergent (Bədən tipi / Yuxu / equipment-home+gym / language-pref / cuisine-pref / pregnancy-postpartum kimi field-lər var idi)
- **v2 (2026-05-23 axşam):** master inventar (`ux-onboarding-questions §4.2/4.3/4.4`) kanonik qəbul edildi; spec tam yenidən yazıldı — L2 A-F, L3 ×9 (Measurement/TargetWeight/Injury/Pain/HealthSnapshot/Cycle-F2/DislikeFood/PowerMilestone/EventTarget), L5 ×4 (MedicalSafety/SCOFF/RedFlag/HealthConsent). `.pen` build qeydləri v2-yə uyğunlaşdırıldı. Voice qaydaları + "məşqçi/coach" qadağan + "mütəxəssis yoxlaması" termin sabitləndi (CLAUDE.md Qəti Qadağalar v3.3).
