package org.betech.fitnes.data.dto

import kotlinx.serialization.Serializable
import org.betech.fitnes.domain.model.OnboardingStep
import org.betech.fitnes.domain.model.QuestionKind
import org.betech.fitnes.domain.model.QuestionOption
import org.betech.fitnes.domain.model.QuestionValidation

@Serializable
data class OnboardingQuestionDto(
    val id: String,
    val step: OnboardingStep,
    val kind: QuestionKind,
    val layer: String,
    val titleKey: String,
    val subContextKey: String,
    val skippable: Boolean,
    val options: List<QuestionOption> = emptyList(),
    val validation: QuestionValidation? = null,
    val analyticsId: String
)
