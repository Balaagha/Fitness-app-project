package org.betech.fitnes.presentation.onboarding.offlinebanner

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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
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
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/** V4 · Offline Banner (Pencil K2TtZa). EmailLogin layout with offline banner + disabled CTA. */
class OfflineBannerScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: OfflineBannerViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                OfflineBannerSideEffect.NavigateBack -> { navigator.pop() }
                OfflineBannerSideEffect.Refresh -> Unit
            }
        }

        Content(
            state = state,
            strings = strings,
            onBack = { viewModel.onIntent(OfflineBannerIntent.BackTapped) },
            onEmail = { viewModel.onIntent(OfflineBannerIntent.EmailChanged(it)) },
            onPassword = { viewModel.onIntent(OfflineBannerIntent.PasswordChanged(it)) },
            onRefresh = { viewModel.onIntent(OfflineBannerIntent.RefreshTapped) },
        )
    }
}

@Composable
private fun Content(
    state: OfflineBannerState,
    strings: Strings,
    onBack: () -> Unit,
    onEmail: (String) -> Unit,
    onPassword: (String) -> Unit,
    onRefresh: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize().background(VoltColors.surface0),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars),
        ) {
            // ── Top offline banner ───────────────────────────────────────
            OfflineBanner(
                text = strings.offlineBannerText,
                refresh = strings.offlineBannerRefresh,
                onRefresh = onRefresh,
            )

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Spacer(Modifier.height(8.dp))

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
                        fontWeight = FontWeight.Bold, fontSize = 28.sp,
                    ),
                    color = VoltColors.onSurface,
                    modifier = Modifier.alpha(0.65f),
                )

                Spacer(Modifier.height(24.dp))

                Caption(strings.emailLabelCaption)
                Spacer(Modifier.height(8.dp))
                DimmedField(state.email, onEmail, strings.emailPlaceholder, KeyboardType.Email, mask = false)

                Spacer(Modifier.height(16.dp))
                Caption(strings.passwordLabel)
                Spacer(Modifier.height(8.dp))
                DimmedField(state.password, onPassword, null, KeyboardType.Password, mask = true)

                Spacer(Modifier.height(12.dp))

                Text(
                    text = strings.offlineFootnote,
                    style = VoltType.bodyMedium.copy(fontSize = 12.sp),
                    color = VoltColors.onSurfaceMuted,
                )

                Spacer(Modifier.weight(1f))

                // Disabled olive CTA
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(VoltColors.moss.copy(alpha = 0.30f))
                        .border(1.dp, VoltColors.outline, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = strings.emailLoginCta,
                        style = VoltType.labelLarge,
                        color = VoltColors.onSurfaceMuted,
                    )
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun OfflineBanner(text: String, refresh: String, onRefresh: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(VoltColors.danger.copy(alpha = 0.22f))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("📵", style = VoltType.bodyMedium.copy(fontSize = 14.sp))
            Text(
                text = text,
                style = VoltType.bodyMedium.copy(fontSize = 13.sp, fontWeight = FontWeight.SemiBold),
                color = VoltColors.danger,
            )
        }
        Text(
            text = refresh,
            style = VoltType.bodyMedium.copy(fontSize = 13.sp, fontWeight = FontWeight.SemiBold),
            color = VoltColors.volt,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onRefresh)
                .padding(horizontal = 6.dp, vertical = 2.dp),
        )
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
        modifier = Modifier.alpha(0.85f),
    )
}

@Composable
private fun DimmedField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String?,
    keyboardType: KeyboardType,
    mask: Boolean,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth().alpha(0.65f),
        placeholder = placeholder?.let { { Text(it, style = VoltType.bodyLarge) } },
        singleLine = true,
        textStyle = VoltType.bodyLarge,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (mask) PasswordVisualTransformation()
        else androidx.compose.ui.text.input.VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = VoltColors.surface1,
            unfocusedContainerColor = VoltColors.surface1,
            focusedBorderColor = VoltColors.outline,
            unfocusedBorderColor = VoltColors.outline,
            focusedTextColor = VoltColors.onSurface,
            unfocusedTextColor = VoltColors.onSurface,
            cursorColor = VoltColors.volt,
        ),
    )
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
