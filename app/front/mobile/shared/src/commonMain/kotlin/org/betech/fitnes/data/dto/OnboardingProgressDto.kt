package org.betech.fitnes.data.dto

import kotlinx.serialization.Serializable
import org.betech.fitnes.domain.model.OnboardingAnswer
import org.betech.fitnes.domain.model.OnboardingStep

@Serializable
data class OnboardingProgressDto(
    val currentStep: OnboardingStep,
    val completedSteps: List<OnboardingStep> = emptyList(),
    val answers: Map<String, OnboardingAnswer> = emptyMap(),
    val startedAt: String? = null,
    val updatedAt: String? = null
)
