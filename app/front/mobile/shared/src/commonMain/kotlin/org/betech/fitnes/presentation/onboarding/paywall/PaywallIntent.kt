package org.betech.fitnes.presentation.onboarding.paywall

/**
 * User-driven intents for the Paywall screen (Pencil ij7jR).
 *
 * `Shown` is fired once from `LaunchedEffect(Unit)` so the VM can emit a
 * `PaywallShown` analytics event without the Composable having to depend on
 * the AnalyticsRepository directly.
 */
sealed interface PaywallIntent {
    data class SelectPlan(val plan: PaywallPlan) : PaywallIntent
    data object CloseTapped : PaywallIntent
    data object ContinueTapped : PaywallIntent
    data object RestoreTapped : PaywallIntent
    data object TermsTapped : PaywallIntent
    data object Shown : PaywallIntent
}
