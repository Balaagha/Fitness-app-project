package org.betech.fitnes.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.typography.VoltType

/**
 * Volt text field — ux-phase3-design-system-spec §8.1.
 * Surface-1 fill, outline border, volt focus border, danger error border.
 */
@Composable
fun VoltTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    errorText: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = label?.let { { Text(it, style = VoltType.labelSmall) } },
        placeholder = placeholder?.let { { Text(it, style = VoltType.bodyLarge) } },
        isError = isError,
        enabled = enabled,
        singleLine = singleLine,
        textStyle = VoltType.bodyLarge,
        shape = RoundedCornerShape(12.dp),
        supportingText = errorText?.let { { Text(it, style = VoltType.labelSmall) } },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = VoltColors.surface1,
            unfocusedContainerColor = VoltColors.surface1,
            disabledContainerColor = VoltColors.surface0,
            errorContainerColor = VoltColors.surface1,
            focusedBorderColor = VoltColors.volt,
            unfocusedBorderColor = VoltColors.outline,
            disabledBorderColor = VoltColors.outline,
            errorBorderColor = VoltColors.danger,
            focusedTextColor = VoltColors.onSurface,
            unfocusedTextColor = VoltColors.onSurface,
            disabledTextColor = VoltColors.onSurfaceMuted,
            errorTextColor = VoltColors.onSurface,
            cursorColor = VoltColors.volt,
            focusedLabelColor = VoltColors.volt,
            unfocusedLabelColor = VoltColors.onSurfaceMuted,
            errorLabelColor = VoltColors.danger,
            focusedPlaceholderColor = VoltColors.onSurfaceMuted,
            unfocusedPlaceholderColor = VoltColors.onSurfaceMuted,
            errorSupportingTextColor = VoltColors.danger,
            focusedSupportingTextColor = VoltColors.onSurfaceMuted,
            unfocusedSupportingTextColor = VoltColors.onSurfaceMuted
        )
    )
}
