package org.betech.fitnes.presentation.onboarding.q3agesoftwarning

sealed interface Q3AgeSoftWarningSideEffect {
    data object NavigateBack : Q3AgeSoftWarningSideEffect
    data object NavigateToQ4 : Q3AgeSoftWarningSideEffect
}
