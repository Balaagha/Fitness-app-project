package org.betech.fitnes.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** User goal — wire values match `prd-auth-data-model §1.3` CHECK. */
@Serializable
enum class GoalType {
    @SerialName("bulk") BULK,
    @SerialName("cut") CUT,
    @SerialName("general_fit") GENERAL_FIT
}

/** Binary per §1.3 CHECK; non-binary is L3 progressive (out of MVP schema). */
@Serializable
enum class Sex {
    @SerialName("male") MALE,
    @SerialName("female") FEMALE
}

/** Training context — equipment/location persona axis. */
@Serializable
enum class TrainingContext {
    @SerialName("serious_gym") SERIOUS_GYM,
    @SerialName("casual_gym") CASUAL_GYM,
    @SerialName("home_only") HOME_ONLY
}

@Serializable
enum class ExperienceLevel {
    @SerialName("beginner") BEGINNER,
    @SerialName("intermediate") INTERMEDIATE,
    @SerialName("advanced") ADVANCED,
    // ATHLETE — competitive/peak phase tier (Q5 Pencil i1Vu9). Maps to
    // the same persona bucket as ADVANCED in §6 for now; split when
    // peak-phase periodisation lands.
    @SerialName("athlete") ATHLETE
}

/** L2 optional — sedentary/active descriptor (separate from `TrainingContext`). */
@Serializable
enum class ActivityLevel {
    @SerialName("sedentary") SEDENTARY,
    @SerialName("light") LIGHT,
    @SerialName("moderate") MODERATE,
    @SerialName("active") ACTIVE,
    @SerialName("very_active") VERY_ACTIVE
}

@Serializable
enum class EquipmentItem {
    @SerialName("bodyweight") BODYWEIGHT,
    @SerialName("dumbbells") DUMBBELLS,
    @SerialName("barbell") BARBELL,
    @SerialName("resistance_bands") RESISTANCE_BANDS,
    @SerialName("pull_up_bar") PULL_UP_BAR,
    @SerialName("bench") BENCH,
    @SerialName("kettlebell") KETTLEBELL,
    @SerialName("cable_machine") CABLE_MACHINE,
    @SerialName("cardio_machine") CARDIO_MACHINE,
    @SerialName("foam_roller") FOAM_ROLLER,
    @SerialName("medicine_ball") MEDICINE_BALL,
    @SerialName("smith_machine") SMITH_MACHINE
}

@Serializable
enum class DietPreference {
    @SerialName("omnivore") OMNIVORE,
    @SerialName("vegetarian") VEGETARIAN,
    @SerialName("vegan") VEGAN,
    @SerialName("halal") HALAL,
    @SerialName("keto") KETO,
    @SerialName("other") OTHER
}

@Serializable
enum class PregnancyPostpartumStatus(val requiresMedicalDisclaimer: Boolean) {
    @SerialName("none") NONE(false),
    @SerialName("pregnant") PREGNANT(true),
    @SerialName("postpartum_lt_6m") POSTPARTUM_LT_6M(true),
    @SerialName("postpartum_gt_6m") POSTPARTUM_GT_6M(false)
}

@Serializable
enum class AuthMethod {
    @SerialName("email") EMAIL_PASSWORD,
    @SerialName("apple") APPLE,
    @SerialName("google") GOOGLE
}

/** Language code at sign-up — §1.3. */
@Serializable
enum class LanguageCode {
    @SerialName("az") AZ,
    @SerialName("ru") RU,
    @SerialName("en") EN
}
