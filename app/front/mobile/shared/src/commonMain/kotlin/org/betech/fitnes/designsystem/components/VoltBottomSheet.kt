package org.betech.fitnes.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.spacing.VoltSpacing

/**
 * Volt modal bottom sheet — surface-1 bg, top corners 24dp, outline-strong drag handle.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoltBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    content: @Composable () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = VoltColors.surface1,
        contentColor = VoltColors.onSurface,
        scrimColor = VoltColors.surface0.copy(alpha = 0.6f),
        dragHandle = { VoltDragHandle() }
    ) {
        Box(Modifier.padding(horizontal = VoltSpacing.lg).padding(bottom = VoltSpacing.xl)) {
            content()
        }
    }
}

@Composable
private fun VoltDragHandle() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = VoltSpacing.md),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(width = 40.dp, height = 4.dp)
                .clip(RoundedCornerShape(percent = 50))
                .background(VoltColors.outlineStrong)
                .height(4.dp)
        )
    }
}
