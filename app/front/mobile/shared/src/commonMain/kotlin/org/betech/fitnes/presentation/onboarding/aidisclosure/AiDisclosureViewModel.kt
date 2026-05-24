package org.betech.fitnes.presentation.onboarding.aidisclosure

import androidx.lifecycle.ViewModel
import org.betech.fitnes.domain.model.AnalyticsEvent
import org.betech.fitnes.domain.repository.AnalyticsRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * AI Disclosure VM (Orbit MVI) — Pencil eQcvv.
 *
 * Apple 2025 review item: explicit AI-generation ack before the plan flow.
 *
 * Analytics:
 *  - [AnalyticsEvent.AiDisclosureShown]    — emitted on `Load` (Composable's
 *    LaunchedEffect fires this once per screen materialisation).
 *  - [AnalyticsEvent.AiDisclosureAccepted] — emitted on `ConfirmTapped`.
 */
class AiDisclosureViewModel(
    private val analytics: AnalyticsRepository,
) : ViewModel(), ContainerHost<AiDisclosureState, AiDisclosureSideEffect> {

    override val container =
        container<AiDisclosureState, AiDisclosureSideEffect>(AiDisclosureState())

    fun onIntent(i: AiDisclosureIntent) = intent {
        when (i) {
            AiDisclosureIntent.BackTapped ->
                postSideEffect(AiDisclosureSideEffect.NavigateBack)

            AiDisclosureIntent.ConfirmTapped -> {
                reduce { state.copy(isAcknowledging = true) }
                analytics.track(AnalyticsEvent.AiDisclosureAccepted)
                postSideEffect(AiDisclosureSideEffect.NavigateToAuthGate)
            }

            AiDisclosureIntent.LearnMoreTapped ->
                postSideEffect(AiDisclosureSideEffect.ShowLearnMoreToast)
        }
    }

    /** Composable calls this from LaunchedEffect once per materialisation. */
    fun onShown() = intent {
        analytics.track(AnalyticsEvent.AiDisclosureShown)
    }
}
