package org.betech.fitnes.presentation.onboarding.parentalnotice

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Parental Notice VM (Orbit MVI) — Pencil XG54w · "07 · Parental Notice".
 *
 * Under-18 guardian consent gate. The continue CTA is gated on
 * [ParentalNoticeState.consentChecked]; we never advance without it.
 *
 * Note: this screen is shown only when birth-year math classifies the
 * user as under 18. The check itself happens upstream (Q3 age). This
 * VM is intentionally analytics-free for now — wire `parental_consent_*`
 * events once the analytics contract for the under-18 cohort is signed.
 */
class ParentalNoticeViewModel :
    ViewModel(),
    ContainerHost<ParentalNoticeState, ParentalNoticeSideEffect> {

    override val container =
        container<ParentalNoticeState, ParentalNoticeSideEffect>(ParentalNoticeState())

    fun onIntent(i: ParentalNoticeIntent) = intent {
        when (i) {
            ParentalNoticeIntent.BackTapped ->
                postSideEffect(ParentalNoticeSideEffect.NavigateBack)

            is ParentalNoticeIntent.ConsentToggled ->
                reduce { state.copy(consentChecked = i.checked) }

            ParentalNoticeIntent.PrivacyChipTapped ->
                postSideEffect(ParentalNoticeSideEffect.OpenPrivacySheet)

            ParentalNoticeIntent.TermsChipTapped ->
                postSideEffect(ParentalNoticeSideEffect.OpenTermsSheet)

            ParentalNoticeIntent.ContinueTapped -> {
                if (!state.consentChecked || state.isSubmitting) return@intent
                reduce { state.copy(isSubmitting = true) }
                postSideEffect(ParentalNoticeSideEffect.NavigateForward)
            }
        }
    }
}
