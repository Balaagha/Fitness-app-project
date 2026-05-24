package org.betech.fitnes.domain.repository

import kotlinx.coroutines.flow.Flow
import org.betech.fitnes.domain.model.OnboardingAnswer
import org.betech.fitnes.domain.model.OnboardingProgress
import org.betech.fitnes.domain.model.OnboardingStep

interface OnboardingRepository {
    suspend fun getProgress(): OnboardingProgress
    suspend fun saveAnswer(stepId: String, answer: OnboardingAnswer)
    suspend fun advanceTo(step: OnboardingStep)
    suspend fun reset()
    fun observeProgress(): Flow<OnboardingProgress>
}
