package org.betech.fitnes.presentation.onboarding.parentalbottomsheet

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Parental Bottom-Sheet VM (Orbit MVI) — Pencil P5mDxB · "V7".
 *
 * All three intents (close X, scrim tap, primary "Anladım") collapse to
 * the same [ParentalBottomSheetSideEffect.Dismiss] — kept distinct so we
 * can wire per-source analytics later without re-shaping the contract.
 */
class ParentalBottomSheetViewModel :
    ViewModel(),
    ContainerHost<ParentalBottomSheetState, ParentalBottomSheetSideEffect> {

    override val container = container<ParentalBottomSheetState, ParentalBottomSheetSideEffect>(
        ParentalBottomSheetState()
    )

    fun onIntent(i: ParentalBottomSheetIntent) = intent {
        when (i) {
            ParentalBottomSheetIntent.CloseTapped,
            ParentalBottomSheetIntent.ScrimTapped,
            ParentalBottomSheetIntent.PrimaryTapped ->
                postSideEffect(ParentalBottomSheetSideEffect.Dismiss)
        }
    }
}
