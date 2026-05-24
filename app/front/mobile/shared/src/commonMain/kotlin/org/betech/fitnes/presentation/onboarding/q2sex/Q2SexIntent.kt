package org.betech.fitnes.presentation.onboarding.q2sex

sealed interface Q2SexIntent {
    data class Select(val choice: SexChoice) : Q2SexIntent
    data object BackTapped : Q2SexIntent
    data object Confirm : Q2SexIntent
}
