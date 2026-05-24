package org.betech.fitnes.presentation.onboarding.q3agesoftwarning

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/** V5 · Q3 Age Soft Warning VM (Pencil iNSs8). Visual-only snapshot — no repo deps. */
class Q3AgeSoftWarningViewModel :
    ViewModel(), ContainerHost<Q3AgeSoftWarningState, Q3AgeSoftWarningSideEffect> {

    override val container =
        container<Q3AgeSoftWarningState, Q3AgeSoftWarningSideEffect>(Q3AgeSoftWarningState())

    fun onIntent(i: Q3AgeSoftWarningIntent) = intent {
        when (i) {
            is Q3AgeSoftWarningIntent.AgeChanged -> reduce { state.copy(age = i.v.coerceIn(13, 90)) }
            Q3AgeSoftWarningIntent.BackTapped ->
                postSideEffect(Q3AgeSoftWarningSideEffect.NavigateBack)
            Q3AgeSoftWarningIntent.ContinueTapped ->
                postSideEffect(Q3AgeSoftWarningSideEffect.NavigateToQ4)
        }
    }
}
