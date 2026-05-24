package org.betech.fitnes.presentation.onboarding.pwdresetemail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
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
import org.betech.fitnes.presentation.onboarding.pwdresetform.PwdResetFormScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 18 · Password Reset — Email (Pencil rzAPa).
 *
 * Single-field e-mail input → mock 800 ms "send link" → forward to
 * PwdResetFormScreen (token + new-password stub).
 * - Surface0 bg, 24dp side padding, back chevron only top bar.
 * - INFO disclaimer reminds user that the link expires in 15 minutes.
 * - CTA enabled only when [PwdResetEmailState.emailValid] is true.
 */
class PwdResetEmailScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: PwdResetEmailViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current
        val snackbarHostState = remember { SnackbarHostState() }

        viewModel.collectSideEffect { effect ->
            when (effect) {
                PwdResetEmailSideEffect.NavigateBack -> {
                    if (!navigator.pop()) Unit
                }
                PwdResetEmailSideEffect.NavigateToForm ->
                    navigator.push(PwdResetFormScreen())
            }
        }

        val err = state.errorMessage
        if (err != null) {
            LaunchedEffect(err) { snackbarHostState.showSnackbar(err) }
        }

        PwdResetEmailContent(
            state = state,
            strings = strings,
            snackbarHostState = snackbarHostState,
            onBack = { viewModel.onIntent(PwdResetEmailIntent.BackTapped) },
            onEmail = { viewModel.onIntent(PwdResetEmailIntent.EmailChanged(it)) },
            onSubmit = { viewModel.onIntent(PwdResetEmailIntent.Submit) },
        )
    }
}

@Composable
private fun PwdResetEmailContent(
    state: PwdResetEmailState,
    strings: Strings,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onEmail: (String) -> Unit,
    onSubmit: () -> Unit,
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
            // ── Top bar (back only) ───────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BackChevronButton(onClick = onBack)
            }

            Spacer(Modifier.height(16.dp))

            // ── Title ────────────────────────────────────────────────────
            Text(
                text = strings.pwdResetEmailTitle,
                style = VoltType.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                ),
                color = VoltColors.onSurface,
            )

            Spacer(Modifier.height(12.dp))

            // ── Subtitle ─────────────────────────────────────────────────
            Text(
                text = strings.pwdResetEmailSubtitle,
                style = VoltType.bodyMedium,
                color = VoltColors.onSurfaceMuted,
            )

            Spacer(Modifier.height(24.dp))

            // ── Email field ──────────────────────────────────────────────
            FieldCaption(strings.emailLabelCaption)
            Spacer(Modifier.height(8.dp))
            FormTextField(
                value = state.email,
                onValueChange = onEmail,
                placeholder = strings.pwdResetEmailPlaceholder,
                keyboardType = KeyboardType.Email,
                enabled = !state.isSubmitting,
            )

            Spacer(Modifier.height(16.dp))

            // ── Info disclaimer: link valid 15 minutes ───────────────────
            VoltDisclaimer(
                text = strings.pwdResetEmailInfo,
                kind = DisclaimerKind.INFO,
            )

            Spacer(Modifier.weight(1f))

            // ── Primary CTA ──────────────────────────────────────────────
            PrimaryCtaButton(
                text = strings.pwdResetEmailCta,
                enabled = state.canSubmit,
                loading = state.isSubmitting,
                onClick = onSubmit,
            )

            Spacer(Modifier.height(24.dp))
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 88.dp),
        )
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// FieldCaption — uppercase tracked label above each input
// ──────────────────────────────────────────────────────────────────────────────

@Composable
private fun FieldCaption(text: String) {
    Text(
        text = text,
        style = VoltType.labelSmall.copy(
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
        ),
        color = VoltColors.onSurfaceMuted,
    )
}

// ──────────────────────────────────────────────────────────────────────────────
// FormTextField — mirrors EmailLogin wrapper (kept local to avoid coupling).
// ──────────────────────────────────────────────────────────────────────────────

@Composable
private fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String?,
    keyboardType: KeyboardType,
    enabled: Boolean,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = placeholder?.let { { Text(it, style = VoltType.bodyLarge) } },
        enabled = enabled,
        singleLine = true,
        textStyle = VoltType.bodyLarge,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = VoltColors.surface1,
            unfocusedContainerColor = VoltColors.surface1,
            disabledContainerColor = VoltColors.surface0,
            errorContainerColor = VoltColors.surface1,
            focusedBorderColor = VoltColors.volt,
            unfocusedBorderColor = VoltColors.outline,
            disabledBorderColor = VoltColors.outline,
            errorBorderColor = VoltColors.danger,
            focusedTextColor = VoltColors.onSurface,
            unfocusedTextColor = VoltColors.onSurface,
            disabledTextColor = VoltColors.onSurfaceMuted,
            cursorColor = VoltColors.volt,
            focusedPlaceholderColor = VoltColors.onSurfaceMuted,
            unfocusedPlaceholderColor = VoltColors.onSurfaceMuted,
        ),
    )
}

// ──────────────────────────────────────────────────────────────────────────────
// PrimaryCtaButton — inline spinner support, identical to EmailLogin atom.
// ──────────────────────────────────────────────────────────────────────────────

@Composable
private fun PrimaryCtaButton(
    text: String,
    enabled: Boolean,
    loading: Boolean,
    onClick: () -> Unit,
) {
    val active = enabled || loading
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (active) VoltColors.volt else VoltColors.surface2)
            .clickable(enabled = enabled && !loading, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                color = VoltColors.onVolt,
                strokeWidth = 2.dp,
            )
        } else {
            Text(
                text = text,
                style = VoltType.labelLarge,
                color = if (active) VoltColors.onVolt else VoltColors.onSurfaceMuted,
            )
        }
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// Back chevron — shared atom (matches AuthGate / EmailLogin).
// ──────────────────────────────────────────────────────────────────────────────

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
