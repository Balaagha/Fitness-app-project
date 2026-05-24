package org.betech.fitnes.presentation.onboarding.deleteacc1

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
import org.betech.fitnes.designsystem.components.VoltDisclaimer
import org.betech.fitnes.designsystem.typography.VoltType
import org.betech.fitnes.localization.LocalStrings
import org.betech.fitnes.localization.Strings
import org.betech.fitnes.presentation.onboarding.deleteacc2.DeleteAcc2Screen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 24 · Delete Account — Confirmation 1 (Pencil e74FR · "Hesabı silməyə hazırsanmı?").
 *
 * Informed-consent / reassurance step. NO destructive call here — that lives
 * in step 2 (Pencil GauGs) behind a typed-confirmation gate.
 *
 * Layout doctrine (mirrors SignoutConfirm patterns):
 *  - 24dp horizontal gutter, scrollable column, surface0 background.
 *  - Back chevron only in top bar.
 *  - Danger triangle icon centered (surface1 64dp circle + red glyph).
 *  - 4 surface1 archive-list rows.
 *  - INFO disclaimer for the 30-day recovery window.
 *  - Ghost "İmtina et" + destructive "Davam et" (red bg, white text).
 */
class DeleteAcc1Screen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: DeleteAcc1ViewModel = koinViewModel()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                DeleteAcc1SideEffect.NavigateBack -> {
                    if (navigator.canPop) navigator.pop()
                }
                DeleteAcc1SideEffect.NavigateToDeleteAcc2 ->
                    navigator.push(DeleteAcc2Screen())
            }
        }

        DeleteAcc1Content(
            strings = strings,
            onBack = { viewModel.onIntent(DeleteAcc1Intent.BackTapped) },
            onCancel = { viewModel.onIntent(DeleteAcc1Intent.CancelTapped) },
            onContinue = { viewModel.onIntent(DeleteAcc1Intent.ContinueTapped) },
        )
    }
}

@Composable
private fun DeleteAcc1Content(
    strings: Strings,
    onBack: () -> Unit,
    onCancel: () -> Unit,
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

            Spacer(Modifier.height(32.dp))

            // ── Danger triangle (centered) ────────────────────────────────
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                DangerTriangleIcon()
            }

            Spacer(Modifier.height(24.dp))

            // ── Title (centered) ──────────────────────────────────────────
            Text(
                text = strings.deleteAcc1Title,
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
                text = strings.deleteAcc1Subtitle,
                style = VoltType.bodyMedium.copy(fontSize = 14.sp),
                color = VoltColors.onSurfaceMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(24.dp))

            // ── Archive list rows ─────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ArchiveItemRow(strings.deleteAcc1Item1, strings.deleteAcc1ArchiveChip)
                ArchiveItemRow(strings.deleteAcc1Item2, strings.deleteAcc1ArchiveChip)
                ArchiveItemRow(strings.deleteAcc1Item3, strings.deleteAcc1ArchiveChip)
                ArchiveItemRow(strings.deleteAcc1Item4, strings.deleteAcc1ArchiveChip)
            }

            Spacer(Modifier.height(16.dp))

            // ── Recovery-window notice (INFO) ─────────────────────────────
            VoltDisclaimer(
                text = strings.deleteAcc1RecoveryNotice,
                kind = DisclaimerKind.INFO,
            )

            Spacer(Modifier.height(20.dp))

            // ── Ghost cancel ──────────────────────────────────────────────
            GhostCancelButton(text = strings.deleteAcc1CtaCancel, onClick = onCancel)

            Spacer(Modifier.height(10.dp))

            // ── Destructive continue ──────────────────────────────────────
            DestructiveButton(text = strings.deleteAcc1CtaContinue, onClick = onContinue)

            Spacer(Modifier.height(24.dp))
        }
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// Atoms (file-local — promote to designsystem if a 3rd screen reuses them)
// ──────────────────────────────────────────────────────────────────────────────

/** 64dp surface1 circle with a danger-colored warning triangle (+ exclamation). */
@Composable
private fun DangerTriangleIcon() {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(30.dp)) { drawWarningTriangle() }
    }
}

private fun DrawScope.drawWarningTriangle() {
    val danger = VoltColors.danger
    val stroke = size.minDimension / 11f
    // Triangle path (rounded, point-up)
    val path = Path().apply {
        moveTo(size.width / 2f, size.height * 0.08f)
        lineTo(size.width * 0.96f, size.height * 0.90f)
        lineTo(size.width * 0.04f, size.height * 0.90f)
        close()
    }
    drawPath(path = path, color = danger, style = Stroke(width = stroke, cap = StrokeCap.Round))
    // Exclamation shaft
    val cx = size.width / 2f
    drawLine(
        color = danger,
        start = Offset(cx, size.height * 0.38f),
        end = Offset(cx, size.height * 0.66f),
        strokeWidth = stroke,
        cap = StrokeCap.Round,
    )
    // Exclamation dot
    drawLine(
        color = danger,
        start = Offset(cx, size.height * 0.80f),
        end = Offset(cx, size.height * 0.81f),
        strokeWidth = stroke * 1.4f,
        cap = StrokeCap.Round,
    )
}

/** Archive-row: surface1 card · volt-tinted bullet · label · archive chip. */
@Composable
private fun ArchiveItemRow(label: String, chipText: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Volt-tinted folder bullet (simple square inside a circle)
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(VoltColors.volt.copy(alpha = 0.16f))
                .border(1.dp, VoltColors.volt.copy(alpha = 0.45f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.size(12.dp)) { drawFolderDot() }
        }
        Text(
            text = label,
            style = VoltType.bodyMedium.copy(fontSize = 14.sp),
            color = VoltColors.onSurface,
            modifier = Modifier.weight(1f),
        )
        // "arxiv" chip — moss tint to differentiate from CTA
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(VoltColors.moss.copy(alpha = 0.18f))
                .border(1.dp, VoltColors.moss.copy(alpha = 0.45f), RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 3.dp),
        ) {
            Text(
                text = chipText,
                style = VoltType.labelSmall.copy(fontSize = 11.sp),
                color = VoltColors.moss,
            )
        }
    }
}

private fun DrawScope.drawFolderDot() {
    // Tiny rounded square — readable at 12dp.
    drawRoundRect(
        color = VoltColors.volt,
        topLeft = Offset(0f, size.height * 0.20f),
        size = androidx.compose.ui.geometry.Size(size.width, size.height * 0.60f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(size.width / 6f),
    )
}

/** Ghost CTA — transparent bg + 1dp volt border + volt label (56dp tall). */
@Composable
private fun GhostCancelButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, VoltColors.volt),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = VoltColors.volt,
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
    ) {
        Text(text = text, style = VoltType.labelLarge)
    }
}

/** Destructive CTA — RED bg + white text (mirrors SignoutConfirm DestructiveButton). */
@Composable
private fun DestructiveButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = VoltColors.danger,
            contentColor = Color.White,
        ),
    ) {
        Text(text = text, style = VoltType.labelLarge)
    }
}

/** 36dp circular back-chevron button — mirrors signoutconfirm pattern. */
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
