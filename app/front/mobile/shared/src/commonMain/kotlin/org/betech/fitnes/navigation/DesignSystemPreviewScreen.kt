package org.betech.fitnes.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.components.DisclaimerKind
import org.betech.fitnes.designsystem.components.VoltAppBar
import org.betech.fitnes.designsystem.components.VoltButton
import org.betech.fitnes.designsystem.components.VoltButtonGhost
import org.betech.fitnes.designsystem.components.VoltButtonSecondary
import org.betech.fitnes.designsystem.components.VoltCard
import org.betech.fitnes.designsystem.components.VoltCheckbox
import org.betech.fitnes.designsystem.components.VoltChip
import org.betech.fitnes.designsystem.components.VoltDisclaimer
import org.betech.fitnes.designsystem.components.VoltProgressBar
import org.betech.fitnes.designsystem.components.VoltRadio
import org.betech.fitnes.designsystem.components.VoltSlider
import org.betech.fitnes.designsystem.components.VoltTextField
import org.betech.fitnes.designsystem.spacing.VoltSpacing
import org.betech.fitnes.designsystem.typography.VoltType
import org.betech.fitnes.localization.LocalStrings

/**
 * Dev-only preview screen for visual verification of the Volt design system.
 * Lists one instance of every Phase 1 component vertically.
 */
class DesignSystemPreviewScreen : Screen {

    @Composable
    override fun Content() {
        val strings = LocalStrings.current
        val scroll = rememberScrollState()

        var textValue by remember { mutableStateOf("") }
        var chipSelected by remember { mutableStateOf(false) }
        var checked by remember { mutableStateOf(true) }
        var radioOn by remember { mutableStateOf(true) }
        var sliderValue by remember { mutableStateOf(0.5f) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(VoltColors.surface0)
        ) {
            VoltAppBar(
                title = "Volt DS Preview",
                onBack = {},
                progress = 0.4f
            )

            Column(
                modifier = Modifier
                    .verticalScroll(scroll)
                    .padding(
                        PaddingValues(
                            start = VoltSpacing.lg,
                            end = VoltSpacing.lg,
                            top = VoltSpacing.lg,
                            bottom = VoltSpacing.xxxl
                        )
                    ),
                verticalArrangement = Arrangement.spacedBy(VoltSpacing.lg)
            ) {
                SectionLabel("Buttons")
                VoltButton(text = strings.continueAction, onClick = {})
                VoltButtonSecondary(text = strings.backAction, onClick = {})
                VoltButtonGhost(text = strings.skipAction, onClick = {})

                SectionLabel("TextField")
                VoltTextField(
                    value = textValue,
                    onValueChange = { textValue = it },
                    label = "E-poçt",
                    placeholder = "ad@nümunə.az"
                )

                SectionLabel("Chip")
                Row(horizontalArrangement = Arrangement.spacedBy(VoltSpacing.sm)) {
                    VoltChip(text = "Üst", selected = chipSelected, onClick = { chipSelected = !chipSelected })
                    VoltChip(text = "Sinə", selected = !chipSelected, onClick = { chipSelected = !chipSelected })
                }

                SectionLabel("ProgressBar (0.4)")
                VoltProgressBar(progress = 0.4f)

                SectionLabel("Checkbox / Radio")
                Row(horizontalArrangement = Arrangement.spacedBy(VoltSpacing.lg)) {
                    VoltCheckbox(checked = checked, onCheckedChange = { checked = it })
                    VoltRadio(selected = radioOn, onClick = { radioOn = !radioOn })
                }

                SectionLabel("Slider (0.5)")
                VoltSlider(value = sliderValue, onValueChange = { sliderValue = it })

                SectionLabel("Card")
                VoltCard {
                    Column(verticalArrangement = Arrangement.spacedBy(VoltSpacing.sm)) {
                        Text("Səni belə tanıdıq", style = VoltType.titleMedium, color = VoltColors.onSurface)
                        Text(
                            "Bu rəqəmlər başlanğıc nöqtəsidir.",
                            style = VoltType.bodyMedium,
                            color = VoltColors.onSurfaceMuted
                        )
                    }
                }

                SectionLabel("Disclaimer — AI Disclosure")
                VoltDisclaimer(text = strings.aiDisclosureBody, kind = DisclaimerKind.AI_DISCLOSURE)

                SectionLabel("Disclaimer — Medical hard-stop")
                VoltDisclaimer(text = strings.medicalHardStopBody, kind = DisclaimerKind.MEDICAL_HARD_STOP)

                SectionLabel("Disclaimer — Info")
                VoltDisclaimer(
                    text = "Plan AI tərəfindən yaradılır",
                    kind = DisclaimerKind.INFO
                )
            }
        }
    }

    @Composable
    private fun SectionLabel(text: String) {
        Text(text, style = VoltType.labelSmall, color = VoltColors.onSurfaceMuted)
    }
}
