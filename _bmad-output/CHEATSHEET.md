# fitnessApp BMad Cheat Sheet

## Günlük Kullanım
- `bmad-help` — daimi başlanğıc nöqtəsi, hər stuck olduqda çağır
- `bmad-help <free-text question>` — context-aware sual

## Phase 1: Analiz (hazırda burdayam)
- `bmad-technical-research` — KMM/Supabase/GCP AI validation
- `bmad-market-research` — Azerbaijani fitness market
- `bmad-brainstorming` — feature ideation (target 100+)
- `bmad-product-brief` — formal brief yarat
- `bmad-prfaq` — Working Backwards stress-test

## Phase 2: Planlama
- `bmad-agent-pm` → CP — Create PRD
- `bmad-agent-ux-designer` → CU — Create UX Design

## Phase 3: Solutioning
- `bmad-agent-architect` → CA — Create Architecture
- `bmad-agent-pm` → EP — Create Epics & Stories
- `bmad-agent-architect` → IR — Implementation Readiness

## Phase 4: İmplementasyon (story başına)
- `bmad-agent-dev` → SP — Sprint Planning (bir kez)
- `bmad-agent-dev` → CS — Create Story
- `bmad-agent-dev` → DS — Dev Story
- `bmad-agent-dev` → CR — Code Review

## Core Tools (her zaman, her fazda)
- `bmad-review-adversarial-general` — cynical review (10+ issue garantili)
- `bmad-review-edge-case-hunter` — path-tracing edge cases
- `bmad-distillator` — uzun dokları sıkıştır (3:1 oran)
- `bmad-advanced-elicitation` — output'u daha derin it
- `bmad-party-mode` — multi-agent diskussiya
- `bmad-investigate` — forensic araştırma (bug/incident)

## Kurallar
1. HƏMIŞƏ fresh chat per workflow
2. Hər phase-də adversarial-general işlət (single-dev sanity check)
3. project-context.md-ni yenilə hər major decision-dan sonra
4. Workflow-un sonunda bmad-help avtomatik növbəti addımı söyləyəcək

## Önemli Dosyalar
- `_bmad-output/project-context.md` — constitution, hər major karardan sonra güncelle
- `_bmad/custom/config.toml` — team-level BMad override'ları
- `_bmad/custom/bmad-agent-*.toml` — agent-specific persistent facts
