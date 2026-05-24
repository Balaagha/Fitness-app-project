package org.betech.fitnes.domain.usecase

import org.betech.fitnes.domain.model.AnalyticsEvent
import org.betech.fitnes.domain.model.OnboardingStep
import org.betech.fitnes.domain.model.Persona
import org.betech.fitnes.domain.repository.AnalyticsRepository
import org.betech.fitnes.domain.repository.OnboardingRepository

class CompleteOnboardingUseCase(
    private val onboarding: OnboardingRepository,
    private val analytics: AnalyticsRepository
) {
    suspend operator fun invoke(persona: Persona) {
        onboarding.advanceTo(OnboardingStep.Complete)
        analytics.track(AnalyticsEvent.OnboardingCompleted(persona.cellId))
    }
}
