package org.betech.fitnes.presentation.onboarding.parentalbottomsheet

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * V7 · Parental Notice — Privacy Policy bottom-sheet (Pencil P5mDxB).
 *
 * Rendered as a full-screen Voyager [Screen] (not Material3
 * `ModalBottomSheet` — avoiding Material's experimental sheet API keeps
 * the KMP surface stable across iOS/Android Compose targets). Composition:
 *
 *  - 0x99 surface0 scrim filling the screen (tap → dismiss).
 *  - Bottom-anchored sheet card (surface1, 24dp top corners), max ~88%
 *    of screen height; inner column is vertically scrollable.
 *  - Header row (eyebrow + title) with close `X` aligned top-right.
 *  - Metadata row (updated date · language tag).
 *  - 4 numbered sections rendered as title + body pairs.
 *  - Footer hint + primary VoltButton "Anladım".
 *
 * Doubles as the Terms-of-use stand-in until dedicated Terms copy lands.
 */
class ParentalBottomSheetScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: ParentalBottomSheetViewModel = koinViewModel()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                ParentalBottomSheetSideEffect.Dismiss -> navigator.pop()
            }
        }

        ParentalBottomSheetContent(
            strings = strings,
            onClose = { viewModel.onIntent(ParentalBottomSheetIntent.CloseTapped) },
            onScrim = { viewModel.onIntent(ParentalBottomSheetIntent.ScrimTapped) },
            onPrimary = { viewModel.onIntent(ParentalBottomSheetIntent.PrimaryTapped) },
        )
    }
}

@Composable
private fun ParentalBottomSheetContent(
    strings: Strings,
    onClose: () -> Unit,
    onScrim: () -> Unit,
    onPrimary: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            // Translucent scrim — tap-to-dismiss.
            .background(Color(0xCC000000))
            .clickable(onClick = onScrim),
    ) {
        // Sheet card — bottom-anchored. We block clickable propagation by
        // attaching a no-op clickable to the sheet itself.
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(VoltColors.surface1)
                .border(
                    width = 1.dp,
                    color = VoltColors.outline,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                )
                .clickable(enabled = false, onClick = {})
                .windowInsetsPadding(WindowInsets.systemBars),
        ) {
            // Drag handle.
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.CenterHorizontally)
                    .size(width = 36.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(VoltColors.outlineStrong),
            )

            Spacer(Modifier.height(12.dp))

            // ── Header row ────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = strings.parentalSheetEyebrow,
                        style = VoltType.labelLarge.copy(
                            fontSize = 11.sp,
                            letterSpacing = 1.4.sp,
                            fontWeight = FontWeight.SemiBold,
                        ),
                        color = VoltColors.volt,
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = strings.parentalSheetTitle,
                        style = VoltType.displayMedium.copy(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                        color = VoltColors.onSurface,
                    )
                }
                CloseButton(onClick = onClose)
            }

            Spacer(Modifier.height(12.dp))

            // Metadata row.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MetaChip(text = strings.parentalSheetUpdatedLabel)
                MetaChip(text = strings.parentalSheetLangLabel)
            }

            Spacer(Modifier.height(16.dp))

            // ── Body (scrollable) ─────────────────────────────────────
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
            ) {
                Section(
                    title = strings.parentalSheetSection1Title,
                    body = strings.parentalSheetSection1Body,
                )
                Section(
                    title = strings.parentalSheetSection2Title,
                    body = strings.parentalSheetSection2Body,
                )
                Section(
                    title = strings.parentalSheetSection3Title,
                    body = strings.parentalSheetSection3Body,
                )
                Section(
                    title = strings.parentalSheetSection4Title,
                    body = strings.parentalSheetSection4Body,
                )
                Spacer(Modifier.height(8.dp))
            }

            // Footer hint.
            Text(
                text = strings.parentalSheetFooterHint,
                style = VoltType.bodyMedium.copy(fontSize = 12.sp),
                color = VoltColors.onSurfaceMuted,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
            )

            // Primary CTA.
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                VoltButton(
                    text = strings.parentalSheetCtaClose,
                    onClick = onPrimary,
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

// ──────────────────────────────────────────────────────────────────────
// Section — title + body block
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun Section(title: String, body: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Text(
            text = title,
            style = VoltType.labelLarge.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            color = VoltColors.onSurface,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = body,
            style = VoltType.bodyMedium.copy(fontSize = 13.sp, lineHeight = 19.sp),
            color = VoltColors.onSurfaceMuted,
        )
    }
}

// ──────────────────────────────────────────────────────────────────────
// Metadata chip — surface2 pill, muted text
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun MetaChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(VoltColors.surface2)
            .border(1.dp, VoltColors.outline, RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
    ) {
        Text(
            text = text,
            style = VoltType.bodyMedium.copy(fontSize = 11.sp),
            color = VoltColors.onSurfaceMuted,
        )
    }
}

// ──────────────────────────────────────────────────────────────────────
// Close (X) button — 36dp circle, onSurface stroke
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun CloseButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(VoltColors.surface2)
            .border(1.dp, VoltColors.outline, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(14.dp)) { drawClose() }
    }
}

private fun DrawScope.drawClose() {
    val sw = size.minDimension / 8f
    drawLine(
        color = VoltColors.onSurface,
        start = Offset(0f, 0f),
        end = Offset(size.width, size.height),
        strokeWidth = sw,
        cap = StrokeCap.Round,
    )
    drawLine(
        color = VoltColors.onSurface,
        start = Offset(size.width, 0f),
        end = Offset(0f, size.height),
        strokeWidth = sw,
        cap = StrokeCap.Round,
    )
}
