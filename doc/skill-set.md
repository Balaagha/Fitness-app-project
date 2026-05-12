# fitnessApp — Tam Skill & Komut Haritası

67 BMad skill + plugin'ler. Kurulu olan her şey burada, proje ile ilgisine göre işaretlenmiş.

---

## Hızlı Başvuru

| Ne yapmak istiyorum | En iyi araç |
|---------------------|-------------|
| Bir sonraki adım ne? | `bmad-help` |
| Araştırma / ideation | `bmad-agent-analyst` (Mary) |
| PRD / Feature spec | `bmad-agent-pm` (John) → CP |
| Mimari karar | `bmad-agent-architect` (Winston) → CA |
| Story implement | `bmad-agent-dev` (Amelia) → DS |
| Hızlı kod değişikliği | `bmad-quick-dev` |
| UX akışı | `bmad-agent-ux-designer` (Sally) → CU |
| Adversarial review | `bmad-review-adversarial-general` |
| Rakip / iş stratejisi | `bmad-cis-agent-innovation-strategist` (Victor) |
| Feature brainstorm | `bmad-cis-agent-brainstorming-coach` (Carson) |
| İnsan gözü gerekli | `bmad-checkpoint-preview` |
| Stakeholder güncelleme | `/stakeholder-update` (PM Plugin) |
| Metrik analizi | `/metrics-review` (PM Plugin) |
| Feature oturumu | `/feature-start <ad>` |

---

## BMad — Phase 1: Analiz

| Skill | Agent / Persona | Ne yapar | Proje ile ilgisi |
|-------|----------------|----------|-----------------|
| `bmad-help` | — | Context-aware sonraki adım önerisi | ⭐ Her oturum başı |
| `bmad-agent-analyst` | Mary 📊 | Araştırma, ideation, pazar analizi | ⭐ Aktif faz |
| `bmad-technical-research` | — | Tech stack validasyonu | ⭐ KMM/Supabase/GCP seçimi |
| `bmad-market-research` | — | Pazar araştırması | ⭐ AZ fitness market |
| `bmad-brainstorming` | — | 100+ fikir üretme | ⭐ Feature keşfi |
| `bmad-product-brief` | — | Formal product brief | ⭐ Araştırma tamamlanınca |
| `bmad-prfaq` | — | Working Backwards stres testi | ⭐ Brief sorgulamak için |
| `bmad-domain-research` | — | Domain deep-dive | ⭐ KMM/Supabase araştırması |

---

## BMad — Phase 2: Planlama

| Skill | Agent / Persona | Ne yapar | Proje ile ilgisi |
|-------|----------------|----------|-----------------|
| `bmad-agent-pm` | John 📋 | PRD, epics, stakeholder sync | ⭐ Ana planlama ajanı |
| `bmad-create-prd` | — | Yapılandırılmış PRD workflow | ⭐ PM agent üzerinden |
| `bmad-validate-prd` | — | PRD kalite kontrolü | ⭐ PRD tamamlanınca |
| `bmad-edit-prd` | — | PRD düzeltme | ⭐ Validasyon sonrası |
| `bmad-agent-ux-designer` | Sally 🎨 | UX akışı, wireframe, kullanıcı yolculuğu | ⭐ PRD sonrası |
| `bmad-create-ux-design` | — | UX tasarım workflow | ⭐ UX agent üzerinden |

---

## BMad — Phase 3: Solutioning

| Skill | Agent / Persona | Ne yapar | Proje ile ilgisi |
|-------|----------------|----------|-----------------|
| `bmad-agent-architect` | Winston 🏗️ | Teknik mimari, trade-off analizi | ⭐ KMM + Supabase arch |
| `bmad-create-architecture` | — | Mimari karar belgesi | ⭐ Architect üzerinden |
| `bmad-create-epics-and-stories` | — | Epic + story breakdown | ⭐ Mimari sonrası |
| `bmad-check-implementation-readiness` | — | Implementasyona hazır mı? | ⭐ Story'lere geçmeden |

---

## BMad — Phase 4: İmplementasyon

| Skill | Agent / Persona | Ne yapar | Proje ile ilgisi |
|-------|----------------|----------|-----------------|
| `bmad-agent-dev` | Amelia 💻 | Story execution, TDD, code review | ⭐ Her story için |
| `bmad-quick-dev` | — | Hızlı kod değişikliği — tam workflow olmadan | ⭐ Küçük fix/tweak |
| `bmad-dev-story` | — | Story implementasyonu (red→green→refactor) | ⭐ Amelia üzerinden |
| `bmad-create-story` | — | Story hazırlama + context yükleme | ⭐ Her story öncesi |
| `bmad-sprint-planning` | — | Sprint planı oluştur / güncelle | ⭐ Sprint başında |
| `bmad-sprint-status` | — | Sprint durumu raporu | ⭐ Haftalık kontrol |
| `bmad-code-review` | — | Çok boyutlu kod incelemesi | ⭐ Her story sonrası |
| `bmad-checkpoint-preview` | — | İnsan gözü — değişikliği anlamlandır, odaklan | ⭐ Merge öncesi |
| `bmad-qa-generate-e2e-tests` | — | API + E2E test üretimi | ⭐ Feature tamamlanınca |

---

## BMad — Her Zaman Kullanılabilir (Anytime)

| Skill | Ne yapar | Proje ile ilgisi |
|-------|----------|-----------------|
| `bmad-review-adversarial-general` | Sert cynical review — 10+ sorun garantili | ⭐⭐ Solo dev için kritik |
| `bmad-review-edge-case-hunter` | Path tracing, edge case tarama | ⭐ Her kritik feature |
| `bmad-advanced-elicitation` | Output'u ikinci geçişle derinleştir | ⭐ Yetersiz sonuç gelince |
| `bmad-party-mode` | Multi-agent tartışma | ⭐ Zor mimari kararlar |
| `bmad-distillator` | Uzun belgeyi 3:1 sıkıştır | ⭐ Büyük spec'leri kısalt |
| `bmad-correct-course` | Scope sapması / yoldan çıkınca düzelt | ⭐ Scope kayarsa |
| `bmad-retrospective` | Epic sonrası lesson learned | ⭐ Her epic bitince |
| `bmad-agent-tech-writer` | Paige 📚 — teknik dokümantasyon | ○ API docs, README |
| `bmad-editorial-review-prose` | Metin iletişim sorunları — kopya edit | ○ AZ/RU/EN içerik |
| `bmad-editorial-review-structure` | Yapısal edit — kes, yeniden düzenle | ○ Büyük spec'ler |
| `bmad-shard-doc` | Büyük markdown'ı bölümlere ayır | ○ PRD/arch büyürse |
| `bmad-index-docs` | Klasördeki tüm doc'lar için index.md | ○ Docs organize |
| `bmad-generate-project-context` | `docs/project-context.md` üret/güncelle | ○ Context sıfırlamak |
| `bmad-document-project` | Brownfield projeyi AI için dokümante et | ○ Mevcut codebase'i açıkla |
| `bmad-qa-generate-e2e-tests` | API + E2E test üretimi | ⭐ Feature tamamlanınca |

---

## BMad — CIS Modülü (Creative Intelligence System)

Yaratıcı düşünce, iş stratejisi ve sunum için özel ajanlar. Fitness app için en alakalı olanlar işaretlendi.

| Skill | Agent / Persona | Ne yapar | Proje ile ilgisi |
|-------|----------------|----------|-----------------|
| `bmad-cis-agent-innovation-strategist` | Victor ⚡ | Disruptive inovasyon, business model | ⭐ AZ pazar stratejisi |
| `bmad-cis-agent-brainstorming-coach` | Carson 🧠 | Facilitated ideation, yes-and | ⭐ Feature brainstorm |
| `bmad-cis-agent-design-thinking-coach` | Maya 🎨 | Human-centered design, empathy | ⭐ UX araştırması |
| `bmad-cis-agent-creative-problem-solver` | Dr. Quinn 🔬 | TRIZ + sistemik problem çözme | ○ Zor mimari sorunlar |
| `bmad-cis-agent-presentation-master` | Caravaggio 🎬 | Pitch deck, App Store materyalleri | ○ Yatırımcı / launch |
| `bmad-cis-agent-storyteller` | Sophia 📖 | Narrative, marka hikayesi | ○ Marketing copy |
| `bmad-cis-design-thinking` | — | Design thinking workshop workflow | ○ UX research session |
| `bmad-cis-innovation-strategy` | — | İnovasyon stratejisi workflow | ○ Strateji session |
| `bmad-cis-problem-solving` | — | Yapısal problem çözme workflow | ○ Zor sorunlar |
| `bmad-cis-storytelling` | — | Narrative oluşturma workflow | ○ Marketing |

---

## BMad — TEA Modülü (Test Engineering)

| Skill | Ne yapar | Proje ile ilgisi |
|-------|----------|-----------------|
| `bmad-tea` | Murat 🧪 — test mimarisi danışmanı | ⭐ Test stratejisi |
| `bmad-testarch-framework` | Test framework seçimi | ⭐ KMM test setup |
| `bmad-testarch-test-design` | Test tasarımı | ⭐ Her feature |
| `bmad-testarch-atdd` | ATDD — kabul testi scaffold | ⭐ Story öncesi |
| `bmad-testarch-ci` | CI pipeline konfigürasyonu | ⭐ GitHub Actions |
| `bmad-testarch-nfr` | Non-functional requirements testi | ○ Perf/güvenlik |
| `bmad-testarch-automate` | Test otomasyon kapsamını genişlet | ○ Coverage artırma |
| `bmad-testarch-test-review` | Test kalite review | ○ Test inceleме |
| `bmad-testarch-trace` | Traceability matrix | ○ Requirement → test |
| `bmad-teach-me-testing` | Test öğrenimi (progressif) | — İlgisiz (expert dev) |

---

## BMad — Meta / Builder Araçları

Yeni skill, workflow veya modül oluşturmak için. Günlük kullanım değil.

| Skill | Ne yapar |
|-------|----------|
| `bmad-customize` | Agent/workflow customization override'ları yaz |
| `bmad-workflow-builder` | Yeni workflow oluştur / düzenle |
| `bmad-agent-builder` | Yeni ajan oluştur / düzenle |
| `bmad-module-builder` | Yeni BMad modülü planla + yarat |
| `bmad-bmb-setup` | BMad Builder modülü kur |
| `bmad-eval-runner` | Skill eval'larını izole ortamda çalıştır |

---

## 2. Product Management Plugin

**`product-management@knowledge-work-plugins` — aktif**

| Komut | BMad Karşılığı | Karar |
|-------|---------------|-------|
| `/write-spec` | `bmad-create-prd` | BMad daha kapsamlı; hızlı taslak için PM plugin |
| `/competitive-brief` | `bmad-market-research` | BMad daha derin |
| `/roadmap-update` | `bmad-edit-prd` | BMad'ı tercih et |
| `/brainstorm` | `bmad-brainstorming` | BMad'ı tercih et |
| `/sprint-planning` | `bmad-sprint-planning` | BMad'ı tercih et |
| `/synthesize-research` | `bmad-domain-research` | BMad'ı tercih et |
| **`/stakeholder-update`** | ❌ BMad'da yok | **UNIQUE — bu plugin'den kullan** |
| **`/metrics-review`** | ❌ BMad'da yok | **UNIQUE — bu plugin'den kullan** |

MCP entegrasyonları (ek auth): Linear, Notion, Jira, Slack, Figma, Amplitude, Intercom.

---

## 3. Feature-Memory Komutları

**Çakışma yok — BMad workflow'ları ile paralel çalışır**

| Komut | Ne yapar |
|-------|----------|
| `/feature-start <ad>` | Yeni oturum — `feature.md` + `notes.md` |
| `/feature-status` | Aktif feature durumu |
| `/feature-decision "<metin>"` | Karar tablosuna ekle |
| `/feature-finding "<metin>"` | Bulgu ekle |
| `/feature-note [etiket] "<metin>"` | Hızlı not (`[impl]` `[gotcha]` `[criteria]` `[refs]` `[invariant]`) |
| `/feature-compact` | notes.md temizle + arşivle |
| `/feature-pause` / `/feature-resume <ad>` | Oturum yönetimi |
| `/feature-end` | Tamamla + arşivle |

---

## 4. Skill Creator

Yeni Claude Code skill'i yaz, mevcut skill'i güncelle. BMad meta-araçlarıyla minör örtüşme.

---

## 5. YouTube Haftalık Özet

Global skill — bu projede tetiklenmez, ilgisiz.

---

## Çakışma Özeti

| Araç | BMad ile ilişki | Karar |
|------|----------------|-------|
| BMad (67 skill) | Ana framework | ✅ Tüm workflow'lar |
| PM Plugin | Kısmi — 2 unique | ✅ `/stakeholder-update` + `/metrics-review` |
| Feature-Memory | Çakışma yok | ✅ Paralel hafıza |
| Skill Creator | Minör örtüşme | ✅ Yeni skill meta-işleri |
| ~~rsmdt-the-startup~~ | %100 örtüşme | 🗑️ Silindi |
| YouTube skill | İlgisiz | — |

---

## İşaret Açıklaması

| İşaret | Anlam |
|--------|-------|
| ⭐ | Bu proje için direkt alakalı — kullanılacak |
| ○ | Zaman zaman işe yarar |
| — | Bu proje için ilgisiz / meta araç |
