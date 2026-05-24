package org.betech.fitnes.domain.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.datetime.Instant

/** Pure domain — wire serialization lives in `OnboardingProgressDto`. */
data class OnboardingProgress(
    val currentStep: OnboardingStep = OnboardingStep.Welcome,
    val completedSteps: ImmutableList<OnboardingStep> = persistentListOf(),
    val answers: ImmutableMap<String, OnboardingAnswer> = persistentMapOf(),
    val startedAt: Instant? = null,
    val updatedAt: Instant? = null
)
