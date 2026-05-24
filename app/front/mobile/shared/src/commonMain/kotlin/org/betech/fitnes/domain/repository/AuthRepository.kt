package org.betech.fitnes.domain.repository

import kotlinx.coroutines.flow.Flow
import org.betech.fitnes.domain.model.AuthSession

interface AuthRepository {
    suspend fun signInWithEmail(email: String, password: String): Result<AuthSession>
    suspend fun signUpWithEmail(email: String, password: String): Result<AuthSession>
    suspend fun signInWithApple(idToken: String): Result<AuthSession>
    suspend fun signInWithGoogle(idToken: String): Result<AuthSession>
    suspend fun signOut()
    fun observeSession(): Flow<AuthSession?>
}
