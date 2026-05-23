# BMad Method — Tam Cheatsheet (fitnessApp)

> **Dilimiz:** Türkçe/Azərbaycanca qarışıq — texniki terminlər İngiliscə.
> Son yenilənmə: 2026-05-16

---

## 1. BMad Nədir?

BMad Method (BMM) — AI agentlərini **kontekst mühəndisliyi** prinsipləri üzrə idarə edən bir framework-dür.
Hər faza bir əvvəlki fazanın output-unu context olaraq alır, beləliklə agentlər "nə qurmaq lazımdır və niyə"ni həmişə bilirlər.

```
Phase 1: Analysis → Phase 2: Planning → Phase 3: Solutioning → Phase 4: Implementation
```

**Altın qayda:** Stuck olduqda **həmişə** `bmad-help` çağır — vəziyyəti skan edib növbəti addımı söyləyir.

---

## 2. BMad-ın Əsas Konseptləri

### 2a. Skill vs Agent Trigger

| Mexanizm | Necə çağırılır | Nə olur |
|---|---|---|
| **Skill** | `bmad-create-prd` yazırsan | Birbaşa workflow başlayır |
| **Agent Trigger** | Agenti yüklə (`bmad-agent-pm`), sonra `CP` yaz | Agent öz menüsündən seçimi işləyir |

**Qısa qayda:** Hansı workflow istədiyini bilirsən → skill. Agent ilə artıq söhbət edirsən → trigger.

### 2b. Fresh Chat Qaydası

**HƏMIŞƏ** yeni workflow üçün yeni chat aç. Köhnə chat-ın context-i agent qərarlarını korlaşdırır.
BMad bunu "context overflow risk" adlandırır — solo dev üçün ən böyük tuzaq.

### 2c. project-context.md — Konstitusiya

`docs/project-context.md` faylı BMad agentlərinin **avtomatik yüklədiyi** əsas fayldır.
Buraya: tech stack, coding standards, domain rules, nə etmə qadağaları daxildir.
Hər böyük arxitektura qərarından sonra bu faylı güncəllə.

---

## 3. Bütün Skills Kataloqu

### 3a. Daimi Başlanğıc Nöqtəsi

| Skill | Nə edir | Nə vaxt işlət |
|---|---|---|
| `bmad-help` | Proyekti skan edib növbəti addımı tövsiyə edir | Stuck olduqda, phase bitdikdə, yeni modül qurulduqda |
| `bmad-help <sual>` | Natural language ilə sual — context-aware cavab | "Where do I start?", "What are my options?" |

---

### 3b. Phase 1 — Analysis (İdeyadan Validasiyaya)

> Məqsəd: Problem space-i anla, ideyanı stress-test et.

| Skill | Agent | Trigger | Nə edir | Output |
|---|---|---|---|---|
| `bmad-brainstorming` | — | — | Strukturlu yaradıcı session, 100+ ideyaya qədər aparır | `brainstorming-report.md` |
| `bmad-domain-research` | Mary (Analyst) | `DR` | Sənaye/domain araşdırması | Research findings |
| `bmad-market-research` | Mary (Analyst) | `MR` | Bazar + rəqib analizi | Research findings |
| `bmad-technical-research` | Mary (Analyst) | `TR` | Texnologiya stack validation | Research findings |
| `bmad-product-brief` | Mary (Analyst) | `BP` | Strateji vision capture (ideyaların aydın olduğu hal) | `product-brief.md` |
| `bmad-prfaq` | Mary (Analyst) | `WB` | Working Backwards — ideyanı məhv etməyə çalış, sağ qalanlar doğrudur | `prfaq-{project}.md` |

**fitnessApp-da nə işlətdik:**
- `bmad-market-research` → BetterMe/NTC/Freeletics analizi (BetterMe AZ dəstəkləyir tapıntısı!)
- `bmad-domain-research` → AZ fitness bazarı, pricing, retention data
- `bmad-technical-research` → KMM+Supabase feasibility (tövsiyə olunan növbəti addım)

---

### 3c. Phase 2 — Planning (Nə quracağıq?)

> Məqsəd: Tələbləri və UX-i sənəd şəklində müəyyən et.

| Skill | Agent | Trigger | Nə edir | Output |
|---|---|---|---|---|
| `bmad-create-prd` | John (PM) | `CP` | Product Requirements Document — FRs, NFRs, success metrics | `PRD.md` |
| `bmad-edit-prd` | John (PM) | `EP` (edit) | Mövcud PRD-i yenilə/genişləndir | Updated `PRD.md` |
| `bmad-validate-prd` | John (PM) | `VP` | PRD-i standarta görə yoxla | PASS/FAIL report |
| `bmad-create-ux-design` | Sally (UX) | `CU` | UX pattern-lər + dizayn spec | `ux-spec.md` |

---

### 3d. Phase 3 — Solutioning (Necə quracağıq?)

> Məqsəd: Texniki qərarları açıq et, işi story-lərə böl.

| Skill | Agent | Trigger | Nə edir | Output |
|---|---|---|---|---|
| `bmad-create-architecture` | Winston (Architect) | `CA` | Sistem arxitekturası + ADR-lər (Architecture Decision Records) | `architecture.md` |
| `bmad-create-epics-and-stories` | John (PM) | `CE` | Epic-ləri story-lərə parçala | Epic files + stories |
| `bmad-check-implementation-readiness` | Winston/John | `IR` | PRD+UX+Arch tam hazırdır? Gate check | PASS/CONCERNS/FAIL |

---

### 3e. Phase 4 — Implementation (Qurmaq!)

> Məqsəd: Bir story başına tam cycle: plan → dev → review.

| Skill | Agent | Trigger | Nə edir | Output |
|---|---|---|---|---|
| `bmad-sprint-planning` | Amelia (Dev) | `SP` | Sprint tracking-i başlat (proyekt başına 1 dəfə) | `sprint-status.yaml` |
| `bmad-create-story` | Amelia (Dev) | `CS` | Növbəti story-ni implement üçün hazırla | `story-[slug].md` |
| `bmad-dev-story` | Amelia (Dev) | `DS` | Story-ni implement et (Amelia persona, spec-ə sadiq qalar) | Working code + tests |
| `bmad-code-review` | Amelia (Dev) | `CR` | Implementation keyfiyyətini validate et | Approved / Changes requested |
| `bmad-qa-generate-e2e-tests` | Amelia (Dev) | `QA` | E2E + API testlər generate et (epic tamamlandıqdan sonra) | Test files |
| `bmad-correct-course` | John (PM) | `CC` | Sprint içi böyük dəyişiklik idarəsi | Updated plan |
| `bmad-sprint-status` | — | — | Sprint progress göstər | Status update |
| `bmad-retrospective` | Amelia (Dev) | `ER` | Epic bittikdən sonra dərsləri çıxar | Lessons learned |

---

### 3f. Quick Flow (Phase 1-3 atlayanda)

| Skill | Nə edir | Nə vaxt işlət |
|---|---|---|
| `bmad-quick-dev` | Kiçik, aydın iş üçün: clarify → plan → implement → review → present | Scope məlum, büyük plan lazım deyil |

---

### 3g. Core Tools (Hər Fazada İşlədilə Bilən)

Bunlar heç bir agent session tələb etmir — birbaşa çağır.

| Skill | Tip | Nə edir | Nə vaxt işlət |
|---|---|---|---|
| `bmad-review-adversarial-general` | Task | Simmetrik skeptik review — 10+ problem tapması məcburidir | Hər deliverable-ı finalize etməzdən əvvəl |
| `bmad-review-edge-case-hunter` | Task | Bütün branching path-ları mexaniki skan — yalnız unhandled case-ləri report edir | Adversarial review-ə əlavə olaraq, mütləq hər ikisini çalışdır |
| `bmad-advanced-elicitation` | Task | LLM output-unu Socratic / first-principles / pre-mortem / red-team ilə dərinləşdir | Çıxış sığ/generic hiss etdirəndə |
| `bmad-party-mode` | Workflow | Bütün agentlər eyni anda konuşur, çoxlu perspektiv | Böyük qərar, birinin fikri ilə qalmaq istəmədikdə |
| `bmad-brainstorming` | Workflow | Strukturlu ideation, 100+ ideyaya qədər | Yeni feature, problem space exploration |
| `bmad-distillator` | Task | Böyük doc-u 3:1 nisbətilə LLM-optimized sıxışdırır | Doc context window-a sığmayanda, token qənaəti |
| `bmad-editorial-review-prose` | Task | Copy-editing — ifadə aydınlığı | Sənədi polish etmək istəyəndə |
| `bmad-editorial-review-structure` | Task | Structural edit — cut/merge/move önerilər | Çoxlu subprocessdən çıxmış sənəd incoherent görünəndə |
| `bmad-shard-doc` | Task | Böyük markdown-ı ## header-lara görə parçala | 500+ sətir doc, LLM context idarəsi |
| `bmad-index-docs` | Task | Qovluqdakı bütün doc-lar üçün index.md yarat | Docs qovluğu böyüdükdə |

---

### 3h. Agent Skills (Persona Yüklə → Trigger İşlət)

| Skill | Persona | Nə edir |
|---|---|---|
| `bmad-agent-analyst` | Mary | Araşdırma, ideation, brief, PRFAQ |
| `bmad-agent-pm` | John | PRD, epics, stories, implementation readiness |
| `bmad-agent-architect` | Winston | Arxitektura, ADR-lər, tech qərarlar |
| `bmad-agent-dev` | Amelia | Story implement, code review, test generation, sprint |
| `bmad-agent-ux-designer` | Sally | UX pattern-lər, dizayn spec |
| `bmad-agent-tech-writer` | Paige | Docs yaz, standartları yenilə, diagramlar, konsept izah |

---

### 3i. CIS Module — Kreativ İntelligence Suite

> Qurulmuş modul — brainstorming, storytelling, innovation üçün xüsusi agentlər.

| Skill | Persona | Nə edir |
|---|---|---|
| `bmad-cis-agent-brainstorming-coach` | Carson | Elite brainstorming facilitation |
| `bmad-cis-agent-design-thinking-coach` | Maya | Human-centered design processes |
| `bmad-cis-agent-innovation-strategist` | Victor | Disruption opportunities, business model innovation |
| `bmad-cis-agent-creative-problem-solver` | Dr. Quinn | Systematic problem-solving methodologies |
| `bmad-cis-agent-storyteller` | Sophia | Compelling narratives, story frameworks |
| `bmad-cis-agent-presentation-master` | Caravaggio | Slide decks, pitch decks, visual storytelling |
| `bmad-cis-innovation-strategy` | — | Disruption + business model innovation workflow |
| `bmad-cis-design-thinking` | — | Human-centered design process workflow |
| `bmad-cis-problem-solving` | — | Structured problem solving |
| `bmad-cis-storytelling` | — | Narrative workflow |

---

### 3j. TEA Module — Test Architect

> Solo dev üçün: built-in QA kifayətdir. TEA enterprise/compliance üçündür.

| Skill | Nə edir |
|---|---|
| `bmad-tea` | Murat (Test Architect) agentini yüklə |
| `bmad-testarch-test-design` | System/epic level test plan |
| `bmad-testarch-atdd` | Acceptance-test-driven development scaffold |
| `bmad-testarch-automate` | Test automation coverage genişləndir |
| `bmad-testarch-test-review` | Test keyfiyyəti validate et |
| `bmad-testarch-trace` | Traceability matrix — requirement ↔ test xəritəsi |
| `bmad-testarch-nfr` | NFR assessment (performance, security, reliability) |
| `bmad-testarch-ci` | CI/CD quality pipeline scaffold |
| `bmad-testarch-framework` | Test framework initialize (Playwright/Cypress) |

---

### 3k. BMad Builder Module (BMB)

> BMad-ı özü üçün genişləndir — custom agent, workflow, modul yarat.

| Skill | Nə edir |
|---|---|
| `bmad-agent-builder` | Yeni agent yarat / mövcudu analiz et / redaktə et |
| `bmad-workflow-builder` | Yeni workflow / skill yarat, analiz et |
| `bmad-module-builder` | BMad modulu planla, yarat, validate et |

---

### 3l. Feature Memory Skills (Bu Proyektə Xas)

> `.claude/features/` sisteminin slash command-ləri.

| Komanda | Nə edir |
|---|---|
| `/feature-start <name>` | Yeni feature/bug/research başlat, cross-session memory yarat |
| `/feature-status` | Aktiv feature-nin progress bar + qərar/tapıntı xülasəsi |
| `/feature-decision <text>` | Feature.md Decisions table-ına əlavə et |
| `/feature-finding <text>` | Feature.md Findings bölməsinə əlavə et |
| `/feature-note [tag] <text>` | Notes.md-ə bir sətir working memory yaz |
| `/feature-compact` | Notes.md-i dedupe + arxivlə (dry-run ilk, sonra "apply") |
| `/feature-pause` | Aktiv pointer-i sil (fayllar qalır) |
| `/feature-resume <name>` | Durdurulmuş feature-ni bərpa et |
| `/feature-list` | Bütün feature-ləri listə (active/paused/archived) |
| `/feature-end` | Code review → pattern promotion → arxiv → pointer sil |

---

### 3m. Digər Proyekt Skills

| Skill | Nə edir |
|---|---|
| `bmad-help` | Context-aware guide, həmişə başlanğıc nöqtəsi |
| `bmad-customize` | Agent/workflow override fayllarını guided şəkildə yaz |
| `bmad-generate-project-context` | Mövcud koddan `docs/project-context.md` generate et |
| `bmad-document-project` | Brownfield layihəni AI konteksti üçün sənədləndir |
| `bmad-prfaq` | Working Backwards PRFAQ challenge |
| `bmad-product-brief` | Product brief yarat |
| `bmad-checkpoint-preview` | Human-in-the-loop review — dəyişikliyi izah et, testə yönləndir |
| `bmad-distillator` | Sənədi lossless LLM sıxışdır (3:1) |

---

## 4. Config Sistemi — Hər Fayl Niyə Orada?

### 4a. Config Fayllarının Xəritəsi

```
fitnessApp/
├── _bmad/
│   ├── config.toml              ← INSTALLER-MANAGED. READ-ONLY. Toxunma!
│   ├── config.user.toml         ← INSTALLER-MANAGED. READ-ONLY. Toxunma!
│   └── custom/                  ← SƏNİN ƏRAZIN. Bütün override-lar buraya.
│       ├── config.toml          ← Team override (git-ə commit edilir)
│       ├── config.user.toml     ← Şəxsi override (gitignored)
│       ├── bmad-agent-dev.toml      ← Amelia üçün persistent_facts
│       ├── bmad-agent-pm.toml       ← John üçün persistent_facts
│       ├── bmad-agent-architect.toml
│       ├── bmad-agent-analyst.toml
│       └── bmad-agent-ux-designer.toml
└── docs/
    └── project-context.md       ← BMad agentlərinin avtomatik yüklədiyi konstitusiya
```

---

### 4b. Mərkəzi Config — 4 Qat Merge

**Prioritet (aşağısı qazanır):**

```
1 (ən aşağı): _bmad/config.toml              ← installer base (READ-ONLY)
2:             _bmad/config.user.toml         ← installer user answers (READ-ONLY)
3:             _bmad/custom/config.toml       ← team overrides (BU FAYILI YAZI)
4 (qazanır):   _bmad/custom/config.user.toml ← personal overrides (gitignored)
```

**Bu proyektdə nə var:**
```toml
# _bmad/custom/config.toml
[modules.bmm]
user_skill_level = "expert"   # 6-7 il təcrübə, terse texniki cavablar
```

---

### 4c. Per-Agent Override — 3 Qat Merge

Hər agent üçün ayrıca override faylı var:

```
1 (ən aşağı): .claude/skills/bmad-agent-dev/customize.toml   ← agent defaults (READ-ONLY)
2:             _bmad/custom/bmad-agent-dev.toml               ← team override (BU FAYILI YAZI)
3 (qazanır):   _bmad/custom/bmad-agent-dev.user.toml         ← personal (gitignored)
```

**Bu proyektdə Amelia-ya (Dev) əlavə persistent_facts:**
```toml
# _bmad/custom/bmad-agent-dev.toml
[agent]
persistent_facts = [
  "Every code change must include corresponding tests.",
  "For Supabase queries, always verify RLS policy compliance.",
  "KMM shared module changes require both iOS and Android verification.",
  "Cost-sensitive operations (Google AI API) must include cost estimation comment.",
  "Use Context7 MCP for library docs (KMM, Compose, SwiftUI, Supabase).",
]
```

---

### 4d. Merge Qaydaları (Shape-Based)

| Dəyər tipi | Nə olur |
|---|---|
| Scalar (string, int, bool) | Override qazanır — base silinir |
| Table `[section]` | Deep merge — hər sahə ayrıca merge olunur |
| Array-of-tables, eyni `code` və ya `id` key | Matching key-lər in-place replace, yeni key-lər append |
| Digər array-lər (scalar, mixed) | **Append** — base əvvəl, sonra team, sonra user |

**Vacib:** `persistent_facts` → append-only (sənin faktların default-lara əlavə olunur, silmir).

---

### 4e. Nəyi Nə Zaman Dəyiş

| Ehtiyac | Hara yaz |
|---|---|
| `user_skill_level`, `planning_artifacts` kimi install settings | `_bmad/custom/config.toml` [modules.bmm] |
| Agentin həmişə yadında saxlaması lazım olan layihə faktları | `_bmad/custom/bmad-agent-{role}.toml` [agent] persistent_facts |
| Agentin menüsünə custom əməliyyat əlavə etmək | `_bmad/custom/bmad-agent-{role}.toml` [[agent.menu]] |
| Party-mode üçün agent descriptor dəyiş | `_bmad/custom/config.toml` [agents.bmad-agent-pm] |
| Yalnız sən istifadə edəcəksən, git-ə getməsin | `_bmad/custom/config.user.toml` |

---

### 4f. customize.toml — Nədir, Niyə Var?

Hər skill qovluğunda `.claude/skills/bmad-agent-pm/customize.toml` var.
Bu fayl agent-in **tam customization surface-ni** göstərir — hansı sahələrin dəyişdirilə biləcəyini görürsən.
**HEÇVAXT** bu faylı redaktə etmə — installer yenilədikdə üzərinə yazılır.
Sadəcə hansı sahələrin mövcud olduğunu öyrənmək üçün oxu.

**Nə Etmə Yanlışlığı:**
```toml
# YANLIŞ — customize.toml-un tam kopyasını override faylına yapışdırma!
# Hər yeniləmədə köhnə default-larla qalacaqsan, driftə düşəcəksin.

# DOĞRU — yalnız dəyişdirdiyin sahələri yaz:
[agent]
persistent_facts = ["Yeni fakt 1.", "Yeni fakt 2."]
```

---

## 5. Günlük Workflow Qaydaları

### 5a. Standard Günlük Sıra

```
1. Yeni chat aç
2. Hansı phase-dəsən? → bmad-help
3. Seçilmiş workflow-u skill ilə çalışdır
4. Workflow bitdikdə → adversarial-general review işlət
5. Əhəmiyyətli qərar? → feature-decision qeyd et
6. Phase bitti? → docs/project-context.md güncəllə
7. Session bitti? → Stop hook soru verir, y/n/selective cavabla
```

### 5b. Hər Phase-in "Done" Meyarı

| Phase | Hazır sayılır |
|---|---|
| Phase 1 | Research findings + product-brief.md + PRFAQ tamamlandı |
| Phase 2 | PRD validate keçdi (bmad-validate-prd PASS) |
| Phase 3 | Architecture + Epics + Implementation Readiness PASS |
| Phase 4 | Hər story: Dev Story → Code Review APPROVED → QA tests green |

### 5c. Adversarial Review Protokolu

**Solo dev üçün kritik** — human reviewer yoxdur, adversarial-general onu kompensasiya edir.

```
Hər deliverable (PRD, arch doc, UX spec, story) finalize-dan əvvəl:
1. bmad-review-adversarial-general → 10+ tapıntı (məcburi)
2. bmad-review-edge-case-hunter → unhandled path-lar
Hər ikisi bitdikdən sonra finalize et.
```

---

## 6. fitnessApp Üçün Növbəti Addımlar

**Hal-hazırki status:** Phase 1 tamamlanmaq üzrə

**Tövsiyə olunan sıra:**
```
1. bmad-technical-research  → KMM+Supabase+GCP AI stack validation
2. bmad-product-brief       → Rəsmi brief yarat (araşdırma bazasında)
3. bmad-prfaq               → "Working Backwards" stress-test
4. [Phase 2]  bmad-create-prd    → PRD (John/PM)
5. [Phase 2]  bmad-create-ux-design → UX spec (Sally/UX)
6. [Phase 3]  bmad-create-architecture → Arxitektura (Winston)
7. [Phase 3]  bmad-create-epics-and-stories → Epic + Story-lər
8. [Phase 3]  bmad-check-implementation-readiness → Gate check
9. [Phase 4]  Story başına: sprint-planning → create-story → dev-story → code-review
```

---

## 7. Tez Referans — Skill Qısa Kodları

```
bmad-help             → Haradadam? Nə etməliyəm?
bmad-agent-pm         → John yüklə, sonra: CP=PRD | VP=validate | CE=epics | IR=readiness
bmad-agent-architect  → Winston yüklə, sonra: CA=architecture | IR=readiness
bmad-agent-dev        → Amelia yüklə, sonra: DS=dev story | CS=create story | CR=review | QA=tests | SP=sprint
bmad-agent-analyst    → Mary yüklə, sonra: MR=market | TR=technical | DR=domain | BP=brief | WB=prfaq
bmad-agent-ux-designer → Sally yüklə, sonra: CU=UX design
bmad-review-adversarial-general  → 10+ problem tap (hər deliverable-da işlət)
bmad-review-edge-case-hunter     → Unhandled path-lar
bmad-advanced-elicitation        → Output-u dərinləşdir (Socratic/first-principles)
bmad-party-mode                  → Bütün agentlər eyni anda
bmad-distillator                 → Doc-u 3:1 sıxışdır
bmad-brainstorming               → 100+ ideyaya qədər strukturlu ideation
bmad-quick-dev                   → Kiçik iş, plan lazım yox
```

---

## 8. Project-Context.md Yaratma — Tam Bələdçi

> **Məqsəd:** Bütün startup fikrinin minimal söz sayı ilə, maksimal kontekst daşıyan, BMad agentlərinin avtomatik yüklədiyi `docs/project-context.md`-ni yaratmaq.

### 8a. Niyə project-context.md Vacibdir?

`docs/project-context.md` BMad agentlərinin (Amelia, Winston, John, Sally) **hər session başında avtomatik yüklədiyi** yeganə fayldır. Bu fayl olmadan:
- Amelia code yazanda arxitektura qaydalarını bilmir
- Winston arxitektura dizayn edəndə MVP scope-dan xəbərsizdir
- John PRD yazanda constraint-ləri unudur

Əgər bu fayl **çox uzundursa** → agent token büdcəsi azalır, context window tükənir.
Əgər **çox qısadırsa** → kritik qərarlar agentlərə çatmır.

**Hədəf:** ~400-600 sətir — hər məlumat cümləsi öz çəkisini daşımalıdır.

---

### 8b. Hazır Vəziyyət (fitnessApp üçün)

Mövcud `docs/project-context.md`: 272 sətir — artıq var, lakin sıxılmamış və bəzi bölmələr verbose.

Mövcud faylın güclü tərəfləri:
- ✅ Tech stack tam var
- ✅ Hard constraints (15 qayda) ətraflı
- ✅ Data model SQL-də
- ✅ Competitive context yenilənib (BetterMe AZ tapıntısı)
- ✅ Monetizasiya + retention benchmarkları

Zəif tərəfləri:
- ❌ KMM+Supabase texniki feasibility hələ yoxdur
- ❌ Bəzi bölmələr agent üçün lazım olmayan prose ilə şişib
- ❌ `bmad-generate-project-context`-in istifadə etdiyi standart format deyil

---

### 8c. Tövsiyə Olunan Workflow (Sıra İlə)

```
Addım 1: bmad-technical-research    → KMM+Supabase+GCP feasibility doc
Addım 2: bmad-distillator           → Mövcud docs-ları sıxışdır
Addım 3: bmad-generate-project-context  → Agent-optimized format
Addım 4: bmad-editorial-review-structure → Structural cuts
Addım 5: bmad-review-adversarial-general → Kritik məlumat itib?
Addım 6: Manual final pass           → Sənin gözlə yoxla
```

---

### 8d. Hər Addımın Detallı İzahı

#### Addım 1: `bmad-technical-research`
**Niyə əvvəl?** Tech research olmadan project-context-dəki "tentative" damğaları qalar.
KMM+Supabase real-world integration feasibility, GCP AI cost per video gen, RevenueCat KMM SDK mövcudluğu.

```
Yeni chat aç → bmad-technical-research
Research topic: "KMM + Supabase + RevenueCat + GCP Cloud AI — fitnessApp MVP feasibility"
Output: _bmad-output/tech-research-kmm-supabase.md
```

#### Addım 2: `bmad-distillator`
**Niyə?** Mövcud docs-ları (project-context.md + CLAUDE.md + feature.md decisions) 3:1 nisbəti ilə sıxışdırır. Məlumat itirilmir, amma prose azalır.

```
Yeni chat aç → bmad-distillator
source_documents: docs/project-context.md, CLAUDE.md
downstream_consumer: "BMad agent context — project-context.md generation"
token_budget: ~300 lines
--validate flag: isteğe bağlı (round-trip test)
Output: distillate faylı → sonra generate-project-context-ə input kimi istifadə
```

#### Addım 3: `bmad-generate-project-context`
**Niyə?** Bu skill xüsusi olaraq `docs/project-context.md` üçün dizayn edilib. Mövcud codebase + artifact-ları oxuyur, BMad agentlərinin istifadə etdiyi **standart format**-da output verir.

```
Yeni chat aç → bmad-generate-project-context
(Skill özü codebase + docs-ları skan edər)
Input üçün hazır tut: distillate + tech-research output
Output: docs/project-context.md (yerini dəyişdirir və ya yeni versiya)
```

**Bu skill nə edir:**
- Bütün `_bmad-output/` artifact-larını oxuyur
- CLAUDE.md-i oxuyur
- Existing project-context.md-i oxuyur
- Standart BMad section formatında yeni versiyanı yazar
- Agent-a lazım olmayan prose-ları kəsər

#### Addım 4: `bmad-editorial-review-structure`
**Niyə?** generate-project-context hər zaman optimal output vermir. Structure review: CUT/MERGE/CONDENSE öneriri, word count azaldır.

```
Eyni chat (generate bitdikdən sonra) → bmad-editorial-review-structure
content: yeni docs/project-context.md
purpose: "BMad agent context file — AI agent will read this every session"
reader_type: llm
length_target: "30% shorter"
Output: prioritized recommendation list + estimated reduction %
```

Önerilən CUT/MERGE-ları qəbul et → manual edit et.

#### Addım 5: `bmad-review-adversarial-general`
**Niyə?** Sıxışdırma prosesində kritik constraint, qadağa, qərar itmiş ola bilər. Adversarial gözü bunu tapır.

```
Eyni chat → bmad-review-adversarial-general
content: final docs/project-context.md
also_consider: "This file is loaded by AI agents — missing constraints = agent will ignore them"
Output: 10+ tapıntı → hər birini manual bax, apply et ya etmə
```

#### Addım 6: Manual Final Pass
Hər addımdan sonra gözlə bir bax:
- Bütün 15 Hard Constraint varmı?
- P0 feature siyahısı tamammı?
- Faza 2 qadağaları açıq yazılıbmı?
- Solo dev constraint-ləri (20h/həftə, IBAM) varmı?
- Competitive context yenidirmi (BetterMe AZ tapıntısı)?

---

### 8e. Alternativ: Tez Yol (Araşdırma Etmədən)

Əgər texniki research lazım deyil (stack artıq müəyyəndirsə) — Addım 1-i atla:

```
Addım 1: bmad-distillator          → Mövcud docs sıxışdır
Addım 2: bmad-generate-project-context → Agent-optimized format
Addım 3: bmad-editorial-review-structure → Structural cuts
Addım 4: bmad-review-adversarial-general → Kritik məlumat yoxlaması
```

---

### 8f. project-context.md-nin Optimal Strukturu

Agentlər üçün ən yaxşı format (section sırası vacibdir):

```markdown
## Business Overview        ← 5-8 sətir, problem + həll + differensiator
## Tech Stack               ← Cədvəl, status (tentative/confirmed)
## Hard Constraints         ← Nömrəli siyahı, hər qayda 1 sətir
## Architecture Patterns    ← KMM structure, Supabase patterns, offline strategy
## Data Model               ← SQL schema (minimal, column-level)
## MVP Scope                ← P0 / P1 / Faza-2 bullet siyahıları
## Competitive Context      ← Cədvəl + differensiator sıralaması
## Monetization             ← Qiymət + retention benchmark-ları
## Out-of-Scope             ← Agentlərə "BU BÖLMƏ ÜÇÜN TƏKLİF ETMƏ" — kritik
## Solo Developer Constraints ← 20h/həftə, IBAM, agent rolları
## Communication Preferences ← Dil, cavab tonu
```

**Hər bölmənin max uzunluğu:**
- Business Overview: 8 sətir
- Hard Constraints: 15 item, hər biri ≤2 sətir
- Architecture Patterns: 15-20 sətir
- Data Model: SQL schema, minimal
- MVP Scope: bullet, prose yox
- Competitive Context: cədvəl (≤8 satır)
- Out-of-Scope: bullet, 6-8 item

---

### 8g. project-context.md vs CLAUDE.md — Fərq Nədir?

| | `docs/project-context.md` | `CLAUDE.md` |
|---|---|---|
| **Kim oxuyur** | BMad agentləri (Amelia, Winston, John, Sally) | Claude Code özü (hər session) |
| **Format** | BMad agent-optimized | Claude Code instructions |
| **İçerik** | Proje konteksti (nə quruluq, niyə, necə) | Davranış qaydaları, workflow qaydaları |
| **Uzunluq** | ~400-600 sətir (agent context budget) | Uzuna ola bilər (Claude Code oxuyur) |
| **Kimi yazır** | `bmad-generate-project-context` | Manual + Claude Code |

**Qayda:** Eyni məlumatı hər iki faylda saxlama — CLAUDE.md agent-specific rules üçün, project-context.md domain facts üçün.

---

## 9. Multi-Agent Project-Context Workflow (TƏFSILATLI)

> **Niyə multi-agent?** Single-shot `bmad-generate-project-context` 4 ayrı domain-i (analyst/PM/architect/UX) bir agent perspective ilə qarışdırır. Müxtəlif specialist agentlər öz section-larını ayrıca yazıb sonra synthesis daha güclü nəticə verir.

### 9a. Section Ownership Matrix

Hər project-context.md section-ı specifik agent-ə həvalə edilir:

| # | Section | Owner Agent | Niyə |
|---|---------|-------------|------|
| 1 | Vision & Problem | **Mary (Analyst)** | Market truth, problem framing |
| 2 | Current Status & Roadmap | **John (PM)** + Paige | Sprint goal, milestone discipline |
| 3 | Success Metrics (per Phase) | **John (PM)** | KPI bar definition |
| 4 | Full Feature Catalog | **John + Mary** | Scope + competitive gap merge |
| 5 | Functional Decomposition | **Winston + Sally** | Module structure (Winston) + UX module map (Sally) |
| 6 | Tech Stack | **Winston (Architect)** | Tooling feasibility |
| 7 | Hard Constraints | **Winston** | Non-negotiable rules (RLS, signed URL, $300 cap) |
| 8 | Architecture & Data Model | **Winston** | Schema + KMM + Supabase + AI pipeline |
| 9 | Out-of-Scope | **Paige (Tech Writer)** | Editorial firewall |
| 10 | Monetization | **John** | Pricing + paywall pattern |
| 11 | Competitive Position | **Mary** | Rival truth + differentiator |
| 12 | Solo Dev & Comms | **Paige** | Working mode |

---

### 9b. 5-Wave Workflow Plan

Hər wave ya **PARALLEL** (eyni anda fresh chat-larda) və ya **SEQUENTIAL** (sırada).

```
Wave A: Input Gap Closure       [SEQUENTIAL, ~30 dəq]   1 skill
Wave B: Specialist Perspectives [PARALLEL, ~45 dəq hər] 4 skill paralel
Wave C: Synthesis                [SEQUENTIAL, ~1 saat]  2 skill
Wave D: Quality Gates            [PARALLEL, ~45 dəq]    3 skill paralel
Wave E: Polish & Lock            [SEQUENTIAL, ~30 dəq]  3 addım
```

**Toplam wall time:** ~3.5 saat (bir focused evening)

---

### 9c. Wave A — Input Gap Closure

**Skill:** `bmad-technical-research`
**Niyə:** Mövcud project-context.md-də yalnız 1 real boşluq var — KMM+Supabase production feasibility. Onsuz Winston perspective sentir.

```
Yeni chat → bmad-technical-research
Topic: "KMM 1.9+ production iOS/Android with Supabase Kotlin SDK + Ktor;
        Edge Functions cost/latency for AI proxy;
        $300 GCP credit lifetime on Veo/Kling for 50 exercise videos."
Output: docs/tech-feasibility.md
```

---

### 9d. Wave B — 4 PARALLEL Specialist Perspectives

**Hər biri AYRI fresh chat. Eyni anda 4 terminal aç və ya bir-bir et amma context-ləri qarışdırma.**

Hər perspective ~150 sətirlik delta md çıxarır — final synthesis-ə input olur.

#### Mary (Analyst) — Vision/Competitive/Differentiators
```
Yeni chat → bmad-agent-analyst (sonra menüdən seç və ya birbaşa prompt:)
"Read docs/project-context.md and CLAUDE.md.
Critique Business Overview + Competitive Context + Differentiator Ranking sections.
Propose 2-3 NEW feature ideas grounded in BetterMe/NTC/Diyetkolik gaps.
Specifically: what local-content moats does BetterMe AZ MT not cover?
Output: _bmad-output/perspective-analyst.md, max 150 lines."
```

#### John (PM) — MVP/Catalog/Monetization/Metrics
```
Yeni chat → bmad-agent-pm
"Read docs/project-context.md.
Audit MVP P0/P1/Faza-2 for 4-month solo dev feasibility (20h/week).
Validate 8/60/199 AZN pricing vs retention benchmarks (D30 17% monthly, 44.1% annual).
Propose monetization additions (referral? family plan?).
Define Alpha (10 testers) / Beta (100 users) / Launch (1K MAU) phase metric targets.
Build Full Feature Catalog: every P0 + P1 + Faza-2 + Idea-Backlog feature tagged.
Output: _bmad-output/perspective-pm.md, max 150 lines."
```

#### Winston (Architect) — Stack/Constraints/Architecture/Data
```
Yeni chat → bmad-agent-architect
"Input: docs/project-context.md + docs/tech-feasibility.md.
Harden: Tech Stack table (remove tentative damgaları), Hard Constraints (15 rule),
Architecture Patterns (KMM structure, Supabase patterns), Data Model (single canonical SQL schema),
AI Hybrid Pipeline (50 manual → 50+ AI gradual), Offline Strategy (SQLDelight sync).
Flag KMM/Supabase risk areas explicitly.
Output: _bmad-output/perspective-architect.md, max 150 lines."
```

#### Sally (UX) — Onboarding/Paywall/Streak/Localization
```
Yeni chat → bmad-agent-ux-designer
"Read docs/project-context.md.
Design: onboarding flow (≤8 sual limit, Apple AI disclosure),
video paywall layout (2.9x conversion lift, dual option 7-day trial + annual),
streak + streak-freeze surface (P0 retention),
AZ localization QA pipeline (manual review every string, MT forbidden).
Map all to activation metric: 4 completed workouts in first 30 days = habit threshold.
Output: _bmad-output/perspective-ux.md, max 150 lines."
```

---

### 9e. Wave C — Synthesis (Sequential)

#### Addım C1: `bmad-distillator`
```
Yeni chat → bmad-distillator
source_documents:
  - _bmad-output/perspective-analyst.md
  - _bmad-output/perspective-pm.md
  - _bmad-output/perspective-architect.md
  - _bmad-output/perspective-ux.md
  - docs/project-context.md (current)
  - docs/tech-feasibility.md
downstream_consumer: "Agent-optimized project-context.md generation"
token_budget: ~500 lines
--validate: true (round-trip lossless check)
Output: _bmad-output/project-context.draft.md
```

#### Addım C2: `bmad-generate-project-context`
```
Same chat → bmad-generate-project-context
Input: project-context.draft.md + section ownership matrix (9a)
"Reformat to canonical agent-optimized structure:
- YAML frontmatter with sections_completed
- Every Hard Constraint enforceable by downstream agents
- Use the 12-section structure from CHEATSHEET 9f"
Output: _bmad-output/project-context.v2.md
```

---

### 9f. Final Section Structure (TARGET: ~156 sətir)

> Mövcud 272 sətirdən 43% azalma + 4 YENİ section.

| # | Section | Lines | Format | Anti-Bloat Qayda |
|---|---------|-------|--------|------------------|
| 1 | `## Vision & Problem` | 6 | 2 qısa para | Tarix yox, "yenidən tərif olundu" meta yox |
| 2 | `## Current Status & Roadmap` 🆕 | 14 | Table: Phase / Window / Goal / Exit Criteria | Daily standup yox, retro yox |
| 3 | `## Success Metrics (per Phase)` 🆕 | 10 | Table: Phase / Metric / Target / Source | "Industry average" yox, vanity metric yox |
| 4 | `## Full Feature Catalog` 🆕 | 28 | Table: Feature / Tier / Module / Status | Description >1 line yox |
| 5 | `## Functional Decomposition` 🆕 | 16 | Bullet, 1 sətir hər biri | File paths yox, class names yox |
| 6 | `## Tech Stack` | 10 | Table: Layer / Tech / Purpose | Version number yox (drift), rationale prose yox |
| 7 | `## Hard Constraints` | 16 | Nömrəli siyahı | "Niyə" izahı yox (link out) |
| 8 | `## Architecture & Data Model` | 26 | Prose + tək SQL bloku | Duplikat schema yox (CLAUDE.md-də olanı sil) |
| 9 | `## Out-of-Scope` | 8 | Bullets | Rationale yox, sadəcə list |
| 10 | `## Monetization` | 8 | Bullets | "Kritik" emoji yox, benchmark prose yox |
| 11 | `## Competitive Position` | 6 | Compressed table (top-3) | Full 6-rival table → `docs/market-research.md` |
| 12 | `## Solo Dev & Comms` | 8 | Bullets, merged | Duplikat scope-creep warning yox |
| **TOPLAM** | | **~156** | | |

---

### 9g. Wave D — Quality Gates (Paralel)

Bunları **eyni anda 3 ayrı chat-da** çalışdırmaq olar.

#### `bmad-review-adversarial-general`
```
Yeni chat → bmad-review-adversarial-general
content: _bmad-output/project-context.v2.md
also_consider:
  - "What critical info dropped from v1 (272 lines)?"
  - "Which constraint can a future agent misinterpret?"
  - "Is every Hard Constraint enforceable without external context?"
Output: 10+ tapıntı
```

#### `bmad-review-edge-case-hunter`
```
Yeni chat → bmad-review-edge-case-hunter
content: project-context.v2.md
also_consider:
  - BetterMe MT marketing pump scenario
  - GCP $300 credit exhaustion mid-MVP
  - KMM Compose Multiplatform interop edge cases
  - Supabase RLS leak paths
  - App Store rejection scenarios (Apple 2024 account deletion, 2025 AI disclosure)
```

#### `bmad-editorial-review-structure`
```
Yeni chat → bmad-editorial-review-structure
content: project-context.v2.md
purpose: "BMad agent constitution — loaded every session"
reader_type: llm
length_target: "156 lines (43% reduction from 272)"
Output: CUT/MERGE/CONDENSE prioritized list
```

---

### 9h. Wave E — Polish & Lock (Sequential)

#### Addım E1: `bmad-editorial-review-prose`
```
Eyni chat → bmad-editorial-review-prose
content: project-context.v2.md (Wave D tövsiyələrini apply etdikdən sonra)
reader_type: llm
style_guide: "Türk/AZ, direct technical, no sugarcoating (per CLAUDE.md Communication)"
```

#### Addım E2: `bmad-checkpoint-preview`
```
Eyni chat → bmad-checkpoint-preview
"Compare v1 (272 lines) vs final (~156 lines).
Show CUT list explicitly. User must approve before write."
```

#### Addım E3: Paige Final Write + Feature Close
```
bmad-agent-tech-writer → final docs/project-context.md
/feature-finding "project-context.md v2 delivered, 156 lines, multi-agent synthesis"
/feature-end  → archive determine-scope-of-start-up
```

---

### 9i. Tezgəlmə Sual: Tək Agent İstifadə Edə Bilərəm?

Bəli, amma tradeoff var:

| Yanaşma | Vaxt | Keyfiyyət | Nə vaxt seç |
|---------|------|-----------|-------------|
| Single-shot `bmad-generate-project-context` | ~45 dəq | Orta — analyst voice dominate edir | Vaxt azdır, mövcud doc artıq yaxşıdır |
| Distillator + Generate + Adversarial (3 addım) | ~1.5 saat | Yaxşı — sıxışdırma + review | Mövcud doc 80% hazırdır |
| **Full 5-wave multi-agent** | **~3.5 saat** | **Ən yaxşı — hər specialist öz tonunda** | **Yeni səviyyəli content lazımdır (4 NEW section)** |

**fitnessApp üçün tövsiyə:** Full 5-wave — çünki user "full app vision" + yeni section-lar (Roadmap, Metrics, Feature Catalog, Decomposition) istəyir, single-shot bunu yığa bilməz.

---

### 9j. Parallelization Real-World Tips

- **4 terminal aç** Wave B üçün (və ya VSCode-da 4 split terminal)
- Hər terminal başında: `cd ~/Desktop/fitnessApp && claude` (fresh session)
- Wave B bitdikdən sonra 4 perspective md-ni bir Finder pəncərəsində göstər
- Wave C-yə girəndə bütün 4 fayl bir chat-a input olur
- Wave D-də yenidən 3 terminal aç — adversarial + edge-case + structural eyni anda
- **Worktree alternativi:** `git worktree add ../fitnessApp-wave-b1 main` — 4 paralel worktree, hər birində fərqli agent

---

### 9k. Müvəffəqiyyət Meyarı

Final `docs/project-context.md` hazır sayılır əgər:
- [ ] ~156 sətir (±10%)
- [ ] 4 YENİ section var (Roadmap, Metrics, Catalog, Decomposition)
- [ ] Hər 15 Hard Constraint var və 1 sətir-də ifadə olunur
- [ ] Single canonical SQL schema (CLAUDE.md-də duplikat silinib)
- [ ] adversarial-general 10+ tapıntısının kritik olanları apply edilib
- [ ] Tarix/meta-history prose yoxdur (present tense)
- [ ] Out-of-Scope açıq və bullet-li
- [ ] Full Feature Catalog hər feature-ı Status (built/spec/idea) ilə işarələyir

---

## 10. Sık Sorulan Suallar

**Q: Agent-lə danışırkən workflow-u necə dəyişim?**
A: Agent yüklüdür — trigger kodu yaz (məs. `DS`). Yeni chat açmağa ehtiyac yoxdur.

**Q: bmad-agent-dev vs bmad-dev-story fərqi?**
A: `bmad-agent-dev` Amelia persona-sını yükləyir, menü açır. `bmad-dev-story` birbaşa workflow-u çalışdırır (agent yüklənmədən). Nəticə eynidir, yol fərqlidir.

**Q: customize.toml-u redaktə edə bilərəm?**
A: Xeyr. O fayl installer-manageddir. `_bmad/custom/` altındakı fayllara yaz.

**Q: persistent_facts nədir?**
A: Agent hər session başında avtomatik context-ə yüklədiyi fakt-lar. "Bizim organizasiya AWS-only" kimi domain sabitlər buraya gedir. `notes.md`-dən fərqi: bu faktlar agentə daimi verilir, sən hər dəfə xatırlatmırsan.

**Q: party-mode nə vaxt istifadə etməli?**
A: Böyük qərar, bir agentin perspektivi ilə qalmaq istəmədikdə. Bütün agentlər paralel düşünür, bir-birilərinə etiraz edir. Solo dev üçün qiymətli.

**Q: bmad-distillator nə vaxt lazımdır?**
A: Market research output-u, PRD, arxitektura sənədi artıq çox böyükdür və agent-ə tam context vermək istəyirsən. 3:1 nisbəti ilə sıxışdırır, məlumat itirilmir.
