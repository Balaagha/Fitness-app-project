package org.betech.fitnes.domain.repository

import kotlinx.collections.immutable.ImmutableList
import org.betech.fitnes.domain.model.OnboardingQuestion

interface OnboardingQuestionRepository {
    suspend fun getQuestions(): ImmutableList<OnboardingQuestion>
}
