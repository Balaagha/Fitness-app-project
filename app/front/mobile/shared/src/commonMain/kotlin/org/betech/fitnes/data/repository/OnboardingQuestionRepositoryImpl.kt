package org.betech.fitnes.data.repository

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.betech.fitnes.data.dto.OnboardingQuestionDto
import org.betech.fitnes.data.mapper.toDomain
import org.betech.fitnes.data.source.remote.RemoteSource
import org.betech.fitnes.domain.model.OnboardingQuestion
import org.betech.fitnes.domain.repository.OnboardingQuestionRepository

class OnboardingQuestionRepositoryImpl(
    private val remote: RemoteSource,
    private val json: Json
) : OnboardingQuestionRepository {

    override suspend fun getQuestions(): ImmutableList<OnboardingQuestion> {
        val raw = remote.fetchOnboardingQuestionsJson()
        val dtoList = json.decodeFromString(
            ListSerializer(OnboardingQuestionDto.serializer()),
            raw
        )
        return dtoList.map { it.toDomain() }.toImmutableList()
    }
}
