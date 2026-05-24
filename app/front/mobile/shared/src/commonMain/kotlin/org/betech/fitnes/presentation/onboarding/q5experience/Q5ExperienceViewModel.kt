package org.betech.fitnes.presentation.onboarding.q5experience

import androidx.lifecycle.ViewModel
import org.betech.fitnes.domain.model.AnalyticsEvent
import org.betech.fitnes.domain.model.ExperienceLevel
import org.betech.fitnes.domain.model.OnboardingAnswer
import org.betech.fitnes.domain.repository.AnalyticsRepository
import org.betech.fitnes.domain.repository.OnboardingRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Q5 · Experience screen VM (Orbit MVI).
 *
 * Persists the schema-canonical [ExperienceLevel] derived from the UI-only
 * [ExperienceChoice]. SerialName values (`beginner` / `intermediate` /
 * `advanced` / `athlete`) are written to the `q5_experience` answer.
 */
class Q5ExperienceViewModel(
    private val onboardingRepo: OnboardingRepository,
    private val analytics: AnalyticsRepository,
) : ViewModel(), ContainerHost<Q5ExperienceState, Q5ExperienceSideEffect> {

    override val container =
        container<Q5ExperienceState, Q5ExperienceSideEffect>(Q5ExperienceState())

    fun onIntent(i: Q5ExperienceIntent) = intent {
        when (i) {
            is Q5ExperienceIntent.Select -> reduce { state.copy(selected = i.choice) }

            Q5ExperienceIntent.BackTapped ->
                postSideEffect(Q5ExperienceSideEffect.NavigateBack)

            Q5ExperienceIntent.Confirm -> {
                val choice = state.selected ?: return@intent
                reduce { state.copy(isSaving = true) }
                try {
                    val level = mapToExperienceLevel(choice)
                    onboardingRepo.saveAnswer(
                        stepId = "q5_experience",
                        answer = OnboardingAnswer.SingleChoice(
                            questionId = "q5_experience",
                            value = level.name.lowercase(),
                        ),
                    )
                    analytics.track(
                        AnalyticsEvent.QuestionAnswered(
                            questionId = "q5_experience",
                            value = choice.name,
                        )
                    )
                    reduce { state.copy(isSaving = false) }
                    postSideEffect(Q5ExperienceSideEffect.NavigateToQ6Context)
                } catch (t: Throwable) {
                    reduce { state.copy(isSaving = false) }
                    postSideEffect(Q5ExperienceSideEffect.ShowError(t.message ?: "Error"))
                }
            }
        }
    }

    private fun mapToExperienceLevel(choice: ExperienceChoice): ExperienceLevel = when (choice) {
        ExperienceChoice.BEGINNER -> ExperienceLevel.BEGINNER
        ExperienceChoice.INTERMEDIATE -> ExperienceLevel.INTERMEDIATE
        ExperienceChoice.ADVANCED -> ExperienceLevel.ADVANCED
        ExperienceChoice.ATHLETE -> ExperienceLevel.ATHLETE
    }
}
