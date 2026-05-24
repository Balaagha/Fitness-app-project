package org.betech.fitnes.presentation.onboarding.q7daysession

import androidx.lifecycle.ViewModel
import org.betech.fitnes.domain.model.AnalyticsEvent
import org.betech.fitnes.domain.model.OnboardingAnswer
import org.betech.fitnes.domain.repository.AnalyticsRepository
import org.betech.fitnes.domain.repository.OnboardingRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Q7 · Day + Session VM (Orbit MVI).
 *
 * The last of the 7 required onboarding questions. Persists TWO atomic
 * answers (`q7_weekly_days` + `q7_session_minutes`) — kept as separate
 * `Numeric` entries instead of the `DaysAndSession` composite so each
 * cell shows up as its own `question_answered` analytics event and
 * progressive profiling can backfill them independently later.
 *
 * On Confirm emits `OnboardingCompleted` — this is the canonical mark
 * that the required onboarding flow is done. PersonaCell is left blank
 * here; the canonical resolver runs on the ProfileSummary screen which
 * has access to all 7 answers via `OnboardingRepository.snapshot()`.
 */
class Q7DaySessionViewModel(
    private val onboardingRepo: OnboardingRepository,
    private val analytics: AnalyticsRepository,
) : ViewModel(), ContainerHost<Q7DaySessionState, Q7DaySessionSideEffect> {

    override val container =
        container<Q7DaySessionState, Q7DaySessionSideEffect>(Q7DaySessionState())

    fun onIntent(i: Q7DaySessionIntent) = intent {
        when (i) {
            is Q7DaySessionIntent.SetDays ->
                reduce { state.copy(daysPerWeek = i.days) }

            is Q7DaySessionIntent.SetMinutes ->
                reduce { state.copy(sessionMinutes = i.minutes) }

            Q7DaySessionIntent.BackTapped ->
                postSideEffect(Q7DaySessionSideEffect.NavigateBack)

            Q7DaySessionIntent.Confirm -> {
                reduce { state.copy(isSaving = true) }
                try {
                    val days = state.daysPerWeek
                    val minutes = state.sessionMinutes

                    onboardingRepo.saveAnswer(
                        stepId = "q7_weekly_days",
                        answer = OnboardingAnswer.Numeric(
                            questionId = "q7_weekly_days",
                            value = days.toDouble(),
                        ),
                    )
                    onboardingRepo.saveAnswer(
                        stepId = "q7_session_minutes",
                        answer = OnboardingAnswer.Numeric(
                            questionId = "q7_session_minutes",
                            value = minutes.toDouble(),
                        ),
                    )

                    analytics.track(
                        AnalyticsEvent.QuestionAnswered(
                            questionId = "q7_weekly_days",
                            value = days.toString(),
                        )
                    )
                    analytics.track(
                        AnalyticsEvent.QuestionAnswered(
                            questionId = "q7_session_minutes",
                            value = minutes.toString(),
                        )
                    )

                    // Last required question → mark onboarding complete.
                    // PersonaCell is resolved on the next screen (ProfileSummary)
                    // where the full answer snapshot is available.
                    analytics.track(
                        AnalyticsEvent.OnboardingCompleted(personaCell = "")
                    )

                    reduce { state.copy(isSaving = false) }
                    postSideEffect(Q7DaySessionSideEffect.NavigateToProfileSummary)
                } catch (t: Throwable) {
                    reduce { state.copy(isSaving = false) }
                    postSideEffect(Q7DaySessionSideEffect.ShowError(t.message ?: "Error"))
                }
            }
        }
    }
}
