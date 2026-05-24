package org.betech.fitnes.data.source.remote

import kotlinx.coroutines.flow.Flow

/**
 * Single backend boundary. JSON-string based so swapping in Supabase + Ktor
 * is a one-file change (mock impl → real impl). Repositories only know this.
 */
interface RemoteSource {
    suspend fun fetchProfileJson(): String?
    suspend fun saveProfileJson(json: String)
    fun observeProfileJson(): Flow<String?>

    suspend fun fetchOnboardingProgressJson(): String
    suspend fun saveOnboardingAnswer(stepId: String, answerJson: String)
    suspend fun advanceOnboardingTo(stepJson: String)
    suspend fun resetOnboarding()
    fun observeOnboardingProgressJson(): Flow<String>

    suspend fun fetchOnboardingQuestionsJson(): String

    suspend fun emitAnalytics(eventJson: String)

    suspend fun signIn(email: String, password: String): String
    suspend fun signUp(email: String, password: String): String
    suspend fun signInApple(idToken: String): String
    suspend fun signInGoogle(idToken: String): String
    suspend fun signOut()
    fun observeSessionJson(): Flow<String?>
}
