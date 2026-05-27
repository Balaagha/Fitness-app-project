package org.betech.fitnes.presentation.onboarding.q3age

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.components.VoltButton
import org.betech.fitnes.designsystem.components.VoltSlider
import org.betech.fitnes.designsystem.typography.VoltType
import org.betech.fitnes.localization.LocalStrings
import org.betech.fitnes.localization.Strings
import org.betech.fitnes.presentation.onboarding.common.QuestionProgress
import org.betech.fitnes.presentation.onboarding.common.QuestionScaffold
import org.betech.fitnes.presentation.onboarding.q2sex.Q2SexScreen
import org.betech.fitnes.presentation.onboarding.q3agesoftwarning.Q3AgeSoftWarningScreen
import org.betech.fitnes.presentation.onboarding.q4heightweight.Q4HeightWeightScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 1.3 · Q3 Age (Pencil owS2i · "Q3 · Yaş").
 *
 * Step 3 of 7. Stepper card with [−]/number/[+] row, "YAŞ" caption,
 * a [VoltSlider] for fast scrubbing 13..90, and a tappable hint row
 * that opens a numeric input dialog for direct entry.
 *
 * On `Confirm`, ages outside the suggested range
 * ([Q3AgeState.SOFT_MIN_AGE]..[Q3AgeState.SOFT_MAX_AGE]) route to
 * `Q3AgeSoftWarningScreen` (Pencil V5 · iNSs8) — non-blocking, informational.
 */
class Q3AgeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: Q3AgeViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                Q3AgeSideEffect.NavigateBack -> {
                    if (!navigator.pop()) navigator.replace(Q2SexScreen())
                }
                Q3AgeSideEffect.NavigateToQ4HeightWeight ->
                    navigator.push(Q4HeightWeightScreen())
                Q3AgeSideEffect.NavigateToSoftWarning ->
                    navigator.push(Q3AgeSoftWarningScreen())
                is Q3AgeSideEffect.ShowError -> Unit // toast hook later
            }
        }

        Q3AgeContent(
            state = state,
            strings = strings,
            onBack = { viewModel.onIntent(Q3AgeIntent.BackTapped) },
            onDecrement = { viewModel.onIntent(Q3AgeIntent.Decrement) },
            onIncrement = { viewModel.onIntent(Q3AgeIntent.Increment) },
            onSlide = { viewModel.onIntent(Q3AgeIntent.SetAge(it)) },
            onOpenDialog = { viewModel.onIntent(Q3AgeIntent.OpenDialog) },
            onCloseDialog = { viewModel.onIntent(Q3AgeIntent.CloseDialog) },
            onSetAge = { viewModel.onIntent(Q3AgeIntent.SetAge(it)) },
            onConfirm = { viewModel.onIntent(Q3AgeIntent.Confirm) },
        )
    }
}

@Composable
private fun Q3AgeContent(
    state: Q3AgeState,
    strings: Strings,
    onBack: () -> Unit,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    onSlide: (Int) -> Unit,
    onOpenDialog: () -> Unit,
    onCloseDialog: () -> Unit,
    onSetAge: (Int) -> Unit,
    onConfirm: () -> Unit,
) {
    QuestionScaffold(
        progress = QuestionProgress(current = 3, total = 7),
        title = strings.q3Title,
        subtitle = strings.q3Subtitle,
        onBack = onBack,
        primaryCta = {
            VoltButton(
                text = strings.continueAction,
                onClick = onConfirm,
                enabled = state.isValid && !state.isSaving,
            )
        },
    ) {
        StepperCard(
            age = state.age,
            ageLabel = strings.q3AgeLabel,
            onDecrement = onDecrement,
            onIncrement = onIncrement,
            onSlide = onSlide,
            onTapNumber = onOpenDialog,
        )

        Spacer(Modifier.height(16.dp))

        HintRow(
            text = strings.q3KeyboardHint,
            onClick = onOpenDialog,
        )
    }

    if (state.showInputDialog) {
        AgeInputDialog(
            initial = state.age,
            strings = strings,
            onCancel = onCloseDialog,
            onConfirm = { typed ->
                onSetAge(typed)
                onCloseDialog()
            },
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Stepper card — surface1, outline, 24dp corner. Houses [−] / number / [+]
// row, "YAŞ" caption, and the VoltSlider with 13/90 range labels.
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun StepperCard(
    age: Int,
    ageLabel: String,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    onSlide: (Int) -> Unit,
    onTapNumber: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, RoundedCornerShape(24.dp))
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(102.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            StepperButton(
                isPrimary = false,
                contentDescription = "decrement",
                onClick = onDecrement,
            ) {
                MinusGlyph()
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(onClick = onTapNumber)
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = age.toString(),
                    style = VoltType.displayLarge.copy(
                        fontSize = 72.sp,
                        fontWeight = FontWeight.ExtraBold,
                    ),
                    color = VoltColors.onSurface,
                )
            }

            StepperButton(
                isPrimary = true,
                contentDescription = "increment",
                onClick = onIncrement,
            ) {
                PlusGlyph()
            }
        }

        Spacer(Modifier.height(4.dp))

        Text(
            text = ageLabel,
            style = VoltType.labelSmall.copy(
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
            ),
            color = VoltColors.onSurfaceMuted,
        )

        Spacer(Modifier.height(24.dp))

        VoltSlider(
            value = age.toFloat(),
            onValueChange = { onSlide(it.toInt()) },
            valueRange = Q3AgeState.MIN_AGE.toFloat()..Q3AgeState.MAX_AGE.toFloat(),
            modifier = Modifier.fillMaxWidth(),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = Q3AgeState.MIN_AGE.toString(),
                style = VoltType.bodyMedium.copy(fontSize = 12.sp),
                color = VoltColors.onSurfaceMuted,
            )
            Text(
                text = Q3AgeState.MAX_AGE.toString(),
                style = VoltType.bodyMedium.copy(fontSize = 12.sp),
                color = VoltColors.onSurfaceMuted,
            )
        }
    }
}

/** 44dp circular stepper button. Primary = volt fill (with onVolt glyph); secondary = surface2 + outline. */
@Composable
private fun StepperButton(
    isPrimary: Boolean,
    contentDescription: String,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val bg = if (isPrimary) VoltColors.volt else VoltColors.surface2
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(bg)
            .border(
                width = if (isPrimary) 0.dp else 1.dp,
                color = if (isPrimary) bg else VoltColors.outline,
                shape = CircleShape,
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
private fun MinusGlyph() {
    Canvas(modifier = Modifier.size(16.dp)) {
        drawLine(
            color = VoltColors.onSurface,
            start = Offset(0f, size.height / 2f),
            end = Offset(size.width, size.height / 2f),
            strokeWidth = size.height / 6f,
            cap = StrokeCap.Round,
        )
    }
}

@Composable
private fun PlusGlyph() {
    Canvas(modifier = Modifier.size(16.dp)) {
        val sw = size.height / 6f
        drawLine(
            color = VoltColors.onVolt,
            start = Offset(0f, size.height / 2f),
            end = Offset(size.width, size.height / 2f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = VoltColors.onVolt,
            start = Offset(size.width / 2f, 0f),
            end = Offset(size.width / 2f, size.height),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Hint row: tiny keypad icon + caption. Tap opens the input dialog.
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun HintRow(
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        KeypadGlyph()
        Text(
            text = text,
            style = VoltType.bodyMedium.copy(fontSize = 12.sp),
            color = VoltColors.onSurfaceMuted,
        )
    }
}

/** Tiny 14x14 keypad glyph — rounded rect outline with 6 dots inside. */
@Composable
private fun KeypadGlyph() {
    Canvas(modifier = Modifier.size(14.dp)) {
        val w = size.width
        val h = size.height
        // Outer rect outline via 4 lines (zero-dep, no path needed)
        val sw = w / 12f
        val r = w * 0.12f
        // Top
        drawLine(
            color = VoltColors.onSurfaceMuted,
            start = Offset(r, 0f),
            end = Offset(w - r, 0f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        // Bottom
        drawLine(
            color = VoltColors.onSurfaceMuted,
            start = Offset(r, h),
            end = Offset(w - r, h),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        // Left
        drawLine(
            color = VoltColors.onSurfaceMuted,
            start = Offset(0f, r),
            end = Offset(0f, h - r),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        // Right
        drawLine(
            color = VoltColors.onSurfaceMuted,
            start = Offset(w, r),
            end = Offset(w, h - r),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        // 6 dots in 2x3 grid
        val dotR = w * 0.06f
        val xs = listOf(w * 0.30f, w * 0.50f, w * 0.70f)
        val ys = listOf(h * 0.38f, h * 0.66f)
        ys.forEach { y ->
            xs.forEach { x ->
                drawCircle(
                    color = VoltColors.onSurfaceMuted,
                    radius = dotR,
                    center = Offset(x, y),
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Direct-entry dialog. Number keypad. Confirm clamps to [13..90].
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun AgeInputDialog(
    initial: Int,
    strings: Strings,
    onCancel: () -> Unit,
    onConfirm: (Int) -> Unit,
) {
    var input by remember { mutableStateOf(initial.toString()) }
    LaunchedEffect(initial) { input = initial.toString() }

    val parsed = input.toIntOrNull()
    val isValid = parsed != null && parsed in Q3AgeState.MIN_AGE..Q3AgeState.MAX_AGE

    AlertDialog(
        onDismissRequest = onCancel,
        containerColor = VoltColors.surface1,
        titleContentColor = VoltColors.onSurface,
        textContentColor = VoltColors.onSurfaceMuted,
        title = {
            Text(
                text = strings.q3DialogTitle,
                style = VoltType.titleMedium,
                color = VoltColors.onSurface,
            )
        },
        text = {
            OutlinedTextField(
                value = input,
                onValueChange = { raw ->
                    // Digits only, max 3 chars.
                    input = raw.filter { it.isDigit() }.take(3)
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val v = parsed?.coerceIn(Q3AgeState.MIN_AGE, Q3AgeState.MAX_AGE)
                        ?: initial
                    onConfirm(v)
                },
                enabled = isValid,
            ) {
                Text(
                    text = strings.q3DialogConfirm,
                    color = if (isValid) VoltColors.volt else VoltColors.onSurfaceMuted,
                    style = VoltType.labelLarge,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(
                    text = strings.q3DialogCancel,
                    color = VoltColors.onSurfaceMuted,
                    style = VoltType.labelLarge,
                )
            }
        },
    )
}
