package org.betech.fitnes.presentation.onboarding.splash

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.localization.LocalStrings
import org.betech.fitnes.presentation.onboarding.languageselect.LanguageSelectScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 0.1 · Splash — 1:1 rebuild of Pencil node `s7yM8w`.
 *
 * Structure mirrors the source frame:
 *   s7yM8w (375×812, volt fill, vertical layout, justify=space_between, pad=0,0,56,0)
 *   ├─ OYDpM             — 1-px spacer (top)
 *   ├─ xnx77   (gap 22)  — vertical brand stack
 *   │   ├─ iA4jF (96×96) — brand-mark wrapper
 *   │   │   └─ kdikq (96×96, layout:none) — logo-icon
 *   │   │       ├─ nUOsf  POLYGON 96×96, fill volt, stroke onVolt 2.5, r=8 (hex outline)
 *   │   │       └─ I3o0VU PATH at (21,21), 54×54, viewBox 24×24, fill onVolt (dumbbell)
 *   │   └─ rM7GW (row)   — wordmark "Fit" + "Lab", fontSize 44, weight 800, ls -1.2
 *   └─ h1uDeB (row, gap 10) — tagline dot + "Elmə əsaslı tam fitness" + dot
 */
class SplashScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: SplashViewModel = koinViewModel()
        val state by viewModel.collectAsState()

        viewModel.collectSideEffect { effect ->
            when (effect) {
                SplashSideEffect.NavigateToLanguageSelect ->
                    navigator.replace(LanguageSelectScreen())
            }
        }

        LaunchedEffect(Unit) { viewModel.onIntent(SplashIntent.Start) }

        SplashContent(state)
    }
}

@Composable
private fun SplashContent(@Suppress("UNUSED_PARAMETER") state: SplashState) {
    val strings = LocalStrings.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VoltColors.volt)
            .padding(bottom = 56.dp), // matches Pencil padding [0,0,56,0]
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        // OYDpM — 1-px top spacer that anchors the space-between layout.
        Spacer(Modifier.height(1.dp).fillMaxWidth())

        // xnx77 — brand stack (logo + wordmark), gap 22.
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(22.dp),
        ) {
            SplashLogo(size = 96.dp)
            WordmarkFitLab()
        }

        // h1uDeB — tagline row, gap 10.
        TaglineRow(text = strings.splashTagline)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// SplashLogo — kdikq replica: polygon outline + dumbbell glyph overlay
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SplashLogo(size: Dp) {
    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center,
    ) {
        // nUOsf — 96×96 rounded hexagon, fill volt, stroke onVolt 2.5px, cornerRadius 8
        RoundedHexagon(
            size = size,
            fillColor = VoltColors.volt,
            strokeColor = VoltColors.onVolt,
            strokeWidth = 2.5.dp,
            cornerRadius = 8.dp,
        )
        // I3o0VU — dumbbell SVG path, 54×54 (viewBox 24×24), fill onVolt.
        // Pencil places this at (21,21) inside the 96×96 frame → naturally centred
        // because (96 - 54) / 2 == 21, so Box(center) reproduces it.
        DumbbellGlyph(
            size = 54.dp,
            color = VoltColors.onVolt,
        )
    }
}

/**
 * 6-sided regular polygon with rounded corners — pointy-top orientation matches
 * the Pencil source. Corner radius is approximated via quadratic bezier joins
 * (the standard rounded-polygon technique; visually indistinguishable from
 * Pencil's `cornerRadius:8` on a 96-px hex).
 */
@Composable
private fun RoundedHexagon(
    size: Dp,
    fillColor: Color,
    strokeColor: Color,
    strokeWidth: Dp,
    cornerRadius: Dp,
) {
    Canvas(modifier = Modifier.size(size)) {
        val sizePx = this.size.minDimension
        val strokePx = strokeWidth.toPx()
        val radiusPx = cornerRadius.toPx()
        val cx = sizePx / 2f
        val cy = sizePx / 2f
        // Inset so the stroke does not get clipped at the bounding box.
        val r = (sizePx / 2f) - (strokePx / 2f)

        // Compute 6 vertices, pointy-top (start at -90°, step 60°).
        val verts = Array(6) { i ->
            val angle = ((60.0 * i - 90.0) * kotlin.math.PI / 180.0).toFloat()
            Offset(cx + r * cos(angle), cy + r * sin(angle))
        }

        val path = Path().apply {
            for (i in 0..5) {
                val prev = verts[(i + 5) % 6]
                val curr = verts[i]
                val next = verts[(i + 1) % 6]

                val inDir = (curr - prev).normalised()
                val outDir = (next - curr).normalised()

                val start = curr - inDir * radiusPx
                val end = curr + outDir * radiusPx

                if (i == 0) moveTo(start.x, start.y) else lineTo(start.x, start.y)
                quadraticBezierTo(curr.x, curr.y, end.x, end.y)
            }
            close()
        }

        drawPath(path = path, color = fillColor)
        drawPath(path = path, color = strokeColor, style = Stroke(width = strokePx))
    }
}

private operator fun Offset.minus(other: Offset) = Offset(x - other.x, y - other.y)
private operator fun Offset.times(scalar: Float) = Offset(x * scalar, y * scalar)
private fun Offset.normalised(): Offset {
    val len = sqrt(x * x + y * y)
    return if (len == 0f) this else Offset(x / len, y / len)
}

/**
 * Dumbbell glyph — exact reproduction of Pencil path I3o0VU.
 *
 * Original viewBox 0,0,24,24; drawn as 5 axis-aligned rectangles:
 *   left  weight cap   x=2,    y=8,  w=2.5, h=8
 *   left  weight body  x=5.5,  y=5,  w=3.5, h=14
 *   bar               x=9,    y=11, w=6,   h=2
 *   right weight body  x=15,   y=5,  w=3.5, h=14
 *   right weight cap   x=19.5, y=8,  w=2.5, h=8
 *
 * Scaling: 54-px target ÷ 24 viewBox = 2.25× per logical unit.
 */
@Composable
private fun DumbbellGlyph(size: Dp, color: Color) {
    Canvas(modifier = Modifier.size(size)) {
        val s = this.size.minDimension / 24f // scale: viewBox unit → px

        fun rect(x: Float, y: Float, w: Float, h: Float) {
            drawRect(
                color = color,
                topLeft = Offset(x * s, y * s),
                size = Size(w * s, h * s),
            )
        }

        rect(2.0f, 8.0f, 2.5f, 8.0f)    // left cap
        rect(5.5f, 5.0f, 3.5f, 14.0f)   // left body
        rect(9.0f, 11.0f, 6.0f, 2.0f)   // bar
        rect(15.0f, 5.0f, 3.5f, 14.0f)  // right body
        rect(19.5f, 8.0f, 2.5f, 8.0f)   // right cap
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Wordmark — rM7GW: "Fit" + "Lab" side by side (no gap), Inter 800 / 44sp / -1.2
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun WordmarkFitLab() {
    val wordmarkStyle = TextStyle(
        fontSize = 44.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = (-1.2).sp,
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Fit", style = wordmarkStyle, color = VoltColors.onVolt)
        Text(text = "Lab", style = wordmarkStyle, color = VoltColors.onVolt)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Tagline — h1uDeB: 6dp rounded square · text · 6dp rounded square
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun TaglineRow(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        TaglineDot()
        Text(
            text = text,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp,
            ),
            color = VoltColors.onVolt,
        )
        TaglineDot()
    }
}

@Composable
private fun TaglineDot() {
    Box(
        modifier = Modifier
            .size(6.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(VoltColors.onVolt),
    )
}
