package org.betech.fitnes.presentation.onboarding.q6context

import androidx.lifecycle.ViewModel
import org.betech.fitnes.domain.model.AnalyticsEvent
import org.betech.fitnes.domain.model.OnboardingAnswer
import org.betech.fitnes.domain.model.TrainingContext
import org.betech.fitnes.domain.repository.AnalyticsRepository
import org.betech.fitnes.domain.repository.OnboardingRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Q6 · Context screen VM (Orbit MVI).
 *
 * UI exposes 4 buckets ([ContextChoice]); the schema column accepts 3
 * ([TrainingContext]). Mapping:
 *
 * | UI choice         | TrainingContext  | Note                                 |
 * |-------------------|------------------|--------------------------------------|
 * | HOME_BODYWEIGHT   | HOME_ONLY        | TODO: track equipment flag separately|
 * | HOME_EQUIPMENT    | HOME_ONLY        | TODO: track equipment flag separately|
 * | GYM               | SERIOUS_GYM      | full equipment expectation           |
 * | HYBRID            | CASUAL_GYM       | TODO: PRD revision may add HYBRID    |
 *
 * The persisted [OnboardingAnswer] keeps the *UI* enum name so the full
 * distinction survives until the schema gains an equipment flag /
 * HYBRID value.
 */
class Q6ContextViewModel(
    private val onboardingRepo: OnboardingRepository,
    private val analytics: AnalyticsRepository,
) : ViewModel(), ContainerHost<Q6ContextState, Q6ContextSideEffect> {

    override val container =
        container<Q6ContextState, Q6ContextSideEffect>(Q6ContextState())

    fun onIntent(i: Q6ContextIntent) = intent {
        when (i) {
            is Q6ContextIntent.Select -> reduce { state.copy(selected = i.choice) }

            Q6ContextIntent.BackTapped ->
                postSideEffect(Q6ContextSideEffect.NavigateBack)

            Q6ContextIntent.Confirm -> {
                val choice = state.selected ?: return@intent
                reduce { state.copy(isSaving = true) }
                try {
                    // Side note: canonical column is derived but we persist the
                    // richer UI value; analytics gets the UI bucket too.
                    @Suppress("UNUSED_VARIABLE")
                    val canonical: TrainingContext = mapToTrainingContext(choice)

                    onboardingRepo.saveAnswer(
                        stepId = "q6_context",
                        answer = OnboardingAnswer.SingleChoice(
                            questionId = "q6_context",
                            value = choice.name,
                        ),
                    )
                    analytics.track(
                        AnalyticsEvent.QuestionAnswered(
                            questionId = "q6_context",
                            value = choice.name,
                        )
                    )
                    reduce { state.copy(isSaving = false) }
                    postSideEffect(Q6ContextSideEffect.NavigateToQ7DaySession)
                } catch (t: Throwable) {
                    reduce { state.copy(isSaving = false) }
                    postSideEffect(Q6ContextSideEffect.ShowError(t.message ?: "Error"))
                }
            }
        }
    }

    private fun mapToTrainingContext(choice: ContextChoice): TrainingContext = when (choice) {
        // TODO: track equipment flag separately — both home buckets collapse to HOME_ONLY today.
        ContextChoice.HOME_BODYWEIGHT -> TrainingContext.HOME_ONLY
        ContextChoice.HOME_EQUIPMENT -> TrainingContext.HOME_ONLY
        ContextChoice.GYM -> TrainingContext.SERIOUS_GYM
        // TODO: PRD revision may add a dedicated HYBRID value; CASUAL_GYM is the
        // closest existing bucket (mixed equipment access).
        ContextChoice.HYBRID -> TrainingContext.CASUAL_GYM
    }
}
