package org.betech.fitnes.designsystem.spacing

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Volt corner-radius scale — mirrors Pencil `$radius-sm/md/lg` variables.
 *
 * Source of truth: `app/design/mobile/app_design.pen` `get_variables`.
 * Use these tokens whenever a `RoundedCornerShape` value is needed instead
 * of inline literals. Component-specific exotic radii (e.g. 8dp pills,
 * 20dp option cards) may stay inline when they don't appear in the Pencil scale.
 */
object VoltRadius {
    val sm: Dp = 12.dp
    val md: Dp = 16.dp
    val lg: Dp = 24.dp
}
