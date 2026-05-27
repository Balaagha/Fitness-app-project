package org.betech.fitnes.presentation.onboarding.q1goal

import androidx.lifecycle.ViewModel
import org.betech.fitnes.domain.model.AnalyticsEvent
import org.betech.fitnes.domain.model.GoalType
import org.betech.fitnes.domain.model.OnboardingAnswer
import org.betech.fitnes.domain.repository.AnalyticsRepository
import org.betech.fitnes.domain.repository.OnboardingRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Q1 · Goal screen VM (Orbit MVI).
 *
 * Persists the schema-canonical [GoalType] derived from the UI-only
 * [GoalChoice] (see mapping note in [Q1GoalState]).
 */
class Q1GoalViewModel(
    private val onboardingRepo: OnboardingRepository,
    private val analytics: AnalyticsRepository,
) : ViewModel(), ContainerHost<Q1GoalState, Q1GoalSideEffect> {

    override val container = container<Q1GoalState, Q1GoalSideEffect>(Q1GoalState())

    fun onIntent(i: Q1GoalIntent) = intent {
        when (i) {
            is Q1GoalIntent.Select -> reduce { state.copy(selected = i.choice) }

            Q1GoalIntent.BackTapped ->
                postSideEffect(Q1GoalSideEffect.NavigateBack)

            Q1GoalIntent.Confirm -> {
                val choice = state.selected ?: return@intent
                reduce { state.copy(isSaving = true) }
                try {
                    val goal = mapToGoalType(choice)
                    onboardingRepo.saveAnswer(
                        stepId = "q1_goal",
                        answer = OnboardingAnswer.SingleChoice(
                            questionId = "q1_goal",
                            value = goal.name.lowercase(),
                        ),
                    )
                    analytics.track(
                        AnalyticsEvent.QuestionAnswered(
                            questionId = "q1_goal",
                            value = choice.name,
                        )
                    )
                    reduce { state.copy(isSaving = false) }
                    postSideEffect(Q1GoalSideEffect.NavigateToQ2Sex)
                } catch (t: Throwable) {
                    reduce { state.copy(isSaving = false) }
                    postSideEffect(Q1GoalSideEffect.ShowError(t.message ?: "Error"))
                }
            }
        }
    }

    // INCREASE_STRENGTH collapses to BULK — closest schema bucket. A dedicated
    // strength sub-goal is a PRD-revision item tracked in the predecessor's
    // findings, not in code.
    private fun mapToGoalType(choice: GoalChoice): GoalType = when (choice) {
        GoalChoice.LOSE_FAT -> GoalType.CUT
        GoalChoice.BUILD_MUSCLE -> GoalType.BULK
        GoalChoice.GET_TONED -> GoalType.GENERAL_FIT
        GoalChoice.INCREASE_STRENGTH -> GoalType.BULK
    }
}
