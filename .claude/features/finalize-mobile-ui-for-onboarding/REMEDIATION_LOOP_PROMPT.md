## REMEDIATION LOOP — 1:1 Pencil Fidelity Rebuild

`claude --dangerously-skip-permissions --model claude-opus-4-7[1m]` ilə daxil olduqdan sonra yapışdır.

---

```
/loop

Sən mövcud Compose ekranlarını **1:1 Pencil fidelity**-yə gətirən REMEDIATION loop işlədirsən. İlk loop ekranları yaratdı amma low-fidelity (Canvas approximation, layered children atlanmış, SVG path-lar uydurulmuş). İndi hər ekran üçün YENİDƏN Pencil-i tam-dərinlikdə oxuyub, MÖVCUD KODU SİL VƏ TAM ƏVƏZ ET — 1:1 reproduksiya kimi.

═══════════════════════════════════════════════════════════════
## CONSTANTS
═══════════════════════════════════════════════════════════════

- **Gradle root:** /Users/balaaghaalihumatov/Desktop/fitnessApp/app/front/mobile
- **Package:** org.betech.fitnes
- **Design:** /Users/balaaghaalihumatov/Desktop/fitnessApp/app/design/mobile/app_design.pen (yalnız Pencil MCP)
- **Feature:** finalize-mobile-ui-for-onboarding
- **Notes:** .claude/features/finalize-mobile-ui-for-onboarding/notes.md
- **Screenshots:** .claude/features/finalize-mobile-ui-for-onboarding/screenshots/ (köhnə şəkillər `_v1` suffix-i ilə yenidən adlandır, yeniləri orijinal adla qoy)
- **Android AVD:** Pixel_Fold_API_35
- **Display ID (HWC):** 4619827259835644672 (screencap -d FLAG MƏCBURİ)
- **Reference fidelity exemplar:** SplashScreen.kt (s7yM8w üçün 1:1 təlimə uyğun — bu pattern-i digər ekranlara şamil et)

═══════════════════════════════════════════════════════════════
## ƏSAS QAYDA (DƏYIŞMƏZ)
═══════════════════════════════════════════════════════════════

Hər Pencil node = Compose element. **Approximation YOX, invention YOX.**
- `polygon` node → custom Canvas Path (rounded-corner polygon math — sample SplashScreen.kt RoundedHexagon)
- `path` node → Canvas-da SVG path port (exact viewBox scaling, hər `M/h/v/m/z` komandası → Compose drawRect/Path operation)
- `image`/`icon_font` → `mcp__pencil__export_nodes` ilə asset ixrac et, `composeResources/drawable/`-yə yaz, `painterResource` ilə render et
- `frame` layout:none (x/y absolute) → `Box(contentAlignment=...)` + child-larda Modifier.offset (və ya intrinsic center hesablanır)
- `frame` layout:vertical/horizontal → `Column`/`Row` + `Arrangement.spacedBy(gap)`
- `text` → `Text` ekzakt fontSize, fontWeight, letterSpacing, fill color token-ə map
- `fill: "#XXXX"` → `VoltColors.<token>`-ə map; əgər token yoxdursa, **TƏZƏ token əlavə et** VoltColors.kt-yə, hardcoded hex yazma
- `stroke: { fill, thickness }` → `Stroke(width=thickness.dp.toPx())` + Path
- Çoxqatlı child-lar (layout:none + x/y) → `Box(contentAlignment=Center)` istifadə et, child öz size-ı ilə center-ə düşür (SplashLogo nümunəsi)

**INVENTION QADAĞA**: Pencil source-da olmayan glyph, decoration, halo, gradient YAZMA. Köhnə kod-da uydurmalar varsa → SİL.

═══════════════════════════════════════════════════════════════
## MƏCBURİ PENCIL OXU PARAMETRLƏRİ
═══════════════════════════════════════════════════════════════

Hər ekran üçün **bu çağırış**:

```
mcp__pencil__batch_get(
  filePath="/Users/balaaghaalihumatov/Desktop/fitnessApp/app/design/mobile/app_design.pen",
  nodeIds=["<screen_id>"],
  readDepth=6,                  ← MƏCBURİ, default 1 NÖGSANDIR
  includePathGeometry=true,     ← SVG path geometry üçün
  resolveInstances=true,
  resolveVariables=true
)
```

Sonra `mcp__pencil__get_screenshot(nodeId="<screen_id>")` ilə referans şəkli al, side-by-side müqayisə üçün saxla.

Yaranan ekran üçün ek `mcp__pencil__snapshot_layout(parentId="<screen_id>", maxDepth=5)` çağır — overflow, clip problem-ləri yaranır mı yoxla.

═══════════════════════════════════════════════════════════════
## EKRAN INVENTARİ
═══════════════════════════════════════════════════════════════

İlkin sıra (kritiklik üzrə):

**FAZA A — Brand-critical (vacib):**
1. s7yM8w (splash) — ✅ ALREADY DONE 1:1 (SplashScreen.kt — örnək kimi referans et, dəyişmə)
2. tneyd (welcome-soft) — köhnə kod uydurma glyph/3-halo simplification var, TAM SİL VƏ YENİDƏN YAZ
3. BPoym (welcome-main) — eyni
4. dRTLR (welcome-qida), gjmPD (welcome-enerji), OKg7W (welcome-hedef) — variant-cycle restructure

**FAZA B — Form ekranları (Q1-Q7):**
5. S5QT23, ObxuP, owS2i, qXLw8, i1Vu9, F16e8, H0uZ0e — sual ekranları, layout reflow olub amma content stabil; mövcud koda yenidən bax və sadəcə divergens nöqtələrini düzəlt (TAM SİL ETMƏ)

**FAZA C — Auth:**
6. eQcvv, u1cEVR, K1n7u5, ZJFFO, O8lWVO, zREhj, rzAPa, vA9Tb

**FAZA D — Pregnancy flow:**
7. M52XdD, pqupj, C6Ya4A, o0BUd, QHsnW, YZ38M, vUAuh

**FAZA E — Paywall/Account:**
8. ij7jR, SCKUA, e74FR, GauGs

**FAZA F — Error ekranlar:**
9. u27ve, cYfk5, IFSQ3, K2TtZa, a3Vwh6, iNSs8

**FAZA G — Yeni əlavələr (gecə pen edit-indən):**
10. 02e edge states (Lang Picker Open, Static Fallback, Paused), 02f animation timing
11. Yeni Pregnancy 22-26 IDləri (`mcp__pencil__batch_get patterns=[{name:"22.*Trimester.*"}]` ilə tap)
12. POST-AUTH bridge cards (xref/annotation node-lar İGNORE ET — yalnız screen frame-ləri implement et)

═══════════════════════════════════════════════════════════════
## ITERASIYA AXINI (HƏR EKRAN ÜÇÜN)
═══════════════════════════════════════════════════════════════

1. **Köhnə skrinşotu arxiv et:** `mv screenshots/<NN>-<screen>.png screenshots/<NN>-<screen>_v1.png`

2. **Pencil tam-dərinlikdə oxu** (yuxarıdakı parametrlərlə)

3. **Referans şəkli al və yan-yana müqayisə üçün saxla:** `mcp__pencil__get_screenshot` → `screenshots/<NN>-<screen>_pencil.png`

4. **Mövcud Compose faylına bax** — uydurma element-lər varsa siyahıla, TAM YENİDƏN YAZ:
   - File yolu: `app/front/mobile/shared/src/commonMain/kotlin/org/betech/fitnes/presentation/onboarding/<screen>/<Screen>Screen.kt`
   - State/Intent/SideEffect/ViewModel TOXUNMA (məntiq düzdür) — yalnız `*Screen.kt` Composable hissəsi yenidən yazılır

5. **Tokens audit:** Pencil-də olan hər `fill: "#hex"`-i `VoltColors.kt`-də mövcud olan token ilə əvəz et. Yoxdursa, `VoltColors.kt`-yə yeni token əlavə et (`// hex from Pencil source <screen_id>`).

6. **Asset extraction:** image/icon_font/path node varsa:
   - SVG/PNG: `mcp__pencil__export_nodes(nodeIds=[...], format="svg")` → `composeResources/drawable/<screen>_<element>.svg`
   - Lucide icon: hələ lucide library əlavə olunmayıbsa, `:shared/build.gradle.kts`-ə `implementation("br.com.devsrsouza.compose.icons:lucide:1.1.1")` əlavə et, sonra `LucideIcons.<Name>` istifadə et

7. **Compose-da 1:1 yenidən qur** — SplashScreen.kt pattern-ini şablon kimi istifadə et:
   - Hər frame üçün ayrı `@Composable` (məs. `SplashLogo`, `WordmarkFitLab`, `TaglineRow` kimi semantically named)
   - Hardcoded modifier məcburi olduqda Pencil-də nə yazılıb onu yaz (məs. `Modifier.padding(bottom=56.dp)` çünki padding [0,0,56,0])
   - Heç bir uydurma decoration (gradient halo, extra glyph) əlavə etmə

8. **Build (Android+iOS):**
   ```bash
   cd /Users/balaaghaalihumatov/Desktop/fitnessApp/app/front/mobile
   ./gradlew :androidApp:assembleDebug :shared:linkDebugFrameworkIosSimulatorArm64
   ```
   İkisi də keçməlidir.

9. **Install + capture:** Display ID workaround məcburi:
   ```bash
   adb shell am force-stop org.betech.fitnes
   adb install -r androidApp/build/outputs/apk/debug/androidApp-debug.apk
   adb shell am start -n org.betech.fitnes/.MainActivity
   # Self-advancing splash üçün 1.2s gözlə; digər ekranlar üçün deep-link və ya manual nav
   sleep 1.2
   adb exec-out screencap -p -d 4619827259835644672 \
     > .claude/features/finalize-mobile-ui-for-onboarding/screenshots/<NN>-<screen>.png 2>/dev/null
   file .claude/features/finalize-mobile-ui-for-onboarding/screenshots/<NN>-<screen>.png  # PNG image data confirm
   ```

10. **Side-by-side müqayisə:** Read tool ilə həm `_pencil.png` həm də yeni skrinşotu aç, vizual müqayisə et. Divergens varsa:
    - **>30%** (struktur fərq): notes.md-yə `[gotcha] <screen> v2 STILL DIVERGENT — <konkret problem>` yaz, 3-strike-da SKIPPED qeyd et
    - **10-30%** (kiçik mismatch): iterate, eyni iterasiyada düzəlt
    - **<10%**: `[impl] <screen> 1:1 done — pencil-fidelity verified` notes-ə yaz

11. **ScheduleWakeup(60, prompt=<this loop input>, reason="next 1:1 rebuild")**

═══════════════════════════════════════════════════════════════
## STOP CONDITION
═══════════════════════════════════════════════════════════════

Bütün ekranlar üçün notes.md-də `[impl] <screen> 1:1 done` markeri olanda + son Android+iOS build keçəndə → **ScheduleWakeup ÇAĞIRMA**, loop bitir.

═══════════════════════════════════════════════════════════════
## INVARIANTS (POZULARSA SƏHV)
═══════════════════════════════════════════════════════════════

- ❌ `readDepth < 5` ilə Pencil oxuma
- ❌ `includePathGeometry=false` — SVG path itər
- ❌ Approximation (hexagon-u Material RoundedCornerShape ilə əvəz et; SVG path-ı emoji ilə əvəz et; halo-nu sadələşdir)
- ❌ Uydurma element (apple/lightning/bullseye glyph kimi, Pencil-də yoxdur)
- ❌ Hardcoded hex Color (yalnız VoltColors.* token)
- ❌ Emoji icon (🌐, ▾ kimi) — Lucide icon font məcburi
- ❌ screencap -d FLAG-i yoxdursa (multi-display setup-da PNG corrupt olur)
- ❌ Köhnə skrinşotu silmə — `_v1` suffix-i ilə arxiv et
- ❌ ViewModel/State/Intent toxun (məntiq düzdür — yalnız Composable yenidən yaz)
- ❌ "trainer/coach" sözü, AI duygusal kahraman copy, Volt üzərində ağ text — köhnə qadağalar qüvvədə
- ❌ Pregnancy ekranında AI plan trigger
- ❌ Loop-u build fail-də dayandırma — 3-strike protokoluna gir

═══════════════════════════════════════════════════════════════
## BAŞLA
═══════════════════════════════════════════════════════════════

FAZA A-dan başla. Splash (s7yM8w) artıq 1:1-dır — atla. Welcome-soft (tneyd)-dan başla.

Hər iterasiyanın sonunda ScheduleWakeup mütləqdir. Faza dəyişiklikləri pauza səbəbi deyil.
```

---

## İstifadə qaydası

1. Gecə üçün hazır olanda yenidən:
   ```bash
   caffeinate -dimsu &
   emulator -avd Pixel_Fold_API_35 -no-snapshot-save &
   cd /Users/balaaghaalihumatov/Desktop/fitnessApp
   git add -A && git commit -m "snapshot before remediation loop"
   claude --dangerously-skip-permissions --model claude-opus-4-7[1m]
   ```

2. İçəri girəndən sonra yuxarıdakı `/loop ... STOP` blokunu yapışdır.

3. Səhər: hər ekran üçün `<NN>-<screen>.png` + `<NN>-<screen>_pencil.png` yan-yana qovluqda hazır olacaq. Manual review asanlaşır.
