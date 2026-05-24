package org.betech.fitnes.presentation.onboarding.deleteacc2

/**
 * One-shot navigation effects for Delete Account · Step 2 (Pencil GauGs).
 *
 * - [NavigateBack] handles both chevron and ghost "İmtina et" — pop without
 *   touching the deletion path.
 * - [NavigateToLanguageSelect] is the post-delete terminus: account is gone,
 *   session is invalidated, user lands back at locale picker (cold-start
 *   equivalent), mirroring the App Store-required permanent-delete flow.
 */
sealed interface DeleteAcc2SideEffect {
    data object NavigateBack : DeleteAcc2SideEffect
    data object NavigateToLanguageSelect : DeleteAcc2SideEffect
}
