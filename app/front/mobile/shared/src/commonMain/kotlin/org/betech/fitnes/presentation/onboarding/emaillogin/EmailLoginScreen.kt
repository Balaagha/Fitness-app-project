package org.betech.fitnes.presentation.onboarding.emaillogin

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
import org.betech.fitnes.presentation.onboarding.emailsignup.EmailSignupScreen
import org.betech.fitnes.presentation.onboarding.paywall.PaywallScreen
import org.betech.fitnes.presentation.onboarding.pwdresetemail.PwdResetEmailScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 16 · Email Login (Pencil O8lWVO).
 *
 * Form-based sign-in: email + password + forgot-password link + primary CTA +
 * footer signup link.
 * - Surface0 bg, 24dp side padding, back chevron only top bar.
 * - Eye toggle for password (off by default).
 * - CTA enabled only when emailValid && password non-empty.
 * - On submit: mock signInWithEmail → AuthSignedIn(method="email") → Paywall.
 * - Failure surfaces error via Snackbar.
 */
class EmailLoginScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: EmailLoginViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current
        val snackbarHostState = remember { SnackbarHostState() }

        viewModel.collectSideEffect { effect ->
            when (effect) {
                EmailLoginSideEffect.NavigateBack -> {
                    if (!navigator.pop()) Unit
                }
                EmailLoginSideEffect.NavigateToForgotPassword ->
                    navigator.push(PwdResetEmailScreen())
                EmailLoginSideEffect.NavigateToSignup ->
                    navigator.push(EmailSignupScreen())
                EmailLoginSideEffect.NavigateToPaywall ->
                    navigator.push(PaywallScreen())
            }
        }

        val err = state.errorMessage
        if (err != null) {
            LaunchedEffect(err) { snackbarHostState.showSnackbar(err) }
        }

        EmailLoginContent(
            state = state,
            strings = strings,
            snackbarHostState = snackbarHostState,
            onBack = { viewModel.onIntent(EmailLoginIntent.BackTapped) },
            onEmail = { viewModel.onIntent(EmailLoginIntent.EmailChanged(it)) },
            onPassword = { viewModel.onIntent(EmailLoginIntent.PasswordChanged(it)) },
            onTogglePw = { viewModel.onIntent(EmailLoginIntent.TogglePasswordVisibility) },
            onForgot = { viewModel.onIntent(EmailLoginIntent.ForgotPasswordTapped) },
            onSignup = { viewModel.onIntent(EmailLoginIntent.SignupTapped) },
            onSubmit = { viewModel.onIntent(EmailLoginIntent.Submit) },
        )
    }
}

@Composable
private fun EmailLoginContent(
    state: EmailLoginState,
    strings: Strings,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onEmail: (String) -> Unit,
    onPassword: (String) -> Unit,
    onTogglePw: () -> Unit,
    onForgot: () -> Unit,
    onSignup: () -> Unit,
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
                text = strings.emailLoginTitle,
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

            Spacer(Modifier.height(8.dp))

            // ── Forgot password link (right-aligned) ─────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement_End,
            ) {
                Text(
                    text = strings.emailLoginForgotPassword,
                    style = VoltType.bodyMedium.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = VoltColors.volt,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(enabled = !state.isSubmitting, onClick = onForgot)
                        .padding(horizontal = 4.dp, vertical = 6.dp),
                )
            }

            Spacer(Modifier.weight(1f))

            // ── Primary CTA (with inline loading spinner) ────────────────
            PrimaryCtaButton(
                text = strings.emailLoginCta,
                enabled = state.canSubmit,
                loading = state.isSubmitting,
                onClick = onSubmit,
            )

            Spacer(Modifier.height(8.dp))

            // ── Footer: "Yeni istifadəçi? Qeydiyyat" ─────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !state.isSubmitting, onClick = onSignup)
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyleCompat(color = VoltColors.onSurfaceMuted) {
                            append(strings.emailLoginNewUserPrefix)
                        }
                        withStyleCompat(
                            color = VoltColors.volt,
                            weight = FontWeight.SemiBold,
                        ) {
                            append(strings.emailLoginSignupLink)
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

// Right-aligned Arrangement alias (local, avoids extra import noise).
private val Arrangement_End = androidx.compose.foundation.layout.Arrangement.End

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
// FormTextField — mirrors EmailSignup wrapper (keyboardType + eye trailing).
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
// EyeToggle (mirrors EmailSignup atoms — duplicated locally to avoid cross-
// module coupling; they are visual primitives, not semantic shared widgets).
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

// ──────────────────────────────────────────────────────────────────────────────
// PrimaryCtaButton — same pattern as EmailSignup (inline spinner support).
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
// Back chevron — shared atom (matches AuthGate / AiDisclosure / EmailSignup).
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
// AnnotatedString helper.
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
