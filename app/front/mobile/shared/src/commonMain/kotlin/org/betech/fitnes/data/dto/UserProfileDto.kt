package org.betech.fitnes.data.dto

import kotlinx.serialization.Serializable
import org.betech.fitnes.domain.model.ActivityLevel
import org.betech.fitnes.domain.model.DietPreference
import org.betech.fitnes.domain.model.EquipmentItem
import org.betech.fitnes.domain.model.ExperienceLevel
import org.betech.fitnes.domain.model.GoalType
import org.betech.fitnes.domain.model.LanguageCode
import org.betech.fitnes.domain.model.PregnancyPostpartumStatus
import org.betech.fitnes.domain.model.Sex
import org.betech.fitnes.domain.model.TrainingContext

/**
 * Wire DTO for `public.user_profiles` row.
 * Field names match Postgres snake_case where they bind to columns.
 */
@Serializable
data class UserProfileDto(
    val user_id: String,
    val email: String? = null,
    val goal: GoalType,
    val gender: Sex,
    val age: Int,
    val height_cm: Int,
    val weight_kg: Double,
    val experience_level: ExperienceLevel,
    val context: TrainingContext,
    val weekly_days: Int,
    val session_duration_min: Int,
    val persona_cell: String,
    val ai_disclosure_accepted_at: String,
    val parental_consent_at: String? = null,
    val pregnancy_postpartum: PregnancyPostpartumStatus = PregnancyPostpartumStatus.NONE,
    val language_at_signup: LanguageCode = LanguageCode.AZ,
    val activity_level_daily: ActivityLevel? = null,
    val target_weight_kg: Double? = null,
    val target_deadline: String? = null,
    val body_fat_visual_estimate: Int? = null,
    val frame_size: String? = null,
    val sleep_h_per_night: Double? = null,
    val stress_pss4_score: Int? = null,
    val sedentary_hours_per_day: Double? = null,
    val step_goal: Int? = null,
    val injury_history: List<String> = emptyList(),
    val movement_restrictions: List<String> = emptyList(),
    val diet_preference: DietPreference? = null,
    val allergies: List<String> = emptyList(),
    val food_intolerances: List<String> = emptyList(),
    val religious_dietary: String? = null,
    val ramazan_active: Boolean? = null,
    val equipment_inventory: List<EquipmentItem> = listOf(EquipmentItem.BODYWEIGHT),
    val motivations: List<String> = emptyList(),
    val notification_cadence: String? = null,
    val trainer_voice_preference: String? = null,
    val created_at: String,
    val updated_at: String
)
