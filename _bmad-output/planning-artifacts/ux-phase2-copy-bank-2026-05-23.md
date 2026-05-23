# Phase 2 — AZ Final Copy Polish Bank

Hazırlanma tarixi: 2026-05-23
Mənbə: feature `detect-design-ui-step-and-tools` · Phase 2 paralel agent output
Şamil: `.pen` `app_design.pen` faylındakı 25 mövcud ekran + V variantları + persona-cell cədvəlləri

---

## Voice & terminologiya
- minimalist, motivasiyaverici, direct, AZ-native, user-agentic (kahraman istifadəçi)
- QADAĞA: "AI sənə plan qurdu" tipli kahraman copy · "trainer/coach/canlı məşqçi/personal coach" · tibbi kontekstdə "təsdiq/approval"
- DÜZGÜN: "mütəxəssis yoxlaması", "uyğunluq yoxlaması", "öz məşqini idarə et"
- Off-round numerics: 47, 1.847, 23%, 2.347 kkal, 147 q
- Persona-cell aware: `{context × sex × goal}` — copy hər birinə nötr qalır

---

## Section 02 — Onboarding (Welcome → Auth)

| # | Node ID | Ekran | Element | AZ Final Copy |
|---|---------|-------|---------|---------------|
| 1 | — | Welcome | eyebrow | Salam |
| 1 | — | Welcome | display title | Öz məşqini idarə et |
| 1 | — | Welcome | subtitle | Planla. İzlə. Ölç. — bir yerdə, Azərbaycan dilində. |
| 1 | — | Welcome | primary CTA | Başla |
| 1 | — | Welcome | secondary CTA | Hesabın var? Daxil ol |
| 1 | — | Welcome | helper | Davam etməklə **Məxfilik Siyasəti** və **İstifadə Şərtləri**-ni qəbul edirsən. |

## Section 03 — Questions Q1-Q7

| # | Node ID | Element | AZ Final Copy |
|---|---------|---------|---------------|
| Q1 | S5QT23 | eyebrow | 1 / 7 · Hədəf |
| Q1 | S5QT23 | display title | Niyə buradasan? |
| Q1 | S5QT23 | subtitle | Plan bu seçimə görə qurulur. İstədiyin vaxt dəyişə bilərsən. |
| Q1 | S5QT23 | option 1 title | Kütlə yığ — əzələ böyütmək |
| Q1 | S5QT23 | option 1 sub | Həftədə 4-6 məşq, kalori artıqlığı |
| Q1 | S5QT23 | option 2 title | Arıqla — yağ at, formanı qoru |
| Q1 | S5QT23 | option 2 sub | Defisit + həcm qoruması |
| Q1 | S5QT23 | option 3 title | Formada qal — ümumi sağlamlıq |
| Q1 | S5QT23 | option 3 sub | Həftədə 2-3 məşq, balanslaşmış |
| Q1 | S5QT23 | primary CTA | Davam et |
| Q2 | ObxuP | eyebrow | 2 / 7 · Cins |
| Q2 | ObxuP | display title | Cinsini seç |
| Q2 | ObxuP | subtitle | Kalori və protein hesabı bu sahəyə bağlıdır. |
| Q2 | ObxuP | option 1 | Qadın |
| Q2 | ObxuP | option 2 | Kişi |
| Q2 | ObxuP | primary CTA | Davam et |
| Q3 | owS2i | eyebrow | 3 / 7 · Yaş |
| Q3 | owS2i | display title | Neçə yaşın var? |
| Q3 | owS2i | subtitle | 13 yaşdan kiçik istifadəçilər üçün app icazə vermir. |
| Q3 | owS2i | placeholder | 24 |
| Q3 | owS2i | inline error (<13) | Bu app 13 yaşdan kiçik istifadəçilər üçün nəzərdə tutulmayıb |
| Q3 | owS2i | inline error (>99) | Yaş 13-99 aralığında olmalıdır |
| Q3 | owS2i | primary CTA | Davam et |
| Q4 | qXLw8 | eyebrow | 4 / 7 · Ölçülər |
| Q4 | qXLw8 | display title | Boyun və çəkin? |
| Q4 | qXLw8 | subtitle | Kalori, protein və su hədəfləri üçün lazımdır. Saxlama yoxdur, məlumat səndə qalır. |
| Q4 | qXLw8 | label A / placeholder | Boy (sm) / 174 |
| Q4 | qXLw8 | label B / placeholder | Çəki (kq) / 73,4 |
| Q4 | qXLw8 | unit toggle | sm·kq / ft·lb |
| Q4 | qXLw8 | inline error | Boy 120-230 sm, çəki 30-250 kq aralığında |
| Q4 | qXLw8 | primary CTA | Davam et |
| Q5 | i1Vu9 | eyebrow | 5 / 7 · Təcrübə |
| Q5 | i1Vu9 | display title | İdmanla əlaqən necədir? |
| Q5 | i1Vu9 | subtitle | Səmimi cavab ən yaxşı plan deməkdir. |
| Q5 | i1Vu9 | option 1 | Yenibaşlayan / İlk 6 ay və ya ara verib qayıtmışam |
| Q5 | i1Vu9 | option 2 | Orta səviyyə / 6 ay – 2 il sabit məşq edirəm |
| Q5 | i1Vu9 | option 3 | Təcrübəli / 2+ il, super-set və ileri texnikalarla işləyirəm |
| Q5 | i1Vu9 | primary CTA | Davam et |
| Q6 | F16e8 | eyebrow | 6 / 7 · Yer |
| Q6 | F16e8 | display title | Harada məşq edirsən? |
| Q6 | F16e8 | subtitle | Avadanlığa görə hərəkətlər və alternativlər seçilir. |
| Q6 | F16e8 | option 1 | Zalda ciddi məşq / Tam avadanlıq, ölçülə bilən hədəflər |
| Q6 | F16e8 | option 2 | Zalda yüngül məşq / Forma qorumaq, həftədə bir-iki dəfə |
| Q6 | F16e8 | option 3 | Evdə məşq / Az avadanlıq və ya yalnız bədən çəkisi |
| Q6 | F16e8 | primary CTA | Davam et |
| Q7 | H0uZ0e | eyebrow | 7 / 7 · Cədvəl |
| Q7 | H0uZ0e | display title | Həftədə neçə dəfə? |
| Q7 | H0uZ0e | subtitle | Bir sessiya nə qədər çəkir? Reallıq olsun — sonra dəyişə bilərsən. |
| Q7 | H0uZ0e | segment A | 2 · 3 · 4 · 5 · 6 · 7 |
| Q7 | H0uZ0e | segment B | 15 dəq · 30 · 45 · 60 |
| Q7 | H0uZ0e | primary CTA | Bitir |

## ProfilePreview (u1cEVR)

| Element | AZ Copy |
|---------|---------|
| eyebrow | Profilin |
| display title | Səni belə tanıdıq — düzdürmü? |
| subtitle | Bu rəqəmlər başlanğıc nöqtəsidir. Hərəkət etdikcə dəqiqləşir. |
| metric 1 | Gündəlik kalori · 2.347 kkal |
| metric 2 | Protein hədəfi · 147 q |
| metric 3 | Su · 2,6 L |
| metric 4 | Həftəlik məşq · 4 gün · 45 dəq |
| helper | Düsturlar elmi əsasdadır (Mifflin-St Jeor). Cavabını dəyişsən, rəqəmlər anında yenilənir. |
| primary CTA | Düzdür, davam |
| secondary CTA | Cavabı dəyişəcəm |

## AI Disclosure (eQcvv) — VERIFY only, kanonik

Body (`Cs4v3/z2ZaHx`): "Plan AI tərəfindən elmi əsaslarla qurulur — son qərar səndədir, istədiyin vaxt dəyişə bilərsən. **Premium istifadəçilərdə plan, göndərilmədən əvvəl bir mütəxəssis tərəfindən yoxlanılıb onaylanır.** Bu tibbi məsləhət deyil — sağlamlıq probleminiz varsa məşqdən əvvəl həkimə müraciət edin."

## Auth flow

| Ekran | Element | AZ |
|-------|---------|-----|
| AuthGate | eyebrow | Hesab yarat və ya daxil ol |
| AuthGate | display | Cavabların itməsin |
| AuthGate | subtitle | Cavabladığın 7 sual hesabına bağlanır — yeni cihazda da əldə edə bilərsən. |
| AuthGate | iOS btn 1 | Apple ilə davam et |
| AuthGate | iOS btn 2 | Google ilə davam et |
| AuthGate | btn 3 | E-poçt ilə davam et |
| AuthGate | footer | Hesabın var? **Daxil ol** |
| AuthGate | offline toast | Bağlantı yoxdur — yenidən cəhd et |
| Signup | display | Hesab yarat |
| Signup | subtitle | E-poçt və parol — sadəcə bu. |
| Signup | label A | E-poçt · ad@nümunə.az |
| Signup | label B | Parol · helper: Ən az 8 simvol və 1 rəqəm |
| Signup | label C | Parolu təkrarla |
| Signup | primary | Hesab yarat |
| Signup | error AUTH_001 | E-poçt düzgün deyil |
| Signup | error AUTH_002 | Parol ən az 8 simvol + 1 rəqəm olmalıdır |
| Signup | error AUTH_003 | Bu e-poçt artıq qeydiyyatdadır — **Daxil ol** |
| Signup | error AUTH_006 | Çox cəhd etdin — 15 dəqiqədən sonra yenidən cəhd et |
| Login | display | Daxil ol |
| Login | secondary CTA | Parolu unutdun? |
| Login | primary | Daxil ol |
| Login | error AUTH_004 | E-poçt və ya parol səhvdir |
| Login | error AUTH_005 | E-poçtun hələ təsdiqlənməyib — **Linki yenidən göndər** |
| ConfirmEmail | eyebrow | E-poçtunu yoxla |
| ConfirmEmail | display | Linki tıkla, qayıt |
| ConfirmEmail | subtitle | **ad@nümunə.az** ünvanına təsdiq linki göndərdik. Görmürsən? Spam qovluğuna bax. |
| ConfirmEmail | timer | Yenidən göndər: 47 san |
| ConfirmEmail | secondary | E-poçtu dəyiş |
| ResetEmail | display | Parolu sıfırla |
| ResetEmail | subtitle | E-poçtuna sıfırlama linki göndərəcəyik. |
| ResetEmail | primary | Link göndər |
| ResetEmail | success toast | Əgər bu e-poçt qeydiyyatdadırsa, link göndərildi |
| ResetForm | display | Yeni parol qur |
| ResetForm | helper | Ən az 8 simvol və 1 rəqəm |
| ResetForm | primary | Saxla və daxil ol |
| ResetForm | error AUTH_013 | Link vaxtı keçib — **Yeni link tələb et** |

## Section 04 — Post-Onboarding & Settings

| Ekran | Element | AZ |
|-------|---------|-----|
| Pregnancy (M52XdD) | eyebrow | Sağlamlıq — opsiyonel |
| Pregnancy | display | Hamilə və ya yeni doğmuşsan? |
| Pregnancy | subtitle | Bu məlumatı bizə bildirsən, sənə uyğun, ehtiyatlı şablon hazırlayırıq. Hər zaman Tənzimləmələrdə dəyişə bilərsən. |
| Pregnancy | option 1 | Bəli, hamiləyəm |
| Pregnancy | option 2 | Bəli, yeni doğmuşam (postpartum) |
| Pregnancy | option 3 | Xeyr, heç biri |
| Pregnancy | secondary | İndi yox, sonra |
| Pregnancy | helper | Cavabın yalnız hesabında saxlanılır. |
| Paywall (ij7jR) | eyebrow | Premium |
| Paywall | display | Daha dərinə getməyə hazırsan? |
| Paywall | subtitle | 5 yox — 100+ hərəkət. Aylıq 1 plan yox — istədiyin qədər. Üstəlik hər planın göndərilməzdən əvvəl mütəxəssis tərəfindən yoxlanılır. |
| Paywall | bullet 1 | 100+ hərəkət · GIF + MP4 + AZ formanot |
| Paywall | bullet 2 | Limitsiz AI plan + həftəlik adaptasiya |
| Paywall | bullet 3 | Mütəxəssis uyğunluq yoxlaması |
| Paywall | bullet 4 | Limitsiz foto-kalori |
| Paywall | bullet 5 | Ramazan rejimi · M və F səs |
| Paywall | option 1 | 7 gün pulsuz sına / Sonra **8 AZN/ay** — istənilən vaxt ləğv et |
| Paywall | option 2 | İllik plan — 60 AZN / **5 AZN/ay effektiv** — 38% qənaət |
| Paywall | option 2 badge | Tövsiyə olunan |
| Paywall | primary | Davam et |
| Paywall | secondary | İndi yox |
| Paywall | footer | Ləğv tək tapla. Avtomatik yenilənmə şərtləri açıqdır. |
| Logout Sync (y4JLHj) | eyebrow | Diqqət |
| Logout Sync | display | 3 sessiya hələ sinxronlaşmayıb |
| Logout Sync | body | Çıxsan, sinxronlaşmamış məşqlər silinə bilər. Əvvəlcə göndərək? |
| Logout Sync | primary | Göndər və çıx |
| Logout Sync | secondary | Anladım, sil |
| Logout Sync | tertiary | İmtina et |
| DeleteConf1 | eyebrow | Hesabı sil |
| DeleteConf1 | display | Bu addım geri qaytarıla bilməz |
| DeleteConf1 | body | Bütün məşqlərin, ölçülərin, fotoların və profilin 30 gün ərzində soft-archive olunur — bu müddətdə geri qayıtsan, hər şey qaytarılır. 30 gün sonra hər şey həmişəlik silinir. |
| DeleteConf1 | primary | Davam et |
| DeleteConf1 | secondary | İmtina et |
| DeleteConf2 | display | Hesabı silməyə əminsənmi? |
| DeleteConf2 | body | Təsdiqləmək üçün **SİL** yaz. |
| DeleteConf2 | placeholder | SİL |
| DeleteConf2 | primary | Hesabı sil |
| DeleteConf2 | toast | Hesabın silindi. 30 gün ərzində geri dönsən, məlumat bərpa olunur. |
| ProCoaching | eyebrow | Yaxında |
| ProCoaching | display | Fərdi mütəxəssis dəstəyi — yaxında |
| ProCoaching | body | İstədiyin mütəxəssisi seç, fərdi məsləhət al. Bu xidmət hazırda hazırlanır — maraqlıdırsa, e-poçtuna birinci xəbər göndəririk. |
| ProCoaching | primary | Məni xəbərdar et |
| ProCoaching | helper | E-poçtun yalnız bu məqsədlə saxlanılır. |

## V variantları (state)

| Variant | Ekran | Element | AZ |
|---------|-------|---------|-----|
| V1 empty | Hərəkət preview | display | Hələ heç bir hərəkət əlavə etməmisən |
| V1 empty | — | sub | Filtrlərdən birini seç və ya "Hamı"ya keç |
| V1 empty | — | CTA | Filtrləri sıfırla |
| V2 error | Generic | display | Nə isə yanlış getdi |
| V2 error | — | sub | Yenidən cəhd et — problem davam edirsə, bir az gözlə. |
| V2 error | — | primary | Yenidən cəhd et |
| V2 error | — | secondary | Geri qayıt |
| V3 loading | Generic | display | Bir saniyə... |
| V3 loading | — | sub (>10s) | Bağlantı yavaşdır — gözləyirik |
| V3 loading | — | sub (sync) | Cavablarını yadda saxlayırıq |
| V4 offline | Sticky banner | text | Offline — yenə də davam edə bilərsən |
| V4 offline | — | sub | Bağlantı qayıtdıqda göndərəcəyik |
| V5 success | Generic | display | Hazır |
| V5 success | profile sync | text | Profilin saxlandı |
| V5 success | reset | text | Parol yeniləndi — daxil ola bilərsən |
| V6 disabled | — | helper (consent) | Davam etmək üçün qutucuğu işarələ |
| V6 disabled | — | helper (disclaimer) | Tibbi qeydi qəbul etmək lazımdır |

## Persona-cell variants (Paywall sub + Welcome accent)

| Cell | Paywall sub | Welcome accent |
|------|-------------|----------------|
| home × F × cut | Evdə, səssiz hərəkətlərlə formanı qoru. 20 dəq, gizliliyi qorunan rejim. | 20 dəqiqəlik məşq — küy və alət yox. |
| home × M × general_fit | Evdə, az avadanlıqla. Bədən çəkisi + tempo işə düşür. | Avadanlıq yox — tempo işə qoş. |
| casual_gym × F × general_fit | Həftədə 2-3 məşq — formanı qoru, irəlilədikcə özünü hiss et. | Aram-aram irəlilə. |
| casual_gym × M × general_fit | Həftədə 3 məşq, sadə struktur, dəqiq ölçü. | 3 məşq — və davamlılıq. |
| serious_gym × F × bulk | Glute və üst gövdə həcm fokusu. RPE, tonnage, həftəlik PR. | Volume + texnika — irəli get. |
| serious_gym × F × cut | Defisitdə güc qoru. Recomp framing. | Şəkilləndir — güc itməsin. |
| serious_gym × M × bulk | Mexaniki gərginlik, RPE 7-9, super-set hazır. | RPE 8 — və izlədiyini ölç. |
| serious_gym × M × cut | Defisitdə həcm qoru, kardio modullu. | Həcm qoru, çəki düşür. |

---

## Verification checklist
- [ ] Heç bir "trainer/coach/canlı məşqçi" sözü yoxdur — yalnız "mütəxəssis yoxlaması"
- [ ] "AI sənə plan qurdu" kahraman copy yoxdur
- [ ] Off-round numerics: 2.347 / 147 / 47 / 8 AZN / 60 AZN / 5 / 38%
- [ ] Paywall = trial + illik 2 seçim
- [ ] AI Disclosure (eQcvv/Cs4v3/z2ZaHx) kanonikdir, DƏYİŞDİRMƏ
- [ ] "müvəffəqiyyət" / "təlimatlar" uzun sözləri copy-də yoxdur
