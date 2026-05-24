package org.betech.fitnes.domain.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * One typed answer carried in `OnboardingProgress.answers[questionId]`.
 * Each variant has a `questionId` so analytics + persistence can roundtrip.
 */
@Serializable
sealed interface OnboardingAnswer {
    val questionId: String

    @Serializable @SerialName("single_choice")
    data class SingleChoice(
        override val questionId: String,
        val value: String
    ) : OnboardingAnswer

    @Serializable @SerialName("multi_choice")
    data class MultiChoice(
        override val questionId: String,
        val values: ImmutableList<String> = persistentListOf()
    ) : OnboardingAnswer

    @Serializable @SerialName("numeric")
    data class Numeric(
        override val questionId: String,
        val value: Double
    ) : OnboardingAnswer

    @Serializable @SerialName("slider")
    data class Slider(
        override val questionId: String,
        val value: Int
    ) : OnboardingAnswer

    @Serializable @SerialName("date_of_birth")
    data class DateOfBirth(
        override val questionId: String,
        val date: LocalDate
    ) : OnboardingAnswer

    @Serializable @SerialName("text_entry")
    data class TextEntry(
        override val questionId: String,
        val text: String
    ) : OnboardingAnswer

    @Serializable @SerialName("boolean")
    data class BooleanAnswer(
        override val questionId: String,
        val value: Boolean
    ) : OnboardingAnswer

    @Serializable @SerialName("height_weight")
    data class HeightWeight(
        override val questionId: String,
        val heightCm: Int,
        val weightKg: Double
    ) : OnboardingAnswer

    @Serializable @SerialName("days_session")
    data class DaysAndSession(
        override val questionId: String,
        val weeklyDays: Int,
        val sessionDurationMin: Int
    ) : OnboardingAnswer
}
