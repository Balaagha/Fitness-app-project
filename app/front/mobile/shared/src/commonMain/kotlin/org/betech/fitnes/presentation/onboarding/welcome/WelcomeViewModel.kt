package org.betech.fitnes.presentation.onboarding.welcome

import androidx.lifecycle.ViewModel
import org.betech.fitnes.domain.repository.AnalyticsRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class WelcomeViewModel(
    @Suppress("unused") private val analytics: AnalyticsRepository,
) : ViewModel(), ContainerHost<WelcomeState, WelcomeSideEffect> {

    override val container = container<WelcomeState, WelcomeSideEffect>(WelcomeState())

    fun onIntent(intent: WelcomeIntent) = intent {
        when (intent) {
            is WelcomeIntent.PageChanged ->
                reduce { state.copy(currentPage = intent.index) }
            WelcomeIntent.SkipTapped ->
                postSideEffect(WelcomeSideEffect.NavigateToQ1Goal)
            WelcomeIntent.StartTapped -> {
                val next = state.currentPage + 1
                if (next < WELCOME_PAGE_COUNT) {
                    postSideEffect(WelcomeSideEffect.AdvanceTo(next))
                } else {
                    postSideEffect(WelcomeSideEffect.NavigateToQ1Goal)
                }
            }
            WelcomeIntent.HaveAccountTapped ->
                postSideEffect(WelcomeSideEffect.NavigateToLogin)
            WelcomeIntent.ChangeLanguageTapped ->
                postSideEffect(WelcomeSideEffect.NavigateToLanguageSelect)
        }
    }
}
