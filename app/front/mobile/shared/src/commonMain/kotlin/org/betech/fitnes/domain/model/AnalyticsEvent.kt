package org.betech.fitnes.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Analytics events — names drawn from `prd-auth-onboarding-analytics-2026-05-22.md`.
 * Mock implementation only — no real telemetry in MVP data layer.
 */
@Serializable
sealed interface AnalyticsEvent {
    val name: String

    @Serializable @SerialName("onboarding_started")
    data object OnboardingStarted : AnalyticsEvent { override val name = "onboarding_started" }

    @Serializable @SerialName("question_answered")
    data class QuestionAnswered(
        val questionId: String,
        val value: String
    ) : AnalyticsEvent { override val name = "question_answered" }

    @Serializable @SerialName("onboarding_completed")
    data class OnboardingCompleted(val personaCell: String) : AnalyticsEvent {
        override val name = "onboarding_completed"
    }

    @Serializable @SerialName("ai_disclosure_shown")
    data object AiDisclosureShown : AnalyticsEvent { override val name = "ai_disclosure_shown" }

    @Serializable @SerialName("ai_disclosure_accepted")
    data object AiDisclosureAccepted : AnalyticsEvent { override val name = "ai_disclosure_accepted" }

    @Serializable @SerialName("paywall_shown")
    data class PaywallShown(val variant: String) : AnalyticsEvent {
        override val name = "paywall_shown"
    }

    @Serializable @SerialName("paywall_dismissed")
    data object PaywallDismissed : AnalyticsEvent { override val name = "paywall_dismissed" }

    @Serializable @SerialName("paywall_converted")
    data class PaywallConverted(val plan: String) : AnalyticsEvent {
        override val name = "paywall_converted"
    }

    @Serializable @SerialName("medical_hard_stop_shown")
    data class MedicalHardStopShown(val reason: String) : AnalyticsEvent {
        override val name = "medical_hard_stop_shown"
    }

    @Serializable @SerialName("pregnancy_nudge_shown")
    data class PregnancyNudgeShown(val ageBucket: String) : AnalyticsEvent {
        override val name = "pregnancy_nudge_shown"
    }

    @Serializable @SerialName("pregnancy_nudge_response")
    data class PregnancyNudgeResponse(val action: String) : AnalyticsEvent {
        override val name = "pregnancy_nudge_response"
    }

    @Serializable @SerialName("auth_signed_in")
    data class AuthSignedIn(val method: String) : AnalyticsEvent {
        override val name = "auth_signed_in"
    }
}
