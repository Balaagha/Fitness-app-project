package org.betech.fitnes.presentation.onboarding.safeplan

sealed interface SafePlanIntent {
    data object BackTapped : SafePlanIntent
    data object OpenTodayTapped : SafePlanIntent
    data object OpenInfoTapped : SafePlanIntent
    data class SelectDay(val index: Int) : SafePlanIntent
}
