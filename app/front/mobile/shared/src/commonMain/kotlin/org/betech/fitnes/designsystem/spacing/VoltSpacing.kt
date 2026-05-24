package org.betech.fitnes.designsystem.spacing

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Volt 8-pt spacing scale.
 * Source: ux-phase3-design-system-spec §4 (4·8·12·16·24·32·48).
 *
 * Usage:
 *   - Prefer `VoltSpacing.lg` for direct import.
 *   - Or read from CompositionLocal: `LocalVoltSpacing.current.lg`.
 */
object VoltSpacing {
    val xs: Dp = 4.dp
    val sm: Dp = 8.dp
    val md: Dp = 12.dp
    val lg: Dp = 16.dp
    val xl: Dp = 24.dp
    val xxl: Dp = 32.dp
    val xxxl: Dp = 48.dp
}

val LocalVoltSpacing = compositionLocalOf { VoltSpacing }
