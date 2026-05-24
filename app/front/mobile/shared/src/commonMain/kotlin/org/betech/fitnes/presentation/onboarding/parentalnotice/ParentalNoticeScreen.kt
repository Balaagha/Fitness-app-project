package org.betech.fitnes.presentation.onboarding.parentalnotice

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import org.betech.fitnes.presentation.onboarding.parentalbottomsheet.ParentalBottomSheetScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 07 · Parental Notice (Pencil XG54w · "Validəynin xəbəri varmı?").
 *
 * Under-18 guardian consent gate. Shown after Q3 age when birth year
 * places the user under 18 — Apple/Google health-app policy requirement.
 *
 * Layout: back chevron + eyebrow → title → volt-info disclaimer card →
 * volt checkbox row (gate) → two surface1 chip-buttons (Privacy / Terms)
 * → primary CTA (enabled only when consent ticked) → ghost back.
 *
 * Chips push [ParentalBottomSheetScreen] — same sheet doubles as Terms
 * placeholder until the dedicated Terms copy lands in Phase 2.
 */
class ParentalNoticeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: ParentalNoticeViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                ParentalNoticeSideEffect.NavigateBack -> navigator.pop()
                ParentalNoticeSideEffect.NavigateForward -> navigator.pop()
                ParentalNoticeSideEffect.OpenPrivacySheet ->
                    navigator.push(ParentalBottomSheetScreen())
                ParentalNoticeSideEffect.OpenTermsSheet ->
                    navigator.push(ParentalBottomSheetScreen())
            }
        }

        ParentalNoticeContent(
            state = state,
            strings = strings,
            onBack = { viewModel.onIntent(ParentalNoticeIntent.BackTapped) },
            onConsentToggle = { viewModel.onIntent(ParentalNoticeIntent.ConsentToggled(it)) },
            onPrivacy = { viewModel.onIntent(ParentalNoticeIntent.PrivacyChipTapped) },
            onTerms = { viewModel.onIntent(ParentalNoticeIntent.TermsChipTapped) },
            onContinue = { viewModel.onIntent(ParentalNoticeIntent.ContinueTapped) },
        )
    }
}

@Composable
private fun ParentalNoticeContent(
    state: ParentalNoticeState,
    strings: Strings,
    onBack: () -> Unit,
    onConsentToggle: (Boolean) -> Unit,
    onPrivacy: () -> Unit,
    onTerms: () -> Unit,
    onContinue: () -> Unit,
) {
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
            // ── Top bar (back chevron) ────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BackChevronButton(onClick = onBack)
            }

            Spacer(Modifier.height(16.dp))

            // ── Scrollable body ───────────────────────────────────────
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
            ) {
                Text(
                    text = strings.parentalNoticeEyebrow,
                    style = VoltType.labelLarge.copy(
                        fontSize = 12.sp,
                        letterSpacing = 1.4.sp,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = VoltColors.volt,
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = strings.parentalNoticeTitle,
                    style = VoltType.displayMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                    ),
                    color = VoltColors.onSurface,
                )

                Spacer(Modifier.height(20.dp))

                VoltInfoDisclaimer(text = strings.parentalNoticeDisclaimer)

                Spacer(Modifier.height(20.dp))

                ConsentRow(
                    label = strings.parentalNoticeConsentLabel,
                    checked = state.consentChecked,
                    onCheckedChange = onConsentToggle,
                )

                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    ChipButton(
                        text = strings.parentalNoticeChipPrivacy,
                        onClick = onPrivacy,
                        modifier = Modifier.weight(1f),
                    )
                    ChipButton(
                        text = strings.parentalNoticeChipTerms,
                        onClick = onTerms,
                        modifier = Modifier.weight(1f),
                    )
                }

                Spacer(Modifier.height(24.dp))
            }

            // ── CTA stack ─────────────────────────────────────────────
            VoltButton(
                text = strings.parentalNoticeCtaContinue,
                onClick = onContinue,
                enabled = state.consentChecked && !state.isSubmitting,
            )
            Spacer(Modifier.height(12.dp))
            VoltButtonGhost(
                text = strings.parentalNoticeCtaBack,
                onClick = onBack,
                enabled = !state.isSubmitting,
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

// ──────────────────────────────────────────────────────────────────────
// Volt-info disclaimer — surface1 card with volt left bar + onSurface body
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun VoltInfoDisclaimer(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, RoundedCornerShape(16.dp)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(VoltColors.volt),
            )
            Text(
                text = text,
                style = VoltType.bodyMedium.copy(fontSize = 13.sp, lineHeight = 18.sp),
                color = VoltColors.onSurface,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

// ──────────────────────────────────────────────────────────────────────
// Consent row — 24dp square volt checkbox + label
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun ConsentRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(if (checked) VoltColors.volt else VoltColors.surface1)
                .border(
                    width = 1.dp,
                    color = if (checked) VoltColors.volt else VoltColors.outlineStrong,
                    shape = RoundedCornerShape(6.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (checked) {
                Canvas(modifier = Modifier.size(14.dp)) { drawCheck() }
            }
        }
        Text(
            text = label,
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
// Chip button — surface1 pill, onSurface text
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun ChipButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = VoltType.labelLarge.copy(fontSize = 13.sp, fontWeight = FontWeight.Medium),
            color = VoltColors.onSurface,
        )
    }
}

// ──────────────────────────────────────────────────────────────────────
// Back chevron — shared atom (mirrors AI Disclosure pattern)
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
