package org.betech.fitnes.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.typography.VoltType

/**
 * Volt top app bar — transparent background, on-surface title.
 * If [progress] is provided, a [VoltProgressBar] is rendered directly below
 * (used in the 7-step onboarding flow).
 *
 * Back icon: a simple Unicode left-arrow glyph — avoids pulling in
 * material-icons-extended in Phase 1. Real lucide `arrow-left` lands in Phase 3.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoltAppBar(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    progress: Float? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        CenterAlignedTopAppBar(
            title = { Text(title, style = VoltType.titleMedium, color = VoltColors.onSurface) },
            navigationIcon = {
                if (onBack != null) {
                    IconButton(onClick = onBack) {
                        Text(
                            text = "←",
                            style = VoltType.titleLarge,
                            color = VoltColors.onSurface
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
                navigationIconContentColor = VoltColors.onSurface,
                titleContentColor = VoltColors.onSurface,
                actionIconContentColor = VoltColors.onSurface
            )
        )
        if (progress != null) {
            VoltProgressBar(progress = progress)
        }
    }
}
