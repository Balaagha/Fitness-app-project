package org.betech.fitnes.presentation.onboarding.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.typography.VoltType

/**
 * Login — STUB. Filled in a later iteration.
 * Welcome navigates here on "Hesabım var".
 */
class LoginScreen : Screen {
    @Composable
    override fun Content() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(VoltColors.surface0),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Login — TODO",
                color = VoltColors.onSurface,
                style = VoltType.titleMedium,
            )
        }
    }
}
