package org.betech.fitnes.presentation.onboarding.todaysafeworkout

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.components.VoltButton
import org.betech.fitnes.designsystem.typography.VoltType
import org.betech.fitnes.localization.LocalStrings
import org.betech.fitnes.localization.Strings
import org.betech.fitnes.presentation.onboarding.exercisedetailpreg.ExerciseDetailPregScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 24 · Today's Safe Workout (Pencil QHsnW · "Yumşaq mobility + nəfəs").
 *
 * Entry from SafePlan → presents a curated static session: session header card
 * (eyebrow + title + 3 metric chips), 5 exercise list rows with chevrons, a
 * primary CTA to start the workout, and a medical-caution footer.
 *
 * Invariant (CLAUDE.md): the session is a curated static template; this
 * screen NEVER triggers AI plan/exercise generation. pregnancy_postpartum=true
 * is a hard-stop and the trimester label is the only personalised header value.
 */
class TodaySafeWorkoutScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: TodaySafeWorkoutViewModel = koinViewModel()
        @Suppress("UNUSED_VARIABLE") val state by viewModel.collectAsState()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                TodaySafeWorkoutSideEffect.NavigateBack -> navigator.pop()
                TodaySafeWorkoutSideEffect.NavigateToExerciseDetail ->
                    navigator.push(ExerciseDetailPregScreen())
            }
        }

        TodaySafeWorkoutContent(
            strings = strings,
            onBack = { viewModel.onIntent(TodaySafeWorkoutIntent.BackTapped) },
            onStart = { viewModel.onIntent(TodaySafeWorkoutIntent.StartWorkoutTapped) },
            onExerciseTap = { viewModel.onIntent(TodaySafeWorkoutIntent.ExerciseTapped(it)) },
        )
    }
}

// ──────────────────────────────────────────────────────────────────────
// Content
// ──────────────────────────────────────────────────────────────────────

private data class ExerciseRow(
    val index: Int,
    val title: String,
    val stats: String,
    val badge: String?,
)

@Composable
private fun TodaySafeWorkoutContent(
    strings: Strings,
    onBack: () -> Unit,
    onStart: () -> Unit,
    onExerciseTap: (Int) -> Unit,
) {
    val exercises = listOf(
        ExerciseRow(1, strings.todaySafeWorkoutEx1Title, strings.todaySafeWorkoutEx1Stats, null),
        ExerciseRow(2, strings.todaySafeWorkoutEx2Title, strings.todaySafeWorkoutEx2Stats, strings.todaySafeWorkoutEx2Badge),
        ExerciseRow(3, strings.todaySafeWorkoutEx3Title, strings.todaySafeWorkoutEx3Stats, null),
        ExerciseRow(4, strings.todaySafeWorkoutEx4Title, strings.todaySafeWorkoutEx4Stats, null),
        ExerciseRow(5, strings.todaySafeWorkoutEx5Title, strings.todaySafeWorkoutEx5Stats, null),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VoltColors.surface0),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = 24.dp),
        ) {
            // ── Top bar: back chevron + trimester subtitle ──────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                BackChevronButton(onClick = onBack)
                Text(
                    text = strings.todaySafeWorkoutTrimesterLabel,
                    style = VoltType.bodyMedium.copy(fontSize = 12.sp),
                    color = VoltColors.onSurfaceMuted,
                )
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                item {
                    Spacer(Modifier.height(8.dp))
                    SessionHeaderCard(strings = strings)
                    Spacer(Modifier.height(24.dp))
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = strings.todaySafeWorkoutSectionExercises,
                            style = VoltType.titleMedium.copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                            ),
                            color = VoltColors.onSurface,
                        )
                        VoltPill(text = strings.todaySafeWorkoutBadgePregSafe)
                    }
                    Spacer(Modifier.height(12.dp))
                }

                items(exercises) { ex ->
                    ExerciseListRow(
                        row = ex,
                        onClick = { onExerciseTap(ex.index) },
                    )
                    Spacer(Modifier.height(12.dp))
                }

                item { Spacer(Modifier.height(8.dp)) }
            }

            // ── CTA + footer ─────────────────────────────────────────
            VoltButton(
                text = strings.todaySafeWorkoutCtaStart,
                onClick = onStart,
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = strings.todaySafeWorkoutFooter,
                style = VoltType.bodyMedium.copy(fontSize = 11.sp),
                color = VoltColors.onSurfaceMuted,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

// ──────────────────────────────────────────────────────────────────────
// Session header card — surface1, eyebrow row + title + 3 chips
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun SessionHeaderCard(strings: Strings) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Eyebrow row: BUGÜN (volt) · intensity (muted) · date (right)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = strings.todaySafeWorkoutEyebrowToday,
                    style = VoltType.labelLarge.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                    ),
                    color = VoltColors.volt,
                )
                Text(
                    text = "·",
                    style = VoltType.bodyMedium.copy(fontSize = 11.sp),
                    color = VoltColors.onSurfaceMuted,
                )
                Text(
                    text = strings.todaySafeWorkoutEyebrowIntensity,
                    style = VoltType.bodyMedium.copy(fontSize = 11.sp),
                    color = VoltColors.onSurfaceMuted,
                )
            }
            Text(
                text = strings.todaySafeWorkoutDateLabel,
                style = VoltType.bodyMedium.copy(fontSize = 11.sp),
                color = VoltColors.onSurfaceMuted,
            )
        }

        Text(
            text = strings.todaySafeWorkoutSessionTitle,
            style = VoltType.displayMedium.copy(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = VoltColors.onSurface,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            HeaderChip(modifier = Modifier.weight(1f), text = strings.todaySafeWorkoutChipDuration)
            HeaderChip(modifier = Modifier.weight(1f), text = strings.todaySafeWorkoutChipCount)
            HeaderChip(modifier = Modifier.weight(1f), text = strings.todaySafeWorkoutChipRpe)
        }
    }
}

@Composable
private fun HeaderChip(modifier: Modifier = Modifier, text: String) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(VoltColors.surface2)
            .border(1.dp, VoltColors.outline, RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = VoltType.bodyMedium.copy(fontSize = 12.sp, fontWeight = FontWeight.Medium),
            color = VoltColors.onSurface,
        )
    }
}

// ──────────────────────────────────────────────────────────────────────
// Volt pill — small accent badge ("Pregnancy-safe")
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun VoltPill(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(VoltColors.volt.copy(alpha = 0.16f))
            .border(1.dp, VoltColors.volt.copy(alpha = 0.45f), RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            text = text,
            style = VoltType.labelLarge.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
            ),
            color = VoltColors.volt,
        )
    }
}

// ──────────────────────────────────────────────────────────────────────
// Exercise list row — surface1 card, number + dot icon + title + stats + chevron
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun ExerciseListRow(row: ExerciseRow, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Number badge
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(VoltColors.surface2)
                .border(1.dp, VoltColors.outline, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = row.index.toString(),
                style = VoltType.labelLarge.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = VoltColors.onSurface,
            )
        }

        // Icon disc (decorative volt dot — exercise glyph placeholder)
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(VoltColors.volt.copy(alpha = 0.14f))
                .border(1.dp, VoltColors.volt.copy(alpha = 0.35f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(VoltColors.volt),
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = row.title,
                    style = VoltType.titleMedium.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = VoltColors.onSurface,
                )
                if (row.badge != null) {
                    SmallVoltBadge(text = row.badge)
                }
            }
            Text(
                text = row.stats,
                style = VoltType.bodyMedium.copy(fontSize = 12.sp),
                color = VoltColors.onSurfaceMuted,
            )
        }

        Canvas(modifier = Modifier.size(12.dp)) { drawForwardChevron() }
    }
}

@Composable
private fun SmallVoltBadge(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(VoltColors.volt.copy(alpha = 0.16f))
            .border(1.dp, VoltColors.volt.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp),
    ) {
        Text(
            text = text,
            style = VoltType.labelLarge.copy(
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = VoltColors.volt,
        )
    }
}

// ──────────────────────────────────────────────────────────────────────
// Atoms — back chevron + forward chevron
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun BackChevronButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(14.dp)) { drawBackChevron() }
    }
}

private fun DrawScope.drawBackChevron() {
    val strokeWidth = size.minDimension / 8f
    val midY = size.height / 2f
    val left = size.width * 0.25f
    val right = size.width * 0.75f
    drawLine(
        color = VoltColors.onSurface,
        start = Offset(right, 0f),
        end = Offset(left, midY),
        strokeWidth = strokeWidth, cap = StrokeCap.Round,
    )
    drawLine(
        color = VoltColors.onSurface,
        start = Offset(left, midY),
        end = Offset(right, size.height),
        strokeWidth = strokeWidth, cap = StrokeCap.Round,
    )
}

private fun DrawScope.drawForwardChevron() {
    val strokeWidth = size.minDimension / 8f
    val midY = size.height / 2f
    val left = size.width * 0.25f
    val right = size.width * 0.75f
    drawLine(
        color = VoltColors.onSurfaceMuted,
        start = Offset(left, 0f),
        end = Offset(right, midY),
        strokeWidth = strokeWidth, cap = StrokeCap.Round,
    )
    drawLine(
        color = VoltColors.onSurfaceMuted,
        start = Offset(right, midY),
        end = Offset(left, size.height),
        strokeWidth = strokeWidth, cap = StrokeCap.Round,
    )
}
