package org.betech.fitnes.presentation.onboarding.q4heightweight

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.collections.immutable.persistentListOf
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.components.VoltButton
import org.betech.fitnes.designsystem.components.VoltMeasurementCard
import org.betech.fitnes.designsystem.typography.VoltType
import org.betech.fitnes.localization.LocalStrings
import org.betech.fitnes.localization.Strings
import org.betech.fitnes.presentation.onboarding.common.QuestionProgress
import org.betech.fitnes.presentation.onboarding.common.QuestionScaffold
import org.betech.fitnes.presentation.onboarding.q3age.Q3AgeScreen
import org.betech.fitnes.presentation.onboarding.q5experience.Q5ExperienceScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 1.4 · Q4 Height + Weight (Pencil qXLw8 · "Q4 · Boy + Çəki").
 *
 * Step 4 of 7. Two stacked [VoltMeasurementCard]s — height (cm/ft toggle)
 * and weight (kg/lb toggle). Internal storage stays metric; toggles only
 * change presentation. Tapping a value opens a numeric input dialog that
 * accepts the displayed unit and converts back to metric on confirm.
 */
class Q4HeightWeightScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: Q4HeightWeightViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                Q4HeightWeightSideEffect.NavigateBack -> {
                    if (!navigator.pop()) navigator.replace(Q3AgeScreen())
                }
                Q4HeightWeightSideEffect.NavigateToQ5Experience ->
                    navigator.push(Q5ExperienceScreen())
                is Q4HeightWeightSideEffect.ShowError -> Unit // toast hook later
            }
        }

        Q4HeightWeightContent(
            state = state,
            strings = strings,
            onBack = { viewModel.onIntent(Q4HeightWeightIntent.BackTapped) },
            onHeightUnit = { viewModel.onIntent(Q4HeightWeightIntent.SetHeightUnit(it)) },
            onWeightUnit = { viewModel.onIntent(Q4HeightWeightIntent.SetWeightUnit(it)) },
            onOpenDialog = { viewModel.onIntent(Q4HeightWeightIntent.OpenDialog(it)) },
            onCloseDialog = { viewModel.onIntent(Q4HeightWeightIntent.CloseDialog) },
            onSetHeightCm = { viewModel.onIntent(Q4HeightWeightIntent.SetHeightCm(it)) },
            onSetWeightKg = { viewModel.onIntent(Q4HeightWeightIntent.SetWeightKg(it)) },
            onConfirm = { viewModel.onIntent(Q4HeightWeightIntent.Confirm) },
        )
    }
}

@Composable
private fun Q4HeightWeightContent(
    state: Q4HeightWeightState,
    strings: Strings,
    onBack: () -> Unit,
    onHeightUnit: (HeightUnit) -> Unit,
    onWeightUnit: (WeightUnit) -> Unit,
    onOpenDialog: (DialogTarget) -> Unit,
    onCloseDialog: () -> Unit,
    onSetHeightCm: (Int) -> Unit,
    onSetWeightKg: (Int) -> Unit,
    onConfirm: () -> Unit,
) {
    val heightUnitOptions = remember(strings) {
        persistentListOf(strings.q4UnitCm, strings.q4UnitFt)
    }
    val weightUnitOptions = remember(strings) {
        persistentListOf(strings.q4UnitKg, strings.q4UnitLb)
    }

    // Display value strings derived from state + unit selection.
    val (heightValue, heightSuffix) = when (state.heightUnit) {
        HeightUnit.CM -> state.heightCm.toString() to strings.q4UnitCm
        HeightUnit.FT -> {
            val (ft, inch) = Q4HeightWeightViewModel.cmToFtIn(state.heightCm)
            "${ft}'${inch}\"" to strings.q4UnitFt
        }
    }
    val (weightValue, weightSuffix) = when (state.weightUnit) {
        WeightUnit.KG -> state.weightKg.toString() to strings.q4UnitKg
        WeightUnit.LB -> Q4HeightWeightViewModel.kgToLb(state.weightKg).toString() to
            strings.q4UnitLb
    }

    QuestionScaffold(
        progress = QuestionProgress(current = 4, total = 7),
        title = strings.q4Title,
        subtitle = strings.q4Subtitle,
        onBack = onBack,
        primaryCta = {
            VoltButton(
                text = strings.continueAction,
                onClick = onConfirm,
                enabled = state.isValid && !state.isSaving,
            )
        },
    ) {
        VoltMeasurementCard(
            leadingIcon = { RulerGlyph() },
            caption = strings.q4HeightCaption,
            value = heightValue,
            valueSuffix = heightSuffix,
            unitOptions = heightUnitOptions,
            selectedUnitIndex = if (state.heightUnit == HeightUnit.CM) 0 else 1,
            onUnitSelected = { idx ->
                onHeightUnit(if (idx == 0) HeightUnit.CM else HeightUnit.FT)
            },
            onValueTap = { onOpenDialog(DialogTarget.HEIGHT) },
        )

        Spacer(Modifier.height(12.dp))

        VoltMeasurementCard(
            leadingIcon = { ScaleGlyph() },
            caption = strings.q4WeightCaption,
            value = weightValue,
            valueSuffix = weightSuffix,
            unitOptions = weightUnitOptions,
            selectedUnitIndex = if (state.weightUnit == WeightUnit.KG) 0 else 1,
            onUnitSelected = { idx ->
                onWeightUnit(if (idx == 0) WeightUnit.KG else WeightUnit.LB)
            },
            onValueTap = { onOpenDialog(DialogTarget.WEIGHT) },
        )

        Spacer(Modifier.height(16.dp))

        HintRow(text = strings.q4Hint)
    }

    when (state.openDialog) {
        DialogTarget.HEIGHT -> HeightInputDialog(
            heightCm = state.heightCm,
            unit = state.heightUnit,
            strings = strings,
            onCancel = onCloseDialog,
            onConfirm = { cm ->
                onSetHeightCm(cm)
                onCloseDialog()
            },
        )
        DialogTarget.WEIGHT -> WeightInputDialog(
            weightKg = state.weightKg,
            unit = state.weightUnit,
            strings = strings,
            onCancel = onCloseDialog,
            onConfirm = { kg ->
                onSetWeightKg(kg)
                onCloseDialog()
            },
        )
        null -> Unit
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Hint row — small info glyph + caption.
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun HintRow(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        InfoGlyph()
        Text(
            text = text,
            style = VoltType.bodyMedium.copy(fontSize = 12.sp),
            color = VoltColors.onSurfaceMuted,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Zero-dep glyphs (Canvas).
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun RulerGlyph() {
    Canvas(modifier = Modifier.size(20.dp)) {
        val sw = size.minDimension / 12f
        // Vertical bar (the "ruler")
        drawLine(
            color = VoltColors.volt,
            start = Offset(size.width * 0.30f, size.height * 0.10f),
            end = Offset(size.width * 0.30f, size.height * 0.90f),
            strokeWidth = sw * 1.4f,
            cap = StrokeCap.Round,
        )
        // Tick marks
        val ticks = listOf(0.22f, 0.40f, 0.58f, 0.76f)
        ticks.forEach { ty ->
            drawLine(
                color = VoltColors.volt,
                start = Offset(size.width * 0.30f, size.height * ty),
                end = Offset(size.width * 0.55f, size.height * ty),
                strokeWidth = sw,
                cap = StrokeCap.Round,
            )
        }
    }
}

@Composable
private fun ScaleGlyph() {
    Canvas(modifier = Modifier.size(20.dp)) {
        val sw = size.minDimension / 10f
        // Dumbbell-ish: two end-caps + bar
        val midY = size.height / 2f
        // Left cap
        drawLine(
            color = VoltColors.volt,
            start = Offset(size.width * 0.12f, midY - size.height * 0.20f),
            end = Offset(size.width * 0.12f, midY + size.height * 0.20f),
            strokeWidth = sw * 2.0f,
            cap = StrokeCap.Round,
        )
        // Right cap
        drawLine(
            color = VoltColors.volt,
            start = Offset(size.width * 0.88f, midY - size.height * 0.20f),
            end = Offset(size.width * 0.88f, midY + size.height * 0.20f),
            strokeWidth = sw * 2.0f,
            cap = StrokeCap.Round,
        )
        // Bar
        drawLine(
            color = VoltColors.volt,
            start = Offset(size.width * 0.12f, midY),
            end = Offset(size.width * 0.88f, midY),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
    }
}

@Composable
private fun InfoGlyph() {
    Canvas(modifier = Modifier.size(14.dp)) {
        val sw = size.minDimension / 10f
        val r = size.minDimension / 2f - sw / 2f
        // Outer circle
        drawCircle(
            color = VoltColors.onSurfaceMuted,
            radius = r,
            center = Offset(size.width / 2f, size.height / 2f),
            style = Stroke(width = sw),
        )
        // Dot (i body)
        drawCircle(
            color = VoltColors.onSurfaceMuted,
            radius = sw * 0.8f,
            center = Offset(size.width / 2f, size.height * 0.42f),
        )
        // Stem
        drawLine(
            color = VoltColors.onSurfaceMuted,
            start = Offset(size.width / 2f, size.height * 0.55f),
            end = Offset(size.width / 2f, size.height * 0.80f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Input dialogs — accept value in displayed unit, convert + clamp on confirm.
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun HeightInputDialog(
    heightCm: Int,
    unit: HeightUnit,
    strings: Strings,
    onCancel: () -> Unit,
    onConfirm: (Int) -> Unit,
) {
    val initialText = when (unit) {
        HeightUnit.CM -> heightCm.toString()
        HeightUnit.FT -> {
            // Single combined input: total inches. Simpler than two fields.
            val (ft, inch) = Q4HeightWeightViewModel.cmToFtIn(heightCm)
            (ft * 12 + inch).toString()
        }
    }
    var input by remember(heightCm, unit) { mutableStateOf(initialText) }
    LaunchedEffect(heightCm, unit) { input = initialText }

    val parsed = input.toIntOrNull()
    val candidateCm = parsed?.let {
        when (unit) {
            HeightUnit.CM -> it
            HeightUnit.FT -> (it * 2.54).toInt()
        }
    }
    val isValid = candidateCm != null &&
        candidateCm in Q4HeightWeightState.MIN_HEIGHT_CM..Q4HeightWeightState.MAX_HEIGHT_CM

    AlertDialog(
        onDismissRequest = onCancel,
        containerColor = VoltColors.surface1,
        titleContentColor = VoltColors.onSurface,
        textContentColor = VoltColors.onSurfaceMuted,
        title = {
            Text(
                text = strings.q4DialogHeightTitle,
                style = VoltType.titleMedium,
                color = VoltColors.onSurface,
            )
        },
        text = {
            DialogTextField(
                value = input,
                onValueChange = { input = it.filter { c -> c.isDigit() }.take(4) },
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val cm = candidateCm?.coerceIn(
                        Q4HeightWeightState.MIN_HEIGHT_CM,
                        Q4HeightWeightState.MAX_HEIGHT_CM,
                    ) ?: heightCm
                    onConfirm(cm)
                },
                enabled = isValid,
            ) {
                Text(
                    text = strings.q3DialogConfirm,
                    color = if (isValid) VoltColors.volt else VoltColors.onSurfaceMuted,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(text = strings.q3DialogCancel, color = VoltColors.onSurfaceMuted)
            }
        },
    )
}

@Composable
private fun WeightInputDialog(
    weightKg: Int,
    unit: WeightUnit,
    strings: Strings,
    onCancel: () -> Unit,
    onConfirm: (Int) -> Unit,
) {
    val initialText = when (unit) {
        WeightUnit.KG -> weightKg.toString()
        WeightUnit.LB -> Q4HeightWeightViewModel.kgToLb(weightKg).toString()
    }
    var input by remember(weightKg, unit) { mutableStateOf(initialText) }
    LaunchedEffect(weightKg, unit) { input = initialText }

    val parsed = input.toIntOrNull()
    val candidateKg = parsed?.let {
        when (unit) {
            WeightUnit.KG -> it
            WeightUnit.LB -> Q4HeightWeightViewModel.lbToKg(it)
        }
    }
    val isValid = candidateKg != null &&
        candidateKg in Q4HeightWeightState.MIN_WEIGHT_KG..Q4HeightWeightState.MAX_WEIGHT_KG

    AlertDialog(
        onDismissRequest = onCancel,
        containerColor = VoltColors.surface1,
        titleContentColor = VoltColors.onSurface,
        textContentColor = VoltColors.onSurfaceMuted,
        title = {
            Text(
                text = strings.q4DialogWeightTitle,
                style = VoltType.titleMedium,
                color = VoltColors.onSurface,
            )
        },
        text = {
            DialogTextField(
                value = input,
                onValueChange = { input = it.filter { c -> c.isDigit() }.take(4) },
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val kg = candidateKg?.coerceIn(
                        Q4HeightWeightState.MIN_WEIGHT_KG,
                        Q4HeightWeightState.MAX_WEIGHT_KG,
                    ) ?: weightKg
                    onConfirm(kg)
                },
                enabled = isValid,
            ) {
                Text(
                    text = strings.q3DialogConfirm,
                    color = if (isValid) VoltColors.volt else VoltColors.onSurfaceMuted,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(text = strings.q3DialogCancel, color = VoltColors.onSurfaceMuted)
            }
        },
    )
}

@Composable
private fun DialogTextField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = VoltColors.onSurface,
            unfocusedTextColor = VoltColors.onSurface,
            focusedBorderColor = VoltColors.volt,
            unfocusedBorderColor = VoltColors.outline,
            cursorColor = VoltColors.volt,
            focusedContainerColor = VoltColors.surface2,
            unfocusedContainerColor = VoltColors.surface2,
        ),
    )
}
