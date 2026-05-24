package org.betech.fitnes.presentation.onboarding.deleteacc2

import org.betech.fitnes.localization.deleteConfirmKeyword

/**
 * Delete Account · Step 2 (Pencil GauGs) — typed-confirmation gate state.
 *
 * The destructive CTA is gated by [matches]: input must equal the
 * locale-invariant [deleteConfirmKeyword] ("SİL") exactly, case-sensitive,
 * including the Azerbaijani uppercase dotted-İ. No trim — leading/trailing
 * whitespace is intentionally significant so accidental keyboard expansion
 * (period+space) does NOT satisfy the gate.
 *
 * [isDeleting] flips while the (mock) cascade runs to disable the button and
 * prevent double-tap during the 800ms simulated network call.
 */
data class DeleteAcc2State(
    val input: String = "",
    val isDeleting: Boolean = false,
) {
    val matches: Boolean get() = input == deleteConfirmKeyword
}
