package org.betech.fitnes.data.repository

import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.Instant
import org.betech.fitnes.data.dto.OnboardingProgressDto
import org.betech.fitnes.data.mapper.toDomain
import org.betech.fitnes.data.mapper.toDto
import org.betech.fitnes.data.source.remote.RemoteSource
import org.betech.fitnes.domain.model.OnboardingAnswer
import org.betech.fitnes.domain.model.OnboardingProgress
import org.betech.fitnes.domain.model.OnboardingStep
import org.betech.fitnes.domain.repository.OnboardingRepository

@OptIn(ExperimentalTime::class)
class OnboardingRepositoryImpl(
    private val remote: RemoteSource,
    private val json: Json
) : OnboardingRepository {

    private val state = MutableStateFlow(OnboardingProgress())

    override suspend fun getProgress(): OnboardingProgress {
        val raw = remote.fetchOnboardingProgressJson()
        val progress = json.decodeFromString<OnboardingProgressDto>(raw).toDomain()
        state.value = progress
        return progress
    }

    override suspend fun saveAnswer(stepId: String, answer: OnboardingAnswer) {
        val current = state.value
        val merged = current.copy(
            answers = (current.answers.toMutableMap()
                .apply { put(stepId, answer) })
                .toImmutableMap(),
            updatedAt = now(),
            startedAt = current.startedAt ?: now()
        )
        state.value = merged
        // Wire write — symmetric with future Supabase
        val answerJson = json.encodeToString(OnboardingAnswer.serializer(), answer)
        remote.saveOnboardingAnswer(stepId, answerJson)
    }

    override suspend fun advanceTo(step: OnboardingStep) {
        val current = state.value
        val completed = if (current.currentStep == step) {
            current.completedSteps
        } else {
            (current.completedSteps + current.currentStep)
                .distinct()
                .toImmutableList()
        }
        val next = current.copy(
            currentStep = step,
            completedSteps = completed,
            updatedAt = now()
        )
        state.value = next
        val stepJson = json.encodeToString(OnboardingStep.serializer(), step)
        remote.advanceOnboardingTo(stepJson)
    }

    override suspend fun reset() {
        remote.resetOnboarding()
        state.value = OnboardingProgress()
    }

    override fun observeProgress(): Flow<OnboardingProgress> = state.asStateFlow()

    /** Test helper / encode current progress for symmetric DTO test. */
    fun encodeCurrent(): String =
        json.encodeToString(OnboardingProgressDto.serializer(), state.value.toDto())

    private fun now(): Instant = Instant.parse(Clock.System.now().toString())
}
