package org.betech.fitnes.presentation.onboarding.resetlinkexpired

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import org.betech.fitnes.presentation.onboarding.pwdresetemail.PwdResetEmailScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectSideEffect

/** V6 · Reset Link Expired (Pencil a3Vwh6). Centered empty-state. */
class ResetLinkExpiredScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: ResetLinkExpiredViewModel = koinViewModel()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                ResetLinkExpiredSideEffect.NavigateToPwdReset -> navigator.push(PwdResetEmailScreen())
                ResetLinkExpiredSideEffect.NavigateBack -> { navigator.pop() }
            }
        }

        Content(
            strings = strings,
            onRequestNew = { viewModel.onIntent(ResetLinkExpiredIntent.RequestNewTapped) },
            onBack = { viewModel.onIntent(ResetLinkExpiredIntent.BackTapped) },
        )
    }
}

@Composable
private fun Content(
    strings: Strings,
    onRequestNew: () -> Unit,
    onBack: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize().background(VoltColors.surface0),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.weight(1f))

            // 64dp question-mark tile
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(VoltColors.surface1)
                    .border(1.dp, VoltColors.outline, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "?",
                    style = VoltType.displayMedium.copy(
                        fontSize = 36.sp, fontWeight = FontWeight.ExtraBold,
                    ),
                    color = VoltColors.volt,
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = strings.resetExpiredTitle,
                style = VoltType.displayMedium.copy(
                    fontWeight = FontWeight.Bold, fontSize = 24.sp,
                ),
                color = VoltColors.onSurface,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = strings.resetExpiredBody,
                style = VoltType.bodyMedium.copy(fontSize = 14.sp),
                color = VoltColors.onSurfaceMuted,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.weight(1f))

            // Primary CTA — volt
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(VoltColors.volt)
                    .clickable(onClick = onRequestNew),
                contentAlignment = Alignment.Center,
            ) {
                Text(strings.resetExpiredCtaNew, style = VoltType.labelLarge, color = VoltColors.onVolt)
            }

            Spacer(Modifier.height(12.dp))

            // Ghost CTA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    strings.resetExpiredCtaBack,
                    style = VoltType.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = VoltColors.onSurfaceMuted,
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
