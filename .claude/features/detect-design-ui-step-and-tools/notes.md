# Notes: detect-design-ui-step-and-tools
<!--
  Working memory for this feature. Short, high-signal one-liners only.
  FORMAT: YYYY-MM-DD HH:MM [tag] one-line content
  TAGS: [impl] [gotcha] [criteria] [refs] [invariant]
  Compacted: 2026-05-23 — 1 entry archived → notes.archive.md
-->

<!-- STICKY: invariants + criteria (preserved across all compactions) -->
2026-05-16 23:31 [invariant] Pencil-i planning'dən əvvəl açma — ux-spec.md tamamlanmadan canvas açmaq tokens olmadan ekran çəkdirir, 1-2 həftə geri-dönüş riski
2026-05-16 23:31 [invariant] Pencil mockup string-ləri AZ olmalı — uzun Azerice sözlər layout-u qırır, dev'də yox tasarımda yaxalanmalı
2026-05-23 14:00 [invariant] Welcome ekranı doğru render olunur (eyebrow + display title "ÖZ MƏŞQİNİ İDARƏ ET" + Volt CTA) — repositioning v1.1 mövqeyi göründüyü kimi tutulub
2026-05-16 23:31 [criteria] Design tokens tək mənbə = .pen faylı; Compose Theme + SwiftUI Color/Font extension-ları shared/ resource-dan generasiya — Pencil-da dəyişən hər iki platformaya axsın
2026-05-16 23:31 [criteria] Sally-yə project-context-dəki UX qaydaları (≤8 onboarding, paywall 2 seçim + video, streak+freeze, offline-first state'lər, AI disclosure, in-app cancel/delete) hard input kimi verilməli — default-da çıxarmır
2026-05-23 14:00 [criteria] Sual ekranı UX müqaviləsi onboarding axın müqaviləsindən AYRI sənəddə yaşamalı — eyni şablon L2/L3/L5/weekly check-in/settings re-prompt-da istifadə olunur
2026-05-23 14:00 [criteria] Cross-ref chip dili: → (settings reference) · ↔ (cross-section) · ● (single entry) · ■ (terminal, danger color) · ⚠ (safety stop, warning color) · ↻ (re-trigger, Faza 2) · ◐ (variant)
2026-05-23 14:00 [criteria] Refactor "AI-mockup tells": real AZ names (Aysel/Rəşad), off-round numerics (1.847 kkal yox 2.000), 1 anchor display per screen, varied gutter 20/24/28/32, moss yalnız dekorativ

<!-- RECENT: by date -->
2026-05-16 23:31 [refs] Sally output = ux-spec.md (IA + user flows + screen list + state list + design system kararı); pixel çəkmir, yazılı kontrat üretir
2026-05-16 23:31 [refs] Pencil MCP Claude Code-a /mcp ilə bağlanır; agent swarm 6 paralel varyant; design-to-code pixel-perfect Compose/SwiftUI çıxarır
2026-05-16 23:31 [impl] Tövsiyə edilən sıra: bmad-create-prd → bmad-create-ux-design → .pen design system → ekran-ekran frame → bmad-create-architecture → bmad-create-epics-and-stories → bmad-dev-story (Pencil MCP açıq)
2026-05-23 14:00 [impl] .pen rename: app/design/mobile/onboarding_flow.pen → app_design.pen (multi-section fayl, yalnız onboarding deyil)
2026-05-23 14:00 [refs] ux-onboarding-questions-2026-05-23.md (yeni v1.0) — sual ekranı şablonu, variant matrisi, copy üslubu, A11y, analytics, handoff checklist (L1+L2+L3+L5)
2026-05-23 14:00 [refs] ux-auth-onboarding-2026-05-22.md v1.2 (axın-yalnız scope) + ux-design-specification.md v1.5 (`.pen` rename + cross-ref split)
2026-05-23 14:00 [gotcha] Pencil MCP tək-yazandır: 10 paralel agent eyni .pen-ə yaza bilməz; agent paralelliyi yalnız mətn spec üçün, .pen icra mən-sıralı
2026-05-23 14:00 [gotcha] Pencil screenshot caching: yeni yaradılan frame batch_design-dan dərhal sonra qara render olunur, batch_get isə content-i doğru göstərir — manual refresh lazım
2026-05-23 14:00 [gotcha] lucide-də `alert-triangle` yox, `triangle-alert` var — ikon adlandırma fərqi
2026-05-23 14:00 [gotcha] Pencil frame width:"fill_container" horizontal layout-da sibling-i clip edə bilər; alignItems:"start" + 2 fill_container child sıxışdırır
2026-05-23 14:00 [impl] .pen layout: 00 Cover (y=-2270, 1360w) · 01 DesignSystem (y=0, exists) · 02 Onboarding (y=6126, y=7138) · 02→03 Bridge (y=8160) · 03 Questions (y=8800) · 04 States (y=11400)
2026-05-23 14:00 [impl] Section label format: 2-digit number (caption) + 54/800 mega title + 17/normal subtitle — pXd5V layout-u kanonik
2026-05-23 14:00 [refs] 5 research agent paralel: cover-spec, design-system-detail, AZ-copy-bank, cross-ref-matrix, visual-refactor-catalog — bütün output-lar Phase 2-də tətbiq olunacaq
2026-05-23 14:00 [impl] Phase 1 .pen icrası tamamlandı: 33 ekran AZ adı, 4 section label, Cover frame, 02→03 bridge with 7-Q dots, 22 cross-ref chip
2026-05-23 22:00 [invariant] AI plan + human approval = quality gate, TRAINER DEYİL — AI elmi-əsaslı plan qurur, premium-da insan (mütəxəssis) onaylayır; fərdi trainer-istifadəçi münasibəti yox. CLAUDE.md "trainer vəd etmə" qaydası KEÇƏRLİDİR.
2026-05-23 22:00 [criteria] Onboarding AI Disclosure copy: "Plan AI tərəfindən elmi əsaslarla qurulur — son qərar səndədir. Premium istifadəçilərdə plan, göndərilmədən əvvəl bir mütəxəssis tərəfindən yoxlanılıb onaylanır. Bu tibbi məsləhət deyil." — "canlı məşqçi" sözü YASAQ
2026-05-23 22:00 [gotcha] Phase 1-də .pen-ə "canlı məşqçi uyğunluq yoxlaması" frazası yazıldı (eQcvv/Cs4v3/z2ZaHx node) — TRAINER kimi səslənir, yenidən düzəliş tələb edir (Phase 5)
2026-05-23 22:00 [refs] .pen cari layout: 00 Cover (y=−2270) · 01 DS (y=0) · 02 Onboarding 3 row (y=6126/7138/7960) · Bridge (y=8870) · 03 Suallar (y=9450) · 04 Post-Onboarding (y=10580)
2026-05-23 22:00 [impl] Sample Workout (#21 / m8M6cD) silindi — paywall post-dashboard olur, onboarding-də yox; ux-design-specification §6 Ekran 21 DEPRECATED işarələndi
2026-05-23 22:00 [impl] ProfilePreview (u1cEVR) onboarding row 1-ə qaytarıldı — handoff yerində, Questions section-dan çıxarıldı (questions yalnız Q1-Q7+V5)
2026-05-23 22:00 [impl] V variantları parent-lərinin altına inline yerləşdi: row 3 y=7960 (V4·V2·V1·V3·V6), V5 questions row-da Q3-paired, V7 Design System-də (x=1820, y=0)
2026-05-23 22:00 [criteria] Phase 5 əvvəl Phase 2-dən: strateji nüans (insan onayı, trainer deyil) bütün AI Disclosure copy + PRD + CLAUDE.md-də düzəlsin, sonra digər polish/Design System/L2-L3-L5 işləri
2026-05-23 23:00 [gotcha] ProCoaching (#26 WXUwF) ekranında "PEŞƏKAR MƏŞQÇİ İLƏ ÇALIŞMAQ" + "gerçək sertifikatlı məşqçi" copy-si Phase 5 düzəlişində SIZIB — düzəldildi → "Fərdi mütəxəssis dəstəyi" + "İstədiyin mütəxəssisi seç"
2026-05-23 23:00 [criteria] Phase 5 "trainer/coach/məşqçi" sweep yenidən bütün post-onboarding ekranlarına yayılmalıdır — sadəcə AI Disclosure (#13) düzəlişi tam coverage demir
2026-05-23 23:00 [impl] Phase 2 batch_design icrası: 25+ ekranda copy polish tətbiq edildi (Q1-Q7, ProfilePreview, AuthGate, Signup/Login, ConfirmEmail, ResetForm V6, Welcome, Lang, Parental, AgeGate Blocked, Pregnancy, Paywall, Logout Sync, DeleteConf1/2, ProCoaching)
2026-05-23 23:30 [impl] Phase 3 DS-də 3 əksik sub-section əlavə olundu: spacing-radius (j7lWc) · iconography (QKUjH) · forms-feedback (I2usVc). DS-də artıq 6 sub-section vardı (cover/colors/typography/buttons/selection/system).
2026-05-23 23:30 [gotcha] Phase 3 spec 9 sub-section təklif edirdi, lakin DS-də 6-sı artıq mövcud — agent spec mövcud strukturu nəzərə almır, mövcud frame-ləri yenidən yaratmaq qadağa
2026-05-23 23:30 [impl] Phase 4 (19 L2/L3/L5 ekranı) növbəti session-a qaldı — spec artıq `_bmad-output/planning-artifacts/ux-phase4-question-screens-spec-2026-05-23.md` daxilində; growing context budget üzündən deferred
