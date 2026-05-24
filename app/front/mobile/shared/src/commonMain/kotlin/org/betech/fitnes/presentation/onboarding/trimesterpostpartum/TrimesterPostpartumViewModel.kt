package org.betech.fitnes.presentation.onboarding.trimesterpostpartum

import androidx.lifecycle.ViewModel
import org.betech.fitnes.domain.model.AnalyticsEvent
import org.betech.fitnes.domain.model.OnboardingAnswer
import org.betech.fitnes.domain.repository.AnalyticsRepository
import org.betech.fitnes.domain.repository.OnboardingRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Trimester / Postpartum VM (Orbit MVI).
 *
 * Persists the period bucket under stepId="trimester_postpartum" as a
 * SingleChoice payload (`t1` / `t2` / `t3` / `postpartum`). Default
 * selection = T2 (most common entry case + safest tap-through).
 *
 * Invariant (CLAUDE.md): NO AnalyticsEvent.PlanGenerationStarted is ever
 * fired from this screen — pregnancy_postpartum=true is a hard-stop on
 * AI plan generation, so confirm always routes to the curated SafePlan.
 */
class TrimesterPostpartumViewModel(
    private val onboardingRepo: OnboardingRepository,
    private val analytics: AnalyticsRepository,
) : ViewModel(), ContainerHost<TrimesterPostpartumState, TrimesterPostpartumSideEffect> {

    override val container =
        container<TrimesterPostpartumState, TrimesterPostpartumSideEffect>(
            TrimesterPostpartumState()
        )

    fun onIntent(i: TrimesterPostpartumIntent) = intent {
        when (i) {
            is TrimesterPostpartumIntent.Select ->
                reduce { state.copy(selected = i.choice) }

            TrimesterPostpartumIntent.BackTapped ->
                postSideEffect(TrimesterPostpartumSideEffect.NavigateBack)

            TrimesterPostpartumIntent.Confirm -> {
                val choice = state.selected
                reduce { state.copy(isSaving = true) }
                try {
                    onboardingRepo.saveAnswer(
                        stepId = "trimester_postpartum",
                        answer = OnboardingAnswer.SingleChoice(
                            questionId = "trimester_postpartum",
                            value = choice.name.lowercase(),
                        ),
                    )
                    analytics.track(
                        AnalyticsEvent.QuestionAnswered(
                            questionId = "trimester_postpartum",
                            value = choice.name,
                        )
                    )
                    reduce { state.copy(isSaving = false) }
                    postSideEffect(TrimesterPostpartumSideEffect.NavigateToSafePlan)
                } catch (t: Throwable) {
                    reduce { state.copy(isSaving = false) }
                    postSideEffect(
                        TrimesterPostpartumSideEffect.ShowError(t.message ?: "Error")
                    )
                }
            }
        }
    }
}
