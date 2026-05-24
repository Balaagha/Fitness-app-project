package org.betech.fitnes.presentation.onboarding.q4heightweight

/**
 * Display unit for height. Internal storage stays metric (cm).
 */
enum class HeightUnit { CM, FT }

/**
 * Display unit for weight. Internal storage stays metric (kg).
 */
enum class WeightUnit { KG, LB }

/**
 * Which value dialog (if any) is open.
 */
enum class DialogTarget { HEIGHT, WEIGHT }

/**
 * Q4 · Height + Weight — Orbit MVI state.
 *
 * Internal source of truth is **always metric** ([heightCm], [weightKg]).
 * Unit toggles only affect display & dialog parsing — see [Q4HeightWeightViewModel]
 * for cm↔ft/in and kg↔lb conversion helpers.
 *
 * Ranges match the BMR/TDEE plausibility band used by §5.5 (calorie math):
 *   height = 120..230 cm    weight = 30..250 kg
 */
data class Q4HeightWeightState(
    val heightCm: Int = 170,
    val weightKg: Int = 70,
    val heightUnit: HeightUnit = HeightUnit.CM,
    val weightUnit: WeightUnit = WeightUnit.KG,
    val openDialog: DialogTarget? = null,
    val isSaving: Boolean = false,
) {
    val isValid: Boolean
        get() = heightCm in MIN_HEIGHT_CM..MAX_HEIGHT_CM &&
            weightKg in MIN_WEIGHT_KG..MAX_WEIGHT_KG

    companion object {
        const val MIN_HEIGHT_CM: Int = 120
        const val MAX_HEIGHT_CM: Int = 230
        const val MIN_WEIGHT_KG: Int = 30
        const val MAX_WEIGHT_KG: Int = 250
    }
}
