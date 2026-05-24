package org.betech.fitnes.presentation.onboarding.settingspregmode

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.typography.VoltType
import org.betech.fitnes.localization.LocalStrings
import org.betech.fitnes.localization.Strings
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 29 · Settings · Pregnancy Mode (Pencil vUAuh · "Hamiləlik rejimi tənzimi").
 *
 * Static management surface for the pregnancy/postpartum safe-template mode.
 * Layout:
 *   - Top bar: back chevron + centered title
 *   - Volt-tinted Status card (AKTIV pill + title + live dot + 3 stat columns
 *     + status sub-row about AI hard-stop)
 *   - Section header "İdarə et"
 *   - 4 navigation rows (surface1 cards: icon · title · subtitle · chevron
 *     · optional trailing label)
 *   - Destructive "Sil" CTA (red) + muted footer
 *
 * Invariant (CLAUDE.md): pregnancy_postpartum=true is an AI hard-stop. This
 * screen NEVER triggers AI plan generation; it only manages the curated
 * static template state. The "Sil" CTA re-enables the standard plan path —
 * standard plan generation itself happens elsewhere, not from this screen.
 *
 * NOTE: row destination screens (period editor, postpartum, doctor notes)
 * are not yet built in Phase 3; effects pop back for now and the real
 * navigation lands when those screens ship.
 */
class SettingsPregModeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: SettingsPregModeViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                SettingsPregModeSideEffect.NavigateBack -> {
                    if (navigator.canPop) navigator.pop()
                }
                // Phase-3 placeholder: destination screens land in later iters.
                SettingsPregModeSideEffect.NavigateToChangePeriod,
                SettingsPregModeSideEffect.NavigateToPostpartum,
                SettingsPregModeSideEffect.NavigateToDoctorNotes,
                SettingsPregModeSideEffect.ConfirmDisable -> Unit
                is SettingsPregModeSideEffect.ToggleReminders -> Unit
            }
        }

        SettingsPregModeContent(
            state = state,
            strings = strings,
            onIntent = viewModel::onIntent,
        )
    }
}

@Composable
private fun SettingsPregModeContent(
    state: SettingsPregModeState,
    strings: Strings,
    onIntent: (SettingsPregModeIntent) -> Unit,
) {
    val rows = listOf(
        RowSpec(
            glyph = RowGlyph.Calendar,
            title = strings.settingsPregModeRowChangePeriodTitle,
            subtitle = strings.settingsPregModeRowChangePeriodSubtitle,
            trailing = strings.settingsPregModeRowChangePeriodTrailing,
            intent = SettingsPregModeIntent.ChangePeriodTapped,
        ),
        RowSpec(
            glyph = RowGlyph.Baby,
            title = strings.settingsPregModeRowPostpartumTitle,
            subtitle = strings.settingsPregModeRowPostpartumSubtitle,
            trailing = null,
            intent = SettingsPregModeIntent.PostpartumTapped,
        ),
        RowSpec(
            glyph = RowGlyph.Bell,
            title = strings.settingsPregModeRowRemindersTitle,
            subtitle = strings.settingsPregModeRowRemindersSubtitle,
            trailing = if (state.remindersOn)
                strings.settingsPregModeRowRemindersOn
            else
                strings.settingsPregModeRowRemindersOff,
            intent = SettingsPregModeIntent.RemindersTapped,
        ),
        RowSpec(
            glyph = RowGlyph.Note,
            title = strings.settingsPregModeRowDoctorTitle,
            subtitle = strings.settingsPregModeRowDoctorSubtitle,
            trailing = null,
            intent = SettingsPregModeIntent.DoctorNotesTapped,
        ),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VoltColors.surface0),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = 24.dp),
        ) {
            item {
                // ── Top bar ───────────────────────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BackChevronButton(
                        onClick = { onIntent(SettingsPregModeIntent.BackTapped) },
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = strings.settingsPregModeTitle,
                        style = VoltType.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                        ),
                        color = VoltColors.onSurface,
                    )
                }
                Spacer(Modifier.height(16.dp))

                // ── Status card ───────────────────────────────────────────
                StatusCard(state = state, strings = strings)

                Spacer(Modifier.height(24.dp))

                // ── Section header "İdarə et" ─────────────────────────────
                Text(
                    text = strings.settingsPregModeSectionManage,
                    style = VoltType.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 2.sp,
                    ),
                    color = VoltColors.onSurfaceMuted,
                )
                Spacer(Modifier.height(12.dp))
            }

            items(rows) { row ->
                NavigationRow(spec = row, onClick = { onIntent(row.intent) })
                Spacer(Modifier.height(10.dp))
            }

            item {
                Spacer(Modifier.height(16.dp))
                DestructiveButton(
                    text = strings.settingsPregModeCtaDisable,
                    onClick = { onIntent(SettingsPregModeIntent.DisableTapped) },
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = strings.settingsPregModeDisableFooter,
                    style = VoltType.bodyMedium.copy(fontSize = 12.sp),
                    color = VoltColors.onSurfaceMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Status card
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun StatusCard(state: SettingsPregModeState, strings: Strings) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(VoltColors.volt.copy(alpha = 0.10f))
            .border(1.dp, VoltColors.volt.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
            .padding(horizontal = 18.dp, vertical = 18.dp),
    ) {
        // Header row: AKTIV pill + title + live dot
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(VoltColors.volt)
                    .padding(horizontal = 8.dp, vertical = 3.dp),
            ) {
                Text(
                    text = strings.settingsPregModeStatusPill,
                    style = VoltType.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 1.5.sp,
                    ),
                    color = VoltColors.onVolt,
                )
            }
            Text(
                text = strings.settingsPregModeCardTitle,
                style = VoltType.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                ),
                color = VoltColors.onSurface,
                modifier = Modifier.weight(1f),
            )
            // Live dot
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(VoltColors.success),
            )
        }

        Spacer(Modifier.height(16.dp))

        // 3 stat columns
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            StatColumn(
                label = strings.settingsPregModeStatPeriodLabel,
                value = strings.settingsPregModeStatPeriodValue,
                modifier = Modifier.weight(1f),
            )
            StatColumn(
                label = strings.settingsPregModeStatWeekLabel,
                value = strings.settingsPregModeStatWeekValue,
                modifier = Modifier.weight(1f),
            )
            StatColumn(
                label = strings.settingsPregModeStatStartLabel,
                value = strings.settingsPregModeStatStartValue,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(Modifier.height(16.dp))

        // Status sub-row
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(VoltColors.surface0.copy(alpha = 0.45f))
                .padding(horizontal = 12.dp, vertical = 10.dp),
        ) {
            Text(
                text = strings.settingsPregModeStatusSub,
                style = VoltType.bodyMedium.copy(fontSize = 12.sp),
                color = VoltColors.onSurfaceMuted,
            )
        }
    }
}

@Composable
private fun StatColumn(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = VoltType.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp,
                letterSpacing = 1.5.sp,
            ),
            color = VoltColors.onSurfaceMuted,
        )
        Text(
            text = value,
            style = VoltType.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
            ),
            color = VoltColors.onSurface,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Navigation row
// ─────────────────────────────────────────────────────────────────────────────

private data class RowSpec(
    val glyph: RowGlyph,
    val title: String,
    val subtitle: String,
    val trailing: String?,
    val intent: SettingsPregModeIntent,
)

private enum class RowGlyph { Calendar, Baby, Bell, Note }

@Composable
private fun NavigationRow(spec: RowSpec, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        RowIcon(glyph = spec.glyph)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = spec.title,
                style = VoltType.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                ),
                color = VoltColors.onSurface,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = spec.subtitle,
                style = VoltType.bodyMedium.copy(fontSize = 12.sp),
                color = VoltColors.onSurfaceMuted,
            )
        }
        if (spec.trailing != null) {
            Text(
                text = spec.trailing,
                style = VoltType.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                ),
                color = VoltColors.volt,
            )
        }
        Canvas(modifier = Modifier.size(12.dp)) { drawForwardChevron() }
    }
}

@Composable
private fun RowIcon(glyph: RowGlyph) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(VoltColors.surface2),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(18.dp)) {
            when (glyph) {
                RowGlyph.Calendar -> drawCalendarGlyph()
                RowGlyph.Baby -> drawBabyGlyph()
                RowGlyph.Bell -> drawBellGlyph()
                RowGlyph.Note -> drawNoteGlyph()
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Glyphs
// ─────────────────────────────────────────────────────────────────────────────

private fun DrawScope.drawCalendarGlyph() {
    val stroke = size.minDimension / 10f
    val volt = VoltColors.volt
    // Body rect
    val left = 0f
    val right = size.width
    val top = size.height * 0.20f
    val bottom = size.height
    drawLine(volt, Offset(left, top), Offset(right, top), stroke, StrokeCap.Round)
    drawLine(volt, Offset(left, top), Offset(left, bottom), stroke, StrokeCap.Round)
    drawLine(volt, Offset(right, top), Offset(right, bottom), stroke, StrokeCap.Round)
    drawLine(volt, Offset(left, bottom), Offset(right, bottom), stroke, StrokeCap.Round)
    // Top divider
    drawLine(
        volt,
        Offset(left, top + size.height * 0.18f),
        Offset(right, top + size.height * 0.18f),
        stroke,
        StrokeCap.Round,
    )
    // Hangers
    drawLine(volt, Offset(size.width * 0.28f, 0f), Offset(size.width * 0.28f, top + size.height * 0.10f), stroke, StrokeCap.Round)
    drawLine(volt, Offset(size.width * 0.72f, 0f), Offset(size.width * 0.72f, top + size.height * 0.10f), stroke, StrokeCap.Round)
}

private fun DrawScope.drawBabyGlyph() {
    val stroke = size.minDimension / 10f
    val volt = VoltColors.volt
    // Head circle
    drawCircle(
        color = volt,
        radius = size.minDimension * 0.22f,
        center = Offset(size.width / 2f, size.height * 0.32f),
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke),
    )
    // Body arc — simplified as two lines forming a heart-ish base
    drawLine(volt, Offset(size.width * 0.20f, size.height * 0.65f), Offset(size.width * 0.50f, size.height), stroke, StrokeCap.Round)
    drawLine(volt, Offset(size.width * 0.80f, size.height * 0.65f), Offset(size.width * 0.50f, size.height), stroke, StrokeCap.Round)
    drawLine(volt, Offset(size.width * 0.20f, size.height * 0.65f), Offset(size.width * 0.80f, size.height * 0.65f), stroke, StrokeCap.Round)
}

private fun DrawScope.drawBellGlyph() {
    val stroke = size.minDimension / 10f
    val volt = VoltColors.volt
    // Bell body — dome via two diagonals + base
    val top = size.height * 0.10f
    val bodyBottom = size.height * 0.75f
    drawLine(volt, Offset(size.width * 0.50f, top), Offset(size.width * 0.20f, bodyBottom), stroke, StrokeCap.Round)
    drawLine(volt, Offset(size.width * 0.50f, top), Offset(size.width * 0.80f, bodyBottom), stroke, StrokeCap.Round)
    drawLine(volt, Offset(size.width * 0.20f, bodyBottom), Offset(size.width * 0.80f, bodyBottom), stroke, StrokeCap.Round)
    // Clapper
    drawLine(volt, Offset(size.width * 0.42f, bodyBottom + size.height * 0.06f), Offset(size.width * 0.58f, bodyBottom + size.height * 0.06f), stroke, StrokeCap.Round)
}

private fun DrawScope.drawNoteGlyph() {
    val stroke = size.minDimension / 10f
    val volt = VoltColors.volt
    // Document outline
    val left = size.width * 0.18f
    val right = size.width * 0.82f
    val top = 0f
    val bottom = size.height
    drawLine(volt, Offset(left, top), Offset(right, top), stroke, StrokeCap.Round)
    drawLine(volt, Offset(left, top), Offset(left, bottom), stroke, StrokeCap.Round)
    drawLine(volt, Offset(right, top), Offset(right, bottom), stroke, StrokeCap.Round)
    drawLine(volt, Offset(left, bottom), Offset(right, bottom), stroke, StrokeCap.Round)
    // Three lines inside
    drawLine(volt, Offset(left + size.width * 0.12f, size.height * 0.30f), Offset(right - size.width * 0.12f, size.height * 0.30f), stroke, StrokeCap.Round)
    drawLine(volt, Offset(left + size.width * 0.12f, size.height * 0.52f), Offset(right - size.width * 0.12f, size.height * 0.52f), stroke, StrokeCap.Round)
    drawLine(volt, Offset(left + size.width * 0.12f, size.height * 0.74f), Offset(right - size.width * 0.30f, size.height * 0.74f), stroke, StrokeCap.Round)
}

private fun DrawScope.drawForwardChevron() {
    val sw = size.minDimension / 7f
    drawLine(
        color = VoltColors.onSurfaceMuted,
        start = Offset(size.width * 0.30f, 0f),
        end = Offset(size.width * 0.80f, size.height / 2f),
        strokeWidth = sw,
        cap = StrokeCap.Round,
    )
    drawLine(
        color = VoltColors.onSurfaceMuted,
        start = Offset(size.width * 0.80f, size.height / 2f),
        end = Offset(size.width * 0.30f, size.height),
        strokeWidth = sw,
        cap = StrokeCap.Round,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Atoms (back chevron + destructive button — mirrors SignoutConfirm pattern)
// ─────────────────────────────────────────────────────────────────────────────

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
    val sw = size.minDimension / 8f
    val midY = size.height / 2f
    val left = size.width * 0.25f
    val right = size.width * 0.75f
    drawLine(VoltColors.onSurface, Offset(right, 0f), Offset(left, midY), sw, StrokeCap.Round)
    drawLine(VoltColors.onSurface, Offset(left, midY), Offset(right, size.height), sw, StrokeCap.Round)
}

@Composable
private fun DestructiveButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = VoltColors.danger,
            contentColor = Color.White,
        ),
    ) {
        Text(text = text, style = VoltType.labelLarge)
    }
}
