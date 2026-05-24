package org.betech.fitnes.presentation.onboarding.emailverify

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.delay
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.typography.VoltType
import org.betech.fitnes.localization.LocalStrings
import org.betech.fitnes.localization.Strings
import org.betech.fitnes.presentation.onboarding.paywall.PaywallScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 17 · Email Verify (Pencil zREhj).
 *
 * 6-digit OTP entry after email signup.
 * - Surface0 bg, back chevron, centred envelope icon + title + subtitle.
 * - Six visual cells driven by a single hidden BasicTextField (numeric).
 *   Any cell tap → focus the hidden field. Auto-submits on 6th digit.
 * - 45s resend cooldown with mm:ss countdown; becomes a volt-coloured tap
 *   when the timer hits 00:00.
 * - "Wrong email? Change" link pops back to EmailSignup.
 *
 * @param initialEmail Defaults to placeholder so deep-link works standalone.
 *   When pushed from EmailSignup, callers can pass the real address.
 */
class EmailVerifyScreen(
    private val initialEmail: String = "ad@nümunə.az",
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: EmailVerifyViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current
        val snackbarHostState = remember { SnackbarHostState() }

        // The email param is rendered directly by the composable (state.copy
        // below) — no VM seeding required for this iteration. A real wiring
        // would add an EmailSeeded intent.

        // 1Hz countdown driver — VM clamps at 0.
        LaunchedEffect(Unit) {
            while (true) {
                delay(1000)
                viewModel.onIntent(EmailVerifyIntent.TickResend)
            }
        }

        viewModel.collectSideEffect { effect ->
            when (effect) {
                EmailVerifySideEffect.NavigateBack -> {
                    if (!navigator.pop()) Unit
                }
                EmailVerifySideEffect.NavigateToPaywall ->
                    navigator.push(PaywallScreen())
                EmailVerifySideEffect.ResendSentToast ->
                    snackbarHostState.showSnackbar(strings.emailVerifyResendToast)
            }
        }

        EmailVerifyContent(
            state = state.copy(email = initialEmail),
            strings = strings,
            snackbarHostState = snackbarHostState,
            onBack = { viewModel.onIntent(EmailVerifyIntent.BackTapped) },
            onCode = { viewModel.onIntent(EmailVerifyIntent.CodeChanged(it)) },
            onResend = { viewModel.onIntent(EmailVerifyIntent.ResendTapped) },
            onWrongEmail = { viewModel.onIntent(EmailVerifyIntent.WrongEmailTapped) },
        )
    }
}

@Composable
private fun EmailVerifyContent(
    state: EmailVerifyState,
    strings: Strings,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onCode: (String) -> Unit,
    onResend: () -> Unit,
    onWrongEmail: () -> Unit,
) {
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
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // ── Top bar (back only) ───────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BackChevronButton(onClick = onBack)
            }

            Spacer(Modifier.height(72.dp))

            // ── Envelope icon ─────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(VoltColors.surface1),
                contentAlignment = Alignment.Center,
            ) {
                Canvas(modifier = Modifier.size(28.dp)) {
                    drawEnvelope(VoltColors.volt)
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Title ─────────────────────────────────────────────────────
            Text(
                text = strings.emailVerifySentTo(state.email),
                style = VoltType.titleLarge.copy(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
                color = VoltColors.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(8.dp))

            // ── Subtitle ──────────────────────────────────────────────────
            Text(
                text = strings.emailVerifySubtitle,
                style = VoltType.bodyMedium,
                color = VoltColors.onSurfaceMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(32.dp))

            // ── OTP boxes (hidden BasicTextField driver) ──────────────────
            OtpRow(
                code = state.code,
                onCodeChange = onCode,
                enabled = !state.isSubmitting,
            )

            Spacer(Modifier.height(16.dp))

            // ── Resend (countdown or tappable) ────────────────────────────
            if (state.canResend) {
                Text(
                    text = strings.emailVerifyResendReady,
                    style = VoltType.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = VoltColors.volt,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onResend)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                )
            } else {
                Text(
                    text = strings.emailVerifyResendCountdown(state.secondsLeft),
                    style = VoltType.bodyMedium,
                    color = VoltColors.onSurfaceMuted,
                )
            }

            Spacer(Modifier.height(8.dp))

            // ── Wrong-email link ──────────────────────────────────────────
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onWrongEmail)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyleCompat(color = VoltColors.onSurfaceMuted) {
                            append(strings.emailVerifyWrongEmailPrefix)
                        }
                        withStyleCompat(
                            color = VoltColors.volt,
                            weight = FontWeight.SemiBold,
                        ) {
                            append(strings.emailVerifyWrongEmailLink)
                        }
                    },
                    style = VoltType.bodyMedium.copy(fontSize = 14.sp),
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(Modifier.weight(1f))
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 88.dp),
        )
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// OtpRow — hidden BasicTextField overlaid on six visual cells.
// Tap on any cell focuses the field; the soft keyboard pops up.
// ──────────────────────────────────────────────────────────────────────────────

@Composable
private fun OtpRow(
    code: String,
    onCodeChange: (String) -> Unit,
    enabled: Boolean,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val gapDp = 8.dp
        val totalGap = gapDp * 5
        val cellWidth = (maxWidth - totalGap) / 6f
        val cellHeight = 64.dp

        // Cursor blink (only for the active empty cell).
        var blink by remember { mutableStateOf(true) }
        LaunchedEffect(code) {
            while (true) {
                delay(500)
                blink = !blink
            }
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            // Hidden BasicTextField — receives all input + cursor focus.
            BasicTextField(
                value = code,
                onValueChange = {
                    val sanitized = it.filter { ch -> ch.isDigit() }.take(6)
                    onCodeChange(sanitized)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cellHeight)
                    .focusRequester(focusRequester)
                    .alpha(0f),
                enabled = enabled,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                textStyle = TextStyle(color = Color.Transparent),
                cursorBrush = SolidColor(Color.Transparent),
            )

            // Visual cell row — tap anywhere → focus hidden field.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = enabled) { focusRequester.requestFocus() },
                horizontalArrangement = Arrangement.spacedBy(gapDp),
            ) {
                repeat(6) { i ->
                    val ch = code.getOrNull(i)
                    val isActive = code.length == i
                    OtpCell(
                        modifier = Modifier
                            .size(width = cellWidth, height = cellHeight),
                        char = ch,
                        active = isActive && enabled,
                        showCursor = isActive && enabled && blink && ch == null,
                    )
                }
            }
        }
    }
}

@Composable
private fun OtpCell(
    modifier: Modifier,
    char: Char?,
    active: Boolean,
    showCursor: Boolean,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(VoltColors.surface1)
            .border(
                width = if (active) 2.dp else 1.dp,
                color = if (active) VoltColors.volt else VoltColors.outline,
                shape = RoundedCornerShape(16.dp),
            ),
        contentAlignment = Alignment.Center,
    ) {
        when {
            char != null -> Text(
                text = char.toString(),
                style = VoltType.titleLarge.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = VoltColors.onSurface,
            )
            showCursor -> Text(
                text = "|",
                style = VoltType.titleLarge.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = VoltColors.volt,
            )
            else -> {} // empty inactive
        }
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// Envelope glyph — body rectangle + triangular flap (no SVG asset needed).
// ──────────────────────────────────────────────────────────────────────────────

private fun DrawScope.drawEnvelope(color: Color) {
    val w = size.width
    val h = size.height
    val sw = w / 14f
    // Body rectangle.
    val rectTopLeft = Offset(0f, h * 0.18f)
    val rectSize = Size(w, h * 0.64f)
    drawRect(
        color = color,
        topLeft = rectTopLeft,
        size = rectSize,
        style = Stroke(width = sw),
    )
    // Triangle flap (from top-left & top-right of rectangle down to center).
    val flapTop = h * 0.18f
    val flapBottom = h * 0.50f
    drawLine(
        color = color,
        start = Offset(0f, flapTop),
        end = Offset(w / 2f, flapBottom),
        strokeWidth = sw,
        cap = StrokeCap.Round,
    )
    drawLine(
        color = color,
        start = Offset(w, flapTop),
        end = Offset(w / 2f, flapBottom),
        strokeWidth = sw,
        cap = StrokeCap.Round,
    )
}

// ──────────────────────────────────────────────────────────────────────────────
// Back chevron — shared atom (matches AuthGate / EmailSignup).
// ──────────────────────────────────────────────────────────────────────────────

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
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
    )
    drawLine(
        color = VoltColors.onSurface,
        start = Offset(left, midY),
        end = Offset(right, size.height),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
    )
}

// ──────────────────────────────────────────────────────────────────────────────
// AnnotatedString helper — local copy (mirrors EmailSignup).
// ──────────────────────────────────────────────────────────────────────────────

private inline fun androidx.compose.ui.text.AnnotatedString.Builder.withStyleCompat(
    color: Color,
    weight: FontWeight = FontWeight.Normal,
    block: androidx.compose.ui.text.AnnotatedString.Builder.() -> Unit,
) {
    pushStyle(SpanStyle(color = color, fontWeight = weight))
    block()
    pop()
}
