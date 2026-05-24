package org.betech.fitnes.presentation.onboarding.authgate

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
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
import org.betech.fitnes.presentation.onboarding.emailsignup.EmailSignupScreen
import org.betech.fitnes.presentation.onboarding.paywall.PaywallScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 14 · AuthGate (Pencil K1n7u5).
 *
 * Apple / Google / Email + ghost skip + legal footer.
 * - Apple button: surface1 fill, onSurface text, 1dp outline border.
 * - Google button: onSurface (near-white) fill, surface0 text, no border.
 * - Email: primary VoltButton (volt + onVolt).
 * - Skip: ghost text link (no underline).
 * - Loading state: small spinner replaces icon inside the active button; all
 *   buttons disabled while inFlight != NONE.
 * - Snackbar hosts auth failures + legal placeholder toasts.
 */
class AuthGateScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: AuthGateViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current
        val snackbarHostState = remember { SnackbarHostState() }

        viewModel.collectSideEffect { effect ->
            when (effect) {
                AuthGateSideEffect.NavigateBack -> {
                    if (!navigator.pop()) navigator.replace(PaywallScreen())
                }
                AuthGateSideEffect.NavigateToEmailSignup ->
                    navigator.push(EmailSignupScreen())
                AuthGateSideEffect.NavigateToPaywall ->
                    navigator.replace(PaywallScreen())
                is AuthGateSideEffect.ShowToast -> {
                    val msg = when (effect.messageKey) {
                        LegalToastKey.LEGAL_TODO -> strings.legalTodoToast
                        else -> effect.messageKey
                    }
                    snackbarHostState.showSnackbar(msg)
                }
            }
        }

        // Surface auth errors via snackbar (Result-mapped messages from VM).
        val err = state.errorMessage
        if (err != null) {
            // collect once per change
            androidx.compose.runtime.LaunchedEffect(err) {
                snackbarHostState.showSnackbar(err)
            }
        }

        AuthGateContent(
            state = state,
            strings = strings,
            snackbarHostState = snackbarHostState,
            onApple = { viewModel.onIntent(AuthGateIntent.AppleTapped) },
            onGoogle = { viewModel.onIntent(AuthGateIntent.GoogleTapped) },
            onEmail = { viewModel.onIntent(AuthGateIntent.EmailTapped) },
            onSkip = { viewModel.onIntent(AuthGateIntent.SkipTapped) },
            onTerms = { viewModel.onIntent(AuthGateIntent.TermsTapped) },
            onPrivacy = { viewModel.onIntent(AuthGateIntent.PrivacyTapped) },
        )
    }
}

@Composable
private fun AuthGateContent(
    state: AuthGateState,
    strings: Strings,
    snackbarHostState: SnackbarHostState,
    onApple: () -> Unit,
    onGoogle: () -> Unit,
    onEmail: () -> Unit,
    onSkip: () -> Unit,
    onTerms: () -> Unit,
    onPrivacy: () -> Unit,
) {
    val anyInFlight = state.inFlight != AuthInFlight.NONE

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
            Spacer(Modifier.height(64.dp))

            Text(
                text = strings.authGateTitle,
                style = VoltType.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                ),
                color = VoltColors.onSurface,
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = strings.authGateSubtitle,
                style = VoltType.bodyMedium.copy(fontSize = 14.sp),
                color = VoltColors.onSurfaceMuted,
            )

            // Push the CTA stack to the bottom.
            Spacer(Modifier.weight(1f))

            // ── CTA stack ──────────────────────────────────────────────
            AppleSsoButton(
                text = strings.authGateAppleCta,
                onClick = onApple,
                enabled = !anyInFlight,
                loading = state.inFlight == AuthInFlight.APPLE,
            )

            Spacer(Modifier.height(12.dp))

            GoogleSsoButton(
                text = strings.authGateGoogleCta,
                onClick = onGoogle,
                enabled = !anyInFlight,
                loading = state.inFlight == AuthInFlight.GOOGLE,
            )

            Spacer(Modifier.height(16.dp))

            OrDivider(text = strings.authGateOrDivider)

            Spacer(Modifier.height(16.dp))

            VoltButton(
                text = strings.authGateEmailCta,
                onClick = onEmail,
                enabled = !anyInFlight,
            )

            Spacer(Modifier.height(16.dp))

            // ── Skip ghost link ────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !anyInFlight, onClick = onSkip)
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = strings.authGateSkipCta,
                    style = VoltType.bodyMedium.copy(fontSize = 14.sp),
                    color = VoltColors.onSurfaceMuted,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(Modifier.height(12.dp))

            // ── Legal footer ──────────────────────────────────────────
            LegalFooter(
                strings = strings,
                onTerms = onTerms,
                onPrivacy = onPrivacy,
            )

            Spacer(Modifier.height(24.dp))
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
// Apple SSO button — surface1 fill + outline + Canvas apple silhouette
// ──────────────────────────────────────────────────────────────────────────────

@Composable
private fun AppleSsoButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    loading: Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (enabled || loading) VoltColors.surface1 else VoltColors.surface2)
            .border(1.dp, VoltColors.outline, RoundedCornerShape(16.dp))
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = VoltColors.onSurface,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Canvas(modifier = Modifier.size(20.dp)) { drawAppleGlyph(VoltColors.onSurface) }
                }
            }
            Spacer(Modifier.size(12.dp))
            Text(
                text = text,
                style = VoltType.labelLarge,
                color = if (enabled || loading) VoltColors.onSurface else VoltColors.onSurfaceMuted,
            )
        }
    }
}

private fun DrawScope.drawAppleGlyph(color: Color) {
    val w = size.width
    val h = size.height
    // Body: two overlapping circles forming an apple silhouette.
    val r = w * 0.32f
    drawCircle(color = color, radius = r, center = Offset(w * 0.36f, h * 0.58f))
    drawCircle(color = color, radius = r, center = Offset(w * 0.64f, h * 0.58f))
    // Bite (background-coloured arc on the right).
    drawCircle(
        color = VoltColors.surface1,
        radius = w * 0.16f,
        center = Offset(w * 0.86f, h * 0.40f),
    )
    // Leaf: a tilted oval up top-right.
    val leafPath = Path().apply {
        moveTo(w * 0.58f, h * 0.18f)
        cubicTo(
            w * 0.74f, h * 0.04f,
            w * 0.86f, h * 0.18f,
            w * 0.66f, h * 0.30f,
        )
        cubicTo(
            w * 0.58f, h * 0.28f,
            w * 0.54f, h * 0.24f,
            w * 0.58f, h * 0.18f,
        )
        close()
    }
    drawPath(path = leafPath, color = color)
}

// ──────────────────────────────────────────────────────────────────────────────
// Google SSO button — near-white fill + 4-quadrant "G" glyph
// ──────────────────────────────────────────────────────────────────────────────

@Composable
private fun GoogleSsoButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    loading: Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (enabled || loading) VoltColors.onSurface else VoltColors.surface2)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = VoltColors.surface0,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Canvas(modifier = Modifier.size(20.dp)) { drawGoogleGlyph() }
                }
            }
            Spacer(Modifier.size(12.dp))
            Text(
                text = text,
                style = VoltType.labelLarge,
                color = if (enabled || loading) VoltColors.surface0 else VoltColors.onSurfaceMuted,
            )
        }
    }
}

/**
 * Zero-dep Google "G": stroked 4-quadrant ring (blue/green/yellow/red) +
 * inner horizontal bar approximating the canonical mark. Not pixel-perfect,
 * but recognisable and brand-evocative without shipping the official SVG.
 */
private fun DrawScope.drawGoogleGlyph() {
    val w = size.width
    val h = size.height
    val stroke = w * 0.18f
    val arcRect = Rect(
        offset = Offset(stroke / 2f, stroke / 2f),
        size = Size(w - stroke, h - stroke),
    )
    val blue = Color(0xFF4285F4)
    val green = Color(0xFF34A853)
    val yellow = Color(0xFFFBBC05)
    val red = Color(0xFFEA4335)
    // 4 arcs (top-right=blue, bottom-right=green, bottom-left=yellow, top-left=red).
    drawArc(
        color = blue, startAngle = -90f, sweepAngle = 90f, useCenter = false,
        topLeft = arcRect.topLeft, size = arcRect.size,
        style = Stroke(width = stroke, cap = StrokeCap.Butt),
    )
    drawArc(
        color = green, startAngle = 0f, sweepAngle = 90f, useCenter = false,
        topLeft = arcRect.topLeft, size = arcRect.size,
        style = Stroke(width = stroke, cap = StrokeCap.Butt),
    )
    drawArc(
        color = yellow, startAngle = 90f, sweepAngle = 90f, useCenter = false,
        topLeft = arcRect.topLeft, size = arcRect.size,
        style = Stroke(width = stroke, cap = StrokeCap.Butt),
    )
    drawArc(
        color = red, startAngle = 180f, sweepAngle = 90f, useCenter = false,
        topLeft = arcRect.topLeft, size = arcRect.size,
        style = Stroke(width = stroke, cap = StrokeCap.Butt),
    )
    // Inner horizontal bar (the "G" notch — blue), right side from centre.
    drawLine(
        color = blue,
        start = Offset(w * 0.50f, h * 0.50f),
        end = Offset(w * 0.95f, h * 0.50f),
        strokeWidth = stroke * 0.9f,
        cap = StrokeCap.Butt,
    )
}

// ──────────────────────────────────────────────────────────────────────────────
// "or" divider — thin line · text · thin line
// ──────────────────────────────────────────────────────────────────────────────

@Composable
private fun OrDivider(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(VoltColors.outline),
        )
        Text(
            text = text,
            style = VoltType.bodyMedium.copy(fontSize = 12.sp),
            color = VoltColors.onSurfaceMuted,
            modifier = Modifier.padding(horizontal = 12.dp),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(VoltColors.outline),
        )
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// Legal footer — "Davam edərək qəbul edirsən: Şərtlər · Məxfilik Siyasəti"
// Annotated rich text would be ideal; for now two tappable Text rows alongside.
// ──────────────────────────────────────────────────────────────────────────────

@Composable
private fun LegalFooter(
    strings: Strings,
    onTerms: () -> Unit,
    onPrivacy: () -> Unit,
) {
    val mutedStyle = SpanStyle(color = VoltColors.onSurfaceMuted)
    val linkStyle = SpanStyle(
        color = VoltColors.onSurface,
        textDecoration = TextDecoration.Underline,
    )

    // First line: descriptive prefix (centered, muted).
    Text(
        text = strings.authGateFooterPrefix.trimEnd(),
        style = VoltType.labelLarge.copy(fontSize = 12.sp, fontWeight = FontWeight.Normal),
        color = VoltColors.onSurfaceMuted,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
    )
    // Second line: two link spans with a separator (tap-area split).
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(linkStyle) { append(strings.authGateFooterTerms) }
            },
            style = VoltType.labelLarge.copy(fontSize = 12.sp, fontWeight = FontWeight.Normal),
            modifier = Modifier
                .clickable(onClick = onTerms)
                .padding(vertical = 4.dp, horizontal = 4.dp),
        )
        Text(
            text = strings.authGateFooterSeparator,
            style = VoltType.labelLarge.copy(fontSize = 12.sp, fontWeight = FontWeight.Normal),
            color = VoltColors.onSurfaceMuted,
        )
        Text(
            text = buildAnnotatedString {
                withStyle(linkStyle) { append(strings.authGateFooterPrivacy) }
            },
            style = VoltType.labelLarge.copy(fontSize = 12.sp, fontWeight = FontWeight.Normal),
            modifier = Modifier
                .clickable(onClick = onPrivacy)
                .padding(vertical = 4.dp, horizontal = 4.dp),
        )
        // mutedStyle reserved for future text wrap; kept to silence unused warning.
        @Suppress("UNUSED_EXPRESSION") mutedStyle
    }
}