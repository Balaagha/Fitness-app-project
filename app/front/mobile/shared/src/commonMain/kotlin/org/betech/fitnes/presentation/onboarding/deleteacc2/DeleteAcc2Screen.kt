package org.betech.fitnes.presentation.onboarding.deleteacc2

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.drawscope.Stroke
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
import org.betech.fitnes.presentation.onboarding.languageselect.LanguageSelectScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 25 · Delete Account — Confirmation 2 (Pencil GauGs · "Son təsdiq").
 *
 * Final destructive gate: type-to-confirm "SİL" keyword unlocks the red CTA.
 * The keyword stays AZ across all locales (locale-invariant schema sentinel)
 * so the comparison is exact and predictable on every keyboard layout.
 *
 * Layout (mirrors DeleteAcc1):
 *  - 24dp gutter, scrollable column, surface0 background.
 *  - Back chevron only in top bar.
 *  - 48dp surface1 circle + danger trash glyph (centered).
 *  - Title 28sp 700 + muted subtitle (centered).
 *  - Caption (12sp 700 ls 2sp muted) + danger-outlined OutlinedTextField with
 *    a trailing check icon that turns danger-coloured when the input matches.
 *  - Helper text (12sp muted).
 *  - Status row: clock glyph + 30-day recovery notice.
 *  - Ghost "İmtina et" + destructive "Hesabı sil" (enabled only on exact match).
 *
 * Post-delete navigation: `replaceAll(LanguageSelectScreen)` — clears the
 * back-stack so the (now-deleted) session can't be popped back into view.
 */
class DeleteAcc2Screen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: DeleteAcc2ViewModel = koinViewModel()
        val strings = LocalStrings.current
        val state by viewModel.collectAsState()

        viewModel.collectSideEffect { effect ->
            when (effect) {
                DeleteAcc2SideEffect.NavigateBack -> {
                    if (navigator.canPop) navigator.pop()
                }
                DeleteAcc2SideEffect.NavigateToLanguageSelect ->
                    navigator.replaceAll(LanguageSelectScreen())
            }
        }

        DeleteAcc2Content(
            strings = strings,
            input = state.input,
            matches = state.matches,
            isDeleting = state.isDeleting,
            onBack = { viewModel.onIntent(DeleteAcc2Intent.BackTapped) },
            onInputChanged = { viewModel.onIntent(DeleteAcc2Intent.InputChanged(it)) },
            onConfirm = { viewModel.onIntent(DeleteAcc2Intent.ConfirmTapped) },
        )
    }
}

@Composable
private fun DeleteAcc2Content(
    strings: Strings,
    input: String,
    matches: Boolean,
    isDeleting: Boolean,
    onBack: () -> Unit,
    onInputChanged: (String) -> Unit,
    onConfirm: () -> Unit,
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

            // ── Danger trash icon (centered) ──────────────────────────────
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                DangerTrashIcon()
            }

            Spacer(Modifier.height(24.dp))

            // ── Title (centered) ──────────────────────────────────────────
            Text(
                text = strings.deleteAcc2Title,
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
                text = strings.deleteAcc2Subtitle,
                style = VoltType.bodyMedium.copy(fontSize = 14.sp),
                color = VoltColors.onSurfaceMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(28.dp))

            // ── Caption ───────────────────────────────────────────────────
            Text(
                text = strings.deleteAcc2Caption,
                style = VoltType.labelSmall.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                ),
                color = VoltColors.onSurfaceMuted,
            )

            Spacer(Modifier.height(8.dp))

            // ── Danger-outlined input ─────────────────────────────────────
            // Use raw OutlinedTextField (not VoltTextField) because we need a
            // PERSISTENT danger outline regardless of focus + custom trailing
            // glyph — VoltTextField's isError still flips border to volt on
            // focus, which would dilute the destructive signal.
            OutlinedTextField(
                value = input,
                onValueChange = onInputChanged,
                enabled = !isDeleting,
                singleLine = true,
                placeholder = {
                    Text(
                        text = strings.deleteAcc2Placeholder,
                        style = VoltType.bodyLarge,
                        color = VoltColors.onSurfaceMuted.copy(alpha = 0.55f),
                    )
                },
                trailingIcon = {
                    val tint = if (matches) VoltColors.danger else VoltColors.onSurfaceMuted
                    Box(
                        modifier = Modifier.size(36.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Canvas(modifier = Modifier.size(16.dp)) { drawCheck(tint) }
                    }
                },
                textStyle = VoltType.bodyLarge.copy(color = VoltColors.onSurface),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = VoltColors.surface1,
                    unfocusedContainerColor = VoltColors.surface1,
                    disabledContainerColor = VoltColors.surface1,
                    focusedBorderColor = VoltColors.danger,
                    unfocusedBorderColor = VoltColors.danger,
                    disabledBorderColor = VoltColors.danger.copy(alpha = 0.5f),
                    cursorColor = VoltColors.danger,
                    focusedTextColor = VoltColors.onSurface,
                    unfocusedTextColor = VoltColors.onSurface,
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(8.dp))

            // ── Helper text ───────────────────────────────────────────────
            Text(
                text = strings.deleteAcc2Helper,
                style = VoltType.labelSmall.copy(fontSize = 12.sp),
                color = VoltColors.onSurfaceMuted,
            )

            Spacer(Modifier.height(16.dp))

            // ── Status row: clock + recovery notice ───────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Canvas(modifier = Modifier.size(12.dp)) { drawClock() }
                Text(
                    text = strings.deleteAcc2RecoveryNotice,
                    style = VoltType.labelSmall.copy(fontSize = 12.sp),
                    color = VoltColors.onSurfaceMuted,
                )
            }

            Spacer(Modifier.height(24.dp))

            // ── Ghost cancel ──────────────────────────────────────────────
            GhostCancelButton(
                text = strings.deleteAcc1CtaCancel,
                onClick = onBack,
                enabled = !isDeleting,
            )

            Spacer(Modifier.height(10.dp))

            // ── Destructive confirm (gated) ───────────────────────────────
            DestructiveButton(
                text = strings.deleteAcc2CtaConfirm,
                enabled = matches && !isDeleting,
                onClick = onConfirm,
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// Atoms (file-local — promote to designsystem on 3rd reuse)
// ──────────────────────────────────────────────────────────────────────────────

/** 48dp surface1 circle with a danger-coloured trash glyph. */
@Composable
private fun DangerTrashIcon() {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(22.dp)) { drawTrashGlyph() }
    }
}

private fun DrawScope.drawTrashGlyph() {
    val danger = VoltColors.danger
    val stroke = size.minDimension / 11f
    val w = size.width
    val h = size.height

    // Lid (top horizontal bar)
    drawLine(
        color = danger,
        start = Offset(w * 0.10f, h * 0.22f),
        end = Offset(w * 0.90f, h * 0.22f),
        strokeWidth = stroke,
        cap = StrokeCap.Round,
    )
    // Handle (small arch)
    drawLine(
        color = danger,
        start = Offset(w * 0.36f, h * 0.10f),
        end = Offset(w * 0.64f, h * 0.10f),
        strokeWidth = stroke,
        cap = StrokeCap.Round,
    )
    drawLine(
        color = danger,
        start = Offset(w * 0.36f, h * 0.10f),
        end = Offset(w * 0.36f, h * 0.22f),
        strokeWidth = stroke,
        cap = StrokeCap.Round,
    )
    drawLine(
        color = danger,
        start = Offset(w * 0.64f, h * 0.10f),
        end = Offset(w * 0.64f, h * 0.22f),
        strokeWidth = stroke,
        cap = StrokeCap.Round,
    )
    // Body (left + right walls + bottom curve)
    drawLine(
        color = danger,
        start = Offset(w * 0.20f, h * 0.30f),
        end = Offset(w * 0.28f, h * 0.92f),
        strokeWidth = stroke,
        cap = StrokeCap.Round,
    )
    drawLine(
        color = danger,
        start = Offset(w * 0.80f, h * 0.30f),
        end = Offset(w * 0.72f, h * 0.92f),
        strokeWidth = stroke,
        cap = StrokeCap.Round,
    )
    drawLine(
        color = danger,
        start = Offset(w * 0.28f, h * 0.92f),
        end = Offset(w * 0.72f, h * 0.92f),
        strokeWidth = stroke,
        cap = StrokeCap.Round,
    )
    // Two vertical guts
    drawLine(
        color = danger,
        start = Offset(w * 0.42f, h * 0.42f),
        end = Offset(w * 0.44f, h * 0.80f),
        strokeWidth = stroke * 0.85f,
        cap = StrokeCap.Round,
    )
    drawLine(
        color = danger,
        start = Offset(w * 0.58f, h * 0.42f),
        end = Offset(w * 0.56f, h * 0.80f),
        strokeWidth = stroke * 0.85f,
        cap = StrokeCap.Round,
    )
}

/** Tiny check glyph used as the OutlinedTextField trailing icon. */
private fun DrawScope.drawCheck(color: Color) {
    val stroke = size.minDimension / 7f
    drawLine(
        color = color,
        start = Offset(size.width * 0.15f, size.height * 0.55f),
        end = Offset(size.width * 0.42f, size.height * 0.82f),
        strokeWidth = stroke,
        cap = StrokeCap.Round,
    )
    drawLine(
        color = color,
        start = Offset(size.width * 0.42f, size.height * 0.82f),
        end = Offset(size.width * 0.88f, size.height * 0.22f),
        strokeWidth = stroke,
        cap = StrokeCap.Round,
    )
}

/** 12dp circular clock glyph for the recovery-notice status row. */
private fun DrawScope.drawClock() {
    val muted = VoltColors.onSurfaceMuted
    val stroke = size.minDimension / 9f
    // Circle outline
    drawCircle(
        color = muted,
        radius = size.minDimension / 2f - stroke / 2f,
        style = Stroke(width = stroke),
    )
    // Hour hand (up)
    val cx = size.width / 2f
    val cy = size.height / 2f
    drawLine(
        color = muted,
        start = Offset(cx, cy),
        end = Offset(cx, cy - size.minDimension * 0.28f),
        strokeWidth = stroke,
        cap = StrokeCap.Round,
    )
    // Minute hand (right)
    drawLine(
        color = muted,
        start = Offset(cx, cy),
        end = Offset(cx + size.minDimension * 0.22f, cy),
        strokeWidth = stroke,
        cap = StrokeCap.Round,
    )
}

/** Ghost CTA — transparent bg + 1dp volt border + volt label (56dp tall). */
@Composable
private fun GhostCancelButton(text: String, onClick: () -> Unit, enabled: Boolean = true) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, VoltColors.volt),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = VoltColors.volt,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = VoltColors.volt.copy(alpha = 0.4f),
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
    ) {
        Text(text = text, style = VoltType.labelLarge)
    }
}

/**
 * Destructive CTA — RED bg + white text. Disabled state preserves danger hue
 * at low alpha so the affordance stays unmistakable (a grey disabled button
 * would visually decouple from the type-to-confirm gate above it).
 */
@Composable
private fun DestructiveButton(text: String, enabled: Boolean, onClick: () -> Unit) {
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
            disabledContainerColor = VoltColors.danger.copy(alpha = 0.32f),
            disabledContentColor = Color.White.copy(alpha = 0.55f),
        ),
    ) {
        Text(text = text, style = VoltType.labelLarge)
    }
}

/** 36dp circular back-chevron button. */
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
