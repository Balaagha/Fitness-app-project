package org.betech.fitnes.presentation.onboarding.pwdresetform

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.typography.VoltType
import org.betech.fitnes.localization.LocalStrings
import org.betech.fitnes.localization.Strings
import org.betech.fitnes.presentation.onboarding.emaillogin.EmailLoginScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 19 · Password Reset · Form (Pencil vA9Tb).
 *
 * Two-field form: new password + confirm + 4 live validation chips +
 * primary CTA. Same scaffold as EmailSignup, no footer link.
 * - Surface0 bg, 24dp side padding, back chevron only top bar.
 * - Per-field eye toggle (off by default).
 * - CTA enabled only when pwValid + confirmMatches.
 * - On submit: 800ms mock delay → navigate to EmailLogin.
 */
class PwdResetFormScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: PwdResetFormViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current
        val snackbarHostState = remember { SnackbarHostState() }

        viewModel.collectSideEffect { effect ->
            when (effect) {
                PwdResetFormSideEffect.NavigateBack -> {
                    if (!navigator.pop()) Unit
                }
                PwdResetFormSideEffect.NavigateToEmailLogin ->
                    navigator.push(EmailLoginScreen())
            }
        }

        val err = state.errorMessage
        if (err != null) {
            LaunchedEffect(err) { snackbarHostState.showSnackbar(err) }
        }

        PwdResetFormContent(
            state = state,
            strings = strings,
            snackbarHostState = snackbarHostState,
            onBack = { viewModel.onIntent(PwdResetFormIntent.BackTapped) },
            onPassword = { viewModel.onIntent(PwdResetFormIntent.PasswordChanged(it)) },
            onConfirm = { viewModel.onIntent(PwdResetFormIntent.ConfirmChanged(it)) },
            onTogglePw = { viewModel.onIntent(PwdResetFormIntent.TogglePasswordVisibility) },
            onToggleConfirm = { viewModel.onIntent(PwdResetFormIntent.ToggleConfirmVisibility) },
            onSubmit = { viewModel.onIntent(PwdResetFormIntent.Submit) },
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PwdResetFormContent(
    state: PwdResetFormState,
    strings: Strings,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onPassword: (String) -> Unit,
    onConfirm: (String) -> Unit,
    onTogglePw: () -> Unit,
    onToggleConfirm: () -> Unit,
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
                text = strings.pwdResetFormTitle,
                style = VoltType.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                ),
                color = VoltColors.onSurface,
            )

            Spacer(Modifier.height(24.dp))

            // ── New password field ───────────────────────────────────────
            FieldCaption(strings.pwdResetFormNewPasswordLabel)
            Spacer(Modifier.height(8.dp))
            FormTextField(
                value = state.password,
                onValueChange = onPassword,
                placeholder = null,
                keyboardType = KeyboardType.Password,
                enabled = !state.isSubmitting,
                visualTransformation = if (state.showPassword)
                    VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    EyeToggle(
                        visible = state.showPassword,
                        onClick = onTogglePw,
                    )
                },
            )

            Spacer(Modifier.height(16.dp))

            // ── Confirm password field ───────────────────────────────────
            FieldCaption(strings.pwdResetFormConfirmLabel)
            Spacer(Modifier.height(8.dp))
            FormTextField(
                value = state.confirm,
                onValueChange = onConfirm,
                placeholder = null,
                keyboardType = KeyboardType.Password,
                enabled = !state.isSubmitting,
                visualTransformation = if (state.showConfirm)
                    VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    EyeToggle(
                        visible = state.showConfirm,
                        onClick = onToggleConfirm,
                    )
                },
            )

            Spacer(Modifier.height(12.dp))

            // ── Validation chips ─────────────────────────────────────────
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ValidationChip(text = strings.pwRuleLength, passed = state.pwLength)
                ValidationChip(text = strings.pwRuleUppercase, passed = state.pwUpper)
                ValidationChip(text = strings.pwRuleDigit, passed = state.pwDigit)
                ValidationChip(text = strings.pwRuleSpecial, passed = state.pwSpecial)
            }

            Spacer(Modifier.weight(1f))

            // ── Primary CTA ──────────────────────────────────────────────
            PrimaryCtaButton(
                text = strings.pwdResetFormCta,
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
// Local atoms — duplicated from EmailSignupScreen so each form-screen owns its
// own styling unit (consistent with the established pattern; promotion to
// design-system happens after a 3rd form-screen ships).
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

@Composable
private fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String?,
    keyboardType: KeyboardType,
    enabled: Boolean,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
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
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
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

@Composable
private fun EyeToggle(visible: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(20.dp)) {
            if (visible) drawEyeOpen(VoltColors.onSurfaceMuted)
            else drawEyeClosed(VoltColors.onSurfaceMuted)
        }
    }
}

private fun DrawScope.drawEyeOpen(color: Color) {
    val w = size.width
    val h = size.height
    val sw = w / 12f
    drawArc(
        color = color,
        startAngle = 200f,
        sweepAngle = 140f,
        useCenter = false,
        topLeft = Offset(0f, h * 0.05f),
        size = androidx.compose.ui.geometry.Size(w, h * 0.9f),
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = sw, cap = StrokeCap.Round),
    )
    drawArc(
        color = color,
        startAngle = 20f,
        sweepAngle = 140f,
        useCenter = false,
        topLeft = Offset(0f, h * 0.05f),
        size = androidx.compose.ui.geometry.Size(w, h * 0.9f),
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = sw, cap = StrokeCap.Round),
    )
    drawCircle(color = color, radius = w * 0.16f, center = Offset(w / 2f, h / 2f))
}

private fun DrawScope.drawEyeClosed(color: Color) {
    val w = size.width
    val h = size.height
    val sw = w / 12f
    drawArc(
        color = color,
        startAngle = 200f,
        sweepAngle = 140f,
        useCenter = false,
        topLeft = Offset(0f, h * 0.20f),
        size = androidx.compose.ui.geometry.Size(w, h * 0.6f),
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = sw, cap = StrokeCap.Round),
    )
    drawLine(
        color = color,
        start = Offset(w * 0.10f, h * 0.85f),
        end = Offset(w * 0.90f, h * 0.15f),
        strokeWidth = sw,
        cap = StrokeCap.Round,
    )
}

@Composable
private fun ValidationChip(text: String, passed: Boolean) {
    Row(
        modifier = Modifier
            .heightIn(min = 28.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(VoltColors.surface1)
            .border(
                width = 1.dp,
                color = if (passed) VoltColors.success else VoltColors.outline,
                shape = RoundedCornerShape(14.dp),
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(CircleShape)
                .background(if (passed) VoltColors.success else VoltColors.surface2),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.size(8.dp)) {
                drawCheckGlyph(if (passed) VoltColors.onVolt else VoltColors.onSurfaceMuted)
            }
        }
        Text(
            text = text,
            style = VoltType.labelSmall.copy(fontSize = 12.sp),
            color = if (passed) VoltColors.onSurface else VoltColors.onSurfaceMuted,
        )
    }
}

private fun DrawScope.drawCheckGlyph(color: Color) {
    val sw = size.minDimension / 5f
    drawLine(
        color = color,
        start = Offset(size.width * 0.15f, size.height * 0.55f),
        end = Offset(size.width * 0.42f, size.height * 0.82f),
        strokeWidth = sw,
        cap = StrokeCap.Round,
    )
    drawLine(
        color = color,
        start = Offset(size.width * 0.42f, size.height * 0.82f),
        end = Offset(size.width * 0.88f, size.height * 0.20f),
        strokeWidth = sw,
        cap = StrokeCap.Round,
    )
}

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
