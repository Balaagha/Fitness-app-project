package org.betech.fitnes.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import org.betech.fitnes.data.dto.AuthSessionDto
import org.betech.fitnes.data.mapper.toDomain
import org.betech.fitnes.data.source.remote.RemoteSource
import org.betech.fitnes.domain.model.AuthSession
import org.betech.fitnes.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val remote: RemoteSource,
    private val json: Json
) : AuthRepository {

    private val session = MutableStateFlow<AuthSession?>(null)

    override suspend fun signInWithEmail(email: String, password: String): Result<AuthSession> =
        runCatching {
            val raw = remote.signIn(email, password)
            decodeAndPush(raw)
        }

    override suspend fun signUpWithEmail(email: String, password: String): Result<AuthSession> =
        runCatching {
            val raw = remote.signUp(email, password)
            decodeAndPush(raw)
        }

    override suspend fun signInWithApple(idToken: String): Result<AuthSession> =
        runCatching {
            val raw = remote.signInApple(idToken)
            decodeAndPush(raw)
        }

    override suspend fun signInWithGoogle(idToken: String): Result<AuthSession> =
        runCatching {
            val raw = remote.signInGoogle(idToken)
            decodeAndPush(raw)
        }

    override suspend fun signOut() {
        remote.signOut()
        session.value = null
    }

    override fun observeSession(): Flow<AuthSession?> = session.asStateFlow()

    private fun decodeAndPush(raw: String): AuthSession {
        val domain = json.decodeFromString<AuthSessionDto>(raw).toDomain()
        session.value = domain
        return domain
    }
}
