package org.betech.fitnes.data.mapper

import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.betech.fitnes.data.dto.UserProfileDto
import org.betech.fitnes.domain.model.EquipmentInventory
import org.betech.fitnes.domain.model.EquipmentItem
import org.betech.fitnes.domain.model.Persona
import org.betech.fitnes.domain.model.PersonaResolver
import org.betech.fitnes.domain.model.UserProfile

private fun parseInstant(raw: String): Instant =
    runCatching { Instant.parse(raw) }.getOrDefault(Instant.fromEpochMilliseconds(0))

private fun parseDate(raw: String?): LocalDate? =
    raw?.let { runCatching { LocalDate.parse(it) }.getOrNull() }

fun UserProfileDto.toDomain(): UserProfile {
    val persona: Persona = PersonaResolver.resolve(context, gender, goal)
    return UserProfile(
        userId = user_id,
        email = email,
        goal = goal,
        sex = gender,
        age = age,
        heightCm = height_cm,
        weightKg = weight_kg,
        experienceLevel = experience_level,
        context = context,
        weeklyDays = weekly_days,
        sessionDurationMin = session_duration_min,
        persona = persona,
        aiDisclosureAcceptedAt = parseInstant(ai_disclosure_accepted_at),
        parentalConsentAt = parental_consent_at?.let { parseInstant(it) },
        pregnancyPostpartum = pregnancy_postpartum,
        languageAtSignup = language_at_signup,
        activityLevelDaily = activity_level_daily,
        targetWeightKg = target_weight_kg,
        targetDeadline = parseDate(target_deadline),
        bodyFatVisualEstimate = body_fat_visual_estimate,
        frameSize = frame_size,
        sleepHoursPerNight = sleep_h_per_night,
        stressPss4Score = stress_pss4_score,
        sedentaryHoursPerDay = sedentary_hours_per_day,
        stepGoal = step_goal,
        injuryHistory = injury_history.toImmutableList(),
        movementRestrictions = movement_restrictions.toImmutableList(),
        dietPreference = diet_preference,
        allergies = allergies.toImmutableList(),
        foodIntolerances = food_intolerances.toImmutableList(),
        religiousDietary = religious_dietary,
        ramazanActive = ramazan_active,
        equipmentInventory = EquipmentInventory(
            equipment_inventory.ifEmpty { listOf(EquipmentItem.BODYWEIGHT) }.toImmutableList()
        ),
        motivations = motivations.toImmutableList(),
        notificationCadence = notification_cadence,
        trainerVoicePreference = trainer_voice_preference,
        createdAt = parseInstant(created_at),
        updatedAt = parseInstant(updated_at)
    )
}

fun UserProfile.toDto(): UserProfileDto = UserProfileDto(
    user_id = userId,
    email = email,
    goal = goal,
    gender = sex,
    age = age,
    height_cm = heightCm,
    weight_kg = weightKg,
    experience_level = experienceLevel,
    context = context,
    weekly_days = weeklyDays,
    session_duration_min = sessionDurationMin,
    persona_cell = persona.cellId,
    ai_disclosure_accepted_at = aiDisclosureAcceptedAt.toString(),
    parental_consent_at = parentalConsentAt?.toString(),
    pregnancy_postpartum = pregnancyPostpartum,
    language_at_signup = languageAtSignup,
    activity_level_daily = activityLevelDaily,
    target_weight_kg = targetWeightKg,
    target_deadline = targetDeadline?.toString(),
    body_fat_visual_estimate = bodyFatVisualEstimate,
    frame_size = frameSize,
    sleep_h_per_night = sleepHoursPerNight,
    stress_pss4_score = stressPss4Score,
    sedentary_hours_per_day = sedentaryHoursPerDay,
    step_goal = stepGoal,
    injury_history = injuryHistory.toList(),
    movement_restrictions = movementRestrictions.toList(),
    diet_preference = dietPreference,
    allergies = allergies.toList(),
    food_intolerances = foodIntolerances.toList(),
    religious_dietary = religiousDietary,
    ramazan_active = ramazanActive,
    equipment_inventory = equipmentInventory.items.toList(),
    motivations = motivations.toList(),
    notification_cadence = notificationCadence,
    trainer_voice_preference = trainerVoicePreference,
    created_at = createdAt.toString(),
    updated_at = updatedAt.toString()
)
