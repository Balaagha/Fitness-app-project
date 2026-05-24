package org.betech.fitnes.domain.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

/**
 * UserProfile — mirrors `public.user_profiles` (prd-auth-data-model §1.3)
 * plus the 20 L2 progressive fields from `docs/project-context.md §3`.
 *
 * 7 required (L1) → first block · 20 optional (L2/L3) → nullable below.
 *
 * Pure domain — wire serialization lives in [UserProfileDto].
 */
data class UserProfile(
    val userId: String,
    val email: String?,

    // ── L1: 7 mandatory onboarding answers ──
    val goal: GoalType,
    val sex: Sex,
    val age: Int,
    val heightCm: Int,
    val weightKg: Double,
    val experienceLevel: ExperienceLevel,
    val context: TrainingContext,
    val weeklyDays: Int,            // 2..7
    val sessionDurationMin: Int,    // 15|30|45|60

    // ── Resolved ──
    val persona: Persona,

    // ── Privacy / compliance ──
    val aiDisclosureAcceptedAt: Instant,
    val parentalConsentAt: Instant? = null,
    val pregnancyPostpartum: PregnancyPostpartumStatus = PregnancyPostpartumStatus.NONE,
    val languageAtSignup: LanguageCode = LanguageCode.AZ,

    // ── L2/L3 optional (20 fields per project-context §3) ──
    val activityLevelDaily: ActivityLevel? = null,
    val targetWeightKg: Double? = null,
    val targetDeadline: LocalDate? = null,
    val bodyFatVisualEstimate: Int? = null,            // 1..5 bucket
    val frameSize: String? = null,                     // small/medium/large
    val sleepHoursPerNight: Double? = null,
    val stressPss4Score: Int? = null,                  // 0..16
    val sedentaryHoursPerDay: Double? = null,
    val stepGoal: Int? = null,
    val injuryHistory: ImmutableList<String> = persistentListOf(),
    val movementRestrictions: ImmutableList<String> = persistentListOf(),
    val dietPreference: DietPreference? = null,
    val allergies: ImmutableList<String> = persistentListOf(),
    val foodIntolerances: ImmutableList<String> = persistentListOf(),
    val religiousDietary: String? = null,
    val ramazanActive: Boolean? = null,
    val equipmentInventory: EquipmentInventory = EquipmentInventory(persistentListOf(EquipmentItem.BODYWEIGHT)),
    val motivations: ImmutableList<String> = persistentListOf(),
    val notificationCadence: String? = null,
    val trainerVoicePreference: String? = null,        // safety-fit voice tone; NOT "trainer/coach"

    // ── Audit ──
    val createdAt: Instant,
    val updatedAt: Instant
)

/** Wraps equipment list so we can attach helpers + keep DI-friendly type. */
data class EquipmentInventory(
    val items: ImmutableList<EquipmentItem> = persistentListOf(EquipmentItem.BODYWEIGHT)
)
