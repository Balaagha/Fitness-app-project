package org.betech.fitnes.presentation.onboarding.q7daysession

/**
 * Q7 · Day + Session screen state (Pencil H0uZ0e).
 *
 * Defaults — days=4, minutes=45 — are the sector-modal cadence and the
 * MVP "balanced" preset (project-context §3.2 onboarding contract).
 * Both selections always have a value, so Confirm is always valid.
 */
data class Q7DaySessionState(
    val daysPerWeek: Int = 4,
    val sessionMinutes: Int = 45,
    val isSaving: Boolean = false,
)

/** Options exposed by the design (Pencil H0uZ0e segmented rows). */
val Q7DaysOptions: List<Int> = listOf(3, 4, 5, 6, 7)
val Q7MinutesOptions: List<Int> = listOf(20, 30, 45, 60)
