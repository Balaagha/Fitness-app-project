package org.betech.fitnes.designsystem.color

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import org.betech.fitnes.designsystem.spacing.LocalVoltSpacing
import org.betech.fitnes.designsystem.spacing.VoltSpacing
import org.betech.fitnes.designsystem.typography.voltTypography
import org.betech.fitnes.localization.LocalStrings
import org.betech.fitnes.localization.currentLocale
import org.betech.fitnes.localization.resolveStrings

/**
 * Volt color system — canonical palette from docs/project-context.md §1b.
 *
 * Doctrine:
 *  - `volt` is the only primary accent (electric yellow).
 *  - Text on `volt` MUST be `onVolt` (black). White on volt is FORBIDDEN.
 *  - `moss` is decorative only — NEVER use as a CTA.
 *  - Legacy orange (#FF6B33) is removed.
 *
 * No hex value may be declared outside this file.
 */
object VoltColors {
    val volt = Color(0xFFE6FF00)
    val onVolt = Color(0xFF0E0E0E)

    val surface0 = Color(0xFF0A0A0B)
    val surface1 = Color(0xFF141416)
    val surface2 = Color(0xFF1E1E21)

    val onSurface = Color(0xFFF2F2F2)
    val onSurfaceMuted = Color(0xFF9A9AA0)

    val moss = Color(0xFFA4B82B)
    val success = Color(0xFF3DD68C)
    val danger = Color(0xFFFF4D4F)
    val warning = Color(0xFFFFB020)

    val outline = Color(0xFF2A2A2E)
    val outlineStrong = Color(0xFF3D3D44)
}

fun voltDarkColorScheme(): ColorScheme = darkColorScheme(
    primary = VoltColors.volt,
    onPrimary = VoltColors.onVolt,
    primaryContainer = VoltColors.volt,
    onPrimaryContainer = VoltColors.onVolt,

    secondary = VoltColors.moss,
    onSecondary = VoltColors.onVolt,
    secondaryContainer = VoltColors.surface2,
    onSecondaryContainer = VoltColors.onSurface,

    tertiary = VoltColors.success,
    onTertiary = VoltColors.onVolt,

    background = VoltColors.surface0,
    onBackground = VoltColors.onSurface,

    surface = VoltColors.surface0,
    onSurface = VoltColors.onSurface,
    surfaceVariant = VoltColors.surface1,
    onSurfaceVariant = VoltColors.onSurfaceMuted,
    surfaceTint = VoltColors.volt,

    error = VoltColors.danger,
    onError = VoltColors.onSurface,

    outline = VoltColors.outline,
    outlineVariant = VoltColors.outlineStrong
)

@Composable
fun VoltTheme(content: @Composable () -> Unit) {
    val strings = remember { resolveStrings(currentLocale()) }
    CompositionLocalProvider(
        LocalVoltSpacing provides VoltSpacing,
        LocalStrings provides strings
    ) {
        MaterialTheme(
            colorScheme = voltDarkColorScheme(),
            typography = voltTypography(),
            content = content
        )
    }
}
