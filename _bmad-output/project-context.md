# fitnessApp — Project Context

## Project Overview
Azerbaycan bazarına yönəlmiş AI-dəstəkli fitness tracking tətbiqi.
Active research phase: scope dəqiqləşdirilir (2026-05-11).

## Tech Stack (TENTATIVE — validate during architecture phase)
- Mobile: Kotlin Multiplatform Mobile (KMM)
- Backend: Supabase (Auth, PostgreSQL, Storage, Edge Functions, Realtime)
- Admin/Web: Next.js on Vercel
- AI Content: Google Cloud AI ($300 credit constraint)
- 3D Assets: Mixamo/Sketchfab first, AI generation later

## Languages
- Code comments: English
- User-facing strings: Azerbaijani (primary), Russian, English
- DB content: tri-lingual columns (name_az, name_ru, name_en)
- Documentation: Turkish (per user preference)

## Hard Constraints
- Supabase RLS məcburi hər table üçün — heç vaxt atlama
- Video/media URL-lər signed URL (TTL 1 saat)
- Offline-first dizayn — internet olmadan əsas məşq işləməli
- $300 GCP kredit limiti — hər AI content cost track olunmalı
- Solo developer — minimal infrastructure overhead

## Naming Conventions
- Database: snake_case
- Kotlin: camelCase (variables), PascalCase (classes)
- Swift: camelCase (variables), PascalCase (classes)
- Files: kebab-case for docs, camelCase for code

## Architecture Patterns
- KMM shared module: business logic, data, network (Ktor), local DB (SQLDelight)
- Platform UI: SwiftUI (iOS), Jetpack Compose (Android)
- State management: per-platform native patterns (no shared UI state library)
- Edge Functions: AI API proxy, RLS bypass logic, payment webhooks

## Out-of-Scope (Phase 2+, don't suggest now)
- Diet modulu (ayrı app/module olacaq)
- Real-time hərəkət analizi (kamera ilə)
- Social/leaderboard features
- Wearable integration (Apple Watch, Wear OS)
- Premium subscription billing

## Competitive Context
- Direct: BetterMe, MyFitnessPal, Nike Training Club, Freeletics
- Unique angle: Tam AZ lokalizasiya + AI video/3D content + ev/zal hibrid

## Monetization
- Freemium model
- Premium target: 5-10 AZN/ay (BetterMe-yə uyğun)
- Phase 2-də diet modulu inteqrasiyası

## Solo Developer Constraints
- No code reviewer — adversarial review məcburi hər PR-da
- No PM — agentlər discipline-i təmin etməli
- No designer — UX agent həqiqi dizayn rolu oynayır
- Time: ~20 saat/həftə (full-time IBAM-da)

## Communication Preferences
- Direct, technical responses — sugar-coating lazım deyil
- Decisions explained with reasoning
- Production-proven tools üstün tutulur
- Iterative challenge-and-refine workflow
