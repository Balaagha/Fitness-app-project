package org.betech.fitnes.presentation.onboarding.q4heightweight

import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt
import org.betech.fitnes.domain.model.AnalyticsEvent
import org.betech.fitnes.domain.model.OnboardingAnswer
import org.betech.fitnes.domain.repository.AnalyticsRepository
import org.betech.fitnes.domain.repository.OnboardingRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Q4 · Height + Weight VM (Orbit MVI).
 *
 * Persistence: two [OnboardingAnswer.Numeric] records — `q4_height_cm` and
 * `q4_weight_kg`. Storage is always metric regardless of the user's chosen
 * display unit. Two analytics events fire (one per dimension).
 *
 * Conversion helpers live in [Companion] so they can be reused from the
 * value-input dialog (where the user may type ft/in or lb directly).
 */
class Q4HeightWeightViewModel(
    private val onboardingRepo: OnboardingRepository,
    private val analytics: AnalyticsRepository,
) : ViewModel(), ContainerHost<Q4HeightWeightState, Q4HeightWeightSideEffect> {

    override val container =
        container<Q4HeightWeightState, Q4HeightWeightSideEffect>(Q4HeightWeightState())

    fun onIntent(i: Q4HeightWeightIntent) = intent {
        when (i) {
            is Q4HeightWeightIntent.SetHeightCm -> reduce {
                state.copy(
                    heightCm = i.cm.coerceIn(
                        Q4HeightWeightState.MIN_HEIGHT_CM,
                        Q4HeightWeightState.MAX_HEIGHT_CM,
                    )
                )
            }

            is Q4HeightWeightIntent.SetWeightKg -> reduce {
                state.copy(
                    weightKg = i.kg.coerceIn(
                        Q4HeightWeightState.MIN_WEIGHT_KG,
                        Q4HeightWeightState.MAX_WEIGHT_KG,
                    )
                )
            }

            is Q4HeightWeightIntent.SetHeightUnit -> reduce { state.copy(heightUnit = i.unit) }
            is Q4HeightWeightIntent.SetWeightUnit -> reduce { state.copy(weightUnit = i.unit) }

            is Q4HeightWeightIntent.OpenDialog -> reduce { state.copy(openDialog = i.target) }
            Q4HeightWeightIntent.CloseDialog -> reduce { state.copy(openDialog = null) }

            Q4HeightWeightIntent.BackTapped ->
                postSideEffect(Q4HeightWeightSideEffect.NavigateBack)

            Q4HeightWeightIntent.Confirm -> {
                if (!state.isValid) return@intent
                reduce { state.copy(isSaving = true) }
                try {
                    onboardingRepo.saveAnswer(
                        stepId = "q4_height_cm",
                        answer = OnboardingAnswer.Numeric(
                            questionId = "q4_height_cm",
                            value = state.heightCm.toDouble(),
                        ),
                    )
                    onboardingRepo.saveAnswer(
                        stepId = "q4_weight_kg",
                        answer = OnboardingAnswer.Numeric(
                            questionId = "q4_weight_kg",
                            value = state.weightKg.toDouble(),
                        ),
                    )
                    analytics.track(
                        AnalyticsEvent.QuestionAnswered(
                            questionId = "q4_height_cm",
                            value = state.heightCm.toString(),
                        )
                    )
                    analytics.track(
                        AnalyticsEvent.QuestionAnswered(
                            questionId = "q4_weight_kg",
                            value = state.weightKg.toString(),
                        )
                    )
                    reduce { state.copy(isSaving = false) }
                    postSideEffect(Q4HeightWeightSideEffect.NavigateToQ5Experience)
                } catch (t: Throwable) {
                    reduce { state.copy(isSaving = false) }
                    postSideEffect(Q4HeightWeightSideEffect.ShowError(t.message ?: "Error"))
                }
            }
        }
    }

    companion object {
        // ── cm ↔ ft/in ─────────────────────────────────────────────────
        private const val CM_PER_INCH: Double = 2.54
        private const val INCHES_PER_FOOT: Int = 12

        /** Convert metric cm to (feet, inches). Inches are rounded; may equal 12 → carry into feet. */
        fun cmToFtIn(cm: Int): Pair<Int, Int> {
            val totalInches = (cm / CM_PER_INCH).roundToInt()
            var ft = totalInches / INCHES_PER_FOOT
            var inch = totalInches % INCHES_PER_FOOT
            if (inch == INCHES_PER_FOOT) { ft += 1; inch = 0 }
            return ft to inch
        }

        /** Convert (feet, inches) back to cm with rounding. */
        fun ftInToCm(ft: Int, inch: Int): Int =
            ((ft * INCHES_PER_FOOT + inch) * CM_PER_INCH).roundToInt()

        // ── kg ↔ lb ────────────────────────────────────────────────────
        private const val LB_PER_KG: Double = 2.2046226218

        fun kgToLb(kg: Int): Int = (kg * LB_PER_KG).roundToInt()

        fun lbToKg(lb: Int): Int = (lb / LB_PER_KG).roundToInt()
    }
}
