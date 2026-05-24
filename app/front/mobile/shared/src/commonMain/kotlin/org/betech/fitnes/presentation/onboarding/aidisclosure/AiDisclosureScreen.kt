package org.betech.fitnes.presentation.onboarding.aidisclosure

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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.components.VoltButton
import org.betech.fitnes.designsystem.components.VoltButtonGhost
import org.betech.fitnes.designsystem.typography.VoltType
import org.betech.fitnes.localization.LocalStrings
import org.betech.fitnes.localization.Strings
import org.betech.fitnes.presentation.onboarding.authgate.AuthGateScreen
import org.betech.fitnes.presentation.onboarding.profilesummary.ProfileSummaryScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 13 · AI Disclosure (Pencil eQcvv · "Sənin planın AI ilə qurulur").
 *
 * Apple 2025 mandatory disclosure. User-agency framed: 4 bullets centre
 * the user as decision-maker. "Mütəxəssis uyğunluq yoxlaması" is the
 * canonical AZ term for Phase-2 expert review — DO NOT swap for "trainer".
 *
 * Layout: hero sparkle tile → title → subtitle → 4 check rows → primary
 * confirm + ghost "Learn more". No progress bar (onboarding-question
 * phase is over). Snackbar hosts the learn-more placeholder toast.
 */
class AiDisclosureScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: AiDisclosureViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current
        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(Unit) { viewModel.onShown() }

        viewModel.collectSideEffect { effect ->
            when (effect) {
                AiDisclosureSideEffect.NavigateBack -> {
                    if (!navigator.pop()) navigator.replace(ProfileSummaryScreen())
                }
                AiDisclosureSideEffect.NavigateToAuthGate ->
                    navigator.push(AuthGateScreen())
                AiDisclosureSideEffect.ShowLearnMoreToast ->
                    snackbarHostState.showSnackbar(strings.aiDisclosureLearnMoreToast)
            }
        }

        AiDisclosureContent(
            state = state,
            strings = strings,
            snackbarHostState = snackbarHostState,
            onBack = { viewModel.onIntent(AiDisclosureIntent.BackTapped) },
            onConfirm = { viewModel.onIntent(AiDisclosureIntent.ConfirmTapped) },
            onLearnMore = { viewModel.onIntent(AiDisclosureIntent.LearnMoreTapped) },
        )
    }
}

@Composable
private fun AiDisclosureContent(
    state: AiDisclosureState,
    strings: Strings,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onConfirm: () -> Unit,
    onLearnMore: () -> Unit,
) {
    val bullets = listOf(
        strings.aiDisclosureBullet1,
        strings.aiDisclosureBullet2,
        strings.aiDisclosureBullet3,
        strings.aiDisclosureBullet4,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VoltColors.surface0),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = 24.dp),
        ) {
            // ── Top bar (back only) ───────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BackChevronButton(onClick = onBack)
            }

            Spacer(Modifier.height(16.dp))

            // ── Scrollable hero + body ────────────────────────────────
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
            ) {
                HeroSparkleTile()

                Spacer(Modifier.height(24.dp))

                Text(
                    text = strings.aiDisclosureTitle,
                    style = VoltType.displayMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                    ),
                    color = VoltColors.onSurface,
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = strings.aiDisclosureSubtitle,
                    style = VoltType.bodyMedium.copy(fontSize = 14.sp),
                    color = VoltColors.onSurfaceMuted,
                )

                Spacer(Modifier.height(24.dp))

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    bullets.forEach { line -> CheckRow(text = line) }
                }

                Spacer(Modifier.height(24.dp))
            }

            // ── CTA stack (bottom anchored) ───────────────────────────
            VoltButton(
                text = strings.aiDisclosureCtaConfirm,
                onClick = onConfirm,
                enabled = !state.isAcknowledging,
            )
            Spacer(Modifier.height(12.dp))
            VoltButtonGhost(
                text = strings.aiDisclosureCtaLearnMore,
                onClick = onLearnMore,
                enabled = !state.isAcknowledging,
            )

            Spacer(Modifier.height(24.dp))
        }

        // Snackbar overlay (bottom-aligned).
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 88.dp),
        )
    }
}

// ──────────────────────────────────────────────────────────────────────
// Hero sparkle tile (56dp volt-fill, onVolt 4-point star)
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun HeroSparkleTile() {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(VoltColors.volt),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(28.dp)) { drawSparkle() }
    }
}

private fun DrawScope.drawSparkle() {
    val color = VoltColors.onVolt
    val cx = size.width / 2f
    val cy = size.height / 2f
    val sw = size.minDimension / 6f
    // 4-point star — two crossing strokes (vertical + horizontal) tapered at ends.
    drawLine(
        color = color,
        start = Offset(cx, 0f),
        end = Offset(cx, size.height),
        strokeWidth = sw,
        cap = StrokeCap.Round,
    )
    drawLine(
        color = color,
        start = Offset(0f, cy),
        end = Offset(size.width, cy),
        strokeWidth = sw,
        cap = StrokeCap.Round,
    )
    // Two small accent dots (diagonal).
    val dot = size.minDimension / 10f
    drawCircle(
        color = color,
        radius = dot,
        center = Offset(size.width * 0.18f, size.height * 0.18f),
    )
    drawCircle(
        color = color,
        radius = dot,
        center = Offset(size.width * 0.82f, size.height * 0.82f),
    )
}

// ──────────────────────────────────────────────────────────────────────
// Check row — 24dp volt-fill circle with onVolt check + multi-line text
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun CheckRow(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(VoltColors.volt),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.size(12.dp)) { drawCheck() }
        }
        Text(
            text = text,
            style = VoltType.bodyMedium.copy(fontSize = 14.sp),
            color = VoltColors.onSurface,
            modifier = Modifier.weight(1f),
        )
    }
}

private fun DrawScope.drawCheck() {
    val color = VoltColors.onVolt
    val sw = size.minDimension / 6f
    drawLine(
        color = color,
        start = Offset(size.width * 0.15f, size.height * 0.55f),
        end = Offset(size.width * 0.42f, size.height * 0.80f),
        strokeWidth = sw,
        cap = StrokeCap.Round,
    )
    drawLine(
        color = color,
        start = Offset(size.width * 0.42f, size.height * 0.80f),
        end = Offset(size.width * 0.88f, size.height * 0.22f),
        strokeWidth = sw,
        cap = StrokeCap.Round,
    )
}

// ──────────────────────────────────────────────────────────────────────
// Back chevron — shared atom (Profile Summary pattern)
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun BackChevronButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(14.dp)) { drawBackChevron() }
    }
}

private fun DrawScope.drawBackChevron() {
    val strokeWidth = size.minDimension / 8f
    val midY = size.height / 2f
    val left = size.width * 0.25f
    val right = size.width * 0.75f
    drawLine(
        color = VoltColors.onSurface,
        start = Offset(right, 0f),
        end = Offset(left, midY),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
    )
    drawLine(
        color = VoltColors.onSurface,
        start = Offset(left, midY),
        end = Offset(right, size.height),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
    )
}
