package org.betech.fitnes.presentation.onboarding.parentalbottomsheet

sealed interface ParentalBottomSheetIntent {
    data object CloseTapped : ParentalBottomSheetIntent
    data object ScrimTapped : ParentalBottomSheetIntent
    data object PrimaryTapped : ParentalBottomSheetIntent
}
