package org.betech.fitnes.presentation.onboarding.welcome

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.delay
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.presentation.onboarding.languageselect.LanguageSelectScreen
import org.betech.fitnes.presentation.onboarding.login.LoginScreen
import org.betech.fitnes.presentation.onboarding.q1goal.Q1GoalScreen
import fitnes.shared.generated.resources.Res
import fitnes.shared.generated.resources.s1
import fitnes.shared.generated.resources.s2
import fitnes.shared.generated.resources.s3
import fitnes.shared.generated.resources.s4
import fitnes.shared.generated.resources.s5
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

class WelcomeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: WelcomeViewModel = koinViewModel()
        val state by viewModel.collectAsState()

        val pagerState = rememberPagerState(pageCount = { WELCOME_PAGE_COUNT })

        viewModel.collectSideEffect { effect ->
            when (effect) {
                is WelcomeSideEffect.AdvanceTo -> pagerState.animateScrollToPage(effect.index)
                WelcomeSideEffect.NavigateToQ1Goal -> navigator.push(Q1GoalScreen())
                WelcomeSideEffect.NavigateToLogin -> navigator.push(LoginScreen())
                WelcomeSideEffect.NavigateToLanguageSelect ->
                    navigator.replace(LanguageSelectScreen())
            }
        }

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect { page ->
                viewModel.onIntent(WelcomeIntent.PageChanged(page))
            }
        }

        WelcomeContent(
            pagerState = pagerState,
            currentPage = state.currentPage,
            onSkip = { viewModel.onIntent(WelcomeIntent.SkipTapped) },
            onPrimary = { viewModel.onIntent(WelcomeIntent.StartTapped) },
            onSecondary = { viewModel.onIntent(WelcomeIntent.HaveAccountTapped) },
        )
    }
}

private data class WelcomePage(
    val bg: DrawableResource,
    val bgImageAlpha: Float = 0.40f,
    val darkOverlayAlpha: Float = 0.69f,
    val eyebrow: String,
    val headlinePlain: String,
    val headlinePill: String,
    val supporting: String,
    val microProof: String? = null,
    val chipIcons: List<String> = emptyList(),
    val bullets: List<String> = emptyList(),
    val usesMossAccent: Boolean = false,
    val primaryCta: String = "Davam et",
    val secondaryCta: String? = null,
    val disclaimer: String? = null,
)

private val WelcomePages = listOf(
    WelcomePage(
        bg = Res.drawable.s1,
        eyebrow = "SƏN İDARƏ ET",
        headlinePlain = "Məşqini bir yerdə",
        headlinePill = "topla.",
        supporting = "Plan, logger, kalori — hamısı bir cibdə. AZ dilində.",
    ),
    WelcomePage(
        bg = Res.drawable.s2,
        bgImageAlpha = 0.38f,
        eyebrow = "ELMƏ SÖYKƏNİR",
        headlinePlain = "Təxmin yox.",
        headlinePill = "Hesab var.",
        supporting = "Kalori, protein, dincəlmə — beynəlxalq idman tibbi standartları ilə hesablanır.",
        microProof = "JISSN 2024 · ACSM standartı",
    ),
    WelcomePage(
        bg = Res.drawable.s3,
        bgImageAlpha = 0.42f,
        eyebrow = "QƏHRƏMAN SƏNSƏN",
        headlinePlain = "AI təklif edir.",
        headlinePill = "Qərar sənin.",
        supporting = "Super-set, drop-set, RPE — peşəkar alətlər, sənin əlində.",
        chipIcons = listOf("super-set", "drop-set", "tempo"),
    ),
    WelcomePage(
        bg = Res.drawable.s4,
        eyebrow = "AZƏRBAYCANDA QURULDU",
        headlinePlain = "Plov, kətə, qutab",
        headlinePill = "hamısı tanış.",
        supporting = "200+ AZ yeməyi, Ramazan rejimi, doğma dil — robot tərcüməsi yox.",
        microProof = "Top-200 AZ yemək bazası",
        usesMossAccent = true,
    ),
    WelcomePage(
        bg = Res.drawable.s5,
        bgImageAlpha = 1.0f,
        darkOverlayAlpha = 0.40f,
        eyebrow = "HAZIRSAN?",
        headlinePlain = "Pulsuz başla.",
        headlinePill = "Limit yox.",
        supporting = "Heç bir ödəniş, heç bir gizli limit. Tamamilə pulsuz başla.",
        bullets = listOf(
            "Sənin idarəndə olan logger + super-set",
            "AZ yeməkləri, Ramazan, doğma dil",
            "Elmə söykənən hesablamalar",
        ),
        primaryCta = "Pulsuz başla",
        secondaryCta = "Artıq hesabım var",
        disclaimer = "Davam edərək, AI-dəstəkli məsləhətlərin son qərarını sənin verdiyini qəbul edirsən.",
    ),
)

@Composable
private fun WelcomeContent(
    pagerState: PagerState,
    currentPage: Int,
    onSkip: () -> Unit,
    onPrimary: () -> Unit,
    onSecondary: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VoltColors.surface0),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { pageIndex ->
            val page = WelcomePages[pageIndex]
            val isActive = pagerState.currentPage == pageIndex
            WelcomePageBody(
                page = page,
                isActive = isActive,
                onSecondary = onSecondary,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(start = 24.dp, end = 24.dp, top = 16.dp),
        ) {
            TopBar(
                pageIndex = currentPage,
                showSkip = currentPage < WELCOME_PAGE_COUNT - 1,
                onSkip = onSkip,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(start = 24.dp, end = 24.dp, bottom = 28.dp),
        ) {
            PrimaryCtaSwap(
                currentPage = currentPage,
                onPrimary = onPrimary,
            )
        }
    }
}

@Composable
private fun WelcomePageBody(
    page: WelcomePage,
    isActive: Boolean,
    onSecondary: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(page.bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = page.bgImageAlpha },
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(VoltColors.surface0.copy(alpha = page.darkOverlayAlpha)),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            VoltColors.surface0.copy(alpha = 0.95f),
                        ),
                    ),
                ),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(start = 24.dp, end = 24.dp, top = 76.dp, bottom = 116.dp),
            verticalArrangement = Arrangement.Bottom,
        ) {
            HeroText(
                page = page,
                isActive = isActive,
                onSecondary = onSecondary,
            )
        }
    }
}

@Composable
private fun TopBar(pageIndex: Int, showSkip: Boolean, onSkip: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProgressBar(
            current = pageIndex,
            total = WELCOME_PAGE_COUNT,
            modifier = Modifier.width(220.dp),
        )
        if (showSkip) {
            Text(
                text = "Keç",
                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium),
                color = VoltColors.onSurfaceMuted,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onSkip)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            )
        } else {
            Spacer(Modifier.width(36.dp))
        }
    }
}

@Composable
private fun ProgressBar(current: Int, total: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.height(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        repeat(total) { i ->
            val target = if (i <= current) VoltColors.volt
            else VoltColors.onSurface.copy(alpha = 0.12f)
            val animatedColor by animateColorAsState(
                targetValue = target,
                animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing),
                label = "progressSegment$i",
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(animatedColor),
            )
        }
    }
}

@Composable
private fun HeroText(
    page: WelcomePage,
    isActive: Boolean,
    onSecondary: () -> Unit,
) {
    val accent = if (page.usesMossAccent) VoltColors.moss else VoltColors.volt
    val pillTextColor = VoltColors.onVolt
    var headlineDone by remember(page) { mutableStateOf(false) }

    LaunchedEffect(page, isActive) {
        if (!isActive) headlineDone = false
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        FadeSlideIn(visible = isActive, delayMs = 0) {
            EyebrowChip(text = page.eyebrow, accent = accent)
        }
        TypewriterText(
            text = page.headlinePlain,
            isActive = isActive,
            style = TextStyle(
                fontSize = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 40.sp,
                letterSpacing = (-0.8).sp,
            ),
            color = VoltColors.onSurface,
            cursorColor = accent,
            charDelayMs = 45L,
            startDelayMs = 120L,
            onFinished = { headlineDone = true },
        )
        FadeSlideIn(visible = isActive && headlineDone, delayMs = 60) {
            HeadlinePill(text = page.headlinePill, fill = accent, textColor = pillTextColor)
        }
        FadeSlideIn(visible = isActive && headlineDone, delayMs = 180) {
            Text(
                text = page.supporting,
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium, lineHeight = 23.sp),
                color = VoltColors.onSurfaceMuted,
            )
        }
        page.microProof?.let { proof ->
            FadeSlideIn(visible = isActive && headlineDone, delayMs = 300) {
                MicroProofChip(text = proof, useMoss = page.usesMossAccent)
            }
        }
        if (page.chipIcons.isNotEmpty()) {
            FadeSlideIn(visible = isActive && headlineDone, delayMs = 300) {
                ChipRow(labels = page.chipIcons, accent = accent)
            }
        }
        if (page.bullets.isNotEmpty()) {
            FadeSlideIn(visible = isActive && headlineDone, delayMs = 300) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    page.bullets.forEach { line ->
                        BulletRow(text = line, accent = accent)
                    }
                }
            }
        }
        page.secondaryCta?.let { sec ->
            FadeSlideIn(visible = isActive && headlineDone, delayMs = 380) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                ) {
                    SecondaryCta(text = sec, onClick = onSecondary)
                    page.disclaimer?.let { d ->
                        Text(
                            text = d,
                            style = TextStyle(
                                fontSize = 10.sp,
                                lineHeight = 14.sp,
                                textAlign = TextAlign.Center,
                            ),
                            color = VoltColors.onSurfaceMuted.copy(alpha = 0.6f),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EyebrowChip(text: String, accent: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(accent.copy(alpha = 0.06f))
            .border(1.dp, accent.copy(alpha = 0.25f), RoundedCornerShape(999.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        Text(
            text = text,
            style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.4.sp),
            color = accent,
        )
    }
}

@Composable
private fun HeadlinePill(text: String, fill: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(fill)
            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 8.dp),
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 40.sp,
                letterSpacing = (-0.8).sp,
            ),
            color = textColor,
        )
    }
}

@Composable
private fun MicroProofChip(text: String, useMoss: Boolean) {
    val borderColor = VoltColors.outlineStrong
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(VoltColors.onSurface.copy(alpha = 0.03f))
            .border(1.dp, borderColor, RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp),
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(if (useMoss) VoltColors.moss else VoltColors.volt),
        )
        Text(
            text = text,
            style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.4.sp),
            color = VoltColors.onSurfaceMuted,
        )
    }
}

@Composable
private fun ChipRow(labels: List<String>, accent: Color) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        labels.forEach { label ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(VoltColors.onSurface.copy(alpha = 0.04f))
                    .border(1.dp, VoltColors.outline, RoundedCornerShape(999.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(accent),
                )
                Text(
                    text = label,
                    style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.3.sp),
                    color = VoltColors.onSurfaceMuted,
                )
            }
        }
    }
}

@Composable
private fun BulletRow(text: String, accent: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(accent.copy(alpha = 0.18f))
                .border(1.dp, accent, RoundedCornerShape(7.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(accent),
            )
        }
        Text(
            text = text,
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, lineHeight = 19.sp),
            color = VoltColors.onSurface,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun PrimaryCtaSwap(currentPage: Int, onPrimary: () -> Unit) {
    val label = WelcomePages[currentPage].primaryCta
    AnimatedContent(
        targetState = label,
        transitionSpec = {
            (fadeIn(tween(220, delayMillis = 60)) togetherWith fadeOut(tween(160)))
                .using(SizeTransform(clip = false))
        },
        label = "primaryCta",
    ) { currentLabel ->
        PrimaryCta(text = currentLabel, onClick = onPrimary)
    }
}

@Composable
private fun PrimaryCta(text: String, onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(shape)
            .background(VoltColors.volt)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.SemiBold),
            color = VoltColors.onVolt,
        )
    }
}

@Composable
private fun SecondaryCta(text: String, onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(shape)
            .background(Color.Transparent)
            .border(1.dp, VoltColors.outlineStrong, shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.SemiBold),
            color = VoltColors.onSurface,
        )
    }
}

@Composable
private fun TypewriterText(
    text: String,
    isActive: Boolean,
    style: TextStyle,
    color: Color,
    cursorColor: Color,
    charDelayMs: Long,
    startDelayMs: Long,
    onFinished: () -> Unit,
) {
    var charCount by remember(text) { mutableIntStateOf(0) }
    val visible by remember(text) { derivedStateOf { text.take(charCount) } }
    val done = charCount >= text.length

    LaunchedEffect(text, isActive) {
        charCount = 0
        if (!isActive) return@LaunchedEffect
        delay(startDelayMs)
        text.indices.forEach { i ->
            delay(charDelayMs)
            charCount = i + 1
        }
        onFinished()
    }

    val cursorVisible by remember(isActive, done) {
        derivedStateOf { isActive && !done }
    }
    var blink by remember { mutableStateOf(true) }
    LaunchedEffect(cursorVisible) {
        if (!cursorVisible) {
            blink = false
            return@LaunchedEffect
        }
        while (true) {
            blink = !blink
            delay(520L)
        }
    }

    val annotated = buildAnnotatedString {
        append(visible)
        if (cursorVisible) {
            withStyle(SpanStyle(color = cursorColor.copy(alpha = if (blink) 1f else 0f))) {
                append("▍")
            }
        }
    }
    Text(text = annotated, style = style, color = color)
}

@Composable
private fun FadeSlideIn(
    visible: Boolean,
    delayMs: Int = 0,
    content: @Composable () -> Unit,
) {
    var shown by remember { mutableStateOf(false) }
    LaunchedEffect(visible) {
        if (!visible) {
            shown = false
            return@LaunchedEffect
        }
        delay(delayMs.toLong())
        shown = true
    }
    val progress by animateFloatAsState(
        targetValue = if (shown) 1f else 0f,
        animationSpec = tween(
            durationMillis = 220,
            easing = FastOutSlowInEasing,
        ),
        label = "fadeSlideProgress",
    )
    Box(
        modifier = Modifier.graphicsLayer {
            alpha = progress
            translationY = (1f - progress) * 14.dp.toPx()
        },
    ) { content() }
}

@Suppress("unused")
private val WelcomePadding = PaddingValues(0.dp)
