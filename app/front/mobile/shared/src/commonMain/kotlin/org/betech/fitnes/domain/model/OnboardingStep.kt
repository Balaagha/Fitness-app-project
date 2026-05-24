package org.betech.fitnes.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Onboarding step taxonomy — drives navigation.
 * Sealed (not enum) so steps can later carry payload (e.g., AgeGateBlocked reason).
 * `stepId` is the stable analytics / persistence key.
 */
@Serializable
sealed interface OnboardingStep {
    val stepId: String

    // Entry / gates
    @Serializable @SerialName("welcome")
    data object Welcome : OnboardingStep { override val stepId = "welcome" }

    @Serializable @SerialName("language_pick")
    data object LanguagePick : OnboardingStep { override val stepId = "language_pick" }

    @Serializable @SerialName("auth_gate")
    data object AuthGate : OnboardingStep { override val stepId = "auth_gate" }

    // L1 — 7 mandatory questions
    @Serializable @SerialName("goal_selection")
    data object GoalSelection : OnboardingStep { override val stepId = "goal_selection" }

    @Serializable @SerialName("sex")
    data object SexStep : OnboardingStep { override val stepId = "sex" }

    @Serializable @SerialName("age")
    data object Age : OnboardingStep { override val stepId = "age" }

    @Serializable @SerialName("height_weight")
    data object HeightWeight : OnboardingStep { override val stepId = "height_weight" }

    @Serializable @SerialName("experience")
    data object Experience : OnboardingStep { override val stepId = "experience" }

    @Serializable @SerialName("context")
    data object ContextStep : OnboardingStep { override val stepId = "context" }

    @Serializable @SerialName("days_session")
    data object DaysSession : OnboardingStep { override val stepId = "days_session" }

    // Safety gates
    @Serializable @SerialName("age_gate_parental")
    data object AgeGateParental : OnboardingStep { override val stepId = "age_gate_parental" }

    @Serializable @SerialName("age_gate_blocked")
    data object AgeGateBlocked : OnboardingStep { override val stepId = "age_gate_blocked" }

    @Serializable @SerialName("pregnancy_nudge")
    data object PregnancyPostpartum : OnboardingStep { override val stepId = "pregnancy_postpartum" }

    @Serializable @SerialName("pregnancy_hard_stop")
    data object PregnancyHardStop : OnboardingStep { override val stepId = "pregnancy_hard_stop" }

    // L2 optional groups
    @Serializable @SerialName("l2_body_metabolism")
    data object L2BodyMetabolism : OnboardingStep { override val stepId = "l2_body_metabolism" }

    @Serializable @SerialName("l2_activity_lifestyle")
    data object L2ActivityLifestyle : OnboardingStep { override val stepId = "l2_activity_lifestyle" }

    @Serializable @SerialName("l2_health_injuries")
    data object L2HealthInjuries : OnboardingStep { override val stepId = "l2_health_injuries" }

    @Serializable @SerialName("l2_food_preferences")
    data object L2FoodPreferences : OnboardingStep { override val stepId = "l2_food_preferences" }

    @Serializable @SerialName("l2_equipment")
    data object L2Equipment : OnboardingStep { override val stepId = "l2_equipment" }

    @Serializable @SerialName("l2_preferences")
    data object L2Preferences : OnboardingStep { override val stepId = "l2_preferences" }

    @Serializable @SerialName("ai_disclosure")
    data object AiDisclosure : OnboardingStep { override val stepId = "ai_disclosure" }

    @Serializable @SerialName("plan_preview")
    data object PlanPreview : OnboardingStep { override val stepId = "plan_preview" }

    @Serializable @SerialName("paywall_offer")
    data object PaywallOffer : OnboardingStep { override val stepId = "paywall_offer" }

    @Serializable @SerialName("complete")
    data object Complete : OnboardingStep { override val stepId = "complete" }
}
