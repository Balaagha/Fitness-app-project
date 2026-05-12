# Fitness App — Project Guide

## Project Overview

Azərbaycan bazarına yönəlmiş AI-dəstəkli fitness tracking tətbiqi. Tətbiq istifadəçilərə fərdi idman proqramları, kalori hədəfləri, AI ilə generasiya edilmiş hərəkət videoları və 3D modellər təklif edir. Həm ev, həm də zal idmanını dəstəkləyir. Gələcəkdə ayrı diet proqramı modulu ilə inteqrasiya nəzərdə tutulur.

**Əsas rəqiblər:** BetterMe, MyFitnessPal, Nike Training Club, Freeletics

---

## Vision & Goals

### Məhsul Vizyonu
- Azərbaycanlı istifadəçilər üçün tam lokallaşdırılmış (AZ / RU / EN) fitness təcrübəsi
- Hər hərəkət üçün AI-generasiya edilmiş demo videoları
- Hər hərəkət üçün 3D model (AI ilə generasiya olunmuş və ya hazır kitabxanadan)
- İstifadəçinin məqsədinə (arıqlamaq / əzələ qazanmaq / forma saxlamaq / güclənmək), fiziki göstəricilərinə və avadanlığına uyğun fərdi proqram
- Kalori hədəfi, makro balansı, su içmə xatırlatması
- Ev idmanı + Zal idmanı — hər ikisini dəstəkləmək

### Gələcək Modullar (Faza 2+)
- **Diet Proqramı** — ayrı modul kimi, fitness app-dan yönləndirmə ilə açılacaq
- Premium abunəlik modeli

---

## Tentative Tech Stack

> Bütün seçimlər araşdırma mərhələsindədir, dəyişə bilər.

| Qat | Texnologiya | Qeyd |
|-----|-------------|------|
| **Mobile** | Kotlin Multiplatform Mobile (KMM) | iOS + Android kod paylaşımı |
| **Backend** | Supabase | Auth, PostgreSQL DB, Storage, Realtime, Edge Functions |
| **Frontend / Admin** | Vercel (Next.js) | Admin panel, landing page |
| **AI Content** | Google Cloud AI | $300 kredit mövcuddur — video + 3D generasiya üçün |
| **3D Modellər** | AI generasiya və ya hazır kitabxana (Mixamo/Sketchfab) | Hərəkət animasiyaları |

### Supabase İstifadə Ssenariləri
- İstifadəçi auth (email, Google, Apple sign-in)
- İdman proqramları, hərəkətlər, məşq tarixi — PostgreSQL
- Video və 3D model faylları — Supabase Storage
- Real-time progress tracking

### Google Cloud AI İstifadəsi
- Hərəkət videoları generasiyası (Veo / VideoFX və ya analoji)
- 3D model / animasiya (alternativ: Mixamo, Sketchfab hazır kitabxanaları)
- $300 kredit limitini nəzərə alaraq cost-per-content optimizasiyası lazımdır

---

## Core Features (MVP Scope — Dəqiqləşdiriləcək)

### İstifadəçi Profilləşməsi
- [ ] Məqsəd seçimi: arıqlamaq / əzələ qazanmaq / forma saxlamaq / güclənmək
- [ ] Cins, yaş, boy, çəki
- [ ] Təcrübə səviyyəsi: yeni başlayan / orta / qabaqcıl
- [ ] Avadanlıq: heç yox (ev) / minimal (rezin, köpük) / tam zal avadanlığı
- [ ] Həftəlik iş yükü: neçə gün, neçə dəqiqə

### İdman Proqramı
- [ ] Fərdiləşdirilmiş həftəlik proqram generasiyası
- [ ] Hər məşq sessiyası üçün strukturlaşdırılmış plan (istiləşmə → əsas hissə → soyuducu)
- [ ] Hər hərəkət üçün: ad (AZ/RU/EN), AI video, 3D model, təkrar/dəst sayı, istirahət vaxtı
- [ ] Ev + Zal versiyaları (alternativ hərəkətlər)
- [ ] Progress tracking: çəki, ölçülər, fotolar

### Kalori & Qidalanma
- [ ] Gündəlik kalori hədəfi hesablaması (BMR + TDEE)
- [ ] Makro bölgüsü (protein / karbohidrat / yağ)
- [ ] Su içmə hədəfi və xatırlatma
- [ ] Yemək logu (sadə — tam diet modulu faza 2-dədir)
- [ ] Diet moduluna yönləndirmə (faza 2 açıldıqda aktiv olacaq)

### AI Content Pipeline
- [ ] Hər hərəkət üçün video generasiya (Google AI)
- [ ] 3D model / animasiya (AI generasiya və ya Mixamo/Sketchfab kitabxanası)
- [ ] Content CMS — yeni hərəkət əlavə etmək üçün admin paneli (Vercel)

### Lokalizasiya
- [ ] Azərbaycan dili (əsas)
- [ ] Rus dili
- [ ] İngilis dili

---

## Architecture Notes

### Data Model (İlkin — Supabase/PostgreSQL)

```
users
  id, email, created_at

user_profiles
  id, user_id, goal, gender, age, height_cm, weight_kg,
  experience_level, equipment_type, weekly_days, session_duration_min

exercises
  id, name_az, name_ru, name_en, category, muscle_groups[],
  equipment_required, video_url, model_3d_url, difficulty, instructions_az

workouts
  id, user_id, week_number, created_at

workout_sessions
  id, workout_id, day_of_week, session_type (warmup/main/cooldown)

workout_session_exercises
  id, session_id, exercise_id, sets, reps, rest_seconds, order_index

progress_logs
  id, user_id, date, weight_kg, body_measurements (jsonb), notes, photos[]

calorie_logs
  id, user_id, date, target_kcal, consumed_kcal, protein_g, carbs_g, fat_g, water_ml
```

### Mobile Arxitekturası (KMM)
- **Shared module**: biznes məntiqi, data layer, network (Ktor), local DB (SQLDelight)
- **iOS target**: SwiftUI
- **Android target**: Jetpack Compose
- Supabase Kotlin SDK shared module-da istifadə ediləcək
- Offline-first: məşq sessiyaları lokal DB-də saxlanır, sonra sync edilir

### AI Content Workflow
1. Admin panel (Vercel) vasitəsilə yeni hərəkət əlavə edilir
2. Google AI API-yə video generasiya sorğusu göndərilir
3. Video → Supabase Storage-a yüklənir, signed URL saxlanır
4. 3D model: əvvəlcə Mixamo kitabxanasından başlamaq (cost-effective), sonra AI generasiya
5. exercises cədvəlindəki video_url və model_3d_url yenilənir

### Supabase Security
- RLS (Row Level Security) hər cədvəl üçün məcburi
- Video/media URL-lər signed URL olmalı (public access yox)
- Edge Functions server-side biznes məntiqi üçün (AI API çağırışları)

---

## Competitive Analysis

| Rəqib | Güclü tərəfləri | Zəif tərəfləri | Bizim üstünlüyümüz |
|-------|-----------------|----------------|---------------------|
| BetterMe | Böyük content kitabxanası, güclü marketing | Zəif AZ lokalizasiya, AI yox | Tam AZ dili, AI video/3D |
| MyFitnessPal | Güclü kalori tracker | Zəif workout planlama | Vahid həll (idman + kalori) |
| Nike Training Club | Yüksək keyfiyyətli videolar | Pullu, az fərdiləşmə | Yerli bazar, AI fərdiləşmə |
| Freeletics | AI coach | Baha, AZ dili yox | AZ dili, ev idmanı focus |

---

## Monetization Model (Tentative)

- **Freemium**: Əsas proqram pulsuz, premium əlavə xüsusiyyətlər ödənişli
- **Premium xüsusiyyətlər**:
  - Tam AI proqram generasiyası (pulsuzda limitli)
  - Qabaqcıl progress analitikası
  - Diet modulu inteqrasiyası (faza 2)
  - Şəxsi mentor / AI coach (gələcək)
- **Azərbaycan bazarı qiyməti**: 5–10 AZN/ay (BetterMe analoji qiymətə uyğun)
- **Ödəniş üsulları**: Kart (Visa/MC), local ödəniş (araşdırılacaq)

---

## Coding Standards

> Texnologiya stack dəqiqləşdikcə əlavə ediləcək.

### Ümumi Qaydalar
- KMM shared module-da platform-specific kod minimum saxla
- Supabase RLS hər cədvəl üçün məcburi — bunu heç vaxt atla
- Video URL-lər signed URL olmalı (public access yox)
- Lokalizasiya stringləri mərkəzləşdirilmiş resurs faylında (shared module-da)
- Offline-first dizayn: internet olmadan da əsas funksionallıq işləməli

### Naming Conventions
- DB: snake_case
- Kotlin: camelCase (dəyişənlər), PascalCase (siniflər)
- Swift: camelCase (dəyişənlər), PascalCase (siniflər)

---

## Running the Project

> İlk setup tamamlandıqdan sonra doldurulacaq.

### Tələblər
- [ ] Supabase hesabı + yeni proyekt yaratmaq
- [ ] Google Cloud hesabı ($300 kredit aktivdir — b.alihummatov@gmail.com)
- [ ] Vercel hesabı
- [ ] Android Studio + Kotlin Multiplatform plugin
- [ ] Xcode (iOS build üçün, Mac tələb olunur)

---

## Future Roadmap

### Faza 1 — MVP
- İstifadəçi onboarding + profil
- Fərdi idman proqramı generasiyası
- Əsas hərəkət kitabxanası (video + 3D)
- Kalori hədəfi tracker
- Ev + Zal idman planları

### Faza 2 — Diet Modulu
- Ayrı diet proqramı (fitness app-dan yönləndirmə)
- Yemək verilənlər bazası (Azərbaycan ərzaqları fokuslu)
- Qidalanma planı generasiyası

### Faza 3 — Advanced AI
- Real-time hərəkət düzəliş analizi (kamera vasitəsilə)
- Sosial xüsusiyyətlər (dostlar, liderlik cədvəli)
- Geyilebilən cihaz inteqrasiyası (Apple Watch, Wear OS)

---

## Active Research

- **[determine-scope-of-start-up]** — Proyektin tam scope-u, texniki stack və rəqabət mövqeyinin müəyyənləşdirilməsi (aktiv — 2026-05-11)

---

## BMad Session Rules

### Context
Solo developer building Azerbaijan-market fitness app. Active scope research phase.
Stack: KMM + Supabase + Google Cloud AI + Vercel.

### Defaults
- Communication: Turkish (mixing English technical terms is fine)
- Code: English comments, multi-language user strings (AZ/RU/EN)
- Always verify Supabase RLS for any DB-touching code
- Cost-conscious: $300 GCP credit total, track AI API costs

### When BMad workflows are active
Follow BMad agent persona and skill conventions.

### Outside BMad workflows (plain chat)
- Direct technical answers, no over-explanation
- Production-proven solutions over experimental
- Question scope creep aggressively (4-month solo dev timeline)
- Use Context7 MCP for library docs before training data

### Avoid
- Suggesting Phase 2 features (diet, real-time analysis, social, wearables)
- Long preambles before code
- "Generic best practices" without project context
