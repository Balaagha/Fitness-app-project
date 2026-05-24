package org.betech.fitnes

import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.betech.fitnes.data.repository.AuthRepositoryImpl
import org.betech.fitnes.data.repository.OnboardingQuestionRepositoryImpl
import org.betech.fitnes.data.repository.OnboardingRepositoryImpl
import org.betech.fitnes.data.repository.UserProfileRepositoryImpl
import org.betech.fitnes.data.source.remote.mock.MockRemoteSource
import org.betech.fitnes.domain.model.OnboardingAnswer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class RepositoryRoundTripTest {

    private fun json() = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        classDiscriminator = "type"
    }

    @Test
    fun `user profile round-trip via mock`() = runTest {
        val j = json()
        val repo = UserProfileRepositoryImpl(MockRemoteSource(j), j)
        val first = repo.getProfile()
        assertNotNull(first)
        repo.saveProfile(first)
        val second = repo.getProfile()
        assertEquals(first.userId, second?.userId)
        assertEquals(first.persona.cellId, second?.persona?.cellId)
    }

    @Test
    fun `onboarding answer saves and observes`() = runTest {
        val j = json()
        val repo = OnboardingRepositoryImpl(MockRemoteSource(j), j)
        repo.getProgress()
        repo.saveAnswer("q1_goal", OnboardingAnswer.SingleChoice("q1_goal", "bulk"))
        val progress = repo.encodeCurrent()
        assertTrue(progress.contains("q1_goal"))
        assertTrue(progress.contains("bulk"))
    }

    @Test
    fun `onboarding questions decode 8 entries`() = runTest {
        val j = json()
        val repo = OnboardingQuestionRepositoryImpl(MockRemoteSource(j), j)
        val list = repo.getQuestions()
        assertEquals(8, list.size)
    }

    @Test
    fun `auth sign in returns session`() = runTest {
        val j = json()
        val repo = AuthRepositoryImpl(MockRemoteSource(j), j)
        val result = repo.signInWithEmail("demo@fitnes.az", "secret123")
        assertTrue(result.isSuccess)
        assertEquals("mock-user-001", result.getOrNull()?.userId)
    }
}
