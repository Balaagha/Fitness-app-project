package org.betech.fitnes.presentation.onboarding.loginerror

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import org.betech.fitnes.presentation.onboarding.pwdresetemail.PwdResetEmailScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * V1 · Login Error (Pencil u27ve).
 * EmailLogin layout with danger border on password field + warning notice.
 */
class LoginErrorScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: LoginErrorViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                LoginErrorSideEffect.NavigateBack -> { navigator.pop() }
                LoginErrorSideEffect.NavigateToForgotPassword ->
                    navigator.push(PwdResetEmailScreen())
            }
        }

        LoginErrorContent(
            state = state,
            strings = strings,
            onBack = { viewModel.onIntent(LoginErrorIntent.BackTapped) },
            onEmail = { viewModel.onIntent(LoginErrorIntent.EmailChanged(it)) },
            onPassword = { viewModel.onIntent(LoginErrorIntent.PasswordChanged(it)) },
            onForgot = { viewModel.onIntent(LoginErrorIntent.ForgotPasswordTapped) },
            onSubmit = { viewModel.onIntent(LoginErrorIntent.SubmitTapped) },
        )
    }
}

@Composable
private fun LoginErrorContent(
    state: LoginErrorState,
    strings: Strings,
    onBack: () -> Unit,
    onEmail: (String) -> Unit,
    onPassword: (String) -> Unit,
    onForgot: () -> Unit,
    onSubmit: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize().background(VoltColors.surface0),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = 24.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BackChevronAtom(onBack)
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = strings.emailLoginTitle,
                style = VoltType.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                ),
                color = VoltColors.onSurface,
            )

            Spacer(Modifier.height(24.dp))

            Caption(strings.emailLabelCaption)
            Spacer(Modifier.height(8.dp))
            PlainField(
                value = state.email,
                onValueChange = onEmail,
                placeholder = strings.emailPlaceholder,
                keyboardType = KeyboardType.Email,
                isError = false,
            )

            Spacer(Modifier.height(16.dp))

            Caption(strings.passwordLabel)
            Spacer(Modifier.height(8.dp))
            PlainField(
                value = state.password,
                onValueChange = onPassword,
                placeholder = null,
                keyboardType = KeyboardType.Password,
                isError = true,
                visualTransformation = if (state.showPassword)
                    VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Canvas(modifier = Modifier.size(18.dp)) { drawWarningGlyph() }
                    }
                },
            )

            // ── Danger error text + icon ─────────────────────────────────
            Spacer(Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Canvas(modifier = Modifier.size(14.dp)) { drawWarningGlyph() }
                Text(
                    text = strings.loginErrorPwBadText,
                    style = VoltType.bodyMedium.copy(fontSize = 13.sp, fontWeight = FontWeight.SemiBold),
                    color = VoltColors.danger,
                )
            }

            Spacer(Modifier.height(16.dp))

            // ── Yellow-bordered warning notice ───────────────────────────
            WarningNotice(strings.loginErrorWarning)

            Spacer(Modifier.height(8.dp))

            // ── Forgot password (right-aligned) ──────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                Text(
                    text = strings.emailLoginForgotPassword,
                    style = VoltType.bodyMedium.copy(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
                    color = VoltColors.volt,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onForgot)
                        .padding(horizontal = 4.dp, vertical = 6.dp),
                )
            }

            Spacer(Modifier.weight(1f))

            // ── Primary CTA ──────────────────────────────────────────────
            PrimaryCta(strings.emailLoginCta, onSubmit)

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun Caption(text: String) {
    Text(
        text = text,
        style = VoltType.labelSmall.copy(
            fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp,
        ),
        color = VoltColors.onSurfaceMuted,
    )
}

@Composable
private fun PlainField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String?,
    keyboardType: KeyboardType,
    isError: Boolean,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = placeholder?.let { { Text(it, style = VoltType.bodyLarge) } },
        singleLine = true,
        isError = isError,
        textStyle = VoltType.bodyLarge,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = VoltColors.surface1,
            unfocusedContainerColor = VoltColors.surface1,
            errorContainerColor = VoltColors.surface1,
            focusedBorderColor = if (isError) VoltColors.danger else VoltColors.volt,
            unfocusedBorderColor = if (isError) VoltColors.danger else VoltColors.outline,
            errorBorderColor = VoltColors.danger,
            focusedTextColor = VoltColors.onSurface,
            unfocusedTextColor = VoltColors.onSurface,
            errorTextColor = VoltColors.onSurface,
            cursorColor = VoltColors.volt,
            errorCursorColor = VoltColors.danger,
            focusedPlaceholderColor = VoltColors.onSurfaceMuted,
            unfocusedPlaceholderColor = VoltColors.onSurfaceMuted,
        ),
    )
}

@Composable
private fun WarningNotice(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(VoltColors.surface1)
            .border(2.dp, VoltColors.warning, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(VoltColors.warning.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.size(14.dp)) { drawWarningGlyph(VoltColors.warning) }
        }
        Text(
            text = text,
            style = VoltType.bodyMedium.copy(fontSize = 13.sp),
            color = VoltColors.onSurface,
        )
    }
}

@Composable
private fun PrimaryCta(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(VoltColors.volt)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text, style = VoltType.labelLarge, color = VoltColors.onVolt)
    }
}

@Composable
private fun BackChevronAtom(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(14.dp)) {
            val sw = size.minDimension / 8f
            val midY = size.height / 2f
            val left = size.width * 0.25f
            val right = size.width * 0.75f
            drawLine(VoltColors.onSurface, Offset(right, 0f), Offset(left, midY), sw, StrokeCap.Round)
            drawLine(VoltColors.onSurface, Offset(left, midY), Offset(right, size.height), sw, StrokeCap.Round)
        }
    }
}

private fun DrawScope.drawWarningGlyph(color: androidx.compose.ui.graphics.Color = VoltColors.danger) {
    val w = size.width; val h = size.height
    val sw = w / 10f
    // triangle outline
    val top = Offset(w / 2f, h * 0.05f)
    val bl = Offset(w * 0.05f, h * 0.92f)
    val br = Offset(w * 0.95f, h * 0.92f)
    drawLine(color, top, bl, sw, StrokeCap.Round)
    drawLine(color, top, br, sw, StrokeCap.Round)
    drawLine(color, bl, br, sw, StrokeCap.Round)
    // exclamation
    drawLine(color, Offset(w / 2f, h * 0.32f), Offset(w / 2f, h * 0.62f), sw, StrokeCap.Round)
    drawCircle(color, radius = sw * 0.7f, center = Offset(w / 2f, h * 0.78f))
}
