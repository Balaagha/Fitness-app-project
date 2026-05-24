package org.betech.fitnes.presentation.onboarding.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.typography.VoltType

/**
 * Home — STUB (post-onboarding final destination).
 *
 * Placeholder so the onboarding flow has a real terminal screen — Paywall
 * (continue OR skip) lands here. Real home dashboard is Phase 2.
 */
class HomeScreen : Screen {
    @Composable
    override fun Content() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(VoltColors.surface0),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(24.dp),
            ) {
                Text(
                    text = "Home — TODO",
                    color = VoltColors.onSurface,
                    style = VoltType.titleMedium.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Text(
                    text = "Onboarding tamamlandı!",
                    color = VoltColors.onSurfaceMuted,
                    style = VoltType.bodyMedium,
                )
            }
        }
    }
}
