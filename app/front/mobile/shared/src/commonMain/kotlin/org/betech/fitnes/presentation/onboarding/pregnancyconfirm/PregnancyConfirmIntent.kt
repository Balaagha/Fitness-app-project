package org.betech.fitnes.presentation.onboarding.pregnancyconfirm

sealed interface PregnancyConfirmIntent {
    data object BackTapped : PregnancyConfirmIntent
    data object ConfirmTapped : PregnancyConfirmIntent
}
