# fitnessApp BMad Cheat Sheet

## Günlük Kullanım
- `bmad-help` — daimi başlanğıc nöqtəsi, hər stuck olduqda çağır
- `bmad-help <free-text question>` — context-aware sual

## Phase 1: Analiz (hazırda burdayam)
- `bmad-agent-analyst` (Mary) → araşdırma və ideation
- `bmad-technical-research` — KMM/Supabase/GCP AI validation
- `bmad-market-research` — Azerbaijani fitness market
- `bmad-brainstorming` — feature ideation (target 100+)
- `bmad-product-brief` — formal brief yarat
- `bmad-prfaq` — Working Backwards stress-test

## Phase 2: Planlama
- `bmad-agent-pm` (John) → CP — Create PRD
- `bmad-agent-ux-designer` (Sally) → CU — Create UX Design

## Phase 3: Solutioning
- `bmad-agent-architect` (Winston) → CA — Create Architecture
- `bmad-agent-pm` (John) → EP — Create Epics & Stories
- `bmad-agent-architect` (Winston) → IR — Implementation Readiness

## Phase 4: İmplementasyon (story başına)
- `bmad-agent-dev` (Amelia) → SP — Sprint Planning (bir kez)
- `bmad-agent-dev` (Amelia) → CS — Create Story
- `bmad-agent-dev` (Amelia) → DS — Dev Story
- `bmad-agent-dev` (Amelia) → CR — Code Review

## Core Tools (her zaman, her fazda)
- `bmad-review-adversarial-general` — cynical review (10+ issue garantili)
- `bmad-review-edge-case-hunter` — path-tracing edge cases
- `bmad-distillator` — uzun dökümanları sıkıştır (3:1 oran)
- `bmad-advanced-elicitation` — output'u daha derin it
- `bmad-party-mode` — multi-agent diskussiya
- `bmad-brainstorming` — fikir üretme

## Kurallar
1. HƏMIŞƏ fresh chat per workflow (context overflow önlemek için)
2. Hər phase-də adversarial-general işlət (single-dev sanity check)
3. docs/project-context.md — her major karardan sonra güncelle
4. Workflow sonunda bmad-help bir sonraki adımı söyler

## Önemli Dosyalar
- `docs/project-context.md` — constitution, BMad agentleri otomatik yükler
- `_bmad/custom/config.toml` — team-level BMad config override (user_skill_level=expert)
- `_bmad/custom/bmad-agent-*.toml` — agent-specific persistent facts (5 agent: dev, architect, pm, analyst, ux-designer)

## Config Notu
- `_bmad/config.toml` → installer-managed, READ-ONLY, düzenleme
- `_bmad/custom/config.toml` → bu dosyayı düzenle (team overrides)
- `_bmad/custom/config.user.toml` → kişisel overrides (gitignored)
