package org.betech.fitnes.presentation.onboarding.parentalbottomsheet

sealed interface ParentalBottomSheetSideEffect {
    /** Close the sheet (and underlying scrim) — voyager pop. */
    data object Dismiss : ParentalBottomSheetSideEffect
}
