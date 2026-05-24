package org.betech.fitnes.presentation.onboarding.q6context

sealed interface Q6ContextIntent {
    data class Select(val choice: ContextChoice) : Q6ContextIntent
    data object BackTapped : Q6ContextIntent
    data object Confirm : Q6ContextIntent
}
