package org.betech.fitnes.presentation.onboarding.q2sex

/**
 * UI-only sex enum for the Q2 Sex screen — 3 options per design (Pencil ObxuP).
 * Maps to the binary schema-canonical [org.betech.fitnes.domain.model.Sex]
 * at save time (see [Q2SexViewModel]). PREFER_NOT_TO_SAY → null sex
 * (analytics captures the UI choice for replay; persistence skips sex write).
 *
 * Mapping:
 *   MALE              → Sex.MALE
 *   FEMALE            → Sex.FEMALE
 *   PREFER_NOT_TO_SAY → null   (neutral baseline applied downstream)
 */
enum class SexChoice { MALE, FEMALE, PREFER_NOT_TO_SAY }

data class Q2SexState(
    val selected: SexChoice? = null,
    val isSaving: Boolean = false,
)
