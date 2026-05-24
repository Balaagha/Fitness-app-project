package org.betech.fitnes.presentation.onboarding.q1goal

sealed interface Q1GoalIntent {
    data class Select(val choice: GoalChoice) : Q1GoalIntent
    data object BackTapped : Q1GoalIntent
    data object Confirm : Q1GoalIntent
}
