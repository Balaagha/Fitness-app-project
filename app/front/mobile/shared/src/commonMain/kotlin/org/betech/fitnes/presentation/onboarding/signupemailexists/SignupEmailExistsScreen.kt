package org.betech.fitnes.presentation.onboarding.signupemailexists

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

/** V2 · Signup Email Exists (Pencil cYfk5). */
class SignupEmailExistsScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: SignupEmailExistsViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                SignupEmailExistsSideEffect.NavigateBack -> { navigator.pop() }
                SignupEmailExistsSideEffect.NavigateToEmailLogin ->
                    navigator.push(EmailLoginScreen())
                SignupEmailExistsSideEffect.ShowPrivacyToast -> Unit
            }
        }

        Content(
            state = state,
            strings = strings,
            onBack = { viewModel.onIntent(SignupEmailExistsIntent.BackTapped) },
            onEmail = { viewModel.onIntent(SignupEmailExistsIntent.EmailChanged(it)) },
            onPassword = { viewModel.onIntent(SignupEmailExistsIntent.PasswordChanged(it)) },
            onConfirm = { viewModel.onIntent(SignupEmailExistsIntent.ConfirmChanged(it)) },
            onGoLogin = { viewModel.onIntent(SignupEmailExistsIntent.GoLoginTapped) },
            onPrivacy = { viewModel.onIntent(SignupEmailExistsIntent.PrivacyTapped) },
        )
    }
}

@Composable
private fun Content(
    state: SignupEmailExistsState,
    strings: Strings,
    onBack: () -> Unit,
    onEmail: (String) -> Unit,
    onPassword: (String) -> Unit,
    onConfirm: (String) -> Unit,
    onGoLogin: () -> Unit,
    onPrivacy: () -> Unit,
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
                text = strings.signupExistsEyebrow,
                style = VoltType.labelSmall.copy(
                    fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp,
                ),
                color = VoltColors.volt,
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = strings.signupExistsTitle,
                style = VoltType.displayMedium.copy(
                    fontWeight = FontWeight.Bold, fontSize = 24.sp,
                ),
                color = VoltColors.onSurface,
            )

            Spacer(Modifier.height(24.dp))

            // Email field with danger border + error text + go-login pill ──
            Caption(strings.emailLabelCaption)
            Spacer(Modifier.height(8.dp))
            ErrorEmailField(value = state.email, onValueChange = onEmail, placeholder = strings.emailPlaceholder)

            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Canvas(modifier = Modifier.size(14.dp)) { drawWarningGlyph() }
                Text(
                    text = strings.signupExistsEmailError,
                    style = VoltType.bodyMedium.copy(fontSize = 13.sp, fontWeight = FontWeight.SemiBold),
                    color = VoltColors.danger,
                )
            }

            Spacer(Modifier.height(12.dp))

            // Go-to-login pill (volt-button-secondary style; success-tinted)
            GoLoginPill(strings.signupExistsGoLoginPill, onGoLogin)

            Spacer(Modifier.height(20.dp))

            Caption(strings.passwordLabel)
            Spacer(Modifier.height(8.dp))
            PlainField(state.password, onPassword, KeyboardType.Password, mask = true)

            Spacer(Modifier.height(16.dp))

            Caption(strings.passwordConfirmLabel)
            Spacer(Modifier.height(8.dp))
            PlainField(state.confirm, onConfirm, KeyboardType.Password, mask = true)

            Spacer(Modifier.height(16.dp))

            // Helper + privacy link
            Text(
                text = strings.signupExistsHelper,
                style = VoltType.bodyMedium.copy(fontSize = 12.sp),
                color = VoltColors.onSurfaceMuted,
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = strings.signupExistsPrivacyLink,
                style = VoltType.bodyMedium.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
                color = VoltColors.volt,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable(onClick = onPrivacy)
                    .padding(vertical = 2.dp),
            )

            Spacer(Modifier.weight(1f))

            // Disabled CTA (olive-grey)
            DisabledCta(strings.signupExistsCta)

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
private fun ErrorEmailField(value: String, onValueChange: (String) -> Unit, placeholder: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(placeholder, style = VoltType.bodyLarge) },
        singleLine = true,
        isError = true,
        textStyle = VoltType.bodyLarge,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        trailingIcon = {
            Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(18.dp)) { drawWarningGlyph() }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = VoltColors.surface1,
            unfocusedContainerColor = VoltColors.surface1,
            errorContainerColor = VoltColors.surface1,
            focusedBorderColor = VoltColors.danger,
            unfocusedBorderColor = VoltColors.danger,
            errorBorderColor = VoltColors.danger,
            focusedTextColor = VoltColors.onSurface,
            unfocusedTextColor = VoltColors.onSurface,
            errorTextColor = VoltColors.onSurface,
            cursorColor = VoltColors.danger,
            errorCursorColor = VoltColors.danger,
        ),
    )
}

@Composable
private fun PlainField(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
    mask: Boolean,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        textStyle = VoltType.bodyLarge,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (mask) PasswordVisualTransformation()
        else androidx.compose.ui.text.input.VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = VoltColors.surface1,
            unfocusedContainerColor = VoltColors.surface1,
            focusedBorderColor = VoltColors.volt,
            unfocusedBorderColor = VoltColors.outline,
            focusedTextColor = VoltColors.onSurface,
            unfocusedTextColor = VoltColors.onSurface,
            cursorColor = VoltColors.volt,
        ),
    )
}

@Composable
private fun GoLoginPill(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(VoltColors.success.copy(alpha = 0.18f))
            .border(1.dp, VoltColors.success, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            style = VoltType.labelLarge.copy(fontSize = 13.sp, fontWeight = FontWeight.SemiBold),
            color = VoltColors.success,
        )
    }
}

@Composable
private fun DisabledCta(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(VoltColors.moss.copy(alpha = 0.35f))
            .border(1.dp, VoltColors.outline, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text, style = VoltType.labelLarge, color = VoltColors.onSurfaceMuted)
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
    val top = Offset(w / 2f, h * 0.05f)
    val bl = Offset(w * 0.05f, h * 0.92f)
    val br = Offset(w * 0.95f, h * 0.92f)
    drawLine(color, top, bl, sw, StrokeCap.Round)
    drawLine(color, top, br, sw, StrokeCap.Round)
    drawLine(color, bl, br, sw, StrokeCap.Round)
    drawLine(color, Offset(w / 2f, h * 0.32f), Offset(w / 2f, h * 0.62f), sw, StrokeCap.Round)
    drawCircle(color, radius = sw * 0.7f, center = Offset(w / 2f, h * 0.78f))
}
