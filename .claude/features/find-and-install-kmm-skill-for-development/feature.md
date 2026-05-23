# Research: find-and-install-kmm-skill-for-development

## Metadata
- **Type**: research
- **Branch**: `n/a`
- **Started**: 2026-05-17
- **Current phase**: 3 / 3
- **Overall status**: in_progress
- **Related features**: none (paralel context: `determine-scope-of-start-up` — paused)

---

## Question
2026 may itibarilə Kotlin Multiplatform (keçmiş "KMM" — rebrand olub) + Compose Multiplatform development üçün Claude Code-un keyfiyyətini maksimuma çıxaracaq hansı **skill / MCP server / plugin / sub-agent / Context7-bənzər doc-loader / Gradle inspector / Compose preview render** alətləri mövcuddur, onlardan hansılarını fitnessApp üçün quraşdırmalıyıq və hansı GitHub repolarından?

## Why It Matters
Bu fitnessApp KMP + Supabase + Compose Multiplatform üzərində qurulur (CLAUDE.md kanonik stack). Claude Code-un default tooling-i web-centric (Node.js, Python) — KMP/Compose üçün xüsusi köməkçi tooling olmasa, agent:
- Gradle DSL səhvləri edir (Kotlin DSL vs Groovy, version catalog, multi-platform source sets)
- expect/actual quirk-larını qaçırır (commonMain vs iosMain visibility)
- Compose Multiplatform iOS-spesifik məhdudiyyətləri bilmir (no `LocalContext`, ResourcesAPI fərqli)
- KMP 2.x → 2.2 keçidlərindəki breaking change-ləri görmür (training cutoff 2026 yanvar olsa belə)
- Supabase Kotlin SDK API-sini hallucinate edir (powersync, postgrest-kt versionları)

Düzgün tooling-lə Claude Code:
- Real-time Kotlin/KMP docs çəkir (Context7 bənzər)
- Gradle çıxışını parse edib səhv lokallaşdırır
- Compose preview screenshot render edir → vizual feedback
- ProGuard/R8 fluent
- iOS framework export problemlərini debug edir

---

## 2026 May — Ekosistem Snapshot (testiq edildi)

| Fact | Status | Mənbə |
|------|--------|-------|
| **KMM** adı 2023-də deprecate olundu → rəsmi ad **KMP** (Kotlin Multiplatform). "M" = Mobile çıxdı çünki desktop/JVM/web/native əhatə olunur. | ✅ təsdiqləndi | kotlinlang.org/multiplatform |
| KMP **stable** (Nov 2023+). Production: Netflix, Cash App, McDonald's. | ✅ stable | jetbrains.com, Android Developers |
| **Compose Multiplatform 1.8.0 → iOS stable** (May 2025). | ✅ stable | JetBrains Kotlin blog |
| **Compose Multiplatform 1.11.0** (May 2026, ən son). Eksperimental native UIView-based text input. | ✅ released | blog.jetbrains.com/kotlin/2026/05 |
| **Swift Export** (Kotlin 2.2.20) ObjC bridge-i əvəz edir, 2026-da stable hədəfi. | ⏳ approaching stable | kotlinlang.org docs |
| **Compose Multiplatform Web (Wasm)** — beta, 2026-da stable hədəfi. | ⏳ beta | JetBrains roadmap |
| Android Studio 2025.2+ **built-in MCP server**, Claude Code `/ide` ilə qoşulur. | ✅ available | jetbrains.com/help/idea/mcp-server |

**Implication for CLAUDE.md:** `Kotlin Multiplatform Mobile (KMM)` terminologiyası köhnəlib — sənədlərdə "KMP" istifadə etmək lazımdır. Compose Multiplatform iOS-də stable olduğu üçün SwiftUI fallback-i yalnız platform-spesifik UI üçün lazımdır (StoreKit, HealthKit), əsas UI ortaq Compose ola bilər.

---

## Tooling Inventory — Tapılan Bütün Alətlər

### A. Skills (drop-in markdown packages)

| # | Repo | Skill name | Target | Stars / Release / Date | KMP-aware | Stack Match | Install |
|---|------|------------|--------|------------------------|-----------|-------------|---------|
| A1 | [Meet-Miyani/compose-skill](https://github.com/Meet-Miyani/compose-skill) | compose-skill | Compose + KMP/CMP (Android/iOS/Desktop/Web) | 219⭐, v5.1.0 (Apr 6, 2026), 33 commits | ✅ **first-class** | 🟢 **best match** — MVI+Koin+Ktor+Room+DataStore+Coil 3+Nav 3+Paging 3 | `git clone https://github.com/Meet-Miyani/compose-skill.git ~/.claude/skills/compose-skill` |
| A2 | [aldefy/compose-skill](https://github.com/aldefy/compose-skill) | compose-expert | Jetpack Compose + CMP (Desktop/iOS/Web), Android TV | 463⭐, v2.3.1 (May 3, 2026), 53 commits | ✅ CMP-aware | 🟡 partial — Paging 3 + Material 3 + Nav 2→3 strong; **DI/network boş** | `/plugin marketplace add aldefy/compose-skill` then `/plugin install compose-expert` (or git clone to ~/.claude/skills/) |
| A3 | [rcosteira79/android-skills](https://github.com/rcosteira79/android-skills) | 18 skills bundle | Android + KMP | 60⭐, v3.0.0 (Apr 17, 2026), 88 commits | ✅ 6 KMP-aware (kmp-ktor, kotlin-coroutines, kotlin-flows, compose, android-data-layer, rxjava-migration) | 🟢 strong — gradle-build-performance, convention plugins, repository pattern | `/plugin marketplace add rcosteira79/android-skills` then `/plugin install android-skills@android-skills` |
| A4 | [ahmed3elshaer/everything-claude-code-mobile](https://github.com/ahmed3elshaer/everything-claude-code-mobile) | 27 agents + 48 skills + 35 commands + 3 MCP servers | Android, iOS, KMP | 47⭐, v1.1.0 (Mar 28, 2026), 24 commits | ✅ **kmp-architect, kmp-build, kmp-dependency-fix, kmp-test, expect-actual, sqldelight-patterns, shared-coroutines, kmp-di, kmp-navigation, kmp-networking, kmp-repositories** | 🟡 mega-toolkit — solo dev üçün noise (liquid-glass-guide, m3-expressive irrelevant) | `/plugin marketplace add ahmed3elshaer/everything-claude-code-mobile` + `/plugin install everything-claude-code-mobile@ahmed3elshaer` + manual rules copy |
| A5 | [dpconde/claude-android-skill](https://github.com/dpconde/claude-android-skill) | claude-android-skill | Android **only** (NowInAndroid pattern) | 218⭐, WIP, 2 commits | ❌ **no KMP** | 🔴 reject — Hilt only (KMP-incompatible), Retrofit (not Ktor) | git clone → ~/.claude/skills/ |
| A6 | [Drjacky/claude-android-ninja](https://github.com/Drjacky/claude-android-ninja) | claude-android-ninja | Android **only** | 52⭐, 477 commits master | ❌ no KMP | 🔴 reject — Hilt+Room+Retrofit Android-specific | `npx openskills install drjacky/claude-android-ninja` or git clone |
| A7 | [mcpmarket — KMP Architect](https://mcpmarket.com/tools/skills/kotlin-multiplatform-kmp-architect) | kmp-architect | KMP architecture | unknown | ✅ KMP-focused | 🟡 closed source, paid marketplace | mcpmarket UI |
| A8 | [mcpmarket — iOS Integration for KMP](https://mcpmarket.com/tools/skills/ios-integration-for-kmp) | ios-integration-for-kmp | KMP→iOS bridge | unknown | ✅ XCFramework export, expect/actual for Keychain/SQLite, Coroutines→SwiftUI ObservableObject | 🟡 closed source | mcpmarket UI |

### B. Sub-agents (single-file Kotlin persona)

| # | Repo | Agent | Stars (repo) | KMP coverage | Install |
|---|------|-------|--------------|--------------|---------|
| B1 | [VoltAgent/awesome-claude-code-subagents](https://github.com/VoltAgent/awesome-claude-code-subagents/blob/main/categories/02-language-specialists/kotlin-specialist.md) | kotlin-specialist | 20k⭐ repo | ✅ explicit "Multiplatform strategies" — expect/actual, platform APIs, Common code maximization, cross-platform testing | Manual: copy `.md` to `~/.claude/agents/` |
| B2 | [rohitg00/awesome-claude-code-toolkit](https://github.com/rohitg00/awesome-claude-code-toolkit/blob/main/agents/language-experts/kotlin-specialist.md) | kotlin-specialist | unknown | ✅ KMP-aware — `commonMain`, `androidMain`, `iosMain`, Ktor Client, SQLDelight | Manual md → `~/.claude/agents/` |

### C. MCP servers (runtime/build tooling)

| # | Server | Latest | Auth | Purpose | KMP Support | Install (Claude Code) |
|---|--------|--------|------|---------|-------------|------------------------|
| C1 | [rnett/gradle-mcp](https://github.com/rnett/gradle-mcp) | v0.0.11 (Mar 28, 2026), 48⭐, Apache-2.0 | none | Gradle project introspection, task exec, test results, **Kotlin REPL**, **Compose UI render**, Build Scans | ✅ multi-project, version catalogs (implied) | jbang: `jbang run --quiet --fresh gradle-mcp@rnett` (requires JDK 25) — add to `.mcp.json` |
| C2 | [IlyaGulya/gradle-mcp-server](https://github.com/IlyaGulya/gradle-mcp-server) | v0.1.1 (Jul 2025), 44⭐ | none | 3 tools (project info, exec task, run tests) | Generic Gradle | ⚠️ **ARCHIVED Mar 2026 → use C1 (rnett/gradle-mcp)** |
| C3 | [Kotzilla MCP Server](https://blog.kotzilla.io/kotzilla-mcp-server-for-kmp) ([docs](https://doc.kotzilla.io/docs/discover/mcpServer)) | active | Kotzilla account (free to try, GDPR) | Runtime dependency graph (Koin), crash symbolication, perf metrics (Android/iOS/Desktop/JS/WASM), blocking call detection, eager init, StateFlow misuse | ✅ **KMP-first-class** | One-prompt setup adds Gradle plugin + monitoring call to Koin |
| C4 | [getsentry/XcodeBuildMCP](https://github.com/getsentry/XcodeBuildMCP) | active, 82 tools | none | xcodebuild CLI driver — build, test, simulator, LLDB, UI automation, project scaffolding | ✅ **critical for KMP iOS** — XCFramework build, simulator runs, structured error JSON | `claude mcp add` (Xcode 16+, macOS 14.5+) |
| C5 | [JetBrains MCP (built-in)](https://www.jetbrains.com/help/idea/mcp-server.html) | bundled in Android Studio 2025.2+ | none | IDE introspection — open files, diagnostics, refactor primitives | ✅ via Android Studio | Install [Claude Code plugin](https://plugins.jetbrains.com/plugin/27310) (id 27310) — `/ide` from terminal |
| C6 | [upstash/context7](https://github.com/upstash/context7) | active | optional API key | Real-time docs fetching — Kotlin, KMP, Compose Multiplatform, Supabase | ✅ doc loader for libraries | Add to `.mcp.json`: `command: "npx"`, `args: ["-y", "@upstash/context7-mcp"]` |
| C7 | [normaltusker/kotlin-mcp-server](https://github.com/normaltusker/kotlin-mcp-server) | active | LLM keys (OpenAI/Gemini/OpenRouter) | Aider integration, Gradle build/test, **Kotlin LSP** | ⚠️ Aider-coupled — Claude-Code-native deyil | Docker / npm install — Claude Code üçün **suboptimal** |
| C8 | [JetBrains/mcp-kotlin-sdk](https://github.com/JetBrains/mcp-kotlin-sdk) (a.k.a. modelcontextprotocol/kotlin-sdk) | official | n/a | **MCP server yazma SDK-sı** — bizim öz MCP-mizi yazmaq üçün, hazır server deyil | — | Gradle dep `io.modelcontextprotocol:kotlin-sdk:0.5.0` |
| C9 | [Build-Scout](https://mcpservers.org/servers/David-Parry/build-scout) | active | none | Multi-build-system (Gradle/Maven/NPM/Cargo/Make/CMake) | Generic | 🔴 overkill — gradle-mcp daha fokuslu |

### D. IDE integration

| # | Tool | Purpose | Install |
|---|------|---------|---------|
| D1 | [Claude Code JetBrains plugin](https://plugins.jetbrains.com/plugin/27310-claude-code-beta-) (Anthropic official) | Diff viewer, file refs `Cmd+Option+K`, diagnostic sharing, `/ide` bridge, Android Studio supported | JetBrains Marketplace → search "Claude Code" → install in **Android Studio Koala+** |

### E. Compose preview rendering

| # | Tool | Use case | Notes |
|---|------|----------|-------|
| E1 | [Compose Preview Screenshot Testing](https://developer.android.com/studio/preview/compose-screenshot-testing) (AGP plugin) | `./gradlew :app:updateDebugScreenshotTest` — diff-based UI regression | Android-only, KMP-də CMP `@Preview` (kotlinlang.org/docs/multiplatform/compose-previews.html) ilə əvəz olunur |
| E2 | [comshot](https://klibs.io/project/mahozad/comshot) (KMP library) | Render Composable to PNG off-screen — Android/JVM/Native/Wasm/JS | Claude workflow-da: gradle task ilə render → png-i Read tool ilə "oxu" → visual feedback loop |
| E3 | Compose Multiplatform `@Preview` (Android Studio K2 mode) | IDE-də CMP composable preview | Manual workflow, Claude inteqrasiya yox |

---

## Phases

### Phase 1: Scope the Question ✅ COMPLETE
- [x] Sub-suallar sıralandı:
  - (a) KMP 2026 may state-i nədir? (rebrand, version, stable platforms)
  - (b) Claude Code üçün hansı KMP skill-lər mövcuddur?
  - (c) Compose Multiplatform skill-ləri içində KMP-aware olan hansılar?
  - (d) Hansı MCP server-lər runtime/build inteqrasiyası verir?
  - (e) IDE inteqrasiyası (Android Studio) necə işləyir?
- [x] Constraints/non-goals:
  - **Constraint**: solo dev, ~20h/həftə → maintenance yükü minimum (aktiv repo + son 90 gün release)
  - **Constraint**: Stack lock-in — MVI + Koin + Ktor + Compose Multiplatform + Supabase Kotlin SDK (Room istifadə etmirik, SQLDelight + Supabase var)
  - **Non-goal**: Hilt/Retrofit/Dagger əsaslı skill-lər (KMP-incompatible)
  - **Non-goal**: Flutter/RN-spesifik plugin-lər
  - **Non-goal**: Closed-source/paid marketplace (open-source first)
- [x] "Done" tərifi: bu feature.md sənədli tövsiyə + Phase 2-də əksər skill-lərin install-ı + Phase 3-də konfiqurasiya yoxlaması.
- **Status:** ✅ complete

### Phase 2: Investigation ✅ COMPLETE
- [x] 12+ alət aşkarlandı (yuxarıdakı A/B/C/D/E inventarı)
- [x] Hər repo üçün maintenance signal yoxlandı (last release, stars, commit count)
- [x] KMP/Compose 2026 state JetBrains rəsmi mənbələri ilə təsdiqləndi
- [x] Stack-match cədvəli quruldu (Findings bölməsində)
- **Status:** ✅ complete

### Phase 3: Synthesize & Recommend 🎯 AKTİV
- [x] Stack-aware skill scoring tamamlandı
- [x] **TÖVSİYƏ EDİLƏN BUNDLE** (aşağıdakı Decisions bölməsində)
- [ ] Install qərarı təsdiq edildikdən sonra `Phase 4 (yeni)`: faktiki quraşdırma
- [ ] CLAUDE.md-də KMM → KMP terminoloji yenilənməsi
- [ ] `.mcp.json` faylının fitnessApp-da yaradılması (root)
- **Status:** ⏳ recommendation hazır, user təsdiqi gözlənir

---

## Decisions / Recommendations

| Date       | Decision | Rationale | Impact |
|------------|----------|-----------|--------|
| 2026-05-17 | **A1 (Meet-Miyani/compose-skill) PRIMARY SKILL** | Stack perfect match (MVI + Koin + Ktor + DataStore + Coil 3 + Nav 3 + Paging 3 + CMP). Active maintenance (v5.1.0 Apr 2026). 219⭐ small ama tracking real Compose+KMP idioms, hallucination risk minimum. | Yüksək — bu Claude-un Compose+KMP kod keyfiyyətini bir səviyyə qaldırır |
| 2026-05-17 | **A3 (rcosteira79/android-skills) SECONDARY SKILL** | 18-skill bundle-da `gradle-build-performance`, `android-gradle-logic`, `kotlin-coroutines`, `kotlin-flows`, `kmp-ktor`, `android-data-layer` məhz bizim ehtiyacımız. A1 ilə overlap minimal (A1=UI/state, A3=build/data/async). v3.0.0 yenidir. | Orta-yüksək — Gradle DSL+coroutines hallucinasiyalarını azaldır |
| 2026-05-17 | **B1 (VoltAgent/kotlin-specialist) AGENT** seçildi (B2 yerinə) | VoltAgent repo 20k⭐ — daha çox community testi, kotlin-specialist KMP coverage daha geniş ("Multiplatform strategies" bölməsi). | Aşağı-orta — agent yalnız ad çağırıldıqda işə düşür, faydası niche |
| 2026-05-17 | **C1 (rnett/gradle-mcp) MCP SERVER** | Yeganə aktiv qalmış Gradle MCP (IlyaGulya archived). Kotlin REPL + Compose UI render + Build Scans tooling-i fitnessApp-da gradle error debug üçün kritikdir. JDK 25 tələbi — `sdkman` ilə təmin et. | Orta-yüksək — `./gradlew build` çıxışını structured JSON-a çevirir, Claude error parse keyfiyyəti artır |
| 2026-05-17 | **C4 (XcodeBuildMCP) MCP SERVER** | KMP iOS framework export (XCFramework) və simulator runs üçün açıq lazımdır. 82 tool, structured JSON, Xcode 16+ uyğun. Solo dev üçün iOS debug loop-unu kəsir. | Yüksək — iOS rejection paths azalır, framework hallucination minimum |
| 2026-05-17 | **C6 (Context7 MCP) DOC LOADER** | CMP 1.11.0 (May 2026) docs Claude training cutoff-dən sonradır → Context7 olmazsa API hallucinasiyaları qaçılmazdır. Supabase Kotlin SDK eyni — versionlar tez dəyişir. | Yüksək — bütün library API hallucinasiya riskini kəsir |
| 2026-05-17 | **D1 (Claude Code JetBrains plugin) IDE BRIDGE** | Android Studio Koala+ inteqrasiyası — diff viewer + diagnostics sharing + `/ide` slash-command. Solo dev workflow üçün ergonomik. Pulsuz, official. | Orta — UX qazancı, kod keyfiyyətinə birbaşa təsir az |
| 2026-05-17 | **C3 (Kotzilla MCP) DEFER FAZA 2** | Production runtime introspection — pre-launch dəyəri yox. Crash reports, Koin graph perf yalnız real istifadəçilərdə işə yarayır. Free tier var amma Kotzilla SDK + monitoring call add etmək MVP onset-də scope creep. | Sıfır — Faza 2-yə qoy |
| 2026-05-17 | **A2 (aldefy/compose-skill), A4 (everything-claude-code-mobile) REJECT** | A2: A1 ilə 80% overlap, A1 daha stack-uyğun. A4: 27 agents/48 skills — solo dev üçün noise, liquid-glass-guide/m3-expressive iOS 26 talab edir bizim deyilik. | Sıfır — install etmə |
| 2026-05-17 | **A5 (dpconde), A6 (Drjacky) REJECT** | Android-only, Hilt/Retrofit Android-spesifik, KMP-də işləmir. | Sıfır |
| 2026-05-17 | **CLAUDE.md-də "KMM" terminologiyası "KMP"-yə yenilənməli** | Rəsmi rebrand 2023-də olub, "M=Mobile" semantically incorrect (desktop/web/native əhatə olunur). Skill-lərin auto-trigger keyword-ləri "KMP" istifadə edir. | Aşağı — fayl təmizliyi, doğru keyword match-i |
| 2026-05-17 | **Compose Preview Screenshot Testing və comshot DEFER** | Test scaffold-u olmayan boş projedə screenshot test yoxdur. Phase 1 (4 ay MVP) sonu test stratejisi qərarlaşandan sonra qayıt. | Sıfır |
| 2026-05-19 | **Context7 query-lərində version tag pin** (`/jetbrains/compose-multiplatform/v1.11.0-alpha02` formatı) | Version drift-dən gələn doc hallucinasiyasını sıfıra endirir; resolve-library-id versions array-dən konkret tag seç. | Orta — bütün CMP/KMP/Supabase Context7 çağırışlarına tətbiq olunur |

---

## TÖVSİYƏ EDİLƏN BUNDLE — fitnessApp final stack

### 4 skill + 3 MCP server + 1 IDE plugin + 1 agent

**Skills (`.claude/skills/`):**
1. **compose-skill** (Meet-Miyani) — primary UI/state/network
2. **android-skills** (rcosteira79) — Gradle/build/coroutines/flows/KMP-data
3. Optional: A1+A3 overlap olduqda namespace conflict ola bilər — A3-dən yalnız ehtiyac olan skill-ləri seçərək kopyala (manual: `kmp-ktor/`, `kotlin-coroutines/`, `kotlin-flows/`, `gradle-build-performance/`, `android-gradle-logic/`, `android-data-layer/`)

**Sub-agent (`.claude/agents/`):**
4. **kotlin-specialist.md** (VoltAgent) — ad-çağırışı ilə

**MCP servers (`.mcp.json` project root):**
5. **gradle-mcp** (rnett) — Gradle introspection
6. **XcodeBuildMCP** (getsentry) — iOS build/test
7. **context7** (upstash) — real-time KMP/Compose/Supabase docs

**IDE:**
8. **Claude Code JetBrains plugin** — Android Studio Koala+

### Konkret install komandaları (sıra ilə icra)

```bash
# === STEP 1: Skills ===
cd /Users/balaaghaalihumatov/Desktop/fitnessApp

# A1 primary
git clone https://github.com/Meet-Miyani/compose-skill.git .claude/skills/compose-skill

# A3 secondary (full bundle, sonra seçici trim oluna bilər)
git clone https://github.com/rcosteira79/android-skills.git /tmp/android-skills
cp -r /tmp/android-skills/skills/* .claude/skills/

# === STEP 2: Sub-agent ===
mkdir -p .claude/agents
curl -fsSL -o .claude/agents/kotlin-specialist.md \
  https://raw.githubusercontent.com/VoltAgent/awesome-claude-code-subagents/main/categories/02-language-specialists/kotlin-specialist.md

# === STEP 3: JDK 25 (gradle-mcp tələbi) ===
# əgər sdkman varsa:
sdk install java 25-tem
sdk default java 25-tem

# === STEP 4: gradle-mcp test ===
# jbang lazımdır
curl -Ls https://sh.jbang.dev | bash -s - app setup
jbang run --quiet --fresh gradle-mcp@rnett --version  # smoke test

# === STEP 5: XcodeBuildMCP ===
claude mcp add xcodebuild -- npx -y @getsentry/xcodebuildmcp

# === STEP 6: Context7 ===
claude mcp add context7 -- npx -y @upstash/context7-mcp

# === STEP 7: .mcp.json hand-edit (gradle-mcp jbang üçün) ===
# project root-da .mcp.json yarat / yenilə:
```

**`.mcp.json` faylının nümunəsi** (fitnessApp root):
```json
{
  "mcpServers": {
    "gradle": {
      "command": "jbang",
      "args": ["run", "--quiet", "--fresh", "gradle-mcp@rnett"]
    },
    "xcodebuild": {
      "command": "npx",
      "args": ["-y", "@getsentry/xcodebuildmcp"]
    },
    "context7": {
      "command": "npx",
      "args": ["-y", "@upstash/context7-mcp"],
      "env": { "CONTEXT7_API_KEY": "${CONTEXT7_API_KEY}" }
    }
  }
}
```

**IDE plugin (UI):**
- Android Studio → Settings → Plugins → Marketplace → "Claude Code" → Install → Restart
- Terminal-də `claude` çağıranda `/ide` ilə birləşdir

### Verification check (post-install)

```bash
# Claude Code restart sonra:
claude
# /skills        — A1+A3 görünməlidir
# /agents        — kotlin-specialist görünməlidir
# /mcp           — gradle, xcodebuild, context7 hamısı "connected" olmalıdır
```

---

## Findings / Sources

### Source 1 — KMP/Compose Multiplatform 2026 state
- **URLs**:
  - [Compose Multiplatform 1.11.0 release (May 2026)](https://blog.jetbrains.com/kotlin/2026/05/compose-multiplatform-1-11-0/)
  - [Compose Multiplatform 1.8.0 — iOS stable (May 2025)](https://blog.jetbrains.com/kotlin/2025/05/compose-multiplatform-1-8-0-released-compose-multiplatform-for-ios-is-stable-and-production-ready/)
  - [Kotlin Multiplatform compatibility & versions](https://kotlinlang.org/docs/multiplatform/compose-compatibility-and-versioning.html)
  - [State of Kotlin 2026](https://devnewsletter.com/p/state-of-kotlin-2026/)
- **Takeaway**: KMM → KMP rebrand rəsmidir. CMP iOS stable May 2025; CMP 1.11.0 cari versiya May 2026. Swift Export 2026-da stable hədəflənir.
- **Confidence**: Yüksək (JetBrains rəsmi blog).

### Source 2 — Meet-Miyani/compose-skill (PRIMARY tövsiyə)
- **URL**: https://github.com/Meet-Miyani/compose-skill
- **Takeaway**: v5.1.0 (Apr 6, 2026), 219⭐, 33 commits, 10 release. MVI + Koin (CMP) + Hilt (Android) + Ktor + Room (KMP-compat) + DataStore + Coil 3 + Nav 3 + Paging 3 + Material 3. Auto-trigger keywords: `@Composable`, `StateFlow`, `ViewModel`, `KMP`, `Ktor`, `recomposition`, `DataStore`, `Room`, `Navigation 3`, `Paging 3`, `Coil`, `MVI`, `CMP`. Install: `git clone https://github.com/Meet-Miyani/compose-skill.git ~/.claude/skills/compose-skill` (və ya `.claude/skills/`).
- **Confidence**: Yüksək.

### Source 3 — aldefy/compose-skill (ALTERNATIVE)
- **URL**: https://github.com/aldefy/compose-skill
- **Takeaway**: v2.3.1 (May 3, 2026), 463⭐ (daha böyük community), 53 commits. CMP-aware amma DI (Koin/Hilt), Ktor, persistence boş. UI/Paging 3/Nav 2→3 strong. Auto-trigger: `@Composable`, `remember`, `LazyColumn`, `NavHost`. Marketplace: `/plugin marketplace add aldefy/compose-skill` → `/plugin install compose-expert`.
- **Confidence**: Yüksək. **Reject reason**: A1-lə 80% feature overlap, A1 stack-uyğun.

### Source 4 — rcosteira79/android-skills (SECONDARY tövsiyə)
- **URL**: https://github.com/rcosteira79/android-skills
- **Takeaway**: v3.0.0 (Apr 17, 2026), 60⭐, 88 commits. 18 skill bundle. KMP-aware skills: `kotlin-coroutines`, `kotlin-flows`, `compose`, `kmp-ktor`, `android-data-layer`, `rxjava-migration`. Android-spesifik skills: `android-dev`, `android-tdd`, `android-ux`, `android-debugging`, `android-source-search`, `android-retrofit`, `coil-compose`, `android-gradle-logic`, `gradle-build-performance`. Bizə lazım olan subset: 6 KMP + 2 Gradle. Install: `/plugin marketplace add rcosteira79/android-skills` → `/plugin install android-skills@android-skills`. Architectural enforcement: Repository pattern + DispatcherProvider + offline-first + Convention Plugins.
- **Confidence**: Yüksək.

### Source 5 — ahmed3elshaer/everything-claude-code-mobile (REJECTED — overkill)
- **URL**: https://github.com/ahmed3elshaer/everything-claude-code-mobile
- **Takeaway**: Mega-toolkit — 27 agents (kmp-architect, kmp-build, kmp-dependency-fix, kmp-test, mobile-architect, ui-impl, network-impl, data-impl, ...), 48 skills, 35 commands, 3 MCP servers. KMP coverage ən geniş. v1.1.0 (Mar 28, 2026), 47⭐, 24 commits. **Reject reason**: Solo dev + 4 ay MVP timeline-da `feature-build --platform=kmp` end-to-end orchestration overkill; `liquid-glass-guide` (iOS 26), `m3-expressive-guide` irrelevant. Lakin: KMP-spesifik bir skill çatışmazlığı halında yenidən baxılmalı.
- **Confidence**: Yüksək.

### Source 6 — dpconde/claude-android-skill (REJECTED — Android-only)
- **URL**: https://github.com/dpconde/claude-android-skill
- **Takeaway**: NowInAndroid pattern, Hilt + Retrofit + Room + Compose, **Android-only, KMP yox**, WIP (2 commit). 218⭐. **Reject reason**: stack mismatch (Hilt KMP-də işləmir; biz Koin istifadə edirik).

### Source 7 — Drjacky/claude-android-ninja (REJECTED — Android-only)
- **URL**: https://github.com/Drjacky/claude-android-ninja
- **Takeaway**: Android + Compose + Nav 3 strong, 52⭐, 477 commit master. **Reject reason**: KMP yox, Hilt/Room/Retrofit lock-in.

### Source 8 — Kotzilla MCP Server (DEFERRED — Faza 2)
- **URL**: https://blog.kotzilla.io/kotzilla-mcp-server-for-kmp
- **Setup docs**: https://doc.kotzilla.io/docs/discover/mcpServer
- **Takeaway**: Production runtime introspection MCP. Exposes Koin dependency graph + crash reports + perf metrics + blocking call detection. Free to try. KMP-first-class (Android/iOS/Desktop/JS/WASM). Setup: "one prompt" registers app + generates config + updates Gradle plugin + version catalog + Koin monitoring call. **Defer reason**: yalnız production data ilə dəyər yaradır; MVP pre-launch faydasız. **Faza 2-yə qaytarmaq**: launch-dan 2 həftə əvvəl.

### Source 9 — rnett/gradle-mcp (PRIMARY MCP)
- **URL**: https://github.com/rnett/gradle-mcp
- **Takeaway**: v0.0.11 (Mar 28, 2026), 48⭐, Apache-2.0. Tools: project introspection, multi-project map, task exec with progress, test filtering + failure diagnostics, dependency search, source browse, **Interactive Kotlin REPL**, **Compose UI render**, Build Scans (Develocity). **JDK 25 tələbi**. JBang install. Replaces IlyaGulya/gradle-mcp-server (archived Mar 14, 2026).
- **Confidence**: Yüksək.

### Source 10 — IlyaGulya/gradle-mcp-server (DEPRECATED)
- **URL**: https://github.com/IlyaGulya/gradle-mcp-server
- **Takeaway**: **ARCHIVED 2026-03-14** → repo özü `rnett/gradle-mcp`-ə yönləndirir. v0.1.1 (Jul 2025), 44⭐. 3 tools (get_gradle_project_info, execute_gradle_task, run_gradle_tests). **Reject reason**: dead.

### Source 11 — getsentry/XcodeBuildMCP (PRIMARY iOS MCP)
- **URL**: https://github.com/getsentry/XcodeBuildMCP
- **Marketing**: https://www.xcodebuildmcp.com/
- **Takeaway**: Active, 82 MCP tools (builds, tests, simulators, real devices, LLDB debugging, UI automation, project scaffolding). xcodebuild CLI driver — no running Xcode required. Structured JSON errors (line/column/file). Tələb: Xcode 16+, macOS 14.5+. **KMP iOS XCFramework integration üçün açıq lazımdır**.
- **Confidence**: Yüksək.

### Source 12 — JetBrains MCP / Android Studio built-in MCP (USE)
- **URLs**:
  - https://www.jetbrains.com/help/idea/mcp-server.html
  - https://code.claude.com/docs/en/jetbrains
- **Takeaway**: IntelliJ 2025.2+ (Android Studio Koala Feature Drop+) built-in MCP server. mcp-jetbrains repo standalone artıq lazım deyil. Claude Code JetBrains plugin (id 27310) marketplace-də. Features: `Cmd+Esc` quick launch, `Cmd+Option+K` file reference inserter, IDE diagnostic auto-share, terminal `/ide` bridge, diff in IDE viewer.
- **Confidence**: Yüksək (rəsmi Anthropic docs).

### Source 13 — upstash/context7 (DOC LOADER)
- **URL**: https://github.com/upstash/context7
- **Takeaway**: Real-time docs fetcher MCP. Mənbə: kitabxanaların rəsmi sənədləri (Kotlin, KMP, CMP, Supabase, Ktor). Server URL: `https://mcp.context7.com/mcp`. Self-host alternativi var. **Bizim üçün kritik**: CMP 1.11.0 docs (May 2026) Claude training cutoff (yan 2026) sonrasıdır → bu MCP olmazsa API hallucinasiya qaçılmazdır.
- **Confidence**: Yüksək.

### Source 14 — normaltusker/kotlin-mcp-server (REJECTED — Aider-coupled)
- **URL**: https://github.com/normaltusker/kotlin-mcp-server
- **Takeaway**: Android + Aider + Gradle build/test + Kotlin LSP + Docker. **Reject reason**: Aider-mərkəzli, Claude-Code-native deyil; Kotlin LSP cəlbedicidir amma rnett/gradle-mcp + JetBrains MCP onsuz da bunu örtür.

### Source 15 — VoltAgent kotlin-specialist sub-agent (USE)
- **URL**: https://github.com/VoltAgent/awesome-claude-code-subagents/blob/main/categories/02-language-specialists/kotlin-specialist.md
- **Takeaway**: 20k⭐ repo, kotlin-specialist agent KMP-aware ("Multiplatform strategies": Common code maximization, expect/actual, platform APIs, cross-platform testing). Coroutines + Ktor + KMP coverage. Standalone `.md` file → `~/.claude/agents/`.
- **Confidence**: Yüksək.

### Source 16 — rohitg00 kotlin-specialist (ALTERNATIVE agent)
- **URL**: https://github.com/rohitg00/awesome-claude-code-toolkit/blob/main/agents/language-experts/kotlin-specialist.md
- **Takeaway**: KMP-aware (commonMain/androidMain/iosMain, Ktor Client, SQLDelight, kotlinx.serialization). **Reject reason**: VoltAgent versiyası daha geniş "Multiplatform strategies" bölməsi ilə.

### Source 17 — comshot (DEFERRED — visual feedback loop)
- **URL**: https://klibs.io/project/mahozad/comshot
- **Takeaway**: KMP library, Composable → PNG render (Android/JVM/Native/Wasm/JS). Potensial Claude workflow: gradle task ilə Composable render → png Read tool ilə "oxu" → Claude composable-ı bir-bir vizual yoxlayar. **Defer reason**: pre-MVP test stratejisi yoxdur; setup overhead solo dev üçün yüksək.

### Source 18 — MCP loading & sub-agent boundary (2026-05-19 live test)
- **Takeaway 1**: MCP tool schema-ları yalnız Claude Code session başlanğıcında yüklənir; yeni MCP server əlavə edildikdə restart məcburidir, runtime hot-reload yoxdur.
- **Takeaway 2**: Sub-agentlər (Agent / Explore tools) skill description-larını görür, **MCP tool-larını görmür** — Context7/gradle/xcodebuild çağırışları yalnız main session-da işləyir. KMP doc fetch tələb edən iş sub-agentə delege edilməməlidir.
- **Takeaway 3**: Context7 çağırış token cost (live ölçüldü): resolve ~600 + query ~3.5K = **~4K total** — əvvəlcədən proqnozlaşdırılan 3-5K range-i təsdiqləndi.
- **Takeaway 4**: Default Compose Multiplatform query-ləri Desktop (`singleWindowApplication`) nümunələrinə skew olur; iOS interop üçün query-də "iOS", "UIViewController", "SwiftUI interop" sözləri açıq yazılmalıdır.

---

## Errors Encountered

| Error | Attempts | Resolution |
|-------|----------|------------|
| mcpmarket.com 429 Too Many Requests | 1 | A7/A8 detalları yalnız ümumi opisanie ilə qiymətləndirildi (closed source onsuz da rədd edildi) |

---

## Progress Log

- 2026-05-17 21:32 — Research initialized
- 2026-05-17 — Goal + Why It Matters yazıldı, sub-question break-down tamamlandı
- 2026-05-17 — 4 paralel WebSearch + 4 paralel WebFetch ilə 8 alət ilkin qiymətləndirildi
- 2026-05-17 — 3 əlavə WebSearch + 2 WebFetch ilə MCP server + Gradle + iOS tooling tamamlandı
- 2026-05-17 — Inventory cədvəli (A/B/C/D/E) hazırlandı, fitnessApp stack-uyğun scoring tamamlandı, 12 qərar yazıldı, install bundle təsvir edildi
- 2026-05-17 — **Phase 3 hazır — user təsdiqi gözlənir**: real install icra ediləcəkmi yoxsa sadəcə tövsiyə qalsın?
- 2026-05-19 — Stop hook wrap-up `y` ilə təsdiqləndi: 12 Decision + 9 Finding (artıq session zamanı yazılıb) lock-in qəbul edildi; duplikasiya yox, yalnız Progress Log mühr.
- 2026-05-19 — **Verification agent ikinci rəy verdi** (general-purpose, paralel web research). 3 düzəliş: (1) ahmed3elshaer/everything-claude-code-mobile-i ADD et (rejected idi — KMP-spesifik skill-ləri kritikdir); (2) Context7 install-də yeni `ctx7 setup` path; (3) XcodeBuildMCP üçün brew tövsiyə. JDK 25 blocker deyil.
- 2026-05-19 — **INSTALL TAMAMLANDI ✅**:
  - **Skills (16 fayl):** compose-skill (Meet-Miyani) + 15 KMP-spesifik (expect-actual, kmp-di, kmp-navigation, kmp-networking, kmp-repositories, koin-patterns, ktor-patterns, shared-coroutines, shared-models, sqldelight-patterns, mvi-architecture, gradle-patterns, offline-first, mobile-security, coroutines-patterns) — hamısı Skill listəsində aktiv
  - **Agents (9 fayl):** kmp-architect, gradle-expert, mobile-architect, android-build-resolver, xcode-build-resolver, shared-model-designer, architecture-impl, network-impl, data-impl
  - **MCP servers (3, hamısı ✓ Connected):** context7 (npx -y @upstash/context7-mcp), gradle (jbang gradle-mcp@rnett, JDK 25 ilə), xcodebuild (/opt/homebrew/bin/xcodebuildmcp mcp)
  - **Sistem alətləri:** JDK 25.0.2 (openjdk@25 brew formula, keg-only), jbang 0.138.0 (~/.jbang/bin/), xcodebuildmcp (homebrew tap + formula)
  - **Konfiqurasiya yeri:** ~/.claude.json (project-scoped local config); .mcp.json yox (claude mcp add CLI istifadə etdi)
- 2026-05-19 — **Faza 4 (install) bitdi, Faza 5 (verification) aktiv**: CLAUDE.md-də "KMM" → "KMP" terminologiya yenilənməsi qalır; ilk real iş cəhdində skill auto-trigger doğrulanmalı.
- 2026-05-19 — **CLAUDE.md KMM → KMP replace tamamlandı** (5 yer): line 5 (Proje Kimliği), line 32 (Context7 MCP qaydası), line 59 (Tech Stack cədvəli — `Kotlin Multiplatform (KMP)` + rebrand qeydi + CMP 1.11.0), line 73 (### KMP Strukturu), line 157 (KMP shared module).
- 2026-05-19 — **Context7 API key əlavə olundu** (`ctx7sk-1034b8ad-9967-4328-accd-6edd95472a99`) — daha böyük rate limit. claude mcp remove + re-add with `-e CONTEXT7_API_KEY=...` ilə yenidən qoşuldu. Status: ✓ Connected.
- 2026-05-19 — **2 paralel test agent (general-purpose) göndərildi** — empirik nəticələr:
  - **Test 1 (skill introspection):** Sub-agent **16 KMP skill description-larını TAM görür** — auto-trigger keyword match işləyir. Token cost baseline: ~5-10K hər session başlanğıcında (description listing).
  - **Test 2 (Context7 fetch):** Sub-agent **MCP tool-larını GÖRMÜR** — context7, gradle, xcodebuild sub-agent context-də deferred tool kimi gəlmir. Bu Claude Code memarlığının xüsusiyyətidir, bug yox.
- 2026-05-22 — Paused on 2026-05-22 (user yeni feature-ə keçir: app feature-list PRD + UX hazırlığı)

## ARXİTEKTURA QEYD-İ (gələcək kontekst üçün vacib)

| Yer | Skill listing | MCP tools |
|-----|---------------|-----------|
| **Main Claude Code session** (`claude` CLI) | ✅ visible | ✅ visible (restart sonra) |
| **Sub-agent** (Agent tool çağırışı, general-purpose və ya Explore) | ✅ visible | ❌ **NOT visible** |

**Implikasiya:** KMP kod işləri üçün main session-da qal. Sub-agent yalnız research/exploration üçün uyğundur — KMP-spesifik build/docs MCP-lərinə ehtiyacı olan iş üçün yox.

## TOKEN COST PROFILE (empirik)

| Yük | Token | Tezlik |
|-----|-------|--------|
| Skill description listinq (83 skill: 67 bmad + 16 KMP) | ~5-10K | Hər session başlanğıcı (one-time) |
| MCP tool descriptions (3 yeni MCP) | <500 | Hər session başlanğıcı (one-time) |
| Skill body inject (auto-trigger) | 1-3K per skill | Yalnız keyword match |
| Reference fayl oxunması | 500-2K per fayl | On-demand |
| Context7 docs fetch (per call) | 1-10K | Yalnız user/Claude tələbi ilə |

`compose-skill` **explicit-only** trigger ("compose-skill" / "@compose-skill" yazılmasa açılmır) — qalan 15 KMP skill keyword-based auto-trigger. `skillListingBudgetFraction=0.03` (.claude/settings.json) → 200K context × 3% = 6K skill listing budget → uyğunlaşır.

- 2026-05-19 — **Restart sonrası live MCP test uğurlu**: `mcp__context7__resolve-library-id` + `query-docs` çağırışı `/jetbrains/compose-multiplatform/v1.11.0-alpha02` tag-ından real kod nümunələri qaytardı. Token ölçüldü: ~4K total. Sub-agent boundary təsdiqləndi (MCP yalnız main session).

## CARİ STATUS (2026-05-19, session sonu)

| Komponent | Status |
|-----------|--------|
| 16 KMP skill | ✅ Installed + visible (main + sub-agent) |
| 9 KMP agent | ✅ Installed in `.claude/agents/` |
| Context7 MCP + API key | ✅ Connected (CLI level), tool schema cari session-da yüklənməyib — restart lazım |
| gradle MCP (jbang + JDK 25) | ✅ Connected (CLI level), tool schema cari session-da yüklənməyib — restart lazım |
| xcodebuild MCP | ✅ Connected (CLI level), tool schema cari session-da yüklənməyib — restart lazım |
| CLAUDE.md terminology (KMM → KMP) | ✅ 5 yer yeniləndi, grep təmizdir |
| JDK 25.0.2 (openjdk@25 brew formula) | ✅ Installed at `/opt/homebrew/opt/openjdk/` |
| jbang 0.138.0 | ✅ Installed at `~/.jbang/bin/jbang` |
| xcodebuildmcp binary | ✅ Installed at `/opt/homebrew/bin/xcodebuildmcp` |
| Android Studio plugin (id 27310) | ⏳ User-side install (UI marketplace) — qalır |

## Notes for Next Session

### İlk addım — session restart sonra MCP test
1. `/quit` + `claude` yenidən başlat → MCP tool-lar (`mcp__context7__*`, `mcp__gradle__*`, `mcp__xcodebuild__*`) cari session-a yüklənəcək
2. Smoke test prompt-u:
   > "Context7 MCP istifadə edərək Compose Multiplatform 1.11 və Supabase Kotlin SDK-nın son API docs-larını çək. resolve-library-id sonra get-library-docs."
3. Gözlənilən: Claude `mcp__context7__resolve-library-id` çağırır → library ID alır → `mcp__context7__get-library-docs` çağırır → real 2026 may docs gəlir
4. Eyni şəkildə gradle-mcp üçün test: "gradle-mcp istifadə edərək bu fitnessApp gradle struktur info-su al" (project yoxdur amma `get_gradle_project_info` tool-u görünməlidir)
5. xcodebuild-mcp üçün test: "xcodebuildmcp tools list" → 82 tool gözlənilir

### İkinci addım — Android Studio plugin (UI)
- Settings → Plugins → Marketplace → "Claude Code" (id 27310) → Install → Restart
- Sonra terminal-də `claude` → `/ide` → bağlantı yaranır → diff viewer, file refs (Cmd+Option+K), diagnostics shared

### Üçüncü addım — feature-end qərarı
- Bu feature.md research-research kimi tamamlanmış sayıla bilər
- /feature-end ilə archive et VƏ YA real KMP kod yazmağa başlayanda burada qal və faktiki istifadə qeydlərini əlavə et

### Risk və qeyd-lər (gələcəyə)
- Faza 2-də (launch-dan 2 həftə əvvəl) Kotzilla MCP yenidən nəzərdən keçirilməli (production runtime introspection)
- Skill auto-trigger keyword-ləri bəzi description-larda "Android" deyir, amma daxilində KMP-aware (məs. `koin-patterns`, `ktor-patterns`, `mvi-architecture`) — narahat olma
- `compose-skill` explicit-only — istifadə etmək üçün "compose-skill istifadə et" və ya "@compose-skill" yaz
- gradle-mcp ilk çağırış 30-60 saniyə çəkə bilər (jbang ilk-dəfə dep download); sonra cache-lənir
