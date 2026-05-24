package org.betech.fitnes.presentation.onboarding.languageselect

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.components.DisclaimerKind
import org.betech.fitnes.designsystem.components.VoltButton
import org.betech.fitnes.designsystem.components.VoltDisclaimer
import org.betech.fitnes.designsystem.typography.VoltType
import org.betech.fitnes.localization.LocalStrings
import org.betech.fitnes.localization.Strings
import org.betech.fitnes.presentation.onboarding.welcome.WelcomeScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 0.2 · Language Select (Pencil X2Pu6z · "01 · Dil Seçimi").
 *
 * Surface-0 background, 28dp side gutters. Three stacked option cards (AZ default
 * selected). Selected card: surface-1 fill + 2dp `volt` stroke + trailing check.
 * Unselected: surface-1 fill + 1dp `outline` stroke. Native-translation notice
 * sits below the list, "Davam et" primary CTA anchored to the bottom.
 *
 * The marketing line in [Strings.langSelectNativeNotice] is the moat copy
 * (BetterMe is machine-translated AZ) — DO NOT machine-translate.
 */
class LanguageSelectScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: LanguageSelectViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                LanguageSelectSideEffect.NavigateToWelcome -> navigator.replace(WelcomeScreen())
                is LanguageSelectSideEffect.ShowError -> Unit // TODO: surface as toast/snackbar
            }
        }

        LanguageSelectContent(
            state = state,
            strings = strings,
            onSelect = { viewModel.onIntent(LanguageSelectIntent.Select(it)) },
            onConfirm = { viewModel.onIntent(LanguageSelectIntent.Confirm) },
        )
    }
}

@Composable
private fun LanguageSelectContent(
    state: LanguageSelectState,
    strings: Strings,
    onSelect: (LanguageCode) -> Unit,
    onConfirm: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VoltColors.surface0)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
        ) {
            // Top bar — tiny brand mark in volt, left-aligned. (Status glyphs from
            // Pencil are mockups; real Android status bar already paints them.)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(63.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BrandBadge()
            }

            // Title
            Text(
                text = strings.langSelectTitle,
                style = VoltType.displayMedium.copy(fontWeight = FontWeight.Bold),
                color = VoltColors.onSurface,
            )

            Spacer(Modifier.height(24.dp))

            // Option list
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                LanguageOptionRow(
                    label = strings.langOptionAz,
                    selected = state.selected == LanguageCode.AZ,
                    onClick = { onSelect(LanguageCode.AZ) },
                )
                LanguageOptionRow(
                    label = strings.langOptionRu,
                    selected = state.selected == LanguageCode.RU,
                    onClick = { onSelect(LanguageCode.RU) },
                )
                LanguageOptionRow(
                    label = strings.langOptionEn,
                    selected = state.selected == LanguageCode.EN,
                    onClick = { onSelect(LanguageCode.EN) },
                )
            }

            Spacer(Modifier.height(32.dp))

            // Native-translation moat notice
            VoltDisclaimer(
                text = strings.langSelectNativeNotice,
                kind = DisclaimerKind.INFO,
            )
        }

        // Bottom CTA — anchored, full-width primary
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 24.dp),
        ) {
            VoltButton(
                text = strings.continueAction,
                onClick = onConfirm,
                enabled = !state.isSaving,
            )
        }
    }
}

@Composable
private fun BrandBadge() {
    Box(
        modifier = Modifier
            .size(width = 64.dp, height = 30.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(VoltColors.volt),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "FitLab",
            style = VoltType.labelSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
            ),
            color = VoltColors.onVolt,
        )
    }
}

@Composable
private fun LanguageOptionRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(20.dp)
    val strokeColor = if (selected) VoltColors.volt else VoltColors.outline
    val strokeWidth: Dp = if (selected) 2.dp else 1.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(77.dp)
            .clip(shape)
            .background(VoltColors.surface1)
            .border(width = strokeWidth, color = strokeColor, shape = shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        GlobeIcon(size = 44.dp, color = if (selected) VoltColors.volt else VoltColors.onSurfaceMuted)
        Spacer(Modifier.width(16.dp))
        Text(
            text = label,
            style = VoltType.titleMedium,
            color = VoltColors.onSurface,
        )
        Spacer(Modifier.weight(1f))
        if (selected) {
            CheckIcon(size = 24.dp, color = VoltColors.volt)
        }
    }
}

/** Minimalist globe — circle + meridian + equator. No raster, no extra dep. */
@Composable
private fun GlobeIcon(size: Dp, color: Color) {
    Canvas(modifier = Modifier.size(size)) {
        val sizePx = this.size.minDimension
        val strokePx = 1.8.dp.toPx()
        val r = sizePx / 2f - strokePx
        val cx = sizePx / 2f
        val cy = sizePx / 2f

        drawCircle(
            color = color,
            radius = r,
            center = Offset(cx, cy),
            style = Stroke(width = strokePx),
        )
        // Equator
        drawLine(
            color = color,
            start = Offset(cx - r, cy),
            end = Offset(cx + r, cy),
            strokeWidth = strokePx,
        )
        // Vertical meridian
        drawLine(
            color = color,
            start = Offset(cx, cy - r),
            end = Offset(cx, cy + r),
            strokeWidth = strokePx,
        )
        // Inner ellipse approximation — two half-arcs for a meridian curve
        val ellipseRx = r * 0.55f
        val path = Path().apply {
            moveTo(cx, cy - r)
            cubicTo(
                cx - ellipseRx, cy - r * 0.4f,
                cx - ellipseRx, cy + r * 0.4f,
                cx, cy + r,
            )
            moveTo(cx, cy - r)
            cubicTo(
                cx + ellipseRx, cy - r * 0.4f,
                cx + ellipseRx, cy + r * 0.4f,
                cx, cy + r,
            )
        }
        drawPath(path = path, color = color, style = Stroke(width = strokePx))
    }
}

/** Bold checkmark — two-stroke polyline. */
@Composable
private fun CheckIcon(size: Dp, color: Color) {
    Canvas(modifier = Modifier.size(size)) {
        val sizePx = this.size.minDimension
        val strokePx = 2.5.dp.toPx()
        val path = Path().apply {
            moveTo(sizePx * 0.20f, sizePx * 0.55f)
            lineTo(sizePx * 0.42f, sizePx * 0.75f)
            lineTo(sizePx * 0.80f, sizePx * 0.28f)
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = strokePx, cap = StrokeCap.Round),
        )
    }
}
