package org.betech.fitnes.presentation.onboarding.parentalnotice

/**
 * State for Parental Notice screen (Pencil XG54w · "07 · Parental Notice").
 *
 * @param consentChecked true once the user ticks the guardian consent box;
 *   primary CTA stays disabled until this flips true.
 * @param isSubmitting true while the continue action is in flight (debounce).
 */
data class ParentalNoticeState(
    val consentChecked: Boolean = false,
    val isSubmitting: Boolean = false,
)
