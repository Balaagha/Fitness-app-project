package org.betech.fitnes.presentation.onboarding.welcome

/**
 * 5 content variants for the Welcome screen — same surface, swapped copy + hero glyph.
 * The rotation is timer-driven (see WelcomeScreen LaunchedEffect); dev deep-links
 * can jump straight to a specific variant via `&devScreen=welcome[1..4]`.
 *
 * Variant 0 (WORKOUT_PLAN) and 1 (SOFT_CONTROL) share the same eyebrow chip index (0)
 * — SOFT_CONTROL is a soft-copy alternate of the workout pencil, not a new category.
 */
enum class WelcomeVariant {
    WORKOUT_PLAN,
    SOFT_CONTROL,
    FOOD,
    ENERGY,
    GOAL;

    /** Maps each variant to the currently-highlighted chip in the 4-chip row. */
    val chipIndex: Int
        get() = when (this) {
            WORKOUT_PLAN, SOFT_CONTROL -> 0
            FOOD -> 1
            ENERGY -> 2
            GOAL -> 3
        }
}

/**
 * Welcome screen UI state. `activeVariant` drives all variant-derived UI:
 * title, subtitle, hero glyph and the highlighted eyebrow chip.
 */
data class WelcomeState(
    val activeVariant: WelcomeVariant = WelcomeVariant.WORKOUT_PLAN,
)
