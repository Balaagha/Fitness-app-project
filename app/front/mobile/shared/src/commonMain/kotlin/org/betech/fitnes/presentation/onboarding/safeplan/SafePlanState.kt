package org.betech.fitnes.presentation.onboarding.safeplan

/**
 * 23 · Safe 4-week Plan state (Pencil o0BUd).
 *
 * Holds only the locally-selected day index (0=Mon … 6=Sun). The plan itself
 * is a curated static template (NOT AI-generated). Default selection = 1
 * (Çərşənbə Axşamı / Tuesday) per spec.
 */
data class SafePlanState(
    val selectedDayIndex: Int = 1,
)
