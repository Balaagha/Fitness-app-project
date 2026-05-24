package org.betech.fitnes.presentation.onboarding.q3agesoftwarning

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
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
import org.betech.fitnes.presentation.onboarding.q4heightweight.Q4HeightWeightScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/** V5 · Q3 Age Soft Warning (Pencil iNSs8). Out-of-range age (75) with non-blocking notice. */
class Q3AgeSoftWarningScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: Q3AgeSoftWarningViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                Q3AgeSoftWarningSideEffect.NavigateBack -> { navigator.pop() }
                Q3AgeSoftWarningSideEffect.NavigateToQ4 -> navigator.push(Q4HeightWeightScreen())
            }
        }

        QuestionScaffold(
            progress = QuestionProgress(current = 3, total = 7),
            title = strings.q3Title,
            subtitle = strings.q3Subtitle,
            onBack = { viewModel.onIntent(Q3AgeSoftWarningIntent.BackTapped) },
            primaryCta = {
                VoltButton(
                    text = strings.continueAction,
                    onClick = { viewModel.onIntent(Q3AgeSoftWarningIntent.ContinueTapped) },
                    enabled = true,
                )
            },
        ) {
            StepperCard(
                age = state.age,
                strings = strings,
                onSlide = { viewModel.onIntent(Q3AgeSoftWarningIntent.AgeChanged(it)) },
            )

            Spacer(Modifier.height(20.dp))

            SoftWarningBanner(text = strings.q3SoftWarningText)
        }
    }
}

@Composable
private fun StepperCard(
    age: Int,
    strings: Strings,
    onSlide: (Int) -> Unit,
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
            modifier = Modifier.fillMaxWidth().height(102.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            CircleGlyph(false)
            Text(
                text = age.toString(),
                style = VoltType.displayLarge.copy(fontSize = 72.sp, fontWeight = FontWeight.ExtraBold),
                color = VoltColors.onSurface,
            )
            CircleGlyph(true)
        }
        Text(
            text = strings.q3AgeLabel,
            style = VoltType.labelSmall.copy(
                fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp,
            ),
            color = VoltColors.onSurfaceMuted,
        )
        Spacer(Modifier.height(24.dp))
        VoltSlider(
            value = age.toFloat(),
            onValueChange = { onSlide(it.toInt()) },
            valueRange = 13f..90f,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("13", style = VoltType.bodyMedium.copy(fontSize = 12.sp), color = VoltColors.onSurfaceMuted)
            Text("90", style = VoltType.bodyMedium.copy(fontSize = 12.sp), color = VoltColors.onSurfaceMuted)
        }
    }
}

@Composable
private fun CircleGlyph(isPlus: Boolean) {
    val bg = if (isPlus) VoltColors.volt else VoltColors.surface2
    val fg = if (isPlus) VoltColors.onVolt else VoltColors.onSurface
    Box(
        modifier = Modifier.size(44.dp).clip(CircleShape).background(bg)
            .border(width = if (isPlus) 0.dp else 1.dp, color = if (isPlus) bg else VoltColors.outline, shape = CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(16.dp)) {
            val sw = size.height / 6f
            drawLine(fg, Offset(0f, size.height / 2f), Offset(size.width, size.height / 2f), sw, StrokeCap.Round)
            if (isPlus) drawLine(fg, Offset(size.width / 2f, 0f), Offset(size.width / 2f, size.height), sw, StrokeCap.Round)
        }
    }
}

@Composable
private fun SoftWarningBanner(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(VoltColors.volt.copy(alpha = 0.10f))
            .border(1.dp, VoltColors.volt.copy(alpha = 0.75f), RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier.size(28.dp).clip(CircleShape).background(VoltColors.volt),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                "!",
                style = VoltType.labelLarge.copy(fontWeight = FontWeight.ExtraBold, fontSize = 16.sp),
                color = VoltColors.onVolt,
            )
        }
        Text(
            text = text,
            style = VoltType.bodyMedium.copy(fontSize = 13.sp),
            color = VoltColors.onSurface,
        )
    }
}
