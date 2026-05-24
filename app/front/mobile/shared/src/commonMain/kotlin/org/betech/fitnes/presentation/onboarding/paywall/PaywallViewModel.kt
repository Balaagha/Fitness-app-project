package org.betech.fitnes.presentation.onboarding.paywall

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import org.betech.fitnes.domain.model.AnalyticsEvent
import org.betech.fitnes.domain.repository.AnalyticsRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Paywall VM (Pencil ij7jR · "22 · Paywall") — Orbit MVI.
 *
 * Behavior contract:
 *  - `Shown`         → `PaywallShown(variant=selectedPlan)` analytics.
 *  - `SelectPlan`    → reduce-only.
 *  - `ContinueTapped`→ simulate 1000 ms purchase, emit
 *                      `PaywallConverted(plan)` then `NavigateToHome`.
 *  - `CloseTapped`   → emit `PaywallDismissed` and `NavigateToHome`
 *                      (skip premium = free tier).
 *  - Restore/Terms   → surface a toast side-effect; real wiring later.
 *
 * Re-entry guard: while `isPurchasing` is true, only `Shown` and
 * `CloseTapped` are honoured so a double-tap on Continue cannot stack.
 *
 * NOTE: no real RevenueCat / IAP call — that wiring lands in Phase 2.
 */
class PaywallViewModel(
    private val analytics: AnalyticsRepository,
) : ViewModel(), ContainerHost<PaywallState, PaywallSideEffect> {

    override val container =
        container<PaywallState, PaywallSideEffect>(PaywallState())

    fun onIntent(i: PaywallIntent) = intent {
        if (state.isPurchasing &&
            i !is PaywallIntent.Shown &&
            i !is PaywallIntent.CloseTapped
        ) return@intent

        when (i) {
            PaywallIntent.Shown -> {
                analytics.track(AnalyticsEvent.PaywallShown(variant = state.selected.name))
            }

            is PaywallIntent.SelectPlan ->
                reduce { state.copy(selected = i.plan) }

            PaywallIntent.ContinueTapped -> {
                reduce { state.copy(isPurchasing = true) }
                // Mock purchase round-trip. Real RevenueCat call lands in Phase 2.
                delay(1000)
                analytics.track(
                    AnalyticsEvent.PaywallConverted(plan = state.selected.name)
                )
                reduce { state.copy(isPurchasing = false) }
                postSideEffect(PaywallSideEffect.NavigateToHome)
            }

            PaywallIntent.CloseTapped -> {
                analytics.track(AnalyticsEvent.PaywallDismissed)
                postSideEffect(PaywallSideEffect.NavigateToHome)
            }

            PaywallIntent.RestoreTapped ->
                postSideEffect(PaywallSideEffect.ShowRestoreToast)

            PaywallIntent.TermsTapped ->
                postSideEffect(PaywallSideEffect.ShowTermsToast)
        }
    }
}
