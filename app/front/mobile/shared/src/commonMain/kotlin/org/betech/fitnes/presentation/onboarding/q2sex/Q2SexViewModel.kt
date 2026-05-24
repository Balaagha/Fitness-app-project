package org.betech.fitnes.presentation.onboarding.q2sex

import androidx.lifecycle.ViewModel
import org.betech.fitnes.domain.model.AnalyticsEvent
import org.betech.fitnes.domain.model.OnboardingAnswer
import org.betech.fitnes.domain.model.Sex
import org.betech.fitnes.domain.repository.AnalyticsRepository
import org.betech.fitnes.domain.repository.OnboardingRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Q2 · Sex screen VM (Orbit MVI).
 *
 * Schema reconciliation: domain [Sex] is binary (MALE/FEMALE).
 * UI exposes a third option [SexChoice.PREFER_NOT_TO_SAY] which maps to
 * a null sex (we skip the schema write but still persist the UI-choice
 * answer for replay + emit analytics).
 */
class Q2SexViewModel(
    private val onboardingRepo: OnboardingRepository,
    private val analytics: AnalyticsRepository,
) : ViewModel(), ContainerHost<Q2SexState, Q2SexSideEffect> {

    override val container = container<Q2SexState, Q2SexSideEffect>(Q2SexState())

    fun onIntent(i: Q2SexIntent) = intent {
        when (i) {
            is Q2SexIntent.Select -> reduce { state.copy(selected = i.choice) }

            Q2SexIntent.BackTapped ->
                postSideEffect(Q2SexSideEffect.NavigateBack)

            Q2SexIntent.Confirm -> {
                val choice = state.selected ?: return@intent
                reduce { state.copy(isSaving = true) }
                try {
                    // Persist the UI-choice for replay (uppercase enum name).
                    // Schema-canonical Sex is derived downstream from this value.
                    onboardingRepo.saveAnswer(
                        stepId = "q2_sex",
                        answer = OnboardingAnswer.SingleChoice(
                            questionId = "q2_sex",
                            value = choice.name,
                        ),
                    )
                    analytics.track(
                        AnalyticsEvent.QuestionAnswered(
                            questionId = "q2_sex",
                            value = choice.name,
                        )
                    )
                    reduce { state.copy(isSaving = false) }
                    postSideEffect(Q2SexSideEffect.NavigateToQ3Age)
                } catch (t: Throwable) {
                    reduce { state.copy(isSaving = false) }
                    postSideEffect(Q2SexSideEffect.ShowError(t.message ?: "Error"))
                }
            }
        }
    }

    /**
     * UI→schema mapping. PREFER_NOT_TO_SAY → null (binary schema cannot
     * express it; downstream consumers must apply the neutral baseline).
     * Kept as a pure function for future reuse (e.g., persona resolver).
     */
    @Suppress("unused")
    private fun mapToSex(choice: SexChoice): Sex? = when (choice) {
        SexChoice.MALE -> Sex.MALE
        SexChoice.FEMALE -> Sex.FEMALE
        SexChoice.PREFER_NOT_TO_SAY -> null
    }
}
