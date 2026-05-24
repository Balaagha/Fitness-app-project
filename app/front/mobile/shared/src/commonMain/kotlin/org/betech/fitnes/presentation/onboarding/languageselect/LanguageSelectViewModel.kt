package org.betech.fitnes.presentation.onboarding.languageselect

import androidx.lifecycle.ViewModel
import org.betech.fitnes.domain.model.AnalyticsEvent
import org.betech.fitnes.domain.repository.AnalyticsRepository
import org.betech.fitnes.domain.repository.UserProfileRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Language select VM — Orbit MVI.
 *
 * Persists chosen locale via [UserProfileRepository.setPreferredLanguage] and emits a
 * `question_answered` analytics row keyed by `preferred_language` (treated as a pseudo
 * onboarding question for funnel parity).
 */
class LanguageSelectViewModel(
    private val userProfileRepo: UserProfileRepository,
    private val analytics: AnalyticsRepository,
) : ViewModel(), ContainerHost<LanguageSelectState, LanguageSelectSideEffect> {

    override val container =
        container<LanguageSelectState, LanguageSelectSideEffect>(LanguageSelectState())

    fun onIntent(intent: LanguageSelectIntent) = intent {
        when (intent) {
            is LanguageSelectIntent.Select -> reduce { state.copy(selected = intent.code) }
            is LanguageSelectIntent.Confirm -> {
                reduce { state.copy(isSaving = true) }
                try {
                    userProfileRepo.setPreferredLanguage(state.selected.toIso())
                    analytics.track(
                        AnalyticsEvent.QuestionAnswered(
                            questionId = "preferred_language",
                            value = state.selected.name,
                        )
                    )
                    postSideEffect(LanguageSelectSideEffect.NavigateToWelcome)
                } catch (t: Throwable) {
                    reduce { state.copy(isSaving = false) }
                    postSideEffect(LanguageSelectSideEffect.ShowError(t.message ?: "Error"))
                }
            }
        }
    }
}
