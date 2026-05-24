package org.betech.fitnes.domain.usecase

import org.betech.fitnes.domain.model.AnalyticsEvent
import org.betech.fitnes.domain.model.OnboardingStep
import org.betech.fitnes.domain.repository.AnalyticsRepository
import org.betech.fitnes.domain.repository.OnboardingRepository

class StartOnboardingUseCase(
    private val onboarding: OnboardingRepository,
    private val analytics: AnalyticsRepository
) {
    suspend operator fun invoke() {
        onboarding.reset()
        onboarding.advanceTo(OnboardingStep.Welcome)
        analytics.track(AnalyticsEvent.OnboardingStarted)
    }
}
