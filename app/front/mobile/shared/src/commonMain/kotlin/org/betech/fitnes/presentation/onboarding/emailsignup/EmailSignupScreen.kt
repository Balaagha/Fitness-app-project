package org.betech.fitnes.presentation.onboarding.emailsignup

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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import org.betech.fitnes.presentation.onboarding.emaillogin.EmailLoginScreen
import org.betech.fitnes.presentation.onboarding.emailverify.EmailVerifyScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 15 · Email Signup (Pencil ZJFFO).
 *
 * Form-based sign-up: email + password + confirm + 4 live validation chips +
 * primary CTA + footer login link.
 * - Surface0 bg, 24dp side padding, back chevron only top bar.
 * - Per-field eye toggle for password/confirm (off by default).
 * - CTA enabled only when emailValid + pwValid + confirmMatches.
 * - On submit: mock signUpWithEmail → AuthSignedIn(method="email") → EmailVerify.
 */
class EmailSignupScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: EmailSignupViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current
        val snackbarHostState = remember { SnackbarHostState() }

        viewModel.collectSideEffect { effect ->
            when (effect) {
                EmailSignupSideEffect.NavigateBack -> {
                    if (!navigator.pop()) Unit
                }
                EmailSignupSideEffect.NavigateToEmailVerify ->
                    navigator.push(EmailVerifyScreen())
                EmailSignupSideEffect.NavigateToEmailLogin ->
                    navigator.push(EmailLoginScreen())
            }
        }

        // Surface async auth errors via snackbar (mirrors AuthGate pattern).
        val err = state.errorMessage
        if (err != null) {
            LaunchedEffect(err) { snackbarHostState.showSnackbar(err) }
        }

        EmailSignupContent(
            state = state,
            strings = strings,
            snackbarHostState = snackbarHostState,
            onBack = { viewModel.onIntent(EmailSignupIntent.BackTapped) },
            onEmail = { viewModel.onIntent(EmailSignupIntent.EmailChanged(it)) },
            onPassword = { viewModel.onIntent(EmailSignupIntent.PasswordChanged(it)) },
            onConfirm = { viewModel.onIntent(EmailSignupIntent.ConfirmChanged(it)) },
            onTogglePw = { viewModel.onIntent(EmailSignupIntent.TogglePasswordVisibility) },
            onToggleConfirm = { viewModel.onIntent(EmailSignupIntent.ToggleConfirmVisibility) },
            onSubmit = { viewModel.onIntent(EmailSignupIntent.Submit) },
            onLogin = { viewModel.onIntent(EmailSignupIntent.LoginTapped) },
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EmailSignupContent(
    state: EmailSignupState,
    strings: Strings,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onEmail: (String) -> Unit,
    onPassword: (String) -> Unit,
    onConfirm: (String) -> Unit,
    onTogglePw: () -> Unit,
    onToggleConfirm: () -> Unit,
    onSubmit: () -> Unit,
    onLogin: () -> Unit,
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
                text = strings.emailSignupTitle,
                style = VoltType.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                ),
                color = VoltColors.onSurface,
            )

            Spacer(Modifier.height(24.dp))

            // ── Email field ──────────────────────────────────────────────
            FieldCaption(strings.emailLabelCaption)
            Spacer(Modifier.height(8.dp))
            FormTextField(
                value = state.email,
                onValueChange = onEmail,
                placeholder = strings.emailPlaceholder,
                keyboardType = KeyboardType.Email,
                enabled = !state.isSubmitting,
            )

            Spacer(Modifier.height(16.dp))

            // ── Password field ───────────────────────────────────────────
            FieldCaption(strings.passwordLabel)
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
            FieldCaption(strings.passwordConfirmLabel)
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

            // ── Validation chips (FlowRow — wraps gracefully) ────────────
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

            // ── Primary CTA (with inline loading spinner) ────────────────
            PrimaryCtaButton(
                text = strings.emailSignupCta,
                enabled = state.canSubmit,
                loading = state.isSubmitting,
                onClick = onSubmit,
            )

            Spacer(Modifier.height(8.dp))

            // ── Footer: "Hesabım var. Daxil ol" ──────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !state.isSubmitting, onClick = onLogin)
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyleCompat(color = VoltColors.onSurfaceMuted) {
                            append(strings.emailSignupHaveAccount)
                        }
                        withStyleCompat(
                            color = VoltColors.volt,
                            weight = FontWeight.SemiBold,
                        ) {
                            append(strings.emailSignupLoginLink)
                        }
                    },
                    style = VoltType.bodyMedium.copy(fontSize = 14.sp),
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(Modifier.height(16.dp))
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
// FormTextField — local OutlinedTextField wrapper that supports keyboardType +
// visualTransformation + trailing icon. VoltTextField (design-system) doesn't
// expose those today; pulling them in here is consistent with Q4 DialogTextField.
// ──────────────────────────────────────────────────────────────────────────────

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

// ──────────────────────────────────────────────────────────────────────────────
// EyeToggle — 24dp Canvas icon (eye open / eye-slash) inside touch target
// ──────────────────────────────────────────────────────────────────────────────

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
    // Outer eye contour — two arcs (upper + lower lid).
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
    // Pupil.
    drawCircle(color = color, radius = w * 0.16f, center = Offset(w / 2f, h / 2f))
}

private fun DrawScope.drawEyeClosed(color: Color) {
    val w = size.width
    val h = size.height
    val sw = w / 12f
    // Lid curve (gentle dome).
    drawArc(
        color = color,
        startAngle = 200f,
        sweepAngle = 140f,
        useCenter = false,
        topLeft = Offset(0f, h * 0.20f),
        size = androidx.compose.ui.geometry.Size(w, h * 0.6f),
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = sw, cap = StrokeCap.Round),
    )
    // Diagonal slash (eye disabled).
    drawLine(
        color = color,
        start = Offset(w * 0.10f, h * 0.85f),
        end = Offset(w * 0.90f, h * 0.15f),
        strokeWidth = sw,
        cap = StrokeCap.Round,
    )
}

// ──────────────────────────────────────────────────────────────────────────────
// ValidationChip — small pill with check (volt-green when passed)
// ──────────────────────────────────────────────────────────────────────────────

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

// ──────────────────────────────────────────────────────────────────────────────
// PrimaryCtaButton — VoltButton equivalent that supports an inline spinner.
// We replicate VoltButton's shape/colors instead of calling it so we can swap
// the label for a CircularProgressIndicator without layout jank.
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
// Back chevron — shared atom (matches AuthGate / AiDisclosure).
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

// ──────────────────────────────────────────────────────────────────────────────
// AnnotatedString helper — keeps footer rich-text concise above.
// ──────────────────────────────────────────────────────────────────────────────

private inline fun androidx.compose.ui.text.AnnotatedString.Builder.withStyleCompat(
    color: Color,
    weight: FontWeight = FontWeight.Normal,
    block: androidx.compose.ui.text.AnnotatedString.Builder.() -> Unit,
) {
    pushStyle(SpanStyle(color = color, fontWeight = weight))
    block()
    pop()
}
