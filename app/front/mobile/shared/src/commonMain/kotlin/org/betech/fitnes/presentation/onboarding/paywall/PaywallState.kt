package org.betech.fitnes.presentation.onboarding.paywall

/**
 * Plan options for Paywall (Pencil ij7jR).
 *
 * Doctrine: 2 options are mandatory (CLAUDE.md transparent-billing rule).
 * Single-option layout is forbidden.
 */
enum class PaywallPlan { TRIAL, ANNUAL }

/**
 * Paywall screen state.
 *
 * - [selected] defaults to [PaywallPlan.ANNUAL] — matches Pencil design and
 *   PRD §9 "yanvar push absolute" guidance toward annual plan.
 * - [isPurchasing] gates the CTA during the mock 1000 ms purchase delay.
 */
data class PaywallState(
    val selected: PaywallPlan = PaywallPlan.ANNUAL,
    val isPurchasing: Boolean = false,
)
