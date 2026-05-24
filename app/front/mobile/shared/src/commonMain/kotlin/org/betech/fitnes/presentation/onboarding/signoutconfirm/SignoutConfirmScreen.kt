package org.betech.fitnes.presentation.onboarding.signoutconfirm

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import org.betech.fitnes.designsystem.components.DisclaimerKind
import org.betech.fitnes.designsystem.components.VoltButtonGhost
import org.betech.fitnes.designsystem.components.VoltDisclaimer
import org.betech.fitnes.designsystem.typography.VoltType
import org.betech.fitnes.localization.LocalStrings
import org.betech.fitnes.localization.Strings
import org.betech.fitnes.presentation.onboarding.languageselect.LanguageSelectScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 23 · Signout Confirm (Pencil SCKUA · "Çıxışa hazırsan?").
 *
 * Reassurance-first signout screen. Cloud-backup + offline-grace status rows
 * lower abandon anxiety; destructive CTA is RED (VoltColors.danger) per CLAUDE.md
 * doctrine — primary `volt` button is reserved for affirmative actions.
 *
 * Layout doctrine:
 *  - Vertically scrollable column (compact phones).
 *  - 24dp horizontal gutter, surface0 background, no progress bar.
 *  - Back chevron in top bar mirrors `BackChevronButton` from pwd-reset-form.
 */
class SignoutConfirmScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: SignoutConfirmViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                SignoutConfirmSideEffect.NavigateToLanguageSelect ->
                    navigator.replaceAll(LanguageSelectScreen())
                SignoutConfirmSideEffect.NavigateBack -> {
                    if (navigator.canPop) navigator.pop()
                    else navigator.replaceAll(LanguageSelectScreen())
                }
            }
        }

        SignoutConfirmContent(
            state = state,
            strings = strings,
            onBack = { viewModel.onIntent(SignoutConfirmIntent.BackTapped) },
            onConfirm = { viewModel.onIntent(SignoutConfirmIntent.ConfirmTapped) },
            onCancel = { viewModel.onIntent(SignoutConfirmIntent.CancelTapped) },
        )
    }
}

@Composable
private fun SignoutConfirmContent(
    state: SignoutConfirmState,
    strings: Strings,
    onBack: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
        ) {
            // ── Top bar (back only) ───────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BackChevronButton(onClick = onBack)
            }

            Spacer(Modifier.height(48.dp))

            // ── Logout door-arrow icon (centered) ─────────────────────────
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                LogoutIcon()
            }

            Spacer(Modifier.height(24.dp))

            // ── Title (centered) ──────────────────────────────────────────
            Text(
                text = strings.signoutTitle,
                style = VoltType.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                ),
                color = VoltColors.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(8.dp))

            // ── Subtitle (centered, muted) ────────────────────────────────
            Text(
                text = strings.signoutSubtitle,
                style = VoltType.bodyMedium.copy(fontSize = 14.sp),
                color = VoltColors.onSurfaceMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(24.dp))

            // ── Status rows (3, 12dp vertical spacing) ────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                StatusRow(text = strings.signoutStatusBackup)
                StatusRow(text = strings.signoutStatusOffline)
                StatusRow(text = strings.signoutStatusSync)
            }

            Spacer(Modifier.height(16.dp))

            // ── Safety / data-retention notice ────────────────────────────
            VoltDisclaimer(
                text = strings.signoutSafetyNotice,
                kind = DisclaimerKind.INFO,
            )

            Spacer(Modifier.height(16.dp))

            // ── Destructive CTA (red, white text) ─────────────────────────
            DestructiveButton(
                text = strings.signoutCtaConfirm,
                onClick = onConfirm,
                enabled = !state.isSigningOut,
            )

            Spacer(Modifier.height(8.dp))

            // ── Cancel (ghost / volt text) ────────────────────────────────
            VoltButtonGhost(
                text = strings.signoutCtaCancel,
                onClick = onCancel,
                enabled = !state.isSigningOut,
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// Atoms
// ──────────────────────────────────────────────────────────────────────────────

/** 64dp surface1 circle hosting a volt-tinted door-out arrow glyph. */
@Composable
private fun LogoutIcon() {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(28.dp)) { drawLogoutGlyph() }
    }
}

private fun DrawScope.drawLogoutGlyph() {
    val stroke = size.minDimension / 10f
    val volt = VoltColors.volt
    // Door frame (left rectangle, open right side)
    val doorLeft = 0f
    val doorRight = size.width * 0.45f
    val doorTop = size.height * 0.10f
    val doorBottom = size.height * 0.90f
    // Top edge of door
    drawLine(
        color = volt,
        start = Offset(doorRight, doorTop),
        end = Offset(doorLeft, doorTop),
        strokeWidth = stroke,
        cap = StrokeCap.Round,
    )
    // Left side of door
    drawLine(
        color = volt,
        start = Offset(doorLeft, doorTop),
        end = Offset(doorLeft, doorBottom),
        strokeWidth = stroke,
        cap = StrokeCap.Round,
    )
    // Bottom edge of door
    drawLine(
        color = volt,
        start = Offset(doorLeft, doorBottom),
        end = Offset(doorRight, doorBottom),
        strokeWidth = stroke,
        cap = StrokeCap.Round,
    )
    // Right arrow shaft (mid-height)
    val midY = size.height / 2f
    val arrowStart = size.width * 0.30f
    val arrowEnd = size.width * 0.98f
    drawLine(
        color = volt,
        start = Offset(arrowStart, midY),
        end = Offset(arrowEnd, midY),
        strokeWidth = stroke,
        cap = StrokeCap.Round,
    )
    // Arrow head — upper diagonal
    drawLine(
        color = volt,
        start = Offset(arrowEnd, midY),
        end = Offset(size.width * 0.75f, midY - size.height * 0.22f),
        strokeWidth = stroke,
        cap = StrokeCap.Round,
    )
    // Arrow head — lower diagonal
    drawLine(
        color = volt,
        start = Offset(arrowEnd, midY),
        end = Offset(size.width * 0.75f, midY + size.height * 0.22f),
        strokeWidth = stroke,
        cap = StrokeCap.Round,
    )
}

/** Cloud-backup status row: 20dp volt circle-check + text. */
@Composable
private fun StatusRow(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
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
            modifier = Modifier
                .padding(end = 4.dp),
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

/**
 * Destructive button — RED background, white content. `VoltButton` doesn't
 * carry a destructive variant (its primary is reserved for affirmative actions),
 * so we instantiate Material3 `Button` inline using `VoltColors.danger`.
 */
@Composable
private fun DestructiveButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = VoltColors.danger,
            contentColor = Color.White,
            disabledContainerColor = VoltColors.surface2,
            disabledContentColor = VoltColors.onSurfaceMuted,
        ),
    ) {
        Text(text = text, style = VoltType.labelLarge)
    }
}

/** 36dp circular back-chevron button — mirrors the pwd-reset-form pattern. */
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
