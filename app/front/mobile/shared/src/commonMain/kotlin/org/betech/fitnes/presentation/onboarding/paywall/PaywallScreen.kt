package org.betech.fitnes.presentation.onboarding.paywall

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
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
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
import org.betech.fitnes.designsystem.components.DisclaimerKind
import org.betech.fitnes.designsystem.components.VoltButton
import org.betech.fitnes.designsystem.components.VoltDisclaimer
import org.betech.fitnes.designsystem.typography.VoltType
import org.betech.fitnes.localization.LocalStrings
import org.betech.fitnes.localization.Strings
import org.betech.fitnes.presentation.onboarding.home.HomeScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 22 · Paywall (Pencil ij7jR · "Daha dərinə getməyə hazırsan?").
 *
 * Two-plan paywall — 7-day trial + annual (default-selected) — with transparent
 * billing notice and payment-method badges. Hard-stop rules (CLAUDE.md):
 *  - MUST show 2 options at all times (transparent-billing trust moat).
 *  - MUST surface the in-app cancel copy near the CTA.
 *  - MUST NOT advertise "trainer / coach" — only "uyğunluq yoxlaması".
 *
 * Layout doctrine:
 *  - LazyColumn (content overflows on small phones).
 *  - 24dp horizontal gutter, surface0 background + subtle volt radial glow.
 *  - Close X (top-right) skips premium → Home stub.
 */
class PaywallScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: PaywallViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current
        val snackbarHost = remember { SnackbarHostState() }

        LaunchedEffect(Unit) { viewModel.onIntent(PaywallIntent.Shown) }

        viewModel.collectSideEffect { effect ->
            when (effect) {
                PaywallSideEffect.NavigateToHome ->
                    navigator.replaceAll(HomeScreen())
                PaywallSideEffect.ShowRestoreToast ->
                    snackbarHost.showSnackbar(strings.paywallRestoreToast)
                PaywallSideEffect.ShowTermsToast ->
                    snackbarHost.showSnackbar(strings.paywallTermsToast)
            }
        }

        PaywallContent(
            state = state,
            strings = strings,
            snackbarHost = snackbarHost,
            onClose = { viewModel.onIntent(PaywallIntent.CloseTapped) },
            onSelect = { viewModel.onIntent(PaywallIntent.SelectPlan(it)) },
            onContinue = { viewModel.onIntent(PaywallIntent.ContinueTapped) },
            onRestore = { viewModel.onIntent(PaywallIntent.RestoreTapped) },
            onTerms = { viewModel.onIntent(PaywallIntent.TermsTapped) },
        )
    }
}

@Composable
private fun PaywallContent(
    state: PaywallState,
    strings: Strings,
    snackbarHost: SnackbarHostState,
    onClose: () -> Unit,
    onSelect: (PaywallPlan) -> Unit,
    onContinue: () -> Unit,
    onRestore: () -> Unit,
    onTerms: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VoltColors.surface0),
    ) {
        // Subtle volt radial glow at top — matches Pencil design.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            VoltColors.volt.copy(alpha = 0.10f),
                            VoltColors.surface0.copy(alpha = 0f),
                        ),
                        radius = 700f,
                    )
                ),
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
        ) {
            // ── Top bar: close X (right only) ──────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                ) {
                    CloseXButton(onClick = onClose)
                }
            }

            // ── Eyebrow + Title ────────────────────────────────────────
            item {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = strings.paywallEyebrow,
                    style = VoltType.labelSmall.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                    ),
                    color = VoltColors.volt,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = strings.paywallTitle,
                    style = VoltType.displayMedium.copy(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = VoltColors.onSurface,
                )
                Spacer(Modifier.height(24.dp))
            }

            // ── Feature checklist (5 rows) ─────────────────────────────
            item {
                val features = listOf(
                    strings.paywallFeature1,
                    strings.paywallFeature2,
                    strings.paywallFeature3,
                    strings.paywallFeature4,
                    strings.paywallFeature5,
                )
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    features.forEach { FeatureRow(text = it) }
                }
                Spacer(Modifier.height(24.dp))
            }

            // ── Plan cards (trial + annual default-selected) ──────────
            item {
                PlanCard(
                    title = strings.paywallTrialTitle,
                    subtitle = strings.paywallTrialSubtitle,
                    selected = state.selected == PaywallPlan.TRIAL,
                    badge = null,
                    onClick = { onSelect(PaywallPlan.TRIAL) },
                )
                Spacer(Modifier.height(12.dp))
                PlanCard(
                    title = strings.paywallAnnualTitle,
                    subtitle = strings.paywallAnnualSubtitle,
                    selected = state.selected == PaywallPlan.ANNUAL,
                    badge = strings.paywallRecommendedBadge,
                    onClick = { onSelect(PaywallPlan.ANNUAL) },
                )
                Spacer(Modifier.height(16.dp))
            }

            // ── Transparent-billing notice (CLAUDE.md rule) ───────────
            item {
                VoltDisclaimer(
                    text = strings.paywallTransparencyNotice,
                    kind = DisclaimerKind.INFO,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = strings.paywallSmallPrint,
                    style = VoltType.labelSmall.copy(fontSize = 11.sp),
                    color = VoltColors.onSurfaceMuted,
                )
                Spacer(Modifier.height(16.dp))
            }

            // ── Primary CTA ────────────────────────────────────────────
            item {
                VoltButton(
                    text = strings.paywallCta,
                    onClick = onContinue,
                    enabled = !state.isPurchasing,
                )
                Spacer(Modifier.height(8.dp))
            }

            // ── Footer links: Restore · Terms ──────────────────────────
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = strings.paywallRestore,
                        style = VoltType.labelLarge.copy(fontSize = 13.sp),
                        color = VoltColors.onSurfaceMuted,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(onClick = onRestore)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                    )
                    Text(
                        text = "·",
                        style = VoltType.labelLarge,
                        color = VoltColors.onSurfaceMuted,
                    )
                    Text(
                        text = strings.paywallTerms,
                        style = VoltType.labelLarge.copy(fontSize = 13.sp),
                        color = VoltColors.onSurfaceMuted,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(onClick = onTerms)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                    )
                }
                Spacer(Modifier.height(12.dp))
            }

            // ── Payment method badges (Apple · Google · m10) ──────────
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    PaymentPill(strings.paywallPaymentApple, Modifier.weight(1f))
                    PaymentPill(strings.paywallPaymentGoogle, Modifier.weight(1f))
                    PaymentPill(strings.paywallPaymentLocal, Modifier.weight(1f))
                }
                Spacer(Modifier.height(24.dp))
            }
        }

        SnackbarHost(
            hostState = snackbarHost,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
        )
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// Atoms
// ──────────────────────────────────────────────────────────────────────────────

@Composable
private fun FeatureRow(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
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
    val sw = size.minDimension / 6f
    drawLine(
        color = VoltColors.onVolt,
        start = Offset(size.width * 0.15f, size.height * 0.55f),
        end = Offset(size.width * 0.42f, size.height * 0.80f),
        strokeWidth = sw,
        cap = StrokeCap.Round,
    )
    drawLine(
        color = VoltColors.onVolt,
        start = Offset(size.width * 0.42f, size.height * 0.80f),
        end = Offset(size.width * 0.88f, size.height * 0.20f),
        strokeWidth = sw,
        cap = StrokeCap.Round,
    )
}

@Composable
private fun PlanCard(
    title: String,
    subtitle: String,
    selected: Boolean,
    badge: String?,
    onClick: () -> Unit,
) {
    val borderWidth = if (selected) 2.dp else 1.dp
    val borderColor = if (selected) VoltColors.volt else VoltColors.outline
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(VoltColors.surface1)
            .border(borderWidth, borderColor, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 18.dp),
    ) {
        Column {
            Text(
                text = title,
                style = VoltType.titleMedium.copy(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
                color = VoltColors.onSurface,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = VoltType.bodyMedium.copy(fontSize = 13.sp),
                color = VoltColors.onSurfaceMuted,
            )
        }
        if (badge != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clip(RoundedCornerShape(8.dp))
                    .background(VoltColors.volt)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    text = badge,
                    style = VoltType.labelSmall.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                    ),
                    color = VoltColors.onVolt,
                )
            }
        }
    }
}

@Composable
private fun PaymentPill(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(34.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = VoltType.labelSmall.copy(fontSize = 11.sp),
            color = VoltColors.onSurface,
            textAlign = TextAlign.Center,
        )
    }
}

/** 36dp circular close button with a vector-drawn X. */
@Composable
private fun CloseXButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(12.dp)) { drawCloseX() }
    }
}

private fun DrawScope.drawCloseX() {
    val sw = size.minDimension / 8f
    drawLine(
        color = VoltColors.onSurface,
        start = Offset(0f, 0f),
        end = Offset(size.width, size.height),
        strokeWidth = sw,
        cap = StrokeCap.Round,
    )
    drawLine(
        color = VoltColors.onSurface,
        start = Offset(size.width, 0f),
        end = Offset(0f, size.height),
        strokeWidth = sw,
        cap = StrokeCap.Round,
    )
}
