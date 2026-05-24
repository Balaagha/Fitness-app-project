package org.betech.fitnes.presentation.onboarding.welcome

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.delay
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.components.HexagonLogo
import org.betech.fitnes.designsystem.typography.VoltType
import org.betech.fitnes.localization.LocalStrings
import org.betech.fitnes.localization.Strings
import org.betech.fitnes.presentation.onboarding.languageselect.LanguageSelectScreen
import org.betech.fitnes.presentation.onboarding.login.LoginScreen
import org.betech.fitnes.presentation.onboarding.q1goal.Q1GoalScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 0.3 · Welcome — 5-variant rotating surface (Pencil BPoym / tneyd / dRTLR / gjmPD / OKg7W).
 *
 * Same layout, swapped copy + hero glyph + active chip. Timer rotates every 2s;
 * dev deep-link can jump to a specific variant via `initialVariant`.
 */
class WelcomeScreen(
    private val initialVariant: WelcomeVariant? = null,
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: WelcomeViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                WelcomeSideEffect.NavigateToQ1Goal -> navigator.push(Q1GoalScreen())
                WelcomeSideEffect.NavigateToLogin -> navigator.push(LoginScreen())
                WelcomeSideEffect.NavigateToLanguageSelect ->
                    navigator.replace(LanguageSelectScreen())
            }
        }

        // Optional dev deep-link jump — runs once.
        LaunchedEffect(initialVariant) {
            initialVariant?.let { viewModel.onIntent(WelcomeIntent.JumpTo(it)) }
        }

        LaunchedEffect(Unit) {
            while (true) {
                delay(2_000L)
                viewModel.onIntent(WelcomeIntent.Rotate)
            }
        }

        WelcomeContent(
            state = state,
            strings = strings,
            onStart = { viewModel.onIntent(WelcomeIntent.StartTapped) },
            onHaveAccount = { viewModel.onIntent(WelcomeIntent.HaveAccountTapped) },
            onChangeLanguage = { viewModel.onIntent(WelcomeIntent.ChangeLanguageTapped) },
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Variant copy mapping
// ─────────────────────────────────────────────────────────────────────────────

private data class VariantCopy(val titleLine1: String, val titleLine2: String?, val subtitle: String)

/**
 * Splits a single-line variant title into 2 lines around the middle space —
 * preserves the visual weight of the original 2-line WORKOUT_PLAN headline.
 */
private fun splitTitle(title: String): Pair<String, String?> {
    val words = title.split(' ')
    if (words.size < 2) return title to null
    val mid = words.size / 2
    val line1 = words.take(mid).joinToString(" ")
    val line2 = words.drop(mid).joinToString(" ")
    return line1 to line2
}

private fun copyFor(variant: WelcomeVariant, strings: Strings): VariantCopy = when (variant) {
    WelcomeVariant.WORKOUT_PLAN ->
        VariantCopy(strings.welcomeTitleLine1, strings.welcomeTitleLine2, strings.welcomeSubtitle)
    WelcomeVariant.SOFT_CONTROL -> {
        val (l1, l2) = splitTitle(strings.welcomeV1Title)
        VariantCopy(l1, l2, strings.welcomeV1Subtitle)
    }
    WelcomeVariant.FOOD -> {
        val (l1, l2) = splitTitle(strings.welcomeV2Title)
        VariantCopy(l1, l2, strings.welcomeV2Subtitle)
    }
    WelcomeVariant.ENERGY -> {
        val (l1, l2) = splitTitle(strings.welcomeV3Title)
        VariantCopy(l1, l2, strings.welcomeV3Subtitle)
    }
    WelcomeVariant.GOAL -> {
        val (l1, l2) = splitTitle(strings.welcomeV4Title)
        VariantCopy(l1, l2, strings.welcomeV4Subtitle)
    }
}

@Composable
private fun WelcomeContent(
    state: WelcomeState,
    strings: Strings,
    onStart: () -> Unit,
    onHaveAccount: () -> Unit,
    onChangeLanguage: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VoltColors.surface0),
    ) {
        // Decorative radial volt glow behind the hexagon (low alpha, top-third).
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 120.dp)
                .size(360.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            VoltColors.volt.copy(alpha = 0.18f),
                            VoltColors.volt.copy(alpha = 0.06f),
                            Color.Transparent,
                        ),
                    ),
                ),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = 28.dp),
        ) {
            // ── Top bar ────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp)
                    .height(40.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BrandPill()
                Spacer(Modifier.weight(1f))
                LanguagePill(label = strings.languagePillLabel, onClick = onChangeLanguage)
            }

            Spacer(Modifier.height(20.dp))

            // ── Hero hexagon with halo — glyph depends on variant ─────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center,
            ) {
                // Halo ring — 1dp outline offset outside the hex.
                HexagonLogo(
                    size = 192.dp,
                    color = VoltColors.outline.copy(alpha = 0.5f),
                    strokeDp = 1.dp,
                )
                HeroForVariant(variant = state.activeVariant)
            }

            Spacer(Modifier.height(20.dp))

            // ── Headline block ────────────────────────────────────────
            HeadlineBlock(
                strings = strings,
                copy = copyFor(state.activeVariant, strings),
            )

            Spacer(Modifier.height(20.dp))

            // ── Rotating eyebrow chips ────────────────────────────────
            EyebrowChipsRow(strings = strings, activeIndex = state.activeVariant.chipIndex)

            Spacer(Modifier.height(20.dp))

            // ── Category pills (4 in a row) ───────────────────────────
            CategoryPillsRow(strings = strings)

            Spacer(Modifier.weight(1f))

            // ── CTAs ──────────────────────────────────────────────────
            PrimaryStartButton(text = strings.welcomeCtaStart, onClick = onStart)
            Spacer(Modifier.height(10.dp))
            GhostHaveAccountButton(text = strings.welcomeCtaHaveAccount, onClick = onHaveAccount)
            Spacer(Modifier.height(20.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Hero glyphs per variant — all volt-tinted, drawn inside a 168dp hex.
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun HeroForVariant(variant: WelcomeVariant) {
    when (variant) {
        WelcomeVariant.WORKOUT_PLAN, WelcomeVariant.SOFT_CONTROL -> {
            HexagonLogo(
                size = 168.dp,
                color = VoltColors.onVolt,
                fillColor = VoltColors.volt,
                strokeDp = 0.dp,
            )
        }
        WelcomeVariant.FOOD -> HexFilledWithGlyph { drawAppleGlyph() }
        WelcomeVariant.ENERGY -> HexFilledWithGlyph { drawLightningGlyph() }
        WelcomeVariant.GOAL -> HexFilledWithGlyph { drawBullseyeGlyph() }
    }
}

/**
 * Volt-filled hex (matches WORKOUT_PLAN visual weight) with a custom glyph
 * drawn on top in `onVolt`. Avoids re-implementing the hex path here.
 */
@Composable
private fun HexFilledWithGlyph(drawGlyph: androidx.compose.ui.graphics.drawscope.DrawScope.() -> Unit) {
    Box(contentAlignment = Alignment.Center) {
        HexagonLogoBlank(size = 168.dp, fillColor = VoltColors.volt)
        Canvas(modifier = Modifier.size(168.dp)) { drawGlyph() }
    }
}

/**
 * Hexagon filled with [fillColor], no internal H-glyph — overlay-friendly base.
 */
@Composable
private fun HexagonLogoBlank(size: androidx.compose.ui.unit.Dp, fillColor: Color) {
    Canvas(modifier = Modifier.size(size)) {
        val sizePx = this.size.minDimension
        val cx = sizePx / 2f
        val cy = sizePx / 2f
        val r = sizePx / 2f - 4.dp.toPx()
        val path = Path().apply {
            for (i in 0..5) {
                val angle = ((60.0 * i - 90.0) * kotlin.math.PI / 180.0).toFloat()
                val x = cx + r * kotlin.math.cos(angle)
                val y = cy + r * kotlin.math.sin(angle)
                if (i == 0) moveTo(x, y) else lineTo(x, y)
            }
            close()
        }
        drawPath(path = path, color = fillColor)
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawAppleGlyph() {
    val cx = size.minDimension / 2f
    val cy = size.minDimension / 2f
    val r = size.minDimension * 0.22f
    val onVolt = VoltColors.onVolt
    // Apple body (circle).
    drawCircle(color = onVolt, radius = r, center = Offset(cx, cy + r * 0.15f))
    // Notch on top — small circle in fill color to suggest indent.
    drawCircle(
        color = VoltColors.volt,
        radius = r * 0.28f,
        center = Offset(cx, cy - r * 0.7f),
    )
    // Stem.
    drawRect(
        color = onVolt,
        topLeft = Offset(cx - 2.dp.toPx(), cy - r * 0.95f),
        size = Size(4.dp.toPx(), r * 0.35f),
    )
    // Leaf — small ellipse offset right of stem.
    val leafPath = Path().apply {
        moveTo(cx + 2.dp.toPx(), cy - r * 0.85f)
        quadraticBezierTo(
            cx + r * 0.6f, cy - r * 1.05f,
            cx + r * 0.5f, cy - r * 0.55f,
        )
        quadraticBezierTo(
            cx + r * 0.15f, cy - r * 0.7f,
            cx + 2.dp.toPx(), cy - r * 0.85f,
        )
        close()
    }
    drawPath(leafPath, color = onVolt)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawLightningGlyph() {
    val cx = size.minDimension / 2f
    val cy = size.minDimension / 2f
    val h = size.minDimension * 0.55f
    val w = size.minDimension * 0.28f
    val onVolt = VoltColors.onVolt
    val path = Path().apply {
        moveTo(cx + w * 0.1f, cy - h / 2f)         // top-right
        lineTo(cx - w * 0.6f, cy + h * 0.05f)      // mid-left
        lineTo(cx - w * 0.05f, cy + h * 0.05f)     // notch in
        lineTo(cx - w * 0.25f, cy + h / 2f)        // bottom point
        lineTo(cx + w * 0.6f, cy - h * 0.1f)       // mid-right
        lineTo(cx + w * 0.05f, cy - h * 0.1f)      // notch back
        close()
    }
    drawPath(path, color = onVolt)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawBullseyeGlyph() {
    val cx = size.minDimension / 2f
    val cy = size.minDimension / 2f
    val r = size.minDimension * 0.30f
    val onVolt = VoltColors.onVolt
    val volt = VoltColors.volt
    val stroke = Stroke(width = 3.dp.toPx())
    drawCircle(color = onVolt, radius = r, center = Offset(cx, cy), style = stroke)
    drawCircle(color = onVolt, radius = r * 0.66f, center = Offset(cx, cy), style = stroke)
    drawCircle(color = onVolt, radius = r * 0.32f, center = Offset(cx, cy))
    // Tiny center void for crosshair feel.
    drawCircle(color = volt, radius = r * 0.10f, center = Offset(cx, cy))
}

// ─────────────────────────────────────────────────────────────────────────────
// Top bar pieces
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun BrandPill() {
    Box(
        modifier = Modifier
            .height(34.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(VoltColors.volt)
            .padding(horizontal = 18.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "FitLab",
            style = VoltType.labelLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
            ),
            color = VoltColors.onVolt,
        )
    }
}

@Composable
private fun LanguagePill(label: String, onClick: () -> Unit) {
    val shape = RoundedCornerShape(17.dp)
    Row(
        modifier = Modifier
            .height(34.dp)
            .clip(shape)
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(text = "🌐", style = VoltType.labelSmall, color = VoltColors.onSurface)
        Text(
            text = label,
            style = VoltType.labelLarge.copy(fontWeight = FontWeight.SemiBold),
            color = VoltColors.onSurface,
        )
        Text(text = "▾", style = VoltType.labelSmall, color = VoltColors.onSurfaceMuted)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Headline block — copy comes from the variant mapping.
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun HeadlineBlock(strings: Strings, copy: VariantCopy) {
    // Variant titles can be quite long; scale headline down a notch for non-base variants.
    val isCompact = (copy.titleLine1.length + (copy.titleLine2?.length ?: 0)) > 14
    val headlineSize = if (isCompact) 32.sp else 46.sp
    val headlineLine = if (isCompact) 32.sp else 46.sp
    val cursorH = if (isCompact) 28.dp else 40.dp

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Eyebrow SALAM
        Text(
            text = strings.welcomeGreeting,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                letterSpacing = 3.sp,
            ),
            color = VoltColors.onSurfaceMuted,
        )

        Spacer(Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = copy.titleLine1,
                style = TextStyle(
                    fontWeight = FontWeight.Black,
                    fontSize = headlineSize,
                    lineHeight = headlineLine,
                ),
                color = VoltColors.onSurface,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .height(cursorH)
                    .width(4.dp)
                    .background(VoltColors.volt),
            )
        }
        if (copy.titleLine2 != null) {
            Text(
                text = copy.titleLine2,
                style = TextStyle(
                    fontWeight = FontWeight.Black,
                    fontSize = headlineSize,
                    lineHeight = headlineLine,
                ),
                color = VoltColors.onSurface,
                textAlign = TextAlign.Center,
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = copy.subtitle,
            style = VoltType.bodyMedium,
            color = VoltColors.onSurfaceMuted,
            textAlign = TextAlign.Center,
            maxLines = 3,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Rotating eyebrow chip row
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun EyebrowChipsRow(strings: Strings, activeIndex: Int) {
    val labels = listOf(
        strings.welcomeChipWorkout,
        strings.welcomeChipFood,
        strings.welcomeChipEnergy,
        strings.welcomeChipGoal,
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        labels.forEachIndexed { i, label ->
            val isActive = i == activeIndex
            Text(
                text = label,
                style = VoltType.bodyMedium.copy(
                    fontSize = 13.sp,
                    fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                ),
                color = if (isActive) VoltColors.volt else VoltColors.onSurfaceMuted,
            )
            if (i != labels.lastIndex) {
                Text(
                    text = " · ",
                    style = VoltType.bodyMedium.copy(fontSize = 13.sp),
                    color = VoltColors.outlineStrong,
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Category pills row (4 equal-width)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun CategoryPillsRow(strings: Strings) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CategoryPill(emoji = "💪", label = strings.welcomePillWorkout, modifier = Modifier.weight(1f))
        CategoryPill(emoji = "🥗", label = strings.welcomePillFood, modifier = Modifier.weight(1f))
        CategoryPill(emoji = "⚡", label = strings.welcomePillEnergy, modifier = Modifier.weight(1f))
        CategoryPill(emoji = "🎯", label = strings.welcomePillGoal, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun CategoryPill(emoji: String, label: String, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(50)
    Row(
        modifier = modifier
            .height(36.dp)
            .clip(shape)
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, shape)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = emoji, style = VoltType.labelSmall.copy(fontSize = 13.sp))
        Spacer(Modifier.width(4.dp))
        Text(
            text = label,
            style = VoltType.labelSmall.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
            color = VoltColors.onSurface,
            maxLines = 1,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// CTA buttons
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun PrimaryStartButton(text: String, onClick: () -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(59.dp)
            .clip(shape)
            .background(VoltColors.volt)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = VoltType.labelLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
            ),
            color = VoltColors.onVolt,
        )
    }
}

@Composable
private fun GhostHaveAccountButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = VoltType.labelLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
            ),
            color = VoltColors.volt,
        )
    }
}
