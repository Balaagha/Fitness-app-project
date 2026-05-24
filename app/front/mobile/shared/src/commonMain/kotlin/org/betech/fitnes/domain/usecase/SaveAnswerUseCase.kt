package org.betech.fitnes.domain.usecase

import org.betech.fitnes.domain.model.AnalyticsEvent
import org.betech.fitnes.domain.model.OnboardingAnswer
import org.betech.fitnes.domain.repository.AnalyticsRepository
import org.betech.fitnes.domain.repository.OnboardingRepository

class SaveAnswerUseCase(
    private val onboarding: OnboardingRepository,
    private val analytics: AnalyticsRepository
) {
    suspend operator fun invoke(stepId: String, answer: OnboardingAnswer) {
        onboarding.saveAnswer(stepId, answer)
        analytics.track(
            AnalyticsEvent.QuestionAnswered(
                questionId = answer.questionId,
                value = answer::class.simpleName.orEmpty()
            )
        )
    }
}
