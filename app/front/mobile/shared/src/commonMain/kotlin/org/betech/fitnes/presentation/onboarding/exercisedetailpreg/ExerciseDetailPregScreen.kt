package org.betech.fitnes.presentation.onboarding.exercisedetailpreg

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
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
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 25 · Exercise Detail (Pregnancy) (Pencil YZ38M).
 *
 * Reached from TodaySafeWorkout (CTA / row tap). Presents one curated
 * pregnancy-safe exercise: PREGNANCY-SAFE badge, video placeholder w/ centered
 * volt play button, title + 3 inline stats, trimester modification notice
 * (volt-tinted), focus-points checklist (3 items), primary "Tamamladım" CTA,
 * footer ghost links (Atla · Daha asan variant).
 *
 * Invariant (CLAUDE.md): curated static template only; NEVER triggers AI
 * plan/exercise generation. pregnancy_postpartum=true hard-stop carries
 * forward from the SafePlan branch entry.
 */
class ExerciseDetailPregScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: ExerciseDetailPregViewModel = koinViewModel()
        @Suppress("UNUSED_VARIABLE") val state by viewModel.collectAsState()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                ExerciseDetailPregSideEffect.NavigateBack -> navigator.pop()
                ExerciseDetailPregSideEffect.NavigateDone -> navigator.pop()
                is ExerciseDetailPregSideEffect.ShowToast -> Unit // transient; UI-only stub
            }
        }

        ExerciseDetailPregContent(
            strings = strings,
            onBack = { viewModel.onIntent(ExerciseDetailPregIntent.BackTapped) },
            onAlternative = { viewModel.onIntent(ExerciseDetailPregIntent.AlternativeTapped) },
            onPlay = { viewModel.onIntent(ExerciseDetailPregIntent.PlayTapped) },
            onDone = { viewModel.onIntent(ExerciseDetailPregIntent.DoneTapped) },
            onSkip = { viewModel.onIntent(ExerciseDetailPregIntent.SkipTapped) },
            onEasier = { viewModel.onIntent(ExerciseDetailPregIntent.EasierTapped) },
        )
    }
}

// ──────────────────────────────────────────────────────────────────────
// Content
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun ExerciseDetailPregContent(
    strings: Strings,
    onBack: () -> Unit,
    onAlternative: () -> Unit,
    onPlay: () -> Unit,
    onDone: () -> Unit,
    onSkip: () -> Unit,
    onEasier: () -> Unit,
) {
    val notes = listOf(
        strings.exerciseDetailPregNote1,
        strings.exerciseDetailPregNote2,
        strings.exerciseDetailPregNote3,
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
            // ── Top bar: back chevron + "⇄ Alternativ" link ──────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                BackChevronButton(onClick = onBack)
                AlternativeLink(text = strings.exerciseDetailPregAlternative, onClick = onAlternative)
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Spacer(Modifier.height(0.dp))

                // Volt pill badge
                VoltPill(text = strings.exerciseDetailPregSafeBadge)

                // Video placeholder card
                MediaCard(caption = strings.exerciseDetailPregMediaCaption, onPlay = onPlay)

                // Title + 3 stats
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = strings.exerciseDetailPregTitle,
                        style = VoltType.displayMedium.copy(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                        color = VoltColors.onSurface,
                    )
                    StatsRow(
                        s1 = strings.exerciseDetailPregStat1,
                        s2 = strings.exerciseDetailPregStat2,
                        s3 = strings.exerciseDetailPregStat3,
                    )
                }

                // Volt warning notice (trimester modification)
                ModificationNotice(
                    title = strings.exerciseDetailPregModTitle,
                    body = strings.exerciseDetailPregModBody,
                )

                // Notes section header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = strings.exerciseDetailPregNotesTitle,
                        style = VoltType.titleMedium.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                        ),
                        color = VoltColors.onSurface,
                    )
                    Text(
                        text = strings.exerciseDetailPregNotesCount,
                        style = VoltType.bodyMedium.copy(fontSize = 11.sp),
                        color = VoltColors.onSurfaceMuted,
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    notes.forEach { note -> NoteRow(text = note) }
                }

                Spacer(Modifier.height(0.dp))
            }

            // ── CTA + footer links ──────────────────────────────────
            VoltButton(
                text = strings.exerciseDetailPregCtaDone,
                onClick = onDone,
            )
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                GhostLink(text = strings.exerciseDetailPregSkip, onClick = onSkip)
                Text(
                    text = "·",
                    style = VoltType.bodyMedium.copy(fontSize = 12.sp),
                    color = VoltColors.onSurfaceMuted,
                    modifier = Modifier.padding(horizontal = 12.dp),
                )
                GhostLink(text = strings.exerciseDetailPregEasier, onClick = onEasier)
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

// ──────────────────────────────────────────────────────────────────────
// Volt pill badge — small accent token ("PREGNANCY-SAFE")
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun VoltPill(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(VoltColors.volt.copy(alpha = 0.16f))
            .border(1.dp, VoltColors.volt.copy(alpha = 0.45f), RoundedCornerShape(999.dp))
            .padding(horizontal = 12.dp, vertical = 5.dp),
    ) {
        Text(
            text = text,
            style = VoltType.labelLarge.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
            ),
            color = VoltColors.volt,
        )
    }
}

// ──────────────────────────────────────────────────────────────────────
// Media card — surface1, ~240dp tall, centered volt play button + caption
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun MediaCard(caption: String, onPlay: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(VoltColors.volt)
                    .clickable(onClick = onPlay),
                contentAlignment = Alignment.Center,
            ) {
                Canvas(modifier = Modifier.size(24.dp)) { drawPlayTriangle() }
            }
            Text(
                text = caption,
                style = VoltType.bodyMedium.copy(fontSize = 11.sp, fontWeight = FontWeight.Medium),
                color = VoltColors.onSurfaceMuted,
            )
        }
    }
}

private fun DrawScope.drawPlayTriangle() {
    val w = size.width
    val h = size.height
    val path = Path().apply {
        moveTo(w * 0.25f, h * 0.15f)
        lineTo(w * 0.85f, h * 0.5f)
        lineTo(w * 0.25f, h * 0.85f)
        close()
    }
    drawPath(path, color = VoltColors.onVolt)
}

// ──────────────────────────────────────────────────────────────────────
// Stats row — 3 inline chips on surface1 background
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun StatsRow(s1: String, s2: String, s3: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        StatChip(modifier = Modifier.weight(1f), text = s1)
        StatChip(modifier = Modifier.weight(1f), text = s2)
        StatChip(modifier = Modifier.weight(1f), text = s3)
    }
}

@Composable
private fun StatChip(modifier: Modifier = Modifier, text: String) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = VoltType.bodyMedium.copy(fontSize = 11.sp, fontWeight = FontWeight.Medium),
            color = VoltColors.onSurface,
        )
    }
}

// ──────────────────────────────────────────────────────────────────────
// Modification notice — volt-tinted callout box w/ warning glyph + title + body
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun ModificationNotice(title: String, body: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(VoltColors.volt.copy(alpha = 0.08f))
            .border(1.dp, VoltColors.volt.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .padding(horizontal = 14.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(VoltColors.volt),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.size(14.dp)) { drawWarningGlyph() }
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = title,
                style = VoltType.titleMedium.copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = VoltColors.volt,
            )
            Text(
                text = body,
                style = VoltType.bodyMedium.copy(fontSize = 12.sp),
                color = VoltColors.onSurface,
            )
        }
    }
}

private fun DrawScope.drawWarningGlyph() {
    val stroke = size.minDimension / 7f
    val cx = size.width / 2f
    drawLine(
        color = VoltColors.onVolt,
        start = Offset(cx, size.height * 0.18f),
        end = Offset(cx, size.height * 0.62f),
        strokeWidth = stroke, cap = StrokeCap.Round,
    )
    drawCircle(
        color = VoltColors.onVolt,
        radius = stroke * 0.6f,
        center = Offset(cx, size.height * 0.82f),
    )
}

// ──────────────────────────────────────────────────────────────────────
// Note row — surface1 card, volt check disc + body text
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun NoteRow(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(VoltColors.volt.copy(alpha = 0.16f))
                .border(1.dp, VoltColors.volt.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.size(12.dp)) { drawCheckGlyph() }
        }
        Text(
            text = text,
            style = VoltType.bodyMedium.copy(fontSize = 13.sp),
            color = VoltColors.onSurface,
            modifier = Modifier.weight(1f),
        )
    }
}

private fun DrawScope.drawCheckGlyph() {
    val stroke = size.minDimension / 6f
    drawLine(
        color = VoltColors.volt,
        start = Offset(size.width * 0.15f, size.height * 0.55f),
        end = Offset(size.width * 0.42f, size.height * 0.82f),
        strokeWidth = stroke, cap = StrokeCap.Round,
    )
    drawLine(
        color = VoltColors.volt,
        start = Offset(size.width * 0.42f, size.height * 0.82f),
        end = Offset(size.width * 0.88f, size.height * 0.22f),
        strokeWidth = stroke, cap = StrokeCap.Round,
    )
}

// ──────────────────────────────────────────────────────────────────────
// Atoms — top-bar back chevron, alternative link, footer ghost link
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

@Composable
private fun AlternativeLink(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Canvas(modifier = Modifier.size(12.dp)) { drawSwapGlyph() }
        Text(
            text = text,
            style = VoltType.bodyMedium.copy(fontSize = 12.sp, fontWeight = FontWeight.Medium),
            color = VoltColors.volt,
        )
    }
}

private fun DrawScope.drawSwapGlyph() {
    val s = size.minDimension / 9f
    // top arrow ←
    drawLine(
        color = VoltColors.volt,
        start = Offset(size.width * 0.15f, size.height * 0.3f),
        end = Offset(size.width * 0.85f, size.height * 0.3f),
        strokeWidth = s, cap = StrokeCap.Round,
    )
    drawLine(
        color = VoltColors.volt,
        start = Offset(size.width * 0.15f, size.height * 0.3f),
        end = Offset(size.width * 0.32f, size.height * 0.15f),
        strokeWidth = s, cap = StrokeCap.Round,
    )
    // bottom arrow →
    drawLine(
        color = VoltColors.volt,
        start = Offset(size.width * 0.15f, size.height * 0.7f),
        end = Offset(size.width * 0.85f, size.height * 0.7f),
        strokeWidth = s, cap = StrokeCap.Round,
    )
    drawLine(
        color = VoltColors.volt,
        start = Offset(size.width * 0.85f, size.height * 0.7f),
        end = Offset(size.width * 0.68f, size.height * 0.85f),
        strokeWidth = s, cap = StrokeCap.Round,
    )
}

@Composable
private fun GhostLink(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
    ) {
        Text(
            text = text,
            style = VoltType.bodyMedium.copy(fontSize = 12.sp, fontWeight = FontWeight.Medium),
            color = VoltColors.onSurfaceMuted,
        )
    }
}

@Suppress("unused")
private fun DrawScope.drawStrokedRectPlaceholder() {
    drawRect(
        color = VoltColors.outline,
        style = Stroke(width = 1f),
    )
}
