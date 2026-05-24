package org.betech.fitnes.data.mapper

import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.datetime.Instant
import org.betech.fitnes.data.dto.OnboardingProgressDto
import org.betech.fitnes.data.dto.OnboardingQuestionDto
import org.betech.fitnes.domain.model.OnboardingProgress
import org.betech.fitnes.domain.model.OnboardingQuestion

private fun parseInstantOrNull(raw: String?): Instant? =
    raw?.let { runCatching { Instant.parse(it) }.getOrNull() }

fun OnboardingProgressDto.toDomain(): OnboardingProgress = OnboardingProgress(
    currentStep = currentStep,
    completedSteps = completedSteps.toImmutableList(),
    answers = answers.toImmutableMap(),
    startedAt = parseInstantOrNull(startedAt),
    updatedAt = parseInstantOrNull(updatedAt)
)

fun OnboardingProgress.toDto(): OnboardingProgressDto = OnboardingProgressDto(
    currentStep = currentStep,
    completedSteps = completedSteps.toList(),
    answers = answers.toMap(),
    startedAt = startedAt?.toString(),
    updatedAt = updatedAt?.toString()
)

fun OnboardingQuestionDto.toDomain(): OnboardingQuestion = OnboardingQuestion(
    id = id,
    step = step,
    kind = kind,
    layer = layer,
    titleKey = titleKey,
    subContextKey = subContextKey,
    skippable = skippable,
    options = options.toImmutableList(),
    validation = validation,
    analyticsId = analyticsId
)
