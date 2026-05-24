package org.betech.fitnes.presentation.onboarding.resetlinkexpired

sealed interface ResetLinkExpiredIntent {
    data object RequestNewTapped : ResetLinkExpiredIntent
    data object BackTapped : ResetLinkExpiredIntent
}
