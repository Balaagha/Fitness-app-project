package org.betech.fitnes.presentation.onboarding.q3age

import androidx.lifecycle.ViewModel
import org.betech.fitnes.domain.model.AnalyticsEvent
import org.betech.fitnes.domain.model.OnboardingAnswer
import org.betech.fitnes.domain.repository.AnalyticsRepository
import org.betech.fitnes.domain.repository.OnboardingRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Q3 · Age screen VM (Orbit MVI).
 *
 * Persistence: numeric answer in years stored as [OnboardingAnswer.Numeric]
 * (value = age.toDouble()) under stepId "q3_age". Numeric was chosen over
 * the [OnboardingAnswer.Slider] variant because age is a free-form integer
 * that may arrive from the keypad too — not strictly a slider value.
 *
 * Routing on [Q3AgeIntent.Confirm]:
 *  - age in `SOFT_MIN_AGE..SOFT_MAX_AGE` → `NavigateToQ4HeightWeight`
 *  - otherwise (Pencil V5 · iNSs8) → `NavigateToSoftWarning` (non-blocking)
 */
class Q3AgeViewModel(
    private val onboardingRepo: OnboardingRepository,
    private val analytics: AnalyticsRepository,
) : ViewModel(), ContainerHost<Q3AgeState, Q3AgeSideEffect> {

    override val container = container<Q3AgeState, Q3AgeSideEffect>(Q3AgeState())

    fun onIntent(i: Q3AgeIntent) = intent {
        when (i) {
            Q3AgeIntent.Decrement -> reduce {
                state.copy(age = (state.age - 1).coerceAtLeast(Q3AgeState.MIN_AGE))
            }

            Q3AgeIntent.Increment -> reduce {
                state.copy(age = (state.age + 1).coerceAtMost(Q3AgeState.MAX_AGE))
            }

            is Q3AgeIntent.SetAge -> reduce {
                state.copy(age = i.value.coerceIn(Q3AgeState.MIN_AGE, Q3AgeState.MAX_AGE))
            }

            Q3AgeIntent.OpenDialog -> reduce { state.copy(showInputDialog = true) }
            Q3AgeIntent.CloseDialog -> reduce { state.copy(showInputDialog = false) }

            Q3AgeIntent.BackTapped ->
                postSideEffect(Q3AgeSideEffect.NavigateBack)

            Q3AgeIntent.Confirm -> {
                if (!state.isValid) return@intent
                reduce { state.copy(isSaving = true) }
                try {
                    onboardingRepo.saveAnswer(
                        stepId = "q3_age",
                        answer = OnboardingAnswer.Numeric(
                            questionId = "q3_age",
                            value = state.age.toDouble(),
                        ),
                    )
                    analytics.track(
                        AnalyticsEvent.QuestionAnswered(
                            questionId = "q3_age",
                            value = state.age.toString(),
                        )
                    )
                    reduce { state.copy(isSaving = false) }
                    val inSuggestedRange =
                        state.age in Q3AgeState.SOFT_MIN_AGE..Q3AgeState.SOFT_MAX_AGE
                    postSideEffect(
                        if (inSuggestedRange) Q3AgeSideEffect.NavigateToQ4HeightWeight
                        else Q3AgeSideEffect.NavigateToSoftWarning
                    )
                } catch (t: Throwable) {
                    reduce { state.copy(isSaving = false) }
                    postSideEffect(Q3AgeSideEffect.ShowError(t.message ?: "Error"))
                }
            }
        }
    }
}
