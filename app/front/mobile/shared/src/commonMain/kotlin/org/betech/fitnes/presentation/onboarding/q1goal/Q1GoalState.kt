package org.betech.fitnes.presentation.onboarding.q1goal

/**
 * UI-only goal enum for the Q1 Goal screen — 4 options per design (Pencil S5QT23).
 * Maps to the 3-value schema-canonical `GoalType` at save time (see
 * [Q1GoalViewModel]). Decoupled from the domain enum so the UI can express
 * the design's 4 buckets without coupling persistence to UI granularity.
 *
 * Mapping:
 *   LOSE_FAT          → GoalType.CUT
 *   BUILD_MUSCLE      → GoalType.BULK
 *   GET_TONED         → GoalType.GENERAL_FIT
 *   INCREASE_STRENGTH → GoalType.BULK
 *     (closest schema bucket — TODO: split bulk into a strength sub-goal
 *      if a future PRD revision adds it.)
 */
enum class GoalChoice { LOSE_FAT, BUILD_MUSCLE, GET_TONED, INCREASE_STRENGTH }

data class Q1GoalState(
    val selected: GoalChoice? = null,
    val isSaving: Boolean = false,
)
