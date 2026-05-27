package org.betech.fitnes.presentation.onboarding.ratelimit

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.typography.VoltType
import org.betech.fitnes.localization.LocalStrings
import org.betech.fitnes.localization.Strings
import org.betech.fitnes.presentation.onboarding.pwdresetemail.PwdResetEmailScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/** V3 · Rate Limit (Pencil IFSQ3). Centered modal sheet over scrim. */
class RateLimitScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: RateLimitViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                RateLimitSideEffect.NavigateToPwdReset -> navigator.push(PwdResetEmailScreen())
                RateLimitSideEffect.Dismiss -> { navigator.pop() }
            }
        }

        ModalContent(
            state = state,
            strings = strings,
            onReset = { viewModel.onIntent(RateLimitIntent.ResetTapped) },
            onDismiss = { viewModel.onIntent(RateLimitIntent.DismissTapped) },
        )
    }
}

@Composable
private fun ModalContent(
    state: RateLimitState,
    strings: Strings,
    onReset: () -> Unit,
    onDismiss: () -> Unit,
) {
    // ── Scrim ────────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VoltColors.scrim)
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center,
    ) {
        // ── Modal card ───────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth(0.86f)
                .clip(RoundedCornerShape(24.dp))
                .background(VoltColors.surface1)
                .border(1.dp, VoltColors.outline, RoundedCornerShape(24.dp))
                .clickable(enabled = false) {}
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Volt hourglass tile
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(VoltColors.volt),
                contentAlignment = Alignment.Center,
            ) {
                Canvas(modifier = Modifier.size(36.dp)) { drawHourglass(VoltColors.onVolt) }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = strings.rateLimitTitle,
                style = VoltType.displayMedium.copy(
                    fontWeight = FontWeight.Bold, fontSize = 22.sp,
                ),
                color = VoltColors.onSurface,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = strings.rateLimitBody,
                style = VoltType.bodyMedium.copy(fontSize = 14.sp),
                color = VoltColors.onSurfaceMuted,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(24.dp))

            // Primary volt CTA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(VoltColors.volt)
                    .clickable(onClick = onReset),
                contentAlignment = Alignment.Center,
            ) {
                Text(strings.rateLimitCtaReset, style = VoltType.labelLarge, color = VoltColors.onVolt)
            }

            Spacer(Modifier.height(12.dp))

            // Ghost CTA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable(onClick = onDismiss),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    strings.rateLimitCtaDismiss,
                    style = VoltType.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = VoltColors.onSurfaceMuted,
                )
            }

            Spacer(Modifier.height(8.dp))

            // Countdown footer
            Text(
                text = strings.rateLimitCountdown(state.minutesLeft, state.secondsLeft),
                style = VoltType.labelSmall.copy(fontSize = 12.sp),
                color = VoltColors.onSurfaceMuted,
            )
        }
    }
}

private fun DrawScope.drawHourglass(color: Color) {
    val w = size.width; val h = size.height
    val sw = w / 12f
    // top bar
    drawLine(color, Offset(w * 0.15f, 0f), Offset(w * 0.85f, 0f), sw, StrokeCap.Round)
    // bottom bar
    drawLine(color, Offset(w * 0.15f, h), Offset(w * 0.85f, h), sw, StrokeCap.Round)
    // sides — hourglass profile (two triangles meeting at center)
    drawLine(color, Offset(w * 0.15f, 0f), Offset(w / 2f, h / 2f), sw, StrokeCap.Round)
    drawLine(color, Offset(w * 0.85f, 0f), Offset(w / 2f, h / 2f), sw, StrokeCap.Round)
    drawLine(color, Offset(w / 2f, h / 2f), Offset(w * 0.15f, h), sw, StrokeCap.Round)
    drawLine(color, Offset(w / 2f, h / 2f), Offset(w * 0.85f, h), sw, StrokeCap.Round)
    // sand grain
    drawCircle(color, radius = sw * 0.8f, center = Offset(w / 2f, h * 0.55f))
}
