package org.betech.fitnes.presentation.onboarding.pregnancynudge

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.components.DisclaimerKind
import org.betech.fitnes.designsystem.components.VoltButton
import org.betech.fitnes.designsystem.components.VoltButtonGhost
import org.betech.fitnes.designsystem.components.VoltDisclaimer
import org.betech.fitnes.designsystem.typography.VoltType
import org.betech.fitnes.localization.LocalStrings
import org.betech.fitnes.localization.Strings
import org.betech.fitnes.presentation.onboarding.pregnancyconfirm.PregnancyConfirmScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 20 · Pregnancy Nudge (Pencil M52XdD · "Hamiləlik Nudge").
 *
 * Data-collection nudge — NOT an AI plan trigger. Asks the user whether
 * pregnancy / postpartum applies so the next step (PregnancyConfirmScreen,
 * Pencil pqupj) can branch into the safe-template flow. Copy avoids medical
 * claims; "tövsiyə olunur" (recommended) not "tələb olunur" (required).
 *
 * Layout (centered): shield/safety icon → title → subtitle → 3 benefit rows
 * → INFO privacy notice → primary "Bəli, bildir" → ghost "İndi yox".
 */
class PregnancyNudgeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: PregnancyNudgeViewModel = koinViewModel()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                PregnancyNudgeSideEffect.NavigateBack -> {
                    if (!navigator.pop()) navigator.pop()
                }
                PregnancyNudgeSideEffect.NavigateToPregnancyConfirm ->
                    navigator.push(PregnancyConfirmScreen())
            }
        }

        PregnancyNudgeContent(
            strings = strings,
            onBack = { viewModel.onIntent(PregnancyNudgeIntent.BackTapped) },
            onYes = { viewModel.onIntent(PregnancyNudgeIntent.YesTapped) },
            onNo = { viewModel.onIntent(PregnancyNudgeIntent.NoTapped) },
        )
    }
}

@Composable
private fun PregnancyNudgeContent(
    strings: Strings,
    onBack: () -> Unit,
    onYes: () -> Unit,
    onNo: () -> Unit,
) {
    val benefits = listOf(
        strings.pregnancyNudgeBenefit1,
        strings.pregnancyNudgeBenefit2,
        strings.pregnancyNudgeBenefit3,
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

            // ── Scrollable hero + body (centered) ─────────────────────
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(24.dp))

                ShieldCheckTile()

                Spacer(Modifier.height(24.dp))

                Text(
                    text = strings.pregnancyNudgeTitle,
                    style = VoltType.displayMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                    ),
                    color = VoltColors.onSurface,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = strings.pregnancyNudgeSubtitle,
                    style = VoltType.bodyMedium.copy(fontSize = 14.sp),
                    color = VoltColors.onSurfaceMuted,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(28.dp))

                // Benefits — left-aligned within centered max-width column.
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    benefits.forEach { line -> BenefitRow(text = line) }
                }

                Spacer(Modifier.height(20.dp))

                VoltDisclaimer(
                    text = strings.pregnancyNudgePrivacy,
                    kind = DisclaimerKind.INFO,
                )

                Spacer(Modifier.height(24.dp))
            }

            // ── CTA stack (bottom anchored) ───────────────────────────
            VoltButton(
                text = strings.pregnancyNudgeCtaYes,
                onClick = onYes,
            )
            Spacer(Modifier.height(12.dp))
            VoltButtonGhost(
                text = strings.pregnancyNudgeCtaNo,
                onClick = onNo,
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

// ──────────────────────────────────────────────────────────────────────
// Shield-check tile (64dp surface1 circle, volt-tinted shield + check)
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun ShieldCheckTile() {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(32.dp)) { drawShieldCheck() }
    }
}

private fun DrawScope.drawShieldCheck() {
    val w = size.width
    val h = size.height
    val stroke = w / 10f
    val volt = VoltColors.volt

    // Shield outline — classic crest shape.
    val shield = Path().apply {
        moveTo(w * 0.5f, h * 0.05f)
        lineTo(w * 0.92f, h * 0.22f)
        lineTo(w * 0.92f, h * 0.55f)
        // Curved bottom (rounded crest tip).
        quadraticBezierTo(w * 0.92f, h * 0.88f, w * 0.5f, h * 0.98f)
        quadraticBezierTo(w * 0.08f, h * 0.88f, w * 0.08f, h * 0.55f)
        lineTo(w * 0.08f, h * 0.22f)
        close()
    }
    drawPath(
        path = shield,
        color = volt,
        style = Stroke(width = stroke, cap = StrokeCap.Round),
    )

    // Inner check mark.
    drawLine(
        color = volt,
        start = Offset(w * 0.30f, h * 0.50f),
        end = Offset(w * 0.46f, h * 0.66f),
        strokeWidth = stroke,
        cap = StrokeCap.Round,
    )
    drawLine(
        color = volt,
        start = Offset(w * 0.46f, h * 0.66f),
        end = Offset(w * 0.72f, h * 0.38f),
        strokeWidth = stroke,
        cap = StrokeCap.Round,
    )
}

// ──────────────────────────────────────────────────────────────────────
// Benefit row — 20dp volt-fill circle + onVolt check + text
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun BenefitRow(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(VoltColors.volt),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.size(10.dp)) { drawCheck() }
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
    val sw = size.minDimension / 5f
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
// Back chevron — shared atom (Profile Summary / AI Disclosure pattern)
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
