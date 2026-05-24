package org.betech.fitnes.domain.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class QuestionKind {
    @SerialName("single_choice") SINGLE_CHOICE,
    @SerialName("multi_choice") MULTI_CHOICE,
    @SerialName("wheel_numeric") WHEEL_NUMERIC,
    @SerialName("stepper") STEPPER,
    @SerialName("segmented") SEGMENTED,
    @SerialName("slider") SLIDER,
    @SerialName("date") DATE,
    @SerialName("text") TEXT,
    @SerialName("boolean") BOOLEAN,
    @SerialName("height_weight") HEIGHT_WEIGHT,
    @SerialName("days_session") DAYS_SESSION
}

@Serializable
data class QuestionOption(
    val value: String,
    /** Display key — UI layer resolves to localized string. */
    val labelKey: String,
    val iconKey: String? = null
)

@Serializable
data class QuestionValidation(
    val minInt: Int? = null,
    val maxInt: Int? = null,
    val minDouble: Double? = null,
    val maxDouble: Double? = null,
    val maxSelections: Int? = null,
    val errorCode: String? = null
)

/** A renderable onboarding question — registry-driven so screens stay thin. */
@Serializable
data class OnboardingQuestion(
    val id: String,
    val step: OnboardingStep,
    val kind: QuestionKind,
    /** Layer per §3 — "L1" required, "L2" progressive, "L5" safety/medical. */
    val layer: String,
    val titleKey: String,
    val subContextKey: String,
    val skippable: Boolean,
    val options: ImmutableList<QuestionOption> = persistentListOf(),
    val validation: QuestionValidation? = null,
    val analyticsId: String
)
