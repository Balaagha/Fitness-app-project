package org.betech.fitnes.presentation.onboarding.parentalbottomsheet

/**
 * State for Parental Privacy bottom-sheet (Pencil P5mDxB · "V7").
 *
 * Stateless for now — content is fully static markdown copy. Kept as a
 * data class so future deltas (e.g. fetched terms revision, scroll
 * progress for the footer hint) plug in without churning the VM contract.
 */
data class ParentalBottomSheetState(
    val unused: Boolean = false,
)
